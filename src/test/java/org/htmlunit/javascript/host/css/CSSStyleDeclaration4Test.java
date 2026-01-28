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
package org.htmlunit.javascript.host.css;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CSSStyleDeclaration}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class CSSStyleDeclaration4Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void serialize() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var node = document.getElementById('div1');\n"
            + "  var style = node.style;\n"
            + "  alert(style.color);\n"
            + "  style.color = 'pink';\n"
            + "  alert(style.color);\n"
            + "  alert(node.getAttribute('style'));\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'><div id='div1' style='color: black'>foo</div></body></html>";

        final HtmlPage page = loadPage(html);
        clone(page.getWebClient());
    }
}
