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
package com.gargoylesoftware.htmlunit;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @author Marc Guillemot
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
        final String scheme = new BasicScheme().getName();

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials("username", "password".toCharArray());

        UsernamePasswordCredentials credentials =
            (UsernamePasswordCredentials) provider.getCredentials(new AuthScope(null, HttpHeader.HOST_LC, 80, realm, scheme), null);
        assertEquals("username", credentials.getUserName());
        assertEquals("password", new String(credentials.getPassword()));

        provider.addCredentials("username", "new password".toCharArray());
        credentials = (UsernamePasswordCredentials) provider
                        .getCredentials(new AuthScope(null, HttpHeader.HOST_LC, 80, realm, scheme), null);
        assertEquals("username", credentials.getUserName());
        assertEquals("new password", new String(credentials.getPassword()));

        provider.addCredentials("new username", "other password".toCharArray());
        credentials = (UsernamePasswordCredentials) provider
                        .getCredentials(new AuthScope(null, HttpHeader.HOST_LC, 80, realm, scheme), null);
        assertEquals("new username", credentials.getUserName());
        assertEquals("other password", new String(credentials.getPassword()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationTwice() throws Exception {
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty".toCharArray());

        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        HtmlPage page = loadPage("Hi There");
        assertTrue(page.asText().contains("Hi There"));
        page = getWebClient().getPage(URL_SECOND);
        assertTrue(page.asText().contains("Hello World"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void removeCredentials() throws Exception {
        final String realm = "blah";
        final String scheme = new BasicScheme().getName();

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials("username", "password".toCharArray(), HttpHeader.HOST_LC, 80, realm);

        UsernamePasswordCredentials credentials =
            (UsernamePasswordCredentials) provider.getCredentials(new AuthScope(null, HttpHeader.HOST_LC, 80, realm, scheme), null);
        assertEquals("username", credentials.getUserName());
        assertEquals("password", new String(credentials.getPassword()));

        provider.removeCredentials(new AuthScope(null, HttpHeader.HOST_LC, 80, realm, scheme));

        credentials = (UsernamePasswordCredentials) provider
                        .getCredentials(new AuthScope(null, HttpHeader.HOST_LC, 80, realm, scheme), null);
        assertNull(credentials);
    }

}
