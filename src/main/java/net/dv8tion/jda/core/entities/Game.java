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
package net.dv8tion.jda.core.entities;

import net.dv8tion.jda.core.utils.Checks;

import java.util.Objects;

/**
 * Represents a Discord {@link net.dv8tion.jda.core.entities.Game Game}.
 * <br>This should contain all information provided from Discord about a Game.
 *
 * @since  2.1
 * @author John A. Grosh
 */
public class Game
{
    protected final String name;
    protected final String url;
    protected final int type;

    protected Game(String name, String url, int type)
    {
        this.name = name;
        this.url = url;
        this.type = type;
    }

    /**
     * Whether this is a <a href="https://discordapp.com/developers/docs/rich-presence/best-practices" target="_blank">Rich Presence</a>
     * <br>If {@code false} the result of {@link #asRichPresence()} is {@code null}
     *
     * @return {@code true} if this is a {@link net.dv8tion.jda.core.entities.RichPresence RichPresence}
     */
    public boolean isRich()
    {
        return false;
    }

    /**
     * {@link net.dv8tion.jda.core.entities.RichPresence RichPresence} representation of
     * this Game.
     *
     * @return RichPresence or {@code null} if {@link #isRich()} returns {@code false}
     */
    public RichPresence asRichPresence()
    {
        return null;
    }

    /**
     * The displayed name of the {@link net.dv8tion.jda.core.entities.Game Game}. If no name has been set, this returns null.
     *
     * @return Possibly-null String containing the Game's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The URL of the {@link net.dv8tion.jda.core.entities.Game Game} if the game is actually a Stream.
     * <br>This will return null for regular games.
     *
     * @return Possibly-null String containing the Game's URL.
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * The type of {@link net.dv8tion.jda.core.entities.Game Game}.
     *
     * @return Never-null {@link net.dv8tion.jda.core.entities.Game.ActivityType ActivityType} representing the type of Game
     */
    public ActivityType getActivityType()
    {
        return ActivityType.fromKey(type);
    }

    /**
     * The raw key for this Game's {@link net.dv8tion.jda.core.entities.Game.ActivityType ActivityType}
     *
     * @return The integer key for this Game's activity type
     */
    public int getActivityTypeRaw()
    {
        return type;
    }

    /**
     * The type of {@link net.dv8tion.jda.core.entities.Game Game}.
     *
     * @return Never-null {@link net.dv8tion.jda.core.entities.Game.GameType GameType} representing the type of Game
     *
     * @deprecated
     *         Use {@link #getActivityType()} instead
     */
    @Deprecated
    public GameType getType()
    {
        return GameType.fromKey(type);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Game))
            return false;
        if (o == this)
            return true;

        Game oGame = (Game) o;
        return oGame.type == type
            && Objects.equals(name, oGame.getName())
            && Objects.equals(url, oGame.getUrl());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, type, url);
    }

    @Override
    public String toString()
    {
        if (url != null)
            return String.format("Game(%s | %s)", name, url);
        else
            return String.format("Game(%s)", name);
    }

    /**
     * Creates a new Game instance with the specified name.
     * <br>In order to appear as "streaming" in the official client you must
     * provide a valid (see documentation of method) streaming URL in {@link #streaming(String, String) Game.streaming(String, String)}.
     *
     * @param  name
     *         The not-null name of the newly created game
     *
     * @throws IllegalArgumentException
     *         if the specified name is null, empty or blank
     *
     * @return A valid Game instance with the provided name with {@link ActivityType#DEFAULT}
     */
    public static Game playing(String name)
    {
        Checks.notBlank(name, "Name");
        return new Game(name, null, ActivityType.DEFAULT.key);
    }

    /**
     * Creates a new Game instance with the specified name and url.
     * <br>The specified URL must be valid according to discord standards in order to display as "streaming" in the official client.
     * A valid streaming URL must be derived from {@code https://twitch.tv/} and can be verified using {@link #isValidStreamingUrl(String)}. (see documentation)
     *
     * @param  name
     *         The not-null name of the newly created game
     * @param  url
     *         The streaming url to use, required to display as "streaming"
     *
     * @throws IllegalArgumentException
     *         If the specified name is null or empty
     *
     * @return A valid Game instance with the provided name and url
     *
     * @see    #isValidStreamingUrl(String)
     */
    public static Game streaming(String name, String url)
    {
        Checks.notEmpty(name, "Provided game name");
        ActivityType type;
        if (isValidStreamingUrl(url))
            type = ActivityType.STREAMING;
        else
            type = ActivityType.DEFAULT;
        return new Game(name, url, type.key);
    }

    /**
     * Creates a new Game instance with the specified name.
     * <br>This will display as {@code Listening name} in the official client
     *
     * @param  name
     *         The not-null name of the newly created game
     *
     * @throws IllegalArgumentException
     *         if the specified name is null, empty or blank
     *
     * @return A valid Game instance with the provided name with {@link ActivityType#LISTENING}
     */
    public static Game listening(String name)
    {
        Checks.notBlank(name, "Name");
        return new Game(name, null, ActivityType.LISTENING.key);
    }

    /**
     * Creates a new Game instance with the specified name.
     * <br>This will display as {@code Watching name} in the official client
     *
     * @param  name
     *         The not-null name of the newly created game
     *
     * @throws IllegalArgumentException
     *         if the specified name is null, empty or blank
     *
     * @return A valid Game instance with the provided name with {@link ActivityType#WATCHING}
     */
    public static Game watching(String name)
    {
        Checks.notBlank(name, "Name");
        return new Game(name, null, ActivityType.WATCHING.key);
    }

    /**
     * Creates a new Game instance with the specified name and url.
     *
     * @param  type
     *         The {@link net.dv8tion.jda.core.entities.Game.ActivityType ActivityType} to use
     * @param  name
     *         The not-null name of the newly created game
     *
     * @throws IllegalArgumentException
     *         If the specified name or type is null or empty
     *
     * @return A valid Game instance with the provided name and url
     */
    public static Game of(ActivityType type, String name)
    {
        return of(type, name, null);
    }

    /**
     * Creates a new Game instance with the specified name and url.
     * <br>The provided url would only be used for {@link net.dv8tion.jda.core.entities.Game.ActivityType#STREAMING GameType.STREAMING}
     * and should be a twitch url.
     *
     * @param  type
     *         The {@link net.dv8tion.jda.core.entities.Game.ActivityType ActivityType} to use
     * @param  name
     *         The not-null name of the newly created game
     * @param  url
     *         The streaming url to use, required to display as "streaming".
     *
     * @throws IllegalArgumentException
     *         If the specified name or type is null or empty
     *
     * @return A valid Game instance with the provided name and url
     *
     * @see    #isValidStreamingUrl(String)
     */
    public static Game of(ActivityType type, String name, String url)
    {
        Checks.notNull(type, "Type");
        switch (type)
        {
            case DEFAULT:
                return playing(name);
            case STREAMING:
                return streaming(name, url);
            case LISTENING:
                return listening(name);
            case WATCHING:
                return watching(name);
            default:
                throw new IllegalArgumentException("GameType " + type + " is not supported!");
        }
    }

    /**
     * Creates a new Game instance with the specified name and url.
     *
     * @param  type
     *         The {@link net.dv8tion.jda.core.entities.Game.GameType GameType} to use
     * @param  name
     *         The not-null name of the newly created game
     *
     * @throws IllegalArgumentException
     *         If the specified name or type is null or empty
     *
     * @return A valid Game instance with the provided name and url
     *
     * @deprecated
     *         Use {@link #of(net.dv8tion.jda.core.entities.Game.ActivityType, String)} instead
     */
    @Deprecated
    public static Game of(GameType type, String name)
    {
        Checks.notNull(type, "GameType");
        return of(ActivityType.fromKey(type.key), name);
    }

    /**
     * Creates a new Game instance with the specified name and url.
     * <br>The provided url would only be used for {@link net.dv8tion.jda.core.entities.Game.GameType#STREAMING GameType.STREAMING}
     * and should be a twitch url.
     *
     * @param  type
     *         The {@link net.dv8tion.jda.core.entities.Game.GameType GameType} to use
     * @param  name
     *         The not-null name of the newly created game
     * @param  url
     *         The streaming url to use, required to display as "streaming".
     *
     * @throws IllegalArgumentException
     *         If the specified name or type is null or empty
     *
     * @return A valid Game instance with the provided name and url
     *
     * @see    #isValidStreamingUrl(String)
     *
     * @deprecated
     *        Use {@link #of(net.dv8tion.jda.core.entities.Game.ActivityType, String, String)} instead
     */
    @Deprecated
    public static Game of(GameType type, String name, String url)
    {
        Checks.notNull(type, "GameType");
        return of(ActivityType.fromKey(type.key), name, url);
    }

    /**
     * Checks if a given String is a valid Twitch url (ie, one that will display "Streaming" on the Discord client).
     *
     * @param  url
     *         The url to check.
     *
     * @return True if the provided url is valid for triggering Discord's streaming status
     */
    public static boolean isValidStreamingUrl(String url)
    {
        return url != null && url.matches("https?://(www\\.)?twitch\\.tv/.+");
    }

    /**
     * The type game being played, differentiating between a game and stream types.
     */
    @Deprecated
    public enum GameType
    {
        /**
         * The GameType used to represent a normal {@link net.dv8tion.jda.core.entities.Game Game} status.
         */
        DEFAULT(0),
        /**
         * Used to indicate that the {@link net.dv8tion.jda.core.entities.Game Game} is a stream
         * <br>This type is displayed as "Streaming" in the discord client.
         */
        STREAMING(1),
        /**
         * Used to indicate that the {@link net.dv8tion.jda.core.entities.Game Game} should display
         * as {@code Listening...} in the official client.
         */
        LISTENING(2),
        /**
         * Used to indicate that the {@link net.dv8tion.jda.core.entities.Game Game} should display
         * as {@code Watching...} in the official client.
         */
        WATCHING(3);

        private final int key;

        GameType(int key)
        {
            this.key = key;
        }

        /**
         * The Discord defined id key for this GameType.
         *
         * @return the id key.
         */
        public int getKey()
        {
            return key;
        }

        /**
         * Gets the GameType related to the provided key.
         * <br>If an unknown key is provided, this returns {@link #DEFAULT}
         *
         * @param  key
         *         The Discord key referencing a GameType.
         *
         * @return The GameType that has the key provided, or {@link #DEFAULT} for unknown key.
         */
        public static GameType fromKey(int key)
        {
            switch (key)
            {
                case 0:
                default:
                    return DEFAULT;
                case 1:
                    return STREAMING;
                case 2:
                    return LISTENING;
                case 3:
                    return WATCHING;
            }
        }
    }

    /**
     * The type of activity, differentiating between a game, stream, and other presence activity types.
     */
    public enum ActivityType
    {
        /**
         * The ActivityType used to represent a normal playing status.
         */
        DEFAULT(0),
        /**
         * Used to indicate that the {@link net.dv8tion.jda.core.entities.Game Game} is a stream
         * <br>This type is displayed as "Streaming" in the discord client.
         */
        STREAMING(1),
        /**
         * Used to indicate that the {@link net.dv8tion.jda.core.entities.Game Game} should display
         * as {@code Listening...} in the official client.
         */
        LISTENING(2),
        /**
         * Used to indicate that the {@link net.dv8tion.jda.core.entities.Game Game} should display
         * as {@code Watching...} in the official client.
         */
        WATCHING(3);

        private final int key;

        ActivityType(int key)
        {
            this.key = key;
        }

        /**
         * The Discord defined id key for this GameType.
         *
         * @return the id key.
         */
        public int getKey()
        {
            return key;
        }

        /**
         * Gets the GameType related to the provided key.
         * <br>If an unknown key is provided, this returns {@link #DEFAULT}
         *
         * @param  key
         *         The Discord key referencing a GameType.
         *
         * @return The GameType that has the key provided, or {@link #DEFAULT} for unknown key.
         */
        public static ActivityType fromKey(int key)
        {
            switch (key)
            {
                case 0:
                default:
                    return DEFAULT;
                case 1:
                    return STREAMING;
                case 2:
                    return LISTENING;
                case 3:
                    return WATCHING;
            }
        }
    }
}
