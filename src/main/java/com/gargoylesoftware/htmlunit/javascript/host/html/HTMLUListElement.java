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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.HtmlDirectory;
import com.gargoylesoftware.htmlunit.html.HtmlMenu;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object "HTMLUListElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClasses({
        @JsxClass(domClass = HtmlDirectory.class, browsers = { @WebBrowser(value = IE, maxVersion = 8) }),
        @JsxClass(domClass = HtmlUnorderedList.class),
        @JsxClass(domClass = HtmlMenu.class, browsers = { @WebBrowser(value = IE, maxVersion = 8) }) })
public class HTMLUListElement extends HTMLListElement {

    /**
     * Returns the value of the <tt>type</tt> attribute.
     * @return the value of the <tt>type</tt> attribute
     */
    @JsxGetter
    public String getType() {
        return super.getType();
    }

    /**
     * Sets the value of the <tt>type</tt> attribute.
     * @param type the value of the <tt>type</tt> attribute
     */
    @JsxSetter
    public void setType(final String type) {
        super.setType(type);
    }
}
