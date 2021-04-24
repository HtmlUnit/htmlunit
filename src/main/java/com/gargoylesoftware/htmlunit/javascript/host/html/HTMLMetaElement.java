/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLMetaElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlMeta.class)
public class HTMLMetaElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLMetaElement() {
    }

    /**
     * Returns the {@code charset} attributee.
     * @return the {@code charset} attribute
     */
    @JsxGetter(IE)
    public String getCharset() {
        return "";
    }

    /**
     * Sets the {@code charset} attribute.
     * @param charset the {@code charset} attribute
     */
    @JsxSetter(IE)
    public void setCharset(final String charset) {
        //empty
    }

    /**
     * Returns the {@code content} attribute.
     * @return the {@code content} attribute
     */
    @JsxGetter
    public String getContent() {
        return getDomNodeOrDie().getAttributeDirect("content");
    }

    /**
     * Sets the {@code content} attribute.
     * @param content the content attribute
     */
    @JsxSetter
    public void setContent(final String content) {
        getDomNodeOrDie().setAttribute("content", content);
    }

    /**
     * Returns the {@code http-equiv} attribute.
     * @return the {@code http-equiv} attribute
     */
    @JsxGetter
    public String getHttpEquiv() {
        return getDomNodeOrDie().getAttribute("http-equiv");
    }

    /**
     * Sets the {@code http-equiv} attribute.
     * @param httpEquiv the http-equiv attribute
     */
    @JsxSetter
    public void setHttpEquiv(final String httpEquiv) {
        getDomNodeOrDie().setAttribute("http-equiv", httpEquiv);
    }

    /**
     * Returns the {@code name} attribute.
     * @return the {@code name} attribute
     */
    @JsxGetter
    @Override
    public String getName() {
        return getDomNodeOrDie().getAttributeDirect("name");
    }

    /**
     * Sets the {@code name} attribute.
     * @param name the {@code name} attribute
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute("name", name);
    }

    /**
     * Returns the {@code scheme} attribute.
     * @return the {@code scheme} attribute
     */
    @JsxGetter
    public String getScheme() {
        return getDomNodeOrDie().getAttributeDirect("scheme");
    }

    /**
     * Sets the {@code scheme} attribute.
     * @param scheme the {@code scheme} attribute
     */
    @JsxSetter
    public void setScheme(final String scheme) {
        getDomNodeOrDie().setAttribute("scheme", scheme);
    }

    /**
     * Returns the {@code url} attribute.
     * @return the {@code url} attribute
     */
    @JsxGetter(IE)
    public String getUrl() {
        return "";
    }

    /**
     * Sets the {@code url} attribute.
     * @param url the {@code url} attribute
     */
    @JsxSetter(IE)
    public void setUrl(final String url) {
        //empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
    }
}
