/*
 * Copyright 2015-2019 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.internal.handle;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.set.TLongSet;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.PrivateChannelImpl;
import net.dv8tion.jda.internal.entities.UserImpl;
import net.dv8tion.jda.internal.managers.AudioManagerImpl;
import net.dv8tion.jda.internal.requests.WebSocketClient;
import net.dv8tion.jda.internal.utils.UnlockHook;
import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;
import net.dv8tion.jda.internal.utils.cache.SnowflakeCacheViewImpl;

public class GuildDeleteHandler extends SocketHandler
{
    public GuildDeleteHandler(JDAImpl api)
    {
        super(api);
    }

    @Override
    protected Long handleInternally(DataObject content)
    {
        final long id = content.getLong("id");
        GuildSetupController setupController = getJDA().getGuildSetupController();
        boolean wasInit = setupController.onDelete(id, content);
        if (wasInit || setupController.isUnavailable(id))
            return null;

        GuildImpl guild = (GuildImpl) getJDA().getGuildById(id);
        boolean unavailable = content.getBoolean("unavailable");
        if (guild == null)
        {
            //getJDA().getEventCache().cache(EventCache.Type.GUILD, id, () -> handle(responseNumber, allContent));
            WebSocketClient.LOG.debug("Received GUILD_DELETE for a Guild that is not currently cached. ID: {} unavailable: {}", id, unavailable);
            return null;
        }

        //If the event is attempting to mark the guild as unavailable, but it is already unavailable,
        // ignore the event
        if (!guild.isAvailable() && unavailable)
            return null;

        if (unavailable)
        {
            setupController.onUnavailable(id);
            getJDA().getGuildsView().remove(id);
            guild.setAvailable(false);
            getJDA().handleEvent(
                    new GuildUnavailableEvent(
                            getJDA(), responseNumber,
                            guild
                    ));
            return null;
        }

        //Remove everything from global cache
        // this prevents some race-conditions for getting audio managers from guilds
        SnowflakeCacheViewImpl<Guild> guildView = getJDA().getGuildsView();
        SnowflakeCacheViewImpl<StoreChannel> storeView = getJDA().getStoreChannelsView();
        SnowflakeCacheViewImpl<TextChannel> textView = getJDA().getTextChannelsView();
        SnowflakeCacheViewImpl<VoiceChannel> voiceView = getJDA().getVoiceChannelsView();
        SnowflakeCacheViewImpl<Category> categoryView = getJDA().getCategoriesView();
        guildView.remove(id);
        try (UnlockHook hook = storeView.writeLock())
        {
            guild.getStoreChannelCache()
                 .forEachUnordered(chan -> storeView.getMap().remove(chan.getIdLong()));
        }
        try (UnlockHook hook = textView.writeLock())
        {
            guild.getTextChannelCache()
                 .forEachUnordered(chan -> textView.getMap().remove(chan.getIdLong()));
        }
        try (UnlockHook hook = voiceView.writeLock())
        {
            guild.getVoiceChannelCache()
                 .forEachUnordered(chan -> voiceView.getMap().remove(chan.getIdLong()));
        }
        try (UnlockHook hook = categoryView.writeLock())
        {
            guild.getCategoryCache()
                 .forEachUnordered(chan -> categoryView.getMap().remove(chan.getIdLong()));
        }
        getJDA().getClient().removeAudioConnection(id);
        final AbstractCacheView<AudioManager> audioManagerView = getJDA().getAudioManagersView();
        try (UnlockHook hook = audioManagerView.writeLock())
        {
            final TLongObjectMap<AudioManager> audioManagerMap = audioManagerView.getMap();
            final AudioManagerImpl manager = (AudioManagerImpl) audioManagerMap.get(id);
            if (manager != null) // close existing audio connection if needed
            {
                MiscUtil.locked(manager.CONNECTION_LOCK, () ->
                {
                    if (manager.isConnected() || manager.isAttemptingToConnect())
                        manager.closeAudioConnection(ConnectionStatus.DISCONNECTED_REMOVED_FROM_GUILD);
                    else
                        audioManagerMap.remove(id);
                });
            }
        }

        //cleaning up all users that we do not share a guild with anymore
        // Anything left in memberIds will be removed from the main userMap
        //Use a new HashSet so that we don't actually modify the Member map so it doesn't affect Guild#getMembers for the leave event.
        TLongSet memberIds = guild.getMembersView().keySet(); // copies keys
        getJDA().getGuildCache().stream()
                .map(GuildImpl.class::cast)
                .forEach(g -> memberIds.removeAll(g.getMembersView().keySet()));
        // Remember, everything left in memberIds is removed from the userMap
        SnowflakeCacheViewImpl<User> userView = getJDA().getUsersView();
        try (UnlockHook hook = userView.writeLock())
        {
            long selfId = getJDA().getSelfUser().getIdLong();
            memberIds.forEach(memberId -> {
                if (memberId == selfId)
                    return true; // don't remove selfUser from cache
                UserImpl user = (UserImpl) userView.getMap().remove(memberId);
                if (user.hasPrivateChannel())
                {
                    PrivateChannelImpl priv = (PrivateChannelImpl) user.getPrivateChannel();
                    user.setFake(true);
                    priv.setFake(true);
                    getJDA().getFakeUserMap().put(user.getIdLong(), user);
                    getJDA().getFakePrivateChannelMap().put(priv.getIdLong(), priv);
                }
                getJDA().getEventCache().clear(EventCache.Type.USER, memberId);
                return true;
            });
        }

        getJDA().handleEvent(
            new GuildLeaveEvent(
                getJDA(), responseNumber,
                guild));
        getJDA().getEventCache().clear(EventCache.Type.GUILD, id);
        return null;
    }
}
