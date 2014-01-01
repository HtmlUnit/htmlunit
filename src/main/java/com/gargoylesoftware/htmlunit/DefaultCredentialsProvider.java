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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;

/**
 * Default HtmlUnit implementation of the <tt>CredentialsProvider</tt> interface. Provides
 * credentials for both web servers and proxies. Supports Digest
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

    private final HashMap<AuthScopeProxy, CredentialsFactory> credentialsMap_
        = new HashMap<AuthScopeProxy, CredentialsFactory>();

    /**
     * Adds credentials for the specified username/password for any host/port/realm combination.
     * The credentials may be for any authentication scheme, including NTLM, digest and basic
     * HTTP authentication. If you are using sensitive username/password information, please do
     * NOT use this method. If you add credentials using this method, any server that requires
     * authentication may receive the specified username and password.
     * @param username the username for the new credentials
     * @param password the password for the new credentials
     */
    public void addCredentials(final String username, final String password) {
        addCredentials(username, password, AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
    }

    /**
     * Adds credentials for the specified username/password on the specified host/port for the
     * specified realm. The credentials may be for any authentication scheme, including NTLM,
     * digest and basic HTTP authentication.
     * @param username the username for the new credentials
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply (<tt>null</tt> if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     * @param realm the realm to which to the new credentials apply (<tt>null</tt> if applicable to any realm)
     */
    public void addCredentials(final String username, final String password, final String host,
            final int port, final String realm) {
        final AuthScope authscope = new AuthScope(host, port, realm, AuthScope.ANY_SCHEME);
        final Credentials credentials = new UsernamePasswordCredentials(username, password);
        setCredentials(authscope, credentials);
    }

    /**
     * Adds NTLM credentials for the specified username/password on the specified host/port.
     * @param username the username for the new credentials; should not include the domain to authenticate with;
     *        for example: <tt>"user"</tt> is correct whereas <tt>"DOMAIN\\user"</tt> is not
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply (<tt>null</tt> if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     * @param workstation The workstation the authentication request is originating from.
     *        Essentially, the computer name for this machine.
     * @param domain the domain to authenticate within
     */
    public void addNTLMCredentials(final String username, final String password, final String host,
            final int port, final String workstation, final String domain) {
        final AuthScope authscope = new AuthScope(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
        final Credentials credentials = new NTCredentials(username, password, workstation, domain);
        setCredentials(authscope, credentials);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setCredentials(final AuthScope authscope, final Credentials credentials) {
        if (authscope == null) {
            throw new IllegalArgumentException("Authentication scope may not be null");
        }
        final CredentialsFactory factory;
        if (credentials instanceof UsernamePasswordCredentials) {
            final UsernamePasswordCredentials userCredentials = (UsernamePasswordCredentials) credentials;
            factory = new UsernamePasswordCredentialsFactory(userCredentials.getUserName(),
                        userCredentials.getPassword());
        }
        else if (credentials instanceof NTCredentials) {
            final NTCredentials ntCredentials = (NTCredentials) credentials;
            factory = new NTCredentialsFactory(ntCredentials.getUserName(), ntCredentials.getPassword(),
                    ntCredentials.getWorkstation(), ntCredentials.getDomain());
        }
        else {
            throw new IllegalArgumentException("Unsupported Credential type: " + credentials.getClass().getName());
        }
        credentialsMap_.put(new AuthScopeProxy(authscope), factory);
    }

    /**
     * Find matching {@link Credentials credentials} for the given authentication scope.
     *
     * @param map the credentials hash map
     * @param authscope the {@link AuthScope authentication scope}
     * @return the credentials
     */
    private static Credentials matchCredentials(final HashMap<AuthScopeProxy, CredentialsFactory> map,
            final AuthScope authscope) {
        final CredentialsFactory factory = map.get(new AuthScopeProxy(authscope));
        Credentials creds = null;
        if (factory == null) {
            int bestMatchFactor = -1;
            AuthScope bestMatch = null;
            for (final AuthScopeProxy proxy : map.keySet()) {
                final AuthScope current = proxy.getAuthScope();
                final int factor = authscope.match(current);
                if (factor > bestMatchFactor) {
                    bestMatchFactor = factor;
                    bestMatch = current;
                }
            }
            if (bestMatch != null) {
                creds = map.get(new AuthScopeProxy(bestMatch)).getInstance();
            }
        }
        else {
            creds = factory.getInstance();
        }
        return creds;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Credentials getCredentials(final AuthScope authscope) {
        if (authscope == null) {
            throw new IllegalArgumentException("Authentication scope may not be null");
        }
        return matchCredentials(credentialsMap_, authscope);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return credentialsMap_.toString();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void clear() {
        credentialsMap_.clear();
    }

    /**
     * We have to wrap {@link AuthScope} instances in a serializable proxy so that the
     * {@link DefaultCredentialsProvider} class can be serialized correctly.
     */
    private static class AuthScopeProxy implements Serializable {
        private AuthScope authScope_;
        public AuthScopeProxy(final AuthScope authScope) {
            authScope_ = authScope;
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
        private String username_;
        private String password_;

        public UsernamePasswordCredentialsFactory(final String username, final String password) {
            username_ = username;
            password_ = password;
        }

        public Credentials getInstance() {
            return new UsernamePasswordCredentials(username_, password_);
        }

        @Override
        public String toString() {
            return getInstance().toString();
        }
    }

    /**
     * We have to create a factory class, so that credentials can be serialized correctly.
     */
    private static class NTCredentialsFactory implements CredentialsFactory, Serializable {
        private String username_;
        private String password_;
        private String workstation_;
        private String domain_;

        public NTCredentialsFactory(final String username, final String password, final String workstation,
                final String domain) {
            username_ = username;
            password_ = password;
            workstation_ = workstation;
            domain_ = domain;
        }

        public Credentials getInstance() {
            return new NTCredentials(username_, password_, workstation_, domain_);
        }

        @Override
        public String toString() {
            return getInstance().toString();
        }
    }

    /**
     * Factory class interface
     */
    private interface CredentialsFactory {
        Credentials getInstance();
    }

}
