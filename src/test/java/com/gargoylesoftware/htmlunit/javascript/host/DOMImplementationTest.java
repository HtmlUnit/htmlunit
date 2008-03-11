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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DOMImplementation}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class DOMImplementationTest extends WebTestCase {

    /**
     * @throws Exception If the test fails
     */
    @Test
    public void testGetFeature() throws Exception {
        testGetFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "HTML", "1.0", true);
        testGetFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "HTML", "2.0", false);
        testGetFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "XML", "1.0", false);
        testGetFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "CSS2", "1.0", false);
        testGetFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "XPath", "3.0", false);
        testGetFeature(BrowserVersion.FIREFOX_2, "HTML", "1.0", true);
        testGetFeature(BrowserVersion.FIREFOX_2, "HTML", "2.0", true);
        testGetFeature(BrowserVersion.FIREFOX_2, "HTML", "3.0", false);
        testGetFeature(BrowserVersion.FIREFOX_2, "XML", "1.0", true);
        testGetFeature(BrowserVersion.FIREFOX_2, "XML", "2.0", true);
        testGetFeature(BrowserVersion.FIREFOX_2, "XML", "3.0", false);
        testGetFeature(BrowserVersion.FIREFOX_2, "CSS2", "1.0", false);
        testGetFeature(BrowserVersion.FIREFOX_2, "CSS2", "2.0", true);
        testGetFeature(BrowserVersion.FIREFOX_2, "CSS2", "3.0", false);
        testGetFeature(BrowserVersion.FIREFOX_2, "XPath", "3.0", true);
    }

    private void testGetFeature(final BrowserVersion browserVersion, final String feature, final String version,
            final boolean expected) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.implementation.hasFeature('" + feature + "', '" + version + "'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);

        assertEquals(Boolean.toString(expected), collectedAlerts.get(0));
    }
    
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testCreateDocument() throws Exception {
        testCreateDocument(BrowserVersion.FIREFOX_2);
        try {
            testCreateDocument(BrowserVersion.INTERNET_EXPLORER_7_0);
            fail("document.implementation.createDocument is not supported in IE.");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testCreateDocument(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    alert(doc);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object XMLDocument]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testCreateDocument_qualifiedName() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', 'mydoc', null);\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"mydoc"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
