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

import java.awt.GraphicsEnvironment;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.AppletConfirmHandler;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObjectTest;

/**
 * Tests for {@link HtmlObject}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlObjectTest extends SimpleWebTestCase {

    private static boolean SKIP_ = false;

    static {
        if (GraphicsEnvironment.isHeadless()) {
            // skip the tests in headless mode
            SKIP_ = true;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "false")
    public void classid() throws Exception {
        if (getBrowserVersion().isIE() && !ActiveXObjectTest.isJacobInstalled()) {
            return;
        }
        final String html = "<html><head>\n"
            // Window Media Player CLASSID
            + "<object id='wm' classid='clsid:6BF52A52-394A-11D3-B153-00C04F79FAA6'></object>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.all.wm.fullScreen);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        client.getOptions().setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void simpleInstantiation() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/emptyApplet.html");

        final HtmlPage page = getWebClient().getPage(url);
        final HtmlObject objectNode = page.getHtmlElementById("myApp");

        assertEquals("net.sourceforge.htmlunit.testapplets.EmptyApplet", objectNode.getApplet().getClass().getName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cacheArchive() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/cacheArchiveApplet.html");

        final HtmlPage page = getWebClient().getPage(url);
        final HtmlObject objectNode = page.getHtmlElementById("myApp");

        assertEquals("net.sourceforge.htmlunit.testapplets.EmptyApplet", objectNode.getApplet().getClass().getName());
    }

    /**
     * Tests the codebase and documentbase properties.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletBaseWithoutCodebase() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonShowCodeBase");
        button.click();
        button = page.getHtmlElementById("buttonShowDocumentBase");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).startsWith("CodeBase: 'file:"));
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).endsWith("target/test-classes/objects/'"));

        assertTrue(collectedStatus.get(1), collectedStatus.get(1).startsWith("DocumentBase: 'file:"));
        assertTrue(collectedStatus.get(1),
                collectedStatus.get(1).endsWith("target/test-classes/objects/simpleAppletDoIt.html'"));
    }

    /**
     * Tests the codebase and documentbase properties.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletBase() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/codebaseApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonShowCodeBase");
        button.click();
        button = page.getHtmlElementById("buttonShowDocumentBase");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).startsWith("CodeBase: 'file:"));
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).endsWith("target/test-classes/objects/'"));

        assertTrue(collectedStatus.get(1), collectedStatus.get(1).startsWith("DocumentBase: 'file:"));
        assertTrue(collectedStatus.get(1),
                collectedStatus.get(1).endsWith("target/test-classes/objects/codebaseApplet.html'"));
    }

    /**
     * Tests the codebase and documentbase properties.
     * @throws Exception if the test fails
     */
    @Test
    public void checkSubdirAppletBase() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/subdir/codebaseApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonShowCodeBase");
        button.click();
        button = page.getHtmlElementById("buttonShowDocumentBase");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).startsWith("CodeBase: 'file:"));
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).endsWith("target/test-classes/objects/'"));

        assertTrue(collectedStatus.get(1), collectedStatus.get(1).startsWith("DocumentBase: 'file:"));
        assertTrue(collectedStatus.get(1),
                collectedStatus.get(1).endsWith("target/test-classes/objects/subdir/codebaseApplet.html'"));
    }

    /**
     * Tests the codebase and documentbase properties.
     * @throws Exception if the test fails
     */
    @Test
    public void checkSubdirRelativeAppletBase() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/subdir/archiveRelativeApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        HtmlButton button = page.getHtmlElementById("buttonShowCodeBase");
        button.click();
        button = page.getHtmlElementById("buttonShowDocumentBase");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).startsWith("CodeBase: 'file:"));
        assertTrue(collectedStatus.get(0), collectedStatus.get(0).endsWith("target/test-classes/objects/subdir/'"));

        assertTrue(collectedStatus.get(1), collectedStatus.get(1).startsWith("DocumentBase: 'file:"));
        assertTrue(collectedStatus.get(1),
                collectedStatus.get(1).endsWith("target/test-classes/objects/subdir/archiveRelativeApplet.html'"));
    }

    /**
     * Tests the handling of parameters.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletParams() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

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
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletExecJs() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        final HtmlButton button = page.getHtmlElementById("execJs7");
        button.click();

        assertEquals(2, collectedStatus.size());
        assertEquals("execJS: '7'", collectedStatus.get(0));
        assertEquals("  '7.0'", collectedStatus.get(1));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setMember() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        final HtmlButton button = page.getHtmlElementById("setValueAttribute");
        button.click();

        assertEquals(1, collectedStatus.size());
        assertEquals("value set for 'myInput' to 'HtmlUnit'", collectedStatus.get(0));

        final String value = page.getElementById("myInput").getAttribute("value");
        assertEquals("HtmlUnit", value);
    }

    /**
     * Tests the handling of parameters.
     * @throws Exception if the test fails
     */
    @Test
    public void checkAppletOverwriteArchive() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/subdir/codebaseParamApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

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
        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/unknownArchiveApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);
        final DomNodeList<DomElement> objects = page.getElementsByTagName("object");
        assertEquals(1, objects.size());

        final HtmlObject htmlObject = (HtmlObject) objects.get(0);
        try {
            htmlObject.getApplet();
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
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/ignoreUnknownArchiveApplet.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);
        final DomNodeList<DomElement> objects = page.getElementsByTagName("object");
        assertEquals(1, objects.size());

        final HtmlObject htmlObject = (HtmlObject) objects.get(0);
        htmlObject.getApplet();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appletConfirmHandler() throws Exception {
        Assume.assumeFalse(SKIP_);

        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        webClient.setAppletConfirmHandler(new AppletConfirmHandler() {
            @Override
            public boolean confirm(final HtmlApplet applet) {
                return false;
            }

            @Override
            public boolean confirm(final HtmlObject applet) {
                assertEquals("simpleAppletDoIt.jar", applet.getArchiveAttribute());
                return true;
            }
        });

        final HtmlPage page = webClient.getPage(url);
        final DomNodeList<DomElement> objects = page.getElementsByTagName("object");
        assertEquals(1, objects.size());

        final HtmlObject htmlObject = (HtmlObject) objects.get(0);
        assertTrue(htmlObject.getApplet() != null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appletConfirmHandlerPermit() throws Exception {
        if (getBrowserVersion().isChrome()) {
            return;
        }

        final URL url = getClass().getResource("/objects/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<>();
        final StatusHandler statusHandler = new StatusHandler() {
            @Override
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        webClient.setAppletConfirmHandler(new AppletConfirmHandler() {
            @Override
            public boolean confirm(final HtmlApplet applet) {
                return true;
            }

            @Override
            public boolean confirm(final HtmlObject applet) {
                assertEquals("simpleAppletDoIt.jar", applet.getArchiveAttribute());
                return false;
            }
        });

        final HtmlPage page = webClient.getPage(url);
        final DomNodeList<DomElement> objects = page.getElementsByTagName("object");
        assertEquals(1, objects.size());

        final HtmlObject htmlObject = (HtmlObject) objects.get(0);
        assertTrue(htmlObject.getApplet() == null);
    }
}
