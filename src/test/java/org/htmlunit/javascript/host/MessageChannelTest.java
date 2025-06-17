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

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link MessageChannel}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class MessageChannelTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Message back from the IFrame, Hello from the main page!")
    public void test() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<p>My body</p>\n"
                + "<iframe src='" + URL_SECOND + "' width='480' height='320'></iframe>\n"
                + "</body>\n"
                + "<script>\n"
                + "  if (window.MessageChannel) {\n"
                + "    var channel = new MessageChannel();\n"
                + "    var para = document.querySelector('p');\n"
                + "    var ifr = document.querySelector('iframe');\n"
                + "    var otherWindow = ifr.contentWindow;\n"
                + "    function iframeLoaded() {\n"
                + "      otherWindow.postMessage('Hello from the main page!', '*', [channel.port2]);\n"
                + "    }\n"
                + "    ifr.addEventListener('load', iframeLoaded, false);\n"
                + "    function handleMessage(e) {\n"
                + "      para.innerHTML = e.data;\n"
                + "    }\n"
                + "    channel.port1.onmessage = handleMessage;\n"
                + "  }\n"
                + "</script></html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>\n"
                + "  <p>iFrame body</p>\n"
                + "</body>\n"
                + "<script>\n"
                + "  if (window.MessageChannel) {\n"
                + "    var para = document.querySelector('p');\n"
                + "    onmessage = function(e) {\n"
                + "      para.innerHTML = e.data;\n"
                + "      e.ports[0].postMessage('Message back from the IFrame');\n"
                + "    }\n"
                + "  }\n"
                + "</script></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        final WebDriver driver = loadPage2(html);
        final List<String> actual = new ArrayList<>();
        actual.add(driver.findElement(By.tagName("p")).getText());
        driver.switchTo().frame(0);
        actual.add(driver.findElement(By.tagName("p")).getText());
        assertEquals(getExpectedAlerts(), actual);
    }

}
