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

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlSubmitInput}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlSubmitInput2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void defaultValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form action='foo.html'>\n"
            + "  <input type='submit' id='myId'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(page.asNormalizedText().indexOf("Submit Query") > -1);
        assertFalse(page.asXml().indexOf("Submit Query") > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void emptyValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "  <input type='submit' id='myId' value=''>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertFalse(page.asNormalizedText().indexOf("Submit Query") > -1);
        assertTrue(page.asXml().indexOf("value=\"\"") > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void onclick() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<form>\n"
            + "  <input id='myInput'>\n"
            + "  <input type='submit' onclick='alert(1)'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        page.getHtmlElementById("myInput").type('\n');

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test that attributes values are not escaped
     * See bug 3074609.
     * @throws Exception if the test fails
     */
    @Test
    public void asXmlNoEscape() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<meta http-equiv='Content-Type' content='text/html; charset=Cp1251'>\n"
            + "</head><body>\n"
            + "<input type='submit' value='&#1083;'/>\n"
            + "<input type='reset' value='&#1083;'/>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final String xml = page.asXml();
        assertTrue("\u043B" + xml, xml.contains("<input type=\"submit\" value=\"\u043B\"/>"));
        assertTrue(xml, xml.contains("<input type=\"reset\" value=\"\u043B\"/>"));
    }
}
