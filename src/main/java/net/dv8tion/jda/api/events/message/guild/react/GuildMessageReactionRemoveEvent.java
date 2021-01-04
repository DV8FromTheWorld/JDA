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

package net.dv8tion.jda.api.events.message.guild.react;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Indicates that a {@link net.dv8tion.jda.api.entities.MessageReaction MessageReaction} was removed from a Message in a Guild
 *
 * <p>Can be used to detect when a reaction is removed in a guild
 */
public class GuildMessageReactionRemoveEvent extends GenericGuildMessageReactionEvent
{
    public GuildMessageReactionRemoveEvent(@Nonnull JDA api, long responseNumber, @Nullable Member member, @Nonnull MessageReaction reaction, long userId)
    {
        super(api, responseNumber, member, reaction, userId);
    }
}
