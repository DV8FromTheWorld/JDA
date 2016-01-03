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
package net.dv8tion.jda.handle;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.impl.JDAImpl;
import net.dv8tion.jda.events.guild.GuildLeaveEvent;
import org.json.JSONObject;

public class GuildLeaveHandler extends SocketHandler
{

    public GuildLeaveHandler(JDAImpl api, int responseNumber)
    {
        super(api, responseNumber);
    }

    @Override
    public void handle(JSONObject content)
    {
        Guild guild = api.getGuildMap().get(content.getString("id"));
        api.getEventManager().handle(
                new GuildLeaveEvent(
                        api, responseNumber,
                        guild));
        api.getGuildMap().remove(guild.getId());
    }
}
