/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link FormData}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class FormDataTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function, undefined, undefined, undefined, undefined, undefined",
            IE8 = "")
    public void functions() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    if (window.FormData) {\n"
            + "      var formData = new FormData();\n"
            + "      alert(typeof formData.append);\n"
            + "      alert(typeof formData.delete);\n"
            + "      alert(typeof formData.get);\n"
            + "      alert(typeof formData.getAll);\n"
            + "      alert(typeof formData.has);\n"
            + "      alert(typeof formData.set);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void post() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  if (window.FormData) {\n"
            + "    var xhr = " + XMLHttpRequest2Test.XHRInstantiation_ + ";\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    var formData = new FormData();\n"
            + "    formData.append('myKey', 'myValue');\n"
            + "    formData.append('myKey', 'myValue2');\n"
            + "    formData.append('myKeyNull', null);\n"
            + "    formData.append('myKeyUndef', undefined);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);
        final List<String> alerts = getCollectedAlerts(driver);
        if (!alerts.isEmpty()) {
            final String[] lines = alerts.get(0).split("\\n");
            assertEquals(17, lines.length);
            assertEquals("Content-Disposition: form-data; name=\"myKey\"", lines[1]);
            assertEquals("", lines[2]);
            assertEquals("myValue", lines[3]);
            assertEquals(lines[0], lines[4]);
            assertEquals("Content-Disposition: form-data; name=\"myKey\"", lines[5]);
            assertEquals("", lines[6]);
            assertEquals("myValue2", lines[7]);
            assertEquals(lines[0], lines[8]);
            assertEquals("Content-Disposition: form-data; name=\"myKeyNull\"", lines[9]);
            assertEquals("", lines[10]);
            assertEquals("null", lines[11]);
            assertEquals(lines[0], lines[12]);
            assertEquals("Content-Disposition: form-data; name=\"myKeyUndef\"", lines[13]);
            assertEquals("", lines[14]);
            assertEquals("undefined", lines[15]);
            assertEquals(lines[0] + "--", lines[16]);
        }
    }

    /**
     * Servlet for {@link #post()}.
     */
    public static class PostServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            final BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }
            writer.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void xmlHttpRequest() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if (document.testForm.fileupload.files) {\n"
            + "    var files = document.testForm.fileupload.files;\n"
            + "    var xhr = " + XMLHttpRequest2Test.XHRInstantiation_ + ";\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    var formData = new FormData();\n"
            + "    formData.append('myKey', files[0]);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
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

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);

        final File tstFile = File.createTempFile("HtmlUnitUploadTest", ".txt");
        try {
            FileUtils.writeStringToFile(tstFile, "Hello HtmlUnit");

            final String path = tstFile.getCanonicalPath();
            driver.findElement(By.name("fileupload")).sendKeys(path);

            driver.findElement(By.id("testBtn")).click();

            final List<String> alerts = getCollectedAlerts(driver);
            if (!alerts.isEmpty()) {
                final String[] lines = alerts.get(0).split("\\n");
                assertEquals(6, lines.length);
                assertTrue(lines[1].startsWith("Content-Disposition: form-data; name=\"myKey\"; filename="));
                assertEquals("Content-Type: text/plain", lines[2]);
                assertEquals("", lines[3]);
                assertEquals("Hello HtmlUnit", lines[4]);
                assertEquals(lines[0] + "--", lines[5]);
            }
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }
}
