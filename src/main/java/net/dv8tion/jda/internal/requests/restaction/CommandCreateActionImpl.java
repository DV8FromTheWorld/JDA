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
package net.dv8tion.jda.internal.requests.restaction;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.requests.Request;
import net.dv8tion.jda.api.requests.Response;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.api.utils.data.SerializableData;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.utils.Checks;
import okhttp3.RequestBody;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CommandCreateActionImpl extends RestActionImpl<Command> implements CommandCreateAction
{
    private final Guild guild;
    private CommandData data;

    public CommandCreateActionImpl(JDAImpl api, CommandData command)
    {
        super(api, Route.Interactions.CREATE_COMMAND.compile(api.getSelfUser().getApplicationId()));
        this.guild = null;
        this.data = command;
    }

    public CommandCreateActionImpl(Guild guild, CommandData command)
    {
        super(guild.getJDA(), Route.Interactions.CREATE_GUILD_COMMAND.compile(guild.getJDA().getSelfUser().getApplicationId(), guild.getId()));
        this.guild = guild;
        this.data = command;
    }

    @Nonnull
    @Override
    public CommandCreateAction addCheck(@Nonnull BooleanSupplier checks)
    {
        return (CommandCreateAction) super.addCheck(checks);
    }

    @Nonnull
    @Override
    public CommandCreateAction setCheck(BooleanSupplier checks)
    {
        return (CommandCreateAction) super.setCheck(checks);
    }

    @Nonnull
    @Override
    public CommandCreateAction deadline(long timestamp)
    {
        return (CommandCreateAction) super.deadline(timestamp);
    }

    @Nonnull
    @Override
    public CommandCreateAction timeout(long timeout, @Nonnull TimeUnit unit)
    {
        return (CommandCreateAction) super.timeout(timeout, unit);
    }

    @Nonnull
    @Override
    public CommandCreateAction setName(@Nonnull String name)
    {
        Checks.notEmpty(name, "Name");
        Checks.notLonger(name, 32, "Name");
        Checks.matches(name, Checks.ALPHANUMERIC_WITH_DASH, "Name");
        data.setName(name);
        return this;
    }

    @Nonnull
    @Override
    public CommandCreateAction setDescription(@Nonnull String description)
    {
        Checks.notEmpty(description, "Description");
        Checks.notLonger(description, 100, "Description");
        data.setDescription(description);
        return this;
    }

    @Nonnull
    @Override
    public CommandCreateAction addOption(@Nonnull String name, @Nonnull String description, @Nonnull OptionType type, @Nonnull Consumer<? super OptionBuilder> builder)
    {
        Checks.notEmpty(name, "Name");
        Checks.notEmpty(description, "Description");
        Checks.notLonger(name, 32, "Name");
        Checks.notLonger(description, 100, "Description");
        Checks.matches(name, Checks.ALPHANUMERIC_WITH_DASH, "Name");
        Checks.notNull(type, "Type");
        Checks.notNull(builder, "Builder");
        Option option = new Option(type, name, description);
        builder.accept(option);
        DataObject json = option.toData();
        switch (option.type)
        {
        case SUB_COMMAND:
            data.addSubcommand(SubcommandData.load(json));
            break;
        case SUB_COMMAND_GROUP:
            data.addSubcommandGroup(SubcommandGroupData.load(json));
            break;
        default:
            data.addOption(OptionData.load(json));
            break;
        }
        return this;
    }

    @Override
    public RequestBody finalizeData()
    {
        return getRequestBody(data.toData());
    }

    @Override
    protected void handleSuccess(Response response, Request<Command> request)
    {
        DataObject json = response.getObject();
        request.onSuccess(new Command(api, guild, json));
    }

    protected static class Option implements OptionBuilder, SerializableData
    {
        private final OptionType type;
        private final String name;
        private final String description;
        private final Map<String, Object> choices = new HashMap<>();
        private final List<Option> options = new ArrayList<>();
        private boolean required, isDefault; // TODO: we can do some validation here, maybe?

        protected Option(OptionType type, String name, String description)
        {
            this.type = type;
            this.name = name;
            this.description = description;
        }

        @Override
        public Option setRequired(boolean required)
        {
            this.required = required;
            return this;
        }

        @Override
        public Option setDefault(boolean isDefault)
        {
            this.isDefault = isDefault;
            return this;
        }

        @Override
        public OptionBuilder addChoice(String name, String value)
        {
            Checks.notEmpty(name, "Name");
            Checks.notLonger(name, 100, "Name");
            Checks.notEmpty(value, "Value");
            Checks.notLonger(value, 100, "Value");
            this.choices.put(name, value);
            return this;
        }

        @Override
        public OptionBuilder addChoice(String name, long value)
        {
            Checks.notEmpty(name, "Name");
            Checks.notLonger(name, 100, "Name");
            this.choices.put(name, value);
            return this;
        }

        @Override
        public Option addOption(String name, String description, OptionType type, Consumer<? super OptionBuilder> builder)
        {
            Checks.notEmpty(name, "Name");
            Checks.notLonger(name, 32, "Name");
            Checks.notEmpty(description, "Description");
            Checks.notLonger(description, 100, "Description");
            Checks.notNull(type, "Type");
            Checks.notNull(builder, "Builder");
            switch (this.type)
            {
            case SUB_COMMAND:
                Checks.check(type != OptionType.SUB_COMMAND && type != OptionType.SUB_COMMAND_GROUP,
                        "You cannot add subcommands or subcommand groups to a subcommand!");
                break;
            case SUB_COMMAND_GROUP:
                Checks.check(type == OptionType.SUB_COMMAND,
                        "You can only add subcommands to a subcommand group!");
                break;
            default:
                throw new IllegalArgumentException("You cannot add an option to another option! Use subcommands instead.");
            }
            Option option = new Option(type, name, description);
            builder.accept(option);
            this.options.add(option);
            return this;
        }

        @Nonnull
        @Override
        public DataObject toData()
        {
            DataObject json = DataObject.empty();
            json.put("name", name);
            json.put("description", description);
            json.put("type", type.getKey());
            json.put("required", required);
            json.put("default", isDefault);
            if (!choices.isEmpty())
            {
                json.put("choices", DataArray.fromCollection(choices.entrySet()
                    .stream()
                    .map(entry ->
                        DataObject.empty()
                            .put("name", entry.getKey())
                            .put("value", entry.getValue()))
                    .collect(Collectors.toList())
                ));
            }
            if (!options.isEmpty())
                json.put("options", DataArray.fromCollection(options));
            return json;
        }
    }
}
