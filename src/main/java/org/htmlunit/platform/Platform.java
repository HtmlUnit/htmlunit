/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.htmlunit.platform.canvas.rendering.AwtRenderingBackend;
import org.htmlunit.platform.canvas.rendering.NoOpRenderingBackend;
import org.htmlunit.platform.canvas.rendering.RenderingBackend;
import org.htmlunit.platform.font.AwtFontUtil;
import org.htmlunit.platform.font.FontUtil;
import org.htmlunit.platform.font.NoOpFontUtil;
import org.htmlunit.platform.image.ImageData;
import org.htmlunit.platform.image.NoOpImageData;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Singleton to handle JDK specific stuff.
 * This is required to support at least the differences with android.
 *
 * @author Ronald Brill
 */
public final class Platform {

    // private static final Log LOG = LogFactory.getLog(Platform.class);

    private static FontUtil FontUtil_;

    private static XmlUtilsHelperAPI HelperXerces_;
    private static XmlUtilsHelperAPI HelperSunXerces_;

    static {
        try {
            HelperSunXerces_ = (XmlUtilsHelperAPI)
                    Class.forName("org.htmlunit.platform.util.XmlUtilsSunXercesHelper").getDeclaredConstructor().newInstance();
        }
        catch (final Exception | LinkageError ignored) {
            // ignore
        }

        try {
            HelperXerces_ = (XmlUtilsHelperAPI)
                    Class.forName("org.htmlunit.platform.util.XmlUtilsXercesHelper").getDeclaredConstructor().newInstance();
        }
        catch (final Exception | LinkageError ignored) {
            // ignore
        }
    }

    /**
     * Forward the call to the correct helper.
     *
     * @param namedNodeMap the node map
     * @param attributesOrderMap the order map
     * @param element the node
     * @param requiredIndex the required index
     * @return the index or requiredIndex
     */
    public static int getIndex(final NamedNodeMap namedNodeMap, final Map<Integer, List<String>> attributesOrderMap,
            final Node element, final int requiredIndex) {
        if (HelperXerces_ != null) {
            final int result = HelperXerces_.getIndex(namedNodeMap, attributesOrderMap, element, requiredIndex);
            if (result != -1) {
                return result;
            }
        }
        if (HelperSunXerces_ != null) {
            final int result = HelperSunXerces_.getIndex(namedNodeMap, attributesOrderMap, element, requiredIndex);
            if (result != -1) {
                return result;
            }
        }

        return requiredIndex;
    }

    /**
     * Returns internal Xerces details about all elements in the specified document.
     * The id of the returned {@link Map} is the {@code nodeIndex} of an element, and the list
     * is the array of ordered attributes names.
     * @param document the document
     * @return the map of an element index with its ordered attribute names
     */
    public static Map<Integer, List<String>> getAttributesOrderMap(final Document document) {
        if (HelperXerces_ != null) {
            final Map<Integer, List<String>> result = HelperXerces_.getAttributesOrderMap(document);
            if (result != null) {
                return result;
            }
        }
        if (HelperSunXerces_ != null) {
            final Map<Integer, List<String>> result = HelperSunXerces_.getAttributesOrderMap(document);
            if (result != null) {
                return result;
            }
        }

        return new HashMap<>();
    }

    /**
     * @param imageWidth the width of the image this backend is for
     * @param imageHeight the height of the image this backend is for
     * @return a new {@link RenderingBackend}. If the {@link AwtRenderingBackend} can't be used a
     * {@link NoOpRenderingBackend} is used instead.
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
     * {@link NoOpFontUtil} is used instead.
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

    public static ImageData buildImageData(final InputStream inputStream) throws IOException {
        try {
            final Class<?> backendClass = Class.forName(
                        "org.htmlunit.platform.image.ImageIOImageData");
            return (ImageData) ConstructorUtils.invokeConstructor(backendClass, inputStream);
        }
        catch (final InvocationTargetException ex) {
            final Throwable targetEx = ex.getTargetException();
            if (targetEx instanceof IOException) {
                throw (IOException) targetEx;
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
