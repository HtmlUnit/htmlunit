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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMText.<br>
 * Represents the text content of an element or attribute.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms757862.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Chuck Dumont
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = DomText.class, browsers = @WebBrowser(IE))
public class XMLDOMText extends XMLDOMCharacterData {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMText() {
    }

    /**
     * Returns the text contained in the node.
     * @return the text contained in the node
     */
    @Override
    public Object getText() {
        final DomText domText = getDomNodeOrDie();
        return domText.getWholeText();
    }

    /**
     * Splits this text node into two text nodes at the specified offset and inserts the new text node into the tree
     * as a sibling that immediately follows this node.
     * @param offset the number of characters at which to split this text node into two nodes, starting from zero
     * @return the new text node
     */
    @JsxFunction
    public Object splitText(final int offset) {
        if (offset < 0) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }

        final DomText domText = getDomNodeOrDie();
        if (offset > domText.getLength()) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }

        return getScriptableFor(domText.splitText(offset));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomText getDomNodeOrDie() {
        return (DomText) super.getDomNodeOrDie();
    }
}
