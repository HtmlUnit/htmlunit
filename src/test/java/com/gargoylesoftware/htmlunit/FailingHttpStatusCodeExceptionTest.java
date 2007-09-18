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

import java.util.Collections;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.ArrayUtils;

/**
 * Tests for {@link FailingHttpStatusCodeException}..
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public final class FailingHttpStatusCodeExceptionTest extends WebTestCase {
    /**
     * Create an instance.
     *
     * @param name
     *            The name of the test.
     */
    public FailingHttpStatusCodeExceptionTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    public void testConstructorWitWebResponse() throws Exception {
        final WebResponseData webResponseData = new WebResponseData(
                ArrayUtils.EMPTY_BYTE_ARRAY, HttpStatus.SC_NOT_FOUND, "not found",
                Collections.EMPTY_LIST);
        final WebResponse webResponse = new WebResponseImpl(webResponseData,
                URL_FIRST, SubmitMethod.GET, 10);
        final FailingHttpStatusCodeException e = new FailingHttpStatusCodeException(
                webResponse);

        assertEquals(webResponse, e.getResponse());
        assertEquals(webResponse.getStatusMessage(), e.getStatusMessage());
        assertEquals(webResponse.getStatusCode(), e.getStatusCode());
        assertTrue("message doesn't contain failing url", e.getMessage()
                .indexOf(URL_FIRST.toExternalForm()) > -1);
    }
}
