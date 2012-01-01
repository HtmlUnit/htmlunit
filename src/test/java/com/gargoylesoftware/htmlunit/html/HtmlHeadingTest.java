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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlHeading1} to {@link HtmlHeading6}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlHeadingTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLHeadingElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <h2 id='myId'>asdf</h2>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("myId")));
            assertTrue(element instanceof HtmlHeading2);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "begin"
            + "<h1>in h1</h1>after h1\n"
            + "<h2>in h2</h2>after h2\n"
            + "<h3>in h3</h3>after h3\n"
            + "<h4>in h4</h4>after h4\n"
            + "<h5>in h5</h5>after h5\n"
            + "<h6>in h6</h6>after h6\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final String expectedText = "begin" + LINE_SEPARATOR
            + "in h1" + LINE_SEPARATOR
            + "after h1" + LINE_SEPARATOR
            + "in h2" + LINE_SEPARATOR
            + "after h2" + LINE_SEPARATOR
            + "in h3" + LINE_SEPARATOR
            + "after h3" + LINE_SEPARATOR
            + "in h4" + LINE_SEPARATOR
            + "after h4" + LINE_SEPARATOR
            + "in h5" + LINE_SEPARATOR
            + "after h5" + LINE_SEPARATOR
            + "in h6" + LINE_SEPARATOR
            + "after h6";

        assertEquals(expectedText, page.asText());
    }
}
