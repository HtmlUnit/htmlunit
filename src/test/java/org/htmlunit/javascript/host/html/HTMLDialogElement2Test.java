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
package org.htmlunit.javascript.host.html;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlDialog;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link HTMLDialogElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDialogElement2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void formClosesDialogWithoutJs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <dialog id='tester' onclose='document.title=\"closed\"'>\n"
            + "      <p>HtmlUNit dialog</p>\n"
            + "      <form method='dialog'>\n"
            + "        <input type='submit' id='close' value='OK' />\n"
            + "      </form>\n"
            + "    </dialog>\n"
            + "  </body>\n"
            + "</html>";

        final WebClient client = getWebClientWithMockWebConnection();
        client.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page = loadPage(html);
        final DomElement elem = page.getElementById("tester");
        if (elem instanceof HtmlDialog) {
            final HtmlDialog dialog = (HtmlDialog) elem;

            assertFalse(dialog.isOpen());
            dialog.show();
            assertTrue(dialog.isOpen());

            page.getElementById("close").click();
            assertFalse(dialog.isOpen());

            assertEquals("", page.getTitleText());
        }
    }
}
