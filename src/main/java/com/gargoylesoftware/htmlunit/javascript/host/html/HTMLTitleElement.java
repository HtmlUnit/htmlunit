/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;

/**
 * The JavaScript object "HTMLTitleElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 */
public class HTMLTitleElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLTitleElement() {
        // Empty.
    }

    /**
     * Returns the <tt>text</tt> attribute.
     * @return the <tt>text</tt> attribute
     */
    @Override
    public String jsxGet_text() {
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
    public void jsxSet_text(final String text) {
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
}
