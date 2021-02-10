/*
 * Copyright 2015-2020 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
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

package net.dv8tion.jda.api.events.guild.update;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Indicates that the public updates channel of a {@link Guild Guild} changed.
 * <br>This is used for notices from Discord for admins and moderators of Community guilds
 *
 * <p>Can be used to detect when a public updates channel changes and retrieve the old one
 *
 * <p>Identifier: {@code public_updates_channel}
 */
public class GuildUpdatePublicUpdatesChannelEvent extends GenericGuildUpdateEvent<TextChannel>
{
    public static final String IDENTIFIER = "public_updates_channel";

    public GuildUpdatePublicUpdatesChannelEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nullable TextChannel oldPublicUpdatesChannel)
    {
        super(api, responseNumber, guild, oldPublicUpdatesChannel, guild.getPublicUpdatesChannel(), IDENTIFIER);
    }

    /**
     * The previous public updates channel.
     * 
     * @return The previous public updates channel
     */
    @Nullable
    public TextChannel getOldPublicUpdatesChannel()
    {
        return getOldValue();
    }

    /**
     * The public updates channel.
     *
     * @return The new public updates
     */
    @Nullable
    public TextChannel getNewPublicUpdatesChannel()
    {
        return getNewValue();
    }
}
