/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlObject}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlObject2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLObjectElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <object id='myId'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlObject.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true"})
    public void checkValidity() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity(' ');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('invalid');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('');\n"
            + "    log(foo.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <object id='foo'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
