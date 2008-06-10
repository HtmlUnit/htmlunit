/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
     * @throws Exception if the test fails
     */
    @Test
    public void getFeature() throws Exception {
        getFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "HTML", "1.0", true);
        getFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "HTML", "2.0", false);
        getFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "XML", "1.0", false);
        getFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "CSS2", "1.0", false);
        getFeature(BrowserVersion.INTERNET_EXPLORER_7_0, "XPath", "3.0", false);
        getFeature(BrowserVersion.FIREFOX_2, "HTML", "1.0", true);
        getFeature(BrowserVersion.FIREFOX_2, "HTML", "2.0", true);
        getFeature(BrowserVersion.FIREFOX_2, "HTML", "3.0", false);
        getFeature(BrowserVersion.FIREFOX_2, "XML", "1.0", true);
        getFeature(BrowserVersion.FIREFOX_2, "XML", "2.0", true);
        getFeature(BrowserVersion.FIREFOX_2, "XML", "3.0", false);
        getFeature(BrowserVersion.FIREFOX_2, "CSS2", "1.0", false);
        getFeature(BrowserVersion.FIREFOX_2, "CSS2", "2.0", true);
        getFeature(BrowserVersion.FIREFOX_2, "CSS2", "3.0", false);
        getFeature(BrowserVersion.FIREFOX_2, "XPath", "3.0", true);
    }

    private void getFeature(final BrowserVersion browserVersion, final String feature, final String version,
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
    public void createDocument() throws Exception {
        createDocument(BrowserVersion.FIREFOX_2);
        try {
            createDocument(BrowserVersion.INTERNET_EXPLORER_7_0);
            fail("document.implementation.createDocument is not supported in IE.");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void createDocument(final BrowserVersion browserVersion) throws Exception {
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
    public void createDocument_qualifiedName() throws Exception {
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
