/**
 * Copyright 2015-2016 Austin Keener & Michael Ritter
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dv8tion.jda.entities.impl;

import net.dv8tion.jda.entities.SelfInfo;
import net.dv8tion.jda.entities.TextChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelfInfoImpl extends UserImpl implements SelfInfo {
    private String email;
    private List<TextChannel> mutedChannels = new ArrayList<>();
    private boolean verified;

    public SelfInfoImpl(String id, String email, JDAImpl api) {
        super(id, api);
        this.email = email;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public SelfInfoImpl setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public List<TextChannel> getMutedChannels() {
        return Collections.unmodifiableList(mutedChannels);
    }

    public SelfInfoImpl setMutedChannels(List<TextChannel> mutedChannels) {
        this.mutedChannels = mutedChannels;
        return this;
    }

    @Override
    public boolean isVerified() {
        return verified;
    }

    public SelfInfoImpl setVerified(boolean verified) {
        this.verified = verified;
        return this;
    }

    public List<TextChannel> getMutedChannelsModifiable() {
        return mutedChannels;
    }
}
