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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BOUNDING_CLIENT_RECT_OFFSET_TWO;

import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;

/**
 * The JavaScript object "HTMLHtmlElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@JsxClass(domClasses = HtmlHtml.class)
public class HTMLHtmlElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLHtmlElement() {
        // Empty.
    }

    /** {@inheritDoc} */
    @Override
    public Object getParentNode() {
        return getWindow().getDocument_js();
    }

    /** {@inheritDoc} */
    @Override
    public int getClientWidth() {
        return getWindow().getInnerWidth();
    }

    /** {@inheritDoc} */
    @Override
    public int getClientHeight() {
        return getWindow().getInnerHeight();
    }

    /**
     * IE has some special idea here.
     * {@inheritDoc}
     */
    @Override
    public int getClientLeft() {
        if (getBrowserVersion().hasFeature(JS_BOUNDING_CLIENT_RECT_OFFSET_TWO)) {
            return 2;
        }
        return super.getClientLeft();
    }

    /**
     * IE has some special idea here.
     * {@inheritDoc}
     */
    @Override
    public int getClientTop() {
        if (getBrowserVersion().hasFeature(JS_BOUNDING_CLIENT_RECT_OFFSET_TWO)) {
            return 2;
        }
        return super.getClientTop();
    }
}

