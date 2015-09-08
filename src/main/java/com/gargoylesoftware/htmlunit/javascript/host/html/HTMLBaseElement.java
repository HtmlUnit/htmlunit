/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.HtmlBase;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object {@code HTMLBaseElement}.
 *
 * @author Ahmed Ashour
 */
@JsxClasses({
        @JsxClass(domClass = HtmlBase.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF),
            @WebBrowser(value = IE, minVersion = 11) }),
        @JsxClass(isJSObject = false, domClass = HtmlBase.class,
            browsers = { @WebBrowser(value = IE, maxVersion = 8) })
    })
public class HTMLBaseElement extends HTMLElement {

    /**
     * The constructor.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public HTMLBaseElement() {
    }

    /**
     * Returns the value of the {@code href} property.
     * @return the value of the "href" property
     */
    @JsxGetter
    public String getHref() {
        return getDomNodeOrDie().getAttribute("href");
    }

    /**
     * Sets the value of the {@code href} property.
     * @param href the value of the "href" property
     */
    @JsxSetter
    public void setHref(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of the {@code target} property.
     * @return the value of the "target" property
     */
    @JsxGetter
    public String getTarget() {
        return getDomNodeOrDie().getAttribute("target");
    }

    /**
     * Sets the value of the {@code target} property.
     * @param target the value of the "target" property
     */
    @JsxSetter
    public void setTarget(final String target) {
        getDomNodeOrDie().setAttribute("target", target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
    }
}
