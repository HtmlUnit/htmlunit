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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for elements with onblur and onfocus attributes.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class FocusableElementTest extends SimpleWebTestCase {

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
