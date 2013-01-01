/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for {@link XHtmlPage}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class XHtmlPageTest extends SimpleWebTestCase {

    /**
     * Regression test for bug 2515873. Originally located in {@link com.gargoylesoftware.htmlunit.xml.XmlPageTest}.
     * @throws Exception if an error occurs
     */
    @Test
    public void xpath1() throws Exception {
        final String html
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE html PUBLIC \n"
            + "    \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:xhtml='http://www.w3.org/1999/xhtml'>\n"
            + "<body><DIV>foo</DIV></body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(getDefaultUrl(), html, "text/xml");
        client.setWebConnection(conn);

        final XHtmlPage page = client.getPage(getDefaultUrl());
        final DomNode body = page.getDocumentElement().getFirstChild().getNextSibling();
        final DomNode div = body.getFirstChild();

        assertEquals(HtmlBody.class, body.getClass());
        assertEquals("body", body.getLocalName());
        assertEquals("DIV", div.getLocalName());
        assertNotNull(page.getFirstByXPath(".//xhtml:body"));
        assertNotNull(page.getFirstByXPath(".//xhtml:DIV"));
        assertNull(page.getFirstByXPath(".//xhtml:div"));
    }

    /**
     * Tests a simplified real-life response from Ajax4jsf. Originally located in
     * {@link com.gargoylesoftware.htmlunit.xml.XmlPageTest}.
     * @throws Exception if an error occurs
     */
    @Test
    public void a4jResponse() throws Exception {
        final String content = "<html xmlns='http://www.w3.org/1999/xhtml'><head>"
            + "<script src='//:'></script>"
            + "</head><body><span id='j_id216:outtext'>Echo Hello World</span></body></html>";
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content, 200, "OK", "text/xml");
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals("text/xml", page.getWebResponse().getContentType());
        assertTrue(XHtmlPage.class.isInstance(page));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xpath2() throws Exception {
        final String html =
              "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:xhtml='http://www.w3.org/1999/xhtml'>\n"
            + "<body><xhtml:div>foo</xhtml:div></body></html>";

        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html, 200, "OK", "application/xhtml+xml");

        final WebClient client = getWebClient();
        client.setWebConnection(conn);
        final XHtmlPage page = client.getPage(URL_FIRST);

        assertEquals(1, page.getByXPath("//:body").size());
        assertEquals(0, page.getByXPath("//:BODY").size());
        assertEquals(0, page.getByXPath("//:bOdY").size());

        assertEquals(1, page.getByXPath("//xhtml:body").size());
        assertEquals(0, page.getByXPath("//xhtml:BODY").size());
        assertEquals(0, page.getByXPath("//xhtml:bOdY").size());

        assertEquals(1, page.getByXPath("//xhtml:div").size());
        assertEquals(0, page.getByXPath("//xhtml:DIV").size());
        assertEquals(0, page.getByXPath("//xhtml:dIv").size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void namespace() throws Exception {
        final String html =
              "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:xhtml='http://www.w3.org/1999/xhtml'>\n"
            + "<body><div>foo</div></body></html>";

        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html, 200, "OK", "application/xhtml+xml");

        final WebClient client = getWebClient();
        client.setWebConnection(conn);
        final XHtmlPage page = client.getPage(URL_FIRST);

        final HtmlDivision div = page.getFirstByXPath("//xhtml:div");
        assertEquals("http://www.w3.org/1999/xhtml", div.getNamespaceURI());
    }

}
