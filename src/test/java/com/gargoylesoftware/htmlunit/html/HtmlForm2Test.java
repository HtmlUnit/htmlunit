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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Tests for {@link HtmlForm}, with BrowserRunner.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlForm2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"myForm", "TypeError"},
            IE = {"myForm", "myForm"})
    public void formsAccessor_FormsAsFunction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    alert(document.forms[0].id);\n"
            + "    alert(document.forms(0).id);\n"
            + "  } catch (err) {\n"
            + "    alert('TypeError');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form id='myForm'>\n"
            + "  <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"myForm", "TypeError"},
            IE = {"myForm", "myForm"})
    public void formsAccessor_FormsAsFunction2() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    alert(document.forms['myName'].id);\n"
            + "    alert(document.forms('myName').id);\n"
            + "  } catch (err) {\n"
            + "    alert('TypeError');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form id='myForm' name='myName'>\n"
            + "  <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"error", "error", "error"},
            IE = {"textfieldid", "textfieldname", "textfieldid"})
    public void asFunction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var f1 = document.forms[0];\n"
            + "  try { alert(f1('textfieldid').id) } catch (e) { alert('error') }\n"
            + "  try { alert(f1('textfieldname').name) } catch (e) { alert('error') }\n"
            + "  try { alert(f1(0).id) } catch (e) { alert('error') }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<p>hello world</p>\n"
            + "<form id='firstid' name='firstname'>\n"
            + "  <input type='text' id='textfieldid' value='foo' />\n"
            + "  <input type='text' name='textfieldname' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            IE = {"textfieldid", "textfieldname", "textfieldid"})
    public void asFunctionFormsFunction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var f1 = document.forms(0);\n"
            + "    try { alert(f1('textfieldid').id) } catch (e) { alert('error') }\n"
            + "    try { alert(f1('textfieldname').name) } catch (e) { alert('error') }\n"
            + "    try { alert(f1(0).id) } catch (e) { alert('error') }\n"
            + "  } catch (e) { alert('TypeError') }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<p>hello world</p>\n"
            + "<form id='firstid' name='firstname'>\n"
            + "  <input type='text' id='textfieldid' value='foo' />\n"
            + "  <input type='text' name='textfieldname' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void base() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <base href='" + URL_SECOND + "'>\n"
            + "</head><body>\n"
            + "<form action='two.html'>\n"
            + "  <input type='submit'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("<html><head></head><body>foo</body></html>");

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ByTagName("input")).click();

        final URL requestedUrl = getMockWebConnection().getLastWebRequest().getUrl();
        final URL expectedUrl = new URL(URL_SECOND, "two.html");
        assertEquals(expectedUrl, requestedUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void emptyActionWithBase() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <base href='" + URL_SECOND + "'>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "  <input type='submit'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("<html><head></head><body>foo</body></html>");

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ByTagName("input")).click();

        final URL requestedUrl = getMockWebConnection().getLastWebRequest().getUrl();
        assertEquals(URL_FIRST.toExternalForm(), requestedUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void emptyActionWithBase2() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <base href='" + URL_SECOND + "'>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "  <input name='myName' value='myValue'>\n"
            + "  <input type='submit'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("<html><head></head><body>foo</body></html>");

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ByTagName("input")).click();

        final URL requestedUrl = getMockWebConnection().getLastWebRequest().getUrl();
        assertEquals(URL_FIRST.toExternalForm(), requestedUrl);
    }

    /**
      * Simulates a bug report where using JavaScript to submit a form that contains a
      * JavaScript action causes a an "IllegalArgumentException: JavaScript URLs can only
      * be used to load content into frames and iframes".
      *
      * @throws Exception if the test fails
      */
    @Test
    @Alerts("clicked")
    public void jSSubmit_JavaScriptAction() throws Exception {
        final String html
            = "<html><head><title>First</title></head>\n"
            + "<body onload='document.getElementById(\"aForm\").submit()'>\n"
            + "<form id='aForm' action='javascript:alert(\"clicked\")'"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test page can't be loaded
     */
    @Test
    @Alerts({"1", "val2"})
    public void malformedHtml_nestedForms() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.forms.length);\n"
            + "    alert(document.forms[0].field2.value);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form id='form1' method='get' action='foo'>\n"
            + "  <input name='field1' value='val1'/>\n"
            + "  <form>\n"
            + "  <input name='field2' value='val2'/>\n"
            + "  <input type='submit' id='submitButton'/>\n"
            + "  </form>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§?par%F6m=Hello+G%FCnter", "par\u00F6m", "Hello G\u00FCnter"})
    public void encodingSubmit() throws Exception {
        stopWebServers();
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <input name='par\u00F6m' value='Hello G\u00FCnter'>\n"
            + "    <input id='mySubmit' type='submit' value='Submit'>\n"
            + "  </form>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=ISO-8859-1", ISO_8859_1, ISO_8859_1);
        driver.findElement(new ById("mySubmit")).click();

        assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());

        final List<NameValuePair> requestedParams = getMockWebConnection().getLastWebRequest().getRequestParameters();
        assertEquals(1, requestedParams.size());
        assertEquals(getExpectedAlerts()[1], requestedParams.get(0).getName());
        assertEquals(getExpectedAlerts()[2], requestedParams.get(0).getValue());
    }

    /**
     * Tests the 'Origin' and 'Referer' HTTP header.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"null", "§§URL§§path?query"})
    public void originRefererHeaderGet() throws Exception {
        final String firstHtml
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form method='get' action='" + URL_SECOND + "'>\n"
            + "    <input id='mySubmit' type='submit' value='Submit'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";
        final String secondHtml = "<html><body></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        final URL requestUrl = new URL(URL_FIRST, "/path?query");
        webConnection.setResponse(URL_SECOND, secondHtml);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(firstHtml, requestUrl);

        driver.findElement(new ById("mySubmit")).click();

        final Map<String, String> lastAdditionalHeaders = webConnection.getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], "" + lastAdditionalHeaders.get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[1], "" + lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

    /**
     * Tests the 'Origin' HTTP header.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"§§URL§§", "§§URL§§/path?query"},
            IE = {"null", "§§URL§§/path?query"})
    public void originRefererHeaderPost() throws Exception {
        final String firstHtml
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form method='post' action='" + URL_SECOND + "'>\n"
            + "    <input id='mySubmit' type='submit' value='Submit'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";
        final String secondHtml = "<html><body></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        final URL requestUrl = new URL(URL_FIRST, "/path?query");
        webConnection.setResponse(URL_SECOND, secondHtml);

        String url = URL_FIRST.toExternalForm();
        url = url.substring(0, url.length() - 1);
        expandExpectedAlertsVariables(url);
        final WebDriver driver = loadPage2(firstHtml, requestUrl);

        driver.findElement(new ById("mySubmit")).click();

        final Map<String, String> lastAdditionalHeaders = webConnection.getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], "" + lastAdditionalHeaders.get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[1], "" + lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "text/html,application/xhtml+xml,application/xml;q=0.9,"
                    + "image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            EDGE = "text/html,application/xhtml+xml,application/xml;q=0.9,"
                    + "image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            FF = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            FF78 = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            IE = "text/html, application/xhtml+xml, image/jxr, */*")
    public void acceptHeader() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head></head><body>\n"
            + "  <form action='test2'>\n"
            + "    <input type=submit id='mySubmit'>\n"
            + "  </form>\n"
            + "</body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", AcceptHeaderServlet.class);

        final WebDriver driver = loadPage2(html, servlets);
        driver.findElement(By.id("mySubmit")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Servlet for {@link #acceptHeader()}.
     */
    public static class AcceptHeaderServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            final Writer writer = response.getWriter();
            final String html = "<html><head><script>\n"
                    + "function test() {\n"
                    + "  alert('" + request.getHeader(HttpHeader.ACCEPT) + "');\n"
                    + "}\n"
                    + "</script></head><body onload='test()'></body></html>";

            writer.write(html);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "gzip, deflate",
            CHROME = "gzip, deflate, br",
            EDGE = "gzip, deflate, br")
    public void acceptEncodingHeader() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head></head><body>\n"
            + "  <form action='test2'>\n"
            + "    <input type=submit id='mySubmit'>\n"
            + "  </form>\n"
            + "</body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", AcceptEncodingHeaderServlet.class);

        final WebDriver driver = loadPage2(html, servlets);
        driver.findElement(By.id("mySubmit")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void formMultipartEncodingTypeTest() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST
                    + "' enctype='" + FormEncodingType.MULTIPART.getName()
                    + "'>\n"
            + "    <input type='file' value='file1'>\n"
            + "    <button id='myButton' type='submit'>Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html, URL_FIRST);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(FormEncodingType.MULTIPART, getMockWebConnection().getLastWebRequest().getEncodingType());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void formUrlEncodedEncodingTypeTest() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                        + "' method='" + HttpMethod.POST
                        + "' enctype='" + FormEncodingType.URL_ENCODED.getName()
                        + "'>\n"
            + "    <button id='myButton' type='submit'>Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";

        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html, URL_FIRST);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(FormEncodingType.URL_ENCODED, getMockWebConnection().getLastWebRequest().getEncodingType());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "third"})
    public void buttonWithFormAction() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head><title>first</title></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <button id='myButton' type='submit' formaction='" + URL_THIRD
                        + "'>Submit with different form action</button>\n"
            + "  </form>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>second</title></head>\n"
                + "<body>\n"
                + "  <p>hello world</p>\n"
                + "</body></html>";

        final String thirdContent = "<html><head><title>third</title></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());
        assertTrue(driver.getPageSource().contains(getExpectedAlerts()[1]));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buttonWithFormActionNegative() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <button id='myButton' type='reset' formaction='" + URL_THIRD
            + "'>Submit with different form action</button>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        //no additional actions
        assertEquals(1, getMockWebConnection().getRequestCount());
        assertEquals(URL_FIRST.toString(), getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "third/"})
    public void inputTypeSubmitWithFormAction() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <input id='myButton' type='submit' formaction='" + URL_THIRD + "' />\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";
        final String thirdContent = "third content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());
        assertTrue(getMockWebConnection().getLastWebRequest()
                    .getUrl().toExternalForm().endsWith(getExpectedAlerts()[1]));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "third content",
            IE = "second content")
    public void inputTypeImageWithFormAction() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <input id='myButton' type='image' alt='Submit' formaction='" + URL_THIRD + "' />\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";
        final String thirdContent = "third content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertTrue("Incorrect conent of new window", driver.getPageSource().contains(getExpectedAlerts()[0]));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buttonSubmitWithFormMethod() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                                + "' method='" + HttpMethod.POST + "'>\n"
            + "    <button id='myButton' type='submit' formmethod='" + HttpMethod.GET
                        + "'>Submit with different form method</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(HttpMethod.GET, getMockWebConnection().getLastWebRequest().getHttpMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputTypeSubmitWithFormMethod() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                                + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input id='myButton' type='submit' formmethod='" + HttpMethod.GET + "' />\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(HttpMethod.GET, getMockWebConnection().getLastWebRequest().getHttpMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "GET",
            IE = "POST")
    public void inputTypeImageWithFormMethod() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                                + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input id='myButton' type='image' alt='Submit' formmethod='" + HttpMethod.GET + "' />\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(),
                UrlUtils.getUrlWithNewQuery(getMockWebConnection().getLastWebRequest().getUrl(), null));
        assertEquals(getExpectedAlerts()[0], getMockWebConnection().getLastWebRequest().getHttpMethod().name());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buttonWithFormEnctype() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                                + "' method='" + HttpMethod.POST
                                + "' enctype='" + FormEncodingType.URL_ENCODED.getName() + "'>\n"
            + "    <input type='file' value='file1'>\n"
            + "    <button id='myButton' type='submit' formenctype='" + FormEncodingType.MULTIPART.getName()
            + "'>Submit with different form encoding type</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(FormEncodingType.MULTIPART, getMockWebConnection().getLastWebRequest().getEncodingType());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputTypeSubmitWithFormEnctype() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                                + "' method='" + HttpMethod.POST
                                + "' enctype='" + FormEncodingType.URL_ENCODED.getName()
                                + "'>\n"
            + "    <input type='file' value='file1'>\n"
            + "    <input id='myButton' type='submit' formenctype='" + FormEncodingType.MULTIPART.getName() + "' />\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(FormEncodingType.MULTIPART, getMockWebConnection().getLastWebRequest().getEncodingType());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "application/x-www-form-urlencoded",
            IE = "multipart/form-data")
    public void inputTypeImageWithFormEnctype() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                                + "' method='" + HttpMethod.POST
                                + "' enctype='" + FormEncodingType.MULTIPART.getName() + "'>\n"
            + "    <input id='myButton' type='image' formenctype='" + FormEncodingType.URL_ENCODED.getName() + "' />\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html, URL_FIRST);
        driver.findElement(By.id("myButton")).click();

        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(getExpectedAlerts()[0],
                    getMockWebConnection().getLastWebRequest().getEncodingType().getName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buttonWithFormTarget() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "' target='_self'>\n"
            + "    <button id='myButton' type='submit' "
                                + "formtarget='_blank'>Submit with different form target</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        // check that initially we have one window
        assertEquals("Incorrect number of openned window", 1, driver.getWindowHandles().size());
        final String firstWindowId = driver.getWindowHandle();

        driver.findElement(By.id("myButton")).click();

        // check that after submit we have a new window
        assertEquals("Incorrect number of openned window", 2, driver.getWindowHandles().size());

        String newWindowId = "";
        for (final String id : driver.getWindowHandles()) {
            if (!firstWindowId.equals(id)) {
                newWindowId = id;
                break;
            }
        }
        // switch to the new window and check its content
        driver.switchTo().window(newWindowId);
        assertTrue("Incorrect conent of new window", driver.getPageSource().contains(secondContent));
        driver.close();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second content")
    public void inputTypeSubmitWithFormTarget() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "' target='_self'>\n"
            + "    <input id='myButton' type='submit' formtarget='_blank' />\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        // check that initially we have one window
        assertEquals("Incorrect number of openned window", 1, driver.getWindowHandles().size());
        final String firstWindowId = driver.getWindowHandle();

        driver.findElement(By.id("myButton")).click();

        // check that after submit we have a new window
        assertEquals("Incorrect number of openned window", 2, driver.getWindowHandles().size());

        String newWindowId = "";
        for (final String id : driver.getWindowHandles()) {
            if (!firstWindowId.equals(id)) {
                newWindowId = id;
                break;
            }
        }
        // switch to the new window and check its content
        driver.switchTo().window(newWindowId);
        assertTrue("Incorrect conent of new window", driver.getPageSource().contains(getExpectedAlerts()[0]));
        driver.close();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "1")
    @NotYetImplemented(IE)
    public void inputTypeImageWithFormTarget() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "' target='_self'>\n"
            + "    <input id='myButton' type='image' alt='Submit' formtarget='_blank' />\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        // check that initially we have one window
        assertEquals("Incorrect number of openned window", 1, driver.getWindowHandles().size());
        final String firstWindowId = driver.getWindowHandle();

        driver.findElement(By.id("myButton")).click();

        // check that after submit we have a new window
        assertEquals("Incorrect number of openned window",
                Integer.parseInt(getExpectedAlerts()[0]), driver.getWindowHandles().size());

        String newWindowId = "";
        for (final String id : driver.getWindowHandles()) {
            if (!firstWindowId.equals(id)) {
                newWindowId = id;
                break;
            }
        }
        // switch to the new window and check its content
        driver.switchTo().window(newWindowId);
        assertTrue("Incorrect conent of new window", driver.getPageSource().contains(secondContent));
        driver.close();
    }

    /**
     * Servlet for {@link #acceptEncodingHeader()}.
     */
    public static class AcceptEncodingHeaderServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            final Writer writer = response.getWriter();
            final String html = "<html><head><script>\n"
                    + "function test() {\n"
                    + "  alert('" + request.getHeader(HttpHeader.ACCEPT_ENCODING) + "');\n"
                    + "}\n"
                    + "</script></head><body onload='test()'></body></html>";

            writer.write(html);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second")
    public void novalidate() throws Exception {
        final String html = "<html>\n"
            + "<head><title>first</title></head>\n"
            + "<body>\n"
            + "  <form name='testForm' action='\" + URL_SECOND + \"' novalidate>\n"
            + "    <input type='submit' id='submit'>\n"
            + "    <input name='test' value='' required='required' >"
            + "  </form>\n"
            + "</body></html>";

        final String html2 = "<?xml version='1.0'?>\n"
            + "<html>\n"
            + "<head><title>second</title></head>\n"
            + "<body>OK</body></html>";
        getMockWebConnection().setDefaultResponse(html2);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("submit")).click();

        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second")
    public void submitFormnovalidate() throws Exception {
        final String html = "<html>\n"
            + "<head><title>first</title></head>\n"
            + "<body>\n"
            + "  <form name='testForm' action='\" + URL_SECOND + \"'>\n"
            + "    <input type='submit' id='submit' formnovalidate>\n"
            + "    <input name='test' value='' required='required' >"
            + "  </form>\n"
            + "</body></html>";

        final String html2 = "<?xml version='1.0'?>\n"
            + "<html>\n"
            + "<head><title>second</title></head>\n"
            + "<body>OK</body></html>";
        getMockWebConnection().setDefaultResponse(html2);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("submit")).click();

        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second")
    public void submitButtonFormnovalidate() throws Exception {
        final String html = "<html>\n"
            + "<head><title>first</title></head>\n"
            + "<body>\n"
            + "  <form name='testForm' action='\" + URL_SECOND + \"'>\n"
            + "    <button type='submit' id='submit' formnovalidate>submit</button>\n"
            + "    <input name='test' value='' required='required' >"
            + "  </form>\n"
            + "</body></html>";

        final String html2 = "<?xml version='1.0'?>\n"
            + "<html>\n"
            + "<head><title>second</title></head>\n"
            + "<body>OK</body></html>";
        getMockWebConnection().setDefaultResponse(html2);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("submit")).click();

        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second")
    public void defaultButtonFormnovalidate() throws Exception {
        final String html = "<html>\n"
            + "<head><title>first</title></head>\n"
            + "<body>\n"
            + "  <form name='testForm' action='\" + URL_SECOND + \"'>\n"
            + "    <button id='submit' formnovalidate>submit</button>\n"
            + "    <input name='test' value='' required='required' >"
            + "  </form>\n"
            + "</body></html>";

        final String html2 = "<?xml version='1.0'?>\n"
            + "<html>\n"
            + "<head><title>second</title></head>\n"
            + "<body>OK</body></html>";
        getMockWebConnection().setDefaultResponse(html2);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("submit")).click();

        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"radioParam2#radioValue2", "selectParam#selectValue", "textParam#textValue",
        "textareaParam#textarea value"},
            IE = {})
    public void submitUsingFormAttribute() throws Exception {
        final String html =
            "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form id='formId'>\n"
            + "  </form>\n"

            + "  <input form='formId' type='text' name='textParam' value='textValue'>\n"

            + "  <fieldset form='formId'>\n"
            + "    <input type='hidden' name='hiddenParam' value='hiddenValue'>\n"
            + "    <input type='text' name='fieldsetTextParam' value='fieldsetTextValue'>\n"
            + "  </fieldset>\n"

            + "  <label for='radioId' form='formId'>Male</label>\n"
            + "  <input id='radioId' type='radio' name='radioParam' value='radioValue'>\n"

            + "  <input form='formId' type='radio' name='radioParam2' value='radioValue2' checked='checked'>\n"

            + "  <select form='formId' name='selectParam'>\n"
            + "    <option value='selectValue' selected='selected'>selected</option>\n"
            + "  </select>\n"

            + "  <textarea form='formId' name='textareaParam'>textarea value</textarea>\n"

            + "  <input form='formId' id='mySubmit' type='submit' value='Submit'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ById("mySubmit")).click();

        final List<NameValuePair> requestedParams = getMockWebConnection().getLastWebRequest().getRequestParameters();
        Collections.sort(requestedParams, Comparator.comparing(NameValuePair::getName));

        assertEquals(getExpectedAlerts().length, requestedParams.size());

        for (int i = 0; i < requestedParams.size(); i++) {
            assertEquals(getExpectedAlerts()[i],
                    requestedParams.get(i).getName() + '#' + requestedParams.get(i).getValue());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"radioParam2#radioValue2", "selectParam#selectValue", "textParam#textValue",
        "textareaParam#textarea value"},
            IE = {})
    public void submitUsingFormAttributeElementsDeclaredBeforeForm() throws Exception {
        final String html =
            "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"

            + "  <input form='formId' type='text' name='textParam' value='textValue'>\n"

            + "  <fieldset form='formId'>\n"
            + "    <input type='hidden' name='hiddenParam' value='hiddenValue'>\n"
            + "    <input type='text' name='fieldsetTextParam' value='fieldsetTextValue'>\n"
            + "  </fieldset>\n"

            + "  <label for='radioId' form='formId'>Male</label>\n"
            + "  <input id='radioId' type='radio' name='radioParam' value='radioValue'>\n"

            + "  <input form='formId' type='radio' name='radioParam2' value='radioValue2' checked='checked'>\n"

            + "  <select form='formId' name='selectParam'>\n"
            + "    <option value='selectValue' selected='selected'>selected</option>\n"
            + "  </select>\n"

            + "  <textarea form='formId' name='textareaParam'>textarea value</textarea>\n"

            + "  <input form='formId' id='mySubmit' type='submit' value='Submit'>\n"

            + "  <form id='formId'>\n"
            + "  </form>\n"

            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ById("mySubmit")).click();

        final List<NameValuePair> requestedParams = getMockWebConnection().getLastWebRequest().getRequestParameters();
        Collections.sort(requestedParams, Comparator.comparing(NameValuePair::getName));

        assertEquals(getExpectedAlerts().length, requestedParams.size());

        for (int i = 0; i < requestedParams.size(); i++) {
            assertEquals(getExpectedAlerts()[i],
                    requestedParams.get(i).getName() + '#' + requestedParams.get(i).getValue());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "textParam#textValue",
            IE = {})
    public void submitUsingFormAttributeElementsDeeplyNested() throws Exception {
        final String html =
            "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"

            + "  <form id='formId'>\n"
            + "  </form>\n"

            + "  <div><div><div><div>\n"
            + "    <input form='formId' type='text' name='textParam' value='textValue'>\n"
            + "  </div></div></div></div>\n"

            + "  <input form='formId' id='mySubmit' type='submit' value='Submit'>\n"

            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ById("mySubmit")).click();

        final List<NameValuePair> requestedParams = getMockWebConnection().getLastWebRequest().getRequestParameters();
        Collections.sort(requestedParams, Comparator.comparing(NameValuePair::getName));

        assertEquals(getExpectedAlerts().length, requestedParams.size());

        for (int i = 0; i < requestedParams.size(); i++) {
            assertEquals(getExpectedAlerts()[i],
                    requestedParams.get(i).getName() + '#' + requestedParams.get(i).getValue());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "hiddenParam#form1",
            IE = "hiddenParam#form2")
    public void submitFromInsideAnother() throws Exception {
        final String html =
            "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form id='formId'>\n"
            + "    <input type='hidden' name='hiddenParam' value='form1'>\n"
            + "  </form>\n"

            + "  <form id='formId2'>\n"
            + "    <input type='hidden' name='hiddenParam' value='form2'>\n"
            + "    <input form='formId' id='mySubmit' type='submit' value='Submit'>\n"
            + "  </form>\n"

            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ById("mySubmit")).click();

        final List<NameValuePair> requestedParams = getMockWebConnection().getLastWebRequest().getRequestParameters();
        Collections.sort(requestedParams, Comparator.comparing(NameValuePair::getName));

        assertEquals(getExpectedAlerts().length, requestedParams.size());

        for (int i = 0; i < requestedParams.size(); i++) {
            assertEquals(getExpectedAlerts()[i],
                    requestedParams.get(i).getName() + '#' + requestedParams.get(i).getValue());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "hiddenParam#form2")
    public void submitFromInsideAnotherInvalidFormRef() throws Exception {
        final String html =
            "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form id='formId'>\n"
            + "    <input type='hidden' name='hiddenParam' value='form1'>\n"
            + "  </form>\n"

            + "  <form id='formId2'>\n"
            + "    <input type='hidden' name='hiddenParam' value='form2'>\n"
            + "    <input form='formIdInvalid' id='mySubmit' type='submit' value='Submit'>\n"
            + "  </form>\n"

            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ById("mySubmit")).click();

        final List<NameValuePair> requestedParams = getMockWebConnection().getLastWebRequest().getRequestParameters();
        Collections.sort(requestedParams, Comparator.comparing(NameValuePair::getName));

        assertEquals(getExpectedAlerts().length, requestedParams.size());

        for (int i = 0; i < requestedParams.size(); i++) {
            assertEquals(getExpectedAlerts()[i],
                    requestedParams.get(i).getName() + '#' + requestedParams.get(i).getValue());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"button#foo", "textfield#"})
    public void submit_NoDefaultValue() throws Exception {
        final String controls =
                "  <input type='text' name='textfield'/>\n"
                + "  <input type='submit' id='mySubmit' name='button' value='foo'/>\n";

        submitParams(controls);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("button#foo")
    public void submit_NoNameOnControl() throws Exception {
        final String controls =
                "  <input type='text' id='textfield' value='blah' />\n"
                + "  <input type='submit' id='mySubmit' name='button' value='foo'/>\n";

        submitParams(controls);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("textfield#blah")
    public void submit_NoNameOnButton() throws Exception {
        final String controls =
                "  <input type='text' id='textfield' value='blah' name='textfield' />\n"
                + "  <button type='submit' id='mySubmit' value='Go'>Go</button>\n";

        submitParams(controls);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"submit#submit", "textfield#blah", "textfield2#blaha"})
    public void submit_NestedInput() throws Exception {
        final String controls =
                "  <table><tr><td>\n"
                + "    <input type='text' name='textfield' value='blah'/>\n"
                + "    </td><td>\n"
                + "    <input type='text' name='textfield2' value='blaha'/>\n"
                + "    </td></tr>\n"

                + "    <tr><td>\n"
                + "    <input type='submit' name='submit' id='mySubmit' value='submit'/>\n"
                + "    </td><td></td></tr>\n"
                + "  </table>\n";

        submitParams(controls);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submit#submit")
    public void submit_IgnoresDisabledControls() throws Exception {
        final String controls =
                "  <input type='text' name='textfield' value='blah' disabled />\n"
                + "  <input type='submit' name='submit' id='mySubmit' value='submit'/>\n";

        submitParams(controls);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hidden#blah", "submit#submit"})
    public void submit_IgnoresDisabledHiddenControls() throws Exception {
        final String controls =
                "  <input type='text' name='textfield' value='blah' disabled />\n"
                + "  <input type='hidden' name='disabledHidden' value='blah' disabled />\n"
                + "  <input type='hidden' name='hidden' value='blah' />\n"
                + "  <input type='submit' name='submit' id='mySubmit' value='submit'/>\n";

        submitParams(controls);
    }

    /**
     * Reset buttons should not be submitted.
     * @see <a href="http://www.w3.org/TR/html4/interact/forms.html#h-17.13.2">Spec</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submit#submit")
    public void submit_IgnoresResetControl() throws Exception {
        final String controls =
                "  <input type='reset' name='reset' value='reset'/>\n"
                + "  <input type='submit' name='submit' id='mySubmit' value='submit'/>\n";

        submitParams(controls);
    }

    /**
     * Reset buttons should not be submitted.
     * @see <a href="http://www.w3.org/TR/html4/interact/forms.html#h-17.13.2">Spec</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submit#submit")
    public void submit_IgnoresResetButtonControl() throws Exception {
        final String controls =
                "  <button type='reset' name='buttonreset' value='buttonreset'>Reset</button>\n"
                + "  <input type='submit' name='submit' id='mySubmit' value='submit'/>\n";

        submitParams(controls);
    }

    private void submitParams(final String controls) throws Exception {
        final String html = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + controls
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(new ById("mySubmit")).click();

        final List<NameValuePair> requestedParams = getMockWebConnection().getLastWebRequest().getRequestParameters();
        Collections.sort(requestedParams, Comparator.comparing(NameValuePair::getName));

        assertEquals(getExpectedAlerts().length, requestedParams.size());

        for (int i = 0; i < requestedParams.size(); i++) {
            assertEquals(getExpectedAlerts()[i],
                    requestedParams.get(i).getName() + '#' + requestedParams.get(i).getValue());
        }
    }
}
