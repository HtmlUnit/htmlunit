/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


/**
 * Tests for Select.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 */
public class SelectTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public SelectTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetSelectedIndex() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    alert(document.form1.select1.length);\n"
                 + "    alert(document.form1.select1.selectedIndex);\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1'>One</option>"
                 + "        <option name='option2' selected>Two</option>"
                 + "        <option name='option3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "3", "1"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetOptions() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    for( i=0; i<options.length; i++ ) {\n"
                 + "        alert(options[i].value);\n"
                 + "        alert(options[i].text);\n"
                 + "    }\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1' value='value1'>One</option>"
                 + "        <option name='option2' value='value2' selected>Two</option>"
                 + "        <option name='option3' value='value3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "value1", "One", "value2", "Two", "value3", "Three"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetOptionLabel() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    for( i=0; i<options.length; i++ ) {\n"
                 + "        alert(options[i].value);\n"
                 + "        alert(options[i].text);\n"
                 + "    }\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1' value='value1' label='OneLabel'>One</option>"
                 + "        <option name='option2' value='value2' label='TwoLabel' selected>Two</option>"
                 + "        <option name='option3' value='value3' label='ThreeLabel'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "value1", "OneLabel", "value2", "TwoLabel", "value3", "ThreeLabel"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     *
     * @throws Exception if the test fails
     */
    public void testGetOptionByIndex() throws Exception {
        final String content
            = "<html><head><title>first</title><script language='JavaScript'>"
            //+ "//<!--"
            + "function buggy(){\n"
            + "var option1 = document.f1.elements['select'][0];\n"
            + "alert(option1!=null);\n"
            + "}\n"
            //+ "//-->\n"
            + "</script></head><body onload='buggy();'>"
            + "<form name='f1' action='xxx.html'><SELECT name='select'>"
            + "<OPTION value='A'>111</OPTION>"
            + "<OPTION value='B'>222</OPTION>"
            + "</SELECT></form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( content );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_FIRST,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("first", page.getTitleText());
        assertEquals( Collections.singletonList("true"), collectedAlerts );
    }

    /**
     *
     * @throws Exception if the test fails
     */
    public void testGetOptionByOptionIndex() throws Exception {
        final String content
            = "<html><head><title>first</title><script language='JavaScript'>"
            //+ "//<!--"
            + "function buggy(){\n"
            + "var option1 = document.form1.select1.options[0];\n"
            + "alert(option1.text);\n"
            + "}\n"
            //+ "//-->\n"
            + "</script></head><body onload='buggy();'>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( content );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_FIRST,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("first", page.getTitleText());
        assertEquals( Collections.singletonList("One"), collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testAddOption() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    var index = options.length;\n"
                 + "    options[index]=new Option('Four','value4');\n"
                 + "    alert(options.length);\n"
                 + "    alert(options[index].text);\n"
                 + "    alert(options[index].value);\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1' value='value1'>One</option>"
                 + "        <option name='option2' value='value2' selected>Two</option>"
                 + "        <option name='option3' value='value3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "4", "Four", "value4"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAddOptionWithAddMethod() throws Exception {
        if(true) {
            notImplemented();
            return;
        }
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    options.add(new Option('Four','value4'), null);\n"
                 + "    alert(options.length);\n"
                 + "    alert(options[index].text);\n"
                 + "    alert(options[index].value);\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1' value='value1'>One</option>"
                 + "        <option name='option2' value='value2' selected>Two</option>"
                 + "        <option name='option3' value='value3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "4", "Four", "value4"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testRemoveOption() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    options[1]=null;\n"
                 + "    alert(options.length);\n"
                 + "    alert(options[1].text);\n"
                 + "    alert(options[1].value);\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1' value='value1'>One</option>"
                 + "        <option name='option2' value='value2' selected>Two</option>"
                 + "        <option name='option3' value='value3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "2", "Three", "value3"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testRemoveOptionWithRemoveMethod() throws Exception {
        if(true) {
            notImplemented();
            return;
        }

        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    options.remove(1);\n"
                 + "    alert(options.length);\n"
                 + "    alert(options[1].text);\n"
                 + "    alert(options[1].value);\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1' value='value1'>One</option>"
                 + "        <option name='option2' value='value2' selected>Two</option>"
                 + "        <option name='option3' value='value3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "2", "Three", "value3"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClearOptions() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    options.length=0;\n"
                 + "    alert(options.length);\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1' value='value1'>One</option>"
                 + "        <option name='option2' value='value2' selected>Two</option>"
                 + "        <option name='option3' value='value3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "0"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOptionArrayHasItemMethod() throws Exception {
        if(true) {
            notImplemented();
            return;
        }
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    alert(options.item(0).text);\n"
                 + "    alert(options.item(0).value);\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1' value='value1'>One</option>"
                 + "        <option name='option2' value='value2' selected>Two</option>"
                 + "        <option name='option3' value='value3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "One",
             "value1"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }
}
