/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

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

    private final Map<AuthScope, Credentials> credentials_ = new HashMap<AuthScope, Credentials>();
    private final Map<AuthScope, Credentials> proxyCredentials_ = new HashMap<AuthScope, Credentials>();
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
     * authentication will receive the specified username and password.
     * @param username The username for the new credentials.
     * @param password The password for the new credentials.
     */
    public void addCredentials(final String username, final String password) {
        addCredentials(username, password, AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
    }

    /**
     * Adds credentials for the specified username/password on the specified host/port for the
     * specified realm. The credentials may be for any authentication scheme, including NTLM,
     * digest and basic HTTP authentication.
     * @param username The username for the new credentials.
     * @param password The password for the new credentials.
     * @param host The host to which to the new credentials apply (<tt>null</tt> if applicable to any host).
     * @param port The port to which to the new credentials apply (negative if applicable to any port).
     * @param realm The realm to which to the new credentials apply (<tt>null</tt> if applicable to any realm).
     */
    public void addCredentials(final String username, final String password, final String host,
            final int port, final String realm) {
        final AuthScope scope = new AuthScope(host, port, realm, AuthScope.ANY_SCHEME);
        final Credentials c = new UsernamePasswordCredentials(username, password);
        credentials_.put(scope, c);
        clearAnswered(); // don't need to be precise, will cause in worst case one extra request
    }

    /**
     * Adds proxy credentials for the specified username/password for any host/port/realm combination.
     * @param username The username for the new credentials.
     * @param password The password for the new credentials.
     */
    public void addProxyCredentials(final String username, final String password) {
        addProxyCredentials(username, password, AuthScope.ANY_HOST, AuthScope.ANY_PORT);
    }

    /**
     * Adds proxy credentials for the specified username/password on the specified host/port.
     * @param username The username for the new credentials.
     * @param password The password for the new credentials.
     * @param host The host to which to the new credentials apply (<tt>null</tt> if applicable to any host).
     * @param port The port to which to the new credentials apply (negative if applicable to any port).
     */
    public void addProxyCredentials(final String username, final String password, final String host, final int port) {
        final AuthScope scope = new AuthScope(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
        final Credentials c = new UsernamePasswordCredentials(username, password);
        proxyCredentials_.put(scope, c);
        clearAnswered(); // don't need to be precise, will cause in worst case one extra request
    }

    /**
     * Adds NTLM credentials for the specified username/password on the specified host/port.
     * @param username The username for the new credentials. This should not include the domain to authenticate with.
     * For example: <tt>"user"</tt> is correct whereas <tt>"DOMAIN\\user"</tt> is not.
     * @param password The password for the new credentials.
     * @param host The host to which to the new credentials apply (<tt>null</tt> if applicable to any host).
     * @param port The port to which to the new credentials apply (negative if applicable to any port).
     * @param clientHost The host the authentication request is originating from. Essentially, the computer name for
     * this machine.
     * @param clientDomain The domain to authenticate within.
     */
    public void addNTLMCredentials(final String username, final String password, final String host,
            final int port, final String clientHost, final String clientDomain) {
        final AuthScope scope = new AuthScope(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
        final Credentials c = new NTCredentials(username, password, clientHost, clientDomain);
        credentials_.put(scope, c);
        clearAnswered(); // don't need to be precise, will cause in worst case one extra request
    }

    /**
     * Adds NTLM proxy credentials for the specified username/password on the specified host/port.
     * @param username The username for the new credentials. This should not include the domain to authenticate with.
     * For example: <tt>"user"</tt> is correct whereas <tt>"DOMAIN\\user"</tt> is not.
     * @param password The password for the new credentials.
     * @param host The host to which to the new credentials apply (<tt>null</tt> if applicable to any host).
     * @param port The port to which to the new credentials apply (negative if applicable to any port).
     * @param clientHost The host the authentication request is originating from. Essentially, the computer name for
     * this machine.
     * @param clientDomain The domain to authenticate within.
     */
    public void addNTLMProxyCredentials(final String username, final String password, final String host,
            final int port, final String clientHost, final String clientDomain) {
        final AuthScope scope = new AuthScope(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
        final Credentials c = new NTCredentials(username, password, clientHost, clientDomain);
        proxyCredentials_.put(scope, c);
        clearAnswered(); // don't need to be precise, will cause in worst case one extra request
    }

    /**
     * Returns the credentials associated with the specified scheme, host and port
     * @param scheme The authentication scheme being used (basic, digest, NTLM, etc).
     * @param host The host we are authenticating for.
     * @param port The port we are authenticating for.
     * @param proxy Whether or not we are authenticating using a proxy.
     * @return The credentials corresponding to the specified scheme, host and port or <code>null</code>
     * if already asked for it to avoid infinite loop
     * @throws CredentialsNotAvailableException If the specified credentials cannot be provided due to an error.
     * @see CredentialsProvider#getCredentials(AuthScheme, String, int, boolean)
     */
    public Credentials getCredentials(final AuthScheme scheme, final String host, final int port, final boolean proxy)
        throws CredentialsNotAvailableException {

        // it's the responsibility of the CredentialProvider to answer only once with a given Credentials
        // to avoid infinite loop if it is incorrect
        // see http://issues.apache.org/bugzilla/show_bug.cgi?id=8140
        if (alreadyAnswered(scheme, host, port, proxy)) {
            getLog().debug("Already answered for " + buildKey(scheme, host, port, proxy)
                    + " returning null");
            return null;
        }

        final Map<AuthScope, Credentials> credentials;
        if (proxy) {
            credentials = proxyCredentials_;
        }
        else {
            credentials = credentials_;
        }

        for (final Map.Entry<AuthScope, Credentials> entry : credentials.entrySet()) {
            final AuthScope scope = entry.getKey();
            final Credentials c = entry.getValue();
            if (matchScheme(scope, scheme) && matchHost(scope, host)
                    && matchPort(scope, port) && matchRealm(scope, scheme)) {

                markAsAnswered(scheme, host, port, proxy);
                getLog().debug("Returning " + c + " for " + buildKey(scheme, host, port, proxy));
                return c;
            }
        }

        getLog().debug("No credential found for " + buildKey(scheme, host, port, proxy));
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
     * Indicates if this provider has already provided an answer for this (scheme, host, port, proxy).
     * @param scheme The scheme
     * @param host the server name.
     * @param port the server port.
     * @param proxy is proxy
     * @return true if the provider has already provided an answer for this.
     */
    protected boolean alreadyAnswered(final AuthScheme scheme, final String host, final int port, final boolean proxy) {
        return answerMarks_.contains(buildKey(scheme, host, port, proxy));
    }

    /**
     * @param scheme The scheme
     * @param host the server name.
     * @param port the server port.
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
        getLog().debug("Flushed marked answers");
    }

    /**
     * Build a key with the specified data
     * @param scheme The scheme
     * @param host the server name.
     * @param port the server port.
     * @param proxy is proxy
     * @return the new key.
     */
    protected Object buildKey(final AuthScheme scheme, final String host, final int port, final boolean proxy) {
        return scheme.getSchemeName() + " " + scheme.getRealm() + " " + host + ":" + port + " " + proxy;
    }

    /**
     * Returns the log object for this class
     * @return The log object
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

}
