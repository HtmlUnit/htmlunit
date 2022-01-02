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
 * Tests for {@link HtmlHeading1} to {@link HtmlHeading6}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlHeadingTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedText() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "begin"
            + "<h1>in h1</h1>after h1\n"
            + "<h2>in h2</h2>after h2\n"
            + "<h3>in h3</h3>after h3\n"
            + "<h4>in h4</h4>after h4\n"
            + "<h5>in h5</h5>after h5\n"
            + "<h6>in h6</h6>after h6\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final String expectedText = "begin\n"
            + "in h1\n"
            + "after h1\n"
            + "in h2\n"
            + "after h2\n"
            + "in h3\n"
            + "after h3\n"
            + "in h4\n"
            + "after h4\n"
            + "in h5\n"
            + "after h5\n"
            + "in h6\n"
            + "after h6";

        assertEquals(expectedText, page.asNormalizedText());
    }
}
