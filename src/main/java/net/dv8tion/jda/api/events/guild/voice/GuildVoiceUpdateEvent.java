/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
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

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.UpdateEvent;

import javax.annotation.Nullable;

/**
 * Indicates that a {@link net.dv8tion.jda.api.entities.Member Member} joined or left a {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
 * <br>Generic event that combines
 * {@link net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent GuildVoiceLeaveEvent},
 * {@link net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent GuildVoiceJoinEvent}, and
 * {@link net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent GuildVoiceMoveEvent} for convenience.
 *
 * <p>Can be used to detect when a Member leaves/joins a channel
 *
 * <h2>Requirements</h2>
 *
 * <p>This event requires the {@link net.dv8tion.jda.api.utils.cache.CacheFlag#VOICE_STATE VOICE_STATE} CacheFlag to be enabled, which requires
 * the {@link net.dv8tion.jda.api.requests.GatewayIntent#GUILD_VOICE_STATES GUILD_VOICE_STATES} intent.
 *
 * <br>{@link net.dv8tion.jda.api.JDABuilder#createLight(String) createLight(String)} disables that CacheFlag by default!
 *
 * <p>Additionally, this event requires the {@link net.dv8tion.jda.api.utils.MemberCachePolicy MemberCachePolicy}
 * to cache the updated members. Discord does not specifically tell us about the updates, but merely tells us the
 * member was updated and gives us the updated member object. In order to fire a specific event like this we
 * need to have the old member cached to compare against.
 *
 * <p>Identifier: {@code voice-channel}
 */
public interface GuildVoiceUpdateEvent extends UpdateEvent<Member, VoiceChannel>
{
    String IDENTIFIER = "voice-channel";

    /**
     * The {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} that the {@link net.dv8tion.jda.api.entities.Member Member} is moved from
     *
     * @return The {@link net.dv8tion.jda.api.entities.VoiceChannel}
     */
    @Nullable
    VoiceChannel getChannelLeft();

    /**
     * The {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} that was joined
     *
     * @return The {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}
     */
    @Nullable
    VoiceChannel getChannelJoined();
}
