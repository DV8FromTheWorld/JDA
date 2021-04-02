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

package net.dv8tion.jda.internal.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.annotation.Nonnull;

public class GuildVoiceStateImpl implements GuildVoiceState
{
    private final JDA api;
    private Guild guild;
    private Member member;
    private VoiceChannelImpl connectedChannel;

    private String sessionId;
    private boolean selfMuted = false;
    private boolean selfDeafened = false;
    private boolean guildMuted = false;
    private boolean guildDeafened = false;
    private boolean suppressed = false;
    private boolean stream = false;

    public GuildVoiceStateImpl(Member member)
    {
        this.api = member.getJDA();
        this.guild = member.getGuild();
        this.member = member;
    }

    @Override
    public boolean isSelfMuted()
    {
        return selfMuted;
    }

    @Override
    public boolean isSelfDeafened()
    {
        return selfDeafened;
    }

    @Nonnull
    @Override
    public JDA getJDA()
    {
        return api;
    }

    @Override
    public String getSessionId()
    {
        return sessionId;
    }

    @Override
    public boolean isMuted()
    {
        return isSelfMuted() || isGuildMuted();
    }

    @Override
    public boolean isDeafened()
    {
        return isSelfDeafened() || isGuildDeafened();
    }

    @Override
    public boolean isGuildMuted()
    {
        return guildMuted;
    }

    @Override
    public boolean isGuildDeafened()
    {
        return guildDeafened;
    }

    @Override
    public boolean isSuppressed()
    {
        return suppressed;
    }

    @Override
    public boolean isStream()
    {
        return stream;
    }

    @Override
    public VoiceChannel getChannel()
    {
        return connectedChannel;
    }

    @Nonnull
    @Override
    public Guild getGuild()
    {
        Guild realGuild = api.getGuildById(guild.getIdLong());
        if (realGuild != null)
            guild = realGuild;
        return guild;
    }

    @Nonnull
    @Override
    public Member getMember()
    {
        Member realMember = getGuild().getMemberById(member.getIdLong());
        if (realMember != null)
            member = realMember;
        return member;
    }

    @Override
    public boolean inVoiceChannel()
    {
        return getChannel() != null;
    }

    @Override
    public int hashCode()
    {
        return getMember().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (!(obj instanceof GuildVoiceState))
            return false;
        GuildVoiceState oStatus = (GuildVoiceState) obj;
        return this.getMember().equals(oStatus.getMember());
    }

    @Override
    public String toString()
    {
        return "VS:" + getGuild().getName() + ':' + getMember().getEffectiveName();
    }

    // -- Setters --

    public GuildVoiceStateImpl setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
        return this;
    }

    public GuildVoiceStateImpl setSelfMuted(boolean selfMuted)
    {
        this.selfMuted = selfMuted;
        return this;
    }

    public GuildVoiceStateImpl setSelfDeafened(boolean selfDeafened)
    {
        this.selfDeafened = selfDeafened;
        return this;
    }

    public GuildVoiceStateImpl setGuildMuted(boolean guildMuted)
    {
        this.guildMuted = guildMuted;
        return this;
    }

    public GuildVoiceStateImpl setGuildDeafened(boolean guildDeafened)
    {
        this.guildDeafened = guildDeafened;
        return this;
    }

    public GuildVoiceStateImpl setSuppressed(boolean suppressed)
    {
        this.suppressed = suppressed;
        return this;
    }

    public GuildVoiceStateImpl setStream(boolean stream)
    {
        this.stream = stream;
        return this;
    }

    public GuildVoiceStateImpl setConnectedChannel(VoiceChannelImpl channel)
    {
        this.connectedChannel = channel;
        return this;
    }
}
