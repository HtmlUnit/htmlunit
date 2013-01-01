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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Test class for {@link HTMLParser}.
 *
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 */
@RunWith(BrowserRunner.class)
public class HTMLParser3Test extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headerVsMetaTagContentType_both() throws Exception {
        HeaderVsMetaTagContentTypeServlet.setEncoding("UTF-8", "ISO-8859-1");
        headerVsMetaTagContentType(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headerVsMetaTagContentType_bothReversed() throws Exception {
        HeaderVsMetaTagContentTypeServlet.setEncoding("ISO-8859-1", "UTF-8");
        headerVsMetaTagContentType(false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headerVsMetaTagContentType4_headerOnly() throws Exception {
        HeaderVsMetaTagContentTypeServlet.setEncoding("UTF-8", null);
        headerVsMetaTagContentType(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headerVsMetaTagContentType_metaOnly() throws Exception {
        HeaderVsMetaTagContentTypeServlet.setEncoding(null, "UTF-8");
        headerVsMetaTagContentType(true);
    }

    private void headerVsMetaTagContentType(final boolean utf8Encoded) throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test", HeaderVsMetaTagContentTypeServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test");
        assertEquals(utf8Encoded, HeaderVsMetaTagContentTypeServlet.utf8String.equals(page.asText()));
    }

    /**
     * Servlet for headerVsMetaTagContentType(boolean).
     */
    public static class HeaderVsMetaTagContentTypeServlet extends HttpServlet {
        private static final String utf8String = "\u064A\u0627 \u0644\u064A\u064A\u064A\u064A\u0644";
        private static String HEADER_ENCODING_;
        private static String META_TAG_ENCODING_;

        private static void setEncoding(final String headerEncoding, final String metaTagEncoding) {
            HEADER_ENCODING_ = headerEncoding;
            META_TAG_ENCODING_ = metaTagEncoding;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            if (HEADER_ENCODING_ != null) {
                response.setCharacterEncoding(HEADER_ENCODING_);
            }
            final Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            String html = "<html><head>";
            if (META_TAG_ENCODING_ != null) {
                html += "<META HTTP-EQUIV='Content-Type' CONTENT='text/html; charset=" + META_TAG_ENCODING_ + "'>";
            }
            html += "</head><body>" + utf8String + "</body></html>";
            writer.write(html);
            writer.close();
        }
    }

}
