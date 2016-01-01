/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Notification}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NotificationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function", "true" },
            IE = { "undefined", "false" })
    public void prototype() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  alert(typeof window.Notification);\n"
            + "  alert('Notification' in window);\n"
            + "} catch(e) { alert('exception');}\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "default",
            IE = "exception")
    public void permission() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "    alert(Notification.permission);\n"
            + "} catch(e) { alert('exception');}\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { },
            IE = "exception")
    public void minimalUsage() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "    new Notification('Hello here');\n"
            + "} catch(e) { alert('exception');}\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE = "exception")
    public void requestPermission() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  alert(typeof Notification.requestPermission);\n"
            + "} catch(e) { alert('exception');}\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }
}
