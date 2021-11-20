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
package com.gargoylesoftware.htmlunit.javascript.host;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link URL}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class URLTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function URL() { [native code] }",
            IE = "[object URL]")
    public void windowURL() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(window.URL);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"https://developer.mozilla.org/", "https://developer.mozilla.org/",
                       "https://developer.mozilla.org/en-US/docs", "https://developer.mozilla.org/en-US/docs",
                       "https://developer.mozilla.org/en-US/docs", "https://developer.mozilla.org/en-US/docs",
                       "http://www.example.com/", "type error", "type error" },
            IE = {})
    public void ctor() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        log(new URL('/', 'https://developer.mozilla.org'));\n"
            + "        var b = new URL('https://developer.mozilla.org');\n"
            + "        log(b);\n"
            + "        log(new URL('en-US/docs', b));\n"
            + "        var d = new URL('/en-US/docs', b);\n"
            + "        log(d);\n"
            + "        log(new URL('/en-US/docs', d));\n"
            + "        log(new URL('/en-US/docs', 'https://developer.mozilla.org/fr-FR/toto'));\n"
            + "        log(new URL('http://www.example.com', 'https://developers.mozilla.com'));\n"
            + "        try {\n"
            + "          new URL('/en-US/docs', '');\n"
            + "        } catch(e) { log('type error'); }\n"
            + "        try {\n"
            + "          new URL('/en-US/docs');\n"
            + "        } catch(e) { log('type error'); }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "http://developer.mozilla.org",
            IE = {})
    public void origin() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        var u = new URL('http://developer.mozilla.org/en-US/docs');\n"
            + "        log(u.origin);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void createObjectURL() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"

            + "    var url = window.URL.createObjectURL(files[0]);\n"
            + "    alert(url);\n"
            + "    window.URL.revokeObjectURL(url);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitUploadTest", ".txt");
        try {
            FileUtils.writeStringToFile(tstFile, "Hello HtmlUnit", ISO_8859_1);

            final String path = tstFile.getCanonicalPath();
            driver.findElement(By.name("fileupload")).sendKeys(path);

            driver.findElement(By.id("testBtn")).click();

            final String url = getCollectedAlerts(driver, 1).get(0);
            Assert.assertTrue(url, url.startsWith("blob:"));
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "a=u&x="},
            IE = {})
    public void searchParams() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        var u = new URL('http://developer.mozilla.org/en-US/docs');\n"
            + "        log(u.searchParams);\n"
            + "        u = new URL('http://developer.mozilla.org/en-US/docs?a=u&x');\n"
            + "        log(u.searchParams);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "a=u&x=", "x=22", "x=22"},
            IE = {})
    public void searchParamsSyncedWithUrlChanges() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        var u = new URL('http://developer.mozilla.org/en-US/docs');\n"
            + "        log(u.searchParams);\n"
            + "        u = new URL('http://developer.mozilla.org/en-US/docs?a=u&x');\n"

            + "        var param = u.searchParams;\n"
            + "        log(param);\n"

            + "        u.search = 'x=22';\n"
            + "        log(u.searchParams);\n"
            + "        log(param);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"/", "/en-US/docs", "/en-US/docs"},
            IE = {})
    public void getPathName() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('http://developer.mozilla.org');\n"
                        + "        log(u.pathname);\n"
                        + "        u = new URL('http://developer.mozilla.org/en-US/docs');\n"
                        + "        log(u.pathname);\n"
                        + "        u = new URL('http://developer.mozilla.org/en-US/docs?a=u&x');\n"
                        + "        log(u.pathname);\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"/path", "/path",
                       "http://developer.mozilla.org/new/path?a=u&x",
                       "http://developer.mozilla.org/?a=u&x"},
            IE = {})
    public void setPathName() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('http://developer.mozilla.org');\n"
                        + "        u.pathname = 'path';\n"
                        + "        log(u.pathname);\n"
                        + "        u.pathname = '/path';\n"
                        + "        log(u.pathname);\n"
                        + "        u = new URL('http://developer.mozilla.org/en-US/docs?a=u&x');\n"
                        + "        u.pathname = 'new/path';"
                        + "        log(u.toString());\n"
                        + "        u.pathname='';\n"
                        + "        log(u.toString());\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"", "#abcd", "#bcd",
                       "#hash", "http://developer.mozilla.org/?a=b#hash",
                       "", "http://developer.mozilla.org/?a=b",
                       "#undefined", "http://developer.mozilla.org/#undefined",
                       "#null", "http://developer.mozilla.org/#null"},
            IE = {})
    public void hash() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('http://developer.mozilla.org');\n"
                        + "        log(u.hash);\n"

                        + "        u = new URL('http://developer.mozilla.org#abcd');\n"
                        + "        log(u.hash);\n"

                        + "        u = new URL('http://developer.mozilla.org?a=b#bcd');\n"
                        + "        log(u.hash);\n"

                        + "        u.hash = 'hash';\n"
                        + "        log(u.hash);\n"
                        + "        log(u.toString());\n"

                        + "        u.hash = '';\n"
                        + "        log(u.hash);\n"
                        + "        log(u.toString());\n"

                        + "        u = new URL('http://developer.mozilla.org#bcd');\n"
                        + "        u.hash = undefined;\n"
                        + "        log(u.hash);\n"
                        + "        log(u.toString());\n"

                        + "        u = new URL('http://developer.mozilla.org#bcd');\n"
                        + "        u.hash = null;\n"
                        + "        log(u.hash);\n"
                        + "        log(u.toString());\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"developer.mozilla.org", "developer.mozilla.org",
                       "new.host", "new.host:1234", "new.host:1234", "0.0.91.160:80",
                       "0.0.0.17:80", "0.0.6.182:80",
                       "new.host", "new.host",
                       "developer.mozilla.org", "developer.mozilla.org:4097", "developer.mozilla.org:80"},
            IE = {})
    public void host() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://developer.mozilla.org/en-US/docs/Web/API/URL/host');\n"
                        + "        log(u.host);\n"

                        + "        u.host = '';\n"
                        + "        log(u.host);\n"

                        + "        u.host = 'new.host';\n"
                        + "        log(u.host);\n"

                        + "        u.host = 'new.host:1234';\n"
                        + "        log(u.host);\n"

                        + "        u.host = ':447';\n"
                        + "        log(u.host);\n"

                        + "        u.host = '23456:80';\n"
                        + "        log(u.host);\n"

                        + "        u.host = 17;\n"
                        + "        log(u.host);\n"

                        + "        u.host = 1718;\n"
                        + "        log(u.host);\n"

                        + "        var u = new URL('https://host.com');\n"
                        + "        u.host = 'new.host:443';\n" //same port as protocol
                        + "        log(u.host);\n"

                        + "        var u = new URL('http://host.com');\n"
                        + "        u.host = 'new.host:80';\n" //same port as protocol
                        + "        log(u.host);\n"

                        + "        u = new URL('https://developer.mozilla.org/en-US/docs/Web/API/URL/host');\n"
                        + "        log(u.host);"

                        + "        u = new URL('https://developer.mozilla.org:4097/en-US/docs/Web/API/URL/host');\n"
                        + "        log(u.host);"

                        + "        u = new URL('https://developer.mozilla.org:80/en-US/docs/Web/API/URL/host');\n"
                        + "        log(u.host);"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"developer.mozilla.org",
                       "developer.mozilla.org", "https://developer.mozilla.org/en-US/docs/Web/API/URL/host",
                       "newhost", "https://newhost/en-US/docs/Web/API/URL/host",
                       "newhost", "https://newhost/en-US/docs/Web/API/URL/host"},
            CHROME =  {"developer.mozilla.org",
                       "developer.mozilla.org", "https://developer.mozilla.org/en-US/docs/Web/API/URL/host",
                       "newhost", "https://newhost/en-US/docs/Web/API/URL/host",
                       "%20%20", "https://%20%20/en-US/docs/Web/API/URL/host"},
            EDGE = {"developer.mozilla.org",
                    "developer.mozilla.org", "https://developer.mozilla.org/en-US/docs/Web/API/URL/host",
                    "newhost", "https://newhost/en-US/docs/Web/API/URL/host",
                    "%20%20", "https://%20%20/en-US/docs/Web/API/URL/host"},
            IE = {})
    @HtmlUnitNYI(CHROME =  {"developer.mozilla.org",
                            "developer.mozilla.org", "https://developer.mozilla.org/en-US/docs/Web/API/URL/host",
                            "newhost", "https://newhost/en-US/docs/Web/API/URL/host",
                            "%20%20", "https:// /en-US/docs/Web/API/URL/host"},
                EDGE = {"developer.mozilla.org",
                        "developer.mozilla.org", "https://developer.mozilla.org/en-US/docs/Web/API/URL/host",
                        "newhost", "https://newhost/en-US/docs/Web/API/URL/host",
                        "%20%20", "https:// /en-US/docs/Web/API/URL/host"})
    public void hostname() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://developer.mozilla.org:443/en-US/docs/Web/API/URL/host');\n"
                        + "        log(u.hostname);\n"

                        + "        u.hostname = '';\n"
                        + "        log(u.hostname);\n"
                        + "        log(u.toString());\n"

                        + "        u.hostname = 'newhost';\n"
                        + "        log(u.hostname);\n"
                        + "        log(u.toString());\n"

                        + "        u.hostname = '  ';\n"
                        + "        log(u.hostname);\n"
                        + "        log(u.toString());\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"https://developer.mozilla.org/en-US/docs/Web/API/URL/host",
                       "http://new.com/href", "http://new.com/hrefWithPort"},
            IE = {})
    public void href() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://developer.mozilla.org:443/en-US/docs/Web/API/URL/host');\n"
                        + "        log(u.href);\n"

                        + "        u.href = 'http://new.com/href';\n"
                        + "        log(u.href);\n"

                        + "        u.href = 'http://new.com:80/hrefWithPort';\n"
                        + "        log(u.href);\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"flabada",
                       "", "https://anonymous@developer.mozilla.org/",
                       "pass", "https://anonymous:pass@developer.mozilla.org/",
                       "17", "https://anonymous:17@developer.mozilla.org/",
                       "undefined", "https://anonymous:undefined@developer.mozilla.org/",
                       "null", "https://anonymous:null@developer.mozilla.org/"},
            IE = {})
    public void password() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://anonymous:flabada@developer.mozilla.org');\n"
                        + "        log(u.password);\n"

                        + "        u.password = '';\n"
                        + "        log(u.password);\n"
                        + "        log(u.toString());\n"

                        + "        u.password = 'pass';\n"
                        + "        log(u.password);\n"
                        + "        log(u.toString());\n"

                        + "        u.password = 17;\n"
                        + "        log(u.password);\n"
                        + "        log(u.toString());\n"

                        + "        u.password = undefined;\n"
                        + "        log(u.password);\n"
                        + "        log(u.toString());\n"

                        + "        u.password = null;\n"
                        + "        log(u.password);\n"
                        + "        log(u.toString());\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"80", "123", "", "https://mydomain.com/svn/Repos/",
                       "", "http://mydomain.com/svn/Repos/"},
            IE = {})
    public void port() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://mydomain.com:80/svn/Repos/');\n"
                        + "        log(u.port);\n"
                        + "        u.port = '123';\n"
                        + "        log(u.port);\n"
                        + "        u.port = '443';\n"
                        + "        log(u.port);\n"
                        + "        log(u.toString());\n"
                        + "        u = new URL('http://mydomain.com:123/svn/Repos/');\n"
                        + "        u.port = '80';\n"
                        + "        log(u.port);\n"
                        + "        log(u.toString());\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"https:",
                       "http:", "http://mydomain.com/svn/Repos/",
                       "http:", "http://mydomain.com/svn/Repos/",
                       "axdeg:", "axdeg://mydomain.com/svn/Repos/",
                       "http:", "http://mydomain.com/svn/Repos/",
                       "http:", "http://mydomain.com/svn/Repos/",
                       "http:", "http://mydomain.com/svn/Repos/",
                       "null:", "null://mydomain.com/svn/Repos/",
                       "ex-unknown"},
            IE = {})
    public void protocol() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://mydomain.com:80/svn/Repos/');\n"
                        + "        log(u.protocol);\n"

                        + "        u.protocol = 'http';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = '';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'axdeg';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'hTTp';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'axdeg ';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = ' axdeg ';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        try {\n"
                        + "        u.protocol = null;\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"
                        + "        } catch(e) { log('ex-null') }\n"

                        + "        try {\n"
                        + "        u.protocol = unknown;\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"
                        + "        } catch(e) { log('ex-unknown') }\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"?q=123",
                       "?a=b&c=d", "https://developer.mozilla.org/search?a=b&c=d",
                       "?a=7", "https://developer.mozilla.org/search?a=7",
                       "?17", "https://developer.mozilla.org/search?17",
                       "", "https://developer.mozilla.org/search",
                       "?Html%20Unit", "https://developer.mozilla.org/search?Html%20Unit",
                       "?Html?Unit", "https://developer.mozilla.org/search?Html?Unit",
                       "?Html/Unit", "https://developer.mozilla.org/search?Html/Unit",
                       "?undefined", "https://developer.mozilla.org/search?undefined",
                       "?null", "https://developer.mozilla.org/search?null"},
            IE = {})
    public void search() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://developer.mozilla.org/search?q=123');\n"
                        + "        log(u.search);\n"

                        + "        u.search = 'a=b&c=d';\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"

                        + "        u.search = '?a=7';\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"

                        + "        u.search = 17;\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"

                        + "        u.search = '';\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"

                        + "        u.search = 'Html Unit';\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"

                        + "        u.search = 'Html?Unit';\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"

                        + "        u.search = 'Html/Unit';\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"

                        + "        u = new URL('https://developer.mozilla.org/search?q=123');\n"
                        + "        u.search = undefined;\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"

                        + "        u.search = null;\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"https://developer.mozilla.org/search?q%20a=1%202%203", "?q%20a=1%202%203"},
            IE = {})
    @HtmlUnitNYI(CHROME = {"https://developer.mozilla.org/search?q a=1 2 3", "?q a=1 2 3"},
                 EDGE = {"https://developer.mozilla.org/search?q a=1 2 3", "?q a=1 2 3"},
                 FF = {"https://developer.mozilla.org/search?q a=1 2 3", "?q a=1 2 3"},
                 FF78 = {"https://developer.mozilla.org/search?q a=1 2 3", "?q a=1 2 3"})
    public void searchEncoding() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://developer.mozilla.org/search?q a=1 2 3');\n"
                        + "        log(u);\n"
                        + "        log(u.search);\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"anonymous",
                       "user", "https://user:flabada@developer.mozilla.org/",
                       "", "https://:flabada@developer.mozilla.org/",
                       "anonymous", "anonymous", "", "",
                       "user", "https://user:pass@developer.mozilla.org/",
                       "17", "https://17:pass@developer.mozilla.org/",
                       "undefined", "https://undefined:pass@developer.mozilla.org/",
                       "null", "https://null:pass@developer.mozilla.org/"},
            IE = {})
    public void username() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://anonymous:flabada@developer.mozilla.org');\n"
                        + "        log(u.username);\n"

                        + "        u.username = 'user';\n"
                        + "        log(u.username);\n"
                        + "        log(u.toString());\n"

                        + "        u.username = '';\n"
                        + "        log(u.username);\n"
                        + "        log(u.toString());\n"

                        + "        u = new URL('https://anonymous@developer.mozilla.org');\n"
                        + "        log(u.username);\n"

                        + "        u = new URL('https://anonymous:@developer.mozilla.org');\n"
                        + "        log(u.username);\n"

                        + "        u = new URL('https://developer.mozilla.org:443');\n"
                        + "        log(u.username);\n"

                        + "        u = new URL('https://:pass@developer.mozilla.org:443');\n"
                        + "        log(u.username);\n"

                        + "        u.username = 'user';\n"
                        + "        log(u.username);\n"
                        + "        log(u.toString());\n"

                        + "        u.username = 17;\n"
                        + "        log(u.username);\n"
                        + "        log(u.toString());\n"

                        + "        u.username = undefined;\n"
                        + "        log(u.username);\n"
                        + "        log(u.toString());\n"

                        + "        u.username = null;\n"
                        + "        log(u.username);\n"
                        + "        log(u.toString());\n"
                        + "      }\n"
                        + "    }\n"
                        + "  </script>\n"
                        + "</head>\n"
                        + "<body onload='test()'>\n"
                        + "</body>\n"
                        + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function URL() { [native code] }",
                       "function URL() { [native code] }",
                       "function URL() { [native code] }",
                       "https://developer.mozilla.org/",
                       "https://developer.mozilla.org/",
                       "https://developer.mozilla.org/"},
            IE = {})
    public void testToString() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (typeof window.URL === 'function') {\n"
            + "    log(URL);\n"
            + "    log('' + URL);\n"
            + "    log(URL.toString());\n"

            + "    var u = new URL('/', 'https://developer.mozilla.org');\n"
            + "    log(u);\n"
            + "    log('' + u);\n"
            + "    log(u.toString());\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = "https://developer.mozilla.org/",
            IE = {})
    public void testToJSON() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  if (typeof window.URL === 'function') {\n"
                + "    var u = new URL('/', 'https://developer.mozilla.org');\n"
                + "    log(u.toJSON());\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
