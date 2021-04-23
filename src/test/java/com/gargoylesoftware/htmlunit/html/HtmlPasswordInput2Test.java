/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;

/**
 * Tests for {@link HtmlPasswordInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlPasswordInput2Test extends SimpleWebTestCase {

    /**
     * Verifies that a asText() returns the value string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bla")
    public void asText() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='password' name='tester' id='tester' value='bla'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(getExpectedAlerts()[0], page.getBody().asText());
    }

    /**
     * How could this test be migrated to WebDriver? How to select the field's content?
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhileSelected() throws Exception {
        final String html =
              "<html><head></head><body>\n"
            + "<input type='password' id='myInput' value='Hello world'><br>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlPasswordInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World");
        assertEquals("Bye World", input.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeLeftArrow() throws Exception {
        final String html = "<html><head></head><body><input type='password' id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlPasswordInput t = page.getHtmlElementById("t");
        t.type('t');
        t.type('e');
        t.type('t');
        assertEquals("tet", t.getValueAttribute());
        t.type(KeyboardEvent.DOM_VK_LEFT);
        assertEquals("tet", t.getValueAttribute());
        t.type('s');
        assertEquals("test", t.getValueAttribute());
        t.type(KeyboardEvent.DOM_VK_SPACE);
        assertEquals("tes t", t.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeDelKey() throws Exception {
        final String html = "<html><head></head><body><input type='password' id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlPasswordInput t = page.getHtmlElementById("t");
        t.type('t');
        t.type('e');
        t.type('t');
        assertEquals("tet", t.getValueAttribute());
        t.type(KeyboardEvent.DOM_VK_LEFT);
        t.type(KeyboardEvent.DOM_VK_LEFT);
        assertEquals("tet", t.getValueAttribute());
        t.type(KeyboardEvent.DOM_VK_DELETE);
        assertEquals("tt", t.getValueAttribute());
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
            + "  <input type='password' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        HtmlPasswordInput input = (HtmlPasswordInput) page.getElementById("foo");
        input = (HtmlPasswordInput) input.cloneNode(true);
        input.type("4711");
        assertEquals("4711", input.getValueAttribute());
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
            + "  <input type='password' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlPasswordInput input = (HtmlPasswordInput) page.getElementById("foo");

        input.type("4711");
        input.reset();
        input.type("0815");

        assertEquals("0815", input.getValueAttribute());
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
            + "  <input type='password' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlPasswordInput input = (HtmlPasswordInput) page.getElementById("foo");

        input.type("4711");
        input.setValueAttribute("");
        input.type("0815");

        assertEquals("0815", input.getValueAttribute());
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
            + "  <input type='password' pattern='[0-9a-zA-Z]{10,40}' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlPasswordInput input = (HtmlPasswordInput) page.getElementById("foo");

        // empty
        assertTrue(input.isValid());
        // invalid
        input.setValueAttribute("0987654321!");
        assertFalse(input.isValid());
        // valid
        input.setValueAttribute("68746d6c756e69742072756c657a21");
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
            + "  <input type='password' id='foo' maxLength='3'>\n"
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
    }

    /**
     * @throws Exception
     *         if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "false", "true", "foobar"},
            IE = {"true", "true", "true", "foobar"})
    public void minLengthValidation() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='password' id='foo' minLength='4'>\n"
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
    }
}
