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

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlEmbed}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlEmbedTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLEmbedElement]", "[object HTMLCollection]", "1", "[object HTMLEmbedElement]"})
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "    alert(document.embeds);\n"
            + "    alert(document.embeds.length);\n"
            + "    alert(document.embeds[0]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <embed id='myId'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("myId")));
            assertTrue(HtmlEmbed.class.isInstance(element));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLEmbedElement]")
    public void saveAs() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <embed id='myId' src='helloworld.bin'/>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("something");
        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlEmbed element = (HtmlEmbed) toHtmlElement(driver.findElement(By.id("myId")));
            final File file = new File(System.getProperty("user.home"), "htmlunit-embed.bin");
            element.saveAs(file);
            final long length = file.length();
            assertTrue(file.delete());
            assertTrue(length > 0);
        }
    }
}
