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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link DOMParser}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class DOMParserTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object DOMParser]", IE8 = "exception")
    public void scriptableToString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "     alert(new DOMParser());\n"
            + "    } catch (e) {alert('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLDocument]", IE8 = "exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void parseFromString_text_html() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<html></html>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/html');\n"
            + "      alert(doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLDocument]", IE8 = "exception")
    public void parseFromString_text_xml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<note/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      alert(doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLDocument]", IE8 = "exception")
    public void parseFromString_application_xml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<note/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'application/xml');\n"
            + "      alert(doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLDocument]", IE8 = "exception")
    public void parseFromString_application_xhtmlXml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<html/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'application/xhtml+xml');\n"
            + "      alert(doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SVGDocument]", CHROME = "[object XMLDocument]",
            IE11 = "[object XMLDocument]", IE8 = "exception")
    @NotYetImplemented(FF)
    public void parseFromString_application_svgXml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<svg  xmlns=\"http://www.w3.org/2000/svg\"/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'image/svg+xml');\n"
            + "      alert(doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void parseFromString_unknownType() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<test/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'unknown/type');\n"
            + "      alert(doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "9", IE8 = "exception")
    public void parseFromString() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<note> ';\n"
            + "    text += '<to>Tove</to> ';\n"
            + "    text += '<from>Jani</from> ';\n"
            + "    text += '<heading>Reminder</heading> ';\n"
            + "    text += '<body>Do not forget me this weekend!</body> ';\n"
            + "    text += '</note>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) { alert('parsererror'); return; }\n"

            + "      var x = doc.documentElement;\n"
            + "      alert(x.childNodes.length);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * In 2.9-SNAPSHOT on 26.10.2010 this was causing an internal error in DOMParser.parseFromString.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "parsererror", IE = "exception")
    @NotYetImplemented(CHROME)
    public void parseFromString_invalidXml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text = '</notvalid> ';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) {\n"
            + "        alert('parsererror');\n"
            + "        return;\n"
            + "      }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "parsererror",
            IE = "0", IE8 = "exception")
    @NotYetImplemented(CHROME)
    public void parseFromString_emptyString() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) {\n"
            + "        alert('parsererror');\n"
            + "        return;\n"
            + "      }\n"
            + "      alert(doc.childNodes.length);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void parseFromString_missingMimeType() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<root/>';\n"
            + "    try {\n"
            + "      var parser=new DOMParser();\n"
            + "      parser.parseFromString(text);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(content);
    }

    /**
     * Regression test for bug 2899485.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "5", "[object CDATASection]", "[object Comment]", "[object Element]",
            "[object ProcessingInstruction]", "[object Text]" },
            IE8 = "exception")
    public void parseFromString_processingInstructionKept() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    var text = '<elementWithChildren>' + '<![CDATA[sampl<<< >>e data]]>' + '<!--a sample comment-->'\n"
            + "      + '<elementWithChildren/>' + '<?target processing instruction data?>' + 'sample text node'\n"
            + "      + '</elementWithChildren>'\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) {\n"
            + "        alert('parsererror');\n"
            + "        return;\n"
            + "      }\n"
            + "      alert(doc.documentElement.childNodes.length);\n"
            + "      for(var i = 0; i < doc.documentElement.childNodes.length; i++) {\n"
            + "        alert(doc.documentElement.childNodes[i]);\n"
            + "      }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";
        loadPageWithAlerts2(html);
    }

}
