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

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link FileList}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class FileListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "true"})
    public void in() throws Exception {
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
}
