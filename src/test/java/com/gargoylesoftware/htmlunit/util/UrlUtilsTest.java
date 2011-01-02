/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
 * @author Martin Tamme
 * @author Sudhan Moghe
 * @author Ahmed Ashour
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

    /**
     * Test {@link UrlUtils#resolveUrl(String, String)} with the normal examples taken from
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a> Section 5.1.
     */
    @Test
    public void resolveUrlWithNormalExamples() {
        final String baseUrl = "http://a/b/c/d;p?q#f";

        assertEquals("g:h",                  UrlUtils.resolveUrl(baseUrl, "g:h"));
        assertEquals("http://a/b/c/g",       UrlUtils.resolveUrl(baseUrl, "g"));
        assertEquals("http://a/b/c/g",       UrlUtils.resolveUrl(baseUrl, "./g"));
        assertEquals("http://a/b/c/g/",      UrlUtils.resolveUrl(baseUrl, "g/"));
        assertEquals("http://a/g",           UrlUtils.resolveUrl(baseUrl, "/g"));
        assertEquals("http://g",             UrlUtils.resolveUrl(baseUrl, "//g"));
        assertEquals("http://a/b/c/d;p?y",   UrlUtils.resolveUrl(baseUrl, "?y"));
        assertEquals("http://a/b/c/g?y",     UrlUtils.resolveUrl(baseUrl, "g?y"));
        assertEquals("http://a/b/c/g?y/./x", UrlUtils.resolveUrl(baseUrl, "g?y/./x"));
        assertEquals("http://a/b/c/d;p?q#s", UrlUtils.resolveUrl(baseUrl, "#s"));
        assertEquals("http://a/b/c/g#s",     UrlUtils.resolveUrl(baseUrl, "g#s"));
        assertEquals("http://a/b/c/g#s/./x", UrlUtils.resolveUrl(baseUrl, "g#s/./x"));
        assertEquals("http://a/b/c/g?y#s",   UrlUtils.resolveUrl(baseUrl, "g?y#s"));
        assertEquals("http://a/b/c/d;x",     UrlUtils.resolveUrl(baseUrl, ";x"));
        assertEquals("http://a/b/c/g;x",     UrlUtils.resolveUrl(baseUrl, "g;x"));
        assertEquals("http://a/b/c/g;x?y#s", UrlUtils.resolveUrl(baseUrl, "g;x?y#s"));
        assertEquals("http://a/b/c/",        UrlUtils.resolveUrl(baseUrl, "."));
        assertEquals("http://a/b/c/",        UrlUtils.resolveUrl(baseUrl, "./"));
        assertEquals("http://a/b/",          UrlUtils.resolveUrl(baseUrl, ".."));
        assertEquals("http://a/b/",          UrlUtils.resolveUrl(baseUrl, "../"));
        assertEquals("http://a/b/g",         UrlUtils.resolveUrl(baseUrl, "../g"));
        assertEquals("http://a/",            UrlUtils.resolveUrl(baseUrl, "../.."));
        assertEquals("http://a/",            UrlUtils.resolveUrl(baseUrl, "../../"));
        assertEquals("http://a/g",           UrlUtils.resolveUrl(baseUrl, "../../g"));

        //Following two cases were failing when original implementation was modified to handle
        //the cases given in RFC 1808. Lots of other test cases failed because of that.
        assertEquals(URL_FIRST + "foo.xml", UrlUtils.resolveUrl(URL_FIRST, "/foo.xml"));
        assertEquals(URL_FIRST + "foo.xml", UrlUtils.resolveUrl(URL_FIRST, "foo.xml"));
    }

    /**
     * Test {@link UrlUtils#resolveUrl(String, String)} with the abnormal examples taken from
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a> Section 5.2.
     */
    @Test
    public void resolveUrlWithAbnormalExamples() {
        final String baseUrl = "http://a/b/c/d;p?q#f";

        assertEquals("http://a/b/c/d;p?q#f", UrlUtils.resolveUrl(baseUrl, ""));

// These examples are fine... but it's not what browsers do (see below)
//        assertEquals("http://a/../g",        UrlUtils.resolveUrl(baseUrl, "../../../g"));
//        assertEquals("http://a/../../g",     UrlUtils.resolveUrl(baseUrl, "../../../../g"));
//        assertEquals("http://a/./g",         UrlUtils.resolveUrl(baseUrl, "/./g"));
//        assertEquals("http://a/../g",        UrlUtils.resolveUrl(baseUrl, "/../g"));
        assertEquals("http://a/g",        UrlUtils.resolveUrl(baseUrl, "../../../g"));
        assertEquals("http://a/g",     UrlUtils.resolveUrl(baseUrl, "../../../../g"));
        assertEquals("http://a/./g",         UrlUtils.resolveUrl(baseUrl, "/./g"));
        assertEquals("http://a/g",        UrlUtils.resolveUrl(baseUrl, "/../g"));

        assertEquals("http://a/b/c/g.",      UrlUtils.resolveUrl(baseUrl, "g."));
        assertEquals("http://a/b/c/.g",      UrlUtils.resolveUrl(baseUrl, ".g"));
        assertEquals("http://a/b/c/g..",     UrlUtils.resolveUrl(baseUrl, "g.."));
        assertEquals("http://a/b/c/..g",     UrlUtils.resolveUrl(baseUrl, "..g"));
        assertEquals("http://a/b/g",         UrlUtils.resolveUrl(baseUrl, "./../g"));
        assertEquals("http://a/b/c/g/",      UrlUtils.resolveUrl(baseUrl, "./g/."));
        assertEquals("http://a/b/c/g/h",     UrlUtils.resolveUrl(baseUrl, "g/./h"));
        assertEquals("http://a/b/c/h",       UrlUtils.resolveUrl(baseUrl, "g/../h"));
        assertEquals("http:g",               UrlUtils.resolveUrl(baseUrl, "http:g"));
        assertEquals("http:",                UrlUtils.resolveUrl(baseUrl, "http:"));
    }

    /**
     * Test {@link UrlUtils#resolveUrl(String, String)} with extra examples.
     */
    @Test
    public void resolveUrlWithExtraExamples() {
        final String baseUrl = "http://a/b/c/d;p?q#f";

        assertEquals("http://a/b/c/d;",      UrlUtils.resolveUrl(baseUrl, ";"));
        assertEquals("http://a/b/c/d;p?",    UrlUtils.resolveUrl(baseUrl, "?"));
        assertEquals("http://a/b/c/d;p?q#",  UrlUtils.resolveUrl(baseUrl, "#"));
        assertEquals("http://a/b/c/d;p?q#s", UrlUtils.resolveUrl(baseUrl, "#s"));

        assertEquals("http://a/f.html", UrlUtils.resolveUrl("http://a/otherFile.html", "../f.html"));
        assertEquals("http://a/f.html", UrlUtils.resolveUrl("http://a/otherFile.html", "../../f.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void percent() throws Exception {
        final URL url = new URL("http://localhost/bug%21.html");
        assertEquals("http://localhost/bug%21.html", UrlUtils.encodeUrl(url, false).toExternalForm());
    }
}
