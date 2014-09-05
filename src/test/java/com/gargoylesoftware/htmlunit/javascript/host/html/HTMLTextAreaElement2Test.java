/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for {@link HTMLTextAreaElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLTextAreaElement2Test extends SimpleWebTestCase {

    /**
     * Method type(...) should not trigger onchange!
     * @throws Exception if the test fails
     */
    @Test
    public void type_onchange() throws Exception {
        final String content
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function changed(e) {\n"
            + "    log('changed: ' + e.value)\n"
            + "  }\n"
            + "  function keypressed(e) {\n"
            + "    log('keypressed: ' + e.value)\n"
            + "  }\n"
            + "  function log(msg) {\n"
            + "    document.getElementById('log').value += msg + '; ';\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "<textarea id='textArea1' onchange='changed(this)' onkeypress='keypressed(this)'></textarea>\n"
            + "<textarea id='log'></textarea>"
            + "</form>"
            + "</body></html>";
        final HtmlPage page = loadPageWithAlerts(content);
        final HtmlTextArea textArea = page.getHtmlElementById("textArea1");
        textArea.type("hello");
        page.setFocusedElement(null); // remove focus on textarea to trigger onchange

        final HtmlTextArea log = page.getHtmlElementById("log");
        final String expectation = "keypressed: ; "
            + "keypressed: h; "
            + "keypressed: he; "
            + "keypressed: hel; "
            + "keypressed: hell; "
            + "changed: hello;";
        assertEquals(expectation, log.asText());
    }

}
