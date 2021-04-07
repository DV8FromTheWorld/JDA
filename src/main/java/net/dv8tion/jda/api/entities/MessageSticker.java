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
package net.dv8tion.jda.api.entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * An object representing a sticker in a Discord message.
 *
 * @see Message#getStickers()
 */
public class MessageSticker implements ISnowflake
{
    private final long id;
    private final String name;
    private final String description;
    private final long packId;
    private final String asset;
    private final String previewAsset;
    private final StickerFormat formatType;
    private final Set<String> tags;

    /** Template for {@link #getStickerUrl()} ()} */
    public static final String ASSET_URL = "https://cdn.discordapp.com/stickers/%s/%s.%s";

    public MessageSticker(final long id, final String name, final String description, final long packId, final String asset, final String previewAsset, final StickerFormat formatType, final Set<String> tags)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.packId = packId;
        this.asset = asset;
        this.previewAsset = previewAsset;
        this.formatType = formatType;
        this.tags = tags;
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    /**
     * The name of the sticker.
     *
     * @return the name of the sticker
     */
    @Nonnull
    public String getName()
    {
        return name;
    }

    /**
     * The description of the sticker.
     *
     * @return the description of the sticker
     */
    @Nonnull
    public String getDescription()
    {
        return description;
    }

    /**
     * The ID of the pack the sticker is from.
     *
     * @return the ID of the pack the sticker is from
     */
    @Nonnull
    public String getPackId()
    {
        return Long.toUnsignedString(getPackIdLong());
    }

    /**
     * The ID of the pack the sticker is from.
     *
     * @return the ID of the pack the sticker is from
     */
    public long getPackIdLong()
    {
        return packId;
    }

    /**
     * The Discord hash-id of the sticker. This represents the actual asset file in the CDN for the sticker.
     * <br><b>The URL for fetching sticker assets is currently private.
     *
     * @return the Discord hash-id of the sticker
     */
    @Nonnull
    public String getAssetHash()
    {
        return asset;
    }

    /**
     * The Discord hash-id of the preview of the sticker. This represents the actual preview asset file in the CDN for the sticker.
     * <br><b>The URL for fetching sticker assets is currently private.
     *
     * @return the Discord hash-id of the preview image of the sticker or {@code null} if the sticker has no preview image
     */
    @Nullable
    public String getPreviewAssetHash()
    {
        return previewAsset;
    }

    /**
     * The url of the sticker asset.
     *
     * @return the url of the sticker
     */
    @Nonnull
    public String getAssetUrl()
    {
        return String.format(ASSET_URL, id, asset, formatType.getExtension());
    }

    /**
     * The {@link StickerFormat Format} of the sticker.
     *
     * @return the format of the sticker
     */
    @Nonnull
    public StickerFormat getFormatType()
    {
        return formatType;
    }

    /**
     * Set of tags of the sticker. Tags can be used instead of the name of the sticker as aliases.
     *
     * @return Possibly-empty unmodifiable Set of tags of the sticker
     */
    @Nonnull
    public Set<String> getTags()
    {
        return tags;
    }

    public enum StickerFormat
    {
        /**
         * The PNG format.
         */
        PNG(1, "png"),
        /**
         * The APNG format.
         */
        APNG(2, "apng"),
        /**
         * The LOTTIE format.
         * <br>Lottie isn't a standard renderable image. It is a JSON with data that can be rendered using the lottie library.
         *
         * @see <a href="https://airbnb.io/lottie/">Lottie website</a>
         */
        LOTTIE(3, "json"),
        /**
         * Represents any unknown or unsupported {@link net.dv8tion.jda.api.entities.MessageSticker MessageSticker} format types.
         */
        UNKNOWN(-1, null);

        private final int id;
        private final String extension;

        StickerFormat(final int id, final String extension)
        {
            this.id = id;
            this.extension = extension;
        }

        public String getExtension()
        {
            return extension;
        }

        @Nonnull
        public static MessageSticker.StickerFormat fromId(int id)
        {
            for (MessageSticker.StickerFormat stickerFormat : values())
            {
                if (stickerFormat.id == id)
                    return stickerFormat;
            }
            return UNKNOWN;
        }
    }
}
