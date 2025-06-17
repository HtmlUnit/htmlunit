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
package org.htmlunit.html;

import org.htmlunit.SimpleWebTestCase;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BaseFrameElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class BaseFrameElementTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void removeIFrameAndAddAgain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var frag1 = document.createDocumentFragment();\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    div.innerHTML = \"<iframe id='iframe'></iframe>\";\n"
            + "    var iframe1 = document.getElementById('iframe');\n"
            + "    frag1.appendChild(iframe1);\n"
            + "    var frag2 = frag1.cloneNode(true);\n"
            + "    document.body.appendChild(frag2);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final BaseFrameElement frame = page.getHtmlElementById("iframe");
        final BaseFrameElement frame2 = frame.getEnclosedWindow().getFrameElement();
        final DomNode parent1 = frame.getParentNode();
        final DomNode parent2 = frame2.getParentNode();
        assertEquals(parent1.getClass().getName(), parent2.getClass().getName());
        assertEquals(frame.hashCode(), frame2.hashCode());
    }
}
