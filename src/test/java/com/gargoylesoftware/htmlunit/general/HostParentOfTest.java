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
package com.gargoylesoftware.htmlunit.general;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HostParentOfTest extends WebDriverTestCase {

    private void isParentOf(final String parent, final String child) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(isParentOf(" + parent + ", " + child + "));\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"

            + "  /*\n"
            + "   * Returns true if o1 prototype is parent/grandparent of o2 prototype\n"
            + "   */\n"
            + "  function isParentOf(o1, o2) {\n"
            + "    o1.prototype.myCustomFunction = function() {};\n"
            + "    return o2.prototype.myCustomFunction != undefined;\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "exception")
    public void _Node_Element() throws Exception {
        isParentOf("Node", "Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "exception")
    public void _Document_XMLDocument() throws Exception {
        isParentOf("Document", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "exception")
    public void _Node_XPathResult() throws Exception {
        isParentOf("Node", "XPathResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "exception")
    public void _Element_HTMLElement() throws Exception {
        isParentOf("Element", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "exception")
    public void _HTMLElement_HTMLHtmlElement() throws Exception {
        isParentOf("HTMLElement", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _CSSStyleDeclaration_ComputedCSSStyleDeclaration() throws Exception {
        isParentOf("CSSStyleDeclaration", "ComputedCSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false")
    public void _Image_HTMLImageElement() throws Exception {
        //although Image != HTMLImageElement, they seem to be synonyms!!!
        isParentOf("Image", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLImageElement_Image() throws Exception {
        //although Image != HTMLImageElement, they seem to be synonyms!!!
        isParentOf("HTMLImageElement", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false")
    public void _Option_HTMLOptionElement() throws Exception {
        //although Option != HTMLOptionElement, they seem to be synonyms!!!
        isParentOf("Option", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOptionElement_Option() throws Exception {
        //although Option != HTMLOptionElement, they seem to be synonyms!!!
        isParentOf("HTMLOptionElement", "Option");
    }
}
