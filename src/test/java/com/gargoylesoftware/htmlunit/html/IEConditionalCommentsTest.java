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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for HtmlUnit's support of IE conditional comments.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms537512.asp">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class IEConditionalCommentsTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "hello", "IE" }, FF = "hello")
    public void ifIE() throws Exception {
        final String html = "<html><head>"
            + "<script>alert('hello')</script>\n"
            + "<!--[if IE]><script>alert('IE')</script><![endif]-->\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "hello", IE6 = { "hello", "IE6" }, FF = "hello")
    public void if_lte_IE6() throws Exception {
        final String html = "<html><head>"
            + "<script>alert('hello')</script>\n"
            + "<!--[if lte IE 6]><script>alert('IE6')</script><![endif]-->\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "hello", "IE up to 7" }, IE8 = "hello", FF = "hello")
    public void if_lte_IE_7() throws Exception {
        final String html = "<html><head>"
            + "<script>alert('hello')</script>\n"
            + "<!--[if lte IE 7]><script>alert('IE up to 7')</script><![endif]-->\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "hello", "lt mso 9" }, FF = "hello")
    public void if_lte_mso_9() throws Exception {
        final String html = "<html><head>"
            + "<script>alert('hello')</script>\n"
            + "<!--[if gte mso 9]><script>alert('gte mso 9')</script><![endif]-->\n"
            + "<!--[if lt mso 9]><script>alert('lt mso 9')</script><![endif]-->\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "<!--[if gte IE]>hello<![endif]-->", "world" }, FF = { "undefined", "undefined" })
    public void incorrectExpression() throws Exception {
        final String html = "<html><head></head><body>"
            + "<div id='div1'><!--[if gte IE]>hello<![endif]--></div>\n"
            + "<div id='div2'><!--[if gte IE 5]>world<![endif]--></div>\n"
            + "<script>\n"
            + "alert(document.getElementById('div1').innerText);\n"
            + "alert(document.getElementById('div2').innerText);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
