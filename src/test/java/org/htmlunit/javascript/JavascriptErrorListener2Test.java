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
package org.htmlunit.javascript;

import org.htmlunit.ScriptException;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JavaScriptErrorListener}.
 *
 * @author Ronald Brill
 */
public class JavascriptErrorListener2Test extends SimpleWebTestCase {

    /**
     * Test for running with a JavaScript error listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void parsingError() throws Exception {
        final StringBuilder scriptExceptions = new StringBuilder();

        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener() {
            @Override
            public void scriptException(final HtmlPage page, final ScriptException scriptException) {
                scriptExceptions.append(scriptException.getCause() + "\n");
            }
        });

        final String html = DOCTYPE_HTML + "<html><body><script>while (</script></body></html>";
        getMockWebConnection().setDefaultResponse(html);
        webClient.getPage(URL_FIRST);

        assertEquals("org.htmlunit.corejs.javascript.EvaluatorException: "
            + "Unexpected end of file (script in " + URL_FIRST + " from (2, 21) to (2, 37)#2)\n",
                scriptExceptions.toString());
    }
}
