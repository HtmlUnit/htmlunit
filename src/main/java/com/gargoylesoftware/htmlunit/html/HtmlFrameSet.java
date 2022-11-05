/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "frameset".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronny Shapiro
 */
public class HtmlFrameSet extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "frameset";

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlFrameSet(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        // force script object creation now to forward onXXX handlers to window
        if (getPage().getWebClient().isJavaScriptEngineEnabled()) {
            getScriptableObject();
        }
    }

    /**
     * Returns the value of the attribute {@code rows}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rows} or an empty string if that attribute isn't defined
     */
    public final String getRowsAttribute() {
        return getAttributeDirect("rows");
    }

    /**
     * Returns the value of the attribute {@code cols}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code cols} or an empty string if that attribute isn't defined
     */
    public final String getColsAttribute() {
        return getAttributeDirect("cols");
    }

    /**
     * Returns the value of the attribute {@code onload}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onload} or an empty string if that attribute isn't defined
     */
    public final String getOnLoadAttribute() {
        return getAttributeDirect("onload");
    }

    /**
     * Returns the value of the attribute {@code onunload}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onunload} or an empty string if that attribute isn't defined
     */
    public final String getOnUnloadAttribute() {
        return getAttributeDirect("onunload");
    }
}
