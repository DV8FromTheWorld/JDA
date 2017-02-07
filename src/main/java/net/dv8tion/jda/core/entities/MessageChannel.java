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
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.dv8tion.jda.core.entities;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.impl.MessageImpl;
import net.dv8tion.jda.core.requests.Request;
import net.dv8tion.jda.core.requests.Response;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.Route;
import org.apache.http.util.Args;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Represents a Discord channel that can have messages and files sent to it.
 */
public interface MessageChannel extends ISnowflake
{
    /**
     * This method is a shortcut method to return the following information in the following situation:
     * If the MessageChannel is instance of..
     * <ul>
     *     <li><b>TextChannel</b> - Returns {@link TextChannel#getName()}</li>
     *     <li><b>PrivateChannel</b> Returns {@link PrivateChannel#getUser()} {@link net.dv8tion.jda.core.entities.User#getName() .getName()}</li>
     *     <li><b>Group</b> - Returns {@link net.dv8tion.jda.client.entities.Group#getName()}</li>
     * </ul>
     *
     * @return
     *      Possibly-null "name" of the MessageChannel. Different implementations determine what the name is.
     */
    String getName();

    //TODO: doc
    ChannelType getType();

    /**
     * Returns the {@link net.dv8tion.jda.core.JDA JDA} instance of this MessageChannel
     *
     * @return
     *      the corresponding JDA instance
     */
    JDA getJDA();

    /**
     * Sends a plain text {@link net.dv8tion.jda.core.entities.Message Message} to this channel.
     * This will fail if the account of the api does not have the {@link net.dv8tion.jda.core.Permission#MESSAGE_WRITE Write-Permission}
     * for this channel set
     * After the Message has been sent, the created {@link net.dv8tion.jda.core.entities.Message Message} object is returned
     * This Object will be null, if the sending failed.
     * When the Rate-limit is reached (10 Messages in 10 secs), a {@link net.dv8tion.jda.core.exceptions.RateLimitedException RateLimitedException} is thrown
     *
     * @param text
     *          the text to send
     * @return
     *      the Message created by this function
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *      If this is a {@link net.dv8tion.jda.core.entities.TextChannel TextChannel} and the logged in account does
     *      not have {@link net.dv8tion.jda.core.Permission#MESSAGE_WRITE Permission.MESSAGE_WRITE}.
     */
    RestAction<Message> sendMessage(String text);
    
    /**
     * Sends a {@link net.dv8tion.jda.core.entities.Message Message} containing a rich embed to this channel.
     * This will fail if the account of the api does not have the {@link net.dv8tion.jda.core.Permission#MESSAGE_WRITE Write-Permission}
     * for this channel set
     * After the Message has been sent, the created {@link net.dv8tion.jda.core.entities.Message Message} object is returned
     * This Object will be null, if the sending failed.
     * When the Rate-limit is reached (10 Messages in 10 secs), a {@link net.dv8tion.jda.core.exceptions.RateLimitedException RateLimitedException} is thrown
     *
     * @param embed
     *          the embed to send
     * @return
     *      the Message created by this function
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *      If this is a {@link net.dv8tion.jda.core.entities.TextChannel TextChannel} and the logged in account does
     *      not have {@link net.dv8tion.jda.core.Permission#MESSAGE_WRITE Permission.MESSAGE_WRITE}.
     */
    RestAction<Message> sendMessage(MessageEmbed embed);

    /**
     * Sends a given {@link net.dv8tion.jda.core.entities.Message Message} to this Channel
     * This method only extracts the mentions, text and tts status out of the given Message-Object
     * Therefore this can also be used to resend already received Messages
     * To allow above behaviour, this method returns a new {@link net.dv8tion.jda.core.entities.Message Message} instance. The passed one is not modified!
     * If the sending of the Message failed (probably Permissions), this method returns null.
     * When the Rate-limit is reached (10 Messages in 10 secs), a {@link net.dv8tion.jda.core.exceptions.RateLimitedException RateLimitedException} is thrown
     *
     * @param msg
     *          the {@link net.dv8tion.jda.core.entities.Message Message} to send
     * @return
     *      The created {@link net.dv8tion.jda.core.entities.Message Message} object or null if it failed
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *      If this is a {@link net.dv8tion.jda.core.entities.TextChannel TextChannel} and the logged in account does
     *      not have {@link net.dv8tion.jda.core.Permission#MESSAGE_WRITE Permission.MESSAGE_WRITE}.
     */
    RestAction<Message> sendMessage(Message msg);

    /**
     * Uploads a file to the Discord servers and sends it to this {@link net.dv8tion.jda.core.entities.TextChannel TextChannel}.
     * Sends the provided {@link net.dv8tion.jda.core.entities.Message Message} with the uploaded file.<br>
     * If you do not wish to send a Message with the uploaded file, you can provide <code>null</code> for
     * the <code>message</code> parameter.
     *
     * @param file
     *          The file to upload to the {@link net.dv8tion.jda.core.entities.TextChannel TextChannel}.
     * @param message
     *          The message to be sent along with the uploaded file. This value can be <code>null</code>.
     * @return
     *      The {@link net.dv8tion.jda.core.entities.Message Message} created from this upload.
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *      <ul>
     *          <li>
     *              If this is a {@link net.dv8tion.jda.core.entities.TextChannel TextChannel} and the logged in account does
     *              not have {@link net.dv8tion.jda.core.Permission#MESSAGE_WRITE Permission.MESSAGE_WRITE}.
     *          </li>
     *          <li>
     *              If this is a {@link net.dv8tion.jda.core.entities.TextChannel TextChannel} and the logged in account does
     *              not have {@link net.dv8tion.jda.core.Permission#MESSAGE_ATTACH_FILES Permission.MESSAGE_ATTACH_FILES}.
     *          </li>
     *      </ul>
     * @throws IOException
     *      If an I/O error occurs while reading the File.
     */
    RestAction<Message> sendFile(File file, Message message) throws IOException;
    RestAction<Message> sendFile(File file, String fileName, Message message) throws IOException;
    RestAction<Message> sendFile(InputStream data, String fileName, Message message);
    RestAction<Message> sendFile(byte[] data, String fileName, Message message);

    /**
     * Attempts to get a {@link net.dv8tion.jda.core.entities.Message Message} from the Discord servers that has
     * the same id as the id provided.<br>
     *
     * @param messageId
     *          The id of the sought after Message
     * @return
     *      The Message defined by the provided id. `Null` if the message doesn't exist.
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *      Thrown if:
     *      <ul>
     *          <li>Attempt to get a message from a channel which this account doesn't have access to.
     *              ({@link net.dv8tion.jda.core.Permission#MESSAGE_READ Permission.MESSAGE_READ})</li>
     *          <li>Attempt to get a message from a channel that this account cannot read the history of.
     *              ({@link net.dv8tion.jda.core.Permission#MESSAGE_HISTORY Permission.MESSAGE_HISTORY})</li>
     *      </ul>
     */
    RestAction<Message> getMessageById(String messageId);

    /**
     * Attempts to delete a {@link net.dv8tion.jda.core.entities.Message Message} from the Discord servers
     * that has the same id as the id provided.<br>
     *
     * @param messageId
     *          The id of the Message which should be deleted
     * @return
     *      True if the message was successfully deleted. False if the message didn't exist.
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *      Thrown if:
     *      <ul>
     *          <li>Attempt to get a message from a channel which this account doesn't have access to.
     *              ({@link net.dv8tion.jda.core.Permission#MESSAGE_READ Permission.MESSAGE_READ})</li>
     *          <li>Attempt to delete another user's message in a channel that this account doesn't have permission to manage.
     *              ({@link net.dv8tion.jda.core.Permission#MESSAGE_MANAGE Permission.MESSAGE_MANAGE})</li>
     *          <li>Attempt to delete another user's message in a PrivateChannel.</li>
     *      </ul>
     */
    RestAction<Void> deleteMessageById(String messageId);

    /**
     * Creates a new {@link net.dv8tion.jda.core.MessageHistory MessageHistory} object for each call of this method.<br>
     * This is <b>NOT</b> and internal message cache, but rather it queries the Discord servers for old messages.
     *
     * @return
     *      The MessageHistory for this channel.
     */
    MessageHistory getHistory();

    //TODO: doc
    RestAction<MessageHistory> getHistoryAround(Message message, int limit);
    RestAction<MessageHistory> getHistoryAround(String messageId, int limit);

    /**
     * Sends the typing status to discord. This is what is used to make the message "X is typing..." appear.<br>
     * The typing status only lasts for 10 seconds or until a message is sent.<br>
     * So if you wish to show continuous typing you will need to call this method once every 10 seconds.
     * <p>
     * The official discord client sends this every 5 seconds even though the typing status lasts 10.
     *
     * @return
     *          {@link net.dv8tion.jda.core.requests.RestAction RestAction}.
     */
    RestAction<Void> sendTyping();

    //TODO: doc
    default RestAction<Void> addReactionById(String messageId, String unicode)
    {
        Args.notNull(messageId, "MessageId");
        Args.containsNoBlanks(unicode, "Provided Unicode");
        String encoded;
        try
        {
            encoded = URLEncoder.encode(unicode, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e); //thanks JDK 1.4
        }
        Route.CompiledRoute route = Route.Messages.ADD_REACTION.compile(getId(), messageId, encoded);
        return new RestAction<Void>(getJDA(), route, null)
        {
            @Override
            protected void handleResponse(Response response, Request request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }

    //TODO: doc
    default RestAction<Void> addReactionById(String messageId, Emote emote)
    {
        Args.notNull(messageId, "MessageId");
        Args.notNull(emote, "Emote");

        Route.CompiledRoute route = Route.Messages.ADD_REACTION.compile(getId(), messageId, String.format("%s:%s", emote.getName(), emote.getId()));
        return new RestAction<Void>(getJDA(), route, null)
        {
            @Override
            protected void handleResponse(Response response, Request request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }

    /**
     * Used to pin a message.<br>
     * If the provided messageId is invalid or not in this channel, this does nothing.
     *
     * @param messageId
     *          The message to pin.
     * @return
     *      {@link net.dv8tion.jda.core.requests.RestAction RestAction}&lt;{@link Void}&gt;
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *          If this is a TextChannel and this account does not have both
     *          {@link net.dv8tion.jda.core.Permission#MESSAGE_READ Permission.MESSAGE_READ} and
     *          {@link net.dv8tion.jda.core.Permission#MESSAGE_MANAGE Permission.MESSAGE_MANAGE}
     */
    RestAction<Void> pinMessageById(String messageId);

    /**
     * Used to unpin a message.<br>
     * If the provided messageId is invalid or not in this channel, this does nothing.
     *
     * @param messageId
     *          The message to pin.
     * @return
     *      True - if the message was successfully unpinned. If false, the message id probably didn't exist or wasn't a message from this channel.
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *          If this is a TextChannel and this account does not have both
     *          {@link net.dv8tion.jda.core.Permission#MESSAGE_READ Permission.MESSAGE_READ} and
     *          {@link net.dv8tion.jda.core.Permission#MESSAGE_MANAGE Permission.MESSAGE_MANAGE}
     */
    RestAction<Void> unpinMessageById(String messageId);

    /**
     * Gets a List of {@link net.dv8tion.jda.core.entities.Message Messages} that have been pinned in this channel.<br>
     * If no messages have been pinned, this returns an empty List.
     *
     * @return
     *      {@link net.dv8tion.jda.core.requests.RestAction RestAction&lt;List&lt;Message&gt;&gt;}
     * @throws net.dv8tion.jda.core.exceptions.PermissionException
     *          If this is a TextChannel and this account does not have
     *          {@link net.dv8tion.jda.core.Permission#MESSAGE_READ Permission.MESSAGE_READ}
     */
    RestAction<List<Message>> getPinnedMessages();

    default RestAction<Message> editMessageById(String id, String newContent)
    {
        return editMessageById(id, new MessageBuilder().appendString(newContent).build());
    }

    default RestAction<Message> editMessageById(String id, Message newContent)
    {
        Args.notNull(id, "id");
        Args.notNull(newContent, "message");

        JSONObject json = ((MessageImpl) newContent).toJSONObject();
        Route.CompiledRoute route = Route.Messages.EDIT_MESSAGE.compile(getId(), id);
        return new RestAction<Message>(getJDA(), route, json)
        {
            @Override
            protected void handleResponse(Response response, Request request)
            {
                if (response.isOk())
                {
                    try
                    {
                        Message m = EntityBuilder.get(api).createMessage(response.getObject());
                        request.onSuccess(m);
                    }
                    catch (IllegalArgumentException e)
                    {
                        request.onFailure(e);
                    }
                }
                else
                {
                    request.onFailure(response);
                }
            }
        };
    }
}
