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
package org.htmlunit;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ScriptPreProcessor}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Chris Erskine
 * @author Hans Donner
 * @author Paul King
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Sudhan Moghe
 */
public class ScriptPreProcessorTest extends WebServerTestCase {

    /**
     * Test the script preprocessor.
     * @throws IOException if the test fails
     */
    @Test
    public void scriptPreProcessor() throws IOException {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final String alertText = "content";
        final String newAlertText = "newcontent";
        final String content = DOCTYPE_HTML
            + "<html><head><title>foo</title><script>\n"
            + "<!--\n   alert('" + alertText + "');\n// -->\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "  <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        // Test null return from pre processor
        client.setScriptPreProcessor(new ScriptPreProcessor() {
            @Override
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                    final int lineNumber, final HtmlElement htmlElement) {
                return null;
            }
        });
        client.setAlertHandler(new AlertHandler() {
            @Override
            public void handleAlert(final Page page, final String message) {
                fail("The pre processor did not remove the JavaScript");
            }

        });
        client.getPage(URL_FIRST);

        // Test modify script in pre processor
        client.setScriptPreProcessor(new ScriptPreProcessor() {
            @Override
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                    final int lineNumber, final HtmlElement htmlElement) {
                final int start = sourceCode.indexOf(alertText);
                final int end = start + alertText.length();

                return sourceCode.substring(0, start) + newAlertText + sourceCode.substring(end);
            }
        });
        client.setAlertHandler(new AlertHandler() {
            @Override
            public void handleAlert(final Page page, final String message) {
                if (!message.equals(newAlertText)) {
                    fail("The pre processor did not modify the JavaScript");
                }
            }

        });
        client.getPage(URL_FIRST);
    }

    /**
     * Test the ScriptPreProcessor's ability to filter out a JavaScript method
     * that is not implemented without affecting the rest of the page.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void scriptPreProcessor_UnimplementedJavascript() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final String content = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<script>document.unimplementedFunction();</script>\n"
            + "<script>alert('implemented function');</script>\n"
            + "</body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        client.setScriptPreProcessor(new ScriptPreProcessor() {
            @Override
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                    final int lineNumber, final HtmlElement htmlElement) {
                if (sourceCode.indexOf("unimplementedFunction") > -1) {
                    return "";
                }
                return sourceCode;
            }
        });
        final List<String> alerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        client.getPage("http://page");

        assertEquals(1, alerts.size());
        assertEquals("implemented function", alerts.get(0).toString());
    }

    /**
     * Verifies that script preprocessing is applied to eval()'ed scripts (bug 2630555).
     * @throws Exception if the test fails
     */
    @Test
    public void scriptPreProcessor_Eval() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>eval('aX'+'ert(\"abc\")');</script></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);

        client.setScriptPreProcessor(new ScriptPreProcessor() {
            @Override
            public String preProcess(final HtmlPage p, final String src, final String srcName,
                    final int lineNumber, final HtmlElement htmlElement) {
                return src.replaceAll("aXert", "alert");
            }
        });

        final List<String> alerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        client.getPage(URL_FIRST);

        assertEquals(1, alerts.size());
        assertEquals("abc", alerts.get(0));
    }
}
