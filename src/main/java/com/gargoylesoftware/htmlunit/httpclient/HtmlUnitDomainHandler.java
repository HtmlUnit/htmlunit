/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.httpclient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.impl.cookie.BasicDomainHandler;

/**
 * Customized BasicDomainHandler for HtmlUnit.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
final class HtmlUnitDomainHandler extends BasicDomainHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        final String domain = cookie.getDomain();
        if (domain == null) {
            return false;
        }
        if (domain.indexOf('.') == -1
                && !HtmlUnitBrowserCompatCookieSpec.LOCAL_FILESYSTEM_DOMAIN.equalsIgnoreCase(domain)) {
            try {
                InetAddress.getByName(domain);
            }
            catch (final UnknownHostException e) {
                return false;
            }
        }

        return super.match(cookie, origin);
    }
}
