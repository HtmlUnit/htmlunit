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
package org.htmlunit.javascript.host.html;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlMeta;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

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
    public HTMLMetaElement() {
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
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
        return getDomNodeOrDie().getAttributeDirect(DomElement.NAME_ATTRIBUTE);
    }

    /**
     * Sets the {@code name} attribute.
     * @param name the {@code name} attribute
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute(DomElement.NAME_ATTRIBUTE, name);
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
     * @return the {@code meta} attribute
     */
    @JsxGetter
    public String getMedia() {
        return getDomNodeOrDie().getAttribute("media");
    }

    /**
     * @param media the media attribute
     */
    @JsxSetter
    public void setMedia(final String media) {
        getDomNodeOrDie().setAttribute("media", media);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
    }
}
