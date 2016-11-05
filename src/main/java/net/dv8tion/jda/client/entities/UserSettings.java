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

package net.dv8tion.jda.client.entities;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;

import java.util.List;
import java.util.Locale;

public interface UserSettings
{

    // TODO: javadoc
    // TODO: Managers

    JDA getJDA();

    OnlineStatus getStatus();
    Locale getLocale();
    DiscordTheme getTheme();

    List<Guild> getGuildPositions();
    List<Guild> getRestrictedGuilds();

    boolean isAllowEmailFriendRequest();
    boolean isConvertEmoticons();
    boolean isDetectPlatformAccounts();
    boolean isDeveloperMode();
    boolean isEnableTTS();
    boolean isShowCurrentGame();
    boolean isRenderEmbeds();
    boolean isMessageDisplayCompact();
    boolean isInlineEmbedMedia();
    boolean isInlineAttachmentMedia();

    enum DiscordTheme
    {

        UNKNOWN(""),
        LIGHT("light"),
        DARK("dark");

        private final String key;

        DiscordTheme(String key)
        {
            this.key = key;
        }

        public static DiscordTheme fromKey(String key)
        {
            for (DiscordTheme theme : values())
            {
                if (theme.key.equals(key))
                    return theme;
            }
            return UNKNOWN;
        }

    }
}
