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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlResetInput}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlResetInputTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reset() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='text' name='textfield1' id='textfield1' value='foo'/>\n"
            + "<input type='password' name='password1' id='password1' value='foo'/>\n"
            + "<input type='hidden' name='hidden1' id='hidden1' value='foo'/>\n"
            + "<input type='radio' name='radioButton' value='foo' checked/>\n"
            + "<input type='radio' name='radioButton' value='bar'/>\n"
            + "<input type='checkbox' name='checkBox' value='check'/>\n"
            + "<select id='select1'>\n"
            + "    <option id='option1' selected value='1'>Option1</option>\n"
            + "    <option id='option2' value='2'>Option2</option>\n"
            + "</select>\n"
            + "<textarea id='textarea1'>Foobar</textarea>\n"
            + "<isindex prompt='Enter some text' id='isindex1'>\n"
            + "<input type='reset' name='resetButton' value='pushme'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlResetInput resetInput = form.getInputByName("resetButton");

        // change all the values to something else
        form.<HtmlRadioButtonInput>getFirstByXPath(
                "//input[@type='radio' and @name='radioButton' and @value='bar']").setChecked(true);
        form.<HtmlCheckBoxInput>getInputByName("checkBox").setChecked(true);
        page.<HtmlOption>getHtmlElementById("option1").setSelected(false);
        page.<HtmlOption>getHtmlElementById("option2").setSelected(true);
        page.<HtmlTextArea>getHtmlElementById("textarea1").setText("Flintstone");
        page.<HtmlTextInput>getHtmlElementById("textfield1").setValueAttribute("Flintstone");
        page.<HtmlHiddenInput>getHtmlElementById("hidden1").setValueAttribute("Flintstone");
        page.<HtmlPasswordInput>getHtmlElementById("password1").setValueAttribute("Flintstone");
        page.<HtmlIsIndex>getHtmlElementById("isindex1").setValue("Flintstone");

        // Check to make sure they did get changed
        assertEquals("bar", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertTrue(form.<HtmlCheckBoxInput>getInputByName("checkBox").isChecked());
        assertFalse(page.<HtmlOption>getHtmlElementById("option1").isSelected());
        assertTrue(page.<HtmlOption>getHtmlElementById("option2").isSelected());
        assertEquals("Flintstone", page.<HtmlTextArea>getHtmlElementById("textarea1").getText());
        assertEquals("Flintstone", page.<HtmlTextInput>getHtmlElementById("textfield1").getValueAttribute());
        assertEquals("Flintstone", page.<HtmlHiddenInput>getHtmlElementById("hidden1").getValueAttribute());
        assertEquals("Flintstone", page.<HtmlIsIndex>getHtmlElementById("isindex1").getValue());

        final HtmlPage secondPage = resetInput.click();
        assertSame(page, secondPage);

        // Check to make sure all the values have been set back to their original values.
        assertEquals("foo", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertFalse(form.<HtmlCheckBoxInput>getInputByName("checkBox").isChecked());
        assertTrue(page.<HtmlOption>getHtmlElementById("option1").isSelected());
        assertFalse(page.<HtmlOption>getHtmlElementById("option2").isSelected());
        assertEquals("Foobar", page.<HtmlTextArea>getHtmlElementById("textarea1").getText());
        assertEquals("foo", page.<HtmlTextInput>getHtmlElementById("textfield1").getValueAttribute());
        assertEquals("foo", page.<HtmlHiddenInput>getHtmlElementById("hidden1").getValueAttribute());
        assertEquals("foo", page.<HtmlPasswordInput>getHtmlElementById("password1").getValueAttribute());
        assertEquals("", page.<HtmlIsIndex>getHtmlElementById("isindex1").getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void outsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='reset' onclick='alert(1)'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        final HtmlResetInput input = page.getHtmlElementById("myInput");
        input.click();

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
