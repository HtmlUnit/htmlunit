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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

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
    @Alerts(DEFAULT = {"", "#hash", ""},
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
                        + "        u.hash = 'hash';\n"
                        + "        log(u.hash);\n"
                        + "        u.hash = '';\n"
                        + "        log(u.hash);\n"
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
    @Alerts(DEFAULT = {"developer.mozilla.org", "developer.mozilla.org", "new.host", "new.host:1234",
            "new.host", "new.host",
            "developer.mozilla.org", "developer.mozilla.org:4097"},
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
            "https://developer.mozilla.org:443/en-US/docs/Web/API/URL/host",
            "https://newhost:443/en-US/docs/Web/API/URL/host",},
            IE = {})
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
    @Alerts(DEFAULT = {"https://developer.mozilla.org:443/en-US/docs/Web/API/URL/host",
            "http://new.com/href"},
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
    @Alerts(DEFAULT = {"flabada", "",
            "https://anonymous@developer.mozilla.org/en-US/docs/Web/API/URL/password",
            "https://anonymous:pass@developer.mozilla.org/en-US/docs/Web/API/URL/password"},
            IE = {})
    public void password() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://anonymous:flabada@developer.mozilla.org/en-US/docs/Web/API/URL/password');\n"
                        + "        log(u.password);\n"
                        + "        u.password = '';\n"
                        + "        log(u.password);\n"
                        + "        log(u.toString());\n"
                        + "        u.password = 'pass';\n"
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
            "", "http://mydomain.com/svn/Repos/",},
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
    @Alerts(DEFAULT = {"https:", "http:",
            "http://mydomain.com:80/svn/Repos/", "http://mydomain.com:80/svn/Repos/"},
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
    @Alerts(DEFAULT = {"q=123", "a=b&c=d", "https://developer.mozilla.org/en-US/docs/Web/API/URL/search?a=b&c=d",
            "", "https://developer.mozilla.org/en-US/docs/Web/API/URL/search"},
            IE = {})
    public void search() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://developer.mozilla.org/en-US/docs/Web/API/URL/search?q=123');\n"
                        + "        log(u.search);\n"
                        + "        u.search = 'a=b&c=d';\n"
                        + "        log(u.search);\n"
                        + "        log(u.toString());\n"
                        + "        u.search = '';\n"
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
    @Alerts(DEFAULT = {"anonymous", "user", "",
            "https://:flabada@developer.mozilla.org/en-US/docs/Web/API/URL/username",
            "https://developer.mozilla.org/en-US/docs/Web/API/URL/username"},
            IE = {})
    public void username() throws Exception {
        final String html =
                "<html>\n"
                        + "<head>\n"
                        + "  <script>\n"
                        + LOG_TITLE_FUNCTION
                        + "    function test() {\n"
                        + "      if (typeof window.URL === 'function') {\n"
                        + "        var u = new URL('https://anonymous:flabada@developer.mozilla.org/en-US/docs/Web/API/URL/username');\n"
                        + "        log(u.username);\n"
                        + "        u.username = 'user';\n"
                        + "        log(u.username);\n"
                        + "        u.username = '';\n"
                        + "        log(u.username);\n"
                        + "        log(u.toString());\n"
                        + "        var u = new URL('https://anonymous@developer.mozilla.org/en-US/docs/Web/API/URL/username');\n"
                        + "        u.username = '';\n"
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
}
