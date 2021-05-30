/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
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

package net.dv8tion.jda.api.events.guild.invite;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Indicates that an {@link Invite} was created or deleted in a {@link Guild}.
 * <br>Every GuildInviteEvent is derived from this event and can be casted.
 *
 * <p>Can be used to detect any GuildInviteEvent.
 *
 * <h2>Requirements</h2>
 *
 * <p>These events require the {@link net.dv8tion.jda.api.requests.GatewayIntent#GUILD_INVITES GUILD_INVITES} intent to be enabled.
 * <br>These events will only fire for invite events that occur in channels where you can {@link net.dv8tion.jda.api.Permission#MANAGE_CHANNEL MANAGE_CHANNEL}.
 */
public class GenericGuildInviteEvent extends GenericGuildEvent
{
    private final String code;
    private final GuildChannel channel;

    public GenericGuildInviteEvent(@NotNull JDA api, long responseNumber, @NotNull String code, @NotNull GuildChannel channel)
    {
        super(api, responseNumber, channel.getGuild());
        this.code = code;
        this.channel = channel;
    }

    /**
     * The invite code.
     * <br>This can be converted to a url with {@code discord.gg/<code>}.
     *
     * @return The invite code
     */
    @NotNull
    public String getCode()
    {
        return code;
    }

    /**
     * The invite url.
     * <br>This uses the {@code https://discord.gg/<code>} format.
     *
     * @return The invite url
     */
    @NotNull
    public String getUrl()
    {
        return "https://discord.gg/" + code;
    }

    /**
     * The {@link GuildChannel} this invite points to.
     *
     * @return {@link GuildChannel}
     */
    @NotNull
    public GuildChannel getChannel()
    {
        return channel;
    }

    /**
     * The {@link ChannelType} for of the {@link #getChannel() channel} this invite points to.
     *
     * @return {@link ChannelType}
     */
    @NotNull
    public ChannelType getChannelType()
    {
        return channel.getType();
    }

    /**
     * The {@link TextChannel} this invite points to.
     *
     * @throws IllegalStateException
     *         If this did not happen in a channel of type {@link ChannelType#TEXT ChannelType.TEXT}
     *
     * @return {@link TextChannel}
     *
     * @see    #getChannel()
     * @see    #getChannelType()
     */
    @NotNull
    public TextChannel getTextChannel()
    {
        if (getChannelType() != ChannelType.TEXT)
            throw new IllegalStateException("The channel is not of type TEXT");
        return (TextChannel) getChannel();
    }

    /**
     * The {@link VoiceChannel} this invite points to.
     *
     * @throws IllegalStateException
     *         If this did not happen in a channel of type {@link ChannelType#VOICE ChannelType.VOICE}
     *
     * @return {@link VoiceChannel}
     *
     * @see    #getChannel()
     * @see    #getChannelType()
     */
    @NotNull
    public VoiceChannel getVoiceChannel()
    {
        if (getChannelType() != ChannelType.VOICE)
            throw new IllegalStateException("The channel is not of type VOICE");
        return (VoiceChannel) getChannel();
    }

    /**
     * The {@link StoreChannel} this invite points to.
     *
     * @throws IllegalStateException
     *         If this did not happen in a channel of type {@link ChannelType#STORE ChannelType.STORE}
     *
     * @return {@link StoreChannel}
     *
     * @see    #getChannel()
     * @see    #getChannelType()
     */
    @NotNull
    public StoreChannel getStoreChannel()
    {
        if (getChannelType() != ChannelType.STORE)
            throw new IllegalStateException("The channel is not of type STORE");
        return (StoreChannel) getChannel();
    }

    /**
     * The {@link Category} this invite points to.
     *
     * @throws IllegalStateException
     *         If this did not happen in a channel of type {@link ChannelType#CATEGORY ChannelType.CATEGORY}
     *
     * @return {@link Category}
     *
     * @see    #getChannel()
     * @see    #getChannelType()
     */
    @NotNull
    public Category getCategory()
    {
        if (getChannelType() != ChannelType.CATEGORY)
            throw new IllegalStateException("The channel is not of type CATEGORY");
        return (Category) getChannel();
    }
}
