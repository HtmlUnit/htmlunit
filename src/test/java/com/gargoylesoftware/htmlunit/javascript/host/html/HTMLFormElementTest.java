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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link HTMLFormElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLFormElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"16", "button1", "button2", "checkbox1", "fileupload1", "hidden1",
            "radio1", "radio1",
            "select1", "select2", "password1", "reset1",
            "reset2", "submit1", "submit2", "textInput1", "textarea1"})
    public void elementsAccessor() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.form1.length);\n"
            + "  for (var i = 0; i < document.form1.length; i++) {\n"
            + "    var element = document.form1.elements[i];\n"
            + "    if (element.type != 'radio' && element != document.form1[element.name]) {\n"
            + "      log('name index not working for '+element.name);\n"
            + "    }\n"
            + "    log(element.name);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='button' name='button1' />\n"
            + "  <button type='button' name='button2'>button2</button>\n"
            + "  <input type='checkbox' name='checkbox1' />\n"
            + "  <input type='file' name='fileupload1' />\n"
            + "  <input type='hidden' name='hidden1' />\n"
            + "  <input type='radio' name='radio1' value='1' />\n"
            + "  <input type='radio' name='radio1' value='2' />\n"
            + "  <select name='select1'>\n"
            + "    <option>foo</option>\n"
            + "  </select>\n"
            + "  <select multiple='multiple' name='select2'>\n"
            + "    <option>foo</option>\n"
            + "  </select>\n"
            + "  <input type='password' name='password1' />\n"
            + "  <input type='reset' name='reset1' />\n"
            + "  <button type='reset' name='reset2'>reset2</button>\n"
            + "  <input type='submit' name='submit1' />\n"
            + "  <button type='submit' name='submit2'>submit2</button>\n"
            + "  <input type='text' name='textInput1' />\n"
            + "  <textarea name='textarea1'>foo</textarea>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined"})
    public void elementsAccessorOutOfBound() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.form1[-1]);\n"
            + "  log(document.form1[2]);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "  <input type='button' name='button1'/>\n"
            + "  <input type='submit' name='submit1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "1", "2", "3"})
    public void radioButtonArray() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var radioArray = document.form1['radio1'];\n"
            + "  log(radioArray.length);\n"
            + "  for (var i = 0; i < radioArray.length; i++) {\n"
            + "    var element = radioArray[i];\n"
            + "    log(element.value);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='radio' name='radio1' value='1'/>\n"
            + "  <input type='radio' name='radio1' value='2'/>\n"
            + "  <input type='radio' name='radio1' value='3'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * If there is only one radio button with a specified name then that radio
     * button will be returned for the name, not an array of radio buttons. Test
     * this.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void radioButton_OnlyOne() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.form1['radio1'].value);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='radio' name='radio1' value='1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://foo.com/", "mailto:me@bar.com", "mailto:me@bar.com"})
    public void actionProperty() throws Exception {
        doTestProperty("action", "action", "http://foo.com/", "mailto:me@bar.com");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myForm", "testForm", "testForm"})
    public void nameProperty() throws Exception {
        doTestProperty("name", "name", "myForm", "testForm");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void defaultEnctype() throws Exception {
        enctype(null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void emptyEnctype() throws Exception {
        enctype("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void blankEnctype() throws Exception {
        enctype(" ");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void unknownEnctype() throws Exception {
        enctype("unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
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
    @Alerts("text/plain")
    public void plainEnctype() throws Exception {
        enctype("text/plain");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void xmlEnctype() throws Exception {
        enctype("text/xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void jsonEnctype() throws Exception {
        enctype("application/json");
    }

    private void enctype(final String encoding) throws Exception {
        String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.forms[0].encoding);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='testForm'";
        if (null != encoding) {
            html = html + " enctype='" + encoding + "'";
        }
        html = html + ">\n"
            + "    <input type='submit' name='submit1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"},
            IE = {"exception", "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"})
    public void jsDefaultEnctype() throws Exception {
        jsEnctype(null);
        jsEncoding(null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"},
            IE = {"exception", "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"})
    public void jsEmptyEnctype() throws Exception {
        jsEnctype("");
        jsEncoding("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"},
            IE = {"exception", "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"})
    public void jsBlankEnctype() throws Exception {
        jsEnctype(" ");
        jsEncoding(" ");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"},
            IE = {"exception", "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"})
    public void jsUnknownEnctype() throws Exception {
        jsEnctype("unknown");
        jsEncoding("unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/x-www-form-urlencoded",
                "application/x-www-form-urlencoded",
                "application/x-www-form-urlencoded"})
    public void jsUrlencodedEnctype() throws Exception {
        jsEnctype("application/x-www-form-urlencoded");
        jsEncoding("application/x-www-form-urlencoded");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"multipart/form-data", "multipart/form-data", "multipart/form-data"})
    public void jsMultipartEnctype() throws Exception {
        jsEnctype("multipart/form-data");
        jsEncoding("multipart/form-data");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain", "text/plain", "text/plain"})
    public void jsPlainEnctype() throws Exception {
        jsEnctype("text/plain");
        jsEncoding("text/plain");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"},
            IE = {"exception", "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"})
    public void jsXmlEnctype() throws Exception {
        jsEnctype("text/xml");
        jsEncoding("text/xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"},
            IE = {"exception", "application/x-www-form-urlencoded",
                        "application/x-www-form-urlencoded"})
    public void jsJsonEnctype() throws Exception {
        jsEnctype("application/json");
        jsEncoding("application/json");
    }

    private void jsEnctype(final String enctype) throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "  function doTest() {\n"
            + "    try {\n"
            + "      document.forms[0].enctype = '" + enctype + "';\n"
            + "      alert(document.forms[0].enctype);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(document.forms[0].encoding);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form id='testForm' name='testForm' method='post' action = 'page2.html'>\n"
            + "    <input type='submit' name='submit1' />\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[0], getExpectedAlerts()[1]});

        driver.findElement(By.name("submit1")).click();
        assertTitle(driver, "Response");
        String headerValue = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);

        if (headerValue.startsWith(FormEncodingType.MULTIPART.getName())) {
            // Can't test equality for multipart/form-data as it will have the form:
            // multipart/form-data; boundary=---------------------------42937861433140731107235900
            headerValue = StringUtils.substringBefore(headerValue, ";");
            assertEquals(getExpectedAlerts()[2], headerValue);
        }
        else {
            assertEquals(getExpectedAlerts()[2], headerValue);
        }
    }

    private void jsEncoding(final String encoding) throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "  function doTest() {\n"
            + "    try {\n"
            + "      document.forms[0].encoding = '" + encoding + "';\n"
            + "      alert(document.forms[0].encoding);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(document.forms[0].enctype);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form id='testForm' name='testForm' method='post' action = 'page2.html'>\n"
            + "    <input type='submit' name='submit1' />\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[0], getExpectedAlerts()[1]});

        driver.findElement(By.name("submit1")).click();
        assertTitle(driver, "Response");
        String headerValue = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);
        if (headerValue.startsWith(FormEncodingType.MULTIPART.getName())) {
            // Can't test equality for multipart/form-data as it will have the form:
            // multipart/form-data; boundary=---------------------------42937861433140731107235900
            headerValue = StringUtils.substringBefore(headerValue, ";");
            assertEquals(getExpectedAlerts()[2], headerValue);
        }
        else {
            assertEquals(getExpectedAlerts()[2], headerValue);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"multipart/form-data", "application/x-www-form-urlencoded", "application/x-www-form-urlencoded"})
    public void encodingProperty() throws Exception {
        doTestProperty("encoding", "enctype", "multipart/form-data", "application/x-www-form-urlencoded");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"text/plain", "application/x-www-form-urlencoded", "newEncoding"},
            IE = {"text/plain", "exception"})
    public void encodingProperty_textPlain() throws Exception {
        doTestProperty("encoding", "enctype", MimeType.TEXT_PLAIN, "newEncoding");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"application/x-www-form-urlencoded", "application/x-www-form-urlencoded", "newEncoding"},
            IE = {"application/x-www-form-urlencoded", "exception"})
    public void encodingProperty_dummyValues() throws Exception {
        doTestProperty("encoding", "enctype", "myEncoding", "newEncoding");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"get", "post", "post"})
    public void methodProperty() throws Exception {
        doTestProperty("method", "method", "get", "post");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"_top", "_parent", "_parent"})
    public void targetProperty() throws Exception {
        doTestProperty("target", "target", "_top", "_parent");
    }

    private void doTestProperty(final String jsProperty, final String htmlProperty,
            final String oldValue, final String newValue) throws Exception {

        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.forms[0]." + jsProperty + ");\n"
            + "  try {\n"
            + "    document.forms[0]." + jsProperty + "='" + newValue + "';\n"
            + "    log(document.forms[0]." + jsProperty + ");\n"
            + "    log(document.forms[0].getAttribute('" + htmlProperty + "'));\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form " + htmlProperty + "='" + oldValue + "'>\n"
            + "  <input type='button' name='button1' />\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver wd = loadPageVerifyTitle2(html);

        final WebElement form = wd.findElement(By.xpath("//form"));
        if (wd instanceof HtmlUnitDriver && getExpectedAlerts().length >= 3) {
            // form.getAttribute("enctype") returns form.getAttribute("encoding") with the FF driver. Bug or feature?
            assertEquals(getExpectedAlerts()[2], form.getAttribute(htmlProperty));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"id2", "foo"})
    public void inputNamedId() throws Exception {
        doTestInputWithName("id");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"action2", "foo"})
    public void inputNamedAction() throws Exception {
        doTestInputWithName("action");
    }

    private void doTestInputWithName(final String name) throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function go() {\n"
            + "  log(document.simple_form." + name + ".value);\n"
            + "  document.simple_form." + name + ".value = 'foo';\n"
            + "  log(document.simple_form." + name + ".value);\n"
            + "}</script></head>\n"
            + "<body onload='go()'>\n"
            + "<p>hello world</p>\n"
            + "<form action='login.jsp' name='simple_form'>\n"
            + "  <input name='" + name + "' type='hidden' value='" + name + "2'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test that used to blow up on page load.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("value = 2")
    public void accessingRadioButtonArrayByName_Regression() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body><form name='whatsnew'>\n"
            + "<input type='radio' name='second' value='1'>\n"
            + "<input type='radio' name='second' value='2' checked>\n"
            + "</form><script>\n"
            + LOG_TITLE_FUNCTION
            + "clickAction();\n"
            + "function clickAction() {\n"
            + "  var value = -1;\n"
            + "  radios = document.forms['whatsnew'].elements['second'];\n"
            + "  for (var i = 0; i < radios.length; i++){\n"
            + "    if (radios[i].checked == true) {\n"
            + "      value = radios[i].value;\n"
            + "      break;\n"
            + "    }\n"
            + "  }\n"
            + "  log('value = ' + value);\n"
            + "}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for a bug that appeared when visiting mail.yahoo.com. Setting the value of one input
     * seems to blow away the other input. Also tests that the form input collection does not get
     * cached before the document is finished loading.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void findInputWithoutTypeDefined() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body onload='alert(document.simple_form.login.value);'>\n"
            + "<p>hello world</p><table><tr><td>\n"
            + "<form action='login.jsp' name='simple_form'>\n"
            + "  <input name='msg' type='hidden' value='0'>\n"
            + "  <script>document.simple_form.msg.value = 1</script>\n"
            + "  <input name='login' size='17' value='foo'>\n"
            + "</form></td></tr></table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test form.length - This method does not count the type=image
     * input tags.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void length() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.form1.length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "  <input type='radio' name='radio1' value='1'/>\n"
            + "  <input type='image' src='foo' value='1'/>\n"
            + "  <input type='submit' name='submit1' value='1'/>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("button1")
    public void get() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.form1[0].name);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='button' name='button1' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that the elements collection is live.
    * @throws Exception if the test fails
    */
    @Test
    @Alerts({"0", "1", "1", "true"})
    public void elementsLive() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<form name='myForm'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oElements = document.myForm.elements;\n"
            + "log(oElements.length);\n"
            + "</script>\n"
            + "<input type='text' name='foo'/>\n"
            + "<script>\n"
            + "log(oElements.length);\n"
            + "log(document.myForm.elements.length);\n"
            + "log(oElements == document.myForm.elements);\n"
            + "</script>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§foo.html")
    public void getFormFromFormsById() throws Exception {
        final String html =
            "<html>\n"
            + "<head></head>\n"
            + "<body onload=\"alert(document.forms['myForm'].action)\">\n"
            + "<form id='myForm' action='foo.html'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§",
            IE = "")
    public void action() throws Exception {
        final String html =
            "<html>\n"
            + "<head></head>\n"
            + "<body onload=\"alert(document.forms['myForm'].action)\">\n"
            + "<form id='myForm'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void actionEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head></head>\n"
            + "<body onload=\"alert(document.forms['myForm'].action)\">\n"
            + "<form id='myForm' action=''>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void actionBlank() throws Exception {
        final String html =
            "<html>\n"
            + "<head></head>\n"
            + "<body onload=\"alert(document.forms['myForm'].action)\">\n"
            + "<form id='myForm' action='  '>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text")
    public void getFieldNamedLikeForm() throws Exception {
        final String html =
            "<html>\n"
            + "<head></head>\n"
            + "<body onload='alert(document.login.login.type)'>\n"
            + "<form name='login' action='foo.html'>\n"
            + "<input name='login' type='text'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that document.myForm.submit returns a field name submit or the submit function
     * depending on the sort of the field named submit.
     * @throws Exception if the test fails
     */
    @Test
    public void fieldNamedSubmit() throws Exception {
        fieldNamedSubmit("<input type='text' name='submit'>\n", "INPUT");
        fieldNamedSubmit("<input type='password' name='submit'>\n", "INPUT");
        fieldNamedSubmit("<input type='submit' name='submit'>\n", "INPUT");
        fieldNamedSubmit("<input type='radio' name='submit'>\n", "INPUT");
        fieldNamedSubmit("<input type='checkbox' name='submit'>\n", "INPUT");
        fieldNamedSubmit("<input type='button' name='submit'>\n", "INPUT");
        fieldNamedSubmit("<button type='submit' name='submit'>\n", "BUTTON");
        fieldNamedSubmit("<textarea name='submit'></textarea>\n", "TEXTAREA");
        fieldNamedSubmit("<select name='submit'></select>\n", "SELECT");
        fieldNamedSubmit("<input type='image' name='submit'>\n", "function");
        fieldNamedSubmit("<input type='IMAGE' name='submit'>\n", "function");
    }

    /**
     * @param htmlSnippet the HTML to embed in the test
     * @param expected the expected alert
     * @throws Exception if the test fails
     */
    private void fieldNamedSubmit(final String htmlSnippet, final String expected) throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (document.login.submit.tagName)\n"
            + "    log(document.login.submit.tagName);\n"
            + "  else"
            + "    log('function');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='login' action='foo.html'>\n"
            + htmlSnippet
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        setExpectedAlerts(expected);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "2"})
    public void fieldFoundWithID() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(IRForm.IRText.value);\n"
            + "  log(IRForm.myField.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form name='IRForm' action='#'>\n"
            + "    <input type='text' id='IRText' value='before'/>\n"
            + "    <input type='image' name='myField' src='foo.gif'/>\n"
            + "    <input type='image' id='myField' src='foo.gif'/>\n"
            + "    <input type='text' name='myField'/>\n"
            + "    <input type='text' id='myField'/>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"INPUT", "idImg1", "img2", "true"})
    public void nonFieldChildFound() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var oForm = document.testForm;\n"
            + "  log(oForm.img.tagName);\n"
            + "  log(oForm.img1.id);\n"
            + "  log(oForm.img2.id);\n"
            + "  log(oForm.testSpan == undefined);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form name='testForm' action='foo'>\n"
            + "    <input type='text' id='img' value='before'/>\n"
            + "    <img name='img' id='idImg' src='foo.png'/>\n"
            + "    <img name='img1' id='idImg1' src='foo.png'/>\n"
            + "    <img id='img2' src='foo.png'/>\n"
            + "    <span id='testSpan'>foo</span>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLImageElement]", "[object HTMLImageElement]"})
    public void findImageWhenNotDirectChild() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.fmLogin.myImgName);\n"
            + "  log(document.fmLogin.myImgId);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='fmLogin' action='doLogin' method='POST'>\n"
            + "    <div>\n"
            + "      <img name='myImgName' src='somewhere'>\n"
            + "      <img id='myImgId' src='somewhere'>\n"
            + "    <div/>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * This test shows a problem in current implementation of host object with visible constructors
     * used to retrieve JS object associated to a particular DOM node. The general problem needs
     * later.
     * @throws Exception if the test fails
     */
    @Test
    public void formIsNotAConstructor() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "var Form = {};\n"
            + "function test() {\n"
            + "  document.getElementById('formId');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form id='formId' name='formName'>\n"
            + "    <input type='text' name='field1' value='barney'>\n"
            + "    <input type='submit'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that the form from the right page is returned after browsing.
     * Regression test for
     * http://sourceforge.net/p/htmlunit/bugs/417/
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"page 1: formPage1", "page 2: formPage2"})
    public void formAccessAfterBrowsing() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  window.name = 'page 1: ' + document.forms[0].name;\n"
            + "  document.location = 'page2.html';\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form name='formPage1' action='foo'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.name);\n"
            + "  log('page 2: ' + document.forms[0].name);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form name='formPage2' action='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function handler() {}", "null", "null"})
    public void onsubmitNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler() {}\n"
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    form.onsubmit = handler;\n"
            + "    log(String(form.onsubmit).replace(/\\n/g, ''));\n"
            + "    form.onsubmit = null;\n"
            + "    log(form.onsubmit);\n"
            + "    try {\n"
            + "      form.onsubmit = undefined;\n"
            + "      log(form.onsubmit);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <form id='myForm'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"page1.html", "page2.html", "page1.html", "page1.html"},
            IE = {"page1.html", "page1.html", "page1.html", "page1.html"})
    @NotYetImplemented(IE)
    public void changeFormActionAfterSubmit() throws Exception {
        final String[] expectedFiles = getExpectedAlerts();
        setExpectedAlerts();

        changeFormActionAfterSubmit("input type='button' value='Test'", expectedFiles[0]);
        changeFormActionAfterSubmit("input type='submit' value='Test'", expectedFiles[1]);
        changeFormActionAfterSubmit("input type='text' value='Test'", expectedFiles[2]);
        changeFormActionAfterSubmit("div", expectedFiles[3]);
    }

    private void changeFormActionAfterSubmit(final String clickable, final String expectedFile) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function submitForm() {\n"
            + "      document.myForm.submit();\n"
            + "      document.myForm.action = 'page2.html';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form action='page1.html' name='myForm'>\n"
            + "    <" + clickable + " id='x' onclick='submitForm();'>foo\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.id("x")).click();
        assertEquals(URL_FIRST + expectedFile, driver.getCurrentUrl().replaceAll("\\?", ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "radio")
    public void item() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        log(document.forms['myForm'].item('myRadio').type);\n"
            + "      } catch(e) { log('exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form action='page1.html' name='myForm'>\n"
            + "    <input type='radio' name='myRadio'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "2")
    public void item_many() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        log(document.forms['myForm'].item('myRadio').length);\n"
            + "      } catch(e) { log('exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form action='page1.html' name='myForm'>\n"
            + "    <input type='radio' name='myRadio'>\n"
            + "    <input type='radio' name='myRadio'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "radio2")
    public void item_many_subindex() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        log(document.forms['myForm'].item('myRadio', 1).id);\n"
            + "      } catch(e) { log('exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form action='page1.html' name='myForm'>\n"
            + "    <input type='radio' name='myRadio' id='radio1'>\n"
            + "    <input type='radio' name='myRadio' id='radio2'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "radio2")
    public void item_integer() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        log(document.forms['myForm'].item(1).id);\n"
            + "      } catch(e) { log('exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form action='page1.html' name='myForm'>\n"
            + "    <input type='radio' name='myRadio' id='radio1'>\n"
            + "    <input type='radio' name='myRadio' id='radio2'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Call to form.submit() should capture the request to be done but the request itself should
     * be first done after the script execution... and only if it is still valid.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"page4.html?f1=v1&f2=v2", "page4.html?f1=v1&f2=v2", "page3.html?f1=v1", "page3.html?f1=v1"},
            IE = {"page3.html?f1=v1", "page3.html?f1=v1", "page3.html?f1=v1", "page3.html?f1=v1"})
    @NotYetImplemented(IE)
    public void changesAfterCallToSubmit() throws Exception {
        final String[] expectedUrlSuffixes = getExpectedAlerts();
        setExpectedAlerts();

        changesAfterCallToSubmit("inputSubmitReturnTrue", expectedUrlSuffixes[0]);
        changesAfterCallToSubmit("inputSubmitVoid", expectedUrlSuffixes[1]);

        changesAfterCallToSubmit("inputSubmitReturnFalse", expectedUrlSuffixes[2]);
        changesAfterCallToSubmit("link", expectedUrlSuffixes[3]);
    }

    private void changesAfterCallToSubmit(final String id, final String expectedUrlSuffix) throws Exception {
        final String html = "<html><head><script>\n"
            + "function submitForm() {\n"
            + "  var f = document.forms[0];\n"
            + "  f.action = 'page3.html';\n"
            + "\n"
            + "  var h = document.createElement('input');\n"
            + "  h.name = 'f1';\n"
            + "  h.value = 'v1';\n"
            + "  f.appendChild(h);\n"
            + "\n"
            + "  f.submit();\n"
            + "\n"
            + "  f.action = 'page4.html';\n"
            + "  var h = document.createElement('input');\n"
            + "  h.name = 'f2';\n"
            + "  h.value = 'v2';\n"
            + "  f.appendChild(h);\n"
            + "  return false;\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<form action='page1.html' name='myForm'>\n"
            + "  <input type='submit' id='inputSubmitReturnTrue' value='With on click on the button, return true' "
            + "onclick='submitForm(); return true'>\n"
            + "  <input type='submit' id='inputSubmitReturnFalse' value='With on click on the button, return false' "
            + "onclick='submitForm(); return false'>\n"
            + "  <input type='submit' id='inputSubmitVoid' value='With on click on the button, no return' "
            + "onclick='submitForm();'>\n"
            + "  <a id='link' href='#'  onclick='return submitForm()'><input type='submit' "
            + "value='With on click on the link'></a>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver wd = loadPageWithAlerts2(html);
        wd.findElement(By.id(id)).click();
        assertEquals(URL_FIRST + expectedUrlSuffix, wd.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            CHROME = "3",
            EDGE = "3",
            IE = "3")
    // sometimes real chrome returns 2
    public void submit_twice() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var f = document.forms[0];\n"
            + "    f.submit();\n"
            + "    f.submit();\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form action='page1.html' name='myForm'>\n"
            + "    <input name='myField' value='some value'>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void targetChangedAfterSubmitCall() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var f = document.forms[0];\n"
            + "  f.submit();\n"
            + "  f.target = 'foo2';\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<form action='page1.html' name='myForm' target='foo1'>\n"
            + "  <input name='myField' value='some value'>\n"
            + "</form>\n"
            + "<div id='clickMe' onclick='test()'>click me</div></body></html>";

        getMockWebConnection().setDefaultResponse("<html><head>"
                + "<script>document.title = 'Name: ' + window.name</script></head></html>");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();

        try {
            driver.switchTo().window("foo2");
            fail("Window foo2 found");
        }
        catch (final NoSuchWindowException e) {
            // ok
        }
        driver.switchTo().window("foo1");
        assertTitle(driver, "Name: foo1");
    }

    /**
     * Verify Content-Type header sent with form submission.
     * @throws Exception if the test fails
     */
    @Test
    public void enctypeGet() throws Exception {
        // for GET, no Content-Type header should be sent
        enctypeTest(false, null, "get", null);
        enctypeTest(false, "", "get", null);
        enctypeTest(false, "application/x-www-form-urlencoded", "get", null);
        enctypeTest(false, "multipart/form-data", "get", null);
        enctypeTest(false, MimeType.TEXT_PLAIN, "get", null);

        enctypeTest(true, null, "get", null);
        enctypeTest(true, "", "get", null);
        enctypeTest(true, "application/x-www-form-urlencoded", "get", null);
        enctypeTest(true, "multipart/form-data", "get", null);
        enctypeTest(true, MimeType.TEXT_PLAIN, "get", null);
    }

    /**
     * Verify Content-Type header sent with form submission.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/x-www-form-urlencoded",
                "application/x-www-form-urlencoded",
                "application/x-www-form-urlencoded",
                "multipart/form-data",
                "text/plain"})
    public void enctypePost() throws Exception {
        enctypeTest(false, null, "post", getExpectedAlerts()[0]);
        enctypeTest(false, "", "post", getExpectedAlerts()[1]);
        enctypeTest(false, "application/x-www-form-urlencoded", "post", getExpectedAlerts()[2]);
        enctypeTest(false, "multipart/form-data", "post", getExpectedAlerts()[3]);
        enctypeTest(false, MimeType.TEXT_PLAIN, "post", getExpectedAlerts()[4]);

        enctypeTest(true, null, "post", getExpectedAlerts()[0]);
        enctypeTest(true, "", "post", getExpectedAlerts()[1]);
        enctypeTest(true, "application/x-www-form-urlencoded", "post", getExpectedAlerts()[2]);
        enctypeTest(true, "multipart/form-data", "post", getExpectedAlerts()[3]);
        enctypeTest(true, MimeType.TEXT_PLAIN, "post", getExpectedAlerts()[4]);
    }

    /**
     * Regression test for bug
     * <a href="http://sf.net/suppor/tracker.php?aid=2860721">2860721</a>: incorrect enctype form attribute
     * should be ignored.
     * @throws Exception if the test fails
     */
    @Test
    public void enctype_incorrect() throws Exception {
        enctypeTest(false, MimeType.TEXT_HTML, "post", "application/x-www-form-urlencoded");
        enctypeTest(false, MimeType.TEXT_HTML, "get", null);

        enctypeTest(true, MimeType.TEXT_HTML, "post", "application/x-www-form-urlencoded");
        enctypeTest(true, MimeType.TEXT_HTML, "get", null);

        enctypeTest(false, MimeType.TEXT_XML, "post", "application/x-www-form-urlencoded");
        enctypeTest(false, MimeType.TEXT_XML, "get", null);

        enctypeTest(true, MimeType.TEXT_XML, "post", "application/x-www-form-urlencoded");
        enctypeTest(true, MimeType.TEXT_XML, "get", null);

        enctypeTest(false, MimeType.APPLICATION_JSON, "post", "application/x-www-form-urlencoded");
        enctypeTest(false, MimeType.APPLICATION_JSON, "get", null);

        enctypeTest(true, MimeType.APPLICATION_JSON, "post", "application/x-www-form-urlencoded");
        enctypeTest(true, MimeType.APPLICATION_JSON, "get", null);
    }

    private void enctypeTest(final boolean html5, final String enctype,
                    final String method, final String expectedCntType) throws Exception {
        String html = "";
        if (html5) {
            html += "<!DOCTYPE html>\n";
        }
        html += "<html><head><script>\n"
            + "function test() {\n"
            + "  var f = document.forms[0];\n"
            + "  f.submit();\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form action='foo.html' ";
        if (enctype != null) {
            html += "enctype='" + enctype + "' ";
        }
        html += "method='" + method + "'>\n"
            + "  <input name='myField' value='some value'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");
        final WebDriver driver = loadPage2(html);
        assertTitle(driver, "Response");
        String headerValue = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);
        if (headerValue != null && headerValue.startsWith(FormEncodingType.MULTIPART.getName())) {
            // Can't test equality for multipart/form-data as it will have the form:
            // multipart/form-data; boundary=---------------------------42937861433140731107235900
            headerValue = StringUtils.substringBefore(headerValue, ";");
            assertEquals(expectedCntType, headerValue);
        }
        else {
            assertEquals(expectedCntType, headerValue);
        }
    }

    /**
     * Verify the default value of enctype for a newly created form element.
     * A similar test is used by jQuery-1.9.1 in its "feature support" detection.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void enctype_defaultValue_html5() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.createElement('form').enctype);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception in case of error
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void enctype_defaultValue() throws Exception {
        final String html = "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.createElement('form').enctype);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Ensure that Multipart form text fields are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    public void submitMultipartTextFieldWithRightEncoding() throws Exception {
        final String html = "<html><body onload='document.forms[0].submit()'>\n"
            + "<form action='foo.html' enctype='multipart/form-data' method='post'>\n"
            + "  <input name='myField' value='éèêäöü'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        final String body = getMockWebConnection().getLastWebRequest().getRequestBody();
        final String expected = "Content-Disposition: form-data; name=\"myField\"\r\n"
            + "\r\n"
            + "éèêäöü";

        assertTrue("Body: " + body, body.contains(expected));
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    public void submitUrlEncodedEmptyForm() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='application/x-www-form-urlencoded' method='post'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        assertEquals(0, getMockWebConnection().getLastWebRequest()
                .getRequestParameters().size());
        assertNull(getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/x-www-form-urlencoded", "myField=abcDEF"})
    public void submitUrlEncodedAsciiText() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='application/x-www-form-urlencoded' method='post'>\n"
                + "    <input name='myField' value='abcDEF'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest()
                .getRequestParameters().get(0).toString());
        assertNull(getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/x-www-form-urlencoded", "my\tFie ld = a b\tc \t"})
    public void submitUrlEncodedSpecialChars() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='application/x-www-form-urlencoded' method='post'>\n"
                + "    <input name='my\tFie ld ' value=' a b\tc \t'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest()
                .getRequestParameters().get(0).toString());
        assertNull(getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/x-www-form-urlencoded", "myField=éèêäöü"})
    public void submitUrlEncodedUnicode() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='application/x-www-form-urlencoded' method='post'>\n"
                + "    <input name='myField' value='éèêäöü'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest()
                .getRequestParameters().get(0).toString());
        assertNull(getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/x-www-form-urlencoded", "myField=HtmlUnit \u043B\u0189"})
    public void submitUrlEncodedUnicodeUTF8() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='application/x-www-form-urlencoded' method='post'>\n"
                + "    <input name='myField' value='HtmlUnit \u043B\u0189'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest()
                .getRequestParameters().get(0).toString());
        assertNull(getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/x-www-form-urlencoded", "myField=HtmlUnit \u043B\u0189"})
    public void submitUrlEncodedUnicodeUTF16() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='application/x-www-form-urlencoded' method='post'>\n"
                + "    <input name='myField' value='HtmlUnit \u043B\u0189'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html, URL_FIRST, "text/html;charset=UTF-16", UTF_16, null);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest()
                .getRequestParameters().get(0).toString());
        assertNull(getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/x-www-form-urlencoded", "myFile=htmlunit-test", ".txt"})
    public void submitUrlEncodedFile() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <form action='foo.html' enctype='application/x-www-form-urlencoded' method='post'>\n"
                + "    <input type='file' id='f' name='myFile'>\n"
                + "    <input id='clickMe' type='submit' value='Click Me'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");

        final WebDriver driver = loadPage2(html);

        final File tmpFile = File.createTempFile("htmlunit-test", ".txt");
        try {
            String path = tmpFile.getAbsolutePath();
            if (driver instanceof InternetExplorerDriver || driver instanceof ChromeDriver) {
                path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
            }
            driver.findElement(By.id("f")).sendKeys(path);
            driver.findElement(By.id("clickMe")).click();
        }
        finally {
            assertTrue(tmpFile.delete());
        }

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        final String body = getMockWebConnection().getLastWebRequest()
                .getRequestParameters().get(0).toString();
        assertTrue(body, body.startsWith(getExpectedAlerts()[1]));
        assertTrue(body, body.endsWith(getExpectedAlerts()[2]));
        assertNull(getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain")
    public void submitPlainTextEmptyForm() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='text/plain' method='post'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        final String body = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertNull(body);
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain", "myField=abcDEF\r\n"})
    public void submitPlainTextAsciiText() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='text/plain' method='post'>\n"
                + "    <input name='myField' value='abcDEF'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        final String body = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertEquals(getExpectedAlerts()[1], body);
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain", "my\tField = abcDEF \t\r\n"})
    public void submitPlainTextSpecialChars() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='text/plain' method='post'>\n"
                + "    <input name='my\tField ' value=' ab\rcD\nEF \t'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        final String body = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertEquals(getExpectedAlerts()[1], body);
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain", "myField=éèêäöü\r\n"})
    public void submitPlainTextUnicode() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='text/plain' method='post'>\n"
                + "    <input name='myField' value='éèêäöü'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        final String body = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertEquals(getExpectedAlerts()[1], body);
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain", "myField=HtmlUnit \u00D0\u00BB\u00C6\u0089\r\n"})
    public void submitPlainTextUnicodeUTF8() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='text/plain' method='post'>\n"
                + "    <input name='myField' value='HtmlUnit \u043B\u0189'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        final String body = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertEquals(getExpectedAlerts()[1], body);
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain", "myField=HtmlUnit \u00D0\u00BB\u00C6\u0089\r\n"})
    public void submitPlainTextUnicodeUTF16() throws Exception {
        final String html = "<html>\n"
                + "<body onload='document.forms[0].submit()'>\n"
                + "  <form action='foo.html' enctype='text/plain' method='post'>\n"
                + "    <input name='myField' value='HtmlUnit \u043B\u0189'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html, URL_FIRST, "text/html;charset=UTF-16", UTF_16, null);
        Thread.sleep(100);

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        final String body = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertEquals(getExpectedAlerts()[1], body);
    }

    /**
     * Ensure that text/plain form parameters are correctly encoded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain", "myFile=htmlunit-test", ".txt\r\n"})
    public void submitPlainTextFile() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <form action='foo.html' enctype='text/plain' method='post'>\n"
                + "    <input type='file' id='f' name='myFile'>\n"
                + "    <input id='clickMe' type='submit' value='Click Me'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse("");

        final WebDriver driver = loadPage2(html);

        final File tmpFile = File.createTempFile("htmlunit-test", ".txt");
        try {
            String path = tmpFile.getAbsolutePath();
            if (driver instanceof InternetExplorerDriver || driver instanceof ChromeDriver) {
                path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
            }
            driver.findElement(By.id("f")).sendKeys(path);
            driver.findElement(By.id("clickMe")).click();
        }
        finally {
            assertTrue(tmpFile.delete());
        }

        final String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        assertEquals(getExpectedAlerts()[0], headerContentType);

        final String body = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertTrue(body, body.startsWith(getExpectedAlerts()[1]));
        assertTrue(body, body.endsWith(getExpectedAlerts()[2]));
    }

    /**
     * Failed as of HtmlUnit-2.7-SNAPSHOT 01.12.2009 as the '#' from the
     * link together with the fact that submission occurs to the same url
     * let HtmlUnit think that it as just navigation to an anchor.
     * @throws Exception if the test fails
     */
    @Test
    public void submitToSameUrlFromLinkOnclick_post() throws Exception {
        submitToSameUrlFromLinkOnclick("post");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submitToSameUrlFromLinkOnclick_get() throws Exception {
        submitToSameUrlFromLinkOnclick("get");
    }

    private void submitToSameUrlFromLinkOnclick(final String method) throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='" + method + "'>\n"
            + "<input name='foo'>\n"
            + "<a href='#' onclick='document.forms[0].submit()' id='clickMe'>submit it</a>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        driver.findElement(By.id("clickMe")).click(); // a second time to be sure to have same resulting Url

        assertEquals(3, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "foo4?foo=", "script4.js"},
            IE = {"", "foo0?foo=", "foo4?foo=", "script4.js"})
    @NotYetImplemented
    public void submitTriggersRequestNotParsed() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var f = document.forms[0];\n"
            + "  for (var i = 0; i < 5; i++) {\n"
            + "    f.action = 'foo' + i;\n"
            + "    f.submit();\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input name='foo'>\n"
            + "  </form>\n"
            + "</body></html>";

        final MockWebConnection connection = getMockWebConnection();
        for (int i = 0; i < 5; i++) {
            final String htmlX = "<html><head>\n"
                + "<title>Page " + i + "</title>\n"
                + "<script src='script" + i + ".js'></script>\n"
                + "<script>alert('page" + i + "');</script>\n"
                + "</head></html>";
            connection.setResponse(new URL(URL_FIRST, "foo" + i), htmlX);
            connection.setResponse(new URL(URL_FIRST, "script" + i + ".js"), "", MimeType.APPLICATION_JAVASCRIPT);
        }
        final String[] expectedRequests = getExpectedAlerts();

        setExpectedAlerts("page4");
        final WebDriver driver = loadPageWithAlerts2(html); // forces to wait, what is needed for FFdriver

        // NB: comparing the sequence order here is not 100% safe with a real browser
        assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(URL_FIRST));

        assertTitle(driver, "Page 4");
    }

    /**
     * When the name of a form field changes... it is still reachable through the original name!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLInputElement]", "undefined",
                        "[object HTMLInputElement]", "[object HTMLInputElement]",
                        "[object HTMLInputElement]", "[object HTMLInputElement]", "[object HTMLInputElement]"},
            IE = {"[object HTMLInputElement]", "undefined",
                        "undefined", "[object HTMLInputElement]",
                        "undefined", "undefined", "[object HTMLInputElement]"})
    public void accessByNameAfterNameChange() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function go() {\n"
            + "  var field = document.simple_form.originalName;\n"
            + "  log(document.simple_form.originalName);\n"
            + "  log(document.simple_form.newName);\n"

            + "  field.name = 'newName';\n"
            + "  log(document.simple_form.originalName);\n"
            + "  log(document.simple_form.newName);\n"

            + "  field.name = 'brandNewName';\n"
            + "  log(document.simple_form.originalName);\n"
            + "  log(document.simple_form.newName);\n"
            + "  log(document.simple_form.brandNewName);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='go()'>\n"
            + "<form name='simple_form'>\n"
            + "  <input name='originalName'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for Bug #1083: lost children should be accessible per name from HTMLFormElement.elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLInputElement]", "[object HTMLInputElement]"})
    public void lostChildrenFromElements() throws Exception {
        final String html
            = "<html><body>\n"
            + "<div><form name='form1' >\n"
            + "</div>\n"
            + "<input name='b'/>\n"
            + "</form><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.form1['b']);\n"
            + "  log(document.form1.elements['b']);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function")
    public void onchangeHandler() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var form = document.getElementsByTagName('form')[0];\n"
            + "  log(typeof form.onchange);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form onchange='cat=true'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "in listener",
            FF = {"in listener", "page2 loaded"},
            FF78 = {"in listener", "page2 loaded"})
    public void dispatchEventSubmitTriggersHandlers() throws Exception {
        // use an iframe to capture alerts among 2 pages
        final String container = "<html><body><iframe src='page1'></iframe></body></html>\n";
        final String page1 = "<html><body>\n"
            + "<form action='page2' id='theForm'><span id='foo'/></form>\n"
            + "<script>\n"
            + "function listener(e) {\n"
            + "  alert('in listener');\n"
            + "}\n"
            + "try {\n"
            + "  document.forms[0].addEventListener('submit', listener, true);\n"
            + "  var e = document.createEvent('HTMLEvents');\n"
            + "  e.initEvent('submit', true, false);\n"
            + "  document.getElementById('theForm').dispatchEvent(e);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";

        final String page2 = "<html><body><script>alert('page2 loaded');</script></body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "page1"), page1);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "page2"), page2);
        loadPageWithAlerts2(container);
    }

    /**
     * Verifies that the event object is correctly made available.
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/425/
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"srcElement null: false", "srcElement==form: true",
                                                        "target null: false", "target==form: true"})
    public void onSubmitEvent() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(_event) {\n"
            + "  var oEvent = _event ? _event : window.event;\n"
            + "  log('srcElement null: ' + (oEvent.srcElement == null));\n"
            + "  log('srcElement==form: ' + (oEvent.srcElement == document.forms[0]));\n"
            + "  log('target null: ' + (oEvent.target == null));\n"
            + "  log('target==form: ' + (oEvent.target == document.forms[0]));\n"

            + "  if (_event.preventDefault) { _event.preventDefault(); }\n"
            + "  return false;\n"
            + "}\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form name='formPage1' action='about:blank' onsubmit='return test(event);'>\n"
            + "<input type='submit' id='theButton'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("theButton")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * This test is used to check that when a form having a target is submitted
     * and if the target is an iframe and the iframe has an onload event, then
     * the onload event is called.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"prepare frame", "submit form", "submitted ok"})
    public void submitWithTargetOnIFrameAndOnload_script() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' name='form1' method='get' action='" + URL_SECOND + "'>\n"
            + "  <input type='button' name='button1' />\n"
            + "</form>\n"
            + "<script>\n"
            + "  // Prepare the iframe for the target\n"
            + "  alert('prepare frame');\n"
            + "  var div = document.createElement('div');\n"
            + "  div.style.display = 'none';\n"
            + "  div.innerHTML = \"<iframe name='frame' id='frame'></iframe>\";\n"
            + "  document.body.appendChild(div);\n"
            + "  // Get the form and set the target\n"
            + "  var form = document.getElementById('form1');\n"
            + "  form.target = 'frame';\n"
            + "  // Finally submit the form with a delay to make sure that the onload of the iframe\n"
            + "  // is called for the submit and not for the page creation\n"
            + "  var t = setTimeout(function() {\n"
            + "    clearTimeout(t);\n"
            + "    var iframe = document.getElementById('frame');\n"
            + "    iframe.onload = function() {\n"
            + "      alert('submitted ' + iframe.contentWindow.document.body.getAttribute('id'));\n"
            + "    };\n"
            + "    alert('submit form');\n"
            + "    form.submit();\n"
            + "  }, 1000);\n"
            + "</script></body></html>";
        final String html2
            = "<?xml version='1.0'?>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'><body id='ok'><span id='result'>OK</span></body></html>";
        getMockWebConnection().setDefaultResponse(html2);

        loadPageWithAlerts2(html, URL_FIRST, 5000);
    }

    /**
     * This test is used to check that when a form having a target is submitted
     * and if the target is an iframe and the iframe has an onload event, then
     * the onload event is called.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"submit form", "listener: submitted ok"})
    public void submitWithTargetOnIFrameAndOnload_bubbling() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' name='form1' method='get' action='" + URL_SECOND + "' target='frame'>\n"
            + "  <input type='button' name='button1' />\n"
            + "</form>\n"
            + "<div style='display:none;'><iframe name='frame' id='frame'></iframe></div>\n"
            + "<script>\n"
            + "  // Get the form and set the target\n"
            + "  var form = document.getElementById('form1');\n"
            + "  var iframe = document.getElementById('frame');\n"
            + "  // Finally submit the form with a delay to make sure that the onload of the iframe\n"
            + "  // is called for the submit and not for the page creation\n"
            + "  var t = setTimeout(function() {\n"
            + "    clearTimeout(t);\n"
            + "    iframe.addEventListener('load', function() {\n"
            + "      alert('listener: submitted ' + iframe.contentWindow.document.body.getAttribute('id'));\n"
            + "    }, true);\n"
            + "    alert('submit form');\n"
            + "    form.submit();\n"
            + "  }, 1000);\n"
            + "</script>\n"
            + "</body></html>";
        final String html2
            = "<?xml version='1.0'?>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'><body id='ok'><span id='result'>OK</span></body></html>";
        getMockWebConnection().setDefaultResponse(html2);

        loadPageWithAlerts2(html, URL_FIRST, 5000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Response", "param1=value1"},
            IE = "requestSubmit() not available")
    public void requestSubmit() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>first</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var myForm = document.getElementById('form1');\n"
            + "  if (myForm.requestSubmit) {\n"
            + "    myForm.requestSubmit();\n"
            + "    return;\n"
            + "  }\n"
            + "  alert('requestSubmit() not available');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onload='doTest()'>\n"
            + "  <form id='form1' name='form1' method='get' action='" + URL_SECOND + "' encoding='text/plain'>\n"
            + "    <input name='param1' type='hidden' value='value1'>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        if (getExpectedAlerts().length == 1) {
            verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[0]});
            return;
        }

        assertTitle(driver, getExpectedAlerts()[0]);
        final String params = getMockWebConnection().getLastWebRequest().getUrl().getQuery();
        assertEquals(getExpectedAlerts()[1], params);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Response", "param1=value1"},
            IE = "requestSubmit() not available")
    public void requestSubmitWithSubmit() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>first</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var myForm = document.getElementById('form1');\n"
            + "  if (myForm.requestSubmit) {\n"
            + "    var sub = document.getElementById('submit1');\n"
            + "    myForm.requestSubmit(sub);\n"
            + "    return;\n"
            + "  }\n"
            + "  alert('requestSubmit() not available');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onload='doTest()'>\n"
            + "  <form id='form1' name='form1' method='get' action='" + URL_SECOND + "' encoding='text/plain'>\n"
            + "    <input name='param1' type='hidden' value='value1'>\n"
            + "    <input id='submit1' type='submit' />\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        if (getExpectedAlerts().length == 1) {
            verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[0]});
            return;
        }

        assertTitle(driver, getExpectedAlerts()[0]);
        final String params = getMockWebConnection().getLastWebRequest().getUrl().getQuery();
        assertEquals(getExpectedAlerts()[1], params);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Response", "param1=value1&submit1="},
            IE = "requestSubmit() not available")
    public void requestSubmitWithButton() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>first</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var myForm = document.getElementById('form1');\n"
            + "  if (myForm.requestSubmit) {\n"
            + "    var sub = document.getElementById('submit1');\n"
            + "    try {\n"
            + "      myForm.requestSubmit(sub);\n"
            + "    } catch (e) { alert('requestSubmit failed' + e); }\n"
            + "    return;\n"
            + "  }\n"
            + "  alert('requestSubmit() not available');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onload='doTest()'>\n"
            + "  <form id='form1' name='form1' method='get' action='" + URL_SECOND + "' encoding='text/plain'>\n"
            + "    <input name='param1' type='hidden' value='value1'>\n"
            + "    <button type='submit' id='submit1' name='submit1'>submit1</button>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        if (getExpectedAlerts().length == 1) {
            verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[0]});
            return;
        }

        assertTitle(driver, getExpectedAlerts()[0]);
        final String params = getMockWebConnection().getLastWebRequest().getUrl().getQuery();
        assertEquals(getExpectedAlerts()[1], params);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"first", "requestSubmit failed"},
            IE = "requestSubmit() not available")
    public void requestSubmitNotMember() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>first</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var myForm = document.getElementById('form1');\n"
            + "  if (myForm.requestSubmit) {\n"
            + "    var sub = document.getElementById('submit2');\n"
            + "    try {\n"
            + "      myForm.requestSubmit(sub);\n"
            + "    } catch (e) { alert('requestSubmit failed'); }\n"
            + "    return;\n"
            + "  }\n"
            + "  alert('requestSubmit() not available');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onload='doTest()'>\n"
            + "  <form id='form1' name='form1' method='get' action='" + URL_SECOND + "' encoding='text/plain'>\n"
            + "    <input name='param1' type='hidden' value='value1'>\n"
            + "  </form>\n"

            + "  <form id='form2' name='form2' method='get' action='" + URL_SECOND + "' encoding='text/plain'>\n"
            + "    <input type='submit' id='submit2' />\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        if (getExpectedAlerts().length == 1) {
            verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[0]});
            return;
        }

        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[1]});
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"first", "requestSubmit failed"},
            IE = "requestSubmit() not available")
    public void requestSubmitNotSubmit() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>first</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var myForm = document.getElementById('form1');\n"
            + "  if (myForm.requestSubmit) {\n"
            + "    var sub = document.getElementById('param1');\n"
            + "    try {\n"
            + "      myForm.requestSubmit(sub);\n"
            + "    } catch (e) { alert('requestSubmit failed'); }\n"
            + "    return;\n"
            + "  }\n"
            + "  alert('requestSubmit() not available');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onload='doTest()'>\n"
            + "  <form id='form1' name='form1' method='get' action='" + URL_SECOND + "' encoding='text/plain'>\n"
            + "    <input id='param1' name='param1' type='hidden' value='value1'>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        if (getExpectedAlerts().length == 1) {
            verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[0]});
            return;
        }

        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[1]});
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "false", "true", "false", "false"})
    public void in() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var f = document.testForm;\n"
            + "  log(f.length);\n"
            + "  log(-1 in f);\n"
            + "  log(0 in f);\n"
            + "  log(1 in f);\n"
            + "  log(42 in f);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm' action='about:blank'>\n"
            + "    <input type='submit' id='theButton'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second")
    public void notRequired() throws Exception {
        required("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first")
    public void required() throws Exception {
        required("required");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first")
    public void requiredEmpty() throws Exception {
        required("required=''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first")
    public void requiredBlank() throws Exception {
        required("required=' '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first")
    public void requiredTrue() throws Exception {
        required("required=true");
        required("required='true'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first")
    public void requiredFalse() throws Exception {
        required("required=false");
        required("required='false'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first")
    public void requiredArbitrary() throws Exception {
        required("required='Arbitrary'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first")
    public void requiredRequired() throws Exception {
        required("required='required'");
    }

    /**
     * @throws Exception if the test fails
     */
    private void required(final String req) throws Exception {
        final String html = "<html>\n"
            + "<head><title>first</title></head>\n"
            + "<body>\n"
            + "  <form name='testForm' action='\" + URL_SECOND + \"'>\n"
            + "    <input type='submit' id='submit'>\n"
            + "    <input name='test' value='' " + req + " >"
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
    @Alerts({"second", "second"})
    public void notRequiredFileInput() throws Exception {
        requiredFileInput("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "second"})
    public void requiredFileInput() throws Exception {
        requiredFileInput("required");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "second"})
    public void requiredFileInputEmpty() throws Exception {
        requiredFileInput("required=''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "second"})
    public void requiredFileInputBlank() throws Exception {
        requiredFileInput("required=' '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "second"})
    public void requiredFileInputTrue() throws Exception {
        requiredFileInput("required=true");
        requiredFileInput("required='true'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "second"})
    public void requiredFileInputFalse() throws Exception {
        requiredFileInput("required=false");
        requiredFileInput("required='false'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "second"})
    public void requiredFileInputArbitrary() throws Exception {
        requiredFileInput("required='Arbitrary'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "second"})
    public void requiredFileInputRequired() throws Exception {
        requiredFileInput("required='required'");
    }

    /**
     * @throws Exception if the test fails
     */
    private void requiredFileInput(final String req) throws Exception {
        final String html = "<html>\n"
            + "<head><title>first</title></head>\n"
            + "<body>\n"
            + "  <form name='testForm' action='\" + URL_SECOND + \"'>\n"
            + "    <input type='submit' id='submit'>\n"
            + "    <input type='file' name='test' value='' " + req + " >"
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

        loadPage2(html);
        final WebElement e = driver.findElement(By.name("test"));
        final String absolutePath = new File("pom.xml").getAbsolutePath();
        e.sendKeys(absolutePath);
        driver.findElement(By.id("submit")).click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());
    }
}
