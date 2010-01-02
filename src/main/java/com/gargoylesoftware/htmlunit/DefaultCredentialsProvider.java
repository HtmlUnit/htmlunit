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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 */
public class DefaultCredentialsProvider implements CredentialsProvider, Serializable  {

    private static final long serialVersionUID = 1036331144926557053L;
    private static final Log LOG = LogFactory.getLog(DefaultCredentialsProvider.class);

    private final Map<AuthScopeProxy, Credentials> credentials_ = new HashMap<AuthScopeProxy, Credentials>();
    private final Map<AuthScopeProxy, Credentials> proxyCredentials_ = new HashMap<AuthScopeProxy, Credentials>();
    private final Set<Object> answerMarks_ = Collections.synchronizedSortedSet(new TreeSet<Object>());

    /**
     * Creates a new <tt>DefaultCredentialsProvider</tt> instance.
     */
    public DefaultCredentialsProvider() {
        // nothing
    }

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
        final AuthScopeProxy scope = new AuthScopeProxy(host, port, realm, AuthScope.ANY_SCHEME);
        final Credentials c = new UsernamePasswordCredentialsExt(username, password);
        credentials_.put(scope, c);
        clearAnswered(); // don't need to be precise, will cause in worst case one extra request
    }

    /**
     * Adds proxy credentials for the specified username/password for any host/port/realm combination.
     * @param username the username for the new credentials
     * @param password the password for the new credentials
     */
    public void addProxyCredentials(final String username, final String password) {
        addProxyCredentials(username, password, AuthScope.ANY_HOST, AuthScope.ANY_PORT);
    }

    /**
     * Adds proxy credentials for the specified username/password on the specified host/port.
     * @param username the username for the new credentials
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply (<tt>null</tt> if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     */
    public void addProxyCredentials(final String username, final String password, final String host, final int port) {
        final AuthScopeProxy scope = new AuthScopeProxy(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
        final Credentials c = new UsernamePasswordCredentialsExt(username, password);
        proxyCredentials_.put(scope, c);
        clearAnswered(); // don't need to be precise, will cause in worst case one extra request
    }

    /**
     * Adds NTLM credentials for the specified username/password on the specified host/port.
     * @param username the username for the new credentials; should not include the domain to authenticate with;
     *        for example: <tt>"user"</tt> is correct whereas <tt>"DOMAIN\\user"</tt> is not
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply (<tt>null</tt> if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     * @param clientHost the host the authentication request is originating from; essentially, the computer name for
     *        this machine.
     * @param clientDomain the domain to authenticate within
     */
    public void addNTLMCredentials(final String username, final String password, final String host,
            final int port, final String clientHost, final String clientDomain) {
        final AuthScopeProxy scope = new AuthScopeProxy(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
        final Credentials c = new NTCredentialsExt(username, password, clientHost, clientDomain);
        credentials_.put(scope, c);
        clearAnswered(); // don't need to be precise, will cause in worst case one extra request
    }

    /**
     * Adds NTLM proxy credentials for the specified username/password on the specified host/port.
     * @param username the username for the new credentials; should not include the domain to authenticate with;
     *        for example: <tt>"user"</tt> is correct whereas <tt>"DOMAIN\\user"</tt> is not.
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply (<tt>null</tt> if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     * @param clientHost the host the authentication request is originating from; essentially, the computer name for
     *        this machine
     * @param clientDomain the domain to authenticate within
     */
    public void addNTLMProxyCredentials(final String username, final String password, final String host,
            final int port, final String clientHost, final String clientDomain) {
        final AuthScopeProxy scope = new AuthScopeProxy(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
        final Credentials c = new NTCredentialsExt(username, password, clientHost, clientDomain);
        proxyCredentials_.put(scope, c);
        clearAnswered(); // don't need to be precise, will cause in worst case one extra request
    }

    /**
     * Returns the credentials associated with the specified scheme, host and port.
     * @param scheme the authentication scheme being used (basic, digest, NTLM, etc)
     * @param host the host we are authenticating for
     * @param port the port we are authenticating for
     * @param proxy Whether or not we are authenticating using a proxy
     * @return the credentials corresponding to the specified scheme, host and port or <code>null</code>
     * if already asked for it to avoid infinite loop
     * @throws CredentialsNotAvailableException if the specified credentials cannot be provided due to an error
     * @see CredentialsProvider#getCredentials(AuthScheme, String, int, boolean)
     */
    public Credentials getCredentials(final AuthScheme scheme, final String host, final int port, final boolean proxy)
        throws CredentialsNotAvailableException {

        // It's the responsibility of the CredentialProvider to answer only once with a
        // given Credentials to avoid infinite loop if it is incorrect:
        // see http://issues.apache.org/bugzilla/show_bug.cgi?id=8140
        if (alreadyAnswered(scheme, host, port, proxy)) {
            LOG.debug("Already answered for " + buildKey(scheme, host, port, proxy) + ", returning null");
            return null;
        }

        final Map<AuthScopeProxy, Credentials> credentials;
        if (proxy) {
            credentials = proxyCredentials_;
        }
        else {
            credentials = credentials_;
        }

        for (final Map.Entry<AuthScopeProxy, Credentials> entry : credentials.entrySet()) {
            final AuthScope scope = entry.getKey().getAuthScope();
            final Credentials c = entry.getValue();
            if (matchScheme(scope, scheme) && matchHost(scope, host)
                && matchPort(scope, port) && matchRealm(scope, scheme)) {
                markAsAnswered(scheme, host, port, proxy);
                LOG.debug("Returning " + c + " for " + buildKey(scheme, host, port, proxy));
                return c;
            }
        }

        LOG.debug("No credential found for " + buildKey(scheme, host, port, proxy));
        return null;
    }

    /**
     * @param scheme the request scheme for which Credentials are asked
     * @param scope the configured authorization scope
     * @return <code>true</code> if the scope's realm matches the one of the scheme
     */
    protected boolean matchRealm(final AuthScope scope, final AuthScheme scheme) {
        return scope.getRealm() == AuthScope.ANY_REALM || scope.getRealm().equals(scheme.getRealm());
    }

    /**
     * @param port the request port for which Credentials are asked
     * @param scope the configured authorization scope
     * @return <code>true</code> if the scope's port matches the provided one
     */
    protected boolean matchPort(final AuthScope scope, final int port) {
        return scope.getPort() == AuthScope.ANY_PORT || scope.getPort() == port;
    }

    /**
     * @param host the request host for which Credentials are asked
     * @param scope the configured authorization scope
     * @return <code>true</code> if the scope's host matches the provided one
     */
    protected boolean matchHost(final AuthScope scope, final String host) {
        return scope.getHost() == AuthScope.ANY_HOST || scope.getHost().equals(host);
    }

    /**
     * @param scheme the request scheme for which Credentials are asked
     * @param scope the configured authorization scope
     * @return <code>true</code> if the scope's scheme matches the provided one
     */
    protected boolean matchScheme(final AuthScope scope, final AuthScheme scheme) {
        return scope.getScheme() == AuthScope.ANY_SCHEME || scope.getScheme().equals(scheme.getSchemeName());
    }

    /**
     * Returns <tt>true</tt> if this provider has already provided an answer for the
     * specified (scheme, host, port, proxy) combination.
     * @param scheme the scheme
     * @param host the server name
     * @param port the server port
     * @param proxy is proxy
     * @return true if the provider has already provided an answer for this
     */
    protected boolean alreadyAnswered(final AuthScheme scheme, final String host, final int port, final boolean proxy) {
        return answerMarks_.contains(buildKey(scheme, host, port, proxy));
    }

    /**
     * Marks the specified (scheme, host, port, proxy) combination as having already been processed.
     * @param scheme the scheme
     * @param host the server name
     * @param port the server port
     * @param proxy is proxy
     */
    protected void markAsAnswered(final AuthScheme scheme, final String host, final int port, final boolean proxy) {
        answerMarks_.add(buildKey(scheme, host, port, proxy));
    }

    /**
     * Clears the cache of answered (scheme, host, port, proxy) combinations.
     */
    protected void clearAnswered() {
        answerMarks_.clear();
        LOG.debug("Flushed marked answers");
    }

    /**
     * Builds a key with the specified data.
     * @param scheme the scheme
     * @param host the server name
     * @param port the server port
     * @param proxy is proxy
     * @return the new key
     */
    protected Object buildKey(final AuthScheme scheme, final String host, final int port, final boolean proxy) {
        return scheme.getSchemeName() + " " + scheme.getRealm() + " " + host + ":" + port + " " + proxy;
    }

    /**
     * Clears the cache of answered credentials requests upon deserialization.
     * @param stream the object stream containing the instance being deserialized
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        answerMarks_.clear();
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
     * We have to extend {@link UsernamePasswordCredentials} so that the
     * {@link DefaultCredentialsProvider} class can be serialized correctly.
     */
    private static class UsernamePasswordCredentialsExt
        extends UsernamePasswordCredentials implements Serializable {
        private static final long serialVersionUID = 6578356387067132849L;
        public UsernamePasswordCredentialsExt(final String username, final String password) {
            super(username, password);
        }
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.writeObject(this.getUserName());
            stream.writeObject(this.getPassword());
        }
        @SuppressWarnings("deprecation")
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            final String username = (String) stream.readObject();
            final String password = (String) stream.readObject();
            this.setUserName(username);
            this.setPassword(password);
        }
    }

    /**
     * We have to extend {@link NTCredentials} so that the
     * {@link DefaultCredentialsProvider} class can be serialized correctly.
     */
    private static class NTCredentialsExt extends NTCredentials implements Serializable {
        private static final long serialVersionUID = 1390068355010421017L;
        public NTCredentialsExt(final String username, final String password, final String host, final String domain) {
            super(username, password, host, domain);
        }
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.writeObject(this.getUserName());
            stream.writeObject(this.getPassword());
            stream.writeObject(this.getHost());
            stream.writeObject(this.getDomain());
        }
        @SuppressWarnings("deprecation")
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            final String username = (String) stream.readObject();
            final String password = (String) stream.readObject();
            final String host = (String) stream.readObject();
            final String domain = (String) stream.readObject();
            this.setUserName(username);
            this.setPassword(password);
            this.setHost(host);
            this.setDomain(domain);
        }
    }

}
