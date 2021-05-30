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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.managers.WebhookManager;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.internal.managers.WebhookManagerImpl;
import net.dv8tion.jda.internal.requests.Requester;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation for {@link net.dv8tion.jda.api.entities.Webhook Webhook}
 *
 * @since  3.0
 */
public class WebhookImpl implements Webhook
{
    private final TextChannel channel;
    private final long id;
    private final WebhookType type;
    private WebhookManager manager;
    private final JDA api;

    private Member owner;
    private User user, ownerUser;
    private String token;
    private ChannelReference sourceChannel;
    private GuildReference sourceGuild;

    public WebhookImpl(TextChannel channel, long id, WebhookType type)
    {
        this(channel, channel.getJDA(), id, type);
    }

    public WebhookImpl(TextChannel channel, JDA api, long id, WebhookType type)
    {
        this.channel = channel;
        this.api = api;
        this.id = id;
        this.type = type;
    }

    @NotNull
    @Override
    public WebhookType getType()
    {
        return type;
    }

    @Override
    public boolean isPartial()
    {
        return channel == null;
    }

    @NotNull
    @Override
    public JDA getJDA()
    {
        return api;
    }

    @NotNull
    @Override
    public Guild getGuild()
    {
        if (channel == null)
            throw new IllegalStateException("Cannot provide guild for this Webhook instance because it does not belong to this shard");
        return getChannel().getGuild();
    }

    @NotNull
    @Override
    public TextChannel getChannel()
    {
        if (channel == null)
            throw new IllegalStateException("Cannot provide channel for this Webhook instance because it does not belong to this shard");
        return channel;
    }

    @Override
    public Member getOwner()
    {
        if (owner == null && channel != null && ownerUser != null)
            return getGuild().getMember(ownerUser); // maybe it exists later?
        return owner;
    }

    @Override
    public User getOwnerAsUser()
    {
        return ownerUser;
    }

    @NotNull
    @Override
    public User getDefaultUser()
    {
        return user;
    }

    @NotNull
    @Override
    public String getName()
    {
        return user.getName();
    }

    @Override
    public String getToken()
    {
        return token;
    }

    @NotNull
    @Override
    public String getUrl()
    {
        return Requester.DISCORD_API_PREFIX + "webhooks/" + getId() + (getToken() == null ? "" : "/" + getToken());
    }

    @Override
    public ChannelReference getSourceChannel()
    {
        return sourceChannel;
    }

    @Override
    public GuildReference getSourceGuild()
    {
        return sourceGuild;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> delete()
    {
        if (token != null)
            return delete(token);

        if (!getGuild().getSelfMember().hasPermission(getChannel(), Permission.MANAGE_WEBHOOKS))
            throw new InsufficientPermissionException(getChannel(), Permission.MANAGE_WEBHOOKS);

        Route.CompiledRoute route = Route.Webhooks.DELETE_WEBHOOK.compile(getId());
        return new AuditableRestActionImpl<>(getJDA(), route);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> delete(@NotNull String token)
    {
        Checks.notNull(token, "Token");
        Route.CompiledRoute route = Route.Webhooks.DELETE_TOKEN_WEBHOOK.compile(getId(), token);
        return new AuditableRestActionImpl<>(getJDA(), route);
    }

    @NotNull
    @Override
    public WebhookManager getManager()
    {
        if (manager == null)
            return manager = new WebhookManagerImpl(this);
        return manager;
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    @Override
    @Deprecated
    public boolean isFake()
    {
        return token == null;
    }

    /* -- Impl Setters -- */

    public WebhookImpl setOwner(Member member, User user)
    {
        this.owner = member;
        this.ownerUser = user;
        return this;
    }

    public WebhookImpl setToken(String token)
    {
        this.token = token;
        return this;
    }

    public WebhookImpl setUser(User user)
    {
        this.user = user;
        return this;
    }

    public WebhookImpl setSourceGuild(GuildReference reference)
    {
        this.sourceGuild = reference;
        return this;
    }

    public WebhookImpl setSourceChannel(ChannelReference reference)
    {
        this.sourceChannel = reference;
        return this;
    }

    /* -- Object Overrides -- */

    @Override
    public int hashCode()
    {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (!(obj instanceof WebhookImpl))
            return false;
        WebhookImpl impl = (WebhookImpl) obj;
        return impl.id == id;
    }

    @Override
    public String toString()
    {
        return "WH:" + getName() + "(" + id + ")";
    }
}
