/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;

/**
 * Default HtmlUnit implementation of the <tt>CredentialsProvider</tt> interface. Provides
 * credentials for both web servers and proxies. Supports NTLM authentication, Digest
 * authentication, and Basic HTTP authentication.
 * 
 * @author Daniel Gredler
 * @version $Revision$
 */
public class DefaultCredentialsProvider implements CredentialsProvider {

    private final Map credentials_;
    private final Map proxyCredentials_;

    /**
     * Creates a new <tt>DefaultCredentialsProvider</tt> instance.
     */
    public DefaultCredentialsProvider() {
        credentials_ = new HashMap();
        proxyCredentials_ = new HashMap();
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
    public void addCredentials( final String username, final String password ) {
        this.addCredentials( username, password, AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM );
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
    public void addCredentials( final String username, final String password, final String host,
            final int port, final String realm ) {
        final AuthScope scope = new AuthScope( host, port, realm, AuthScope.ANY_SCHEME );
        final Credentials c = new UsernamePasswordCredentials( username, password );
        credentials_.put( scope, c );
    }

    /**
     * Adds proxy credentials for the specified username/password for any host/port/realm combination.
     * @param username The username for the new credentials.
     * @param password The password for the new credentials.
     */
    public void addProxyCredentials( final String username, final String password ) {
        this.addProxyCredentials( username, password, AuthScope.ANY_HOST, AuthScope.ANY_PORT );
    }

    /**
     * Adds proxy credentials for the specified username/password on the specified host/port.
     * @param username The username for the new credentials.
     * @param password The password for the new credentials.
     * @param host The host to which to the new credentials apply (<tt>null</tt> if applicable to any host).
     * @param port The port to which to the new credentials apply (negative if applicable to any port).
     */
    public void addProxyCredentials( final String username, final String password, final String host, final int port ) {
        final AuthScope scope = new AuthScope( host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME );
        final Credentials c = new UsernamePasswordCredentials( username, password );
        proxyCredentials_.put( scope, c );
    }

    /**
     * Returns the credentials associated with the specified scheme, host and port.
     * @param scheme The authentication scheme being used (basic, digest, NTLM, etc).
     * @param host The host we are authenticating for.
     * @param port The port we are authenticating for.
     * @param proxy Whether or not we are authenticating using a proxy.
     * @return The credentials correponding to the specified schem, host and port.
     * @throws CredentialsNotAvailableException If the specified credentials cannot be provided due to an error.
     * @see CredentialsProvider#getCredentials(AuthScheme, String, int, boolean)
     */
    public Credentials getCredentials( final AuthScheme scheme, final String host, final int port, final boolean proxy )
        throws CredentialsNotAvailableException {
        final Map credentials;
        if( proxy ) {
            credentials = proxyCredentials_;
        }
        else {
            credentials = credentials_;
        }
        for( final Iterator i = credentials.entrySet().iterator(); i.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) i.next();
            final AuthScope scope = (AuthScope) entry.getKey();
            final Credentials c = (Credentials) entry.getValue();
            if( scope.getScheme() == AuthScope.ANY_SCHEME || scope.getScheme().equals(scheme.getSchemeName()) ) {
                if( scope.getHost() == AuthScope.ANY_HOST || scope.getHost().equals(host) ) {
                    if( scope.getPort() == AuthScope.ANY_PORT || scope.getPort() == port ) {
                        if( scope.getRealm() == AuthScope.ANY_REALM || scope.getRealm().equals(scheme.getRealm()) ) {
                            return c;
                        }
                    }
                }
            }
        }
        return null;
    }

}
