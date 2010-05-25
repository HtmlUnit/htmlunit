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

import java.io.Serializable;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;

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
public class DefaultCredentialsProvider extends BasicCredentialsProvider implements Serializable {

    private static final long serialVersionUID = 8243249154922357903L;

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
     * Adds proxy credentials for the specified username/password for any host/port/realm combination.
     * @param username the username for the new credentials
     * @param password the password for the new credentials
     * @deprecated as of 2.8, please use {@link #addCredentials(String, String)} instead
     */
    @Deprecated
    public void addProxyCredentials(final String username, final String password) {
        addCredentials(username, password);
    }

    /**
     * Adds proxy credentials for the specified username/password on the specified host/port.
     * @param username the username for the new credentials
     * @param password the password for the new credentials
     * @param host the host to which to the new credentials apply (<tt>null</tt> if applicable to any host)
     * @param port the port to which to the new credentials apply (negative if applicable to any port)
     * @deprecated as of 2.8, please use {@link #addCredentials(String, String, String, int, String)} instead
     */
    @Deprecated
    public void addProxyCredentials(final String username, final String password, final String host, final int port) {
        addCredentials(username, password, host, port, AuthScope.ANY_REALM);
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
        final AuthScope authscope = new AuthScope(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
        final Credentials credentials = new NTCredentials(username, password, clientHost, clientDomain);
        setCredentials(authscope, credentials);
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
     * @deprecated as of 2.8,
     *             please use {@link #addNTLMCredentials(String, String, String, int, String, String)} instead
     */
    @Deprecated
    public void addNTLMProxyCredentials(final String username, final String password, final String host,
            final int port, final String clientHost, final String clientDomain) {
        addNTLMCredentials(username, password, host, port, clientHost, clientDomain);
    }
 
}
