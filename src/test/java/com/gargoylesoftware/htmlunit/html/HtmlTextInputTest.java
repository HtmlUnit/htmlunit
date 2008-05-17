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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlTextInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlTextInputTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='text1'/>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput text1 = (HtmlTextInput) page.getHtmlElementById("text1");
        text1.type("abcd");
        assertEquals("abcd", text1.getValueAttribute());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e && e.target.value.length > 2)\n"
            + "      e.preventDefault();\n"
            + "    else if (!e && window.event.srcElement.value.length > 2)\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('text1').onkeydown = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<input id='text1'/>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput text1 = (HtmlTextInput) page.getHtmlElementById("text1");
        text1.type("abcd");
        assertEquals("abc", text1.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeNewLine() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "<input name='myText' id='myText'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        
        final HtmlTextInput textInput = (HtmlTextInput) firstPage.getHtmlElementById("myText");

        final HtmlPage secondPage = (HtmlPage) textInput.type('\n');
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void selection() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(getSelection(document.getElementById('text1')).length);\n"
            + "  }\n"
            + "  function getSelection(element) {\n"
            + "    if (typeof element.selectionStart == 'number') {\n"
            + "      return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "    } else if (document.selection && document.selection.createRange) {\n"
            + "      return document.selection.createRange().text;\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<input id='text1'/>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = { "undefined,undefined", "3,undefined", "3,10" },
            FF = { "11,11", "3,11", "3,10" })
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = { "undefined,undefined", "-3,undefined", "-3,15" },
            FF = { "11,11", "0,11", "0,11" })
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = { "undefined,undefined", "10,undefined", "10,5" },
            FF = { "11,11", "10,11", "5,5" })
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput'>\n"
            + "<script>\n"
            + "    var input = document.getElementById('myTextInput');\n"
            + "    input.value = 'Hello there';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.selectionStart = " + selectionStart + ";\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.selectionEnd = " + selectionEnd + ";\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }
}
