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

/**
 * The JavaScript object "HTMLHtmlElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HTMLHtmlElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLHtmlElement() {
        // Empty.
    }

    /** {@inheritDoc} */
    @Override
    public Object jsxGet_parentNode() {
        return getWindow().jsxGet_document();
    }

    /** {@inheritDoc} */
    @Override
    public int jsxGet_clientWidth() {
        return getWindow().jsxGet_innerWidth();
    }

    /** {@inheritDoc} */
    @Override
    public int jsxGet_clientHeight() {
        return getWindow().jsxGet_innerHeight();
    }
}
