/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.net.MalformedURLException;
import java.net.URL;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlEmbed;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLEmbedElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLEmbedElement">MDN Documentation</a>
 */
@JsxClass(domClass = HtmlEmbed.class)
public class HTMLEmbedElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the value of the {@code align} property.
     * @return the value of the {@code align} property
     */
    @JsxGetter
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     */
    @JsxSetter
    public void setAlign(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the {@code height} property.
     * @return the value of the {@code height} property
     */
    @JsxGetter(propertyName = "height")
    public String getHeight_js() {
        return getDomNodeOrDie().getAttributeDirect("height");
    }

    /**
     * Sets the value of the {@code height} property.
     * @param height the value of the {@code height} property
     */
    @JsxSetter(propertyName = "height")
    public void setHeight_js(final String height) {
        getDomNodeOrDie().setAttribute("height", height);
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        return getDomNodeOrDie().getAttributeDirect("width");
    }

    /**
     * Sets the value of the {@code width} property.
     * @param width the value of the {@code width} property
     */
    @JsxSetter(propertyName = "width")
    public void setWidth_js(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the value of the {@code src} property.
     * @return the value of the {@code src} property
     */
    @JsxGetter
    public String getSrc() {
        final HtmlEmbed embed = (HtmlEmbed) getDomNodeOrDie();
        String src = embed.getAttributeDirect("src");
        if (ATTRIBUTE_NOT_DEFINED == src) {
            return src;
        }
        try {
            final URL expandedSrc = ((HtmlPage) embed.getPage()).getFullyQualifiedUrl(src);
            src = expandedSrc.toString();
        }
        catch (final MalformedURLException ignored) {
            // ignore
        }
        return src;
    }

    /**
     * Sets the value of the {@code src} property.
     * @param src the value of the {@code src} property
     */
    @JsxSetter
    public void setSrc(final String src) {
        getDomNodeOrDie().setAttribute("src", src);
    }

    /**
     * Returns the value of the {@code type} property.
     * @return the value of the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return getDomNodeOrDie().getAttributeDirect("type");
    }

    /**
     * Sets the value of the {@code type} property.
     * @param type the value of the {@code type} property
     */
    @JsxSetter
    public void setType(final String type) {
        getDomNodeOrDie().setAttribute("type", type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
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
     * @param name the {@code name} attribute value
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute(DomElement.NAME_ATTRIBUTE, name);
    }

}
