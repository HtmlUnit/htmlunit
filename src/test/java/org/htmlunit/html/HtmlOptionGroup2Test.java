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
 * Tests for {@link HtmlOptionGroup}.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class HtmlOptionGroup2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLOptGroupElement]")
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <select>\n"
            + "    <optgroup id='myId' label='my label'>\n"
            + "      <option>My Option</option>\n"
            + "    </optgroup>\n"
            + "  </select>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver && getExpectedAlerts()[0].contains("HTMLOptGroupElement")) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(HtmlOptionGroup.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

}
