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

/**
 * Tests for {@link HtmlNumberInput}.
 *
 * @author Ronald Brill
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlNumberInput2Test extends SimpleWebTestCase {

    /**
     * Verifies that a asText() returns the value string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void asText() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='number' name='tester' id='tester' value='123'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(getExpectedAlerts()[0], page.getBody().asText());
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
            + "  <input type='number' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        HtmlNumberInput input = (HtmlNumberInput) page.getElementById("foo");
        input = (HtmlNumberInput) input.cloneNode(true);
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
            + "  <input type='number' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlNumberInput input = (HtmlNumberInput) page.getElementById("foo");

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
            + "  <input type='number' id='foo'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlNumberInput input = (HtmlNumberInput) page.getElementById("foo");

        input.type("4711");
        input.setValueAttribute("");
        input.type("0815");

        assertEquals("0815", input.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMinValidation() throws Exception {
        final String htmlContent = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='number' id='first' min='10'>\n"
                + "  <input type='number' id='second'>\n"
                + "  <input type='number' id='third' min='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlNumberInput first = (HtmlNumberInput) page.getElementById("first");
        final HtmlNumberInput second = (HtmlNumberInput) page.getElementById("second");
        final HtmlNumberInput third = (HtmlNumberInput) page.getElementById("third");

        // empty
        assertTrue(first.isValid());
        // lesser
        first.setValueAttribute("9");
        assertFalse(first.isValid());
        // equal
        first.setValueAttribute("10");
        assertTrue(first.isValid());
        // bigger
        first.setValueAttribute("11");
        assertTrue(first.isValid());

        second.setValueAttribute("10");
        assertTrue(second.isValid());
        third.setValueAttribute("10");
        assertTrue(third.isValid());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMaxValidation() throws Exception {
        final String htmlContent = "<html>\n" + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='number' id='first' max='10'>\n"
                + "  <input type='number' id='second'>\n"
                + "  <input type='number' id='third' max='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlNumberInput first = (HtmlNumberInput) page.getElementById("first");
        final HtmlNumberInput second = (HtmlNumberInput) page.getElementById("second");
        final HtmlNumberInput third = (HtmlNumberInput) page.getElementById("third");

        // empty
        assertTrue(first.isValid());
        // lesser
        first.setValueAttribute("8");
        assertTrue(first.isValid());
        // equal
        first.setValueAttribute("10");
        assertTrue(first.isValid());
        // bigger
        first.setValueAttribute("11");
        assertFalse(first.isValid());

        second.setValueAttribute("10");
        assertTrue(second.isValid());
        third.setValueAttribute("10");
        assertTrue(third.isValid());
    }
}
