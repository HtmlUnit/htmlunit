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
package com.gargoylesoftware.htmlunit.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * Unit tests for {@link HtmlInlineFrame}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlInlineFrameTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setSrcAttribute() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='" + URL_SECOND + "'>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        assertEquals(URL_SECOND.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Second", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());

        iframe.setSrcAttribute(URL_THIRD.toExternalForm());
        assertEquals(URL_THIRD.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setSrcAttributeWithWhiteSpaces() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='\n" + URL_SECOND + "\n'>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        assertEquals(URL_SECOND.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Second", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());

        iframe.setSrcAttribute(URL_THIRD.toExternalForm());
        assertEquals(URL_THIRD.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());
    }

    /**
     * Tests that a recursive src attribute (i.e. src="#xyz") doesn't result in an
     * infinite loop (bug #459).
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void recursiveSrcAttribute() throws Exception {
        final String html = "<html><body><iframe id='a' src='#abc'></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlInlineFrame iframe = page.getHtmlElementById("a");
        assertNotNull(iframe.getEnclosedPage());
    }

    /**
     * Tests that a recursive src is prevented.
     * @throws Exception if an error occurs
     */
    @Test
    public void recursiveNestedFrames() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='" + URL_SECOND + "'>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head>\n"
            + "<body><iframe id='iframe2_1' src='" + URL_FIRST + "'></iframe></body></html>";
        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        assertEquals(URL_SECOND.toExternalForm(), iframe.getSrcAttribute());
        final HtmlPage iframePage = (HtmlPage) iframe.getEnclosedPage();
        assertEquals("Second", iframePage.getTitleText());

        // the nested frame should not have been loaded
        final HtmlInlineFrame iframeIn2 = iframePage.getHtmlElementById("iframe2_1");
        assertEquals(URL_FIRST.toExternalForm(), iframeIn2.getSrcAttribute());
        final HtmlPage iframePage2 = (HtmlPage) iframeIn2.getEnclosedPage();
        assertEquals(URL_FIRST.toExternalForm(), iframeIn2.getEnclosedPage().getUrl());

        // the nested nested frame should not have been loaded
        final HtmlInlineFrame iframeIn3 = iframePage2.getHtmlElementById("iframe1");
        assertEquals(URL_SECOND.toExternalForm(), iframeIn3.getSrcAttribute());
        assertEquals("about:blank", iframeIn3.getEnclosedPage().getUrl());
    }

    /**
     * Tests that an invalid src attribute (i.e. src="foo://bar") doesn't result
     * in a NPE (bug #458).
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void invalidSrcAttribute() throws Exception {
        final String html = "<html><body><iframe id='a' src='foo://bar'></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlInlineFrame iframe = page.getHtmlElementById("a");
        assertNotNull(iframe.getEnclosedPage());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setSrcAttribute_ViaJavaScript() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='" + URL_SECOND + "'></iframe>\n"
            + "<script type='text/javascript'>document.getElementById('iframe1').src = '" + URL_THIRD + "';\n"
            + "</script></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        assertEquals(URL_THIRD.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());
    }

    /**
     * Verifies that cloned frames do no reload their content (bug #613).
     * @throws Exception if an error occurs
     */
    @Test
    public void frameCloneDoesNotReloadFrame() throws Exception {
        final String html1 = "<html><body><iframe src='" + URL_SECOND + "'></iframe></body></html>";
        final String html2 = "<html><body>abc</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(URL_SECOND, html2);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(2, conn.getRequestCount());

        page.cloneNode(true);
        assertEquals(2, conn.getRequestCount());
    }

    /**
     * Verifies that frames added via document.write() don't get their contents loaded twice (bug #238).
     * @throws Exception if an error occurs
     */
    @Test
    public void frameWriteDoesNotReloadFrame() throws Exception {
        final String html1 =
              "<html><body>\n"
            + "<script>document.write('<iframe id=\"f\" src=\"" + URL_SECOND + "\"></iframe>')</script>\n"
            + "</body></html>";
        final String html2 = "<html><body>iframe content</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(URL_SECOND, html2);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlElement iFrame = page.getHtmlElementById("f");

        assertEquals("iframe", iFrame.getTagName());

        final HtmlPage enclosedPage = (HtmlPage) ((HtmlInlineFrame) iFrame).getEnclosedPage();
        assertEquals("iframe content", enclosedPage.asText());

        assertEquals(2, conn.getRequestCount());
    }

    /**
     * Verifies that frames added via element.innerHtml() are resolved.
     * @throws Exception if an error occurs
     */
    @Test
    public void frameSetInnerHtmlDoesLoadFrame() throws Exception {
        final String html1 =
              "<html><body>\n"
            + "<iframe id='myFrame' src='" + URL_THIRD + "'></iframe>';\n"
            + "<span id='A'></span>\n"
            + "<script>\n"
            + "  var frame='<iframe id=\"f\" src=\"" + URL_SECOND + "\"></iframe>';\n"
            + "  document.getElementById('A').innerHTML = frame;\n"
            + "</script>\n"
            + "</body></html>";
        final String html2 = "<html><body>iframe content</body></html>";
        final String html3 = "<html><head></head><body>Third content</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(URL_SECOND, html2);
        conn.setResponse(URL_THIRD, html3);

        final HtmlPage page = client.getPage(URL_FIRST);

        final HtmlElement myFrame = page.getHtmlElementById("myFrame");
        assertEquals("iframe", myFrame.getTagName());

        HtmlPage enclosedPage = (HtmlPage) ((HtmlInlineFrame) myFrame).getEnclosedPage();
        assertEquals("Third content", enclosedPage.asText());

        final HtmlElement iFrame = page.getHtmlElementById("f");
        assertEquals("iframe", iFrame.getTagName());

        enclosedPage = (HtmlPage) ((HtmlInlineFrame) iFrame).getEnclosedPage();
        assertEquals("iframe content", enclosedPage.asText());

        assertEquals(3, conn.getRequestCount());
    }

    /**
     * Verifies that frames added via element.innerHtml() are resolved.
     * @throws Exception if an error occurs
     */
    @Test
    public void frameSetInnerHtmlDoesLoadFrameContentTimeout() throws Exception {
        final String html1 =
              "<html><body>\n"
            + "<iframe id='myFrame' src='" + URL_THIRD + "'></iframe>';\n"
            + "<span id='A'></span>\n"
            + "<script>\n"
            + "  function createIframe() {\n"
            + "    var frame='<iframe id=\"f\" src=\"" + URL_SECOND + "\"></iframe>';\n"
            + "    document.getElementById('A').innerHTML = frame;\n"
            + "  }\n"
            + "  setTimeout('createIframe()', 100);\n"
            + "</script>\n"
            + "</body></html>";
        final String html2 = "<html><body>iframe content</body></html>";
        final String html3 = "<html><head></head><body>Third content</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(URL_SECOND, html2);
        conn.setResponse(URL_THIRD, html3);

        final HtmlPage page = client.getPage(URL_FIRST);

        final HtmlElement myFrame = page.getHtmlElementById("myFrame");
        assertEquals("iframe", myFrame.getTagName());

        HtmlPage enclosedPage = (HtmlPage) ((HtmlInlineFrame) myFrame).getEnclosedPage();
        assertEquals("Third content", enclosedPage.asText());

        // wait for the timer
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        jobManager.waitForJobs(1000);

        final HtmlElement iFrame = page.getHtmlElementById("f");
        assertEquals("iframe", iFrame.getTagName());

        enclosedPage = (HtmlPage) ((HtmlInlineFrame) iFrame).getEnclosedPage();
        assertEquals("iframe content", enclosedPage.asText());

        assertEquals(3, conn.getRequestCount());
    }

    /**
     * Issue 3046109.
     * The iframe has no source and is filled from javascript.
     * @throws Exception if an error occurs
     */
    @Test
    public void frameContentCreationViaJavascript() throws Exception {
        final String html =
            "<html><head><title>frames</title></head>\n"
            + "<body>\n"
            + "<iframe name='foo'></iframe>\n"
            + "<script type='text/javascript'>\n"
            + "var doc = window.frames['foo'].document;\n"
            + "doc.open();\n"
            + "doc.write('<html><body><div id=\"myContent\">Hi Folks!</div></body></html>');\n"
            + "doc.close();\n"
            + "</script>\n"
            + "<body>\n"
            + "</html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection conn = getMockWebConnection();

        conn.setDefaultResponse(html);
        final HtmlPage page = webClient.getPage(URL_FIRST);

        final HtmlPage enclosedPage = (HtmlPage) page.getFrames().get(0).getEnclosedPage();
        final HtmlElement element = enclosedPage.getHtmlElementById("myContent");
        final String content = element.asText();
        assertEquals("Hi Folks!", content);
    }

    /**
     * The iframe has no source and is filled from javascript.
     * The javascript writes unicode charactes into the iframe content.
     * @throws Exception if an error occurs
     */
    @Test
    public void frameContentCreationViaJavascriptUnicode() throws Exception {
        final String html =
            "<html><head><title>frames</title></head>\n"
            + "<body>\n"
            + "<iframe name='foo'></iframe>\n"
            + "<script type='text/javascript'>\n"
            + "var doc = window.frames['foo'].document;\n"
            + "doc.open();\n"
            + "doc.write('<html><body><div id=\"myContent\">Hello worl\u0414</div></body></html>');\n"
            + "doc.close();\n"
            + "</script>\n"
            + "<body>\n"
            + "</html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection conn = getMockWebConnection();

        conn.setResponse(URL_FIRST, html, "text/html; charset=UTF-8", UTF_8);
        final HtmlPage page = webClient.getPage(URL_FIRST);

        final HtmlPage enclosedPage = (HtmlPage) page.getFrames().get(0).getEnclosedPage();
        final HtmlElement element = enclosedPage.getHtmlElementById("myContent");
        final String content = element.asText();
        assertEquals("Hello worl\u0414", content);
    }

    /**
     * The iframe has no source and is filled from javascript.
     * The javascript writes windows charactes into the iframe content.
     * @throws Exception if an error occurs
     */
    @Test
    public void frameContentCreationViaJavascriptISO_8859_1() throws Exception {
        final String html =
            "<html><head><title>frames</title></head>\n"
            + "<body>\n"
            + "<iframe name='foo'></iframe>\n"
            + "<script type='text/javascript'>\n"
            + "var doc = window.frames['foo'].document;\n"
            + "doc.open();\n"
            + "doc.write('<html><body><div id=\"myContent\">\u00e4\u00f6\u00fc</div></body></html>');\n"
            + "doc.close();\n"
            + "</script>\n"
            + "<body>\n"
            + "</html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection conn = getMockWebConnection();
        webClient.setWebConnection(conn);

        conn.setResponse(URL_FIRST, html, "text/html; charset=ISO-8859-1", ISO_8859_1);
        final HtmlPage page = webClient.getPage(URL_FIRST);

        final HtmlPage enclosedPage = (HtmlPage) page.getFrames().get(0).getEnclosedPage();
        final HtmlElement element = enclosedPage.getHtmlElementById("myContent");
        final String content = element.asText();
        assertEquals("\u00e4\u00f6\u00fc", content);
    }

    /**
     * Issue 3046109.
     * The iframe has a source but is filled from javascript before.
     * @throws Exception if an error occurs
     */
    @Test
    public void frameContentCreationViaJavascriptBeforeFrameResolved() throws Exception {
        final String html =
            "<html><head><title>frames</title></head>\n"
            + "<body>\n"
            + "<iframe name='foo' src='" + URL_SECOND + "'></iframe>\n"
            + "<script type='text/javascript'>\n"
            + "var doc = window.frames['foo'].document;\n"
            + "doc.open();\n"
            + "doc.write('<html><body><div id=\"myContent\">Hi Folks!</div></body></html>');\n"
            + "doc.close();\n"
            + "</script>\n"
            + "<body>\n"
            + "</html>";

        final String frameHtml =
            "<html><head><title>inside frame</title></head>\n"
            + "<body>\n"
            + "<div id=\"myContent\">inside frame</div>\n"
            + "<body>\n"
            + "</html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        webClient.setWebConnection(conn);

        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, frameHtml);
        final HtmlPage page = webClient.getPage(URL_FIRST);

        final HtmlPage enclosedPage = (HtmlPage) page.getFrames().get(0).getEnclosedPage();
        final HtmlElement element = enclosedPage.getHtmlElementById("myContent");
        final String content = element.asText();
        assertEquals("Hi Folks!", content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "before<iframe id='iframe1' src='" + URL_SECOND + "'></iframe>after\n"
            + "</body></html>";
        final String secondContent = "<html><head></head><body>Second content</body></html>";
        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        final HtmlPage page = client.getPage(URL_FIRST);
        final String ls = System.lineSeparator();
        assertEquals("First" + ls + "before" + ls
                + "Second content" + ls + "after", page.asText());
    }

    /**
     * See issue #1825.
     * @exception Exception If the test fails
     */
    @Test
    public void brokenIframe() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>"
                + "1<div>2<iframe/>3</div>4"
                + "</body>\n"
                + "</html>";

        final HtmlPage page = loadPage(html);
        assertEquals("1" + System.lineSeparator() + "2", page.asText());
    }
}
