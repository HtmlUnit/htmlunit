/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLOptionElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLOptionElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SELECT")
    //TODO: WebDriver tests passes even with HtmlUnit direct usage fails!
    public void click() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  function init() {\n"
            + "    var s = document.getElementById('s');\n"
            + "    if (s.addEventListener) {\n"
            + "      s.addEventListener('click', handle, false);\n"
            + "    } else if (s.attachEvent) {\n"
            + "      s.attachEvent('onclick', handle);\n"
            + "    }\n"
            + "  }\n"
            + "  function handle(event) {\n"
            + "    if (event.target)\n"
            + "      alert(event.target.nodeName);\n"
            + "    else\n"
            + "      alert(event.srcElement.nodeName);\n"
            + "  }\n"
            + "</script></head><body onload='init()'>\n"
            + "  <select id='s'>\n"
            + "    <option value='a'>A</option>\n"
            + "    <option id='opb' value='b'>B</option>\n"
            + "    <option value='c'>C</option>\n"
            + "  </select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
