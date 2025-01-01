/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.html;

import org.htmlunit.Page;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlRb;
import org.htmlunit.html.HtmlRp;
import org.htmlunit.html.HtmlRt;
import org.htmlunit.html.HtmlRtc;
import org.htmlunit.html.HtmlRuby;
import org.htmlunit.html.HtmlUnknownElement;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.xml.XmlPage;

/**
 * The JavaScript object {@code HTMLUnknownElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlUnknownElement.class)
public class HTMLUnknownElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
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
                if (HtmlRb.TAG_NAME.equals(name)
                                || HtmlRp.TAG_NAME.equals(name)
                                || HtmlRt.TAG_NAME.equals(name)
                                || HtmlRtc.TAG_NAME.equals(name)
                                || HtmlRuby.TAG_NAME.equals(name)) {
                    return "HTMLElement";
                }

                if (name.indexOf('-') != -1) {
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
        final String nodeName = getNodeName();
        if ("BGSOUND".equals(nodeName) || "KEYGEN".equals(nodeName)) {
            return true;
        }
        return super.isEndTagForbidden();
    }
}
