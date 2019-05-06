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
package net.dv8tion.jda.api.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.GuildController;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MemberAction;
import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.api.requests.restaction.pagination.PaginationAction;
import net.dv8tion.jda.api.utils.cache.MemberCacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
import net.dv8tion.jda.internal.requests.EmptyRestAction;
import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a Discord {@link net.dv8tion.jda.api.entities.Guild Guild}.
 * This should contain all information provided from Discord about a Guild.
 */
public interface Guild extends ISnowflake
{
    /**
     * Retrieves the available regions for this Guild
     * <br>Shortcut for {@link #retrieveRegions(boolean) retrieveRegions(true)}
     * <br>This will include deprecated voice regions by default.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type {@link java.util.EnumSet EnumSet}
     */
    @Nonnull
    @CheckReturnValue
    default RestAction<EnumSet<Region>> retrieveRegions()
    {
        return retrieveRegions(true);
    }

    /**
     * Retrieves the available regions for this Guild
     *
     * @param  includeDeprecated
     *         Whether to include deprecated regions
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type {@link java.util.EnumSet EnumSet}
     */
    @Nonnull
    @CheckReturnValue
    RestAction<EnumSet<Region>> retrieveRegions(boolean includeDeprecated);

    /**
     * Adds the user represented by the provided id to this guild.
     * <br>This requires an <b>OAuth2 Access Token</b> with the scope {@code guilds.join}.
     *
     * @param  accessToken
     *         The access token
     * @param  userId
     *         The user id
     *
     * @throws IllegalArgumentException
     *         If the user id or access token is blank, empty, or null,
     *         or if the provided user is already in this guild
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have {@link net.dv8tion.jda.api.Permission#CREATE_INSTANT_INVITE Permission.CREATE_INSTANT_INVITE}
     *
     * @return {@link MemberAction MemberAction}
     *
     * @see    <a href="https://discordapp.com/developers/docs/topics/oauth2" target="_blank">Discord OAuth2 Documentation</a>
     *
     * @since  3.7.0
     */
    @Nonnull
    @CheckReturnValue
    MemberAction addMember(@Nonnull String accessToken, @Nonnull String userId);

    /**
     * Adds the provided user to this guild.
     * <br>This requires an <b>OAuth2 Access Token</b> with the scope {@code guilds.join}.
     *
     * @param  accessToken
     *         The access token
     * @param  user
     *         The user
     *
     * @throws IllegalArgumentException
     *         If the user or access token is blank, empty, or null,
     *         or if the provided user is already in this guild
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have {@link net.dv8tion.jda.api.Permission#CREATE_INSTANT_INVITE Permission.CREATE_INSTANT_INVITE}
     *
     * @return {@link MemberAction MemberAction}
     *
     * @see    <a href="https://discordapp.com/developers/docs/topics/oauth2" target="_blank">Discord OAuth2 Documentation</a>
     *
     * @since  3.7.0
     */
    @Nonnull
    @CheckReturnValue
    default MemberAction addMember(@Nonnull String accessToken, @Nonnull User user)
    {
        Checks.notNull(user, "User");
        return addMember(accessToken, user.getId());
    }

    /**
     * Adds the user represented by the provided id to this guild.
     * <br>This requires an <b>OAuth2 Access Token</b> with the scope {@code guilds.join}.
     *
     * @param  accessToken
     *         The access token
     * @param  userId
     *         The user id
     *
     * @throws IllegalArgumentException
     *         If the user id or access token is blank, empty, or null,
     *         or if the provided user is already in this guild
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have {@link net.dv8tion.jda.api.Permission#CREATE_INSTANT_INVITE Permission.CREATE_INSTANT_INVITE}
     *
     * @return {@link MemberAction MemberAction}
     *
     * @see    <a href="https://discordapp.com/developers/docs/topics/oauth2" target="_blank">Discord OAuth2 Documentation</a>
     *
     * @since  3.7.0
     */
    @Nonnull
    @CheckReturnValue
    default MemberAction addMember(@Nonnull String accessToken, long userId)
    {
        return addMember(accessToken, Long.toUnsignedString(userId));
    }

    /**
     * The human readable name of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <p>
     * This value can be modified using {@link GuildManager#setName(String)}.
     *
     * @return Never-null String containing the Guild's name.
     */
    @Nonnull
    String getName();

    /**
     * The Discord hash-id of the {@link net.dv8tion.jda.api.entities.Guild Guild} icon image.
     * If no icon has been set, this returns {@code null}.
     * <p>
     * The Guild icon can be modified using {@link GuildManager#setIcon(Icon)}.
     *
     * @return Possibly-null String containing the Guild's icon hash-id.
     */
    @Nullable
    String getIconId();

    /**
     * The URL of the {@link net.dv8tion.jda.api.entities.Guild Guild} icon image.
     * If no icon has been set, this returns {@code null}.
     * <p>
     * The Guild icon can be modified using {@link GuildManager#setIcon(Icon)}.
     *
     * @return Possibly-null String containing the Guild's icon URL.
     */
    @Nullable
    String getIconUrl();

    /**
     * The Features of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <p>
     * <b>Possible known features:</b>
     * <ul>
     *     <li>VIP_REGIONS - Guild has VIP voice regions</li>
     *     <li>VANITY_URL - Guild a vanity URL (custom invite link). See {@link #retrieveVanityUrl()}</li>
     *     <li>INVITE_SPLASH - Guild has custom invite splash. See {@link #getSplashId()} and {@link #getSplashUrl()}</li>
     *     <li>VERIFIED - Guild is "verified"</li>
     *     <li>MORE_EMOJI - Guild is able to use more than 50 emoji</li>
     * </ul>
     *
     * @return Never-null, unmodifiable Set containing all of the Guild's features.
     */
    @Nonnull
    Set<String> getFeatures();

    /**
     * The Discord hash-id of the splash image for this Guild. A Splash image is an image displayed when viewing a
     * Discord Guild Invite on the web or in client just before accepting or declining the invite.
     * If no splash has been set, this returns {@code null}.
     * <br>Splash images are VIP/Partner Guild only.
     * <p>
     * The Guild splash can be modified using {@link GuildManager#setSplash(Icon)}.
     *
     * @return Possibly-null String containing the Guild's splash hash-id
     */
    @Nullable
    String getSplashId();

    /**
     * The URL of the splash image for this Guild. A Splash image is an image displayed when viewing a
     * Discord Guild Invite on the web or in client just before accepting or declining the invite.
     * If no splash has been set, this returns {@code null}.
     * <br>Splash images are VIP/Partner Guild only.
     * <p>
     * The Guild splash can be modified using {@link GuildManager#setSplash(Icon)}.
     *
     * @return Possibly-null String containing the Guild's splash URL.
     */
    @Nullable
    String getSplashUrl();

    /**
     * Gets the vanity url for this Guild. The vanity url is the custom invite code of partnered / official Guilds.
     * The returned String will be the code that can be provided to {@code discord.gg/{code}} to get the invite link.
     * <br>You can check {@link #getFeatures()} to see if this Guild has a vanity url
     * <p>
     * This action requires the {@link net.dv8tion.jda.api.Permission#MANAGE_SERVER MANAGE_SERVER} permission.
     * <p>
     * Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The ban list cannot be fetched due to a permission discrepancy</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>We were removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the logged in account does not have the {@link net.dv8tion.jda.api.Permission#MANAGE_SERVER MANAGE_SERVER} permission.
     * @throws java.lang.IllegalStateException
     *         If the guild doesn't have the VANITY_URL feature
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: String
     *         <br>The vanity url of this server
     *
     * @see    #getFeatures()
     */
    @Nonnull
    @CheckReturnValue
    RestAction<String> retrieveVanityUrl();

    /**
     * Provides the {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} that has been set as the channel
     * which {@link net.dv8tion.jda.api.entities.Member Members} will be moved to after they have been inactive in a
     * {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} for longer than {@link #getAfkTimeout()}.
     * <br>If no channel has been set as the AFK channel, this returns {@code null}.
     * <p>
     * This value can be modified using {@link GuildManager#setAfkChannel(VoiceChannel)}.
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} that is the AFK Channel.
     */
    @Nullable
    VoiceChannel getAfkChannel();

    /**
     * Provides the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} that has been set as the channel
     * which newly joined {@link net.dv8tion.jda.api.entities.Member Members} will be announced in.
     * <br>If no channel has been set as the system channel, this returns {@code null}.
     * <p>
     * This value can be modified using {@link GuildManager#setSystemChannel(TextChannel)}.
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} that is the system Channel.
     */
    @Nullable
    TextChannel getSystemChannel();

    /**
     * The {@link net.dv8tion.jda.api.entities.Member Member} object for the owner of this Guild.
     *
     * <p>Ownership can be transferred using {@link GuildController#transferOwnership(Member)}.
     *
     * @return Member object for the Guild owner.
     *
     * @see    #getOwnerIdLong()
     */
    @Nullable
    Member getOwner();

    /**
     * The ID for the current owner of this guild.
     * <br>This is useful for debugging purposes or as a shortcut.
     *
     * @return The ID for the current owner
     *
     * @see    #getOwner()
     */
    long getOwnerIdLong();

    /**
     * The ID for the current owner of this guild.
     * <br>This is useful for debugging purposes or as a shortcut.
     *
     * @return The ID for the current owner
     *
     * @see    #getOwner()
     */
    @Nonnull
    default String getOwnerId()
    {
        return Long.toUnsignedString(getOwnerIdLong());
    }

    /**
     * The {@link net.dv8tion.jda.api.entities.Guild.Timeout Timeout} set for this Guild representing the amount of time
     * that must pass for a Member to have had no activity in a {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}
     * to be considered AFK. If {@link #getAfkChannel()} is not {@code null} (thus an AFK channel has been set) then Member
     * will be automatically moved to the AFK channel after they have been inactive for longer than the returned Timeout.
     * <br>Default is {@link Timeout#SECONDS_300 300 seconds (5 minutes)}.
     * <p>
     * This value can be modified using {@link GuildManager#setAfkTimeout(net.dv8tion.jda.api.entities.Guild.Timeout)}.
     *
     * @return The {@link net.dv8tion.jda.api.entities.Guild.Timeout Timeout} set for this Guild.
     */
    @Nonnull
    Timeout getAfkTimeout();

    /**
     * The Voice {@link net.dv8tion.jda.api.Region Region} that this Guild is
     * using for audio connections.
     * <br>If the Region is not recognized, returns {@link net.dv8tion.jda.api.Region#UNKNOWN UNKNOWN} but you
     * can still use the {@link #getRegionRaw()} to retrieve the raw name this region has.
     *
     * <p>This value can be modified using {@link GuildManager#setRegion(net.dv8tion.jda.api.Region)}.
     *
     * @return The the audio Region this Guild is using for audio connections. Can return Region.UNKNOWN.
     */
    @Nonnull
    default Region getRegion()
    {
        return Region.fromKey(getRegionRaw());
    }

    /**
     * The raw voice region name that this Guild is using
     * for audio connections.
     * <br>This is resolved to an enum constant of {@link net.dv8tion.jda.api.Region Region} by {@link #getRegion()}!
     *
     * <p>This value can be modified using {@link GuildManager#setRegion(net.dv8tion.jda.api.Region)}.
     *
     * @return Raw region name
     */
    @Nonnull
    String getRegionRaw();

    /**
     * Used to determine if the provided {@link net.dv8tion.jda.api.entities.User User} is a member of this Guild.
     *
     * @param  user
     *         The user to determine whether or not they are a member of this guild.
     *
     * @return True - if this user is present in this guild.
     */
    boolean isMember(@Nonnull User user);

    /**
     * Gets the {@link net.dv8tion.jda.api.entities.Member Member} object of the currently logged in account in this guild.
     * <br>This is basically {@link net.dv8tion.jda.api.JDA#getSelfUser()} being provided to {@link #getMember(User)}.
     *
     * @return The Member object of the currently logged in account.
     */
    @Nonnull
    Member getSelfMember();

    /**
     * Gets the Guild specific {@link net.dv8tion.jda.api.entities.Member Member} object for the provided
     * {@link net.dv8tion.jda.api.entities.User User}.
     * <br>If the user is not in this guild, {@code null} is returned.
     *
     * @param  user
     *         The {@link net.dv8tion.jda.api.entities.User User} which to retrieve a related Member object for.
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided user is null
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.Member Member} for the related {@link net.dv8tion.jda.api.entities.User User}.
     */
    @Nullable
    Member getMember(@Nonnull User user);

    /**
     * Gets a {@link net.dv8tion.jda.api.entities.Member Member} object via the id of the user. The id relates to
     * {@link net.dv8tion.jda.api.entities.User#getId()}, and this method is similar to {@link JDA#getUserById(String)}
     * <br>This is more efficient that using {@link JDA#getUserById(String)} and {@link #getMember(User)}.
     * <br>If no Member in this Guild has the {@code userId} provided, this returns {@code null}.
     *
     * @param  userId
     *         The Discord id of the User for which a Member object is requested.
     *
     * @throws java.lang.NumberFormatException
     *         If the provided {@code id} cannot be parsed by {@link Long#parseLong(String)}
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.Member Member} with the related {@code userId}.
     */
    @Nullable
    default Member getMemberById(@Nonnull String userId)
    {
        return getMemberCache().getElementById(userId);
    }

    /**
     * Gets a {@link net.dv8tion.jda.api.entities.Member Member} object via the id of the user. The id relates to
     * {@link net.dv8tion.jda.api.entities.User#getIdLong()}, and this method is similar to {@link JDA#getUserById(long)}
     * <br>This is more efficient that using {@link JDA#getUserById(long)} and {@link #getMember(User)}.
     * <br>If no Member in this Guild has the {@code userId} provided, this returns {@code null}.
     *
     * @param  userId
     *         The Discord id of the User for which a Member object is requested.
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.Member Member} with the related {@code userId}.
     */
    @Nullable
    default Member getMemberById(long userId)
    {
        return getMemberCache().getElementById(userId);
    }

    /**
     * Searches for a {@link net.dv8tion.jda.api.entities.Member} that has the matching Discord Tag.
     * <br>Format has to be in the form {@code Username#Discriminator} where the
     * username must be between 2 and 32 characters (inclusive) matching the exact casing and the discriminator
     * must be exactly 4 digits.
     * <br>This does not check the {@link net.dv8tion.jda.api.entities.Member#getNickname() nickname} of the member
     * but the username.
     *
     * <p>This only checks users that are in this guild. If a user exists
     * with the tag that is not available in the {@link #getMemberCache() Member-Cache} it will not be detected.
     * <br>Currently Discord does not offer a way to retrieve a user by their discord tag.
     *
     * @param  tag
     *         The Discord Tag in the format {@code Username#Discriminator}
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided tag is null or not in the described format
     *
     * @return The {@link net.dv8tion.jda.api.entities.Member} for the discord tag or null if no member has the provided tag
     *
     * @see    net.dv8tion.jda.api.JDA#getUserByTag(String)
     */
    @Nullable
    default Member getMemberByTag(@Nonnull String tag)
    {
        User user = getJDA().getUserByTag(tag);
        return user == null ? null : getMember(user);
    }

    /**
     * Searches for a {@link net.dv8tion.jda.api.entities.Member} that has the matching Discord Tag.
     * <br>Format has to be in the form {@code Username#Discriminator} where the
     * username must be between 2 and 32 characters (inclusive) matching the exact casing and the discriminator
     * must be exactly 4 digits.
     * <br>This does not check the {@link net.dv8tion.jda.api.entities.Member#getNickname() nickname} of the member
     * but the username.
     *
     * <p>This only checks users that are in this guild. If a user exists
     * with the tag that is not available in the {@link #getMemberCache() Member-Cache} it will not be detected.
     * <br>Currently Discord does not offer a way to retrieve a user by their discord tag.
     *
     * @param  username
     *         The name of the user
     * @param  discriminator
     *         The discriminator of the user
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided arguments are null or not in the described format
     *
     * @return The {@link net.dv8tion.jda.api.entities.Member} for the discord tag or null if no member has the provided tag
     */
    @Nullable
    default Member getMemberByTag(@Nonnull String username, @Nonnull String discriminator)
    {
        User user = getJDA().getUserByTag(username, discriminator);
        return user == null ? null : getMember(user);
    }

    /**
     * A list of all {@link net.dv8tion.jda.api.entities.Member Members} in this Guild.
     * <br>The Members are not provided in any particular order.
     *
     * <p>This copies the backing store into a list. This means every call
     * creates a new list with O(n) complexity. It is recommended to store this into
     * a local variable or use {@link #getMemberCache()} and use its more efficient
     * versions of handling these values.
     *
     * @return Immutable list of all members in this Guild.
     */
    @Nonnull
    default List<Member> getMembers()
    {
        return getMemberCache().asList();
    }

    /**
     * Gets a list of all {@link net.dv8tion.jda.api.entities.Member Members} who have the same name as the one provided.
     * <br>This compares against {@link net.dv8tion.jda.api.entities.Member#getUser()}{@link net.dv8tion.jda.api.entities.User#getName() .getName()}
     * <br>If there are no {@link net.dv8tion.jda.api.entities.Member Members} with the provided name, then this returns an empty list.
     *
     * @param  name
     *         The name used to filter the returned Members.
     * @param  ignoreCase
     *         Determines if the comparison ignores case when comparing. True - case insensitive.
     *
     * @return Possibly-empty immutable list of all Members with the same name as the name provided.
     */
    @Nonnull
    default List<Member> getMembersByName(@Nonnull String name, boolean ignoreCase)
    {
        return getMemberCache().getElementsByUsername(name, ignoreCase);
    }

    /**
     * Gets a list of all {@link net.dv8tion.jda.api.entities.Member Members} who have the same nickname as the one provided.
     * <br>This compares against {@link Member#getNickname()}. If a Member does not have a nickname, the comparison results as false.
     * <br>If there are no {@link net.dv8tion.jda.api.entities.Member Members} with the provided name, then this returns an empty list.
     *
     * @param  nickname
     *         The nickname used to filter the returned Members.
     * @param  ignoreCase
     *         Determines if the comparison ignores case when comparing. True - case insensitive.
     *
     * @return Possibly-empty immutable list of all Members with the same nickname as the nickname provided.
     */
    @Nonnull
    default List<Member> getMembersByNickname(@Nullable String nickname, boolean ignoreCase)
    {
        return getMemberCache().getElementsByNickname(nickname, ignoreCase);
    }

    /**
     * Gets a list of all {@link net.dv8tion.jda.api.entities.Member Members} who have the same effective name as the one provided.
     * <br>This compares against {@link net.dv8tion.jda.api.entities.Member#getEffectiveName()}}.
     * <br>If there are no {@link net.dv8tion.jda.api.entities.Member Members} with the provided name, then this returns an empty list.
     *
     *
     * @param  name
     *         The name used to filter the returned Members.
     * @param  ignoreCase
     *         Determines if the comparison ignores case when comparing. True - case insensitive.
     *
     * @return Possibly-empty immutable list of all Members with the same effective name as the name provided.
     */
    @Nonnull
    default List<Member> getMembersByEffectiveName(@Nonnull String name, boolean ignoreCase)
    {
        return getMemberCache().getElementsByName(name, ignoreCase);
    }

    /**
     * Gets a list of {@link net.dv8tion.jda.api.entities.Member Members} that have all
     * {@link net.dv8tion.jda.api.entities.Role Roles} provided.
     * <br>If there are no {@link net.dv8tion.jda.api.entities.Member Members} with all provided roles, then this returns an empty list.
     *
     * @param  roles
     *         The {@link net.dv8tion.jda.api.entities.Role Roles} that a {@link net.dv8tion.jda.api.entities.Member Member}
     *         must have to be included in the returned list.
     *
     * @throws java.lang.IllegalArgumentException
     *         If a provided {@link net.dv8tion.jda.api.entities.Role Role} is from a different guild or null.
     *
     * @return Possibly-empty immutable list of Members with all provided Roles.
     */
    @Nonnull
    default List<Member> getMembersWithRoles(@Nonnull Role... roles)
    {
        return getMemberCache().getElementsWithRoles(roles);
    }

    /**
     * Gets a list of {@link net.dv8tion.jda.api.entities.Member Members} that have all provided
     * {@link net.dv8tion.jda.api.entities.Role Roles}.
     * <br>If there are no {@link net.dv8tion.jda.api.entities.Member Members} with all provided roles, then this returns an empty list.
     *
     * @param  roles
     *         The {@link net.dv8tion.jda.api.entities.Role Roles} that a {@link net.dv8tion.jda.api.entities.Member Member}
     *         must have to be included in the returned list.
     *
     * @throws java.lang.IllegalArgumentException
     *         If a provided {@link net.dv8tion.jda.api.entities.Role Role} is from a different guild or null.
     *
     * @return Possibly-empty immutable list of Members with all provided Roles.
     */
    @Nonnull
    default List<Member> getMembersWithRoles(@Nonnull Collection<Role> roles)
    {
        return getMemberCache().getElementsWithRoles(roles);
    }

    /**
     * {@link net.dv8tion.jda.api.utils.cache.MemberCacheView MemberCacheView} for all cached
     * {@link net.dv8tion.jda.api.entities.Member Members} of this Guild.
     *
     * @return {@link net.dv8tion.jda.api.utils.cache.MemberCacheView MemberCacheView}
     */
    @Nonnull
    MemberCacheView getMemberCache();

    /**
     * Gets the {@link net.dv8tion.jda.api.entities.Category Category} from this guild that matches the provided id.
     * This method is similar to {@link net.dv8tion.jda.api.JDA#getCategoryById(String)}, but it only checks in this
     * specific Guild. <br>If there is no matching {@link net.dv8tion.jda.api.entities.Category Category} this returns
     * {@code null}.
     *
     * @param id
     *         The snowflake ID of the wanted Category
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.Category Category} for the provided ID.
     * @throws java.lang.IllegalArgumentException
     *         If the provided ID is not a valid {@code long}
     */
    @Nullable
    default Category getCategoryById(@Nonnull String id)
    {
        return getCategoryCache().getElementById(id);
    }

    /**
     * Gets the {@link net.dv8tion.jda.api.entities.Category Category} from this guild that matches the provided id.
     * This method is similar to {@link net.dv8tion.jda.api.JDA#getCategoryById(String)}, but it only checks in this
     * specific Guild. <br>If there is no matching {@link net.dv8tion.jda.api.entities.Category Category} this returns
     * {@code null}.
     *
     * @param id
     *         The snowflake ID of the wanted Category
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.Category Category} for the provided ID.
     */
    @Nullable
    default Category getCategoryById(long id)
    {
        return getCategoryCache().getElementById(id);
    }

    /**
     * Gets all {@link net.dv8tion.jda.api.entities.Category Categories} in this {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <br>The returned categories will be sorted according to their position.
     *
     * <p>This copies the backing store into a list. This means every call
     * creates a new list with O(n) complexity. It is recommended to store this into
     * a local variable or use {@link #getCategoryCache()} and use its more efficient
     * versions of handling these values.
     *
     * @return An immutable list of all {@link net.dv8tion.jda.api.entities.Category Categories} in this Guild.
     */
    @Nonnull
    default List<Category> getCategories()
    {
        return getCategoryCache().asList();
    }

    /**
     * Gets a list of all {@link net.dv8tion.jda.api.entities.Category Categories} in this Guild that have the same
     * name as the one provided. <br>If there are no matching categories this will return an empty list.
     *
     * @param name
     *         The name to check
     * @param ignoreCase
     *         Whether to ignore case on name checking
     * @return Immutable list of all categories matching the provided name
     * @throws java.lang.IllegalArgumentException
     *         If the provided name is {@code null}
     */
    @Nonnull
    default List<Category> getCategoriesByName(@Nonnull String name, boolean ignoreCase)
    {
        return getCategoryCache().getElementsByName(name, ignoreCase);
    }

    /**
     * Sorted {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView} of
     * all cached {@link net.dv8tion.jda.api.entities.Category Categories} of this Guild.
     * <br>Categories are sorted according to their position.
     *
     * @return Sorted {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView}
     */
    @Nonnull
    SortedSnowflakeCacheView<Category> getCategoryCache();

    /**
     * Gets a {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} from this guild that has the same id as the
     * one provided. This method is similar to {@link net.dv8tion.jda.api.JDA#getTextChannelById(String)}, but it only
     * checks this specific Guild for a TextChannel.
     * <br>If there is no {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} with an id that matches the provided
     * one, then this returns {@code null}.
     *
     * @param  id
     *         The id of the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
     *
     * @throws java.lang.NumberFormatException
     *         If the provided {@code id} cannot be parsed by {@link Long#parseLong(String)}
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} with matching id.
     */
    @Nullable
    default TextChannel getTextChannelById(@Nonnull String id)
    {
        return getTextChannelCache().getElementById(id);
    }

    /**
     * Gets a {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} from this guild that has the same id as the
     * one provided. This method is similar to {@link net.dv8tion.jda.api.JDA#getTextChannelById(long)}, but it only
     * checks this specific Guild for a TextChannel.
     * <br>If there is no {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} with an id that matches the provided
     * one, then this returns {@code null}.
     *
     * @param  id
     *         The id of the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} with matching id.
     */
    @Nullable
    default TextChannel getTextChannelById(long id)
    {
        return getTextChannelCache().getElementById(id);
    }

    /**
     * Gets all {@link net.dv8tion.jda.api.entities.TextChannel TextChannels} in this {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <br>The channels returned will be sorted according to their position.
     *
     * <p>This copies the backing store into a list. This means every call
     * creates a new list with O(n) complexity. It is recommended to store this into
     * a local variable or use {@link #getTextChannelCache()} and use its more efficient
     * versions of handling these values.
     *
     * @return An immutable List of all {@link net.dv8tion.jda.api.entities.TextChannel TextChannels} in this Guild.
     */
    @Nonnull
    default List<TextChannel> getTextChannels()
    {
        return getTextChannelCache().asList();
    }

    /**
     * Gets a list of all {@link net.dv8tion.jda.api.entities.TextChannel TextChannels} in this Guild that have the same
     * name as the one provided.
     * <br>If there are no {@link net.dv8tion.jda.api.entities.TextChannel TextChannels} with the provided name, then this returns an empty list.
     *
     * @param  name
     *         The name used to filter the returned {@link net.dv8tion.jda.api.entities.TextChannel TextChannels}.
     * @param  ignoreCase
     *         Determines if the comparison ignores case when comparing. True - case insensitive.
     *
     * @return Possibly-empty immutable list of all TextChannels names that match the provided name.
     */
    @Nonnull
    default List<TextChannel> getTextChannelsByName(@Nonnull String name, boolean ignoreCase)
    {
        return getTextChannelCache().getElementsByName(name, ignoreCase);
    }

    /**
     * Sorted {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView} of
     * all cached {@link net.dv8tion.jda.api.entities.TextChannel TextChannels} of this Guild.
     * <br>TextChannels are sorted according to their position.
     *
     * @return Sorted {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView}
     */
    @Nonnull
    SortedSnowflakeCacheView<TextChannel> getTextChannelCache();

    /**
     * Gets a {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} from this guild that has the same id as the
     * one provided. This method is similar to {@link net.dv8tion.jda.api.JDA#getVoiceChannelById(String)}, but it only
     * checks this specific Guild for a VoiceChannel.
     * <br>If there is no {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} with an id that matches the provided
     * one, then this returns {@code null}.
     *
     * @param  id
     *         The id of the {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
     *
     * @throws java.lang.NumberFormatException
     *         If the provided {@code id} cannot be parsed by {@link Long#parseLong(String)}
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} with matching id.
     */
    @Nullable
    default VoiceChannel getVoiceChannelById(@Nonnull String id)
    {
        return getVoiceChannelCache().getElementById(id);
    }

    /**
     * Gets a {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} from this guild that has the same id as the
     * one provided. This method is similar to {@link net.dv8tion.jda.api.JDA#getVoiceChannelById(long)}, but it only
     * checks this specific Guild for a VoiceChannel.
     * <br>If there is no {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} with an id that matches the provided
     * one, then this returns {@code null}.
     *
     * @param  id
     *         The id of the {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} with matching id.
     */
    @Nullable
    default VoiceChannel getVoiceChannelById(long id)
    {
        return getVoiceChannelCache().getElementById(id);
    }

    /**
     * Gets all {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannels} in this {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <br>The channels returned will be sorted according to their position.
     *
     * <p>This copies the backing store into a list. This means every call
     * creates a new list with O(n) complexity. It is recommended to store this into
     * a local variable or use {@link #getVoiceChannelCache()} and use its more efficient
     * versions of handling these values.
     *
     * @return An immutable List of {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannels}.
     */
    @Nonnull
    default List<VoiceChannel> getVoiceChannels()
    {
        return getVoiceChannelCache().asList();
    }

    /**
     * Gets a list of all {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannels} in this Guild that have the same
     * name as the one provided.
     * <br>If there are no {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannels} with the provided name, then this returns an empty list.
     *
     * @param  name
     *         The name used to filter the returned {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannels}.
     * @param  ignoreCase
     *         Determines if the comparison ignores case when comparing. True - case insensitive.
     *
     * @return Possibly-empty immutable list of all VoiceChannel names that match the provided name.
     */
    @Nonnull
    default List<VoiceChannel> getVoiceChannelsByName(@Nonnull String name, boolean ignoreCase)
    {
        return getVoiceChannelCache().getElementsByName(name, ignoreCase);
    }

    /**
     * Sorted {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView} of
     * all cached {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannels} of this Guild.
     * <br>VoiceChannels are sorted according to their position.
     *
     * @return Sorted {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView}
     */
    @Nonnull
    SortedSnowflakeCacheView<VoiceChannel> getVoiceChannelCache();

    /**
     * Populated list of {@link GuildChannel channels} for this guild.
     * This includes all types of channels, such as category/voice/text.
     * <br>This includes hidden channels by default.
     *
     * <p>The returned list is ordered in the same fashion as it would be by the official discord client.
     * <ol>
     *     <li>TextChannel without parent</li>
     *     <li>VoiceChannel without parent</li>
     *     <li>Categories
     *         <ol>
     *             <li>TextChannel with category as parent</li>
     *             <li>VoiceChannel with category as parent</li>
     *         </ol>
     *     </li>
     * </ol>
     *
     * @return Immutable list of channels for this guild
     *
     * @see    #getChannels(boolean)
     */
    @Nonnull
    default List<GuildChannel> getChannels()
    {
        return getChannels(true);
    }

    /**
     * Populated list of {@link GuildChannel channels} for this guild.
     * This includes all types of channels, such as category/voice/text.
     *
     * <p>The returned list is ordered in the same fashion as it would be by the official discord client.
     * <ol>
     *     <li>TextChannel without parent</li>
     *     <li>VoiceChannel without parent</li>
     *     <li>Categories
     *         <ol>
     *             <li>TextChannel with category as parent</li>
     *             <li>VoiceChannel with category as parent</li>
     *         </ol>
     *     </li>
     * </ol>
     *
     *
     * @param  includeHidden
     *         Whether to include channels with denied {@link Permission#VIEW_CHANNEL View Channel Permission}
     *
     * @return Immutable list of channels for this guild
     *
     * @see    #getChannels()
     */
    @Nonnull
    List<GuildChannel> getChannels(boolean includeHidden);

    /**
     * Gets a {@link net.dv8tion.jda.api.entities.Role Role} from this guild that has the same id as the
     * one provided.
     * <br>If there is no {@link net.dv8tion.jda.api.entities.Role Role} with an id that matches the provided
     * one, then this returns {@code null}.
     *
     * @param  id
     *         The id of the {@link net.dv8tion.jda.api.entities.Role Role}.
     *
     * @throws java.lang.NumberFormatException
     *         If the provided {@code id} cannot be parsed by {@link Long#parseLong(String)}
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.Role Role} with matching id.
     */
    @Nullable
    default Role getRoleById(@Nonnull String id)
    {
        return getRoleCache().getElementById(id);
    }

    /**
     * Gets a {@link net.dv8tion.jda.api.entities.Role Role} from this guild that has the same id as the
     * one provided.
     * <br>If there is no {@link net.dv8tion.jda.api.entities.Role Role} with an id that matches the provided
     * one, then this returns {@code null}.
     *
     * @param  id
     *         The id of the {@link net.dv8tion.jda.api.entities.Role Role}.
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.Role Role} with matching id.
     */
    @Nullable
    default Role getRoleById(long id)
    {
        return getRoleCache().getElementById(id);
    }

    /**
     * Gets all {@link net.dv8tion.jda.api.entities.Role Roles} in this {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <br>The roles returned will be sorted according to their position. The highest role being at index 0
     * and the lowest at the last index.
     *
     * <p>This copies the backing store into a list. This means every call
     * creates a new list with O(n) complexity. It is recommended to store this into
     * a local variable or use {@link #getRoleCache()} and use its more efficient
     * versions of handling these values.
     *
     * @return An immutable List of {@link net.dv8tion.jda.api.entities.Role Roles}.
     */
    @Nonnull
    default List<Role> getRoles()
    {
        return getRoleCache().asList();
    }

    /**
     * Gets a list of all {@link net.dv8tion.jda.api.entities.Role Roles} in this Guild that have the same
     * name as the one provided.
     * <br>If there are no {@link net.dv8tion.jda.api.entities.Role Roles} with the provided name, then this returns an empty list.
     *
     * @param  name
     *         The name used to filter the returned {@link net.dv8tion.jda.api.entities.Role Roles}.
     * @param  ignoreCase
     *         Determines if the comparison ignores case when comparing. True - case insensitive.
     *
     * @return Possibly-empty immutable list of all Role names that match the provided name.
     */
    @Nonnull
    default List<Role> getRolesByName(@Nonnull String name, boolean ignoreCase)
    {
        return getRoleCache().getElementsByName(name, ignoreCase);
    }

    /**
     * Sorted {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView} of
     * all cached {@link net.dv8tion.jda.api.entities.Role Roles} of this Guild.
     * <br>Roles are sorted according to their position.
     *
     * @return Sorted {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView}
     */
    @Nonnull
    SortedSnowflakeCacheView<Role> getRoleCache();

    /**
     * Gets an {@link net.dv8tion.jda.api.entities.Emote Emote} from this guild that has the same id as the
     * one provided.
     * <br>If there is no {@link net.dv8tion.jda.api.entities.Emote Emote} with an id that matches the provided
     * one, then this returns {@code null}.
     * <br>This will be null if {@link net.dv8tion.jda.api.utils.cache.CacheFlag#EMOTE} is disabled.
     *
     * <p><b>Unicode emojis are not included as {@link net.dv8tion.jda.api.entities.Emote Emote}!</b>
     *
     * @param  id
     *         the emote id
     *
     * @throws java.lang.NumberFormatException
     *         If the provided {@code id} cannot be parsed by {@link Long#parseLong(String)}
     *
     * @return An Emote matching the specified Id.
     */
    @Nullable
    default Emote getEmoteById(@Nonnull String id)
    {
        return getEmoteCache().getElementById(id);
    }

    /**
     * Gets an {@link net.dv8tion.jda.api.entities.Emote Emote} from this guild that has the same id as the
     * one provided.
     * <br>If there is no {@link net.dv8tion.jda.api.entities.Emote Emote} with an id that matches the provided
     * one, then this returns {@code null}.
     * <br>This will be null if {@link net.dv8tion.jda.api.utils.cache.CacheFlag#EMOTE} is disabled.
     *
     * <p><b>Unicode emojis are not included as {@link net.dv8tion.jda.api.entities.Emote Emote}!</b>
     *
     * @param  id
     *         the emote id
     *
     * @return An Emote matching the specified Id.
     */
    @Nullable
    default Emote getEmoteById(long id)
    {
        return getEmoteCache().getElementById(id);
    }

    /**
     * Gets all custom {@link net.dv8tion.jda.api.entities.Emote Emotes} belonging to this {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <br>Emotes are not ordered in any specific way in the returned list.
     *
     * <p><b>Unicode emojis are not included as {@link net.dv8tion.jda.api.entities.Emote Emote}!</b>
     *
     * <p>This copies the backing store into a list. This means every call
     * creates a new list with O(n) complexity. It is recommended to store this into
     * a local variable or use {@link #getEmoteCache()} and use its more efficient
     * versions of handling these values.
     *
     * @return An immutable List of {@link net.dv8tion.jda.api.entities.Emote Emotes}.
     */
    @Nonnull
    default List<Emote> getEmotes()
    {
        return getEmoteCache().asList();
    }

    /**
     * Gets a list of all {@link net.dv8tion.jda.api.entities.Emote Emotes} in this Guild that have the same
     * name as the one provided.
     * <br>If there are no {@link net.dv8tion.jda.api.entities.Emote Emotes} with the provided name, then this returns an empty list.
     * <br>This will be empty if {@link net.dv8tion.jda.api.utils.cache.CacheFlag#EMOTE} is disabled.
     *
     * <p><b>Unicode emojis are not included as {@link net.dv8tion.jda.api.entities.Emote Emote}!</b>
     *
     * @param  name
     *         The name used to filter the returned {@link net.dv8tion.jda.api.entities.Emote Emotes}.
     * @param  ignoreCase
     *         Determines if the comparison ignores case when comparing. True - case insensitive.
     *
     * @return Possibly-empty immutable list of all Role names that match the provided name.
     */
    @Nonnull
    default List<Emote> getEmotesByName(@Nonnull String name, boolean ignoreCase)
    {
        return getEmoteCache().getElementsByName(name, ignoreCase);
    }

    /**
     * {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView} of
     * all cached {@link net.dv8tion.jda.api.entities.Emote Emotes} of this Guild.
     * <br>This will be empty if {@link net.dv8tion.jda.api.utils.cache.CacheFlag#EMOTE} is disabled.
     *
     * @return {@link net.dv8tion.jda.api.utils.cache.SnowflakeCacheView SnowflakeCacheView}
     */
    @Nonnull
    SnowflakeCacheView<Emote> getEmoteCache();

    /**
     * Retrieves a list of emotes together with their respective creators.
     *
     * <p>Note that {@link ListedEmote#getUser()} is only available if the currently
     * logged in account has {@link net.dv8tion.jda.api.Permission#MANAGE_EMOTES Permission.MANAGE_EMOTES}.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: List of {@link net.dv8tion.jda.api.entities.ListedEmote ListedEmote}
     *
     * @since  3.8.0
     */
    @Nonnull
    @CheckReturnValue
    RestAction<List<ListedEmote>> retrieveEmotes();

    /**
     * Retrieves a listed emote together with its respective creator.
     * <br><b>This does not include unicode emoji.</b>
     *
     * <p>Note that {@link ListedEmote#getUser()} is only available if the currently
     * logged in account has {@link net.dv8tion.jda.api.Permission#MANAGE_EMOTES Permission.MANAGE_EMOTES}.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_EMOJI UNKNOWN_EMOJI}
     *     <br>If the provided id does not correspond to an emote in this guild</li>
     * </ul>
     *
     * @param  id
     *         The emote id
     *
     * @throws IllegalArgumentException
     *         If the provided id is not a valid snowflake
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@link net.dv8tion.jda.api.entities.ListedEmote ListedEmote}
     *
     * @since  3.8.0
     */
    @Nonnull
    @CheckReturnValue
    RestAction<ListedEmote> retrieveEmoteById(@Nonnull String id);

    /**
     * Retrieves a listed emote together with its respective creator.
     *
     * <p>Note that {@link ListedEmote#getUser()} is only available if the currently
     * logged in account has {@link net.dv8tion.jda.api.Permission#MANAGE_EMOTES Permission.MANAGE_EMOTES}.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_EMOJI UNKNOWN_EMOJI}
     *     <br>If the provided id does not correspond to an emote in this guild</li>
     * </ul>
     *
     * @param  id
     *         The emote id
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@link net.dv8tion.jda.api.entities.ListedEmote ListedEmote}
     *
     * @since  3.8.0
     */
    @Nonnull
    @CheckReturnValue
    default RestAction<ListedEmote> retrieveEmoteById(long id)
    {
        return retrieveEmoteById(Long.toUnsignedString(id));
    }

    /**
     * Retrieves a listed emote together with its respective creator.
     *
     * <p>Note that {@link ListedEmote#getUser()} is only available if the currently
     * logged in account has {@link net.dv8tion.jda.api.Permission#MANAGE_EMOTES Permission.MANAGE_EMOTES}.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_EMOJI UNKNOWN_EMOJI}
     *     <br>If the provided emote does not correspond to an emote in this guild anymore</li>
     * </ul>
     *
     * @param  emote
     *         The emote
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@link net.dv8tion.jda.api.entities.ListedEmote ListedEmote}
     *
     * @since  3.8.0
     */
    @Nonnull
    @CheckReturnValue
    default RestAction<ListedEmote> retrieveEmote(@Nonnull Emote emote)
    {
        Checks.notNull(emote, "Emote");
        if (emote.getGuild() != null)
            Checks.check(emote.getGuild().equals(this), "Emote must be from the same Guild!");
        if (emote instanceof ListedEmote && !emote.isFake())
        {
            ListedEmote listedEmote = (ListedEmote) emote;
            if (listedEmote.hasUser() || !getSelfMember().hasPermission(Permission.MANAGE_EMOTES))
                return new EmptyRestAction<>(getJDA(), listedEmote);
        }
        return retrieveEmoteById(emote.getId());
    }

    /**
     * Retrieves an unmodifiable list of the currently banned {@link net.dv8tion.jda.api.entities.User Users}.
     * <br>If you wish to ban or unban a user, use either {@link GuildController#ban(User, int) GuildController.ban(User, int)} or
     * {@link GuildController#unban(User) GuildController.unban(User)}.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The ban list cannot be fetched due to a permission discrepancy</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>We were removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the logged in account does not have the {@link net.dv8tion.jda.api.Permission#BAN_MEMBERS} permission.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@literal List<}{@link net.dv8tion.jda.api.entities.Guild.Ban Ban}{@literal >}
     *         <br>An unmodifiable list of all users currently banned from this Guild
     */
    @Nonnull
    @CheckReturnValue
    RestAction<List<Ban>> retrieveBanList();

    /**
     * Retrieves a {@link net.dv8tion.jda.api.entities.Guild.Ban Ban} of the provided ID
     * <br>If you wish to ban or unban a user, use either {@link GuildController#ban(String, int)}  GuildController.ban(id, int)} or
     * {@link GuildController#unban(String)}  GuildController.unban(id)}.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The ban list cannot be fetched due to a permission discrepancy</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>We were removed from the Guild before finishing the task</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_BAN UNKNOWN_BAN}
     *     <br>Either the ban was removed before finishing the task or it did not exist in the first place</li>
     * </ul>
     *
     * @param  userId
     *         the id of the banned user
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the logged in account does not have the {@link net.dv8tion.jda.api.Permission#BAN_MEMBERS} permission.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@link net.dv8tion.jda.api.entities.Guild.Ban Ban}
     *         <br>An unmodifiable ban object for the user banned from this guild
     */
    @Nonnull
    @CheckReturnValue
    default RestAction<Ban> retrieveBanById(long userId)
    {
        return retrieveBanById(Long.toUnsignedString(userId));
    }

    /**
     * Retrieves a {@link net.dv8tion.jda.api.entities.Guild.Ban Ban} of the provided ID
     * <br>If you wish to ban or unban a user, use either {@link GuildController#ban(String, int) GuildController.ban(id, int)} or
     * {@link GuildController#unban(String) GuildController.unban(id)}.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The ban list cannot be fetched due to a permission discrepancy</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>We were removed from the Guild before finishing the task</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_BAN UNKNOWN_BAN}
     *     <br>Either the ban was removed before finishing the task or it did not exist in the first place</li>
     * </ul>
     *
     * @param  userId
     *         the id of the banned user
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the logged in account does not have the {@link net.dv8tion.jda.api.Permission#BAN_MEMBERS} permission.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@link net.dv8tion.jda.api.entities.Guild.Ban Ban}
     *         <br>An unmodifiable ban object for the user banned from this guild
     */
    @Nonnull
    @CheckReturnValue
    RestAction<Ban> retrieveBanById(@Nonnull String userId);

    /**
     * Retrieves a {@link net.dv8tion.jda.api.entities.Guild.Ban Ban} of the provided {@link net.dv8tion.jda.api.entities.User User}
     * <br>If you wish to ban or unban a user, use either {@link GuildController#ban(User, int) GuildController.ban(User, int)} or
     * {@link GuildController#unban(User) GuildController.unban(User)}.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The ban list cannot be fetched due to a permission discrepancy</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>We were removed from the Guild before finishing the task</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_BAN UNKNOWN_BAN}
     *     <br>Either the ban was removed before finishing the task or it did not exist in the first place</li>
     * </ul>
     *
     * @param  bannedUser
     *         the banned user
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the logged in account does not have the {@link net.dv8tion.jda.api.Permission#BAN_MEMBERS} permission.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@link net.dv8tion.jda.api.entities.Guild.Ban Ban}
     *         <br>An unmodifiable ban object for the user banned from this guild
     */
    @Nonnull
    @CheckReturnValue
    default RestAction<Ban> retrieveBan(@Nonnull User bannedUser)
    {
        Checks.notNull(bannedUser, "bannedUser");
        return retrieveBanById(bannedUser.getId());
    }

    /**
     * The method calculates the amount of Members that would be pruned if {@link GuildController#prune(int)} was executed.
     * Prunability is determined by a Member being offline for at least <i>days</i> days.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.api.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The prune count cannot be fetched due to a permission discrepancy</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>We were removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param  days
     *         Minimum number of days since a member has been offline to get affected.
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the account doesn't have {@link net.dv8tion.jda.api.Permission#KICK_MEMBERS KICK_MEMBER} Permission.
     * @throws IllegalArgumentException
     *         If the provided days are less than {@code 1}
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: Integer
     *         <br>The amount of Members that would be affected.
     */
    @Nonnull
    @CheckReturnValue
    RestAction<Integer> retrievePrunableMemberCount(int days);

    /**
     * The @everyone {@link net.dv8tion.jda.api.entities.Role Role} of this {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <br>This role is special because its {@link net.dv8tion.jda.api.entities.Role#getPosition() position} is calculated as
     * {@code -1}. All other role positions are 0 or greater. This implies that the public role is <b>always</b> below
     * any custom roles created in this Guild. Additionally, all members of this guild are implied to have this role so
     * it is not included in the list returned by {@link net.dv8tion.jda.api.entities.Member#getRoles() Member.getRoles()}.
     * <br>The ID of this Role is the Guild's ID thus it is equivalent to using {@link #getRoleById(long) getRoleById(getIdLong())}.
     *
     * @return The @everyone {@link net.dv8tion.jda.api.entities.Role Role}
     */
    @Nonnull
    Role getPublicRole();

    /**
     * The default {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} for a {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <br>This is the channel that the Discord client will default to opening when a Guild is opened for the first time when accepting an invite
     * that is not directed at a specific {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
     *
     * <p>Note: This channel is the first channel in the guild (ordered by position) that the {@link #getPublicRole()}
     * has the {@link net.dv8tion.jda.api.Permission#MESSAGE_READ Permission.MESSAGE_READ} in.
     *
     * @return The {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} representing the default channel for this guild
     */
    @Nullable
    TextChannel getDefaultChannel();

    /**
     * Returns the {@link GuildManager GuildManager} for this Guild, used to modify
     * all properties and settings of the Guild.
     * <br>You modify multiple fields in one request by chaining setters before calling {@link net.dv8tion.jda.api.requests.RestAction#queue() RestAction.queue()}.
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have {@link net.dv8tion.jda.api.Permission#MANAGE_SERVER Permission.MANAGE_SERVER}
     *
     * @return The Manager of this Guild
     */
    @Nonnull
    GuildManager getManager();

    /**
     * Returns the {@link net.dv8tion.jda.api.managers.GuildController GuildController} for this Guild. The controller
     * is used to perform all admin style functions in the Guild. A few include: kicking, banning, changing member roles,
     * changing role and channel positions, and more. Checkout the {@link net.dv8tion.jda.api.managers.GuildController GuildController}
     * class for more info.
     *
     * @return The controller for this Guild.
     */
    @Nonnull
    GuildController getController();

    /**
     * A {@link PaginationAction PaginationAction} implementation
     * that allows to {@link Iterable iterate} over all {@link net.dv8tion.jda.api.audit.AuditLogEntry AuditLogEntries} of
     * this Guild.
     * <br>This iterates from the most recent action to the first logged one. (Limit 90 days into history by discord api)
     *
     * <h1>Examples</h1>
     * <pre><code>
     * public boolean isLogged(Guild guild, ActionType type, long targetId)
     * {
     *     for (AuditLogEntry entry : guild.<u>retrieveAuditLogs().cache(false)</u>)
     *     {
     *         if (entry.getType() == type{@literal &&} entry.getTargetIdLong() == targetId)
     *             return true; // The action is logged
     *     }
     *     return false; // nothing found in audit logs
     * }
     *
     * public{@literal List<AuditLogEntry>} getActionsBy(Guild guild, User user)
     * {
     *     return guild.<u>retrieveAuditLogs().cache(false)</u>.stream()
     *         .filter(it{@literal ->} it.getUser().equals(user))
     *         .collect(Collectors.toList()); // collects actions done by user
     * }
     * </code></pre>
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account
     *         does not have the permission {@link net.dv8tion.jda.api.Permission#VIEW_AUDIT_LOGS VIEW_AUDIT_LOGS}
     *
     * @return {@link AuditLogPaginationAction AuditLogPaginationAction}
     */
    @Nonnull
    @CheckReturnValue
    AuditLogPaginationAction retrieveAuditLogs();

    /**
     * Used to leave a Guild. If the currently logged in account is the owner of this guild ({@link net.dv8tion.jda.api.entities.Guild#getOwner()})
     * then ownership of the Guild needs to be transferred to a different {@link net.dv8tion.jda.api.entities.Member Member}
     * before leaving using {@link GuildController#transferOwnership(Member)}.
     *
     * @throws java.lang.IllegalStateException
     *         Thrown if the currently logged in account is the Owner of this Guild.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: {@link java.lang.Void}
     */
    @Nonnull
    @CheckReturnValue
    RestAction<Void> leave();

    /**
     * Used to completely delete a Guild. This can only be done if the currently logged in account is the owner of the Guild.
     * <br>If the account has MFA enabled, use {@link #delete(String)} instead to provide the MFA code.
     *
     * @throws net.dv8tion.jda.api.exceptions.PermissionException
     *         Thrown if the currently logged in account is not the owner of this Guild.
     * @throws java.lang.IllegalStateException
     *         If the currently logged in account has MFA enabled. ({@link net.dv8tion.jda.api.entities.SelfUser#isMfaEnabled()}).
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction} - Type: {@link java.lang.Void}
     */
    @Nonnull
    @CheckReturnValue
    RestAction<Void> delete();

    /**
     * Used to completely delete a guild. This can only be done if the currently logged in account is the owner of the Guild.
     * <br>This method is specifically used for when MFA is enabled on the logged in account {@link SelfUser#isMfaEnabled()}.
     * If MFA is not enabled, use {@link #delete()}.
     *
     * @param  mfaCode
     *         The Multifactor Authentication code generated by an app like
     *         <a href="https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2" target="_blank">Google Authenticator</a>.
     *         <br><b>This is not the MFA token given to you by Discord.</b> The code is typically 6 characters long.
     *
     * @throws net.dv8tion.jda.api.exceptions.PermissionException
     *         Thrown if the currently logged in account is not the owner of this Guild.
     * @throws java.lang.IllegalArgumentException
     *         If the provided {@code mfaCode} is {@code null} or empty when {@link SelfUser#isMfaEnabled()} is true.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction} - Type: {@link java.lang.Void}
     */
    @Nonnull
    @CheckReturnValue
    RestAction<Void> delete(@Nullable String mfaCode);

    /**
     * The {@link net.dv8tion.jda.api.managers.AudioManager AudioManager} that represents the
     * audio connection for this Guild.
     * <br>If no AudioManager exists for this Guild, this will create a new one.
     * <br>This operation is synchronized on all audio managers for this JDA instance,
     * this means that calling getAudioManager() on any other guild while a thread is accessing this method may be locked.
     *
     * @return The AudioManager for this Guild.
     *
     * @see    net.dv8tion.jda.api.JDA#getAudioManagerCache() JDA.getAudioManagerCache()
     */
    @Nonnull
    AudioManager getAudioManager();

    /**
     * Returns the {@link net.dv8tion.jda.api.JDA JDA} instance of this Guild
     *
     * @return the corresponding JDA instance
     */
    @Nonnull
    JDA getJDA();

    /**
     * Retrieves all {@link net.dv8tion.jda.api.entities.Invite Invites} for this guild.
     * <br>Requires {@link net.dv8tion.jda.api.Permission#MANAGE_SERVER MANAGE_SERVER} in this guild.
     * Will throw a {@link net.dv8tion.jda.api.exceptions.InsufficientPermissionException InsufficientPermissionException} otherwise.
     *
     * <p>To get all invites for a {@link GuildChannel GuildChannel}
     * use {@link GuildChannel#retrieveInvites() GuildChannel.retrieveInvites()}
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         if the account does not have {@link net.dv8tion.jda.api.Permission#MANAGE_SERVER MANAGE_SERVER} in this Guild.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: List{@literal <}{@link net.dv8tion.jda.api.entities.Invite Invite}{@literal >}
     *         <br>The list of expanded Invite objects
     *
     * @see     GuildChannel#retrieveInvites()
     */
    @Nonnull
    @CheckReturnValue
    RestAction<List<Invite>> retrieveInvites();

    /**
     * Retrieves all {@link net.dv8tion.jda.api.entities.Webhook Webhooks} for this Guild.
     * <br>Requires {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS MANAGE_WEBHOOKS} in this Guild.
     *
     * <p>To get all webhooks for a specific {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}, use
     * {@link TextChannel#retrieveWebhooks()}
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         if the account does not have {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS MANAGE_WEBHOOKS} in this Guild.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction RestAction} - Type: List{@literal <}{@link net.dv8tion.jda.api.entities.Webhook Webhook}{@literal >}
     *         <br>A list of all Webhooks in this Guild.
     *
     * @see     TextChannel#retrieveWebhooks()
     */
    @Nonnull
    @CheckReturnValue
    RestAction<List<Webhook>> retrieveWebhooks();

    /**
     * A list containing the {@link net.dv8tion.jda.api.entities.GuildVoiceState GuildVoiceState} of every {@link net.dv8tion.jda.api.entities.Member Member}
     * in this {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * <br>This will never return an empty list because if it were empty, that would imply that there are no
     * {@link net.dv8tion.jda.api.entities.Member Members} in this {@link net.dv8tion.jda.api.entities.Guild Guild}, which is
     * impossible.
     *
     * @return Never-empty list containing all the {@link GuildVoiceState GuildVoiceStates} on this {@link net.dv8tion.jda.api.entities.Guild Guild}.
     */
    @Nonnull
    List<GuildVoiceState> getVoiceStates();

    /**
     * Returns the verification-Level of this Guild. Verification level is one of the factors that determines if a Member
     * can send messages in a Guild.
     * <br>For a short description of the different values, see {@link net.dv8tion.jda.api.entities.Guild.VerificationLevel}.
     * <p>
     * This value can be modified using {@link GuildManager#setVerificationLevel(net.dv8tion.jda.api.entities.Guild.VerificationLevel)}.
     *
     * @return The Verification-Level of this Guild.
     */
    @Nonnull
    VerificationLevel getVerificationLevel();

    /**
     * Returns the default message Notification-Level of this Guild. Notification level determines when Members get notification
     * for messages. The value returned is the default level set for any new Members that join the Guild.
     * <br>For a short description of the different values, see {@link net.dv8tion.jda.api.entities.Guild.NotificationLevel NotificationLevel}.
     * <p>
     * This value can be modified using {@link GuildManager#setDefaultNotificationLevel(net.dv8tion.jda.api.entities.Guild.NotificationLevel)}.
     *
     * @return The default message Notification-Level of this Guild.
     */
    @Nonnull
    NotificationLevel getDefaultNotificationLevel();

    /**
     * Returns the level of multifactor authentication required to execute administrator restricted functions in this guild.
     * <br>For a short description of the different values, see {@link net.dv8tion.jda.api.entities.Guild.MFALevel MFALevel}.
     * <p>
     * This value can be modified using {@link GuildManager#setRequiredMFALevel(net.dv8tion.jda.api.entities.Guild.MFALevel)}.
     *
     * @return The MFA-Level required by this Guild.
     */
    @Nonnull
    MFALevel getRequiredMFALevel();

    /**
     * The level of content filtering enabled in this Guild.
     * <br>This decides which messages sent by which Members will be scanned for explicit content.
     *
     * @return {@link net.dv8tion.jda.api.entities.Guild.ExplicitContentLevel ExplicitContentLevel} for this Guild
     */
    @Nonnull
    ExplicitContentLevel getExplicitContentLevel();

    /**
     * Checks if the current Verification-level of this guild allows JDA to send messages to it.
     *
     * @return True if Verification-level allows sending of messages, false if not.
     *
     * @see    net.dv8tion.jda.api.entities.Guild.VerificationLevel
     *         VerificationLevel Enum with a list of possible verification-levels and their requirements
     */
    boolean checkVerification();

    /**
     * Returns whether or not this Guild is available. A Guild can be unavailable, if the Discord server has problems.
     * <br>If a Guild is unavailable, no actions on it can be performed (Messages, Manager,...)
     *
     * @return If the Guild is available
     */
    boolean isAvailable();

    /**
     * Represents the idle time allowed until a user is moved to the
     * AFK {@link net.dv8tion.jda.api.entities.VoiceChannel} if one is set
     * ({@link net.dv8tion.jda.api.entities.Guild#getAfkChannel() Guild.getAfkChannel()}).
     */
    enum Timeout
    {
        SECONDS_60(60),
        SECONDS_300(300),
        SECONDS_900(900),
        SECONDS_1800(1800),
        SECONDS_3600(3600);

        private final int seconds;

        Timeout(int seconds)
        {
            this.seconds = seconds;
        }

        /**
         * The amount of seconds represented by this {@link Timeout}.
         *
         * @return An positive non-negative int representing the timeout amount in seconds.
         */
        public int getSeconds()
        {
            return seconds;
        }

        /**
         * Retrieves the {@link net.dv8tion.jda.api.entities.Guild.Timeout Timeout} based on the amount of seconds requested.
         * <br>If the {@code seconds} amount provided is not valid for Discord, an IllegalArgumentException will be thrown.
         *
         * @param  seconds
         *         The amount of seconds before idle timeout.
         *
         * @throws java.lang.IllegalArgumentException
         *         If the provided {@code seconds} is an invalid timeout amount.
         *
         * @return The {@link net.dv8tion.jda.api.entities.Guild.Timeout Timeout} related to the amount of seconds provided.
         */
        @Nonnull
        public static Timeout fromKey(int seconds)
        {
            for (Timeout t : values())
            {
                if (t.getSeconds() == seconds)
                    return t;
            }
            throw new IllegalArgumentException("Provided key was not recognized. Seconds: " + seconds);
        }
    }

    /**
     * Represents the Verification-Level of the Guild.
     * The Verification-Level determines what requirement you have to meet to be able to speak in this Guild.
     * <p>
     * <br><b>None</b>      {@literal ->} everyone can talk.
     * <br><b>Low</b>       {@literal ->} verified email required.
     * <br><b>Medium</b>    {@literal ->} you have to be member of discord for at least 5min.
     * <br><b>High</b>      {@literal ->} you have to be member of this guild for at least 10min.
     * <br><b>Very High</b> {@literal ->} you must have a verified phone on your discord account.
     */
    enum VerificationLevel
    {
        NONE(0),
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        VERY_HIGH(4),
        UNKNOWN(-1);

        private final int key;

        VerificationLevel(int key)
        {
            this.key = key;
        }

        /**
         * The Discord id key for this Verification Level.
         *
         * @return Integer id key for this VerificationLevel.
         */
        public int getKey()
        {
            return key;
        }

        /**
         * Used to retrieve a {@link net.dv8tion.jda.api.entities.Guild.VerificationLevel VerificationLevel} based
         * on the Discord id key.
         *
         * @param  key
         *         The Discord id key representing the requested VerificationLevel.
         *
         * @return The VerificationLevel related to the provided key, or {@link #UNKNOWN VerificationLevel.UNKNOWN} if the key is not recognized.
         */
        @Nonnull
        public static VerificationLevel fromKey(int key)
        {
            for (VerificationLevel level : VerificationLevel.values())
            {
                if(level.getKey() == key)
                    return level;
            }
            return UNKNOWN;
        }
    }

    /**
     * Represents the Notification-level of the Guild.
     * The Verification-Level determines what messages you receive pings for.
     * <p>
     * <br><b>All_Messages</b>   {@literal ->} Every message sent in this guild will result in a message ping.
     * <br><b>Mentions_Only</b>  {@literal ->} Only messages that specifically mention will result in a ping.
     */
    enum NotificationLevel
    {
        ALL_MESSAGES(0),
        MENTIONS_ONLY(1),
        UNKNOWN(-1);

        private final int key;

        NotificationLevel(int key)
        {
            this.key = key;
        }

        /**
         * The Discord id key used to represent this NotificationLevel.
         *
         * @return Integer id for this NotificationLevel.
         */
        public int getKey()
        {
            return key;
        }

        /**
         * Used to retrieve a {@link net.dv8tion.jda.api.entities.Guild.NotificationLevel NotificationLevel} based
         * on the Discord id key.
         *
         * @param  key
         *         The Discord id key representing the requested NotificationLevel.
         *
         * @return The NotificationLevel related to the provided key, or {@link #UNKNOWN NotificationLevel.UNKNOWN} if the key is not recognized.
         */
        @Nonnull
        public static NotificationLevel fromKey(int key)
        {
            for (NotificationLevel level : values())
            {
                if (level.getKey() == key)
                    return level;
            }
            return UNKNOWN;
        }
    }

    /**
     * Represents the Multifactor Authentication level required by the Guild.
     * <br>The MFA Level restricts administrator functions to account with MFA Level equal to or higher than that set by the guild.
     * <p>
     * <br><b>None</b>             {@literal ->} There is no MFA level restriction on administrator functions in this guild.
     * <br><b>Two_Factor_Auth</b>  {@literal ->} Users must have 2FA enabled on their account to perform administrator functions.
     */
    enum MFALevel
    {
        NONE(0),
        TWO_FACTOR_AUTH(1),
        UNKNOWN(-1);

        private final int key;

        MFALevel(int key)
        {
            this.key = key;
        }

        /**
         * The Discord id key used to represent this MFALevel.
         *
         * @return Integer id for this MFALevel.
         */
        public int getKey()
        {
            return key;
        }

        /**
         * Used to retrieve a {@link net.dv8tion.jda.api.entities.Guild.MFALevel MFALevel} based
         * on the Discord id key.
         *
         * @param  key
         *         The Discord id key representing the requested MFALevel.
         *
         * @return The MFALevel related to the provided key, or {@link #UNKNOWN MFALevel.UNKNOWN} if the key is not recognized.
         */
        @Nonnull
        public static MFALevel fromKey(int key)
        {
            for (MFALevel level : values())
            {
                if (level.getKey() == key)
                    return level;
            }
            return UNKNOWN;
        }
    }

    /**
     * The Explicit-Content-Filter Level of a Guild.
     * <br>This decides whom's messages should be scanned for explicit content.
     */
    enum ExplicitContentLevel
    {
        OFF(0, "Don't scan any messages."),
        NO_ROLE(1, "Scan messages from members without a role."),
        ALL(2, "Scan messages sent by all members."),

        UNKNOWN(-1, "Unknown filter level!");

        private final int key;
        private final String description;

        ExplicitContentLevel(int key, String description)
        {
            this.key = key;
            this.description = description;
        }

        /**
         * The key for this level
         *
         * @return key
         */
        public int getKey()
        {
            return key;
        }

        /**
         * Description of this level in the official Discord Client (as of 5th May, 2017)
         *
         * @return Description for this level
         */
        @Nonnull
        public String getDescription()
        {
            return description;
        }

        @Nonnull
        public static ExplicitContentLevel fromKey(int key)
        {
            for (ExplicitContentLevel level : values())
            {
                if (level.key == key)
                    return level;
            }
            return UNKNOWN;
        }
    }

    /**
     * Represents a Ban object.
     *
     * @see #retrieveBanList()
     * @see <a href="https://discordapp.com/developers/docs/resources/guild#ban-object" target="_blank">Discord Docs: Ban Object</a>
     */
    class Ban
    {
        protected final User user;
        protected final String reason;

        public Ban(User user, String reason)
        {
            this.user = user;
            this.reason = reason;
        }

        /**
         * The {@link net.dv8tion.jda.api.entities.User User} that was banned
         *
         * @return The banned User
         */
        @Nonnull
        public User getUser()
        {
            return user;
        }

        /**
         * The reason why this user was banned
         *
         * @return The reason for this ban, or {@code null}
         */
        @Nullable
        public String getReason()
        {
            return reason;
        }

        @Override
        public String toString()
        {
            return "GuildBan:" + user + (reason == null ? "" : '(' + reason + ')');
        }
    }
}
