/**
 *    Copyright 2015 Austin Keener & Michael Ritter
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
package net.dv8tion.jda.entities;

import net.dv8tion.jda.JDA;

import java.time.OffsetDateTime;
import java.util.List;

public interface Message
{
    /**
     * The Id of this Message
     *
     * @return String Id
     */
    String getId();

    /**
     * A immutable list of all mentioned users. if noone was mentioned, this list is empty
     * In {@link net.dv8tion.jda.entities.PrivateChannel PrivateChannel's}, this always returns an empty List
     *
     * @return list of mentioned users
     */
    List<User> getMentionedUsers();

    /**
     * Is this Message mentioning everyone using @everyone?
     * In {@link net.dv8tion.jda.entities.PrivateChannel PrivateChannel's}, this always returns false
     *
     * @return if mentioning everyone
     */
    boolean mentionsEveryone();

    /**
     * Gives a copy of the Object holding the time this message was originally sent
     *
     * @return time of Message
     */
    OffsetDateTime getTime();

    /**
     * Returns true, if this Message was edited as least once
     *
     * @return if edited
     */
    boolean isEdited();

    /**
     * Gives a copy of the Object holding the time this message was last recently updated
     * If this message was never updated ({@link #isEdited()} returns false), this will be NULL
     *
     * @return time of most recent update
     */
    OffsetDateTime getEditedTimestamp();

    /**
     * The author of this Message
     *
     * @return Message author
     */
    User getAuthor();

    /**
     * The textual content of this message.
     * Mentioned users will get returned as @Username
     * If you want the actual Content (mentions as &lt;@id&gt;), use {@link #getRawContent()} instead
     *
     * @return message-text
     */
    String getContent();

    /**
     * The raw textual content of this message.
     * Mentioned users will get returned as &lt;@id&gt;
     *
     * @return raw message-text
     */
    String getRawContent();

    /**
     * Checks, whether this Message was sent in a {@link net.dv8tion.jda.entities.PrivateChannel PrivateChannel} (Private Message),
     * or in a {@link net.dv8tion.jda.entities.TextChannel TextChannel} (sent in Guild channel)
     *
     * @return
     *      true, if this Message is from a PrivateChannel, false if it is from a TextChannel
     */
    boolean isPrivate();

    /**
     * Returns the Id of the Channel this Message was sent in.
     * This can be the id of either a {@link net.dv8tion.jda.entities.TextChannel TextChannel} or a {@link net.dv8tion.jda.entities.PrivateChannel PrivateChannel}
     * To get the corresponding channel, you can use {@link net.dv8tion.jda.JDA#getTextChannelById(String)} or {@link net.dv8tion.jda.JDA#getPrivateChannelById(String)}
     * (Hint: {@link #isPrivate()} could be helpful!)
     *
     * @return
     *      The Id of the Channel this was sent in
     */
    String getChannelId();

    /**
     * Is this Message supposed to be TTS (Text-to-speach)
     *
     * @return if message is tts
     */
    boolean isTTS();

    /**
     * Edits this Messages content to the provided String.
     * If The Message was not created by this account, this does not have any effect
     * If this method failed, null gets returned
     *
     * @param newContent the new content of the Message
     * @return a new Message-Object for the edited message
     */
    Message updateMessage(String newContent);

    /**
     * Deletes this Message from the server.
     * Calling this function on a Message created by another User while not having the
     * {@link net.dv8tion.jda.Permission#MESSAGE_MANAGE MESSAGE_MANAGE Permission} will have no effect
     */
    void deleteMessage();

    /**
     * Marks this Message as read.
     */
    void acknowledge();

    /**
     * Returns the {@link net.dv8tion.jda.JDA JDA} instance of this Message
     * @return
     *      the corresponding JDA instance
     */
    JDA getJDA();
}
