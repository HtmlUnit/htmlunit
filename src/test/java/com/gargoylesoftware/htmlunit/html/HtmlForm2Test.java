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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlForm}, with BrowserRunner.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlForm2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "myForm", "myForm" },
            FF = { "myForm", "TypeError" })
    public void formsAccessor_FormsAsFunction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    try {\n"
            + "        alert(document.forms[0].id);\n"
            + "        alert(document.forms(0).id);\n"
            + "    } catch (err) {\n"
            + "        alert('TypeError');\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form id='myForm'>\n"
            + "    <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "myForm", "myForm" },
            FF = { "myForm", "TypeError" })
    public void formsAccessor_FormsAsFunction2() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    try {\n"
            + "        alert(document.forms['myName'].id);\n"
            + "        alert(document.forms('myName').id);\n"
            + "    } catch (err) {\n"
            + "        alert('TypeError');\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form id='myForm' name='myName'>\n"
            + "    <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "textfieldid", "textfieldname", "textfieldid" },
            FF = { "error", "error", "error" })
    public void asFunction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test(){\n"
            + "  var f1 = document.forms[0];\n"
            + "  try { alert(f1('textfieldid').id) } catch (e) { alert('error') }\n"
            + "  try { alert(f1('textfieldname').name) } catch (e) { alert('error') }\n"
            + "  try { alert(f1(0).id) } catch (e) { alert('error') }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<p>hello world</p>\n"
            + "<form id='firstid' name='firstname'>\n"
            + "  <input type='text' id='textfieldid' value='foo' />\n"
            + "  <input type='text' name='textfieldname' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "textfieldid", "textfieldname", "textfieldid" },
            FF = { "TypeError" })
    public void asFunctionFormsFunction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test(){\n"
            + "  try {\n"
            + "    var f1 = document.forms(0);\n"
            + "    try { alert(f1('textfieldid').id) } catch (e) { alert('error') }\n"
            + "    try { alert(f1('textfieldname').name) } catch (e) { alert('error') }\n"
            + "    try { alert(f1(0).id) } catch (e) { alert('error') }\n"
            + "  } catch (e) { alert('TypeError') }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<p>hello world</p>\n"
            + "<form id='firstid' name='firstname'>\n"
            + "  <input type='text' id='textfieldid' value='foo' />\n"
            + "  <input type='text' name='textfieldname' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
