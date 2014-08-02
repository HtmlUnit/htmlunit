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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_NOSCRIPT_DISPLAY_INLINE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.NOSCRIPT_BODY_AS_TEXT;

import java.util.Map;

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Wrapper for the HTML element "noscript".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlNoScript extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "noscript";

    /**
     * Creates an instance of HtmlNoScript
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlNoScript(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    @Override
    public DomNode appendChild(final Node node) {
        final WebClient webClient = getPage().getWebClient();
        if (!webClient.getOptions().isJavaScriptEnabled()
            || webClient.getBrowserVersion().hasFeature(NOSCRIPT_BODY_AS_TEXT)) {
            return super.appendChild(node);
        }
        return null;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the default display style.
     *
     * @return the default display style.
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        if (!getPage().getWebClient().getOptions().isJavaScriptEnabled()) {
            return DisplayStyle.BLOCK;
        }
        if (hasFeature(CSS_NOSCRIPT_DISPLAY_INLINE)) {
            return DisplayStyle.INLINE;
        }
        return DisplayStyle.NONE;
    }

    /**
     * Indicates if a node without children should be written in expanded form as XML
     * (i.e. with closing tag rather than with "/&gt;")
     * @return <code>true</code> to make generated XML readable as HTML
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

}
