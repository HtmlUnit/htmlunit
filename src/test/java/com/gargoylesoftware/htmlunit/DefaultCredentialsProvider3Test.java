/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.auth.AuthScope;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DefaultCredentialsProvider3Test {

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
        final String scheme = "NTLM";

        DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials(username, password, host, port, realm);

        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        Assert.assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        Assert.assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));

        provider = SerializationUtils.clone(provider);

        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        Assert.assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        Assert.assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
    }
}
