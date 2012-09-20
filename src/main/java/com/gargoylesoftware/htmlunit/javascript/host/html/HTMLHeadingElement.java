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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object "HTMLHeadingElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass
public class HTMLHeadingElement extends HTMLElement {
    /*    Because it is associated with many HtmlClasses, the one-to-many
    configurations are in JavaScriptConfiguration.getHtmlJavaScriptMapping() */

    /**
     * Creates an instance.
     */
    public HTMLHeadingElement() {
        // Empty.
    }

    /**
     * Returns the value of the <tt>align</tt> property.
     * @return the value of the <tt>align</tt> property
     */
    @JsxGetter
    public String jsxGet_align() {
        return getAlign(false);
    }

    /**
     * Sets the value of the <tt>align</tt> property.
     * @param align the value of the <tt>align</tt> property
     */
    @JsxSetter
    public void jsxSet_align(final String align) {
        setAlign(align, false);
    }
}
