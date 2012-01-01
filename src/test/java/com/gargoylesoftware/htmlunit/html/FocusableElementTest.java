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
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for elements with onblur and onfocus attributes.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class FocusableElementTest extends WebTestCase {

    private static final String COMMON_ID = " id='focusId'";
    private static final String COMMON_EVENTS = " onblur=\"alert('foo onblur')\" onfocus=\"alert('foo onfocus')\"";
    private static final String COMMON_ATTRIBUTES = COMMON_ID + COMMON_EVENTS;

    /**
     * Full page driver for onblur and onfocus tests.
     *
     * @param htmlContent HTML fragment for body of page with a focusable element identified by a focusId ID attribute
     * Must have onfocus event that raises an alert of "foo1 onfocus" and an onblur event that raises an alert of "foo
     * onblur" on the second element.
     * @throws Exception if the test fails
     */
    private void onClickPageTest(final String htmlContent) throws Exception {
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), htmlContent, collectedAlerts);
        final HtmlElement element = page.getHtmlElementById("focusId");

        element.focus();
        element.blur();

        final String[] expectedAlerts = {"foo onfocus", "foo onblur", "foo onfocus", "foo onblur"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Body driver for onblur and onfocus tests.
     *
     * @param htmlBodyContent HTML tag name for simple tag with text body
     * @throws Exception if the test fails
     */
    private void onClickBodyTest(final String htmlBodyContent) throws Exception {
        onClickPageTest("<html><head><title>foo</title></head><body>\n"
                + htmlBodyContent
                + "<script type=\"text/javascript\" language=\"JavaScript\">\n"
                + "document.getElementById('focusId').focus();\n"
                + "document.getElementById('focusId').blur();\n"
                + "</script></body></html>");
    }

    /**
     * Simple tag name driver for onblur and onfocus tests.
     *
     * @param tagName HTML tag name for simple tag with text body
     * @param tagAttributes additional attribute(s) to add to the generated tag
     * @throws Exception if the test fails
     */
    private void onClickSimpleTest(final String tagName, final String tagAttributes) throws Exception {
        onClickBodyTest("<" + tagName + COMMON_ATTRIBUTES
                + " " + tagAttributes + ">Text</" + tagName + ">");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of anchor element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void anchor_onblur_onfocus() throws Exception {
        onClickSimpleTest("a", "href=\".\"");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of area element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void area_onblur_onfocus() throws Exception {
        onClickBodyTest("<map><area " + COMMON_ATTRIBUTES
                + " shape=\"rect\" coords=\"0,0,1,1\" href=\".\">\n"
                + "</area></map>");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of button element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testButton_onblur_onfocus() throws Exception {
        onClickSimpleTest("button", "name=\"foo\" value=\"bar\" type=\"button\"");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of label element surrounding input element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testLabelContainsInput_onblur_onfocus() throws Exception {
        onClickBodyTest("<form><label " + COMMON_ID + ">"
                + "Foo<input type=\"text\" name=\"foo\"" + COMMON_EVENTS + "></label></form>\n");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of label element referencing an input element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testLabelReferencesInput_onblur_onfocus() throws Exception {
        onClickBodyTest("<form><label " + COMMON_ID + " for=\"fooId\">Foo</label>\n"
                + "<input type=\"text\" name=\"foo\" id=\"fooId\"" + COMMON_EVENTS + "></form>\n");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of select element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testSelect_onblur_onfocus() throws Exception {
        onClickBodyTest("<form><select " + COMMON_ATTRIBUTES + "><option>1</option></select></form>\n");
    }

    /**
     * Test onblur and onfocus handlers and blur() and focus() methods of textarea element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTextarea_onblur_onfocus() throws Exception {
        onClickBodyTest("<form><textarea " + COMMON_ATTRIBUTES + ">Text</textarea></form>\n");
    }

    /**
     * Regression test for https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1161705&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    public void testOnBlurWith2Pages() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  var bCalled = false;\n"
            + "  function testOnBlur() {\n"
            + "    if (bCalled)\n"
            + "      throw 'problem!'; // to get the error immediately rather than an infinite loop\n"
            + "    bCalled = true;\n"
            + "    document.getElementById('text2').focus();\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onLoad='document.getElementById(\"text1\").focus()'>\n"
            + "  <input type='text' id='text1' onblur='testOnBlur()'>\n"
            + "  <input type='text' id='text2'>\n"
            + "  <a href='foo'>this page again</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        page.getAnchors().get(0).click();
    }

    /**
     * Test focus on all types of elements.
     * @throws Exception if the test fails
     */
    @Test
    public void testOnAllElements() throws Exception {
        testHTMLFile("FocusableElementTest_onAllElements.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void focusin() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function handler(_e) {\n"
            + "    var e = _e ? _e : window.event;\n"
            + "    var textarea = document.getElementById('myTextarea');\n"
            + "    textarea.value += e.type + ' ' + (e.target ? e.target : e.srcElement).id + ',';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    document.getElementById('select1').onfocus = handler;\n"
            + "    document.getElementById('select1').onfocusin = handler;\n"
            + "    document.getElementById('select1').onfocusout = handler;\n"
            + "    document.getElementById('select1').onblur = handler;\n"
            + "    document.getElementById('select2').onfocus = handler;\n"
            + "    document.getElementById('select2').onfocusin = handler;\n"
            + "    document.getElementById('select2').onfocusout = handler;\n"
            + "    document.getElementById('select2').onblur = handler;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='select1'>\n"
            + "    <option>Austria</option><option>Belgium</option><option>Bulgaria</option>\n"
            + "  </select>\n"
            + "  <select id='select2'>\n"
            + "    <option>Austria</option><option>Belgium</option><option>Bulgaria</option>\n"
            + "  </select>\n"
            + "  <textarea id='myTextarea' cols='80' rows='20'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlSelect select1 = (HtmlSelect) page.getElementById("select1");
        final HtmlSelect select2 = (HtmlSelect) page.getElementById("select2");
        final HtmlTextArea textArea = (HtmlTextArea) page.getElementById("myTextarea");
        page.setFocusedElement(select1);
        page.setFocusedElement(select2);
        page.setFocusedElement(select1);
        final String expectedString;
        if (getBrowserVersion().isIE()) {
            expectedString = "focusin select1,focus select1,focusout select1,focusin select2,blur select1,"
                + "focus select2,focusout select2,focusin select1,blur select2,focus select1,";
        }
        else {
            expectedString = "focus select1,blur select1,focus select2,blur select2,focus select1,";
        }
        assertEquals(expectedString, textArea.getText());
    }
}
