/*
 * Copyright 2015-2020 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
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

package net.dv8tion.jda.api.utils.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;

/**
 * Flags used to enable cache services for JDA.
 * <br>Check the flag descriptions to see which {@link net.dv8tion.jda.api.requests.GatewayIntent intents} are required to use them.
 */
public enum CacheFlag
{
    /**
     * Enables cache for {@link Member#getActivities()}
     *
     * <p>Requires {@link net.dv8tion.jda.api.requests.GatewayIntent#GUILD_PRESENCES GUILD_PRESENCES} intent to be enabled.
     */
    ACTIVITY(GatewayIntent.GUILD_PRESENCES),
    /**
     * Enables cache for {@link Member#getVoiceState()}
     * <br>This will always be cached for self member.
     *
     * <p>Requires {@link net.dv8tion.jda.api.requests.GatewayIntent#GUILD_VOICE_STATES GUILD_VOICE_STATES} intent to be enabled.
     */
    VOICE_STATE(GatewayIntent.GUILD_VOICE_STATES),
    /**
     * Enables cache for {@link Guild#getEmoteCache()}
     *
     * <p>Requires {@link net.dv8tion.jda.api.requests.GatewayIntent#GUILD_EMOJIS GUILD_EMOJIS} intent to be enabled.
     */
    EMOTE(GatewayIntent.GUILD_EMOJIS),
    /**
     * Enables cache for {@link Member#getOnlineStatus(net.dv8tion.jda.api.entities.ClientType) Member.getOnlineStatus(ClientType)}
     *
     * <p>Requires {@link net.dv8tion.jda.api.requests.GatewayIntent#GUILD_PRESENCES GUILD_PRESENCES} intent to be enabled.
     */
    CLIENT_STATUS(GatewayIntent.GUILD_PRESENCES),
    /**
     * Enables cache for {@link GuildChannel#getMemberPermissionOverrides()}
     */
    MEMBER_OVERRIDES(null),
    /** Enables cache for {@link JDA#getVoiceChannelCache()} */
    CHANNELS_VOICE(null),
    /** Enables cache for {@link JDA#getTextChannelCache()} */
    CHANNELS_TEXT(null),
    /** Enables cache for {@link JDA#getStoreChannelCache()} */
    CHANNELS_STORE(null),
    /** Enables cache for {@link JDA#getCategoryCache()} */
    CHANNELS_CATEGORY(null)
    ;
    private final GatewayIntent requiredIntent;

    CacheFlag(GatewayIntent requiredIntent)
    {
        this.requiredIntent = requiredIntent;
    }

    /**
     * The required {@link GatewayIntent} for this cache flag.
     *
     * @return The required intent, or null if no intents are required.
     */
    @Nullable
    public GatewayIntent getRequiredIntent()
    {
        return requiredIntent;
    }

    /**
     * Converts the provided {@link ChannelType ChannelTypes} to the respective ConfigFlags.
     *
     * @param  types
     *         The channel types
     *
     * @throws IllegalArgumentException
     *         If null is provided
     *
     * @return {@link EnumSet} of the cache flags
     */
    @Nonnull
    public static EnumSet<CacheFlag> fromChannels(@Nonnull ChannelType... types)
    {
        Checks.noneNull(types, "ChannelType");
        if (types.length == 0)
            return EnumSet.noneOf(CacheFlag.class);

        EnumSet<CacheFlag> enabled = EnumSet.noneOf(CacheFlag.class);
        for (ChannelType type : types)
        {
            switch (type)
            {
                case TEXT: enabled.add(CHANNELS_TEXT); break;
                case VOICE: enabled.add(CHANNELS_VOICE); break;
                case STORE: enabled.add(CHANNELS_STORE); break;
                case CATEGORY: enabled.add(CHANNELS_CATEGORY); break;
            }
        }
        return enabled;
    }

    /**
     * Converts the provided {@link ChannelType ChannelTypes} to the respective ConfigFlags.
     *
     * @param  types
     *         The channel types
     *
     * @throws IllegalArgumentException
     *         If null is provided
     *
     * @return {@link EnumSet} of the cache flags
     */
    @Nonnull
    public static EnumSet<CacheFlag> fromChannels(@Nonnull Collection<ChannelType> types)
    {
        Checks.noneNull(types, "ChannelType");
        return fromChannels(types.toArray(new ChannelType[0]));
    }

    /**
     * Shortcut for {@code fromChannels(ChannelType.values())}.
     *
     * @return All channel related cache flags as {@link EnumSet}
     */
    @Nonnull
    public static EnumSet<CacheFlag> channels()
    {
        return fromChannels(ChannelType.values());
    }
}
