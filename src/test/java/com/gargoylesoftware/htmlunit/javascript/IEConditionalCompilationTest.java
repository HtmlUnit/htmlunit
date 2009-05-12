/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Test for {@link IEConditionalCompilationScriptPreProcessor}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class IEConditionalCompilationTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "testing @cc_on", IE7 = "testing @cc_on")
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
    @Alerts(IE6 = "5.6", IE7 = "5.7")
    public void ifTest() throws Exception {
        final String script = "/*@cc_on@if(@_jscript_version>=5){alert(@_jscript_version)}@end@*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE6 = "5.6", IE7 = "5.7")
    public void variables_jscript_version() throws Exception {
        final String script = "/*@cc_on alert(@_jscript_version) @*/";
        testScript(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE6 = "6626", IE7 = "5730")
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

    private void testScript(final String script)
        throws Exception {
        final String html
            = "<html><head><title>foo</title></head>\n"
            + "<script>\n"
            + script
            + "</script>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
