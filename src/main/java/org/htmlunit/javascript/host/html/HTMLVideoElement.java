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
package org.htmlunit.javascript.host.html;

import org.htmlunit.html.HtmlVideo;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLVideoElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlVideo.class)
public class HTMLVideoElement extends HTMLMediaElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @JsxGetter
    public int getWidth() {
        final String value = getDomNodeOrDie().getAttributeDirect("width");
        final Integer intValue = HTMLCanvasElement.getValue(value);
        if (intValue != null) {
            return intValue;
        }
        return 0;
    }

    /**
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    @JsxSetter
    public void setWidth(final int width) {
        getDomNodeOrDie().setAttribute("width", Integer.toString(width));
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @JsxGetter
    public int getHeight() {
        final String value = getDomNodeOrDie().getAttributeDirect("height");
        final Integer intValue = HTMLCanvasElement.getValue(value);
        if (intValue != null) {
            return intValue;
        }
        return 0;
    }

    /**
     * Sets the {@code height} property.
     * @param height the {@code height} property
     */
    @JsxSetter
    public void setHeight(final int height) {
        getDomNodeOrDie().setAttribute("height", Integer.toString(height));
    }

    @Override
    protected String getNodeNameCustomize() {
        return "VIDEO";
    }
}
