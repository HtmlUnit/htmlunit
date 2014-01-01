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

import com.gargoylesoftware.htmlunit.html.HtmlNoEmbed;
import com.gargoylesoftware.htmlunit.html.HtmlNoFrames;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object "HTMLNoShowElement".
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@JsxClasses({
    @JsxClass(domClass = HtmlNoEmbed.class, browsers = @WebBrowser(value = IE, maxVersion = 8)),
    @JsxClass(domClass = HtmlNoFrames.class, browsers = @WebBrowser(value = IE, maxVersion = 8)),
    @JsxClass(domClass = HtmlNoScript.class, browsers = @WebBrowser(value = IE, maxVersion = 8))
})
public class HTMLNoShowElement extends HTMLElement {
    // nothing so far
}
