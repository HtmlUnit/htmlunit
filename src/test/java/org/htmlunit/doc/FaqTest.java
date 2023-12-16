/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.htmlunit.BrowserVersion;
import org.htmlunit.ScriptPreProcessor;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.XHtmlPage;
import org.junit.Test;

/**
 * Tests for the sample code from the documentation to make sure
 * we adapt the documentation or do not break the samples.
 *
 * @author Ronald Brill
 */
public class FaqTest {

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
            final XHtmlPage page = webClient.loadXHtmlCodeIntoCurrentWindow(htmlCode);

            // work with the xhtml page

            assertEquals("Title\ncontent...", page.asNormalizedText());
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
            final HtmlPage page = webClient.loadXHtmlCodeIntoCurrentWindow(htmlCode);

            // work with the html page

            assertEquals("Title\ncontent...", page.asNormalizedText());
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void scriptPreProcessor() throws Exception {
        final URL url = new URL("https://www.htmlunit.org");

        // create a ScriptPreProcessor
        final ScriptPreProcessor myScriptPreProcessor = new ScriptPreProcessor() {

            @Override
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                    final int lineNumber, final HtmlElement htmlElement) {

                // modify the source code here

                return sourceCode;
            }
        };

        try (WebClient webClient = new WebClient()) {
            // activate the ScriptPreProcessor
            webClient.setScriptPreProcessor(myScriptPreProcessor);

            // use the client as usual
            final HtmlPage page = webClient.getPage(url);
        }
    }
}
