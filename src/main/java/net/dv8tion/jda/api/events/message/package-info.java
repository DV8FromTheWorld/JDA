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

/**
 * Events that are fired for {@link net.dv8tion.jda.api.entities.Message Messages} in
 * a {@link net.dv8tion.jda.api.entities.MessageChannel MessageChannel}.
 * Such as {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent receiving}.
 *
 * <p>These events combine all {@link net.dv8tion.jda.api.entities.MessageChannel MessageChannel}
 * messages but specifications can be found in subpackages.
 *
 * <h2>Requirements</h2>
 *
 * <p>Due to them being combinations, their requirements are a bit narrowed down.
 * These will only work in guild text channels if the {@link net.dv8tion.jda.api.requests.GatewayIntent#GUILD_MESSAGES GUILD_MESSAGES}
 * intent is enabled, and {@link net.dv8tion.jda.api.requests.GatewayIntent#DIRECT_MESSAGES DIRECT_MESSAGES} for
 * private channels. They will not fire at all if neither are enabled
 */
package net.dv8tion.jda.api.events.message;
