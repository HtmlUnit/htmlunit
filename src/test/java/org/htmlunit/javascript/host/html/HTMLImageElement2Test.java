/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HTMLImageElement}.
 *
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLImageElement2Test extends SimpleWebTestCase {

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute but javascript is disabled,
     * the image is not downloaded.
     * Issue: 3123380
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoad_notDownloadedWhenJavascriptDisabled() throws Exception {
        final String html = "<html><body><img src='" + URL_SECOND + "' onload='alert(1)'></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        client.getOptions().setJavaScriptEnabled(false);

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_SECOND, "foo", MimeType.IMAGE_PNG);

        loadPageWithAlerts(html);
        assertEquals(URL_FIRST, conn.getLastWebRequest().getUrl());
    }
}
