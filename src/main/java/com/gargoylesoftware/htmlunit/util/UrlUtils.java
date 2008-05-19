/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
     * @param u the URL on which to base the returned URL
     * @param newProtocol the new protocol to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified protocol
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewProtocol(final URL u, final String newProtocol) throws MalformedURLException {
        return createNewUrl(newProtocol, u.getHost(), u.getPort(), u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified host.
     * @param u the URL on which to base the returned URL
     * @param newHost the new host to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified host
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewHost(final URL u, final String newHost) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), newHost, u.getPort(), u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified port.
     * @param u the URL on which to base the returned URL
     * @param newPort the new port to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified port
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewPort(final URL u, final int newPort) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getHost(), newPort, u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified path.
     * @param u the URL on which to base the returned URL
     * @param newPath the new path to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified path
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewPath(final URL u, final String newPath) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getHost(), u.getPort(), newPath, u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified reference.
     * @param u the URL on which to base the returned URL
     * @param newRef the new reference to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified reference
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewRef(final URL u, final String newRef) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getHost(), u.getPort(), u.getPath(), newRef, u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified query string.
     * @param u the URL on which to base the returned URL
     * @param newQuery the new query string to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified query string
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewQuery(final URL u, final String newQuery) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getHost(), u.getPort(), u.getPath(), u.getRef(), newQuery);
    }

    /**
     * Creates a new URL based on the specified fragments.
     * @param protocol the protocol to use (may not be <tt>null</tt>)
     * @param host the host to use (may not be <tt>null</tt>)
     * @param port the port to use (may be <tt>-1</tt> if no port is specified)
     * @param path the path to use (may be <tt>null</tt> and may omit the initial <tt>'/'</tt>)
     * @param ref the reference to use (may be <tt>null</tt> and must not include the <tt>'#'</tt>)
     * @param query the query to use (may be <tt>null</tt> and must not include the <tt>'?'</tt>)
     * @return a new URL based on the specified fragments
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    private static URL createNewUrl(final String protocol, final String host, final int port,
            final String path, final String ref, final String query) throws MalformedURLException {
        final StringBuilder s = new StringBuilder();
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
