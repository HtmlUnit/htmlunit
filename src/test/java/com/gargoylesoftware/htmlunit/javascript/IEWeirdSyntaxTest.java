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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Test for IE weird JavaScript syntax (supported by IE8).
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class IEWeirdSyntaxTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void semicolon_before_finally() throws Exception {
        doTestTryCatchFinally("", ";");
        doTestTryCatchFinally("", "\n;\n");
        doTestTryCatchFinally("", "\n;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void semicolon_before_catch() throws Exception {
        doTestTryCatchFinally(";", "");
        doTestTryCatchFinally("\n;\n", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void semicolonAndComment_before_catchAndFinally() throws Exception {
        doTestTryCatchFinally("// comment\n;\n", "");
        doTestTryCatchFinally("", "// comment\n;\n");
        doTestTryCatchFinally("", "// comment\n // other comment\n;\n");
        doTestTryCatchFinally("", "// comment\n ; // other comment\n");
    }

    private void doTestTryCatchFinally(final String beforeCatch, final String beforeFinally) throws Exception {
        final String html = "<html>\n"
            + "<script>\n"
            + "  try {\n"
            + "    alert('1');\n"
            + "  }" + beforeCatch
            + "  catch(e) {\n"
            + "    alert('2');\n"
            + "  }" + beforeFinally
            + "  finally {\n"
            + "    alert('3');\n"
            + "  }\n"
            + "</script>\n"
            + "</html>";
        doTestWithEvaluatorExceptionExcept(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void windowDotHandlerFunction() throws Exception {
        final String html = "<html><head><script>\n"
            + "function window.onload() {\n"
            + "  alert(1);\n"
            + "}\n"
            + "</script></head>"
            + "<body></body></html>";
        doTestWithEvaluatorExceptionExcept(html);
    }

    private void doTestWithEvaluatorExceptionExcept(final String html) throws Exception {
        try {
            loadPageWithAlerts2(html);
        }
        catch (final Exception e) {
            if (!(e.getCause() instanceof ScriptException)) {
                throw e;
            }
        }
    }
}
