/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * Tests for Form
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 */
public class FormTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public FormTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testElementsAccessor() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1.length)\n"
            + "    for( var i=0; i< document.form1.length; i++) {\n"
            + "        var element = document.form1.elements[i];"
            + "        if( element.type != 'radio' && element != document.form1[element.name] ) {\n"
            + "            alert('name index not working for '+element.name);\n"
            + "        }\n"
            + "        alert( element.name )\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='button' name='button1' />"
            + "    <button type='button' name='button2' />"
            + "    <input type='checkbox' name='checkbox1' />"
            + "    <input type='file' name='fileupload1' />"
            + "    <input type='hidden' name='hidden1' />"
            + "    <input type='radio' name='radio1' value='1' />"
            + "    <input type='radio' name='radio1' value='2' />"
            + "    <select name='select1'>"
            + "        <option>foo</option>"
            + "    </select>"
            + "    <select multiple='multiple' name='select2'>"
            + "        <option>foo</option>"
            + "    </select>"
            + "    <input type='password' name='password1' />"
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
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "16", "button1", "button2", "checkbox1", "fileupload1", "hidden1",
            "radio1", "radio1",
            "select1", "select2", "password1", "reset1",
            "reset2", "submit1", "submit2", "textInput1", "textarea1"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testRadioButtonArray() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var radioArray = document.form1['radio1'];"
            + "    alert(radioArray.length)\n"
            + "    for( var i=0; i< radioArray.length; i++) {\n"
            + "        var element = radioArray[i];"
            + "        alert( element.value )\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='radio' name='radio1' value='1'/>"
            + "    <input type='radio' name='radio1' value='2'/>"
            + "    <input type='radio' name='radio1' value='3'/>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();

        final List expectedAlerts = Arrays.asList( new String[]{
            "3", "1", "2", "3"
        } );

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final HtmlPage page = loadPage(content, collectedAlerts);

        assertEquals("foo", page.getTitleText());
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * If there is only one radio button with a specified name then that radio
     * button will be returned for the name, not an array of radio buttons.  Test
     * this.
     * @throws Exception if the test fails
     */
    public void testRadioButton_OnlyOne() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1['radio1'].value);"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='radio' name='radio1' value='1'/>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "1"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testActionProperty() throws Exception {
        final String jsProperty = "action";
        final String htmlProperty = "action";
        final String oldValue = "http://foo.com";
        final String newValue = "mailto:me@bar.com";

        final HtmlForm form = doTestProperty( jsProperty, htmlProperty, oldValue, newValue );
        assertEquals( newValue, form.getActionAttribute() );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testEncodingProperty() throws Exception {
        final String jsProperty = "encoding";
        final String htmlProperty = "enctype";
        final String oldValue = "myEncoding";
        final String newValue = "newEncoding";

        final HtmlForm form = doTestProperty( jsProperty, htmlProperty, oldValue, newValue );
        assertEquals( newValue, form.getEnctypeAttribute() );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testMethodProperty() throws Exception {
        final String jsProperty = "method";
        final String htmlProperty = "method";
        final String oldValue = "get";
        final String newValue = "post";

        final HtmlForm form = doTestProperty( jsProperty, htmlProperty, oldValue, newValue );
        assertEquals( newValue, form.getMethodAttribute() );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testTargetProperty() throws Exception {
        final String jsProperty = "target";
        final String htmlProperty = "target";
        final String oldValue = "_top";
        final String newValue = "_parent";

        final HtmlForm form = doTestProperty( jsProperty, htmlProperty, oldValue, newValue );
        assertEquals( newValue, form.getTargetAttribute() );
    }


    private HtmlForm doTestProperty(
            final String jsProperty,
            final String htmlProperty,
            final String oldValue,
            final String newValue )
        throws
            Exception {

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1."+jsProperty+");\n"
            + "    document.form1."+jsProperty+"='"+newValue+"'\n"
            + "    alert(document.form1."+jsProperty+");\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>"
            + "<form name='form1' "+htmlProperty+"='"+oldValue+"'>"
            + "    <input type='button' name='button1' />"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList( new String[]{
            oldValue, newValue
        } );

        assertEquals( expectedAlerts, collectedAlerts );
        return page.getFormByName("form1");
    }


    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmit() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>"
            + "<form name='form1' method='get' action='http://second'>"
            + "    <input type='button' name='button1' />"
            + "    <input type='button' name='button2' />"
            + "</form>"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage)client.getPage(URL_FIRST);
        assertEquals( "first", page.getTitleText() );

        final HtmlPage secondPage
            = (HtmlPage)page.executeJavaScriptIfPossible("document.form1.submit()", "test", true, null).getNewPage();
        assertEquals( "second", secondPage.getTitleText() );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmit_target() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>"
            + "<form name='form1' method='get' action='http://second' target='MyNewWindow'>"
            + "    <input type='button' name='button1' />"
            + "</form>"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage)client.getPage(URL_FIRST);
        assertEquals( "first", page.getTitleText() );

        final HtmlPage secondPage
            = (HtmlPage)page.executeJavaScriptIfPossible("document.form1.submit()", "test", true, null).getNewPage();
        assertEquals( "second", secondPage.getTitleText() );
        assertEquals( "MyNewWindow", secondPage.getEnclosingWindow().getName() );
    }

    
    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmitDoesntCallOnSubmit() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<form name='form1' method='get' action='http://second' onsubmit=\"alert('onsubmit called')\">"
            + "    <input type='submit' />"
            + "</form>"
            + "<a href='javascript:document.form1.submit()' id='link1'>Click me</a>"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );

        final HtmlPage page = (HtmlPage)client.getPage(URL_FIRST);
        final HtmlAnchor link = (HtmlAnchor)page.getHtmlElementById("link1");
        link.click();

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
    }
    

    /**
     * @throws Exception if the test fails
     */
    public void testInputNamedId() throws Exception {
        doTestInputWithName("id");
    }


    /**
     * @throws Exception if the test fails
     */
    public void testInputNamedAction() throws Exception {
        doTestInputWithName("action");
    }


    private void doTestInputWithName( final String name ) throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function go() {\n"
            + "   alert(document.simple_form."+name+".value);\n"
            + "   document.simple_form."+name+".value='foo';\n"
            + "   alert(document.simple_form."+name+".value);\n"
            + "}</script></head>"
            + "<body onload='go()'>"
            + "<p>hello world</p>"
            + "<form action='login.jsp' name='simple_form'>"
            + "    <input name='"+name+"' type='hidden' value='"+name+"2'>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());
        final List expectedAlerts = Arrays.asList( new String[]{
            name+"2", "foo"
        } );
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test that used to blow up on page load
     * @throws Exception if the test fails
     */
    public void testAccessingRadioButtonArrayByName_Regression() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final String firstContent
            = "<html><head><title>Button Test</title></head><body><form name='whatsnew'>"
            + "<input type='radio' name='second' value='1'>"
            + "<input type='radio' name='second' value='2' checked>"
            + "</form><script>clickAction();\n"
            + "function clickAction(){\n"
            + "    var value = -1;\n"
            + "    radios = document.forms['whatsnew'].elements['second'];\n"
            + "    for (var i=0; i < radios.length; i++){\n"
            + "        if (radios[i].checked == true) {\n"
            + "            value = radios[i].value;\n"
            + "            break;\n"
            + "        }\n"
            + "    }\n"
            + "    alert('value = ' + value);\n"
            + "}\n"
            + "</script></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage)client.getPage(URL_FIRST);
        assertEquals("Button Test", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{"value = 2"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Test for a bug that appeared when visiting mail.yahoo.com. Setting the value of one input
     * seems to blow away the other input. Also tests that the form input collection does not get
     * cached before the document is finished loading.
     * @throws Exception if the test fails
     */
    public void testFindInputWithoutTypeDefined() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>"
            + "<body onload='alert(document.simple_form.login.value);'>"
            + "<p>hello world</p><table><tr><td>"
            + "<form action='login.jsp' name='simple_form'>"
            + "    <input name='msg' type='hidden' value='0'>"
            + "    <script>document.simple_form.msg.value=1</script>"
            + "    <input name='login' size='17' value='foo'>"
            + "</form></td></tr></table>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();

        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        assertEquals("foo", page.getTitleText());
        final List expectedAlerts = Arrays.asList( new String[]{
            "foo"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmit_MultipleButtons() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>"
            + "<form name='form1' method='get' action='http://second'>"
            + "    <button type='submit' name='button1' id='button1'/>"
            + "    <button type='submit' name='button2' />"
            + "</form>"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage)client.getPage(URL_FIRST);
        assertEquals( "first", page.getTitleText() );

        final HtmlButton button = (HtmlButton)page.getHtmlElementById("button1");
        final HtmlPage secondPage = (HtmlPage)button.click();
        assertEquals( "second", secondPage.getTitleText() );
        assertEquals("http://second?button1=", secondPage.getWebResponse().getUrl());
    }

    /**
     * Test form.length - This method does not count the type=image
     * input tags.
     * @throws Exception if the test fails
     */
    public void testLength() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1.length);"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>"
            + "    <input type='radio' name='radio1' value='1'/>"
            + "    <input type='image' src='foo' value='1'/>"
            + "    <input type='submit' name='submit1' value='1'/>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList( new String[]{
            "2"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGet() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1[0].name)\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='button' name='button1' />"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList( new String[]{ "button1" } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
    * @throws Exception if the test fails
    */
    public void testLostFunction() throws Exception {

        final String content
            = "<html><head><title>foo</title><script>"
            + " function onSubmit() { alert('hi!'); return false; }"
            + "</script></head><body>"
            + "<form onsubmit='return onSubmit();'>"
            + " <input type='submit' id='clickMe' />"
            + "</form>"
            + "</body></html>";
    
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSubmitInput button = (HtmlSubmitInput)
            page.getHtmlElementById("clickMe");
        button.click();
        final List expectedAlerts = Collections.singletonList("hi!");
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAssignedOnsubmit() throws Exception {

        final String content
            = "<html><head><title>foo</title><script>"
            + " function onSubmit() { alert('hi!'); return false; }"
            + " function init() { document.myForm.onsubmit = onSubmit; }"
            + " window.onload = init;"
            + "</script></head><body>"
            + "<form name='myForm'>"
            + " <input type='submit' id='clickMe' />"
            + "</form>"
            + "</body></html>";
     
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSubmitInput button = (HtmlSubmitInput)
            page.getHtmlElementById("clickMe");
        button.click();
        final List expectedAlerts = Collections.singletonList("hi!");
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that the elements collection is live
    * @throws Exception if the test fails
    */
    public void testElementsLive() throws Exception {

        final String content = "<html>"
            + "<body>"
            + "<form name='myForm'>"
            + "<script>"
            + "var oElements = document.myForm.elements;"
            + "alert(oElements.length);"
            + "</script>"
            + "<input type='text' name='foo'/>"
            + "<script>"
            + "alert(oElements.length);"
            + "alert(document.myForm.elements.length);"
            + "alert(oElements == document.myForm.elements);"
            + "</script>"
            + "</form>"
            + "</body>"
            + "</html>";

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList( new String[]{"0", "1", "1", "true"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetFormFromFormsById() throws Exception {
        final String content =
            "<html>"
            + "<head></head>"
            + "<body onload=\"alert(document.forms['myForm'].action)\">"
            + "<form id='myForm' action='foo.html'>"
            + "</form>"
            + "</body>"
            + "</html>";
        
        
        final List collectedAlerts = new ArrayList();
        final List expectedAlerts = Arrays.asList( new String[]{
            "foo.html"
        } );
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetFieldNamedLikeForm() throws Exception {
        final String content =
            "<html>"
            + "<head></head>"
            + "<body onload='alert(document.login.login.type)'>"
            + "<form name='login' action='foo.html'>"
            + "<input name='login' type='text'>"
            + "</form>"
            + "</body>"
            + "</html>";
        
        final List collectedAlerts = new ArrayList();
        final List expectedAlerts = Arrays.asList( new String[]{ "text" } );
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * document.myForm.submit returns a field name submit or the submit function
     * depending on the sort of the field named submit
     * @throws Exception if the test fails
     */
    public void testFieldNamedSubmit() throws Exception {
        testFieldNamedSubmit("<input type='text' name='submit'>", "INPUT");
        testFieldNamedSubmit("<input type='password' name='submit'>", "INPUT");
        testFieldNamedSubmit("<input type='submit' name='submit'>", "INPUT");
        testFieldNamedSubmit("<input type='radio' name='submit'>", "INPUT");
        testFieldNamedSubmit("<input type='checkbox' name='submit'>", "INPUT");
        testFieldNamedSubmit("<input type='button' name='submit'>", "INPUT");
        testFieldNamedSubmit("<button type='submit' name='submit'>", "BUTTON");
        testFieldNamedSubmit("<textarea name='submit'></textarea>", "TEXTAREA");
        testFieldNamedSubmit("<select name='submit'></select>", "SELECT");
        testFieldNamedSubmit("<input type='image' name='submit'>", "function");
        testFieldNamedSubmit("<input type='IMAGE' name='submit'>", "function");
    }

    /**
     * @param htmlSnippet The html to embed in the test
     * @param expected The expected alert
     * @throws Exception if the test fails
     */
    private void testFieldNamedSubmit(final String htmlSnippet, final String expected) throws Exception {
        final String content =
            "<html>"
            + "<head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  if (document.login.submit.tagName)"
            + "    alert(document.login.submit.tagName);"
            + "  else"
            + "    alert('function');"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + "<form name='login' action='foo.html'>"
            + htmlSnippet
            + "</form>"
            + "</body>"
            + "</html>";

        final List collectedAlerts = new ArrayList();
        final List expectedAlerts = Arrays.asList( new String[]{ expected } );
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFieldFoundWithID() throws Exception {
        final String content = "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "    alert(IRForm.IRText.value);"
            + "    alert(IRForm.myField.length);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + " <form name='IRForm' action='#'>"
            + " <input type='text' id='IRText' value='before'/>"
            + " <input type='image' name='myField' src='foo.gif'/>"
            + " <input type='image' id='myField' src='foo.gif'/>"
            + " <input type='text' name='myField'/>"
            + " <input type='text' id='myField'/>"
            + " </form>"
            + "</body>"
            + "</html>";
        final List expectedAlerts = Arrays.asList( new String[]{ "before", "2" } );
        final List collectedAlerts = new ArrayList();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
