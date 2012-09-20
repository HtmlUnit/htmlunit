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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

/**
 * Tests for {@link HTMLOptionElement}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLOptionElementTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "some text", "some value", "false", "some other text", "some other value", "true" })
    public void readPropsBeforeAdding() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var oOption = new Option('some text', 'some value');\n"
            + "    alert(oOption.text);\n"
            + "    alert(oOption.value);\n"
            + "    alert(oOption.selected);\n"
            + "    oOption.text = 'some other text';\n"
            + "    oOption.value = 'some other value';\n"
            + "    oOption.selected = true;\n"
            + "    alert(oOption.text);\n"
            + "    alert(oOption.value);\n"
            + "    alert(oOption.selected);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 1323425.
     * See http://sourceforge.net/tracker/index.php?func=detail&aid=1323425&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void selectingOrphanedOptionCreatedByDocument() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<form name='myform'/>\n"
            + "<script language='javascript'>\n"
            + "var select = document.createElement('select');\n"
            + "var opt = document.createElement('option');\n"
            + "opt.value = 'x';\n"
            + "opt.selected = true;\n"
            + "select.appendChild(opt);\n"
            + "document.myform.appendChild(select);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Regression test for 1592728.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "2" })
    public void setSelected() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var sel = document.form1.select1;\n"
            + "  alert(sel.selectedIndex);\n"
            + "  sel.options[0].selected = false;\n"
            + "  alert(sel.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' onchange='this.form.submit()'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
            + "        <option value='option2' name='option2'>Two</option>\n"
            + "        <option value='option3' name='option3' selected>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for 1672048.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttribute() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  document.getElementById('option1').setAttribute('class', 'bla bla');\n"
            + "  var o = new Option('some text', 'some value');\n"
            + "  o.setAttribute('class', 'myClass');\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option value='option1' id='option1' name='option1'>One</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "undefined", "caught exception for negative index" })
    public void optionIndexOutOfBound() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var options = document.getElementById('testSelect').options;\n"
            + "  alert(options[55]);\n"
            + "  try\n"
            + "  {\n"
            + "    alert(options[-55]);\n"
            + "  }\n"
            + "  catch (e)\n"
            + "  {\n"
            + "    alert('caught exception for negative index');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "o2: text: Option 2, label: , value: 2, defaultSelected: false, selected: false",
        "o3: text: Option 3, label: , value: 3, defaultSelected: true, selected: false",
        "0", "1" })
    public void constructor() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function dumpOption(_o) {\n"
            + "  return 'text: ' + _o.text\n"
            + " + ', label: ' + _o.label\n"
            + " + ', value: ' + _o.value\n"
            + " + ', defaultSelected: ' + _o.defaultSelected\n"
            + " + ', selected: ' + _o.selected;\n"
            + "}\n"
            + "function doTest() {\n"
            + "   var o2 = new Option('Option 2', '2');\n"
            + "   alert('o2: ' + dumpOption(o2));\n"
            + "   var o3 = new Option('Option 3', '3', true, false);\n"
            + "   alert('o3: ' + dumpOption(o3));\n"
            + "   document.form1.select1.appendChild(o3);\n"
            + "   alert(document.form1.select1.options.selectedIndex);\n"
            + "   document.form1.reset();\n"
            + "   alert(document.form1.select1.options.selectedIndex);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
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
    public void insideBold() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var sel = document.form1.select1;\n"
            + "  sel.options[0] = null;\n"
            + "  alert(sel.options.length);\n"
            + "}</script></head><body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "  <b>\n"
            + "    <select name='select1'>\n"
            + "        <option>One</option>\n"
            + "    </select>\n"
            + "  </b>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "[object]", "[object]", "[object]", "[object]", "null",
            "[object]", "null", "[object]", "[object]", "null" },
            FF = { "null", "[object Attr]", "null", "null", "null",
            "null", "null", "null", "null", "null" })
    public void getAttributeNode() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  var o1 = s.options[0];\n"
            + "  alert(o1.getAttributeNode('id'))\n"
            + "  alert(o1.getAttributeNode('name'))\n"
            + "  alert(o1.getAttributeNode('value'))\n"
            + "  alert(o1.getAttributeNode('selected'))\n"
            + "  alert(o1.getAttributeNode('foo'))\n"
            + "  var o2 = s.options[1];\n"
            + "  alert(o2.getAttributeNode('id'))\n"
            + "  alert(o2.getAttributeNode('name'))\n"
            + "  alert(o2.getAttributeNode('value'))\n"
            + "  alert(o2.getAttributeNode('selected'))\n"
            + "  alert(o2.getAttributeNode('foo'))\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option name='option1'>One</option>\n"
            + "        <option>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @NotYetImplemented
    @Alerts("1")
    public void without_new() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  s.options[0] = Option('one', 'two');\n"
            + "  alert(s.length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "  <select id='testSelect'>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "value1", "text1", "label1", "value2", "text2", "" })
    public void label() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  var lastIndex = s.length;\n"
            + "  s.length += 1;\n"
            + "  s[lastIndex].value = 'value2';\n"
            + "  s[lastIndex].text  = 'text2';\n"
            + "  alert(s[0].value);\n"
            + "  alert(s[0].text);\n"
            + "  alert(s[0].label);\n"
            + "  alert(s[1].value);\n"
            + "  alert(s[1].text);\n"
            + "  alert(s[1].label);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select id='testSelect'>\n"
            + "    <option value='value1' label='label1'>text1</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlSelect select = page.getHtmlElementById("testSelect");
        assertEquals("value1", select.getOption(0).getValueAttribute());
        assertEquals("text1", select.getOption(0).getTextContent());
        assertEquals("label1", select.getOption(0).getLabelAttribute());
        assertEquals("value2", select.getOption(1).getValueAttribute());
        assertEquals("text2", select.getOption(1).getTextContent());
        assertEquals("", select.getOption(1).getLabelAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "null", "[object]", "null" }, FF = { "[object Text]", "[object Text]", "[object Text]" })
    public void text() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  var lastIndex = s.length;\n"
            + "  s.length += 1;\n"
            + "  alert(s[lastIndex].firstChild);\n"
            + "  s[lastIndex].text  = 'text2';\n"
            + "  alert(s[lastIndex].firstChild);\n"
            + "  s[lastIndex].text  = '';\n"
            + "  alert(s[lastIndex].firstChild);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select id='testSelect'>\n"
            + "    <option value='value1' label='label1'>text1</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Visibility of elements should not impact the determination of the value of option
     * without value attribute or the text of options. This tests for one part a regression
     * introduced in rev. 4367 as well probably as a problem that exists since a long time.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "text1", "text1b", "text2" })
    public void text_when_not_displayed() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var s = document.getElementById('testSelect1');\n"
            + "  alert(s.options[0].text);\n"
            + "  alert(s.options[1].text);\n"
            + "  var s2 = document.getElementById('testSelect2');\n"
            + "  alert(s2.options[0].text);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div style='display: none'>\n"
            + "  <select id='testSelect1'>\n"
            + "    <option>text1</option>\n"
            + "    <option><strong>text1b</strong></option>\n"
            + "  </select>\n"
            + "  </div>\n"
            + "  <div style='visibility: hidden'>\n"
            + "  <select id='testSelect2'>\n"
            + "    <option>text2</option>\n"
            + "  </select>\n"
            + "  </div>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * For IE nested nodes aren't used as default value attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "text0", "text1", "text1b", "text2" },
            IE = { "", "", "", "" })
    public void defaultValueFromNestedNodes() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var s0 = document.getElementById('testSelect0');\n"
            + "  alert(s0.options[0].value);\n"
            + "  var s = document.getElementById('testSelect1');\n"
            + "  alert(s.options[0].value);\n"
            + "  alert(s.options[1].value);\n"
            + "  var s2 = document.getElementById('testSelect2');\n"
            + "  alert(s2.options[0].value);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select id='testSelect0'>\n"
            + "    <option>text0</option>\n"
            + "  </select>\n"
            + "  <div style='display: none'>\n"
            + "  <select id='testSelect1'>\n"
            + "    <option>text1</option>\n"
            + "    <option><strong>text1b</strong></option>\n"
            + "  </select>\n"
            + "  </div>\n"
            + "  <div style='visibility: hidden'>\n"
            + "  <select id='testSelect2'>\n"
            + "    <option>text2</option>\n"
            + "  </select>\n"
            + "  </div>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "[object]", FF = "[object HTMLFormElement]")
    public void form() throws Exception {
        final String html
            = "<html><body><form><select id='s'><option>a</option></select></form><script>\n"
            + "alert(document.getElementById('s').options[0].form);\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

}
