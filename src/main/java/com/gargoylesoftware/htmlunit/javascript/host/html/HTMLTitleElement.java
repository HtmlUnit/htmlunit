/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_READONLY_FOR_SOME_TAGS;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlTitle;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object "HTMLTitleElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlTitle.class)
public class HTMLTitleElement extends HTMLElement {

    /**
     * Returns the <tt>text</tt> attribute.
     * @return the <tt>text</tt> attribute
     */
    @Override
    @JsxGetter
    public String getText() {
        final DomNode firstChild = getDomNodeOrDie().getFirstChild();
        if (firstChild != null) {
            return firstChild.getNodeValue();
        }
        return "";
    }

    /**
     * Sets the <tt>text</tt> attribute.
     * @param text the <tt>text</tt> attribute
     */
    @JsxSetter
    public void setText(final String text) {
        final DomNode htmlElement = getDomNodeOrDie();
        DomNode firstChild = htmlElement.getFirstChild();
        if (firstChild == null) {
            firstChild = new DomText(htmlElement.getPage(), text);
            htmlElement.appendChild(firstChild);
        }
        else {
            firstChild.setNodeValue(text);
        }
    }

    /**
     * Overwritten to throw an exception in IE8/9.
     * @param value the new value for the contents of this node
     */
    @JsxSetter
    @Override
    public void setInnerHTML(final Object value) {
        if (getBrowserVersion().hasFeature(JS_INNER_HTML_READONLY_FOR_SOME_TAGS)) {
            throw Context.reportRuntimeError("innerHTML is read-only for tag 'title'");
        }
        super.setInnerHTML(value);
    }
}
