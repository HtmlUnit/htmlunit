/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link Document}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Document2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void createElementWithAngleBrackets() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var select = document.createElement('<select>');\n"
            + "    alert(select.add == undefined);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "DIV", "exception" },
            IE = { "DIV", "false", "mySelect", "0", "OPTION", "myOption", "0" })
    public void createElementWithHtml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.createElement('<div>').tagName);\n"
            + "      var select = document.createElement(\"<select id='mySelect'><option>hello</option>\");\n"
            + "      alert(select.add == undefined);\n"
            + "      alert(select.id);\n"
            + "      alert(select.childNodes.length);\n"
            + "      var option = document.createElement(\"<option id='myOption'>\");\n"
            + "      alert(option.tagName);\n"
            + "      alert(option.id);\n"
            + "      alert(option.childNodes.length);\n"
            + "    }\n"
            + "    catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Dedicated test for 3410657.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "false", IE = "true")
    public void createElementPrototype() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  var HAS_EXTENDED_CREATE_ELEMENT_SYNTAX = (function() {\n"
            + "    try {\n"
            + "      var el = document.createElement('<input name=\"x\">');\n"
            + "      return el.tagName.toLowerCase() === 'input' && el.name === 'x';\n"
            + "    } catch (err) {\n"
            + "      return false;\n"
            + "    }\n"
            + "  })();\n"
            + "  alert(HAS_EXTENDED_CREATE_ELEMENT_SYNTAX)\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
