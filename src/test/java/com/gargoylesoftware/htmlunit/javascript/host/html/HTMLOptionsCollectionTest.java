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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLOptionsCollection}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLOptionsCollectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "4" })
    public void length() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var sel = document.form1.select0;\n"
            + "  alert(sel.options.length);\n"
            + "  sel = document.form1.select1;\n"
            + "  alert(sel.options.length);\n"
            + "  sel = document.form1.select4;\n"
            + "  alert(sel.options.length);\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select0'>\n"
            + "    </select>\n"
            + "    <select name='select1'>\n"
            + "        <option>One</option>\n"
            + "    </select>\n"
            + "    <select name='select4'>\n"
            + "        <option>One</option>\n"
            + "        <option>Two</option>\n"
            + "        <option>Three</option>\n"
            + "        <option>Four</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "exception", "exception" },
            FF = { "1", "4" })
    public void setLength_negative() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var sel = document.form1.select1;\n"
            + "  try {\n"
            + "    sel.options.length = -1;\n"
            + "    alert(sel.options.length);\n"
            + "  } catch (e) { alert('exception'); }\n"

            + "  var sel = document.form1.select4;\n"
            + "  try {\n"
            + "    sel.options.length = -1;\n"
            + "    alert(sel.options.length);\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option>One</option>\n"
            + "    </select>\n"
            + "    <select name='select4'>\n"
            + "        <option>One</option>\n"
            + "        <option>Two</option>\n"
            + "        <option>Three</option>\n"
            + "        <option>Four</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0" })
    public void setLength_zero() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var sel = document.form1.select0;\n"
            + "  try {\n"
            + "    sel.options.length = 0;\n"
            + "    alert(sel.options.length);\n"
            + "  } catch (e) { alert(e); }\n"

            + "  sel = document.form1.select1;\n"
            + "  try {\n"
            + "    sel.options.length = 0;\n"
            + "    alert(sel.options.length);\n"
            + "  } catch (e) { alert(e); }\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select0'>\n"
            + "    </select>\n"
            + "    <select name='select1'>\n"
            + "        <option>One</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "", "4", "One", "1", "", "0" },
            FF = { "1", "", "4", "One", "1", "", "1" })
    public void setLength_increase() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var sel = document.form1.select0;\n"
            + "  try {\n"
            + "    sel.options.length = 1;\n"
            + "    alert(sel.options.length);\n"
            + "    alert(sel.options[0].text);\n"
            + "  } catch (e) { alert(e); }\n"

            + "  sel = document.form1.select1;\n"
            + "  try {\n"
            + "    sel.options.length = 4;\n"
            + "    alert(sel.options.length);\n"
            + "    alert(sel.options[0].text);\n"
            + "    alert(sel.options[0].childNodes.length);\n"
            + "    alert(sel.options[1].text);\n"
            + "    alert(sel.options[1].childNodes.length);\n"
            + "  } catch (e) { alert(e); }\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select0'>\n"
            + "    </select>\n"
            + "    <select name='select1'>\n"
            + "        <option>One</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
