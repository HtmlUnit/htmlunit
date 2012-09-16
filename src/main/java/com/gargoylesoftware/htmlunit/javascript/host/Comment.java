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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.annotations.BrowserName.IE;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.annotations.WebBrowser;

/**
 * A JavaScript object for a Comment.
 *
 * JavaScript: in IE, Comment is Element, but in FF: Comment is CharacterDataImpl.
 *
 * However, in DOM, Comment is CharacterDataImpl.
 *
 * @version $Revision$
 * @author Mirko Friedenhagen
 * @author Ahmed Ashour
 */
public final class Comment extends CharacterDataImpl {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Comment() {
    }

    /**
     * Returns the element ID.
     * @return the ID of this element
     */
    @JsxGetter(@WebBrowser(IE))
    public String jsxGet_id() {
        return "";
    }

    /**
     * Returns the class defined for this element.
     * @return the class name
     */
    @JsxGetter(@WebBrowser(IE))
    public Object jsxGet_className() {
        return "";
    }

    /**
     * Returns the tag name of this element.
     * @return the tag name
     */
    @JsxGetter(@WebBrowser(IE))
    public Object jsxGet_tagName() {
        return "!";
    }

    /**
     * Returns the text of this element.
     * @return the text
     */
    @JsxGetter(@WebBrowser(IE))
    public String jsxGet_text() {
        return "<!--" + jsxGet_data() + "-->";
    }

    /**
     * Returns the document of this element.
     * @return the document
     */
    @JsxGetter(@WebBrowser(IE))
    public Object jsxGet_document() {
        return getWindow().jsxGet_document();
    }

    /**
     * Gets the attribute node for the specified attribute.
     * @param attributeName the name of the attribute to retrieve
     * @return the attribute node for the specified attribute
     */
    @JsxFunction(@WebBrowser(IE))
    public Object jsxFunction_getAttributeNode(final String attributeName) {
        return null;
    }

    /**
     * Returns the value of the specified attribute.
     * @param attributeName attribute name
     * @param flags IE-specific flags (see the MSDN documentation for more info)
     * @return the value of the specified attribute, <code>null</code> if the attribute is not defined
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536429.aspx">MSDN Documentation</a>
     * @see <a href="http://reference.sitepoint.com/javascript/Element/getAttribute">IE Bug Documentation</a>
     */
    @JsxFunction(@WebBrowser(IE))
    public Object jsxFunction_getAttribute(final String attributeName, final Integer flags) {
        return null;
    }

    /**
     * Gets the innerText attribute.
     * @return the innerText
     */
    @JsxGetter(@WebBrowser(IE))
    public String jsxGet_innerText() {
        return "";
    }

    /**
     * Currently does nothing.
     * @param value the new value for the contents of this node
     */
    @JsxSetter(@WebBrowser(IE))
    public void jsxSet_innerText(final String value) {
        // nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        if (getWindow().getWebWindow() != null
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_COMMENT_ELEMENT)) {
            return "HTMLCommentElement";
        }
        return super.getClassName();
    }
}
