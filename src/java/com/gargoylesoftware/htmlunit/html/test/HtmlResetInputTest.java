/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlIsIndex;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.Collections;

/**
 *  Tests for HtmlSubmitInput
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlResetInputTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlResetInputTest( final String name ) {
        super( name );
    }


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
        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );
        final HtmlResetInput resetInput = (HtmlResetInput)form.getInputByName("resetButton");

        // change all the values to something else
        form.setCheckedRadioButton("radioButton", "bar");
        ((HtmlCheckBoxInput)form.getInputByName("checkBox")).setChecked(true);
        ((HtmlOption)page.getHtmlElementById("option1")).setSelected(false);
        ((HtmlOption)page.getHtmlElementById("option2")).setSelected(true);
        ((HtmlTextArea)page.getHtmlElementById("textarea1")).setText("Flintstone");
        ((HtmlTextInput)page.getHtmlElementById("textfield1")).setValueAttribute("Flintstone");
        ((HtmlHiddenInput)page.getHtmlElementById("hidden1")).setValueAttribute("Flintstone");
        ((HtmlPasswordInput)page.getHtmlElementById("password1")).setValueAttribute("Flintstone");
        ((HtmlIsIndex)page.getHtmlElementById("isindex1")).setValue("Flintstone");

        // Check to make sure they did get changed
        assertEquals( "bar", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertTrue( ((HtmlCheckBoxInput)form.getInputByName("checkBox")).isChecked() );
        assertFalse(((HtmlOption)page.getHtmlElementById("option1")).isSelected());
        assertTrue(((HtmlOption)page.getHtmlElementById("option2")).isSelected());
        assertEquals( "Flintstone", ((HtmlTextArea)page.getHtmlElementById("textarea1")).getText());
        assertEquals( "Flintstone", ((HtmlTextInput)page.getHtmlElementById("textfield1")).getValueAttribute());
        assertEquals( "Flintstone", ((HtmlHiddenInput)page.getHtmlElementById("hidden1")).getValueAttribute());
        assertEquals( "Flintstone", ((HtmlIsIndex)page.getHtmlElementById("isindex1")).getValue());

        final HtmlPage secondPage = (HtmlPage)resetInput.click();
        assertSame( page, secondPage );

        // Check to make sure all the values have been set back to their original values.
        assertEquals( "foo", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertFalse( ((HtmlCheckBoxInput)form.getInputByName("checkBox")).isChecked() );
        assertTrue(((HtmlOption)page.getHtmlElementById("option1")).isSelected());
        assertFalse(((HtmlOption)page.getHtmlElementById("option2")).isSelected());
        assertEquals( "Foobar", ((HtmlTextArea)page.getHtmlElementById("textarea1")).getText());
        assertEquals( "foo", ((HtmlTextInput)page.getHtmlElementById("textfield1")).getValueAttribute());
        assertEquals( "foo", ((HtmlHiddenInput)page.getHtmlElementById("hidden1")).getValueAttribute());
        assertEquals( "foo", ((HtmlPasswordInput)page.getHtmlElementById("password1")).getValueAttribute());
        assertEquals( "", ((HtmlIsIndex)page.getHtmlElementById("isindex1")).getValue());
    }

}
