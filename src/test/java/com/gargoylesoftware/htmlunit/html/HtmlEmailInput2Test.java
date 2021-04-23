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
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlEmailInput}.
 *
 * @author Ronald Brill
 * @author Anton Demydenko
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
        assertEquals("abc@email.com", input.getValueAttribute());
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

        assertEquals("xyz@email.com", input.getValueAttribute());
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

        assertEquals("xyz@email.com", input.getValueAttribute());
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
        input.setValueAttribute("abc@eemail.com");
        assertFalse(input.isValid());
        // valid
        input.setValueAttribute("abc@email.com");
        assertTrue(input.isValid());
    }
}
