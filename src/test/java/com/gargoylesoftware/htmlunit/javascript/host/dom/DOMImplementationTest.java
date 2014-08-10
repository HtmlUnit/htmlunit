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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link DOMImplementation}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DOMImplementationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Core 1.0: true", "Core 2.0: true", "Core 3.0: true" },
            IE8 = { "Core 1.0: false", "Core 2.0: false", "Core 3.0: false" },
            IE11 = { "Core 1.0: true", "Core 2.0: true", "Core 3.0: false" })
    public void hasFeature_Core() throws Exception {
        hasFeature("Core", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "HTML 1.0: true", "HTML 2.0: true", "HTML 3.0: true" },
            IE11 = { "HTML 1.0: true", "HTML 2.0: true", "HTML 3.0: false" },
            IE8 = { "HTML 1.0: true", "HTML 2.0: false", "HTML 3.0: false" })
    public void hasFeature_HTML() throws Exception {
        hasFeature("HTML", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "XML 1.0: true", "XML 2.0: true", "XML 3.0: true" },
            IE11 = { "XML 1.0: true", "XML 2.0: true", "XML 3.0: false" },
            IE8 = { "XML 1.0: false", "XML 2.0: false", "XML 3.0: false" })
    public void hasFeature_XML() throws Exception {
        hasFeature("XML", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "XHTML 1.0: true", "XHTML 2.0: true", "XHTML 3.0: true" },
            IE8 = { "XHTML 1.0: false", "XHTML 2.0: false", "XHTML 3.0: false" },
            IE11 = { "XHTML 1.0: true", "XHTML 2.0: true", "XHTML 3.0: false" })
    public void hasFeature_XHTML() throws Exception {
        hasFeature("XHTML", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Views 1.0: true", "Views 2.0: true", "Views 3.0: true" },
            IE11 = { "Views 1.0: false", "Views 2.0: true", "Views 3.0: false" },
            IE8 = { "Views 1.0: false", "Views 2.0: false", "Views 3.0: false" })
    public void hasFeature_Views() throws Exception {
        hasFeature("Views", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "StyleSheets 1.0: true", "StyleSheets 2.0: true", "StyleSheets 3.0: true" },
            IE = { "StyleSheets 1.0: false", "StyleSheets 2.0: false", "StyleSheets 3.0: false" })
    public void hasFeature_StyleSheets() throws Exception {
        hasFeature("StyleSheets", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "CSS 1.0: true", "CSS 2.0: true", "CSS 3.0: true" },
            IE = { "CSS 1.0: false", "CSS 2.0: false", "CSS 3.0: false" })
    public void hasFeature_CSS() throws Exception {
        hasFeature("CSS", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "CSS2 1.0: true", "CSS2 2.0: true", "CSS2 3.0: true" },
            IE11 = { "CSS2 1.0: false", "CSS2 2.0: true", "CSS2 3.0: false" },
            IE8 = { "CSS2 1.0: false", "CSS2 2.0: false", "CSS2 3.0: false" })
    public void hasFeature_CSS2() throws Exception {
        hasFeature("CSS2", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "CSS3 1.0: true", "CSS3 2.0: true", "CSS3 3.0: true" },
            IE = { "CSS3 1.0: false", "CSS3 2.0: false", "CSS3 3.0: false" })
    public void hasFeature_CSS3() throws Exception {
        hasFeature("CSS3", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Events 1.0: true", "Events 2.0: true", "Events 3.0: true" },
            IE8 = { "Events 1.0: false", "Events 2.0: false", "Events 3.0: false" },
            IE11 = { "Events 1.0: false", "Events 2.0: true", "Events 3.0: true" })
    public void hasFeature_Events() throws Exception {
        hasFeature("Events", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "UIEvents 1.0: true", "UIEvents 2.0: true", "UIEvents 3.0: true" },
            IE8 = { "UIEvents 1.0: false", "UIEvents 2.0: false", "UIEvents 3.0: false" },
            IE11 = { "UIEvents 1.0: false", "UIEvents 2.0: false", "UIEvents 3.0: true" })
    public void hasFeature_UIEvents() throws Exception {
        hasFeature("UIEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "MouseEvents 1.0: true", "MouseEvents 2.0: true", "MouseEvents 3.0: true" },
            IE8 = { "MouseEvents 1.0: false", "MouseEvents 2.0: false", "MouseEvents 3.0: false" },
            IE11 = { "MouseEvents 1.0: false", "MouseEvents 2.0: true", "MouseEvents 3.0: true" })
    public void hasFeature_MouseEvents() throws Exception {
        hasFeature("MouseEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "TextEvents 1.0: true", "TextEvents 2.0: true", "TextEvents 3.0: true" },
            IE = { "TextEvents 1.0: false", "TextEvents 2.0: false", "TextEvents 3.0: false" })
    public void hasFeature_TextEvents() throws Exception {
        hasFeature("TextEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "KeyboardEvents 1.0: true", "KeyboardEvents 2.0: true", "KeyboardEvents 3.0: true" },
            IE = { "KeyboardEvents 1.0: false", "KeyboardEvents 2.0: false", "KeyboardEvents 3.0: false" })
    public void hasFeature_KeyboardEvents() throws Exception {
        hasFeature("KeyboardEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "MutationEvents 1.0: true", "MutationEvents 2.0: true", "MutationEvents 3.0: true" },
            IE8 = { "MutationEvents 1.0: false", "MutationEvents 2.0: false", "MutationEvents 3.0: false" },
            IE11 = { "MutationEvents 1.0: false", "MutationEvents 2.0: true", "MutationEvents 3.0: true" })
    public void hasFeature_MutationEvents() throws Exception {
        hasFeature("MutationEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
        DEFAULT = { "MutationNameEvents 1.0: true", "MutationNameEvents 2.0: true", "MutationNameEvents 3.0: true" },
        IE = { "MutationNameEvents 1.0: false", "MutationNameEvents 2.0: false", "MutationNameEvents 3.0: false" })
    public void hasFeature_MutationNameEvents() throws Exception {
        hasFeature("MutationNameEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "HTMLEvents 1.0: true", "HTMLEvents 2.0: true", "HTMLEvents 3.0: true" },
            IE8 = { "HTMLEvents 1.0: false", "HTMLEvents 2.0: false", "HTMLEvents 3.0: false" },
            IE11 = { "HTMLEvents 1.0: false", "HTMLEvents 2.0: true", "HTMLEvents 3.0: true" })
    public void hasFeature_HTMLEvents() throws Exception {
        hasFeature("HTMLEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Range 1.0: true", "Range 2.0: true", "Range 3.0: true" },
            IE11 = { "Range 1.0: false", "Range 2.0: true", "Range 3.0: false" },
            IE8 = { "Range 1.0: false", "Range 2.0: false", "Range 3.0: false" })
    public void hasFeature_Range() throws Exception {
        hasFeature("Range", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Traversal 1.0: true", "Traversal 2.0: true", "Traversal 3.0: true" },
            IE11 = { "Traversal 1.0: false", "Traversal 2.0: true", "Traversal 3.0: false" },
            IE8 = { "Traversal 1.0: false", "Traversal 2.0: false", "Traversal 3.0: false" })
    public void hasFeature_Traversal() throws Exception {
        hasFeature("Traversal", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "LS 1.0: true", "LS 2.0: true", "LS 3.0: true" },
            IE = { "LS 1.0: false", "LS 2.0: false", "LS 3.0: false" })
    public void hasFeature_LS() throws Exception {
        hasFeature("LS", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "LS-Async 1.0: true", "LS-Async 2.0: true", "LS-Async 3.0: true" },
            IE = { "LS-Async 1.0: false", "LS-Async 2.0: false", "LS-Async 3.0: false" })
    public void hasFeature_LSAsync() throws Exception {
        hasFeature("LS-Async", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Validation 1.0: true", "Validation 2.0: true", "Validation 3.0: true" },
            IE = { "Validation 1.0: false", "Validation 2.0: false", "Validation 3.0: false" })
    public void hasFeature_Validation() throws Exception {
        hasFeature("Validation", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "XPath 1.0: true", "XPath 2.0: true", "XPath 3.0: true" },
            IE = { "XPath 1.0: false", "XPath 2.0: false", "XPath 3.0: false" })
    public void hasFeature_XPath() throws Exception {
        hasFeature("XPath", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.0: true",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.1: true",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.2: false" },
            CHROME = { "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.0: false",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.1: true",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.2: false" },
            IE8 = { "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.0: false",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.1: false",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.2: false" })
    public void hasFeature_SVG_BasicStructure() throws Exception {
        hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "['1.0', '1.1', '1.2']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "http://www.w3.org/TR/SVG11/feature#Shape 1.0: true",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.1: true",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.2: false" },
            CHROME = { "http://www.w3.org/TR/SVG11/feature#Shape 1.0: false",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.1: true",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.2: false" },
            IE8 = { "http://www.w3.org/TR/SVG11/feature#Shape 1.0: false",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.1: false",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.2: false" })
    public void hasFeature_SVG_Shape() throws Exception {
        hasFeature("http://www.w3.org/TR/SVG11/feature#Shape", "['1.0', '1.1', '1.2']");
    }

    private void hasFeature(final String feature, final String versions) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var feature = '" + feature + "';\n"
            + "    var versions = " + versions + ";\n"
            + "    for (var j=0; j<versions.length; ++j) {\n"
            + "      var version = versions[j];\n"
            + "      alert(feature + ' ' + version + ': ' + document.implementation.hasFeature(feature, version));\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLDocument]", IE8 = { })
    public void createDocument() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    if (document.implementation.createDocument) {\n"
            + "      alert(document.implementation.createDocument('', '', null));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "mydoc", "null", "mydoc", "null" }, IE8 = { })
    public void createDocument_qualifiedName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    if (document.implementation.createDocument) {\n"
            + "      var doc = document.implementation.createDocument('', 'mydoc', null);\n"
            + "      alert(doc.documentElement.tagName);\n"
            + "      alert(doc.documentElement.prefix);\n"
            + "      alert(doc.documentElement.localName);\n"
            + "      alert(doc.documentElement.namespaceURI);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "mydoc", "null", "mydoc", "http://mynamespace" }, IE8 = { })
    public void createDocument_namespaceAndQualifiedName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    if (document.implementation.createDocument) {\n"
            + "      var doc = document.implementation.createDocument('http://mynamespace', 'mydoc', null);\n"
            + "      alert(doc.documentElement.tagName);\n"
            + "      alert(doc.documentElement.prefix);\n"
            + "      alert(doc.documentElement.localName);\n"
            + "      alert(doc.documentElement.namespaceURI);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "m:mydoc", "m", "mydoc", "http://mynamespace" }, IE8 = { })
    public void createDocument_namespaceAndQualifiedNameWithPrefix() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    if (document.implementation.createDocument) {\n"
            + "      var doc = document.implementation.createDocument('http://mynamespace', 'm:mydoc', null);\n"
            + "      alert(doc.documentElement.tagName);\n"
            + "      alert(doc.documentElement.prefix);\n"
            + "      alert(doc.documentElement.localName);\n"
            + "      alert(doc.documentElement.namespaceURI);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
