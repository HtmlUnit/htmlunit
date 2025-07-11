/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.awt.GraphicsEnvironment;
import java.util.LinkedList;
import java.util.List;

import org.htmlunit.ClipboardHandler;
import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.javascript.host.event.KeyboardEvent;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.platform.AwtClipboardHandler;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlTextInput}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Anton Demydenko
 */
public class HtmlTextInput2Test extends SimpleWebTestCase {

    private static boolean SKIP_ = false;

    static {
        if (GraphicsEnvironment.isHeadless()) {
            // skip the tests in headless mode
            SKIP_ = true;
        }
    }

    /**
     * Verifies that asNormalizedText() returns the value string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bla")
    public void asNormalizedText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
        final String html = DOCTYPE_HTML + "<html><head></head><body><input id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("", t.getValueAttribute());
        assertEquals("abc", t.getValue());
        t.type('\b');
        assertEquals("", t.getValueAttribute());
        assertEquals("ab", t.getValue());
        t.type('\b');
        assertEquals("", t.getValueAttribute());
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
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
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
        final String html = DOCTYPE_HTML + "<html><body><input id='t' disabled='disabled'/></body></html>";
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
        assertEquals("", text1.getValueAttribute());
        assertEquals("abc", text1.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeNewLine() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "<input name='myText' id='myText'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";

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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='Hello world'><br>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World");
        assertEquals("Hello world", input.getValueAttribute());
        assertEquals("Bye World", input.getValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhen_selectPositionChanged() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='Hello world'><br>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World!");
        assertEquals("Hello world", input.getValueAttribute());
        assertEquals("Bye World!", input.getValue());

        input.type("\b");
        assertEquals("Hello world", input.getValueAttribute());
        assertEquals("Bye World", input.getValue());

        input.setSelectionStart(4);
        input.setSelectionEnd(4);
        input.type("Bye ");
        assertEquals("Hello world", input.getValueAttribute());
        assertEquals("Bye Bye World", input.getValue());

        input.type("\b\b\b\b");
        assertEquals("Hello world", input.getValueAttribute());
        assertEquals("Bye World", input.getValue());

        input.setSelectionStart(0);
        input.setSelectionEnd(3);
        input.type("Hello");
        assertEquals("Hello world", input.getValueAttribute());
        assertEquals("Hello World", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type_specialCharacters() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form>\n"
            + "<input id='t' onkeyup='document.forms[0].lastKey.value = event.keyCode'>\n"
            + "<input id='lastKey'>\n"
            + "</form>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        final HtmlTextInput lastKey = page.getHtmlElementById("lastKey");
        t.type("abc");
        assertEquals("", t.getValueAttribute());
        assertEquals("abc", t.getValue());
        assertEquals("", lastKey.getValueAttribute());
        assertEquals("67", lastKey.getValue());

        // character in private use area E000–F8FF
        t.type("\uE014");
        assertEquals("", t.getValueAttribute());
        assertEquals("abc", t.getValue());

        // TODO: find a way to handle left & right keys, ...
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head><body onload=''><input type='text' onkeydown='' /></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlPage page2 = clone(page);
        assertNotNull(page2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeLeftArrow() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head></head><body><input id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type('t');
        t.type('e');
        t.type('t');
        assertEquals("", t.getValueAttribute());
        assertEquals("tet", t.getValue());
        t.type(KeyboardEvent.DOM_VK_LEFT);
        assertEquals("", t.getValueAttribute());
        assertEquals("tet", t.getValue());
        t.type('s');
        assertEquals("", t.getValueAttribute());
        assertEquals("test", t.getValue());
        t.type(KeyboardEvent.DOM_VK_SPACE);
        assertEquals("", t.getValueAttribute());
        assertEquals("tes t", t.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeDelKey() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head></head><body><input id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type('t');
        t.type('e');
        t.type('t');
        assertEquals("", t.getValueAttribute());
        assertEquals("tet", t.getValue());
        t.type(KeyboardEvent.DOM_VK_LEFT);
        t.type(KeyboardEvent.DOM_VK_LEFT);
        assertEquals("", t.getValueAttribute());
        assertEquals("tet", t.getValue());
        t.type(KeyboardEvent.DOM_VK_DELETE);
        assertEquals("", t.getValueAttribute());
        assertEquals("tt", t.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submitOnEnter() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
        assertEquals("", input.getValueAttribute());
        assertEquals("4711", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typingAndReset() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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

        assertEquals("", input.getValueAttribute());
        assertEquals("0815", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typingAndSetValueAttribute() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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

        assertEquals("", input.getValueAttribute());
        assertEquals("47110815", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typingAndSetValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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

        assertEquals("", input.getValueAttribute());
        assertEquals("0815", input.getValue());
    }

    /**
     * @throws Exception
     *         if the test fails
     */
    @Test
    @Alerts({"text", "x", "x", "hidden", "x", "x"})
    public void setType() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='text' id='foo' value='x'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlInput input = (HtmlInput) page.getElementById("foo");
        assertEquals(getExpectedAlerts()[0], input.getType());
        assertEquals(getExpectedAlerts()[1], input.getValueAttribute());
        assertEquals(getExpectedAlerts()[2], input.getValue());

        input.changeType("hidden", true);
        assertEquals(getExpectedAlerts()[0], input.getType());
        assertEquals(getExpectedAlerts()[1], input.getValueAttribute());
        assertEquals(getExpectedAlerts()[2], input.getValue());

        final HtmlInput newInput = (HtmlInput) page.getElementById("foo");
        assertEquals(getExpectedAlerts()[3], newInput.getType());
        assertEquals(getExpectedAlerts()[4], newInput.getValueAttribute());
        assertEquals(getExpectedAlerts()[5], newInput.getValue());

        assertNotSame(input, newInput);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void patternValidation() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
    @Alerts({"true", "true", "true", "", "foo"})
    public void maxLengthValidation() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
        assertEquals(getExpectedAlerts()[4], input.getValue());
    }

    /**
     * @throws Exception
     *         if the test fails
     */
    @Test
    @Alerts({"true", "false", "true", "", "foobar"})
    public void minLengthValidation() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
        assertEquals(getExpectedAlerts()[4], input.getValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clipboardCopy() throws Exception {
        Assumptions.assumeFalse(SKIP_);

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <form>\n"
                + "    <input type='text' id='i1' value='aswjdfue'>\n"
                + "  </form>\n"
                + "  <button id='check' onclick='test()'>Test</button>\n"
                + "</body></html>";

        final ClipboardHandler clipboardHandler = new AwtClipboardHandler();
        getWebClient().setClipboardHandler(clipboardHandler);

        clipboardHandler.setClipboardContent("");
        final HtmlPage page = loadPage(html);

        final HtmlInput input = (HtmlInput) page.getElementById("i1");

        final Keyboard kb = new Keyboard();
        kb.press(KeyboardEvent.DOM_VK_CONTROL);
        kb.type('a');
        kb.type('c');
        kb.release(KeyboardEvent.DOM_VK_CONTROL);
        input.type(kb);

        assertEquals("aswjdfue", clipboardHandler.getClipboardContent());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clipboardPaste() throws Exception {
        Assumptions.assumeFalse(SKIP_);

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <form>\n"
                + "    <input type='text' id='i1'>\n"
                + "  </form>\n"
                + "  <button id='check' onclick='test()'>Test</button>\n"
                + "</body></html>";

        final ClipboardHandler clipboardHandler = new AwtClipboardHandler();
        getWebClient().setClipboardHandler(clipboardHandler);

        clipboardHandler.setClipboardContent("xyz");
        final HtmlPage page = loadPage(html);

        final HtmlInput input = (HtmlInput) page.getElementById("i1");

        final Keyboard kb = new Keyboard();
        kb.press(KeyboardEvent.DOM_VK_CONTROL);
        kb.type('v');
        kb.release(KeyboardEvent.DOM_VK_CONTROL);
        input.type(kb);

        assertEquals("xyz", input.getValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clipboardPasteFakeClipboard() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <form>\n"
                + "    <input type='text' id='i1'>\n"
                + "  </form>\n"
                + "  <button id='check' onclick='test()'>Test</button>\n"
                + "</body></html>";

        final ClipboardHandler clipboardHandler = new ClipboardHandler() {
            @Override
            public String getClipboardContent() {
                return "#dbbb";
            }

            @Override
            public void setClipboardContent(final String string) {
            }

        };
        getWebClient().setClipboardHandler(clipboardHandler);

        clipboardHandler.setClipboardContent("xyz");
        final HtmlPage page = loadPage(html);

        final HtmlInput input = (HtmlInput) page.getElementById("i1");

        final Keyboard kb = new Keyboard();
        kb.press(KeyboardEvent.DOM_VK_CONTROL);
        kb.type('v');
        kb.release(KeyboardEvent.DOM_VK_CONTROL);
        input.type(kb);

        assertEquals("#dbbb", input.getValue());
    }
}
