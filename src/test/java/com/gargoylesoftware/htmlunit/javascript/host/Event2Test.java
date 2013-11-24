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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests that the events triggered in the right order.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class Event2Test extends WebDriverTestCase {

    /**
     * Test event order for clicking on a select option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "change [select] [-]", "click [clickMe] [1]" },
            IE = { "change [select] [-]", "click [select] [-]" },
            IE8 = { "click [select] [1]", "click [clickMe] [1]", "change [select] [-]" })
    @NotYetImplemented({ FF, IE8 })
    @BuggyWebDriver // FFDriver wrongly generates a "click [select] [1]" first that doesn't occur manually
    public void optionClick() throws Exception {
        final String firstSnippet = "       <select name='select' id='select' size='2'\n";
        final String secondSnippet = ">\n"
                + "               <option id='o_id1' value='o_value1'>option1</option>\n"
                + "               <option id='clickMe' value='o_value2'>option2</option>\n"
                + "               <option id='o_id3' value='o_value3'>option3</option>\n"
                + "       </select>\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a radio button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "click [radio] [1]", "change [radio] [-]" },
            IE = { "click [radio] [-]" },
            IE10 = { "change [radio] [-]", "click [radio] [1]" })
    public void radioClick() throws Exception {
        final String firstSnippet = "       <input type='radio' name='radio' id='clickMe' value='2'\n";
        final String secondSnippet = ">Radio\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a check box.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "click [checkbox] [1]", "change [checkbox] [-]" },
            IE = { "click [checkbox] [-]" },
            IE10 = { "change [checkbox] [-]", "click [checkbox] [1]" })
    public void checkboxClick() throws Exception {
        final String firstSnippet = "       <input type='checkbox' name='checkbox' id='clickMe' value='2'\n";
        final String secondSnippet = ">Checkbox\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a entry field.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "click [clickMe] [1]",
            IE8 = "click [clickMe] [-]")
    public void inputTextClick() throws Exception {
        final String firstSnippet = "       <input type='text' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a password field.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "click [clickMe] [1]",
            IE8 = "click [clickMe] [-]")
    public void inputPasswordClick() throws Exception {
        final String firstSnippet = "       <input type='password' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a text area.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "click [clickMe] [1]",
            IE8 = "click [clickMe] [-]")
    public void textareaClick() throws Exception {
        final String firstSnippet = "       <textarea name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = "></textarea>\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a submit button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts()
    public void submitClick() throws Exception {
        final String firstSnippet = "       <input type='submit' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a reset button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "click [clickMe] [1]",
            IE8 = "click [clickMe] [-]")
    public void resetClick() throws Exception {
        final String firstSnippet = "       <input type='reset' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a reset button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "click [clickMe] [1]",
            IE8 = "click [clickMe] [-]")
    public void buttonClick() throws Exception {
        final String firstSnippet = "       <input type='button' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on an anchor.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "click [clickMe] [1]",
            IE8 = "click [clickMe] [-]")
    public void anchorClick() throws Exception {
        final String firstSnippet = "       <a href='#' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">anchor</a>\n";

        testEvents(firstSnippet, secondSnippet);
    }

    private void testEvents(final String firstSnippet, final String secondSnippet) throws Exception {
        final String html =
                "<html>\n"
                + "<head>\n"
                + "  <script type='text/javascript'>\n"
                + "  <!--\n"
                + "    function dumpEvent(event) {\n"
                + "      var msg = '';\n"
                + "      msg = msg + event.type;\n"
                + "\n"
                + "      // target\n"
                + "      var eTarget;\n"
                + "      if (event.target) {\n"
                + "        eTarget = event.target;\n"
                + "      } else if (event.srcElement) {\n"
                + "        eTarget = event.srcElement;\n"
                + "      }\n"
                + "      // defeat Safari bug\n"
                + "      if (eTarget.nodeType == 3) {\n"
                + "        eTarget = eTarget.parentNode;\n"
                + "      }\n"
                + "\n"
                + "      if (eTarget.name) {\n"
                + "        msg = msg + ' [' + eTarget.name + ']';\n"
                + "      } else {\n"
                + "        msg = msg + ' [' + eTarget.id + ']';\n"
                + "      }\n"
                + "\n"
                + "      // key code\n"
                + "      var eCode;\n"
                + "      if (event.keyCode) {\n"
                + "        eCode = event.keyCode;\n"
                + "      } else if (event.which) {\n"
                + "        eCode = event.which;\n"
                + "      } else if (event.button) {\n"
                + "        eCode = event.button;\n"
                + "      }\n"
                + "      if (eCode) {\n"
                + "        var char = String.fromCharCode(eCode);\n"
                + "        msg = msg + ' [' + eCode + ']';\n"
                + "      } else {\n"
                + "        msg = msg + ' [-]';\n"
                + "      }\n"
                + "\n"
                + "      alert(msg);\n"
                + "    }\n"
                + "  //-->\n"
                + "  </script>\n"
                + "</head>\n"
                + "    <form id='form' name='form' action='#'>\n"
                + "        <input type='text' id='start' name='startText'/>\n"
                + "\n"
                + firstSnippet
                + "           onclick='dumpEvent(event);'\n"
//                + "           ondblclick='dumpEvent(event);'\n"
//                + "           oncontextmenu='dumpEvent(event);'\n"
//                + "           onfocus='dumpEvent(event);'\n"
//                + "           onblur='dumpEvent(event);'\n"
//                + "           onmousedown = 'dumpEvent(event);'\n"
//                + "           onmouseup = 'dumpEvent(event);'\n"
//                + "           onkeydown = 'dumpEvent(event);'\n"
//                + "           onkeyup = 'dumpEvent(event);'\n"
//                + "           onkeypress = 'dumpEvent(event);'\n"
//                + "           onselect = 'dumpEvent(event);'\n"
                + "           onchange = 'dumpEvent(event);'"
                + secondSnippet
                + "   </form>\n"
                + "</body>\n"
                + "</html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
