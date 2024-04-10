/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.http;

import static org.htmlunit.BrowserVersionFeatures.HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.htmlunit.BrowserVersion;

/**
 * A cookie. This class is immutable.
 *
 * @author Daniel Gredler
 * @author Nicolas Belisle
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class Cookie implements Serializable {

    private final String domain_;
    private final String name_;
    private final String value_;
    private final String path_;
    private final Date expiryDate_;
    private final boolean isSecure_;
    private final boolean isHttpOnly_;
    private final String sameSite_;

    /**
     * Creates a new cookie with the specified name and value which applies to the specified domain,
     * the specified path, and expires on the specified date.
     * @param domain the domain to which this cookie applies
     * @param name the cookie name
     * @param value the cookie name
     * @param path the path to which this cookie applies
     * @param expires the date on which this cookie expires
     * @param secure whether or not this cookie is secure (i.e. HTTPS vs HTTP)
     * @param httpOnly whether or not this cookie should be only used for HTTP(S) headers
     * @param sameSite the sameSite attribute
     */
    public Cookie(final String domain, final String name, final String value, final String path, final Date expires,
        final boolean secure, final boolean httpOnly, final String sameSite) {
        if (domain == null) {
            throw new IllegalArgumentException("Cookie domain must be specified");
        }

        domain_ = domain.toLowerCase(Locale.ROOT);
        name_ = name;
        if (value == null) {
            value_ = "";
        }
        else {
            value_ = value;
        }
        path_ = path;
        expiryDate_ = expires;

        isSecure_ = secure;
        isHttpOnly_ = httpOnly;

        sameSite_ = sameSite;
    }

    /**
     * Returns the cookie name.
     * @return the cookie name
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the cookie value.
     * @return the cookie value
     */
    public String getValue() {
        return value_;
    }

    /**
     * Returns the domain to which this cookie applies ({@code null} for all domains).
     * @return the domain to which this cookie applies ({@code null} for all domains)
     */
    public String getDomain() {
        return domain_;
    }

    /**
     * Returns the path to which this cookie applies ({@code null} for all paths).
     * @return the path to which this cookie applies ({@code null} for all paths)
     */
    public String getPath() {
        return path_;
    }

    /**
     * Returns the date on which this cookie expires ({@code null} if it never expires).
     * @return the date on which this cookie expires ({@code null} if it never expires)
     */
    public Date getExpires() {
        return expiryDate_;
    }

    /**
     * Returns whether or not this cookie is secure (i.e. HTTPS vs HTTP).
     * @return whether or not this cookie is secure (i.e. HTTPS vs HTTP)
     */
    public boolean isSecure() {
        return isSecure_;
    }

    /**
     * Returns whether or not this cookie is HttpOnly (i.e. not available in JS).
     * @see <a href="http://en.wikipedia.org/wiki/HTTP_cookie#Secure_and_HttpOnly">Wikipedia</a>
     * @return whether or not this cookie is HttpOnly (i.e. not available in JS).
     */
    public boolean isHttpOnly() {
        return isHttpOnly_;
    }

    /**
     * @return the SameSite value or {@code null} if not set.
     */
    public String getSameSite() {
        return sameSite_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + "=" + getValue()
            + (getDomain() == null ? "" : ";domain=" + getDomain())
            + (getPath() == null ? "" : ";path=" + getPath())
            + (getExpires() == null ? "" : ";expires=" + getExpires())
            + (isSecure() ? ";secure" : "")
            + (isHttpOnly() ? ";httpOnly" : "")
            + (getSameSite() == null ? "" : ";sameSite=" + getSameSite());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Cookie)) {
            return false;
        }
        final Cookie other = (Cookie) o;
        final String path = getPath() == null ? "/" : getPath();
        final String otherPath = other.getPath() == null ? "/" : other.getPath();
        return new EqualsBuilder()
                    .append(getName(), other.getName())
                    .append(getDomain(), other.getDomain())
                    .append(path, otherPath)
                    .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final String path = getPath() == null ? "/" : getPath();
        return new HashCodeBuilder()
                    .append(getName())
                    .append(getDomain())
                    .append(path)
                    .toHashCode();
    }

    public boolean matches(final URL url, final BrowserVersion browserVersion) {
        final String host = url.getHost();
        // URLs like "about:blank" don't have cookies and we need to catch these
        // cases here before HttpClient complains
        if (host.isEmpty()) {
            return false;
        }

        final String protocol = url.getProtocol();
        if (protocol == null || !protocol.startsWith("http")) {
            return false;
        }

        // domain check
        if (!matchesDomain(host, browserVersion)
                || !matchesPath(url.getPath())
                || !matchesIsSecure(protocol, host)) {
            return false;
        }

        return true;
    }

    private boolean matchesDomain(final String host, final BrowserVersion browserVersion) {
        String domain = getDomain();
        if (domain == null) {
            return false;
        }

        final int dotIndex = domain.indexOf('.');
        if (dotIndex == 0 && domain.length() > 1 && domain.indexOf('.', 1) == -1) {
            domain = domain.toLowerCase(Locale.ROOT);
            if (browserVersion.hasFeature(HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS)) {
                domain = domain.substring(1);
            }
            return host.equals(domain);
        }

        if (dotIndex == -1) {
            try {
                InetAddress.getByName(domain);
            }
            catch (final UnknownHostException e) {
                return false;
            }
        }

        if (domain.startsWith(".")) {
            domain = domain.substring(1);
        }
        domain = domain.toLowerCase(Locale.ROOT);
        if (host.equals(domain)) {
            return true;
        }

        if (HttpUtils.isIPv4Address(host) || HttpUtils.isIPv6Address(host)) {
            return false;
        }

        if (host.endsWith(domain)) {
            final int prefix = host.length() - domain.length();
            // Either a full match or a prefix ending with a '.'
            if (prefix == 0) {
                return true;
            }
            if (prefix > 1 && host.charAt(prefix - 1) == '.') {
                return true;
            }
        }
        return false;
    }

    private boolean matchesPath(final String urlPath) {
        String normalizedCookiePath = getPath();
        if (normalizedCookiePath == null) {
            normalizedCookiePath = "/";
        }
        if (normalizedCookiePath.length() > 1 && normalizedCookiePath.endsWith("/")) {
            normalizedCookiePath = normalizedCookiePath.substring(0, normalizedCookiePath.length() - 1);
        }
        if (urlPath.startsWith(normalizedCookiePath)) {
            if ("/".equals(normalizedCookiePath)) {
                return true;
            }
            if (urlPath.length() == normalizedCookiePath.length()) {
                return true;
            }
            if (urlPath.charAt(normalizedCookiePath.length()) == '/') {
                return true;
            }
        }
        return false;
    }

    private boolean matchesIsSecure(final String protocol, final String host) {
        return !isSecure()
                || "https".equals(protocol)
                || "localhost".equalsIgnoreCase(host);
    }

}
