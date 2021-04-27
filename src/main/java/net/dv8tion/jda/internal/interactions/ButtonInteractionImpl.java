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

package net.dv8tion.jda.internal.interactions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.button.Button;
import net.dv8tion.jda.api.interactions.button.ButtonInteraction;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;

import javax.annotation.Nonnull;

public class ButtonInteractionImpl extends InteractionImpl implements ButtonInteraction
{
    private final String customId;
    private final Message message;
    private final Button button;

    public ButtonInteractionImpl(JDAImpl jda, DataObject data)
    {
        super(jda, data);

        message = jda.getEntityBuilder().createMessage(data.getObject("message"));
        customId = data.getObject("data").getString("custom_id");
        button = message.getButtonById(customId);
    }

    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public MessageChannel getChannel()
    {
        return (MessageChannel) super.getChannel();
    }

    @Nonnull
    @Override
    public String getComponentId()
    {
        return customId;
    }

    @Nonnull
    @Override
    public Message getMessage()
    {
        return message;
    }

    @Nonnull
    @Override
    public Button getButton()
    {
        return button;
    }
}
