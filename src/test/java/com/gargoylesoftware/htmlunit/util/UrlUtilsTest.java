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
package com.gargoylesoftware.htmlunit.util;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for UrlUtils.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class UrlUtilsTest extends WebTestCase {

    /**
     * Creates a new instance.
     * @param name The name of the new instance.
     */
    public UrlUtilsTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testGetUrlWithNewProtocol() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewProtocol(a, "ftp");
        assertEquals("ftp://my.home.com/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testGetUrlWithNewHost() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewHost(a, "your.home.com");
        assertEquals("http://your.home.com/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testGetUrlWithNewPort() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewPort(a, 8080);
        assertEquals("http://my.home.com:8080/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testGetUrlWithNewPath() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewPath(a, "/es/indice.html");
        assertEquals("http://my.home.com/es/indice.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testGetUrlWithNewRef() throws Exception {

        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewRef(a, "abc");
        assertEquals("http://my.home.com/index.html?query#abc", b.toExternalForm());

        final URL c = new URL("http://my.home.com/#ref");
        final URL d = UrlUtils.getUrlWithNewRef(c, "xyz");
        assertEquals("http://my.home.com/#xyz", d.toExternalForm());

        final URL e = new URL("http://my.home.com#ref");
        final URL f = UrlUtils.getUrlWithNewRef(e, "xyz");
        assertEquals("http://my.home.com#xyz", f.toExternalForm());
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testGetUrlWithNewQuery() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewQuery(a, "xyz");
        assertEquals("http://my.home.com/index.html?xyz#ref", b.toExternalForm());
    }

}
