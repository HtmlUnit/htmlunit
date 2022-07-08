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
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser;

/**
 * Tests for {@link HtmlTimeInput}.
 *
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlTimeInput2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(TestedBrowser.IE)
    public void minValidation() throws Exception {
        final String htmlContent = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='time' id='first' min='09:00'>\n"
                + "  <input type='time' id='second'>\n"
                + "  <input type='time' id='third' min='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlTimeInput first = (HtmlTimeInput) page.getElementById("first");
        final HtmlTimeInput second = (HtmlTimeInput) page.getElementById("second");
        final HtmlTimeInput third = (HtmlTimeInput) page.getElementById("third");

        // empty
        assertTrue(first.isValid());
        // lesser
        first.setValue("08:00");
        assertFalse(first.isValid());
        // equal
        first.setValue("09:00");
        assertTrue(first.isValid());
        // bigger
        first.setValue("10:00");
        assertTrue(first.isValid());

        second.setValue("09:00");
        assertTrue(second.isValid());
        third.setValue("09:00");
        assertTrue(third.isValid());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(TestedBrowser.IE)
    public void maxValidation() throws Exception {
        final String htmlContent = "<html>\n" + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='time' id='first' max='09:00'>\n"
                + "  <input type='time' id='second'>\n"
                + "  <input type='time' id='third' max='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlTimeInput first = (HtmlTimeInput) page.getElementById("first");
        final HtmlTimeInput second = (HtmlTimeInput) page.getElementById("second");
        final HtmlTimeInput third = (HtmlTimeInput) page.getElementById("third");

        // empty
        assertTrue(first.isValid());
        // lesser
        first.setValue("08:00");
        assertTrue(first.isValid());
        // equal
        first.setValue("09:00");
        assertTrue(first.isValid());
        // bigger
        first.setValue("10:00");
        assertFalse(first.isValid());

        second.setValue("09:00");
        assertTrue(second.isValid());
        third.setValue("09:00");
        assertTrue(third.isValid());
    }
}
