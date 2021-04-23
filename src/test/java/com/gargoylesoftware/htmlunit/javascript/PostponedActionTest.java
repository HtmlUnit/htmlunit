/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for the {@link PostponedAction}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Matthias Brandt
 */
@RunWith(BrowserRunner.class)
public class PostponedActionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("before after second.html third.html")
    public void loadingJavaScript() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  document.title += ' before';\n"
            + "  var iframe2 = document.createElement('iframe');\n"
            + "  iframe2.src = 'frame2.html';\n"
            + "  document.body.appendChild(iframe2);\n"
            + "  var iframe3 = document.createElement('iframe');\n"
            + "  document.body.appendChild(iframe3);\n"
            + "  iframe3.src = 'frame3.html';\n"
            + "  document.title += ' after';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent
            = "<script>top.document.title += ' second.html';</script>";
        final String thirdContent
            = "<script>top.document.title += ' third.html';</script>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_FIRST, "frame2.html"), secondContent);
        conn.setResponse(new URL(URL_FIRST, "frame3.html"), thirdContent);

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("before after second.html")
    public void loadingJavaScript2() throws Exception {
        final String firstContent = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  document.title += ' before';\n"
            + "  var iframe = document.createElement('iframe');\n"
            + "  document.body.appendChild(iframe);\n"
            + "  iframe.contentWindow.location.replace('" + URL_SECOND + "');\n"
            + "  document.title += ' after';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent
            = "<script>top.document.title += ' second.html';</script>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Test case for bug #1686.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("setting timeout before after iframe.html simpleAlert")
    public void loadingJavaScriptWithTimeout() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  document.title += ' before';\n"
                + "  var iframe = document.createElement('iframe');\n"
                + "  iframe.src = 'iframe.html';\n"
                + "  document.body.appendChild(iframe);\n"
                + "  document.title += ' after';\n"
                + "}\n"
                + "function timeout() {\n"
                + "  document.title += ' setting timeout';\n"
                + "  window.setTimeout(function() {test()}, 400);\n"
                + "  window.setTimeout(function() {top.document.title += ' simpleAlert'}, 500);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='timeout()'>\n"
                + "</body>\n"
                + "</html>";
        final String secondContent
                = "<script>top.document.title += ' iframe.html'</script>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_FIRST, "iframe.html"), secondContent);

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }
}
