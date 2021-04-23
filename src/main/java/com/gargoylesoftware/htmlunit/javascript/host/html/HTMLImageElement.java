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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_EMPTY_SOURCE_RETURNS_0x0;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_24x24_0x0;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object {@code HTMLImageElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
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
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public void jsConstructor() {
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
        final String src = img.getSrcAttribute();
        if ("".equals(src)) {
            return src;
        }
        try {
            final HtmlPage page = (HtmlPage) img.getPage();
            return page.getFullyQualifiedUrl(src).toExternalForm();
        }
        catch (final MalformedURLException e) {
            final String msg = "Unable to create fully qualified URL for src attribute of image " + e.getMessage();
            throw Context.reportRuntimeError(msg);
        }
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
        final String alt = getDomNodeOrDie().getAttributeDirect("alt");
        return alt;
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
        final String border = getDomNodeOrDie().getAttributeDirect("border");
        return border;
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
        final boolean acceptArbitraryValues = getBrowserVersion().hasFeature(JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);

        final String align = getDomNodeOrDie().getAttributeDirect("align");
        if (acceptArbitraryValues) {
            return align;
        }

        final String normalizedValue = NORMALIZED_ALIGN_VALUES.get(align.toLowerCase(Locale.ROOT));
        if (null != normalizedValue) {
            return normalizedValue;
        }
        return "";
    }

    /**
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     */
    @JsxSetter
    public void setAlign(final String align) {
        final boolean acceptArbitraryValues = getBrowserVersion().hasFeature(JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
        if (acceptArbitraryValues) {
            getDomNodeOrDie().setAttribute("align", align);
            return;
        }

        final String normalizedValue = NORMALIZED_ALIGN_VALUES.get(align.toLowerCase(Locale.ROOT));
        if (null != normalizedValue) {
            getDomNodeOrDie().setAttribute("align", normalizedValue);
            return;
        }

        throw Context.reportRuntimeError("Cannot set the align property to invalid value: '" + align + "'");
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @Override
    @JsxGetter
    public int getWidth() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        final String widthAttrib = img.getWidthAttribute();

        if (DomElement.ATTRIBUTE_NOT_DEFINED != widthAttrib) {
            try {
                return Integer.parseInt(widthAttrib);
            }
            catch (final NumberFormatException e) {
                // ignore
            }
        }

        final String src = img.getSrcAttribute();
        if (DomElement.ATTRIBUTE_NOT_DEFINED == src) {
            final BrowserVersion browserVersion = getBrowserVersion();
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30)) {
                return 28;
            }
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0)
                    || browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_24x24_0x0)) {
                return 0;
            }
            return 24;
        }

        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_EMPTY_SOURCE_RETURNS_0x0) && StringUtils.isEmpty(src)) {
            return 0;
        }
        if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0) && StringUtils.isBlank(src)) {
            return 0;
        }

        try {
            return img.getWidth();
        }
        catch (final IOException e) {
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30)) {
                return 28;
            }
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0)) {
                return 16;
            }
            return 24;
        }
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
    @Override
    @JsxGetter
    public int getHeight() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        final String height = img.getHeightAttribute();

        if (DomElement.ATTRIBUTE_NOT_DEFINED != height) {
            try {
                return Integer.parseInt(height);
            }
            catch (final NumberFormatException e) {
                // ignore
            }
        }

        final String src = img.getSrcAttribute();
        if (DomElement.ATTRIBUTE_NOT_DEFINED == src) {
            final BrowserVersion browserVersion = getBrowserVersion();
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30)) {
                return 30;
            }
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0)
                    || browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_24x24_0x0)) {
                return 0;
            }
            return 24;
        }

        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_EMPTY_SOURCE_RETURNS_0x0) && StringUtils.isEmpty(src)) {
            return 0;
        }
        if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0) && StringUtils.isBlank(src)) {
            return 0;
        }

        try {
            return img.getHeight();
        }
        catch (final IOException e) {
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30)) {
                return 30;
            }
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0)) {
                return 16;
            }
            return 24;
        }
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

}
