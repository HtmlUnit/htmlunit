/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D;

/**
 * A JavaScript object for {@link com.gargoylesoftware.htmlunit.html.HtmlCanvas}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLCanvasElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLCanvasElement() {
    }

    /**
     * Returns the "width" property.
     * @return the "width" property
     */
    public String jsxGet_width() {
        String width = getDomNodeOrDie().getAttribute("width");
        if (width == DomElement.ATTRIBUTE_NOT_DEFINED) {
            width = "300";
        }
        return width;
    }

    /**
     * Sets the "width" property.
     * @param width the "width" property
     */
    public void jsxSet_width(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the "height" property.
     * @return the "height" property
     */
    public String jsxGet_height() {
        String height = getDomNodeOrDie().getAttribute("height");
        if (height == DomElement.ATTRIBUTE_NOT_DEFINED) {
            height = "150";
        }
        return height;
    }

    /**
     * Sets the "height" property.
     * @param height the "height" property
     */
    public void jsxSet_height(final String height) {
        getDomNodeOrDie().setAttribute("height", height);
    }

    /**
     * Gets the context.
     * @param contextId the context id
     * @return Returns an object that exposes an API for drawing on the canvas,
     * or null if the given context ID is not supported
     */
    public Object jsxFunction_getContext(final String contextId) {
        if (contextId.equals("2d")) {
            final CanvasRenderingContext2D context = new CanvasRenderingContext2D();
            context.setParentScope(getParentScope());
            context.setPrototype(getPrototype(context.getClass()));
            return context;
        }
        return null;
    }
}
