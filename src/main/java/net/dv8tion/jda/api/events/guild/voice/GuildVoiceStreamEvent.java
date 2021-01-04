/*
 * Copyright 2015-2021 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
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

package net.dv8tion.jda.api.events.guild.voice;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nonnull;

/**
 * Indicates that a {@link net.dv8tion.jda.api.entities.Member Member} started or ended a stream.
 *
 * <p>Can be used to detect when a user goes live or stops streaming.
 */
public class GuildVoiceStreamEvent extends GenericGuildVoiceEvent
{
    private final boolean stream;

    public GuildVoiceStreamEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, boolean stream)
    {
        super(api, responseNumber, member);
        this.stream = stream;
    }

    /**
     * True if this user started streaming. False if the user stopped streaming.
     *
     * @return True, if the stream started
     */
    public boolean isStream()
    {
        return stream;
    }
}
