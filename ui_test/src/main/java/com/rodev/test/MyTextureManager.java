/*
 * Modern UI.
 * Copyright (C) 2019-2022 BloCamLimb. All rights reserved.
 *
 * Modern UI is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Modern UI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Modern UI. If not, see <https://www.gnu.org/licenses/>.
 */

package com.rodev.test;

import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.NativeImage;
import icyllis.modernui.graphics.opengl.GLCore;
import icyllis.modernui.graphics.opengl.GLTexture;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_REPEAT;

/**
 * This class maintains OpenGL 2D textures decoded from local client resources.
 */
@ApiStatus.Internal
public class MyTextureManager {

    private static final MyTextureManager INSTANCE = new MyTextureManager();

    public static final int CACHE_MASK = 0x1;
    public static final int MIPMAP_MASK = 0x2;

    private final Object mLock = new Object();
    private Map<String, Map<String, GLTexture>> mTextures = new HashMap<>();

    private MyTextureManager() {
    }

    /**
     * @return the global texture manager instance
     */
    public static MyTextureManager getInstance() {
        return INSTANCE;
    }

    // internal use
    public void reload() {
        synchronized (mLock) {
            // see Cleaner
            mTextures.clear();
            mTextures = new HashMap<>();
        }
    }

    /**
     * Get or create an OpenGL 2D texture from the given resource. {@link #CACHE_MASK} will use
     * cache or create into the cache. {@link #MIPMAP_MASK} will generate mipmaps for
     * the resource texture.
     *
     * @param namespace the application namespace
     * @param path      the path to the resource
     * @param flags     behavior flags
     * @return texture
     */
    @Nonnull
    public GLTexture getOrCreate(@Nonnull String namespace, @Nonnull String path, int flags) {
        final GLTexture texture;
        if ((flags & CACHE_MASK) != 0) {
            synchronized (mLock) {
                Map<String, GLTexture> cache = mTextures.computeIfAbsent(namespace, n -> new HashMap<>());
                GLTexture entry = cache.get(path);
                if (entry != null) {
                    return entry;
                } else {
                    texture = new GLTexture(GLCore.GL_TEXTURE_2D);
                    cache.put(path, texture);
                }
            }
        } else {
            texture = new GLTexture(GLCore.GL_TEXTURE_2D);
        }
        try (InputStream stream = ModernUI.getInstance().getResourceStream(namespace, path)) {
            NativeImage image = NativeImage.decode(null, stream);
            create(texture, image, (flags & MIPMAP_MASK) != 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texture;
    }

    /**
     * Create the texture from stream, cache will not be used, stream will be closed
     * automatically.
     *
     * @param stream resource stream
     * @param mipmap should generate mipmaps
     * @return texture
     */
    @Nonnull
    public GLTexture create(@Nonnull InputStream stream, boolean mipmap) {
        GLTexture texture = new GLTexture(GLCore.GL_TEXTURE_2D);
        try (stream) {
            NativeImage image = NativeImage.decode(null, stream);
            create(texture, image, mipmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texture;
    }

    /**
     * Create the texture from channel, cache will not be used, channel will be closed
     * automatically.
     *
     * @param channel resource channel
     * @param mipmap  should generate mipmaps
     * @return texture
     */
    @Nonnull
    public GLTexture create(@Nonnull ReadableByteChannel channel, boolean mipmap) {
        GLTexture texture = new GLTexture(GLCore.GL_TEXTURE_2D);
        try (channel) {
            NativeImage image = NativeImage.decode(null, channel);
            create(texture, image, mipmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texture;
    }

    private void create(@Nonnull GLTexture texture, @Nonnull NativeImage image, boolean mipmap) {
        int width = image.getWidth();
        int height = image.getHeight();
        texture.setDimension(width, height, 1);
        if (Core.isOnRenderThread()) {
            texture.allocate2D(image.getInternalGlFormat(), width, height, mipmap ? 4 : 0);
            texture.upload(0, 0, 0, width, height, 0,
                    0, 0, 1, image.getGlFormat(), GLCore.GL_UNSIGNED_BYTE, image.getPixels());
            texture.setFilter(true, true);
            texture.setWrap(GL_REPEAT, GL_REPEAT);
            if (mipmap) {
                texture.generateMipmap();
            }
            image.close();
        } else {
            if (mipmap) {
                Core.postOnRenderThread(() -> {
                    int w = image.getWidth();
                    int h = image.getHeight();
                    texture.allocate2D(image.getInternalGlFormat(), w, h, 4);
                    texture.upload(0, 0, 0, w, h, 0,
                            0, 0, 1, image.getGlFormat(), GLCore.GL_UNSIGNED_BYTE, image.getPixels());
                    texture.setFilter(true, true);
                    texture.setWrap(GL_REPEAT, GL_REPEAT);
                    texture.generateMipmap();
                    image.close();
                });
            } else {
                Core.postOnRenderThread(() -> {
                    int w = image.getWidth();
                    int h = image.getHeight();
                    texture.allocate2D(image.getInternalGlFormat(), w, h, 0);
                    texture.upload(0, 0, 0, w, h, 0,
                            0, 0, 1, image.getGlFormat(), GLCore.GL_UNSIGNED_BYTE, image.getPixels());
                    texture.setFilter(true, true);
                    texture.setWrap(GL_REPEAT, GL_REPEAT);
                    image.close();
                });
            }
        }
    }
}
