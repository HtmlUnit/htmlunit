/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlTable}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlTable2Test extends WebDriverTestCase {

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("One Two\n1 2")
    public void getVisibleText() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <table id='tester'>"
            + "    <tr><th>One</th><th>Two</th></tr>"
            + "    <tr><td>1</td><td>2</td></tr>"
            + "  </table>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLTableElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table id='myId'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlTable.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * Table can have multiple children of &lt;thead&gt;, &lt;tbody&gt; and &lt;tfoot&gt;.
     * Also, IE adds TR between THEAD and TD if missing.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TBODY->TR->TD->Two", "THEAD->TR->TD->One", "THEAD->TR->TD->Three"})
    public void two_theads() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    for (var child = myTable1.firstChild; child != null; child = child.nextSibling) {\n"
            + "      alert(debug(child));\n"
            + "    }\n"
            + "  }\n"
            + "  function debug(node) {\n"
            + "    return node.nodeValue != null ? node.nodeValue : (node.nodeName + '->' + debug(node.firstChild));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<table id='myTable1'>"
            + "<td>Two</td>"
            + "<thead><td>One</td></thead>"
            + "<thead><tr><td>Three</td></tr></thead>"
            + "</table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
