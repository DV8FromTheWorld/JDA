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

package net.dv8tion.jda.api.requests.restaction.order;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * An extension of {@link net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction ChannelOrderAction} with
 * similar functionality, but constrained to the bounds of a single {@link net.dv8tion.jda.api.entities.Category Category}.
 * <br>To apply the changes you must finish the {@link net.dv8tion.jda.api.requests.RestAction RestAction}.
 *
 * <p>Before you can use any of the {@code move} methods
 * you must use either {@link #selectPosition(Object) selectPosition(GuildChannel)} or {@link #selectPosition(int)}!
 *
 * @param  <T>
 *         The type of {@link net.dv8tion.jda.api.entities.GuildChannel GuildChannel} to move
 *         using this CategoryOrderAction, either {@link net.dv8tion.jda.api.entities.TextChannel TextChannel},
 *         or {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
 *
 * @author Kaidan Gustave
 */
public class CategoryOrderAction<T extends GuildChannel> extends ChannelOrderAction<T>
{
    protected final Category category;

    /**
     * Creates a new CategoryOrderAction for the specified {@link net.dv8tion.jda.api.entities.Category Category}
     *
     * @param  category
     *         The target {@link net.dv8tion.jda.api.entities.Category Category}
     *         which the new CategoryOrderAction will order channels from.
     * @param  type
     *         The {@link net.dv8tion.jda.api.entities.ChannelType ChannelType} that
     *         matches the returning value of {@link net.dv8tion.jda.api.entities.GuildChannel#getType() GuildChannel#getType()}
     *         for the generic {@link net.dv8tion.jda.api.entities.GuildChannel GuildChannel} type {@code T}.
     *
     * @throws java.lang.IllegalArgumentException
     *         If the {@code ChannelType} is not one that can be retrieved from a {@code Category}.
     *         Currently the only two allowed are {@link ChannelType#TEXT} and {@link ChannelType#VOICE}.
     */
    @SuppressWarnings("unchecked")
    public CategoryOrderAction(Category category, ChannelType type)
    {
        super(category.getGuild(), type, (Collection<T>) getChannelsOfType(category, type));
        this.category = category;
    }

    /**
     * Gets the {@link net.dv8tion.jda.api.entities.Category Category}
     * controlled by this CategoryOrderAction.
     *
     * @return The {@link net.dv8tion.jda.api.entities.Category Category}
     *         of this CategoryOrderAction.
     */
    @Nonnull
    public Category getCategory()
    {
        return category;
    }

    @Override
    protected void validateInput(T entity)
    {
        Checks.notNull(entity, "Provided channel");
        Checks.check(getCategory().equals(entity.getParent()), "Provided channel's Category is not this Category!");
        Checks.check(orderList.contains(entity), "Provided channel is not in the list of orderable channels!");
    }

    @Nonnull
    private static Collection<? extends GuildChannel> getChannelsOfType(Category category, ChannelType type)
    {
        Checks.notNull(type, "ChannelType");
        Checks.notNull(category, "Category");
        // In the event Discord allows a new channel type to be nested in categories,
        // supporting them via CategoryOrderAction is just a matter of adding a new case here.
        switch(type)
        {
            case TEXT:
                return category.getTextChannels();
            case VOICE:
                return category.getVoiceChannels();
            default:
                throw new IllegalArgumentException("Cannot order category with specified channel type " + type);
        }
    }
}
