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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

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
 * @author Ronald Brill
 */
@JsxClass(domClass = DomComment.class)
public class Comment extends CharacterData {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public Comment() {
    }

    /**
     * Returns the text of this element.
     * @return the text
     */
    @JsxGetter(IE)
    public String getText() {
        return "<!--" + getData() + "-->";
    }

    /**
     * Gets the attribute node for the specified attribute.
     * @param attributeName the name of the attribute to retrieve
     * @return the attribute node for the specified attribute
     */
    @JsxFunction(IE)
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
    @JsxFunction(IE)
    public Object getAttribute(final String attributeName, final Integer flags) {
        return null;
    }
}
