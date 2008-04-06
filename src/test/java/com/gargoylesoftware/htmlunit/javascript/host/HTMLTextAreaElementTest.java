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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for {@link HTMLTextAreaElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLTextAreaElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "alert(document.form1.textarea1.value )\n"
            + "document.form1.textarea1.value='PoohBear';\n"
            + "alert(document.form1.textarea1.value )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='post' >\n"
            + "<textarea name='textarea1' cols='45' rows='4'>1234</textarea>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"1234", "PoohBear"};
        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOnChange() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + " <textarea name='textarea1' onchange='alert(this.value)'></textarea>\n"
            + "<input name='myButton' type='button' onclick='document.form1.textarea1.value=\"from button\"'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = page.getFormByName("form1");
        final HtmlTextArea textarea = form.getTextAreaByName("textarea1");
        textarea.setText("foo");
        final HtmlButtonInput button = (HtmlButtonInput) form.getInputByName("myButton");
        button.click();

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests that setValue doesn't has side effect. Test for bug 1155063.
     * @throws Exception if the test fails
     */
    @Test
    public void testSetValue() throws Exception {
        final String content = "<html><head></head>\n"
            + "<body>\n"
            + "<form name='form1'>\n"
            + "<textarea name='question'></textarea>\n"
            + "<input type='button' name='btn_submit' value='Next'>\n"
            + "</form>\n"
            + "<script>\n"
            + "document.form1.question.value = 'some text';\n"
            + "alert(document.form1.elements[0].tagName);\n"
            + "alert(document.form1.elements[1].tagName);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        final String[] expectedAlerts = {"TEXTAREA", "INPUT"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void testTextLength() throws Exception {
        final String[] alertsIE = {"undefined", "undefined"};
        testTextLength(BrowserVersion.INTERNET_EXPLORER_6_0, alertsIE);
        final String[] alertsFF = {"11", "0"};
        testTextLength(BrowserVersion.FIREFOX_2, alertsFF);
    }

    private void testTextLength(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String content = "<html>\n"
            + "<body>\n"
            + "<textarea id='myTextArea'></textarea>\n"
            + "<script>\n"
            + "    var textarea = document.getElementById('myTextArea');\n"
            + "    textarea.value = 'hello there';\n"
            + "    alert(textarea.textLength);\n"
            + "    textarea.value = '';\n"
            + "    alert(textarea.textLength);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void testSelection() throws Exception {
        testSelection(3, 10, BrowserVersion.INTERNET_EXPLORER_6_0,
                new String[] {"undefined,undefined", "3,undefined", "3,10"});
        testSelection(3, 10, BrowserVersion.FIREFOX_2, new String[] {"11,11", "3,11", "3,10"});
        
        testSelection(-3, 15, BrowserVersion.INTERNET_EXPLORER_6_0,
                new String[] {"undefined,undefined", "-3,undefined", "-3,15"});
        testSelection(-3, 15, BrowserVersion.FIREFOX_2, new String[] {"11,11", "0,11", "0,11"});

        testSelection(10, 5, BrowserVersion.INTERNET_EXPLORER_6_0,
                new String[] {"undefined,undefined", "10,undefined", "10,5"});
        testSelection(10, 5, BrowserVersion.FIREFOX_2, new String[] {"11,11", "10,11", "5,5"});
    }

    private void testSelection(final int selectionStart, final int selectionEnd,
            final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myTextArea'></textarea>\n"
            + "<script>\n"
            + "    var textarea = document.getElementById('myTextArea');\n"
            + "    textarea.value = 'Hello there';\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.selectionStart = " + selectionStart + ";\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.selectionEnd = " + selectionEnd + ";\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

}
