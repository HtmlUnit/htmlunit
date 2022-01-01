/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link DOMParser}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DOMParserTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DOMParser]")
    public void scriptableToString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(new DOMParser());\n"
            + "    } catch (e) {log('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "", "§§URL§§"})
    public void parseFromString_text_html() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<html></html>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/html');\n"
            + "      log(doc);\n"
            + "      log(doc.body.innerHTML);\n"
            + "      log(doc.URL);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "<div></div>", "§§URL§§"})
    public void parseFromString_text_html_div() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<div></div>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/html');\n"
            + "      log(doc);\n"
            + "      log(doc.body.innerHTML);\n"
            + "      log(doc.URL);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void parseFromString_text_xml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<note/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      log(doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void parseFromString_application_xml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<note/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'application/xml');\n"
            + "      log(doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void parseFromString_application_xhtmlXml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<html/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'application/xhtml+xml');\n"
            + "      log(doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void parseFromString_application_svgXml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<svg xmlns=\"http://www.w3.org/2000/svg\"/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'image/svg+xml');\n"
            + "      log(doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void parseFromString_unknownType() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<test/>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'unknown/type');\n"
            + "      log(doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("9")
    public void parseFromString() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
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
            + "      if (doc.getElementsByTagName('parsererror').length > 0) { log('parsererror'); return; }\n"

            + "      var x = doc.documentElement;\n"
            + "      log(x.childNodes.length);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * In 2.9-SNAPSHOT on 26.10.2010 this was causing an internal error in DOMParser.parseFromString.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "parsererror",
            IE = "exception")
    public void parseFromString_invalidXml() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = '</notvalid> ';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) {\n"
            + "        log('parsererror');\n"
            + "        return;\n"
            + "      }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "parsererror",
            IE = "0")
    public void parseFromString_emptyString() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) {\n"
            + "        log('parsererror');\n"
            + "        return;\n"
            + "      }\n"
            + "      log(doc.childNodes.length);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void parseFromString_missingMimeType() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<root/>';\n"
            + "    try {\n"
            + "      var parser=new DOMParser();\n"
            + "      parser.parseFromString(text);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * Regression test for bug 2899485.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"5", "[object CDATASection]", "[object Comment]", "[object Element]",
                "[object ProcessingInstruction]", "[object Text]"})
    public void parseFromString_processingInstructionKept() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = '<elementWithChildren>' + '<![CDATA[sampl<<< >>e data]]>' + '<!--a sample comment-->'\n"
            + "      + '<elementWithChildren/>' + '<?target processing instruction data?>' + 'sample text node'\n"
            + "      + '</elementWithChildren>';\n"
            + "    try {\n"
            + "      var parser = new DOMParser();\n"
            + "      var doc = parser.parseFromString(text, 'text/xml');\n"
            + "      if (doc.getElementsByTagName('parsererror').length > 0) {\n"
            + "        log('parsererror');\n"
            + "        return;\n"
            + "      }\n"
            + "      log(doc.documentElement.childNodes.length);\n"
            + "      for(var i = 0; i < doc.documentElement.childNodes.length; i++) {\n"
            + "        log(doc.documentElement.childNodes[i]);\n"
            + "      }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("[object HTMLDocument]")
    public void parseFromString_doNotExecuteScripts() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var html = '<script>document.title = \"parsed script executed\";</' + 'script>';\n"
            + "      var parser = new DOMParser();\n"
            + "      log(parser.parseFromString(html, 'text/html'));\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("[object HTMLDocument]")
    public void parseFromString_doNotExecuteSvgScripts() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var html = '<svg viewBox=\"0 0 10 10\" xmlns=\"http://www.w3.org/2000/svg\">'\n"
            + "                + '<script>document.title = \"parsed script executed\";</' + 'script>'\n"
            + "                + '</svg>';\n"
            + "      var parser = new DOMParser();\n"
            + "      log(parser.parseFromString(html, 'text/html'));\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
