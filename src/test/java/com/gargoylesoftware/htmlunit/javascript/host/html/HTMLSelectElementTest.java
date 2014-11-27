/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
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
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLSelectElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "1", "3", "0" })
    public void getSelectedIndex() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      alert(document.form1.select1.length);\n"
            + "      alert(document.form1.select1.selectedIndex);\n"

            + "      alert(document.form1.selectMulti.length);\n"
            + "      alert(document.form1.selectMulti.selectedIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1'>One</option>\n"
            + "      <option name='option2' selected>Two</option>\n"
            + "      <option name='option3'>Three</option>\n"
            + "    </select>\n"

            + "    <select name='selectMulti' multiple>\n"
            + "      <option name='option1' selected>One</option>\n"
            + "      <option name='option2'>Two</option>\n"
            + "      <option name='option3' selected>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "0", "3", "-1" })
    public void getSelectedIndexNothingSelected() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      alert(document.form1.select1.length);\n"
            + "      alert(document.form1.select1.selectedIndex);\n"

            + "      alert(document.form1.selectMulti.length);\n"
            + "      alert(document.form1.selectMulti.selectedIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1'>One</option>\n"
            + "      <option name='option2'>Two</option>\n"
            + "      <option name='option3'>Three</option>\n"
            + "    </select>\n"

            + "    <select name='selectMulti' multiple>\n"
            + "      <option name='option1'>One</option>\n"
            + "      <option name='option2'>Two</option>\n"
            + "      <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "-1", "0", "-1" })
    public void getSelectedIndexNoOption() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      alert(document.form1.select1.length);\n"
            + "      alert(document.form1.select1.selectedIndex);\n"

            + "      alert(document.form1.selectMulti.length);\n"
            + "      alert(document.form1.selectMulti.selectedIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "    </select>\n"
            + "    <select name='selectMulti' multiple>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "1", "3", "2" })
    public void setSelectedIndex() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      alert(document.form1.select1.length);\n"
            + "      alert(document.form1.select1.selectedIndex);\n"

            + "      document.form1.select1.selectedIndex = 2;\n"
            + "      alert(document.form1.select1.length);\n"
            + "      alert(document.form1.select1.selectedIndex);\n"
            + "      document.form1.select1.selectedIndex = -1;\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1' action='/foo' method='get'>\n"
            + "    <select name='select1'>\n"
            + "      <option value='option1' name='option1'>One</option>\n"
            + "      <option value='option2' name='option2' selected>Two</option>\n"
            + "      <option value='option3' name='option3'>Three</option>\n"
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
    public void selectedIndex2() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      var oSelect = document.getElementById('main');\n"
            + "      var oOption = new Option('bla', 1);\n"
            + "      oSelect.options[oSelect.options.length] = oOption;\n"
            + "      oOption.selected = false;\n"
            + "      alert(oSelect.selectedIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form action=''>\n"
            + "    <select id='main'/>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-1", "2", "-1", "-1" },
            IE = { "-1", "2", "-1", "-1" })
    public void setSelectedIndexInvalidValue() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      var s = document.form1.select1;\n"
            + "      s.selectedIndex = -1;\n"
            + "      alert(s.selectedIndex);\n"

            + "      s.selectedIndex = 2;\n"
            + "      alert(s.selectedIndex);\n"

            + "      try { s.selectedIndex = 25; } catch (e) { alert('exception') }\n"
            + "      alert(s.selectedIndex);\n"

            + "      try { s.selectedIndex = -14; } catch (e) { alert('exception') }\n"
            + "      alert(s.selectedIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1' action='http://test' method='get'>\n"
            + "    <select name='select1'>\n"
            + "      <option value='option1' name='option1'>One</option>\n"
            + "      <option value='option2' name='option2' selected>Two</option>\n"
            + "      <option value='option3' name='option3'>Three</option>\n"
            + "    </select>\n"
            + "    <input type='submit' id='clickMe' name='submit' value='button'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "value1", "One", "value2", "Two", "value3", "Three" })
    public void getOptions() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      var options = document.form1.select1.options;\n"
            + "      alert(options.length);\n"
            + "      for (i=0; i<options.length; i++) {\n"
            + "        alert(options[i].value);\n"
            + "        alert(options[i].text);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "      <option name='option2' value='value2' selected>Two</option>\n"
            + "      <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "value1", "One", "value2", "Two", "value3", "Three" })
    public void getOptionLabel() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      var options = document.form1.select1.options;\n"
            + "      alert(options.length);\n"
            + "      for (i=0; i<options.length; i++) {\n"
            + "        alert(options[i].value);\n"
            + "        alert(options[i].text);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1' label='OneLabel'>One</option>\n"
            + "      <option name='option2' value='value2' label='TwoLabel' selected>Two</option>\n"
            + "      <option name='option3' value='value3' label='ThreeLabel'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "true", "true", "false" })
    public void getOptionSelected() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      var options = document.form1.select1.options;\n"
            + "      alert(options[0].selected);\n"
            + "      alert(options[1].selected);\n"
            + "      options[0].selected = true;\n"
            + "      alert(options[0].selected);\n"
            + "      alert(options[1].selected);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "      <option name='option2' value='value2' selected>Two</option>\n"
            + "      <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getOptionByIndex() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      var option1 = document.f1.elements['select'][0];\n"
            + "      alert(option1!=null);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='f1' action='xxx.html'>\n"
            + "    <SELECT name='select'>\n"
            + "      <OPTION value='A'>111</OPTION>\n"
            + "      <OPTION value='B'>222</OPTION>\n"
            + "    </SELECT>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("One")
    public void getOptionByOptionIndex() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      var option1 = document.form1.select1.options[0];\n"
            + "      alert(option1.text);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "      <option name='option2' value='value2' selected>Two</option>\n"
            + "      <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "4", "Four", "value4" })
    public void addOption() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      var options = document.form1.select1.options;\n"
            + "      var index = options.length;\n"
            + "      options[index] = new Option('Four','value4');\n"
            + "      alert(options.length);\n"
            + "      alert(options[index].text);\n"
            + "      alert(options[index].value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "      <option name='option2' value='value2' selected>Two</option>\n"
            + "      <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "true", "4", "Four", "value4", "true", "3", "false" })
    public void addOptionSelected() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      var oSelect = document.form1.select1;\n"
            + "      var options = oSelect.options;\n"
            + "      var firstSelectedIndex = oSelect.selectedIndex;\n"
            + "      alert(firstSelectedIndex);\n"
            + "      alert(options[firstSelectedIndex].selected);\n"

            + "      var index = options.length;\n"
            + "      var oOption = new Option('Four','value4');\n"
            + "      oOption.selected = true;\n"
            + "      options[index] = oOption;\n"

            + "      alert(options.length);\n"
            + "      alert(options[index].text);\n"
            + "      alert(options[index].value);\n"
            + "      alert(options[index].selected);\n"
            + "      alert(oSelect.selectedIndex);\n"
            + "      alert(options[firstSelectedIndex].selected);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "      <option name='option2' value='value2' selected>Two</option>\n"
            + "      <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "4", "Four", "value4" },
            IE8 = { "exception", "3", "Three", "value3" })
    public void addOptionWithAddMethodIndexNull() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      var options = document.form1.select1;\n"
            + "      try {\n"
            + "        options.add(new Option('Four','value4'), null);\n"
            + "      } catch(e) { alert('exception'); }\n"
            + "      alert(options.length);\n"
            + "      var index = options.length - 1;\n"
            + "      alert(options[index].text);\n"
            + "      alert(options[index].value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "      <option name='option2' value='value2' selected>Two</option>\n"
            + "      <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for bug 1570478.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "4", "Four", "value4", "Three b", "value3b" })
    public void addOptionWithAddMethodNoSecondParameter() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      var oSelect = document.form1.select1;\n"
            + "      try {\n"
            + "        oSelect.add(new Option('Four', 'value4'));\n"
            + "        alert(oSelect.length);\n"
            + "        alert(oSelect[oSelect.length-1].text);\n"
            + "        alert(oSelect[oSelect.length-1].value);\n"

            + "        oSelect.add(new Option('Three b', 'value3b'), 3);\n"
            + "        alert(oSelect[3].text);\n"
            + "        alert(oSelect[3].value);\n"
            + "      } catch(e) { alert('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "      <option name='option2' value='value2' selected>Two</option>\n"
            + "      <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for bug 3319397.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "test", "testValue" },
            IE8 = { "0", "exception" })
    public void addOptionTooEmptySelectWithAddMethodIndexNull() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      var oSelect = document.form1.select1;\n"
            + "      try {\n"
            + "        alert(oSelect.length);\n"
            + "        oSelect.add(new Option('test', 'testValue'), null);\n"
            + "        alert(oSelect[oSelect.length-1].text);\n"
            + "        alert(oSelect[oSelect.length-1].value);\n"
            + "      } catch(e) { alert('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "0", "foo*" })
    public void addOptionMethodIndexMinusOneEmptySelect() throws Exception {
        addOptionMethod(", -1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "-1", "foo" })
    public void addOptionMethodIndexMinusOneEmptySelectMulti() throws Exception {
        addOptionMethod(", -1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "0", "foo*" })
    public void addOptionMethodIndexZeroEmptySelect() throws Exception {
        addOptionMethod(", 0", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "-1", "foo" })
    public void addOptionMethodIndexZeroEmptySelectMulti() throws Exception {
        addOptionMethod(", 0", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "0", "foo*" })
    public void addOptionMethodIndexOneEmptySelect() throws Exception {
        addOptionMethod(", 1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "-1", "foo" })
    public void addOptionMethodIndexOneEmptySelectMulti() throws Exception {
        addOptionMethod(", 1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "0", "foo*" })
    public void addOptionMethodIndexFourEmptySelect() throws Exception {
        addOptionMethod(", 4", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1", "-1", "foo" })
    public void addOptionMethodIndexFourEmptySelectMulti() throws Exception {
        addOptionMethod(", 4", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "1", "One", "Two*", "Three", "foo" })
    public void addOptionMethodIndexMinusOne() throws Exception {
        addOptionMethod(", -1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "1", "One", "Two*", "Three*", "foo" })
    public void addOptionMethodIndexMinusOneMulti() throws Exception {
        addOptionMethod(", -1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "2", "foo", "One", "Two*", "Three" })
    public void addOptionMethodIndexZero() throws Exception {
        addOptionMethod(", 0", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "2", "foo", "One", "Two*", "Three*" })
    public void addOptionMethodIndexZeroMulti() throws Exception {
        addOptionMethod(", 0", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "2", "One", "foo", "Two*", "Three" })
    public void addOptionMethodIndexOne() throws Exception {
        addOptionMethod(", 1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "2", "One", "foo", "Two*", "Three*" })
    public void addOptionMethodIndexOneMulti() throws Exception {
        addOptionMethod(", 1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "1", "One", "Two*", "foo", "Three" })
    public void addOptionMethodhIndexTwo() throws Exception {
        addOptionMethod(", 2", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "1", "One", "Two*", "foo", "Three*" })
    public void addOptionMethodhIndexTwoMulti() throws Exception {
        addOptionMethod(", 2", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "1", "One", "Two*", "Three", "foo" })
    public void addOptionMethodIndexThree() throws Exception {
        addOptionMethod(", 3", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "1", "One", "Two*", "Three*", "foo" })
    public void addOptionMethodIndexThreeMulti() throws Exception {
        addOptionMethod(", 3", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "1", "One", "Two*", "Three", "foo" })
    public void addOptionMethodIndexFour() throws Exception {
        addOptionMethod(", 4", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "4", "1", "One", "Two*", "Three*", "foo" })
    public void addOptionMethodIndexFourMulti() throws Exception {
        addOptionMethod(", 4", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "1", "0", "foo*" },
            IE8 = { "0", "exception" })
    public void addOptionMethodOptionNullEmptySelect() throws Exception {
        addOptionMethod(", null", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "1", "-1", "foo" },
            IE8 = { "0", "exception" })
    public void addOptionMethodOptionNullEmptySelectMulti() throws Exception {
        addOptionMethod(", null", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "exception" })
    public void addOptionMethodNewOptionEmptySelect() throws Exception {
        addOptionMethod(", new Option('foo', '123')", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "exception" })
    public void addOptionMethodNewOptionEmptySelectMulti() throws Exception {
        addOptionMethod(", new Option('foo', '123')", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "1", "One", "Two*", "Three", "foo" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionNull() throws Exception {
        addOptionMethod(", null", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "1", "One", "Two*", "Three*", "foo" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionNullMulti() throws Exception {
        addOptionMethod(", null", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "exception" })
    public void addOptionMethodNewOption() throws Exception {
        addOptionMethod(", new Option('foo', '123')", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "exception" })
    public void addOptionMethodNewOptionMulti() throws Exception {
        addOptionMethod(", new Option('foo', '123')", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "2", "foo", "One", "Two*", "Three" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionFirst() throws Exception {
        addOptionMethod(", oSelect.options[0]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "2", "foo", "One", "Two*", "Three*" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionFirstMulti() throws Exception {
        addOptionMethod(", oSelect.options[0]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "2", "One", "foo", "Two*", "Three" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionSecond() throws Exception {
        addOptionMethod(", oSelect.options[1]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "2", "One", "foo", "Two*", "Three*" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionSecondMulti() throws Exception {
        addOptionMethod(", oSelect.options[1]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "1", "One", "Two*", "foo", "Three" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionThird() throws Exception {
        addOptionMethod(", oSelect.options[2]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "1", "One", "Two*", "foo", "Three*" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionThirdMulti() throws Exception {
        addOptionMethod(", oSelect.options[2]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "1", "One", "Two*", "Three", "foo" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionLast() throws Exception {
        addOptionMethod(", oSelect.options[3]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "4", "1", "One", "Two*", "Three*", "foo" },
            IE8 = { "3", "exception" })
    public void addOptionMethodOptionLastMulti() throws Exception {
        addOptionMethod(", oSelect.options[3]", false, true);
    }

    private void addOptionMethod(final String param, final boolean empty, final boolean multi) throws Exception {
        String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.select1;\n"
            + "        alert(oSelect.length);\n"
            + "        var opt = new Option('foo', '123');\n"
            + "        oSelect.add(opt" + param + ");\n"

            + "        alert(oSelect.length);\n"
            + "        alert(oSelect.selectedIndex);\n"
            + "        for (i=0; i<oSelect.options.length; i++) {\n"
            + "          alert(oSelect.options[i].text + (oSelect.options[i].selected ? '*' : ''));\n"
            + "        }\n"
            + "      } catch (e) { alert('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <select name='select1' " + (multi ? "multiple" : "") + ">\n";
        if (!empty) {
            html = html
                    + "      <option name='option1' value='value1'>One</option>\n"
                    + "      <option name='option2' value='value2' selected>Two</option>\n"
                    + "      <option name='option3' value='value3'" + (multi ? "selected" : "") + ">Three</option>\n";
        }
        html = html
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 308.
     * See http://sourceforge.net/p/htmlunit/bugs/308/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "1" })
    public void addWithIndexEmptySelect() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.testSelect;\n"
            + "        alert(oSelect.length);\n"
            + "        var opt = new Option('foo', '123');\n"
            + "        oSelect.add(opt, -1);\n"
            + "        alert(oSelect.length);\n"
            + "      } catch (e) { alert('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <select name='testSelect'></select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "0", "-1" },
            IE = { "0", "exception" })
    public void removeOptionMethodIndexMinusOneEmptySelect() throws Exception {
        removeOptionMethod("-1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "0", "-1" },
            IE = { "0", "exception" })
    public void removeOptionMethodIndexMinusOneEmptySelectMulti() throws Exception {
        removeOptionMethod("-1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodIndexZeroEmptySelect() throws Exception {
        removeOptionMethod("0", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodIndexZeroEmptySelectMulti() throws Exception {
        removeOptionMethod("0", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodIndexOneEmptySelect() throws Exception {
        removeOptionMethod("1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodIndexOneEmptySelectMulti() throws Exception {
        removeOptionMethod("1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodIndexFourEmptySelect() throws Exception {
        removeOptionMethod("4", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodIndexFourEmptySelectMulti() throws Exception {
        removeOptionMethod("4", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "3", "1", "One", "Two*", "Three" },
            IE = { "3", "exception" })
    public void removeOptionMethodIndexMinusOne() throws Exception {
        removeOptionMethod("-1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "3", "1", "One", "Two*", "Three*" },
            IE = { "3", "exception" })
    public void removeOptionMethodIndexMinusOneMulti() throws Exception {
        removeOptionMethod("-1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three" })
    public void removeOptionMethodIndexZero() throws Exception {
        removeOptionMethod("0", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three*" })
    public void removeOptionMethodIndexZeroMulti() throws Exception {
        removeOptionMethod("0", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "One*", "Three" })
    public void removeOptionMethodIndexOne() throws Exception {
        removeOptionMethod("1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "1", "One", "Three*" })
    public void removeOptionMethodIndexOneMulti() throws Exception {
        removeOptionMethod("1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "1", "One", "Two*" })
    public void removeOptionMethodhIndexTwo() throws Exception {
        removeOptionMethod("2", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "1", "One", "Two*" })
    public void removeOptionMethodhIndexTwoMulti() throws Exception {
        removeOptionMethod("2", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "3", "1", "One", "Two*", "Three" })
    public void removeOptionMethodIndexThree() throws Exception {
        removeOptionMethod("3", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "3", "1", "One", "Two*", "Three*" })
    public void removeOptionMethodIndexThreeMulti() throws Exception {
        removeOptionMethod("3", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "3", "1", "One", "Two*", "Three" })
    public void removeOptionMethodIndexFour() throws Exception {
        removeOptionMethod("4", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "3", "1", "One", "Two*", "Three*" })
    public void removeOptionMethodIndexFourMulti() throws Exception {
        removeOptionMethod("4", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodOptionNullEmptySelect() throws Exception {
        removeOptionMethod("null", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodOptionNullEmptySelectMulti() throws Exception {
        removeOptionMethod("null", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodNewOptionEmptySelect() throws Exception {
        removeOptionMethod("new Option('foo', '123')", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "-1" })
    public void removeOptionMethodNewOptionEmptySelectMulti() throws Exception {
        removeOptionMethod("new Option('foo', '123')", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three" })
    public void removeOptionMethodOptionNull() throws Exception {
        removeOptionMethod("null", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three*" })
    public void removeOptionMethodOptionNullMulti() throws Exception {
        removeOptionMethod("null", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three" })
    public void removeOptionMethodNewOption() throws Exception {
        removeOptionMethod("new Option('foo', '123')", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three*" })
    public void removeOptionMethodNewOptionMulti() throws Exception {
        removeOptionMethod("new Option('foo', '123')", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three" })
    public void removeOptionMethodOptionFirst() throws Exception {
        removeOptionMethod("oSelect.options[0]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three*" })
    public void removeOptionMethodOptionFirstMulti() throws Exception {
        removeOptionMethod("oSelect.options[0]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three" })
    public void removeOptionMethodOptionSecond() throws Exception {
        removeOptionMethod("oSelect.options[1]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three*" })
    public void removeOptionMethodOptionSecondMulti() throws Exception {
        removeOptionMethod("oSelect.options[1]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three" })
    public void removeOptionMethodOptionThird() throws Exception {
        removeOptionMethod("oSelect.options[2]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three*" })
    public void removeOptionMethodOptionThirdMulti() throws Exception {
        removeOptionMethod("oSelect.options[2]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three" })
    public void removeOptionMethodOptionLast() throws Exception {
        removeOptionMethod("oSelect.options[3]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "2", "0", "Two*", "Three*" })
    public void removeOptionMethodOptionLastMulti() throws Exception {
        removeOptionMethod("oSelect.options[3]", false, true);
    }

    private void removeOptionMethod(final String param, final boolean empty, final boolean multi) throws Exception {
        String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest(){\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.select1;\n"
            + "        alert(oSelect.length);\n"
            + "        oSelect.remove(" + param + ");\n"

            + "        alert(oSelect.length);\n"
            + "        alert(oSelect.selectedIndex);\n"
            + "        for (i=0; i<oSelect.options.length; i++) {\n"
            + "          alert(oSelect.options[i].text + (oSelect.options[i].selected ? '*' : ''));\n"
            + "        }\n"
            + "      } catch (e) { alert('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <select name='select1' " + (multi ? "multiple" : "") + ">\n";
        if (!empty) {
            html = html
                    + "      <option name='option1' value='value1'>One</option>\n"
                    + "      <option name='option2' value='value2' selected>Two</option>\n"
                    + "      <option name='option3' value='value3'" + (multi ? "selected" : "") + ">Three</option>\n";
        }
        html = html
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "Three", "value3" })
    public void removeOption() throws Exception {
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
    public void removeOptionWithRemoveMethod() throws Exception {
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
    @Alerts({ "2", "Three", "value3" })
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
    public void clearOptions() throws Exception {
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
    public void increaseOptionsSettingLength() throws Exception {
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
    public void optionArrayHasItemMethod() throws Exception {
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
    @Alerts(DEFAULT = { "Two", "", "Two", "", "" },
            IE8 = { "", "", "", "", "" })
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
    public void setValue() throws Exception {
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
    @Alerts(DEFAULT = "exception",
            IE = { "2-2", "1-1", "2-2", "0-0", "2-2", "1-1" })
    public void optionsDelegateToSelect() throws Exception {
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
    public void optionsArrayAdd() throws Exception {
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
    public void selectedIndex() throws Exception {
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
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'><option>hello</option></select>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "true", "false", "false", "0",
                        "0", "false", "false", "false", "-1" },
            IE8 = { "0", "true", "false", "false", "0",
                    "0", "true", "false", "false", "0" })
    @NotYetImplemented(IE8)
    public void defaultSelectedValue_SizeNegativeOne() throws Exception {
        final String[] expected = getExpectedAlerts();
        defaultSelectedValue("-1", false, Arrays.copyOf(expected, 5));
        defaultSelectedValue("-1", true, Arrays.copyOfRange(expected, 5, 10));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts (DEFAULT = { "0", "true", "false", "false", "0",
                         "0", "false", "false", "false", "-1" },
            IE8 = { "0", "true", "false", "false", "0",
                    "0", "true", "false", "false", "0" })
    @NotYetImplemented(IE8)
    public void defaultSelectedValue_SizeZero() throws Exception {
        final String[] expected = getExpectedAlerts();
        defaultSelectedValue("0", false, Arrays.copyOf(expected, 5));
        defaultSelectedValue("0", true, Arrays.copyOfRange(expected, 5, 10));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "true", "false", "false", "0",
                        "1", "false", "false", "false", "-1" },
            IE8 = { "1", "true", "false", "false", "0",
                    "1", "true", "false", "false", "0" })
    @NotYetImplemented(IE8)
    public void defaultSelectedValue_SizeOne() throws Exception {
        final String[] expected = getExpectedAlerts();
        defaultSelectedValue("1", false, Arrays.copyOf(expected, 5));
        defaultSelectedValue("1", true, Arrays.copyOfRange(expected, 5, 10));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void defaultSelectedValue_SizeTwo() throws Exception {
        defaultSelectedValue("2", false, "2", "false", "false", "false", "-1");
        defaultSelectedValue("2", true, "2", "false", "false", "false", "-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "true", "false", "false", "0",
                        "0", "false", "false", "false", "-1" },
            IE8 = { "0", "true", "false", "false", "0",
                    "0", "true", "false", "false", "0" })
    @NotYetImplemented(IE8)
    public void defaultSelectedValue_SizeInvalid() throws Exception {
        final String[] expected = getExpectedAlerts();
        defaultSelectedValue("x", false, Arrays.copyOf(expected, 5));
        defaultSelectedValue("x", true, Arrays.copyOfRange(expected, 5, 10));
    }

    /**
     * Tests default selection status for options in a select of the specified size, optionally
     * allowing multiple selections.
     * @param size the select input's size attribute
     * @param multiple whether or not the select input should allow multiple selections
     * @param expected the expected alerts
     * @throws Exception if the test fails
     */
    private void defaultSelectedValue(final String size, final boolean multiple, final String... expected)
        throws Exception {

        setExpectedAlerts(expected);
        final String html =
            "<html>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + "   function test(){\n"
            + "      alert(document.getElementById('s').size);\n"
            + "      alert(document.getElementById('a').selected);\n"
            + "      alert(document.getElementById('b').selected);\n"
            + "      alert(document.getElementById('c').selected);\n"
            + "      alert(document.getElementById('s').selectedIndex);\n"
            + "   }\n"
            + "</script>\n"
            + "<form id='f'>\n"
            + "   <select id='s' size='" + size + "'" + (multiple ? " multiple" : "") + ">\n"
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
    public void size() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.size + 5);//to test if int or string\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "false", "false" })
    public void multiple() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.f['s1'].multiple);\n"
            + "    alert(document.f['s2'].multiple);\n"
            + "    document.f['s1'].multiple = false;\n"
            + "    alert(document.f['s1'].multiple);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form name='f'>\n"
            + "    <select name='s1' multiple>\n"
            + "      <option name='option1'>One</option>\n"
            + "      <option name='option2'>Two</option>\n"
            + "      <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "    <select name='s2'>\n"
            + "      <option name='option4'>Four</option>\n"
            + "      <option name='option5'>Five</option>\n"
            + "      <option name='option6'>Six</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void deselectMultiple() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <form name='f'>\n"
            + "    <select name='s1' multiple>\n"
            + "      <option name='option1'>One</option>\n"
            + "      <option id='it' name='option2' selected='true'>Two</option>\n"
            + "      <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
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
    @Alerts({ "0", "1" })
    public void selectedIndex_onfocus() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    alert(s.selectedIndex);\n"
            + "    s.selectedIndex = 1;\n"
            + "    alert(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect' onfocus='alert(\"select-focus\")'>\n"
            + "    <option value='o1'>hello</option>\n"
            + "    <option value='o2'>there</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "o1", "o2" })
    public void value_onfocus() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    alert(s.value);\n"
            + "    s.value = 'o2';\n"
            + "    alert(s.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect' onfocus='alert(\"select-focus\")'>\n"
            + "    <option value='o1'>hello</option>\n"
            + "    <option value='o2'>there</option>\n"
            + "  </select>\n"
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
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'></select>\n"
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
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'></select>\n"
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
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'></select>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "first", "null", "null" },
            IE8 = { "first", "null", "exception" })
    public void item() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>first</option>\n"
            + "    <option>second</option>\n"
            + "  </select>\n"

            + "  <script>\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    alert(s.item(0).text);\n"
            + "    alert(s.item(300));\n"
            + "    try { alert(s.item(-5)); } catch(e) { alert('exception'); }\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "two", "" },
            FF = { "two", "two" })
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
            + "</head>\n"
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
    @Alerts({ "two", "one" })
    public void valueByValue() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.value);\n"
            + "    select.value = 'one';\n"
            + "    alert(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option value='one'>1</option>\n"
            + "    <option selected value='two'>2</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "two", "" },
            FF = { "two", "two" })
    public void valueByValueCase() throws Exception {
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
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option value='one'>1</option>\n"
            + "    <option selected value='two'>2</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "two", "One" },
            IE = { "two", "" })
    public void valueByText() throws Exception {
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
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "two", "One" },
            IE = { "two", "" })
    public void valueByTextTrim() throws Exception {
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
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option> One </option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "two", "" },
            FF = { "two", "two" })
    public void valueNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.value);\n"
            + "    select.value = null;\n"
            + "    alert(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "One", "Two", "One" })
    public void valueAfterReset() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    alert(select.value);\n"
            + "    select.options[1].selected = true;\n"
            + "    alert(select.value);\n"
            + "    form.reset();\n"
            + "    alert(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form id='myForm' name='myForm'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option value='One'>One</option>\n"
            + "    <option value='Two'>Two</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
