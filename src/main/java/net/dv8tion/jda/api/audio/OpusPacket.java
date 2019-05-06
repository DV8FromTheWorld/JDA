/*
 * Copyright 2015-2018 Austin Keener & Michael Ritter & Florian Spieß
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

package net.dv8tion.jda.core.audio;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class OpusPacket implements Comparable<OpusPacket>
{
    public static final int OPUS_SAMPLE_RATE = 48000;   //(Hz) We want to use the highest of qualities! All the bandwidth!
    public static final int OPUS_FRAME_SIZE = 960;      //An opus frame size of 960 at 48000hz represents 20 milliseconds of audio.
    public static final int OPUS_FRAME_TIME_AMOUNT = 20;//This is 20 milliseconds. We are only dealing with 20ms opus packets.
    public static final int OPUS_CHANNEL_COUNT = 2;     //We want to use stereo. If the audio given is mono, the encoder promotes it
                                                        // to Left and Right mono (stereo that is the same on both sides)

    private final long userId;
    private final byte[] opusAudio;
    private final Decoder decoder;
    private final AudioPacket rawPacket;

    private short[] decoded;
    private boolean triedDecode;

    OpusPacket(AudioPacket packet, long userId, Decoder decoder)
    {
        this.rawPacket = packet;
        this.userId = userId;
        this.decoder = decoder;
        this.opusAudio = packet.getEncodedAudio();
    }

    public char getSequence()
    {
        return rawPacket.getSequence();
    }

    public int getTimestamp()
    {
        return rawPacket.getTimestamp();
    }

    public int getSsrc()
    {
        return rawPacket.getSSRC();
    }

    public long getUserId()
    {
        return userId;
    }

    public boolean canDecode()
    {
        return decoder != null && decoder.isInOrder(getSequence());
    }

    public byte[] getOpusAudio()
    {
        //prevent write access to backing array
        return Arrays.copyOf(opusAudio, opusAudio.length);
    }

    public synchronized short[] decode()
    {
        if (triedDecode)
            return decoded;
        if (decoder == null)
            throw new IllegalStateException("No decoder available");
        if (!decoder.isInOrder(getSequence()))
            throw new IllegalStateException("Packet is not in order");
        triedDecode = true;
        return decoded = decoder.decodeFromOpus(rawPacket);
    }

    public byte[] getAudioData(double volume)
    {
        return getAudioData(decode(), volume);
    }

    public static byte[] getAudioData(short[] decoded, double volume)
    {
        if (decoded == null)
            throw new IllegalArgumentException("Cannot get audio data from null");
        int byteIndex = 0;
        byte[] audio = new byte[decoded.length * 2];
        for (short s : decoded)
        {
            if (volume != 1.0)
                s = (short) (s * volume);

            byte leftByte  = (byte) ((s >>> 8) & 0xFF);
            byte rightByte = (byte)  (s        & 0xFF);
            audio[byteIndex] = leftByte;
            audio[byteIndex + 1] = rightByte;
            byteIndex += 2;
        }
        return audio;
    }

    @Override
    public int compareTo(@Nonnull OpusPacket o)
    {
        return getSequence() - o.getSequence();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getSequence(), getTimestamp(), getOpusAudio());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (!(obj instanceof OpusPacket))
            return false;
        OpusPacket other = (OpusPacket) obj;
        return getSequence() == other.getSequence()
            && getTimestamp() == other.getTimestamp()
            && getSsrc() == other.getSsrc();
    }
}
