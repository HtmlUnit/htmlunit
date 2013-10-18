/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DISPLAY_DEFAULT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_112;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_HTML_GENERIC_ELEMENT_CLASS_NAME;

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
@JsxClass(domClasses = HtmlUnknownElement.class)
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
        if (getWindow().getWebWindow() != null && getBrowserVersion().hasFeature(JS_HTML_GENERIC_ELEMENT_CLASS_NAME)) {
            return "HTMLGenericElement";
        }
        return super.getClassName();
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isLowerCaseInOuterHtml() {
        return true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * {@inheritDoc}
    */
    @Override
    public String getDefaultStyleDisplay() {
        final String tagName = getTagName();
        if (getBrowserVersion().hasFeature(CSS_DISPLAY_DEFAULT)) {
            if ("ARTICLE".equals(tagName)
                    || "ASIDE".equals(tagName)
                    || "FIGCAPTION".equals(tagName)
                    || "FIGURE".equals(tagName)
                    || "FOOTER".equals(tagName)
                    || "HEADER".equals(tagName)
                    || "NAV".equals(tagName)
                    || "SECTION".equals(tagName)) {
                return "block";
            }

            // FF 3.6
            if ("METER".equals(tagName)) {
                return "inline-block";
            }
            if ("PROGRESS".equals(tagName)) {
                return "inline-block";
            }
        }
        if ("RUBY".equals(tagName)) {
            if (getBrowserVersion().hasFeature(CSS_DISPLAY_DEFAULT)) {
                return "inline";
            }
            return "ruby";
        }
        if ("RT".equals(tagName)) {
            if (getBrowserVersion().hasFeature(CSS_DISPLAY_DEFAULT)) {
                return "inline";
            }
            return "ruby-text";
        }
        return "inline";
    }
}
