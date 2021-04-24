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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlDateTimeLocalInput}.
 *
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlDateTimeInput2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented({FF, FF78, IE})
    public void testMinValidation() throws Exception {
        final String htmlContent = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='datetime-local' id='first' min='2018-12-01T00:00'>\n"
                + "  <input type='datetime-local' id='second'>\n"
                + "  <input type='datetime-local' id='third' min='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlDateTimeLocalInput first = (HtmlDateTimeLocalInput) page.getElementById("first");
        final HtmlDateTimeLocalInput second = (HtmlDateTimeLocalInput) page.getElementById("second");
        final HtmlDateTimeLocalInput third = (HtmlDateTimeLocalInput) page.getElementById("third");

        // empty
        assertTrue(first.isValid());
        // lesser
        first.setValueAttribute("2018-11-01T00:00");
        assertFalse(first.isValid());
        // equal
        first.setValueAttribute("2018-12-01T00:00");
        assertTrue(first.isValid());
        // bigger
        first.setValueAttribute("2018-12-01T01:00");
        assertTrue(first.isValid());

        second.setValueAttribute("2018-11-01T00:00");
        assertTrue(second.isValid());
        third.setValueAttribute("2018-11-01T00:00");
        assertTrue(third.isValid());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented({FF, FF78, IE})
    public void testMaxValidation() throws Exception {
        final String htmlContent = "<html>\n" + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='datetime-local' id='first' max='2018-12-01T00:00'>\n"
                + "  <input type='datetime-local' id='second'>\n"
                + "  <input type='datetime-local' id='third' max='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlDateTimeLocalInput first = (HtmlDateTimeLocalInput) page.getElementById("first");
        final HtmlDateTimeLocalInput second = (HtmlDateTimeLocalInput) page.getElementById("second");
        final HtmlDateTimeLocalInput third = (HtmlDateTimeLocalInput) page.getElementById("third");

        // empty
        assertTrue(first.isValid());
        // lesser
        first.setValueAttribute("2018-11-01T00:00");
        assertTrue(first.isValid());
        // equal
        first.setValueAttribute("2018-12-01T00:00");
        assertTrue(first.isValid());
        // bigger
        first.setValueAttribute("2018-12-11T00:00");
        assertFalse(first.isValid());

        second.setValueAttribute("2018-12-01T00:00");
        assertTrue(second.isValid());
        third.setValueAttribute("2018-12-01T00:00");
        assertTrue(third.isValid());
    }
}
