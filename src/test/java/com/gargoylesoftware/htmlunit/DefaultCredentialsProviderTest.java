/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.auth.NTLMScheme;
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
    public void testAddNTLMCredentials() throws Exception {
        final String userName = "foo";
        final String domain = "myDomain";
        final String password = "password";
        final String host = "my.host";
        final String clientHost = "client.host";
        final int port = 1234;

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addNTLMCredentials(userName, password, host, port, clientHost, domain);

        final NTLMScheme scheme = new NTLMScheme("NTLM");
        final Credentials credentials = provider.getCredentials(scheme, host, port, false);
        assertNotNull(credentials);
        assertTrue(NTCredentials.class.isInstance(credentials));
        final NTCredentials ntCredentials = (NTCredentials) credentials;
        assertEquals(userName, ntCredentials.getUserName());
        assertEquals(password, ntCredentials.getPassword());
        assertEquals(clientHost, ntCredentials.getHost());
        assertEquals(domain, ntCredentials.getDomain());
    }
}
