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
package com.gargoylesoftware.htmlunit;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Unit tests for {@link BrowserVersion}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class BrowserVersion2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            IE = "Accept: text/html, application/xhtml+xml, */*")
    @NotYetImplemented
    public void getUrlAcceptHeader() throws Exception {
        final String html = "<html><body>Response</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    private String acceptHeaderString() {
        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();

        final StringBuilder sb = new StringBuilder();
        for (final Entry<String, String> headerEntry : headers.entrySet()) {
            final String headerName = headerEntry.getKey();
            final String headerNameLower = headerName.toLowerCase(Locale.ENGLISH);
            if ("accept".equals(headerNameLower)) {
                sb.append(headerName);
                sb.append(": ");
                sb.append(headerEntry.getValue());
            }
        }
        return sb.toString();
    }
}
