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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link FormData}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Thorsten Wendelmuth
 */
@RunWith(BrowserRunner.class)
public class FormDataTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "function", "function", "function", "function", "function"},
            IE = {"function", "undefined", "undefined", "undefined", "undefined", "undefined"})
    public void functions() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
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
    public void empty() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData();\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);
        final List<String> alerts = getCollectedAlerts(driver, 1);
        if (!alerts.isEmpty()) {
            String[] lines = alerts.get(0).split("\\n");
            if (lines.length == 2 && lines[0].isEmpty()) {
                // response of IE contains an additional empty line -> remove
                lines = Arrays.copyOfRange(lines, 1, 2);
            }
            assertEquals("Response: " + alerts.get(0) + "; line count", 1, lines.length);
            assertTrue(lines[0].startsWith("--") && lines[0].endsWith("--"));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void append() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData();\n"
            + "    formData.append('myKey', 'myValue');\n"
            + "    formData.append('myKey', 'myValue2');\n"
            + "    formData.append('myKeyNull', null);\n"
            + "    formData.append('myKeyUndef', undefined);\n"
            + "    formData.append('myKeyEmpty', '');\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);
        final List<String> alerts = getCollectedAlerts(driver, 1);
        if (!alerts.isEmpty()) {
            final String[] lines = alerts.get(0).split("\\n");
            assertEquals("Response: " + alerts.get(0) + "; line count", 21, lines.length);
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
            assertEquals(lines[0], lines[16]);
            assertEquals("Content-Disposition: form-data; name=\"myKeyEmpty\"", lines[17]);
            assertEquals("", lines[18]);
            assertEquals("", lines[19]);
            assertEquals(lines[0] + "--", lines[20]);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appendFile() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var files = document.testForm.myFile.files;\n"
            + "    var formData = new FormData();\n"
            + "    formData.append('myKey', files[0]);\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='myFile' name='myFile'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);

        final File tstFile = File.createTempFile("HtmlUnitUploadTest", ".txt");
        try {
            FileUtils.writeStringToFile(tstFile, "Hello HtmlUnit", ISO_8859_1);

            final String path = tstFile.getCanonicalPath();
            driver.findElement(By.name("myFile")).sendKeys(path);

            driver.findElement(By.id("testBtn")).click();

            final List<String> alerts = getCollectedAlerts(driver, 1);
            if (!alerts.isEmpty()) {
                final String[] lines = alerts.get(0).split("\\n");
                assertEquals(6, lines.length);
                assertEquals("Content-Disposition: form-data; name=\"myKey\"; filename=\""
                                + tstFile.getName() + "\"", lines[1]);
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appendFileWithFileName() throws Exception {
        final String alerts = appendFile(".txt", "myFileName");
        final String[] lines = alerts.split("\\n");
        assertEquals(6, lines.length);
        assertEquals("Content-Disposition: form-data; name=\"myKey\"; filename=\"myFileName\"", lines[1]);
        assertEquals("Content-Type: text/plain", lines[2]);
        assertEquals("", lines[3]);
        assertEquals("Hello HtmlUnit", lines[4]);
        assertEquals(lines[0] + "--", lines[5]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appendFileWithEmptyFileName() throws Exception {
        final String alerts = appendFile(".txt", "");

        final String[] lines = alerts.split("\\n");
        assertEquals(6, lines.length);
        assertEquals("Content-Disposition: form-data; name=\"myKey\"; filename=\"\"", lines[1]);
        assertEquals("Content-Type: text/plain", lines[2]);
        assertEquals("", lines[3]);
        assertEquals("Hello HtmlUnit", lines[4]);
        assertEquals(lines[0] + "--", lines[5]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Content-Disposition: form-data; name=\"myKey\"; filename=\"test\"",
                        "Content-Type: application/octet-stream", "", "Hello HtmlUnit"},
            IE = {"Content-Disposition: form-data; name=\"myKey\"; filename=\"test\"",
                        "Content-Type: text/plain", "", "Hello HtmlUnit"})
    public void appendFileWithUnknownExtension() throws Exception {
        final String alerts = appendFile(".htmlunit", "test");

        final String[] expectedAlerts = getExpectedAlerts();
        final String[] lines = alerts.split("\\n");
        assertEquals(6, lines.length);
        assertEquals(expectedAlerts[0], lines[1]);
        assertEquals(expectedAlerts[1], lines[2]);
        assertEquals(expectedAlerts[2], lines[3]);
        assertEquals(expectedAlerts[3], lines[4]);
        assertEquals(lines[0] + "--", lines[5]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Content-Disposition: form-data; name=\"myKey\"; filename=\"test\"",
                        "Content-Type: application/octet-stream", "", "Hello HtmlUnit"},
              IE = {"Content-Disposition: form-data; name=\"myKey\"; filename=\"test\"",
                      "Content-Type: text/plain", "", "Hello HtmlUnit"})
    public void appendFileWithoutExtension() throws Exception {
        final String alerts = appendFile("", "test");

        final String[] expectedAlerts = getExpectedAlerts();
        final String[] lines = alerts.split("\\n");
        assertEquals(6, lines.length);
        assertEquals(expectedAlerts[0], lines[1]);
        assertEquals(expectedAlerts[1], lines[2]);
        assertEquals(expectedAlerts[2], lines[3]);
        assertEquals(expectedAlerts[3], lines[4]);
        assertEquals(lines[0] + "--", lines[5]);
    }

    private String appendFile(final String extension, final String name) throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var files = document.testForm.myFile.files;\n"
            + "    var formData = new FormData();\n"
            + "    formData.append('myKey', files[0], '" + name + "');\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='file' id='myFile' name='myFile'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);

        final File tstFile = File.createTempFile("HtmlUnitUploadTest", extension);
        try {
            FileUtils.writeStringToFile(tstFile, "Hello HtmlUnit", ISO_8859_1);

            final String path = tstFile.getCanonicalPath();
            driver.findElement(By.name("myFile")).sendKeys(path);

            driver.findElement(By.id("testBtn")).click();

            final List<String> alerts = getCollectedAlerts(driver, 1);
            return alerts.get(0);
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"myKey", "myKey1"},
            IE = "no delete")
    public void delete() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData();\n"
            + "    if (!formData.delete) { alert('no delete'); return; }\n"

            + "    formData.append('myKey', 'myValue');\n"
            + "    formData.append('myKey1', '');\n"
            + "    formData.append('mykey 2', '');\n"
            + "    formData.append('mykey3', 'myVal3');\n"
            + "    formData.append('mykey3', 'myVal4');\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"

            + "  try {\n"
            + "    formData.delete('mykey');\n"
            + "    formData.delete('mykey 2');\n"
            + "    formData.delete('mykey3');\n"
            + "    formData.delete('');\n"
            + "  } catch (e) {\n"
            + "    alert('delete: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);
        final WebDriver driver = loadPage2(html, servlets);
        final String alerts = getCollectedAlerts(driver, 1).get(0);
        for (final String expected : getExpectedAlerts()) {
            assertTrue(expected + " not found", alerts.contains(expected));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"myValue", "null", "null", "null", "null"},
            IE = "no get")
    public void get() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData();\n"
            + "    if (!formData.get) { alert('no get'); return; }\n"

            + "    formData.append('myKey', 'myValue');\n"
            + "    formData.append('myKey', 'myValue2');\n"
            + "    formData.append('mykey3', 'myValue3');\n"
            + "    formData.append('mykey4', '');\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"

            + "  try {\n"
            + "    alert(formData.get('myKey'));\n"
            + "    alert(formData.get('mykey'));\n"
            + "    alert(formData.get('myKey3'));\n"
            + "    alert(formData.get('myKey4'));\n"
            + "    alert(formData.get(''));\n"
            + "  } catch (e) {\n"
            + "    alert('get: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        loadPageWithAlerts2(html, servlets);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"myValue,myValue2", "", "", "", ""},
            IE = "no getAll")
    public void getAll() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData();\n"
            + "    if (!formData.get) { alert('no getAll'); return; }\n"

            + "    formData.append('myKey', 'myValue');\n"
            + "    formData.append('myKey', 'myValue2');\n"
            + "    formData.append('mykey3', 'myValue3');\n"
            + "    formData.append('mykey4', '');\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"

            + "  try {\n"
            + "    alert(formData.getAll('myKey'));\n"
            + "    alert(formData.getAll('mykey'));\n"
            + "    alert(formData.getAll('myKey3'));\n"
            + "    alert(formData.getAll('myKey4'));\n"
            + "    alert(formData.getAll(''));\n"
            + "  } catch (e) {\n"
            + "    alert('getAll: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        loadPageWithAlerts2(html, servlets);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "false"},
            IE = "no has")
    public void has() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData();\n"
            + "    if (!formData.has) { alert('no has'); return; }\n"

            + "    formData.append('myKey', 'myValue');\n"
            + "    formData.append('myKey1', '');\n"
            + "    formData.append('mykey 2', '');\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"

            + "  try {\n"
            + "    alert(formData.has('myKey'));\n"
            + "    alert(formData.has('mykey'));\n"
            + "    alert(formData.has(''));\n"
            + "  } catch (e) {\n"
            + "    alert('has: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no set",
            FF = "")
    public void set() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData();\n"
            + "    if (!formData.set) { alert('no set'); return; }\n"
            + "    formData.append('myKey1', 'myValue1');\n"
            + "    formData.append('myKey1', 'myValue2');\n"
            + "    formData.append('myKey3', 'myValue3');\n"
            + "    formData.append('myKey4', 'myValue4');\n"
            + "    formData.set('myKey1', 'new1');\n"
            + "    formData.set('myKey4', 'new4');\n"
            + "    formData.set('myKeyX', 'newX');\n"
            + "  } catch (e) {\n"
            + "    alert('set: ' + e.message);\n"
            + "    return;\n"
            + "  }\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);
        final List<String> alerts = getCollectedAlerts(driver, 1);

        if (alerts.get(0).equals("no set")) {
            assertEquals(getExpectedAlerts(), alerts);
            return;
        }

        final String[] lines = alerts.get(0).split("\\n");
        assertEquals("Response: " + alerts.get(0) + "; line count", 17, lines.length);
        assertEquals("Content-Disposition: form-data; name=\"myKey1\"", lines[1]);
        assertEquals("", lines[2]);
        assertEquals("new1", lines[3]);
        assertEquals(lines[0], lines[4]);
        assertEquals("Content-Disposition: form-data; name=\"myKey3\"", lines[5]);
        assertEquals("", lines[6]);
        assertEquals("myValue3", lines[7]);
        assertEquals(lines[0], lines[8]);
        assertEquals("Content-Disposition: form-data; name=\"myKey4\"", lines[9]);
        assertEquals("", lines[10]);
        assertEquals("new4", lines[11]);
        assertEquals(lines[0], lines[12]);
        assertEquals("Content-Disposition: form-data; name=\"myKeyX\"", lines[13]);
        assertEquals("", lines[14]);
        assertEquals("newX", lines[15]);
        assertEquals(lines[0] + "--", lines[16]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void fromForm() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData(document.testForm);\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "  }\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='text' id='myText' name='myText' value='textxy'>\n"
            + "    <input type='file' id='myFile' name='myFile'>\n"
            + "    <input type='hidden' id='myHidden' name='myHidden' value='hiddenxy'>\n"
            + "    <input type='button' id='myButton' name='myButton' value='buttonxy'>\n"
            + "    <input type='submit' id='mySubmit' name='mySubmit' value='submitxy'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);

        final File tstFile = File.createTempFile("HtmlUnitUploadTest", ".txt");
        try {
            FileUtils.writeStringToFile(tstFile, "Hello HtmlUnit", ISO_8859_1);

            final String path = tstFile.getCanonicalPath();
            driver.findElement(By.name("myFile")).sendKeys(path);

            driver.findElement(By.id("testBtn")).click();

            final List<String> alerts = getCollectedAlerts(driver, 1);
            if (!alerts.isEmpty()) {
                final String[] lines = alerts.get(0).split("\\n");
                assertEquals("Response: " + alerts.get(0) + "; line count", 14, lines.length);
                assertEquals("Content-Disposition: form-data; name=\"myText\"", lines[1]);
                assertEquals("", lines[2]);
                assertEquals("textxy", lines[3]);
                assertEquals(lines[0], lines[4]);
                assertTrue(lines[5].startsWith("Content-Disposition: form-data; name=\"myFile\"; filename="));
                assertEquals("Content-Type: text/plain", lines[6]);
                assertEquals("", lines[7]);
                assertEquals("Hello HtmlUnit", lines[8]);
                assertEquals(lines[0], lines[9]);
                assertEquals("Content-Disposition: form-data; name=\"myHidden\"", lines[10]);
                assertEquals("", lines[11]);
                assertEquals("hiddenxy", lines[12]);
                assertEquals(lines[0] + "--", lines[13]);
            }
        }
        finally {
            FileUtils.deleteQuietly(tstFile);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void fromFormAndAppend() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData(document.testForm);\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "  }\n"
            + "  try {\n"
            + "    formData.append('myText', 'new value');\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='text' id='myText' name='myText' value='textxy'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);

        driver.findElement(By.id("testBtn")).click();

        final List<String> alerts = getCollectedAlerts(driver, 1);
        if (!alerts.isEmpty()) {
            final String[] lines = alerts.get(0).split("\\n");
            assertEquals("Response: " + alerts.get(0) + "; line count", 9, lines.length);
            assertEquals("Content-Disposition: form-data; name=\"myText\"", lines[1]);
            assertEquals("", lines[2]);
            assertEquals("textxy", lines[3]);
            assertEquals(lines[0], lines[4]);
            assertEquals("Content-Disposition: form-data; name=\"myText\"", lines[5]);
            assertEquals("", lines[6]);
            assertEquals("new value", lines[7]);
            assertEquals(lines[0] + "--", lines[8]);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void fromFormChangeBeforeSend() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var formData = new FormData(document.testForm);\n"
            + "  } catch (e) {\n"
            + "    alert('create: ' + e.message);\n"
            + "  }\n"
            + "  try {\n"
            + "    document.getElementById('myText').value = 'new value';\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert(xhr.responseText);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='text' id='myText' name='myText' value='textxy'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='test()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet.class);

        final WebDriver driver = loadPage2(html, servlets);

        driver.findElement(By.id("testBtn")).click();

        final List<String> alerts = getCollectedAlerts(driver, 1);
        if (!alerts.isEmpty()) {
            final String[] lines = alerts.get(0).split("\\n");
            assertEquals("Response: " + alerts.get(0) + "; line count", 5, lines.length);
            assertEquals("Content-Disposition: form-data; name=\"myText\"", lines[1]);
            assertEquals("", lines[2]);
            assertEquals("textxy", lines[3]);
            assertEquals(lines[0] + "--", lines[4]);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void defaultEnctype() throws Exception {
        enctype(null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void emptyEnctype() throws Exception {
        enctype("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void blankEnctype() throws Exception {
        enctype(" ");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void unknownEnctype() throws Exception {
        enctype("unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void urlencodedEnctype() throws Exception {
        enctype("application/x-www-form-urlencoded");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void multipartEnctype() throws Exception {
        enctype("multipart/form-data");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void plainEnctype() throws Exception {
        enctype("text/plain");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void xmlEnctype() throws Exception {
        enctype("text/xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void jsonEnctype() throws Exception {
        enctype("application/json");
    }

    private void enctype(final String enctype) throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var formData = new FormData(document.testForm);\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('POST', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "  } catch (e) {\n"
            + "    alert('send: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='testForm' enctype='" + enctype + "'>\n"
            + "    <input type='text' id='myText' name='myText' value='textxy'>\n"
            + "  </form>\n"
            + "  <button id='testBtn' onclick='doTest()'>Tester</button>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {});

        driver.findElement(By.id("testBtn")).click();
        String headerValue = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);
        // Can't test equality for multipart/form-data as it will have the form:
        // multipart/form-data; boundary=---------------------------42937861433140731107235900
        headerValue = StringUtils.substringBefore(headerValue, ";");
        assertEquals(getExpectedAlerts()[0], headerValue);
    }

    /**
     * Going through entries() via suggested for ... of method
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"myKey", "myValue", "myKey2", "", "myKey", "myvalue2"},
            IE = "no entries")
    public void entries_forOf() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var formData = new FormData();\n"
            + "  if (!formData.get) {\n"
            + "    alert('no entries');\n"
            + "    return;"
            + "  }\n"

            + "  formData.append('myKey', 'myValue');\n"
            + "  formData.append('myKey2', '');\n"
            + "  formData.append('myKey', 'myvalue2');\n"

            + "  for (var pair of formData.entries()) {\n"
            + "    alert(pair[0]);\n"
            + "    alert(pair[1]);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Checks if the iterator works correctly.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object Iterator]", "done", "value",
                        "myKey", "myValue", "myKey2", "", "myKey", "myvalue2"},
            FF = {"[object FormData Iterator]", "done", "value",
                        "myKey", "myValue", "myKey2", "", "myKey", "myvalue2"},
            FF78 = {"[object FormData Iterator]", "done", "value",
                        "myKey", "myValue", "myKey2", "", "myKey", "myvalue2"},
            IE = "no entries")
    @HtmlUnitNYI(CHROME = {"[object Iterator]", "value", "done",
                        "myKey", "myValue", "myKey2", "", "myKey", "myvalue2"},
            EDGE = {"[object Iterator]", "value", "done",
                    "myKey", "myValue", "myKey2", "", "myKey", "myvalue2"},
            FF = {"[object FormData Iterator]", "value", "done",
                        "myKey", "myValue", "myKey2", "", "myKey", "myvalue2"},
            FF78 = {"[object FormData Iterator]", "value", "done",
                        "myKey", "myValue", "myKey2", "", "myKey", "myvalue2"})
    public void entriesIterator() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var formData = new FormData();\n"
            + "  if (!formData.get) {\n"
            + "    alert('no entries');\n"
            + "    return;"
            + "  }\n"

            + "  formData.append('myKey', 'myValue');\n"
            + "  formData.append('myKey2', '');\n"
            + "  formData.append('myKey', 'myvalue2');\n"

            + "  var iterator = formData.entries();\n"
            + "  alert(iterator);\n"

            + "  var nextItem = iterator.next();\n"
            + "  for (var x in nextItem) {\n"
            + "    alert(x);\n"
            + "  }\n"

            + "  while (nextItem.done == false) {\n"
            + "    alert(nextItem.value[0]);\n"
            + "    alert(nextItem.value[1]);\n"
            + "    nextItem = iterator.next();\n"
            + "  }\n"

            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Servlet for post().
     */
    public static class PostServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            final Writer writer = response.getWriter();
            final BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }
        }
    }
}
