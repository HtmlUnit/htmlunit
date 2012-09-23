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

import com.gargoylesoftware.htmlunit.html.HtmlBase;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object "HTMLBaseElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(domClass = HtmlBase.class)
public class HTMLBaseElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLBaseElement() {
        // Empty.
    }

    /**
     * Returns the value of the "href" property.
     * @return the value of the "href" property
     */
    @JsxGetter
    public String getHref() {
        return getDomNodeOrDie().getAttribute("href");
    }

    /**
     * Sets the value of the "href" property.
     * @param href the value of the "href" property
     */
    @JsxSetter
    public void setHref(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of the "target" property.
     * @return the value of the "target" property
     */
    @JsxGetter
    public String getTarget() {
        return getDomNodeOrDie().getAttribute("target");
    }

    /**
     * Sets the value of the "target" property.
     * @param target the value of the "target" property
     */
    @JsxSetter
    public void setTarget(final String target) {
        getDomNodeOrDie().setAttribute("target", target);
    }

}
