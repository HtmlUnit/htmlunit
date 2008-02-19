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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Test for {@link IEConditionalCompilationScriptPreProcessor}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class IEConditionalCompilationTest {

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void simple() throws Exception {
        final String script = "/*@cc_on alert('testing @cc_on'); @*/";
        
        testScript(BrowserVersion.INTERNET_EXPLORER_6_0, script,
                new String[] {"testing @cc_on"});
        testScript(BrowserVersion.FIREFOX_2, script,
                new String[] {});
        
        final String script2 = "var a={b:/*@cc_on!@*/false,c:/*@cc_on!@*/false};\n"
            + "var foo = (1 + 2/*V*/);\n"
            + "alert(foo)";
        
        testScript(BrowserVersion.INTERNET_EXPLORER_6_0, script2,
                new String[] {"3"});
        testScript(BrowserVersion.FIREFOX_2, script2,
                new String[] {"3"});
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void ifTest() throws Exception {
        final String script = "/*@cc_on@if(@_jscript_version>=5){alert(@_jscript_version)}@end@*/";
        
        testScript(BrowserVersion.INTERNET_EXPLORER_6_0, script, new String[] {"5.6"});
        testScript(BrowserVersion.FIREFOX_2, script, new String[] {});
    }
    
    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void variables_jscript_version() throws Exception {
        final String script = "/*@cc_on alert(@_jscript_version) @*/";
        
        testScript(BrowserVersion.INTERNET_EXPLORER_6_0, script, new String[] {"5.6"});
        testScript(BrowserVersion.INTERNET_EXPLORER_7_0, script, new String[] {"5.7"});
        testScript(BrowserVersion.FIREFOX_2, script, new String[] {});
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void variables_jscript_build() throws Exception {
        final String script = "/*@cc_on alert(@_jscript_build) @*/";
        
        testScript(BrowserVersion.INTERNET_EXPLORER_6_0, script, new String[] {"6626"});
        testScript(BrowserVersion.INTERNET_EXPLORER_7_0, script, new String[] {"5730"});
        testScript(BrowserVersion.FIREFOX_2, script, new String[] {});
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void reservedString() throws Exception {
        final String script = "/*@cc_on alert('testing /*@cc_on'); @*/";
        
        testScript(BrowserVersion.INTERNET_EXPLORER_6_0, script,
                new String[] {"testing /*@cc_on"});
        testScript(BrowserVersion.FIREFOX_2, script,
                new String[] {});
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void set() throws Exception {
        final String script = "/*@cc_on @set @mine = 12 alert(@mine); @*/";
        
        testScript(BrowserVersion.INTERNET_EXPLORER_6_0, script,
                new String[] {"12"});
        testScript(BrowserVersion.FIREFOX_2, script,
                new String[] {});
    }

    private void testScript(final BrowserVersion browserVersion, final String script, final String[] expectedAlerts)
        throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<script>\n"
            + script
            + "</script>\n"
            + "<body>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);
        client.getPage(WebTestCase.URL_FIRST);
        Assert.assertEquals(Arrays.asList(expectedAlerts), collectedAlerts);
    }
    
}
