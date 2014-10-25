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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.ACTIVEX_CHECK;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMNamedNodeMap}.
 *
 * @version $Revision$
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
    @Alerts(DEFAULT = "no ActiveX", IE = "[object Object]")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(attrs));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "1")
    public void length() throws Exception {
        tester("alert(attrs.length);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "undefined")
    public void byName_attribute() throws Exception {
        tester("alert(attrs.name);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "undefined")
    public void byName_map() throws Exception {
        tester("alert(attrs['name']);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "name")
    public void byNumber() throws Exception {
        tester("alert(attrs[0].nodeName);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void byNumber_unknown() throws Exception {
        tester("debug(attrs[1]);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "name=y")
    public void getNamedItem() throws Exception {
        tester("debug(attrs.getNamedItem('name'));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void getNamedItem_unknown() throws Exception {
        tester("debug(attrs.getNamedItem('unknown'));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void getNamedItem_caseSensitive() throws Exception {
        tester("debug(attrs.getNamedItem('NaMe'));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception-getNull", FF = "no ActiveX")
    public void getNamedItem_null() throws Exception {
        final String test = ""
            + "try {\n"
            + "  attrs.getNamedItem(null);\n"
            + "} catch(e) { alert('exception-getNull'); }\n";

        tester(test, "<blah/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "name=y")
    public void item() throws Exception {
        tester("debug(attrs.item(0));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void item_unknown() throws Exception {
        tester("debug(attrs.item(1));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "2", "name1=y1", "name2=y2", "null" })
    public void nextNode() throws Exception {
        final String test = ""
            + "alert(attrs.length);\n"
            + "debug(attrs.nextNode());\n"
            + "debug(attrs.nextNode());\n"
            + "debug(attrs.nextNode());\n";

        tester(test, "<blah name1='y1' name2='y2'/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "1", "name=y", "0", "null", "name=y" })
    public void removeNamedItem() throws Exception {
        final String test = ""
            + "alert(attrs.length);\n"
            + "debug(attrs.getNamedItem('name'));\n"
            + "var attr = attrs.removeNamedItem('name');\n"
            + "alert(attrs.length);\n"
            + "debug(attrs.getNamedItem('name'));\n"
            + "debug(attr);\n";

        tester(test);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "1", "1", "null" })
    public void removeNamedItem_unknown() throws Exception {
        final String test = ""
            + "alert(attrs.length);\n"
            + "var attr = attrs.removeNamedItem('other');\n"
            + "alert(attrs.length);\n"
            + "debug(attr);\n";

        tester(test);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "1", "1", "null" })
    public void removeNamedItem_caseSensitive() throws Exception {
        final String test = ""
            + "alert(attrs.length);\n"
            + "var attr = attrs.removeNamedItem('NaMe');\n"
            + "alert(attrs.length);\n"
            + "debug(attr);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "0", "exception-removeNull" })
    public void removeNamedItem_null() throws Exception {
        final String test = ""
            + "alert(attrs.length);\n"
            + "try {\n"
            + "  attrs.removeNamedItem(null);\n"
            + "} catch(e) { alert('exception-removeNull'); }\n";

        tester(test, "<blah/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "2", "name1=y1", "name1=y1", "name2=y2", "name1=y1" })
    public void reset() throws Exception {
        final String test = ""
            + "alert(attrs.length);\n"
            + "debug(attrs.nextNode());\n"
            + "attrs.reset();"
            + "debug(attrs.nextNode());\n"
            + "debug(attrs.nextNode());\n"
            + "attrs.reset();"
            + "debug(attrs.nextNode());\n";

        tester(test, "<blah name1='y1' name2='y2'/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "1", "myAttr=", "true" })
    public void setNamedItem() throws Exception {
        final String test = ""
            + "alert(attrs.length);\n"
            + "var attr = doc.createAttribute('myAttr');\n"
            + "var node = attrs.setNamedItem(attr);\n"
            + "alert(attrs.length);\n"
            + "debug(attrs.getNamedItem('myAttr'));\n"
            + "alert(node === attr);\n";

        tester(test, "<blah/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "0", "exception-setNull" })
    public void setNamedItem_null() throws Exception {
        final String test = ""
            + "alert(attrs.length);\n"
            + "try {\n"
            + "  attrs.setNamedItem(null);\n"
            + "} catch(e) { alert('exception-setNull'); }\n";

        tester(test, "<blah/>");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<blah name='y'/>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    try {\n"
            + "      var attrs = doc.documentElement.attributes;\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    if (e != null) {\n"
            + "      alert(e.nodeName + '=' + e.nodeValue);\n"
            + "    } else {\n"
            + "      alert('null');\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
