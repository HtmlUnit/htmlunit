/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
import java.util.List;

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
 */
public class IEConditionalCompilationTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param name The name of the test
     */
    public IEConditionalCompilationTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testSimple() throws Exception {
        final String script = "/*@cc_on alert( 'testing @cc_on' ); @*/";
        
        testScript( BrowserVersion.INTERNET_EXPLORER_6_0, script,
                new String[] {"testing @cc_on"} );
        testScript( BrowserVersion.FIREFOX_2, script,
                new String[] {} );
    }
    
    /**
     * @throws Exception If the test fails.
     */
    public void testReservedString() throws Exception {
        final String script = "/*@cc_on alert( 'testing /*@cc_on' ); @*/";
        
        testScript( BrowserVersion.INTERNET_EXPLORER_6_0, script,
                new String[] {"testing /*@cc_on"} );
        testScript( BrowserVersion.FIREFOX_2, script,
                new String[] {} );
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testSet() throws Exception {
        if( notYetImplemented() ) {
            return;
        }
        
        final String script = "/*@cc_on @set @mine = 12 alert( @mine ); @*/";
        
        testScript( BrowserVersion.INTERNET_EXPLORER_6_0, script,
                new String[] {"12"} );
        testScript( BrowserVersion.FIREFOX_2, script,
                new String[] {} );
    }

    private void testScript( final BrowserVersion browserVersion, final String script, final String[] expectedAlerts )
        throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<script>\n"
            + script
            + "</script>\n"
            + "<body>\n"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);
        client.getPage( URL_FIRST );
        assertEquals( expectedAlerts, collectedAlerts );
    }
    
}
