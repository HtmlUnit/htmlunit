/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * The JavaScript object "HTMLHtmlElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLHtmlElement extends HTMLElement {

    private static final long serialVersionUID = 4942983761903195465L;

    /**
     * Creates an instance.
     */
    public HTMLHtmlElement() {
        // Empty.
    }

    /**
     * <p>Overridden because this <tt>childNodes</tt> collection should only ever contain
     * the <tt>head</tt> and <tt>body</tt> nodes (in HTML pages).</p>
     *
     * {@inheritDoc}
     */
    public Object jsxGet_childNodes() {
        // XML behavior
        if (getDomNodeOrDie().getPage() instanceof XmlPage) {
            return super.jsxGet_childNodes();
        }
        // HTML behavior
        if (childNodes_ == null) {
            childNodes_ = new HTMLCollection(this);
            childNodes_.init(getDomNodeOrDie(), "./*[name() = 'head' or name() = 'body']");
        }
        return childNodes_;
    }

}
