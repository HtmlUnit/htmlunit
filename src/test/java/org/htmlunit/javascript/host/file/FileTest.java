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
package org.htmlunit.javascript.host.file;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.File;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link org.htmlunit.javascript.host.file.File}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class FileTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "ScriptExceptionTest1.txt",
                       "Sun Jul 26 2015 10:21:47 GMT-0400 (Eastern Daylight Time)",
                       "1437920507000", "", "14", MimeType.TEXT_PLAIN},
            FF = {"1", "ScriptExceptionTest1.txt", "undefined",
                  "1437920507000", "", "14", MimeType.TEXT_PLAIN},
            FF_ESR = {"1", "ScriptExceptionTest1.txt", "undefined",
                      "1437920507000", "", "14", MimeType.TEXT_PLAIN})
    public void properties() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"
            + "    log(files.length);\n"

            + "    var file = files[0];\n"
            + "    log(file.name);\n"
            + "    log(file.lastModifiedDate);\n"
            + "    log(file.lastModified);\n"
            + "    log(file.webkitRelativePath);\n"
            + "    log(file.size);\n"
            + "    log(file.type);\n"
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

            // do not use millis here because different file systems
            // have different precisions
            assertTrue(tstFile.setLastModified(1437920507000L));

            final String path = tstFile.getCanonicalPath();
            driver.findElement(By.name("fileupload")).sendKeys(path);

            driver.findElement(By.id("testBtn")).click();

            final String[] expected = getExpectedAlerts();
            if (expected.length > 1) {
                expected[1] = tstFile.getName();
            }

            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "function", "Hello HtmlUnit"})
    public void text() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"
            + "    log(files.length);\n"

            + "    var file = files[0];\n"
            + "    log(typeof file.text);\n"

            + "    try {\n"
            + "      file.text().then(function(text) { log(text); });\n"
            + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
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

            verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain")
    public void typeTxt() throws Exception {
        type(".txt");
        type(".tXT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void typeHtmlUnit() throws Exception {
        type(".htmlunit");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void typeEmpty() throws Exception {
        type("");
    }

    private void type(final String extension) throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"

            + "    var file = files[0];\n"
            + "    log(file.type);\n"
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

        final File tstFile = File.createTempFile("HtmlUnitUploadTest", extension);
        try {
            final String path = tstFile.getCanonicalPath();
            driver.findElement(By.name("fileupload")).sendKeys(path);

            driver.findElement(By.id("testBtn")).click();

            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "TypeError true"})
    public void ctorNoArgs() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(File in window);\n"

                + "    try {\n"
                + "      log(new File());\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
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
    @Alerts({"[object File]", "htMluniT.txt", "", "true", "0", ""})
    public void ctorEmpty() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var file = new File([], 'htMluniT.txt');\n"
                + "      log(file);\n"
                + "      log(file.name);\n"
                + "      log(file.type);\n"
                + "      log(file.lastModified >= now);\n"
                + "      log(file.size);\n"

                + "      file.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object File]", "htMluniT.txt", "", "true", "8", "HtmlUnit"})
    public void ctorString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var file = new File(['HtmlUnit'], 'htMluniT.txt');\n"
                + "      log(file);\n"
                + "      log(file.name);\n"
                + "      log(file.type);\n"
                + "      log(file.lastModified >= now);\n"
                + "      log(file.size);\n"

                + "      file.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object File]", "htMluniT.txt", "application/octet-stream", "1234567", "8",
             "HtmlUnit"})
    public void ctorStringWithOptions() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var file = new File(['HtmlUnit'], 'htMluniT.txt',"
                              + "{type: 'application/octet-stream', lastModified: '1234567'});\n"
                + "      log(file);\n"
                + "      log(file.name);\n"
                + "      log(file.type);\n"
                + "      log(file.lastModified);\n"
                + "      log(file.size);\n"

                + "      file.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object File]", "htMluniT.txt", "", "true", "16",
             "HtmlUnitis great"})
    public void ctorStrings() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var file = new File(['Html', 'Unit', 'is great'], 'htMluniT.txt');\n"
                + "      log(file);\n"
                + "      log(file.name);\n"
                + "      log(file.type);\n"
                + "      log(file.lastModified >= now);\n"
                + "      log(file.size);\n"

                + "      file.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object File]", "htMluniT.txt", "", "true", "12", "HtmlUnitMMMK"})
    public void ctorMixed() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var nab = new ArrayBuffer(2);\n"
                + "      var nabv = new Uint8Array(nab, 0, 2);\n"
                + "      nabv.set([77, 77], 0);\n"
                + "      var file = new File(['HtmlUnit',"
                                      + "nab, new Int8Array([77,75])], 'htMluniT.txt');\n"
                + "      log(file);\n"
                + "      log(file.name);\n"
                + "      log(file.type);\n"
                + "      log(file.lastModified >= now);\n"
                + "      log(file.size);\n"

                + "      file.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Sun Jul 26 2015 10:21:47 GMT-0400 (Eastern Daylight Time)",
            FF = "undefined",
            FF_ESR = "undefined")
    public void lastModifiedDate() throws Exception {
        lastModifiedDate(getBrowserVersion().getSystemTimezone().getID(), getBrowserVersion().getBrowserLocale());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Sun Jul 26 2015 14:21:47 GMT+0000 (Greenwich Mean Time)",
            FF = "undefined",
            FF_ESR = "undefined")
    public void lastModifiedDateGMT() throws Exception {
        lastModifiedDate("GMT", getBrowserVersion().getBrowserLocale());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Sun Jul 26 2015 14:21:47 GMT+0000 (Coordinated Universal Time)",
            FF = "undefined",
            FF_ESR = "undefined")
    public void lastModifiedDateUTC() throws Exception {
        lastModifiedDate("UTC", getBrowserVersion().getBrowserLocale());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Sun Jul 26 2015 16:21:47 GMT+0200 (Mitteleuropäische Sommerzeit)",
            FF = "undefined",
            FF_ESR = "undefined")
    public void lastModifiedDateBerlin() throws Exception {
        lastModifiedDate("Europe/Berlin", Locale.GERMANY);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Sun Jul 26 2015 23:21:47 GMT+0900 (日本標準時)",
            FF = "undefined",
            FF_ESR = "undefined")
    public void lastModifiedDateJST() throws Exception {
        lastModifiedDate("JST", Locale.JAPAN);
    }

    private void lastModifiedDate(final String tz, final Locale locale) throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"
            + "    var file = files[0];\n"
            + "    log(file.lastModifiedDate);\n"
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

        final File tstFile = File.createTempFile("HtmlUnitUploadTest", ".txt");
        try {
            FileUtils.writeStringToFile(tstFile, "Hello HtmlUnit", ISO_8859_1);

            // do not use millis here because different file systems
            // have different precisions
            assertTrue(tstFile.setLastModified(1437920507000L));

            final String path = tstFile.getCanonicalPath();

            shutDownAll();
            try {
                final BrowserVersion.BrowserVersionBuilder builder
                    = new BrowserVersion.BrowserVersionBuilder(getBrowserVersion());
                builder.setSystemTimezone(TimeZone.getTimeZone(tz));
                builder.setBrowserLanguage(locale.toLanguageTag());
                setBrowserVersion(builder.build());

                final WebDriver driver = loadPage2(html);
                driver.findElement(By.name("fileupload")).sendKeys(path);

                driver.findElement(By.id("testBtn")).click();

                final String[] expected = getExpectedAlerts();
                if (expected.length > 1) {
                    expected[1] = tstFile.getName();
                }

                verifyTitle2(driver, getExpectedAlerts());
            }
            finally {
                shutDownAll();
            }
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }
}
