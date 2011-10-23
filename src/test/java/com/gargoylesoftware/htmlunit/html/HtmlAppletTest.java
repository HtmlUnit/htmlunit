/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlApplet}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlAppletTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object HTMLAppletElement]", "[object HTMLAppletElement]" },
            IE = { "[object]", "[object]" })
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "    alert(document.applets[0]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <applet id='myId'></applet>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlApplet.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_appletDisabled() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "  <applet id='myId'>Your browser doesn't support applets</object>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlApplet appletNode = page.getHtmlElementById("myId");
        assertEquals("Your browser doesn't support applets", appletNode.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_appletEnabled() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "  <applet id='myId'>Your browser doesn't support applets</object>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        webClient.setWebConnection(connection);
        connection.setDefaultResponse(html);
        webClient.setAppletEnabled(true);
        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlApplet appletNode = page.getHtmlElementById("myId");
        assertEquals("", appletNode.asText()); // should we display something else?
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void simpleInstantiation() throws Exception {
        final URL url = getClass().getResource("/applets/emptyApplet.html");

        final HtmlPage page = getWebClient().getPage(url);
        final HtmlApplet appletNode = page.getHtmlElementById("myApp");

        assertEquals("net.sourceforge.htmlunit.testapplets.EmptyApplet", appletNode.getApplet().getClass().getName());
    }

    /**
     * Tests the codebase and documentbase properties.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletBaseWithoutCodebase() throws Exception {
        final URL url = getClass().getResource("/applets/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonShowCodeBase");
        button.click();
        button = page.getHtmlElementById("buttonShowDocumentBase");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).startsWith("CodeBase: 'file:"));
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).endsWith("target/test-classes/applets/'"));

        assertTrue(collectedStatus.get(1), collectedStatus.get(1).startsWith("DocumentBase: 'file:"));
        assertTrue(collectedStatus.get(1),
                collectedStatus.get(1).endsWith("target/test-classes/applets/simpleAppletDoIt.html'"));
    }

    /**
     * Tests the codebase and documentbase properties.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletBase() throws Exception {
        final URL url = getClass().getResource("/applets/codebaseApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonShowCodeBase");
        button.click();
        button = page.getHtmlElementById("buttonShowDocumentBase");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).startsWith("CodeBase: 'file:"));
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).endsWith("target/test-classes/applets/'"));

        assertTrue(collectedStatus.get(1), collectedStatus.get(1).startsWith("DocumentBase: 'file:"));
        assertTrue(collectedStatus.get(1),
                collectedStatus.get(1).endsWith("target/test-classes/applets/codebaseApplet.html'"));
    }

    /**
     * Tests the codebase and documentbase properties.
     * @throws Exception if the test fails
     */
    @Test
    public void checkSubdirAppletBase() throws Exception {
        final URL url = getClass().getResource("/applets/subdir/codebaseApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonShowCodeBase");
        button.click();
        button = page.getHtmlElementById("buttonShowDocumentBase");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).startsWith("CodeBase: 'file:"));
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).endsWith("target/test-classes/applets/'"));

        assertTrue(collectedStatus.get(1), collectedStatus.get(1).startsWith("DocumentBase: 'file:"));
        assertTrue(collectedStatus.get(1),
                collectedStatus.get(1).endsWith("target/test-classes/applets/subdir/codebaseApplet.html'"));
    }

    /**
     * Tests the codebase and documentbase properties.
     * @throws Exception if the test fails
     */
    @Test
    public void checkSubdirRelativeAppletBase() throws Exception {
        final URL url = getClass().getResource("/applets/subdir/archiveRelativeApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonShowCodeBase");
        button.click();
        button = page.getHtmlElementById("buttonShowDocumentBase");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).startsWith("CodeBase: 'file:"));
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).endsWith("target/test-classes/applets/subdir/'"));

        assertTrue(collectedStatus.get(1), collectedStatus.get(1).startsWith("DocumentBase: 'file:"));
        assertTrue(collectedStatus.get(1),
                collectedStatus.get(1).endsWith("target/test-classes/applets/subdir/archiveRelativeApplet.html'"));
    }

    /**
     * Tests the handling of parameters.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletParams() throws Exception {
        final URL url = getClass().getResource("/applets/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonParam1");
        button.click();
        button = page.getHtmlElementById("buttonParam2");
        button.click();
        button = page.getHtmlElementById("buttonParamCodebase");
        button.click();
        button = page.getHtmlElementById("buttonParamArchive");
        button.click();

        assertEquals(4, collectedStatus.size());
        assertEquals("param1: 'value1'", collectedStatus.get(0));
        assertEquals("param2: 'value2'", collectedStatus.get(1));
        assertEquals("codebase: 'null'", collectedStatus.get(2));
        assertEquals("archive: 'simpleAppletDoIt.jar'", collectedStatus.get(3));
    }

    /**
     * Tests the handling of parameters.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletOverwriteArchive() throws Exception {
        final URL url = getClass().getResource("/applets/subdir/codebaseParamApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonParam1");
        button.click();
        button = page.getHtmlElementById("buttonParam2");
        button.click();
        button = page.getHtmlElementById("buttonParamCodebase");
        button.click();
        button = page.getHtmlElementById("buttonParamArchive");
        button.click();

        assertEquals(4, collectedStatus.size());
        assertEquals("param1: 'value1'", collectedStatus.get(0));
        assertEquals("param2: 'value2'", collectedStatus.get(1));
        assertEquals("codebase: '..'", collectedStatus.get(2));
        assertEquals("archive: 'simpleAppletDoIt.jar'", collectedStatus.get(3));
    }

    /**
     * Tests the processing of an applet definition with wrong archive.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletUnknownArchive() throws Exception {
        final URL url = getClass().getResource("/applets/unknownArchiveApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);
        final DomNodeList<HtmlElement> applets = page.getElementsByTagName("applet");
        assertEquals(1, applets.size());

        final HtmlApplet htmlApplet = (HtmlApplet) applets.get(0);
        try {
            htmlApplet.getApplet();
        }
        catch (final Exception e) {
            assertEquals("java.lang.ClassNotFoundException: net.sourceforge.htmlunit.testapplets.EmptyApplet",
                e.getMessage());
        }
    }

    /**
     * Tests the processing of an applet definition with one valid and one wrong archive.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletIgnoreUnknownArchive() throws Exception {
        final URL url = getClass().getResource("/applets/ignoreUnknownArchiveApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);
        final DomNodeList<HtmlElement> applets = page.getElementsByTagName("applet");
        assertEquals(1, applets.size());

        final HtmlApplet htmlApplet = (HtmlApplet) applets.get(0);
        htmlApplet.getApplet();
    }
}
