/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for {@link XMLSerializer}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XMLSerializerTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        final String expectedStringIE =
            "<note>13109<to>Tove</to>13109<from>Jani</from>13109<body>Do32not32forget32me32this32weekend!</body>"
            + "13109<outer>131099<inner>Some32Value</inner></outer>1310</note>1310";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, expectedStringIE);
        final String expectedStringFF =
            "<note>32<to>Tove</to>3210<from>Jani</from>321032<body>Do32not32forget32me32this32weekend!</body>"
            + "32<outer>10323232<inner>Some32Value</inner></outer>32</note>";
        test(BrowserVersion.FIREFOX_2, expectedStringFF);
    }

    private void test(final BrowserVersion browserVersion, final String expectedString)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<note> ';\n"
            + "    text += '<to>Tove</to> \\n';\n"
            + "    text += '<from>Jani</from> \\n ';\n"
            + "    text += '<body>Do not forget me this weekend!</body> ';\n"
            + "    text += '<outer>\\n ';\n"
            + "    text += '  <inner>Some Value</inner>';\n"
            + "    text += '</outer> ';\n"
            + "    text += '</note>';\n"
            + "    if (window.ActiveXObject) {\n"
            + "      var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      doc.async=false;\n"
            + "      doc.loadXML(text);\n"
            + "      var xml = doc.xml;\n"
            + "    } else {\n"
            + "      var parser=new DOMParser();\n"
            + "      var doc=parser.parseFromString(text,'text/xml');\n"
            + "      var serializer = new XMLSerializer();\n"
            + "      var xml = serializer.serializeToString(doc.documentElement);\n"
            + "    }\n"
            + "    var ta = document.getElementById('myTextArea');\n"
            + "    for (var i=0; i < xml.length; i++) {\n"
            + "      if (xml.charCodeAt(i) < 33)\n"
            + "        ta.value += xml.charCodeAt(i);\n"
            + "      else\n"
            + "        ta.value += xml.charAt(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(browserVersion, content, null);
        final HtmlTextArea textArea = (HtmlTextArea) page.getHtmlElementById("myTextArea");
        assertEquals(expectedString, textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testNameSpaces() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String expectedStringIE =
            "<?xml32version=\"1.0\"?>1310<xsl:stylesheet32version=\"1.0\"32"
            + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">13109<xsl:template32match=\"/\">131099<html>1310999"
            + "<body>1310999</body>131099</html>13109</xsl:template>1310</xsl:stylesheet>1310";
        testNameSpaces(BrowserVersion.INTERNET_EXPLORER_7_0, expectedStringIE);
        final String expectedStringFF =
            "<xsl:stylesheet32version=\"1.0\"32xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">103232"
            + "<xsl:template32match=\"/\">103232<html>1032323232<body>1032323232</body>103232</html>103232"
            + "</xsl:template>10</xsl:stylesheet>";
        testNameSpaces(BrowserVersion.FIREFOX_2, expectedStringFF);
    }

    private void testNameSpaces(final BrowserVersion browserVersion, final String expectedString) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    if (window.ActiveXObject) {\n"
            + "      var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      doc.async=false;\n"
            + "      doc.loadXML(text);\n"
            + "      var xml = doc.xml;\n"
            + "    } else {\n"
            + "      var parser=new DOMParser();\n"
            + "      var doc=parser.parseFromString(text,'text/xml');\n"
            + "      var serializer = new XMLSerializer();\n"
            + "      var xml = serializer.serializeToString(doc.documentElement);\n"
            + "    }\n"
            + "    var ta = document.getElementById('myTextArea');\n"
            + "    for (var i=0; i < xml.length; i++) {\n"
            + "      if (xml.charCodeAt(i) < 33)\n"
            + "        ta.value += xml.charCodeAt(i);\n"
            + "      else\n"
            + "        ta.value += xml.charAt(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(browserVersion, html, null);
        final HtmlTextArea textArea = (HtmlTextArea) page.getHtmlElementById("myTextArea");
        assertEquals(expectedString, textArea.getText());
    }
}
