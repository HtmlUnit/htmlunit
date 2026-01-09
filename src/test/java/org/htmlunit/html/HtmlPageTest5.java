/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlPage}.
 *
 * @author Ronald Brill
 */
public class HtmlPageTest5 extends SimpleWebTestCase {
    /**
     * Test tabbing to the next element.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts({"focus-0", "blur-0", "focus-1", "blur-1", "focus-2"})
    public void tabNext() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>First</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <form name='form1' method='post' onsubmit='return false;'>\n"
                + "    <input type='submit' name='submit0' id='submit0' "
                         + "tabindex='1' onblur='alert(\"blur-0\")' onfocus='alert(\"focus-0\")'>\n"
                + "    <input type='submit' name='submit1' id='submit1' "
                         + "tabindex='2' onblur='alert(\"blur-1\")' onfocus='alert(\"focus-1\")'>\n"
                + "    <input type='submit' name='submit2' id='submit2' "
                         + "tabindex='3' onblur='alert(\"blur-2\")' onfocus='alert(\"focus-2\")'>\n"
                + "    <div id='div1'>foo</div>\n"
                + "  </form>\n"
                + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        assertEquals("submit0", page.tabToNextElement().getAttribute("name"));
        assertEquals("submit1", page.tabToNextElement().getAttribute("name"));
        assertEquals("submit2", page.tabToNextElement().getAttribute("name"));

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test tabbing to the previous element.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts({"focus-2", "blur-2", "focus-1", "blur-1", "focus-0"})
    public void tabPrevious() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>First</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <form name='form1' method='post' onsubmit='return false;'>\n"
                + "    <input type='submit' name='submit0' id='submit0' "
                         + "tabindex='1' onblur='alert(\"blur-0\")' onfocus='alert(\"focus-0\")'>\n"
                + "    <input type='submit' name='submit1' id='submit1' "
                         + "tabindex='2' onblur='alert(\"blur-1\")' onfocus='alert(\"focus-1\")'>\n"
                + "    <input type='submit' name='submit2' id='submit2' "
                         + "tabindex='3' onblur='alert(\"blur-2\")' onfocus='alert(\"focus-2\")'>\n"
                + "    <div id='div1'>foo</div>\n"
                + "  </form>\n"
                + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        assertEquals("submit2", page.tabToPreviousElement().getAttribute("name"));
        assertEquals("submit1", page.tabToPreviousElement().getAttribute("name"));
        assertEquals("submit0", page.tabToPreviousElement().getAttribute("name"));

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test tabbing where there are no tabbable elements.
     * @throws Exception if something goes wrong
     */
    @Test
    public void keyboard_NoTabbableElements() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>First</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <form name='form1' method='post' onsubmit='return false;'>\n"
                + "    <div id='div1'>foo</div>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        DomElement focus = page.getFocusedElement();
        assertTrue("original", (focus == null)
                || (focus == page.getDocumentElement())
                || (focus == page.getBody()));

        focus = page.tabToPreviousElement();
        assertNull("previous", focus);

        focus = page.tabToNextElement();
        assertNull("next", focus);

        focus = page.pressAccessKey('a');
        assertNull("accesskey", focus);

        final String[] expectedAlerts = {};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test tabbing where there is only one tabbable element.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts({"focus-0", "blur-0", "focus-0"})
    public void keyboard_OneTabbableElement() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>First</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <form name='form1' method='post' onsubmit='return false;'>\n"
                + "    <input type='submit' name='submit0' id='submit0' "
                            + "onblur='alert(\"blur-0\")' onfocus='alert(\"focus-0\")'>"
                + "    <div id='div1'>foo</div>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        final HtmlElement element = page.getHtmlElementById("submit0");

        final DomElement focus = page.getFocusedElement();
        assertTrue("original", (focus == null)
                || (focus == page.getDocumentElement())
                || (focus == page.getBody()));

        final DomElement accessKey = page.pressAccessKey('x');
        assertEquals("accesskey", focus, accessKey);

        assertEquals("next", element, page.tabToNextElement());
        assertEquals("nextAgain", element, page.tabToNextElement());

        page.getFocusedElement().blur();
        assertNull("original", page.getFocusedElement());

        assertEquals("previous", element, page.tabToPreviousElement());
        assertEquals("previousAgain", element, page.tabToPreviousElement());

        assertEquals("accesskey", element, page.pressAccessKey('z'));

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test pressing an accesskey.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts({"focus-0", "blur-0", "focus-2", "blur-2", "focus-1"})
    public void accessKeys() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>First</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <form name='form1' method='post' onsubmit='return false;'>\n"
                + "    <input type='submit' name='submit0' id='submit0' "
                         + "accesskey='a' onblur='alert(\"blur-0\")' onfocus='alert(\"focus-0\")'>\n"
                + "    <input type='submit' name='submit1' id='submit1' "
                         + "accesskey='b' onblur='alert(\"blur-1\")' onfocus='alert(\"focus-1\")'>\n"
                + "    <input type='submit' name='submit2' id='submit2' "
                         + "accesskey='c' onblur='alert(\"blur-2\")' onfocus='alert(\"focus-2\")'>\n"
                + "    <div id='div1'>foo</div>\n"
                + "  </form>\n"
                + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        assertEquals("submit0", page.pressAccessKey('a').getAttribute("name"));
        assertEquals("submit2", page.pressAccessKey('c').getAttribute("name"));
        assertEquals("submit1", page.pressAccessKey('b').getAttribute("name"));

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test that a button can be selected via accesskey.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("buttonPushed")
    public void pressAccessKey_Button() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>First</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button name='button1' id='button1' "
                        + "accesskey='1' disabled onclick='alert(\"buttonPushed\")'>foo</button>\n"
                + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        final HtmlElement button = page.getHtmlElementById("button1");
        page.pressAccessKey('1');
        assertEquals(new String[0], collectedAlerts);

        button.removeAttribute("disabled");
        page.pressAccessKey('1');
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
