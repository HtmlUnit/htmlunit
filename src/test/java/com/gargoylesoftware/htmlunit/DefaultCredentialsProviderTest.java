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
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.auth.BasicScheme;
import org.junit.Test;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DefaultCredentialsProviderTest extends WebTestCase {

    /**
     * Test for NTLM credentials.
     * @throws Exception if the test fails
     */
    @Test
    public void addNTLMCredentials() throws Exception {
        final String username = "foo";
        final String domain = "MYDOMAIN";
        final String password = "password";
        final String host = "my.host";
        final String clientHost = "CLIENT.HOST";
        final int port = 1234;
        final String realm = AuthScope.ANY_REALM;
        final String scheme = "NTLM";

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addNTLMCredentials(username, password, host, port, clientHost, domain);

        final Credentials credentials = provider.getCredentials(new AuthScope(host, port, realm, scheme));
        assertNotNull(credentials);
        assertTrue(NTCredentials.class.isInstance(credentials));

        final NTCredentials ntCredentials = (NTCredentials) credentials;
        assertEquals(username, ntCredentials.getUserName());
        assertEquals(password, ntCredentials.getPassword());
        assertEquals(clientHost, ntCredentials.getWorkstation());
        assertEquals(domain, ntCredentials.getDomain());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String username = "foo";
        final String password = "password";
        final String host = "my.host";
        final int port = 1234;
        final String realm = "blah";
        final String clientDomain = "MYDOMAIN";
        final String clientHost = "CLIENT.HOST";
        final String scheme = "NTLM";

        DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials(username, password, host, port, realm);
        provider.addNTLMCredentials(username, password, host, port, clientHost, clientDomain);

        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));

        provider = clone(provider);

        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
    }

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
        provider.addCredentials("username", "password");

        UsernamePasswordCredentials credentials =
            (UsernamePasswordCredentials) provider.getCredentials(new AuthScope("host", 80, realm, scheme));
        assertEquals("username", credentials.getUserName());
        assertEquals("password", credentials.getPassword());

        provider.addCredentials("username", "new password");
        credentials = (UsernamePasswordCredentials) provider.getCredentials(new AuthScope("host", 80, realm, scheme));
        assertEquals("username", credentials.getUserName());
        assertEquals("new password", credentials.getPassword());

        provider.addCredentials("new username", "other password");
        credentials = (UsernamePasswordCredentials) provider.getCredentials(new AuthScope("host", 80, realm, scheme));
        assertEquals("new username", credentials.getUserName());
        assertEquals("other password", credentials.getPassword());
    }
}
