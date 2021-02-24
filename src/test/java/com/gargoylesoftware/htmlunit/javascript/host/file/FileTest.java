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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.file.File}.
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
            FF78 = {"1", "ScriptExceptionTest1.txt", "undefined",
                    "1437920507000", "", "14", MimeType.TEXT_PLAIN},
            IE = {"1", "ScriptExceptionTest1.txt",
                            "Sun Jul 26 2015 10:21:47 GMT-0400 (Eastern Daylight Time)",
                            "undefined", "undefined", "14", MimeType.TEXT_PLAIN})
    public void properties() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"
            + "    alert(files.length);\n"

            + "    var file = files[0];\n"
            + "    alert(file.name);\n"
            + "    alert(file.lastModifiedDate);\n"
            + "    alert(file.lastModified);\n"
            + "    alert(file.webkitRelativePath);\n"
            + "    alert(file.size);\n"
            + "    alert(file.type);\n"
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

            verifyAlerts(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "function", "Hello HtmlUnit"},
            IE = {"1", "undefined", "TypeError true"})
    public void text() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"
            + "    alert(files.length);\n"

            + "    var file = files[0];\n"
            + "    alert(typeof file.text);\n"

            + "    try {\n"
            + "      file.text().then(function(text) { alert(text); });\n"
            + "    } catch(e) { alert('TypeError ' + (e instanceof TypeError)); }\n"
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

            verifyAlerts(driver, getExpectedAlerts());
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
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"

            + "    var file = files[0];\n"
            + "    alert(file.type);\n"
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

            verifyAlerts(driver, getExpectedAlerts());
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
                + "<head><title>foo</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    alert(File in window);\n"

                + "    try {\n"
                + "      alert(new File());\n"
                + "    } catch(e) { alert('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
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
    @Alerts(DEFAULT = {"[object File]", "htMluniT.txt", "", "true", "0",
                ""},
            IE = "TypeError true")
    public void ctorEmpty() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head><title>foo</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var file = new File([], 'htMluniT.txt');\n"
                + "      alert(file);\n"
                + "      alert(file.name);\n"
                + "      alert(file.type);\n"
                + "      alert(file.lastModified >= now);\n"
                + "      alert(file.size);\n"

                + "      file.text().then(function(text) { alert(text); });\n"
                + "    } catch(e) { alert('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
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
    @Alerts(DEFAULT = {"[object File]", "htMluniT.txt", "", "true", "8",
                "HtmlUnit"},
            IE = "TypeError true")
    public void ctorString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head><title>foo</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var file = new File(['HtmlUnit'], 'htMluniT.txt');\n"
                + "      alert(file);\n"
                + "      alert(file.name);\n"
                + "      alert(file.type);\n"
                + "      alert(file.lastModified >= now);\n"
                + "      alert(file.size);\n"

                + "      file.text().then(function(text) { alert(text); });\n"
                + "    } catch(e) { alert('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
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
    @Alerts(DEFAULT = {"[object File]", "htMluniT.txt", "application/octet-stream", "1234567", "8",
                "HtmlUnit"},
            IE = "TypeError true")
    public void ctorStringWithOptions() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head><title>foo</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var file = new File(['HtmlUnit'], 'htMluniT.txt',"
                              + "{type: 'application/octet-stream', lastModified: '1234567'});\n"
                + "      alert(file);\n"
                + "      alert(file.name);\n"
                + "      alert(file.type);\n"
                + "      alert(file.lastModified);\n"
                + "      alert(file.size);\n"

                + "      file.text().then(function(text) { alert(text); });\n"
                + "    } catch(e) { alert('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
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
    @Alerts(DEFAULT = {"[object File]", "htMluniT.txt", "", "true", "16",
                "HtmlUnitis great"},
            IE = "TypeError true")
    public void ctorStrings() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head><title>foo</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var file = new File(['Html', 'Unit', 'is great'], 'htMluniT.txt');\n"
                + "      alert(file);\n"
                + "      alert(file.name);\n"
                + "      alert(file.type);\n"
                + "      alert(file.lastModified >= now);\n"
                + "      alert(file.size);\n"

                + "      file.text().then(function(text) { alert(text); });\n"
                + "    } catch(e) { alert('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
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
    @Alerts(DEFAULT = {"[object File]", "htMluniT.txt", "", "true", "12",
                "HtmlUnitMMMK"},
            IE = "TypeError true")
    public void ctorMixed() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head><title>foo</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var now = Date.now();\n"
                + "      var nab = new ArrayBuffer(2);\n"
                + "      var nabv = new Uint8Array(nab, 0, 2);\n"
                + "      nabv.set([77, 77], 0);\n"
                + "      var file = new File(['HtmlUnit',"
                                      + "nab, new Int8Array([77,75])], 'htMluniT.txt');\n"
                + "      alert(file);\n"
                + "      alert(file.name);\n"
                + "      alert(file.type);\n"
                + "      alert(file.lastModified >= now);\n"
                + "      alert(file.size);\n"

                + "      file.text().then(function(text) { alert(text); });\n"
                + "    } catch(e) { alert('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }
}
