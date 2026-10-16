/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link DOMImplementation}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 * @author Adam Afeltowicz
 */
public class DOMImplementationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Core 1.0: true", "Core 2.0: true", "Core 3.0: true"})
    public void hasFeature_Core() throws Exception {
        hasFeature("Core", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HTML 1.0: true", "HTML 2.0: true", "HTML 3.0: true"})
    public void hasFeature_HTML() throws Exception {
        hasFeature("HTML", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"XML 1.0: true", "XML 2.0: true", "XML 3.0: true"})
    public void hasFeature_XML() throws Exception {
        hasFeature("XML", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"XHTML 1.0: true", "XHTML 2.0: true", "XHTML 3.0: true"})
    public void hasFeature_XHTML() throws Exception {
        hasFeature("XHTML", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Views 1.0: true", "Views 2.0: true", "Views 3.0: true"})
    public void hasFeature_Views() throws Exception {
        hasFeature("Views", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"StyleSheets 1.0: true", "StyleSheets 2.0: true", "StyleSheets 3.0: true"})
    public void hasFeature_StyleSheets() throws Exception {
        hasFeature("StyleSheets", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"CSS 1.0: true", "CSS 2.0: true", "CSS 3.0: true"})
    public void hasFeature_CSS() throws Exception {
        hasFeature("CSS", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"CSS2 1.0: true", "CSS2 2.0: true", "CSS2 3.0: true"})
    public void hasFeature_CSS2() throws Exception {
        hasFeature("CSS2", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"CSS3 1.0: true", "CSS3 2.0: true", "CSS3 3.0: true"})
    public void hasFeature_CSS3() throws Exception {
        hasFeature("CSS3", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Events 1.0: true", "Events 2.0: true", "Events 3.0: true"})
    public void hasFeature_Events() throws Exception {
        hasFeature("Events", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"UIEvents 1.0: true", "UIEvents 2.0: true", "UIEvents 3.0: true"})
    public void hasFeature_UIEvents() throws Exception {
        hasFeature("UIEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"MouseEvents 1.0: true", "MouseEvents 2.0: true", "MouseEvents 3.0: true"})
    public void hasFeature_MouseEvents() throws Exception {
        hasFeature("MouseEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TextEvents 1.0: true", "TextEvents 2.0: true", "TextEvents 3.0: true"})
    public void hasFeature_TextEvents() throws Exception {
        hasFeature("TextEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"KeyboardEvents 1.0: true", "KeyboardEvents 2.0: true", "KeyboardEvents 3.0: true"})
    public void hasFeature_KeyboardEvents() throws Exception {
        hasFeature("KeyboardEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"MutationEvents 1.0: true", "MutationEvents 2.0: true", "MutationEvents 3.0: true"})
    public void hasFeature_MutationEvents() throws Exception {
        hasFeature("MutationEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"MutationNameEvents 1.0: true", "MutationNameEvents 2.0: true", "MutationNameEvents 3.0: true"})
    public void hasFeature_MutationNameEvents() throws Exception {
        hasFeature("MutationNameEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HTMLEvents 1.0: true", "HTMLEvents 2.0: true", "HTMLEvents 3.0: true"})
    public void hasFeature_HTMLEvents() throws Exception {
        hasFeature("HTMLEvents", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Range 1.0: true", "Range 2.0: true", "Range 3.0: true"})
    public void hasFeature_Range() throws Exception {
        hasFeature("Range", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Traversal 1.0: true", "Traversal 2.0: true", "Traversal 3.0: true"})
    public void hasFeature_Traversal() throws Exception {
        hasFeature("Traversal", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"LS 1.0: true", "LS 2.0: true", "LS 3.0: true"})
    public void hasFeature_LS() throws Exception {
        hasFeature("LS", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"LS-Async 1.0: true", "LS-Async 2.0: true", "LS-Async 3.0: true"})
    public void hasFeature_LSAsync() throws Exception {
        hasFeature("LS-Async", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Validation 1.0: true", "Validation 2.0: true", "Validation 3.0: true"})
    public void hasFeature_Validation() throws Exception {
        hasFeature("Validation", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"XPath 1.0: true", "XPath 2.0: true", "XPath 3.0: true"})
    public void hasFeature_XPath() throws Exception {
        hasFeature("XPath", "['1.0', '2.0', '3.0']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://www.w3.org/TR/SVG11/feature#BasicStructure 1.0: true",
             "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.1: true",
             "http://www.w3.org/TR/SVG11/feature#BasicStructure 1.2: true"})
    public void hasFeature_SVG_BasicStructure() throws Exception {
        hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "['1.0', '1.1', '1.2']");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://www.w3.org/TR/SVG11/feature#Shape 1.0: true",
             "http://www.w3.org/TR/SVG11/feature#Shape 1.1: true",
             "http://www.w3.org/TR/SVG11/feature#Shape 1.2: true"})
    public void hasFeature_SVG_Shape() throws Exception {
        hasFeature("http://www.w3.org/TR/SVG11/feature#Shape", "['1.0', '1.1', '1.2']");
    }

    private void hasFeature(final String feature, final String versions) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var feature = '" + feature + "';\n"
            + "    var versions = " + versions + ";\n"
            + "    for (var j = 0; j < versions.length; j++) {\n"
            + "      var version = versions[j];\n"
            + "      log(feature + ' ' + version + ': ' + document.implementation.hasFeature(feature, version));\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void createDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.implementation.createDocument('', '', null));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mydoc", "null", "mydoc", "null"})
    public void createDocument_qualifiedName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', 'mydoc', null);\n"
            + "    log(doc.documentElement.tagName);\n"
            + "    log(doc.documentElement.prefix);\n"
            + "    log(doc.documentElement.localName);\n"
            + "    log(doc.documentElement.namespaceURI);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mydoc", "null", "mydoc", "http://mynamespace"})
    public void createDocument_namespaceAndQualifiedName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('http://mynamespace', 'mydoc', null);\n"
            + "    log(doc.documentElement.tagName);\n"
            + "    log(doc.documentElement.prefix);\n"
            + "    log(doc.documentElement.localName);\n"
            + "    log(doc.documentElement.namespaceURI);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"m:mydoc", "m", "mydoc", "http://mynamespace"})
    public void createDocument_namespaceAndQualifiedNameWithPrefix() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('http://mynamespace', 'm:mydoc', null);\n"
            + "    log(doc.documentElement.tagName);\n"
            + "    log(doc.documentElement.prefix);\n"
            + "    log(doc.documentElement.localName);\n"
            + "    log(doc.documentElement.namespaceURI);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "undefined"})
    public void createHTMLDocument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument();\n"
                + "      log(doc);\n"
                + "      log(doc.window);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "newdoctitle"})
    public void createHTMLDocument_title() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('newdoctitle');\n"
                + "      log(doc);\n"
                + "      log(doc.title);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", ""})
    public void createHTMLDocument_titleEmpty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('');\n"
                + "      log(doc);\n"
                + "      log(doc.title);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void createHTMLDocument_jQuery() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('');\n"
                + "      doc.body.innerHTML = '<form></form><form></form>';\n"
                + "      log(doc.body.childNodes.length);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("createdElement")
    public void createHTMLDocument_createElement() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('');\n"
                + "      var p = doc.createElement('p');\n"
                + "      p.innertHTML = 'createdElement';\n"
                + "      log(p.innertHTML);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<html><head></head><body></body></html>",
             "<html><head><title></title></head><body></body></html>",
             "<html><head><title>abc</title></head><body></body></html>"})
    public void createHTMLDocument_htmlCode() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument();\n"
                + "      log(doc.documentElement.outerHTML);\n"
                + "    } catch(e) { logEx(e); }\n"

                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('');\n"
                + "      log(doc.documentElement.outerHTML);\n"
                + "    } catch(e) { logEx(e); }\n"

                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('abc');\n"
                + "      log(doc.documentElement.outerHTML);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<html><head><title>test</title></head>"
            + "<body><p>This is a new paragraph.</p></body></html>")
    public void createHTMLDocumentAddParagraph() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('test');\n"
                + "      var p = doc.createElement('p');\n"
                + "      p.innerHTML = 'This is a new paragraph.';\n"
                + "      doc.body.appendChild(p);"
                + "      log(doc.documentElement.outerHTML);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<html><head><title>test</title></head><body><p>Hello</p></body></html>")
    public void createHTMLDocumentInnerAddParagraph() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('test');\n"
                + "      doc.body.innerHTML = '<p>Hello</p>';\n"
                + "      log(doc.documentElement.outerHTML);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<html><head><title>test</title></head><body><img src=\"x\" onerror=\"log(1)\"></body></html>")
    public void createHTMLDocumentInnerAddImg() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var doc = document.implementation.createHTMLDocument('test');\n"
                + "      doc.body.innerHTML = '<img src=\"x\" onerror=\"log(1)\">';\n"
                + "      log(doc.documentElement.outerHTML);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("before1")
    public void createHTMLDocumentInnerAddImgAddDocToIframe() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var frame = document.getElementById('theFrame');\n"

                + "      var doc = document.implementation.createHTMLDocument('test');\n"
                + "      doc.body.innerHTML = '<img src=\"x\" onerror=\"window.parent.document.title += 1\">';\n"

                         // Copy the new HTML document into the frame
                + "      var destDocument = frame.contentDocument;\n"
                + "      var srcNode = doc.documentElement;\n"
                + "      var newNode = destDocument.importNode(srcNode, true);\n"
                + "      destDocument.replaceChild(newNode, destDocument.documentElement);\n"
                + "      window.parent.document.title += 'before';"

                + "    } catch(e) { window.parent.document.title += 'exception'; }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <iframe id='theFrame' src='about:blank' />"
                + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Can be removed if the one before works.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("before import;after import;1")
    public void createHTMLDocumentInnerAddImgAddDocToIframe1() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var frame = document.getElementById('theFrame');\n"

                + "      var doc = document.implementation.createHTMLDocument('test');\n"
                + "      doc.body.innerHTML = '<img src=\"x\" onerror=\"window.parent.document.title += 1\">';\n"

                         // Copy the new HTML document into the frame
                + "      var destDocument = frame.contentDocument;\n"
                + "      var srcNode = doc.documentElement;\n"
                + "      document.title += 'before import;';\n"
                + "      var newNode = destDocument.importNode(srcNode, true);\n"
                + "      document.title += 'after import;';\n"
                + "      destDocument.replaceChild(newNode, destDocument.documentElement);\n"

                + "    } catch(e) { document.title += 'exception'; }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <iframe id='theFrame' src='about:blank' />"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"10", "html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "html"})
    public void createDocumentType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var dt = document.implementation.createDocumentType('html',\n"
            + "          '-//W3C//DTD HTML 4.01//EN', 'http://www.w3.org/TR/html4/strict.dtd');\n"
            + "      log(dt.nodeType);\n"
            + "      log(dt.name);\n"
            + "      log(dt.publicId);\n"
            + "      log(dt.systemId);\n"
            + "      log(dt.nodeName);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"html", "[]", "[]"})
    public void createDocumentTypeEmptyIds() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var dt = document.implementation.createDocumentType('html', '', '');\n"
            + "      log(dt.name);\n"
            + "      log('[' + dt.publicId + ']');\n"
            + "      log('[' + dt.systemId + ']');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object DocumentType]", "true", "true"})
    public void createDocumentTypePrototypeChain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var dt = document.implementation.createDocumentType('html', '', '');\n"
            + "      log(Object.prototype.toString.call(dt));\n"
            + "      log(dt instanceof DocumentType);\n"
            + "      log(dt instanceof Node);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "null", "false", "0"})
    public void createDocumentTypeOwnerAndParent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var dt = document.implementation.createDocumentType('html', '', '');\n"
            + "      log(dt.ownerDocument === document);\n"
            + "      log(dt.parentNode);\n"
            + "      log(dt.hasChildNodes());\n"
            + "      log(dt.childNodes.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The name, the public id and the system id are read only.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"html", "pub", "sys"})
    public void createDocumentTypeReadOnlyProperties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var dt = document.implementation.createDocumentType('html', 'pub', 'sys');\n"
            + "      dt.name = 'other';\n"
            + "      dt.publicId = 'other';\n"
            + "      dt.systemId = 'other';\n"
            + "      log(dt.name);\n"
            + "      log(dt.publicId);\n"
            + "      log(dt.systemId);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The name is a qualified name; there is no case folding.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"html", "HTML", "svg:svg", "my-doctype", "_x", "a.b1"})
    public void createDocumentTypeNames() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var names = ['html', 'HTML', 'svg:svg', 'my-doctype', '_x', 'a.b1'];\n"
            + "      for (var i = 0; i < names.length; i++) {\n"
            + "        try {\n"
            + "          log(document.implementation.createDocumentType(names[i], '', '').name);\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"no error", "no error", "InvalidCharacterError/DOMException",
                       "InvalidCharacterError/DOMException", "no error"},
            FF_ESR = {"InvalidCharacterError/DOMException", "InvalidCharacterError/DOMException",
                      "InvalidCharacterError/DOMException", "InvalidCharacterError/DOMException",
                      "InvalidCharacterError/DOMException"})
    @HtmlUnitNYI(
            FF_ESR = {"no error", "no error", "InvalidCharacterError/DOMException",
                      "InvalidCharacterError/DOMException", "no error"})
    public void createDocumentTypeInvalidNames() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var names = ['', '1a', 'a b', 'a>b', '-a'];\n"
            + "      for (var i = 0; i < names.length; i++) {\n"
            + "        try {\n"
            + "          document.implementation.createDocumentType(names[i], '', '');\n"
            + "          log('no error');\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * All three arguments are required, the values are converted to strings.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"TypeError", "TypeError", "null", "undefined"})
    public void createDocumentTypeArguments() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        document.implementation.createDocumentType('html');\n"
            + "        log('no error');\n"
            + "      } catch(e) { log(e.name); }\n"
            + "      try {\n"
            + "        document.implementation.createDocumentType('html', '');\n"
            + "        log('no error');\n"
            + "      } catch(e) { log(e.name); }\n"
            + "      log(document.implementation.createDocumentType('html', null, null).publicId);\n"
            + "      log(document.implementation.createDocumentType('html', undefined, undefined).systemId);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"10", "html", "pub", "sys", "true", "true"})
    public void createDocumentTypeCloneNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var dt = document.implementation.createDocumentType('html', 'pub', 'sys');\n"
            + "      var clone = dt.cloneNode(false);\n"
            + "      log(clone.nodeType);\n"
            + "      log(clone.name);\n"
            + "      log(clone.publicId);\n"
            + "      log(clone.systemId);\n"
            + "      log(clone !== dt);\n"
            + "      log(dt.isEqualNode(clone));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The doctype of a document can be replaced.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "foo", "2"})
    public void createDocumentTypeReplaceDoctype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var dt = document.implementation.createDocumentType('foo', '', '');\n"
            + "      document.replaceChild(dt, document.doctype);\n"
            + "      log(document.doctype === dt);\n"
            + "      log(document.childNodes[0] === dt);\n"
            + "      log(document.doctype.name);\n"
            + "      log(document.childNodes.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The doctype is the first child of a new document; the document adopts it.
     * (needs the doctype handling in createDocument)
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "2", "true", "root", "true"})
    public void createDocumentWithDocumentType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var dt = document.implementation.createDocumentType('root', 'pub', 'sys');\n"
            + "      var doc = document.implementation.createDocument(null, 'root', dt);\n"
            + "      log(doc.doctype === dt);\n"
            + "      log(doc.childNodes.length);\n"
            + "      log(doc.childNodes[0] === dt);\n"
            + "      log(doc.documentElement.tagName);\n"
            + "      log(dt.ownerDocument === doc);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Every document has its own implementation and the doctype belongs to the document of the
     * implementation.
     * (HtmlUnit creates the doctype for the page of the window)
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false",
            FF = "false",
            FF_ESR = "false")
    public void createDocumentTypeOwnerOfOtherDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var doc = document.implementation.createHTMLDocument('x');\n"
            + "      var dt = doc.implementation.createDocumentType('html', '', '');\n"
            + "      log(dt.ownerDocument === doc);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
