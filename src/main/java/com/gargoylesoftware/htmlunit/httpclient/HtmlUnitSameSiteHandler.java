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

import org.apache.commons.lang3.StringUtils;

import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieOrigin;
import org.apache.hc.client5.http.cookie.MalformedCookieException;
import org.apache.hc.client5.http.cookie.SetCookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
/**
 * Customized CookieAttributeHandler for handling of the samesite attribute.
 *
 * @author Ronald Brill
 * @author Joerg Werner
 */
final class HtmlUnitSameSiteHandler implements CommonCookieAttributeHandler {

    private static final String SAMESITE_ATTR = "samesite";

    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        // nothing
    }

    @Override
    public void parse(final SetCookie cookie, final String value) throws MalformedCookieException {
        ((BasicClientCookie) cookie).setAttribute(SAMESITE_ATTR, StringUtils.toRootLowerCase(value));
    }

    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        return true;
    }

    @Override
    public String getAttributeName() {
        return SAMESITE_ATTR;
    }
}
