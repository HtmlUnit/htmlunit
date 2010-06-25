/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import static org.junit.Assert.assertSame;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * Tests for {@link HTMLFormElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
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
    public void testElementsAccessor() throws Exception {
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
    public void testElementsAccessorOutOfBound() throws Exception {
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
    public void testRadioButtonArray() throws Exception {
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
    public void testRadioButton_OnlyOne() throws Exception {
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
    public void testActionProperty() throws Exception {
        doTestProperty("action", "action", "http://foo.com/", "mailto:me@bar.com");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testNameProperty() throws Exception {
        doTestProperty("name", "name", "myForm", "testForm");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEncodingProperty() throws Exception {
        doTestProperty("encoding", "enctype", "myEncoding", "newEncoding");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMethodProperty() throws Exception {
        doTestProperty("method", "method", "get", "post");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testTargetProperty() throws Exception {
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
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form " + htmlProperty + "='" + oldValue + "'>\n"
            + "    <input type='button' name='button1' />\n"
            + "</form>\n"
            + "</body></html>";

        setExpectedAlerts(oldValue, newValue);
        final WebDriver wd = loadPageWithAlerts2(html);

        final WebElement form = wd.findElement(By.xpath("//form"));
        assertEquals(newValue, form.getAttribute(htmlProperty));
    }

    /**
     * Tests form reset and input default values while emulating IE.
     * @throws Exception if the test fails
     */
    @Test
    public void testFormReset() throws Exception {
        // As tested with IE 6.0 on Win2k; note that refreshing the page will get you different results;
        // you need to open a new browser instance each time you test this.
        final String[] expectedIE = {
            "before setting default values",               /* Before setting default values. */
            "text: initial1 initial1 false false",
            "file:   false false",
            "image: initial3 initial3 false false",
            "radio: initial4 initial4 true true",
            "reset: initial5 initial5 false false",
            "hidden: initial6 initial6 false false",
            "button: initial7 initial7 false false",
            "submit: initial8 initial8 false false",
            "password: initial9 initial9 false false",
            "checkbox: initial10 initial10 true true",
            "textarea: initial11 initial11 undefined undefined",
            "after setting default values",                /* After setting default values. */
            "text: initial1 default1 false false",
            "file:  default2 false false",
            "image: default3 default3 false false",
            "radio: default4 default4 true false",
            "reset: initial5 default5 false false",
            "hidden: initial6 default6 false false",
            "button: initial7 default7 false false",
            "submit: initial8 default8 false false",
            "password: initial9 default9 false false",
            "checkbox: default10 default10 true false",
            "textarea: initial11 default11 undefined undefined",
            "after resetting the form",                    /* After resetting the form. */
            "text: default1 default1 false false",
            "file:  default2 false false",
            "image: default3 default3 false false",
            "radio: default4 default4 false false",
            "reset: initial5 default5 false false",
            "hidden: default6 default6 false false",
            "button: initial7 default7 false false",
            "submit: initial8 default8 false false",
            "password: default9 default9 false false",
            "checkbox: default10 default10 false false",
            "textarea: default11 default11 undefined undefined" };

        // As tested with Firefox 2.0.20 and 3.0.13 on Linux.
        final String[] expectedFF = {
            "before setting default values",               /* Before setting default values. */
            "text: initial1 initial1 false false",
            "file:  initial2 false false",                 // THIS LINE DIFFERS FROM IE; see HtmlFileInput constructor.
            "image: initial3 initial3 false false",
            "radio: initial4 initial4 true true",
            "reset: initial5 initial5 false false",
            "hidden: initial6 initial6 false false",
            "button: initial7 initial7 false false",
            "submit: initial8 initial8 false false",
            "password: initial9 initial9 false false",
            "checkbox: initial10 initial10 true true",
            "textarea: initial11 initial11 undefined undefined",
            "after setting default values",                /* After setting default values. */
            "text: default1 default1 false false",       // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "file:  default2 false false",
            "image: default3 default3 false false",
            "radio: default4 default4 false false",    // DIFFERS FROM IE; see HtmlRadioButtonInput.setDefaultChecked()
            "reset: default5 default5 false false",      // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "hidden: default6 default6 false false",     // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "button: default7 default7 false false",     // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "submit: default8 default8 false false",     // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "password: default9 default9 false false",   // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "checkbox: default10 default10 false false", // DIFFERS FROM IE; see HtmlCheckBoxInput.setDefaultChecked()
            "textarea: default11 default11 undefined undefined",
            "after resetting the form",                    /* After resetting the form. */
            "text: default1 default1 false false",
            "file:  default2 false false",
            "image: default3 default3 false false",
            "radio: default4 default4 false false",
            "reset: default5 default5 false false",        // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "hidden: default6 default6 false false",
            "button: default7 default7 false false",       // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "submit: default8 default8 false false",       // DIFFERS FROM IE; see HtmlInput.setDefaultValue()
            "password: default9 default9 false false",
            "checkbox: default10 default10 false false",
            "textarea: default11 default11 undefined undefined" };

        final String[] expectedAlerts = getBrowserVersion().isFirefox() ? expectedFF : expectedIE;
        setExpectedAlerts(expectedAlerts);

        final String html = "<html>\n"
            + "  <head>\n"
            + "    <title>Reset Test</title>\n"
            + "    <script>\n"
            + "      var form1;\n"
            + "      var text1;\n"
            + "      var file1;\n"
            + "      var image1;\n"
            + "      var radio1;\n"
            + "      var reset1;\n"
            + "      var hidden1;\n"
            + "      var button1;\n"
            + "      var submit1;\n"
            + "      var password1;\n"
            + "      var checkbox1;\n"
            + "      var textarea1;\n"
            + "      function test() {\n"
            + "        // --- initialize local variables, verify the initial default values --- //\n"
            + "        form1 = document.getElementById('form1');\n"
            + "        text1 = document.getElementById('text1');\n"
            + "        file1 = document.getElementById('file1');\n"
            + "        image1 = document.getElementById('image1');\n"
            + "        radio1 = document.getElementById('radio1');\n"
            + "        reset1 = document.getElementById('reset1');\n"
            + "        hidden1 = document.getElementById('hidden1');\n"
            + "        button1 = document.getElementById('button1');\n"
            + "        submit1 = document.getElementById('submit1');\n"
            + "        password1 = document.getElementById('password1');\n"
            + "        checkbox1 = document.getElementById('checkbox1');\n"
            + "        textarea1 = document.getElementById('textarea1');\n"
            + "        alerts('before setting default values');\n"
            + "        // --- change default values around, verify the new default values --- //\n"
            + "        text1.defaultValue = 'default1';\n"
            + "        file1.defaultValue = 'default2';\n"
            + "        image1.defaultValue = 'default3';\n"
            + "        radio1.defaultValue = 'default4';\n"
            + "        radio1.defaultChecked = false;\n"
            + "        reset1.defaultValue = 'default5';\n"
            + "        hidden1.defaultValue = 'default6';\n"
            + "        button1.defaultValue = 'default7';\n"
            + "        submit1.defaultValue = 'default8';\n"
            + "        password1.defaultValue = 'default9';\n"
            + "        checkbox1.defaultValue = 'default10';\n"
            + "        checkbox1.defaultChecked = false;\n"
            + "        textarea1.defaultValue = 'default11';\n"
            + "        alerts('after setting default values');\n"
            + "        // --- reset the form, verify the input values were reset as appropriate --- //\n"
            + "        form1.reset();\n"
            + "        alerts('after resetting the form');\n"
            + "      }\n"
            + "      function alerts(caption) {\n"
            + "        alert(caption);\n"
            + "        alertOne('text', text1);\n"
            + "        alertOne('file', file1);\n"
            + "        alertOne('image', image1);\n"
            + "        alertOne('radio', radio1);\n"
            + "        alertOne('reset', reset1);\n"
            + "        alertOne('hidden', hidden1);\n"
            + "        alertOne('button', button1);\n"
            + "        alertOne('submit', submit1);\n"
            + "        alertOne('password', password1);\n"
            + "        alertOne('checkbox', checkbox1);\n"
            + "        alertOne('textarea', textarea1);\n"
            + "      }\n"
            + "      function alertOne(text, field) {\n"
            + "        alert(text + ': ' + field.value + ' ' + field.defaultValue "
            + "          + ' ' + field.checked + ' ' + field.defaultChecked);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <form id='form1' name='form1'>\n"
            + "      <input type='text' id='text1' name='text1' value='initial1' />\n"
            + "      <input type='file' id='file1' name='file1' value='initial2' />\n"
            + "      <input type='image' id='image1' name='image1' value='initial3' />\n"
            + "      <input type='radio' id='radio1' name='radio1' value='initial4' checked='checked' />\n"
            + "      <input type='reset' id='reset1' name='reset1' value='initial5' />\n"
            + "      <input type='hidden' id='hidden1' name='hidden1' value='initial6' />\n"
            + "      <input type='button' id='button1' name='button1' value='initial7' />\n"
            + "      <input type='submit' id='submit1' name='submit1' value='initial8' />\n"
            + "      <input type='password' id='password1' name='password1' value='initial9' />\n"
            + "      <input type='checkbox' id='checkbox1' name='checkbox1' value='initial10' checked='checked' />\n"
            + "      <textarea id='textarea1' name='textarea1'>initial11</textarea>\n"
            + "    </form>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFormSubmit() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <input type='button' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);
        final HtmlPage page = loadPageWithAlerts(html);

        final HtmlPage secondPage =
            (HtmlPage) page.executeJavaScript("document.form1.submit()").getNewPage();
        assertEquals("second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOnSubmitChangesAction() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form name='form1' action='" + URL_SECOND + "' onsubmit='this.action=\"" + URL_THIRD + "\"' "
            + "method='post'>\n"
            + "    <input type='submit' id='button1' />\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html></html>");

        final HtmlPage page = loadPageWithAlerts(html);
        final Page page2 = page.<HtmlElement>getHtmlElementById("button1").click();

        assertEquals(URL_THIRD.toExternalForm(), page2.getWebResponse().getWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFormSubmit_target() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "' target='MyNewWindow'>\n"
            + "    <input type='button' name='button1' />\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final HtmlPage page = loadPageWithAlerts(html);

        final HtmlPage secondPage
            = (HtmlPage) page.executeJavaScript("document.form1.submit()").getNewPage();
        assertEquals("second", secondPage.getTitleText());
        assertEquals("MyNewWindow", secondPage.getEnclosingWindow().getName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFormSubmitDoesntCallOnSubmit() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "' onsubmit=\"this.action = 'foo.html'\">\n"
            + "    <input type='submit' />\n"
            + "</form>\n"
            + "<a href='javascript:document.form1.submit()' id='link1'>Click me</a>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlAnchor link = page.getHtmlElementById("link1");
        final HtmlPage page2 = link.click();
        assertEquals("second", page2.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "id2", "foo" })
    public void testInputNamedId() throws Exception {
        doTestInputWithName("id");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "action2", "foo" })
    public void testInputNamedAction() throws Exception {
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
    public void testAccessingRadioButtonArrayByName_Regression() throws Exception {
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
    public void testFindInputWithoutTypeDefined() throws Exception {
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
     * @throws Exception if the test fails
     */
    @Test
    public void testFormSubmit_MultipleButtons() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "'>\n"
            + "    <button type='submit' name='button1' id='button1'/>\n"
            + "    <button type='submit' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("first", page.getTitleText());

        final HtmlButton button = page.getHtmlElementById("button1");
        final HtmlPage secondPage = button.click();
        assertEquals("second", secondPage.getTitleText());
        assertEquals(URL_SECOND + "?button1=", secondPage.getWebResponse().getWebRequest().getUrl());
    }

    /**
     * Test form.length - This method does not count the type=image
     * input tags.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void testLength() throws Exception {
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
    public void testGet() throws Exception {
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
    * @throws Exception if the test fails
    */
    @Test
    public void testLostFunction() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + " function onSubmit() { alert('hi!'); return false; }\n"
            + "</script></head><body>\n"
            + "<form onsubmit='return onSubmit();'>\n"
            + " <input type='submit' id='clickMe' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSubmitInput button = page.getHtmlElementById("clickMe");
        button.click();
        final String[] expectedAlerts = {"hi!"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAssignedOnsubmit() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + " function onSubmit() { alert('hi!'); return false; }\n"
            + " function init() { document.myForm.onsubmit = onSubmit; }\n"
            + " window.onload = init;\n"
            + "</script></head><body>\n"
            + "<form name='myForm'>\n"
            + " <input type='submit' id='clickMe' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSubmitInput button = page.getHtmlElementById("clickMe");
        button.click();
        final String[] expectedAlerts = {"hi!"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that the elements collection is live.
    * @throws Exception if the test fails
    */
    @Test
    @Alerts({ "0", "1", "1", "true" })
    public void testElementsLive() throws Exception {
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
    public void testGetFormFromFormsById() throws Exception {
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
    public void testGetFieldNamedLikeForm() throws Exception {
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
    public void testFieldNamedSubmit() throws Exception {
        testFieldNamedSubmit("<input type='text' name='submit'>\n", "INPUT");
        testFieldNamedSubmit("<input type='password' name='submit'>\n", "INPUT");
        testFieldNamedSubmit("<input type='submit' name='submit'>\n", "INPUT");
        testFieldNamedSubmit("<input type='radio' name='submit'>\n", "INPUT");
        testFieldNamedSubmit("<input type='checkbox' name='submit'>\n", "INPUT");
        testFieldNamedSubmit("<input type='button' name='submit'>\n", "INPUT");
        testFieldNamedSubmit("<button type='submit' name='submit'>\n", "BUTTON");
        testFieldNamedSubmit("<textarea name='submit'></textarea>\n", "TEXTAREA");
        testFieldNamedSubmit("<select name='submit'></select>\n", "SELECT");
        testFieldNamedSubmit("<input type='image' name='submit'>\n", "function");
        testFieldNamedSubmit("<input type='IMAGE' name='submit'>\n", "function");
    }

    /**
     * @param htmlSnippet the HTML to embed in the test
     * @param expected the expected alert
     * @throws Exception if the test fails
     */
    private void testFieldNamedSubmit(final String htmlSnippet, final String expected) throws Exception {
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
    public void testFieldFoundWithID() throws Exception {
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
    public void testNonFieldChildFound() throws Exception {
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
    public void testFormIsNotAConstructor() throws Exception {
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
    public void testFormAccessAfterBrowsing() throws Exception {
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
     * Verifies that the event object is correctly made available.
     * Regression test for https://sf.net/tracker/index.php?func=detail&aid=1648014&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "srcElement null: true", "srcElement==form: false", "target null: false", "target==form: true" },
        IE = { "srcElement null: false", "srcElement==form: true", "target null: true", "target==form: false" })
    public void testOnSubmitEvent() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = getMockWebConnection();

        final String html = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function test(_event) {\n"
            + "  var oEvent = _event ? _event : window.event;\n"
            + "  alert('srcElement null: ' + (oEvent.srcElement == null));\n"
            + "  alert('srcElement==form: ' + (oEvent.srcElement == document.forms[0]));\n"
            + "  alert('target null: ' + (oEvent.target == null));\n"
            + "  alert('target==form: ' + (oEvent.target == document.forms[0]));\n"
            + "}\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form name='formPage1' action='about:blank' onsubmit='test(event)'>\n"
            + "<input type='submit' id='theButton'>\n"
            + "</form>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        page.<HtmlElement>getHtmlElementById("theButton").click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * In action "this" should be the window and not the form.
     * @throws Exception if the test fails
     */
    @Test
    public void testThisInJavascriptAction() throws Exception {
        final String content
            = "<html>\n"
            + "<body>\n"
            + "<form action='javascript:alert(this == window)'>\n"
            + "<input type='submit' id='theButton'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"true"};
        final HtmlPage page1 = loadPage(getBrowserVersion(), content, collectedAlerts);
        final Page page2 = page1.<HtmlElement>getHtmlElementById("theButton").click();

        assertEquals(expectedAlerts, collectedAlerts);
        assertSame(page1, page2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "function handler() {}", "null" })
    public void testOnsubmitNull() throws Exception {
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
    public void fileInput_fireOnChange() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<form>\n"
            + "  <input type='file' name='myFile' id='myFile' onchange='alert(this.value)'/>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        final String[] expectedAlerts = {"dummy.txt"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlFileInput fileInput = page.getHtmlElementById("myFile");
        fileInput.focus();
        fileInput.setAttribute("value", "dummy.txt");
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        // remove focus to trigger onchange
        page.setFocusedElement(null);
        assertEquals(expectedAlerts, collectedAlerts);
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
    @Alerts(FF = "exception", IE = "radio")
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
    @Alerts(FF = "exception", IE = "2")
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
    @Alerts(FF = "exception", IE = "radio2")
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
    @Alerts(FF = "exception", IE = "radio2")
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
        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.id("clickMe")).click();

        try {
            driver.switchTo().window("foo2");
            Assert.fail("Window foo2 found");
        }
        catch (final NoSuchWindowException e) {
            // ok
        }
        driver.switchTo().window("foo1");
        setExpectedAlerts("foo1");
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
     * Calling form.submit() immediately triggers a request but only the
     * last response for a page is parsed.
     * @throws Exception if the test fails
     */
    @Test
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
                + "</head></html>";
            connection.setResponse(new URL(getDefaultUrl(), "foo" + i), htmlX);
            connection.setResponse(new URL(getDefaultUrl(), "script" + i + ".js"), "", JAVASCRIPT_MIME_TYPE);
        }
        final WebDriver driver = loadPage2(html);

        // NB: comparing the sequence order here is not 100% safe with a real browser
        final String[] expectedRequests = {"", "foo0", "foo1", "foo2", "foo3", "foo4", "script4.js"};
        assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(getDefaultUrl()));

        assertEquals("Page 4", driver.getTitle());
    }

    /**
     * When the name of a form field changes... it is still reachable through the original name!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object HTMLInputElement]", "undefined", "[object HTMLInputElement]", "[object HTMLInputElement]" },
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
     * This test is used to check that when a form having a target is submitted
     * and if the target is an iframe and the iframe has an onload event, then
     * the onload event is called.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "prepare frame", "submit form", "submitted ok" })
    public void testSubmitWithTargetOnIFrameAndOnload_script() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
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
        loadPageWithAlerts(html, getDefaultUrl(), 5000);
    }

    /**
     * This test is used to check that when a form having a target is submitted
     * and if the target is an iframe and the iframe has an onload event, then
     * the onload event is called. This is a Firefox-specific test.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "submit form", "submitted ok" })
    public void testSubmitWithTargetOnIFrameAndOnload_bubbling_FF() throws Exception {
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
            + "      alert('submitted ' + iframe.contentWindow.document.body.getAttribute('id'));\n"
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
        loadPageWithAlerts(html, getDefaultUrl(), 5000);
    }

    /**
     * This test is used to check that when a form having a target is submitted
     * and if the target is an iframe and the iframe has an onload event, then
     * the onload event is called. This is an IE-specific test.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "submit form", "submitted ok" })
    public void testSubmitWithTargetOnIFrameAndOnload_attached_IE() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' name='form1' method='get' action='" + URL_SECOND + "' target='frame'>\n"
            + "    <input type='button' name='button1' />\n"
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
            + "    iframe.attachEvent('onload', function() {\n"
            + "      alert('submitted ' + iframe.contentWindow.document.body.getAttribute('id'));\n"
            + "    });\n"
            + "    alert('submit form');\n"
            + "    form.submit();\n"
            + "  }, 1000);\n"
            + "</script>\n"
            + "</body></html>";
        final String html2
            = "<?xml version='1.0'?>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'><body id='ok'><span id='result'>OK</span></html>";
        getMockWebConnection().setDefaultResponse(html2);
        loadPageWithAlerts(html, getDefaultUrl(), 5000);
    }

}
