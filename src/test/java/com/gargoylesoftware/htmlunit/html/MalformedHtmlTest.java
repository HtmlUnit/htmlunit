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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Set of tests for ill formed HTML code.
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class MalformedHtmlTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "in test", "BODY" })
    public void testBodyAttributeWhenOpeningBodyGenerated() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function test(){\n"
            + "    alert('in test');\n"
            + "    alert(document.getElementById('span1').parentNode.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "<span id='span1'>hello</span>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts({"2", "3", "text3", "null" })
    public void testLostFormChildren() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function test(){\n"
            + "    alert(document.forms[0].childNodes.length);\n"
            + "    alert(document.forms[0].elements.length);\n"
            + "    alert(document.forms[0].elements[2].name);\n"
            + "    alert(document.getElementById('text4').form);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div>\n"
            + "<form action='foo'>\n"
            + "<input type='text' name='text1'/>\n"
            + "<input type='text' name='text2'/>\n"
            + "</div>\n"
            + "<input type='text' name='text3'/>\n"
            + "</form>\n"
            + "<input type='text' name='text4' id='text4'/>\n"
            + "</body></html>";

        loadPageWithAlerts(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts("Test document")
    public void testTitleAfterInsertedBody() throws Exception {
        final String content = "<html><head>"
            + "<noscript><link href='other.css' rel='stylesheet' type='text/css'></noscript>"
            + "<title>Test document</title>"
            + "</head><body onload='alert(document.title)'>\n"
            + "foo"
            + "</body></html>";

        loadPageWithAlerts(content);
    }
}
