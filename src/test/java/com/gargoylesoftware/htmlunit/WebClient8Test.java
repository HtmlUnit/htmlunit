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

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link WebClient} running with js disabled.
 *
 * @author Ronald Brill
 * @author Ronny Shapiro
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

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void appendChildMoved() throws Exception {
        final String html = "<html>\n"
                + "<head><title>foo</title></head>\n"
                + "<body>\n"
                + "<p>hello</p>\n"
                + "</body></html>";

        final String html2 = "<html>\n"
                + "<head><title>foo</title></head>\n"
                + "<body>\n"
                + "<p id='tester'>world</p>\n"
                + "</body></html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final HtmlPage page = loadPage(webClient, html, null, URL_FIRST);
            final HtmlPage page2 = loadPage(webClient, html2, null, URL_SECOND);

            final DomNodeList<DomElement> elements = page.getElementsByTagName("*");
            assertEquals(5, elements.getLength());

            page.getBody().appendChild(page2.getElementById("tester"));
            assertEquals(6, elements.getLength());
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void iFrame() throws Exception {
        final String html = "<html>\n"
                + "<head><title>foo</title></head>\n"
                + "<body>\n"
                + "  <iframe id='tester' src='second.html'></iframe>\n"
                + "</body></html>";

        final String html2 = "<html>\n"
                + "<head><title>frame</title></head>\n"
                + "<body>\n"
                + "</body></html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final MockWebConnection webConnection = getMockWebConnection();
            webConnection.setResponse(URL_FIRST, html);
            webConnection.setDefaultResponse(html2);
            webClient.setWebConnection(webConnection);

            final HtmlPage page = webClient.getPage(URL_FIRST);

            final HtmlInlineFrame iFrame = (HtmlInlineFrame) page.getElementById("tester");
            assertEquals("frame", ((HtmlPage) iFrame.getEnclosedWindow().getEnclosedPage()).getTitleText());
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void iFrameTextContent() throws Exception {
        final String html = "<html>\n"
                + "<head><title>foo</title></head>\n"
                + "<body>\n"
                + "  <iframe id='tester' src='second.html'></iframe>\n"
                + "</body></html>";

        final String plainContent = "plain frame content";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final MockWebConnection webConnection = getMockWebConnection();
            webConnection.setResponse(URL_FIRST, html);
            webConnection.setDefaultResponse(plainContent, 200, "OK", MimeType.TEXT_PLAIN);
            webClient.setWebConnection(webConnection);

            final HtmlPage page = webClient.getPage(URL_FIRST);

            final HtmlInlineFrame iFrame = (HtmlInlineFrame) page.getElementById("tester");
            assertEquals(plainContent, ((TextPage) iFrame.getEnclosedWindow().getEnclosedPage()).getContent());
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void iFrameInFragment() throws Exception {
        final String html = "<html>\n"
                + "<head><title>foo</title></head>\n"
                + "<body>\n"
                + "<p id='para'>hello</p>\n"
                + "</body></html>";

        final String html2 = "<html>\n"
                + "<head><title>frame</title></head>\n"
                + "<body>\n"
                + "</body></html>";

        final String fragment = "<iframe id='tester' src='second.html'></iframe>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final MockWebConnection webConnection = getMockWebConnection();
            webConnection.setResponse(URL_FIRST, html);
            webConnection.setDefaultResponse(html2);
            webClient.setWebConnection(webConnection);

            final HtmlPage page = webClient.getPage(URL_FIRST);
            final DomElement para = page.getElementById("para");
            page.getWebClient().getPageCreator().getHtmlParser().parseFragment(para, fragment);

            final HtmlInlineFrame iFrame = (HtmlInlineFrame) page.getElementById("tester");
            assertEquals("frame", ((HtmlPage) iFrame.getEnclosedWindow().getEnclosedPage()).getTitleText());
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void script() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "  <script src='script.js'></script>\n"
                + "  <script src='script.js' async></script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        final String script = "alert('test');";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final MockWebConnection webConnection = getMockWebConnection();
            webConnection.setResponse(URL_FIRST, html);
            webConnection.setDefaultResponse(script);
            webClient.setWebConnection(webConnection);

            webClient.getPage(URL_FIRST);
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void link() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "  <link rel='stylesheet' href='simple.css'>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final MockWebConnection webConnection = getMockWebConnection();
            webConnection.setResponse(URL_FIRST, html);
            webConnection.setResponse(new URL(URL_FIRST, "simple.css"), "");

            webClient.setWebConnection(webConnection);

            webClient.getPage(URL_FIRST);
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void object() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <object type='application/pdf' classid='cls12345'></object>\n"
                + "</body></html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final MockWebConnection webConnection = getMockWebConnection();
            webConnection.setResponse(URL_FIRST, html);
            webConnection.setResponse(new URL(URL_FIRST, "simple.css"), "");

            webClient.setWebConnection(webConnection);

            webClient.getPage(URL_FIRST);
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void svgScript() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
                + "    <script id='myId'></script>\n"
                + "  </svg>\n"
                + "</body></html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final HtmlPage page = loadPage(webClient, html, null, URL_FIRST);
            assertEquals(page.getBody().getChildElementCount(), 1);
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void invalidElementEventWithNoJS() throws Exception {
        final String html = "<html>"
                + "<head>"
                + "  <title>foo</title>"
                + "</head>"
                + "  <body onLoad='ready()'>"
                + "  </body>"
                + "</html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            loadPage(webClient, html, null, URL_FIRST);
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void checkingWithNoJS() throws Exception {
        final String html = "<html>"
                + "<head>"
                + "  <title>foo</title>"
                + "</head>"
                + "  <body>"
                + "    <form id='form1'>\n"
                + "      <input type='checkbox' name='checkbox' id='checkbox'>Check me</input>\n"
                + "      <input type='radio' name='radio' id='radio'>Check me</input>\n"
                + "    </form>"
                + "  </body>"
                + "</html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            final HtmlPage page = loadPage(webClient, html, null, URL_FIRST);

            final HtmlCheckBoxInput checkBox = page.getHtmlElementById("checkbox");
            checkBox.setChecked(true);

            final HtmlRadioButtonInput radioButton = page.getHtmlElementById("radio");
            radioButton.setChecked(true);
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void frameSetWithNoJS() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "</head>\n"
                + "<frameset cols='200,*' frameborder='0' framespacing='0' border='0' >"
                + "  <frame src='menu.html' marginheight=0 marginwidth=0"
                                    + " frameborder=0 scrolling='no' noresize name='leftarea'>"
                + "  <frame src='intro.html' marginheight=0 marginwidth=0"
                                    + " frameborder=0 noresize name='mainarea'>"
                + "</frameset>"
                + "</html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            loadPage(webClient, html, null, URL_FIRST);
        }
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void imageEventHandlersWithNoJs() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "  <body>\n"
                + "    <img onerror='doSomething(this)' />\n"
                + "  </body>\n"
                + "</html>";

        try (WebClient webClient = new WebClient(getBrowserVersion(), false, null, -1)) {
            loadPage(webClient, html, null, URL_FIRST);
        }
    }
}
