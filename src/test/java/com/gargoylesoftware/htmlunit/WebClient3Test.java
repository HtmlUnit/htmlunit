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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;

import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link WebClient} using WebDriverTestCase.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebClient3Test extends WebDriverTestCase {

    static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Regression test for bug 3012067: a null pointer exception was occurring.
     * @throws Exception if an error occurs
     */
    @Test
    public void bug3012067_npe() throws Exception {
        final String html = "<html><body>\n"
            + "<form action='" + URL_FIRST + "#foo' method='post'></form>\n"
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
        for (int i = 0; i < binaryContent.length; i++) {
            binaryContent[i] = (byte) (RANDOM.nextInt(Byte.MAX_VALUE));
        }
        mockConnection.setDefaultResponse(binaryContent, 200, "OK", MimeType.APPLICATION_OCTET_STREAM);
        final URL urlFoo = new URL(URL_FIRST, "foo.html");
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

        loadPage2("", new URL(URL_FIRST, "foo?a=<b>i</b>"));
    }

    /**
     * Was causing a "java.net.URISyntaxException: Malformed escape pair".
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

        final URL url = new URL(URL_FIRST, "foo.png?cb=%%RANDOM_NUMBER%%");
        loadPage2("", url);

        // real browsers do not send this request
        // 'Unable to parse URI query'
        assertEquals(0, getMockWebConnection().getRequestCount());
    }

    /**
     * Was causing a "java.net.URISyntaxException: Malformed escape pair".
     * This is a simplified version of {@link #escapeRequestQuery2a()} only testing
     * that no exception is thrown. The request performed is not fully correct.
     * This test can be removed once {@link #escapeRequestQuery2a()} runs correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void escapeRequestQuery2b() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final URL url = new URL(URL_FIRST, "foo.png?cb=%%RANDOM_NUMBER%%");
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
            assertEquals("Run " + i, URL_SECOND.toExternalForm(), webDriver.getCurrentUrl());
        }
    }

    /**
     * Ensures, that a window opened by an anchor with target attribute is attached
     * to the javascript event loop.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"open", "first", "second"})
    public void windowOpenedByAnchorTargetIsAttachedToJavascriptEventLoop() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String firstContent = "<html>\n"
            + "<head>\n"
            + "<script type='text/javascript'>\n"
            + "  function info(msg) {\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a id='testAnchor' href='" + URL_SECOND + "' target='_blank' onclick='info(\"open\")'>to second</a>\n"
            + "</body></html>";
        final String secondContent = "<html><head>\n"
            + "<script type='text/javascript'>\n"
            + "  function first() {\n"
            + "    window.opener.info('first');\n"
            + "    window.setTimeout(second, 10);\n"
            + "  }\n"
            + "  function second() {\n"
            + "    window.opener.info('second');\n"
            + "    window.close();\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onLoad='window.setTimeout(first, 5);'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        driver.findElement(By.id("testAnchor")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Ensures, that a window opened by a form with target attribute is attached
     * to the javascript event loop.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"open", "first", "second"})
    public void windowOpenedByFormTargetIsAttachedToJavascriptEventLoop() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String firstContent = "<html>\n"
            + "<head>\n"
            + "<script type='text/javascript'>\n"
            + "  function info(msg) {\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form action='" + URL_SECOND + "' target='_blank'>\n"
            + "  <input id='testSubmit' type='submit' value='Submit' onclick='info(\"open\")'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "<html><head>\n"
            + "<script type='text/javascript'>\n"
            + "  function first() {\n"
            + "    window.opener.info('first');\n"
            + "    window.setTimeout(second, 10);\n"
            + "  }\n"
            + "  function second() {\n"
            + "    window.opener.info('second');\n"
            + "    window.close();\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onLoad='window.setTimeout(first, 5);'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        driver.findElement(By.id("testSubmit")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Ensures, that a window opened by javascript window.open is attached
     * to the javascript event loop.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"open", "first", "second"})
    public void windowOpenedByJavascriptIsAttachedToJavascriptEventLoop() throws Exception {
        final String firstContent = "<html>\n"
            + "<head>\n"
            + "<script type='text/javascript'>\n"
            + "  function info(msg) {\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a id='testAnchor' href='#'"
            + "    onclick='info(\"open\");window.open(\"" + URL_SECOND + "\", \"Popup\", \"\");'>open window</a>\n"
            + "</body></html>";
        final String secondContent = "<html><head>\n"
            + "<script type='text/javascript'>\n"
            + "  function first() {\n"
            + "    window.opener.info('first');\n"
            + "    window.setTimeout(second, 10);\n"
            + "  }\n"
            + "  function second() {\n"
            + "    window.opener.info('second');\n"
            + "    window.close();\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onLoad='window.setTimeout(first, 5);'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        driver.findElement(By.id("testAnchor")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Ensures, that a window opened by javascript and than filled by a form with target attribute
     * is attached to the javascript event loop.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"open", "first", "second"})
    public void windowOpenedByJavascriptFilledByFormTargetIsAttachedToJavascriptEventLoop() throws Exception {
        final String firstContent = "<html>\n"
            + "<head>\n"
            + "<script type='text/javascript'>\n"
            + "  function info(msg) {\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form action='" + URL_SECOND + "' name='myForm'>\n"
            + "  <input id='testSubmit' type='button' value='Submit' "
            + "    onclick='info(\"open\");"
            + "    window.open(\"" + URL_SECOND + "\", \"Popup\");"
            + "    document.myForm.target = \"Popup\";'"
            + "  >\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "<html><head>\n"
            + "<script type='text/javascript'>\n"
            + "  function first() {\n"
            + "    window.opener.info('first');\n"
            + "    window.setTimeout(second, 10);\n"
            + "  }\n"
            + "  function second() {\n"
            + "    window.opener.info('second');\n"
            + "    window.close();\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onLoad='window.setTimeout(first, 5);'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        driver.findElement(By.id("testSubmit")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"Executed", "later"})
    // TODO [IE]ERRORPAGE real IE displays own error page if response is to small
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
        conn.setResponse(URL_FIRST, errorHtml, 404, "Not Found", MimeType.TEXT_HTML, new ArrayList<NameValuePair>());

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
        final String html = "<html><body>\n"
                + "<script src='a%20b.js'></script>\n"
                + "</body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_FIRST, "a%20b.js"), "alert('hello');", "text/javascript");

        loadPageWithAlerts2(html);
    }

    /**
     * Test "deflate" encoding without ZLIB header and checksum fields.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("modified")
    public void deflateCompressionGZipCompatible() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        doTestDeflateCompression(true);
    }

    /**
     * Test "deflate" encoding with ZLIB header and checksum fields.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "modified",
            IE = "Hello world")
    @NotYetImplemented(IE)
    // IE does not support deflate compression anymore but I couldn't find a way to disable it in HttpClient
    public void deflateCompressionNonGZipCompatible() throws Exception {
        doTestDeflateCompression(false);
    }

    private void doTestDeflateCompression(final boolean gzipCompatibleCompression) throws Exception {
        final byte[] input = "document.title = 'modified';".getBytes(UTF_8);

        final byte[] buffer = new byte[100];
        final Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, gzipCompatibleCompression);
        deflater.setInput(input);
        deflater.finish();

        final int compressedDataLength = deflater.deflate(buffer);
        final byte[] content = new byte[compressedDataLength];
        System.arraycopy(buffer, 0, content, 0, compressedDataLength);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "deflate"));
        headers.add(new NameValuePair(HttpHeader.CONTENT_LENGTH, String.valueOf(compressedDataLength)));

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_SECOND, content, 200, "OK", "text/javascript", headers);

        final String html = "<html><head>\n"
            + "<title>Hello world</title>\n"
            + "<script src='" + URL_SECOND + "'></script>\n"
            + "</head><body><script>alert(document.title)</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("executed")
    public void javascriptContentDetectorWithoutContentType() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<script>alert('executed')</script>", 200, "OK", null);
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "executed",
             IE = "")
    @NotYetImplemented(IE)
    public void javascriptContentDetectorWithoutContentType500() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<script>alert('executed')</script>", 500, "OK", null);
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("executed")
    public void javascriptContentDetectorWithoutContentTypeWhitespace() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse(" \t \r\n \n   <script>alert('executed')</script>", 200, "OK", null);
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void javascriptContentDetectorWithoutContentTypeTextBefore() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("Attention<script>alert('executed')</script>", 200, "OK", null);
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("executed")
    public void javascriptContentDetectorWithoutContentUppercase() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<SCRIPT>alert('executed')</SCRIPT>", 200, "OK", null);
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("executed")
    public void javascriptContentDetectorWithoutContentMixedCase() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<scRIPt>alert('executed')</scRIPt>", 200, "OK", null);
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public void javascriptContentDetectorContentTypeTextPlain() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<script>alert('executed')</script>", 200, "OK", MimeType.TEXT_PLAIN);
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @NotYetImplemented
    public void javascriptContentDetectorContentTypeApplicationJavascript() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<script>alert('executed')</script>", 200, "OK", MimeType.APPLICATION_JAVASCRIPT);
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void encodingCharsetGB2312() throws Exception {
        encodingCharset("\u6211\u662F\u6211\u7684 Abc", "GB2312", "GB2312");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void encodingCharsetGB2312GBKChar() throws Exception {
        encodingCharset("\u4eb8 Abc", "GB2312", "GBK");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void encodingCharsetGBK() throws Exception {
        encodingCharset("\u6211\u662F\u6211\u7684 \u4eb8 Abc", "GBK", "GBK");
    }

    private void encodingCharset(final String title,
            final String metaCharset, final String responseCharset) throws Exception {
        final String html =
            "<html><head>\n"
            + "<meta http-equiv='Content-Type' content='text/html; charset=" + metaCharset + "'>\n"
            + "<title>" + title + "</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final String firstResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n" + html;

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer =
                new PrimitiveWebServer(Charset.forName(responseCharset), firstResponse, firstResponse)) {
            final String url = "http://localhost:" + primitiveWebServer.getPort() + "/";
            final WebDriver driver = getWebDriver();

            driver.get(url);
            assertEquals(title, driver.getTitle());
        }
    }
}
