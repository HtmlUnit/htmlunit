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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.htmlunit.SgmlPage;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLImageElement}.
 *
 * @author Mike Bowler
 * @author George Murnock
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlImage.class)
public class HTMLImageElement extends HTMLElement {
    private static final Map<String, String> NORMALIZED_ALIGN_VALUES;
    static {
        NORMALIZED_ALIGN_VALUES = new HashMap<>();
        NORMALIZED_ALIGN_VALUES.put("center", "center");
        NORMALIZED_ALIGN_VALUES.put("left", "left");
        NORMALIZED_ALIGN_VALUES.put("right", "right");
        NORMALIZED_ALIGN_VALUES.put("bottom", "bottom");
        NORMALIZED_ALIGN_VALUES.put("middle", "middle");
        NORMALIZED_ALIGN_VALUES.put("top", "top");
        NORMALIZED_ALIGN_VALUES.put("absbottom", "absBottom");
        NORMALIZED_ALIGN_VALUES.put("absmiddle", "absMiddle");
        NORMALIZED_ALIGN_VALUES.put("baseline", "baseline");
        NORMALIZED_ALIGN_VALUES.put("texttop", "textTop");
    }

    private boolean endTagForbidden_ = true;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeError("Invalid constructor.");
    }

    /**
     * JavaScript constructor.
     */
    public void jsConstructorImage() {
        final SgmlPage page = (SgmlPage) getWindow().getWebWindow().getEnclosedPage();
        final DomElement fake =
                page.getWebClient().getPageCreator().getHtmlParser()
                    .getFactory(HtmlImage.TAG_NAME)
                    .createElement(page, HtmlImage.TAG_NAME, null);
        setDomNode(fake);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);
        if ("image".equalsIgnoreCase(domNode.getLocalName())) {
            endTagForbidden_ = false;
        }
    }

    /**
     * Sets the {@code src} attribute.
     * @param src the {@code src} attribute value
     */
    @JsxSetter
    public void setSrc(final String src) {
        final HtmlElement img = getDomNodeOrDie();
        img.setAttribute(DomElement.SRC_ATTRIBUTE, src);
    }

    /**
     * Returns the value of the {@code src} attribute.
     * @return the value of the {@code src} attribute
     */
    @JsxGetter
    public String getSrc() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        return img.getSrc();
    }

    /**
     * Sets the {@code onload} event handler for this element.
     * @param onload the {@code onload} event handler for this element
     */
    @Override
    public void setOnload(final Object onload) {
        super.setOnload(onload);

        // maybe the onload handler was not called so far
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        img.doOnLoad();
    }

    /**
     * Returns the value of the {@code alt} property.
     * @return the value of the {@code alt} property
     */
    @JsxGetter
    public String getAlt() {
        return getDomNodeOrDie().getAttributeDirect("alt");
    }

    /**
     * Sets the value of the {@code alt} property.
     * @param alt the value
     */
    @JsxSetter
    public void setAlt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter
    public String getBorder() {
        return getDomNodeOrDie().getAttributeDirect("border");
    }

    /**
     * Sets the {@code border} attribute.
     * @param border the {@code border} attribute
     */
    @JsxSetter
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the {@code align} property.
     * @return the value of the {@code align} property
     */
    @JsxGetter
    public String getAlign() {
        return getDomNodeOrDie().getAttributeDirect("align");
    }

    /**
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     */
    @JsxSetter
    public void setAlign(final String align) {
        getDomNodeOrDie().setAttribute("align", align);
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter
    public int getWidth() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        return img.getWidthOrDefault();
    }

    /**
     * Sets the value of the {@code width} property.
     * @param width the value of the {@code width} property
     */
    @JsxSetter
    public void setWidth(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the value of the {@code height} property.
     * @return the value of the {@code height} property
     */
    @JsxGetter
    public int getHeight() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        return img.getHeightOrDefault();
    }

    /**
     * Sets the value of the {@code height} property.
     * @param height the value of the {@code height} property
     */
    @JsxSetter
    public void setHeight(final String height) {
        getDomNodeOrDie().setAttribute("height", height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return endTagForbidden_;
    }

    /**
     * Support for the image.complete property.
     * @return the value of the {@code complete} property
     */
    @JsxGetter
    public boolean isComplete() {
        return ((HtmlImage) getDomNodeOrDie()).isComplete();
    }

    /**
     * Returns the value of the {@code naturalWidth} property.
     * @return the value of the {@code naturalWidth} property
     */
    @JsxGetter
    public int getNaturalWidth() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        try {
            return img.getWidth();
        }
        catch (final IOException e) {
            return 0;
        }
    }

    /**
     * Returns the value of the {@code naturalHeight} property.
     * @return the value of the {@code naturalHeight} property
     */
    @JsxGetter
    public int getNaturalHeight() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        try {
            return img.getHeight();
        }
        catch (final IOException e) {
            return 0;
        }
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

}
