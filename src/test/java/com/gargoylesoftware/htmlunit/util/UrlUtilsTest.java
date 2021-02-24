/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.util;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.net.InetAddress;
import java.net.URL;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link UrlUtils}.
 *
 * @author Daniel Gredler
 * @author Martin Tamme
 * @author Sudhan Moghe
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Hartmut Arlt
 */
public class UrlUtilsTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void toUrlSafe() throws Exception {
        URL url = UrlUtils.toUrlSafe("http://my.home.com/index.html?query#ref");
        assertEquals("http://my.home.com/index.html?query#ref", url.toExternalForm());

        url = UrlUtils.toUrlSafe("about:blank");
        assertEquals(UrlUtils.URL_ABOUT_BLANK, url);

        url = UrlUtils.toUrlSafe("about:Blank");
        assertEquals(UrlUtils.URL_ABOUT_BLANK, url);

        url = UrlUtils.toUrlSafe("about:config");
        assertEquals("about:config", url.toExternalForm());
    }

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
        URL a = new URL("http://my.home.com/index.html?query#ref");
        URL b = UrlUtils.getUrlWithNewHost(a, "your.home.com");
        assertEquals("http://your.home.com/index.html?query#ref", b.toExternalForm());

        // preserve user info
        a = new URL("http://john.smith:secret@my.home.com/index.html?query#ref");
        b = UrlUtils.getUrlWithNewHost(a, "your.home.com");
        assertEquals("http://john.smith:secret@your.home.com/index.html?query#ref", b.toExternalForm());

        a = new URL("http://john.smith:@my.home.com/index.html?query#ref");
        b = UrlUtils.getUrlWithNewHost(a, "your.home.com");
        assertEquals("http://john.smith:@your.home.com/index.html?query#ref", b.toExternalForm());

        a = new URL("http://john.smith@my.home.com/index.html?query#ref");
        b = UrlUtils.getUrlWithNewHost(a, "your.home.com");
        assertEquals("http://john.smith@your.home.com/index.html?query#ref", b.toExternalForm());

        a = new URL("http://@my.home.com/index.html?query#ref");
        b = UrlUtils.getUrlWithNewHost(a, "your.home.com");
        assertEquals("http://@your.home.com/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewHostAndPort() throws Exception {
        URL a = new URL("http://my.home.com/index.html?query#ref");
        URL b = UrlUtils.getUrlWithNewHostAndPort(a, "your.home.com", 4711);
        assertEquals("http://your.home.com:4711/index.html?query#ref", b.toExternalForm());

        b = UrlUtils.getUrlWithNewHostAndPort(a, "your.home.com", -1);
        assertEquals("http://your.home.com/index.html?query#ref", b.toExternalForm());

        a = new URL("http://john.smith:secret@my.home.com/index.html?query#ref");
        b = UrlUtils.getUrlWithNewHostAndPort(a, "your.home.com", -1);
        assertEquals("http://john.smith:secret@your.home.com/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewPort() throws Exception {
        URL a = new URL("http://my.home.com/index.html?query#ref");
        URL b = UrlUtils.getUrlWithNewPort(a, 8080);
        assertEquals("http://my.home.com:8080/index.html?query#ref", b.toExternalForm());

        b = UrlUtils.getUrlWithNewPort(a, -1);
        assertEquals("http://my.home.com/index.html?query#ref", b.toExternalForm());

        a = new URL("http://john.smith:secret@my.home.com/index.html?query#ref");
        b = UrlUtils.getUrlWithNewPort(a, 8080);
        assertEquals("http://john.smith:secret@my.home.com:8080/index.html?query#ref", b.toExternalForm());
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
        URL a = new URL("http://my.home.com/index.html?query#ref");
        URL b = UrlUtils.getUrlWithNewRef(a, "abc");
        assertEquals("http://my.home.com/index.html?query#abc", b.toExternalForm());

        a = new URL("http://my.home.com/#ref");
        b = UrlUtils.getUrlWithNewRef(a, "xyz");
        assertEquals("http://my.home.com/#xyz", b.toExternalForm());

        a = new URL("http://my.home.com#ref");
        b = UrlUtils.getUrlWithNewRef(a, "xyz");
        assertEquals("http://my.home.com#xyz", b.toExternalForm());

        a = new URL("http://my.home.com");
        b = UrlUtils.getUrlWithNewRef(a, "xyz");
        assertEquals("http://my.home.com#xyz", b.toExternalForm());

        a = new URL("http://my.home.com");
        b = UrlUtils.getUrlWithNewRef(a, null);
        assertEquals("http://my.home.com", b.toExternalForm());

        a = new URL("http://my.home.com");
        b = UrlUtils.getUrlWithNewRef(a, "");
        assertEquals("http://my.home.com#", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewQuery() throws Exception {
        URL a = new URL("http://my.home.com/index.html?query#ref");
        URL b = UrlUtils.getUrlWithNewQuery(a, "xyz");
        assertEquals("http://my.home.com/index.html?xyz#ref", b.toExternalForm());

        // DOS
        a = new URL("file://c:/index.html?query");
        b = UrlUtils.getUrlWithNewQuery(a, "xyz");
        assertEquals("file://c:/index.html?xyz", b.toExternalForm());
        // UNIX
        a = new URL("file:///index.html?query");
        b = UrlUtils.getUrlWithNewQuery(a, "xyz");
        assertEquals("file:/index.html?xyz", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithProtocolAndAuthority() throws Exception {
        URL a = new URL("http://my.home.com/index.html?query#ref");
        URL b = UrlUtils.getUrlWithProtocolAndAuthority(a);
        assertEquals("http://my.home.com", b.toExternalForm());

        a = new URL("http://my.home.com/folder/index.html?query#ref");
        b = UrlUtils.getUrlWithProtocolAndAuthority(a);
        assertEquals("http://my.home.com", b.toExternalForm());

        a = new URL("http://my.home.com/folder/subfolder/index.html");
        b = UrlUtils.getUrlWithProtocolAndAuthority(a);
        assertEquals("http://my.home.com", b.toExternalForm());

        a = new URL("http://my.home.com");
        b = UrlUtils.getUrlWithProtocolAndAuthority(a);
        assertEquals("http://my.home.com", b.toExternalForm());

        a = new URL("http://my.home.com?query");
        b = UrlUtils.getUrlWithProtocolAndAuthority(a);
        assertEquals("http://my.home.com", b.toExternalForm());

        a = new URL("http://my.home.com/");
        b = UrlUtils.getUrlWithProtocolAndAuthority(a);
        assertEquals("http://my.home.com", b.toExternalForm());

        a = new URL("http://my.home.com/#href");
        b = UrlUtils.getUrlWithProtocolAndAuthority(a);
        assertEquals("http://my.home.com", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewUserName() throws Exception {
        URL a = new URL("http://my.home.com/index.html?query");
        URL b = UrlUtils.getUrlWithNewUserName(a, "abc");
        assertEquals("http://abc@my.home.com/index.html?query", b.toExternalForm());

        a = new URL("http://my.home.com");
        b = UrlUtils.getUrlWithNewUserName(a, "xyz");
        assertEquals("http://xyz@my.home.com", b.toExternalForm());

        a = new URL("http://user:pw@my.home.com");
        b = UrlUtils.getUrlWithNewUserName(a, "xyz");
        assertEquals("http://xyz:pw@my.home.com", b.toExternalForm());

        a = new URL("http://user:@my.home.com");
        b = UrlUtils.getUrlWithNewUserName(a, "xyz");
        assertEquals("http://xyz:@my.home.com", b.toExternalForm());

        a = new URL("http://user@my.home.com");
        b = UrlUtils.getUrlWithNewUserName(a, "xyz");
        assertEquals("http://xyz@my.home.com", b.toExternalForm());

        a = new URL("http://:pw@my.home.com");
        b = UrlUtils.getUrlWithNewUserName(a, "xyz");
        assertEquals("http://xyz:pw@my.home.com", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewUserPassword() throws Exception {
        URL a = new URL("http://my.home.com/index.html?query");
        URL b = UrlUtils.getUrlWithNewUserPassword(a, "abc");
        assertEquals("http://:abc@my.home.com/index.html?query", b.toExternalForm());

        a = new URL("http://my.home.com");
        b = UrlUtils.getUrlWithNewUserPassword(a, "xyz");
        assertEquals("http://:xyz@my.home.com", b.toExternalForm());

        a = new URL("http://user:pw@my.home.com");
        b = UrlUtils.getUrlWithNewUserPassword(a, "xyz");
        assertEquals("http://user:xyz@my.home.com", b.toExternalForm());

        a = new URL("http://user:@my.home.com");
        b = UrlUtils.getUrlWithNewUserPassword(a, "xyz");
        assertEquals("http://user:xyz@my.home.com", b.toExternalForm());

        a = new URL("http://user@my.home.com");
        b = UrlUtils.getUrlWithNewUserPassword(a, "xyz");
        assertEquals("http://user:xyz@my.home.com", b.toExternalForm());

        a = new URL("http://:pw@my.home.com");
        b = UrlUtils.getUrlWithNewUserPassword(a, "xyz");
        assertEquals("http://:xyz@my.home.com", b.toExternalForm());
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

        assertEquals("http://a/g",           UrlUtils.resolveUrl(baseUrl, "/../../g"));
        assertEquals("http://a/.g",          UrlUtils.resolveUrl(baseUrl, "/.g"));
        assertEquals("http://a/..g",         UrlUtils.resolveUrl(baseUrl, "/..g"));
        assertEquals("http://a/...g",        UrlUtils.resolveUrl(baseUrl, "/...g"));

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
        URL url = new URL("http://localhost/bug%21.html");
        assertEquals("http://localhost/bug%21.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug%0F.html");
        assertEquals("http://localhost/bug%0F.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug%ff.html");
        assertEquals("http://localhost/bug%ff.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug%AB.html");
        assertEquals("http://localhost/bug%AB.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://john.smith:secret@localhost/bug%AB.html");
        assertEquals("http://john.smith:secret@localhost/bug%AB.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));
    }

    /**
     * Tests for #1587.
     * @throws Exception if the test fails
     */
    @Test
    public void percentEncoding() throws Exception {
        URL url = new URL("http://localhost/bug%.html");
        assertEquals("http://localhost/bug%25.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug%a.html");
        assertEquals("http://localhost/bug%25a.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug%ak.html");
        assertEquals("http://localhost/bug%25ak.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug.html?namelist=Woman%2g%20Daily");
        assertEquals("http://localhost/bug.html?namelist=Woman%252g%20Daily",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug.html?namelist=Woman%u2122%20Daily");
        assertEquals("http://localhost/bug.html?namelist=Woman%25u2122%20Daily",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug.html?%");
        assertEquals("http://localhost/bug.html?%25",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug.html?%2");
        assertEquals("http://localhost/bug.html?%252",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/bug.html?%2x");
        assertEquals("http://localhost/bug.html?%252x",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));
    }

    /**
     * Tests for #1951.
     * @throws Exception if the test fails
     */
    @Test
    public void percentEncoding2() throws Exception {
        URL url = new URL("http://localhost/foo%%20bar.html");
        assertEquals("http://localhost/foo%25%20bar.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/foo%20bar.html");
        assertEquals("http://localhost/foo%20bar.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/foo%ar.html");
        assertEquals("http://localhost/foo%25ar.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/foo%%xyz.html");
        assertEquals("http://localhost/foo%25%25xyz.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/foo%20%xyz.html");
        assertEquals("http://localhost/foo%20%25xyz.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));

        url = new URL("http://localhost/foo%2x%bar.html");
        assertEquals("http://localhost/foo%252x%bar.html",
                UrlUtils.encodeUrl(url, false, ISO_8859_1));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void relativeBase() throws Exception {
        final String baseUrl = "a/a1/a2";
        assertEquals("b",      UrlUtils.resolveUrl(baseUrl, "../../b"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void sameFile() throws Exception {
        assertTrue(UrlUtils.sameFile(null, null));
        assertFalse(UrlUtils.sameFile(new URL("http://localhost/bug.htm"), null));
        assertFalse(UrlUtils.sameFile(null, new URL("http://localhost/bug.html")));

        assertTrue(UrlUtils.sameFile(new URL("http://localhost/bug.html"), new URL("http://localhost/bug.html")));
        assertFalse(UrlUtils.sameFile(new URL("http://localhost/bug.htm"), new URL("http://localhost/bug.html")));

        // assertTrue(new URL("http://localhost/bug.html").sameFile(new URL("http://localhost/test/../bug.html")));
        assertTrue(UrlUtils.sameFile(new URL("http://localhost/bug.html"),
                    new URL("http://localhost/test/../bug.html")));

        assertTrue(UrlUtils.sameFile(new URL("http://localhost"), new URL("http://localhost")));
        assertTrue(UrlUtils.sameFile(new URL("http://localhost/"), new URL("http://localhost/")));
        assertTrue(UrlUtils.sameFile(new URL("http://localhost/"), new URL("http://localhost")));

        assertTrue(UrlUtils.sameFile(new URL("http://localhost/bug.html?test"),
                        new URL("http://localhost/bug.html?test")));
        assertFalse(UrlUtils.sameFile(new URL("http://localhost/bug.html?test"),
                        new URL("http://localhost/bug.html?rest")));

        assertTrue(UrlUtils.sameFile(new URL("http://localhost/bug.html#test"),
                new URL("http://localhost/bug.html#rest")));

        assertTrue(UrlUtils.sameFile(new URL("https://localhost/bug.html"), new URL("https://localhost/bug.html")));
        assertFalse(UrlUtils.sameFile(new URL("http://localhost/bug.htm"), new URL("https://localhost/bug.html")));

        assertTrue(UrlUtils.sameFile(new URL("http://localhost:81/bug.html"), new URL("http://localhost:81/bug.html")));
        assertFalse(UrlUtils.sameFile(new URL("http://localhost:81/bug.htm"), new URL("http://localhost:80/bug.html")));
        assertTrue(UrlUtils.sameFile(new URL("http://localhost/bug.html"), new URL("http://localhost:80/bug.html")));
        assertTrue(UrlUtils.sameFile(new URL("https://localhost:443/bug.html"), new URL("https://localhost/bug.html")));

        // issue #1787
        // final URL u1 = new URL("http://sourceforge.net/");
        // final URL u2 = new URL("http://ch3.sourceforge.net/");
        final URL u1 = new URL("http://rbri.de/");
        final URL u2 = new URL("http://jonas.rbri.de/");
        assertEquals(InetAddress.getByName(u1.getHost()), InetAddress.getByName(u2.getHost()));
        assertFalse(UrlUtils.sameFile(u1, u2));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void normalize() throws Exception {
        assertEquals("http://localhost:80/bug.html", UrlUtils.normalize(new URL("http://localhost/bug.html")));
        assertEquals("https://localhost:443/bug.html", UrlUtils.normalize(new URL("https://localhost/bug.html")));

        assertEquals("http://localhost:80/test/bug.html",
                    UrlUtils.normalize(new URL("http://localhost/test/./bug.html")));
        assertEquals("http://localhost:80/../bug.html", UrlUtils.normalize(new URL("http://localhost/../bug.html")));
        assertEquals("http://localhost:80/bug.html", UrlUtils.normalize(new URL("http://localhost/test/../bug.html")));

        assertEquals("http://localhost:80/", UrlUtils.normalize(new URL("http://localhost")));
        assertEquals("http://localhost:80/", UrlUtils.normalize(new URL("http://localhost/")));

        assertEquals("http://localhost:80/bug.html?test",
                    UrlUtils.normalize(new URL("http://localhost/bug.html?test")));
        assertEquals("http://localhost:80/bug.html", UrlUtils.normalize(new URL("http://localhost/bug.html#anchor")));
        assertEquals("http://localhost:80/bug.html?test",
                    UrlUtils.normalize(new URL("http://localhost/bug.html?test#anchor")));

        assertEquals("http://localhost:80/bug.html", UrlUtils.normalize(new URL("http://localhost:80/bug.html")));
        assertEquals("http://localhost:81/bug.html", UrlUtils.normalize(new URL("http://localhost:81/bug.html")));

        assertEquals("https://localhost:443/bug.html",
                    UrlUtils.normalize(new URL("https://localhost:443/bug.html")));
        assertEquals("https://localhost:8443/bug.html",
                    UrlUtils.normalize(new URL("https://localhost:8443/bug.html")));
    }
}
