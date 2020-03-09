/*
 * Copyright 2015-2020 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
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

package net.dv8tion.jda.internal.requests.restaction.operator;

import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.Helpers;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FlatMapErrorRestAction<T> extends RestActionOperator<T, T>
{
    private final Predicate<? super Throwable> check;
    private final Function<? super Throwable, ? extends RestAction<? extends T>> map;

    public FlatMapErrorRestAction(RestAction<T> action, Predicate<? super Throwable> check, Function<? super Throwable, ? extends RestAction<? extends T>> map)
    {
        super(action);
        this.check = check;
        this.map = map;
    }

    @Override
    public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure)
    {
        Consumer<? super Throwable> contextFailure = contextWrap(failure);
        action.queue(success, contextWrap((error) -> {
            try
            {
                if (check.test(error))
                {
                    // If check passed we can apply the fallback function and flatten it
                    RestAction<? extends T> then = map.apply(error);
                    if (then == null)
                        doFailure(failure, new IllegalStateException("FlatMapError operand is null", error)); // No contextFailure because error already has context
                    else
                        then.queue(success, contextFailure); // Use contextFailure here to apply new context to new errors
                }
                else doFailure(failure, error); // No contextFailure because error already has context
            }
            catch (Throwable e)
            {
                doFailure(failure, Helpers.appendCause(e, error)); // No contextFailure because error already has context
            }
        }));
    }

    @Override
    public T complete(boolean shouldQueue) throws RateLimitedException
    {
        try
        {
            return action.complete(shouldQueue);
        }
        catch (Throwable error)
        {
            try
            {
                if (check.test(error))
                {
                    RestAction<? extends T> then = map.apply(error);
                    if (then == null)
                        throw new IllegalStateException("FlatMapError operand is null", error);
                    return then.complete(shouldQueue);
                }
            }
            catch (Throwable e)
            {
                if (e instanceof IllegalStateException && e.getCause() == error)
                    throw (IllegalStateException) e;
                fail(Helpers.appendCause(e, error));
            }
            fail(error);
        }
        throw new AssertionError("Unreachable");
    }

    @Nonnull
    @Override
    public CompletableFuture<T> submit(boolean shouldQueue)
    {
        //TODO: Figure out how to propagate cancel without making a custom class
        CompletableFuture<T> future = new CompletableFuture<>();
        action.submit(shouldQueue).whenComplete((value, error) -> {
            if (error != null)
            {
                error = error instanceof CompletionException && error.getCause() != null ? error.getCause() : error;
                if (!check.test(error))
                {
                    future.completeExceptionally(error);
                    return;
                }

                RestAction<? extends T> then = map.apply(error.getCause());
                if (then == null)
                    future.completeExceptionally(new IllegalStateException("FlatMapError operand is null", error.getCause()));
                else then.submit(shouldQueue).whenComplete((s, t) -> {
                    if (t instanceof CompletionException && t.getCause() != null) future.completeExceptionally(t.getCause());
                    else if (t != null) future.completeExceptionally(t);
                    else future.complete(s);
                });
            }
            else future.complete(value);
        });
        return future;
    }

    @Contract("_ -> fail")
    private void fail(Throwable error) throws RateLimitedException
    {
        if (error instanceof RuntimeException)
            throw (RuntimeException) error;
        else if (error instanceof Error)
            throw (Error) error;
        else if (error instanceof RateLimitedException)
            throw (RateLimitedException) error;
        else
            throw new RuntimeException(error);
    }
}
