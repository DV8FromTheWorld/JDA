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

package net.dv8tion.jda.api.entities.data;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.data.id.MemberId;
import net.dv8tion.jda.api.entities.data.light.LightMemberData;
import net.dv8tion.jda.api.entities.data.provider.DataProvider;
import net.dv8tion.jda.api.entities.data.rich.RichMemberData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface MutableMemberData extends MemberData
{
    DataProvider<MemberId, MutableMemberData> LIGHT_PROVIDER = (id, flags) -> LightMemberData.SINGLETON;
    DataProvider<MemberId, MutableMemberData> RICH_PROVIDER = (id, flags) -> new RichMemberData(flags);

    void setNickname(@Nullable String nickname);
    void setTimeJoined(long time);
    void setTimeBoosted(long time);
    void setActivities(@Nonnull List<Activity> activities);
    void setOnlineStatus(@Nonnull OnlineStatus status);
    void setOnlineStatus(@Nonnull ClientType type, @Nonnull OnlineStatus status);

}
