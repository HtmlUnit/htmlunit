/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLFormElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLFormElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "16", "button1", "button2", "checkbox1", "fileupload1", "hidden1",
            "radio1", "radio1",
            "select1", "select2", "password1", "reset1",
            "reset2", "submit1", "submit2", "textInput1", "textarea1" })
    public void elementsAccessor() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1.length)\n"
            + "    for (var i=0; i< document.form1.length; i++) {\n"
            + "        var element = document.form1.elements[i];\n"
            + "        if (element.type != 'radio' && element != document.form1[element.name]) {\n"
            + "            alert('name index not working for '+element.name);\n"
            + "        }\n"
            + "        alert(element.name)\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <button type='button' name='button2'>button2</button>\n"
            + "    <input type='checkbox' name='checkbox1' />\n"
            + "    <input type='file' name='fileupload1' />\n"
            + "    <input type='hidden' name='hidden1' />\n"
            + "    <input type='radio' name='radio1' value='1' />\n"
            + "    <input type='radio' name='radio1' value='2' />\n"
            + "    <select name='select1'>\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <select multiple='multiple' name='select2'>\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <input type='password' name='password1' />\n"
            + "    <input type='reset' name='reset1' />\n"
            + "    <button type='reset' name='reset2'>reset2</button>\n"
            + "    <input type='submit' name='submit1' />\n"
            + "    <button type='submit' name='submit2'>submit2</button>\n"
            + "    <input type='text' name='textInput1' />\n"
            + "    <textarea name='textarea1'>foo</textarea>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "undefined", "undefined" })
    public void elementsAccessorOutOfBound() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1[-1]);\n"
            + "    alert(document.form1[2]);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <input type='button' name='button1'/>\n"
            + "    <input type='submit' name='submit1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "1", "2", "3" })
    public void radioButtonArray() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var radioArray = document.form1['radio1'];\n"
            + "    alert(radioArray.length)\n"
            + "    for (var i=0; i< radioArray.length; i++) {\n"
            + "        var element = radioArray[i];\n"
            + "        alert(element.value)\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='radio' name='radio1' value='1'/>\n"
            + "    <input type='radio' name='radio1' value='2'/>\n"
            + "    <input type='radio' name='radio1' value='3'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1['radio1'].value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='radio' name='radio1' value='1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "http://foo.com/", "mailto:me@bar.com", "mailto:me@bar.com" })
    public void actionProperty() throws Exception {
        doTestProperty("action", "action", "http://foo.com/", "mailto:me@bar.com");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "myForm", "testForm", "testForm" })
    public void nameProperty() throws Exception {
        doTestProperty("name", "name", "myForm", "testForm");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "multipart/form-data", "application/x-www-form-urlencoded", "application/x-www-form-urlencoded" })
    public void encodingProperty() throws Exception {
        doTestProperty("encoding", "enctype", "multipart/form-data", "application/x-www-form-urlencoded");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myEncoding", "newEncoding", "newEncoding" },
            FF17 = { "application/x-www-form-urlencoded", "application/x-www-form-urlencoded", "newEncoding" })
    public void encodingProperty_dummyValues() throws Exception {
        doTestProperty("encoding", "enctype", "myEncoding", "newEncoding");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "get", "post", "post" })
    public void methodProperty() throws Exception {
        doTestProperty("method", "method", "get", "post");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "_top", "_parent", "_parent" })
    public void targetProperty() throws Exception {
        doTestProperty("target", "target", "_top", "_parent");
    }

    private void doTestProperty(final String jsProperty, final String htmlProperty,
            final String oldValue, final String newValue) throws Exception {

        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.forms[0]." + jsProperty + ");\n"
            + "    document.forms[0]." + jsProperty + "='" + newValue + "';\n"
            + "    alert(document.forms[0]." + jsProperty + ");\n"
            + "    alert(document.forms[0].getAttribute('" + htmlProperty + "'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form " + htmlProperty + "='" + oldValue + "'>\n"
            + "    <input type='button' name='button1' />\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver wd = loadPageWithAlerts2(html);

        final WebElement form = wd.findElement(By.xpath("//form"));
        if (wd instanceof HtmlUnitDriver) {
            // form.getAttribute("enctype") returns form.getAttribute("encoding") with the FF driver. Bug or feature?
            assertEquals(getExpectedAlerts()[2], form.getAttribute(htmlProperty));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "id2", "foo" })
    public void inputNamedId() throws Exception {
        doTestInputWithName("id");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "action2", "foo" })
    public void inputNamedAction() throws Exception {
        doTestInputWithName("action");
    }

    private void doTestInputWithName(final String name) throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function go() {\n"
            + "   alert(document.simple_form." + name + ".value);\n"
            + "   document.simple_form." + name + ".value='foo';\n"
            + "   alert(document.simple_form." + name + ".value);\n"
            + "}</script></head>\n"
            + "<body onload='go()'>\n"
            + "<p>hello world</p>\n"
            + "<form action='login.jsp' name='simple_form'>\n"
            + "    <input name='" + name + "' type='hidden' value='" + name + "2'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test that used to blow up on page load.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("value = 2")
    public void accessingRadioButtonArrayByName_Regression() throws Exception {
        final String html
            = "<html><head><title>Button Test</title></head><body><form name='whatsnew'>\n"
            + "<input type='radio' name='second' value='1'>\n"
            + "<input type='radio' name='second' value='2' checked>\n"
            + "</form><script>clickAction();\n"
            + "function clickAction(){\n"
            + "    var value = -1;\n"
            + "    radios = document.forms['whatsnew'].elements['second'];\n"
            + "    for (var i=0; i < radios.length; i++){\n"
            + "        if (radios[i].checked == true) {\n"
            + "            value = radios[i].value;\n"
            + "            break;\n"
            + "        }\n"
            + "    }\n"
            + "    alert('value = ' + value);\n"
            + "}\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
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
            = "<html><head><title>foo</title></head>\n"
            + "<body onload='alert(document.simple_form.login.value);'>\n"
            + "<p>hello world</p><table><tr><td>\n"
            + "<form action='login.jsp' name='simple_form'>\n"
            + "    <input name='msg' type='hidden' value='0'>\n"
            + "    <script>document.simple_form.msg.value=1</script>\n"
            + "    <input name='login' size='17' value='foo'>\n"
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
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1.length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <input type='radio' name='radio1' value='1'/>\n"
            + "    <input type='image' src='foo' value='1'/>\n"
            + "    <input type='submit' name='submit1' value='1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("button1")
    public void get() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1[0].name)\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='button' name='button1' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the elements collection is live.
    * @throws Exception if the test fails
    */
    @Test
    @Alerts({ "0", "1", "1", "true" })
    public void elementsLive() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<form name='myForm'>\n"
            + "<script>\n"
            + "var oElements = document.myForm.elements;\n"
            + "alert(oElements.length);\n"
            + "</script>\n"
            + "<input type='text' name='foo'/>\n"
            + "<script>\n"
            + "alert(oElements.length);\n"
            + "alert(document.myForm.elements.length);\n"
            + "alert(oElements == document.myForm.elements);\n"
            + "</script>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getFormFromFormsById() throws Exception {
        final String html =
            "<html>\n"
            + "<head></head>\n"
            + "<body onload=\"alert(document.forms['myForm'].action)\">\n"
            + "<form id='myForm' action='foo.html'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        if (getBrowserVersion().isFirefox()) {
            setExpectedAlerts(URL_FIRST + "foo.html");
        }
        else {
            setExpectedAlerts("foo.html");
        }

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
            + "function test() {\n"
            + "  if (document.login.submit.tagName)\n"
            + "    alert(document.login.submit.tagName);\n"
            + "  else"
            + "    alert('function');\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "before", "2" })
    public void fieldFoundWithID() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "    alert(IRForm.IRText.value);\n"
            + "    alert(IRForm.myField.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + " <form name='IRForm' action='#'>\n"
            + " <input type='text' id='IRText' value='before'/>\n"
            + " <input type='image' name='myField' src='foo.gif'/>\n"
            + " <input type='image' id='myField' src='foo.gif'/>\n"
            + " <input type='text' name='myField'/>\n"
            + " <input type='text' id='myField'/>\n"
            + " </form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "INPUT", "idImg1", "img2", "true" })
    public void nonFieldChildFound() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "    var oForm = document.testForm;\n"
            + "    alert(oForm.img.tagName);\n"
            + "    alert(oForm.img1.id);\n"
            + "    alert(oForm.img2.id);\n"
            + "    alert(oForm.testSpan == undefined);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + " <form name='testForm' action='foo'>\n"
            + " <input type='text' id='img' value='before'/>\n"
            + " <img name='img' id='idImg' src='foo.png'/>\n"
            + " <img name='img1' id='idImg1' src='foo.png'/>\n"
            + " <img id='img2' src='foo.png'/>\n"
            + " <span id='testSpan'>foo</span>\n"
            + " </form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
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
            + "    document.getElementById('formId');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "   <form id='formId' name='formName'>\n"
            + "     <input type='text' name='field1' value='barney'>\n"
            + "     <input type='submit'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that the form from the right page is returned after browsing.
     * Regression test for
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1627983&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "page 1: formPage1", "page 2: formPage2" })
    public void formAccessAfterBrowsing() throws Exception {
        final String html = "<html><head><title>first</title>\n"
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
        final String secondContent = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(window.name);\n"
            + "  alert('page 2: ' + document.forms[0].name);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form name='formPage2' action='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "function handler() {}", "null" })
    public void onsubmitNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function handler() {}\n"
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    form.onsubmit = handler;\n"
            + "    alert(String(form.onsubmit).replace(/\\n/g, ''));\n"
            + "    form.onsubmit = null;\n"
            + "    alert(form.onsubmit);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <form id='myForm'></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void changeFormActionAfterSubmit() throws Exception {
        changeFormActionAfterSubmit("input type='button' value='Test'", "page1.html");
        changeFormActionAfterSubmit("input type='submit' value='Test'", "page2.html");
        changeFormActionAfterSubmit("input type='text' value='Test'", "page1.html");
        changeFormActionAfterSubmit("div", "page1.html");
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
        // caution: IE7 doesn't put a trailing "?"
        assertEquals(getDefaultUrl() + expectedFile, driver.getCurrentUrl().replaceAll("\\?", ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception", IE = "radio")
    public void item() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      try {\n"
            + "        alert(document.forms['myForm'].item('myRadio').type);\n"
            + "      } catch(e) { alert('exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form action='page1.html' name='myForm'>\n"
            + "    <input type='radio' name='myRadio'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception", IE = "2")
    public void item_many() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      try {\n"
            + "        alert(document.forms['myForm'].item('myRadio').length);\n"
            + "      } catch(e) { alert('exception') }\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception", IE = "radio2")
    public void item_many_subindex() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      try {\n"
            + "        alert(document.forms['myForm'].item('myRadio', 1).id);\n"
            + "      } catch(e) { alert('exception') }\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception", IE = "radio2")
    public void item_integer() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      try {\n"
            + "        alert(document.forms['myForm'].item(1).id);\n"
            + "      } catch(e) { alert('exception') }\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * Call to form.submit() should capture the request to be done but the request itself should
     * be first done after the script execution... and only if it is still valid.
     * @throws Exception if the test fails
     */
    @Test
    public void changes_after_call_to_submit() throws Exception {
        changes_after_call_to_submit("inputSubmitReturnTrue", "page4.html?f1=v1&f2=v2");
        changes_after_call_to_submit("inputSubmitVoid", "page4.html?f1=v1&f2=v2");

        changes_after_call_to_submit("inputSubmitReturnFalse", "page3.html?f1=v1");
        changes_after_call_to_submit("link", "page3.html?f1=v1");
    }

    private void changes_after_call_to_submit(final String id, final String expectedUrlSuffix) throws Exception {
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
    public void submit_twice() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var f = document.forms[0];\n"
            + "  f.submit();\n"
            + "  f.submit();\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form action='page1.html' name='myForm'>\n"
            + "  <input name='myField' value='some value'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPageWithAlerts2(html);
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo1")
    public void target_changed_after_submit_call() throws Exception {
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

        getMockWebConnection().setDefaultResponse("<html><head><script>alert(window.name)</script></head></html>");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();

        try {
            driver.switchTo().window("foo2");
            Assert.fail("Window foo2 found");
        }
        catch (final NoSuchWindowException e) {
            // ok
        }
        driver.switchTo().window("foo1");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Verify Content-Type header sent with form submission.
     * @throws Exception if the test fails
     */
    @Test
    public void enctype() throws Exception {
        enctypeTest("", "post", "application/x-www-form-urlencoded");
        enctypeTest("application/x-www-form-urlencoded", "post", "application/x-www-form-urlencoded");
        enctypeTest("multipart/form-data", "post", "multipart/form-data");

        // for GET, no Content-Type header should be sent
        enctypeTest("", "get", null);
        enctypeTest("application/x-www-form-urlencoded", "get", null);
        enctypeTest("multipart/form-data", "get", null);
    }

    /**
     * Regression test for bug
     * <a href="http://sf.net/suppor/tracker.php?aid=2860721">2860721</a>: incorrect enctype form attribute
     * should be ignored.
     * @throws Exception if the test fails
     */
    @Test
    public void enctype_incorrect() throws Exception {
        enctypeTest("text/html", "post", "application/x-www-form-urlencoded");
        enctypeTest("text/html", "get", null);
    }

    /**
     * Verify the default value of enctype for a newly created form element.
     * A similar test is used by jQuery-1.9.1 in its "feature support" detection.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/x-www-form-urlencoded")
    @NotYetImplemented
    public void enctype_defaultValue() throws Exception {
        final String html = "<html><body><script>\n"
            + "alert(document.createElement('form').enctype)\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    private void enctypeTest(final String enctype, final String method, final String expectedCntType) throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var f = document.forms[0];\n"
            + "  f.submit();\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form action='foo.html' enctype='" + enctype + "' method='" + method + "'>\n"
            + "  <input name='myField' value='some value'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPageWithAlerts2(html);
        String headerValue = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get("Content-Type");
        // Can't test equality for multipart/form-data as it will have the form:
        // multipart/form-data; boundary=---------------------------42937861433140731107235900
        headerValue = StringUtils.substringBefore(headerValue, ";");
        assertEquals(expectedCntType, headerValue);
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
     * For FF3.6 and IE8: calling form.submit() immediately triggers a request but only the
     * last response for a page is parsed.
     * For FF10+ and Chrome: only one request, the last one.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "foo4", "script4.js" },
            FF3_6 = { "", "foo0", "foo1", "foo2", "foo3", "foo4", "script4.js" },
            IE = { "", "foo0", "foo1", "foo2", "foo3", "foo4", "script4.js" })
    @NotYetImplemented(FF17)
    public void submitTriggersRequestNotParsed() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var f = document.forms[0];\n"
            + "  for (var i=0; i<5; ++i) {\n"
            + "    f.action = 'foo' + i;\n"
            + "    f.submit();\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form>\n"
            + "<input name='foo'>\n"
            + "</form></body></html>";

        final MockWebConnection connection = getMockWebConnection();
        for (int i = 0; i < 5; ++i) {
            final String htmlX = "<html><head>\n"
                + "<title>Page " + i + "</title>\n"
                + "<script src='script" + i + ".js'></script>\n"
                + "<script>alert('page" + i + "');</script>\n"
                + "</head></html>";
            connection.setResponse(new URL(getDefaultUrl(), "foo" + i), htmlX);
            connection.setResponse(new URL(getDefaultUrl(), "script" + i + ".js"), "", JAVASCRIPT_MIME_TYPE);
        }
        final String[] expectedRequests = getExpectedAlerts();

        setExpectedAlerts("page4");
        final WebDriver driver = loadPageWithAlerts2(html); // forces to wait, what is needed for FFdriver

        // NB: comparing the sequence order here is not 100% safe with a real browser
        assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(getDefaultUrl()));

        assertEquals("Page 4", driver.getTitle());
    }

    /**
     * When the name of a form field changes... it is still reachable through the original name!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLInputElement]", "undefined",
                        "[object HTMLInputElement]", "[object HTMLInputElement]" },
            IE = { "[object]", "undefined", "[object]", "undefined" })
    public void accessByNameAfterNameChange() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function go() {\n"
            + "   alert(document.simple_form.originalName);\n"
            + "   alert(document.simple_form.newName);\n"
            + "   document.simple_form.originalName.name = 'newName';\n"
            + "   alert(document.simple_form.originalName);\n"
            + "   alert(document.simple_form.newName);\n"
            + "}</script></head>\n"
            + "<body onload='go()'>\n"
            + "<form name='simple_form'>\n"
            + "   <input name='originalName'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2995968: lost children should be accessible per name from HTMLFormElement.elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLInputElement]", "[object HTMLInputElement]" },
        IE = { "[object]", "[object]" })
    public void lostChildrenFromElements() throws Exception {
        final String html
            = "<html><body>\n"
            + "<div><form name='form1' >\n"
            + "</div>\n"
            + "<input name='b'/>\n"
            + "</form><script>\n"
            + "  alert(document.form1['b']);\n"
            + "  alert(document.form1.elements['b']);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "function", IE = "string")
    public void onchangeHandler() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + " var form = document.getElementsByTagName('form')[0];\n"
            + " alert(typeof form.onchange);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form onchange='cat=true'></form>"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "in listener", "page2 loaded" }, IE = "exception")
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

        getMockWebConnection().setResponse(new URL(getDefaultUrl() + "page1"), page1);
        getMockWebConnection().setResponse(new URL(getDefaultUrl() + "page2"), page2);
        loadPageWithAlerts2(container);
    }
}
