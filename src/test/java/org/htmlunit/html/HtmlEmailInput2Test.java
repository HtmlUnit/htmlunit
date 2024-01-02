/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.javascript.host.event.KeyboardEvent;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HtmlEmailInput}.
 *
 * @author Ronald Brill
 * @author Anton Demydenko
 * @author Michael Lueck
 */
@RunWith(BrowserRunner.class)
public class HtmlEmailInput2Test extends SimpleWebTestCase {

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
            + "  <input type='email' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        HtmlEmailInput input = (HtmlEmailInput) page.getElementById("foo");
        input = (HtmlEmailInput) input.cloneNode(true);
        input.type("abc@email.com");
        assertEquals("", input.getValueAttribute());
        assertEquals("abc@email.com", input.getValue());
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
            + "  <input type='email' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlEmailInput input = (HtmlEmailInput) page.getElementById("foo");

        input.type("abc@email.com");
        input.reset();
        input.type("xyz@email.com");

        assertEquals("", input.getValueAttribute());
        assertEquals("xyz@email.com", input.getValue());
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
            + "  <input type='email' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlEmailInput input = (HtmlEmailInput) page.getElementById("foo");

        input.type("abc@email.com");
        input.setValueAttribute("");
        input.type("xyz@email.com");

        assertEquals("", input.getValueAttribute());
        assertEquals("abc@email.comxyz@email.com", input.getValue());
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
            + "  <input type='email' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlEmailInput input = (HtmlEmailInput) page.getElementById("foo");

        input.type("abc@email.com");
        input.setValue("");
        input.type("xyz@email.com");

        assertEquals("", input.getValueAttribute());
        assertEquals("xyz@email.com", input.getValue());
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
            + "  <input type='email' pattern='.+@email.com' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlEmailInput input = (HtmlEmailInput) page.getElementById("foo");

        // empty
        assertTrue(input.isValid());
        // invalid
        input.setValue("abc@eemail.com");
        assertFalse(input.isValid());
        // valid
        input.setValue("abc@email.com");
        assertTrue(input.isValid());
    }

    /**
     * Test should verify that even if there is no pattern
     * the emailInput still validates the email adress as browsers would do.
     * @throws Exception if the test fails
     */
    @Test
    public void basicValidation() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='email' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlEmailInput input = (HtmlEmailInput) page.getElementById("foo");

        // empty
        assertTrue(input.isValid());
        // invalid
        input.setValue("abc");
        assertFalse(input.isValid());
        // valid
        input.setValue("abc@email.com");
        assertTrue(input.isValid());
    }

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
            + "  <input type='email' name='tester' id='tester' value='bla'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(getExpectedAlerts()[0], page.getBody().asNormalizedText());
    }

    /**
     * How could this test be migrated to WebDriver? How to select the field's content?
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhileSelected() throws Exception {
        final String html =
              "<html><head></head><body>\n"
            + "<input type='email' id='myInput' value='Hello@world.com'><br>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlEmailInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("bye@world.com");
        assertEquals("Hello@world.com", input.getValueAttribute());
        assertEquals("bye@world.com", input.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeLeftArrow() throws Exception {
        final String html = "<html><head></head><body><input type='email' id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlEmailInput t = page.getHtmlElementById("t");
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
        final String html = "<html><head></head><body><input type='email' id='t'/></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlEmailInput t = page.getHtmlElementById("t");
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
     * @throws Exception
     *         if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "", "a@b.co"})
    public void maxLengthValidation() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='email' id='foo' maxLength='6'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlInput input = (HtmlInput) page.getElementById("foo");
        assertEquals(getExpectedAlerts()[0], Boolean.toString(input.isValid()));
        input.type("a@b.c");
        assertEquals(getExpectedAlerts()[1], Boolean.toString(input.isValid()));
        input.type("om");
        assertEquals(getExpectedAlerts()[2], Boolean.toString(input.isValid()));
        assertEquals(getExpectedAlerts()[3], input.getValueAttribute());
        assertEquals(getExpectedAlerts()[4], input.getValue());
    }

    /**
     * @throws Exception
     *         if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "", "a@b.com"},
            IE = {"true", "true", "true", "", "a@b.com"})
    public void minLengthValidation() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='email' id='foo' minLength='6'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlInput input = (HtmlInput) page.getElementById("foo");
        assertEquals(getExpectedAlerts()[0], Boolean.toString(input.isValid()));
        input.type("a@b.c");
        assertEquals(getExpectedAlerts()[1], Boolean.toString(input.isValid()));
        input.type("om");
        assertEquals(getExpectedAlerts()[2], Boolean.toString(input.isValid()));
        assertEquals(getExpectedAlerts()[3], input.getValueAttribute());
        assertEquals(getExpectedAlerts()[4], input.getValue());
    }
}
