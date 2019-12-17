/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebClient} running with js disabled.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebClient8Test extends SimpleWebTestCase {

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void asText() throws Exception {
        final String ls = System.lineSeparator();
        final String html =
                "<html><head><title>foo</title></head>\n"
                + "<body><div>Hello <b>HtmlUnit</b></div></body></html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final HtmlPage page = loadPage(webClient, html, null, URL_FIRST);
            assertEquals("foo" + ls + "Hello HtmlUnit", page.asText());
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void asXml() throws Exception {
        final String html =
                "<html><head><title>foo</title></head>\n"
                + "<body><div>Hello <b>HtmlUnit</b></div></body></html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final HtmlPage page = loadPage(webClient, html, null, URL_FIRST);
            assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n<html>\r\n"
                    + "  <head>\r\n"
                    + "    <title>\r\n      foo\r\n    </title>\r\n"
                    + "  </head>\r\n"
                    + "  <body>\r\n"
                    + "    <div>\r\n      Hello \r\n"
                    + "      <b>\r\n        HtmlUnit\r\n      </b>\r\n"
                    + "    </div>\r\n"
                    + "  </body>\r\n"
                    + "</html>\r\n",
                    page.asXml());
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void cloneNode() throws Exception {
        final String html = "<html>\n"
                + "<head><title>foo</title></head>\n"
                + "<body>\n"
                + "<p>hello world</p>\n"
                + "</body></html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final HtmlPage page = loadPage(webClient, html, null, URL_FIRST);

            final String org = page.asXml();

            final HtmlPage clonedPage = page.cloneNode(true);
            final String clone = clonedPage.asXml();

            assertEquals(org, clone);
        }
    }
}
