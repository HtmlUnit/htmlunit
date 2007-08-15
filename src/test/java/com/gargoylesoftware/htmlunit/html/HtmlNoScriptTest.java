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

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for elements inside {@link HtmlNoScript}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HtmlNoScriptTest extends WebTestCase {

    /**
     * Create an instance
     *
     * @param name The name of the test
     */
    public HtmlNoScriptTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testGetElementById() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('second'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "    <input type='text' id='first' name='textfield'/>\n"
            + "    <noscript>\n"
            + "    <input type='text' id='second' name='button'/>\n"
            + "    </noscript>\n"
            + "</body></html>\n";
        
        final String[] expectedAlerts = {"null"};
        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testChildNodes() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var noscript = document.getElementById('myDiv' ).childNodes.item(0);\n"
            + "    alert(noscript.childNodes.length);"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "    <div id='myDiv'><noscript>\n"
            + "        <input type='text' name='button'/>\n"
            + "      </noscript></div>\n"
            + "</body></html>\n";
        
        final String[] expectedAlerts = {"0"};
        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testJavaScript() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  alert(1);"
            + "</script>\n"
            + "<noscript>\n"
            + "  <script>\n"
            + "    alert(2);"
            + "  </script>\n"
            + "</noscript>\n"
            + "</head><body>\n"
            + "</body></html>\n";
        
        final String[] expectedAlerts = {"1"};
        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testFormValues() throws Exception {
        final String htmlContent
            = "<html><body>\n"
            + "<form name='item' method='post'>\n"
            + "  <noscript>\n"
            + "    <input type=hidden name='__webpage_no_js__' value='1'>\n"
            + "  </noscript>\n"
            + "  <input type=hidden name='myParam' value='myValue'>\n"
            + "  <input type='submit'>\n"
            + "</form>\n"
            + "</body></html>\n";
        
        final HtmlPage firstPage = loadPage(htmlContent);
        final HtmlForm form = (HtmlForm) firstPage.getForms().get(0);
        final HtmlPage secondPage = (HtmlPage) form.submit((SubmittableElement) null);
        
        final MockWebConnection mockWebConnection = getMockConnection(secondPage);
        assertEquals(1, mockWebConnection.getLastParameters().size());
        assertTrue(secondPage.asXml().indexOf("__webpage_no_js__") > -1);
    }
}
