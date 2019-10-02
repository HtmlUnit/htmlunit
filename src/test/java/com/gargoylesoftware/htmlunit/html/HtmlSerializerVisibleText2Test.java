/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.ISO_8859_1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlSerializerVisibleText.HtmlSerializerTextBuilder;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests for {@link HtmlSerializerVisibleText}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlSerializerVisibleText2Test extends WebDriverTestCase {

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "baz",
            CHROME = "",
            IE = "<foo>\n<bar>baz</bar>\n</foo>")
    @NotYetImplemented({CHROME, IE})
    public void xmlPage() throws Exception {
        final String xml = "<xml>\n"
                + "  <foo>\n"
                + "    <bar>baz</bar>\n"
                + "  </foo>\n"
                + "</xml>";
        final WebDriver driver = loadPage2(xml, URL_FIRST, "text/xml;charset=ISO-8859-1", ISO_8859_1);
        final String text = driver.findElement(By.xpath("//foo")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final XmlPage page = (XmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            DomNode node = page.getFirstByXPath("//foo");
            assertEquals(getExpectedAlerts()[0], node.getVisibleText());
        }
    }
}
