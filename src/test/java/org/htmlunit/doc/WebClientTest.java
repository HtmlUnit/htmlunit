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
package org.htmlunit.doc;

import java.util.TimeZone;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;


/**
 * Tests for the sample code from the documentation to make sure
 * we adapt the docu or do not break the samples.
 *
 * @author Ronald Brill
 */
public class WebClientTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void homePage_Firefox() throws Exception {
        try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
            // disable javascript
            webClient.getOptions().setJavaScriptEnabled(false);
            // disable css support
            webClient.getOptions().setCssEnabled(false);

            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");
            assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void homePage_Firefox2() throws Exception {
        try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
            // proceed with the js execution on unhandled js errors
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");
            assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void changeBrowserLanguage() throws Exception {
        final BrowserVersion.BrowserVersionBuilder builder =
                        new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX);

        builder.setSystemTimezone(TimeZone.getTimeZone("Europe/Berlin"));
        builder.setBrowserLanguage("de-DE");
        builder.setAcceptLanguageHeader("de-DE,de");

        final BrowserVersion germanFirefox = builder.build();
        try (WebClient webClient = new WebClient(germanFirefox)) {
            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");
            assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void changeUserAgent() throws Exception {
        final BrowserVersion.BrowserVersionBuilder builder =
                        new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX);

        builder.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 14_5 like Mac OS X) "
                + "AppleWebKit/605.1.15 (KHTML, like Gecko) FxiOS/128.0 Mobile/15E148 Safari/605.1.15");

        final BrowserVersion iosFirefox = builder.build();
        try (WebClient webClient = new WebClient(iosFirefox)) {
            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");
            assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());
        }
    }
}
