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
 * Tests for {@link HtmlPreformattedText}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlPreformattedTextTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLPreElement]",
            IE8 = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <pre id='myId'>Some Text</pre>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlPreformattedText.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  hello   abc")
    public void asText() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<pre id='foo'>  hello \t abc</pre>"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("foo")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1\n2\n3\n4")
    public void asTextDifferentLineBreaks() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<pre id='foo'>1\n2\r\n3\r4</pre>"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("foo")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("start\n  hello   abc \nend")
    public void asTextInsideDiv() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<div id='foo'>start<pre>  hello \t abc </pre>end</div>"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("foo")).getText());
    }
}
