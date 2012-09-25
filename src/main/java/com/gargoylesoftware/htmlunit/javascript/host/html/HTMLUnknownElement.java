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

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * The JavaScript object "HTMLUnknownElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(domClasses = HtmlUnknownElement.class)
public class HTMLUnknownElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLUnknownElement() {
        // Empty.
    }

    /**
     * Gets the JavaScript property "nodeName" for the current node.
     * @return the node name
     */
    @Override
    public String getNodeName() {
        final Page page = getDomNodeOrDie().getPage();
        if (page instanceof XmlPage || (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_112)
            && ((HtmlPage) page).getNamespaces().containsKey(getDomNodeOrDie().getPrefix()))) {
            return getDomNodeOrDie().getLocalName();
        }
        return super.getNodeName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        if (getWindow().getWebWindow() != null
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_GENERIC_ELEMENT)) {
            return "HTMLGenericElement";
        }
        return super.getClassName();
    }
}
