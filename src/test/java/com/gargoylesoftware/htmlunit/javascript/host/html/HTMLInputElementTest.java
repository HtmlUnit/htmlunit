/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HTMLInputElement} and buttons.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLInputElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "text", "textfield1", "form1", "cat" })
    public void standardProperties_Text() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1.textfield1.value)\n"
            + "    alert(document.form1.textfield1.type)\n"
            + "    alert(document.form1.textfield1.name)\n"
            + "    alert(document.form1.textfield1.form.name)\n"
            + "    document.form1.textfield1.value='cat'\n"
            + "    alert(document.form1.textfield1.value)\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({
            "button", "button", "checkbox", "file", "hidden", "select-one",
            "select-multiple", "password", "reset", "reset", "submit",
            "submit", "text", "textarea" })
    public void textProperties() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1.button1.type)\n"
            + "    alert(document.form1.button2.type)\n"
            + "    alert(document.form1.checkbox1.type)\n"
            + "    alert(document.form1.fileupload1.type)\n"
            + "    alert(document.form1.hidden1.type)\n"
            + "    alert(document.form1.select1.type)\n"
            + "    alert(document.form1.select2.type)\n"
            + "    alert(document.form1.password1.type)\n"
            + "    alert(document.form1.reset1.type)\n"
            + "    alert(document.form1.reset2.type)\n"
            + "    alert(document.form1.submit1.type)\n"
            + "    alert(document.form1.submit2.type)\n"
            + "    alert(document.form1.textInput1.type)\n"
            + "    alert(document.form1.textarea1.type)\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='button' name='button1'></button>\n"
            + "    <button type='button' name='button2'></button>\n"
            + "    <input type='checkbox' name='checkbox1' />\n"
            + "    <input type='file' name='fileupload1' />\n"
            + "    <input type='hidden' name='hidden1' />\n"
            + "    <select name='select1'>\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <select multiple='multiple' name='select2'>\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <input type='password' name='password1' />\n"
            + "    <input type='radio' name='radio1' />\n"
            + "    <input type='reset' name='reset1' />\n"
            + "    <button type='reset' name='reset2'></button>\n"
            + "    <input type='submit' name='submit1' />\n"
            + "    <button type='submit' name='submit2'></button>\n"
            + "    <input type='text' name='textInput1' />\n"
            + "    <textarea name='textarea1'>foo</textarea>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true" })
    public void checkedAttribute_Checkbox() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.form1.checkbox1.checked)\n"
            + "    document.form1.checkbox1.checked=true\n"
            + "    alert(document.form1.checkbox1.checked)\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='cheCKbox' name='checkbox1' id='checkbox1' value='foo' />\n"
            + "</form>\n"
            + "<a href='javascript:test()' id='clickme'>click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlCheckBoxInput checkBox = page.getHtmlElementById("checkbox1");
        assertFalse(checkBox.isChecked());
        page.<HtmlAnchor>getHtmlElementById("clickme").click();
        assertTrue(checkBox.isChecked());

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void checkedAttribute_Radio() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.form1.radio1[0].checked)\n"
            + "    alert(document.form1.radio1[1].checked)\n"
            + "    alert(document.form1.radio1[2].checked)\n"
            + "    document.form1.radio1[1].checked=true\n"
            + "    alert(document.form1.radio1[0].checked)\n"
            + "    alert(document.form1.radio1[1].checked)\n"
            + "    alert(document.form1.radio1[2].checked)\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='radio' name='radio1' id='radioA' value='a' checked='checked'/>\n"
            + "    <input type='RADIO' name='radio1' id='radioB' value='b' />\n"
            + "    <input type='radio' name='radio1' id='radioC' value='c' />\n"
            + "</form>\n"
            + "<a href='javascript:test()' id='clickme'>click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlRadioButtonInput radioA = page.getHtmlElementById("radioA");
        final HtmlRadioButtonInput radioB = page.getHtmlElementById("radioB");
        final HtmlRadioButtonInput radioC = page.getHtmlElementById("radioC");
        assertTrue(radioA.isChecked());
        assertFalse(radioB.isChecked());
        assertFalse(radioC.isChecked());
        page.<HtmlAnchor>getHtmlElementById("clickme").click();
        assertFalse(radioA.isChecked());
        assertTrue(radioB.isChecked());
        assertFalse(radioC.isChecked());

        final String[] expectedAlerts = {"true", "false", "false", "false", "true", "false"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void disabledAttribute() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.form1.button1.disabled)\n"
            + "    alert(document.form1.button2.disabled)\n"
            + "    alert(document.form1.button3.disabled)\n"
            + "    document.form1.button1.disabled=true\n"
            + "    document.form1.button2.disabled=false\n"
            + "    document.form1.button3.disabled=true\n"
            + "    alert(document.form1.button1.disabled)\n"
            + "    alert(document.form1.button2.disabled)\n"
            + "    alert(document.form1.button3.disabled)\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='submit' name='button1' value='1'/>\n"
            + "    <input type='submit' name='button2' value='2' disabled/>\n"
            + "    <input type='submit' name='button3' value='3'/>\n"
            + "</form>\n"
            + "<a href='javascript:test()' id='clickme'>click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlForm form = page.getFormByName("form1");

        final HtmlSubmitInput button1 = form.getInputByName("button1");
        final HtmlSubmitInput button2 = form.getInputByName("button2");
        final HtmlSubmitInput button3 = form.getInputByName("button3");
        assertFalse(button1.isDisabled());
        assertTrue(button2.isDisabled());
        assertFalse(button3.isDisabled());
        page.<HtmlAnchor>getHtmlElementById("clickme").click();
        assertTrue(button1.isDisabled());
        assertFalse(button2.isDisabled());
        assertTrue(button3.isDisabled());

        final String[] expectedAlerts = {"false", "true", "false", "true", "false", "true"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputValue() throws Exception {
        final String html =
            "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + " document.form1.textfield1.value = 'blue';\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='post' onsubmit='doTest()'>\n"
            + " <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);

        final HtmlForm form = page.getFormByName("form1");
        form.submit(null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputSelect_NotDefinedAsPropertyAndFunction() throws Exception {
        final String html =
            "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + " document.form1.textfield1.select();\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='post' onsubmit='doTest()'>\n"
            + " <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);

        final HtmlForm form = page.getFormByName("form1");
        form.submit(null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void thisDotFormInOnClick() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>First</title></head>\n"
            + "<body>\n"
            + "<form name='form1'>\n"
            + "<input type='submit' name='button1' onClick=\"this.form.target='_blank'; return false;\">\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), htmlContent, null);
        assertEquals("First", page.getTitleText());

        assertEquals("", page.getFormByName("form1").getTargetAttribute());

        ((HtmlSubmitInput) page.getFormByName("form1").getInputByName("button1")).click();

        assertEquals("_blank", page.getFormByName("form1").getTargetAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true" })
    public void fieldDotForm() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title><script>\n"
            + "function test(){\n"
            + "  var f = document.form1;\n"
            + "  alert(f == f.mySubmit.form);\n"
            + "  alert(f == f.myText.form);\n"
            + "  alert(f == f.myPassword.form);\n"
            + "  alert(f == document.getElementById('myImage').form);\n"
            + "  alert(f == f.myButton.form);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "<input type='submit' name='mySubmit'>\n"
            + "<input type='text' name='myText'>\n"
            + "<input type='password' name='myPassword'>\n"
            + "<input type='button' name='myButton'>\n"
            + "<input type='image' src='foo' name='myImage' id='myImage'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputNameChange() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + " document.form1.textfield1.name = 'changed';\n"
            + " alert(document.form1.changed.name);\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='post' onsubmit='doTest()'>\n"
            + " <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + " <input type='submit' name='button1' value='pushme' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final MockWebConnection connection = (MockWebConnection) page.getWebClient().getWebConnection();

        final HtmlForm form = page.getFormByName("form1");
        form.<HtmlInput>getInputByName("button1").click();

        final String[] expectedAlerts = {"changed"};
        assertEquals(expectedAlerts, collectedAlerts);

        final List<NameValuePair> expectedParameters = Arrays.asList(new NameValuePair[] {
            new NameValuePair("changed", "foo"),
            new NameValuePair("button1", "pushme")
        });
        assertEquals(expectedParameters, connection.getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onChange() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + " <input type='text' name='text1' onchange='alert(this.value)'>\n"
            + "<input name='myButton' type='button' onclick='document.form1.text1.value=\"from button\"'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);

        final HtmlForm form = page.getFormByName("form1");
        final HtmlTextInput textinput = form.getInputByName("text1");
        textinput.setValueAttribute("foo");
        final HtmlButtonInput button = form.getInputByName("myButton");
        button.click();
        assertEquals("from button", textinput.getValueAttribute());

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onChangeSetByJavaScript() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + " <input type='text' name='text1' id='text1'>\n"
            + "<input name='myButton' type='button' onclick='document.form1.text1.value=\"from button\"'>\n"
            + "</form>\n"
            + "<script>\n"
            + "document.getElementById('text1').onchange= function(event) { alert(this.value) };\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);

        final HtmlForm form = page.getFormByName("form1");
        final HtmlTextInput textinput = form.getInputByName("text1");
        textinput.setValueAttribute("foo");
        final HtmlButtonInput button = form.getInputByName("myButton");
        button.click();
        assertEquals("from button", textinput.getValueAttribute());

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);
    }

    /**
     * Test the default value of a radio and checkbox buttons.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"on", "on" })
    public void defautValue() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.myForm.myRadio.value);\n"
            + "    alert(document.myForm.myCheckbox.value);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='radio' name='myRadio'/>\n"
            + "<input type='checkbox' name='myCheckbox'/>\n"
            + "</form></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test that changing type doesn't throw.
     * Test must be extended when setting type really does something.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"radio", "hidden", "image" })
    public void changeType() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.myForm.myRadio;\n"
            + "    alert(input.type);\n"
            + "    input.type = 'hidden';\n"
            + "    alert(input.type);\n"
            + "    input.setAttribute('type', 'image');\n"
            + "    alert(input.type);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='radio' name='myRadio'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);

        assertTrue(HtmlImageInput.class.isInstance(page.getFormByName("myForm").getInputByName("myRadio")));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"radio", "hidden", "image" })
    public void changeType2() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.myForm.myRadio;\n"
            + "    alert(input.type);\n"
            + "    input.type = 'hidden';\n"
            + "    alert(input.type);\n"
            + "    input.setAttribute('type', 'image');\n"
            + "    alert(input.type);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>"
            + "<input type='radio' name='myRadio'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);

        assertTrue(HtmlImageInput.class.isInstance(page.getFormByName("myForm").getInputByName("myRadio")));
    }

    /**
     * Inputs have properties not only from there own type.
     * Works with Mozilla, Firefox and IE... but not with HtmlUnit now.
     * @throws Exception if the test fails
     */
    @Test
    public void defaultValues() throws Exception {
        final String html
            = "<html><head></head><body>\n"
                + "<form name='myForm'>\n"
                + "<input type='button' name='myButton'/>\n"
                + "<input type='submit' name='mySubmit' value='submit it!'/>\n"
                + "<input type='file' name='myFile'/>\n"
                + "<input type='checkbox' name='myCheckbox' checked='true'/>\n"
                + "<input type='radio' name='myRadio' checked='true'/>\n"
                + "<input type='text' name='myText'/>\n"
                + "<input type='password' name='myPwd'/>\n"
                + "</form>\n"
                + "<script>\n"
                + "function details(_oInput) {\n"
                + "  alert(_oInput.type + ': '\n"
                + "  + _oInput.checked + ', ' \n"
                + "  + _oInput.defaultChecked + ', '\n"
                + "  + ((String(_oInput.click).indexOf('function') != -1) ? 'function' : 'unknown') + ', '\n"
                + "  + ((String(_oInput.select).indexOf('function') != -1) ? 'function' : 'unknown') + ', '\n"
                + "  + _oInput.defaultValue + ', '\n"
                + "  + _oInput.value\n"
                + " );\n"
                + "}\n"
                + "var oForm = document.myForm;\n"
                + "details(oForm.myButton);\n"
                + "details(oForm.mySubmit);\n"
                + "details(oForm.myFile);\n"
                + "details(oForm.myCheckbox);\n"
                + "details(oForm.myRadio);\n"
                + "details(oForm.myText);\n"
                + "details(oForm.myPwd);\n"
                + "</script>\n"
                + "</body></html>";
        final String[] expectedAlerts = {
            "button: false, false, function, function, , ",
            "submit: false, false, function, function, submit it!, submit it!",
            "file: false, false, function, function, , ",
            "checkbox: true, true, function, function, , on",
            "radio: true, true, function, function, , on",
            "text: false, false, function, function, , ",
            "password: false, false, function, function, , "
        };
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getBrowserVersion(), html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text", "hidden" })
    public void createInputAndChangeType() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.createElement('INPUT');\n"
            + "    alert(input.type);\n"
            + "    input.type = 'hidden';\n"
            + "    alert(input.type);\n"
            + "    myForm.appendChild(input);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "</form></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buttonOutsideForm() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<button id='clickMe' onclick='alert(123)'>click me</button>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        final Page page2 = page.<HtmlElement>getHtmlElementById("clickMe").click();

        assertSame(page, page2);

        final String[] expectedAlerts = {"123"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that field delegates submit to the containing form.
     * @throws Exception if the test fails
     */
    @Test
    public void onChangeCallsFormSubmit() throws Exception {
        final String content
            = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form name='test' action='foo'>\n"
            + "<input name='field1' onchange='submit()'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse("<html><title>page 2</title><body></body></html>");
        webConnection.setResponse(URL_FIRST, content);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlPage page2 = (HtmlPage)
            page.getFormByName("test").<HtmlInput>getInputByName("field1").setValueAttribute("bla");
        assertEquals("page 2", page2.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "30", "undefined", "30", "30", "30", "40", "50", "string", "number" })
    public void maxLength() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    alert(input.maxlength);\n"
            + "    alert(input.maxLength);\n"
            + "    alert(input.MaxLength);\n"
            + "    alert(input.getAttribute('maxlength'));\n"
            + "    alert(input.getAttribute('maxLength'));\n"
            + "    alert(input.getAttribute('MaxLength'));\n"
            + "    input.setAttribute('MaXlenGth', 40);\n"
            + "    alert(input.maxLength);\n"
            + "    input.maxLength = 50;\n"
            + "    alert(input.getAttribute('maxlength'));\n"
            + "    alert(typeof input.getAttribute('maxLength'));\n"
            + "    alert(typeof input.maxLength);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' maxlength='30'/>\n"
            + "</form></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts("hello")
    public void selectionRange() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    input.setSelectionRange(2, 7);\n"
            + "    alert('hello');"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<input id='myInput' value='some test'>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        assertEquals("me te", input.getSelectedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text text", "password password", "hidden hidden",
            "checkbox checkbox", "radio radio", "file file", "checkbox checkbox" })
    public void typeCase() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var t = document.getElementById('aText');\n"
            + "  alert(t.type + ' ' + t.getAttribute('type'));\n"
            + "  var p = document.getElementById('aPassword');\n"
            + "  alert(p.type + ' ' + p.getAttribute('type'));\n"
            + "  var h = document.getElementById('aHidden');\n"
            + "  alert(h.type + ' ' + h.getAttribute('type'));\n"
            + "  var cb = document.getElementById('aCb');\n"
            + "  alert(cb.type + ' ' + cb.getAttribute('type'));\n"
            + "  var r = document.getElementById('aRadio');\n"
            + "  alert(r.type + ' ' + r.getAttribute('type'));\n"
            + "  var f = document.getElementById('aFile');\n"
            + "  alert(f.type + ' ' + f.getAttribute('type'));\n"
            + "  f.type = 'CHECKBOX';\n"
            + "  alert(f.type + ' ' + f.getAttribute('type'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form action='foo'>\n"
            + "<input Type='TeXt' id='aText' value='some test'>\n"
            + "<input tYpe='PassWord' id='aPassword' value='some test'>\n"
            + "<input tyPe='Hidden' id='aHidden' value='some test'>\n"
            + "<input typE='CheckBox' id='aCb'>\n"
            + "<input TYPE='rAdiO' id='aRadio'>\n"
            + "<input type='FILE' id='aFile'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void readOnly() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    alert(input.readOnly);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<input id='myInput' value='some test' readonly='false'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"", "hello", "left", "hi", "right" },
            IE = {"", "error", "", "", "error", "", "" })
    public void align() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var i = document.getElementById('i');\n"
            + "        alert(i.align);\n"
            + "        set(i, 'hello');\n"
            + "        alert(i.align);\n"
            + "        set(i, 'left');\n"
            + "        alert(i.align);\n"
            + "        set(i, 'hi');\n"
            + "        alert(i.align);\n"
            + "        set(i, 'right');\n"
            + "        alert(i.align);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.align = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><input type='text' id='i' /></body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "A", "a", "A", "a8", "8Afoo", "8", "@" })
    public void accessKey() throws Exception {
        final String html
            = "<html><body><input id='a1'></input><input id='a2' accesskey='A'></input><script>\n"
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a';\n"
            + "a2.accessKey = 'A';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a8';\n"
            + "a2.accessKey = '8Afoo';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = '8';\n"
            + "a2.accessKey = '@';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

}
