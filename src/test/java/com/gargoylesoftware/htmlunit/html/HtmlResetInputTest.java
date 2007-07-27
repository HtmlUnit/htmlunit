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
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for HtmlResetInput
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
public class HtmlResetInputTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param name The name of the test
     */
    public HtmlResetInputTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testReset() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "<input type='text' name='textfield1' id='textfield1' value='foo'/>"
            + "<input type='password' name='password1' id='password1' value='foo'/>"
            + "<input type='hidden' name='hidden1' id='hidden1' value='foo'/>"
            + "<input type='radio' name='radioButton' value='foo' checked/>"
            + "<input type='radio' name='radioButton' value='bar'/>"
            + "<input type='checkbox' name='checkBox' value='check'/>"
            + "<select id='select1'>"
            + "    <option id='option1' selected value='1'>Option1</option>"
            + "    <option id='option2' value='2'>Option2</option>"
            + "</select>"
            + "<textarea id='textarea1'>Foobar</textarea>"
            + "<isindex prompt='Enter some text' id='isindex1'>"
            + "<input type='reset' name='resetButton' value='pushme'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        
        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final HtmlResetInput resetInput = (HtmlResetInput) form.getInputByName("resetButton");

        // change all the values to something else
        ((HtmlRadioButtonInput) form.getByXPath(
                "//input[@type='radio' and @name='radioButton' and @value='bar']").get(0)).setChecked(true);
        ((HtmlCheckBoxInput) form.getInputByName("checkBox")).setChecked(true);
        ((HtmlOption) page.getHtmlElementById("option1")).setSelected(false);
        ((HtmlOption) page.getHtmlElementById("option2")).setSelected(true);
        ((HtmlTextArea) page.getHtmlElementById("textarea1")).setText("Flintstone");
        ((HtmlTextInput) page.getHtmlElementById("textfield1")).setValueAttribute("Flintstone");
        ((HtmlHiddenInput) page.getHtmlElementById("hidden1")).setValueAttribute("Flintstone");
        ((HtmlPasswordInput) page.getHtmlElementById("password1")).setValueAttribute("Flintstone");
        ((HtmlIsIndex) page.getHtmlElementById("isindex1")).setValue("Flintstone");

        // Check to make sure they did get changed
        assertEquals("bar", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertTrue(((HtmlCheckBoxInput) form.getInputByName("checkBox")).isChecked());
        assertFalse(((HtmlOption) page.getHtmlElementById("option1")).isSelected());
        assertTrue(((HtmlOption) page.getHtmlElementById("option2")).isSelected());
        assertEquals("Flintstone", ((HtmlTextArea) page.getHtmlElementById("textarea1")).getText());
        assertEquals("Flintstone", ((HtmlTextInput) page.getHtmlElementById("textfield1")).getValueAttribute());
        assertEquals("Flintstone", ((HtmlHiddenInput) page.getHtmlElementById("hidden1")).getValueAttribute());
        assertEquals("Flintstone", ((HtmlIsIndex) page.getHtmlElementById("isindex1")).getValue());

        final HtmlPage secondPage = (HtmlPage) resetInput.click();
        assertSame(page, secondPage);

        // Check to make sure all the values have been set back to their original values.
        assertEquals("foo", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertFalse(((HtmlCheckBoxInput) form.getInputByName("checkBox")).isChecked());
        assertTrue(((HtmlOption) page.getHtmlElementById("option1")).isSelected());
        assertFalse(((HtmlOption) page.getHtmlElementById("option2")).isSelected());
        assertEquals("Foobar", ((HtmlTextArea) page.getHtmlElementById("textarea1")).getText());
        assertEquals("foo", ((HtmlTextInput) page.getHtmlElementById("textfield1")).getValueAttribute());
        assertEquals("foo", ((HtmlHiddenInput) page.getHtmlElementById("hidden1")).getValueAttribute());
        assertEquals("foo", ((HtmlPasswordInput) page.getHtmlElementById("password1")).getValueAttribute());
        assertEquals("", ((HtmlIsIndex) page.getHtmlElementById("isindex1")).getValue());
    }

    /**
     * @throws Exception If the test fails
     */
    public void testOutsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='reset' onclick='alert(1)'>\n"
            + "</body></html>\n";

        final String[] expectedAlerts = {"1"};
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(html, collectedAlerts);
        final HtmlResetInput input = (HtmlResetInput) page.getHtmlElementById("myInput");
        input.click();
        
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
