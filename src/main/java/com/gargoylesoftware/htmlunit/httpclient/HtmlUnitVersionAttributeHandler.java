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

import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
import org.apache.hc.client5.http.cookie.MalformedCookieException;
import org.apache.hc.client5.http.cookie.SetCookie;
import org.apache.hc.client5.http.impl.cookie.AbstractCookieAttributeHandler;

/**
 * VersionAttributeHandler for HtmlUnit.
 *
 * @author Ronald Brill
 */
final class HtmlUnitVersionAttributeHandler extends AbstractCookieAttributeHandler
                implements CommonCookieAttributeHandler {

    /**
     * Parse cookie version attribute.
     */
    @Override
    public void parse(final SetCookie cookie, final String value) throws MalformedCookieException {
        if (value == null) {
            throw new MalformedCookieException("Missing value for version attribute");
        }
        int version = 0;
        try {
            version = Integer.parseInt(value);
        }
        catch (final NumberFormatException e) {
            // ignore invalid versions
        }
        // TODO
        //cookie.setVersion(version);
    }

    @Override
    public String getAttributeName() {
        // TODO
        //return SetCookie.VERSION_ATTR;
        return null;
    }
}
