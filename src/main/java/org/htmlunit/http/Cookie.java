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
import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    private final String samesite_;

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

        samesite_ = sameSite;
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
        return samesite_;
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
}
