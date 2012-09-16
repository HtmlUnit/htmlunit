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

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.xalan.xsltc.runtime.AttributeList;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxSetter;

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
    public void jsxSet_src(final String src) {
        final HtmlElement img = getDomNodeOrDie();
        img.setAttribute("src", src);
        getWindow().getWebWindow().getWebClient()
            .getJavaScriptEngine().addPostponedAction(new ImageOnLoadAction(img.getPage()));
    }

    /**
     * Returns the value of the <tt>src</tt> attribute.
     * @return the value of the <tt>src</tt> attribute
     */
    @JsxGetter
    public String jsxGet_src() {
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
    public void jsxSet_onload(final Object onloadHandler) {
        setEventHandlerProp("onload", onloadHandler);
        getWindow().getWebWindow().getWebClient()
            .getJavaScriptEngine().addPostponedAction(new ImageOnLoadAction(getDomNodeOrDie().getPage()));
    }

    /**
     * Returns the <tt>onload</tt> event handler for this element.
     * @return the <tt>onload</tt> event handler for this element
     */
    @JsxGetter
    public Object jsxGet_onload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Custom JavaScript postponed action which downloads the image and invokes the onload handler, if necessary.
     */
    private class ImageOnLoadAction extends PostponedAction {
        public ImageOnLoadAction(final Page page) {
            super(page);
        }
        @Override
        public void execute() throws Exception {
            final HtmlImage img = (HtmlImage) getDomNodeOrNull();
            if (img != null) {
                img.doOnLoad();
            }
        }
    }

    /**
     * Returns the value of the "alt" property.
     * @return the value of the "alt" property
     */
    @JsxGetter
    public String jsxGet_alt() {
        final String alt = getDomNodeOrDie().getAttribute("alt");
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    @JsxSetter
    public void jsxSet_alt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    @JsxGetter
    public String jsxGet_border() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    @JsxSetter
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    @JsxGetter
    public String jsxGet_align() {
        final boolean acceptArbitraryValues =
                getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);

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
    public void jsxSet_align(String align) {
        align = align.toLowerCase();
        final boolean acceptArbitraryValues =
                getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
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
    @JsxGetter
    public int jsxGet_width() {
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
    public void jsxSet_width(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the value of the "height" property.
     * @return the value of the "height" property
     */
    @JsxGetter
    public int jsxGet_height() {
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
    public void jsxSet_height(final String height) {
        getDomNodeOrDie().setAttribute("height", height);
    }
}
