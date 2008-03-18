/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
     * @throws Exception If the test fails.
     */
    @Test
    @Alerts(IE6 = "testing @cc_on", IE7 = "testing @cc_on", FF2 = { })
    public void simple() throws Exception {
        final String script = "/*@cc_on alert('testing @cc_on'); @*/";
        testScript(script);
    }

    /**
     * @throws Exception If the test fails.
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
     * @throws Exception If the test fails.
     */
    @Test
    @Alerts(IE6 = "5.6", IE7 = "5.7", FF2 = { })
    public void ifTest() throws Exception {
        final String script = "/*@cc_on@if(@_jscript_version>=5){alert(@_jscript_version)}@end@*/";
        testScript(script);
    }
    
    /**
     * @throws Exception If the test fails.
     */
    @Test
    @Alerts(IE6 = "5.6", IE7 = "5.7", FF2 = { })
    public void variables_jscript_version() throws Exception {
        final String script = "/*@cc_on alert(@_jscript_version) @*/";
        testScript(script);
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    @Alerts(IE6 = "6626", IE7 = "5730", FF2 = { })
    public void variables_jscript_build() throws Exception {
        final String script = "/*@cc_on alert(@_jscript_build) @*/";
        testScript(script);
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    @Alerts(IE6 = "testing /*@cc_on", IE7 = "testing /*@cc_on", FF2 = { })
    public void reservedString() throws Exception {
        final String script = "/*@cc_on alert('testing /*@cc_on'); @*/";
        testScript(script);
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    @Alerts(IE6 = "12", IE7 = "12", FF2 = { })
    public void set() throws Exception {
        final String script = "/*@cc_on @set @mine = 12 alert(@mine); @*/";
        testScript(script);
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    @Alerts(IE6 = "win", IE7 = "win", FF2 = { })
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

        loadWithAlerts(html);
    }
}
