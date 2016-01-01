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

/**
 * Customized CookieAttributeHandler for handling of the httponly attribute.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Nicolas Belisle
 * @author Ronald Brill
 * @author John J Murdoch
 */
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicClientCookie;

final class HtmlUnitHttpOnlyHandler implements CommonCookieAttributeHandler {

    private static final String HTTPONLY_ATTR = "httponly";

    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        // nothing
    }

    @Override
    public void parse(final SetCookie cookie, final String value) throws MalformedCookieException {
        ((BasicClientCookie) cookie).setAttribute(HTTPONLY_ATTR, "true");
    }

    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        return true;
    }

    @Override
    public String getAttributeName() {
        return HTTPONLY_ATTR;
    }
}
