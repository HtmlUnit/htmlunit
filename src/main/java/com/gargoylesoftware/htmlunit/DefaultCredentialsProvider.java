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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;

/**
 * Default HtmlUnit implementation of the <tt>CredentialsProvider</tt> interface. Provides
 * credentials for both web servers and proxies. Supports NTLM authentication, Digest
 * authentication, and Basic HTTP authentication.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Vikram Shitole
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Nicolas Belisle
 */
public class DefaultCredentialsProvider implements CredentialsProvider, Serializable {

    private static final long serialVersionUID = -1622084425121018024L;
    private final Map<AuthScopeProxy, CredentialsFactory> credentials_;

    /**
     * Creates a new <tt>DefaultCredentialsProvider</tt> instance.
     */
    public DefaultCredentialsProvider() {
        credentials_ = new HashMap<AuthScopeProxy, CredentialsFactory>();
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        credentials_.clear();
    }

    /**
     * {@inheritDoc}
     */
    public Credentials getCredentials(final AuthScope authscope) {
        for (final Map.Entry<AuthScopeProxy, CredentialsFactory> entry : credentials_.entrySet()) {
            final AuthScope authScopeEntry = entry.getKey().getAuthScope();
            if (authScopeEntry.match(authscope) > 0) {
                return entry.getValue().getInstance();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setCredentials(final AuthScope authScope, final Credentials credentials) {
        final AuthScopeProxy authScopeProxy =
            new AuthScopeProxy(authScope.getHost(), authScope.getPort(), authScope.getRealm(), authScope.getScheme());
        final String username =
            StringUtils.substringBetween(credentials.getUserPrincipal().toString(), "[principal: ", "]");
        final UsernamePasswordCredentialsFactory credentialsFactory =
            new UsernamePasswordCredentialsFactory(username, credentials.getPassword());
        credentials_.put(authScopeProxy, credentialsFactory);
    }

    /**
     * We have to wrap {@link AuthScope} instances in a serializable proxy so that the
     * {@link DefaultCredentialsProvider} class can be serialized correctly.
     */
    private static class AuthScopeProxy implements Serializable {
        private static final long serialVersionUID = 1464373861677912537L;
        private AuthScope authScope_;

        public AuthScopeProxy(final String host, final int port, final String realm, final String scheme) {
            authScope_ = new AuthScope(host, port, realm, scheme);
        }
        public AuthScope getAuthScope() {
            return authScope_;
        }
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.writeObject(authScope_.getHost());
            stream.writeInt(authScope_.getPort());
            stream.writeObject(authScope_.getRealm());
            stream.writeObject(authScope_.getScheme());
        }
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            final String host = (String) stream.readObject();
            final int port = stream.readInt();
            final String realm = (String) stream.readObject();
            final String scheme = (String) stream.readObject();
            authScope_ = new AuthScope(host, port, realm, scheme);
        }
        @Override
        public int hashCode() {
            return authScope_.hashCode();
        }
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof AuthScopeProxy && authScope_.equals(((AuthScopeProxy) obj).getAuthScope());
        }
    }

    /**
     * We have to create a factory class, so that credentials can be serialized correctly.
     */
    private static class UsernamePasswordCredentialsFactory implements CredentialsFactory, Serializable {
        private static final long serialVersionUID = 5578356387067132849L;

        private String username_;
        private String password_;

        public UsernamePasswordCredentialsFactory(final String username, final String password) {
            username_ = username;
            password_ = password;
        }

        public Credentials getInstance() {
            return new UsernamePasswordCredentials(username_, password_);
        }
    }

    /**
     * Factory class interface
     */
    private interface CredentialsFactory {
        Credentials getInstance();
    }
}
