/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit;

import static org.htmlunit.WebTestCase.URL_FIRST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.auth.BasicUserPrincipal;
import org.apache.http.auth.Credentials;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WebRequest}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Rodney Gitzel
 * @author Ronald Brill
 * @author Joerg Werner
 * @author Michael Lueck
 * @author Kristof Neirynck
 */
public class WebRequestTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headers() throws Exception {
        final WebRequest request = new WebRequest(URL_FIRST);
        final int initialSize = request.getAdditionalHeaders().size();
        request.setAdditionalHeader("Accept", "nothing");
        assertEquals(initialSize, request.getAdditionalHeaders().size());
        request.setAdditionalHeader("ACCEPT", "compares");
        assertEquals(initialSize, request.getAdditionalHeaders().size());
        request.removeAdditionalHeader("ACcEpT");
        assertEquals(initialSize - 1, request.getAdditionalHeaders().size());
    }

    /**
     * Tests for Bug #901.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void setUrl_eliminateDirUp() throws Exception {
        final URL url1 = new URL("http://htmlunit.sf.net/foo.html");
        final URL url2 = new URL("http://htmlunit.sf.net/dir/foo.html");
        final URL url3 = new URL("http://htmlunit.sf.net/dir/foo.html?a=1&b=2");

        // with directory/..
        WebRequest request = new WebRequest(new URL("http://htmlunit.sf.net/bla/../foo.html"));
        assertEquals(url1, request.getUrl());

        // with /..
        request = new WebRequest(new URL("http://htmlunit.sf.net/../foo.html"));
        assertEquals(url1, request.getUrl());

        // with /(\w\w)/.. (c.f. 2854634)
        request = new WebRequest(new URL("http://htmlunit.sf.net/dir/fu/../foo.html"));
        assertEquals(url2, request.getUrl());

        // with /../..
        request = new WebRequest(new URL("http://htmlunit.sf.net/../../foo.html"));
        assertEquals(url1, request.getUrl());

        // with ../.. (c.f. 2854634)
        request = new WebRequest(new URL("http://htmlunit.sf.net/dir/foo/bar/../../foo.html"));
        assertEquals(url2, request.getUrl());

        request = new WebRequest(
                          new URL("http://htmlunit.sf.net/dir/foo/bar/boo/hoo/silly/../../../../../foo.html"));
        assertEquals(url2, request.getUrl());

        // with /.
        request = new WebRequest(new URL("http://htmlunit.sf.net/./foo.html"));
        assertEquals(url1, request.getUrl());

        // with /\w//. (c.f. 2854634)
        request = new WebRequest(new URL("http://htmlunit.sf.net/a/./foo.html"));
        assertEquals(new URL("http://htmlunit.sf.net/a/foo.html"), request.getUrl());

        // with /.
        request = new WebRequest(new URL("http://htmlunit.sf.net/dir/./foo.html"));
        assertEquals(url2, request.getUrl());

        // with /. and query
        request = new WebRequest(new URL("http://htmlunit.sf.net/dir/./foo.html?a=1&b=2"));
        assertEquals(url3, request.getUrl());

        // pathological
        request = new WebRequest(
                new URL("http://htmlunit.sf.net/dir/foo/bar/./boo/hoo/silly/.././../../../.././foo.html?a=1&b=2"));
        assertEquals(url3, request.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setUrl_removePort() throws Exception {
        final URL url1 = new URL("http://htmlunit.sf.net/foo.html");
        final URL url2 = new URL("https://htmlunit.sf.net/foo.html");

        WebRequest request = new WebRequest(new URL("http://htmlunit.sf.net:80/foo.html"));
        assertEquals(url1, request.getUrl());

        request = new WebRequest(new URL("https://htmlunit.sf.net:443/foo.html"));
        assertEquals(url2, request.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setUrl_preservePort() throws Exception {
        final URL url1 = new URL("http://htmlunit.sf.net:8080/foo.html");
        final URL url2 = new URL("https://htmlunit.sf.net:8443/foo.html");

        WebRequest request = new WebRequest(new URL("http://htmlunit.sf.net:8080/foo.html"));
        assertEquals(url1, request.getUrl());

        request = new WebRequest(new URL("https://htmlunit.sf.net:8443/foo.html"));
        assertEquals(url2, request.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void credentials() throws Exception {
        final URL url = new URL("http://john.smith:secret@localhost/");
        final WebRequest request = new WebRequest(url);
        final Credentials credentials = request.getUrlCredentials();
        assertEquals(new BasicUserPrincipal("john.smith"), credentials.getUserPrincipal());
        assertEquals("secret", credentials.getPassword());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void credentialsAndEmptyPath() throws Exception {
        final URL url = new URL("http://john.smith:secret@localhost");
        final WebRequest request = new WebRequest(url);
        final Credentials credentials = request.getUrlCredentials();
        assertNotNull(credentials, "Credentials object is null");
        assertEquals(new BasicUserPrincipal("john.smith"), credentials.getUserPrincipal());
        assertEquals("secret", credentials.getPassword());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void credentialsAndPathWithDots() throws Exception {
        final URL url = new URL("http://john.smith:secret@localhost/../foo.html");
        final WebRequest request = new WebRequest(url);
        final Credentials credentials = request.getUrlCredentials();
        assertNotNull(credentials, "Credentials object is null");
        assertEquals(new BasicUserPrincipal("john.smith"), credentials.getUserPrincipal());
        assertEquals("secret", credentials.getPassword());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void credentialsAndInternationalizedDomainName() throws Exception {
        final URL url = new URL("http://john.smith:secret@löcälhöst/");
        final WebRequest request = new WebRequest(url);
        final Credentials credentials = request.getUrlCredentials();
        assertNotNull(credentials, "Credentials object is null");
        assertEquals(new BasicUserPrincipal("john.smith"), credentials.getUserPrincipal());
        assertEquals("secret", credentials.getPassword());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void credentialsOnlyUsernameInURL() throws Exception {
        final URL url = new URL("http://john.smith@localhost/");
        final WebRequest request = new WebRequest(url);
        final Credentials credentials = request.getUrlCredentials();
        assertEquals(new BasicUserPrincipal("john.smith"), credentials.getUserPrincipal());
        assertEquals("", credentials.getPassword());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void credentialsOnlyPasswordInURL() throws Exception {
        final URL url = new URL("http://:secret@localhost/");
        final WebRequest request = new WebRequest(url);
        final Credentials credentials = request.getUrlCredentials();
        assertEquals(new BasicUserPrincipal(""), credentials.getUserPrincipal());
        assertEquals("secret", credentials.getPassword());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void credentialsEmptyURL() throws Exception {
        final URL url = new URL("http://:@localhost/");
        final WebRequest request = new WebRequest(url);
        final Credentials credentials = request.getUrlCredentials();
        assertEquals(new BasicUserPrincipal(""), credentials.getUserPrincipal());
        assertEquals("", credentials.getPassword());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void accept_encoding() throws Exception {
        final URL url = new URL("http://localhost/");
        final WebRequest request = new WebRequest(url);
        assertEquals("gzip, deflate", request.getAdditionalHeaders().get(HttpHeader.ACCEPT_ENCODING));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void idn() throws Exception {
        final String internationalized = "\u0645\u0635\u0631";
        final URL url = new URL("http://" + internationalized + ".com/" + internationalized);
        final WebRequest request = new WebRequest(url);
        final URL expected = new URL("http://xn--wgbh1c.com/" + internationalized);
        assertEquals(expected, request.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void hiddenFileInWindows() throws Exception {
        final URL url = new URL("file://c:/testing/.hidden/folder");
        final WebRequest request = new WebRequest(url);
        assertEquals(url.toExternalForm(), request.getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void hiddenFileInWindows2() throws Exception {
        final URL url = new URL("file:/c:/testing/.hidden/folder");
        final WebRequest request = new WebRequest(url);
        assertEquals(url.toExternalForm(), request.getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getRequestParametersNone() throws Exception {
        final URL url = new URL("http://localhost/test");
        final WebRequest request = new WebRequest(url);
        assertEquals(0, request.getRequestParameters().size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getRequestParametersFromUrlGet() throws Exception {
        final URL url = new URL("http://localhost/test?x=u");
        final WebRequest request = new WebRequest(url);

        // parameters are not parsed from the url
        assertEquals(0, request.getRequestParameters().size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getRequestParametersFromUrlKeyOnlyGet() throws Exception {
        final URL url = new URL("http://localhost/test?x=");
        final WebRequest request = new WebRequest(url);

        // parameters are not parsed from the url
        assertEquals(0, request.getRequestParameters().size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getRequestParametersFromUrlPost() throws Exception {
        final URL url = new URL("http://localhost/test?x=u");
        final WebRequest request = new WebRequest(url);
        request.setHttpMethod(HttpMethod.POST);

        assertEquals(0, request.getRequestParameters().size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getRequestParametersFromUrlEncodedBodyPost() throws Exception {
        final URL url = new URL("http://localhost/test");
        final WebRequest request = new WebRequest(url);
        request.setHttpMethod(HttpMethod.POST);
        request.setEncodingType(FormEncodingType.URL_ENCODED);
        request.setRequestBody("x=u");

        assertEquals(0, request.getRequestParameters().size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final WebRequest request = new WebRequest(URL_FIRST);
        request.setCharset(StandardCharsets.UTF_8);
        request.setDefaultResponseContentCharset(StandardCharsets.US_ASCII);

        final byte[] bytes = SerializationUtils.serialize(request);
        final WebRequest deserialized = (WebRequest) SerializationUtils.deserialize(bytes);

        assertEquals(URL_FIRST, deserialized.getUrl());
        assertEquals(StandardCharsets.UTF_8, deserialized.getCharset());
        assertEquals(StandardCharsets.US_ASCII, deserialized.getDefaultResponseContentCharset());
    }
}
