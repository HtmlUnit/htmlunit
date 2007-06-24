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
package com.gargoylesoftware.htmlunit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Tests for {@link WebResponseData}.
 *
 * @version $Revision: $
 * @author Daniel Gredler
 */
public class WebResponseDataTest extends WebTestCase {

    private static final String GZIPPED_FILE = "test.html.gz";

    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public WebResponseDataTest(final String name) {
        super(name);
    }

    /**
     * Tests that gzipped content is handled correctly.
     * @throws Exception If the test fails.
     */
    public void testGZippedContent() throws Exception {

        final InputStream stream = getClass().getResourceAsStream(GZIPPED_FILE);
        final byte[] zippedContent = IOUtils.toByteArray(stream);

        final List headers = new ArrayList();
        headers.add(new KeyValuePair("Content-Encoding", "gzip"));

        final WebResponseData data = new WebResponseData(zippedContent, 200, "OK", headers);
        final String body = new String(data.getBody(), "UTF-8");
        assertTrue(StringUtils.contains(body, "Test"));
    }

    /**
     * Verifies that a null body input stream is handled correctly. A null body may be sent, for
     * example, when a 304 (Not Modified) response is sent to the client. See bug 1706505.
     * @throws Exception If the test fails.
     */
    public void testNullBody() throws Exception {
        final InputStream body = null;
        final List headers = new ArrayList();
        final WebResponseData data = new WebResponseData(body, 304, "NOT_MODIFIED", headers);
        assertNull(data.getBody());
    }

}
