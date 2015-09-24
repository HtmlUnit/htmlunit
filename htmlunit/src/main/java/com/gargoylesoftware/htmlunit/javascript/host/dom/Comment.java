/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code Comment}.
 *
 * JavaScript: in IE, Comment is Element, but in FF: Comment is CharacterDataImpl.
 *
 * However, in DOM, Comment is CharacterDataImpl.
 *
 * @author Mirko Friedenhagen
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClasses({
        @JsxClass(domClass = DomComment.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11),
                    @WebBrowser(EDGE) }),
        @JsxClass(isJSObject = false, isDefinedInStandardsMode = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8))
    })
public class Comment extends CharacterData {

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public Comment() {
    }

    /**
     * Returns the element ID.
     * @return the ID of this element
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getId() {
        return "";
    }

    /**
     * Returns the class defined for this element.
     * @return the class name
     */
    @JsxGetter(value = @WebBrowser(value = IE, maxVersion = 8), propertyName = "className")
    public Object getClassName_js() {
        return "";
    }

    /**
     * Returns the tag name of this element.
     * @return the tag name
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public Object getTagName() {
        return "!";
    }

    /**
     * Returns the text of this element.
     * @return the text
     */
    @JsxGetter(@WebBrowser(IE))
    public String getText() {
        return "<!--" + getData() + "-->";
    }

    /**
     * Returns the document of this element.
     * @return the document
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public Object getDocument() {
        return getWindow().getDocument_js();
    }

    /**
     * Gets the attribute node for the specified attribute.
     * @param attributeName the name of the attribute to retrieve
     * @return the attribute node for the specified attribute
     */
    @JsxFunction(@WebBrowser(IE))
    public Object getAttributeNode(final String attributeName) {
        return null;
    }

    /**
     * Returns the value of the specified attribute.
     * @param attributeName attribute name
     * @param flags IE-specific flags (see the MSDN documentation for more info)
     * @return the value of the specified attribute, {@code null} if the attribute is not defined
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536429.aspx">MSDN Documentation</a>
     * @see <a href="http://reference.sitepoint.com/javascript/Element/getAttribute">IE Bug Documentation</a>
     */
    @JsxFunction(@WebBrowser(IE))
    public Object getAttribute(final String attributeName, final Integer flags) {
        return null;
    }

    /**
     * Gets the innerText attribute.
     * @return the innerText
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getInnerText() {
        return "";
    }

    /**
     * Currently does nothing.
     * @param value the new value for the contents of this node
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setInnerText(final String value) {
        // nothing
    }
}
