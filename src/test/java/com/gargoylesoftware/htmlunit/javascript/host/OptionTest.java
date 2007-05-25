/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
import java.util.List;

import com.gargoylesoftware.htmlunit.WebTestCase;


/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.Option}.
 *
 * @version  $Revision$
 * @author Marc Guillemot
 */
public class OptionTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public OptionTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testReadPropsBeforeAdding() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    var oOption = new Option('some text', 'some value');\n"
            + "    alert(oOption.text);\n"
            + "    alert(oOption.value);\n"
            + "    alert(oOption.selected);\n"
            + "    oOption.text = 'some other text';\n"
            + "    oOption.value = 'some other value';\n"
            + "    oOption.selected = true;\n"
            + "    alert(oOption.text);\n"
            + "    alert(oOption.value);\n"
            + "    alert(oOption.selected);\n"
            + "}</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "</body></html>";

        final String[] expectedAlerts = new String[]{
            "some text", "some value", "false",
            "some other text", "some other value", "true"
        };

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Regression test for bug 1323425
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1323425&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    public void testSelectingOrphanedOptionCreatedByDocument() throws Exception {
        final String content = "<html>"
            + "<body>"
            + "<form name='myform'/>"
            + "<script language='javascript'>"
            + "var select = document.createElement('select');"
            + "var opt = document.createElement('option');"
            + "opt.value = 'x';"
            + "opt.selected = true;"
            + "select.appendChild(opt);"
            + "document.myform.appendChild(select);"
            + "</script>"
            + "</body></html>";
        loadPage(content);
    }

    /**
     * Regression test for 1592728 
     * @throws Exception if the test fails
     */
    public void testSetSelected() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "  var sel = document.form1.select1;" 
            + "  alert(sel.selectedIndex);"
            + "  sel.options[0].selected = false;"
            + "  alert(sel.selectedIndex);"
            + "}</script></head><body onload='doTest()'>"
            + "<form name='form1'>"
            + "    <select name='select1' onchange='this.form.submit()'>"
            + "        <option value='option1' name='option1'>One</option>"
            + "        <option value='option2' name='option2'>Two</option>"
            + "        <option value='option3' name='option3' selected>Three</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"2", "2"};

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Regression test for 1672048 
     * @throws Exception if the test fails
     */
    public void testSetAttribute() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "  document.getElementById('option1').setAttribute('class', 'bla bla');" 
            + "  var o = new Option('some text', 'some value');" 
            + "  o.setAttribute('class', 'myClass');\n"
            + "}</script></head><body onload='doTest()'>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option value='option1' id='option1' name='option1'>One</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {};

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOptionIndexOutOfBound() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest()"
            + "{"
            + "  var options = document.getElementById('testSelect').options;" 
            + "  alert(options[55]);"
            + "  try"
            + "  {"
            + "    alert(options[-55]);" 
            + "  }"
            + "  catch (e)"
            + "  {"
            + "    alert('catched exception for negative index');" 
            + "  }"
            + "}"
            + "</script></head><body onload='doTest()'>"
            + "<form name='form1'>"
            + "    <select name='select1' id='testSelect'>"
            + "        <option value='option1' name='option1'>One</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"undefined", "catched exception for negative index"};

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testConstructor() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function dumpOption(_o) {"
            + "  return 'text: ' + _o.text "
            + " + ', label: ' + _o.label" 
            + " + ', value: ' + _o.value" 
            + " + ', defaultSelected: ' + _o.defaultSelected"
            + " + ', selected: ' + _o.selected;"
            + "}\n"
            + "function doTest() {"
            + "   var o2 = new Option('Option 2', '2');\n"
            + "   alert('o2: ' + dumpOption(o2));\n"
            + "   var o3 = new Option('Option 3', '3', true, false); \n"
            + "   alert('o3: ' + dumpOption(o3));\n"
            + "   document.form1.select1.appendChild(o3);\n"
            + "   alert(document.form1.select1.options.selectedIndex);"
            + "   document.form1.reset();"
            + "   alert(document.form1.select1.options.selectedIndex);"
            + "}"
            + "</script></head><body onload='doTest()'>"
            + "<form name='form1'>"
            + "    <select name='select1' id='testSelect'>"
            + "        <option value='option1' name='option1'>One</option>"
            + "    </select>"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {
            "o2: text: Option 2, label: , value: 2, defaultSelected: false, selected: false",
            "o3: text: Option 3, label: , value: 3, defaultSelected: true, selected: false",
            "0",
            "1"
        };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }
}
