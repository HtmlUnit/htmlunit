/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlOption}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlOption2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "oDown,sDown,dDown,oUp,sUp,dUp,",
            FF3_6 = "sUp,dUp,oUp,sUp,dUp,",
            IE = "sDown,dDown,sUp,dUp,",
            CHROME = "sUp,dUp,")
    // there seems to be a bug in selenium; for FF >= 10 this triggers
    // "sDown,dDown,sUp,dUp,oDown,sDown,dDown,oUp,sUp,dUp," but a
    // manual test shows, that this is wrong.
    @BuggyWebDriver({ Browser.FF10, Browser.FF17 })
    public void onMouse() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <form>\n"
            + "    <div"
                    + " onMouseDown='debug(\"dDown\");'"
                    + " onMouseUp='debug(\"dUp\");'>\n"
            + "    <select name='select1' size='4'"
                        + " onMouseDown='debug(\"sDown\");'"
                        + " onMouseUp='debug(\"sUp\");'>\n"
            + "      <option id='opt1' value='option1'>Option1</option>\n"
            + "      <option id='opt2' value='option2' selected='selected'>Option2</option>\n"
            + "      <option id='opt3' value='option3'"
                        + " onMouseDown='debug(\"oDown\");'"
                        + " onMouseUp='debug(\"oUp\");'>Option3</option>\n"
            + "    </select>\n"
            + "    </div>\n"
            + "  </form>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("opt3")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }
}
