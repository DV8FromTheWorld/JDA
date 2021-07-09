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
package net.dv8tion.jda.api.entities;

import javax.annotation.Nullable;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;

import javax.annotation.Nonnull;

/**
 * An object representing a reference in a Discord message.
 * @see Message#getMessageReference()
 */
public class MessageReference
{
    private final long messageId;
    private final long channelId;
    private final long guildId;

    private final MessageChannel channel;
    private final Guild guild;
    private final Message referencedMessage;
    private final JDA api;

    public MessageReference(long messageId, long channelId, long guildId, @Nullable Message referencedMessage, JDA api)
    {
        this.messageId = messageId;
        this.channelId = channelId;
        this.guildId = guildId;
        this.referencedMessage = referencedMessage;

        TextChannel tc = api.getTextChannelById(channelId);

        if (tc == null)
        {
            this.channel = api.getPrivateChannelById(channelId);
            this.guild = null;
        }
        else
        {
            this.channel = tc;
            this.guild = api.getGuildById(guildId);
        }

        this.api = api;
    }

    /**
     * Retrieves the referenced message for this message.
     *
     * <p>The following {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} are possible:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The request was attempted after the account lost access to the {@link net.dv8tion.jda.api.entities.Guild Guild}
     *         typically due to being kicked or removed, or after {@link net.dv8tion.jda.api.Permission#MESSAGE_READ Permission.MESSAGE_READ}
     *         was revoked in the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The request was attempted after the account lost {@link net.dv8tion.jda.api.Permission#MESSAGE_HISTORY Permission.MESSAGE_HISTORY}
     *         in the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message has already been deleted.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>The request was attempted after the channel was deleted.</li>
     * </ul>
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *          If this reference refers to a {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} and the logged in account does not have
     *         <ul>
     *             <li>{@link net.dv8tion.jda.api.Permission#MESSAGE_READ Permission.MESSAGE_READ}</li>
     *             <li>{@link net.dv8tion.jda.api.Permission#MESSAGE_HISTORY Permission.MESSAGE_HISTORY}</li>
     *         </ul>
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@link net.dv8tion.jda.api.entities.Message}
     */
    @Nonnull
    public RestAction<Message> resolve()
    {
        checkPermission(Permission.MESSAGE_READ);
        checkPermission(Permission.MESSAGE_HISTORY);

        Route.CompiledRoute route = Route.Messages.GET_MESSAGE.compile(getChannelId(), getMessageId());
        JDAImpl jda = (JDAImpl) getJDA();

        return new RestActionImpl<>(jda, route,
                (response, request) -> jda.getEntityBuilder().createMessage(response.getObject(), getChannel(), false));
    }

    /**
     * Referenced message.
     *
     * <p>This will have different meaning depending on the {@link Message#getType() type} of message.
     * Usually, this is a {@link MessageType#INLINE_REPLY INLINE_REPLY} reference.
     * This can be null even if the type is {@link MessageType#INLINE_REPLY INLINE_REPLY}, when the message it references doesn't exist or discord wasn't able to resolve it in time.
     *
     * @return The referenced message, or null
     */
    @Nullable
    public Message getMessage()
    {
        return referencedMessage;
    }

    /**
     * The channel from which this message originates.
     * <br>Messages from other guilds can be referenced, in which case JDA may not have the channel cached.
     *
     * @return The origin channel for this message reference.
     */
    @Nonnull
    public MessageChannel getChannel()
    {
        return channel;
    }


    /**
     * The guild for this reference.
     * <br>
     * This will be null if the message did not come from a guild, or JDA did not have the guild cached
     *
     * @return The guild, or null.
     */
    @Nullable
    public Guild getGuild()
    {
        return guild;
    }

    /**
     * Returns the message id for this reference.
     *
     * @return The message id
     */
    public long getMessageIdLong()
    {
        return messageId;
    }

    /**
     * Returns the channel id for this reference.
     *
     * @return The channel id
     */
    public long getChannelIdLong()
    {
        return channelId;
    }

    /**
     * Returns the guild id for this reference.
     *
     * @return The guild id
     */
    public long getGuildIdLong()
    {
        return guildId;
    }

    /**
     * Returns the message id for this reference.
     *
     * @return The message id
     */
    @Nonnull
    public String getMessageId()
    {
        return Long.toUnsignedString(getMessageIdLong());
    }

    /**
     * Returns the channel id for this reference.
     *
     * @return The channel id
     */
    @Nonnull
    public String getChannelId()
    {
        return Long.toUnsignedString(getChannelIdLong());
    }

    /**
     * Returns the guild id for this reference.
     *
     * @return The guild id
     */
    @Nonnull
    public String getGuildId()
    {
        return Long.toUnsignedString(getGuildIdLong());
    }

    /**
     * Returns the JDA instance related to this message reference.
     *
     * @return The corresponding JDA instance
     */
    @Nonnull
    public JDA getJDA()
    {
        return api;
    }

    private void checkPermission(Permission permission)
    {
        if (guild == null)
        {
            return;
        }

        Member selfMember = guild.getSelfMember();
        if (!selfMember.hasPermission(permission))
            throw new InsufficientPermissionException((GuildChannel) channel, permission);
    }
}
