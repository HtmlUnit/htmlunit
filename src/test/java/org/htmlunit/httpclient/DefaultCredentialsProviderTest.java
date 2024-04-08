/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.httpclient;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.impl.auth.BasicScheme;
import org.htmlunit.HttpHeader;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DefaultCredentialsProviderTest extends SimpleWebTestCase {

    /**
     * Test that successive calls to {@link DefaultCredentialsProvider#addCredentials(String, String)}
     * overwrite values previously set.
     * @throws Exception if the test fails
     */
    @Test
    public void overwrite() throws Exception {
        final String realm = "blah";
        final String scheme = new BasicScheme().getSchemeName();

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials("username", "password".toCharArray());

        Credentials credentials = provider.getCredentials(new AuthScope(HttpHeader.HOST_LC, 80, realm, scheme));
        assertEquals("username", credentials.getUserPrincipal().getName());
        assertEquals("password", credentials.getPassword());

        provider.addCredentials("username", "new password".toCharArray());
        credentials = provider.getCredentials(new AuthScope(HttpHeader.HOST_LC, 80, realm, scheme));
        assertEquals("username", credentials.getUserPrincipal().getName());
        assertEquals("new password", credentials.getPassword());

        provider.addCredentials("new username", "other password".toCharArray());
        credentials = provider.getCredentials(new AuthScope(HttpHeader.HOST_LC, 80, realm, scheme));
        assertEquals("new username", credentials.getUserPrincipal().getName());
        assertEquals("other password", credentials.getPassword());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationTwice() throws Exception {
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                        .addCredentials("jetty", "jetty".toCharArray());

        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        HtmlPage page = loadPage("Hi There");
        assertTrue(page.asNormalizedText().contains("Hi There"));
        page = getWebClient().getPage(URL_SECOND);
        assertTrue(page.asNormalizedText().contains("Hello World"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void removeCredentials() throws Exception {
        final String realm = "blah";
        final String scheme = new BasicScheme().getSchemeName();

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials("username", "password".toCharArray(), HttpHeader.HOST_LC, 80, realm);

        Credentials credentials = provider.getCredentials(new AuthScope(HttpHeader.HOST_LC, 80, realm, scheme));
        assertEquals("username", credentials.getUserPrincipal().getName());
        assertEquals("password", credentials.getPassword());

        provider.removeCredentials(new AuthScope(HttpHeader.HOST_LC, 80, realm, scheme));

        credentials = provider.getCredentials(new AuthScope(HttpHeader.HOST_LC, 80, realm, scheme));
        assertNull(credentials);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void passwordNull() throws Exception {
        final String realm = "blah";
        final String scheme = new BasicScheme().getSchemeName();

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials("username", (char[]) null, HttpHeader.HOST_LC, 80, realm);

        final Credentials credentials = provider.getCredentials(new AuthScope(HttpHeader.HOST_LC, 80, realm, scheme));
        assertEquals("username", credentials.getUserPrincipal().getName());
        assertNull(credentials.getPassword());
    }
}
