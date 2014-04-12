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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Window} that use background jobs.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WindowConcurrency2Test extends WebDriverTestCase {

    /**
     * When <tt>setInterval()</tt> is called with a 0 millisecond delay, Internet Explorer turns it
     * into a <tt>setTimeout()</tt> call, and Firefox imposes a minimum timer restriction.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "xxx",
            IE8 = "x")
    public void setIntervalZeroDelay() throws Exception {
        final String html
            = "<html><body><div id='d'></div>\n"
            + "<script>\n"
            + "  var count=0;\n"

            + "  function doTimeout() {\n"
            + "    document.getElementById('d').innerHTML += 'x';\n"
            + "    count += 1;\n"
            + "    if (count>2) {;\n"
            + "      clearInterval(id);\n"
            + "    };\n"
            + "  }\n"

            + "  var id = setInterval(doTimeout, 0);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        Thread.sleep(100);
        final String info = driver.findElement(By.id("d")).getText();
        assertEquals(getExpectedAlerts()[0], info);
    }
}
