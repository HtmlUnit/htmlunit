/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.base.testing.EventCatcher;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 *  Tests for WebClient
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Chris Erskine
 */
public class WebClientTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public WebClientTest( final String name ) {
        super( name );
    }


    /**
     * Test the situation where credentials are required but they haven't been specified.
     *
     * @throws Exception If something goes wrong.
     */
    public void testCredentialProvider_NoCredentials() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "No access</body></html>";
        final WebClient client = new WebClient();
        client.setPrintContentOnFailingStatusCode( false );

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse(
            htmlContent, 401, "Credentials missing or just plain wrong", "text/plain" );
        client.setWebConnection( webConnection );

        try {
            client.getPage(
                    URL_GARGOYLE,
                    SubmitMethod.POST, Collections.EMPTY_LIST );
            fail( "Expected FailingHttpStatusCodeException" );
        }
        catch( final FailingHttpStatusCodeException e ) {
            assertEquals( 401, e.getStatusCode() );
        }
    }


    /**
     * Test that {@link WebClient#assertionFailed(String)} actually throws an exception.
     */
    public void testAssertionFailed() {
        final WebClient client = new WebClient();

        try {
            client.assertionFailed( "foobar" );
            fail( "Expected AssertionFailedError" );
        }
        catch( junit.framework.AssertionFailedError e ) {
            assertEquals( "foobar", e.getMessage() );
        }
    }


    /**
     * Test that the {@link WebWindowEvent#CHANGE} window event gets fired at the
     * appropriate time.
     * @throws Exception If something goes wrong.
     */
    public void testHtmlWindowEvents_changed() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>"
                 + "</body></html>";
        final WebClient client = new WebClient();
        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(client);

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = ( HtmlAnchor )firstPage.getHtmlElementById( "a2" );

        final List firstExpectedEvents = Arrays.asList( new Object[] {
            new WebWindowEvent(
                client.getCurrentWindow(), WebWindowEvent.CHANGE, null, firstPage),
        } );
        assertEquals( firstExpectedEvents, eventCatcher.getEvents() );

        eventCatcher.clear();
        final HtmlPage secondPage = ( HtmlPage )anchor.click();

        final List secondExpectedEvents = Arrays.asList( new Object[] {
            new WebWindowEvent(
                client.getCurrentWindow(), WebWindowEvent.CHANGE, firstPage, secondPage),
        } );
        assertEquals( secondExpectedEvents, eventCatcher.getEvents() );
    }


    /**
     * Test that the {@link WebWindowEvent#OPEN} window event gets fired at
     * the appropriate time.
     * @throws Exception If something goes wrong.
     */
    public void testHtmlWindowEvents_opened() throws Exception {
        final String page1Content
                 = "<html><head><title>foo</title>"
                 + "<script>window.open('http://page2', 'myNewWindow')</script>"
                 + "</head><body>"
                 + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>"
                 + "</body></html>";
        final String page2Content
                 = "<html><head><title>foo</title></head><body></body></html>";
        final WebClient client = new WebClient();
        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(client);

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(
            new URL("http://page1"), page1Content, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://page2"), page2Content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://page1" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", firstPage.getTitleText());

        final WebWindow firstWindow = client.getCurrentWindow();
        final WebWindow secondWindow = client.getWebWindowByName("myNewWindow");
        final List expectedEvents = Arrays.asList( new Object[] {
            new WebWindowEvent(
                secondWindow, WebWindowEvent.OPEN, null, null),
            new WebWindowEvent(
                secondWindow, WebWindowEvent.CHANGE, null, secondWindow.getEnclosedPage()),
            new WebWindowEvent(
                firstWindow, WebWindowEvent.CHANGE, null, firstPage),
        } );
        assertEquals( expectedEvents, eventCatcher.getEvents() );
    }


    /**
     * Test that the {@link WebWindowEvent#CLOSE} window event gets fired at
     * the appropriate time.
     * @throws Exception If something goes wrong.
     */
    public void testHtmlWindowEvents_closedFromFrame() throws Exception {
        final String firstContent
                 = "<html><head><title>first</title></head><body>"
                 + "<iframe src='http://third' id='frame1'>"
                 + "<a href='http://second' id='a2'>link to foo2</a>"
                 + "</body></html>";
        final String secondContent
                 = "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent
                 = "<html><head><title>third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
        assertEquals("first", firstPage.getTitleText());

        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(client);

        final HtmlInlineFrame frame = (HtmlInlineFrame)firstPage.getHtmlElementById("frame1");
        final HtmlPage thirdPage = (HtmlPage)frame.getEnclosedPage();

        // Load the second page
        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById( "a2" );
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertEquals("second", secondPage.getTitleText());

        final WebWindow firstWindow = client.getCurrentWindow();
        final List expectedEvents = Arrays.asList( new Object[] {
            new WebWindowEvent(
                frame.getEnclosedWindow(), WebWindowEvent.CLOSE, thirdPage, null),
            new WebWindowEvent(
                firstWindow, WebWindowEvent.CHANGE, firstPage, secondPage),
        } );
        assertEquals( expectedEvents.get(0), eventCatcher.getEvents().get(0) );
        assertEquals( expectedEvents, eventCatcher.getEvents() );
    }


    /**
     * Test a 301 redirection code where the original request was a GET.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection301_MovedPermanently_GetMethod() throws Exception {
        final int statusCode = 301;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     * From the http spec:  If the 301 status code is received in response
     * to a request other than GET or HEAD, the user agent MUST NOT automatically
     * redirect the request unless it can be confirmed by the user, since this
     * might change the conditions under which the request was issued.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection301_MovedPermanently_PostMethod() throws Exception {
        final int statusCode = 301;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = null;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
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

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     * Test a 302 redirection code.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection302_MovedTemporarily_GetMethod() throws Exception {
        final int statusCode = 302;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     * Tests a 303 redirection code.  This should be the same as a 302.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection303_SeeOther_GetMethod() throws Exception {
        final int statusCode = 303;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     * Tests a 303 redirection code - this should be the same as a 302.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection303_SeeOther_PostMethod() throws Exception {
        final int statusCode = 303;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     * Tests a 307 redirection code.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection307_TemporaryRedirect_GetMethod() throws Exception {
        final int statusCode = 307;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     * Tests a 307 redirection code.
     * @throws Exception If something goes wrong.
     */
    public void testRedirection307_TemporaryRedirect_PostMethod() throws Exception {
        final int statusCode = 307;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = null;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
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
            final SubmitMethod expectedRedirectedRequestMethod )
        throws
             Exception {

        final String firstContent = "<html><head><title>First</title></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setPrintContentOnFailingStatusCode(false);

        final List headers = Collections.singletonList(
            new KeyValuePair("Location", "http://second") );
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webConnection.setResponse(
            URL_FIRST, firstContent, statusCode,
            "Some error", "text/html", headers );
        webConnection.setResponse(
            URL_SECOND, secondContent, 200,
            "OK", "text/html", Collections.EMPTY_LIST );

        webClient.setWebConnection( webConnection );

        final URL url = URL_FIRST;

        HtmlPage page;
        WebResponse webResponse;

        //
        // Second time redirection is turned on (default setting)
        //
        page = (HtmlPage)webClient.getPage(url, initialRequestMethod, Collections.EMPTY_LIST);
        webResponse = page.getWebResponse();
        if( expectedRedirectedRequestMethod == null ) {
            // No redirect should have happened
            assertEquals( statusCode, webResponse.getStatusCode() );
            assertEquals( initialRequestMethod, webConnection.getLastMethod() );
        }
        else {
            // A redirect should have happened
            assertEquals( 200, webResponse.getStatusCode() );
            assertEquals( "Second", page.getTitleText() );
            assertEquals( expectedRedirectedRequestMethod, webConnection.getLastMethod() );
        }

        //
        // Second time redirection is turned off
        //
        webClient.setRedirectEnabled(false);
        page = (HtmlPage)webClient.getPage(url, initialRequestMethod, Collections.EMPTY_LIST);
        webResponse = page.getWebResponse();
        assertEquals( statusCode, webResponse.getStatusCode() );
        assertEquals( initialRequestMethod, webConnection.getLastMethod() );

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
        catch( final NullPointerException e ) {
            // expected path
        }
    }


    /**
     * Test {@link WebClient#setPageCreator(PageCreator)}.
     * @throws Exception If something goes wrong.
     */
    public void testSetPageCreator() throws Exception {
        final String page1Content
                 = "<html><head><title>foo</title>"
                 + "</head><body>"
                 + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(
            new URL("http://page1"), page1Content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );
        final List collectedPageCreationItems = new ArrayList();
        client.setPageCreator( new CollectingPageCreator(collectedPageCreationItems) );

        final Page page = client.getPage(
                new URL( "http://page1" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertTrue( "instanceof TextPage", page instanceof TextPage );

        final List expectedPageCreationItems = Arrays.asList( new Object[] {
            page
        } );

        assertEquals( expectedPageCreationItems, collectedPageCreationItems );
    }


    /** A PageCreator that collects data */
    private class CollectingPageCreator implements PageCreator {
        private final List collectedPages_;
        /**
         * Create an instance
         * @param list The list that will contain the data
         */
        public CollectingPageCreator( final List list ) {
            this.collectedPages_ = list;
        }
        /**
         * Create a page
         * @param webResponse The web response
         * @param webWindow The web window
         * @return The new page
         * @throws IOException If an IO problem occurs
         */
        public Page createPage( final WebResponse webResponse, final WebWindow webWindow )
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
                 = "<html><head><title>foo</title></head><body>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
            new URL( "http://first?a=b" ), SubmitMethod.POST, Collections.EMPTY_LIST );

        assertEquals(
            "http://first?a=b",
            page.getWebResponse().getUrl().toExternalForm() );
    }

    /**
     * Test that double / in query string are not changed.
     * @throws Exception If something goes wrong.
     */
    public void testLoadPage_SlashesInQueryString() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head>"
                 + "<body><a href='foo.html?id=UYIUYTY//YTYUY..F'>to page 2</a>"
                 + "</body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);
        
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlAnchor link = (HtmlAnchor) page.getAnchors().get(0);
        final Page page2 = link.click(); 
        assertEquals("http://first/foo.html?id=UYIUYTY//YTYUY..F", page2.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * Test that the query string is encoded to be valid.
     * @throws Exception If something goes wrong.
     */
    public void testLoadPage_EncodeQueryString() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "</body></html>";
        
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        // with query string not encoded
        HtmlPage page = (HtmlPage) client.getPage(new URL("http://first?a=b c&d=ιθ"));
        assertEquals(
            "http://first?a=b%20c&d=%C3%A9%C3%A8",
            page.getWebResponse().getUrl().toExternalForm());


        // with query string already encoded
        page = (HtmlPage) client.getPage(new URL("http://first?a=b%20c&d=%C3%A9%C3%A8"));
        assertEquals(
            "http://first?a=b%20c&d=%C3%A9%C3%A8",
            page.getWebResponse().getUrl().toExternalForm());
        
        // with query string partially encoded
        page = (HtmlPage) client.getPage(new URL("http://first?a=b%20c&d=e f"));
        assertEquals(
            "http://first?a=b%20c&d=e%20f",
            page.getWebResponse().getUrl().toExternalForm());
        
        // with anchor
        page = (HtmlPage) client.getPage(new URL("http://first?a=b c#myAnchor"));
        assertEquals(
            "http://first?a=b%20c#myAnchor",
            page.getWebResponse().getUrl().toExternalForm());
        
        // with query string containing encoded "&", "=", "+", ",", and "$"
        page = (HtmlPage) client.getPage(new URL("http://first?a=%26%3D%20%2C%24"));
        assertEquals(
            "http://first?a=%26%3D%20%2C%24",
            page.getWebResponse().getUrl().toExternalForm());        
    }

    /**
     * Test loading a page with POST parameters.
     * @throws Exception If something goes wrong.
     */
    public void testLoadFilePage() throws Exception {

        // create a real file to read
        // it could be usefull to have existing files to test in a special location in filesystem. 
        // It will be really needed when we have to test binary files using the file protocol.
        final String htmlContent = "<html><head><title>foo</title></head><body></body></html>";
        final File currentDirectory = new File((new File("")).getAbsolutePath());
        final File tmpFile = File.createTempFile("test", ".html", currentDirectory);
        tmpFile.deleteOnExit();
        final String encoding = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();
        FileUtils.writeStringToFile(tmpFile, htmlContent, encoding);

        URL fileURL = new URL("file://" + tmpFile.getCanonicalPath());
        
        final WebClient client = new WebClient();
        final HtmlPage page = (HtmlPage) client.getPage(fileURL);

        assertEquals(htmlContent, page.getWebResponse().getContentAsString());
        assertEquals("text/html", page.getWebResponse().getContentType());
        assertEquals(200, page.getWebResponse().getStatusCode());
        assertEquals("foo", page.getTitleText());
    }

    /**
     * Test redirecting with javascript during page load.
     * @throws Exception If something goes wrong.
     */
    public void testRedirectViaJavaScriptDuringInitialPageLoad() throws Exception {
        final String firstContent = "<html><head><title>First</title><script>"
            + "location.href='http://second'"
            + "</script></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            URL_SECOND, secondContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        webClient.setWebConnection( webConnection );

        final URL url = URL_FIRST;

        final HtmlPage page = (HtmlPage)webClient.getPage(
            url, SubmitMethod.GET, Collections.EMPTY_LIST);
        assertEquals("Second", page.getTitleText());
    }


    /**
     * Test tabbing where there are no tabbable elements.
     * @throws Exception If something goes wrong.
     */
    public void testKeyboard_NoTabbableElements() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = getPageForKeyboardTest(webClient, new String[0]);
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        assertNull( "original", webClient.getElementWithFocus() );
        assertNull( "next", page.tabToNextElement() );
        assertNull( "previous", page.tabToPreviousElement() );
        assertNull( "accesskey", page.pressAccessKey('a') );

        final List expectedAlerts = Collections.EMPTY_LIST;
        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * Test tabbing where there is only one tabbable element.
     * @throws Exception If something goes wrong.
     */
    public void testKeyboard_OneTabbableElement() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ null });
        final HtmlElement element = page.getHtmlElementById("submit0");

        assertNull( "original", webClient.getElementWithFocus() );
        assertNull( "accesskey", page.pressAccessKey('x') );

        assertEquals( "next", element, page.tabToNextElement() );
        assertEquals( "nextAgain", element, page.tabToNextElement() );

        webClient.getElementWithFocus().blur();
        assertNull( "original", webClient.getElementWithFocus() );

        assertEquals( "previous", element, page.tabToPreviousElement() );
        assertEquals( "previousAgain", element, page.tabToPreviousElement() );

        assertEquals( "accesskey", element, page.pressAccessKey('z') );

        final List expectedAlerts = Arrays.asList( new String[]{"focus-0", "blur-0", "focus-0"} );
        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * Test pressing an accesskey.
     * @throws Exception If something goes wrong.
     */
    public void testAccessKeys() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });

        assertEquals( "submit0", page.pressAccessKey('a').getAttributeValue("name") );
        assertEquals( "submit2", page.pressAccessKey('c').getAttributeValue("name") );
        assertEquals( "submit1", page.pressAccessKey('b').getAttributeValue("name") );

        final List expectedAlerts = Arrays.asList( new String[]{
            "focus-0", "blur-0", "focus-2", "blur-2", "focus-1"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Test tabbing to the next element.
     * @throws Exception If something goes wrong.
     */
    public void testTabNext() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });

        assertEquals( "submit0", page.tabToNextElement().getAttributeValue("name") );
        assertEquals( "submit1", page.tabToNextElement().getAttributeValue("name") );
        assertEquals( "submit2", page.tabToNextElement().getAttributeValue("name") );

        final List expectedAlerts = Arrays.asList( new String[]{
            "focus-0", "blur-0", "focus-1", "blur-1", "focus-2"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Test tabbing to the previous element.
     * @throws Exception If something goes wrong.
     */
    public void testTabPrevious() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });

        assertEquals( "submit2", page.tabToPreviousElement().getAttributeValue("name") );
        assertEquals( "submit1", page.tabToPreviousElement().getAttributeValue("name") );
        assertEquals( "submit0", page.tabToPreviousElement().getAttributeValue("name") );

        final List expectedAlerts = Arrays.asList( new String[]{
            "focus-2", "blur-2", "focus-1", "blur-1", "focus-0"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Test that a button can be selected via accesskey.
     * @throws Exception If something goes wrong.
     */
    public void testPressAccessKey_Button() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });
        final HtmlElement button = page.getHtmlElementById("button1");

        final List expectedAlerts = Collections.singletonList("buttonPushed");
        collectedAlerts.clear();

        button.removeAttribute("disabled");
        page.pressAccessKey('1');

        assertEquals( expectedAlerts, collectedAlerts );
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
        final WebClient webClient, final String[] tabIndexValues ) throws Exception {

        final StringBuffer buffer = new StringBuffer();
        buffer.append(
            "<html><head><title>First</title></head><body><form name='form1' method='post' onsubmit='return false;'>");

        for( int i=0; i<tabIndexValues.length; i++ ) {
            buffer.append( "<input type='submit' name='submit");
            buffer.append(i);
            buffer.append("' id='submit");
            buffer.append(i);
            buffer.append("'");
            if( tabIndexValues[i] != null ) {
                buffer.append(" tabindex='");
                buffer.append(tabIndexValues[i]);
                buffer.append("'");
            }
            buffer.append(" onblur='alert(\"blur-"+i+"\")'");
            buffer.append(" onfocus='alert(\"focus-"+i+"\")'");
            buffer.append(" accesskey='"+(char)('a'+i)+"'");
            buffer.append(">");
        }
        buffer.append("<div id='div1'>foo</div>"); // something that isn't tabbable

        // Elements that are tabbable but are disabled
        buffer.append("<button name='button1' id='button1' disabled onclick='alert(\"buttonPushed\")' ");
        buffer.append("accesskey='1'>foo</button>");

        buffer.append("</form></body></html>");

        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webConnection.setResponse(
            new URL("http://first/"), buffer.toString(), 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final URL url = new URL("http://first/");
        return (HtmlPage)webClient.getPage(url, SubmitMethod.GET, Collections.EMPTY_LIST);
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
            webResponse, webClient.getCurrentWindow() );
        assertInstanceOf( page, HtmlPage.class);

        final HtmlPage htmlPage = (HtmlPage)page;
        assertEquals("first", htmlPage.getTitleText() );
    }

    /**
     * Test {@link WebClient#expandUrl(URL,String)} for the case where an anchor name
     * was specified.
     * @throws Exception If the test fails.
     */
    public void testExpandUrl() throws Exception {
        assertEquals(
            "http://first/",
            WebClient.expandUrl(URL_FIRST, "#second").toExternalForm());
    }

    /** Test the accessors for refreshHandler */
    public void testRefreshHandlerAccessors() {
        final WebClient webClient = new WebClient();
        assertInstanceOf( webClient.getRefreshHandler(), DefaultRefreshHandler.class );
        
        final RefreshHandler handler = new DefaultRefreshHandler() {};
        webClient.setRefreshHandler( handler );
        assertSame( handler, webClient.getRefreshHandler() );
    }
    
    /** 
     * Test the script preprocessor
     * @throws IOException if the test fails
     */
    public void testScriptPreProcessor() throws IOException {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );
        final String alertText = "content";
        final String newAlertText = "newcontent";
        final String content
             = "<html><head><title>foo</title><script>"
             + "<!--\n   alert('" + alertText + "');\n// -->"
             + "</script></head><body>"
             + "<p>hello world</p>"
             + "<form name='form1'>"
             + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
             + "    <input type='text' name='textfield2' id='textfield2'/>"
             + "</form>"
             + "</body></html>";

        webConnection.setDefaultResponse( content );
        client.setWebConnection( webConnection );

        // Test null return from pre processor
        client.setScriptPreProcessor( new ScriptPreProcessor() {
            public String preProcess( final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                                      final HtmlElement htmlElement ) {
                return null;
            }
        });
        client.setAlertHandler( new AlertHandler() {
            public void handleAlert( final Page page, final String message ) {
                fail("The pre processor did not remove the javascript");
            }

        });
        client.getPage( new URL( "http://www.yahoo.com" ) );

        // Test modify script in pre processor
        client.setScriptPreProcessor( new ScriptPreProcessor() {
            public String preProcess( final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                                      final HtmlElement htmlElement ) {
                final int start = sourceCode.indexOf( alertText );
                final int end = start + alertText.length();

                return sourceCode.substring( 0, start ) + newAlertText + sourceCode.substring( end );
            }
        });
        client.setAlertHandler( new AlertHandler() {
            public void handleAlert( final Page page, final String message ) {
                if( !message.equals( newAlertText ) ) {
                    fail( "The pre processor did not modify the javascript" );
                }
            }

        });
        client.getPage( new URL( "http://www.yahoo.com" ) );
     }
    
    /** 
     * Test the ScriptPreProcessor's ability to filter out a javascript method
     * that is not implemented without affecting the rest of the page.
     * 
     * @throws Exception if the test fails
     */
    public void testScriptPreProcessor_UnimplementedJavascript() throws Exception {
        
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );
        final String content = "<html><head><title>foo</title></head><body>"
            + "<p>hello world</p>"
            + "<script>document.unimplementedFunction();</script>"
            + "<script>alert('implemented function');</script>"
            + "</body></html>";

        webConnection.setDefaultResponse( content );
        client.setWebConnection( webConnection );

        client.setScriptPreProcessor( new ScriptPreProcessor() {
            public String preProcess( final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                                      final HtmlElement htmlElement ) {
                if (sourceCode.indexOf("unimplementedFunction") > -1) {
                    return "";
                }
                return sourceCode;
            }
        });
        final List alerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(alerts));
        client.getPage( new URL( "http://page" ) );
        
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
                 = "<html><head><title>foo</title>"
                 + "</head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(
            new URL("http://page1"), page1Content, 200, "OK", "text/html; charset=garbage", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final Page page = client.getPage(
                new URL( "http://page1" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertInstanceOf(page, HtmlPage.class);
    }
    
    /**
     * Colons are legal in the path of a url but {@link WebClient#expandUrl(URL,String)} was
     * blowing up on this case.  Ensure it's fixed.
     * @throws Exception If the test fails.
     */
    public void testExpandUrlHandlesColonsInRelativeUrl() throws Exception {
        final URL newUrl = WebClient.expandUrl( new URL("http://host/foo"), "/bar/blah:de:blah");
        assertEquals( "http://host/bar/blah:de:blah", newUrl.toExternalForm() );
    }

    /**
     * Test reuse of a single {@link HtmlPage} object to submit the same form multiple times.
     *
     * @throws Exception if test fails
     */
    public void testReusingHtmlPageToSubmitFormMultipleTimes() throws Exception {

        final String firstContent = "<html><head><title>First</title></head>" +
                                    "<body onload='document.myform.mysubmit.focus()'>" +
                                    "<form action='" + URL_SECOND + "' name='myform'>" +
                                    "<input type='submit' name='mysubmit'>" +
                                    "</form></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body>Second</body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "text/html", Collections.EMPTY_LIST);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        for (int i = 0; i < 1000; i++) {
            page.getFormByName("myform").submit();
        }
    }

    /**
     * Test the value of window.opener when a link has target="_top"
     * @throws Exception if test fails
     */
    public void testOpenerInFrameset() throws Exception {

        final String firstContent = "<html><head><script>alert(window.opener)</script><frameset cols='*'>"
                                    + "<frame src='" + URL_SECOND + "'>"
                                    + "</frameset>"
                                    + "</html>";
        final String secondContent = "<html><body><a href='" + URL_FIRST + "' target='_top'>to top</a></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);
        
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        final HtmlPage pageInFrame = (HtmlPage) ((WebWindow) page.getFrames().get(0)).getEnclosedPage();
        ((HtmlAnchor) pageInFrame.getAnchors().get(0)).click();

        final List expectedAlerts = Arrays.asList( new String[]{
            "null", "null"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }
    
    /**
     * Test setting the NekoHTML logging and parsing flags
     * 
     * @throws Exception if test fails
     */
    public void testNekoFlagSetters() throws Exception {
        assertEquals("Default logging is wrong", false, WebClient.getValidateHtml());
        WebClient.setValidateHtml(true);
        assertEquals("Logging did not get set", true, WebClient.getValidateHtml());
        assertEquals("Default ignore content is wrong", false, WebClient.getIgnoreOutsideContent());
        WebClient.setIgnoreOutsideContent(true);
        assertEquals("Ignore content did not get set", true, WebClient.getIgnoreOutsideContent());
    }
    /**
     * Unset the static items set in tests here
     * @throws Exception if superclass throws
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        WebClient.setValidateHtml(false);
        WebClient.setIgnoreOutsideContent(false);
    }
}

