/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
 */
@RunWith(BrowserRunner.class)
public class HtmlEmailInput2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typing() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='email' id='foo'>\n"
            + "  <input type='email' id='foo2' value='x@email.com' >\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        HtmlEmailInput emailInput = (HtmlEmailInput) page.getElementById("foo");
        emailInput.focus();
        emailInput.type("my@email.com");

        emailInput = (HtmlEmailInput) page.getElementById("foo2");
        emailInput.focus();
        emailInput.type("abc");
        assertEquals("abcx@email.com", emailInput.getValueAttribute());

        emailInput.setSelectionStart(4);
        emailInput.type("xx");
        assertEquals("abcxxx@email.com", emailInput.getValueAttribute());

        emailInput.setSelectionStart(1);
        emailInput.type("y");
        assertEquals("ay@email.com", emailInput.getValueAttribute());
    }
}
