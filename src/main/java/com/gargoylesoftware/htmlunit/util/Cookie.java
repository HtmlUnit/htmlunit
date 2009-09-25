/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A cookie. This class is immutable.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class Cookie implements Serializable {

    private static final long serialVersionUID = 1960945152963263900L;

    /** The cookie name. */
    private final String name_;

    /** The cookie value. */
    private final String value_;

    /** The domain to which this cookie applies (<tt>null</tt> for all domains). */
    private final String domain_;

    /** The path to which this cookie applies (<tt>null</tt> for all paths). */
    private final String path_;

    /** The date on which this cookie expires (<tt>null</tt> if it never expires). */
    private final Date expires_;

    /** Whether or not this cookie is secure (i.e. HTTPS vs HTTP). */
    private final boolean secure_;

    /**
     * Creates a new cookie with the specified name and value. The new cookie applies to all
     * domains and all paths, never expires and is not secure.
     * @param name the cookie name
     * @param value the cookie name
     */
    public Cookie(final String name, final String value) {
        this(name, value, null);
    }

    /**
     * Creates a new cookie with the specified name and value which applies to the specified domain.
     * The new cookie applies to all paths, never expires and is not secure.
     * @param name the cookie name
     * @param value the cookie name
     * @param domain the domain to which this cookie applies
     */
    public Cookie(final String name, final String value, final String domain) {
        this(name, value, domain, null, null, false);
    }

    /**
     * Creates a new cookie with the specified name and value which applies to the specified domain,
     * the specified path, and expires on the specified date.
     * @param name the cookie name
     * @param value the cookie name
     * @param domain the domain to which this cookie applies
     * @param path the path to which this cookie applies
     * @param expires the date on which this cookie expires
     * @param secure whether or not this cookie is secure (i.e. HTTPS vs HTTP)
     */
    public Cookie(final String name, final String value, final String domain, final String path, final Date expires,
        final boolean secure) {
        name_ = name;
        value_ = (value != null ? value : ""); // HttpClient 3.1 doesn't like null cookie values
        domain_ = domain;
        path_ = path;
        expires_ = expires;
        secure_ = secure;
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
     * Returns the domain to which this cookie applies (<tt>null</tt> for all domains).
     * @return the domain to which this cookie applies (<tt>null</tt> for all domains)
     */
    public String getDomain() {
        return domain_;
    }

    /**
     * Returns the path to which this cookie applies (<tt>null</tt> for all paths).
     * @return the path to which this cookie applies (<tt>null</tt> for all paths)
     */
    public String getPath() {
        return path_;
    }

    /**
     * Returns the date on which this cookie expires (<tt>null</tt> if it never expires).
     * @return the date on which this cookie expires (<tt>null</tt> if it never expires)
     */
    public Date getExpires() {
        return expires_;
    }

    /**
     * Returns whether or not this cookie is secure (i.e. HTTPS vs HTTP).
     * @return whether or not this cookie is secure (i.e. HTTPS vs HTTP)
     */
    public boolean isSecure() {
        return secure_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name_ + "=" + value_
            + (domain_ != null ? ";domain=" + domain_ : "")
            + (path_ != null ? ";path=" + path_ : "")
            + (expires_ != null ? ";expires=" + expires_ : "")
            + (secure_ ? ";secure" : "");
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
        return new EqualsBuilder().append(name_, other.name_).append(domain_, other.domain_).append(path_, other.path_)
            .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name_).append(domain_).append(path_).toHashCode();
    }

    /**
     * Converts this cookie to an HttpClient cookie.
     * @return an HttpClient version of this cookie
     */
    public org.apache.commons.httpclient.Cookie toHttpClient() {
        return new org.apache.commons.httpclient.Cookie(domain_, name_, value_, path_, expires_, secure_);
    }

    /**
     * Converts the specified collection of cookies into an array of HttpClient cookies.
     * @param cookies the cookies to be converted
     * @return the specified cookies, as HttpClient cookies
     */
    public static org.apache.commons.httpclient.Cookie[] toHttpClient(final Collection< Cookie > cookies) {
        final org.apache.commons.httpclient.Cookie[] array = new org.apache.commons.httpclient.Cookie[cookies.size()];
        final Iterator< Cookie > it = cookies.iterator();
        for (int i = 0; i < cookies.size(); i++) {
            array[i] = it.next().toHttpClient();
        }
        return array;
    }

    /**
     * Converts the specified array of HttpClient cookies into a list of cookies.
     * @param cookies the cookies to be converted
     * @return the specified HttpClient cookies, as cookies
     */
    public static List< Cookie > fromHttpClient(final org.apache.commons.httpclient.Cookie[] cookies) {
        final List< Cookie > list = new ArrayList< Cookie >(cookies.length);
        for (int i = 0; i < cookies.length; i++) {
            final org.apache.commons.httpclient.Cookie c = cookies[i];
            final Cookie cookie =
                new Cookie(c.getName(), c.getValue(), c.getDomain(), c.getPath(), c.getExpiryDate(), c.getSecure());
            list.add(cookie);
        }
        return list;
    }

}
