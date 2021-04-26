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

package net.dv8tion.jda.api.requests.restaction;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.Helpers;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public interface CommandEditAction extends RestAction<Command>
{
    @Nonnull
    @Override
    @CheckReturnValue
    CommandEditAction setCheck(@Nullable BooleanSupplier checks);

    @Nonnull
    @Override
    @CheckReturnValue
    CommandEditAction addCheck(@Nonnull BooleanSupplier checks);

    @Nonnull
    @Override
    @CheckReturnValue
    CommandEditAction timeout(long timeout, @Nonnull TimeUnit unit);

    @Nonnull
    @Override
    @CheckReturnValue
    CommandEditAction deadline(long timestamp);

    @Nonnull
    @CheckReturnValue
    CommandEditAction apply(@Nonnull CommandData commandData);

    @Nonnull
    @CheckReturnValue
    CommandEditAction setName(@Nullable String name);

    @Nonnull
    @CheckReturnValue
    CommandEditAction setDescription(@Nullable String description);

    @Nonnull
    @CheckReturnValue
    CommandEditAction clearOptions();

    @Nonnull
    @CheckReturnValue
    CommandEditAction addOption(@Nonnull String name, @Nonnull String description, @Nonnull OptionType type, @Nonnull Consumer<? super CommandCreateAction.OptionBuilder> builder);

    @Nonnull
    @CheckReturnValue
    default CommandEditAction addOption(@Nonnull String name, @Nonnull String description, @Nonnull OptionType type)
    {
        return addOption(name, description, type, Helpers.emptyConsumer());
    }
}
