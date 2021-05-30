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

package net.dv8tion.jda.api.events.channel.category.update;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.UpdateEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Indicates that a {@link net.dv8tion.jda.api.entities.Category Category} was updated.
 * <br>Every category update event derived from this event and can be casted.
 *
 * <p>Can be used to detect any category update event
 */
public abstract class GenericCategoryUpdateEvent<T> extends GenericCategoryEvent implements UpdateEvent<Category, T>
{
    protected final T previous;
    protected final T next;
    protected final String identifier;

    public GenericCategoryUpdateEvent(
        @NotNull JDA api, long responseNumber, @NotNull Category category,
        @Nullable T previous, @Nullable T next, @NotNull String identifier)
    {
        super(api, responseNumber, category);
        this.previous = previous;
        this.next = next;
        this.identifier = identifier;
    }

    @NotNull
    @Override
    public Category getEntity()
    {
        return getCategory();
    }

    @NotNull
    @Override
    public String getPropertyIdentifier()
    {
        return identifier;
    }

    @Nullable
    @Override
    public T getOldValue()
    {
        return previous;
    }

    @Nullable
    @Override
    public T getNewValue()
    {
        return next;
    }

    @Override
    public String toString()
    {
        return "CategoryUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ')';
    }
}
