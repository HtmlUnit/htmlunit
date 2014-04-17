/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for {@link HtmlTextInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
*/
@RunWith(BrowserRunner.class)
public class HtmlTextInputTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head></head><body><input id='t'/></body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("abc", t.getValueAttribute());
        t.type('\b');
        assertEquals("ab", t.getValueAttribute());
        t.type('\b');
        assertEquals("a", t.getValueAttribute());
        t.type('\b');
        assertEquals("", t.getValueAttribute());
        t.type('\b');
        assertEquals("", t.getValueAttribute());
    }

    /**
     * This test caused a StringIndexOutOfBoundsException as of HtmlUnit-2.7-SNAPSHOT on 27.10.2009.
     * This came from the fact that cloneNode() uses clone() and the two HtmlTextInput instances
     * were referencing the same DoTypeProcessor: type in second field were reflected in the first one.
     * @throws Exception if the test fails
     */
    @Test
    public void type_StringIndexOutOfBoundsException() throws Exception {
        type_StringIndexOutOfBoundsException("<input type='text' id='t'>");
        type_StringIndexOutOfBoundsException("<input type='password' id='t'>");
        type_StringIndexOutOfBoundsException("<textarea id='t'></textarea>");
    }

    void type_StringIndexOutOfBoundsException(final String tag) throws Exception {
        final String html = "<html><head></head><body>\n"
            + tag + "\n"
            + "<script>\n"
            + "function copy(node) {\n"
            + "  e.value = '231';"
            + "}"
            + "var e = document.getElementById('t');\n"
            + "e.onkeyup = copy;\n"
            + "var c = e.cloneNode();\n"
            + "c.id = 't2';\n"
            + "document.body.appendChild(c);\n"
            + "</script>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlElement t = page.getHtmlElementById("t2");
        t.type("abc");
        assertEquals("abc", t.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeWhileDisabled() throws Exception {
        final String html = "<html><body><input id='t' disabled='disabled'/></body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("", t.getValueAttribute());
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
        final HtmlTextInput text1 = page.getHtmlElementById("text1");
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

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);

        final HtmlTextInput textInput = firstPage.getHtmlElementById("myText");

        final HtmlPage secondPage = (HtmlPage) textInput.type('\n');
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    public void setSelectionText() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    document.selection.createRange().text = 'new';\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='My old value'><br>\n"
            + "<input id='myButton' type='button' value='Test' onclick='test()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        final HtmlButtonInput button = page.getHtmlElementById("myButton");
        page.setFocusedElement(input);
        input.setSelectionStart(3);
        input.setSelectionEnd(6);
        button.click();
        assertEquals("My new value", input.getValueAttribute());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhenSelected() throws Exception {
        final String html =
              "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='Hello world'><br>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World");
        assertEquals("Bye World", input.getValueAttribute());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhen_selectPositionChanged() throws Exception {
        final String html =
              "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='Hello world'><br>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World!");
        assertEquals("Bye World!", input.getValueAttribute());

        input.type("\b");
        assertEquals("Bye World", input.getValueAttribute());

        input.setSelectionStart(4);
        input.setSelectionEnd(4);
        input.type("Bye ");
        assertEquals("Bye Bye World", input.getValueAttribute());

        input.type("\b\b\b\b");
        assertEquals("Bye World", input.getValueAttribute());

        input.setSelectionStart(0);
        input.setSelectionEnd(3);
        input.type("Hello");
        assertEquals("Hello World", input.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type_specialCharacters() throws Exception {
        final String html = "<html><head></head><body>"
            + "<form>"
            + "<input id='t' onkeyup='document.forms[0].lastKey.value = event.keyCode'>"
            + "<input id='lastKey'>"
            + "</form>"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput t = page.getHtmlElementById("t");
        final HtmlTextInput lastKey = page.getHtmlElementById("lastKey");
        t.type("abc");
        assertEquals("abc", t.getValueAttribute());
        assertEquals("67", lastKey.getValueAttribute());

        // character in private use area E000–F8FF
        t.type("\uE014");
        assertEquals("abc", t.getValueAttribute());

        // TODO: find a way to handle left & right keys, ...
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = "<html><head></head><body onload=''><input type='text' onkeydown='' /></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlPage page2 = clone(page);
        assertNotNull(page2);
    }

}
