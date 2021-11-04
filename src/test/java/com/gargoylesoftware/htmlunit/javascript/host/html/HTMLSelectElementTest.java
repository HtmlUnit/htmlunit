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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLSelectElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
@RunWith(BrowserRunner.class)
public class HTMLSelectElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "1", "3", "0"})
    public void getSelectedIndex() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      log(document.form1.select1.length);\n"
            + "      log(document.form1.select1.selectedIndex);\n"

            + "      log(document.form1.selectMulti.length);\n"
            + "      log(document.form1.selectMulti.selectedIndex);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "0", "3", "-1"})
    public void getSelectedIndexNothingSelected() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      log(document.form1.select1.length);\n"
            + "      log(document.form1.select1.selectedIndex);\n"

            + "      log(document.form1.selectMulti.length);\n"
            + "      log(document.form1.selectMulti.selectedIndex);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "-1", "0", "-1"})
    public void getSelectedIndexNoOption() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      log(document.form1.select1.length);\n"
            + "      log(document.form1.select1.selectedIndex);\n"

            + "      log(document.form1.selectMulti.length);\n"
            + "      log(document.form1.selectMulti.selectedIndex);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "1", "3", "2"})
    public void setSelectedIndex() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      log(document.form1.select1.length);\n"
            + "      log(document.form1.select1.selectedIndex);\n"

            + "      document.form1.select1.selectedIndex = 2;\n"
            + "      log(document.form1.select1.length);\n"
            + "      log(document.form1.select1.selectedIndex);\n"
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

        final WebDriver webdriver = loadPageVerifyTitle2(html);
        webdriver.findElement(By.id("clickMe")).click();

        assertEquals(URL_FIRST + "foo?submit=button", webdriver.getCurrentUrl());
        assertSame("method", HttpMethod.GET, getMockWebConnection().getLastMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void selectedIndex2() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var oSelect = document.getElementById('main');\n"
            + "      var oOption = new Option('bla', 1);\n"
            + "      oSelect.options[oSelect.options.length] = oOption;\n"
            + "      oOption.selected = false;\n"
            + "      log(oSelect.selectedIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form action=''>\n"
            + "    <select id='main'/>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "2", "-1", "-1"})
    public void setSelectedIndexInvalidValue() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var s = document.form1.select1;\n"
            + "      s.selectedIndex = -1;\n"
            + "      log(s.selectedIndex);\n"

            + "      s.selectedIndex = 2;\n"
            + "      log(s.selectedIndex);\n"

            + "      try { s.selectedIndex = 25; } catch (e) { log('exception') }\n"
            + "      log(s.selectedIndex);\n"

            + "      try { s.selectedIndex = -14; } catch (e) { log('exception') }\n"
            + "      log(s.selectedIndex);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "value1", "One", "value2", "Two", "value3", "Three"})
    public void getOptions() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var options = document.form1.select1.options;\n"
            + "      log(options.length);\n"
            + "      for (var i = 0; i < options.length; i++) {\n"
            + "        log(options[i].value);\n"
            + "        log(options[i].text);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "value1", "One", "value2", "Two", "value3", "Three"})
    public void getOptionLabel() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var options = document.form1.select1.options;\n"
            + "      log(options.length);\n"
            + "      for (var i = 0; i < options.length; i++) {\n"
            + "        log(options[i].value);\n"
            + "        log(options[i].text);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "true", "false"})
    public void getOptionSelected() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var options = document.form1.select1.options;\n"
            + "      log(options[0].selected);\n"
            + "      log(options[1].selected);\n"
            + "      options[0].selected = true;\n"
            + "      log(options[0].selected);\n"
            + "      log(options[1].selected);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getOptionByIndex() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var option1 = document.f1.elements['select'][0];\n"
            + "      log(option1 != null);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("One")
    public void getOptionByOptionIndex() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var option1 = document.form1.select1.options[0];\n"
            + "      log(option1.text);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "Four", "value4"})
    public void addOption() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var options = document.form1.select1.options;\n"
            + "      var index = options.length;\n"
            + "      options[index] = new Option('Four','value4');\n"
            + "      log(options.length);\n"
            + "      log(options[index].text);\n"
            + "      log(options[index].value);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "true", "4", "Four", "value4", "true", "3", "false"})
    public void addOptionSelected() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var oSelect = document.form1.select1;\n"
            + "      var options = oSelect.options;\n"
            + "      var firstSelectedIndex = oSelect.selectedIndex;\n"
            + "      log(firstSelectedIndex);\n"
            + "      log(options[firstSelectedIndex].selected);\n"

            + "      var index = options.length;\n"
            + "      var oOption = new Option('Four','value4');\n"
            + "      oOption.selected = true;\n"
            + "      options[index] = oOption;\n"

            + "      log(options.length);\n"
            + "      log(options[index].text);\n"
            + "      log(options[index].value);\n"
            + "      log(options[index].selected);\n"
            + "      log(oSelect.selectedIndex);\n"
            + "      log(options[firstSelectedIndex].selected);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "Four", "value4"})
    public void addOptionWithAddMethodIndexNull() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var options = document.form1.select1;\n"
            + "      try {\n"
            + "        options.add(new Option('Four','value4'), null);\n"
            + "      } catch(e) { log('exception'); }\n"
            + "      log(options.length);\n"
            + "      var index = options.length - 1;\n"
            + "      log(options[index].text);\n"
            + "      log(options[index].value);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for bug 1570478.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "Four", "value4", "Three b", "value3b"})
    public void addOptionWithAddMethodNoSecondParameter() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var oSelect = document.form1.select1;\n"
            + "      try {\n"
            + "        oSelect.add(new Option('Four', 'value4'));\n"
            + "        log(oSelect.length);\n"
            + "        log(oSelect[oSelect.length-1].text);\n"
            + "        log(oSelect[oSelect.length-1].value);\n"

            + "        oSelect.add(new Option('Three b', 'value3b'), 3);\n"
            + "        log(oSelect[3].text);\n"
            + "        log(oSelect[3].value);\n"
            + "      } catch(e) { log('exception'); }\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for bug 3319397.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "test", "testValue"})
    public void addOptionTooEmptySelectWithAddMethodIndexNull() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var oSelect = document.form1.select1;\n"
            + "      try {\n"
            + "        log(oSelect.length);\n"
            + "        oSelect.add(new Option('test', 'testValue'), null);\n"
            + "        log(oSelect[oSelect.length-1].text);\n"
            + "        log(oSelect[oSelect.length-1].value);\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "0", "foo*"})
    public void addOptionMethodIndexMinusOneEmptySelect() throws Exception {
        addOptionMethod(", -1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "-1", "foo"})
    public void addOptionMethodIndexMinusOneEmptySelectMulti() throws Exception {
        addOptionMethod(", -1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "0", "foo*"})
    public void addOptionMethodIndexZeroEmptySelect() throws Exception {
        addOptionMethod(", 0", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "-1", "foo"})
    public void addOptionMethodIndexZeroEmptySelectMulti() throws Exception {
        addOptionMethod(", 0", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "0", "foo*"})
    public void addOptionMethodIndexOneEmptySelect() throws Exception {
        addOptionMethod(", 1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "-1", "foo"})
    public void addOptionMethodIndexOneEmptySelectMulti() throws Exception {
        addOptionMethod(", 1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "0", "foo*"})
    public void addOptionMethodIndexFourEmptySelect() throws Exception {
        addOptionMethod(", 4", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "-1", "foo"})
    public void addOptionMethodIndexFourEmptySelectMulti() throws Exception {
        addOptionMethod(", 4", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three", "foo"})
    public void addOptionMethodIndexMinusOne() throws Exception {
        addOptionMethod(", -1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three*", "foo"})
    public void addOptionMethodIndexMinusOneMulti() throws Exception {
        addOptionMethod(", -1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "2", "foo", "One", "Two*", "Three"})
    public void addOptionMethodIndexZero() throws Exception {
        addOptionMethod(", 0", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "2", "foo", "One", "Two*", "Three*"})
    public void addOptionMethodIndexZeroMulti() throws Exception {
        addOptionMethod(", 0", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "2", "One", "foo", "Two*", "Three"})
    public void addOptionMethodIndexOne() throws Exception {
        addOptionMethod(", 1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "2", "One", "foo", "Two*", "Three*"})
    public void addOptionMethodIndexOneMulti() throws Exception {
        addOptionMethod(", 1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "foo", "Three"})
    public void addOptionMethodhIndexTwo() throws Exception {
        addOptionMethod(", 2", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "foo", "Three*"})
    public void addOptionMethodhIndexTwoMulti() throws Exception {
        addOptionMethod(", 2", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three", "foo"})
    public void addOptionMethodIndexThree() throws Exception {
        addOptionMethod(", 3", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three*", "foo"})
    public void addOptionMethodIndexThreeMulti() throws Exception {
        addOptionMethod(", 3", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three", "foo"})
    public void addOptionMethodIndexFour() throws Exception {
        addOptionMethod(", 4", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three*", "foo"})
    public void addOptionMethodIndexFourMulti() throws Exception {
        addOptionMethod(", 4", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "0", "foo*"})
    public void addOptionMethodOptionNullEmptySelect() throws Exception {
        addOptionMethod(", null", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "-1", "foo"})
    public void addOptionMethodOptionNullEmptySelectMulti() throws Exception {
        addOptionMethod(", null", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "exception"})
    public void addOptionMethodNewOptionEmptySelect() throws Exception {
        addOptionMethod(", new Option('foo', '123')", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "exception"})
    public void addOptionMethodNewOptionEmptySelectMulti() throws Exception {
        addOptionMethod(", new Option('foo', '123')", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three", "foo"})
    public void addOptionMethodOptionNull() throws Exception {
        addOptionMethod(", null", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three*", "foo"})
    public void addOptionMethodOptionNullMulti() throws Exception {
        addOptionMethod(", null", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "exception"})
    public void addOptionMethodNewOption() throws Exception {
        addOptionMethod(", new Option('foo', '123')", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "exception"})
    public void addOptionMethodNewOptionMulti() throws Exception {
        addOptionMethod(", new Option('foo', '123')", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "2", "foo", "One", "Two*", "Three"})
    public void addOptionMethodOptionFirst() throws Exception {
        addOptionMethod(", oSelect.options[0]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "2", "foo", "One", "Two*", "Three*"})
    public void addOptionMethodOptionFirstMulti() throws Exception {
        addOptionMethod(", oSelect.options[0]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "2", "One", "foo", "Two*", "Three"})
    public void addOptionMethodOptionSecond() throws Exception {
        addOptionMethod(", oSelect.options[1]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "2", "One", "foo", "Two*", "Three*"})
    public void addOptionMethodOptionSecondMulti() throws Exception {
        addOptionMethod(", oSelect.options[1]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "foo", "Three"})
    public void addOptionMethodOptionThird() throws Exception {
        addOptionMethod(", oSelect.options[2]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "foo", "Three*"})
    public void addOptionMethodOptionThirdMulti() throws Exception {
        addOptionMethod(", oSelect.options[2]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three", "foo"})
    public void addOptionMethodOptionLast() throws Exception {
        addOptionMethod(", oSelect.options[3]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "1", "One", "Two*", "Three*", "foo"})
    public void addOptionMethodOptionLastMulti() throws Exception {
        addOptionMethod(", oSelect.options[3]", false, true);
    }

    private void addOptionMethod(final String param, final boolean empty, final boolean multi) throws Exception {
        String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.select1;\n"
            + "        log(oSelect.length);\n"
            + "        var opt = new Option('foo', '123');\n"
            + "        oSelect.add(opt" + param + ");\n"

            + "        log(oSelect.length);\n"
            + "        log(oSelect.selectedIndex);\n"
            + "        for (var i = 0; i < oSelect.options.length; i++) {\n"
            + "          log(oSelect.options[i].text + (oSelect.options[i].selected ? '*' : ''));\n"
            + "        }\n"
            + "      } catch (e) { log('exception'); }\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 308.
     * See http://sourceforge.net/p/htmlunit/bugs/308/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1"})
    public void addWithIndexEmptySelect() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.testSelect;\n"
            + "        log(oSelect.length);\n"
            + "        var opt = new Option('foo', '123');\n"
            + "        oSelect.add(opt, -1);\n"
            + "        log(oSelect.length);\n"
            + "      } catch (e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <select name='testSelect'></select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "0", "-1"},
            IE = {"0", "exception"})
    public void removeOptionMethodIndexMinusOneEmptySelect() throws Exception {
        removeOptionMethod("-1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "0", "-1"},
            IE = {"0", "exception"})
    public void removeOptionMethodIndexMinusOneEmptySelectMulti() throws Exception {
        removeOptionMethod("-1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodIndexZeroEmptySelect() throws Exception {
        removeOptionMethod("0", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodIndexZeroEmptySelectMulti() throws Exception {
        removeOptionMethod("0", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodIndexOneEmptySelect() throws Exception {
        removeOptionMethod("1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodIndexOneEmptySelectMulti() throws Exception {
        removeOptionMethod("1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodIndexFourEmptySelect() throws Exception {
        removeOptionMethod("4", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodIndexFourEmptySelectMulti() throws Exception {
        removeOptionMethod("4", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "3", "1", "One", "Two*", "Three"},
            IE = {"3", "exception"})
    public void removeOptionMethodIndexMinusOne() throws Exception {
        removeOptionMethod("-1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "3", "1", "One", "Two*", "Three*"},
            IE = {"3", "exception"})
    public void removeOptionMethodIndexMinusOneMulti() throws Exception {
        removeOptionMethod("-1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three"})
    public void removeOptionMethodIndexZero() throws Exception {
        removeOptionMethod("0", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three*"})
    public void removeOptionMethodIndexZeroMulti() throws Exception {
        removeOptionMethod("0", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "One*", "Three"})
    public void removeOptionMethodIndexOne() throws Exception {
        removeOptionMethod("1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "1", "One", "Three*"})
    public void removeOptionMethodIndexOneMulti() throws Exception {
        removeOptionMethod("1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "1", "One", "Two*"})
    public void removeOptionMethodhIndexTwo() throws Exception {
        removeOptionMethod("2", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "1", "One", "Two*"})
    public void removeOptionMethodhIndexTwoMulti() throws Exception {
        removeOptionMethod("2", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "1", "One", "Two*", "Three"})
    public void removeOptionMethodIndexThree() throws Exception {
        removeOptionMethod("3", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "1", "One", "Two*", "Three*"})
    public void removeOptionMethodIndexThreeMulti() throws Exception {
        removeOptionMethod("3", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "1", "One", "Two*", "Three"})
    public void removeOptionMethodIndexFour() throws Exception {
        removeOptionMethod("4", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "1", "One", "Two*", "Three*"})
    public void removeOptionMethodIndexFourMulti() throws Exception {
        removeOptionMethod("4", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodOptionNullEmptySelect() throws Exception {
        removeOptionMethod("null", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodOptionNullEmptySelectMulti() throws Exception {
        removeOptionMethod("null", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodNewOptionEmptySelect() throws Exception {
        removeOptionMethod("new Option('foo', '123')", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "-1"})
    public void removeOptionMethodNewOptionEmptySelectMulti() throws Exception {
        removeOptionMethod("new Option('foo', '123')", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three"})
    public void removeOptionMethodOptionNull() throws Exception {
        removeOptionMethod("null", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three*"})
    public void removeOptionMethodOptionNullMulti() throws Exception {
        removeOptionMethod("null", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three"})
    public void removeOptionMethodNewOption() throws Exception {
        removeOptionMethod("new Option('foo', '123')", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three*"})
    public void removeOptionMethodNewOptionMulti() throws Exception {
        removeOptionMethod("new Option('foo', '123')", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three"})
    public void removeOptionMethodOptionFirst() throws Exception {
        removeOptionMethod("oSelect.options[0]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three*"})
    public void removeOptionMethodOptionFirstMulti() throws Exception {
        removeOptionMethod("oSelect.options[0]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three"})
    public void removeOptionMethodOptionSecond() throws Exception {
        removeOptionMethod("oSelect.options[1]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three*"})
    public void removeOptionMethodOptionSecondMulti() throws Exception {
        removeOptionMethod("oSelect.options[1]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three"})
    public void removeOptionMethodOptionThird() throws Exception {
        removeOptionMethod("oSelect.options[2]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three*"})
    public void removeOptionMethodOptionThirdMulti() throws Exception {
        removeOptionMethod("oSelect.options[2]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three"})
    public void removeOptionMethodOptionLast() throws Exception {
        removeOptionMethod("oSelect.options[3]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "0", "Two*", "Three*"})
    public void removeOptionMethodOptionLastMulti() throws Exception {
        removeOptionMethod("oSelect.options[3]", false, true);
    }

    private void removeOptionMethod(final String param, final boolean empty, final boolean multi) throws Exception {
        String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.select1;\n"
            + "        log(oSelect.length);\n"
            + "        oSelect.remove(" + param + ");\n"

            + "        log(oSelect.length);\n"
            + "        log(oSelect.selectedIndex);\n"
            + "        for (var i = 0; i < oSelect.options.length; i++) {\n"
            + "          log(oSelect.options[i].text + (oSelect.options[i].selected ? '*' : ''));\n"
            + "        }\n"
            + "      } catch (e) { log('exception'); }\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "Three", "value3"})
    public void removeOption() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var options = document.form1.select1.options;\n"
            + "  options[1]=null;\n"
            + "  log(options.length);\n"
            + "  log(options[1].text);\n"
            + "  log(options[1].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1'>\n"
            + "    <option name='option1' value='value1'>One</option>\n"
            + "    <option name='option2' value='value2' selected>Two</option>\n"
            + "    <option name='option3' value='value3'>Three</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "Three", "value3"})
    public void removeOptionWithRemoveMethod() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var select = document.form1.select1;\n"
            + "  select.remove(1);\n"
            + "  log(select.length);\n"
            + "  log(select[1].text);\n"
            + "  log(select[1].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1'>\n"
            + "    <option name='option1' value='value1'>One</option>\n"
            + "    <option name='option2' value='value2' selected>Two</option>\n"
            + "    <option name='option3' value='value3'>Three</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Method remove on the options collection exists only for IE.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "Three", "value3"})
    public void optionsRemoveMethod() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var options = document.form1.select1.options;\n"
            + "  try {\n"
            + "    options.remove(1);\n"
            + "    log(options.length);\n"
            + "    log(options[1].text);\n"
            + "    log(options[1].value);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1'>\n"
            + "    <option name='option1' value='value1'>One</option>\n"
            + "    <option name='option2' value='value2' selected>Two</option>\n"
            + "    <option name='option3' value='value3'>Three</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void clearOptions() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var options = document.form1.select1.options;\n"
            + "  options.length = 0;\n"
            + "  log(options.length);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1'>\n"
            + "    <option name='option1' value='value1'>One</option>\n"
            + "    <option name='option2' value='value2' selected>Two</option>\n"
            + "    <option name='option3' value='value3'>Three</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that option array is filled with empty options when length is increased.
     * Test case for bug 1370484
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "", "", "foo", "fooValue"})
    public void increaseOptionsSettingLength() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var options = document.form1.select1.options;\n"
            + "  log(options.length);\n"
            + "  options.length = 2;\n"
            + "  log(options.length);\n"
            + "  log(options[1].text);\n"
            + "  log(options[1].value);\n"
            + "  options.length = 50;\n"
            + "  options[49].text = 'foo';\n"
            + "  options[49].value = 'fooValue';\n"
            + "  log(options[49].text);\n"
            + "  log(options[49].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1'>\n"
            + "    <option name='option1' value='value1'>One</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"One", "value1"})
    public void optionArrayHasItemMethod() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var options = document.form1.select1.options;\n"
            + "  log(options.item(0).text);\n"
            + "  log(options.item(0).value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1'>\n"
            + "    <option name='option1' value='value1'>One</option>\n"
            + "    <option name='option2' value='value2' selected>Two</option>\n"
            + "    <option name='option3' value='value3'>Three</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Two", "", "Two", "", ""})
    public void getValue() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  for (var i = 1; i < 6; i++)\n"
            + "  log(document.form1['select' + i].value);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1'>\n"
            + "    <option name='option1'>One</option>\n"
            + "    <option name='option2' selected is='test'>Two</option>\n"
            + "    <option name='option3'>Three</option>\n"
            + "  </select>\n"
            + "  <select name='select2'>\n"
            + "  </select>\n"
            + "  <select name='select3' multiple>\n"
            + "    <option name='option1'>One</option>\n"
            + "    <option name='option2' selected>Two</option>\n"
            + "    <option name='option3' selected>Three</option>\n"
            + "  </select>\n"
            + "  <select name='select4' multiple>\n"
            + "    <option name='option1'>One</option>\n"
            + "    <option name='option2'>Two</option>\n"
            + "    <option name='option3'>Three</option>\n"
            + "  </select>\n"
            + "  <select name='select5' multiple>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1"})
    public void setValue() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.form1.select1.selectedIndex);\n"
            + "  document.form1.select1.value = 'option2';\n"
            + "  log(document.form1.select1.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' action='http://test'>\n"
            + "  <select name='select1'>\n"
            + "    <option value='option1' name='option1'>One</option>\n"
            + "    <option value='option2' name='option2'>Two</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that options delegates to select (bug 1111597).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"2-2", "1-1", "2-2", "0-0", "2-2", "1-1"})
    public void optionsDelegateToSelect() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
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
            + "  } catch (e) { log('exception'); }\n"
            + "}\n"
            + "function doAlerts(s) {\n"
            + "  log(s.childNodes.length + '-' + s.options.childNodes.length);\n"
            + "  log(s.selectedIndex + '-' + s.options.selectedIndex);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='test'>\n"
            + "  <select id='select1'><option>a</option><option selected='selected'>b</option></select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that options delegates to select (bug 1111597).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "b", "3", "c"})
    public void optionsArrayAdd() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var s = document.getElementById('select1');\n"
            + "  var lengthBefore = s.options.length;\n"
            + "  log(lengthBefore);\n"
            + "  log(s.options.item(lengthBefore - 1).text);\n"
            + "  var opt = document.createElement(\"OPTION\");\n"
            + "  opt.value = 'c';\n"
            + "  opt.text = 'c';\n"
            + "  s.options.add(opt);\n"
            + "  var lengthAfterAdd = s.options.length;\n"
            + "  log(lengthAfterAdd);\n"
            + "  log(s.options.item(lengthAfterAdd - 1).text);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-1")
    public void selectedIndex() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    s.options.length = 0;\n"
            + "    s.selectedIndex = 0;\n"
            + "    log(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'><option>hello</option></select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "true", "false", "false", "0"})
    public void defaultSelectedValue_SizeNegativeOne() throws Exception {
        defaultSelectedValue("-1", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "false", "false", "false", "-1"})
    public void defaultSelectedValue_SizeNegativeOne_Multi() throws Exception {
        defaultSelectedValue("-1", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "true", "false", "false", "0"})
    public void defaultSelectedValue_SizeZero() throws Exception {
        defaultSelectedValue("0", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "false", "false", "false", "-1"})
    public void defaultSelectedValue_SizeZero_Multi() throws Exception {
        defaultSelectedValue("0", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "true", "false", "false", "0"})
    public void defaultSelectedValue_SizeOne() throws Exception {
        defaultSelectedValue("1", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "false", "false", "false", "-1"})
    public void defaultSelectedValue_SizeOne_Multi() throws Exception {
        defaultSelectedValue("1", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "false", "false", "false", "-1"})
    public void defaultSelectedValue_SizeTwo() throws Exception {
        defaultSelectedValue("2", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "false", "false", "false", "-1"})
    public void defaultSelectedValue_SizeTwo_Multi() throws Exception {
        defaultSelectedValue("2", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "true", "false", "false", "0"})
    public void defaultSelectedValue_SizeInvalid() throws Exception {
        defaultSelectedValue("x", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "false", "false", "false", "-1"})
    public void defaultSelectedValue_SizeInvalid_Mulzi() throws Exception {
        defaultSelectedValue("x", true);
    }

    /**
     * Tests default selection status for options in a select of the specified size, optionally
     * allowing multiple selections.
     * @param size the select input's size attribute
     * @param multiple whether or not the select input should allow multiple selections
     * @param expected the expected alerts
     * @throws Exception if the test fails
     */
    private void defaultSelectedValue(final String size, final boolean multiple)
        throws Exception {

        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "   function test() {\n"
            + "     log(document.getElementById('s').size);\n"
            + "     log(document.getElementById('a').selected);\n"
            + "     log(document.getElementById('b').selected);\n"
            + "     log(document.getElementById('c').selected);\n"
            + "     log(document.getElementById('s').selectedIndex);\n"
            + "   }\n"
            + "</script>\n"
            + "<form id='f'>\n"
            + "  <select id='s' size='" + size + "'" + (multiple ? " multiple" : "") + ">\n"
            + "    <option id='a' value='a'>a</option>\n"
            + "    <option id='b' value='b'>b</option>\n"
            + "    <option id='c' value='c'>c</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("5")
    public void size() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.size + 5);//to test if int or string\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "false"})
    public void multiple() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.f['s1'].multiple);\n"
            + "    log(document.f['s2'].multiple);\n"
            + "    document.f['s1'].multiple = false;\n"
            + "    log(document.f['s1'].multiple);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void deselectMultiple() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<body>\n"
            + "  <form name='f'>\n"
            + "    <select name='s1' multiple>\n"
            + "      <option name='option1'>One</option>\n"
            + "      <option id='it' name='option2' selected='true'>Two</option>\n"
            + "      <option name='option3'>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver webdriver = loadPage2(html);
        final WebElement firstOption = webdriver.findElement(By.id("it"));
        assertTrue(firstOption.isSelected());
        firstOption.click();
        assertFalse(firstOption.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1"})
    public void selectedIndex_onfocus() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    log(s.selectedIndex);\n"
            + "    s.selectedIndex = 1;\n"
            + "    log(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect' onfocus='log(\"select-focus\")'>\n"
            + "    <option value='o1'>hello</option>\n"
            + "    <option value='o2'>there</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"o1", "o2"})
    public void value_onfocus() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    log(s.value);\n"
            + "    s.value = 'o2';\n"
            + "    log(s.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect' onfocus='log(\"select-focus\")'>\n"
            + "    <option value='o1'>hello</option>\n"
            + "    <option value='o2'>there</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "0", "-1"})
    public void selectedIndex_appendChild() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    var o = document.createElement('option');\n"
            + "    log(s.selectedIndex);\n"
            + "    s.appendChild(o);\n"
            + "    log(s.selectedIndex);\n"
            + "    s.removeChild(o);\n"
            + "    log(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'></select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "0", "-1"})
    public void selectedIndex_insertBefore() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    var o = document.createElement('option');\n"
            + "    log(s.selectedIndex);\n"
            + "    s.insertBefore(o, null);\n"
            + "    log(s.selectedIndex);\n"
            + "    s.removeChild(o);\n"
            + "    log(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'></select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "0", "-1"})
    public void selectedIndex_add() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = document.getElementById('mySelect');\n"
            + "    var o = document.createElement('option');\n"
            + "    log(s.selectedIndex);\n"
            + "    if (document.all)\n"
            + "      s.add(o);\n"
            + "    else\n"
            + "      s.add(o, null);\n"
            + "    log(s.selectedIndex);\n"
            + "    s.removeChild(o);\n"
            + "    log(s.selectedIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'></select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "null", "null"})
    public void item() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<body>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>first</option>\n"
            + "    <option>second</option>\n"
            + "  </select>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var s = document.getElementById('mySelect');\n"
            + "    log(s.item(0).text);\n"
            + "    log(s.item(300));\n"
            + "    try { log(s.item(-5)); } catch(e) { log('exception'); }\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"two", ""})
    public void value() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.value);\n"
            + "    select.value = 'three';\n"
            + "    log(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option value='one'>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"two", "one"})
    public void valueByValue() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.value);\n"
            + "    select.value = 'one';\n"
            + "    log(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option value='one'>1</option>\n"
            + "    <option selected value='two'>2</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"two", ""})
    public void valueByValueCase() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.value);\n"
            + "    select.value = 'One';\n"
            + "    log(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option value='one'>1</option>\n"
            + "    <option selected value='two'>2</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"two", "One"},
            IE = {"two", ""})
    public void valueByText() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.value);\n"
            + "    select.value = 'One';\n"
            + "    log(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"two", "One"},
            IE = {"two", ""})
    public void valueByTextTrim() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.value);\n"
            + "    select.value = 'One';\n"
            + "    log(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option> One </option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"two", ""})
    public void valueNull() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.value);\n"
            + "    select.value = null;\n"
            + "    log(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"two", "", ""})
    public void valueOther() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.value);\n"
            + "    select.value = 1234;\n"
            + "    log(select.value);\n"
            + "    select.value = select;\n"
            + "    log(select.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='mySelect'>\n"
            + "    <option>One</option>\n"
            + "    <option selected value='two'>Two</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"One", "Two", "One"})
    public void valueAfterReset() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    var select = document.getElementById('mySelect');\n"
            + "    log(select.value);\n"
            + "    select.options[1].selected = true;\n"
            + "    log(select.value);\n"
            + "    form.reset();\n"
            + "    log(select.value);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over")
    @BuggyWebDriver(FF = "mouse overmouse overmouse over",
            FF78 = "mouse overmouse overmouse over")
    public void mouseOver() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "      document.title += 'mouse over';\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <select name='select1' id='select1' size='4' onmouseover='doTest()'>\n"
            + "      <option value='option1' id='option1' >Option1</option>\n"
            + "      <option value='option2' id='option2'>Option2</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.id("select1")));
        actions.perform();
        Thread.sleep(400);

        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "mouse over",
            FF78 = "mouse over")
    @BuggyWebDriver(FF = "mouse overmouse overmouse over",
            FF78 = "mouse overmouse overmouse over")
    public void mouseOverDisabledSelect() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "      document.title += 'mouse over';\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <select name='select1' id='select1' size='4' onmouseover='doTest()' disabled='disabled'>\n"
            + "      <option value='option1' id='option1'>Option1</option>\n"
            + "      <option value='option2' id='option2'>Option2</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.id("select1")));
        actions.perform();
        Thread.sleep(400);

        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "2", "1", "2", "1", "1"},
            IE = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined"})
    public void labels() throws Exception {
        final String html =
            "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      debug(document.getElementById('e1'));\n"
            + "      debug(document.getElementById('e2'));\n"
            + "      debug(document.getElementById('e3'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      var labels = document.getElementById('e4').labels;\n"
            + "      document.body.removeChild(document.getElementById('l4'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      log(labels ? labels.length : labels);\n"
            + "    }\n"
            + "    function debug(e) {\n"
            + "      log(e.labels ? e.labels.length : e.labels);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='e1'>e 1</select><br>\n"
            + "  <label>something <label> click here <select id='e2'>e 2</select></label></label><br>\n"
            + "  <label for='e3'> and here</label>\n"
            + "  <select id='e3'>e 3</select><br>\n"
            + "  <label id='l4' for='e4'> what about</label>\n"
            + "  <label> this<select id='e4'>e 4</select></label><br>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "false", "true", "false", "false"},
            IE = {"1", "true", "true", "true", "true"})
    public void in() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    log(options.length);\n"
            + "    log(-1 in options);\n"
            + "    log(0 in options);\n"
            + "    log(1 in options);\n"
            + "    log(42 in options);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n2"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "[object HTMLOptionElement]", "2"},
            IE = {"null", "[object HTMLOptionElement]", "2"})
    public void addOptionByAssigningViaIndex() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var select = document.getElementById('select1');\n"
            + "      log(select[1]);\n"
            + "      select[1] = new Option('text','value');\n"
            + "      log(select[1]);\n"
            + "      log(select.options.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <select id='select1'>\n"
            + "    <option name='option1' value='value1'>One</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "[object HTMLOptionElement]", "8"},
            IE = {"null", "[object HTMLOptionElement]", "8"})
    public void addOptionByAssigningViaIndex2() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var select = document.getElementById('select1');\n"
            + "      log(select[7]);\n"
            + "      select[7] = new Option('text','value');\n"
            + "      log(select[7]);\n"
            + "      log(select.options.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <select id='select1'>\n"
            + "    <option name='option1' value='value1'>One</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLFormElement]")
    public void form() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <select id='a'>\n"
            + "      <option name='option1' value='value1'>One</option>\n"
            + "    </select>\n"
            + "  </form>"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    log(document.getElementById('a').form);\n"
            + "  </script>"
            + "</body>"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void deselectFromMultiple() throws Exception {
        final String html
            = "<html><body>\n"
            + "<select id='s' multiple>\n"
            + "  <option selected value='one'>One</option>\n"
            + "  <option value='two'>Two</option>\n"
            + "  <option selected value='three'>Three</option>\n"
            + "</select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement multiSelect = driver.findElement(By.id("s"));
        final List<WebElement> options = multiSelect.findElements(By.tagName("option"));

        WebElement option = options.get(0);
        assertTrue(option.isSelected());
        option.click();
        assertFalse(option.isSelected());
        option.click();
        assertTrue(option.isSelected());

        option = options.get(2);
        assertTrue(option.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void optionClick() throws Exception {
        final String html
            = "<html><body>\n"
            + "<select id='s' multiple>\n"
            + "  <option selected value='one'>One</option>\n"
            + "  <option value='two'>Two</option>\n"
            + "  <option selected value='three'>Three</option>\n"
            + "</select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement multiSelect = driver.findElement(By.id("s"));
        final List<WebElement> options = multiSelect.findElements(By.tagName("option"));

        assertTrue(options.get(0).isSelected());
        assertFalse(options.get(1).isSelected());
        assertTrue(options.get(2).isSelected());

        options.get(0).click();

        assertFalse(options.get(0).isSelected());
        assertFalse(options.get(1).isSelected());
        assertTrue(options.get(2).isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    // https://github.com/mozilla/geckodriver/issues/584
    public void optionClickActions() throws Exception {
        final String html
            = "<html><body>\n"
            + "<select id='s' multiple>\n"
            + "  <option selected value='one'>One</option>\n"
            + "  <option value='two'>Two</option>\n"
            + "  <option selected value='three'>Three</option>\n"
            + "</select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement multiSelect = driver.findElement(By.id("s"));
        final List<WebElement> options = multiSelect.findElements(By.tagName("option"));

        assertTrue(options.get(0).isSelected());
        assertFalse(options.get(1).isSelected());
        assertTrue(options.get(2).isSelected());

        new Actions(driver).click(options.get(0)).perform();

        assertTrue(options.get(0).isSelected());
        assertFalse(options.get(1).isSelected());
        assertFalse(options.get(2).isSelected());
    }
}
