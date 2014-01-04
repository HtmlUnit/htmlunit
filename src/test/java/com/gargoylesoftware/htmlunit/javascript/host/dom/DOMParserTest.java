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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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
    @Browsers({ CHROME, FF, IE11 })
    @Alerts("[object DOMParser]")
    public void scriptableToString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(new DOMParser());\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ CHROME, FF, IE11 })
    @Alerts("9")
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
            + "    var parser = new DOMParser();\n"
            + "    try {\n"
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
    @Browsers({ CHROME, FF, IE11 })
    @Alerts(DEFAULT = "parsererror",
            IE = "exception")
    public void parseFromString_invalidXml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text = '</notvalid> ';\n"
            + "    var parser = new DOMParser();\n"
            + "    try {\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) { alert('parsererror'); return; }\n"
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
    @Browsers({ CHROME, FF, IE11 })
    @Alerts(DEFAULT = "parsererror",
            IE = "0")
    public void parseFromString_emptyString() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='';\n"
            + "    var parser = new DOMParser();\n"
            + "    try {\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) { alert('parsererror'); return; }\n"

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
    @Browsers({ CHROME, FF, IE11 })
    @Alerts(DEFAULT = "exception",
            CHROME = "")
    public void parseFromString_missingMimeType() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<root/>';\n"
            + "    var parser=new DOMParser();\n"
            + "    try {\n"
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
    @Browsers({ CHROME, FF, IE11 })
    @Alerts({ "5", "[object CDATASection]", "[object Comment]", "[object Element]", "[object ProcessingInstruction]",
        "[object Text]" })
    public void parseFromString_processingInstructionKept() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    var text = '<elementWithChildren>' + '<![CDATA[sampl<<< >>e data]]>' + '<!--a sample comment-->'\n"
            + "      + '<elementWithChildren/>' + '<?target processing instruction data?>' + 'sample text node'\n"
            + "      + '</elementWithChildren>'\n"
            + "    var parser = new DOMParser();\n"
            + "    try {\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) { alert('parsererror'); return; }\n"

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
