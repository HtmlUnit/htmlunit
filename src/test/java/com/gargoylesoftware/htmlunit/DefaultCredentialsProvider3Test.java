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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.junit.Test;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @author Marc Guillemot
 */
public class DefaultCredentialsProvider3Test {

    /**
     * Test.
     */
    @Test
    public void serialization() {
        final String protocol = "https";
        final String username = "foo";
        final char[] password = "password".toCharArray();
        final String host = "my.host";
        final int port = 1234;
        final String realm = "blah";
        final String scheme = "NTLM";

        DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials(username, password, host, port, realm);

        assertNotNull(provider.getCredentials(new AuthScope(protocol, host, port, realm, scheme), null));
        assertNull(provider.getCredentials(new AuthScope(protocol, "invalidHost", port, realm, scheme), null));
        assertNotNull(provider.getCredentials(new AuthScope(protocol, host, port, realm, scheme), null));
        assertNull(provider.getCredentials(new AuthScope(protocol, "invalidHost", port, realm, scheme), null));

        provider = SerializationUtils.clone(provider);

        assertNotNull(provider.getCredentials(new AuthScope(protocol, host, port, realm, scheme), null));
        assertNull(provider.getCredentials(new AuthScope(protocol, "invalidHost", port, realm, scheme), null));
        assertNotNull(provider.getCredentials(new AuthScope(protocol, host, port, realm, scheme), null));
        assertNull(provider.getCredentials(new AuthScope(protocol, "invalidHost", port, realm, scheme), null));
    }
}
