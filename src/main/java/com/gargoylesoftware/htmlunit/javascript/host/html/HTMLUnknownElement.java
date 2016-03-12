/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_HTML_HYPHEN_ELEMENT_CLASS_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_HTML_RUBY_ELEMENT_CLASS_NAME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlRp;
import com.gargoylesoftware.htmlunit.html.HtmlRt;
import com.gargoylesoftware.htmlunit.html.HtmlRuby;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * The JavaScript object {@code HTMLUnknownElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlUnknownElement.class)
public class HTMLUnknownElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLUnknownElement() {
    }

    /**
     * Gets the JavaScript property {@code nodeName} for the current node.
     * @return the node name
     */
    @Override
    public String getNodeName() {
        final HtmlElement elem = getDomNodeOrDie();
        final Page page = elem.getPage();
        if (page instanceof XmlPage) {
            return elem.getLocalName();
        }
        return super.getNodeName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        if (getWindow().getWebWindow() != null) {
            final HtmlElement element = getDomNodeOrNull();
            if (element != null) {
                final String name = element.getNodeName();
                if (getBrowserVersion().hasFeature(JS_HTML_RUBY_ELEMENT_CLASS_NAME)
                        && (HtmlRp.TAG_NAME.equals(name)
                                || HtmlRt.TAG_NAME.equals(name)
                                || HtmlRuby.TAG_NAME.equals(name)
                                || "rb".equals(name)
                                || "rtc".equals(name))) {
                    return "HTMLElement";
                }

                if (name.indexOf('-') != -1
                    && getBrowserVersion().hasFeature(JS_HTML_HYPHEN_ELEMENT_CLASS_NAME)) {
                    return "HTMLElement";
                }
            }
        }
        return super.getClassName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isLowerCaseInOuterHtml() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        if ("IMAGE".equals(getNodeName())) {
            return true;
        }
        return super.isEndTagForbidden();
    }
}
