/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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
public class HTMLSelectElementTest extends WebDriverTestCase {

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

        loadPageWithAlerts2(html);
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
            + "<form name='form1' action='/foo' method='get'>\n"
            + "    <select name='select1'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
            + "        <option value='option2' name='option2' selected>Two</option>\n"
            + "        <option value='option3' name='option3'>Three</option>\n"
            + "    </select>\n"
            + "    <input type='submit' id='clickMe' name='submit' value='button'>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");

        final WebDriver webdriver = loadPageWithAlerts2(html);
        webdriver.findElement(By.id("clickMe")).click();

        assertEquals(getDefaultUrl() + "foo?submit=button", webdriver.getCurrentUrl());
        assertSame("method", HttpMethod.GET, getMockWebConnection().getLastMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void testSelectedIndex2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var oSelect = document.getElementById('main');\n"
            + "    var oOption = new Option('bla', 1);\n"
            + "    oSelect.options[oSelect.options.length] = oOption;\n"
            + "    oOption.selected = false;\n"
            + "    alert(oSelect.selectedIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form action=''>\n"
            + "  <select id='main'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "-1", "2", "-1", "-1" },
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
    }

    /**
     * Test for bug 1570478.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = "exception",
            DEFAULT = { "4", "Four", "value4", "Three b", "value3b" })
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

        loadPageWithAlerts2(html);
    }

    /**
     * Test for bug 3319397.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "0", "exception" },
            FF = { "0", "test", "testValue" })
    public void testAddOptionTooEmptySelectWithAddMethod_IE() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var oSelect = document.form1.select1;\n"
            + "  try {\n"
            + "    alert(oSelect.length);\n"
            + "    oSelect.add(new Option('test', 'testValue'), null);\n"
            + "    alert(oSelect[oSelect.length-1].text);\n"
            + "    alert(oSelect[oSelect.length-1].value);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 1304741.
     * See https://sourceforge.net/tracker/index.php?func=detail&aid=1304741&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = { "0", "exception" },
            DEFAULT = { "0", "1" })
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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
            + "    var select = document.form1.select1;\n"
            + "    select.remove(1);\n"
            + "    alert(select.length);\n"
            + "    alert(select[1].text);\n"
            + "    alert(select[1].value);\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * Method remove on the options collection exists only for IE.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = "exception",
            DEFAULT = { "2", "Three", "value3" })
    public void optionsRemoveMethod() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var options = document.form1.select1.options;\n"
            + "  try {\n"
            + "    options.remove(1);\n"
            + "    alert(options.length);\n"
            + "    alert(options[1].text);\n"
            + "    alert(options[1].value);\n"
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
    }

    /**
     * Test that options delegates to select (bug 1111597).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = { "2-2", "1-1", "2-2", "0-0", "2-2", "1-1" })
    public void testOptionsDelegateToSelect() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var s = document.getElementById('select1');\n"
            + "    doAlerts(s);\n"
            + "\n"
            + "    s.selectedIndex = 0;\n"
            + "    doAlerts(s);\n"
            + "\n"
            + "    s.options.selectedIndex = 1;\n"
            + "    doAlerts(s);\n"
            + "  } catch (e) { alert('exception') }\n"
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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
        testDefaultSelectedValue("2", false, "2", "false", "false", "false", "-1");
        testDefaultSelectedValue("2", true, "2", "false", "false", "false", "-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDefaultSelectedValue_SizeInvalid() throws Exception {
        testDefaultSelectedValue("x", false, "0", "true", "false", "false", "0");
        testDefaultSelectedValue("x", true, "0", "false", "false", "false", "-1");
    }

    /**
     * Tests default selection status for options in a select of the specified size, optionally
     * allowing multiple selections.
     * @param size the select input's size attribute
     * @param multiple whether or not the select input should allow multiple selections
     * @param expected the expected alerts
     * @throws Exception if the test fails
     */
    private void testDefaultSelectedValue(final String size, final boolean multiple, final String... expected)
        throws Exception {

        setExpectedAlerts(expected);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void deselectMultiple() throws Exception {
        final String html = "<html><body>\n"
            + "<form name='f'>\n"
            + "    <select name='s1' multiple>\n"
            + "        <option name='option1'>One</option>\n"
            + "        <option id='it' name='option2' selected='true'>Two</option>\n"
            + "        <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver webdriver = loadPageWithAlerts2(html);
        final WebElement firstOption = webdriver.findElement(By.id("it"));
        assertTrue(firstOption.isSelected());
        firstOption.click();
        assertFalse(firstOption.isSelected());
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "first", "null", "null" }, IE = { "first", "null", "exception" })
    public void item() throws Exception {
        final String html =
            "<html><head>\n"
            + "<body>\n"
            + "<select id='mySelect'>\n"
            + "  <option>first</option>\n"
            + "  <option>second</option>\n"
            + "</select>\n"
            + "<script>\n"
            + "var s = document.getElementById('mySelect');\n"
            + "alert(s.item(0).text);\n"
            + "alert(s.item(300));\n"
            + "try { alert(s.item(-5)); } catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "two", "" }, FF = { "two", "two" })
    public void value() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.value);\n"
            + "    select.value = 'three';\n"
            + "    alert(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option value='one'>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "two", "" }, FF = { "two", "One" })
    public void value2() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.value);\n"
            + "    select.value = 'One';\n"
            + "    alert(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
