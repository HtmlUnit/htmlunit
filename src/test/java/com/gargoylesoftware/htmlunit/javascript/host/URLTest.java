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
            FF = "function URL() {\n    [native code]\n}",
            FF78 = "function URL() {\n    [native code]\n}",
            IE = "[object URL]")
    public void windowURL() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(window.URL);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "https://developer.mozilla.org/", "https://developer.mozilla.org/",
                "https://developer.mozilla.org/en-US/docs", "https://developer.mozilla.org/en-US/docs",
                "https://developer.mozilla.org/en-US/docs", "https://developer.mozilla.org/en-US/docs",
                "http://www.example.com/", "type error", "type error" },
            IE = {})
    public void ctor() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        alert(new URL('/', 'https://developer.mozilla.org'));\n"
            + "        var b = new URL('https://developer.mozilla.org');\n"
            + "        alert(b);\n"
            + "        alert(new URL('en-US/docs', b));\n"
            + "        var d = new URL('/en-US/docs', b);\n"
            + "        alert(d);\n"
            + "        alert(new URL('/en-US/docs', d));\n"
            + "        alert(new URL('/en-US/docs', 'https://developer.mozilla.org/fr-FR/toto'));\n"
            + "        alert(new URL('http://www.example.com', 'https://developers.mozilla.com'));\n"
            + "        try {\n"
            + "          new URL('/en-US/docs', '');\n"
            + "        } catch(e) { alert('type error'); }\n"
            + "        try {\n"
            + "          new URL('/en-US/docs');\n"
            + "        } catch(e) { alert('type error'); }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
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
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        var u = new URL('http://developer.mozilla.org/en-US/docs');\n"
            + "        alert(u.origin);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
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
            + "<head><title>foo</title>\n"
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
            + "    function test() {\n"
            + "      if (typeof window.URL === 'function') {\n"
            + "        var u = new URL('http://developer.mozilla.org/en-US/docs');\n"
            + "        alert(u.searchParams);\n"
            + "        u = new URL('http://developer.mozilla.org/en-US/docs?a=u&x');\n"
            + "        alert(u.searchParams);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
