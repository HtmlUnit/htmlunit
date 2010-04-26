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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLSelectElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "1" })
    public void testGetSelectedIndex() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.form1.select1.length);\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1'>One</option>\n"
            + "        <option name='option2' selected>Two</option>\n"
            + "        <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "1", "3", "2" })
    public void testSetSelectedIndex() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.form1.select1.length);\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "    document.form1.select1.selectedIndex = 2;\n"
            + "    alert(document.form1.select1.length);\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "    document.form1.select1.selectedIndex = -1;\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' action='http://test' method='get'>\n"
            + "    <select name='select1'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
            + "        <option value='option2' name='option2' selected>Two</option>\n"
            + "        <option value='option3' name='option3'>Three</option>\n"
            + "    </select>\n"
            + "    <input type='submit' id='clickMe' name='submit' value='button'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        getMockWebConnection().setDefaultResponse("");

        final HtmlSubmitInput button = page.getHtmlElementById("clickMe");
        final HtmlPage newPage = button.click();

        assertEquals("http://test/?submit=button", newPage.getWebResponse().getWebRequest().getUrl());
        assertSame("method", HttpMethod.GET, getMockWebConnection().getLastMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "-1", "2", "exception", "2", "exception", "2" },
            IE = { "-1", "2", "-1", "-1" })
    public void testSetSelectedIndexInvalidValue() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var s = document.form1.select1;\n"
            + "  s.selectedIndex = -1;\n"
            + "  alert(s.selectedIndex);\n"
            + "  s.selectedIndex = 2;\n"
            + "  alert(s.selectedIndex);\n"
            + "  try { s.selectedIndex = 25; } catch (e) { alert('exception') }\n"
            + "  alert(s.selectedIndex);\n"
            + "  try { s.selectedIndex = -14; } catch (e) { alert('exception') }\n"
            + "  alert(s.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' action='http://test' method='get'>\n"
            + "    <select name='select1'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
            + "        <option value='option2' name='option2' selected>Two</option>\n"
            + "        <option value='option3' name='option3'>Three</option>\n"
            + "    </select>\n"
            + "    <input type='submit' id='clickMe' name='submit' value='button'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "value1", "One", "value2", "Two", "value3", "Three" })
    public void testGetOptions() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    for (i=0; i<options.length; i++) {\n"
            + "        alert(options[i].value);\n"
            + "        alert(options[i].text);\n"
            + "    }\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "value1", "One", "value2", "Two", "value3", "Three" })
    public void testGetOptionLabel() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    for (i=0; i<options.length; i++) {\n"
            + "        alert(options[i].value);\n"
            + "        alert(options[i].text);\n"
            + "    }\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1' label='OneLabel'>One</option>\n"
            + "        <option name='option2' value='value2' label='TwoLabel' selected>Two</option>\n"
            + "        <option name='option3' value='value3' label='ThreeLabel'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "true", "true", "false" })
    public void testGetOptionSelected() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    alert(options[0].selected);\n"
            + "    alert(options[1].selected);\n"
            + "    options[0].selected = true;\n"
            + "    alert(options[0].selected);\n"
            + "    alert(options[1].selected);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void testGetOptionByIndex() throws Exception {
        final String html
            = "<html><head><title>first</title><script language='JavaScript'>\n"
            //+ "//<!--"
            + "function buggy(){\n"
            + "var option1 = document.f1.elements['select'][0];\n"
            + "alert(option1!=null);\n"
            + "}\n"
            //+ "//-->\n"
            + "</script></head><body onload='buggy();'>\n"
            + "<form name='f1' action='xxx.html'><SELECT name='select'>\n"
            + "<OPTION value='A'>111</OPTION>\n"
            + "<OPTION value='B'>222</OPTION>\n"
            + "</SELECT></form></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("One")
    public void testGetOptionByOptionIndex() throws Exception {
        final String html
            = "<html><head><title>first</title><script language='JavaScript'>\n"
            //+ "//<!--"
            + "function buggy(){\n"
            + "var option1 = document.form1.select1.options[0];\n"
            + "alert(option1.text);\n"
            + "}\n"
            //+ "//-->\n"
            + "</script></head><body onload='buggy();'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</form></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "4", "Four", "value4" })
    public void testAddOption() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    var index = options.length;\n"
            + "    options[index] = new Option('Four','value4');\n"
            + "    alert(options.length);\n"
            + "    alert(options[index].text);\n"
            + "    alert(options[index].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "true", "4", "Four", "value4", "true", "3", "false" })
    public void testAddOptionSelected() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
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
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "4", "Four", "value4" })
    @Browsers(Browser.FF)
    public void testAddOptionWithAddMethod_FF() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1;\n"
            + "    options.add(new Option('Four','value4'), null);\n"
            + "    alert(options.length);\n"
            + "    var index = options.length - 1;\n"
            + "    alert(options[index].text);\n"
            + "    alert(options[index].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test for bug 1570478.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = { "4", "Four", "value4", "Three b", "value3b" })
    public void testAddOptionWithAddMethod_IE() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var oSelect = document.form1.select1;\n"
            + "  try {\n"
            + "    oSelect.add(new Option('Four', 'value4'));\n"
            + "    alert(oSelect.length);\n"
            + "    alert(oSelect[oSelect.length-1].text);\n"
            + "    alert(oSelect[oSelect.length-1].value);\n"
            + "    oSelect.add(new Option('Three b', 'value3b'), 3);\n"
            + "    alert(oSelect[3].text);\n"
            + "    alert(oSelect[3].value);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 1304741.
     * See https://sourceforge.net/tracker/index.php?func=detail&aid=1304741&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0", "exception" }, IE = { "0", "1" })
    public void testAddWith1Arg() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var oSelect = document.forms.testForm.testSelect;\n"
            + "    alert(oSelect.length);\n"
            + "    var opt = new Option('foo', '123');\n"
            + "    oSelect.add(opt);\n"
            + "    alert(oSelect.length);\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='testForm'>\n"
            + "<select name='testSelect'></select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "Three", "value3" })
    public void testRemoveOption() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    options[1]=null;\n"
            + "    alert(options.length);\n"
            + "    alert(options[1].text);\n"
            + "    alert(options[1].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "Three", "value3" })
    public void testRemoveOptionWithRemoveMethod() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1;\n"
            + "    options.remove(1);\n"
            + "    alert(options.length);\n"
            + "    alert(options[1].text);\n"
            + "    alert(options[1].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void testClearOptions() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    options.length=0;\n"
            + "    alert(options.length);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test that option array is filled with empty options when length is increased.
     * Test case for bug 1370484
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2", "", "", "foo", "fooValue" })
    public void testIncreaseOptionsSettingLength() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
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
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "One", "value1" })
    public void testOptionArrayHasItemMethod() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    alert(options.item(0).text);\n"
            + "    alert(options.item(0).value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "Two", "", "Two", "", "" }, IE = { "", "", "", "", "" })
    public void getValue() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    for (var i=1; i<6; i++)\n"
            + "    alert(document.form1['select' + i].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1'>One</option>\n"
            + "        <option name='option2' selected is='test'>Two</option>\n"
            + "        <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "    <select name='select2'>\n"
            + "    </select>\n"
            + "    <select name='select3' multiple>\n"
            + "        <option name='option1'>One</option>\n"
            + "        <option name='option2' selected>Two</option>\n"
            + "        <option name='option3' selected>Three</option>\n"
            + "    </select>\n"
            + "    <select name='select4' multiple>\n"
            + "        <option name='option1'>One</option>\n"
            + "        <option name='option2'>Two</option>\n"
            + "        <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "    <select name='select5' multiple>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Changes made through JS should not trigger an onchange event.
     * @throws Exception if the test fails
     */
    @Test
    public void testNoOnchangeFromJS() throws Exception {
        final String html = "<html><head><title>Test infinite loop on js onchange</title></head>\n"
            + "<body><form name='myForm'>\n"
            + "<select name='a' onchange='this.form.b.selectedIndex=0'>\n"
            + "<option value='1'>one</option>\n"
            + "<option value='2'>two</option>\n"
            + "</select>\n"
            + "<select name='b' onchange='alert(\"b changed\")'>\n"
            + "<option value='G'>green</option>\n"
            + "<option value='R' selected>red</option>\n"
            + "</select>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlSelect selectA = page.getFormByName("myForm").getSelectByName("a");
        final HtmlOption optionA2 = selectA.getOption(1);

        assertEquals("two", optionA2.asText());

        final HtmlSelect selectB = page.getFormByName("myForm").getSelectByName("b");
        assertEquals(1, selectB.getSelectedOptions().size());
        assertEquals("red", selectB.getSelectedOptions().get(0).asText());

         // changed selection in first select
        optionA2.setSelected(true);
        assertTrue(optionA2.isSelected());
        assertEquals(1, selectB.getSelectedOptions().size());
        assertEquals("green", selectB.getSelectedOptions().get(0).asText());

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1" })
    public void testSetValue() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "    document.form1.select1.value = 'option2';\n"
            + "    alert(document.form1.select1.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' action='http://test'>\n"
            + "    <select name='select1'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
            + "        <option value='option2' name='option2'>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test for bug 1159709.
     * @throws Exception if the test fails
     */
    @Test
    public void testRightPageAfterOnchange() throws Exception {
        final String html
            = "<html><body>\n"
            + "<iframe src='fooIFrame.html'></iframe>\n"
            + "<form name='form1' action='http://first' method='post'>\n"
            + "    <select name='select1' onchange='this.form.submit()'>\n"
            + "        <option value='option1' selected='true' name='option1'>One</option>\n"
            + "        <option value='option2' name='option2'>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse("<html><body></body></html>");
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlForm form = page.getFormByName("form1");
        final HtmlSelect select = form.getSelectByName("select1");
        final Page page2 = select.setSelectedAttribute("option2", true);
        assertEquals("http://first/", page2.getWebResponse().getWebRequest().getUrl());
    }

    /**
     * Test that options delegates to select (bug 1111597).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2-2", "1-1", "2-2", "0-0", "2-2", "1-1" })
    @Browsers(Browser.IE)
    public void testOptionsDelegateToSelect() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var s = document.getElementById('select1');\n"
            + "  doAlerts(s);\n"
            + "  \n"
            + "  s.selectedIndex = 0;\n"
            + "  doAlerts(s);\n"
            + "  \n"
            + "  s.options.selectedIndex = 1;\n"
            + "  doAlerts(s);\n"
            + "}\n"
            + "function doAlerts(s) {\n"
            + "  alert(s.childNodes.length + '-' + s.options.childNodes.length);\n"
            + "  alert(s.selectedIndex + '-' + s.options.selectedIndex);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='test'>\n"
            + "<select id='select1'>"
            + "<option>a</option>"
            + "<option selected='selected'>b</option>"
            + "</select></form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test that options delegates to select (bug 1111597).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "b", "3", "c" })
    public void testOptionsArrayAdd() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var s = document.getElementById('select1');\n"
            + "  var lengthBefore = s.options.length;\n"
            + "  alert(lengthBefore);\n"
            + "  alert(s.options.item(lengthBefore - 1).text);\n"
            + "  var opt = document.createElement(\"OPTION\");\n"
            + "  opt.value = 'c';\n"
            + "  opt.text = 'c';\n"
            + "  s.options.add(opt);\n"
            + "  var lengthAfterAdd = s.options.length;\n"
            + "  alert(lengthAfterAdd);\n"
            + "  alert(s.options.item(lengthAfterAdd - 1).text);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='test'>\n"
            + "<select id='select1'>\n"
            + "<option>a</option>\n"
            + "<option selected='selected'>b</option>\n"
            + "</select></form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Tests that select delegates submit to the containing form.
     * @throws Exception if the test fails
     */
    @Test
    public void testOnChangeCallsFormSubmit() throws Exception {
        final String html
            = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form name='test' action='foo'>\n"
            + "<select name='select1' onchange='submit()'>\n"
            + "<option>a</option>\n"
            + "<option selected='selected'>b</option>\n"
            + "</select></form>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse("<html><title>page 2</title><body></body></html>");
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlPage page2 = page.getFormByName("test").getSelectByName("select1").getOption(0).click();
        assertEquals("page 2", page2.getTitleText());
    }

    /**
     * Test for bug 1684652.
     * @throws Exception if the test fails
     */
    @Test
    public void testSelectedIndexReset() throws Exception {
        final String html
            = "<html><head><title>first</title></head>\n"
            + "<body onload='document.forms[0].testSelect.selectedIndex = -1; "
            + "document.forms[0].testSelect.options[0].selected=true;'>\n"
            + "<form>\n"
            + "<select name='testSelect'>\n"
            + "<option value='testValue'>value</option>\n"
            + "</select>\n"
            + "<input id='testButton' type='submit'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final Page page2 = page.<HtmlElement>getHtmlElementById("testButton").click();
        final URL url2 = page2.getWebResponse().getWebRequest().getUrl();
        assertTrue("Select in URL " + url2, url2.toExternalForm().contains("testSelect=testValue"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-1")
    public void testSelectedIndex() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    s.options.length = 0;\n"
            + "    s.selectedIndex = 0;\n"
            + "    alert(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<select id='mySelect'><option>hello</option></select>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDefaultSelectedValue_SizeNegativeOne() throws Exception {
        testDefaultSelectedValue("-1", false, new String[] {"0", "true", "false", "false", "0"});
        testDefaultSelectedValue("-1", true, new String[] {"0", "false", "false", "false", "-1"});
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDefaultSelectedValue_SizeZero() throws Exception {
        testDefaultSelectedValue("0", false, new String[] {"0", "true", "false", "false", "0"});
        testDefaultSelectedValue("0", true, new String[] {"0", "false", "false", "false", "-1"});
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDefaultSelectedValue_SizeOne() throws Exception {
        testDefaultSelectedValue("1", false, new String[] {"1", "true", "false", "false", "0"});
        testDefaultSelectedValue("1", true, new String[] {"1", "false", "false", "false", "-1"});
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDefaultSelectedValue_SizeTwo() throws Exception {
        testDefaultSelectedValue("2", false, new String[] {"2", "false", "false", "false", "-1"});
        testDefaultSelectedValue("2", true, new String[] {"2", "false", "false", "false", "-1"});
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDefaultSelectedValue_SizeInvalid() throws Exception {
        testDefaultSelectedValue("x", false, new String[] {"0", "true", "false", "false", "0"});
        testDefaultSelectedValue("x", true, new String[] {"0", "false", "false", "false", "-1"});
    }

    /**
     * Tests default selection status for options in a select of the specified size, optionally
     * allowing multiple selections.
     * @param size the select input's size attribute
     * @param multiple whether or not the select input should allow multiple selections
     * @param expected the expected alerts
     * @throws Exception if the test fails
     */
    private void testDefaultSelectedValue(final String size, final boolean multiple, final String[] expected)
        throws Exception {
        final String m;
        if (multiple) {
            m = " multiple";
        }
        else {
            m = "";
        }
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test(){\n"
            + "      alert(document.getElementById('s').size);\n"
            + "      alert(document.getElementById('a').selected);\n"
            + "      alert(document.getElementById('b').selected);\n"
            + "      alert(document.getElementById('c').selected);\n"
            + "      alert(document.getElementById('s').selectedIndex);\n"
            + "   }\n"
            + "</script>\n"
            + "<form id='f'>\n"
            + "   <select id='s' size='" + size + "'" + m + ">\n"
            + "      <option id='a' value='a'>a</option>\n"
            + "      <option id='b' value='b'>b</option>\n"
            + "      <option id='c' value='c'>c</option>\n"
            + "   </select>\n"
            + "</form>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(getBrowserVersion(), html, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("5")
    public void testSize() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.size + 5);//to test if int or string\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<select id='mySelect'/>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "false", "false" })
    public void testMultiple() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.f['s1'].multiple);\n"
            + "    alert(document.f['s2'].multiple);\n"
            + "    document.f['s1'].multiple = false;\n"
            + "    alert(document.f['s1'].multiple);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='f'>\n"
            + "    <select name='s1' multiple>\n"
            + "        <option name='option1'>One</option>\n"
            + "        <option name='option2'>Two</option>\n"
            + "        <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "    <select name='s2'>\n"
            + "        <option name='option4'>Four</option>\n"
            + "        <option name='option5'>Five</option>\n"
            + "        <option name='option6'>Six</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts()
    public void selectedIndex_onfocus() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    s.selectedIndex = 1;\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<select id='mySelect' onfocus='alert(1)'><option>hello</option><option>there</option></select>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-1", "0", "-1" })
    public void selectedIndex_appendChild() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    var o = document.createElement('option');\n"
            + "    alert(s.selectedIndex);\n"
            + "    s.appendChild(o);\n"
            + "    alert(s.selectedIndex);\n"
            + "    s.removeChild(o);\n"
            + "    alert(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<select id='mySelect'></select>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-1", "0", "-1" })
    public void selectedIndex_insertBefore() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    var o = document.createElement('option');\n"
            + "    alert(s.selectedIndex);\n"
            + "    s.insertBefore(o, null);\n"
            + "    alert(s.selectedIndex);\n"
            + "    s.removeChild(o);\n"
            + "    alert(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<select id='mySelect'></select>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-1", "0", "-1" })
    public void selectedIndex_add() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    var o = document.createElement('option');\n"
            + "    alert(s.selectedIndex);\n"
            + "    if (document.all)\n"
            + "      s.add(o);\n"
            + "    else\n"
            + "      s.add(o, null);\n"
            + "    alert(s.selectedIndex);\n"
            + "    s.removeChild(o);\n"
            + "    alert(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<select id='mySelect'></select>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }
}
