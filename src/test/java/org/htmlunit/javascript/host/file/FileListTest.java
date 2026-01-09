/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import org.apache.commons.io.FileUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link FileList}.
 *
 * @author Ronald Brill
 */
public class FileListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "true"})
    public void in() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"
            + "    log(files.length);\n"

            + "    log(0 in files);\n"
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
            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object File]"})
    public void item() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  if (document.testForm.fileupload.files) {\n"
                + "    var files = document.testForm.fileupload.files;\n"
                + "    log(files.length);\n"

                + "    log(files.item(0));\n"
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
            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "null", "null"})
    public void itemWrong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  if (document.testForm.fileupload.files) {\n"
                + "    var files = document.testForm.fileupload.files;\n"
                + "    log(files.length);\n"

                + "    log(files.item(-1));\n"
                + "    log(files.item(1));\n"
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
            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object File]"})
    public void indexed() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  if (document.testForm.fileupload.files) {\n"
                + "    var files = document.testForm.fileupload.files;\n"
                + "    log(files.length);\n"

                + "    log(files[0]);\n"
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
            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "undefined", "undefined"})
    public void indexedWrong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  if (document.testForm.fileupload.files) {\n"
                + "    var files = document.testForm.fileupload.files;\n"
                + "    log(files.length);\n"

                + "    log(files[-1]);\n"
                + "    log(files[1]);\n"
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
            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object File]"})
    public void iterator() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  if (document.testForm.fileupload.files) {\n"
                + "    var files = document.testForm.fileupload.files;\n"
                + "    log(files.length);\n"

                + "    for (var i of files) {\n"
                + "      log(i);\n"
                + "    }\n"
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
            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }
}
