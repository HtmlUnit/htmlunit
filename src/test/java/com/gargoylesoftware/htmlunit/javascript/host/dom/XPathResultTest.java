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
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link XPathResult}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Chuck Dumont
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XPathResultTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4", "1", "3"},
            IE = "evaluate not supported")
    public void resultType() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "        text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "        text += '  <xsl:template match=\"/\">\\n';\n"
            + "        text += '  <html>\\n';\n"
            + "        text += '    <body>\\n';\n"
            + "        text += '      <div/>\\n';\n"
            + "        text += '      <div/>\\n';\n"
            + "        text += '    </body>\\n';\n"
            + "        text += '  </html>\\n';\n"
            + "        text += '  </xsl:template>\\n';\n"
            + "        text += '</xsl:stylesheet>';\n"
            + "        var parser = new DOMParser();\n"
            + "        var doc = parser.parseFromString(text, 'text/xml');\n"
            + "        var expressions = ['//div', 'count(//div)', 'count(//div) = 2'];\n"
            + "        for (var i = 0; i < expressions.length; i++) {\n"
            + "          var expression = expressions[i];\n"
            + "          var result = doc.evaluate(expression, doc.documentElement, null,"
                                + " XPathResult.ANY_TYPE, null);\n"
            + "          log(result.resultType);\n"
            + "        }\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"7", "id1", "id2"},
            IE = "evaluate not supported")
    public void snapshotType() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "        text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "        text += '  <xsl:template match=\"/\">\\n';\n"
            + "        text += '  <html>\\n';\n"
            + "        text += '    <body>\\n';\n"
            + "        text += '      <div id=\\'id1\\'/>\\n';\n"
            + "        text += '      <div id=\\'id2\\'/>\\n';\n"
            + "        text += '    </body>\\n';\n"
            + "        text += '  </html>\\n';\n"
            + "        text += '  </xsl:template>\\n';\n"
            + "        text += '</xsl:stylesheet>';\n"
            + "        var parser=new DOMParser();\n"
            + "        var doc=parser.parseFromString(text,'text/xml');\n"
            + "        var result = doc.evaluate('//div', doc.documentElement, null,"
                            + " XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n"
            + "        log(result.resultType);\n"
            + "        for (var i = 0; i < result.snapshotLength; i++) {\n"
            + "          log(result.snapshotItem(i).getAttribute('id'));\n"
            + "        }\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"9", "id1"},
            IE = "evaluate not supported")
    public void singleNodeValue() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "        text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "        text += '  <xsl:template match=\"/\">\\n';\n"
            + "        text += '  <html>\\n';\n"
            + "        text += '    <body>\\n';\n"
            + "        text += '      <div id=\\'id1\\'/>\\n';\n"
            + "        text += '      <div id=\\'id2\\'/>\\n';\n"
            + "        text += '    </body>\\n';\n"
            + "        text += '  </html>\\n';\n"
            + "        text += '  </xsl:template>\\n';\n"
            + "        text += '</xsl:stylesheet>';\n"
            + "        var parser=new DOMParser();\n"
            + "        var doc=parser.parseFromString(text,'text/xml');\n"
            + "        var result = doc.evaluate('//div', doc.documentElement, null,"
                            + " XPathResult.FIRST_ORDERED_NODE_TYPE, null);\n"
            + "        log(result.resultType);\n"
            + "        log(result.singleNodeValue.getAttribute('id'));\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"id1", "id2"},
            IE = "evaluate not supported")
    public void iterateNext() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "        text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "        text += '  <xsl:template match=\"/\">\\n';\n"
            + "        text += '  <html>\\n';\n"
            + "        text += '    <body>\\n';\n"
            + "        text += '      <div id=\"id1\"/>\\n';\n"
            + "        text += '      <div id=\"id2\"/>\\n';\n"
            + "        text += '    </body>\\n';\n"
            + "        text += '  </html>\\n';\n"
            + "        text += '  </xsl:template>\\n';\n"
            + "        text += '</xsl:stylesheet>';\n"
            + "        var parser=new DOMParser();\n"
            + "        var doc=parser.parseFromString(text,'text/xml');\n"
            + "        var result = doc.evaluate('" + "//div" + "', doc.documentElement, "
                            + "null, XPathResult.ANY_TYPE, null);\n"

            + "        var thisNode = result.iterateNext();\n"
            + "        while (thisNode) {\n"
            + "          log(thisNode.getAttribute('id'));\n"
            + "          thisNode = result.iterateNext();\n"
            + "        }\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "7",
            IE = "evaluate not supported")
    public void notOr() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var expression = \".//*[@id='level1']/*[not(preceding-sibling::* or following-sibling::*)]\";\n"
            + "        var result = document.evaluate(expression, document, null, "
                            + "XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n"
            + "        log(result.resultType);\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"bar", "foo", "foo"},
            IE = "evaluate not supported")
    public void stringType() throws Exception {
        final String html = "<html><head><title attr=\"bar\">foo</title><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var result = document.evaluate('//title/@attr', document, null, "
                            + "XPathResult.STRING_TYPE, null);\n"
            + "        log(result.stringValue);\n"
            + "        result = document.evaluate('//title', document, null, "
                            + "XPathResult.STRING_TYPE, null);\n"
            + "        log(result.stringValue);\n"
            + "        var result = document.evaluate('//title/text()', document, null, "
                            + "XPathResult.STRING_TYPE, null);\n"
            + "        log(result.stringValue);\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "true"},
            IE = "evaluate not supported")
    public void numberType() throws Exception {
        final String html = "<html><head><title attr=\"1234\">4321.5</title><span>foo</span><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var result = document.evaluate('//title/@attr', document, null, "
                            + "XPathResult.NUMBER_TYPE, null);\n"
            + "        log(result.numberValue === 1234);\n"
            + "        result = document.evaluate('//title', document, null, "
                            + "XPathResult.NUMBER_TYPE, null);\n"
            + "        log(result.numberValue === 4321.5);\n"
            + "        result = document.evaluate('//title/text()', document, null, "
                            + "XPathResult.NUMBER_TYPE, null);\n"
            + "        log(result.numberValue === 4321.5);\n"
            + "        result = document.evaluate('//span', document, null, "
                            + "XPathResult.NUMBER_TYPE, null);\n"
            + "        log(isNaN(result.numberValue));\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "true", "true", "true"},
            IE = "evaluate not supported")
    public void booleanType() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var result = document.evaluate('//unknown', document, null, "
                            + "XPathResult.BOOLEAN_TYPE, null);\n"
            + "        log(result.booleanValue === false);\n"

            + "        var result = document.evaluate('//title', document, null, "
                            + "XPathResult.BOOLEAN_TYPE, null);\n"
            + "        log(result.booleanValue === true);\n"

            + "        result = document.evaluate('//div', document, null, "
                            + "XPathResult.BOOLEAN_TYPE, null);\n"
            + "        log(result.booleanValue === true);\n"
            + "        result = document.evaluate('//div/@attr', document, null, "
                        + "XPathResult.BOOLEAN_TYPE, null);\n"
            + "        log(result.booleanValue === true);\n"

            + "        result = document.evaluate('//span', document, null, "
                            + "XPathResult.BOOLEAN_TYPE, null);\n"
            + "        log(result.booleanValue === true);\n"
            + "        result = document.evaluate('//span/@attr', document, null, "
                            + "XPathResult.BOOLEAN_TYPE, null);\n"
            + "        log(result.booleanValue === true);\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div attr=\"false\">false</span>\n"
            + "  <span attr=\"true\">true</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4", "not boolean", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void emptySetTypeAny() throws Exception {
        emptySetType("XPathResult.ANY_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "not boolean", "NaN", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void emptySetTypeNumber() throws Exception {
        emptySetType("XPathResult.NUMBER_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2", "not boolean", "not number", "", "not node", "not length"},
            IE = "evaluate not supported")
    public void emptySetTypeString() throws Exception {
        emptySetType("XPathResult.STRING_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "false", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void emptySetTypeBoolean() throws Exception {
        emptySetType("XPathResult.BOOLEAN_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4", "not boolean", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void emptySetTypeUnorderedNodeIterator() throws Exception {
        emptySetType("XPathResult.UNORDERED_NODE_ITERATOR_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"5", "not boolean", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void emptySetTypeOrderedNodeIterator() throws Exception {
        emptySetType("XPathResult.ORDERED_NODE_ITERATOR_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"6", "not boolean", "not number", "not string", "not node", "0"},
            IE = "evaluate not supported")
    public void emptySetTypeUnorderedNodeSnapshot() throws Exception {
        emptySetType("XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"7", "not boolean", "not number", "not string", "not node", "0"},
            IE = "evaluate not supported")
    public void emptySetTypeOrderedNodeSnapshot() throws Exception {
        emptySetType("XPathResult.ORDERED_NODE_SNAPSHOT_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"8", "not boolean", "not number", "not string", "null", "not length"},
            IE = "evaluate not supported")
    public void emptySetTypeAnyOrderedNode() throws Exception {
        emptySetType("XPathResult.ANY_UNORDERED_NODE_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"9", "not boolean", "not number", "not string", "null", "not length"},
            IE = "evaluate not supported")
    public void emptySetTypeFirstOrderedNode() throws Exception {
        emptySetType("XPathResult.FIRST_ORDERED_NODE_TYPE");
    }

    private void emptySetType(final String type) throws Exception {
        type("//unknown", type);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "false", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void zeroTypeBoolean() throws Exception {
        typeBoolean("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "true", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void minusOneTypeBoolean() throws Exception {
        typeBoolean("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "true", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void infTypeBoolean() throws Exception {
        typeBoolean("1.0 div 0.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "true", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void minusInfTypeBoolean() throws Exception {
        typeBoolean("-1.0 div 0.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "true", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void stringTypeBoolean() throws Exception {
        typeBoolean("\"abc\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "false", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void emptyStringTypeBoolean() throws Exception {
        typeBoolean("\"\"");
    }

    private void typeBoolean(final String xpath) throws Exception {
        type(xpath, "XPathResult.BOOLEAN_TYPE");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "not boolean", "0", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void zeroTypeNumber() throws Exception {
        typeNumber("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "not boolean", "Infinity", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void infTypeNumber() throws Exception {
        typeNumber("1.0 div 0.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "not boolean", "-Infinity", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void minusInfTypeNumber() throws Exception {
        typeNumber("-1.0 div 0.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "not boolean", "NaN", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void stringTypeNumber() throws Exception {
        typeNumber("\"abc\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "not boolean", "123.4", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void numberStringTypeNumber() throws Exception {
        typeNumber("\"123.4\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "false", "not number", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void emptyStringTypeNumber() throws Exception {
        typeBoolean("\"\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "not boolean", "1", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void trueTypeNumber() throws Exception {
        typeNumber("true()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "not boolean", "0", "not string", "not node", "not length"},
            IE = "evaluate not supported")
    public void falseTypeNumber() throws Exception {
        typeNumber("false()");
    }

    private void typeNumber(final String xpath) throws Exception {
        type(xpath, "XPathResult.NUMBER_TYPE");
    }

    private void type(final String xpath, final String type) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.evaluate && XPathResult) {\n"
            + "      try {\n"
            + "        var result = document.evaluate('" + xpath + "', document, null, "
                            + type + ", null);\n"
            + "        log(result.resultType);\n"
            + "        try {\n"
            + "          log(result.booleanValue);\n"
            + "        } catch (e) { log('not boolean'); }\n"

            + "        try {\n"
            + "          log(result.numberValue);\n"
            + "        } catch (e) { log('not number'); }\n"

            + "        try {\n"
            + "          log(result.stringValue);\n"
            + "        } catch (e) { log('not string'); }\n"

            + "        try {\n"
            + "          log(result.singleNodeValue);\n"
            + "        } catch (e) { log('not node'); }\n"

            + "        try {\n"
            + "          log(result.snapshotLength);\n"
            + "        } catch (e) { log('not length'); }\n"
            + "      } catch (e) { log(e); }\n"
            + "    } else {\n"
            + "      log('evaluate not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div attr=\"false\">false</span>\n"
            + "  <span attr=\"true\">true</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
