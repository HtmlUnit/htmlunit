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
package com.gargoylesoftware.htmlunit.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL utilities class that makes it easy to create new URLs based off of old URLs
 * without having to assemble or parse them yourself.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public final class UrlUtils {

    /**
     * Disallow instantiation of this class.
     */
    private UrlUtils() {
        // Empty.
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified protocol.
     * @param u The URL on which to base the returned URL.
     * @param newProtocol The new protocol to use in the returned URL.
     * @return A new URL identical to the specified URL, except using the specified protocol.
     * @throws MalformedURLException If there is a problem creating the new URL.
     */
    public static URL getUrlWithNewProtocol(final URL u, final String newProtocol) throws MalformedURLException {
        return createNewUrl(newProtocol, u.getHost(), u.getPort(), u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified host.
     * @param u The URL on which to base the returned URL.
     * @param newHost The new host to use in the returned URL.
     * @return A new URL identical to the specified URL, except using the specified host.
     * @throws MalformedURLException If there is a problem creating the new URL.
     */
    public static URL getUrlWithNewHost(final URL u, final String newHost) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), newHost, u.getPort(), u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified port.
     * @param u The URL on which to base the returned URL.
     * @param newPort The new port to use in the returned URL.
     * @return A new URL identical to the specified URL, except using the specified port.
     * @throws MalformedURLException If there is a problem creating the new URL.
     */
    public static URL getUrlWithNewPort(final URL u, final int newPort) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getHost(), newPort, u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified path.
     * @param u The URL on which to base the returned URL.
     * @param newPath The new path to use in the returned URL.
     * @return A new URL identical to the specified URL, except using the specified path.
     * @throws MalformedURLException If there is a problem creating the new URL.
     */
    public static URL getUrlWithNewPath(final URL u, final String newPath) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getHost(), u.getPort(), newPath, u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified reference.
     * @param u The URL on which to base the returned URL.
     * @param newRef The new reference to use in the returned URL.
     * @return A new URL identical to the specified URL, except using the specified reference.
     * @throws MalformedURLException If there is a problem creating the new URL.
     */
    public static URL getUrlWithNewRef(final URL u, final String newRef) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getHost(), u.getPort(), u.getPath(), newRef, u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified query string.
     * @param u The URL on which to base the returned URL.
     * @param newQuery The new query string to use in the returned URL.
     * @return A new URL identical to the specified URL, except using the specified query string.
     * @throws MalformedURLException If there is a problem creating the new URL.
     */
    public static URL getUrlWithNewQuery(final URL u, final String newQuery) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getHost(), u.getPort(), u.getPath(), u.getRef(), newQuery);
    }

    /**
     * Creates a new URL based on the specified fragments.
     * @param protocol The protocol to use. May not be <tt>null</tt>.
     * @param host The host to use. May not be <tt>null</tt>.
     * @param port The port to use. May be <tt>-1</tt> if no port is specified.
     * @param path The path to use. May be <tt>null</tt> and may omit the initial <tt>'/'</tt>.
     * @param ref The reference to use. May be <tt>null</tt>. Must not include the <tt>'#'</tt>.
     * @param query The query to use. May be <tt>null</tt>. Must not include the <tt>'?'</tt>.
     * @return A new URL based on the specified fragments.
     * @throws MalformedURLException If there is a problem creating the new URL.
     */
    private static URL createNewUrl(final String protocol, final String host, final int port,
            final String path, final String ref, final String query) throws MalformedURLException {
        final StringBuffer s = new StringBuffer();
        s.append(protocol);
        s.append("://");
        s.append(host);
        if (port != -1) {
            s.append(":").append(port);
        }
        if (path != null && path.length() > 0) {
            if (!path.startsWith("/")) {
                s.append("/");
            }
            s.append(path);
        }
        if (query != null) {
            s.append("?").append(query);
        }
        if (ref != null) {
            s.append("#").append(ref);
        }
        final URL url = new URL(s.toString());
        return url;
    }

}
