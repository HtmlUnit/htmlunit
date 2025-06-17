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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link Window} that use background jobs.
 *
 * @author Ronald Brill
 */
public class WindowConcurrency2Test extends WebDriverTestCase {

    /**
     * When <tt>setInterval()</tt> is called with a 0 millisecond delay, Internet Explorer turns it
     * into a <tt>setTimeout()</tt> call, and Firefox imposes a minimum timer restriction.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("xxx")
    public void setIntervalZeroDelay() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'></div>\n"
            + "<script>\n"
            + "  var count = 0;\n"

            + "  function doTimeout() {\n"
            + "    document.getElementById('d').innerHTML += 'x';\n"
            + "    count++;\n"
            + "    if (count > 2) {\n"
            + "      clearInterval(id);\n"
            + "    }\n"
            + "  }\n"

            + "  var id = setInterval(doTimeout, 0);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        verify(() -> driver.findElement(By.id("d")).getText(), getExpectedAlerts()[0]);
    }
}
