/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DISPLAY_DEFAULT;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "rt".
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlRt extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "rt";

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlRt(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the default display style.
     *
     * @return the default display style.
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        if (hasFeature(CSS_DISPLAY_DEFAULT)) {
            return DisplayStyle.INLINE;
        }
        return DisplayStyle.RUBY_TEXT;
    }
}
