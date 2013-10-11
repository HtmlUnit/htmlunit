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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE10;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Test for {@link IEConditionalCompilationScriptPreProcessor}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Adam Doupe
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class IEConditionalCompilationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "testing @cc_on")
    public void simple() throws Exception {
        final String script = "/*@cc_on alert('testing @cc_on'); @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void simple2() throws Exception {
        final String script = "var a={b:/*@cc_on!@*/false,c:/*@cc_on!@*/false};\n"
            + "var foo = (1 + 2/*V*/);\n"
            + "alert(foo)";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "testing @cc_on")
    public void simple3() throws Exception {
        final String script = "/*@cc_on @*/\n"
            + "/*@if (@_win32)\n"
            + "alert('testing @cc_on');\n"
            + "/*@end @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "1", "testing @cc_on" })
    //TODO: fails with IE8 and IE10 with WebDriver, but succeeds manually
    @BuggyWebDriver({ IE8, IE10 })
    public void simple4() throws Exception {
        final String script = "/*@cc_on alert(1) @*/\n"
            + "/*@if (@_win32)\n"
            + "alert('testing @cc_on');\n"
            + "/*@end @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = "5.8",
            IE10 = "10")
    public void ifTest() throws Exception {
        final String script = "/*@cc_on@if(@_jscript_version>=5){alert(@_jscript_version)}@end@*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = "5.8",
            IE10 = "10")
    public void variables_jscript_version() throws Exception {
        final String script = "/*@cc_on alert(@_jscript_version) @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = "18702",
            IE10 = "16660")
    public void variables_jscript_build() throws Exception {
        final String script = "/*@cc_on alert(@_jscript_build) @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "testing /*@cc_on")
    public void reservedString() throws Exception {
        final String script = "/*@cc_on alert('testing /*@cc_on'); @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "12")
    public void set() throws Exception {
        final String script = "/*@cc_on @set @mine = 12 alert(@mine); @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "win")
    public void elif() throws Exception {
        final String script = "/*@cc_on @if(@_win32)type='win';@elif(@_mac)type='mac';@end alert(type); @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "$2")
    public void dollar_single_quote_in_string() throws Exception {
        final String script = "/*@cc_on var test='$2'; alert(test);@*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "$2")
    public void dollar_double_quote_in_string() throws Exception {
        final String script = "/*@cc_on var test=\"$2\"; alert(test);@*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "\\")
    public void slashes_in_single_quotes() throws Exception {
        final String script = "/*@cc_on var test='\\\\\'; alert(test);@*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "$")
    public void slash_dollar_in_single_quotes() throws Exception {
        final String script = "/*@cc_on var test='\\$\'; alert(test);@*/";
        testScript(script);
    }

    private void testScript(final String script) throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + script + "\n"
            + "</script>\n"
            + "</head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "false",
            IE = "true")
    public void escaping() throws Exception {
        final String script = "var isMSIE=eval('false;/*@cc_on@if(@\\x5fwin32)isMSIE=true@end@*/');\n"
            + "alert(isMSIE);";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "false",
            IE = "true")
    public void eval() throws Exception {
        final String script =
            "var isMSIE;\n"
            + "eval('function f() { isMSIE=eval(\"false;/*@cc_on@if(@' + '_win32)isMSIE=true@end@*/\") }');\n"
            + "f();\n"
            + "alert(isMSIE);";
        testScript(script);
    }

    /**
     * Regression test for bug 3076667.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "Alert")
    public void bug3076667() throws Exception {
        final String script =
            "/*@cc_on @*/\n"
            + "/*@if (true) alert('Alert');\n"
            + "@end @*/ ";
        testScript(script);
    }

    /**
    * As of HtmlUnit-2.9, escaped double quote \" was altered.
    * @throws Exception if the test fails
    */
    @Test
    @Alerts(IE = "1")
    public void escapedDoubleQuote() throws Exception {
        final String script =
            "/*@cc_on\n"
            + "document.write(\"\\\"\\\"\");\n"
            + "alert(1)\n"
            + "@*/\n"
            + "</script></html>";

        testScript(script);
    }
}
