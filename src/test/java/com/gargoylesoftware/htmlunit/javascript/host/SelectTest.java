/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * Tests for {@link HTMLSelectElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 */
public class SelectTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public SelectTest( final String name ) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetSelectedIndex() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    alert(document.form1.select1.length);\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1'>One</option>"
            + "        <option name='option2' selected>Two</option>"
            + "        <option name='option3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"3", "1"};

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetSelectedIndex() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    alert(document.form1.select1.length);\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "    document.form1.select1.selectedIndex = 2;\n"
            + "    alert(document.form1.select1.length);\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "    document.form1.select1.selectedIndex = -1;\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1' action='http://test' method='get'>"
            + "    <select name='select1'>"
            + "        <option value='option1' name='option1'>One</option>"
            + "        <option value='option2' name='option2' selected>Two</option>"
            + "        <option value='option3' name='option3'>Three</option>"
            + "    </select>"
            + "    <input type='submit' id='clickMe' name='submit' value='button'>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"3", "1", "3", "2"};
        assertEquals( expectedAlerts, collectedAlerts );

        final HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("clickMe");
        final HtmlPage newPage = (HtmlPage) button.click();

        final MockWebConnection webConnection = (MockWebConnection) newPage.getWebClient().getWebConnection();

        assertEquals("http://test?submit=button", newPage.getWebResponse().getUrl());
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetSelectedIndexInvalidValue() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    document.form1.select1.selectedIndex = -1;\n"
            + "    document.form1.select1.selectedIndex = 2;\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1' action='http://test' method='get'>"
            + "    <select name='select1'>"
            + "        <option value='option1' name='option1'>One</option>"
            + "        <option value='option2' name='option2' selected>Two</option>"
            + "        <option value='option3' name='option3'>Three</option>"
            + "    </select>"
            + "    <input type='submit' id='clickMe' name='submit' value='button'>"
            + "</form>"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("clickMe");
        final HtmlPage newPage = (HtmlPage) button.click();

        assertEquals("http://test?select1=option3&submit=button", newPage.getWebResponse().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetOptions() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    for( i=0; i<options.length; i++ ) {\n"
            + "        alert(options[i].value);\n"
            + "        alert(options[i].text);\n"
            + "    }\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"value1", "One", "value2", "Two", "value3", "Three"};

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetOptionLabel() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    for( i=0; i<options.length; i++ ) {\n"
            + "        alert(options[i].value);\n"
            + "        alert(options[i].text);\n"
            + "    }\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1' label='OneLabel'>One</option>"
            + "        <option name='option2' value='value2' label='TwoLabel' selected>Two</option>"
            + "        <option name='option3' value='value3' label='ThreeLabel'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"value1", "OneLabel", "value2", "TwoLabel", "value3", "ThreeLabel"};

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetOptionSelected() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    alert(options[0].selected);\n"
            + "    alert(options[1].selected);\n"
            + "    options[0].selected = true;\n"
            + "    alert(options[0].selected);\n"
            + "    alert(options[1].selected);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"false", "true", "true", "false"};

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     *
     * @throws Exception if the test fails
     */
    public void testGetOptionByIndex() throws Exception {
        final String content
            = "<html><head><title>first</title><script language='JavaScript'>"
            //+ "//<!--"
            + "function buggy(){\n"
            + "var option1 = document.f1.elements['select'][0];\n"
            + "alert(option1!=null);\n"
            + "}\n"
            //+ "//-->\n"
            + "</script></head><body onload='buggy();'>"
            + "<form name='f1' action='xxx.html'><SELECT name='select'>"
            + "<OPTION value='A'>111</OPTION>"
            + "<OPTION value='B'>222</OPTION>"
            + "</SELECT></form></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        assertEquals("first", page.getTitleText());
        assertEquals( Collections.singletonList("true"), collectedAlerts );
    }

    /**
     *
     * @throws Exception if the test fails
     */
    public void testGetOptionByOptionIndex() throws Exception {
        final String content
            = "<html><head><title>first</title><script language='JavaScript'>"
            //+ "//<!--"
            + "function buggy(){\n"
            + "var option1 = document.form1.select1.options[0];\n"
            + "alert(option1.text);\n"
            + "}\n"
            //+ "//-->\n"
            + "</script></head><body onload='buggy();'>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</form></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("first", page.getTitleText());
        assertEquals( Collections.singletonList("One"), collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAddOption() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    var index = options.length;\n"
            + "    options[index] = new Option('Four','value4');\n"
            + "    alert(options.length);\n"
            + "    alert(options[index].text);\n"
            + "    alert(options[index].value);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"4", "Four", "value4"};

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAddOptionSelected() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var oSelect = document.form1.select1;\n"
            + "    var options = oSelect.options;\n"
            + "    var firstSelectedIndex = oSelect.selectedIndex;\n"
            + "    alert(firstSelectedIndex);\n"
            + "    alert(options[firstSelectedIndex].selected);\n"
            + "    var index = options.length;\n"
            + "    var oOption = new Option('Four','value4');\n"
            + "    oOption.selected = true;\n"
            + "    options[index] = oOption;\n"
            + "    alert(options.length);\n"
            + "    alert(options[index].text);\n"
            + "    alert(options[index].value);\n"
            + "    alert(options[index].selected);\n"
            + "    alert(oSelect.selectedIndex);\n"
            + "    alert(options[firstSelectedIndex].selected);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"1", "true", "4", "Four", "value4", "true", "3", "false"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAddOptionWithAddMethod_FF() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1;\n"
            + "    options.add(new Option('Four','value4'), null);\n"
            + "    alert(options.length);\n"
            + "    var index = options.length - 1;\n"
            + "    alert(options[index].text);\n"
            + "    alert(options[index].value);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"4", "Four", "value4"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Test for bug 1570478.
     * @throws Exception if the test fails
     */
    public void testAddOptionWithAddMethod_IE() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var oSelect = document.form1.select1;\n"
            + "    oSelect.add(new Option('Four', 'value4'));\n"
            + "    alert(oSelect.length);\n"
            + "    alert(oSelect[oSelect.length-1].text);\n"
            + "    alert(oSelect[oSelect.length-1].value);\n"
            + "    oSelect.add(new Option('Three b', 'value3b'), 3);\n"
            + "    alert(oSelect[3].text);\n"
            + "    alert(oSelect[3].value);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"4", "Four", "value4", "Three b", "value3b"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Regression test for bug 1304741
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1304741&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    public void testAddWith1Arg() throws Exception {
        final String content
            = "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + " var oSelect = document.forms.testForm.testSelect;"
            + " alert(oSelect.length);"
            + " var opt = new Option('foo', '123');"
            + " oSelect.add(opt);"
            + " alert(oSelect.length);"
            + "}"
            + "</script>"
            + "</head>"
            + ""
            + "<body onload='test()'>"
            + "<form name='testForm'>"
            + "<select name='testSelect'></select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"0", "1"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testRemoveOption() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    options[1]=null;\n"
            + "    alert(options.length);\n"
            + "    alert(options[1].text);\n"
            + "    alert(options[1].value);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"2", "Three", "value3"};
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testRemoveOptionWithRemoveMethod() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1;\n"
            + "    options.remove(1);\n"
            + "    alert(options.length);\n"
            + "    alert(options[1].text);\n"
            + "    alert(options[1].value);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"2", "Three", "value3"};
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClearOptions() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    options.length=0;\n"
            + "    alert(options.length);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"0"};
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Test that option array is filled with empty options when lenght is increased.
     * Test case for bug 1370484
     * @throws Exception if the test fails
     */
    public void testIncreaseOptionsSettingLength() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    alert(options.length);\n"
            + "    options.length = 2;\n"
            + "    alert(options.length);\n"
            + "    alert(options[1].text);\n"
            + "    alert(options[1].value);\n"
            + "    options.length = 50;\n"
            + "    options[49].text = 'foo';\n"
            + "    options[49].value = 'fooValue';\n"
            + "    alert(options[49].text);\n"
            + "    alert(options[49].value);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"1", "2", "", "", "foo", "fooValue"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOptionArrayHasItemMethod() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    alert(options.item(0).text);\n"
            + "    alert(options.item(0).value);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"One", "value1"};
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetValue() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    for (var i=1; i<6; ++i)\n"
            + "    alert(document.form1['select' + i].value);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1'>One</option>"
            + "        <option name='option2' selected is='test'>Two</option>"
            + "        <option name='option3'>Three</option>"
            + "    </select>"
            + "    <select name='select2'>"
            + "    </select>"
            + "    <select name='select3' multiple>"
            + "        <option name='option1'>One</option>"
            + "        <option name='option2' selected>Two</option>"
            + "        <option name='option3' selected>Three</option>"
            + "    </select>"
            + "    <select name='select4' multiple>"
            + "        <option name='option1'>One</option>"
            + "        <option name='option2'>Two</option>"
            + "        <option name='option3'>Three</option>"
            + "    </select>"
            + "    <select name='select5' multiple>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"Two", "", "Two", "", ""};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * changed made through JS should not trigger an onchange
     * @throws Exception if the test fails
     */
    public void testNoOnchangeFromJS() throws Exception {
        final String content = "<html><head><title>Test infinite loop on js onchange</title></head>"
            + "<body><form name='myForm'>"
            + "<select name='a' onchange='this.form.b.selectedIndex=0'>"
            + "<option value='1'>one</option>"
            + "<option value='2'>two</option>"
            + "</select>"
            + "<select name='b' onchange='alert(\"b changed\")'>"
            + "<option value='G'>green</option>"
            + "<option value='R' selected>red</option>"
            + "</select>"
            + "</form>"
            + "</body>"
            + "</html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSelect selectA = page.getFormByName("myForm").getSelectByName("a");
        final HtmlOption optionA2 = selectA.getOption(1);

        assertEquals("two", optionA2.asText());

        final HtmlSelect selectB = page.getFormByName("myForm").getSelectByName("b");
        assertEquals(1, selectB.getSelectedOptions().size());
        assertEquals("red", ((HtmlOption) selectB.getSelectedOptions().get(0)).asText());

         // changed selection in first select
        optionA2.setSelected(true);
        assertTrue(optionA2.isSelected());
        assertEquals(1, selectB.getSelectedOptions().size());
        assertEquals("green", ((HtmlOption) selectB.getSelectedOptions().get(0)).asText());

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetValue() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "    document.form1.select1.value = 'option2';\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1' action='http://test'>"
            + "    <select name='select1'>"
            + "        <option value='option1' name='option1'>One</option>"
            + "        <option value='option2' name='option2'>Two</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"0", "1"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Test for bug 1159709.
     * @throws Exception if the test fails
     */
    public void testRightPageAfterOnchange() throws Exception {
        final String content
            = "<html><body>"
            + "<iframe src='fooIFrame.html'></iframe>"
            + "<form name='form1' action='http://first' method='post'>"
            + "    <select name='select1' onchange='this.form.submit()'>"
            + "        <option value='option1' selected='true' name='option1'>One</option>"
            + "        <option value='option2' name='option2'>Two</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        webConnection.setDefaultResponse("<html><body></body></html>");
        webConnection.setResponse(URL_FIRST, content);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        final HtmlForm form = page.getFormByName("form1");
        final HtmlSelect select = form.getSelectByName("select1");
        final Page page2 = select.setSelectedAttribute("option2", true);
        assertEquals("http://first", page2.getWebResponse().getUrl());
    }

    /**
     * Test that options delegates to select (bug 1111597).
     * @throws Exception if the test fails.
     */
    public void testOptionsDelegateToSelect() throws Exception {

        final String content
            = "<html><head>"
            + "<script>"
            + "function doTest() {"
            + "  var s = document.getElementById('select1');"
            + "  alert(s.childNodes.length);"
            + "  alert(s.options.childNodes.length);"
            + "  alert(s.selectedIndex);"
            + "  alert(s.options.selectedIndex);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='doTest()'>"
            + "<form name='test'>"
            + "<select id='select1'>"
            + "<option>a</option>"
            + "<option selected='selected'>b</option>"
            + "</select></form>"
            + "</body></html>";

        final String[] expectedAlerts = {"2", "2", "1", "1"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        assertEquals( expectedAlerts, collectedAlerts );

        try {
            loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);
            fail("Should have thrown a JS error");
        }
        catch (final ScriptException e) {
            // that's ok
        }
    }

    /**
     * Test that options delegates to select (bug 1111597).
     * @throws Exception if the test fails.
     */
    public void testOptionsArrayAdd() throws Exception {

        final String content
            = "<html><head>"
            + "<script>"
            + "function doTest() {"
            + "  var s = document.getElementById('select1');"
            + "  var lengthBefore = s.options.length;"
            + "  alert(lengthBefore);"
            + "  alert(s.options.item(lengthBefore - 1).text);"
            + "  var opt = document.createElement(\"OPTION\");"
            + "  opt.value = 'c';"
            + "  opt.text = 'c';"
            + "  s.options.add(opt);"
            + "  var lengthAfterAdd = s.options.length;"
            + "  alert(lengthAfterAdd);"
            + "  alert(s.options.item(lengthAfterAdd - 1).text);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='doTest()'>"
            + "<form name='test'>"
            + "<select id='select1'>"
            + "<option>a</option>"
            + "<option selected='selected'>b</option>"
            + "</select></form>"
            + "</body></html>";

        final String[] expectedAlerts = {"2", "b", "3", "c"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Test that select delegates submit to form
     * @throws Exception if the test fails.
     */
    public void testOnChangeCallsFormSubmit() throws Exception {

        final String content
            = "<html><head>"
            + "</head>"
            + "<body>"
            + "<form name='test' action='foo'>"
            + "<select name='select1' onchange='submit()'>"
            + "<option>a</option>"
            + "<option selected='selected'>b</option>"
            + "</select></form>"
            + "</body></html>";

        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        webConnection.setDefaultResponse("<html><title>page 2</title><body></body></html>");
        webConnection.setResponse(URL_FIRST, content);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        final HtmlPage page2 = (HtmlPage) page.getFormByName("test").getSelectByName("select1").getOption(0).click();
        assertEquals("page 2", page2.getTitleText());
    }

    /**
     * Test for 1684652
     * @throws Exception if the test fails
     */
    public void testSelectedIndexReset() throws Exception {
        final String content
            = "<html><head><title>first</title></head>"
            + "<body onload='document.forms[0].testSelect.selectedIndex = -1; "
            + "document.forms[0].testSelect.options[0].selected=true;'>"
            + "<form>"
            + "<select name='testSelect'>"
            + "<option value='testValue'>value</option>"
            + "</select>"
            + "<input id='testButton' type='submit'>"
            + "</form>"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final Page page2 = ((ClickableElement) page.getHtmlElementById("testButton")).click();
        final URL url2 = page2.getWebResponse().getUrl();
        assertTrue("Select in url " + url2, url2.toExternalForm().indexOf("testSelect=testValue") != -1);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSelectedIndex() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = document.getElementById( 'mySelect' );\n"
            + "    s.options.length = 0;\n"
            + "    s.selectedIndex = 0;\n"
            + "    alert( s.selectedIndex );\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<select id='mySelect'><option>hello</option></select>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"-1"};
        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        loadPage( html, collectedAlerts );
        assertEquals( expectedAlerts, collectedAlerts);
    }
}
