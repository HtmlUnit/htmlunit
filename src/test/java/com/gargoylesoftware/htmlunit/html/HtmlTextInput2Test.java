/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlTextInput}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Anton Demydenko
*/
@RunWith(BrowserRunner.class)
public class HtmlTextInput2Test extends SimpleWebTestCase {

    /**
     * Verifies that asNormalizedText() returns the value string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bla")
    public void asNormalizedText() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='text' name='tester' id='tester' value='bla'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(getExpectedAlerts()[0], page.getBody().asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head></head><body><input id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("abc", t.getValueAttribute());
        assertEquals("abc", t.getValue());
        t.type('\b');
        assertEquals("ab", t.getValueAttribute());
        assertEquals("ab", t.getValue());
        t.type('\b');
        assertEquals("a", t.getValueAttribute());
        assertEquals("a", t.getValue());
        t.type('\b');
        assertEquals("", t.getValueAttribute());
        assertEquals("", t.getValue());
        t.type('\b');
        assertEquals("", t.getValueAttribute());
        assertEquals("", t.getValue());
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
            + "  e.value = '231';\n"
            + "}\n"
            + "var e = document.getElementById('t');\n"
            + "e.onkeyup = copy;\n"
            + "var c = e.cloneNode();\n"
            + "c.id = 't2';\n"
            + "document.body.appendChild(c);\n"
            + "</script>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlElement t = page.getHtmlElementById("t2");
        t.type("abc");
        assertEquals("abc", t.asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeWhileDisabled() throws Exception {
        final String html = "<html><body><input id='t' disabled='disabled'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("", t.getValueAttribute());
        assertEquals("", t.getValue());
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

        final HtmlPage page = loadPage(html);
        final HtmlTextInput text1 = page.getHtmlElementById("text1");
        text1.type("abcd");
        assertEquals("abc", text1.getValueAttribute());
        assertEquals("abc", text1.getValue());
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
    @Alerts({"exception", "My old value", "My old value"})
    public void setSelectionText() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      document.selection.createRange().text = 'new';\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='My old value'><br>\n"
            + "<input id='myButton' type='button' value='Test' onclick='test()'>\n"
            + "</body></html>";

        final List<String> alerts = new LinkedList<>();

        final HtmlPage page = loadPage(html, alerts);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        final HtmlButtonInput button = page.getHtmlElementById("myButton");
        page.setFocusedElement(input);
        input.setSelectionStart(3);
        input.setSelectionEnd(6);
        button.click();

        alerts.add(input.getValueAttribute());
        alerts.add(input.getValue());
        assertEquals(getExpectedAlerts(), alerts);
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

        final HtmlPage page = loadPage(html);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World");
        assertEquals("Bye World", input.getValueAttribute());
        assertEquals("Bye World", input.getValue());
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

        final HtmlPage page = loadPage(html);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World!");
        assertEquals("Bye World!", input.getValueAttribute());
        assertEquals("Bye World!", input.getValue());

        input.type("\b");
        assertEquals("Bye World", input.getValueAttribute());
        assertEquals("Bye World", input.getValue());

        input.setSelectionStart(4);
        input.setSelectionEnd(4);
        input.type("Bye ");
        assertEquals("Bye Bye World", input.getValueAttribute());
        assertEquals("Bye Bye World", input.getValue());

        input.type("\b\b\b\b");
        assertEquals("Bye World", input.getValueAttribute());
        assertEquals("Bye World", input.getValue());

        input.setSelectionStart(0);
        input.setSelectionEnd(3);
        input.type("Hello");
        assertEquals("Hello World", input.getValueAttribute());
        assertEquals("Hello World", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type_specialCharacters() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<form>\n"
            + "<input id='t' onkeyup='document.forms[0].lastKey.value = event.keyCode'>\n"
            + "<input id='lastKey'>\n"
            + "</form>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        final HtmlTextInput lastKey = page.getHtmlElementById("lastKey");
        t.type("abc");
        assertEquals("abc", t.getValueAttribute());
        assertEquals("abc", t.getValue());
        assertEquals("67", lastKey.getValueAttribute());
        assertEquals("67", lastKey.getValue());

        // character in private use area E000–F8FF
        t.type("\uE014");
        assertEquals("abc", t.getValueAttribute());
        assertEquals("abc", t.getValue());

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

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeLeftArrow() throws Exception {
        final String html = "<html><head></head><body><input id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type('t');
        t.type('e');
        t.type('t');
        assertEquals("tet", t.getValueAttribute());
        assertEquals("tet", t.getValue());
        t.type(KeyboardEvent.DOM_VK_LEFT);
        assertEquals("tet", t.getValueAttribute());
        assertEquals("tet", t.getValue());
        t.type('s');
        assertEquals("test", t.getValueAttribute());
        assertEquals("test", t.getValue());
        t.type(KeyboardEvent.DOM_VK_SPACE);
        assertEquals("tes t", t.getValueAttribute());
        assertEquals("tes t", t.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeDelKey() throws Exception {
        final String html = "<html><head></head><body><input id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type('t');
        t.type('e');
        t.type('t');
        assertEquals("tet", t.getValueAttribute());
        assertEquals("tet", t.getValue());
        t.type(KeyboardEvent.DOM_VK_LEFT);
        t.type(KeyboardEvent.DOM_VK_LEFT);
        assertEquals("tet", t.getValueAttribute());
        assertEquals("tet", t.getValue());
        t.type(KeyboardEvent.DOM_VK_DELETE);
        assertEquals("tt", t.getValueAttribute());
        assertEquals("tt", t.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submitOnEnter() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <form action='result.html'>\n"
            + "    <input id='t' value='hello'/>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");

        t.type("\n");

        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submitOnEnterWithoutForm() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <input id='t' value='hello'/>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");

        t.type("\n");

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typingAndClone() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        HtmlTextInput input = (HtmlTextInput) page.getElementById("foo");
        input = (HtmlTextInput) input.cloneNode(true);
        input.type("4711");
        assertEquals("4711", input.getValueAttribute());
        assertEquals("4711", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typingAndReset() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlTextInput input = (HtmlTextInput) page.getElementById("foo");

        input.type("4711");
        input.reset();
        input.type("0815");

        assertEquals("0815", input.getValueAttribute());
        assertEquals("0815", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typingAndSetValueAttribute() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlTextInput input = (HtmlTextInput) page.getElementById("foo");

        input.type("4711");
        input.setValueAttribute("");
        input.type("0815");

        assertEquals("0815", input.getValueAttribute());
        assertEquals("0815", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typingAndSetValue() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlTextInput input = (HtmlTextInput) page.getElementById("foo");

        input.type("4711");
        input.setValue("");
        input.type("0815");

        assertEquals("0815", input.getValueAttribute());
        assertEquals("0815", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void patternValidation() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='text' pattern='[a-z]{4,8}' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlTextInput input = (HtmlTextInput) page.getElementById("foo");

        // empty
        assertTrue(input.isValid());
        // invalid
        input.setValue("foo");
        assertFalse(input.isValid());
        // valid
        input.setValue("foobar");
        assertTrue(input.isValid());
    }

    /**
     * @throws Exception
     *         if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "foo"})
    public void maxLengthValidation() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='text' id='foo' maxLength='3'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlInput input = (HtmlInput) page.getElementById("foo");
        assertEquals(getExpectedAlerts()[0], Boolean.toString(input.isValid()));
        input.type("foo");
        assertEquals(getExpectedAlerts()[1], Boolean.toString(input.isValid()));
        input.type("bar");
        assertEquals(getExpectedAlerts()[2], Boolean.toString(input.isValid()));
        assertEquals(getExpectedAlerts()[3], input.getValueAttribute());
        assertEquals(getExpectedAlerts()[3], input.getValue());
    }

    /**
     * @throws Exception
     *         if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "foobar"},
            IE = {"true", "true", "true", "foobar"})
    public void minLengthValidation() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='text' id='foo' minLength='4'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlInput input = (HtmlInput) page.getElementById("foo");
        assertEquals(getExpectedAlerts()[0], Boolean.toString(input.isValid()));
        input.type("foo");
        assertEquals(getExpectedAlerts()[1], Boolean.toString(input.isValid()));
        input.type("bar");
        assertEquals(getExpectedAlerts()[2], Boolean.toString(input.isValid()));
        assertEquals(getExpectedAlerts()[3], input.getValueAttribute());
        assertEquals(getExpectedAlerts()[3], input.getValue());
    }
}
