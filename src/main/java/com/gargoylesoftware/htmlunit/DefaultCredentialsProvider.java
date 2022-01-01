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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;

/**
 * Default HtmlUnit implementation of the <tt>CredentialsProvider</tt> interface. Provides
 * credentials for both web servers and proxies. Supports Digest
 * authentication, Socks authentication and Basic HTTP authentication.
 *
 * @author Daniel Gredler
 * @author Vikram Shitole
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Nicolas Belisle
 * @author Ronald Brill
 */
public class DefaultCredentialsProvider implements CredentialsProvider, Serializable {

    /** The {@code null} value represents any host. */
    public static final String ANY_HOST = null;

    /**The {@code -1} value represents any port. */
    public static final int ANY_PORT = -1;

    /** The {@code null} value represents any realm. */
    public static final String ANY_REALM = null;

    /** The {@code null} value represents any authentication scheme. */
    public static final String ANY_SCHEME = null;

    // Because this is used for the whole JVM i try to make it as less invasive as possible.
    // But in general this might disturb other application running on the same JVM.
    private static final class SocksProxyAuthenticator extends Authenticator {
        private CredentialsProvider credentialsProvider_;

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            // java.base/java/net/SocksSocketImpl.java line 154 ff
            // no RequestorType set from java - we have to check the requesting prompt string
            final boolean isProxy = Authenticator.RequestorType.PROXY.equals(getRequestorType())
                    || "SOCKS authentication".equals(getRequestingPrompt());
            if (!isProxy) {
                return null;
            }

            final AuthScope authScope = new AuthScope(getRequestingHost(), getRequestingPort(), getRequestingScheme());
            final Credentials credentials = credentialsProvider_.getCredentials(authScope);
            if (credentials == null) {
                return null;
            }

            return new PasswordAuthentication(credentials.getUserPrincipal().getName(),
                    credentials.getPassword().toCharArray());
        }
    }

    private static SocksProxyAuthenticator SocksAuthenticator_;

    private final Map<AuthScopeProxy, Credentials> credentialsMap_ = new HashMap<>();

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
        addCredentials(username, password, ANY_HOST, ANY_PORT, ANY_REALM);
    }

    /**
     * Adds credentials for the specified username/password on the specified host/port for the
     * specified realm. The credentials may be for any authentication scheme, including NTLM,
     * digest and basic HTTP authentication.
     * @param username the username for the new credentials
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply ({@code null} if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     * @param realm the realm to which to the new credentials apply ({@code null} if applicable to any realm)
     */
    public void addCredentials(final String username, final String password, final String host,
            final int port, final String realm) {
        final AuthScope authscope = new AuthScope(host, port, realm, ANY_SCHEME);
        final Credentials credentials = new UsernamePasswordCredentials(username, password);
        setCredentials(authscope, credentials);
    }

    /**
     * Adds NTLM credentials for the specified username/password on the specified host/port.
     * @param username the username for the new credentials; should not include the domain to authenticate with;
     *        for example: <tt>"user"</tt> is correct whereas <tt>"DOMAIN\\user"</tt> is not
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply ({@code null} if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     * @param workstation The workstation the authentication request is originating from.
     *        Essentially, the computer name for this machine.
     * @param domain the domain to authenticate within
     */
    public void addNTLMCredentials(final String username, final String password, final String host,
            final int port, final String workstation, final String domain) {
        final AuthScope authscope = new AuthScope(host, port, ANY_REALM, ANY_SCHEME);
        final Credentials credentials = new NTCredentials(username, password, workstation, domain);
        setCredentials(authscope, credentials);
    }

    /**
     * Adds Socks credentials for the specified username/password on the specified host/port.
     * @param username the username for the new credentials
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply ({@code null} if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     */
    public void addSocksCredentials(final String username, final String password, final String host,
            final int port) {
        final AuthScope authscope = new AuthScope(host, port, ANY_REALM, ANY_SCHEME);
        final Credentials credentials = new UsernamePasswordCredentials(username, password);
        setCredentials(authscope, credentials);

        initSocksAuthenticatorIfNeeded(this);
    }

    private static synchronized void initSocksAuthenticatorIfNeeded(final CredentialsProvider provider) {
        if (SocksAuthenticator_ == null) {
            SocksAuthenticator_ = new SocksProxyAuthenticator();
            SocksAuthenticator_.credentialsProvider_ = provider;

            Authenticator.setDefault(SocksAuthenticator_);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setCredentials(final AuthScope authscope, final Credentials credentials) {
        if (authscope == null) {
            throw new IllegalArgumentException("Authentication scope may not be null");
        }

        if ((credentials instanceof UsernamePasswordCredentials) || (credentials instanceof NTCredentials)) {
            credentialsMap_.put(new AuthScopeProxy(authscope), credentials);
            return;
        }

        throw new IllegalArgumentException("Unsupported Credential type: " + credentials.getClass().getName());
    }

    /**
     * Find matching {@link Credentials credentials} for the given authentication scope.
     *
     * @param map the credentials hash map
     * @param authscope the {@link AuthScope authentication scope}
     * @return the credentials
     */
    private static Credentials matchCredentials(final Map<AuthScopeProxy, Credentials> map, final AuthScope authscope) {
        Credentials creds = map.get(new AuthScopeProxy(authscope));
        if (creds == null) {
            int bestMatchFactor = -1;
            AuthScopeProxy bestMatch = null;
            for (final AuthScopeProxy proxy : map.keySet()) {
                final AuthScope current = proxy.getAuthScope();
                final int factor = authscope.match(current);
                if (factor > bestMatchFactor) {
                    bestMatchFactor = factor;
                    bestMatch = proxy;
                }
            }
            if (bestMatch != null) {
                creds = map.get(bestMatch);
            }
        }
        return creds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Credentials getCredentials(final AuthScope authscope) {
        if (authscope == null) {
            throw new IllegalArgumentException("Authentication scope may not be null");
        }
        return matchCredentials(credentialsMap_, authscope);
    }

    /**
     * Removes the credentials from the AuthScope.
     * @param authscope the AuthScope to remove the credentials of
     * @return whether it was removed or not
     */
    public synchronized boolean removeCredentials(final AuthScope authscope) {
        if (authscope == null) {
            throw new IllegalArgumentException("Authentication scope may not be null");
        }
        int bestMatchFactor = -1;
        AuthScopeProxy bestMatch = null;
        for (final AuthScopeProxy proxy : credentialsMap_.keySet()) {
            final AuthScope current = proxy.getAuthScope();
            final int factor = authscope.match(current);
            if (factor > bestMatchFactor) {
                bestMatchFactor = factor;
                bestMatch = proxy;
            }
        }
        return credentialsMap_.remove(bestMatch) != null;
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
    @Override
    public synchronized void clear() {
        credentialsMap_.clear();
    }

    /**
     * We have to wrap {@link AuthScope} instances in a serializable proxy so that the
     * {@link DefaultCredentialsProvider} class can be serialized correctly.
     */
    private static class AuthScopeProxy implements Serializable {
        private AuthScope authScope_;

        AuthScopeProxy(final AuthScope authScope) {
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
}
