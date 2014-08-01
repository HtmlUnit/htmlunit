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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_112;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_HTML_GENERIC_ELEMENT_CLASS_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_HTML_HYPHEN_ELEMENT_CLASS_NAME;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * The JavaScript object "HTMLUnknownElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlUnknownElement.class)
public class HTMLUnknownElement extends HTMLElement {

    /**
     * Gets the JavaScript property "nodeName" for the current node.
     * @return the node name
     */
    @Override
    public String getNodeName() {
        final HtmlElement elem = getDomNodeOrDie();
        final Page page = elem.getPage();
        if (page instanceof XmlPage || (getBrowserVersion().hasFeature(GENERATED_112)
            && ((HtmlPage) page).getNamespaces().containsKey(elem.getPrefix()))) {
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
            if (getBrowserVersion().hasFeature(JS_HTML_GENERIC_ELEMENT_CLASS_NAME)) {
                return "HTMLGenericElement";
            }
            if (getTagName().indexOf('-') != -1
                    && getBrowserVersion().hasFeature(JS_HTML_HYPHEN_ELEMENT_CLASS_NAME)) {
                return "HTMLElement";
            }
        }
        return super.getClassName();
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isLowerCaseInOuterHtml() {
        return true;
    }
}
