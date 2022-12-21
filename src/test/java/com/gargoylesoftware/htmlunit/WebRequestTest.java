/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.WebTestCase.URL_FIRST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.BasicUserPrincipal;
import org.apache.http.auth.Credentials;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.httpclient.HttpClientConverter;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Tests for {@link WebRequest}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Rodney Gitzel
 * @author Ronald Brill
 * @author Joerg Werner
 * @author Michael Lueck
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
        assertNotNull("Credentials object is null", credentials);
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
        assertNotNull("Credentials object is null", credentials);
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
        assertNotNull("Credentials object is null", credentials);
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
    public void getParametersNone() throws Exception {
        final URL url = new URL("http://localhost/test");
        final WebRequest request = new WebRequest(url);
        assertEquals(0, request.getParameters().size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getParametersFromUrlGet() throws Exception {
        final URL url = new URL("http://localhost/test?x=u");
        final WebRequest request = new WebRequest(url);

        assertEquals(1, request.getParameters().size());
        assertEquals("x", request.getParameters().get(0).getName());
        assertEquals("u", request.getParameters().get(0).getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getParametersFromUrlKeyEqualsOnlyGet() throws Exception {
        final URL url = new URL("http://localhost/test?x=");
        final WebRequest request = new WebRequest(url);

        assertEquals(1, request.getParameters().size());
        assertEquals("x", request.getParameters().get(0).getName());
        assertEquals("", request.getParameters().get(0).getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getParametersFromUrlKeyOnlyGet() throws Exception {
        final URL url = new URL("http://localhost/test?x");
        final WebRequest request = new WebRequest(url);

        assertEquals(1, request.getParameters().size());
        assertEquals("x", request.getParameters().get(0).getName());
        assertEquals("", request.getParameters().get(0).getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getParametersFromRequestParametersGet() throws Exception {
        final URL url = new URL("http://localhost/test?x=u");
        final WebRequest request = new WebRequest(url);

        final List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        request.setRequestParameters(pairs);
        assertEquals(1, request.getParameters().size());
        assertEquals("x", request.getParameters().get(0).getName());
        assertEquals("u", request.getParameters().get(0).getValue());

        pairs.add(new NameValuePair("hello", "world"));
        assertEquals(1, request.getParameters().size());
        assertEquals("hello", request.getParameters().get(0).getName());
        assertEquals("world", request.getParameters().get(0).getValue());

        // test for our internal conversation
        String query = URLEncodedUtils.
                format(HttpClientConverter.nameValuePairsToHttpClient(pairs), StandardCharsets.UTF_8);
        assertEquals("http://localhost/test?hello=world", UrlUtils.toURI(url, query).toString());

        pairs.add(new NameValuePair("empty", ""));
        assertEquals(2, request.getParameters().size());
        assertEquals("empty", request.getParameters().get(1).getName());
        assertEquals("", request.getParameters().get(1).getValue());

        // test for our internal conversation
        query = URLEncodedUtils.
                format(HttpClientConverter.nameValuePairsToHttpClient(pairs), StandardCharsets.UTF_8);
        assertEquals("http://localhost/test?hello=world&empty=", UrlUtils.toURI(url, query).toString());

        pairs.add(new NameValuePair("null", null));
        assertEquals(3, request.getParameters().size());
        assertEquals("null", request.getParameters().get(2).getName());
        assertEquals("", request.getParameters().get(2).getValue());

        // test for our internal conversation
        query = URLEncodedUtils.
                format(HttpClientConverter.nameValuePairsToHttpClient(pairs), StandardCharsets.UTF_8);
        assertEquals("http://localhost/test?hello=world&empty=&null", UrlUtils.toURI(url, query).toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getParametersFromUrlPost() throws Exception {
        final URL url = new URL("http://localhost/test?x=u");
        final WebRequest request = new WebRequest(url);
        request.setHttpMethod(HttpMethod.POST);

        assertEquals(0, request.getParameters().size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getParametersFromUrlEncodedBodyPost() throws Exception {
        final URL url = new URL("http://localhost/test");
        final WebRequest request = new WebRequest(url);
        request.setHttpMethod(HttpMethod.POST);
        request.setEncodingType(FormEncodingType.URL_ENCODED);
        request.setRequestBody("x=u");

        assertEquals(1, request.getParameters().size());
        assertEquals("x", request.getParameters().get(0).getName());
        assertEquals("u", request.getParameters().get(0).getValue());
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

    @Test
    public void getParametersShouldNotModifyAlreadyNormalizedRequestParams() throws Exception {
        final WebRequest request = new WebRequest(new URL("http://localhost/test"));
        request.setHttpMethod(HttpMethod.POST);
        request.setEncodingType(FormEncodingType.MULTIPART);

        final List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        requestParams.add(new NameValuePair("test", "x"));
        requestParams.add(new KeyDataPair("file",
                                          new File("test"),
                                          "test",
                                          "application/octet-stream",
                                          StandardCharsets.UTF_8));
        request.setRequestParameters(requestParams);

        //check that the result of getParams is equal to the requestParams after "normalization"
        assertEquals(requestParams, request.getParameters());
    }

    @Test
    public void getParametersShouldNormalizeMultiPartRequestParams() throws Exception {
        final WebRequest request = new WebRequest(new URL("http://localhost/test"));
        request.setHttpMethod(HttpMethod.POST);
        request.setEncodingType(FormEncodingType.MULTIPART);

        final List<NameValuePair> requestParams = new ArrayList<>();
        requestParams.add(new NameValuePair("test", null));
        requestParams.add(new KeyDataPair("file", null, null, null, StandardCharsets.UTF_8));
        request.setRequestParameters(requestParams);

        final List<NameValuePair> expectedResults = new ArrayList<>();
        expectedResults.add(new NameValuePair("test", ""));
        // the constructor of the KeyDataPair already creates normalized object
        // where the value is set to empty string if the passed file is null.
        expectedResults.add(new KeyDataPair("file", null, null, null, StandardCharsets.UTF_8));

        final List<NameValuePair> normalizedParams = request.getParameters();
        assertEquals(expectedResults, normalizedParams);

        // check that the value of the KeyDataPair is really normalized to empty string
        assertEquals("", normalizedParams.get(1).getValue());
    }
}
