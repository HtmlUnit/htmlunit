/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;

/**
 * The JavaScript object "HTMLHtmlElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@JsxClass(domClass = HtmlHtml.class)
public class HTMLHtmlElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLHtmlElement() {
        // Empty.
    }

    /** {@inheritDoc} */
    @Override
    public Object get_parentNode() {
        return getWindow().get_document();
    }

    /** {@inheritDoc} */
    @Override
    public int get_clientWidth() {
        return getWindow().get_innerWidth();
    }

    /** {@inheritDoc} */
    @Override
    public int get_clientHeight() {
        return getWindow().get_innerHeight();
    }

    /**
     * IE has some special idea here.
     * {@inheritDoc}
     */
    @Override
    public int get_clientLeft() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_BOUNDING_CLIENT_RECT_OFFSET_TWO)) {
            return 2;
        }
        return super.get_clientLeft();
    }

    /**
     * IE has some special idea here.
     * {@inheritDoc}
     */
    @Override
    public int get_clientTop() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_BOUNDING_CLIENT_RECT_OFFSET_TWO)) {
            return 2;
        }
        return super.get_clientTop();
    }
}

