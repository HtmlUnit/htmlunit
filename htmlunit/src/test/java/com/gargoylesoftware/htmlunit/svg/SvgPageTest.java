/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.svg;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for {@link SvgPage}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SvgPageTest extends WebServerTestCase {

    /**
     * Tests namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void namespace() throws Exception {
        final String content
            = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<svg xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "  <rect id=\"rect\" width=\"50\" height=\"50\" fill=\"green\""
            + " onclick=\"alert(document.getElementById('rect'))\"/>\n"
            + "</svg>";

        final SgmlPage page = testDocument(content, "text/xml");
        final Node root = page.getDocumentElement();
        assertEquals("svg", root.getNodeName());
        assertEquals("http://www.w3.org/2000/svg", root.getNamespaceURI());
    }

    /**
     * Utility method to test SVG page of different MIME types.
     * @param content the SVG content
     * @param mimeType the MIME type
     * @return the page returned by the WebClient
     * @throws Exception if a problem occurs
     */
    private SgmlPage testDocument(final String content, final String mimeType) throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content, 200, "OK", mimeType);
        client.setWebConnection(webConnection);
        final SgmlPage page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals(mimeType, page.getWebResponse().getContentType());
        assertTrue(SvgPage.class.isInstance(page));
        assertEquals(content, page.getWebResponse().getContentAsString());
        return page;
    }
}
