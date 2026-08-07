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
package org.htmlunit.doc;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTextInput;
import org.junit.jupiter.api.Test;

/**
 * Tests for code from the documentation.
 *
 * @author Ronald Brill
 */
public class InputTests {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void value() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.wetator.org/testform/");
            final HtmlTextInput input = page.getHtmlElementById("project");

            // before any edit, all three agree
            System.out.println(input.getValueAttribute());  // "initial"
            System.out.println(input.getRawValue());        // "initial"
            System.out.println(input.getValue());           // "initial"

            input.type("my fancy HtmlUnit project");

            // the attribute never moved -- it is still the DEFAULT value
            System.out.println(input.getValueAttribute()); // "initial"
            // but the live value has
            System.out.println(input.getRawValue());        // "edited"
            System.out.println(input.getValue());           // "edited"
        }
    }
}
