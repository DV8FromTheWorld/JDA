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

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.EntityBuilder;
import net.dv8tion.jda.internal.requests.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReadyHandler extends SocketHandler
{

    public ReadyHandler(JDAImpl api)
    {
        super(api);
    }

    @Override
    protected Long handleInternally(JSONObject content)
    {
        EntityBuilder builder = getJDA().getEntityBuilder();

        //Core
        JSONArray guilds = content.getJSONArray("guilds");
        JSONObject selfJson = content.getJSONObject("user");

        builder.createSelfUser(selfJson);
        if (getJDA().getGuildSetupController().setIncompleteCount(guilds.length()))
        {
            for (int i = 0; i < guilds.length(); i++)
            {
                JSONObject guild = guilds.getJSONObject(i);
                getJDA().getGuildSetupController().onReady(guild.getLong("id"), guild);
            }
        }

        handleReady(content);
        return null;
    }

    public void handleReady(JSONObject content)
    {
        EntityBuilder builder = getJDA().getEntityBuilder();
        JSONArray privateChannels = content.getJSONArray("private_channels");

        for (int i = 0; i < privateChannels.length(); i++)
        {
            JSONObject chan = privateChannels.getJSONObject(i);
            ChannelType type = ChannelType.fromId(chan.getInt("type"));

            switch (type)
            {
                case PRIVATE:
                    builder.createPrivateChannel(chan);
                    break;
                default:
                    WebSocketClient.LOG.warn("Received a Channel in the private_channels array in READY of an unknown type! Type: {}", type);
            }
        }
    }
}
