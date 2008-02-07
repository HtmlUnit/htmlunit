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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase2;

/**
 * Tests for {@link ActiveXObject}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class ActiveXObjectTest extends WebTestCase2 {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testXMLHttpRequestFlavours() throws Exception {
        assertFalse(ActiveXObject.isXMLHttpRequest(null));
        assertFalse(ActiveXObject.isXMLHttpRequest("foo"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Microsoft.XMLHTTP"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.3.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.4.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.5.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.6.0"));
        assertTrue(ActiveXObject.isXMLDocument("Microsoft.XmlDom"));
        assertTrue(ActiveXObject.isXMLDocument("MSXML4.DOMDocument"));
        assertTrue(ActiveXObject.isXMLDocument("Msxml2.DOMDocument.6.0"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testXMLDocument() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    alert(doc);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
