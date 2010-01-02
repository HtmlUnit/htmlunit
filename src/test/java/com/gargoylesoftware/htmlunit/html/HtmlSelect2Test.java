/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlSelect}, but with BrowserRunner.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlSelect2Test extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "two", "" }, FF = { "two", "two" })
    public void value() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.value);\n"
            + "    select.value = 'three';\n"
            + "    alert(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option value='one'>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "two", "" }, FF = { "two", "One" })
    public void value2() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.value);\n"
            + "    select.value = 'One';\n"
            + "    alert(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }
}
