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

package net.dv8tion.jda.api.events.user.update;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

/**
 * Indicates that the {@link OnlineStatus OnlineStatus} of a {@link net.dv8tion.jda.api.entities.User User} changed.
 * <br>As with any presence updates this either happened for a {@link net.dv8tion.jda.api.entities.Member Member} in a Guild !
 *
 * <p>Can be used to retrieve the User who changed their status and their previous status.
 *
 * <p>Identifier: {@code status}
 */
public class UserUpdateOnlineStatusEvent extends GenericUserUpdateEvent<OnlineStatus> implements GenericUserPresenceEvent
{
    public static final String IDENTIFIER = "status";

    private final Guild guild;

    public UserUpdateOnlineStatusEvent(JDA api, long responseNumber, User user, Guild guild, OnlineStatus oldOnlineStatus)
    {
        super(api, responseNumber, user, oldOnlineStatus, guild.getMember(user).getOnlineStatus(), IDENTIFIER);
        this.guild = guild;
    }

    @Override
    public Guild getGuild()
    {
        return guild;
    }

    @Override
    public Member getMember()
    {
        return guild.getMember(getUser());
    }

    /**
     * The old status
     *
     * @return The old status
     */
    public OnlineStatus getOldOnlineStatus()
    {
        return getOldValue();
    }

    /**
     * The new status
     *
     * @return The new status
     */
    public OnlineStatus getNewOnlineStatus()
    {
        return getNewValue();
    }
}
