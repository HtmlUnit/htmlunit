/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.htmlunit.HttpHeader;
import org.htmlunit.HttpMethod;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.junit.annotation.NotYetImplemented;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link Location}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Michael Ottati
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Atsushi Nakagawa
 * @author Lai Quang Duong
 * @author Kanoko Yamamoto
 */
@RunWith(BrowserRunner.class)
public class Location2Test extends WebDriverTestCase {

    /**
     * Regression test for bug 742902.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void documentLocationGet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(top.document.location);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void documentLocationSet() throws Exception {
        final String html1 = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <title>test1</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      document.location = 'foo.html';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('ok');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "foo.html"), html2);

        final WebDriver driver = loadPage2(html1);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void documentLocationHref() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(top.document.location.href);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "about:blank", "blank", "", "about:", ""})
    public void about_blank_attributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var location = frames[0].document.location;\n"
            + "  log(location.hash);\n"
            + "  log(location.href);\n"
            + "  log(location.pathname);\n"
            + "  log(location.port);\n"
            + "  log(location.protocol);\n"
            + "  log(location.search);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe src='about:blank'></iframe>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "about:blank?query", "blank", "", "about:", "?query"},
            FF = {"", "about:blank?query", "blank", "", "about:", ""},
            FF_ESR = {"", "about:blank?query", "blank", "", "about:", ""})
    public void about_blank_query_attributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var doc = frames[0].document;\n"
            + "        var location = doc.location;\n"
            + "        log(location.hash);\n"
            + "        log(location.href);\n"
            + "        log(location.pathname);\n"
            + "        log(location.port);\n"
            + "        log(location.protocol);\n"
            + "        log(location.search);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe src='about:blank?query'></iframe>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "about:blank?", "blank", "", "about:", ""})
    public void about_blank_emptyquery_attributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var doc = frames[0].document;\n"
            + "        var location = doc.location;\n"
            + "        log(location.hash);\n"
            + "        log(location.href);\n"
            + "        log(location.pathname);\n"
            + "        log(location.port);\n"
            + "        log(location.protocol);\n"
            + "        log(location.search);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe src='about:blank?'></iframe>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"#hash", "about:blank#hash", "blank", "", "about:", ""})
    public void about_blank_hash_attributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var doc = frames[0].document;\n"
            + "        var location = doc.location;\n"
            + "        log(location.hash);\n"
            + "        log(location.href);\n"
            + "        log(location.pathname);\n"
            + "        log(location.port);\n"
            + "        log(location.protocol);\n"
            + "        log(location.search);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe src='about:blank#hash'></iframe>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"#hash", "about:blank?#hash", "blank", "", "about:", ""})
    public void about_blank_emptyquery_hash_attributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var doc = frames[0].document;\n"
            + "        var location = doc.location;\n"
            + "        log(location.hash);\n"
            + "        log(location.href);\n"
            + "        log(location.pathname);\n"
            + "        log(location.port);\n"
            + "        log(location.protocol);\n"
            + "        log(location.search);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe src='about:blank?#hash'></iframe>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"#hash", "about:blank?query#hash", "blank", "", "about:", "?query"},
            FF = {"#hash", "about:blank?query#hash", "blank", "", "about:", ""},
            FF_ESR = {"#hash", "about:blank?query#hash", "blank", "", "about:", ""})
    public void about_blank_query_hash_attributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var doc = frames[0].document;\n"
            + "        var location = doc.location;\n"
            + "        log(location.hash);\n"
            + "        log(location.href);\n"
            + "        log(location.pathname);\n"
            + "        log(location.port);\n"
            + "        log(location.protocol);\n"
            + "        log(location.search);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe src='about:blank?query#hash'></iframe>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "about:blank", "#foo", "about:blank#foo"})
    public void about_blank_set_hash() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var doc = frames[0].document;\n"
            + "        var location = doc.location;\n"

            + "        log(location.hash);\n"
            + "        log(location.href);\n"
            + "        location.hash = 'foo';\n"
            + "        log(location.hash);\n"
            + "        log(location.href);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe src='about:blank'></iframe>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("?foobar")
    public void search() throws Exception {
        checkSearch(URL_FIRST + "?foobar");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void emptySearch() throws Exception {
        checkSearch(URL_FIRST + "?");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void noSearch() throws Exception {
        checkSearch(URL_FIRST.toExternalForm());
    }

    private void checkSearch(final String url) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.location.search);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(html);
        loadPage2(html, new URL(url));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"#a%20b", "§§URL§§#a%20b", "#a%20b", "§§URL§§#a%20b", "#abc;,/?:@&=+$-_.!~*()ABC123foo",
             "#%25%20%5E%5B%5D%7C%22%3C%3E%7B%7D%5C"})
    public void hashEncoding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.location.hash = 'a b';\n"
            + "    log(window.location.hash);\n"
            + "    log(window.location.href);\n"
            + "    window.location.hash = 'a%20b';\n"
            + "    log(window.location.hash);\n"
            + "    log(window.location.href);\n"
            + "    window.location.hash = 'abc;,/?:@&=+$-_.!~*()ABC123foo';\n"
            + "    log(window.location.hash);\n"
            + "    window.location.hash = '%25%20%5E%5B%5D%7C%22%3C%3E%7B%7D%5C';\n"
            + "    log(window.location.hash);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"#myDataTable=foo%3Dojkoj", "§§URL§§#myDataTable=foo%3Dojkoj"})
    @NotYetImplemented
    public void hashEncoding2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "window.location.hash = 'myDataTable=foo%3Dojkoj';\n"
            + "log(window.location.hash);\n"
            + "log(window.location.href);\n"
            + "</script></body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"#%C3%BC%C3%B6%C3%A4", "§§URL§§#%C3%BC%C3%B6%C3%A4"})
    public void hashEncoding3() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "window.location.hash = 'üöä';\n"
            + "log(window.location.hash);\n"
            + "log(window.location.href);\n"
            + "</script></body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("#%3Ca%3Efoobar%3C/a%3E")
    public void hash() throws Exception {
        checkHash(URL_FIRST + "?#<a>foobar</a>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void emptyHash() throws Exception {
        checkHash(URL_FIRST + "#");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void noHash() throws Exception {
        checkHash(URL_FIRST.toExternalForm());
    }

    private void checkHash(final String url) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.location.hash);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(html);
        loadPage2(html, new URL(url));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"#hello", "#hi"})
    public void setHash2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.location.hash = 'hello';\n"
            + "    log(window.location.hash);\n"
            + "    window.location.hash = '#hi';\n"
            + "    log(window.location.hash);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that setting <tt>location.href</tt> to a hash behaves like setting <tt>location.hash</tt>
     * (ie doesn't result in a server hit). See bug #688.
     * @throws Exception if an error occurs
     */
    @Test
    public void setHrefWithOnlyHash() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>document.location.href = '#x';</script></body></html>";
        loadPage2(html);
    }

    /**
     * Verifies that setting <tt>location.href</tt> to a new URL with a hash, where the only difference between the
     * new URL and the old URL is the hash, behaves like setting <tt>location.hash</tt> (ie doesn't result in a
     * server hit). See bug 2101735.
     * @throws Exception if an error occurs
     */
    @Test
    public void setHrefWithOnlyHash2() throws Exception {
        final String html = "<script>document.location.href = '" + URL_FIRST + "#x';</script>";
        loadPage2(html);
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void replace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        final WebDriver driver = loadPage2(html);

        assertTitle(driver, "Second");
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void replaceLastInHistory() throws Exception {
        final String startContent = DOCTYPE_HTML
                + "<html><head><title>First Page</title></head><body></body></html>";

        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second Page</title><script>\n"
            + "function doTest() {\n"
            + "  location.replace('" + URL_THIRD + "');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final String thirdContent = DOCTYPE_HTML
                + "<html><head><title>Third Page§</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        final WebDriver driver = loadPage2(startContent);
        driver.get(URL_SECOND.toExternalForm());

        verifyTitle2(DEFAULT_WAIT_TIME, driver, "Third Page");

        // navigate back
        driver.navigate().back();
        assertTitle(driver, "First Page");
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("on-load")
    public void replaceOnload() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>First</title>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "function doTest() {\n"
            + "  location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>Second</title></head>\n"
                + "<body onload='window.top.name += \"on-load\" + \"\\u00a7\";'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);

        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        assertTitle(driver, "Second");
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void replaceFirstInHistory() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First Page</title><script>\n"
            + "function doTest() {\n"
            + "  location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>Second Page</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPageWithAlerts2(firstContent);
        assertTitle(driver, "Second Page");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void assign() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    location.assign('" + URL_SECOND + "');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent, URL_FIRST);
        assertTitle(driver, "Second");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("on-load")
    public void assignOnload() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title><script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "  function test() {\n"
            + "    location.assign('" + URL_SECOND + "');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>Second</title></head>\n"
                + "<body onload='window.top.name += \"on-load\" + \"\\u00a7\";'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);

        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        assertTitle(driver, "Second");
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void assingByEquals() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    location = '" + URL_SECOND + "';\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent, URL_FIRST);
        assertTitle(driver, "Second");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("on-load")
    public void assingByEqualsOnload() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title><script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "  function test() {\n"
            + "    location  = '" + URL_SECOND + "';\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>Second</title></head>\n"
                + "<body onload='window.top.name += \"on-load\" + \"\\u00a7\";'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);

        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        assertTitle(driver, "Second");
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void changeLocationToNonHtml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <script>\n"
            + "      document.location.href = 'foo.txt';\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "foo.txt"), "bla bla", MimeType.TEXT_PLAIN);
        final WebDriver driver = loadPage2(html, URL_FIRST);
        assertTrue(driver.getPageSource().contains("bla bla"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void jsLocation() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "      document.location.href = 'javascript:log(\"foo\")';\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§", "§§URL§§", "§§URL§§"})
    public void testToString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var l = window.location;\n"
            + "  log(l);\n"
            + "  log('' + l);\n"
            + "  log(l.toString());\n"
            + "</script>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1 2 3")
    public void href_postponed() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  document.title += ' 1';\n"
            + "  self.frames['frame1'].document.location.href = '" + URL_SECOND + "';\n"
            + "  document.title += ' 2';\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <iframe name='frame1' id='frame1'/>\n"
            + "</body></html>";
        final String secondHtml = DOCTYPE_HTML
                + "<html><body><script>top.document.title += ' 3';</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = loadPage2(firstHtml);

        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "foo3.html", "foo2.html"})
    public void onlick_set_location_WithHref() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <a href='foo2.html' onclick='document.location = \"foo3.html\"'>click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);

        assertEquals(new String[] {}, getMockWebConnection().getRequestedUrls(URL_FIRST));
        driver.findElement(By.tagName("a")).click();

        assertEquals(getExpectedAlerts(), getMockWebConnection().getRequestedUrls(URL_FIRST));
        assertEquals(URL_FIRST + "foo2.html", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "foo3.html"})
    public void onlick_set_location_WithoutHref() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <a onclick='document.location = \"foo3.html\"'>click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();

        assertEquals(getExpectedAlerts(), getMockWebConnection().getRequestedUrls(URL_FIRST));
        assertEquals(URL_FIRST + "foo3.html", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"supported", "onhashchange §§URL§§#1 §§URL§§"})
    public void onHashChange() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + " if ('onhashchange' in window) { log('supported') }\n"
            + " function locationHashChanged(event) {\n"
            + "   if (event) {\n"
            + "     log('onhashchange ' + event.newURL + ' ' + event.oldURL);\n"
            + "   } else {\n"
            + "     log('onhashchange -');\n"
            + "   }\n"
            + " }\n"
            + " window.onhashchange = locationHashChanged;\n"
            + "</script>\n"
            + "<body>\n"
            + "  <a id='click' href='#1'>change hash</a>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts()[0]);
        Thread.sleep(100);

        driver.findElement(By.id("click")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("onhashchange #/foo")
    public void getNextPageWithOnlyHashChangeShouldTriggerHashChangeEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + " window.onhashchange = function(event) {\n"
            + "    log('onhashchange ' + window.location.hash);\n"
            + " }\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.navigate().to(driver.getCurrentUrl() + "#/foo");

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"supported", "onhashchange §§URL§§#1  §§URL§§"})
    public void onHashChangeJS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + " if ('onhashchange' in window) { log('supported') }\n"
            + " function locationHashChanged(event) {\n"
            + "   if (event) {\n"
            + "     log('onhashchange ' + event.newURL + '  ' + event.oldURL);\n"
            + "   } else {\n"
            + "     log('onhashchange -');\n"
            + "   }\n"
            + " }\n"
            + " window.onhashchange = locationHashChanged;\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button id='click' onclick='location.hash=1'>change hash</button>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);
        verifyWindowName2(driver, getExpectedAlerts()[0]);
        Thread.sleep(100);

        driver.findElement(By.id("click")).click();
        verifyWindowName2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§test.html")
    //real browsers don't show the alert, since it is quickly closed through JavaScript
    @NotYetImplemented
    public void locationAfterOpenClosePopup() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var win = window.open('" + URL_SECOND + "','test','',true);\n"
            + "      win.close();\n"
            + "      location.href = 'test.html';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button id='click' onclick='test()'>Test</button>\n"
            + "</body>\n"
            + "</html>";
        final String popup = DOCTYPE_HTML
              + "<html>\n"
              + "<head>\n"
              + "  <title>popup with script</title>\n"
              + "  <script>\n"
              + "    alert('the root of all evil');\n"
              + "  </script>\n"
              + "</head>\n"
              + "<body>Popup</body>\n"
              + "</html>";
        final String target = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <title>target</title>\n"
            + "</head>\n"
            + "<body>Target</body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, popup);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.html"), target);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("click")).click();
        try {
            assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
        }
        finally {
            releaseResources();
            shutDownAll();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void refererHeaderWhenSettingLocation() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Base</title></head>\n"
                + "<body>\n"
                + "  <a id='link' href='content.html' target='content'>Link</a>\n"
                + "  <a id='jsLink' href='#' onclick=\"javascript:window.location='content.html';\">jsLink</a>\n"
                + "</body></html>";

        final String content = DOCTYPE_HTML
                + "<html><head><title>Content</title></head><body><p>content</p></body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_FIRST, "content.html"), content);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);

        assertEquals(1, conn.getRequestCount());

        // click an anchor with href and target
        driver.findElement(By.id("link")).click();
        // because real browsers are doing the open async, we have to be a bit patient
        Thread.sleep(DEFAULT_WAIT_TIME);

        assertEquals(2, conn.getRequestCount());
        Map<String, String> lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], lastAdditionalHeaders.get(HttpHeader.REFERER));

        loadPage2(html);
        assertEquals(3, conn.getRequestCount());
        // click an anchor with onclick which sets frame.location
        driver.findElement(By.id("jsLink")).click();
        assertEquals(4, conn.getRequestCount());
        lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§menu.html")
    public void refererHeaderWhenSettingFrameLocation() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Frameset</title></head>\n"
                + "<frameset rows='20%,80%'>\n"
                + "  <frame src='menu.html' name='menu'>\n"
                + "  <frame src='content.html' name='content'>\n"
                + "</frameset></html>";

        final String menu = DOCTYPE_HTML
                + "<html><head><title>Menu</title></head>\n"
                + "<body>\n"
                + "  <a id='link' href='newContent.html' target='content'>Link</a>\n"
                + "  <a id='jsLink' href='#' "
                        + "onclick=\"javascript:top.content.location='newContent.html';\">jsLink</a>\n"
                + "</body></html>";

        final String content = DOCTYPE_HTML
                + "<html><head><title>Content</title></head><body><p>content</p></body></html>";
        final String newContent = DOCTYPE_HTML
                + "<html><head><title>New Content</title></head><body><p>new content</p></body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_FIRST, "menu.html"), menu);
        conn.setResponse(new URL(URL_FIRST, "content.html"), content);
        conn.setResponse(new URL(URL_FIRST, "newContent.html"), newContent);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);

        assertEquals(3, conn.getRequestCount());

        // click an anchor with href and target
        driver.switchTo().frame(0);
        driver.findElement(By.id("link")).click();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }
        assertEquals(4, conn.getRequestCount());
        Map<String, String> lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], lastAdditionalHeaders.get(HttpHeader.REFERER));

        // click an anchor with onclick which sets frame.location
        driver.findElement(By.id("jsLink")).click();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }
        assertEquals(5, conn.getRequestCount());
        lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void origin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(window.location.origin);\n"
                + "</script></body></html>";

        final URL url = URL_FIRST;
        final String origin = url.getProtocol() + "://" + url.getHost() + ':' + url.getPort();
        expandExpectedAlertsVariables(origin);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void documentOrigin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.location.origin);\n"
                + "</script></body></html>";

        final URL url = URL_FIRST;
        final String origin = url.getProtocol() + "://" + url.getHost() + ':' + url.getPort();
        expandExpectedAlertsVariables(origin);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            CHROME = "§§URL§§a.html?p1=sieben&p2",
            EDGE = "§§URL§§a.html?p1=sieben&p2")
    public void reloadGet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head></head>\n"
            + "  <body>\n"
            + "    <a href='javascript:window.location.reload(true);' id='link'>reload</a>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);
        final WebDriver driver = loadPage2(html, new URL(URL_FIRST + "a.html?p1=sieben&p2"));
        assertEquals(1, getMockWebConnection().getRequestCount());

        driver.findElement(By.id("link")).click();
        assertEquals(2, getMockWebConnection().getRequestCount());

        assertEquals(HttpMethod.GET, getMockWebConnection().getLastWebRequest().getHttpMethod());
        assertEquals(URL_FIRST + "a.html?p1=sieben&p2", getMockWebConnection().getLastWebRequest().getUrl());

        final List<NameValuePair> params = getMockWebConnection().getLastWebRequest().getRequestParameters();
        assertEquals(2, params.size());
        assertEquals("p1", params.get(0).getName());
        assertEquals("sieben", params.get(0).getValue());
        assertEquals("p2", params.get(1).getName());
        assertEquals("", params.get(1).getValue());

        expandExpectedAlertsVariables(URL_FIRST);
        final Map<String, String> additionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertNull(additionalHeaders.get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[0], "" + additionalHeaders.get(HttpHeader.REFERER));
        assertEquals("localhost:" + PORT, additionalHeaders.get(HttpHeader.HOST));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§a.html",
            FF = "null",
            FF_ESR = "null")
    public void reloadGetNoHash() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head></head>\n"
            + "  <body>\n"
            + "    <button onclick='window.location.reload();' id='reload'>reload</button>\n"
            + "    <button onclick='window.document.title=window.location.toString();' "
                        + "id='log'>log location</button>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);
        final WebDriver driver = loadPage2(html, new URL(URL_FIRST + "a.html"));
        assertEquals(1, getMockWebConnection().getRequestCount());

        driver.findElement(By.id("reload")).click();
        assertEquals(2, getMockWebConnection().getRequestCount());

        assertEquals(HttpMethod.GET, getMockWebConnection().getLastWebRequest().getHttpMethod());
        assertEquals(URL_FIRST + "a.html", getMockWebConnection().getLastWebRequest().getUrl());
        expandExpectedAlertsVariables(URL_FIRST);
        final Map<String, String> additionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertNull(additionalHeaders.get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[0], "" + additionalHeaders.get(HttpHeader.REFERER));
        assertEquals("localhost:" + PORT, additionalHeaders.get(HttpHeader.HOST));

        assertEquals(URL_FIRST + "a.html", driver.getCurrentUrl());
        driver.findElement(By.id("log")).click();
        assertEquals(URL_FIRST + "a.html", driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§a.html",
            FF = "null",
            FF_ESR = "null")
    public void reloadGetHashDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_SESSION_STORAGE_FUNCTION
            + "      function test() {\n"
            + "        log('1 ' + window.location);\n"
            + "        window.location.hash='1';\n"
            + "        log('2 ' + window.location);\n"
            + "        window.location.reload();\n"
            + "        log('3 ' + window.location);"
            + "      }\n"
            + "      log('load');\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <button onclick='test()' id='reload'>reload</button>\n"
            + "    <button onclick='log(\"4 \" + window.location.toString());' id='log'>log location</button>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);
        final WebDriver driver = loadPage2(html, new URL(URL_FIRST + "a.html"));
        assertEquals(1, getMockWebConnection().getRequestCount());

        driver.findElement(By.id("reload")).click();
        // real ff seems to process the reload a bit async
        Thread.sleep(100);
        assertEquals(2, getMockWebConnection().getRequestCount());

        assertEquals(HttpMethod.GET, getMockWebConnection().getLastWebRequest().getHttpMethod());
        assertEquals(URL_FIRST + "a.html", getMockWebConnection().getLastWebRequest().getUrl());
        expandExpectedAlertsVariables(URL_FIRST);
        final Map<String, String> additionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertNull(additionalHeaders.get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[0], "" + additionalHeaders.get(HttpHeader.REFERER));
        assertEquals("localhost:" + PORT, additionalHeaders.get(HttpHeader.HOST));

        assertEquals(URL_FIRST + "a.html#1", driver.getCurrentUrl());
        driver.findElement(By.id("log")).click();
        verifySessionStorage2(driver, new String[] {"load", "1 http://localhost:22222/a.html",
            "2 http://localhost:22222/a.html#1", "3 http://localhost:22222/a.html#1",
            "load", "4 http://localhost:22222/a.html#1"});
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§a.html",
            FF = "null",
            FF_ESR = "null")
    public void reloadGetHash() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head></head>\n"
            + "  <body>\n"
            + "    <button onclick='window.location.hash=\"1\";' id='changeHash'>change hash</button>\n"
            + "    <button onclick='window.location.reload();' id='reload'>reload</button>\n"
            + "    <button onclick='window.document.title=window.location.toString();' id='log'>log location</button>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);
        final WebDriver driver = loadPage2(html, new URL(URL_FIRST + "a.html"));
        assertEquals(1, getMockWebConnection().getRequestCount());

        // make sure changing the hash itself does not trigger the reload
        driver.findElement(By.id("changeHash")).click();
        assertEquals(1, getMockWebConnection().getRequestCount());

        // reload
        driver.findElement(By.id("reload")).click();
        assertEquals(2, getMockWebConnection().getRequestCount());

        assertEquals(HttpMethod.GET, getMockWebConnection().getLastWebRequest().getHttpMethod());
        assertEquals(URL_FIRST + "a.html", getMockWebConnection().getLastWebRequest().getUrl());
        expandExpectedAlertsVariables(URL_FIRST);
        final Map<String, String> additionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertNull(additionalHeaders.get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[0], "" + additionalHeaders.get(HttpHeader.REFERER));
        assertEquals("localhost:" + PORT, additionalHeaders.get(HttpHeader.HOST));

        assertEquals(URL_FIRST + "a.html#1", driver.getCurrentUrl());
        driver.findElement(By.id("log")).click();
        assertEquals(URL_FIRST + "a.html#1", driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§a.html",
            FF = "null",
            FF_ESR = "null")
    public void reloadGetHashChanged() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head></head>\n"
            + "  <body>\n"
            + "    <button onclick='window.location.hash=\"1\";' id='changeHash'>change hash</button>\n"
            + "    <button onclick='window.location.reload();' id='reload'>reload</button>\n"
            + "    <button onclick='window.document.title=window.location.toString();' id='log'>log location</button>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);
        final WebDriver driver = loadPage2(html, new URL(URL_FIRST + "a.html#abc"));
        assertEquals(1, getMockWebConnection().getRequestCount());

        // make sure changing the hash itself does not trigger the reload
        driver.findElement(By.id("changeHash")).click();
        assertEquals(1, getMockWebConnection().getRequestCount());

        // reload
        driver.findElement(By.id("reload")).click();
        assertEquals(2, getMockWebConnection().getRequestCount());

        assertEquals(HttpMethod.GET, getMockWebConnection().getLastWebRequest().getHttpMethod());
        assertEquals(URL_FIRST + "a.html", getMockWebConnection().getLastWebRequest().getUrl());
        expandExpectedAlertsVariables(URL_FIRST);
        final Map<String, String> additionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertNull(additionalHeaders.get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[0], "" + additionalHeaders.get(HttpHeader.REFERER));
        assertEquals("localhost:" + PORT, additionalHeaders.get(HttpHeader.HOST));

        assertEquals(URL_FIRST + "a.html#1", driver.getCurrentUrl());
        driver.findElement(By.id("log")).click();
        assertEquals(URL_FIRST + "a.html#1", driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"3", "§§URL§§", "§§URL§§/second/a.html?urlParam=urlVal"},
            EDGE = {"3", "§§URL§§", "§§URL§§/second/a.html?urlParam=urlVal"},
            FF = {"3", "§§URL§§", "§§URL§§/"},
            FF_ESR = {"3", "§§URL§§", "§§URL§§/"})
    public void reloadPost() throws Exception {
        final String form = DOCTYPE_HTML
              + "<html>\n"
              + "  <head></head>\n"
              + "  <body>\n"
              + "    <form method='POST' name='form' action='" + URL_SECOND + "a.html?urlParam=urlVal'>\n"
              + "      <input type='hidden' name='p1' value='seven'>\n"
              + "      <input type='hidden' name='p2' value=''>\n"
              + "      <input type='submit' id='enter' name='sub' value='ok'>\n"
              + "    </form>\n"
              + "  </body>\n"
              + "</html>";

        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head></head>\n"
            + "  <body>\n"
            + "    <a href='javascript:window.location.reload(true);' id='link'>reload</a>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);

        final WebDriver driver = loadPage2(form, URL_FIRST);
        assertEquals(1, getMockWebConnection().getRequestCount());
        driver.findElement(By.id("enter")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());

        driver.findElement(By.id("link")).click();

        // works only in the debugger
        try {
            driver.switchTo().alert().accept();
        }
        catch (final NoAlertPresentException e) {
            // ignore
        }
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());

        assertEquals(HttpMethod.POST, getMockWebConnection().getLastWebRequest().getHttpMethod());
        assertEquals(URL_SECOND + "a.html?urlParam=urlVal", getMockWebConnection().getLastWebRequest().getUrl());

        final List<NameValuePair> params = getMockWebConnection().getLastWebRequest().getRequestParameters();
        assertEquals(4, params.size());

        assertEquals("urlParam", params.get(0).getName());
        assertEquals("urlVal", params.get(0).getValue());

        assertEquals("p1", params.get(1).getName());
        assertEquals("seven", params.get(1).getValue());
        assertEquals("p2", params.get(2).getName());
        assertEquals("", params.get(2).getValue());

        assertEquals("sub", params.get(3).getName());
        assertEquals("ok", params.get(3).getValue());

        expandExpectedAlertsVariables("http://localhost:" + PORT);
        final Map<String, String> additionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[1], "" + additionalHeaders.get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[2], additionalHeaders.get(HttpHeader.REFERER));
        assertEquals("localhost:" + PORT, additionalHeaders.get(HttpHeader.HOST));
    }

    /**
     * Tests that location.reload() works correctly when invoked across frames.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§upper.html", "§§URL§§lower.html"})
    public void reloadAcrossFrames() throws Exception {
        final String framesetContent = DOCTYPE_HTML
            + "<html>\n"
            + "  <frameset rows='100,*'>\n"
            + "    <frame name='upper' src='upper.html'/>\n"
            + "    <frame name='lower' src='lower.html'/>\n"
            + "  </frameset>\n"
            + "</html>";

        final String upperContent = DOCTYPE_HTML
            + "<html><body><h1>upper</h1></body></html>";
        final String lowerContent = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  parent.upper.location.reload();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body><h1>lower</h1><button id='tester' onclick='test()'>test</button></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, framesetContent);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "upper.html"), upperContent);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "lower.html"), lowerContent);

        final WebDriver driver = loadPage2(framesetContent, URL_FIRST);

        expandExpectedAlertsVariables(URL_FIRST);
        driver.switchTo().frame("upper");
        assertEquals(getExpectedAlerts()[0],
                ((JavascriptExecutor) driver).executeScript("return document.location.href"));
        driver.switchTo().defaultContent();
        driver.switchTo().frame("lower");
        assertEquals(getExpectedAlerts()[1],
                ((JavascriptExecutor) driver).executeScript("return document.location.href"));

        driver.findElement(By.id("tester")).click();

        driver.switchTo().defaultContent();
        driver.switchTo().frame("upper");
        assertEquals(getExpectedAlerts()[0],
                ((JavascriptExecutor) driver).executeScript("return document.location.href"));
        driver.switchTo().defaultContent();
        driver.switchTo().frame("lower");
        assertEquals(getExpectedAlerts()[1],
                ((JavascriptExecutor) driver).executeScript("return document.location.href"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "ancestorOrigins,assign,hash,host,hostname,href,origin,"
                    + "pathname,port,protocol,reload,replace,search,toString",
            FF = "assign,hash,host,hostname,href,origin,"
               + "pathname,port,protocol,reload,replace,search,toString",
            FF_ESR = "assign,hash,host,hostname,href,origin,"
                   + "pathname,port,protocol,reload,replace,search,toString")
    @HtmlUnitNYI(CHROME = "assign,hash,host,hostname,href,origin,"
                        + "pathname,port,protocol,reload,replace,search,toString",
                 EDGE = "assign,hash,host,hostname,href,origin,"
                      + "pathname,port,protocol,reload,replace,search,toString")
    public void keys() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var keys = Object.keys(location);\n"
            + "  keys.sort();\n"
            + "  log(keys);\n"
            + "</script></head>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void protoKeys() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var keys = Object.keys(location.__proto__);\n"
            + "  keys.sort();\n"
            + "  log(keys);\n"
            + "</script></head>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"assign - {\"writable\":false,\"enumerable\":true,\"configurable\":false}",
             "hash - {\"enumerable\":true,\"configurable\":false}",
             "host - {\"enumerable\":true,\"configurable\":false}",
             "hostname - {\"enumerable\":true,\"configurable\":false}",
             "href - {\"enumerable\":true,\"configurable\":false}",
             "origin - {\"enumerable\":true,\"configurable\":false}",
             "pathname - {\"enumerable\":true,\"configurable\":false}",
             "port - {\"enumerable\":true,\"configurable\":false}",
             "protocol - {\"enumerable\":true,\"configurable\":false}",
             "reload - {\"writable\":false,\"enumerable\":true,\"configurable\":false}",
             "replace - {\"writable\":false,\"enumerable\":true,\"configurable\":false}",
             "search - {\"enumerable\":true,\"configurable\":false}",
             "toString - {\"writable\":false,\"enumerable\":true,\"configurable\":false}"})
    public void ownPropertyDescriptor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var props = ['assign', 'hash', 'host', 'hostname', 'href', "
                    + "'origin', 'pathname', 'port', 'protocol', 'reload', 'replace', 'search', 'toString'];\n"
            + "  props.forEach("
            + "    item => log(item + ' - ' + JSON.stringify(Object.getOwnPropertyDescriptor(window.location, item)))"
            + "  );\n"
            + "</script></head>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }
}
