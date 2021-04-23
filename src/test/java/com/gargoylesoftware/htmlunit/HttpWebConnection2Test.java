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
package com.gargoylesoftware.htmlunit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnection2Test extends WebDriverTestCase {

    /**
     * Test for broken gzip content.
     * @throws Exception if the test fails
     */
    @Test
    public void brokenGzip() throws Exception {
        final byte[] content = new byte[] {-1};
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        headers.add(new NameValuePair(HttpHeader.CONTENT_LENGTH, String.valueOf(content.length)));

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, content, 404, "OK", MimeType.TEXT_HTML, headers);

        // only check that no exception is thrown
        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);
        assertTrue(driver.getPageSource().length() > 100);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void redirectBrokenGzip() throws Exception {
        final String html = "<html></html>";

        final List<NameValuePair> headers = Arrays.asList(new NameValuePair("Location", URL_SECOND.toString()),
                new NameValuePair("Content-Encoding", "gzip"));
        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, "12", 302, "Some error", MimeType.TEXT_HTML, headers);
        conn.setResponse(URL_SECOND, html);

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);
        assertEquals(URL_SECOND.toString(), driver.getCurrentUrl());
    }

}
