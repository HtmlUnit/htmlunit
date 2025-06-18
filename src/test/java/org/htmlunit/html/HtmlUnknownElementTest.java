/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlUnknownElement}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class HtmlUnknownElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLUnknownElement]", "[object HTMLUnknownElement]", "[object HTMLElement]"})
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId1'));\n"
            + "    log(document.getElementById('myId2'));\n"
            + "    log(document.getElementById('myId3'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<xml id='myId1'/>\n"
            + "<doesnt_exist id='myId2'/>\n"
            + "<doesnt-exist id='myId3'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(HtmlUnknownElement.class.isInstance(page.getHtmlElementById("myId1")));
            assertTrue(HtmlUnknownElement.class.isInstance(page.getHtmlElementById("myId2")));
            assertTrue(HtmlUnknownElement.class.isInstance(page.getHtmlElementById("myId3")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLUnknownElement]", "[object HTMLUnknownElement]", "[object HTMLElement]"})
    public void simpleScriptable_strict() throws Exception {
        final String header = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
        final String html = header + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId1'));\n"
            + "    log(document.getElementById('myId2'));\n"
            + "    log(document.getElementById('myId3'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<xml id='myId1'/>\n"
            + "<doesnt_exist id='myId2'/>\n"
            + "<doesnt-exist id='myId3'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(HtmlUnknownElement.class.isInstance(page.getHtmlElementById("myId1")));
            assertTrue(HtmlUnknownElement.class.isInstance(page.getHtmlElementById("myId2")));
            assertTrue(HtmlUnknownElement.class.isInstance(page.getHtmlElementById("myId3")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><title>foo</title>\n"
            + "<foo></foo>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final String xml = driver.getPageSource();
        assertTrue("Node not expanded in: " + xml, xml.contains("</foo>"));
    }
}
