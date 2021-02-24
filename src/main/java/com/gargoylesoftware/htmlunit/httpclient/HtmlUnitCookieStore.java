/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.httpclient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;

import com.gargoylesoftware.htmlunit.CookieManager;

/**
 * Implementation of {@link CookieStore} like {@link org.apache.http.impl.client.BasicCookieStore}
 * BUT using our own {@link CookieManager} as back end.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public final class HtmlUnitCookieStore implements CookieStore, Serializable {
    private CookieManager manager_;

    /**
     * Constructor.
     *
     * @param manager the CookieManager
     */
    public HtmlUnitCookieStore(final CookieManager manager) {
        manager_ = manager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addCookie(final Cookie cookie) {
        manager_.addCookie(new com.gargoylesoftware.htmlunit.util.Cookie((ClientCookie) cookie));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<Cookie> getCookies() {
        return com.gargoylesoftware.htmlunit.util.Cookie.toHttpClient(manager_.getCookies());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean clearExpired(final Date date) {
        return manager_.clearExpired(date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void clear() {
        manager_.clearCookies();
    }
}
