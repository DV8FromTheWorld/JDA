/**
 * Copyright 2015-2016 Austin Keener & Michael Ritter
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dv8tion.jda.entities.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDAInfo;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.exceptions.RateLimitedException;
import net.dv8tion.jda.handle.EntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.function.Consumer;

public class PrivateChannelImpl implements PrivateChannel {
    private final String id;
    private final User user;
    private final JDAImpl api;

    public PrivateChannelImpl(String id, User user, JDAImpl api) {
        this.id = id;
        this.user = user;
        this.api = api;
    }

    @Override
    public JDA getJDA() {
        return api;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Message sendMessage(String text) {
        return sendMessage(new MessageBuilder().appendString(text).build());
    }

    @Override
    public Message sendMessage(Message msg) {
        if (api.getMessageLimit() != null) {
            throw new RateLimitedException(api.getMessageLimit() - System.currentTimeMillis());
        }
        try {
            JSONObject response = api.getRequester().post("https://discordapp.com/api/channels/" + getId() + "/messages",
                    new JSONObject().put("content", msg.getRawContent()));
            if (response.has("retry_after")) {
                long retry_after = response.getLong("retry_after");
                api.setMessageTimeout(retry_after);
                throw new RateLimitedException(retry_after);
            }
            return new EntityBuilder(api).createMessage(response);
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            //sending failed
            return null;
        }
    }

    @Override
    public void sendMessageAsync(String text, Consumer<Message> callback) {
        sendMessageAsync(new MessageBuilder().appendString(text).build(), callback);
    }

    @Override
    public void sendMessageAsync(Message msg, Consumer<Message> callback) {
        ((MessageImpl) msg).setChannelId(getId());
        TextChannelImpl.AsyncMessageSender.getInstance(getJDA()).enqueue(msg, callback);
    }

    @Override
    @Deprecated
    public Message sendFile(File file) {
        return sendFile(file, null);
    }

    @Override
    @Deprecated
    public void sendFileAsync(File file, Consumer<Message> callback) {
        sendFileAsync(file, null, callback);
    }

    @Override
    public Message sendFile(File file, Message message) {
        JDAImpl api = (JDAImpl) getJDA();
        try {
            MultipartBody body = Unirest.post("https://discordapp.com/api/channels/" + getId() + "/messages")
                    .header("authorization", getJDA().getAuthToken())
                    .header("user-agent", JDAInfo.GITHUB + " " + JDAInfo.VERSION)
                    .field("file", file);
            if (message != null)
                body.field("content", message.getRawContent()).field("tts", message.isTTS());

            HttpResponse<JsonNode> response = body.asJson();

            JSONObject messageJson = new JSONObject(response.getBody().toString());
            return new EntityBuilder(api).createMessage(messageJson);
        }
        catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendFileAsync(File file, Message message, Consumer<Message> callback) {
        Thread thread = new Thread(() ->
        {
            Message messageReturn = sendFile(file, message);
            if (callback != null)
                callback.accept(message);
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void sendTyping() {
        api.getRequester().post("https://discordapp.com/api/channels/" + getId() + "/typing", new JSONObject());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PrivateChannel))
            return false;
        PrivateChannel oPChannel = (PrivateChannel) o;
        return this == oPChannel || this.getId().equals(oPChannel.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "PC:" + getUser().getUsername() + '(' + getId() + ')';
    }
}
