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

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.ActionRow;
import net.dv8tion.jda.api.interactions.Component;
import net.dv8tion.jda.api.interactions.commands.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.AllowedMentions;
import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public interface ReplyAction extends RestAction<InteractionHook>, AllowedMentions<ReplyAction>
{
    @Nonnull
    @CheckReturnValue
    default ReplyAction addEmbeds(@Nonnull MessageEmbed... embeds)
    {
        Checks.noneNull(embeds, "MessageEmbed");
        return addEmbeds(Arrays.asList(embeds));
    }

    @Nonnull
    @CheckReturnValue
    ReplyAction addEmbeds(@Nonnull Collection<MessageEmbed> embeds);

    @Nonnull
    @CheckReturnValue
    default ReplyAction addActionRow(@Nonnull Component... components)
    {
        return addActionRows(ActionRow.of(components));
    }

    @Nonnull
    @CheckReturnValue
    default ReplyAction addActionRows(@Nonnull Collection<? extends ActionRow> rows)
    {
        Checks.noneNull(rows, "ActionRows");
        return addActionRows(rows.toArray(new ActionRow[0]));
    }

    @Nonnull
    @CheckReturnValue
    ReplyAction addActionRows(@Nonnull ActionRow... rows);

    // doesn't support embeds or attachments
    @Nonnull
    @CheckReturnValue
    ReplyAction setEphemeral(boolean ephemeral);

// Currently not supported, sad face
//    @Nonnull
//    @CheckReturnValue
//    default CommandReplyAction addFile(@Nonnull File file, @Nonnull AttachmentOption... options)
//    {
//        Checks.notNull(file, "File");
//        return addFile(file, file.getName(), options);
//    }
//
//    @Nonnull
//    @CheckReturnValue
//    default CommandReplyAction addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options)
//    {
//        try
//        {
//            Checks.notNull(file, "File");
//            Checks.check(file.exists() && file.canRead(), "Provided file either does not exist or cannot be read from!");
//            return addFile(new FileInputStream(file), name, options);
//        }
//        catch (FileNotFoundException e)
//        {
//            throw new IllegalArgumentException(e);
//        }
//    }
//
//    @Nonnull
//    @CheckReturnValue
//    default CommandReplyAction addFile(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options)
//    {
//        Checks.notNull(data, "Data");
//        return addFile(new ByteArrayInputStream(data), name, options);
//    }
//
//    @Nonnull
//    @CheckReturnValue
//    CommandReplyAction addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options);

    @Nonnull
    @Override
    ReplyAction setCheck(@Nullable BooleanSupplier checks);

    @Nonnull
    @Override
    ReplyAction timeout(long timeout, @Nonnull TimeUnit unit);

    @Nonnull
    @Override
    ReplyAction deadline(long timestamp);

    @Nonnull
    ReplyAction setTTS(final boolean isTTS);

    @Nonnull
    ReplyAction setContent(@Nullable final String content);

    enum Flag
    {
        EPHEMERAL(6);

        private final int raw;

        Flag(int offset)
        {
            this.raw = 1 << offset;
        }

        public int getRaw()
        {
            return raw;
        }
    }

    enum ResponseType
    { // TODO: Write better docs
//        /**  */ Unused (this is only for HTTP webhooks)
//        PONG(1),
//        /** ACK a command without sending a message, eating the user's input */
//        ACKNOWLEDGE(2),
//        /** Respond with a message, eating the user's input */
//        CHANNEL_MESSAGE(3),
        /** Respond with a message, showing the user's input */
        CHANNEL_MESSAGE_WITH_SOURCE(4),
        /** ACK a command without sending a message, showing the user's input */
        DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE(5),
        ;
        private final int raw;

        ResponseType(int raw)
        {
            this.raw = raw;
        }

        public int getRaw()
        {
            return raw;
        }
    }
}
