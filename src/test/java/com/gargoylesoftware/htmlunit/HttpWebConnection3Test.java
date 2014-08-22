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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.util.Arrays;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests using the {@link PrimitiveWebServer}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnection3Test extends WebDriverTestCase {

    private PrimitiveWebServer primitiveWebServer_;

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = { "Host", "Connection", "Accept", "User-Agent", "Accept-Encoding", "Accept-Language" }, 
        FF = { "Host", "User-Agent", "Accept", "Accept-Language", "Accept-Encoding", "Connection" },
        IE = { "Accept", "Accept-Language", "User-Agent", "Accept-Encoding", "Host", "DNT", "Connection" })
    @NotYetImplemented({ CHROME, IE })
    public void headers() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Length: 2\r\n"
            + "Content-Type: text/plain\r\n"
            + "\r\n"
            + "Hi";

        primitiveWebServer_ = new PrimitiveWebServer(PORT, response.getBytes());
        primitiveWebServer_.start();
        final WebDriver driver = getWebDriver();

        driver.get("http://localhost:" + PORT + "");
        final String firstRequest = primitiveWebServer_.getRequests().get(0);
        final String[] headers = firstRequest.split("\\r\\n");
        final String[] result = new String[headers.length - 1];
        for (int i = 0; i < result.length; i++) {
            final String header = headers[i + 1];
            result[i] = header.substring(0, header.indexOf(':'));
        }
        assertEquals(Arrays.asList(getExpectedAlerts()).toString(), Arrays.asList(result).toString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @After
    public void stopServer() throws Exception {
        if (primitiveWebServer_ != null) {
            primitiveWebServer_.stop();
        }
    }
}
