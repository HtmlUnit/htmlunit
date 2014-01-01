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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMNodeList}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMNodeListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(list));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("2")
    public void length() throws Exception {
        tester("alert(list.length);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("0")
    public void length_empty() throws Exception {
        tester("alert(list.length);\n", "<root/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("undefined")
    public void byName_attribute() throws Exception {
        tester("alert(list.child1);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("undefined")
    public void byName_map() throws Exception {
        tester("alert(list['child1']);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("child1=null")
    public void byNumber() throws Exception {
        tester("debug(list[0]);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void byNumber_unknown() throws Exception {
        tester("debug(list[2]);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("child1=null")
    public void item() throws Exception {
        tester("debug(list.item(0));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void item_unknown() throws Exception {
        tester("debug(list.item(2));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "2", "child1=null", "child2=null", "null" })
    public void nextNode() throws Exception {
        final String test = ""
            + "alert(list.length);\n"
            + "debug(list.nextNode());\n"
            + "debug(list.nextNode());\n"
            + "debug(list.nextNode());\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "2", "child1=null", "child1=null", "child2=null", "child1=null" })
    public void reset() throws Exception {
        final String test = ""
            + "alert(list.length);\n"
            + "debug(list.nextNode());\n"
            + "list.reset();"
            + "debug(list.nextNode());\n"
            + "debug(list.nextNode());\n"
            + "list.reset();"
            + "debug(list.nextNode());\n";

        tester(test);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void in() throws Exception {
        tester("alert(0 in list);\n");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void in_length() throws Exception {
        tester("alert('length' in list);\n");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Browsers(IE)
    @Alerts("false")
    public void in_unknownIndex() throws Exception {
        tester("alert(-1 in list);\n");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void in_unknownIndex2() throws Exception {
        tester("alert(2 in list);\n");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Browsers(IE)
    @Alerts("false")
    public void in_unknown() throws Exception {
        tester("alert('child1' in list);\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = ""
            + "<root>"
            + "<child1/>"
            + "<child2/>"
            + "</root>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    try {\n"
            + "      var list = root.childNodes;\n"
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
