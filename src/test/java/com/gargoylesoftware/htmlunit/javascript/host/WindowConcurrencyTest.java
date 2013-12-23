/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

/**
 * Tests for {@link Window} that use background jobs.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Daniel Gredler
 * @author Frank Danek
 */
public class WindowConcurrencyTest extends SimpleWebTestCase {

    private WebClient client_;
    private long startTime_;

    private void startTimedTest() {
        startTime_ = System.currentTimeMillis();
    }

    private void assertMaxTestRunTime(final long maxRunTimeMilliseconds) {
        final long endTime = System.currentTimeMillis();
        final long runTime = endTime - startTime_;
        assertTrue("\nTest took too long to run and results may not be accurate. Please try again. "
            + "\n  Actual Run Time: "
            + runTime
            + "\n  Max Run Time: "
            + maxRunTimeMilliseconds, runTime < maxRunTimeMilliseconds);
    }

    /**
     * Sets up the tests.
     */
    @Before
    public void before() {
        client_ = new WebClient();
    }

    /**
     * Tears down the tests.
     */
    @After
    public void after() {
        client_.closeAllWindows();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setTimeout() throws Exception {
        final String content
            = "<html><body><script language='JavaScript'>window.setTimeout('alert(\"Yo!\")',1);\n"
            + "</script></body></html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        loadPage(client_, content, collectedAlerts);
        assertEquals(0, client_.waitForBackgroundJavaScript(1000));
        assertEquals(new String[] {"Yo!"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setTimeoutByReference() throws Exception {
        final String content = "<html><body><script language='JavaScript'>\n"
            + "function doTimeout() {alert('Yo!');}\n"
            + "window.setTimeout(doTimeout,1);\n"
            + "</script></body></html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        loadPage(client_, content, collectedAlerts);
        assertEquals(0, client_.waitForBackgroundJavaScript(1000));
        assertEquals(new String[] {"Yo!"}, collectedAlerts);
    }

    /**
     * Just tests that setting and clearing an interval doesn't throw an exception.
     * @throws Exception if the test fails
     */
    @Test
    public void setAndClearInterval() throws Exception {
        final String content
            = "<html><body>\n"
            + "<script>\n"
            + "window.setInterval('alert(\"Yo!\")', 500);\n"
            + "function foo() { alert('Yo2'); }\n"
            + "var i = window.setInterval(foo, 500);\n"
            + "window.clearInterval(i);\n"
            + "</script></body></html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        loadPage(client_, content, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setIntervalFunctionReference() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setInterval(doAlert, 100);\n"
            + "    }\n"
            + "    var iterationNumber=0;\n"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "      if (++iterationNumber >= 3) {\n"
            + "        clearInterval(threadID);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        loadPage(client_, content, collectedAlerts);
        assertEquals(0, client_.waitForBackgroundJavaScript(1000));
        assertEquals(Collections.nCopies(3, "blah"), collectedAlerts);
    }

    /**
     * When <tt>setInterval()</tt> is called with a 0 millisecond delay, Internet Explorer turns it
     * into a <tt>setTimeout()</tt> call, and Firefox imposes a minimum timer restriction.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void setIntervalZeroDelay() throws Exception {
        final String html
            = "<html><body><div id='d'></div>\n"
            + "<script>var id = setInterval('document.getElementById(\"d\").innerHTML += \"x\"', 0);</script>\n"
            + "</body></html>";

        WebClient client = new WebClient(BrowserVersion.FIREFOX_17);
        try {
            final HtmlPage page1 = loadPage(client, html, new ArrayList<String>());
            Thread.sleep(1000);
            page1.executeJavaScript("clearInterval(id)");
            client.waitForBackgroundJavaScript(1000);
            assertTrue(page1.getElementById("d").asText().length() > 1);
        }
        finally {
            client.closeAllWindows();
        }

        client = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
        try {
            final HtmlPage page2 = loadPage(client, html, new ArrayList<String>());
            client.waitForBackgroundJavaScript(1000);
            assertEquals(1, page2.getElementById("d").asText().length());
        }
        finally {
            client.closeAllWindows();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clearInterval() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "  var count;\n"
            + "  var id;\n"
            + "  function test() {\n"
            + "    count = 0;\n"
            + "    id = setInterval(callback, 100);\n"
            + "  };\n"
            + "  function callback() {\n"
            + "    count++;\n"
            + "    clearInterval(id);\n"
            + "    // Give the callback time to show its ugly face.\n"
            + "    // If it fires between now and then, we'll know.\n"
            + "    setTimeout('alert(count)', 500);\n"
            + "  }\n"
            + "</script></body></html>";
        final String[] expected = {"1"};
        final List<String> actual = Collections.synchronizedList(new ArrayList<String>());
        startTimedTest();
        loadPage(client_, html, actual);
        assertEquals(0, client_.waitForBackgroundJavaScript(10000));
        assertEquals(expected, actual);
        assertMaxTestRunTime(5000);
    }

    /**
     * Test that a script started by a timer is stopped if the page that started it
     * is not loaded anymore.
     * @throws Exception if the test fails
     */
    @Test
    public void setTimeoutStopped() throws Exception {
        final String firstContent
            = "<html><head>\n"
            + "<script language='JavaScript'>window.setTimeout('alert(\"Yo!\")', 10000);</script>\n"
            + "</head><body onload='document.location.replace(\"" + URL_SECOND + "\")'></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client_.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client_.setWebConnection(webConnection);

        final HtmlPage page = client_.getPage(URL_FIRST);
        assertEquals(0, page.getWebClient().waitForBackgroundJavaScript(2000));
        assertEquals("Second", page.getTitleText());
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clearTimeout() throws Exception {
        final String content =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var id = setTimeout('doAlert()', 2000);\n"
            + "      clearTimeout(id);\n"
            + "    }\n"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        loadPage(client_, content, collectedAlerts);
        client_.waitForBackgroundJavaScript(2000);
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * Verifies that calling clearTimeout() on a callback which has already fired
     * does not affect said callback.
     * @throws Exception if the test fails
     */
    @Test
    public void clearTimeout_DoesNotStopExecutingCallback() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "  var id;\n"
            + "  function test() {\n"
            + "    id = setTimeout(callback, 1);\n"
            + "  };\n"
            + "  function callback() {\n"
            + "    alert(id != 0);\n"
            + "    clearTimeout(id);\n"
            + "    // Make sure we weren't stopped.\n"
            + "    alert('completed');\n"
            + "  }\n"
            + "</script><div id='a'></div></body></html>";
        final String[] expected = {"true", "completed"};
        final List<String> actual = Collections.synchronizedList(new ArrayList<String>());
        loadPage(client_, html, actual);
        client_.waitForBackgroundJavaScript(5000);
        assertEquals(expected, actual);
    }

    /**
     * Tests that nested setTimeouts that are deeper than Thread.MAX_PRIORITY
     * do not cause an exception.
     * @throws Exception if the test fails
     */
    @Test
    public void nestedSetTimeoutAboveMaxPriority() throws Exception {
        final int max = Thread.MAX_PRIORITY + 1;
        final String content = "<html><body><script language='JavaScript'>\n"
            + "var depth = 0;\n"
            + "var maxdepth = " + max + ";\n"
            + "function addAnother() {\n"
            + "  if (depth < maxdepth) {\n"
            + "    window.alert('ping');\n"
            + "    depth++;\n"
            + "    window.setTimeout('addAnother();', 1);\n"
            + "  }\n"
            + "}\n"
            + "addAnother();\n"
            + "</script></body></html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        loadPage(client_, content, collectedAlerts);
        assertEquals(0, client_.waitForBackgroundJavaScript((max + 1) * 1000));
        assertEquals(Collections.nCopies(max, "ping"), collectedAlerts);
    }

    /**
     * Regression test for bug #693 with clearInterval.
     * @see <a href="http://sourceforge.net/p/htmlunit/bugs/693/">
     * bug details</a>
     * @throws Exception if the test fails
     */
    @Test
    public void clearInterval_threadInterrupt() throws Exception {
        doTestClearX_threadInterrupt("Interval");
    }

    /**
     * Regression test for bug #2093370 with clearTimeout.
     * @see <a href="http://sourceforge.net/p/htmlunit/bugs/693/">
     * bug details</a>
     * @throws Exception if the test fails
     */
    @Test
    public void clearTimeout_threadInterrupt() throws Exception {
        doTestClearX_threadInterrupt("Timeout");
    }

    private void doTestClearX_threadInterrupt(final String x) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function f() {\n"
            + "    alert('started');\n"
            + "    clear" + x + "(window.timeoutId);\n"
            + "    mySpecialFunction();\n"
            + "    alert('finished');\n"
            + "  }\n"
            + "  function test() {\n"
            + "    window.timeoutId = set" + x + "(f, 10);\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "<span id='clickMe' onclick='test()'>click me</span>"
            + "</body></html>";

        final String[] expectedAlerts = {"started", "finished"};

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(client_, html, collectedAlerts);
        final Function mySpecialFunction = new BaseFunction() {
            @Override
            public Object call(final Context cx, final Scriptable scope,
                    final Scriptable thisObj, final Object[] args) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new RuntimeException("My thread is already interrupted");
                }
                return null;
            }
        };
        final Window window = (Window) page.getEnclosingWindow().getScriptObject();
        ScriptableObject.putProperty(window, "mySpecialFunction", mySpecialFunction);
        page.getHtmlElementById("clickMe").click();
        client_.waitForBackgroundJavaScript(5000);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that when all windows are closed, background JS jobs are stopped (see bug 2127419).
     * @throws Exception if the test fails
     */
    @Test
    public void verifyCloseAllWindowsStopsJavaScript() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function f() {\n"
            + "    alert('Oh no!');\n"
            + "  }\n"
            + "  function test() {\n"
            + "    window.timeoutId = setInterval(f, 1000);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        client_.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client_.setWebConnection(webConnection);

        client_.getPage(URL_FIRST);
        client_.closeAllWindows();
        client_.waitForBackgroundJavaScript(5000);
        assertEquals(0, collectedAlerts.size());
    }

    /**
     * Verifies that when you go to a new page, background JS jobs are stopped (see bug 2127419).
     * @throws Exception if the test fails
     */
    @Test
    public void verifyGoingToNewPageStopsJavaScript() throws Exception {
        final String html1 = "<html><head><title>foo</title><script>\n"
            + "  function f() {\n"
            + "    alert('Oh no!');\n"
            + "  }\n"
            + "  function test() {\n"
            + "    window.timeoutId = setInterval(f, 1000);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String html2 = "<html></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        client_.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(URL_SECOND, html2);
        client_.setWebConnection(conn);

        client_.getPage(URL_FIRST);
        client_.getPage(URL_SECOND);

        client_.waitForBackgroundJavaScript(5000);
        client_.waitForBackgroundJavaScript(5000);

        assertEquals(0, collectedAlerts.size());
    }

    /**
     * Our Window proxy caused troubles.
     * @throws Exception if the test fails
     */
    @Test
    public void setTimeoutOnFrameWindow() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    frames[0].setTimeout(f, 0);\n"
            + "  }\n"
            + "  function f() {\n"
            + "    alert('in f');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<iframe src='about:blank'></iframe>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        client_.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client_.setWebConnection(conn);

        client_.getPage(URL_FIRST);
        assertEquals(0, client_.waitForBackgroundJavaScriptStartingBefore(1000));

        final String[] expectedAlerts = {"in f"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for
     * <a href="http://sourceforge.net/support/tracker.php?aid=2820051>bug 2820051</a>.
     * @throws Exception if the test fails
     */
    @Test
    public void concurrentModificationException_computedStyles() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  getComputedStyle(document.body, null);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<iframe src='foo.html' name='myFrame' id='myFrame'></iframe>\n"
            + "</body></html>";

        final String html2 = "<html><head><script>\n"
            + "function forceStyleComputationInParent() {\n"
            + "  var newNode = parent.document.createElement('span');\n"
            + "  parent.document.body.appendChild(newNode);\n"
            + "  parent.getComputedStyle(newNode, null);\n"
            + "}\n"
            + "setInterval(forceStyleComputationInParent, 10);\n"
            + "</script></head></body></html>";

        client_ = new WebClient(BrowserVersion.FIREFOX_17);
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse(html2);
        client_.setWebConnection(webConnection);

        final HtmlPage page1 = client_.getPage(URL_FIRST);

        // Recreating what can occur with two threads requires
        // to know a bit about the style invalidation used in Window.DomHtmlAttributeChangeListenerImpl
        final HtmlElement elt = new HtmlDivision("div", page1, new HashMap<String, DomAttr>()) {
            @Override
            public DomNode getParentNode() {
                // this gets called by CSS invalidation logic
                try {
                    Thread.sleep(1000); // enough to let setInterval run
                }
                catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return super.getParentNode();
            }
        };
        page1.getBody().appendChild(elt);
    }

    /**
     * As of HtmlUnit-2,11 snapshot from 20.08.2012, following exception was occurring in background processing:
     * >net.sourceforge.htmlunit.corejs.javascript.EcmaError: ReferenceError: "foo" is not defined.<.
     * The cause was that the setTimeout was executed even once the page was unloaded. Strangely, loading
     * directly the second page didn't allow to reproduce the problem.
     * @throws Exception if the test fails
     */
    @Test
    public void cleanSetTimeout() throws Exception {
        final String page1 = "<html><body>"
            + "<a id=it href='" + URL_SECOND.toString() + "'>link</a>\n"
            + "</body></html>";

        final String html = "<html><body>\n"
            + "<form action='foo2' name='waitform'></form>"
            + "<script>"
            + "function foo() {\n"
            + "  setTimeout('foo()', 10);\n"
            + "}\n"
            + "function t() {\n"
            + "  setTimeout('foo()', 10);\n"
            + "  document.waitform.submit();\n"
            + "}\n"
            + "t();\n"
            + "</script>\n"
            + "</body></html>";

        final MockWebConnection mockWebConnection = new MockWebConnection() {
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
                try {
                    Thread.sleep(50); // causes the background job to be scheduled in this time
                }
                catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return super.getResponse(request);
            }
        };
        mockWebConnection.setDefaultResponse("<html></html>");

        mockWebConnection.setResponse(URL_FIRST, page1);
        mockWebConnection.setResponse(URL_SECOND, html);

        client_.setWebConnection(mockWebConnection);
        final List<ScriptException> scriptExceptions = new ArrayList<ScriptException>();
        client_.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            public void loadScriptError(final HtmlPage htmlPage, final URL scriptUrl, final Exception exception) {
            }

            public void malformedScriptURL(final HtmlPage htmlPage, final String url,
                    final MalformedURLException malformedURLException) {
            }

            public void scriptException(final HtmlPage htmlPage, final ScriptException scriptException) {
                scriptExceptions.add(scriptException);
            }

            public void timeoutError(final HtmlPage htmlPage, final long allowedTime, final long executionTime) {
            }
        });

        final HtmlPage page = client_.getPage(URL_FIRST);
        ((HtmlElement) page.getElementById("it")).click();
        Thread.sleep(500);
        assertThat(scriptExceptions, is(Collections.<ScriptException>emptyList()));
    }
}
