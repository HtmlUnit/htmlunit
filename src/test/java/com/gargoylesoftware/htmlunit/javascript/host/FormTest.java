/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
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
public class FormTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public FormTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testElementsAccessor() throws Exception {
        final String content
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
            + "    <button type='button' name='button2' />\n"
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
            + "    <button type='reset' name='reset2' />\n"
            + "    <input type='submit' name='submit1' />\n"
            + "    <button type='submit' name='submit2' />\n"
            + "    <input type='text' name='textInput1' />\n"
            + "    <textarea name='textarea1'>foo</textarea>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {
            "16", "button1", "button2", "checkbox1", "fileupload1", "hidden1",
            "radio1", "radio1",
            "select1", "select2", "password1", "reset1",
            "reset2", "submit1", "submit2", "textInput1", "textarea1"
        };

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testRadioButtonArray() throws Exception {
        final String content
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

        final List<String> collectedAlerts = new ArrayList<String>();

        final String[] expectedAlerts = {"3", "1", "2", "3"};

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final HtmlPage page = loadPage(content, collectedAlerts);

        assertEquals("foo", page.getTitleText());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * If there is only one radio button with a specified name then that radio
     * button will be returned for the name, not an array of radio buttons.  Test
     * this.
     * @throws Exception if the test fails
     */
    public void testRadioButton_OnlyOne() throws Exception {
        final String content
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testActionProperty() throws Exception {
        final String jsProperty = "action";
        final String htmlProperty = "action";
        final String oldValue = "http://foo.com";
        final String newValue = "mailto:me@bar.com";

        final HtmlForm form = doTestProperty(jsProperty, htmlProperty, oldValue, newValue);
        assertEquals(newValue, form.getActionAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testNameProperty() throws Exception {
        final String jsProperty = "name";
        final String htmlProperty = "name";
        final String oldValue = "myForm";
        final String newValue = "testForm";

        final HtmlForm form = doTestProperty(jsProperty, htmlProperty, oldValue, newValue);
        assertEquals(newValue, form.getNameAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testEncodingProperty() throws Exception {
        final String jsProperty = "encoding";
        final String htmlProperty = "enctype";
        final String oldValue = "myEncoding";
        final String newValue = "newEncoding";

        final HtmlForm form = doTestProperty(jsProperty, htmlProperty, oldValue, newValue);
        assertEquals(newValue, form.getEnctypeAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testMethodProperty() throws Exception {
        final String jsProperty = "method";
        final String htmlProperty = "method";
        final String oldValue = "get";
        final String newValue = "post";

        final HtmlForm form = doTestProperty(jsProperty, htmlProperty, oldValue, newValue);
        assertEquals(newValue, form.getMethodAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testTargetProperty() throws Exception {
        final String jsProperty = "target";
        final String htmlProperty = "target";
        final String oldValue = "_top";
        final String newValue = "_parent";

        final HtmlForm form = doTestProperty(jsProperty, htmlProperty, oldValue, newValue);
        assertEquals(newValue, form.getTargetAttribute());
    }

    private HtmlForm doTestProperty(
            final String jsProperty,
            final String htmlProperty,
            final String oldValue,
            final String newValue)
        throws
            Exception {

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.forms[0]." + jsProperty + ");\n"
            + "    document.forms[0]." + jsProperty + "='" + newValue + "'\n"
            + "    alert(document.forms[0]." + jsProperty + ");\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form " + htmlProperty + "='" + oldValue + "'>\n"
            + "    <input type='button' name='button1' />\n"
            + "</form>\n"
            + "</body></html>";

        final String[] expectedAlerts = {oldValue, newValue};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
        return (HtmlForm) page.getForms().get(0);
    }

    /**
     * Tests form reset and input default values while emulating IE.
     * @throws Exception if the test fails
     */
    public void testFormReset_IE() throws Exception {
        // As tested with IE 6.0 on Win2k; note that refreshing the page will get you different results;
        // you need to open a new browser instance each time you test this.
        final String[] expected = {
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
        formResetTest(BrowserVersion.INTERNET_EXPLORER_6_0, expected);
    }

    /**
     * Tests form reset and input default values while emulating Mozilla.
     * @throws Exception if the test fails
     */
    public void testFormReset_Firefox() throws Exception {
        // As tested with Firefox 1.0.3 on Win2k.
        final String[] expected = {
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
            "textarea: initial11 default11 undefined undefined",
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
        formResetTest(BrowserVersion.FIREFOX_2, expected);
    }

    /**
     * Tests form reset and input default values while emulating the specified browser.
     * @param browserVersion The version of the browser to emulate.
     * @param expected The expected results.
     * @throws Exception if the test fails
     */
    private void formResetTest(final BrowserVersion browserVersion, final String[] expected) throws Exception {
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

        final List<String> collected = new ArrayList<String>();
        loadPage(browserVersion, html, collected);

        // The lists are too long to call assertEquals() on them directly and get meaningful failure info.
        for (int i = 0; i < expected.length; i++) {
            final String s1 = expected[i];
            final String s2;
            if (collected.size() > i) {
                s2 = (String) collected.get(i);
            }
            else {
                s2 = null;
            }
            assertEquals("At index " + i + ", expected '" + s1 + "' but got '" + s2 + "'.", s1, s2);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmit() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String firstContent
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

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        final HtmlPage secondPage =
            (HtmlPage) page.executeJavaScript("document.form1.submit()").getNewPage();
        assertEquals("second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOnSubmitChangesAction() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String firstContent
            = "<html><body>\n"
            + "<form name='form1' action='" + URL_SECOND + "' onsubmit='this.action=\"" + URL_THIRD + "\"' "
            + "method='post'>\n"
            + "    <input type='submit' id='button1' />\n"
            + "</form>\n"
            + "</body></html>";
        final String defaultContent = "<html></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(defaultContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final Page page2 = ((ClickableElement) page.getHtmlElementById("button1")).click();

        assertEquals(URL_THIRD.toExternalForm(), page2.getWebResponse().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmit_target() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String firstContent
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

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        final HtmlPage secondPage
            = (HtmlPage) page.executeJavaScript("document.form1.submit()").getNewPage();
        assertEquals("second", secondPage.getTitleText());
        assertEquals("MyNewWindow", secondPage.getEnclosingWindow().getName());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmitDoesntCallOnSubmit() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "' onsubmit=\"alert('onsubmit called')\">\n"
            + "    <input type='submit' />\n"
            + "</form>\n"
            + "<a href='javascript:document.form1.submit()' id='link1'>Click me</a>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlAnchor link = (HtmlAnchor) page.getHtmlElementById("link1");
        link.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testInputNamedId() throws Exception {
        doTestInputWithName("id");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testInputNamedAction() throws Exception {
        doTestInputWithName("action");
    }

    private void doTestInputWithName(final String name) throws Exception {
        final String content
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());
        final String[] expectedAlerts = {name + '2', "foo"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test that used to blow up on page load
     * @throws Exception if the test fails
     */
    public void testAccessingRadioButtonArrayByName_Regression() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
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

        webConnection.setResponse(URL_FIRST, firstContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("Button Test", page.getTitleText());

        final String[] expectedAlerts = {"value = 2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for a bug that appeared when visiting mail.yahoo.com. Setting the value of one input
     * seems to blow away the other input. Also tests that the form input collection does not get
     * cached before the document is finished loading.
     * @throws Exception if the test fails
     */
    public void testFindInputWithoutTypeDefined() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<body onload='alert(document.simple_form.login.value);'>\n"
            + "<p>hello world</p><table><tr><td>\n"
            + "<form action='login.jsp' name='simple_form'>\n"
            + "    <input name='msg' type='hidden' value='0'>\n"
            + "    <script>document.simple_form.msg.value=1</script>\n"
            + "    <input name='login' size='17' value='foo'>\n"
            + "</form></td></tr></table>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();

        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        assertEquals("foo", page.getTitleText());
        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmit_MultipleButtons() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String firstContent
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

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        final HtmlButton button = (HtmlButton) page.getHtmlElementById("button1");
        final HtmlPage secondPage = (HtmlPage) button.click();
        assertEquals("second", secondPage.getTitleText());
        assertEquals(URL_SECOND + "?button1=", secondPage.getWebResponse().getUrl());
    }

    /**
     * Test form.length - This method does not count the type=image
     * input tags.
     * @throws Exception if the test fails
     */
    public void testLength() throws Exception {
        final String content
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"2"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGet() throws Exception {
        final String content
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"button1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
    * @throws Exception if the test fails
    */
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
        final HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("clickMe");
        button.click();
        final String[] expectedAlerts = {"hi!"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
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
        final HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("clickMe");
        button.click();
        final String[] expectedAlerts = {"hi!"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that the elements collection is live
    * @throws Exception if the test fails
    */
    public void testElementsLive() throws Exception {
        final String content = "<html>\n"
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"0", "1", "1", "true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetFormFromFormsById() throws Exception {
        final String content =
            "<html>\n"
            + "<head></head>\n"
            + "<body onload=\"alert(document.forms['myForm'].action)\">\n"
            + "<form id='myForm' action='foo.html'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"foo.html"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetFieldNamedLikeForm() throws Exception {
        final String content =
            "<html>\n"
            + "<head></head>\n"
            + "<body onload='alert(document.login.login.type)'>\n"
            + "<form name='login' action='foo.html'>\n"
            + "<input name='login' type='text'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"text"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * document.myForm.submit returns a field name submit or the submit function
     * depending on the sort of the field named submit
     * @throws Exception if the test fails
     */
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
     * @param htmlSnippet The html to embed in the test
     * @param expected The expected alert
     * @throws Exception if the test fails
     */
    private void testFieldNamedSubmit(final String htmlSnippet, final String expected) throws Exception {
        final String content =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {expected};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFieldFoundWithID() throws Exception {
        final String content = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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
        final String[] expectedAlerts = {"before", "2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testNonFieldChildFound() throws Exception {
        final String content = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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
        final String[] expectedAlerts = {"INPUT", "idImg1", "img2", "true"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * This test shows a problem in current implementation of host object with visible constructors
     * used to retrieve JS object associated to a particular DOM node. The general problem needs
     * later.
     * @throws Exception if the test fails
     */
    public void testFormIsNotAConstructor() throws Exception {
        final String content = "<html><head>\n"
            + "<script>\n"
            + "var Form = {};\n"
            + "function test()\n"
            + "{\n"
            + "    document.getElementById('formId');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "   <form id='formId' name='formName'>\n"
            + "     <input type='text' name='field1' value='barney'>\n"
            + "     <input type='submit'>\n"
            + "</body>\n"
            + "</html>";
        final List<String> emptyList = Collections.emptyList();
        createTestPageForRealBrowserIfNeeded(content, emptyList);
        loadPage(content);
    }
    
    /**
     * Test that the form from the right page is returned after browsing.
     * Regression test for
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1627983&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    public void testFormAccessAfterBrowsing() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String firstContent = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  alert('page 1: ' + document.forms[0].name);\n"
            + "  document.location = 'page2.html';\n"
            + ""
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form name='formPage1' action='foo'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  alert('page 2: ' + document.forms[0].name);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form name='formPage2' action='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection(webConnection);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);

        final String[] expectedAlerts = {"page 1: formPage1", "page 2: formPage2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that the event object is correctly made available
     * Regression test for
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1648014&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    public void testOnSubmitEvent() throws Exception {
        final String[] expectedAlertsIE = {"srcElement null: false", "srcElement==form: true",
            "target null: true", "target==form: false"};
        testOnSubmitEvent(BrowserVersion.INTERNET_EXPLORER_6_0, expectedAlertsIE);

        final String[] expectedAlertsFF = {"srcElement null: true", "srcElement==form: false",
            "target null: false", "target==form: true"};
        testOnSubmitEvent(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    /**
     *
     * @param browserVersion browser version
     * @param expectedAlerts expected alerts
     * @throws Exception If the test fails
     */
    protected void testOnSubmitEvent(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final WebClient client = new WebClient(browserVersion);
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String content = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function test(_event)\n"
            + "{\n"
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

        webConnection.setResponse(URL_FIRST, content);
        client.setWebConnection(webConnection);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        ((ClickableElement) page.getHtmlElementById("theButton")).click();

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * In action "this" should be the window and not the form
     * @throws Exception if the test fails
     */
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
        final HtmlPage page1 = loadPage(content, collectedAlerts);
        final Page page2 = ((ClickableElement) page1.getHtmlElementById("theButton")).click();

        assertEquals(expectedAlerts, collectedAlerts);
        assertSame(page1, page2);
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testOnsubmitNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function handler() {}\n"
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    form.onsubmit = handler;\n"
            + "    alert(form.onsubmit);\n"
            + "    form.onsubmit = null;\n"
            + "    alert(form.onsubmit);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <form id='myForm'></form>\n"
            + "</body></html>";
        
        final String[] expectedAlerts = {"\nfunction handler() {\n}\n", "null"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
