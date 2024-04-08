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

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * A cookie. This class is immutable.
 *
 * @author Daniel Gredler
 * @author Nicolas Belisle
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class Cookie implements Serializable {

    private final ClientCookie httpClientCookie_;

    /**
     * Creates a new cookie with the specified name and value which applies to the specified domain.
     * The new cookie applies to all paths, never expires and is not secure.
     * @param domain the domain to which this cookie applies
     * @param name the cookie name
     * @param value the cookie name
     */
    public Cookie(final String domain, final String name, final String value) {
        this(domain, name, value, null, null, false);
    }

    /**
     * Creates a new cookie with the specified name and value which applies to the specified domain,
     * the specified path, and expires on the specified date.
     * @param domain the domain to which this cookie applies
     * @param name the cookie name
     * @param value the cookie name
     * @param path the path to which this cookie applies
     * @param expires the date on which this cookie expires
     * @param secure whether or not this cookie is secure (i.e. HTTPS vs HTTP)
     */
    public Cookie(final String domain, final String name, final String value, final String path, final Date expires,
        final boolean secure) {
        this(domain, name, value, path, expires, secure, false, null);
    }

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
     */
    public Cookie(final String domain, final String name, final String value, final String path, final Date expires,
        final boolean secure, final boolean httpOnly) {
        this(domain, name, value, path, expires, secure, httpOnly, null);
    }

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

        final BasicClientCookie cookie = new BasicClientCookie(name, value == null ? "" : value);

        cookie.setDomain(domain);
        // BasicDomainHandler.match(Cookie, CookieOrigin) checks the attib also (see #333)
        cookie.setAttribute(ClientCookie.DOMAIN_ATTR, domain);

        cookie.setPath(path);
        if (expires != null) {
            cookie.setExpiryDate(expires);
        }
        cookie.setSecure(secure);
        if (httpOnly) {
            cookie.setAttribute("httponly", "true");
        }

        if (sameSite != null) {
            cookie.setAttribute("samesite", sameSite);
        }

        httpClientCookie_ = cookie;
    }

    /**
     * Creates a new HtmlUnit cookie from the HttpClient cookie provided.
     * @param clientCookie the HttpClient cookie
     */
    public Cookie(final ClientCookie clientCookie) {
        httpClientCookie_ = clientCookie;
    }

    /**
     * Creates a new cookie with the specified name and value which applies to the specified domain,
     * the specified path, and expires after the specified amount of time.
     * @param domain the domain to which this cookie applies
     * @param name the cookie name
     * @param value the cookie name
     * @param path the path to which this cookie applies
     * @param maxAge the number of seconds for which this cookie is valid; <code>-1</code> indicates that the
     *        cookie should never expire; other negative numbers are not allowed
     * @param secure whether or not this cookie is secure (i.e. HTTPS vs HTTP)
     */
    public Cookie(final String domain, final String name, final String value, final String path, final int maxAge,
        final boolean secure) {
        this(domain, name, value, path, convertToExpiryDate(maxAge), secure);
    }

    private static Date convertToExpiryDate(final int maxAge) {
        if (maxAge < -1) {
            throw new IllegalArgumentException("invalid max age:  " + maxAge);
        }

        if (maxAge >= 0) {
            return new Date(System.currentTimeMillis() + (maxAge * 1000L));
        }

        return null;
    }

    /**
     * Returns the cookie name.
     * @return the cookie name
     */
    public String getName() {
        return httpClientCookie_.getName();
    }

    /**
     * Returns the cookie value.
     * @return the cookie value
     */
    public String getValue() {
        return httpClientCookie_.getValue();
    }

    /**
     * Returns the domain to which this cookie applies ({@code null} for all domains).
     * @return the domain to which this cookie applies ({@code null} for all domains)
     */
    public String getDomain() {
        return httpClientCookie_.getDomain();
    }

    /**
     * Returns the path to which this cookie applies ({@code null} for all paths).
     * @return the path to which this cookie applies ({@code null} for all paths)
     */
    public String getPath() {
        return httpClientCookie_.getPath();
    }

    /**
     * Returns the date on which this cookie expires ({@code null} if it never expires).
     * @return the date on which this cookie expires ({@code null} if it never expires)
     */
    public Date getExpires() {
        return httpClientCookie_.getExpiryDate();
    }

    /**
     * Returns whether or not this cookie is secure (i.e. HTTPS vs HTTP).
     * @return whether or not this cookie is secure (i.e. HTTPS vs HTTP)
     */
    public boolean isSecure() {
        return httpClientCookie_.isSecure();
    }

    /**
     * Returns whether or not this cookie is HttpOnly (i.e. not available in JS).
     * @see <a href="http://en.wikipedia.org/wiki/HTTP_cookie#Secure_and_HttpOnly">Wikipedia</a>
     * @return whether or not this cookie is HttpOnly (i.e. not available in JS).
     */
    public boolean isHttpOnly() {
        return httpClientCookie_.getAttribute("httponly") != null;
    }

    /**
     * @return the SameSite value or {@code null} if not set.
     */
    public String getSameSite() {
        return httpClientCookie_.getAttribute("samesite");
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

    /**
     * Converts this cookie to an HttpClient cookie.
     * @return an HttpClient version of this cookie
     */
    public org.apache.http.cookie.Cookie toHttpClient() {
        return httpClientCookie_;
    }
}
