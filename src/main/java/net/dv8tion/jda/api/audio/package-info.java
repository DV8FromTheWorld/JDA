/*
 * Copyright 2015-2019 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
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
 * The audio API for Discord.
 * This is only available if not specifically disabled through the {@link net.dv8tion.jda.api.JDABuilder JDABuilder}.
 *
 * <p>We encode/decode opus audio packages that can be used in an {@link net.dv8tion.jda.internal.audio.AudioConnection AudioConnection}.
 * To interact with the connection an {@link net.dv8tion.jda.api.audio.AudioSendHandler AudioSendHandler}
 * or an {@link net.dv8tion.jda.api.audio.AudioReceiveHandler AudioReceiveHandler} have to be specified the audio manager.
 */
package net.dv8tion.jda.api.audio;
