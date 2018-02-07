/*
 *     Copyright 2015-2018 Austin Keener & Michael Ritter & Florian Spieß
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

package net.dv8tion.jda.core.entities.impl;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import gnu.trove.map.TLongObjectMap;
import net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.AccountTypeException;
import net.dv8tion.jda.core.exceptions.GuildUnavailableException;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;
import net.dv8tion.jda.core.managers.GuildManagerUpdatable;
import net.dv8tion.jda.core.managers.impl.AudioManagerImpl;
import net.dv8tion.jda.core.requests.Request;
import net.dv8tion.jda.core.requests.Response;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.Route;
import net.dv8tion.jda.core.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.core.utils.Checks;
import net.dv8tion.jda.core.utils.MiscUtil;
import net.dv8tion.jda.core.utils.cache.MemberCacheView;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.core.utils.cache.impl.MemberCacheViewImpl;
import net.dv8tion.jda.core.utils.cache.impl.SnowflakeCacheViewImpl;
import net.dv8tion.jda.core.utils.cache.impl.SortedSnowflakeCacheView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class GuildImpl implements Guild
{
    private final long id;
    private final JDAImpl api;

    private final SortedSnowflakeCacheView<Category> categoryCache = new SortedSnowflakeCacheView<Category>(Channel::getName, Comparator.naturalOrder());
    private final SortedSnowflakeCacheView<VoiceChannel> voiceChannelCache = new SortedSnowflakeCacheView<VoiceChannel>(Channel::getName, Comparator.naturalOrder());
    private final SortedSnowflakeCacheView<TextChannel> textChannelCache = new SortedSnowflakeCacheView<TextChannel>(Channel::getName, Comparator.naturalOrder());
    private final SortedSnowflakeCacheView<Role> roleCache = new SortedSnowflakeCacheView<Role>(Role::getName, Comparator.reverseOrder());
    private final SnowflakeCacheViewImpl<Emote> emoteCache = new SnowflakeCacheViewImpl<>(Emote::getName);
    private final MemberCacheViewImpl memberCache = new MemberCacheViewImpl();

    private final TLongObjectMap<JSONObject> cachedPresences = MiscUtil.newLongMap();

    private final Object mngLock = new Object();
    private volatile GuildManager manager;
    private volatile GuildManagerUpdatable managerUpdatable;
    private volatile GuildController controller;

    //public for internal accessors
    // getters do additional checks which might throw
    public Member owner;
    public String name;

    private String iconId;
    private String splashId;
    private String region = "";
    private Set<String> features = Collections.emptySet();
    private VoiceChannel afkChannel;
    private TextChannel systemChannel;
    private Role publicRole;
    private VerificationLevel verificationLevel = VerificationLevel.UNKNOWN;
    private NotificationLevel defaultNotificationLevel = NotificationLevel.UNKNOWN;
    private ExplicitContentLevel explicitContentLevel = ExplicitContentLevel.UNKNOWN;
    private MFALevel mfaLevel = MFALevel.UNKNOWN;
    private Timeout afkTimeout = Timeout.UNKNOWN;
    private boolean available;
    private boolean canSendVerification = false;

    public GuildImpl(JDAImpl api, long id)
    {
        this.id = id;
        this.api = api;
    }

    @NonNull
    @Override
    public String getName()
    {
        return name == null ? "null" : name;
    }

    @Override
    public String getIconId()
    {
        return iconId;
    }

    @Override
    public String getIconUrl()
    {
        return iconId == null ? null : "https://cdn.discordapp.com/icons/" + id + "/" + iconId + ".jpg";
    }

    @NonNull
    @Override
    public Set<String> getFeatures()
    {
        return features;
    }

    @Override
    public String getSplashId()
    {
        return splashId;
    }

    @Override
    public String getSplashUrl()
    {
        return splashId == null ? null : "https://cdn.discordapp.com/splashes/" + id + "/" + splashId + ".jpg";
    }

    @NonNull
    @Override
    public RestAction<String> getVanityUrl()
    {
        if (!isAvailable())
            throw new GuildUnavailableException();
        if (!getSelfMember().hasPermission(Permission.MANAGE_SERVER))
            throw new InsufficientPermissionException(Permission.MANAGE_SERVER);
        if (!getFeatures().contains("VANITY_URL"))
            throw new IllegalStateException("This guild doesn't have a vanity url");

        Route.CompiledRoute route = Route.Guilds.GET_VANITY_URL.compile(getId());

        return new RestAction<String>(api, route)
        {
            @Override
            protected void handleResponse(Response response, Request<String> request)
            {
                if (!response.isOk())
                {
                    request.onFailure(response);
                    return;
                }

                request.onSuccess(response.getObject().getString("code"));
            }
        };
    }

    @Override
    public VoiceChannel getAfkChannel()
    {
        return afkChannel;
    }

    @Override
    public TextChannel getSystemChannel()
    {
        return systemChannel;
    }

    @NonNull
    @Override
    public RestAction<List<Webhook>> getWebhooks()
    {
        if (!getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS))
            throw new InsufficientPermissionException(Permission.MANAGE_WEBHOOKS);

        Route.CompiledRoute route = Route.Guilds.GET_WEBHOOKS.compile(getId());

        return new RestAction<List<Webhook>>(api, route)
        {
            @Override
            protected void handleResponse(Response response, Request<List<Webhook>> request)
            {
                if (!response.isOk())
                {
                    request.onFailure(response);
                    return;
                }

                JSONArray array = response.getArray();
                List<Webhook> webhooks = new ArrayList<>(array.length());
                EntityBuilder builder = api.getEntityBuilder();

                for (Object object : array)
                {
                    try
                    {
                        webhooks.add(builder.createWebhook((JSONObject) object));
                    }
                    catch (JSONException | NullPointerException e)
                    {
                        JDAImpl.LOG.error("Error creating webhook from json", e);
                    }
                }

                request.onSuccess(Collections.unmodifiableList(webhooks));
            }
        };
    }

    @NonNull
    @Override
    public Member getOwner()
    {
        if (owner != null)
            return owner;
        else if (!isAvailable())
            throw new GuildUnavailableException();
        throw new IllegalStateException("Owner is null");
    }

    @NonNull
    @Override
    public Timeout getAfkTimeout()
    {
        return afkTimeout;
    }

    @NonNull
    @Override
    public String getRegionRaw()
    {
        return region;
    }

    @Override
    public boolean isMember(User user)
    {
        return memberCache.getMap().containsKey(user.getIdLong());
    }

    @NonNull
    @Override
    public Member getSelfMember()
    {
        final Member self = getMember(getJDA().getSelfUser());
        if (self != null)
            return self;
        else if (!isAvailable())
            throw new GuildUnavailableException();
        throw new IllegalStateException("SelfMember is null!");
    }

    @Override
    public Member getMember(User user)
    {
        return getMemberById(user.getIdLong());
    }

    @NonNull
    @Override
    public MemberCacheView getMemberCache()
    {
        return memberCache;
    }

    @NonNull
    @Override
    public SnowflakeCacheView<Category> getCategoryCache()
    {
        return categoryCache;
    }

    @NonNull
    @Override
    public SnowflakeCacheView<TextChannel> getTextChannelCache()
    {
        return textChannelCache;
    }

    @NonNull
    @Override
    public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache()
    {
        return voiceChannelCache;
    }

    @NonNull
    @Override
    public SnowflakeCacheView<Role> getRoleCache()
    {
        return roleCache;
    }

    @NonNull
    @Override
    public SnowflakeCacheView<Emote> getEmoteCache()
    {
        return emoteCache;
    }

    @NonNull
    @Override
    public RestAction<List<User>> getBans()
    {
        if (!isAvailable())
            throw new GuildUnavailableException();
        if (!getSelfMember().hasPermission(Permission.BAN_MEMBERS))
            throw new InsufficientPermissionException(Permission.BAN_MEMBERS);

        Route.CompiledRoute route = Route.Guilds.GET_BANS.compile(getId());
        return new RestAction<List<User>>(getJDA(), route)
        {
            @Override
            protected void handleResponse(Response response, Request<List<User>> request)
            {
                if (!response.isOk())
                {
                    request.onFailure(response);
                    return;
                }

                EntityBuilder builder = api.getEntityBuilder();
                List<User> bans = new LinkedList<>();
                JSONArray bannedArr = response.getArray();

                for (int i = 0; i < bannedArr.length(); i++)
                {
                    JSONObject user = bannedArr.getJSONObject(i).getJSONObject("user");
                    bans.add(builder.createFakeUser(user, false));
                }
                request.onSuccess(Collections.unmodifiableList(bans));
            }
        };
    }

    @NonNull
    @Override
    public RestAction<List<Ban>> getBanList()
    {
        if (!isAvailable())
            throw new GuildUnavailableException();
        if (!getSelfMember().hasPermission(Permission.BAN_MEMBERS))
            throw new InsufficientPermissionException(Permission.BAN_MEMBERS);

        Route.CompiledRoute route = Route.Guilds.GET_BANS.compile(getId());
        return new RestAction<List<Ban>>(getJDA(), route)
        {
            @Override
            protected void handleResponse(Response response, Request<List<Ban>> request)
            {
                if (!response.isOk())
                {
                    request.onFailure(response);
                    return;
                }

                EntityBuilder builder = api.getEntityBuilder();
                List<Ban> bans = new LinkedList<>();
                JSONArray bannedArr = response.getArray();

                for (int i = 0; i < bannedArr.length(); i++)
                {
                    final JSONObject object = bannedArr.getJSONObject(i);
                    JSONObject user = object.getJSONObject("user");
                    bans.add(new Ban(builder.createFakeUser(user, false), object.optString("reason", null)));
                }
                request.onSuccess(Collections.unmodifiableList(bans));
            }
        };
    }

    @NonNull
    @Override
    public RestAction<Integer> getPrunableMemberCount(int days)
    {
        if (!isAvailable())
            throw new GuildUnavailableException();
        if (!getSelfMember().hasPermission(Permission.KICK_MEMBERS))
            throw new InsufficientPermissionException(Permission.KICK_MEMBERS);

        if (days < 1)
            throw new IllegalArgumentException("Days amount must be at minimum 1 day.");

        Route.CompiledRoute route = Route.Guilds.PRUNABLE_COUNT.compile(getId()).withQueryParams("days", Integer.toString(days));
        return new RestAction<Integer>(getJDA(), route)
        {
            @Override
            protected void handleResponse(Response response, Request<Integer> request)
            {
                if (response.isOk())
                    request.onSuccess(response.getObject().getInt("pruned"));
                else
                    request .onFailure(response);
            }
        };
    }

    @NonNull
    @Override
    public Role getPublicRole()
    {
        if (publicRole != null)
            return publicRole;
        else if (!isAvailable())
            throw new GuildUnavailableException();
        throw new IllegalStateException("PublicRole is null!");
    }

    @Nullable
    @Override
    public TextChannel getDefaultChannel()
    {
        final Role role = getPublicRole();
        return getTextChannelsMap().valueCollection().stream()
                .filter(c -> role.hasPermission(c, Permission.MESSAGE_READ))
                .sorted(Comparator.naturalOrder())
                .findFirst().orElse(null);
    }

    @NonNull
    @Override
    public GuildManager getManager()
    {
        GuildManager mng = manager;
        if (mng == null)
        {
            synchronized (mngLock)
            {
                mng = manager;
                if (mng == null)
                    mng = manager = new GuildManager(this);
            }
        }
        return mng;
    }

    @NonNull
    @Override
    public GuildManagerUpdatable getManagerUpdatable()
    {
        GuildManagerUpdatable mng = managerUpdatable;
        if (mng == null)
        {
            synchronized (mngLock)
            {
                mng = managerUpdatable;
                if (mng == null)
                    mng = managerUpdatable = new GuildManagerUpdatable(this);
            }
        }
        return mng;
    }

    @NonNull
    @Override
    public GuildController getController()
    {
        GuildController ctrl = controller;
        if (ctrl == null)
        {
            synchronized (mngLock)
            {
                ctrl = controller;
                if (ctrl == null)
                    ctrl = controller = new GuildController(this);
            }
        }
        return ctrl;
    }

    @NonNull
    @Override
    public MentionPaginationAction getRecentMentions()
    {
        AccountTypeException.check(getJDA().getAccountType(), AccountType.CLIENT);
        return getJDA().asClient().getRecentMentions(this);
    }

    @NonNull
    @Override
    public AuditLogPaginationAction getAuditLogs()
    {
        return new AuditLogPaginationAction(this);
    }

    @NonNull
    @Override
    public RestAction<Void> leave()
    {
        if (owner.equals(getSelfMember()))
            throw new IllegalStateException("Cannot leave a guild that you are the owner of! Transfer guild ownership first!");

        Route.CompiledRoute route = Route.Self.LEAVE_GUILD.compile(getId());
        return new RestAction<Void>(api, route)
        {
            @Override
            protected void handleResponse(Response response, Request<Void> request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }

    @NonNull
    @Override
    public RestAction<Void> delete()
    {
        if (api.getSelfUser().isMfaEnabled())
            throw new IllegalStateException("Cannot delete a guild without providing MFA code. Use Guild#delete(String)");

        return delete(null);
    }

    @NonNull
    @Override
    public RestAction<Void> delete(@Nullable String mfaCode)
    {
        if (!owner.equals(getSelfMember()))
            throw new PermissionException("Cannot delete a guild that you do not own!");

        JSONObject mfaBody = null;
        if (api.getSelfUser().isMfaEnabled())
        {
            Checks.notEmpty(mfaCode, "Provided MultiFactor Auth code");
            mfaBody = new JSONObject().put("code", mfaCode);
        }

        Route.CompiledRoute route = Route.Guilds.DELETE_GUILD.compile(getId());
        return new RestAction<Void>(api, route, mfaBody)
        {
            @Override
            protected void handleResponse(Response response, Request<Void> request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }

    @NonNull
    @Override
    public AudioManager getAudioManager()
    {
        if (!api.isAudioEnabled())
            throw new IllegalStateException("Audio is disabled. Cannot retrieve an AudioManager while audio is disabled.");

        final TLongObjectMap<AudioManager> managerMap = api.getAudioManagerMap();
        AudioManager mng = managerMap.get(id);
        if (mng == null)
        {
            // No previous manager found -> create one
            synchronized (managerMap)
            {
                mng = managerMap.get(id);
                if (mng == null)
                {
                    mng = new AudioManagerImpl(this);
                    managerMap.put(id, mng);
                }
            }
        }
        return mng;
    }

    @NonNull
    @Override
    public JDAImpl getJDA()
    {
        return api;
    }

    @NonNull
    @Override
    public List<GuildVoiceState> getVoiceStates()
    {
        return Collections.unmodifiableList(
                getMembersMap().valueCollection().stream().map(Member::getVoiceState).collect(Collectors.toList()));
    }

    @NonNull
    @Override
    public VerificationLevel getVerificationLevel()
    {
        return verificationLevel;
    }

    @NonNull
    @Override
    public NotificationLevel getDefaultNotificationLevel()
    {
        return defaultNotificationLevel;
    }

    @NonNull
    @Override
    public MFALevel getRequiredMFALevel()
    {
        return mfaLevel;
    }

    @NonNull
    @Override
    public ExplicitContentLevel getExplicitContentLevel()
    {
        return explicitContentLevel;
    }

    @Override
    public boolean checkVerification()
    {
        if (api.getAccountType() == AccountType.BOT)
            return true;
        if(canSendVerification)
            return true;

        if (api.getSelfUser().getPhoneNumber() != null)
            return canSendVerification = true;

        switch (verificationLevel)
        {
            case VERY_HIGH:
                break; // we already checked for a verified phone number
            case HIGH:
                if (ChronoUnit.MINUTES.between(getSelfMember().getJoinDate(), OffsetDateTime.now()) < 10)
                    break;
            case MEDIUM:
                if (ChronoUnit.MINUTES.between(MiscUtil.getCreationTime(api.getSelfUser()), OffsetDateTime.now()) < 5)
                    break;
            case LOW:
                if (!api.getSelfUser().isVerified())
                    break;
            case NONE:
                canSendVerification = true;
                return true;
            case UNKNOWN:
                return true; // try and let discord decide
        }
        return false;
    }

    @Override
    public boolean isAvailable()
    {
        return available;
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    @NonNull
    @Override
    public RestAction<List<Invite>> getInvites()
    {
        if (!this.getSelfMember().hasPermission(Permission.MANAGE_SERVER))
            throw new InsufficientPermissionException(Permission.MANAGE_SERVER);

        final Route.CompiledRoute route = Route.Invites.GET_GUILD_INVITES.compile(getId());

        return new RestAction<List<Invite>>(api, route)
        {
            @Override
            protected void handleResponse(final Response response, final Request<List<Invite>> request)
            {
                if (response.isOk())
                {
                    EntityBuilder entityBuilder = this.api.getEntityBuilder();
                    JSONArray array = response.getArray();
                    List<Invite> invites = new ArrayList<>(array.length());
                    for (int i = 0; i < array.length(); i++)
                        invites.add(entityBuilder.createInvite(array.getJSONObject(i)));
                    request.onSuccess(Collections.unmodifiableList(invites));
                }
                else
                {
                    request.onFailure(response);
                }
            }
        };
    }

    // ---- Setters -----

    public GuildImpl setAvailable(boolean available)
    {
        this.available = available;
        return this;
    }

    public GuildImpl setOwner(Member owner)
    {
        this.owner = owner;
        return this;
    }

    public GuildImpl setName(String name)
    {
        this.name = name;
        return this;
    }

    public GuildImpl setIconId(@Nullable String iconId)
    {
        this.iconId = iconId;
        return this;
    }

    public GuildImpl setFeatures(Set<String> features)
    {
        this.features = Collections.unmodifiableSet(features);
        return this;
    }

    public GuildImpl setSplashId(@Nullable String splashId)
    {
        this.splashId = splashId;
        return this;
    }

    public GuildImpl setRegion(String region)
    {
        this.region = region;
        return this;
    }

    public GuildImpl setAfkChannel(@Nullable VoiceChannel afkChannel)
    {
        this.afkChannel = afkChannel;
        return this;
    }

    public GuildImpl setSystemChannel(@Nullable TextChannel systemChannel)
    {
        this.systemChannel = systemChannel;
        return this;
    }

    public GuildImpl setPublicRole(Role publicRole)
    {
        this.publicRole = publicRole;
        return this;
    }

    public GuildImpl setVerificationLevel(VerificationLevel level)
    {
        this.verificationLevel = level;
        this.canSendVerification = false;   //recalc on next send
        return this;
    }

    public GuildImpl setDefaultNotificationLevel(NotificationLevel level)
    {
        this.defaultNotificationLevel = level;
        return this;
    }

    public GuildImpl setRequiredMFALevel(MFALevel level)
    {
        this.mfaLevel = level;
        return this;
    }

    public GuildImpl setExplicitContentLevel(ExplicitContentLevel level)
    {
        this.explicitContentLevel = level;
        return this;
    }

    public GuildImpl setAfkTimeout(Timeout afkTimeout)
    {
        this.afkTimeout = afkTimeout;
        return this;
    }

    // -- Map getters --

    public TLongObjectMap<Category> getCategoriesMap()
    {
        return categoryCache.getMap();
    }

    public TLongObjectMap<TextChannel> getTextChannelsMap()
    {
        return textChannelCache.getMap();
    }

    public TLongObjectMap<VoiceChannel> getVoiceChannelsMap()
    {
        return voiceChannelCache.getMap();
    }

    public TLongObjectMap<Member> getMembersMap()
    {
        return memberCache.getMap();
    }

    public TLongObjectMap<Role> getRolesMap()
    {
        return roleCache.getMap();
    }

    public TLongObjectMap<Emote> getEmoteMap()
    {
        return emoteCache.getMap();
    }

    public TLongObjectMap<JSONObject> getCachedPresenceMap()
    {
        return cachedPresences;
    }


    // -- Object overrides --

    @Override
    public boolean equals(@Nullable Object o)
    {
        if (!(o instanceof GuildImpl))
            return false;
        GuildImpl oGuild = (GuildImpl) o;
        return this == oGuild || this.id == oGuild.id;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(id);
    }

    @Override
    public String toString()
    {
        return "G:" + getName() + '(' + id + ')';
    }
}
