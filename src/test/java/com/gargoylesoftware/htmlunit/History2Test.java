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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link History} with {@link WebClient}.
 *
 * @author Madis Pärn
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class History2Test extends SimpleWebTestCase {

    /**
     * Tests history {@link WebClientOptions#setHistorySizeLimit(int) sizeLimit}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void historyCacheLimit() throws Exception {
        final String content = "<html><head>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  for(var idx = 0; idx < 100; idx++) {\n"
                + "    history.pushState({}, 'Page '+idx, 'page_'+idx+'.html');\n"
                + "  }\n"
                + "  alert(history.length);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.getOptions().setHistorySizeLimit(5);
        loadPageWithAlerts(content);
    }

    /**
     * Tests going back in history should use the page cache.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void historyCacheSize() throws Exception {
        final String content = "<html><head><title></title>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        final int startCount = getMockWebConnection().getRequestCount();
        getMockWebConnection().setDefaultResponse(content);
        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.getOptions().setHistorySizeLimit(5);

        final TopLevelWindow window = (TopLevelWindow) webClient.getCurrentWindow();
        final History history = window.getHistory();

        loadPage(content);
        loadPage(content);
        loadPage(content);
        loadPage(content);
        history.back();
        history.back();
        history.back();
        history.back();

        assertEquals(4, getMockWebConnection().getRequestCount() - startCount);
    }

    /**
     * Tests going back in history should use the page cache, but we have
     * to respect the HistoryPageCacheLimit.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void historyPageCacheLimit() throws Exception {
        final String content = "<html><head>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        final int startCount = getMockWebConnection().getRequestCount();
        getMockWebConnection().setDefaultResponse(content);
        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.getOptions().setHistorySizeLimit(5);
        webClient.getOptions().setHistoryPageCacheLimit(2);

        final TopLevelWindow window = (TopLevelWindow) webClient.getCurrentWindow();
        final History history = window.getHistory();

        loadPage(content);
        loadPage(content);
        loadPage(content);
        loadPage(content);
        history.back();
        history.back();
        history.back();

        assertEquals(6, getMockWebConnection().getRequestCount() - startCount);
    }

    /**
     * Tests going back in history should use the page cache, but we have
     * to respect the HistoryPageCacheLimit.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void historyPageCacheLimitZero() throws Exception {
        final String content = "<html><head>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        final int startCount = getMockWebConnection().getRequestCount();
        getMockWebConnection().setDefaultResponse(content);
        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.getOptions().setHistorySizeLimit(5);
        webClient.getOptions().setHistoryPageCacheLimit(0);

        final TopLevelWindow window = (TopLevelWindow) webClient.getCurrentWindow();
        final History history = window.getHistory();

        loadPage(content);
        loadPage(content);
        loadPage(content);
        loadPage(content);
        history.back();
        history.back();
        history.back();

        assertEquals(7, getMockWebConnection().getRequestCount() - startCount);
    }

    /**
     * Tests going back in history should use the page cache, but we have
     * to respect the HistoryPageCacheLimit.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void historyPageCacheLimitMinusOne() throws Exception {
        final String content = "<html><head>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        final int startCount = getMockWebConnection().getRequestCount();
        getMockWebConnection().setDefaultResponse(content);
        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.getOptions().setHistorySizeLimit(5);
        webClient.getOptions().setHistoryPageCacheLimit(-1);

        final TopLevelWindow window = (TopLevelWindow) webClient.getCurrentWindow();
        final History history = window.getHistory();

        loadPage(content);
        loadPage(content);
        loadPage(content);
        loadPage(content);
        history.back();
        history.back();
        history.back();

        assertEquals(7, getMockWebConnection().getRequestCount() - startCount);
    }
}
