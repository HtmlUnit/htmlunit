/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host.test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class InputTest extends WebTestCase {
    public InputTest( final String name ) {
        super(name);
    }


    public void testStandardProperties_Text() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "alert(document.form1.textfield1.value)\n"
                 + "alert(document.form1.textfield1.type)\n"
                 + "alert(document.form1.textfield1.name)\n"
                 + "alert(document.form1.textfield1.form.name)\n"
                 + "document.form1.textfield1.value='cat'\n"
                 + "alert(document.form1.textfield1.value)\n"
                 + "</script></head><body>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='text' name='textfield1' value='foo' />"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);

         final List expectedAlerts = Arrays.asList( new String[]{
             "foo", "text", "textfield1", "form1", "cat"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testTextProperties() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "alert(document.form1.button1.type)\n"
                 + "alert(document.form1.button2.type)\n"
                 + "alert(document.form1.checkbox1.type)\n"
                 + "alert(document.form1.fileupload1.type)\n"
                 + "alert(document.form1.hidden1.type)\n"
                 + "alert(document.form1.select1.type)\n"
                 + "alert(document.form1.select2.type)\n"
                 + "alert(document.form1.password1.type)\n"
                 + "alert(document.form1.reset1.type)\n"
                 + "alert(document.form1.reset2.type)\n"
                 + "alert(document.form1.submit1.type)\n"
                 + "alert(document.form1.submit2.type)\n"
                 + "alert(document.form1.textInput1.type)\n"
                 + "alert(document.form1.textarea1.type)\n"
                 + "</script></head><body>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='button' name='button1' />"
                 + "    <button type='button' name='button2' />"
                 + "    <input type='checkbox' name='checkbox1' />"
                 + "    <input type='file' name='fileupload1' />"
                 + "    <input type='hidden' name='hidden1' />"
                 + "    <select name='select1'>"
                 + "        <option>foo</option>"
                 + "    </select>"
                 + "    <select multiple='multiple' name='select2'>"
                 + "        <option>foo</option>"
                 + "    </select>"
                 + "    <input type='password' name='password1' />"
                 + "    <input type='radio' name='radio1' />"
                 + "    <input type='reset' name='reset1' />"
                 + "    <button type='reset' name='reset2' />"
                 + "    <input type='submit' name='submit1' />"
                 + "    <button type='submit' name='submit2' />"
                 + "    <input type='text' name='textInput1' />"
                 + "    <textarea name='textarea1'>foo</textarea>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);

         final List expectedAlerts = Arrays.asList( new String[]{
             "button", "button", "checkbox", "file", "hidden", "select-one",
             "select-multiple", "password", "reset", "reset", "submit",
             "submit", "text", "textarea"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testCheckedAttribute_Checkbox() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function test() {"
                 + "    alert(document.form1.checkbox1.checked)\n"
                 + "    document.form1.checkbox1.checked=true\n"
                 + "    alert(document.form1.checkbox1.checked)\n"
                 + "}"
                 + "</script></head><body>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='checkbox' name='checkbox1' id='checkbox1' value='foo' />"
                 + "</form>"
                 + "<a href='javascript:test()' id='clickme'>click me</a>\n"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput)page.getHtmlElementById("checkbox1");
         assertFalse( checkBox.isChecked() );
         ((HtmlAnchor)page.getHtmlElementById("clickme")).click();
         assertTrue( checkBox.isChecked() );

         final List expectedAlerts = Arrays.asList( new String[]{
             "false", "true"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testCheckedAttribute_Radio() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function test() {"
                 + "    alert(document.form1.radio1[0].checked)\n"
                 + "    alert(document.form1.radio1[1].checked)\n"
                 + "    alert(document.form1.radio1[2].checked)\n"
                 + "    document.form1.radio1[1].checked=true\n"
                 + "    alert(document.form1.radio1[0].checked)\n"
                 + "    alert(document.form1.radio1[1].checked)\n"
                 + "    alert(document.form1.radio1[2].checked)\n"
                 + "}"
                 + "</script></head><body>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='radio' name='radio1' id='radioA' value='a' checked='checked'/>"
                 + "    <input type='radio' name='radio1' id='radioB' value='b' />"
                 + "    <input type='radio' name='radio1' id='radioC' value='c' />"
                 + "</form>"
                 + "<a href='javascript:test()' id='clickme'>click me</a>\n"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         final HtmlRadioButtonInput radioA
            = (HtmlRadioButtonInput)page.getHtmlElementById("radioA");
         final HtmlRadioButtonInput radioB
            = (HtmlRadioButtonInput)page.getHtmlElementById("radioB");
         final HtmlRadioButtonInput radioC
            = (HtmlRadioButtonInput)page.getHtmlElementById("radioC");
         assertTrue( radioA.isChecked() );
         assertFalse( radioB.isChecked() );
         assertFalse( radioC.isChecked() );
         ((HtmlAnchor)page.getHtmlElementById("clickme")).click();
         assertFalse( radioA.isChecked() );
         assertTrue( radioB.isChecked() );
         assertFalse( radioC.isChecked() );

         final List expectedAlerts = Arrays.asList( new String[]{
             "true", "false", "false", "false", "true", "false"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }
}
