/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.activex.javascript.msxml;

import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.ACTIVEX_CHECK;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callLoadXMLDOMDocumentFromURL;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.createTestHTML;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link XMLDOMNamedNodeMap}.
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLDOMNamedNodeMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void scriptableToString() throws Exception {
        tester("log(Object.prototype.toString.call(attrs));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void length() throws Exception {
        tester("log(attrs.length);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void byName_attribute() throws Exception {
        tester("log(attrs.name);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void byName_map() throws Exception {
        tester("log(attrs['name']);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void byNumber() throws Exception {
        tester("log(attrs[0].nodeName);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void byNumber_unknown() throws Exception {
        tester("debug(attrs[1]);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void getNamedItem() throws Exception {
        tester("debug(attrs.getNamedItem('name'));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void getNamedItem_unknown() throws Exception {
        tester("debug(attrs.getNamedItem('unknown'));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void getNamedItem_caseSensitive() throws Exception {
        tester("debug(attrs.getNamedItem('NaMe'));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void getNamedItem_null() throws Exception {
        final String test = ""
            + "try {\n"
            + "  attrs.getNamedItem(null);\n"
            + "} catch(e) { log('exception-getNull'); }\n";

        tester(test, "<blah/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void item() throws Exception {
        tester("debug(attrs.item(0));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no ActiveX")
    public void item_unknown() throws Exception {
        tester("debug(attrs.item(1));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void nextNode() throws Exception {
        final String test = ""
            + "log(attrs.length);\n"
            + "debug(attrs.nextNode());\n"
            + "debug(attrs.nextNode());\n"
            + "debug(attrs.nextNode());\n";

        tester(test, "<blah name1='y1' name2='y2'/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void removeNamedItem() throws Exception {
        final String test = ""
            + "log(attrs.length);\n"
            + "debug(attrs.getNamedItem('name'));\n"
            + "var attr = attrs.removeNamedItem('name');\n"
            + "log(attrs.length);\n"
            + "debug(attrs.getNamedItem('name'));\n"
            + "debug(attr);\n";

        tester(test);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void removeNamedItem_unknown() throws Exception {
        final String test = ""
            + "log(attrs.length);\n"
            + "var attr = attrs.removeNamedItem('other');\n"
            + "log(attrs.length);\n"
            + "debug(attr);\n";

        tester(test);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void removeNamedItem_caseSensitive() throws Exception {
        final String test = ""
            + "log(attrs.length);\n"
            + "var attr = attrs.removeNamedItem('NaMe');\n"
            + "log(attrs.length);\n"
            + "debug(attr);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void removeNamedItem_null() throws Exception {
        final String test = ""
            + "log(attrs.length);\n"
            + "try {\n"
            + "  attrs.removeNamedItem(null);\n"
            + "} catch(e) { log('exception-removeNull'); }\n";

        tester(test, "<blah/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void reset() throws Exception {
        final String test = ""
            + "log(attrs.length);\n"
            + "debug(attrs.nextNode());\n"
            + "attrs.reset();\n"
            + "debug(attrs.nextNode());\n"
            + "debug(attrs.nextNode());\n"
            + "attrs.reset();\n"
            + "debug(attrs.nextNode());\n";

        tester(test, "<blah name1='y1' name2='y2'/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void setNamedItem() throws Exception {
        final String test = ""
            + "log(attrs.length);\n"
            + "var attr = doc.createAttribute('myAttr');\n"
            + "var node = attrs.setNamedItem(attr);\n"
            + "log(attrs.length);\n"
            + "debug(attrs.getNamedItem('myAttr'));\n"
            + "log(node === attr);\n";

        tester(test, "<blah/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void setNamedItem_null() throws Exception {
        final String test = ""
            + "log(attrs.length);\n"
            + "try {\n"
            + "  attrs.setNamedItem(null);\n"
            + "} catch(e) { log('exception-setNull'); }\n";

        tester(test, "<blah/>");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<blah name='y'/>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    try {\n"
            + "      var attrs = doc.documentElement.attributes;\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    if (e != null) {\n"
            + "      log(e.nodeName + '=' + e.nodeValue);\n"
            + "    } else {\n"
            + "      log('null');\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(createTestHTML(html));
    }
}
