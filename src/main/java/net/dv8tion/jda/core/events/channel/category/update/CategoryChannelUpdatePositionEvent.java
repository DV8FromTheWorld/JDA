/*
 *     Copyright 2015-2017 Austin Keener & Michael Ritter & Florian Spieß
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
package net.dv8tion.jda.core.events.channel.category.update;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.CategoryChannel;

/**
 * <b><u>CategoryChannelUpdatePositionEvent</u></b><br>
 * Fired if a {@link CategoryChannel CategoryChannel}'s position changes.<br>
 * <br>
 * Use: Detect when a CategoryChannel position changes and get it's previous position.
 */
public class CategoryChannelUpdatePositionEvent extends GenericCategoryChannelUpdateEvent
{
    private final int oldPosition;

    public CategoryChannelUpdatePositionEvent(JDA api, long responseNumber, CategoryChannel channel, int oldPosition)
    {
        super(api, responseNumber, channel);
        this.oldPosition = oldPosition;
    }

    public int getOldPosition()
    {
        return oldPosition;
    }
}
