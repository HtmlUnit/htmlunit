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
package com.gargoylesoftware.htmlunit.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;

/**
 * Tests for the sample code from the documentation to make sure
 * we adapt the docu or do not break the samples.
 *
 * @author Ronald Brill
 */
public class FaqTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xhtmlPageFromString() throws Exception {
        final String ls = System.lineSeparator();
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
            final XHtmlPage page = webClient.loadXHtmlCodeIntoCurrentWindow(htmlCode);

            // work with the xhtml page

            assertEquals("Title" + ls + "content...", page.asText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void htmlPageFromString() throws Exception {
        final String ls = System.lineSeparator();
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
            final HtmlPage page = webClient.loadXHtmlCodeIntoCurrentWindow(htmlCode);

            // work with the html page

            assertEquals("Title" + ls + "content...", page.asText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void checkSvgSupport() throws Exception {
        final String svg =  "<svg xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.w3.org/2000/svg\">"
                + "<circle cx=\"5\" cy=\"5\" r=\"4\" stroke=\"black\" stroke-width=\"1\" fill=\"red\" />"
                + "</svg>";
        final BufferedImage img = ImageIO.read(new ByteArrayInputStream(svg.getBytes(StandardCharsets.US_ASCII)));
        assertNotNull(img);
    }
}
