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

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
    @Alerts("function URL() { [native code] }")
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
    @Alerts({"https://developer.mozilla.org/", "https://developer.mozilla.org/",
             "https://developer.mozilla.org/en-US/docs", "https://developer.mozilla.org/en-US/docs",
             "https://developer.mozilla.org/en-US/docs", "https://developer.mozilla.org/en-US/docs",
             "http://www.example.com/", "type error", "type error" })
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
    @Alerts("http://developer.mozilla.org")
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
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("http://developer.mozilla.org")
    public void originDefaultPort() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        var u = new URL('http://developer.mozilla.org:80/en-US/docs');\n"
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
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("http://developer.mozilla.org:1234")
    public void originPort() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        var u = new URL('http://developer.mozilla.org:1234/en-US/docs');\n"
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
    @Alerts({"", "a=u&x="})
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
    @Alerts({"", "a=u&x=", "x=22", "x=22"})
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
    @Alerts({"/", "/en-US/docs", "/en-US/docs"})
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
    @Alerts({"/path", "/path",
             "http://developer.mozilla.org/new/path?a=u&x",
             "http://developer.mozilla.org/?a=u&x"})
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
    @Alerts({"", "#abcd", "#bcd",
             "#hash", "http://developer.mozilla.org/?a=b#hash",
             "", "http://developer.mozilla.org/?a=b",
             "#undefined", "http://developer.mozilla.org/#undefined",
             "#null", "http://developer.mozilla.org/#null"})
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
    @Alerts({"developer.mozilla.org", "developer.mozilla.org",
             "new.host", "new.host:1234", "new.host:1234", "0.0.91.160:80",
             "0.0.0.17:80", "0.0.6.182:80",
             "new.host", "new.host",
             "developer.mozilla.org", "developer.mozilla.org:4097", "developer.mozilla.org:80"})
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
                    "%20%20", "https://%20%20/en-US/docs/Web/API/URL/host"})
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
    @Alerts({"https://developer.mozilla.org/en-US/docs/Web/API/URL/host",
             "http://new.com/href", "http://new.com/hrefWithPort"})
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
    @Alerts({"flabada",
             "", "https://anonymous@developer.mozilla.org/",
             "pass", "https://anonymous:pass@developer.mozilla.org/",
             "17", "https://anonymous:17@developer.mozilla.org/",
             "undefined", "https://anonymous:undefined@developer.mozilla.org/",
             "null", "https://anonymous:null@developer.mozilla.org/"})
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
    @Alerts({"80", "123", "", "https://mydomain.com/svn/Repos/",
             "", "http://mydomain.com/svn/Repos/"})
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
    @Alerts({"https:",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "ex-unknown"})
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

                        + "        u.protocol = ' http ';\n"
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
    @Alerts({"https:",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "ex-unknown"})
    public void protocol2() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://mydomain.com:80/svn/Repos/');\n"
                        + "        log(u.protocol);\n"

                        + "        u.protocol = 'http:';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = '';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'axdeg:';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'hTTp:';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'axdeg: ';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = ' axdeg: ';\n"
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
    @Alerts({"https:",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "http:", "http://mydomain.com/svn/Repos/",
             "ex-unknown"})
    public void protocol3() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://mydomain.com:80/svn/Repos/');\n"
                        + "        log(u.protocol);\n"

                        + "        u.protocol = 'http://www.htmlunit.org';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = '';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'axdeg://www.htmlunit.org';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'hTTp://www.htmlunit.org';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'axdeg://www.htmlunit.org ';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = ' axdeg://www.htmlunit.org ';\n"
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
    @Alerts(DEFAULT = {"https:",
                       "http:", "http://mydomain.com/svn/Repos/",
                       "https:", "https://mydomain.com/svn/Repos/",
                       "ftp:", "ftp://mydomain.com/svn/Repos/",
                       "ftp:", "ftp://mydomain.com/svn/Repos/",
                       "ws:", "ws://mydomain.com/svn/Repos/",
                       "wss:", "wss://mydomain.com/svn/Repos/",
                       "file:", "file://mydomain.com/svn/Repos/"},
            FF = {"https:",
                  "http:", "http://mydomain.com/svn/Repos/",
                  "https:", "https://mydomain.com/svn/Repos/",
                  "ftp:", "ftp://mydomain.com/svn/Repos/",
                  "ftp:", "ftp://mydomain.com/svn/Repos/",
                  "ws:", "ws://mydomain.com/svn/Repos/",
                  "wss:", "wss://mydomain.com/svn/Repos/",
                  "file:", "file:///svn/Repos/"},
            FF_ESR = {"https:",
                      "http:", "http://mydomain.com/svn/Repos/",
                      "https:", "https://mydomain.com/svn/Repos/",
                      "ftp:", "ftp://mydomain.com/svn/Repos/",
                      "ftp:", "ftp://mydomain.com/svn/Repos/",
                      "ws:", "ws://mydomain.com/svn/Repos/",
                      "wss:", "wss://mydomain.com/svn/Repos/",
                      "file:", "file:///svn/Repos/"})
    @HtmlUnitNYI(
            FF = {"https:",
                  "http:", "http://mydomain.com/svn/Repos/",
                  "https:", "https://mydomain.com/svn/Repos/",
                  "ftp:", "ftp://mydomain.com/svn/Repos/",
                  "ftp:", "ftp://mydomain.com/svn/Repos/",
                  "ws:", "ws://mydomain.com/svn/Repos/",
                  "wss:", "wss://mydomain.com/svn/Repos/",
                  "file:", "file://mydomain.com/svn/Repos/"},
            FF_ESR = {"https:",
                      "http:", "http://mydomain.com/svn/Repos/",
                      "https:", "https://mydomain.com/svn/Repos/",
                      "ftp:", "ftp://mydomain.com/svn/Repos/",
                      "ftp:", "ftp://mydomain.com/svn/Repos/",
                      "ws:", "ws://mydomain.com/svn/Repos/",
                      "wss:", "wss://mydomain.com/svn/Repos/",
                      "file:", "file://mydomain.com/svn/Repos/"})
    public void specialScheme() throws Exception {
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

                        + "        u.protocol = 'https';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'ftp';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'ftps';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'ws';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'wss';\n"
                        + "        log(u.protocol);\n"
                        + "        log(u.toString());\n"

                        + "        u.protocol = 'file';\n"
                        + "        log(u.protocol);\n"
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
    @Alerts({"?q=123",
             "?a=b&c=d", "https://developer.mozilla.org/search?a=b&c=d",
             "?a=7", "https://developer.mozilla.org/search?a=7",
             "?17", "https://developer.mozilla.org/search?17",
             "", "https://developer.mozilla.org/search",
             "?Html%20Unit", "https://developer.mozilla.org/search?Html%20Unit",
             "?Html?Unit", "https://developer.mozilla.org/search?Html?Unit",
             "?Html/Unit", "https://developer.mozilla.org/search?Html/Unit",
             "?undefined", "https://developer.mozilla.org/search?undefined",
             "?null", "https://developer.mozilla.org/search?null"})
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
    @Alerts({"https://developer.mozilla.org/search?q%20a=1%202%203", "?q%20a=1%202%203"})
    @HtmlUnitNYI(CHROME = {"https://developer.mozilla.org/search?q a=1 2 3", "?q a=1 2 3"},
                 EDGE = {"https://developer.mozilla.org/search?q a=1 2 3", "?q a=1 2 3"},
                 FF = {"https://developer.mozilla.org/search?q a=1 2 3", "?q a=1 2 3"},
                 FF_ESR = {"https://developer.mozilla.org/search?q a=1 2 3", "?q a=1 2 3"})
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
    @Alerts({"anonymous",
             "user", "https://user:flabada@developer.mozilla.org/",
             "", "https://:flabada@developer.mozilla.org/",
             "anonymous", "anonymous", "", "",
             "user", "https://user:pass@developer.mozilla.org/",
             "17", "https://17:pass@developer.mozilla.org/",
             "undefined", "https://undefined:pass@developer.mozilla.org/",
             "null", "https://null:pass@developer.mozilla.org/"})
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
    @Alerts({"function URL() { [native code] }",
             "function URL() { [native code] }",
             "function URL() { [native code] }",
             "https://developer.mozilla.org/",
             "https://developer.mozilla.org/",
             "https://developer.mozilla.org/"})
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
    @Alerts("https://developer.mozilla.org/")
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"https://htmlunit.org/", "https://htmlunit.org/", "true"})
    public void webkitURL() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (typeof window.URL === 'function') {\n"
            + "    var url = new URL('https://htmlunit.org');\n"
            + "    var wkUrl = new webkitURL('https://htmlunit.org');\n"
            + "    log(url);\n"
            + "    log(wkUrl);\n"
            + "    log(Object.getPrototypeOf(url) == Object.getPrototypeOf(wkUrl));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
