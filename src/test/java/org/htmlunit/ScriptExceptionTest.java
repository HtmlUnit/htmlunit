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

import static org.junit.jupiter.api.Assertions.fail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ScriptException}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public final class ScriptExceptionTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void constructor() throws Exception {
        final String message = "bla bla";
        final Throwable t = new RuntimeException(message);
        final HtmlPage page = loadPage(DOCTYPE_HTML + "<html></html>");
        final ScriptException exception = new ScriptException(page, t);

        assertEquals(t, exception.getCause());
        assertEquals(message, exception.getMessage());
    }

    /**
     * Tests access to the page where the exception occurred from the exception.
     * @throws Exception if the test fails
     */
    @Test
    public void getPage() throws Exception {
        final String html = DOCTYPE_HTML + "<html><script>notExisting()</script></html>";
        try {
            loadPage(html);
        }
        catch (final ScriptException e) {
            assertEquals(URL_FIRST, e.getPage().getUrl());
        }
    }

    /**
     * Test the JavaScript stacktrace.
     * Note: this test will fail when the information (line, col, source name) provided to execute
     * scripts is improved. In this case this unit test should be adapted IF the provided information
     * in the script stack trace gets better and provides more useful information to understand the
     * cause of a problem.
     * @throws Exception if the test fails
     */
    @Test
    public void scriptStackTrace() throws Exception {
        testScriptStackTrace("ScriptExceptionTest1");
        testScriptStackTrace("ScriptExceptionTest2");
    }

    private void testScriptStackTrace(final String baseFileName) throws Exception {
        try {
            loadPage(getBrowserVersion(), getFileContent(baseFileName + ".html"), null,
                new URL("http://www.gargoylesoftware.com/"));
        }
        catch (final ScriptException e) {
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printScriptStackTrace(printWriter);

            final String expectedTrace = getFileContent(baseFileName + ".txt");
            assertEquals(expectedTrace, stringWriter.toString());
            return;
        }
        fail("Exception not thrown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void errorLineNumber() throws Exception {
        testErrorLineNumber("testJsError1.html", 6);
        testErrorLineNumber("testJsError2.html", 7);
        testErrorLineNumber("testJsError3.html", 6);
        testErrorLineNumber("testJsError4.html", 8);
    }

    private void testErrorLineNumber(final String fileName, final int errorLine) throws Exception {
        final WebClient webClient = getWebClient();
        final URL url = getClass().getClassLoader().getResource(fileName);
        assertNotNull(url);
        try {
            final HtmlPage page = webClient.getPage(url);
            page.getHtmlElementById("clickMe").click();
            fail();
        }
        catch (final ScriptException e) {
            assertEquals(errorLine, e.getFailingLineNumber());
        }
    }
}
