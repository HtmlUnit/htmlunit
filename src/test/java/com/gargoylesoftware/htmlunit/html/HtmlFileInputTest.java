/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link HtmlFileInput}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlFileInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeHeader() throws Exception {
        final String htmlContent
            = "<html>\n"
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(htmlContent);
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        path = path.substring(path.indexOf('/') + 1).replace('/', '\\');

        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        final String contentType = request.getAdditionalHeaders().get(HttpHeader.CONTENT_TYPE);
        assertTrue(StringUtils.isNotBlank(contentType));
        assertFalse(StringUtils.containsIgnoreCase(contentType, "charset"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Content-Disposition: form-data; name=\"myInput\"; filename=\"\"")
    public void empty() throws Exception {
        final String htmlContent
            = "<html>\n"
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(htmlContent);
        driver.findElement(By.id("mySubmit")).click();

        final String pageSource = getMockWebConnection().getLastWebRequest().getRequestBody();
        final Matcher matcher = Pattern.compile(getExpectedAlerts()[0]).matcher(pageSource);
        assertTrue(pageSource, matcher.find());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Content-Disposition: form-data; name=\"myInput\"; filename=\"realm.properties\"")
    // since 2.28
    // there is an option for IE, for local and trusted sites IE includes the file path
    // because we do not support any IE specific setting we do not send the filename as
    // done by the other browsers
    public void realFile() throws Exception {
        final String htmlContent
            = "<html>\n"
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(htmlContent);

        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        path = path.substring(path.indexOf('/') + 1).replace('/', '\\');

        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();

        final String pageSource = getMockWebConnection().getLastWebRequest().getRequestBody();
        final Matcher matcher = Pattern.compile(getExpectedAlerts()[0]).matcher(pageSource);
        assertTrue(pageSource, matcher.find());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void chunked() throws Exception {
        final String htmlContent
            = "<html>\n"
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(htmlContent);

        driver.findElement(By.id("mySubmit")).click();

        final String pageSource = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertFalse(pageSource.contains("chunked"));
    }

    /**
     * Verifies that getText() returns an empty string.
     * @throws Exception if the test fails
     */
    @Test
    public void getText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='file' name='foo' id='foo' value='bla'>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final WebElement input = driver.findElement(By.id("foo"));
        assertEquals("", input.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"exception", "-Hello world-Hello world-0", "-Hello world-Hello world-0"},
            IE = {"-Hello world-Hello world-0", "-Hello world-Hello world-0"})
    public void setValueOnChange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var input = document.getElementById('f');\n"
              + "    try{\n"
              + "      input.value = 'HtmlUnit';\n"
              + "    } catch(e) { log('exception'); }\n"
              + "    log(input.value + '-' + input.defaultValue "
                          + "+ '-' + input.getAttribute('value') "
                          + "+ '-' + input.files.length);\n"

              + "    try{\n"
              + "      input.value = '';\n"
              + "    } catch(e) { log('exception'); }\n"
              + "    log(input.value + '-' + input.defaultValue "
                          + "+ '-' + input.getAttribute('value') "
                          + "+ '-' + input.files.length);\n"
              + "  }\n"
              + "</script>\n"
              + "<body>\n"
              + "  <input type='file' id='f' value='Hello world' onChange='log(\"foo\");log(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='test()'>setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();

        verifyTitle2(driver, getExpectedAlerts());

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"-HtmlUnit-HtmlUnit-0", "---0"})
    public void setDefaultValueOnChange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var input = document.getElementById('f');\n"
              + "    try{\n"
              + "      input.defaultValue = 'HtmlUnit';\n"
              + "    } catch(e) { log('exception'); }\n"
              + "    log(input.value + '-' + input.defaultValue "
                          + "+ '-' + input.getAttribute('value') "
                          + "+ '-' + input.files.length);\n"

              + "    try{\n"
              + "      input.defaultValue = '';\n"
              + "    } catch(e) { log('exception'); }\n"
              + "    log(input.value + '-' + input.defaultValue "
                          + "+ '-' + input.getAttribute('value') "
                          + "+ '-' + input.files.length);\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='file' id='f' value='Hello world' onChange='log(\"foo\");log(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='test()'>setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();

        verifyTitle2(driver, getExpectedAlerts());

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValues() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('file1');\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'file';\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"file\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='file1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('file1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'file';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"file\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='file1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-initial-initial", "-initial-initial",
                       "exception", "-initial-initial", "-initial-initial",
                       "-newDefault-newDefault", "-newDefault-newDefault"},
            IE = {"-initial-initial", "-initial-initial",
                  "-initial-initial", "-initial-initial",
                  "-newDefault-newDefault", "-newDefault-newDefault"})
    public void resetByClick() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var file = document.getElementById('testId');\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    try{\n"
            + "      file.value = 'newValue';\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.defaultValue = 'newDefault';\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-initial-initial", "-initial-initial",
                       "exception", "-initial-initial", "-initial-initial",
                       "-newDefault-newDefault", "-newDefault-newDefault"},
            IE = {"-initial-initial", "-initial-initial",
                  "-initial-initial", "-initial-initial",
                  "-newDefault-newDefault", "-newDefault-newDefault"})
    public void resetByJS() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var file = document.getElementById('testId');\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    try{\n"
            + "      file.value = 'newValue';\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.defaultValue = 'newDefault';\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-initial-initial", "-default-default",
                       "exception", "-default-default", "-attribValue-attribValue",
                       "-newDefault-newDefault"},
            IE = {"-initial-initial", "-default-default",
                  "-default-default", "-attribValue-attribValue",
                  "-newDefault-newDefault"})
    public void value() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var file = document.getElementById('testId');\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.defaultValue = 'default';\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    try{\n"
            + "      file.value = 'newValue';\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.setAttribute('value', 'attribValue');\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.defaultValue = 'newDefault';\n"
            + "    log(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("textLength not available")
    public void textLength() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    if(text.textLength) {\n"
            + "      log(text.textLength);\n"
            + "    } else {\n"
            + "      log('textLength not available');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "0"},
            IE = {"ex start", "ex end", "exception"})
    public void selection() throws Exception {
        final String html =
              "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = getSelection(document.getElementById('text1'));\n"
            + "    if (s != undefined) {\n"
            + "      log(s.length);\n"
            + "    }\n"
            + "  }\n"
            + "  function getSelection(element) {\n"
            + "    try {\n"
            + "      log(element.selectionStart);\n"
            + "    } catch(e) { log('ex start'); }\n"
            + "    try {\n"
            + "      log(element.selectionEnd);\n"
            + "    } catch(e) { log('ex end'); }\n"
            + "    try {\n"
            + "      return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='file' id='text1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception value", "null,null",
                       "exception", "null,null",
                       "exception", "null,null"},
            IE = {"exception", "exception",
                  "exception", "exception",
                  "exception", "exception"})
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception value", "null,null",
                       "exception", "null,null",
                       "exception", "null,null"},
            IE = {"exception", "exception",
                  "exception", "exception",
                  "exception", "exception"})
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception value", "null,null",
                       "exception", "null,null",
                       "exception", "null,null"},
            IE = {"exception", "exception",
                  "exception", "exception",
                  "exception", "exception"})
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='Bonjour' type='file'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"

            + "  try{\n"
            + "    input.value = '12345678900';\n"
            + "  } catch(e) { log('exception value'); }\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"

            + "  try {\n"
            + "    input.selectionStart = " + selectionStart + ";\n"
            + "  } catch(e) { log('exception'); }\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"

            + "  try {\n"
            + "    input.selectionEnd = " + selectionEnd + ";\n"
            + "  } catch(e) { log('exception'); }\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception"},
            IE = "exception")
    public void selectionOnUpdate() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='Hello' type='file'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.selectionStart = 4;\n"
            + "    input.selectionEnd = 5;\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.value = 'abcdefghif';\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.value = 'abcd';\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.selectionStart = 0;\n"
            + "    input.selectionEnd = 4;\n"

            + "    input.value = 'a';\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"changed2", "changed"})
    public void firingOnchange() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "<form onchange='log(\"changed\")'>\n"
            + "  <input type='file' id='file1' onchange='log(\"changed2\")' "
                + "onkeydown='log(\"onkeydown2\")' "
                + "onkeypress='log(\"onkeypress2\")' "
                + "onkeyup='log(\"onkeyup2\")'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final File tmpFile = File.createTempFile("htmlunit-test", ".txt");
        driver.findElement(By.id("file1")).sendKeys(tmpFile.getAbsolutePath());
        assertTrue(tmpFile.delete());

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void nonZeroWidthHeight() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var file = document.getElementById('testId');\n"
                + "    log(file.clientWidth > 2);\n"
                + "    log(file.clientHeight > 2);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<form>\n"
                + "  <input type='file' id='testId'>\n"
                + "</form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("C:\\fakepath\\pom.xml--null")
    // since 2.28
    // there is an option for IE, for local and trusted sites IE includes the file path
    // because we do not support any IE specific setting we do not send the filename as
    // done by the other browsers
    public void value2() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var input = document.getElementById('f');\n"
              + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='file' id='f'>\n"
              + "  <button id='clickMe' onclick='test()'>Click Me</button>\n"
              + "</body></html>";

        final String absolutePath = new File("pom.xml").getAbsolutePath();

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("f")).sendKeys(absolutePath);
        driver.findElement(By.id("clickMe")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"--null", "--"})
    public void setAttribute() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var input = document.createElement('input');\n"
              + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
              + "    input.setAttribute('value', '');\n"
              + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body onload='test()'>\n"
              + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"foo", "change"})
    public void onchange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='file' id='f' value='Hello world' onChange='log(\"foo\");log(event.type);'>\n"
              + "  <button id='clickMe' onclick='test()'>Click Me</button>\n"
              + "</body></html>";

        final String absolutePath = new File("pom.xml").getAbsolutePath();

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("f")).sendKeys(absolutePath);

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("C:\\fakepath\\pom.xml")
    // since 2.28
    // there is an option for IE, for local and trusted sites IE includes the file path
    // because we do not support any IE specific setting we do not send the filename as
    // done by the other browsers
    public void getAttribute() throws Exception {
        final String html =
              "<html><body>\n"
              + "  <input type='file' id='f'>\n"
              + "</body></html>";

        final String absolutePath = new File("pom.xml").getAbsolutePath();

        final WebDriver driver = loadPage2(html);
        final WebElement e = driver.findElement(By.id("f"));
        e.sendKeys(absolutePath);
        assertEquals(getExpectedAlerts()[0], e.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("--")
    public void minMaxStep() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    log(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidate() throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('o1').willValidate);\n"
                + "      log(document.getElementById('o2').willValidate);\n"
                + "      log(document.getElementById('o3').willValidate);\n"
                + "      log(document.getElementById('o4').willValidate);\n"
                + "      log(document.getElementById('o5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <input type='file' id='o1'>\n"
                + "    <input type='file' id='o2' disabled>\n"
                + "    <input type='file' id='o3' hidden>\n"
                + "    <input type='file' id='o4' readonly>\n"
                + "    <input type='file' id='o5' style='display: none'>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationEmpty() throws Exception {
        validation("<input type='file' id='e1'>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='file' id='e1'>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='file' id='e1'>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='file' id='e1'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-false-false-false-false-false-false-false-false-false-true",
                       "true"},
            IE = {"false",
                  "undefined-false-false-false-false-false-false-undefined-false-false-true",
                  "true"})
    public void validationRequired() throws Exception {
        validation("<input type='file' id='e1' required>\n", "");
    }

    private void validation(final String htmlPart, final String jsPart) throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function logValidityState(s) {\n"
                + "      log(s.badInput"
                        + "+ '-' + s.customError"
                        + "+ '-' + s.patternMismatch"
                        + "+ '-' + s.rangeOverflow"
                        + "+ '-' + s.rangeUnderflow"
                        + "+ '-' + s.stepMismatch"
                        + "+ '-' + s.tooLong"
                        + "+ '-' + s.tooShort"
                        + " + '-' + s.typeMismatch"
                        + " + '-' + s.valid"
                        + " + '-' + s.valueMissing);\n"
                + "    }\n"
                + "    function test() {\n"
                + "      var elem = document.getElementById('e1');\n"
                + jsPart
                + "      log(elem.checkValidity());\n"
                + "      logValidityState(elem.validity);\n"
                + "      log(elem.willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + htmlPart
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void clear() throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f =  document.createElement('input');\n"
                + "      f.type='file';\n"
                + "      f.id='fileId';\n"
                + "      document.body.appendChild(f);"

                + "      f.value='';\n"
                + "      log(f.files.length);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
