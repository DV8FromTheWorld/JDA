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
import net.dv8tion.jda.api.utils.AttachmentOption;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;

public interface InteractionWebhookAction extends WebhookMessageAction
{
    @Nonnull
    @CheckReturnValue
    InteractionWebhookAction setEphemeral(boolean ephemeral);

    @Nonnull
    @Override
    InteractionWebhookAction setContent(@Nullable String content);

    @Nonnull
    @Override
    InteractionWebhookAction setTTS(boolean tts);

    @Nonnull
    @Override
    InteractionWebhookAction addEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds);

    @Nonnull
    @Override
    default InteractionWebhookAction addEmbeds(@Nonnull MessageEmbed embed, @Nonnull MessageEmbed... other)
    {
        return (InteractionWebhookAction) WebhookMessageAction.super.addEmbeds(embed, other);
    }

    @Nonnull
    @Override
    InteractionWebhookAction addFile(@Nonnull String name, @Nonnull InputStream data, @Nonnull AttachmentOption... options);

    @Nonnull
    @Override
    default InteractionWebhookAction addFile(@Nonnull String name, @Nonnull byte[] data, @Nonnull AttachmentOption... options)
    {
        return (InteractionWebhookAction) WebhookMessageAction.super.addFile(name, data, options);
    }

    @Nonnull
    @Override
    default InteractionWebhookAction addFile(@Nonnull String name, @Nonnull File data, @Nonnull AttachmentOption... options)
    {
        return (InteractionWebhookAction) WebhookMessageAction.super.addFile(name, data, options);
    }

    @Nonnull
    @Override
    default InteractionWebhookAction addFile(@Nonnull File file, @Nonnull AttachmentOption... options)
    {
        return (InteractionWebhookAction) WebhookMessageAction.super.addFile(file, options);
    }

    @Nonnull
    @Override
    default InteractionWebhookAction addActionRow(@Nonnull Component... components)
    {
        return (InteractionWebhookAction) WebhookMessageAction.super.addActionRow(components);
    }

    @Nonnull
    @Override
    default InteractionWebhookAction addActionRows(@Nonnull Collection<? extends ActionRow> rows)
    {
        return (InteractionWebhookAction) WebhookMessageAction.super.addActionRows(rows);
    }

    @Nonnull
    @Override
    InteractionWebhookAction addActionRows(@Nonnull ActionRow... rows);
}
