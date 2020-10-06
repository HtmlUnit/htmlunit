/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.doc;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;

/**
 * Tests for the sample code from the documentation to make sure
 * we adapt the docu or do not break the samples.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class FaqTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xhtmlPageFromString() throws Exception {
        final BrowserVersion browserVersion = BrowserVersion.FIREFOX;

        final String htmlCode = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\""
                + "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "  <head>"
                + "    <title>Title</title>"
                + "  </head>"
                + "  <body>"
                + "    content..."
                + "  </body>"
                + "</html> ";
        try (WebClient webClient = new WebClient(browserVersion)) {
            final HTMLParser htmlParser = webClient.getPageCreator().getHtmlParser();
            final WebWindow webWindow = webClient.getCurrentWindow();

            final StringWebResponse webResponse =
                    new StringWebResponse(htmlCode, new URL("http://htmlunit.sourceforge.net/test.html"));
            final XHtmlPage page = new XHtmlPage(webResponse, webWindow);
            webWindow.setEnclosedPage(page);

            htmlParser.parse(webResponse, page, true);
            // work with the html page

            assertEquals("Title\r\ncontent...", page.asText());
        }
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void htmlPageFromString() throws Exception {
        final BrowserVersion browserVersion = BrowserVersion.FIREFOX;

        final String htmlCode = "<html>"
                + "  <head>"
                + "    <title>Title</title>"
                + "  </head>"
                + "  <body>"
                + "    content..."
                + "  </body>"
                + "</html> ";
        try (WebClient webClient = new WebClient(browserVersion)) {
            final HTMLParser htmlParser = webClient.getPageCreator().getHtmlParser();
            final WebWindow webWindow = webClient.getCurrentWindow();

            final StringWebResponse webResponse =
                    new StringWebResponse(htmlCode, new URL("http://htmlunit.sourceforge.net/test.html"));
            final HtmlPage page = new HtmlPage(webResponse, webWindow);
            webWindow.setEnclosedPage(page);

            htmlParser.parse(webResponse, page, true);
            // work with the html page

            assertEquals("Title\r\ncontent...", page.asText());
        }
    }
}
