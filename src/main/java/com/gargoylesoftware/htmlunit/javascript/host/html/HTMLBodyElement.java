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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.net.MalformedURLException;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;

/**
 * The JavaScript object "HTMLBodyElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
public class HTMLBodyElement extends HTMLElement {

    private static final long serialVersionUID = -915040139319661419L;

    /**
     * Creates a new instance.
     */
    public HTMLBodyElement() {
        // Empty.
    }

    /**
     * Creates the event handler from the attribute value. This has to be done no matter which browser
     * is simulated to handle ill-formed HTML code with many body (possibly generated) elements.
     * @param attributeName the attribute name
     * @param value the value
     */
    public void createEventHandlerFromAttribute(final String attributeName, final String value) {
        // when many body tags are found while parsing, attributes of
        // different tags are added and should create an event handler when needed
        if (attributeName.toLowerCase().startsWith("on")) {
            createEventHandler(attributeName, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(final ComputedCSSStyleDeclaration style) {
        if (getBrowserVersion().isIE()) {
            style.setDefaultLocalStyleAttribute("margin", "15px 10px");
            style.setDefaultLocalStyleAttribute("padding", "0px");
        }
        else {
            style.setDefaultLocalStyleAttribute("margin-left", "8px");
            style.setDefaultLocalStyleAttribute("margin-right", "8px");
            style.setDefaultLocalStyleAttribute("margin-top", "8px");
            style.setDefaultLocalStyleAttribute("margin-bottom", "8px");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLElement jsxGet_offsetParent() {
        return null;
    }

    /**
     * Returns the value of the <tt>aLink</tt> attribute.
     * @return the value of the <tt>aLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533070.aspx">MSDN Documentation</a>
     */
    public String jsxGet_aLink() {
        String aLink = getDomNodeOrDie().getAttribute("aLink");
        if (aLink == DomElement.ATTRIBUTE_NOT_DEFINED && getBrowserVersion().isFirefox()) {
            aLink = "#ee0000";
        }
        return aLink;
    }

    /**
     * Sets the value of the <tt>aLink</tt> attribute.
     * @param aLink the value of the <tt>aLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533070.aspx">MSDN Documentation</a>
     */
    public void jsxSet_aLink(final String aLink) {
        final String s;
        if (isColorHexadecimal(aLink)) {
            s = aLink;
        }
        else {
            s = "#000000";
        }
        this.getDomNodeOrDie().setAttribute("aLink", s);
    }

    /**
     * Returns the value of the <tt>background</tt> attribute.
     * @return the value of the <tt>background</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533498.aspx">MSDN Documentation</a>
     */
    public String jsxGet_background() {
        final HtmlElement node = getDomNodeOrDie();
        String background = node.getAttribute("background");
        if (background != DomElement.ATTRIBUTE_NOT_DEFINED && getBrowserVersion().isFirefox()) {
            try {
                final HtmlPage page = (HtmlPage) node.getPage();
                background = page.getFullyQualifiedUrl(background).toExternalForm();
            }
            catch (final MalformedURLException e) {
                Context.throwAsScriptRuntimeEx(e);
            }
        }
        return background;
    }

    /**
     * Sets the value of the <tt>background</tt> attribute.
     * @param background the value of the <tt>background</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533498.aspx">MSDN Documentation</a>
     */
    public void jsxSet_background(final String background) {
        this.getDomNodeOrDie().setAttribute("background", background);
    }

    /**
     * Returns the value of the <tt>bgColor</tt> attribute.
     * @return the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    public String jsxGet_bgColor() {
        String bgColor = getDomNodeOrDie().getAttribute("bgColor");
        if (bgColor == DomElement.ATTRIBUTE_NOT_DEFINED && getBrowserVersion().isFirefox()) {
            bgColor = "#ffffff";
        }
        return bgColor;
    }

    /**
     * Sets the value of the <tt>bgColor</tt> attribute.
     * @param bgColor the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    public void jsxSet_bgColor(final String bgColor) {
        final String s;
        if (isColorHexadecimal(bgColor)) {
            s = bgColor;
        }
        else {
            s = "#000000";
        }
        this.getDomNodeOrDie().setAttribute("bgColor", s);
    }

    /**
     * Returns <tt>true</tt> if the specified string is an RGB in hexadecimal notation.
     * @param s the string to check
     * @return <tt>true</tt> if the specified string is an RGB in hexadecimal notation
     */
    public static boolean isColorHexadecimal(final String s) {
        return s.toLowerCase().matches("#([0-9a-f]{6})");
    }

}
