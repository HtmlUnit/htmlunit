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
package com.gargoylesoftware.htmlunit.javascript.host.activex;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMComment.<br>
 * Represents the content of an XML comment.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms765529.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Mirko Friedenhagen
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(domClass = DomComment.class, browsers = @WebBrowser(IE))
public final class XMLDOMComment extends XMLDOMCharacterData {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMComment() {
    }

    /**
     * Returns the text contained in the node.
     * @return the text contained in the node
     */
    @Override
    public String getText() {
        return (String) getData();
    }
}
