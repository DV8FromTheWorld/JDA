/*
 *     Copyright 2015-2016 Austin Keener & Michael Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dv8tion.jda.core.entities;

import net.dv8tion.jda.core.requests.RestAction;

import java.util.Collection;
import java.util.List;

/**
 * Represents a Discord Text Channel. See {@link net.dv8tion.jda.core.entities.Channel Channel} and
 * {@link net.dv8tion.jda.core.entities.MessageChannel MessageChannel} for more information.<p>
 * Internal implementation of this class is available at
 * {@link net.dv8tion.jda.core.entities.impl.TextChannelImpl TextChannelImpl}.<br>
 * Note: Internal implementation should not be used directly.
 */
public interface TextChannel extends Channel, MessageChannel, Comparable<TextChannel>, IMentionable
{
    /**
     * The topic set for this TextChannel.
     * If no topic has been set, this returns null.
     *
     * @return
     *      Possibly-null String containing the topic of this TextChannel.
     */
    String getTopic();

    /**
     * Bulk deletes a list of messages. <b>This is not the same as calling {@link net.dv8tion.jda.core.entities.Message#deleteMessage()} in a loop.</b><br>
     * This is much more efficient, but it has a different ratelimit. You may call this once per second per Guild.
     * <p>
     * Must be at least 2 messages and not be more than 100 messages at a time.<br>
     * If you only have 1 message, use the {@link Message#deleteMessage()} method.<br>
     * <p>
     * You must have {@link net.dv8tion.jda.core.Permission#MESSAGE_MANAGE Permission.MESSAGE_MANAGE} in this channel to use
     * this function.
     * <p>
     * This method is best used when using {@link net.dv8tion.jda.core.MessageHistory MessageHistory} to delete a large amount
     * of messages. If you have a large amount of messages but only their message Ids, please use
     * {@link #deleteMessagesByIds(java.util.Collection)}
     *
     * @param messages
     *      The messages to delete.
     * @return
     *          {@link net.dv8tion.jda.core.requests.RestAction RestAction}
     * @throws IllegalArgumentException
     *      If the size of the list less than 2 or more than 100 messages.
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *      If this account does not have MANAGE_MESSAGES
     */
    RestAction<Void> deleteMessages(Collection<Message> messages);

    /**
     * Bulk deletes a list of messages. <b>This is not the same as calling {@link net.dv8tion.jda.core.entities.MessageChannel#deleteMessageById(String)} in a loop.</b> <br>
     * This is much more efficient, but it has a different ratelimit. You may call this once per second per Guild.
     * <p>
     * Must be at least 2 messages and not be more than 100 messages at a time.<br>
     * If you only have 1 message, use the {@link net.dv8tion.jda.core.entities.Message#deleteMessage()} method.<br>
     * <p>
     * You must have {@link net.dv8tion.jda.core.Permission#MESSAGE_MANAGE Permission.MESSAGE_MANAGE} in this channel to use
     * this function.
     * <p>
     * This method is best used when you have a large amount of messages but only their message Ids. If you are using
     * {@link net.dv8tion.jda.core.MessageHistory MessageHistory} or have {@link net.dv8tion.jda.core.entities.Message Message}
     * objects, it would be easier to use {@link #deleteMessages(java.util.Collection)}.
     *
     * @param messageIds
     *      The messages to delete.
     * @return
     *          {@link net.dv8tion.jda.core.requests.RestAction RestAction}
     * @throws java.lang.IllegalArgumentException
     *      If the size of the list less than 2 or more than 100 messages.
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *      If this account does not have MANAGE_MESSAGES
     */
    RestAction<Void> deleteMessagesByIds(Collection<String> messageIds);

    RestAction<List<Webhook>> getWebhooks();

    // RestAction<Webhook> createWebhook();

    RestAction<Void> deleteWebhookById(String id);

    boolean canTalk();

    boolean canTalk(Member member);
}
