/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.file;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link File}.
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
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"
            + "    alert(files.length);\n"

            + "    alert(0 in files);\n"
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
            FileUtils.writeStringToFile(tstFile, "Hello HtmlUnit", TextUtil.DEFAULT_CHARSET);

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
