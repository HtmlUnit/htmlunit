/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlStyle}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlStyleTest extends SimpleWebTestCase {

    /**
     * Verifies that a asText() returns "checked" or "unchecked" according to the state of the checkbox.
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<style type='text/css' id='testStyle'>\n"
            + "img { border: 0px }\n"
            + "</style>\n"
            + "</head><body>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final DomNode node = page.getHtmlElementById("testStyle");
        assertEquals("style", node.getNodeName());
        assertEquals("", node.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLStyleElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<style type='text/css' id='myId'>\n"
            + "img { border: 0px }\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlStyle.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * See <a href="http://sourceforge.net/support/tracker.php?aid=2802096">Bug 2802096</a>.
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<style type='text/css'></style>\n"
            + "<style type='text/css'><!-- \n"
            + "body > p { color: red }\n"
            + "--></style>\n"
            + "</head><body>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final String xml = page.asXml();
        assertTrue("Style node not expanded in: " + xml, xml.contains("</style>"));

        final String xmlWithoutSpace = xml.replaceAll("\\s", "");
        assertTrue(xml, xmlWithoutSpace.contains("<styletype=\"text/css\"><!--body>p{color:red}--></style>"));
    }
}
