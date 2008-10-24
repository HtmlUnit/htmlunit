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
package com.gargoylesoftware.htmlunit;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Test;
import org.mortbay.jetty.Server;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebResponseData}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class WebResponseDataTest extends WebTestCase {

    private static final String GZIPPED_FILE = "testfiles/test.html.gz";

    private Server server_;

    /**
     * Tests that gzipped content is handled correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void testGZippedContent() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(GZIPPED_FILE);
        final byte[] zippedContent = IOUtils.toByteArray(stream);

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));

        final WebResponseData data = new WebResponseData(zippedContent, HttpStatus.SC_OK, "OK", headers);
        final String body = new String(data.getBody(), "UTF-8");
        assertTrue(StringUtils.contains(body, "Test"));
    }

    /**
     * Verifies that a null body input stream is handled correctly. A null body may be sent, for
     * example, when a 304 (Not Modified) response is sent to the client. See bug 1706505.
     * @throws Exception if the test fails
     */
    @Test
    public void testNullBody() throws Exception {
        final InputStream body = null;
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        final WebResponseData data = new WebResponseData(body, 304, "NOT_MODIFIED", headers);
        assertNull(data.getBody());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void deflateCompression() throws Exception {
        server_ = HttpWebConnectionTest.startWebServer("src/test/resources/pjl-comp-filter", null);
        final  WebRequestSettings settings = new WebRequestSettings(new URL("http://localhost:"
            + HttpWebConnectionTest.PORT + "/index.html"));
        settings.addAdditionalHeader("Accept-Encoding", "deflate");
        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage(settings);
        assertEquals("Hello Compressed World!", page.asText());
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
    }

}
