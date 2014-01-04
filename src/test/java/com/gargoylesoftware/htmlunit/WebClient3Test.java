/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static org.junit.Assert.assertArrayEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Deflater;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link WebClient} using WebDriverTestCase.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class WebClient3Test extends WebDriverTestCase {

    /**
     * Regression test for bug 2822048: a 302 redirect without Location header.
     * @throws Exception if an error occurs
     */
    // TODO [IE11]ERRORPAGE real IE11 displays his own error page (res://ieframe.dll/dnserror.htm#<url>)
    @Test
    public void redirect302WithoutLocation() throws Exception {
        final String html = "<html><body><a href='page2'>to redirect</a></body></html>";
        getMockWebConnection().setDefaultResponse("", 302, "Found", null);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        assertEquals(getDefaultUrl() + "page2", driver.getCurrentUrl());
    }

    /**
     * Regression test for bug 3017719: a 302 redirect should change the page url.
     * @throws Exception if an error occurs
     */
    @Test
    public void redirect302() throws Exception {
        final String html = "<html><body><a href='redirect.html'>redirect</a></body></html>";

        final URL url = new URL(getDefaultUrl(), "page2.html");
        getMockWebConnection().setResponse(url, html);

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Location", "/page2.html"));
        getMockWebConnection().setDefaultResponse("", 302, "Found", null, headers);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        assertEquals(url.toString(), driver.getCurrentUrl());
    }

    /**
     * Regression test for bug 3035155.
     * Bug was fixes in HttpClient 4.1.
     * @throws Exception if an error occurs
     */
    @Test
    public void redirect302UrlsInQuery() throws Exception {
        final String html = "<html><body><a href='redirect.html'>redirect</a></body></html>";

        final URL url = new URL(getDefaultUrl(), "page2.html");
        getMockWebConnection().setResponse(url, html);

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Location", "/page2.html?param=http%3A//somwhere.org"));
        getMockWebConnection().setDefaultResponse("", 302, "Found", null, headers);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        assertEquals(url.toString() + "?param=http%3A//somwhere.org", driver.getCurrentUrl());
    }

    /**
     * Regression test for bug 3012067: a null pointer exception was occurring.
     * @throws Exception if an error occurs
     */
    @Test
    public void bug3012067_npe() throws Exception {
        final String html = "<html><body>\n"
            + "<form action='" + getDefaultUrl() + "#foo' method='post'></form>\n"
            + "<script>\n"
            + "function doWork() {\n"
            + "  var f = document.forms[0];\n"
            + "  f.submit();\n"
            + "  f.submit();\n"
            + "}\n"
            + "</script>\n"
            + "<span id='clickMe' onclick='doWork()'>click</span>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
    }

    /**
     * Ensure that response stream can be read more than one time.
     * @throws Exception if an error occurs
     */
    @Test
    public void readStreamTwice() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<iframe src='binaryFile.bin'></iframe>\n"
            + "<iframe src='foo.html'></iframe>\n"
            + "</body></html>";

        final MockWebConnection mockConnection = getMockWebConnection();
        final byte[] binaryContent = new byte[4818];
        final Random random = new Random();
        for (int i = 0; i < binaryContent.length; ++i) {
            binaryContent[i] = (byte) (random.nextInt(Byte.MAX_VALUE));
        }
        mockConnection.setDefaultResponse(binaryContent, 200, "OK", "application/octet-stream");
        final URL urlFoo = new URL(getDefaultUrl(), "foo.html");
        mockConnection.setResponse(urlFoo, "<html></html>");

        final WebDriver driver = loadPage2(html);
        final WebElement iframe1 = driver.findElement(By.tagName("iframe"));
        if (driver instanceof HtmlUnitDriver) {
            final HtmlInlineFrame htmlUnitIFrame1 = (HtmlInlineFrame) toHtmlElement(iframe1);
            final WebResponse iframeWebResponse = htmlUnitIFrame1.getEnclosedPage().getWebResponse();
            byte[] receivedBytes = IOUtils.toByteArray(iframeWebResponse.getContentAsStream());
            receivedBytes = IOUtils.toByteArray(iframeWebResponse.getContentAsStream());
            assertArrayEquals(binaryContent, receivedBytes);
        }
    }

    /**
     * Was causing an Exception in IE simulation
     * as of HtmlUnit-2.8-SNAPSHOT on Aug. 04, 2010.
     * @throws Exception if the test fails
     */
    @Test
    public void escapeRequestQuery() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        loadPage2("", new URL(getDefaultUrl(), "foo?a=<b>i</b>"));
    }

    /**
     * Was causing a "java.net.URISyntaxException: Malformed escape pair"
     * as of HtmlUnit-2.12-SNAPSHOT on Nov. 29, 2012.
     * HtmlUnit now escapes the "%%" to "%25%25" to build a valid URL but FF doesn't care
     * and sends the invalid "%%" sequence as it.
     * This will be quite difficult to simulate FF here as HttpClient's HttpRequestBase
     * uses URI and "%%" can't be part of the query string for a URI.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void escapeRequestQuery2a() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final URL url = new URL(getDefaultUrl(), "foo.png?cb=%%RANDOM_NUMBER%%");
        loadPage2("", url);

        assertEquals(url, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * Was causing a "java.net.URISyntaxException: Malformed escape pair"
     * as of HtmlUnit-2.12-SNAPSHOT on Nov. 29, 2012.
     * This is a simplified version of {@link #escapeRequestQuery2a()} only testing
     * that no exception is thrown. The request performed is not fully correct.
     * This test can be removed once {@link #escapeRequestQuery2a()} runs correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void escapeRequestQuery2b() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final URL url = new URL(getDefaultUrl(), "foo.png?cb=%%RANDOM_NUMBER%%");
        loadPage2("", url);
    }

    /**
     * Regression test for issue 3193004.
     * Ensure that the click returns once the target page has been loaded into the target window.
     * @throws Exception if an error occurs
     */
    @Test
    public void clickReturnsWhenThePageHasBeenCompleteLoaded() throws Exception {
        final String firstContent = "<html><head>\n"
            + "<script>window.setInterval(\'',1);</script></head>\n"
            + "<body><a href='" + URL_SECOND + "'>to second</a></body></html>";
        final String secondContent = "<html><body></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_SECOND, secondContent);

        for (int i = 1; i < 100; i++) {
            final WebDriver webDriver = loadPage2(firstContent);
            webDriver.findElement(By.tagName("a")).click();
            Assert.assertEquals("Run " + i, URL_SECOND.toExternalForm(), webDriver.getCurrentUrl());
        }
    }

    /**
     * Ensures, that a window opened by an anchor with target attribute is attached
     * to the javascript event loop.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts ({ "open", "first", "second" })
    // TODO [IE11]MODALPANEL real IE11 opens a modal panel 'really close window?' which webdriver cannot handle
    public void windowOpenedByAnchorTargetIsAttachedToJavascriptEventLoop() throws Exception {
        final String firstContent = "<html>"
            + "<head>"
            + "<script type='text/javascript'>"
            + "  function info(msg) {"
            + "    alert(msg);"
            + "  }"
            + "</script>"
            + "</head>"
            + "<body>"
            + " <a id='testAnchor' href='" + URL_SECOND + "' target='_blank' onclick='info(\"open\")'>to second</a>"
            + "</body></html>";
        final String secondContent = "<html><head>"
            + "<script type='text/javascript'>"
            + "  function first() {"
            + "    window.opener.info('first');"
            + "    window.setTimeout(second, 10);"
            + "  }"
            + "  function second() {"
            + "    window.opener.info('second');"
            + "    window.close();"
            + "  }"
            + "</script>"
            + "</head>"

            + "<body onLoad='window.setTimeout(first, 5);'></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstContent);
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        driver.findElement(By.id("testAnchor")).click();
        Thread.sleep(1000);
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Ensures, that a window opened by a form with target attribute is attached
     * to the javascript event loop.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts ({ "open", "first", "second" })
    // TODO [IE11]MODALPANEL real IE11 opens a modal panel 'really close window?' which webdriver cannot handle
    public void windowOpenedByFormTargetIsAttachedToJavascriptEventLoop() throws Exception {
        final String firstContent = "<html>"
            + "<head>"
            + "<script type='text/javascript'>"
            + "  function info(msg) {"
            + "    alert(msg);"
            + "  }"
            + "</script>"
            + "</head>"
            + "<body>"
            + "<form action='" + URL_SECOND + "' target='_blank'>"
            + " <input id='testSubmit' type='submit' value='Submit' onclick='info(\"open\")'>"
            + "</form>"
            + "</body></html>";
        final String secondContent = "<html><head>"
            + "<script type='text/javascript'>"
            + "  function first() {"
            + "    window.opener.info('first');"
            + "    window.setTimeout(second, 10);"
            + "  }"
            + "  function second() {"
            + "    window.opener.info('second');"
            + "    window.close();"
            + "  }"
            + "</script>"
            + "</head>"

            + "<body onLoad='window.setTimeout(first, 5);'></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstContent);
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        driver.findElement(By.id("testSubmit")).click();
        Thread.sleep(1000);
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Ensures, that a window opened by javascript window.open is attached
     * to the javascript event loop.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts ({ "open", "first", "second" })
    public void windowOpenedByJavascriptIsAttachedToJavascriptEventLoop() throws Exception {
        final String firstContent = "<html>"
            + "<head>"
            + "<script type='text/javascript'>"
            + "  function info(msg) {"
            + "    alert(msg);"
            + "  }"
            + "</script>"
            + "</head>"
            + "<body>"
            + " <a id='testAnchor' href='#'"
            + "     onclick='info(\"open\");window.open(\"" + URL_SECOND + "\", \"Popup\", \"\");'>open window</a>"
            + "</body></html>";
        final String secondContent = "<html><head>"
            + "<script type='text/javascript'>"
            + "  function first() {"
            + "    window.opener.info('first');"
            + "    window.setTimeout(second, 10);"
            + "  }"
            + "  function second() {"
            + "    window.opener.info('second');"
            + "    window.close();"
            + "  }"
            + "</script>"
            + "</head>"

            + "<body onLoad='window.setTimeout(first, 5);'></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstContent);
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        driver.findElement(By.id("testAnchor")).click();
        Thread.sleep(1000);
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Ensures, that a window opened by javascript and than filled by an form with target attribute
     * is attached to the javascript event loop.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts ({ "open", "first", "second" })
    public void windowOpenedByJavascriptFilledByFormTargetIsAttachedToJavascriptEventLoop() throws Exception {
        final String firstContent = "<html>"
            + "<head>"
            + "<script type='text/javascript'>"
            + "  function info(msg) {"
            + "    alert(msg);"
            + "  }"
            + "</script>"
            + "</head>"
            + "<body>"
            + "<form action='" + URL_SECOND + "' name='myForm'>"
            + " <input id='testSubmit' type='button' value='Submit' "
            + "   onclick='info(\"open\");"
            + "   window.open(\"" + URL_SECOND + "\", \"Popup\");"
            + "   document.myForm.target = \"Popup\";'"
            + " >"
            + "</form>"
            + "</body></html>";
        final String secondContent = "<html><head>"
            + "<script type='text/javascript'>"
            + "  function first() {"
            + "    window.opener.info('first');"
            + "    window.setTimeout(second, 10);"
            + "  }"
            + "  function second() {"
            + "    window.opener.info('second');"
            + "    window.close();"
            + "  }"
            + "</script>"
            + "</head>"

            + "<body onLoad='window.setTimeout(first, 5);'></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstContent);
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        driver.findElement(By.id("testSubmit")).click();
        Thread.sleep(1000);
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts ({ "Executed", "later" })
    // TODO [IE11]ERRORPAGE real IE11 displays own error page if response is to small
    public void execJavascriptOnErrorPages() throws Exception {
        final String errorHtml = "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<script type='text/javascript'>\n"
                + "  alert('Executed');\n"
                + "  setTimeout(\"alert('later')\", 10);\n"
                + "</script>\n"
                + "</body></html>\n";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, errorHtml, 404, "Not Found", "text/html", new ArrayList<NameValuePair>());

        loadPageWithAlerts2(URL_FIRST, 42);
    }

    /**
     * This test was failing due to a change made in revision 7104 (not in any release)
     * that was transforming %20 into %2520.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("hello")
    public void urlEncodingPercent20() throws Exception {
        final String html = "<html><body>"
                + "<script src='a%20b.js'></script>"
                + "</body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(getDefaultUrl() + "a%20b.js"), "alert('hello');", "text/javascript");

        loadPageWithAlerts2(html);
    }

    /**
     * Test "deflate" encoding without ZLIB header and checksum fields.
     * This was failing as of HtmlUnit-2.10.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("modified")
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void deflateCompressionGZipCompatible() throws Exception {
        doTestDeflateCompression(true);
    }

    /**
     * Test "deflate" encoding with ZLIB header and checksum fields.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "modified",
            IE11 = "Hello world")
    @NotYetImplemented(IE11)
    // IE11 does not support deflate compression anymore but I couldn't find a way to disable it in HttpClient
    public void deflateCompressionNonGZipCompatible() throws Exception {
        doTestDeflateCompression(false);
    }

    private void doTestDeflateCompression(final boolean gzipCompatibleCompression) throws Exception {
        final byte[] input = "document.title = 'modified';".getBytes("UTF-8");

        final byte[] buffer = new byte[100];
        final Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, gzipCompatibleCompression);
        deflater.setInput(input);
        deflater.finish();

        final int compressedDataLength = deflater.deflate(buffer);
        final byte[] content = new byte[compressedDataLength];
        System.arraycopy(buffer, 0, content, 0, compressedDataLength);

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Encoding", "deflate"));
        headers.add(new NameValuePair("Content-Length", String.valueOf(compressedDataLength)));

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_SECOND, content, 200, "OK", "text/javascript", headers);

        final String html = "<html><head>"
            + "<title>Hello world</title>"
            + "<script src='" + URL_SECOND + "'></script>"
            + "</head><body><script>alert(document.title)</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
