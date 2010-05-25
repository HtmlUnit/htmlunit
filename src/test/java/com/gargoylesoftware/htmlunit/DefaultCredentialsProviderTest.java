/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertNotNull;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.junit.Test;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DefaultCredentialsProviderTest extends WebTestCase {

    //FIXME NTLM Is only partially supported in HttpClient 4.0
    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String username = "foo";
        final String password = "password";
        final String host = "my.host";
        final int port = 1234;
        final String realm = "blah";
        final String clientHost = "client.host";
        final AuthScope authScope = new AuthScope(host, port, realm);
        final AuthScope proxyAuthScope = new AuthScope(clientHost, port);

        final AuthScope invalidAuthScope = new AuthScope("invalidHost", port, realm);
        final AuthScope invalidProxyAuthScope = new AuthScope("invalidHost", port);

        DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.setCredentials(authScope, new UsernamePasswordCredentials(username, password));
        provider.setCredentials(proxyAuthScope, new UsernamePasswordCredentials(username, password));

        assertNotNull(provider.getCredentials(authScope));
        assertNull(provider.getCredentials(invalidAuthScope));
        assertNotNull(provider.getCredentials(proxyAuthScope));
        assertNull(provider.getCredentials(invalidProxyAuthScope));

        provider = clone(provider);

        assertNotNull(provider.getCredentials(authScope));
        assertNull(provider.getCredentials(invalidAuthScope));
        assertNotNull(provider.getCredentials(proxyAuthScope));
        assertNull(provider.getCredentials(invalidProxyAuthScope));
    }

    /**
     * Test that successive calls to {@link DefaultCredentialsProvider#addCredentials(String, String)}
     * overwrite values previously set.
     * @throws Exception if the test fails
     */
    @Test
    public void overwrite() throws Exception {
        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("username", "password"));

        UsernamePasswordCredentials credentials =
            (UsernamePasswordCredentials) provider.getCredentials(AuthScope.ANY);
        assertEquals("username", credentials.getUserName());
        assertEquals("password", credentials.getPassword());

        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("username", "new password"));
        credentials = (UsernamePasswordCredentials) provider.getCredentials(AuthScope.ANY);
        assertEquals("username", credentials.getUserName());
        assertEquals("new password", credentials.getPassword());

        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("new username", "other password"));
        credentials = (UsernamePasswordCredentials) provider.getCredentials(AuthScope.ANY);
        assertEquals("new username", credentials.getUserName());
        assertEquals("other password", credentials.getPassword());
    }
}
