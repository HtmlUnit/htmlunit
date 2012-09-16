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

import com.gargoylesoftware.htmlunit.javascript.annotations.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.annotations.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;

/**
 * A JavaScript object for DocumentFragment.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 *
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-core.html#ID-B63ED1A3">
 * W3C Dom Level 1</a>
 */
public class DocumentFragment extends Node {

    //TODO: seems that in IE, DocumentFragment extends HTMLDocument

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jsxGet_xml() {
        final Node node = jsxGet_firstChild();
        if (node != null) {
            return node.jsxGet_xml();
        }
        return "";
    }

    /**
     * Creates a new HTML attribute with the specified name.
     *
     * @param attributeName the name of the attribute to create
     * @return an attribute with the specified name
     */
    @JsxFunction(@WebBrowser(IE))
    public Object jsxFunction_createAttribute(final String attributeName) {
        return getDocument().jsxFunction_createAttribute(attributeName);
    }

    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName the tag name
     * @return the new HTML element, or NOT_FOUND if the tag is not supported
     */
    @JsxFunction(@WebBrowser(IE))
    public Object jsxFunction_createElement(final String tagName) {
        return getDocument().jsxFunction_createElement(tagName);
    }

    /**
     * Returns HTML document.
     * @return HTML document
     */
    protected HTMLDocument getDocument() {
        return (HTMLDocument) getDomNodeOrDie().getPage().getScriptObject();
    }

    /**
     * Creates a new Comment.
     * @param comment the comment text
     * @return the new Comment
     */
    @JsxFunction(@WebBrowser(IE))
    public Object jsxFunction_createComment(final String comment) {
        return getDocument().jsxFunction_createComment(comment);
    }

    /**
     * Creates a new document fragment.
     * @return a newly created document fragment
     */
    @JsxFunction(@WebBrowser(IE))
    public Object jsxFunction_createDocumentFragment() {
        return getDocument().jsxFunction_createDocumentFragment();
    }

    /**
     * Create a new DOM text node with the given data.
     *
     * @param newData the string value for the text node
     * @return the new text node or NOT_FOUND if there is an error
     */
    @JsxFunction(@WebBrowser(IE))
    public Object jsxFunction_createTextNode(final String newData) {
        return getDocument().jsxFunction_createTextNode(newData);
    }
}
