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
package com.gargoylesoftware.htmlunit.util;

import java.net.URL;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link UrlUtils}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class UrlUtilsTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewProtocol() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewProtocol(a, "ftp");
        assertEquals("ftp://my.home.com/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewHost() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewHost(a, "your.home.com");
        assertEquals("http://your.home.com/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewPort() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewPort(a, 8080);
        assertEquals("http://my.home.com:8080/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewPath() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewPath(a, "/es/indice.html");
        assertEquals("http://my.home.com/es/indice.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewRef() throws Exception {
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
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewQuery() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewQuery(a, "xyz");
        assertEquals("http://my.home.com/index.html?xyz#ref", b.toExternalForm());
    }

}
