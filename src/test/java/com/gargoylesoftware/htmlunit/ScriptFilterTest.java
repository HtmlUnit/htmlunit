/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for ScriptFilter
 * @version $Revision$
 * @author <a href="mailto:chen_jun@users.sourceforge.net"> Chen Jun </a>
 */
public class ScriptFilterTest extends WebTestCase {

    /**
     * Create an instance.
     * @param name The name of the test
     */
    public ScriptFilterTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testScriptForEvent() throws Exception {
        // IE accepts it with () or without
        testScriptForEvent("onload");
        testScriptForEvent("onload()");
    }

    private void testScriptForEvent(final String eventName) throws Exception {
        final String content
            = "<html><head><title>foo</title>"
            + "<script FOR='window' EVENT='" + eventName + "' LANGUAGE='javascript'>"
            + " document.form1.txt.value='hello';"
            + " alert(document.form1.txt.value);"
            + "</script></head><body>"
            + "<form name='form1'><input type=text name='txt'></form></body></html>";
        final List collectedAlerts = new ArrayList();

        final List expectedAlerts = Arrays.asList( new String[]{
            "hello"
        });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(content, collectedAlerts);
        assertEquals( expectedAlerts, collectedAlerts );
    }
}
