/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for CSS selectors.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CSSSelectorTest extends WebDriverTestCase {

    /**
     * Test for bug 1287.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("li2")
    @NotYetImplemented(Browser.IE)
    public void nth_child() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('li:nth-child(2)')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void nth_child_no_argument() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    alert(document.querySelectorAll('li:nth-child()'));\n"
            + "  } catch (e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "li1", "li4", "li7", "li10" })
    @NotYetImplemented(Browser.IE)
    public void nth_child_equation() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var list = document.querySelectorAll('li:nth-child(3n+1)');\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    alert(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "  <li id='li4'></li>\n"
            + "  <li id='li5'></li>\n"
            + "  <li id='li6'></li>\n"
            + "  <li id='li7'></li>\n"
            + "  <li id='li8'></li>\n"
            + "  <li id='li9'></li>\n"
            + "  <li id='li10'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Exception should be thrown for an invalid selector.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void invalid() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    alert(document.querySelectorAll('td:gt(4)').length);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "ul2" })
    public void directAdjacentSelector() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var list = document.querySelectorAll('p+ul');\n"
            + "    alert(list.length);\n"
            + "    alert(list[0].id);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='ul1'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='ul2'></ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "thing1" })
    public void prefixAttribute() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var list = document.querySelectorAll('[id^=\"thing\"]');\n"
            + "    alert(list.length);\n"
            + "    alert(list[0].id);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing1'></ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "something" })
    public void suffixAttribute() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var list = document.querySelectorAll('[id$=\"thing\"]');\n"
            + "    alert(list.length);\n"
            + "    alert(list[0].id);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing2'></ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "2", "something", "thing2" })
    public void substringAttribute() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var list = document.querySelectorAll('[id*=\"thing\"]');\n"
            + "    alert(list.length);\n"
            + "    alert(list[0].id);\n"
            + "    alert(list[1].id);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing2'></ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "ul2" })
    public void generalAdjacentSelector() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var list = document.querySelectorAll('div~ul');\n"
            + "    alert(list.length);\n"
            + "    alert(list[0].id);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <p></p>\n"
            + "  <ul id='ul2'></ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
