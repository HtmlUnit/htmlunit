/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebWindowListener}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebWindowListenerTest extends SimpleWebTestCase {

    /**
     * Testcase for issue #1101.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void eventOrder() throws Exception {
        final String firstHtml
            = "<html>\n"
            + "<head><title>Test 1</title></head>\n"
            + "<body>\n"
            + "<script type='text/javascript'>\n"
            + "  document.location.href='" + URL_SECOND + "';\n"
            + "</script>\n"
            + "<p>Second Page<p>\n"
            + "</body></html>";

        final String secondHtml
            = "<html>\n"
            + "<head><title>Test 2</title></head>\n"
            + "<body><p>Second Page<p></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final StringBuilder msg = new StringBuilder();
        getWebClient().addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                msg.append("changed '" + ((HtmlPage) event.getNewPage()).getTitleText() + "'; ");
            }
            @Override
            public void webWindowClosed(final WebWindowEvent event) {
            }
        });

        loadPage(firstHtml);
        assertEquals("changed 'Test 1'; changed 'Test 2'; ", msg.toString());
    }

}
