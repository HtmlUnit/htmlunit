/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Test for IE weird JavaScript syntax.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class IEWeirdSyntaxTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = { "1", "2" })
    public void semicolon_before_finally() throws Exception {
        doTestTryCatchFinally("", ";");
        doTestTryCatchFinally("", "\n;\n");
        doTestTryCatchFinally("", "\n;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = { "1", "2" })
    public void semicolon_before_catch() throws Exception {
        doTestTryCatchFinally(";", "");
        doTestTryCatchFinally("\n;\n", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = { "1", "2" })
    public void semicolonAndComment_before_catchAndFinally() throws Exception {
        doTestTryCatchFinally("// comment\n;\n", "");
        doTestTryCatchFinally("", "// comment\n;\n");
        doTestTryCatchFinally("", "// comment\n // other comment\n;\n");
        doTestTryCatchFinally("", "// comment\n ; // other comment\n");
    }

    private void doTestTryCatchFinally(final String beforeCatch, final String beforeFinally) throws Exception {
        final String html = "<html><script>\n"
            + "try {\n"
            +  "alert('1');\n"
            +  "}" + beforeCatch
            +  "catch(e) {\n"
            +  "}" + beforeFinally
            +  "finally {\n"
            +  "alert('2');\n"
            +  "}\n"
            +  "</script></html>";
        doTestWithEvaluatorExceptionExceptForIE(html);
    }

    private void doTestWithEvaluatorExceptionExceptForIE(final String html) throws Exception {
        try {
            loadPageWithAlerts(html);
        }
        catch (final ScriptException e) {
            if (e.getCause() instanceof EvaluatorException && !getBrowserVersion().isIE()) {
                // this is normal
            }
            else {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    public void windowDotHandlerFunction() throws Exception {
        final String html = "<html><head><script>\n"
            + "function window.onload() {\n"
            + "  alert(1);\n"
            + "}\n"
            + "</script></head>"
            + "<body></body></html>";
        doTestWithEvaluatorExceptionExceptForIE(html);
    }
}
