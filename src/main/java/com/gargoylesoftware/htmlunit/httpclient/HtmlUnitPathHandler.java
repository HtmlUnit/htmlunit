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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTTP_COOKIE_EXTRACT_PATH_FROM_LOCATION;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicPathHandler;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Customized BasicPathHandler for HtmlUnit.
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
final class HtmlUnitPathHandler extends BasicPathHandler {
    private final BrowserVersion browserVersion_;

    HtmlUnitPathHandler(final BrowserVersion browserVersion) {
        browserVersion_ = browserVersion;
    }

    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        // nothing, browsers seem not to perform any validation
    }

    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        CookieOrigin newOrigin = origin;
        String targetpath = origin.getPath();
        if (browserVersion_.hasFeature(HTTP_COOKIE_EXTRACT_PATH_FROM_LOCATION) && !targetpath.isEmpty()) {
            final int lastSlashPos = targetpath.lastIndexOf('/');
            if (lastSlashPos > 1 && lastSlashPos < targetpath.length()) {
                targetpath = targetpath.substring(0, lastSlashPos);
                newOrigin = new CookieOrigin(origin.getHost(), origin.getPort(), targetpath, origin.isSecure());
            }
        }

        return super.match(cookie, newOrigin);
    }
}
