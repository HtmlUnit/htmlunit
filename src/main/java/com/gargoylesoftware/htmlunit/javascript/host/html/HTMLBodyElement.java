/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_158;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_41;

import java.net.MalformedURLException;
import java.util.Locale;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;

/**
 * The JavaScript object "HTMLBodyElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
@JsxClass(domClass = HtmlBody.class)
public class HTMLBodyElement extends HTMLElement {

    /**
     * Creates the event handler from the attribute value. This has to be done no matter which browser
     * is simulated to handle ill-formed HTML code with many body (possibly generated) elements.
     * @param attributeName the attribute name
     * @param value the value
     */
    public void createEventHandlerFromAttribute(final String attributeName, final String value) {
        // when many body tags are found while parsing, attributes of
        // different tags are added and should create an event handler when needed
        if (attributeName.toLowerCase(Locale.ENGLISH).startsWith("on")) {
            createEventHandler(attributeName, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(final ComputedCSSStyleDeclaration style) {
        if (getBrowserVersion().hasFeature(GENERATED_41)) {
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
    public HTMLElement getOffsetParent_js() {
        return null;
    }

    /**
     * Returns the value of the <tt>aLink</tt> attribute.
     * @return the value of the <tt>aLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533070.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getALink() {
        return getDomNodeOrDie().getAttribute("aLink");
    }

    /**
     * Sets the value of the <tt>aLink</tt> attribute.
     * @param aLink the value of the <tt>aLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533070.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setALink(final String aLink) {
        setColorAttribute("aLink", aLink);
    }

    /**
     * Returns the value of the <tt>background</tt> attribute.
     * @return the value of the <tt>background</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533498.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getBackground() {
        final HtmlElement node = getDomNodeOrDie();
        String background = node.getAttribute("background");
        if (background != DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(GENERATED_158)) {
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
    @JsxSetter
    public void setBackground(final String background) {
        getDomNodeOrDie().setAttribute("background", background);
    }

    /**
     * Returns the value of the <tt>bgColor</tt> attribute.
     * @return the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getBgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the <tt>bgColor</tt> attribute.
     * @param bgColor the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setBgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Returns the value of the <tt>link</tt> attribute.
     * @return the value of the <tt>link</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534119.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getLink() {
        return getDomNodeOrDie().getAttribute("link");
    }

    /**
     * Sets the value of the <tt>link</tt> attribute.
     * @param link the value of the <tt>link</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534119.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setLink(final String link) {
        setColorAttribute("link", link);
    }

    /**
     * Returns the value of the <tt>text</tt> attribute.
     * @return the value of the <tt>text</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @Override
    @JsxGetter
    public String getText() {
        return getDomNodeOrDie().getAttribute("text");
    }

    /**
     * Sets the value of the <tt>text</tt> attribute.
     * @param text the value of the <tt>text</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setText(final String text) {
        setColorAttribute("text", text);
    }

    /**
     * Returns the value of the <tt>vLink</tt> attribute.
     * @return the value of the <tt>vLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getVLink() {
        return getDomNodeOrDie().getAttribute("vLink");
    }

    /**
     * Sets the value of the <tt>vLink</tt> attribute.
     * @param vLink the value of the <tt>vLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setVLink(final String vLink) {
        setColorAttribute("vLink", vLink);
    }

}
