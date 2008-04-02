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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Common superclass for HtmlUnit tests.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Michael Ottati
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public abstract class WebTestCase {
    /** Constant for the URL http://first which is used in the tests. */
    public static final URL URL_FIRST;

    /** Constant for the URL http://second which is used in the tests. */
    public static final URL URL_SECOND;

    /** Constant for the URL http://third which is used in the tests. */
    public static final URL URL_THIRD;

    /** Constant for the URL http://www.gargoylesoftware.com which is used in the tests. */
    public static final URL URL_GARGOYLE;

    /**
     * The name of the system property used to determine if files should be generated
     * or not in {@link #createTestPageForRealBrowserIfNeeded(String,List)}.
     */
    public static final String PROPERTY_GENERATE_TESTPAGES
        = "com.gargoylesoftware.htmlunit.WebTestCase.GenerateTestpages";

    private BrowserVersion browserVersion_;
    
    private String[] expectedAlerts_;
    
    static {
        try {
            URL_FIRST = new URL("http://first");
            URL_SECOND = new URL("http://second");
            URL_THIRD = new URL("http://third");
            URL_GARGOYLE = new URL("http://www.gargoylesoftware.com/");
        }
        catch (final MalformedURLException e) {
            // This is theoretically impossible.
            throw new IllegalStateException("Unable to create URL constants");
        }
    }

    /**
     * Load a page with the specified HTML using the default browser version.
     * @param html The HTML to use.
     * @return The new page.
     * @throws Exception if something goes wrong.
     */
    public static final HtmlPage loadPage(final String html) throws Exception {
        return loadPage(html, null);
    }

    /**
     * Load a page with the specified HTML and collect alerts into the list.
     * @param browserVersion the browser version to use
     * @param html The HTML to use.
     * @param collectedAlerts The list to hold the alerts.
     * @return The new page.
     * @throws Exception If something goes wrong.
     */
    public static final HtmlPage loadPage(final BrowserVersion browserVersion,
            final String html, final List<String> collectedAlerts) throws Exception {
        return loadPage(browserVersion, html, collectedAlerts, URL_GARGOYLE);
    }

    /**
     * User the default browser version to load a page with the specified HTML
     * and collect alerts into the list.
     * @param html The HTML to use.
     * @param collectedAlerts The list to hold the alerts.
     * @return The new page.
     * @throws Exception If something goes wrong.
     */
    public static final HtmlPage loadPage(final String html, final List<String> collectedAlerts) throws Exception {
        return loadPage(BrowserVersion.getDefault(), html, collectedAlerts, URL_GARGOYLE);
    }

    /**
     * Loads an external URL, accounting for the fact that the remote server may be down or the
     * machine running the tests may not be connected to the internet.
     * @param url the URL to load
     * @return the loaded page, or <tt>null</tt> if there were connectivity issues
     * @throws Exception if an error occurs
     */
    protected static final HtmlPage loadUrl(final String url) throws Exception {
        try {
            final WebClient client = new WebClient();
            client.setUseInsecureSSL(true);
            return (HtmlPage) client.getPage(url);
        }
        catch (final ConnectException e) {
            // The remote server is probably down.
            System.out.println("Connection could not be made to " + url);
            return null;
        }
        catch (final SocketException e) {
            // The local machine may not be online.
            System.out.println("Connection could not be made to " + url);
            return null;
        }
        catch (final UnknownHostException e) {
            // The local machine may not be online.
            System.out.println("Connection could not be made to " + url);
            return null;
        }
    }

    /**
     * Returns the log that is being used for all testing objects.
     * @return The log.
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Loads a page with the specified HTML and collect alerts into the list.
     * @param html The HTML to use.
     * @param collectedAlerts The list to hold the alerts.
     * @param url The URL that will use as the document host for this page
     * @return The new page.
     * @throws Exception If something goes wrong.
     */
    protected static final HtmlPage loadPage(final String html, final List<String> collectedAlerts,
            final URL url) throws Exception {

        return loadPage(BrowserVersion.getDefault(), html, collectedAlerts, url);
    }

    /**
     * Load a page with the specified HTML and collect alerts into the list.
     * @param browserVersion the browser version to use
     * @param html The HTML to use.
     * @param collectedAlerts The list to hold the alerts.
     * @param url The URL that will use as the document host for this page
     * @return The new page.
     * @throws Exception If something goes wrong.
     */
    protected static final HtmlPage loadPage(final BrowserVersion browserVersion,
            final String html, final List<String> collectedAlerts, final URL url) throws Exception {

        final WebClient client = new WebClient(browserVersion);
        if (collectedAlerts != null) {
            client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        }

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        return (HtmlPage) client.getPage(url);
    }

    /**
     * Assert that the specified object is null.
     * @param object The object to check.
     */
    public static void assertNull(final Object object) {
        Assert.assertNull("Expected null but found [" + object + "]", object);
    }

    /**
     * Facility to test external form of urls. Comparing external form of URLs is
     * really faster than URL.equals() as the host doesn't need to be resolved.
     * @param expectedUrl the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final URL expectedUrl, final URL actualUrl) {
        Assert.assertEquals(expectedUrl.toExternalForm(), actualUrl.toExternalForm());
    }

    /**
     * Asserts the two objects are equal.
     * @param expected the expected object.
     * @param actual the object to test.
     */
    protected void assertEquals(final Object expected, final Object actual) {
        Assert.assertEquals(expected, actual);
    }

    /**
     * Facility to test external form of urls. Comparing external form of URLs is
     * really faster than URL.equals() as the host doesn't need to be resolved.
     * @param message the message to display if assertion fails
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String message, final URL expectedUrl, final URL actualUrl) {
        Assert.assertEquals(message, expectedUrl.toExternalForm(), actualUrl.toExternalForm());
    }

    /**
     * Facility to test external form of an URL.
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String expectedUrl, final URL actualUrl) {
        Assert.assertEquals(expectedUrl, actualUrl.toExternalForm());
    }

    /**
     * Facility method to avoid having to create explicitly a list from
     * a String[] (for example when testing received alerts).
     * Transforms the String[] to a List before calling
     * {@link junit.framework.Assert#assertEquals(java.lang.Object, java.lang.Object)}.
     * @param expected the expected strings
     * @param actual the collection of strings to test
     */
    protected void assertEquals(final String[] expected, final List<String> actual) {
        Assert.assertEquals(Arrays.asList(expected), actual);
    }

    /**
     * Facility method to avoid having to create explicitly a list from
     * a String[] (for example when testing received alerts).
     * Transforms the String[] to a List before calling
     * {@link junit.framework.Assert#assertEquals(java.lang.String, java.lang.Object, java.lang.Object)}.
     * @param message the message to display if assertion fails
     * @param expected the expected strings
     * @param actual the collection of strings to test
     */
    protected void assertEquals(final String message, final String[] expected, final List<String> actual) {
        Assert.assertEquals(message, Arrays.asList(expected), actual);
    }

    /**
     * Facility to test external form of an URL.
     * @param message the message to display if assertion fails
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String message, final String expectedUrl, final URL actualUrl) {
        Assert.assertEquals(message, expectedUrl, actualUrl.toExternalForm());
    }

    /**
     * Assert the specified condition is true.
     * @param condition condition to test.
     */
    protected void assertTrue(final boolean condition) {
        Assert.assertTrue(condition);
    }

    /**
     * Assert the specified condition is true.
     * @param message message to show.
     * @param condition condition to test.
     */
    protected void assertTrue(final String message, final boolean condition) {
        Assert.assertTrue(message, condition);
    }

    /**
     * Assert the specified condition is false.
     * @param condition condition to test.
     */
    protected void assertFalse(final boolean condition) {
        Assert.assertFalse(condition);
    }

    /**
     * Returns an input stream for the specified file name. Refer to {@link #getFileObject(String)}
     * for details on how the file is located.
     * @param fileName The base file name.
     * @return The input stream.
     * @throws FileNotFoundException If the file cannot be found.
     */
    public static InputStream getFileAsStream(final String fileName) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(getFileObject(fileName)));
    }

    /**
     * Returns a File object for the specified file name. This is different from just
     * <code>new File(fileName)</code> because it will adjust the location of the file
     * depending on how the code is being executed.
     *
     * @param fileName The base filename.
     * @return The new File object.
     * @throws FileNotFoundException if !file.exists()
     */
    public static File getFileObject(final String fileName) throws FileNotFoundException {
        final String localizedName = fileName.replace('/', File.separatorChar);

        File file = new File(localizedName);
        if (!file.exists()) {
            file = new File("../../" + localizedName);
        }

        if (!file.exists()) {
            try {
                System.out.println("currentDir=" + new File(".").getCanonicalPath());
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            throw new FileNotFoundException(localizedName);
        }
        return file;
    }

    /**
     * Facility method transforming expectedAlerts to a list and calling
     * {@link #createTestPageForRealBrowserIfNeeded(String, List)}.
     * @param content the content of the HTML page
     * @param expectedAlerts the expected alerts
     * @throws IOException if writing file fails
     */
    protected void createTestPageForRealBrowserIfNeeded(final String content, final String[] expectedAlerts)
        throws IOException {
        createTestPageForRealBrowserIfNeeded(content, Arrays.asList(expectedAlerts));
    }

    /**
     * Generates an instrumented HTML file in the temporary dir to easily make a manual test in a real browser.
     * The file is generated only if the system property {@link #PROPERTY_GENERATE_TESTPAGES} is set.
     * @param content the content of the HTML page
     * @param expectedAlerts the expected alerts
     * @throws IOException if writing file fails
     */
    protected void createTestPageForRealBrowserIfNeeded(final String content, final List<String> expectedAlerts)
        throws IOException {
        final Log log = LogFactory.getLog(WebTestCase.class);
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null) {
            // should be optimized....

            // calls to alert() should be replaced by call to custom function
            String newContent = StringUtils.replace(content, "alert(", "htmlunitReserved_caughtAlert(");

            final String instrumentationJS = createInstrumentationScript(expectedAlerts);

            // first version, we assume that there is a <head> and a </body> or a </frameset>
            if (newContent.indexOf("<head>") > -1) {
                newContent = StringUtils.replaceOnce(newContent, "<head>", "<head>" + instrumentationJS);
            }
            else {
                newContent = StringUtils.replaceOnce(newContent, "<html>",
                        "<html>\n<head>\n" + instrumentationJS + "\n</head>\n");
            }
            final String endScript = "\n<script>htmlunitReserved_addSummaryAfterOnload();</script>\n";
            if (newContent.contains("</body>")) {
                newContent = StringUtils.replaceOnce(newContent, "</body>",  endScript + "</body>");
            }
            else {
                throw new RuntimeException("Currently only content with a <head> and a </body> is supported");
            }

            final File f = File.createTempFile("TEST" + '_', ".html");
            FileUtils.writeStringToFile(f, newContent, "ISO-8859-1");
            log.info("Test file written: " + f.getAbsolutePath());
        }
        else {
            log.debug("System property \"" + PROPERTY_GENERATE_TESTPAGES
                    + "\" not set, don't generate test HTML page for real browser");
        }
    }

    /**
     * @param expectedAlerts the list of the expected alerts
     * @return the script to be included at the beginning of the generated HTML file
     * @throws IOException in case of problem
     */
    private String createInstrumentationScript(final List<String> expectedAlerts) throws IOException {
        // generate the js code
        final InputStream is = getClass().getClassLoader().getResourceAsStream("alertVerifier.js");
        final String baseJS = IOUtils.toString(is);
        IOUtils.closeQuietly(is);

        final StringBuilder sb = new StringBuilder();
        sb.append("\n<script type='text/javascript'>\n");
        sb.append("var htmlunitReserved_tab = [");
        for (final ListIterator<String> iter = expectedAlerts.listIterator(); iter.hasNext();) {
            if (iter.hasPrevious()) {
                sb.append(", ");
            }
            final String message = iter.next();
            sb.append("{expected: \"").append(message).append("\"}");
        }
        sb.append("];\n\n");
        sb.append(baseJS);
        sb.append("</script>\n");
        return sb.toString();
    }

    /**
     * Convenience method to pull the MockWebConnection out of an HtmlPage created with
     * the loadPage method.
     * @param page HtmlPage to get the connection from
     * @return the MockWebConnection that served this page
     */
    protected static final MockWebConnection getMockConnection(final HtmlPage page) {
        return (MockWebConnection) page.getWebClient().getWebConnection();
    }

    /**
     * Runs the calling JUnit test again and fails only if it already runs.<br/>
     * This is helpful for tests that don't currently work but should work one day,
     * when the tested functionality has been implemented.<br/>
     * The right way to use it is:
     * <pre>
     * public void testXXX() {
     *   if (notYetImplemented()) {
     *       return;
     *   }
     *
     *   ... the real (now failing) unit test
     * }
     * </pre>
     * @return <tt>false</tt> when not itself already in the call stack
     */
    protected boolean notYetImplemented() {
        if (notYetImplementedFlag.get() != null) {
            return false;
        }
        notYetImplementedFlag.set(Boolean.TRUE);

        final Method testMethod = findRunningJUnitTestMethod();
        try {
            getLog().info("Running " + testMethod.getName() + " as not yet implemented");
            testMethod.invoke(this, (Object[]) new Class[] {});
            Assert.fail(testMethod.getName() + " is marked as not implemented but already works");
        }
        catch (final Exception e) {
            getLog().info(testMethod.getName() + " fails which is normal as it is not yet implemented");
            // method execution failed, it is really "not yet implemented"
        }
        finally {
            notYetImplementedFlag.set(null);
        }

        return true;
    }

    /**
     * Finds from the call stack the active running JUnit test case
     * @return the test case method
     * @throws RuntimeException if no method could be found.
     */
    private Method findRunningJUnitTestMethod() {
        final Class< ? > cl = getClass();
        final Class< ? >[] args = new Class[] {};

        // search the initial junit test
        final Throwable t = new Exception();
        for (int i = t.getStackTrace().length - 1; i >= 0; i--) {
            final StackTraceElement element = t.getStackTrace()[i];
            if (element.getClassName().equals(cl.getName())) {
                try {
                    final Method m = cl.getMethod(element.getMethodName(), args);
                    if (isPublicTestMethod(m)) {
                        return m;
                    }
                }
                catch (final Exception e) {
                    // can't access, ignore it
                }
            }
        }

        throw new RuntimeException("No JUnit test case method found in call stack");
    }

    /**
     * From Junit. Test if the method is a junit test.
     * @param method the method
     * @return <code>true</code> if this is a junit test.
     */
    private boolean isPublicTestMethod(final Method method) {
        return method.getParameterTypes().length == 0
            && (method.getName().startsWith("test") || method.getAnnotation(Test.class) != null)
            && method.getReturnType() == Void.TYPE
            && Modifier.isPublic(method.getModifiers());
    }

    private static final ThreadLocal<Boolean> notYetImplementedFlag = new ThreadLocal<Boolean>();

    /**
     * Load the specified resource for the supported browsers and tests
     * that the generated log corresponds to the expected one for this browser.
     *
     * @param fileName the resource name which resides in /resources folder and
     *        belongs to the same package as the test class.
     *
     * @throws Exception if the test fails
     */
    protected void testHTMLFile(final String fileName) throws Exception {
        final String resourcePath = getClass().getPackage().getName().replace('.', '/') + '/' + fileName;
        final URL url = getClass().getClassLoader().getResource(resourcePath);
        
        final Map<String, BrowserVersion> testedBrowser = new HashMap<String, BrowserVersion>();
        testedBrowser.put("FIREFOX_2", BrowserVersion.FIREFOX_2);
        testedBrowser.put("INTERNET_EXPLORER_6_0", BrowserVersion.INTERNET_EXPLORER_6_0);

        for (final Map.Entry<String, BrowserVersion> entry : testedBrowser.entrySet()) {
            final String browserKey = entry.getKey();
            final BrowserVersion browserVersion = entry.getValue();

            final WebClient client = new WebClient(browserVersion);
        
            final HtmlPage page = (HtmlPage) client.getPage(url);
            final HtmlElement want = page.getHtmlElementById(browserKey);
            
            final HtmlElement got = page.getHtmlElementById("log");
            
            final List<String> expected = readChildElementsText(want);
            final List<String> actual = readChildElementsText(got);
            
            Assert.assertEquals(expected, actual);
        }
    }

    private List<String> readChildElementsText(final HtmlElement elt) {
        final List<String> list = new ArrayList<String>();
        for (final HtmlElement child : elt.getChildElements()) {
            list.add(child.asText());
        }
        return list;
    }
    
    void setBrowserVersion(final BrowserVersion browserVersion) {
        browserVersion_ = browserVersion;
    }
    
    /**
     * Returns a newly created WebClient with the current {@link BrowserVersion}.
     * @return a newly created WebClient with the current {@link BrowserVersion}.
     */
    protected final WebClient getWebClient() {
        return new WebClient(getBrowserVersion());
    }
    
    /**
     * Returns the current {@link BrowserVersion}.
     * @return current {@link BrowserVersion}.
     */
    protected final BrowserVersion getBrowserVersion() {
        if (browserVersion_ == null) {
            throw new IllegalStateException("You must annotate the test class with '@RunWith(BrowserRunner.class)'");
        }
        return browserVersion_;
    }
    void setExpectedAlerts(final String[] expectedAlerts) {
        expectedAlerts_ = expectedAlerts;
    }

    /**
     * Load a page with the specified HTML using the current browser version, and asserts the alerts
     * equal the expected alerts.
     * @param html The HTML to use.
     * @return The new page.
     * @throws Exception if something goes wrong.
     */
    protected final HtmlPage loadPageWithAlerts(final String html) throws Exception {
        if (expectedAlerts_ == null) {
            throw new IllegalStateException("You must annotate the test method with '@Alerts(...)'");
        }
        
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts_);
        
        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_GARGOYLE, html);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);
        assertEquals(expectedAlerts_, collectedAlerts);
        return page;
    }
}
