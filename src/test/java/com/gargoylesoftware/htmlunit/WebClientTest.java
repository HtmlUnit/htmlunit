/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.base.testing.EventCatcher;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests for {@link WebClient}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Chris Erskine
 * @author Hans Donner
 * @author Paul King
 * @author Ahmed Ashour
 */
public class WebClientTest extends WebTestCase {

    /**
     * Create an instance
     *
     * @param name The name of the test
     */
    public WebClientTest(final String name) {
        super(name);
    }

    /**
     * Test the situation where credentials are required but they haven't been specified.
     *
     * @throws Exception If something goes wrong.
     */
    public void testCredentialProvider_NoCredentials() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "No access</body></html>";
        final WebClient client = new WebClient();
        client.setPrintContentOnFailingStatusCode(false);

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(
            htmlContent, 401, "Credentials missing or just plain wrong", "text/plain");
        client.setWebConnection(webConnection);

        try {
            client.getPage(new WebRequestSettings(URL_GARGOYLE, SubmitMethod.POST));
            fail("Expected FailingHttpStatusCodeException");
        }
        catch (final FailingHttpStatusCodeException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    /**
     * Test that {@link WebClient#assertionFailed(String)} actually throws an exception.
     * @deprecated
     */
    public void testAssertionFailed() {
        final WebClient client = new WebClient();

        try {
            client.assertionFailed("foobar");
            fail("Expected AssertionFailedError");
        }
        catch (final junit.framework.AssertionFailedError e) {
            assertEquals("foobar", e.getMessage());
        }
    }

    /**
     * Test that the {@link WebWindowEvent#CHANGE} window event gets fired at the
     * appropriate time.
     * @throws Exception If something goes wrong.
     */
    public void testHtmlWindowEvents_changed() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final WebClient client = new WebClient();
        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(client);

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_GARGOYLE);
        final HtmlAnchor anchor = (HtmlAnchor) firstPage.getHtmlElementById("a2");

        final List<WebWindowEvent> firstExpectedEvents = Arrays.asList(new WebWindowEvent[] {
            new WebWindowEvent(
                client.getCurrentWindow(), WebWindowEvent.CHANGE, null, firstPage),
        });
        assertEquals(firstExpectedEvents, eventCatcher.getEvents());

        eventCatcher.clear();
        final HtmlPage secondPage = (HtmlPage) anchor.click();

        final List<WebWindowEvent> secondExpectedEvents = Arrays.asList(new WebWindowEvent[] {
            new WebWindowEvent(
                client.getCurrentWindow(), WebWindowEvent.CHANGE, firstPage, secondPage),
        });
        assertEquals(secondExpectedEvents, eventCatcher.getEvents());
    }

    /**
     * Test that the {@link WebWindowEvent#OPEN} window event gets fired at
     * the appropriate time.
     * @throws Exception If something goes wrong.
     */
    public void testHtmlWindowEvents_opened() throws Exception {
        final String page1Content
            = "<html><head><title>foo</title>\n"
            + "<script>window.open('" + URL_SECOND + "', 'myNewWindow')</script>\n"
            + "</head><body>\n"
            + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final String page2Content
            = "<html><head><title>foo</title></head><body></body></html>";
        final WebClient client = new WebClient();
        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(client);

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, page1Content);
        webConnection.setResponse(URL_SECOND, page2Content);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("foo", firstPage.getTitleText());

        final WebWindow firstWindow = client.getCurrentWindow();
        final WebWindow secondWindow = client.getWebWindowByName("myNewWindow");
        final List<WebWindowEvent> expectedEvents = Arrays.asList(new WebWindowEvent[] {
            new WebWindowEvent(
                secondWindow, WebWindowEvent.OPEN, null, null),
            new WebWindowEvent(
                secondWindow, WebWindowEvent.CHANGE, null, secondWindow.getEnclosedPage()),
            new WebWindowEvent(
                firstWindow, WebWindowEvent.CHANGE, null, firstPage),
        });
        assertEquals(expectedEvents, eventCatcher.getEvents());
    }

    /**
     * Test that the {@link WebWindowEvent#CLOSE} window event gets fired at
     * the appropriate time.
     * @throws Exception If something goes wrong.
     */
    public void testHtmlWindowEvents_closedFromFrame() throws Exception {
        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<iframe src='" + URL_THIRD + "' id='frame1'>\n"
            + "<a href='" + URL_SECOND + "' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent
            = "<html><head><title>third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("first", firstPage.getTitleText());

        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(client);

        final HtmlInlineFrame frame = (HtmlInlineFrame) firstPage.getHtmlElementById("frame1");
        final HtmlPage thirdPage = (HtmlPage) frame.getEnclosedPage();

        // Load the second page
        final HtmlAnchor anchor = (HtmlAnchor) firstPage.getHtmlElementById("a2");
        final HtmlPage secondPage = (HtmlPage) anchor.click();
        assertEquals("second", secondPage.getTitleText());

        final WebWindow firstWindow = client.getCurrentWindow();
        final List<WebWindowEvent> expectedEvents = Arrays.asList(new WebWindowEvent[] {
            new WebWindowEvent(
                frame.getEnclosedWindow(), WebWindowEvent.CLOSE, thirdPage, null),
            new WebWindowEvent(
                firstWindow, WebWindowEvent.CHANGE, firstPage, secondPage),
        });
        assertEquals(expectedEvents.get(0), eventCatcher.getEvents().get(0));
        assertEquals(expectedEvents, eventCatcher.getEvents());
    }

    /**
     * Test a 301 redirection code where the original request was a GET.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection301_MovedPermanently_GetMethod() throws Exception {
        final int statusCode = 301;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Common utility for GET after POST redirection on same urls
     * @param statusCode The code to return from the initial request
     * @throws Exception if the test fails.
     */
    private void doTestRedirectionSameUrlAfterPost(final int statusCode)
        throws
             Exception {

        final String firstContent = "<html><head><title>First</title></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final List<KeyValuePair> headers =
            Collections.singletonList(new KeyValuePair("Location", URL_FIRST.toExternalForm()));
        
        // builds a webconnection that first sends a redirect and then a "normal" response for
        // the same requested url
        final MockWebConnection webConnection = new MockWebConnection(webClient) {
            private int count_ = 0;
            public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
                ++count_;
                if (count_ == 1) {
                    final WebResponse response = super.getResponse(webRequestSettings);
                    setResponse(webRequestSettings.getURL(), secondContent);
                    return response;
                }
                else {
                    return super.getResponse(webRequestSettings);
                }
            }
        };
        webConnection.setResponse(
                URL_FIRST, firstContent, statusCode,
                "Some error", "text/html", headers);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(new WebRequestSettings(URL_FIRST, SubmitMethod.POST));
        final WebResponse webResponse = page.getWebResponse();
        // A redirect should have happened
        assertEquals(200, webResponse.getStatusCode());
        assertEquals(URL_FIRST, webResponse.getUrl());
        assertEquals("Second", page.getTitleText());
        assertEquals(SubmitMethod.GET, webResponse.getRequestMethod());
    }

    /**
     * From the http spec:  If the 301 status code is received in response
     * to a request other than GET or HEAD, the user agent MUST NOT automatically
     * redirect the request unless it can be confirmed by the user, since this
     * might change the conditions under which the request was issued.
     * BUT Firefox follows the redirection
     * @throws Exception If something goes wrong.
     */
    public void testRedirection301_MovedPermanently_PostMethod() throws Exception {
        final int statusCode = 301;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
        doTestRedirectionSameUrlAfterPost(statusCode);
    }

    /**
     * From the http spec:  Note: RFC 1945 and RFC 2068 specify that the client
     * is not allowed to change the method on the redirected request.  However,
     * most existing user agent implementations treat 302 as if it were a 303
     * response, performing a GET on the Location field-value regardless
     * of the original request method. The status codes 303 and 307 have
     * been added for servers that wish to make unambiguously clear which
     * kind of reaction is expected of the client.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection302_MovedTemporarily_PostMethod() throws Exception {
        final int statusCode = 302;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
        doTestRedirectionSameUrlAfterPost(statusCode);
    }

    /**
     * Test a 302 redirection code.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection302_MovedTemporarily_GetMethod() throws Exception {
        final int statusCode = 302;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Test a 302 redirection code with "," in url parameters.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection302_MovedTemporarily_CommaInParameters() throws Exception {
        doTestRedirection(302, SubmitMethod.GET, SubmitMethod.GET, URL_SECOND + "/foo.html?foo1=abc&foo2=1,2,3,4");
    }

    /**
     * Tests a 303 redirection code.  This should be the same as a 302.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection303_SeeOther_GetMethod() throws Exception {
        final int statusCode = 303;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Tests a 303 redirection code - this should be the same as a 302.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection303_SeeOther_PostMethod() throws Exception {
        final int statusCode = 303;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
        doTestRedirectionSameUrlAfterPost(statusCode);
    }

    /**
     * Tests a 307 redirection code.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection307_TemporaryRedirect_GetMethod() throws Exception {
        final int statusCode = 307;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Tests a 307 redirection code.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection307_TemporaryRedirect_PostMethod() throws Exception {
        final int statusCode = 307;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = null;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Basic logic for all the redirection tests.
     *
     * @param statusCode The code to return from the initial request
     * @param initialRequestMethod The initial request.
     * @param expectedRedirectedRequestMethod The submit method of the second (redirected) request.
     * If a redirect is not expected to happen then this must be null
     * @throws Exception if the test fails.
     */
    private void doTestRedirection(
            final int statusCode,
            final SubmitMethod initialRequestMethod,
            final SubmitMethod expectedRedirectedRequestMethod)
        throws Exception {

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod,
                URL_SECOND.toExternalForm());
    }

    /**
     * Basic logic for all the redirection tests.
     *
     * @param statusCode The code to return from the initial request
     * @param initialRequestMethod The initial request.
     * @param expectedRedirectedRequestMethod The submit method of the second (redirected) request.
     * If a redirect is not expected to happen then this must be null
     * @param newLocation the Location set in the redirection header
     * @throws Exception if the test fails.
     */
    private void doTestRedirection(
            final int statusCode,
            final SubmitMethod initialRequestMethod,
            final SubmitMethod expectedRedirectedRequestMethod,
            final String newLocation)
        throws
        Exception {

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod, newLocation, false);
        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod, newLocation, true);
    }

    /**
     * Browsers allow many redirections to the same url before to stop redirections.
     * See Bug 1619765 and feature request 1472343.
     * @throws Exception if the test fails.
     */
    public void testRedirectionSameURL() throws Exception {
        final HtmlPage page1 = getPageWithRedirectionsSameURL(1);
        assertEquals("Second", page1.getTitleText());

        final HtmlPage page2 = getPageWithRedirectionsSameURL(30);
        assertEquals("Redirect needed 21", page2.getWebResponse().getStatusMessage());
    }
    
    private HtmlPage getPageWithRedirectionsSameURL(final int nbRedirections) throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final URL url = URL_FIRST;
        final List<KeyValuePair> headers =
            Collections.singletonList(new KeyValuePair("Location", URL_FIRST.toExternalForm()));
        final MockWebConnection webConnection = new MockWebConnection(webClient) {
            private int count_ = 0;
            public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
                ++count_;
                if (count_ < nbRedirections) {
                    setResponse(url, firstContent, 302, "Redirect needed " + count_, "text/html", headers);
                    return super.getResponse(webRequestSettings);
                }
                else if (count_ == nbRedirections) {
                    final WebResponse response = super.getResponse(webRequestSettings);
                    setResponse(webRequestSettings.getURL(), secondContent);
                    return response;
                }
                else {
                    return super.getResponse(webRequestSettings);
                }
            }
        };
        webConnection.setResponse(url, firstContent, 302, "Redirect needed", "text/html", headers);
        webClient.setWebConnection(webConnection);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        
        return (HtmlPage) webClient.getPage(url);
    }

    /**
     * Basic logic for all the redirection tests.
     *
     * @param statusCode The code to return from the initial request
     * @param initialRequestMethod The initial request.
     * @param expectedRedirectedRequestMethod The submit method of the second (redirected) request.
     * If a redirect is not expected to happen then this must be null
     * @param newLocation the Location set in the redirection header
     * @param useProxy indicates if the test should be performed with a proxy
     * @throws Exception if the test fails.
     */
    private void doTestRedirection(
            final int statusCode,
            final SubmitMethod initialRequestMethod,
            final SubmitMethod expectedRedirectedRequestMethod,
            final String newLocation,
            final boolean useProxy)
        throws
             Exception {

        final String firstContent = "<html><head><title>First</title></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient;
        final String proxyHost;
        final int proxyPort;
        if (useProxy) {
            proxyHost = "someHost";
            proxyPort = 12233345;
            webClient = new WebClient(BrowserVersion.getDefault(), proxyHost, proxyPort);
        }
        else {
            proxyHost = null;
            proxyPort = 0;
            webClient = new WebClient();
        }

        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setPrintContentOnFailingStatusCode(false);

        final List<KeyValuePair> headers = Collections.singletonList(new KeyValuePair("Location", newLocation));
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(
            URL_FIRST, firstContent, statusCode,
            "Some error", "text/html", headers);
        webConnection.setResponse(new URL(newLocation), secondContent);

        webClient.setWebConnection(webConnection);

        final URL url = URL_FIRST;

        HtmlPage page;
        WebResponse webResponse;

        //
        // Second time redirection is turned on (default setting)
        //
        page = (HtmlPage) webClient.getPage(new WebRequestSettings(url, initialRequestMethod));
        webResponse = page.getWebResponse();
        if (expectedRedirectedRequestMethod == null) {
            // No redirect should have happened
            assertEquals(statusCode, webResponse.getStatusCode());
            assertEquals(initialRequestMethod, webConnection.getLastMethod());
        }
        else {
            // A redirect should have happened
            assertEquals(HttpStatus.SC_OK, webResponse.getStatusCode());
            assertEquals(newLocation, webResponse.getUrl());
            assertEquals("Second", page.getTitleText());
            assertEquals(expectedRedirectedRequestMethod, webConnection.getLastMethod());
        }
        assertEquals(proxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(proxyPort, webConnection.getLastWebRequestSettings().getProxyPort());

        //
        // Second time redirection is turned off
        //
        webClient.setRedirectEnabled(false);
        page = (HtmlPage) webClient.getPage(new WebRequestSettings(url, initialRequestMethod));
        webResponse = page.getWebResponse();
        assertEquals(statusCode, webResponse.getStatusCode());
        assertEquals(initialRequestMethod, webConnection.getLastMethod());
        assertEquals(proxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(proxyPort, webConnection.getLastWebRequestSettings().getProxyPort());
    }

    /**
     * Test passing in a null page creator.
     */
    public void testSetPageCreator_null() {
        final WebClient webClient = new WebClient();
        try {
            webClient.setPageCreator(null);
            fail("Expected NullPointerException");
        }
        catch (final NullPointerException e) {
            // expected path
        }
    }

    /**
     * Test {@link WebClient#setPageCreator(PageCreator)}.
     * @throws Exception If something goes wrong.
     */
    public void testSetPageCreator() throws Exception {
        final String page1Content
            = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, page1Content);

        client.setWebConnection(webConnection);
        final List<Page> collectedPageCreationItems = new ArrayList<Page>();
        client.setPageCreator(new CollectingPageCreator(collectedPageCreationItems));

        final Page page = client.getPage(URL_FIRST);
        assertTrue("instanceof TextPage", page instanceof TextPage);

        final List<Page> expectedPageCreationItems = Arrays.asList(new Page[] {
            page
        });

        assertEquals(expectedPageCreationItems, collectedPageCreationItems);
    }

    /** A PageCreator that collects data */
    private class CollectingPageCreator implements PageCreator {
        private final List<Page> collectedPages_;
        /**
         * Create an instance
         * @param list The list that will contain the data
         */
        public CollectingPageCreator(final List<Page> list) {
            collectedPages_ = list;
        }
        
        /**
         * Create a page
         * @param webResponse The web response
         * @param webWindow The web window
         * @return The new page
         * @throws IOException If an IO problem occurs
         */
        public Page createPage(final WebResponse webResponse, final WebWindow webWindow)
            throws IOException {
            final Page page = new TextPage(webResponse, webWindow);
            webWindow.setEnclosedPage(page);
            collectedPages_.add(page);
            return page;
        }
    }

    /**
     * Test loading a page with POST parameters.
     * @throws Exception If something goes wrong.
     */
    public void testLoadPage_PostWithParameters() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "</body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final String urlString = "http://first?a=b";
        final URL url = new URL(urlString);
        final HtmlPage page = (HtmlPage) client.getPage(new WebRequestSettings(url, SubmitMethod.POST));

        assertEquals(urlString, page.getWebResponse().getUrl());
    }

    /**
     * Test that double / in query string are not changed.
     * @throws Exception If something goes wrong.
     */
    public void testLoadPage_SlashesInQueryString() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<body><a href='foo.html?id=UYIUYTY//YTYUY..F'>to page 2</a>\n"
            + "</body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlAnchor link = (HtmlAnchor) page.getAnchors().get(0);
        final Page page2 = link.click();
        assertEquals("http://first/foo.html?id=UYIUYTY//YTYUY..F", page2.getWebResponse().getUrl());
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception If something goes wrong.
     */
    public void testLoadPage_EncodeRequest() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "</body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        // with query string not encoded
        HtmlPage page = (HtmlPage) client.getPage("http://first?a=b c&d=" + ((char) 0xE9) + ((char) 0xE8));
        assertEquals("http://first?a=b%20c&d=%C3%A9%C3%A8", page.getWebResponse().getUrl());

        // with query string already encoded
        page = (HtmlPage) client.getPage("http://first?a=b%20c&d=%C3%A9%C3%A8");
        assertEquals("http://first?a=b%20c&d=%C3%A9%C3%A8", page.getWebResponse().getUrl());

        // with query string partially encoded
        page = (HtmlPage) client.getPage("http://first?a=b%20c&d=e f");
        assertEquals("http://first?a=b%20c&d=e%20f", page.getWebResponse().getUrl());

        // with anchor
        page = (HtmlPage) client.getPage("http://first?a=b c#myAnchor");
        assertEquals("http://first?a=b%20c#myAnchor", page.getWebResponse().getUrl());

        // with query string containing encoded "&", "=", "+", ",", and "$"
        page = (HtmlPage) client.getPage("http://first?a=%26%3D%20%2C%24");
        assertEquals("http://first?a=%26%3D%20%2C%24", page.getWebResponse().getUrl());

        // with character to encode in path
        page = (HtmlPage) client.getPage("http://first/page 1.html");
        assertEquals("http://first/page%201.html", page.getWebResponse().getUrl());

        // with character to encode in path
        page = (HtmlPage) client.getPage("http://first/page 1.html");
        assertEquals("http://first/page%201.html", page.getWebResponse().getUrl());
    }

    /**
     * Test loading a file page.
     *
     * @throws Exception If something goes wrong.
     */
    public void testLoadFilePage() throws Exception {

        // Create a real file to read.
        // It could be useful to have existing files to test in a special location in filesystem.
        // It will be really needed when we have to test binary files using the file protocol.

        final String htmlContent = "<html><head><title>foo</title></head><body></body></html>";
        final File currentDirectory = new File((new File("")).getAbsolutePath());
        final File tmpFile = File.createTempFile("test", ".html", currentDirectory);
        tmpFile.deleteOnExit();
        final String encoding = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();
        FileUtils.writeStringToFile(tmpFile, htmlContent, encoding);

        // Test a normal file URL.

        final WebClient client = new WebClient();
        final URL url = new URL("file://" + tmpFile.getCanonicalPath());
        final HtmlPage page = (HtmlPage) client.getPage(url);

        assertEquals(htmlContent, page.getWebResponse().getContentAsString());
        assertEquals("text/html", page.getWebResponse().getContentType());
        assertEquals(200, page.getWebResponse().getStatusCode());
        assertEquals("foo", page.getTitleText());

        // Test a file URL with a query portion (needs to work for Dojo, for example).

        final URL url2 = new URL(url.toExternalForm() + "?with=query");
        final HtmlPage page2 = (HtmlPage) client.getPage(url2);

        assertEquals(htmlContent, page2.getWebResponse().getContentAsString());
        assertEquals("text/html", page2.getWebResponse().getContentType());
        assertEquals(200, page2.getWebResponse().getStatusCode());
        assertEquals("foo", page2.getTitleText());

        // Test a file URL with a ref portion (needs to work for Dojo, for example).

        final URL url3 = new URL(url.toExternalForm() + "#reference");
        final HtmlPage page3 = (HtmlPage) client.getPage(url3);

        assertEquals(htmlContent, page3.getWebResponse().getContentAsString());
        assertEquals("text/html", page3.getWebResponse().getContentType());
        assertEquals(200, page3.getWebResponse().getStatusCode());
        assertEquals("foo", page3.getTitleText());
    }

    /**
     * Test loading a file page with xml content. Regression test for bug 1113487.
     *
     * @throws Exception If something goes wrong.
     */
    public void testLoadFilePageXml() throws Exception {
        final String xmlContent = "<?xml version='1.0' encoding='UTF-8'?>\n"
            + "<dataset>\n"
            + "<table name=\"USER\">\n"
            + "<column>ID</column>\n"
            + "<row>\n"
            + "<value>116517</value>\n"
            + "</row>\n"
            + "</table>\n"
            + "</dataset>";
        final File currentDirectory = new File((new File("")).getAbsolutePath());
        final File tmpFile = File.createTempFile("test", ".xml", currentDirectory);
        tmpFile.deleteOnExit();
        final String encoding = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();
        FileUtils.writeStringToFile(tmpFile, xmlContent, encoding);

        final URL fileURL = new URL("file://" + tmpFile.getCanonicalPath());

        final WebClient client = new WebClient();
        final XmlPage page = (XmlPage) client.getPage(fileURL);

        assertEquals(xmlContent, page.getWebResponse().getContentAsString());
        // "text/xml" or "application/xml", it doesn't matter
        assertEquals("/xml", StringUtils.substring(page.getWebResponse().getContentType(), -4));
        assertEquals(200, page.getWebResponse().getStatusCode());
    }

    /**
     * Test redirecting with javascript during page load.
     * @throws Exception If something goes wrong.
     */
    public void testRedirectViaJavaScriptDuringInitialPageLoad() throws Exception {
        final String firstContent = "<html><head><title>First</title><script>\n"
            + "location.href='" + URL_SECOND + "'\n"
            + "</script></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        webClient.setWebConnection(webConnection);

        final URL url = URL_FIRST;

        final HtmlPage page = (HtmlPage) webClient.getPage(url);
        assertEquals("Second", page.getTitleText());
    }

    /**
     * Test tabbing where there are no tabbable elements.
     * @throws Exception If something goes wrong.
     */
    public void testKeyboard_NoTabbableElements() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = getPageForKeyboardTest(webClient, new String[0]);
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        assertNull("original", page.getElementWithFocus());
        assertNull("next", page.tabToNextElement());
        assertNull("previous", page.tabToPreviousElement());
        assertNull("accesskey", page.pressAccessKey('a'));

        final List< ? > expectedAlerts = Collections.EMPTY_LIST;
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test tabbing where there is only one tabbable element.
     * @throws Exception If something goes wrong.
     */
    public void testKeyboard_OneTabbableElement() throws Exception {
        final WebClient webClient = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{null});
        final HtmlElement element = page.getHtmlElementById("submit0");

        assertNull("original", page.getElementWithFocus());
        assertNull("accesskey", page.pressAccessKey('x'));

        assertEquals("next", element, page.tabToNextElement());
        assertEquals("nextAgain", element, page.tabToNextElement());

        page.getElementWithFocus().blur();
        assertNull("original", page.getElementWithFocus());

        assertEquals("previous", element, page.tabToPreviousElement());
        assertEquals("previousAgain", element, page.tabToPreviousElement());

        assertEquals("accesskey", element, page.pressAccessKey('z'));

        final String[] expectedAlerts = {"focus-0", "blur-0", "focus-0"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test pressing an accesskey.
     * @throws Exception If something goes wrong.
     */
    public void testAccessKeys() throws Exception {
        final WebClient webClient = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{"1", "2", "3"});

        assertEquals("submit0", page.pressAccessKey('a').getAttributeValue("name"));
        assertEquals("submit2", page.pressAccessKey('c').getAttributeValue("name"));
        assertEquals("submit1", page.pressAccessKey('b').getAttributeValue("name"));

        final String[] expectedAlerts = {
            "focus-0", "blur-0", "focus-2", "blur-2", "focus-1"
        };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test tabbing to the next element.
     * @throws Exception If something goes wrong.
     */
    public void testTabNext() throws Exception {
        final WebClient webClient = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{"1", "2", "3"});

        assertEquals("submit0", page.tabToNextElement().getAttributeValue("name"));
        assertEquals("submit1", page.tabToNextElement().getAttributeValue("name"));
        assertEquals("submit2", page.tabToNextElement().getAttributeValue("name"));

        final String[] expectedAlerts = {
            "focus-0", "blur-0", "focus-1", "blur-1", "focus-2"
        };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test tabbing to the previous element.
     * @throws Exception If something goes wrong.
     */
    public void testTabPrevious() throws Exception {
        final WebClient webClient = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{"1", "2", "3"});

        assertEquals("submit2", page.tabToPreviousElement().getAttributeValue("name"));
        assertEquals("submit1", page.tabToPreviousElement().getAttributeValue("name"));
        assertEquals("submit0", page.tabToPreviousElement().getAttributeValue("name"));

        final String[] expectedAlerts = {
            "focus-2", "blur-2", "focus-1", "blur-1", "focus-0"
        };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that a button can be selected via accesskey.
     * @throws Exception If something goes wrong.
     */
    public void testPressAccessKey_Button() throws Exception {
        final WebClient webClient = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{"1", "2", "3"});
        final HtmlElement button = page.getHtmlElementById("button1");

        final String[] expectedAlerts = {"buttonPushed"};
        collectedAlerts.clear();

        button.removeAttribute("disabled");
        page.pressAccessKey('1');

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Return a loaded page for one of the keyboard tests.
     * @param webClient the WebClient to load the page from.
     * @param tabIndexValues The tab index values.  One input will be created for each item
     * in this list.
     * @return The loaded page.
     * @throws Exception If something goes wrong.
     */
    private HtmlPage getPageForKeyboardTest(
        final WebClient webClient, final String[] tabIndexValues) throws Exception {

        final StringBuffer buffer = new StringBuffer();
        buffer.append(
            "<html><head><title>First</title></head><body><form name='form1' method='post' onsubmit='return false;'>");

        for (int i = 0; i < tabIndexValues.length; i++) {
            buffer.append("<input type='submit' name='submit");
            buffer.append(i);
            buffer.append("' id='submit");
            buffer.append(i);
            buffer.append("'");
            if (tabIndexValues[i] != null) {
                buffer.append(" tabindex='");
                buffer.append(tabIndexValues[i]);
                buffer.append("'");
            }
            buffer.append(" onblur='alert(\"blur-" + i + "\")'");
            buffer.append(" onfocus='alert(\"focus-" + i + "\")'");
            buffer.append(" accesskey='" + (char) ('a' + i) + "'");
            buffer.append(">\n");
        }
        buffer.append("<div id='div1'>foo</div>\n"); // something that isn't tabbable

        // Elements that are tabbable but are disabled
        buffer.append("<button name='button1' id='button1' disabled onclick='alert(\"buttonPushed\")' ");
        buffer.append("accesskey='1'>foo</button>\n");

        buffer.append("</form></body></html>");

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, buffer.toString());
        webClient.setWebConnection(webConnection);

        return (HtmlPage) webClient.getPage(URL_FIRST);
    }

    /**
     * Test {@link WebClient#loadWebResponseInto(WebResponse,WebWindow)}
     * @throws Exception If the test fails.
     */
    public void testLoadWebResponseInto() throws Exception {
        final WebClient webClient = new WebClient();
        final WebResponse webResponse = new StringWebResponse(
            "<html><head><title>first</title></head><body></body></html>");

        final Page page = webClient.loadWebResponseInto(
            webResponse, webClient.getCurrentWindow());
        assertInstanceOf(page, HtmlPage.class);

        final HtmlPage htmlPage = (HtmlPage) page;
        assertEquals("first", htmlPage.getTitleText());
    }

    /**
     * Verifies that exceptions are thrown on failing status code and the returned page
     * is still set as the current page in the WebWindow.
     *
     * @throws Exception if test fails
     */
    public void testGetPageFailingStatusCode() throws Exception {
        final String firstContent = "<html><head><title>Hello World</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_FIRST, firstContent, 500, "BOOM", "text/html", emptyList);
        webClient.setWebConnection(webConnection);
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.setPrintContentOnFailingStatusCode(false);
        try {
            webClient.getPage(URL_FIRST);
            fail("Should have thrown");
        }
        catch (final FailingHttpStatusCodeException e) {
            assertEquals(e.getStatusCode(), 500);
            assertEquals(e.getStatusMessage(), "BOOM");
            assertEquals(firstContent, e.getResponse().getContentAsString());
        }
        final HtmlPage page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
        assertEquals("Hello World", page.getTitleText());
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testProxyConfig() throws Exception {

        final String defaultProxyHost = "defaultProxyHost";
        final int defaultProxyPort = 777;
        final String html = "<html><head><title>Hello World</title></head><body></body></html>";
        final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_6_0,
                defaultProxyHost, defaultProxyPort);
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        // Make sure default proxy settings are used.
        webClient.getPage(URL_FIRST);
        assertEquals(defaultProxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(defaultProxyPort, webConnection.getLastWebRequestSettings().getProxyPort());

        // Make sure custom proxy settings are used.
        final String customProxyHost = "customProxyHost";
        final int customProxyPort = 1000;
        final WebRequestSettings settings = new WebRequestSettings(URL_FIRST);
        settings.setProxyHost(customProxyHost);
        settings.setProxyPort(customProxyPort);
        webClient.getPage(settings);
        assertEquals(customProxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(customProxyPort, webConnection.getLastWebRequestSettings().getProxyPort());

        // Make sure proxy bypass works with default proxy settings.
        webClient.addHostsToProxyBypass(URL_FIRST.getHost());
        webClient.getPage(URL_FIRST);
        assertEquals(null, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(0, webConnection.getLastWebRequestSettings().getProxyPort());

        // Make sure proxy bypass doesn't work with custom proxy settings.
        webClient.getPage(settings);
        assertEquals(customProxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(customProxyPort, webConnection.getLastWebRequestSettings().getProxyPort());

        // Make sure we can remove proxy bypass filters.
        webClient.removeHostsFromProxyBypass(URL_FIRST.getHost());
        webClient.getPage(URL_FIRST);
        assertEquals(defaultProxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(defaultProxyPort, webConnection.getLastWebRequestSettings().getProxyPort());
    }

    /**
     * Regression test for
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1669097&group_id=47038&atid=448266
     * @throws Exception If the test fails.
     */
    public void testProxyConfigWithRedirect() throws Exception {

        final String defaultProxyHost = "defaultProxyHost";
        final int defaultProxyPort = 777;
        final String html = "<html><head><title>Hello World</title></head><body></body></html>";
        final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_6_0,
                defaultProxyHost, defaultProxyPort);

        webClient.addHostsToProxyBypass("hostToByPass");
        
        final String location2 = "http://hostToByPass/foo.html";
        final List<KeyValuePair> headers = Collections.singletonList(new KeyValuePair("Location", location2));
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, html, 302, "Some error", "text/html", headers);
        webConnection.setResponse(new URL(location2), "<html><head><title>2nd page</title></head></html>");
        webClient.setWebConnection(webConnection);

        final Page page2 = webClient.getPage(URL_FIRST);
        webClient.getPage(URL_FIRST);
        assertEquals(null, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(0, webConnection.getLastWebRequestSettings().getProxyPort());
        assertEquals(location2, page2.getWebResponse().getUrl());

        // Make sure default proxy settings are used.
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setRedirectEnabled(false);
        final Page page1 = webClient.getPage(URL_FIRST);
        assertEquals(defaultProxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(defaultProxyPort, webConnection.getLastWebRequestSettings().getProxyPort());
        assertEquals(URL_FIRST, page1.getWebResponse().getUrl());
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testProxyConfigForJS() throws Exception {

        final String defaultProxyHost = "defaultProxyHost";
        final int defaultProxyPort = 777;
        final String html = "<html><head><title>Hello World</title>\n"
            + "<script language='javascript' type='text/javascript' src='foo.js'></script>\n"
            + "</head><body></body></html>";
        final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_6_0,
                defaultProxyHost, defaultProxyPort);
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(new URL(URL_FIRST, "foo.js"), "", "text/javascript");
        webClient.setWebConnection(webConnection);

        // Make sure default proxy settings are used.
        webClient.getPage(URL_FIRST);
        assertEquals(defaultProxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(defaultProxyPort, webConnection.getLastWebRequestSettings().getProxyPort());

        // Make sure proxy bypass works with default proxy settings.
        webClient.addHostsToProxyBypass(URL_FIRST.getHost());
        webClient.getPage(URL_FIRST);
        assertEquals(null, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(0, webConnection.getLastWebRequestSettings().getProxyPort());

        // Make sure we can remove proxy bypass filters.
        webClient.removeHostsFromProxyBypass(URL_FIRST.getHost());
        webClient.getPage(URL_FIRST);
        assertEquals(defaultProxyHost, webConnection.getLastWebRequestSettings().getProxyHost());
        assertEquals(defaultProxyPort, webConnection.getLastWebRequestSettings().getProxyPort());
    }

    /**
     * Test {@link WebClient#expandUrl(URL,String)} for the case where an anchor name
     * was specified.
     * @throws Exception If the test fails.
     */
    public void testExpandUrl() throws Exception {
        assertEquals("http://first#second", WebClient.expandUrl(URL_FIRST, "#second"));
        assertEquals("http://first?a=1&b=2", WebClient.expandUrl(new URL("http://first?a=1&b=2"), ""));
        assertEquals("http://first?b=2&c=3", WebClient.expandUrl(new URL("http://first?a=1&b=2"), "?b=2&c=3"));
        assertEquals("file:/home/myself/test.js",
                WebClient.expandUrl(new URL("file:/home/myself/myTest.html"), "test.js"));
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testExpandUrlWithFile() throws Exception {
        final String urlString = "http://host/page.html";
        final URL url = new URL(urlString);
        assertEquals(urlString + "#second", WebClient.expandUrl(url, "#second"));
    }

    /** Test the accessors for refreshHandler */
    public void testRefreshHandlerAccessors() {
        final WebClient webClient = new WebClient();
        assertInstanceOf(webClient.getRefreshHandler(), ImmediateRefreshHandler.class);

        final RefreshHandler handler = new ImmediateRefreshHandler() {
            private static final long serialVersionUID = 5357553245330318812L;
        };
        webClient.setRefreshHandler(handler);
        assertSame(handler, webClient.getRefreshHandler());
    }

    /**
     * Test the script preprocessor
     * @throws IOException if the test fails
     */
    public void testScriptPreProcessor() throws IOException {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String alertText = "content";
        final String newAlertText = "newcontent";
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "<!--\n   alert('" + alertText + "');\n// -->\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        // Test null return from pre processor
        client.setScriptPreProcessor(new ScriptPreProcessor() {
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                                      final HtmlElement htmlElement) {
                return null;
            }
        });
        client.setAlertHandler(new AlertHandler() {
            public void handleAlert(final Page page, final String message) {
                fail("The pre processor did not remove the javascript");
            }

        });
        client.getPage("http://www.yahoo.com");

        // Test modify script in pre processor
        client.setScriptPreProcessor(new ScriptPreProcessor() {
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                                      final HtmlElement htmlElement) {
                final int start = sourceCode.indexOf(alertText);
                final int end = start + alertText.length();

                return sourceCode.substring(0, start) + newAlertText + sourceCode.substring(end);
            }
        });
        client.setAlertHandler(new AlertHandler() {
            public void handleAlert(final Page page, final String message) {
                if (!message.equals(newAlertText)) {
                    fail("The pre processor did not modify the javascript");
                }
            }

        });
        client.getPage("http://www.yahoo.com");
    }

    /**
     * Test the ScriptPreProcessor's ability to filter out a javascript method
     * that is not implemented without affecting the rest of the page.
     *
     * @throws Exception if the test fails
     */
    public void testScriptPreProcessor_UnimplementedJavascript() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String content = "<html><head><title>foo</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<script>document.unimplementedFunction();</script>\n"
            + "<script>alert('implemented function');</script>\n"
            + "</body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        client.setScriptPreProcessor(new ScriptPreProcessor() {
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                                      final HtmlElement htmlElement) {
                if (sourceCode.indexOf("unimplementedFunction") > -1) {
                    return "";
                }
                return sourceCode;
            }
        });
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        client.getPage("http://page");

        assertEquals(1, alerts.size());
        assertEquals("implemented function", alerts.get(0).toString());
    }

    /**
     * Apparently if the browsers receive a charset that they don't understand, they ignore
     * it and assume ISO-8895-1.  Ensure we do the same.
     * @throws Exception If the test fails.
     */
    public void testBadCharset() throws Exception {
        final String page1Content
            = "<html><head><title>foo</title>\n"
            + "</head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, page1Content, "text/html; charset=garbage");

        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertInstanceOf(page, HtmlPage.class);
    }

    /**
     * Colons are legal in the path of a url but {@link WebClient#expandUrl(URL,String)} was
     * blowing up on this case.  Ensure it's fixed.
     * @throws Exception If the test fails.
     */
    public void testExpandUrlHandlesColonsInRelativeUrl() throws Exception {
        final URL newUrl = WebClient.expandUrl(new URL("http://host/foo"), "/bar/blah:de:blah");
        assertEquals("http://host/bar/blah:de:blah", newUrl);
    }

    /**
     * Test reuse of a single {@link HtmlPage} object to submit the same form multiple times.
     *
     * @throws Exception if test fails
     */
    public void testReusingHtmlPageToSubmitFormMultipleTimes() throws Exception {

        final String firstContent = "<html><head><title>First</title></head>\n"
            + "<body onload='document.myform.mysubmit.focus()'>\n"
            + "<form action='" + URL_SECOND + "' name='myform'>\n"
            + "<input type='submit' name='mysubmit'>\n"
            + "</form></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body>Second</body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        for (int i = 0; i < 100; i++) {
            page.getFormByName("myform").submit((SubmittableElement) null);
        }
    }

    /**
     * Test the value of window.opener when a link has target="_top"
     * @throws Exception if test fails
     */
    public void testOpenerInFrameset() throws Exception {

        final String firstContent = "<html><head><script>alert(window.opener)</script><frameset cols='*'>\n"
                                    + "<frame src='" + URL_SECOND + "'>\n"
                                    + "</frameset>\n"
                                    + "</html>";
        final String secondContent = "<html><body><a href='" + URL_FIRST + "' target='_top'>to top</a></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        final HtmlPage pageInFrame = (HtmlPage) ((WebWindow) page.getFrames().get(0)).getEnclosedPage();
        ((HtmlAnchor) pageInFrame.getAnchors().get(0)).click();

        final String[] expectedAlerts = {"null", "null"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test setting the NekoHTML logging and parsing flags
     *
     * @throws Exception if test fails
     */
    public void testNekoFlagSetters() throws Exception {
        assertEquals("Default ignore content is wrong", false, WebClient.getIgnoreOutsideContent());
        WebClient.setIgnoreOutsideContent(true);
        assertTrue("Ignore content did not get set", WebClient.getIgnoreOutsideContent());
    }
    
    /**
     * Unset the static items set in tests here
     * @throws Exception if superclass throws
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        WebClient.setIgnoreOutsideContent(false);
    }

    /**
     *
     * @throws Exception if an error occurs
     */
    public void testGuessContentType() throws Exception {
        final WebClient webClient = new WebClient();

        // test real files with bad file suffix
        assertEquals("tiny-png.img", "image/png", webClient.guessContentType(getTestFile("tiny-png.img")));
        assertEquals("tiny-jpg.img", "image/jpeg", webClient.guessContentType(getTestFile("tiny-jpg.img")));
        assertEquals("tiny-gif.img", "image/gif", webClient.guessContentType(getTestFile("tiny-gif.img")));

        // tests empty files, type should be determined from file suffix
        assertEquals("empty.png", "image/png", webClient.guessContentType(getTestFile("empty.png")));
        assertEquals("empty.jpg", "image/jpeg", webClient.guessContentType(getTestFile("empty.jpg")));
        assertEquals("empty.gif", "image/gif", webClient.guessContentType(getTestFile("empty.gif")));
        assertEquals("empty.js", "text/javascript", webClient.guessContentType(getTestFile("empty.js")));
    }

    /**
     * Test that no encoding disturb file reads from filesystem.
     * For instance this test failed under Linux with LANG=de_DE.UTF-8 or LANG=C
     * but worked for LANG=de_DE.ISO-8859-1
     * @throws Exception if the test fails.
     */
    public void testBinaryFileFromFileSystem() throws Exception {
    	testBinaryFileFromFileSystem(BrowserVersion.FIREFOX_2);
    	testBinaryFileFromFileSystem(BrowserVersion.INTERNET_EXPLORER_6_0);
    }

    private void testBinaryFileFromFileSystem(final BrowserVersion browser) throws Exception {
        final String testfileName = "tiny-jpg.img";
        final File testfile = getTestFile(testfileName);
        final byte[] directBytes = IOUtils.toByteArray(new FileInputStream(testfile));
        final String directStr = hexRepresentation(directBytes);
        final WebClient client = new WebClient(browser);
        final Page testpage = client.getPage(testfile.toURI().toURL());
        final byte[] webclientBytes = IOUtils.toByteArray(testpage.getWebResponse().getContentAsStream());
        final String webclientStr = hexRepresentation(webclientBytes);
        assertEquals(directStr, webclientStr);
    }

    /**
     * Helper to make hex diff human easier to read for human eyes
     * @param digest the bytes
     * @return the hex representation
     */
    private static String hexRepresentation(final byte[] digest) {
        final StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            final byte b = digest[i];
            hexString.append(Integer.toHexString(0xFF & b));
            hexString.append(" ");
        }
        return hexString.toString().trim();
    }

    /**
     * Gets the file located in testfiles from the file name
     * @param fileName the file name
     * @return the file
     * @throws Exception if a pb occurs
     */
    private File getTestFile(final String fileName) throws Exception {
        final URL url = getClass().getClassLoader().getResource("testfiles/" + fileName);
        if (url == null) {
            throw new FileNotFoundException(fileName);
        }
        final File file = new File(new URI(url.toString()));

        return file;
    }

    /**
     * Test that additional header are correctly transmitted to the web connection.
     * @throws Exception If something goes wrong.
     */
    public void testRequestHeader() throws Exception {
        final String content = "<html></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);
        assertNull(webConnection.getLastAdditionalHeaders().get("foo-header"));

        client.addRequestHeader("foo-header", "foo value");
        client.getPage(URL_FIRST);
        assertEquals("foo value", webConnection.getLastAdditionalHeaders().get("foo-header"));

        client.removeRequestHeader("foo-header");
        client.getPage(URL_FIRST);
        assertNull(webConnection.getLastAdditionalHeaders().get("foo-header"));
    }

    /**
     * Test that content type is looked in a case insensitive way.
     * Cf <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>:
     * "All media type values, subtype values, and parameter names as defined
     * are case-insensitive".
     * @throws Exception If something goes wrong.
     */
    public void testContentTypeCaseInsensitive() throws Exception {
        final String content = "<html><head>\n"
            + "<script type='Text/Javascript' src='foo.js'></script>\n"
            + "</head></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse("alert('foo')", 200, "OK", "Text/Javascript");
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final String[] expectedAlerts = {"foo"};

        webConnection.setResponse(URL_FIRST, content, "Text/Html");
        assertInstanceOf(client.getPage(URL_FIRST), HtmlPage.class);
        assertEquals(expectedAlerts, collectedAlerts);

        webConnection.setResponse(URL_FIRST, content, "Text/XHtml");
        collectedAlerts.clear();
        assertInstanceOf(client.getPage(URL_FIRST), HtmlPage.class);
        assertEquals(expectedAlerts, collectedAlerts);

        webConnection.setResponse(URL_FIRST, content, "Text/Xml");
        assertInstanceOf(client.getPage(URL_FIRST), XmlPage.class);
        webConnection.setResponse(URL_FIRST, content, "ApplicaTion/Xml");
        assertInstanceOf(client.getPage(URL_FIRST), XmlPage.class);

        webConnection.setResponse(URL_FIRST, content, "Text/Plain");
        assertInstanceOf(client.getPage(URL_FIRST), TextPage.class);

        webConnection.setResponse(URL_FIRST, "", "Text/JavaScript");
        assertInstanceOf(client.getPage(URL_FIRST), JavaScriptPage.class);
    }

    /**
     * Load a javascript function from an external file using src references
     * inside a script element.
     *
     * @throws Exception if the test fails
     */
    public void testLoadFilePageWithExternalJS() throws Exception {
        final File currentDirectory = new File((new File("")).getAbsolutePath());

        final String encoding = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();

        // javascript file
        final File tmpFileJS = File.createTempFile("test", ".js", currentDirectory);
        tmpFileJS.deleteOnExit();
        FileUtils.writeStringToFile(tmpFileJS, "alert('foo')", encoding);

        // html file
        final String html = "<html><head></head><body>\n"
            + "<script language='javascript' type='text/javascript' src='" + tmpFileJS.getName() + "'></script>\n"
            + "</body></html>";
        final File tmpFile = File.createTempFile("test", ".html", currentDirectory);
        tmpFile.deleteOnExit();
        FileUtils.writeStringToFile(tmpFile, html, encoding);

        final URL fileURL = new URL("file://" + tmpFile.getCanonicalPath());
        final WebClient webClient = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        webClient.getPage(fileURL);

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that WebClient.getPage(String) calls WebClient.getPage(URL) with the right URL
     * @throws Exception if the test fails
     */
    public void testGetPageWithStringArg() throws Exception {
        final URL[] calledUrls = {null};
        final WebClient wc = new WebClient() {
            private static final long serialVersionUID = -8065766721260679248L;

            public Page getPage(final URL url) throws IOException, FailingHttpStatusCodeException {
                calledUrls[0] = url;
                return null;
            }
        };

        wc.getPage(URL_GARGOYLE.toExternalForm());
        assertEquals(URL_GARGOYLE, calledUrls[0]);
    }

    /**
     * Verifies that {@link WebClient#getPage(WebWindow, WebRequestSettings)} calls OnBeforeUnload
     * on the specified window's page, not on the client's "current" page.
     * @throws Exception if an error occurs
     */
    public void testOnBeforeUnloadCalledOnCorrectPage() throws Exception {
        final String html = "<html><body onbeforeunload='alert(7)'><iframe></iframe></body></html>";
        final List<String> alerts = new ArrayList<String>();
        loadPage(html, alerts);
        assertTrue(alerts.isEmpty());
    }

    /**
     * Test that '+' is not encoded in urls
     * @throws Exception if the test fails
     */
    public void testPlusNotEncodedInUrl() throws Exception {
        final URL url = new URL("http://host/search/my+category/");

        final HtmlPage page = loadPage("<html></html>", new ArrayList<String>(), url);
        assertEquals("http://host/search/my+category/", page.getWebResponse().getUrl());
    }
}
