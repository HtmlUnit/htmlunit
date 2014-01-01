/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_READONLY_FOR_SOME_TAGS;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object "HTMLHtmlElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlHtml.class)
public class HTMLHtmlElement extends HTMLElement {

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

    /**
     * Overwritten to throw an exception in IE8/9.
     * @param value the new value for replacing this node
     */
    @JsxSetter
    @Override
    public void setOuterHTML(final String value) {
        throw Context.reportRuntimeError("outerHTML is read-only for tag 'html'");
    }

    /**
     * Overwritten to throw an exception in IE8/9.
     * @param value the new value for the contents of this node
     */
    @JsxSetter
    @Override
    public void setInnerHTML(final Object value) {
        if (getBrowserVersion().hasFeature(JS_INNER_HTML_READONLY_FOR_SOME_TAGS)) {
            throw Context.reportRuntimeError("innerHTML is read-only for tag 'html'");
        }
        super.setInnerHTML(value);
    }

    /**
     * Overwritten to throw an exception because this is readonly.
     * @param value the new value for the contents of this node
     */
    @Override
    protected void setInnerTextImpl(final String value) {
        throw Context.reportRuntimeError("innerText is read-only for tag 'html'");
    }
}
