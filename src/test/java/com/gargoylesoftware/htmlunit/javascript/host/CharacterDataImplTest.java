/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link CharacterDataImpl}.
 *
 * @version $Revision$
 * @author David K. Taylor
 */
@RunWith(BrowserRunner.class)
public class CharacterDataImplTest extends WebTestCase {

    /**
     * Regression test for inline text nodes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some Text", "9", "3", "Some Text", "#text" })
    public void characterDataImpl_textNode() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.length);\n"
            + "    alert(text1.nodeType);\n"
            + "    alert(text1.nodeValue);\n"
            + "    alert(text1.nodeName);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("First", page.getTitleText());
    }

    /**
     * Regression test for setting the data property of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some New Text", "Some New Text" })
    public void characterDataImpl_setData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.data='Some New Text';\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.nodeValue);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * Regression test for setting the nodeValue property of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some New Text", "Some New Text" })
    public void characterDataImpl_setNodeValue() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.nodeValue='Some New Text';\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.nodeValue);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * Regression test for appendData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some Text Appended")
    public void characterDataImpl_appendData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.appendData(' Appended');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * Regression test for deleteData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some Text")
    public void characterDataImpl_deleteData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.deleteData(5, 11);\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Not So New Text</div></body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * Regression test for insertData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some New Text")
    public void characterDataImpl_insertData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.insertData(5, 'New ');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * Regression test for replaceData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some New Text")
    public void characterDataImpl_replaceData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.replaceData(5, 3, 'New');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Old Text</div></body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * Regression test for substringData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"New", "Some New Text" })
    public void characterDataImpl_substringData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    alert(text1.substringData(5, 3));\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some New Text</div></body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * Regression test for substringData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some ", "Text", "true" })
    public void textImpl_splitText() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    var text2=text1.splitText(5);\n"
            + "    alert(text1.data);\n"
            + "    alert(text2.data);\n"
            + "    alert(text1.nextSibling==text2);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());
    }
}
