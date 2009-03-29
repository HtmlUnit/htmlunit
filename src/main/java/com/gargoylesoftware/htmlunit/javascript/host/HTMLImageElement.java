/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.html.HtmlImage.TAG_NAME;

import java.net.MalformedURLException;

import org.apache.xalan.xsltc.runtime.AttributeList;
import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;

/**
 * The JavaScript object that represents an "Image".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLImageElement extends HTMLElement {

    private static final long serialVersionUID = 5630843390548382869L;

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
        final HtmlElement fake = HTMLParser.getFactory(TAG_NAME).createElement(page, TAG_NAME, new AttributeList());
        setDomNode(fake);
    }

    /**
     * Sets the <tt>src</tt> attribute.
     * @param src the <tt>src</tt> attribute value
     */
    public void jsxSet_src(final String src) {
        getDomNodeOrDie().setAttribute("src", src);
        getWindow().getJavaScriptEngine().addPostponedAction(new ImageOnLoadAction());
    }

    /**
     * Returns the value of the <tt>src</tt> attribute.
     * @return the value of the <tt>src</tt> attribute
     */
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
    public void jsxSet_onload(final Object onloadHandler) {
        setEventHandlerProp("onload", onloadHandler);
        getWindow().getJavaScriptEngine().addPostponedAction(new ImageOnLoadAction());
    }

    /**
     * Returns the <tt>onload</tt> event handler for this element.
     * @return the <tt>onload</tt> event handler for this element
     */
    public Object jsxGet_onload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Custom JavaScript postponed action which downloads the image and invokes the onload handler, if necessary.
     */
    private class ImageOnLoadAction implements PostponedAction {
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
    public String jsxGet_alt() {
        String alt = getDomNodeOrDie().getAttribute("alt");
        if (alt == NOT_FOUND) {
            alt = "";
        }
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    public void jsxSet_alt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    public String jsxGet_border() {
        String border = getDomNodeOrDie().getAttribute("border");
        if (border == NOT_FOUND) {
            border = "";
        }
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    public String jsxGet_align() {
        return getAlign();
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    public void jsxSet_align(final String align) {
        setAlign(align, false);
    }

}
