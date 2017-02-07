/*
 *     Copyright 2015-2016 Austin Keener & Michael Ritter
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
package net.dv8tion.jda.core.entities;


import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.requests.RestAction;

import java.util.Collection;

/**
 * Represents a Discord User.
 * Contains all publicly available information about a specific Discord User.
 */
public interface User extends ISnowflake, IMentionable, IFakeable
{

    /**
     * The username of the {@link net.dv8tion.jda.core.entities.User User}. Length is between 2 and 32 characters (inclusive).
     *
     * @return
     *      Never-null String containing the {@link net.dv8tion.jda.core.entities.User User}'s username.
     */
    String getName();

    /**
     * The discriminator of the {@link net.dv8tion.jda.core.entities.User User}. Used to differentiate between users with the same usernames.<br>
     * This will be important when the friends list is released for human readable searching.<br>
     * Ex: DV8FromTheWorld#9148
     *
     * @return
     *      Never-null String containing the {@link net.dv8tion.jda.core.entities.User User} discriminator.
     */
    String getDiscriminator();

    /**
     * The Discord Id for this user's avatar image.
     * If the user has not set an image, this will return null.
     *
     * @return
     *      Possibly-null String containing the {@link net.dv8tion.jda.core.entities.User User} avatar id.
     */
    String getAvatarId();

    /**
     * The URL for the user's avatar image.
     * If the user has not set an image, this will return null.
     *
     * @return
     *      Possibly-null String containing the {@link net.dv8tion.jda.core.entities.User User} avatar url.
     */
    String getAvatarUrl();

    /**
     * The Discord Id for this user's default avatar image.
     *
     * @return
     *      Never-null String containing the {@link net.dv8tion.jda.core.entities.User User} default avatar id.
     */
    String getDefaultAvatarId();

    /**
     * The URL for the for the user's default avatar image.
     *
     * @return
     *      Never-null String containing the {@link net.dv8tion.jda.core.entities.User User} default avatar url.
     */
    String getDefaultAvatarUrl();

    /**
     * The URL for the user's avatar image
     * If they do not have an avatar set, this will return the URL of their
     * default avatar
     * 
     * @return
     *      Never-null String containing the {@link net.dv8tion.jda.core.entities.User User} effective avatar url.
     */
    String getEffectiveAvatarUrl();
    
    /**
     * Whether or not the currently logged in user and this user have a currently open
     * {@link net.dv8tion.jda.core.entities.PrivateChannel PrivateChannel} or not.
     *
     * @return
     *      True if the logged in account shares a PrivateChannel with this user.
     */
    boolean hasPrivateChannel();

    RestAction<PrivateChannel> openPrivateChannel();

    /**
     * Finds and collects all mutual guilds between the current {@link JDA} instance and the {@link User} instance.
     *
     * @return Collection of mutual {@link Guild} objects.
     */
    Collection<Guild> getMutualGuilds();

    /**
     * Gets the {@link net.dv8tion.jda.core.entities.PrivateChannel PrivateChannel} of this
     * {@link net.dv8tion.jda.core.entities.User User} for use in sending direct messages.
     *
     * @return
     *      Never-null {@link net.dv8tion.jda.core.entities.PrivateChannel PrivateChannel} that is associated with this {@link net.dv8tion.jda.core.entities.User User}.
     */
    PrivateChannel getPrivateChannel();

    /**
     * Returns whether or not the given user is a Bot-Account (special badge in client, some different behaviour)
     *
     * @return
     *      If the User's Account is marked as Bot
     */
    boolean isBot();

    /**
     * Returns the {@link net.dv8tion.jda.core.JDA JDA} instance of this User
     * @return
     *      the corresponding JDA instance
     */
    JDA getJDA();
}
