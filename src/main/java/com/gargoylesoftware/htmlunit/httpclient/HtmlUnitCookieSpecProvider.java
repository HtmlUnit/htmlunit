/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import org.apache.hc.client5.http.cookie.CookieSpec;
import org.apache.hc.client5.http.cookie.CookieSpecFactory;
import org.apache.hc.core5.http.protocol.HttpContext;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Customized CookieSpecProvider for HtmlUnit.
 *
 * @author Ronald Brill
 * @author Joerg Werner
 */
public final class HtmlUnitCookieSpecProvider implements CookieSpecFactory {

    private final BrowserVersion browserVersion_;

    /**
     * Constructor.
     * @param browserVersion the browserVersion
     */
    public HtmlUnitCookieSpecProvider(final BrowserVersion browserVersion) {
        browserVersion_ = browserVersion;
    }

    @Override
    public CookieSpec create(final HttpContext context) {
        return new HtmlUnitBrowserCompatCookieSpec(browserVersion_);
    }
}
