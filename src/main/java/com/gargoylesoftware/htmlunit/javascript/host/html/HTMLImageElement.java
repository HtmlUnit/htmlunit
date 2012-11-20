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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.xalan.xsltc.runtime.AttributeList;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object that represents an "Image".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClasses = HtmlImage.class)
public class HTMLImageElement extends HTMLElement {
    private static final Map<String, String> NORMALIZED_ALIGN_VALUES;
    static {
        NORMALIZED_ALIGN_VALUES = new HashMap<String, String>();
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

    private boolean instantiatedViaJavaScript_ = false;

    /**
     * Creates an instance.
     */
    public HTMLImageElement() {
        // Empty.
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    @JsxConstructor
    public void jsConstructor() {
        instantiatedViaJavaScript_ = true;
        final SgmlPage page = (SgmlPage) getWindow().getWebWindow().getEnclosedPage();
        final DomElement fake =
                HTMLParser.getFactory(HtmlImage.TAG_NAME).createElement(page, HtmlImage.TAG_NAME, new AttributeList());
        setDomNode(fake);
    }

    /**
     * Sets the <tt>src</tt> attribute.
     * @param src the <tt>src</tt> attribute value
     */
    @JsxSetter
    public void setSrc(final String src) {
        final HtmlElement img = getDomNodeOrDie();
        img.setAttribute("src", src);
    }

    /**
     * Returns the value of the <tt>src</tt> attribute.
     * @return the value of the <tt>src</tt> attribute
     */
    @JsxGetter
    public String getSrc() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        final String src = img.getSrcAttribute();
        if (instantiatedViaJavaScript_ && "".equals(src)) {
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
     * Sets the <tt>onload</tt> event handler for this element.
     * @param onloadHandler the <tt>onload</tt> event handler for this element
     */
    @JsxSetter
    public void setOnload(final Object onloadHandler) {
        setEventHandlerProp("onload", onloadHandler);

        // maybe the onload handler was not called so far
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        img.doOnLoad();
    }

    /**
     * Returns the <tt>onload</tt> event handler for this element.
     * @return the <tt>onload</tt> event handler for this element
     */
    @JsxGetter
    public Object getOnload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Returns the value of the "alt" property.
     * @return the value of the "alt" property
     */
    @JsxGetter
    public String getAlt() {
        final String alt = getDomNodeOrDie().getAttribute("alt");
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    @JsxSetter
    public void setAlt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    @JsxGetter
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    @JsxSetter
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    @JsxGetter
    public String getAlign() {
        final boolean acceptArbitraryValues = getBrowserVersion().hasFeature(JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);

        final String align = getDomNodeOrDie().getAttribute("align");
        if (acceptArbitraryValues) {
            return align;
        }

        final String normalizedValue = NORMALIZED_ALIGN_VALUES.get(align.toLowerCase());
        if (null != normalizedValue) {
            return normalizedValue;
        }
        return "";
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    @JsxSetter
    public void setAlign(String align) {
        align = align.toLowerCase();
        final boolean acceptArbitraryValues = getBrowserVersion().hasFeature(JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
        if (acceptArbitraryValues) {
            getDomNodeOrDie().setAttribute("align", align);
            return;
        }

        final String normalizedValue = NORMALIZED_ALIGN_VALUES.get(align.toLowerCase());
        if (null != normalizedValue) {
            getDomNodeOrDie().setAttribute("align", normalizedValue);
            return;
        }

        throw Context.reportRuntimeError("Cannot set the align property to invalid value: '" + align + "'");
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @Override
    @JsxGetter
    public int getWidth() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        final String width = img.getWidthAttribute();
        try {
            return Integer.parseInt(width);
        }
        catch (final NumberFormatException e) {
            return 24; // anything else
        }
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    @JsxSetter
    public void setWidth(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the value of the "height" property.
     * @return the value of the "height" property
     */
    @Override
    @JsxGetter
    public int getHeight() {
        final HtmlImage img = (HtmlImage) getDomNodeOrDie();
        final String height = img.getHeightAttribute();
        try {
            return Integer.parseInt(height);
        }
        catch (final NumberFormatException e) {
            return 24; // anything else
        }
    }

    /**
     * Sets the value of the "height" property.
     * @param height the value of the "height" property
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
        return true;
    }
}
