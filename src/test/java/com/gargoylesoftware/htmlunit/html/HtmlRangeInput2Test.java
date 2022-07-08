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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;

/**
 * Tests for {@link HtmlRangeInput}.
 *
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlRangeInput2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDefault() throws Exception {
        final String htmlContent = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='range' id='first'>\n"
                + "  <input type='range' id='second' min='-100' max='100'>\n"
                + "  <input type='range' id='third' min='-15' max='85'>\n"
                + "  <input type='range' id='forth' min='foo' max='bar'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlRangeInput first = (HtmlRangeInput) page.getElementById("first");
        final HtmlRangeInput second = (HtmlRangeInput) page.getElementById("second");
        final HtmlRangeInput third = (HtmlRangeInput) page.getElementById("third");
        final HtmlRangeInput forth = (HtmlRangeInput) page.getElementById("forth");

        assertEquals("50", first.getValueAttribute());
        assertEquals("50", first.getValue());
        assertEquals("0", second.getValueAttribute());
        assertEquals("0", second.getValue());
        assertEquals("35", third.getValueAttribute());
        assertEquals("35", third.getValue());
        assertEquals("50", forth.getValueAttribute());
        assertEquals("50", forth.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void minValidation() throws Exception {
        final String htmlContent = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='range' id='first' min='10'>\n"
                + "  <input type='range' id='second'>\n"
                + "  <input type='range' id='third' min='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlRangeInput first = (HtmlRangeInput) page.getElementById("first");
        final HtmlRangeInput second = (HtmlRangeInput) page.getElementById("second");
        final HtmlRangeInput third = (HtmlRangeInput) page.getElementById("third");

        final String defaultFirstValue = first.getValue();

        // empty
        assertTrue(first.isValid());
        // invalid
        first.setValue("foo");
        assertTrue(first.isValid());
        assertEquals(defaultFirstValue, first.getValueAttribute());
        assertEquals(defaultFirstValue, first.getValue());
        // lesser
        first.setValue("1");
        assertTrue(first.isValid());
        assertEquals("10", first.getValueAttribute());
        assertEquals("10", first.getValue());
        // equal
        first.setValue("10");
        assertTrue(first.isValid());
        assertEquals("10", first.getValueAttribute());
        assertEquals("10", first.getValue());
        // bigger
        first.setValue("100");
        assertTrue(first.isValid());
        assertEquals("100", first.getValueAttribute());
        assertEquals("100", first.getValue());

        second.setValue("0");
        assertTrue(second.isValid());
        assertEquals("0", second.getValueAttribute());
        assertEquals("0", second.getValue());

        third.setValue("0");
        assertTrue(third.isValid());
        assertEquals("0", third.getValueAttribute());
        assertEquals("0", third.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void maxValidation() throws Exception {
        final String htmlContent = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='range' id='first' max='10'>\n"
                + "  <input type='range' id='second'>\n"
                + "  <input type='range' id='third' max='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlRangeInput first = (HtmlRangeInput) page.getElementById("first");
        final HtmlRangeInput second = (HtmlRangeInput) page.getElementById("second");
        final HtmlRangeInput third = (HtmlRangeInput) page.getElementById("third");

        final String defaultFirstValue = first.getValue();

        // empty
        assertTrue(first.isValid());
        // invalid
        first.setValue("foo");
        assertTrue(first.isValid());
        assertEquals(defaultFirstValue, first.getValueAttribute());
        assertEquals(defaultFirstValue, first.getValue());
        // lesser
        first.setValue("1");
        assertTrue(first.isValid());
        assertEquals("1", first.getValueAttribute());
        assertEquals("1", first.getValue());
        // equal
        first.setValue("10");
        assertTrue(first.isValid());
        assertEquals("10", first.getValueAttribute());
        assertEquals("10", first.getValue());
        // bigger
        first.setValue("100");
        assertTrue(first.isValid());
        assertEquals("10", first.getValueAttribute());
        assertEquals("10", first.getValue());

        second.setValue("0");
        assertTrue(second.isValid());
        assertEquals("0", second.getValueAttribute());
        assertEquals("0", second.getValue());

        third.setValue("0");
        assertTrue(third.isValid());
        assertEquals("0", third.getValueAttribute());
        assertEquals("0", third.getValue());
    }
}
