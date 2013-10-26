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
package com.gargoylesoftware.htmlunit.html;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for elements with onblur and onfocus attributes.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class FocusableElement2Test extends WebDriverTestCase {
    private static final String COMMON_ID = " id='focusId'";
    private static final String COMMON_EVENTS = " onblur=\"alert('onblur')\" onfocus=\"alert('onfocus')\"";
    private static final String COMMON_ATTRIBUTES = COMMON_ID + COMMON_EVENTS;

    /**
     * Full page driver for onblur and onfocus tests.
     *
     * @param html HTML fragment for body of page with a focusable element identified by a focusId ID attribute
     * Must have onfocus event that raises an alert of "foo1 onfocus" and an onblur event that raises an alert of "foo
     * onblur" on the second element.
     * @throws Exception if the test fails
     */
    private void testTagWithClick(String tag) throws Exception {
        tag = tag.replaceFirst(">", COMMON_ATTRIBUTES + ">");
        testWithClick(tag);
    }

    private void testWithClick(final String body) throws Exception {
        final String html = "<html><head><title>foo</title></head><body>\n"
                + body
                + "<div id='other'>other</div>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("focusId")).click();
        driver.findElement(By.id("other")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    private void testWithCallFocusBlur(String tag) throws Exception {
        tag = tag.replaceFirst(">", COMMON_ATTRIBUTES + ">");

        final String html = "<html><head><title>foo</title></head><body>\n"
            + tag
            + "<script type=\"text/javascript\" language=\"JavaScript\">\n"
            + "document.getElementById('focusId').focus();\n"
            + "document.getElementById('focusId').blur();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

     /**
     * Test onblur and onfocus handlers and blur() and focus() methods of anchor element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "onfocus", "onblur" })
    public void anchor_onblur_onfocus() throws Exception {
        testTagWithClick("<a href='javascript:void(0)'>link</a>");
    }

    /**
    * Test onblur and onfocus handlers and blur() and focus() methods of anchor element.
    *
    * @throws Exception if the test fails
    */
    @Test
    @Alerts({ "onfocus", "onblur" })
    public void anchor_onblur_onfocus_methodsCalls() throws Exception {
        testWithCallFocusBlur("<a href='.'>link</a>");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of anchor element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void div_onblur_onfocus() throws Exception {
        testTagWithClick("<div>hello</div>");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of anchor element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void div_onblur_onfocus_methods() throws Exception {
        testTagWithClick("<div>hello</div>");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of button element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "onfocus", "onblur" })
    public void testButton_onblur_onfocus() throws Exception {
        testTagWithClick("<button name='foo' value='bar' type='button'>button</button>");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of label element surrounding input element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "onfocus", "onblur" })
    public void testLabelContainsInput_onblur_onfocus() throws Exception {
        final String body = "<form><label " + COMMON_ID + ">"
                + "Foo<input type=\"text\" name=\"foo\"" + COMMON_EVENTS + "></label></form>\n";
        testWithClick(body);
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of label element referencing an input element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "onfocus", "onblur" })
    public void testLabelReferencesInput_onblur_onfocus() throws Exception {
        final String body = "<form><label " + COMMON_ID + " for=\"fooId\">Foo</label>\n"
                + "<input type=\"text\" name=\"foo\" id=\"fooId\"" + COMMON_EVENTS + "></form>\n";
        testWithClick(body);
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of select element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "onfocus", "onblur" })
    public void testSelect_onblur_onfocus() throws Exception {
        testTagWithClick("<select><option>1</option></select>");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of textarea element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "onfocus", "onblur" })
    public void testTextarea_onblur_onfocus() throws Exception {
        testTagWithClick("<textarea>Text</textarea>");
    }

    /**
     * Test that focus() called on a non focusable element doesn't trigger document's focus handlers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "done\nfocus",
            FF17 = "done",
            IE8 = "done")
    public void focusOnNonFocusableElementShouldNotTriggerDocumentFocus() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log').value += x + '\\n';\n"
                + "  }\n"
                + "  function ff() {\n"
                + "    log('focus');\n"
                + "  }\n"
                + "</script>\n"

                + "</head><body>"
                + "<div id='it'>div</div>\n"
                + "<textarea id='log'></textarea>\n"
                + "<script>\n"
                + "  if (document.addEventListener) {\n"
                + "    document.addEventListener('focus', ff, true);\n"
                + "  }\n"
                + "  else {\n"
                + "    document.attachEvent('onfocus', ff);\n"
                + "  }\n"
                + "  document.getElementById('it').focus();\n"
                + "  document.getElementById('it').blur();\n"
                + "  log('done');\n"
                + "</script>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("log")).getAttribute("value").trim());
    }

    /**
     * Test that focus() called on a non focusable element doesn't let focused element loose the focus.
     *
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver(Browser.IE)
    @Alerts({ "input1", "focus1", "div", "input2", "blur1", "focus2" })
    public void focusOnNonFocusableElementShouldNotChangeCurrentFocus() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log').value += x + '\\n';\n"
                + "  }\n"
                + "  function ff() {\n"
                + "    log('focus');\n"
                + "  }\n"
                + "</script>\n"

                + "</head><body>"
                + "<textarea id='log'></textarea>\n"
                + "<div id='div' onblur=\"log('blur')\" onfocus=\"log('focus')\">div</div>\n"
                + "<input id='input1' onblur=\"log('blur1')\" onfocus=\"log('focus1')\">\n"
                + "<input id='input2' onblur=\"log('blur2')\" onfocus=\"log('focus2')\">\n"
                + "<script>\n"
                + "  \n"
                + "  log('input1');\n"
                + "  document.getElementById('input1').focus();\n"
                + "  log('div');\n"
                + "  document.getElementById('div').focus();\n"
                + "  log('input2');\n"
                + "  document.getElementById('input2').focus();\n"
                + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String[] alerts = driver.findElement(By.id("log")).getAttribute("value").split("\r?\n");
        assertEquals(getExpectedAlerts(), Arrays.asList(alerts));
    }

    /**
     * Test that click called on a non focusable element removes focus from focused element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "focus1", "blur1" })
    public void clickOnNonFocusableElementChangesCurrentFocus() throws Exception {
        final String html = "<html><body>\n"
            + "<textarea id='log'></textarea>\n"
            + "<div id='div' onblur=\"log('blur')\" onfocus=\"log('focus')\">div</div>\n"
            + "<input id='input1' onblur=\"log('blur1')\" onfocus=\"log('focus1')\">\n"
            + "<script>\n"
            + "  function log(x) {\n"
            + "    document.getElementById('log').value += x + '\\n';\n"
            + "  }\n"
            + "  \n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("input1")).click();
        driver.findElement(By.id("div")).click();
        final String[] alerts = driver.findElement(By.id("log")).getAttribute("value").split("\r?\n");
        assertEquals(getExpectedAlerts(), Arrays.asList(alerts));
    }
}
