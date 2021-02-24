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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
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
 * Tests for {@link FileReader}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class FileReaderTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void ctorReadyState() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var reader = new FileReader();\n"
            + "      alert(reader.readyState);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("testBtn")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:text/plain;base64,SHRtbFVuaXQ=")
    public void readAsDataURL_file() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        var dataURL = reader.result;\n"
            + "        alert(dataURL);\n"
            + "      };\n"
            + "      reader.readAsDataURL(files[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsDataURLTest", ".txt");
        try {
            FileUtils.write(tstFile, "HtmlUnit", StandardCharsets.UTF_8);

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
    @Alerts(DEFAULT = "data:application/octet-stream;base64,SHRtbFVuaXRpcyBncmVhdA==",
            IE = "data:;base64,SHRtbFVuaXRpcyBncmVhdA==")
    public void readAsDataURL_blob() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var blob = new Blob(['Html', 'Unit', 'is great']);\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        var dataURL = reader.result;\n"
            + "        alert(dataURL);\n"
            + "      };\n"
            + "      reader.readAsDataURL(blob);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:text/plain;base64,SHRtbFVuaXRpcyBncmVhdA==")
    public void readAsDataURL_blobMimeType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var blob = new Blob(['Html', 'Unit', 'is great'], {type : 'text/plain'});\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        var dataURL = reader.result;\n"
            + "        alert(dataURL);\n"
            + "      };\n"
            + "      reader.readAsDataURL(blob);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:unknown;base64,SHRtbFVuaXRpcyBncmVhdA==",
            IE = "data:unKNown;base64,SHRtbFVuaXRpcyBncmVhdA==")
    public void readAsDataURL_blobMimeTypeUnknown() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var blob = new Blob(['Html', 'Unit', 'is great'], {type : 'unKNown'});\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        var dataURL = reader.result;\n"
            + "        alert(dataURL);\n"
            + "      };\n"
            + "      reader.readAsDataURL(blob);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:application/octet-stream;base64,"
                + "Niii65mOV9yO6adjkXdWd+zTIXFcOWwumIGlIFRqQ05seTG+J2dx0KcD",
            IE = "data:;base64,Niii65mOV9yO6adjkXdWd+zTIXFcOWwumIGlIFRqQ05seTG+J2dx0KcD")
    @NotYetImplemented(IE) // fails only on linux
    public void readAsDataURLUnknown() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function previewFile() {\n"
            + "    var file = document.querySelector('input[type=file]').files[0];\n"
            + "    var reader = new FileReader();\n"
            + "    reader.addEventListener('load', function () {\n"
            + "      alert(reader.result);\n"
            + "    }, false);\n"
            + "\n"
            + "    if (file) {\n"
            + "      reader.readAsDataURL(file);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input type='file' onchange='previewFile()'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final String filename = "testfiles/random.bytes";
        final URL fileURL = getClass().getClassLoader().getResource(filename);
        assertNotNull("Resource '" + filename + "' not found", fileURL);
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        driver.findElement(By.tagName("input")).sendKeys(file.getAbsolutePath());
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "#data:",
            FF = "#data:image/png;base64,",
            FF78 = "#data:image/png;base64,",
            IE = "#null")
    public void readAsDataURLEmptyImage() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function previewFile() {\n"
            + "    var file = document.querySelector('input[type=file]').files[0];\n"
            + "    var reader = new FileReader();\n"
            + "    reader.addEventListener('load', function () {\n"
            + "      alert('#' + reader.result);\n"
            + "    }, false);\n"

            + "    if (file) {\n"
            + "      reader.readAsDataURL(file);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input type='file' onchange='previewFile()'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final String filename = "testfiles/empty.png";
        final URL fileURL = getClass().getClassLoader().getResource(filename);
        assertNotNull("Resource '" + filename + "' not found", fileURL);
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        driver.findElement(By.tagName("input")).sendKeys(file.getAbsolutePath());
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object ArrayBuffer]", "8"})
    public void readAsArrayBuffer() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        alert(reader.result);\n"
            + "        alert(reader.result.byteLength);\n"
            + "      };\n"
            + "      reader.readAsArrayBuffer(files[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsArrayBufferTest", ".txt");
        try {
            FileUtils.write(tstFile, "HtmlUnit", StandardCharsets.UTF_8);

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
    @Alerts({"[object ArrayBuffer]", "128"})
    public void readAsArrayBufferUnknown() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function previewFile() {\n"
            + "    var file = document.querySelector('input[type=file]').files[0];\n"
            + "    var reader = new FileReader();\n"
            + "    reader.addEventListener('load', function () {\n"
            + "      alert(reader.result);\n"
            + "      alert(reader.result.byteLength);\n"
            + "    }, false);\n"
            + "\n"
            + "    if (file) {\n"
            + "      reader.readAsArrayBuffer(file);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input type='file' onchange='previewFile()'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final String filename = "testfiles/tiny-png.img";
        final URL fileURL = getClass().getClassLoader().getResource(filename);
        assertNotNull("Resource '" + filename + "' not found", fileURL);
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        driver.findElement(By.tagName("input")).sendKeys(file.getAbsolutePath());
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object ArrayBuffer]", "0"})
    public void readAsArrayBufferEmptyImage() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function previewFile() {\n"
            + "    var file = document.querySelector('input[type=file]').files[0];\n"
            + "    var reader = new FileReader();\n"
            + "    reader.addEventListener('load', function () {\n"
            + "      alert(reader.result);\n"
            + "      alert(reader.result.byteLength);\n"
            + "    }, false);\n"
            + "\n"
            + "    if (file) {\n"
            + "      reader.readAsArrayBuffer(file);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input type='file' onchange='previewFile()'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final String filename = "testfiles/empty.png";
        final URL fileURL = getClass().getClassLoader().getResource(filename);
        assertNotNull("Resource '" + filename + "' not found", fileURL);
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        driver.findElement(By.tagName("input")).sendKeys(file.getAbsolutePath());
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Html \u00dcnit")
    public void readAsText() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        alert(reader.result);\n"
            + "      };\n"
            + "      reader.readAsText(files[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsTextTest", ".txt");
        try {
            FileUtils.write(tstFile, "Html \u00dcnit", StandardCharsets.UTF_8);

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
    @Alerts("Html \u00dcnit")
    public void readAsTextEncodingNull() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        alert(reader.result);\n"
            + "      };\n"
            + "      reader.readAsText(files[0], null);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsTextTest", ".txt");
        try {
            FileUtils.write(tstFile, "Html \u00dcnit", StandardCharsets.UTF_8);

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
    @Alerts("Html \u00dcnit")
    public void readAsTextEncodingUndefined() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        alert(reader.result);\n"
            + "      };\n"
            + "      reader.readAsText(files[0], undefined);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsTextTest", ".txt");
        try {
            FileUtils.write(tstFile, "Html \u00dcnit", StandardCharsets.UTF_8);

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
    @Alerts("Html \u00dcnit")
    public void readAsTextEncodingUnknown() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        alert(reader.result);\n"
            + "      };\n"
            + "      reader.readAsText(files[0], 'Unknown');\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsTextTest", ".txt");
        try {
            FileUtils.write(tstFile, "Html \u00dcnit", StandardCharsets.UTF_8);

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
    @Alerts("Html \u00dcnit")
    public void readAsTextUtf8() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        alert(reader.result);\n"
            + "      };\n"
            + "      reader.readAsText(files[0], 'utf-8');\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsTextTest", ".txt");
        try {
            FileUtils.write(tstFile, "Html \u00dcnit", StandardCharsets.UTF_8);

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
    @Alerts("Html \u00dcnit")
    public void readAsTextIso88591() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        alert(reader.result);\n"
            + "      };\n"
            + "      reader.readAsText(files[0], 'iso-8859-1');\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsTextTest", ".txt");
        try {
            FileUtils.write(tstFile, "Html \u00dcnit", Charset.forName("ISO-8859-1"));

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
    @Alerts({"", "0"})
    public void readAsTextEmpty() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var files = document.testForm.fileupload.files;\n"
            + "      var reader = new FileReader();\n"
            + "      reader.onload = function() {\n"
            + "        alert(reader.result);\n"
            + "        alert(reader.result.length);\n"
            + "      };\n"
            + "      reader.readAsText(files[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "<head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='fileupload' name='fileupload'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final File tstFile = File.createTempFile("HtmlUnitReadAsTextTest", ".txt");
        try {
            FileUtils.write(tstFile, "", StandardCharsets.UTF_8);

            final String path = tstFile.getCanonicalPath();
            driver.findElement(By.name("fileupload")).sendKeys(path);

            driver.findElement(By.id("testBtn")).click();

            verifyAlerts(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }
}
