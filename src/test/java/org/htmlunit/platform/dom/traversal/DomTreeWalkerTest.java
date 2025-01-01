/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.platform.dom.traversal;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlHead;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/**
 * Tests for {@link DomTreeWalker}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public final class DomTreeWalkerTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void firstChild() throws Exception {
        final String html = "<html><head><script>\n"
                + "function test() {\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "  <form name='f1'>\n"
                + "    <input>\n"
                + "    <INPUT>\n"
                + "  </form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            final TreeWalker walker = new DomTreeWalker(page.getDocumentElement(), NodeFilter.SHOW_ALL, null, true);
            final Node node = walker.firstChild();
            assertTrue(node.getClass().getName(), node instanceof HtmlHead);
        }
    }
}
