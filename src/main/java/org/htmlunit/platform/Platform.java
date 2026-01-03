/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.platform;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.htmlunit.platform.canvas.rendering.AwtRenderingBackend;
import org.htmlunit.platform.canvas.rendering.NoOpRenderingBackend;
import org.htmlunit.platform.canvas.rendering.RenderingBackend;
import org.htmlunit.platform.font.AwtFontUtil;
import org.htmlunit.platform.font.FontUtil;
import org.htmlunit.platform.font.NoOpFontUtil;
import org.htmlunit.platform.image.ImageData;
import org.htmlunit.platform.image.NoOpImageData;

/**
 * Singleton to handle JDK specific stuff.
 * This is required to support at least the differences with android.
 *
 * @author Ronald Brill
 * @author Sven Strickroth
 */
public final class Platform {

    // private static final Log LOG = LogFactory.getLog(Platform.class);

    private static FontUtil FontUtil_;

    /**
     * @param imageWidth the width of the image this backend is for
     * @param imageHeight the height of the image this backend is for
     * @return a new {@link RenderingBackend}. If the {@link AwtRenderingBackend} can't be used a
     *         {@link NoOpRenderingBackend} is used instead.
     */
    public static RenderingBackend getRenderingBackend(final int imageWidth, final int imageHeight) {
        // for Android
        try {
            final Class<?> backendClass = Class.forName(
                        "org.htmlunit.platform.canvas.rendering.AwtRenderingBackend");
            return (RenderingBackend) ConstructorUtils.invokeConstructor(backendClass, imageWidth, imageHeight);
        }
        catch (final Throwable e) {
            return new NoOpRenderingBackend(imageWidth, imageHeight);
        }
    }

    /**
     * @return a new {@link FontUtil}. If the {@link AwtFontUtil} can't be used a
     *         {@link NoOpFontUtil} is used instead.
     */
    public static FontUtil getFontUtil() {
        // for Android
        if (FontUtil_ != null) {
            return FontUtil_;
        }

        try {
            final Class<?> backendClass = Class.forName("org.htmlunit.platform.font.AwtFontUtil");
            FontUtil_ = (FontUtil) ConstructorUtils.invokeConstructor(backendClass);
            return FontUtil_;
        }
        catch (final Throwable e) {
            FontUtil_ = new NoOpFontUtil();
            return FontUtil_;
        }
    }

    /**
     * @param inputStream the {@link InputStream} to read from
     * @return a new {@link ImageData} object constructed from the given inputStream
     * @throws IOException in case of error
     */
    public static ImageData buildImageData(final InputStream inputStream) throws IOException {
        try {
            final Class<?> backendClass = Class.forName(
                        "org.htmlunit.platform.image.ImageIOImageData");
            return (ImageData) ConstructorUtils.invokeConstructor(backendClass, inputStream);
        }
        catch (final InvocationTargetException ex) {
            final Throwable targetEx = ex.getTargetException();
            if (targetEx instanceof IOException exception) {
                throw exception;
            }

            return new NoOpImageData();
        }
        catch (final RuntimeException ex) {
            throw ex;
        }
        catch (final Throwable ex) {
            return new NoOpImageData();
        }
    }

    private Platform() {
        // util class
    }
}
