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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE10;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link DOMImplementation}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class DOMImplementationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "HTML 1.0: true", "HTML 2.0: true", "HTML 3.0: false" },
            IE6 = { "HTML 1.0: true", "HTML 2.0: false", "HTML 3.0: false" },
            IE8 = { "HTML 1.0: true", "HTML 2.0: false", "HTML 3.0: false" })
    public void hasFeature_HTML() throws Exception {
        hasFeature("HTML", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "XML 1.0: true", "XML 2.0: true", "XML 3.0: false" },
            IE6 = { "XML 1.0: false", "XML 2.0: false", "XML 3.0: false" },
            IE8 = { "XML 1.0: false", "XML 2.0: false", "XML 3.0: false" })
    public void hasFeature_XML() throws Exception {
        hasFeature("XML", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "CSS2 1.0: false", "CSS2 2.0: true", "CSS2 3.0: false" },
            IE = { "CSS2 1.0: false", "CSS2 2.0: false", "CSS2 3.0: false" })
    public void hasFeature_CSS2() throws Exception {
        hasFeature("CSS2", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "CSS3 1.0: false", "CSS3 2.0: false", "CSS3 3.0: false" },
            IE = { "CSS3 1.0: false", "CSS3 2.0: false", "CSS3 3.0: false" })
    public void hasFeature_CSS3() throws Exception {
        hasFeature("CSS3", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "XPath 3.0: true",
            IE = "XPath 3.0: false")
    public void hasFeature_XPath() throws Exception {
        hasFeature("XPath", "['3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.0: true",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.1: true",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.2: false" },
            IE6 = { "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.0: false",
                "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.1: false",
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
            IE6 = { "http://www.w3.org/TR/SVG11/feature#Shape 1.0: false",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.1: false",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.2: false" },
            IE8 = { "http://www.w3.org/TR/SVG11/feature#Shape 1.0: false",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.1: false",
                "http://www.w3.org/TR/SVG11/feature#Shape 1.2: false" })
    public void hasFeature_SVG_Shape() throws Exception {
        hasFeature("http://www.w3.org/TR/SVG11/feature#Shape", "['1.0', '1.1', '1.2']");
    }

    private void hasFeature(final String feature, final String versions) throws Exception {
        final String html = "<html><head>\n"
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
    @Browsers({ CHROME, FF, IE10 })
    @Alerts(DEFAULT = "[object Document]",
            FF = "[object XMLDocument]")
    @NotYetImplemented(CHROME)
    public void createDocument() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.implementation.createDocument('', '', null));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ FF, CHROME, IE10 })
    @Alerts("mydoc")
    public void createDocument_qualifiedName() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', 'mydoc', null);\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
