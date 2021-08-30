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

import static com.gargoylesoftware.htmlunit.BrowserVersion.INTERNET_EXPLORER;
import static java.util.Arrays.asList;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;
import com.gargoylesoftware.htmlunit.html.parser.neko.HtmlUnitNekoHtmlParser;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests for {@link WebClient}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Chris Erskine
 * @author Hans Donner
 * @author Paul King
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Carsten Steul
 * @author Joerg Werner
 */
@RunWith(BrowserRunner.class)
public class WebClientTest extends SimpleWebTestCase {

    /**
     * Test the situation where credentials are required but they haven't been specified.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void credentialProvider_NoCredentials() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
                + "No access</body></html>";
        final WebClient client = getWebClient();
        client.getOptions().setPrintContentOnFailingStatusCode(false);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent, 401, "Credentials missing or just plain wrong",
                MimeType.TEXT_PLAIN);
        client.setWebConnection(webConnection);

        try {
            client.getPage(new WebRequest(URL_FIRST, HttpMethod.POST));
            fail("Expected FailingHttpStatusCodeException");
        }
        catch (final FailingHttpStatusCodeException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    /**
     * Test that the {@link WebWindowEvent#CHANGE} window event gets fired at the
     * appropriate time.
     * @throws Exception if something goes wrong
     */
    @Test
    public void htmlWindowEvents_changed() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
                + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>\n"
                + "</body></html>";
        final WebClient client = getWebClient();

        final List<WebWindowEvent> events = new LinkedList<>();
        client.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = firstPage.getHtmlElementById("a2");

        final List<WebWindowEvent> firstExpectedEvents = Arrays.asList(new WebWindowEvent[] {
            new WebWindowEvent(client.getCurrentWindow(), WebWindowEvent.CHANGE, null, firstPage)
        });
        assertEquals(firstExpectedEvents, events);

        events.clear();
        final HtmlPage secondPage = anchor.click();

        final List<WebWindowEvent> secondExpectedEvents = Arrays.asList(new WebWindowEvent[] {
            new WebWindowEvent(client.getCurrentWindow(), WebWindowEvent.CHANGE, firstPage, secondPage)
        });
        assertEquals(secondExpectedEvents, events);
    }

    /**
     * Test that the {@link WebWindowEvent#OPEN} window event gets fired at
     * the appropriate time.
     * @throws Exception if something goes wrong
     */
    @Test
    public void htmlWindowEvents_opened() throws Exception {
        final String page1Content = "<html><head><title>foo</title>\n"
                + "<script>window.open('" + URL_SECOND + "', 'myNewWindow')</script>\n"
                + "</head><body>\n"
                + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>\n"
                + "</body></html>";
        final String page2Content = "<html><head><title>foo</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<WebWindowEvent> events = new LinkedList<>();
        client.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, page1Content);
        webConnection.setResponse(URL_SECOND, page2Content);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
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
        assertEquals(expectedEvents, events);
    }

    /**
     * Test that the {@link WebWindowEvent#CLOSE} window event gets fired at
     * the appropriate time.
     * @throws Exception if something goes wrong
     */
    @Test
    public void htmlWindowEvents_closedFromFrame() throws Exception {
        final String firstContent = "<html><head><title>first</title></head><body>\n"
                + "<iframe src='" + URL_THIRD + "' id='frame1'></iframe>\n"
                + "<a href='" + URL_SECOND + "' id='a2'>link to foo2</a>\n"
                + "</body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>third</title></head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        assertEquals("first", firstPage.getTitleText());

        final List<WebWindowEvent> events = new LinkedList<>();
        client.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final HtmlInlineFrame frame = firstPage.getHtmlElementById("frame1");
        final HtmlPage thirdPage = (HtmlPage) frame.getEnclosedPage();

        // Load the second page
        final HtmlAnchor anchor = firstPage.getHtmlElementById("a2");
        final HtmlPage secondPage = anchor.click();
        assertEquals("second", secondPage.getTitleText());

        final WebWindow firstWindow = client.getCurrentWindow();
        final List<WebWindowEvent> expectedEvents = Arrays.asList(new WebWindowEvent[] {
            new WebWindowEvent(
                frame.getEnclosedWindow(), WebWindowEvent.CLOSE, thirdPage, null),
            new WebWindowEvent(
                firstWindow, WebWindowEvent.CHANGE, firstPage, secondPage),
        });
        assertEquals(expectedEvents.get(0), events.get(0));
        assertEquals(expectedEvents, events);
    }

    /**
     * Test a 301 redirection code where the original request was a GET.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection301_MovedPermanently_GetMethod() throws Exception {
        final int statusCode = 301;
        final HttpMethod initialRequestMethod = HttpMethod.GET;
        final HttpMethod expectedRedirectedRequestMethod = HttpMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Common utility for GET after POST redirection on same URLs
     * @param statusCode the code to return from the initial request
     * @throws Exception if the test fails
     */
    private void doTestRedirectionSameUrlAfterPost(final int statusCode) throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = getWebClient();

        final List<NameValuePair> headers =
                Collections.singletonList(new NameValuePair("Location", URL_FIRST.toExternalForm()));

        // builds a webconnection that first sends a redirect and then a "normal" response for
        // the same requested URL
        final MockWebConnection webConnection = new MockWebConnection() {
            private int count_ = 0;
            @Override
            public WebResponse getResponse(final WebRequest webRequest) throws IOException {
                ++count_;
                if (count_ == 1) {
                    final WebResponse response = super.getResponse(webRequest);
                    setResponse(webRequest.getUrl(), secondContent);
                    return response;
                }
                return super.getResponse(webRequest);
            }
        };
        webConnection.setResponse(URL_FIRST, firstContent, statusCode, "Some error", MimeType.TEXT_HTML, headers);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(new WebRequest(URL_FIRST, HttpMethod.POST));
        final WebResponse webResponse = page.getWebResponse();
        // A redirect should have happened
        assertEquals(200, webResponse.getStatusCode());
        assertEquals(URL_FIRST, webResponse.getWebRequest().getUrl());
        assertEquals("Second", page.getTitleText());
        assertSame(HttpMethod.GET, webResponse.getWebRequest().getHttpMethod());
    }

    /**
     * From the HTTP spec:  If the 301 status code is received in response
     * to a request other than GET or HEAD, the user agent MUST NOT automatically
     * redirect the request unless it can be confirmed by the user, since this
     * might change the conditions under which the request was issued.
     * BUT Firefox follows the redirection
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection301_MovedPermanently_PostMethod() throws Exception {
        doTestRedirection(301, HttpMethod.POST, HttpMethod.GET);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void redirection301_MovedPermanently_PostMethod2() throws Exception {
        doTestRedirectionSameUrlAfterPost(301);
    }

    /**
     * From the HTTP spec:  Note: RFC 1945 and RFC 2068 specify that the client
     * is not allowed to change the method on the redirected request. However,
     * most existing user agent implementations treat 302 as if it were a 303
     * response, performing a GET on the Location field-value regardless
     * of the original request method. The status codes 303 and 307 have
     * been added for servers that wish to make unambiguously clear which
     * kind of reaction is expected of the client.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection302_MovedTemporarily_PostMethod() throws Exception {
        doTestRedirection(302, HttpMethod.POST, HttpMethod.GET);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void redirection302_MovedTemporarily_PostMethod2() throws Exception {
        doTestRedirectionSameUrlAfterPost(302);
    }

    /**
     * Test a 302 redirection code.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection302_MovedTemporarily_GetMethod() throws Exception {
        final int statusCode = 302;
        final HttpMethod initialRequestMethod = HttpMethod.GET;
        final HttpMethod expectedRedirectedRequestMethod = HttpMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Test a 302 redirection code with "," in URL parameters.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection302_MovedTemporarily_CommaInParameters() throws Exception {
        doTestRedirection(302, HttpMethod.GET, HttpMethod.GET, URL_SECOND + "/foo.html?foo1=abc&foo2=1,2,3,4");
    }

    /**
     * Tests a 303 redirection code. This should be the same as a 302.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection303_SeeOther_GetMethod() throws Exception {
        final int statusCode = 303;
        final HttpMethod initialRequestMethod = HttpMethod.GET;
        final HttpMethod expectedRedirectedRequestMethod = HttpMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Tests a 303 redirection code - this should be the same as a 302.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection303_SeeOther_PostMethod() throws Exception {
        doTestRedirection(303, HttpMethod.POST, HttpMethod.GET);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection303_SeeOther_PostMethod2() throws Exception {
        doTestRedirectionSameUrlAfterPost(303);
    }

    /**
     * Tests a 307 redirection code.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection307_TemporaryRedirect_GetMethod() throws Exception {
        final int statusCode = 307;
        final HttpMethod initialRequestMethod = HttpMethod.GET;
        final HttpMethod expectedRedirectedRequestMethod = HttpMethod.GET;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Tests a 307 redirection code.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirection307_TemporaryRedirect_PostMethod() throws Exception {
        final int statusCode = 307;
        final HttpMethod initialRequestMethod = HttpMethod.POST;
        final HttpMethod expectedRedirectedRequestMethod = HttpMethod.POST;

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod);
    }

    /**
     * Basic logic for all the redirection tests.
     *
     * @param statusCode the code to return from the initial request
     * @param initialRequestMethod the initial request
     * @param expectedRedirectedRequestMethod the submit method of the second (redirected) request
     * If a redirect is not expected to happen then this must be null
     * @throws Exception if the test fails
     */
    private void doTestRedirection(
            final int statusCode,
            final HttpMethod initialRequestMethod,
            final HttpMethod expectedRedirectedRequestMethod)
        throws Exception {

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod,
                URL_SECOND.toExternalForm());
    }

    /**
     * Basic logic for all the redirection tests.
     *
     * @param statusCode the code to return from the initial request
     * @param initialRequestMethod the initial request
     * @param expectedRedirectedRequestMethod the submit method of the second (redirected) request
     * If a redirect is not expected to happen then this must be null
     * @param newLocation the Location set in the redirection header
     * @throws Exception if the test fails
     */
    private void doTestRedirection(
            final int statusCode,
            final HttpMethod initialRequestMethod,
            final HttpMethod expectedRedirectedRequestMethod,
            final String newLocation)
        throws Exception {

        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod, newLocation, false);
        doTestRedirection(statusCode, initialRequestMethod, expectedRedirectedRequestMethod, newLocation, true);
    }

    /**
     * Browsers allow many redirections to the same URL before to stop redirections.
     * See Bug 1619765 and feature request 1472343.
     * @throws Exception if the test fails
     */
    @Test
    public void redirectionSameURL() throws Exception {
        final HtmlPage page1 = getPageWithRedirectionsSameURL(1);
        assertEquals("Second", page1.getTitleText());

        try {
            getPageWithRedirectionsSameURL(30);
        }
        catch (final Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Too much redirect"));
        }
    }

    private HtmlPage getPageWithRedirectionsSameURL(final int nbRedirections) throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = getWebClient();

        final URL url = URL_FIRST;
        final List<NameValuePair> headers =
                Collections.singletonList(new NameValuePair("Location", URL_FIRST.toExternalForm()));
        final MockWebConnection webConnection = new MockWebConnection() {
            private int count_ = 0;
            @Override
            public WebResponse getResponse(final WebRequest webRequest) throws IOException {
                ++count_;
                if (count_ < nbRedirections) {
                    setResponse(url, firstContent, 302, "Redirect needed " + count_, MimeType.TEXT_HTML, headers);
                    return super.getResponse(webRequest);
                }
                else if (count_ == nbRedirections) {
                    final WebResponse response = super.getResponse(webRequest);
                    setResponse(webRequest.getUrl(), secondContent);
                    return response;
                }
                else {
                    return super.getResponse(webRequest);
                }
            }
        };
        webConnection.setResponse(url, firstContent, 302, "Redirect needed", MimeType.TEXT_HTML, headers);
        webClient.setWebConnection(webConnection);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        return webClient.getPage(url);
    }

    /**
     * Verifies that any additional headers in the original {@link WebRequest} instance are kept
     * and sent to the redirect location. Specifically, the "Referer" header set in various locations was
     * being lost during redirects (see bug 1987911).
     * @throws Exception if an error occurs
     */
    @Test
    public void redirection_AdditionalHeadersMaintained() throws Exception {
        redirection_AdditionalHeadersMaintained(301);
        redirection_AdditionalHeadersMaintained(302);
    }

    private void redirection_AdditionalHeadersMaintained(final int statusCode) throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);

        final List<NameValuePair> headers = asList(new NameValuePair("Location", URL_SECOND.toString()));
        conn.setResponse(URL_FIRST, "", statusCode, "", MimeType.TEXT_HTML, headers);
        conn.setResponse(URL_SECOND, "<html><body>abc</body></html>");

        final WebRequest request = new WebRequest(URL_FIRST);
        request.setAdditionalHeader("foo", "bar");
        client.getPage(request);

        assertEquals(URL_SECOND, conn.getLastWebRequest().getUrl());
        assertEquals("bar", conn.getLastAdditionalHeaders().get("foo"));
    }

    /**
     * Basic logic for all the redirection tests.
     *
     * @param statusCode the code to return from the initial request
     * @param initialRequestMethod the initial request
     * @param expectedRedirectedRequestMethod the submit method of the second (redirected) request
     * If a redirect is not expected to happen then this must be null
     * @param newLocation the Location set in the redirection header
     * @param useProxy indicates if the test should be performed with a proxy
     * @throws Exception if the test fails
     */
    @SuppressWarnings("resource")
    private void doTestRedirection(
            final int statusCode,
            final HttpMethod initialRequestMethod,
            final HttpMethod expectedRedirectedRequestMethod,
            final String newLocation,
            final boolean useProxy)
        throws Exception {

        final String firstContent = "<html><head><title>First</title></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient;
        final String proxyHost;
        final int proxyPort;
        if (useProxy) {
            proxyHost = "someHost";
            proxyPort = 12233345;
            webClient = new WebClient(getBrowserVersion(), proxyHost, proxyPort);
        }
        else {
            proxyHost = null;
            proxyPort = 0;
            webClient = getWebClient();
        }

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);

        final List<NameValuePair> headers = Collections.singletonList(new NameValuePair("Location", newLocation));
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent, statusCode, "Some error", MimeType.TEXT_HTML, headers);
        webConnection.setResponse(new URL(newLocation), secondContent);

        webClient.setWebConnection(webConnection);

        final URL url = URL_FIRST;

        HtmlPage page;
        WebResponse webResponse;

        //
        // Second time redirection is turned on (default setting)
        //
        page = webClient.getPage(new WebRequest(url, initialRequestMethod));
        webResponse = page.getWebResponse();
        if (expectedRedirectedRequestMethod == null) {
            // No redirect should have happened
            assertEquals(statusCode, webResponse.getStatusCode());
            assertEquals(initialRequestMethod, webConnection.getLastMethod());
        }
        else {
            // A redirect should have happened
            assertEquals(HttpStatus.SC_OK, webResponse.getStatusCode());
            assertEquals(newLocation, webResponse.getWebRequest().getUrl());
            assertEquals("Second", page.getTitleText());
            assertEquals(expectedRedirectedRequestMethod, webConnection.getLastMethod());
        }
        assertEquals(proxyHost, webConnection.getLastWebRequest().getProxyHost());
        assertEquals(proxyPort, webConnection.getLastWebRequest().getProxyPort());
        assertNull(webConnection.getLastWebRequest().getProxyScheme());
        assertFalse(webConnection.getLastWebRequest().isSocksProxy());

        //
        // Second time redirection is turned off
        //
        webClient.getOptions().setRedirectEnabled(false);
        page = webClient.getPage(new WebRequest(url, initialRequestMethod));
        webResponse = page.getWebResponse();
        assertEquals(statusCode, webResponse.getStatusCode());
        assertEquals(initialRequestMethod, webConnection.getLastMethod());
        assertEquals(proxyHost, webConnection.getLastWebRequest().getProxyHost());
        assertEquals(proxyPort, webConnection.getLastWebRequest().getProxyPort());
        assertNull(webConnection.getLastWebRequest().getProxyScheme());
        assertFalse(webConnection.getLastWebRequest().isSocksProxy());

        webClient.close();
    }

    /**
     * Test passing in a null page creator.
     */
    @Test
    public void setPageCreator_null() {
        final WebClient webClient = getWebClient();
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
     * @throws Exception if something goes wrong
     */
    @Test
    public void setPageCreator() throws Exception {
        final String page1Content = "<html><head><title>foo</title>\n"
                + "</head><body>\n"
                + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>\n"
                + "</body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, page1Content);

        client.setWebConnection(webConnection);
        final List<Page> collectedPageCreationItems = new ArrayList<>();
        client.setPageCreator(new CollectingPageCreator(collectedPageCreationItems));

        final Page page = client.getPage(URL_FIRST);
        assertTrue("instanceof TextPage", page instanceof TextPage);

        final List<Page> expectedPageCreationItems = Arrays.asList(new Page[] {page});

        assertEquals(expectedPageCreationItems, collectedPageCreationItems);
    }

    /** A PageCreator that collects data. */
    private static class CollectingPageCreator implements PageCreator {
        private final List<Page> collectedPages_;

        private static final HTMLParser htmlParser_ = new HtmlUnitNekoHtmlParser();

        /**
         * Creates an instance.
         * @param list the list that will contain the data
         */
        CollectingPageCreator(final List<Page> list) {
            collectedPages_ = list;
        }

        /**
         * Creates a page.
         * @param webResponse the web response
         * @param webWindow the web window
         * @return the new page
         * @throws IOException if an IO problem occurs
         */
        @Override
        public Page createPage(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
            final Page page = new TextPage(webResponse, webWindow);
            webWindow.setEnclosedPage(page);
            collectedPages_.add(page);
            return page;
        }

        @Override
        public HTMLParser getHtmlParser() {
            return htmlParser_;
        }
    }

    /**
     * Tests loading a page with POST parameters.
     * @throws Exception if something goes wrong
     */
    @Test
    public void loadPage_PostWithParameters() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
                + "</body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final String urlString = "http://first?a=b";
        final URL url = new URL(urlString);
        final HtmlPage page = client.getPage(new WebRequest(url, HttpMethod.POST));

        assertEquals("http://first/?a=b", page.getUrl());
    }

    /**
     * Test that double / in query string are not changed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void loadPage_SlashesInQueryString() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head>\n"
                + "<body><a href='foo.html?id=UYIUYTY//YTYUY..F'>to page 2</a>\n"
                + "</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final Page page2 = page.getAnchors().get(0).click();
        final URL url2 = new URL(URL_FIRST, "foo.html?id=UYIUYTY//YTYUY..F");
        assertEquals(url2.toExternalForm(), page2.getUrl());
    }

    /**
     * Test loading a file page.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void loadFilePage() throws Exception {
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

        final WebClient client = getWebClient();
        final URL url = new URL("file://" + tmpFile.getCanonicalPath());
        final HtmlPage page = client.getPage(url);

        assertEquals(htmlContent, page.getWebResponse().getContentAsString());
        assertEquals(MimeType.TEXT_HTML, page.getWebResponse().getContentType());
        assertEquals(200, page.getWebResponse().getStatusCode());
        assertEquals("foo", page.getTitleText());

        // Test a file URL with a query portion (needs to work for Dojo, for example).

        final URL url2 = new URL(url + "?with=query");
        final HtmlPage page2 = client.getPage(url2);

        assertEquals(htmlContent, page2.getWebResponse().getContentAsString());
        assertEquals(MimeType.TEXT_HTML, page2.getWebResponse().getContentType());
        assertEquals(200, page2.getWebResponse().getStatusCode());
        assertEquals("foo", page2.getTitleText());

        // Test a file URL with a ref portion (needs to work for Dojo, for example).

        final URL url3 = new URL(url + "#reference");
        final HtmlPage page3 = client.getPage(url3);

        assertEquals(htmlContent, page3.getWebResponse().getContentAsString());
        assertEquals(MimeType.TEXT_HTML, page3.getWebResponse().getContentType());
        assertEquals(200, page3.getWebResponse().getStatusCode());
        assertEquals("foo", page3.getTitleText());
    }

    /**
     * Test loading a file page with non ascii names.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void loadFilePageEncoded() throws Exception {
        final WebClient client = getWebClient();

        final String whitespaceFilename = "white space.txt";
        final URL whitespaceFileURL = getClass().getClassLoader().getResource(whitespaceFilename);
        assertNotNull("Resource '" + whitespaceFilename + "' not found", whitespaceFileURL);
        final File whitespaceFile = new File(whitespaceFileURL.toURI());
        assertTrue("File '" + whitespaceFile.getAbsolutePath() + "' does not exist", whitespaceFile.exists());

        String url = "file://" + whitespaceFile.getCanonicalPath();
        Page page = client.getPage(url);
        assertEquals("the name of this file contains a blank", page.getWebResponse().getContentAsString());

        // encode the whitespace
        url = "file://" + whitespaceFile.getCanonicalPath().replace(" ", "%20");
        page = client.getPage(url);
        assertEquals("the name of this file contains a blank", page.getWebResponse().getContentAsString());

        final String unicodeFilename = "\u6A94\u6848\uD30C\uC77C\u30D5\u30A1\u30A4\u30EB\u0645\u0644\u0641.txt";
        final URL unicodeFileURL = getClass().getClassLoader().getResource(unicodeFilename);
        assertNotNull("Resource '" + unicodeFilename + "' not found", unicodeFileURL);
        final File unicodeFile = new File(unicodeFileURL.toURI());
        assertTrue("File '" + unicodeFile.getAbsolutePath() + "' does not exist", unicodeFile.exists());

        url = "file://" + unicodeFile.getCanonicalPath();
        page = client.getPage(url);
        assertEquals("", page.getWebResponse().getContentAsString());

        url = url.replace(
                unicodeFilename,
                "%e6%aa%94%e6%a1%88%ed%8c%8c%ec%9d%bc%e3%83%95%e3%82%a1%e3%82%a4%e3%83%ab%d9%85%d9%84%d9%81.txt");
        page = client.getPage(url);
        assertEquals("", page.getWebResponse().getContentAsString());
    }

    /**
     * Test loading a file page with XML content. Regression test for bug 1113487.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void loadFilePageXml() throws Exception {
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

        final WebClient client = getWebClient();
        final XmlPage page = (XmlPage) client.getPage(fileURL);

        assertEquals(xmlContent, page.getWebResponse().getContentAsString());
        // "text/xml" or "application/xml", it doesn't matter
        assertEquals("/xml", StringUtils.substring(page.getWebResponse().getContentType(), -4));
        assertEquals(200, page.getWebResponse().getStatusCode());
    }

    /**
     * Test redirecting with JavaScript during page load.
     * @throws Exception if something goes wrong
     */
    @Test
    public void redirectViaJavaScriptDuringInitialPageLoad() throws Exception {
        final String firstContent = "<html><head><title>First</title><script>\n"
                + "location.href='" + URL_SECOND + "';\n"
                + "</script></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        webClient.setWebConnection(webConnection);

        final URL url = URL_FIRST;

        final HtmlPage page = webClient.getPage(url);
        assertEquals("Second", page.getTitleText());
    }

    /**
     * Test tabbing where there are no tabbable elements.
     * @throws Exception if something goes wrong
     */
    @Test
    public void keyboard_NoTabbableElements() throws Exception {
        final WebClient webClient = getWebClient();
        final HtmlPage page = getPageForKeyboardTest(webClient, new String[0]);
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        DomElement focus = page.getFocusedElement();
        assertTrue("original", (focus == null)
                || (focus == page.getDocumentElement())
                || (focus == page.getBody()));

        focus = page.tabToPreviousElement();
        assertNull("previous", focus);

        focus = page.tabToNextElement();
        assertNull("next", focus);

        focus = page.pressAccessKey('a');
        assertNull("accesskey", focus);

        final String[] expectedAlerts = {};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test tabbing where there is only one tabbable element.
     * @throws Exception if something goes wrong
     */
    @Test
    public void keyboard_OneTabbableElement() throws Exception {
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{null});
        final HtmlElement element = page.getHtmlElementById("submit0");

        final DomElement focus = page.getFocusedElement();
        assertTrue("original", (focus == null)
                || (focus == page.getDocumentElement())
                || (focus == page.getBody()));

        final DomElement accessKey = page.pressAccessKey('x');
        assertEquals("accesskey", focus, accessKey);

        assertEquals("next", element, page.tabToNextElement());
        assertEquals("nextAgain", element, page.tabToNextElement());

        page.getFocusedElement().blur();
        assertNull("original", page.getFocusedElement());

        assertEquals("previous", element, page.tabToPreviousElement());
        assertEquals("previousAgain", element, page.tabToPreviousElement());

        assertEquals("accesskey", element, page.pressAccessKey('z'));

        final String[] expectedAlerts = {"focus-0", "blur-0", "focus-0"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test pressing an accesskey.
     * @throws Exception if something goes wrong
     */
    @Test
    public void accessKeys() throws Exception {
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{"1", "2", "3"});

        assertEquals("submit0", page.pressAccessKey('a').getAttribute("name"));
        assertEquals("submit2", page.pressAccessKey('c').getAttribute("name"));
        assertEquals("submit1", page.pressAccessKey('b').getAttribute("name"));

        final String[] expectedAlerts = {"focus-0", "blur-0", "focus-2", "blur-2", "focus-1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test tabbing to the next element.
     * @throws Exception if something goes wrong
     */
    @Test
    public void tabNext() throws Exception {
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{"1", "2", "3"});

        assertEquals("submit0", page.tabToNextElement().getAttribute("name"));
        assertEquals("submit1", page.tabToNextElement().getAttribute("name"));
        assertEquals("submit2", page.tabToNextElement().getAttribute("name"));

        final String[] expectedAlerts = {"focus-0", "blur-0", "focus-1", "blur-1", "focus-2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test tabbing to the previous element.
     * @throws Exception if something goes wrong
     */
    @Test
    public void tabPrevious() throws Exception {
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{"1", "2", "3"});

        assertEquals("submit2", page.tabToPreviousElement().getAttribute("name"));
        assertEquals("submit1", page.tabToPreviousElement().getAttribute("name"));
        assertEquals("submit0", page.tabToPreviousElement().getAttribute("name"));

        final String[] expectedAlerts = {"focus-2", "blur-2", "focus-1", "blur-1", "focus-0"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that a button can be selected via accesskey.
     * @throws Exception if something goes wrong
     */
    @Test
    public void pressAccessKey_Button() throws Exception {
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
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
     * Returns a loaded page for one of the keyboard tests.
     * @param webClient the WebClient to load the page from
     * @param tabIndexValues the tab index values; one input will be created for each item
     *        in this list
     * @return the loaded page
     * @throws Exception if something goes wrong
     */
    private static HtmlPage getPageForKeyboardTest(
            final WebClient webClient, final String[] tabIndexValues) throws Exception {

        final StringBuilder builder = new StringBuilder();
        builder.append("<html><head><title>First</title></head><body>")
                .append("<form name='form1' method='post' onsubmit='return false;'>");

        for (int i = 0; i < tabIndexValues.length; i++) {
            builder.append("<input type='submit' name='submit");
            builder.append(i);
            builder.append("' id='submit");
            builder.append(i);
            builder.append("'");
            if (tabIndexValues[i] != null) {
                builder.append(" tabindex='");
                builder.append(tabIndexValues[i]);
                builder.append("'");
            }
            builder.append(" onblur='alert(\"blur-" + i + "\")'");
            builder.append(" onfocus='alert(\"focus-" + i + "\")'");
            builder.append(" accesskey='" + (char) ('a' + i) + "'");
            builder.append(">\n");
        }
        builder.append("<div id='div1'>foo</div>\n"); // something that isn't tabbable

        // Elements that are tabbable but are disabled
        builder.append("<button name='button1' id='button1' disabled onclick='alert(\"buttonPushed\")' ");
        builder.append("accesskey='1'>foo</button>\n");

        builder.append("</form></body></html>");

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, builder.toString());
        webClient.setWebConnection(webConnection);

        return webClient.getPage(URL_FIRST);
    }

    /**
     * Test {@link WebClient#loadWebResponseInto(WebResponse,WebWindow)}.
     * @throws Exception if the test fails
     */
    @Test
    public void loadWebResponseInto() throws Exception {
        final WebClient webClient = getWebClient();
        final WebResponse webResponse = new StringWebResponse(
                "<html><head><title>first</title></head><body></body></html>", URL_FIRST);

        final Page page = webClient.loadWebResponseInto(webResponse, webClient.getCurrentWindow());
        assertTrue(HtmlPage.class.isInstance(page));

        final HtmlPage htmlPage = (HtmlPage) page;
        assertEquals("first", htmlPage.getTitleText());
    }

    /**
     * Verifies that exceptions are thrown on failing status code and the returned page
     * is still set as the current page in the WebWindow.
     *
     * @throws Exception if test fails
     */
    @Test
    public void getPageFailingStatusCode() throws Exception {
        final String firstContent = "<html><head><title>Hello World</title></head><body></body></html>";

        final WebClient webClient = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        final List<NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_FIRST, firstContent, 500, "BOOM", MimeType.TEXT_HTML, emptyList);
        webClient.setWebConnection(webConnection);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
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
     * @throws Exception if the test fails
     */
    @Test
    public void proxyConfig() throws Exception {
        // Create the client.
        final String defaultProxyHost = "defaultProxyHost";
        final int defaultProxyPort = 777;
        try (WebClient webClient = new WebClient(getBrowserVersion(),
                defaultProxyHost, defaultProxyPort)) {

            // Configure the mock web connection.
            final String html = "<html><head><title>Hello World</title></head><body></body></html>";
            final MockWebConnection webConnection = new MockWebConnection();
            webConnection.setResponse(URL_FIRST, html);
            webClient.setWebConnection(webConnection);

            // Make sure the default proxy settings are used.
            webClient.getPage(URL_FIRST);
            assertEquals(defaultProxyHost, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(defaultProxyPort, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());

            // Change the webclient default proxy settings.
            final String defaultProxyHost2 = "defaultProxyHost2";
            final int defaultProxyPort2 = 532;
            webClient.getOptions().getProxyConfig().setProxyHost(defaultProxyHost2);
            webClient.getOptions().getProxyConfig().setProxyPort(defaultProxyPort2);

            // Make sure the new default proxy settings are used.
            webClient.getPage(URL_FIRST);
            assertEquals(defaultProxyHost2, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(defaultProxyPort2, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());

            // Make sure the custom proxy settings are used.
            final String customProxyHost = "customProxyHost";
            final int customProxyPort = 1000;
            final WebRequest request = new WebRequest(URL_FIRST);
            request.setProxyHost(customProxyHost);
            request.setProxyPort(customProxyPort);
            webClient.getPage(request);
            assertEquals(customProxyHost, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(customProxyPort, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());

            // Make sure the proxy bypass works with default proxy settings.
            webClient.getOptions().getProxyConfig().addHostsToProxyBypass(URL_FIRST.getHost());
            webClient.getPage(URL_FIRST);
            assertNull(webConnection.getLastWebRequest().getProxyHost());
            assertEquals(0, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());

            // Make sure the proxy bypass doesn't work with custom proxy settings.
            webClient.getPage(request);
            assertEquals(customProxyHost, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(customProxyPort, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());

            // Make sure we can remove proxy bypass filters.
            webClient.getOptions().getProxyConfig().removeHostsFromProxyBypass(URL_FIRST.getHost());
            webClient.getPage(URL_FIRST);
            assertEquals(defaultProxyHost2, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(defaultProxyPort2, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());
        }
    }

    /**
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/431/.
     * @throws Exception if an error occurs
     */
    @Test
    public void proxyConfigWithRedirect() throws Exception {
        final String defaultProxyHost = "defaultProxyHost";
        final int defaultProxyPort = 777;
        final String html = "<html><head><title>Hello World</title></head><body></body></html>";
        try (WebClient webClient = new WebClient(getBrowserVersion(), defaultProxyHost, defaultProxyPort)) {

            webClient.getOptions().getProxyConfig().addHostsToProxyBypass("hostToByPass");

            final String location2 = "http://hostToByPass/foo.html";
            final List<NameValuePair> headers = Collections.singletonList(new NameValuePair("Location", location2));
            final MockWebConnection webConnection = new MockWebConnection();
            webConnection.setResponse(URL_FIRST, html, 302, "Some error", MimeType.TEXT_HTML, headers);
            webConnection.setResponse(new URL(location2), "<html><head><title>2nd page</title></head></html>");
            webClient.setWebConnection(webConnection);

            final Page page2 = webClient.getPage(URL_FIRST);
            webClient.getPage(URL_FIRST);
            assertEquals(null, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(0, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());
            assertEquals(location2, page2.getUrl());

            // Make sure default proxy settings are used.
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setRedirectEnabled(false);
            final Page page1 = webClient.getPage(URL_FIRST);
            assertEquals(defaultProxyHost, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(defaultProxyPort, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());
            assertEquals(URL_FIRST, page1.getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void proxyConfigForJS() throws Exception {
        final String defaultProxyHost = "defaultProxyHost";
        final int defaultProxyPort = 777;
        final String html = "<html><head><title>Hello World</title>\n"
                + "<script language='javascript' type='text/javascript' src='foo.js'></script>\n"
                + "</head><body></body></html>";
        try (WebClient webClient = new WebClient(getBrowserVersion(), defaultProxyHost, defaultProxyPort)) {
            final MockWebConnection webConnection = new MockWebConnection();
            webConnection.setResponse(URL_FIRST, html);
            webConnection.setResponse(new URL(URL_FIRST, "foo.js"), "", "text/javascript");
            webClient.setWebConnection(webConnection);

            // Make sure default proxy settings are used.
            webClient.getPage(URL_FIRST);
            assertEquals(defaultProxyHost, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(defaultProxyPort, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());

            // Make sure proxy bypass works with default proxy settings.
            webClient.getOptions().getProxyConfig().addHostsToProxyBypass(URL_FIRST.getHost());
            webClient.getPage(URL_FIRST);
            assertNull(webConnection.getLastWebRequest().getProxyHost());
            assertEquals(0, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());

            // Make sure we can remove proxy bypass filters.
            webClient.getOptions().getProxyConfig().removeHostsFromProxyBypass(URL_FIRST.getHost());
            webClient.getPage(URL_FIRST);
            assertEquals(defaultProxyHost, webConnection.getLastWebRequest().getProxyHost());
            assertEquals(defaultProxyPort, webConnection.getLastWebRequest().getProxyPort());
            assertNull(webConnection.getLastWebRequest().getProxyScheme());
            assertFalse(webConnection.getLastWebRequest().isSocksProxy());
        }
    }

    /**
     * Test {@link WebClient#expandUrl(URL,String)} for the case where an anchor name
     * was specified.
     * @throws Exception if the test fails
     */
    @Test
    public void expandUrl() throws Exception {
        final String prefix = URL_FIRST.toExternalForm();
        assertEquals(prefix + "#second", WebClient.expandUrl(URL_FIRST, "#second"));
        assertEquals(prefix + "?a=1&b=2", WebClient.expandUrl(new URL(prefix + "?a=1&b=2"), ""));
        assertEquals(prefix + "?b=2&c=3", WebClient.expandUrl(new URL(prefix + "?a=1&b=2"), "?b=2&c=3"));
        assertEquals("file:/home/myself/test.js",
                WebClient.expandUrl(new URL("file:/home/myself/myTest.html"), "test.js"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void expandUrlWithFile() throws Exception {
        final String urlString = "http://host/page.html";
        final URL url = new URL(urlString);
        assertEquals(urlString + "#second", WebClient.expandUrl(url, "#second"));
    }

    /** Test the accessors for refreshHandler. */
    @Test
    public void refreshHandlerAccessors() {
        final WebClient webClient = getWebClient();
        assertTrue(ImmediateRefreshHandler.class.isInstance(webClient.getRefreshHandler()));

        final RefreshHandler handler = new ImmediateRefreshHandler();
        webClient.setRefreshHandler(handler);
        assertSame(handler, webClient.getRefreshHandler());
    }

    /**
     * Apparently if the browsers receive a charset that they don't understand, they ignore
     * it and assume ISO-8895-1. Ensure we do the same.
     * @throws Exception if the test fails
     */
    @Test
    public void badCharset() throws Exception {
        final String page1Content = "<html><head><title>foo</title>\n"
                + "</head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, page1Content, "text/html; charset=garbage");

        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertTrue(HtmlPage.class.isInstance(page));
    }

    /**
     * Colons are legal in the path of a URL but {@link WebClient#expandUrl(URL,String)} was
     * blowing up on this case. Ensure it's fixed.
     * @throws Exception if the test fails
     */
    @Test
    public void expandUrlHandlesColonsInRelativeUrl() throws Exception {
        final URL newUrl = WebClient.expandUrl(new URL("http://host/foo"), "/bar/blah:de:blah");
        assertEquals("http://host/bar/blah:de:blah", newUrl);
    }

    /**
     * Test reuse of a single {@link HtmlPage} object to submit the same form multiple times.
     * @throws Exception if test fails
     */
    @Test
    public void reusingHtmlPageToSubmitFormMultipleTimes() throws Exception {
        final String firstContent = "<html><head><title>First</title></head>\n"
                + "<body onload='document.myform.mysubmit.focus()'>\n"
                + "<form action='" + URL_SECOND + "' name='myform'>\n"
                + "<input type='submit' name='mysubmit'>\n"
                + "</form></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body>Second</body></html>";

        final WebClient webClient = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        for (int i = 0; i < 100; i++) {
            final HtmlElement button = page.getFormByName("myform").getInputByName("mysubmit");
            button.click();
        }
    }

    /**
     * Test the value of window.opener when a link has target="_top".
     * @throws Exception if test fails
     */
    @Test
    public void openerInFrameset() throws Exception {
        final String firstContent = "<html><head><script>alert(window.opener)</script><frameset cols='*'>\n"
                + "<frame src='" + URL_SECOND + "'>\n"
                + "</frameset>\n"
                + "</html>";
        final String secondContent = "<html><body><a href='" + URL_FIRST + "' target='_top'>to top</a></body></html>";

        final WebClient webClient = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlPage pageInFrame = (HtmlPage) ((WebWindow) page.getFrames().get(0)).getEnclosedPage();
        pageInFrame.getAnchors().get(0).click();

        final String[] expectedAlerts = {"null", "null"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void guessContentType() throws Exception {
        final WebClient c = getWebClient();

        // tests empty files, type should be determined from file suffix
        assertEquals("empty.png", MimeType.IMAGE_PNG, c.guessContentType(getTestFile("empty.png")));
        assertEquals("empty.jpg", MimeType.IMAGE_JPEG, c.guessContentType(getTestFile("empty.jpg")));
        assertEquals("empty.gif", MimeType.IMAGE_GIF, c.guessContentType(getTestFile("empty.gif")));
        assertEquals("empty.js", MimeType.APPLICATION_JAVASCRIPT, c.guessContentType(getTestFile("empty.js")));
        assertEquals("empty.css", "text/css", c.guessContentType(getTestFile("empty.css")));

        // test real files with bad file suffix
        assertEquals("tiny-png.img", MimeType.IMAGE_PNG, c.guessContentType(getTestFile("tiny-png.img")));
        assertEquals("tiny-jpg.img", MimeType.IMAGE_JPEG, c.guessContentType(getTestFile("tiny-jpg.img")));
        assertEquals("tiny-gif.img", MimeType.IMAGE_GIF, c.guessContentType(getTestFile("tiny-gif.img")));

        // tests XHTML files, types will be determined based on a mixture of file suffixes and contents
        // note that "xhtml.php" returns content type "text/xml" in Firefox, but "application/xml" is good enough...
        assertEquals("xhtml.php", "application/xml", c.guessContentType(getTestFile("xhtml.php")));
        assertEquals("xhtml.htm", MimeType.TEXT_HTML, c.guessContentType(getTestFile("xhtml.htm")));
        assertEquals("xhtml.html", MimeType.TEXT_HTML, c.guessContentType(getTestFile("xhtml.html")));
        assertEquals("xhtml.xhtml", MimeType.APPLICATION_XHTML, c.guessContentType(getTestFile("xhtml.xhtml")));
    }

    /**
     * Test that no encoding disturb file reads from filesystem.
     * For instance this test failed under Linux with LANG=de_DE.UTF-8 or LANG=C
     * but worked for LANG=de_DE.ISO-8859-1
     * @throws Exception if the test fails
     */
    @Test
    public void binaryFileFromFileSystem() throws Exception {
        final String testfileName = "tiny-jpg.img";
        final File testfile = getTestFile(testfileName);
        final byte[] directBytes = IOUtils.toByteArray(new FileInputStream(testfile));
        final String directStr = hexRepresentation(directBytes);
        final WebClient client = getWebClient();
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
        final StringBuilder hexString = new StringBuilder();
        for (final byte b : digest) {
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
     * @throws Exception if something goes wrong
     */
    @Test
    public void requestHeader() throws Exception {
        final String content = "<html></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
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
     * Request - Client - Default Headers.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void requestHeaderOverwritesClient() throws Exception {
        final String fromRequest = "from request";
        final String fromClient = "from client";

        final String content = "<html></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        final WebRequest wr = new WebRequest(URL_FIRST);
        client .getPage(wr);
        assertNotEquals(fromRequest, webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));
        assertNotEquals(fromClient, webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));

        // request overwrites default
        wr.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, fromRequest);
        client .getPage(wr);
        assertEquals(fromRequest, webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));
        assertNotEquals(fromClient, webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));

        // request overwrites client
        client.addRequestHeader(HttpHeader.ACCEPT_LANGUAGE, fromClient);
        client .getPage(wr);
        assertEquals(fromRequest, webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));
        assertNotEquals(fromClient, webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));
    }

    /**
     * Request - Client - Default Headers.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void clientHeaderOverwritesDefault() throws Exception {
        final String fromClient = "from client";

        final String content = "<html></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        client .getPage(URL_FIRST);
        assertNotEquals(fromClient, webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));

        // client overwrites default
        client.addRequestHeader(HttpHeader.ACCEPT_LANGUAGE, fromClient);
        client .getPage(URL_FIRST);
        assertEquals(fromClient, webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void requestHeaderDoNotOverwriteWebRequestAcceptHeader() throws Exception {
        final String content = "<html></html>";
        final WebClient webClient = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content);
        webClient.setWebConnection(webConnection);

        // default accept header
        webClient.getPage(URL_FIRST);
        assertNotNull(webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));
        assertNotEquals("application/pdf", webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));

        // request with accept header
        final WebRequest wr = new WebRequest(URL_FIRST, "application/pdf",
                                    webClient.getBrowserVersion().getAcceptEncodingHeader());
        webClient.getPage(wr);
        assertEquals("application/pdf", webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));

        // request has an accept header use the one from the request
        webClient.addRequestHeader(HttpHeader.ACCEPT, MimeType.IMAGE_PNG);
        webClient.getPage(wr);
        assertEquals("application/pdf", webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));

        // request has no longer an accept header use the one from the client
        wr.removeAdditionalHeader(HttpHeader.ACCEPT);
        webClient.getPage(wr);
        assertEquals("image/png", webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void requestHeaderDoNotOverwriteWebRequestAcceptHeader2() throws Exception {
        final String content = "<html></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        // default accept header
        client.getPage(URL_FIRST);
        assertNotNull(webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));
        assertNotEquals("application/pdf", webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));

        // request with accept header
        final WebRequest wr = new WebRequest(URL_FIRST, HttpMethod.GET);
        wr.setAdditionalHeader(HttpHeader.ACCEPT, "application/pdf");
        client.getPage(wr);
        assertEquals("application/pdf", webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));

        // request has an accept header use the one from the request
        client.addRequestHeader(HttpHeader.ACCEPT, "image/png");
        client.getPage(wr);
        assertEquals("application/pdf", webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));

        // request has no longer an accept header use the one from the client
        wr.removeAdditionalHeader(HttpHeader.ACCEPT);
        client.getPage(wr);
        assertEquals("image/png", webConnection.getLastAdditionalHeaders().get(HttpHeader.ACCEPT));
    }

    /**
     * Test that content type is looked in a case insensitive way.
     * Cf <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>:
     * "All media type values, subtype values, and parameter names as defined
     * are case-insensitive".
     * @throws Exception if something goes wrong
     */
    @Test
    public void contentTypeCaseInsensitive() throws Exception {
        final String content = "<html><head>\n"
                + "<script type='Text/Javascript' src='foo.js'></script>\n"
                + "</head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse("alert('foo')", 200, "OK", "Text/Javascript");
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final String[] expectedAlerts = {"foo"};

        webConnection.setResponse(URL_FIRST, content, "Text/Html");
        assertTrue(HtmlPage.class.isInstance(client.getPage(URL_FIRST)));
        assertEquals(expectedAlerts, collectedAlerts);

        webConnection.setResponse(URL_FIRST, content, "Text/Xml");
        assertTrue(XmlPage.class.isInstance(client.getPage(URL_FIRST)));
        webConnection.setResponse(URL_FIRST, content, "ApplicaTion/Xml");
        assertTrue(XmlPage.class.isInstance(client.getPage(URL_FIRST)));

        webConnection.setResponse(URL_FIRST, content, MimeType.TEXT_PLAIN);
        assertTrue(TextPage.class.isInstance(client.getPage(URL_FIRST)));

        webConnection.setResponse(URL_FIRST, "", "Text/JavaScript");
        assertTrue(HtmlPage.class.isInstance(client.getPage(URL_FIRST)));
    }

    /**
     * Load a JavaScript function from an external file using src references
     * inside a script element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void loadFilePageWithExternalJS() throws Exception {
        final File currentDirectory = new File((new File("")).getAbsolutePath());

        final String encoding = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();

        // JavaScript file
        final File tmpFileJS = File.createTempFile("test", ".js", currentDirectory);
        tmpFileJS.deleteOnExit();
        FileUtils.writeStringToFile(tmpFileJS, "alert('foo')", encoding);

        // HTML file
        final String html = "<html><head></head><body>\n"
                + "<script language='javascript' type='text/javascript' src='" + tmpFileJS.getName() + "'></script>\n"
                + "</body></html>";
        final File tmpFile = File.createTempFile("test", ".html", currentDirectory);
        tmpFile.deleteOnExit();
        FileUtils.writeStringToFile(tmpFile, html, encoding);

        final URL fileURL = new URL("file://" + tmpFile.getCanonicalPath());
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        webClient.getPage(fileURL);

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that {@link WebClient#getPage(WebWindow, WebRequest)} calls OnBeforeUnload
     * on the specified window's page, not on the client's "current" page.
     * @throws Exception if an error occurs
     */
    @Test
    public void onBeforeUnloadCalledOnCorrectPage() throws Exception {
        final String html = "<html><body onbeforeunload='alert(7)'><iframe></iframe></body></html>";
        final List<String> alerts = new ArrayList<>();
        loadPage(html, alerts);
        assertTrue(alerts.isEmpty());
    }

    /**
     * Verifies that URLs are automatically encoded before being sent to the server, like
     * regular browsers do (verified by sniffing HTTP headers).
     * @throws Exception if an error occurs
     */
    @Test
    public void urlEncoding() throws Exception {
        final URL url = new URL("http://host/x+y\u00E9/a\u00E9 b?c \u00E9 d");
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX, "<html></html>", new ArrayList<String>(), url);
        final WebRequest wrs = page.getWebResponse().getWebRequest();
        assertEquals("http://host/x+y%C3%A9/a%C3%A9%20b?c%20%C3%A9%20d", wrs.getUrl());
    }

    /**
     * Verifies that URLs are automatically encoded before being sent to the server, like
     * regular browsers do (verified by sniffing HTTP headers).
     * @throws Exception if an error occurs
     */
    @Test
    public void urlEncoding2() throws Exception {
        final URL url = new URL("http://host/x+y\u00E9/a\u00E9 b?c \u00E9 d");
        final HtmlPage page = loadPage(INTERNET_EXPLORER, "<html></html>", new ArrayList<String>(), url);
        final WebRequest wrs = page.getWebResponse().getWebRequest();
        assertEquals("http://host/x+y%C3%A9/a%C3%A9%20b?c%20\u00E9%20d", wrs.getUrl());
    }

    /**
     * Test that '+' is not encoded in URLs.
     * @throws Exception if the test fails
     */
    @Test
    public void plusNotEncodedInUrl() throws Exception {
        final URL url = new URL("http://host/search/my+category/");
        final HtmlPage page = loadPage("<html></html>", new ArrayList<String>(), url);
        final WebRequest wrs = page.getWebResponse().getWebRequest();
        assertEquals("http://host/search/my+category/", wrs.getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void cssEnablementControlsCssLoading() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);

        final String html =
                "<html>\n"
                        + "  <head>\n"
                        + "    <link href='" + URL_SECOND + "' rel='stylesheet'></link>\n"
                        + "  </head>\n"
                        + "  <body onload='alert(document.styleSheets.length)'>\n"
                        + "    <div>abc</div>\n"
                        + "  </body>\n"
                        + "</html>";
        conn.setResponse(URL_FIRST, html);

        final String css = ".foo { color: green; }";
        conn.setResponse(URL_SECOND, css, 200, "OK", MimeType.TEXT_CSS, new ArrayList<NameValuePair>());

        final List<String> actual = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        client.getPage(URL_FIRST);
        assertEquals(new String[]{"1"}, actual);

        actual.clear();
        client.getOptions().setCssEnabled(false);
        client.getPage(URL_FIRST);
        assertEquals(new String[]{"0"}, actual);

        actual.clear();
        client.getOptions().setCssEnabled(true);
        client.getPage(URL_FIRST);
        assertEquals(new String[]{"1"}, actual);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void getPageJavascriptProtocol() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse("<html><head><title>Hello World</title></head><body></body></html>");
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        Page page = webClient.getPage("javascript:void(alert(document.location))");
        assertEquals("about:blank", page.getUrl());
        assertEquals(new String[] {"about:blank"}, collectedAlerts);
        collectedAlerts.clear();

        page = webClient.getPage(URL_FIRST);
        final Page page2 = webClient.getPage("javascript:void(alert(document.title))");
        assertSame(page, page2);
        assertEquals(new String[] {"Hello World"}, collectedAlerts);

        webClient.getPage("javascript:void(document.body.setAttribute('foo', window.screen.availWidth))");
        assertEquals("1920", ((HtmlPage) page).getBody().getAttribute("foo"));
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void getPageJavascriptProtocolTextPage() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse("some text", "plain/text");
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        Page page = webClient.getPage(URL_FIRST);

        page = webClient.getPage("javascript:void(alert(document.location))");
        assertEquals("about:blank", page.getUrl());
        assertEquals(new String[] {"about:blank"}, collectedAlerts);
        collectedAlerts.clear();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void javaScriptTimeout() throws Exception {
        final WebClient client = getWebClient();
        final long timeout = 2000;
        final long oldTimeout = client.getJavaScriptTimeout();
        client.setJavaScriptTimeout(timeout);

        try {
            client.getOptions().setThrowExceptionOnScriptError(false);

            final String content = "<html><body><script>while(1) {}</script></body></html>";
            final MockWebConnection webConnection = new MockWebConnection();
            webConnection.setDefaultResponse(content);
            client.setWebConnection(webConnection);

            final Exception[] exceptions = {null};
            final Thread runner = new Thread() {
                @Override
                public void run() {
                    try {
                        client.getPage(URL_FIRST);
                    }
                    catch (final Exception e) {
                        exceptions[0] = e;
                    }
                }
            };

            runner.start();

            runner.join(timeout * 2);
            if (runner.isAlive()) {
                runner.interrupt();
                fail("Script was still running after timeout");
            }
            assertNull(exceptions[0]);
        }
        finally {
            client.setJavaScriptTimeout(oldTimeout);
        }
    }

    /**
     * Protects against the regression detailed in bug 1975445.
     * @throws Exception if an error occurs
     */
    @Test
    public void openWindowWithNullUrl() throws Exception {
        final WebClient client = getWebClient();
        final WebWindow window = client.openWindow(null, "TestingWindow");
        assertNotNull(window);
    }

    /**
     * Basic window tracking testing.
     * @throws Exception if an error occurs
     */
    @Test
    public void basicWindowTracking() throws Exception {
        // Create mock web connection.
        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse("<html></html");

        // Make sure a new client start with a single window.
        final WebClient client = getWebClient();
        client.setWebConnection(conn);
        assertEquals(1, client.getWebWindows().size());

        // Make sure the initial window is the current window.
        final WebWindow window1 = client.getCurrentWindow();
        assertSame(window1, client.getCurrentWindow());
        assertNotNull(window1);

        // Make sure that we keep track of a new window when we open it.
        final WebWindow window2 = client.openWindow(URL_FIRST, "blah");
        assertSame(window2, client.getCurrentWindow());
        assertEquals(2, client.getWebWindows().size());
        assertNotNull(window2);

        // Make sure that we keep track of another new window when we open it.
        final WebWindow window3 = client.openWindow(URL_SECOND, "foo");
        assertSame(window3, client.getCurrentWindow());
        assertEquals(3, client.getWebWindows().size());
        assertNotNull(window3);

        // Close the last window, make sure that the second window becomes the current window.
        ((TopLevelWindow) window3).close();
        assertSame(window2, client.getCurrentWindow());
        assertEquals(2, client.getWebWindows().size());

        // Close the first window, make sure that the second window is still the current window.
        ((TopLevelWindow) window1).close();
        assertSame(window2, client.getCurrentWindow());
        assertEquals(1, client.getWebWindows().size());

        // Close the only remaining window, make sure the client still has a current window.
        ((TopLevelWindow) window2).close();
        assertNotNull(client.getCurrentWindow());
        assertNotSame(window1, client.getCurrentWindow());
        assertNotSame(window2, client.getCurrentWindow());
        assertNotSame(window3, client.getCurrentWindow());
        assertEquals(1, client.getWebWindows().size());
    }

    /**
     * Previous window should become current window after current window is closed in onLoad event.
     * @throws Exception if an error occurs
     */
    @Test
    public void windowTracking_SpecialCase1() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection conn = new MockWebConnection();

        final String html1 = "<html><head><title>First</title></head>\n"
                + "<body><form name='form1'>\n"
                + "<button id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</button>\n"
                + "</form></body></html>";
        conn.setResponse(URL_FIRST, html1);

        final String html2 = "<html><head><title>Second</title></head>\n"
                + "<body onload='doTest()'>\n"
                + "<script>\n"
                + "  function doTest() {\n"
                + "    window.close();\n"
                + "  }\n"
                + "</script></body></html>";
        conn.setDefaultResponse(html2);

        webClient.setWebConnection(conn);
        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        buttonA.click();
        assertNotNull(webClient.getCurrentWindow().getEnclosedPage());
        assertEquals("First", ((HtmlPage) webClient.getCurrentWindow().getEnclosedPage()).getTitleText());
    }

    /**
     * Previous window should become current window after current window is closed while loading the page.
     * @throws Exception if an error occurs
     */
    @Test
    public void windowTracking_SpecialCase2() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection conn = new MockWebConnection();

        final String html1 = "<html><head><title>First</title></head>\n"
                + "<body><form name='form1'>\n"
                + "<button id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</button>\n"
                + "</form></body></html>";
        conn.setResponse(URL_FIRST, html1);

        final String html2 = "<html><head><title>Third</title>\n"
                + "<script type=\"text/javascript\">\n"
                + "     window.close();\n"
                + "</script></head></html>";
        conn.setDefaultResponse(html2);

        webClient.setWebConnection(conn);
        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        buttonA.click();
        assertNotNull(webClient.getCurrentWindow().getEnclosedPage());
        assertEquals("First", ((HtmlPage) webClient.getCurrentWindow().getEnclosedPage()).getTitleText());
    }

    /**
     * Previous window should become current window after current window is closed.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "Third page loaded")
    public void windowTracking_SpecialCase3() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String html1 = "<html><head><title>First</title></head>\n"
                + "<body>\n"
                + "<button id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</button>\n"
                + "</body></html>";
        conn.setResponse(URL_FIRST, html1);

        final String html2 = "<html><head><title>Second</title></head>\n"
                + "<body onUnload='doTest()'>\n"
                + "<form name='form1' action='" + URL_THIRD + "'>\n"
                + "<button id='clickme' type='button' onclick='postBack();'>Submit</button></form>\n"
                + "<script>\n"
                + "    function doTest() {\n"
                + "      window.close();\n"
                + "    }\n"
                + "    function postBack() {\n"
                + "      var frm = document.forms[0];\n"
                + "      frm.submit();\n"
                + "    }\n"
                + "</script></body></html>";
        conn.setResponse(URL_SECOND, html2);

        final String html3 = "<html><head><title>Third</title>\n"
                + "<script type=\"text/javascript\">\n"
                + "     alert('Third page loaded');\n"
                + "     window.close();\n"
                + "</script></head></html>";
        conn.setResponse(URL_THIRD, html3);
        conn.setDefaultResponse(html3);

        webClient.setWebConnection(conn);
        final HtmlPage firstPage = webClient.getPage(URL_FIRST);

        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        buttonA.click();
        final HtmlPage secondPage = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
        assertEquals("Second", secondPage.getTitleText());

        final HtmlButton buttonB = secondPage.getHtmlElementById("clickme");
        buttonB.click();
        assertEquals("First", ((HtmlPage) webClient.getCurrentWindow().getEnclosedPage()).getTitleText());
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Bug 2890847: Triggering the creation of an empty frame based on some user action should not
     * make the empty frame the current window.
     * @throws Exception if an error occurs
     */
    @Test
    public void windowTracking_SpecialCase4() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);

        final String html = "<html><head><title>Test</title></head><body>\n"
                + "<div id='d' onclick='this.innerHTML+=\"<iframe></iframe>\";'>go</div></body></html>";
        conn.setResponse(URL_FIRST, html);

        final HtmlPage page = client.getPage(URL_FIRST);
        page.getHtmlElementById("d").click();
        assertEquals("Test", ((HtmlPage) client.getCurrentWindow().getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void openWindowWithAboutBlank() throws Exception {
        final WebClient client = getWebClient();
        final WebWindow window = client.openWindow(UrlUtils.URL_ABOUT_BLANK, "TestingWindow");
        assertNotNull(window);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void openWindowFromTextContent() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        client.setWebConnection(webConnection);
        webConnection.setResponse(URL_FIRST, "some text", MimeType.TEXT_PLAIN);

        client.getPage(URL_FIRST);
        final WebWindow window = client.openWindow(URL_FIRST, "TestingWindow");
        assertNotNull(window);
        assertTrue(window.getEnclosedPage().toString(), window.getEnclosedPage() instanceof TextPage);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void cssErrorHandler() throws Exception {
        final WebClient client = getWebClient();
        assertTrue(client.getCssErrorHandler() instanceof DefaultCssErrorHandler);

        final MutableInt fatals = new MutableInt();
        final MutableInt errors = new MutableInt();
        final MutableInt warnings = new MutableInt();
        final StringBuilder errorUri = new StringBuilder();
        final CSSErrorHandler handler = new CSSErrorHandler() {
            @Override
            public void warning(final CSSParseException exception) throws CSSException {
                warnings.increment();
            }
            @Override
            public void fatalError(final CSSParseException exception) throws CSSException {
                fatals.increment();
            }
            @Override
            public void error(final CSSParseException exception) throws CSSException {
                errors.increment();
                errorUri.append(exception.getURI());
            }
        };
        client.setCssErrorHandler(handler);
        assertEquals(handler, client.getCssErrorHandler());

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, "<html><body><style></style></body></html>");
        conn.setResponse(URL_SECOND, "<html><body><style>.x{color:red;}</style></body></html>");
        conn.setResponse(URL_THIRD, "<html><body><style>.x{color{}}}</style></body></html>");
        client.setWebConnection(conn);

        final HtmlPage page1 = client.getPage(URL_FIRST);
        ((HTMLStyleElement) page1.getBody().getFirstChild().getScriptableObject()).getSheet();
        assertEquals(0, warnings.intValue());
        assertEquals(0, errors.intValue());
        assertEquals(0, fatals.intValue());

        final HtmlPage page2 = client.getPage(URL_SECOND);
        ((HTMLStyleElement) page2.getBody().getFirstChild().getScriptableObject()).getSheet();
        assertEquals(0, warnings.intValue());
        assertEquals(0, errors.intValue());
        assertEquals(0, fatals.intValue());

        final HtmlPage page3 = client.getPage(URL_THIRD);
        ((HTMLStyleElement) page3.getBody().getFirstChild().getScriptableObject()).getSheet();
        assertEquals(1, warnings.intValue());
        assertEquals(2, errors.intValue());
        assertEquals(0, fatals.intValue());
        assertEquals("http://127.0.0.1:" + PORT + "/third/http://127.0.0.1:" + PORT + "/third/", errorUri.toString());
    }

    /**
     * Tests that the JavaScript parent scope is set correctly when shuffling windows around.
     * @throws Exception if test fails
     */
    @Test
    public void maintainJavaScriptParentScope() throws Exception {
        final String basicContent = "<html><head>\n"
                + "<title>basicContentTitle</title>\n"
                + "</head><body>\n"
                + "<p>Hello World</p>\n"
                + "</body></html>";

        final String jsContent = "<html><head>\n"
                + "<title>jsContentTitle</title>\n"
                + "<script>function foo() {alert('Ran Here')}</script>\n"
                + "<script>function bar() {}</script>\n"
                + "</head><body onload='bar()'>\n"
                + "<input type='button' id='button' onclick='foo()'/>"
                + "</body></html>";

        final HtmlPage jsPage = loadPage(jsContent);
        final WebClient webClient = jsPage.getWebClient();
        final WebWindow firstWindow = webClient.getCurrentWindow();
        getMockConnection(jsPage).setResponse(URL_SECOND, basicContent);

        final CollectingAlertHandler alertHandler = new CollectingAlertHandler();
        webClient.setAlertHandler(alertHandler);

        final HtmlButtonInput buttonBefore = jsPage.getHtmlElementById("button");

        final WebWindow secondWindow = webClient.openWindow(null, "second window");
        webClient.setCurrentWindow(secondWindow);
        webClient.getPage(URL_SECOND);

        webClient.setCurrentWindow(firstWindow);

        final HtmlPage currentPage = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
        final HtmlButtonInput buttonAfter = currentPage.getHtmlElementById("button");
        assertSame(buttonBefore, buttonAfter);

        buttonAfter.click();

        assertEquals(1, alertHandler.getCollectedAlerts().size());
        assertEquals("Ran Here", alertHandler.getCollectedAlerts().get(0));
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void currentWindow() throws Exception {
        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        final String html = "<html><body onload='document.getElementById(\"f\").src=\"frame.html\";'>\n"
                + "<iframe id='f'></iframe></body></html>";
        conn.setResponse(URL_FIRST, html);
        final URL frameUrl = new URL(URL_FIRST, "frame.html");
        conn.setResponse(frameUrl, "<html><body></body></html>");
        conn.setResponse(URL_SECOND, "<html><body></body></html>");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(2, client.getWebWindows().size());
        assertEquals(frameUrl,
                client.getCurrentWindow().getEnclosedPage().getUrl());

        // loading a new page should be done in the top window
        client.getPage(URL_SECOND);
        assertTrue(client.getCurrentWindow() instanceof TopLevelWindow);
        assertEquals(1, client.getWebWindows().size());
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void currentWindow2() throws Exception {
        final String html = "<html><head><script>\n"
                + "function createFrame() {\n"
                + "  var f = document.createElement('iframe');\n"
                + "  f.setAttribute('style', 'width: 0pt; height: 0pt');\n"
                + "  document.body.appendChild(f);\n"
                + "  f.src = \"javascript:''\";\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='setTimeout(createFrame, 10)'></body></html>";

        final HtmlPage page = loadPage(html);
        assertTrue(page.getEnclosingWindow() instanceof TopLevelWindow);
        page.getWebClient().waitForBackgroundJavaScriptStartingBefore(1000);

        assertSame(page.getEnclosingWindow(), page.getWebClient().getCurrentWindow());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void getTopLevelWindows() throws Exception {
        @SuppressWarnings("resource")
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, "<html><body><iframe></iframe></body></html>");
        conn.setResponse(URL_SECOND, "<html><body></body></html>");
        client.setWebConnection(conn);

        final WebWindow firstWindow = client.getWebWindows().get(0);

        assertEquals(1, client.getWebWindows().size());
        assertEquals(1, client.getTopLevelWindows().size());
        assertEquals(client.getCurrentWindow(), client.getWebWindows().get(0));
        assertEquals(client.getCurrentWindow(), client.getTopLevelWindows().get(0));
        assertEquals(firstWindow, client.getWebWindows().get(0));
        assertEquals(firstWindow, client.getTopLevelWindows().get(0));

        client.getPage(URL_FIRST);

        assertEquals(2, client.getWebWindows().size());
        assertEquals(1, client.getTopLevelWindows().size());
        assertEquals(client.getCurrentWindow(), client.getWebWindows().get(0));
        assertEquals(client.getCurrentWindow(), client.getTopLevelWindows().get(0));
        assertEquals(firstWindow, client.getWebWindows().get(0));
        assertEquals(firstWindow, client.getTopLevelWindows().get(0));

        client.getPage(URL_SECOND);

        assertEquals(1, client.getWebWindows().size());
        assertEquals(1, client.getTopLevelWindows().size());
        assertEquals(client.getCurrentWindow(), client.getWebWindows().get(0));
        assertEquals(client.getCurrentWindow(), client.getTopLevelWindows().get(0));
        assertEquals(firstWindow, client.getWebWindows().get(0));
        assertEquals(firstWindow, client.getTopLevelWindows().get(0));

        client.openWindow(URL_SECOND, "a");

        assertEquals(2, client.getWebWindows().size());
        assertEquals(2, client.getTopLevelWindows().size());
        assertEquals(client.getCurrentWindow(), client.getWebWindows().get(1));
        assertEquals(client.getCurrentWindow(), client.getTopLevelWindows().get(1));
        assertEquals(client.getWebWindows().get(1), client.getTopLevelWindows().get(1));
        assertEquals(firstWindow, client.getWebWindows().get(0));
        assertEquals(firstWindow, client.getTopLevelWindows().get(0));
        assertNotEquals(firstWindow, client.getWebWindows().get(1));
        assertNotEquals(firstWindow, client.getTopLevelWindows().get(1));

        client.openWindow(URL_SECOND, "b");

        assertEquals(3, client.getWebWindows().size());
        assertEquals(3, client.getTopLevelWindows().size());
        assertEquals(client.getCurrentWindow(), client.getWebWindows().get(2));
        assertEquals(client.getCurrentWindow(), client.getTopLevelWindows().get(2));
        assertEquals(firstWindow, client.getWebWindows().get(0));
        assertEquals(firstWindow, client.getTopLevelWindows().get(0));
        assertEquals(client.getWebWindows().get(1), client.getTopLevelWindows().get(1));
        assertNotEquals(firstWindow, client.getWebWindows().get(1));
        assertNotEquals(firstWindow, client.getTopLevelWindows().get(1));
        assertEquals(client.getWebWindows().get(2), client.getTopLevelWindows().get(2));
        assertNotEquals(firstWindow, client.getWebWindows().get(2));
        assertNotEquals(firstWindow, client.getTopLevelWindows().get(2));

        client.close();

        assertEquals(1, client.getWebWindows().size());
        assertEquals(1, client.getTopLevelWindows().size());
    }

    /**
     * Test that the result of getTopLevelWindows() is usable without
     * getting a ConcurrentModificationException.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void getTopLevelWindowsJSConcurrency() throws Exception {
        final String html = "<html><head><title>Toplevel</title></head>\n<body>\n"
                + "<script>\n"
                + "  setInterval(function() {\n"
                + "    window.open('');\n"
                + "  }, 10);\n"
                + "</script>\n"
                + "</body></html>\n";

        final WebClient client = getWebClientWithMockWebConnection();
        getMockWebConnection().setResponse(URL_FIRST, html);

        client.getPage(URL_FIRST);
        final List<TopLevelWindow> windows = client.getTopLevelWindows();
        for (int i = 0; i < 100; i++) {
            for (final TopLevelWindow window : windows) {
                Thread.sleep(13);
                window.getName();
            }
        }
    }

    /**
     * Regression test for Bug #861.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void urlWithDirectoryUp() throws Exception {
        final URL url = new URL("http://htmlunit.sf.net/foo.html");
        final URL urlWithDirectoryUp = new URL("http://htmlunit.sf.net/bla/../foo.html");

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(url, "");
        client.setWebConnection(webConnection);

        final Page page = client.getPage(urlWithDirectoryUp);
        assertEquals(url, page.getUrl());
    }

    /**
     * Test that close() stops all threads. This wasn't the case as
     * of HtmlUnit-2.7-SNAPSHOT 11.12.2009.
     * @throws Exception if test fails
     */
    @Test
    public void close() throws Exception {
        final String html = "<html><head></head>\n"
                + "<body onload='setInterval(addFrame, 1)'>\n"
                + "<iframe src='second.html'></iframe>\n"
                + "<script>\n"
                + "function addFrame() {\n"
                + "  var f = document.createElement('iframe');\n"
                + "  f.src = 'second.html';\n"
                + "  document.body.appendChild(f);\n"
                + "}\n"
                + "</script>\n"
                + "</body></html>";

        final String html2 = "<html><head><script>\n"
                + "function doSomething() {}\n"
                + "setInterval(doSomething, 100);\n"
                + "</script>\n"
                + "</head><body></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setDefaultResponse(html2);

        @SuppressWarnings("resource")
        final WebClient webClient = getWebClient();
        final int initialJSThreads = getJavaScriptThreads().size();
        webClient.setWebConnection(getMockWebConnection());
        webClient.getPage(URL_FIRST);

        int nbJSThreads = getJavaScriptThreads().size();
        final int nbNewJSThreads = nbJSThreads - initialJSThreads;
        assertTrue(nbNewJSThreads + " threads", nbNewJSThreads > 0);

        // close and verify that the WebClient is clean
        webClient.close();
        assertEquals(1, webClient.getWebWindows().size());
        nbJSThreads = getJavaScriptThreads().size();

        assertEquals(initialJSThreads, nbJSThreads);
    }

    /**
     * Tests that setThrowExceptionOnScriptError also works,
     * if an exception is thrown from onerror handler.
     * Regression test for bug 3534371.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        final String html = "<html><body>\n"
                + "<script type='application/javascript'>\n"
                + "  window.onerror = function() { foo.bar() };\n"
                + "  doit();\n"
                + "</script>\n"
                + "</body></html>";

        final WebClient webClient = getWebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        loadPage(html);
    }

    /**
     * Testcase for issue #1652.
     * @throws Exception if the test fails
     */
    @Test
    public void aboutBlankSharedRequest() throws Exception {
        final WebClient webClient = getWebClient();

        final WebWindow firstWindow = webClient.openWindow(UrlUtils.URL_ABOUT_BLANK, "Window 1");
        assertNotNull(firstWindow);

        final WebRequest firstRequest1 = firstWindow.getEnclosedPage().getWebResponse().getWebRequest();
        assertEquals("about:blank", firstRequest1.getUrl().toExternalForm());
        firstRequest1.setUrl(UrlUtils.toUrlSafe(UrlUtils.ABOUT_BLANK + "#anchor"));

        final WebWindow secondWindow = webClient.openWindow(UrlUtils.URL_ABOUT_BLANK, "Window 2");
        assertNotNull(secondWindow);
        final WebRequest secondRequest = secondWindow.getEnclosedPage().getWebResponse().getWebRequest();
        assertEquals("about:blank", secondRequest.getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void closeToClearCache() throws Exception {
        final Cache cache = createMock(Cache.class);
        try (WebClient webClient = getWebClient()) {
            webClient.setCache(cache);
            cache.clear();
            expectLastCall().atLeastOnce();

            replay(cache);
        }
        verify(cache);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void webSocketDisabled() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);

        final String html =
                "<html>\n"
                    + "  <head>\n"
                    + "    <script>alert('WebSocket' in window);</script>\n"
                    + "  </head>\n"
                    + "  <body>\n"
                    + "  </body>\n"
                    + "</html>";
        conn.setResponse(URL_FIRST, html);

        final List<String> actual = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        client.getPage(URL_FIRST);
        assertEquals(new String[]{"true"}, actual);

        actual.clear();
        client.getOptions().setWebSocketEnabled(false);
        client.getPage(URL_FIRST);
        assertEquals(new String[]{"false"}, actual);

        actual.clear();
        client.getOptions().setWebSocketEnabled(true);
        client.getPage(URL_FIRST);
        assertEquals(new String[]{"true"}, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void loadHtmlCodeIntoCurrentWindow() throws Exception {
        final String htmlCode = "<html>"
                + "  <head>"
                + "    <title>Title</title>"
                + "  </head>"
                + "  <body>"
                + "    content..."
                + "  </body>"
                + "</html> ";

        final WebClient client = getWebClient();
        final HtmlPage page = client.loadHtmlCodeIntoCurrentWindow(htmlCode);
        assertEquals("content...", page.getBody().asNormalizedText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void loadXHtmlCodeIntoCurrentWindow() throws Exception {
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

        final WebClient client = getWebClient();
        final HtmlPage page = client.loadXHtmlCodeIntoCurrentWindow(htmlCode);
        assertEquals("content...", page.getBody().asNormalizedText());
    }
}
