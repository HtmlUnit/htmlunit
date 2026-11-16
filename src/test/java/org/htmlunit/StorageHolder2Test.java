/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit;

import java.util.Map;

import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StorageHolder}.
 *
 * @author Ronald Brill
 */
public class StorageHolder2Test extends SimpleWebTestCase {

    /**
     * @throws Exception in case of error
     */
    @Test
    public void populateLocalStorage() throws Exception {
        final WebClient webClient = getWebClient();

        final Map<String, String> localStorage = webClient.getStorageHolder().getLocalStorage(URL_FIRST);
        assertEquals(0, localStorage.size());

        localStorage.put("myCat", "Tom");

        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "  document.title = localStorage.getItem('myCat', 'Tom');"
            + "</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(1, localStorage.size());
        assertEquals("Tom", page.getTitleText());
    }

    /**
     * @throws Exception in case of error
     */
    @Test
    public void populateSessionStorage() throws Exception {
        final WebClient webClient = getWebClient();

        final Map<String, String> sessionStorage =
                    webClient.getStorageHolder().getSessionStorage(webClient.getCurrentWindow(), URL_FIRST);
        assertEquals(0, sessionStorage.size());

        sessionStorage.put("myCat", "Tom");

        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "  document.title = sessionStorage.getItem('myCat', 'Tom');"
            + "</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(1, sessionStorage.size());
        assertEquals("Tom", page.getTitleText());
    }
}
