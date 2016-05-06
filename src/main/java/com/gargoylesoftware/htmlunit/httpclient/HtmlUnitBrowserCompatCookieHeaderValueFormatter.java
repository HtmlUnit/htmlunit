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

import org.apache.http.message.BasicHeaderValueFormatter;

/**
 * Customized BasicHeaderValueFormatter for HtmlUnit.
 *
 * We use our own class because browsers do not automatically quote version1 cookies
 * if the value contains special chars.
 * I guess this is something special for HttpClient because HttpClient also removes
 * the quotes from cookies (@see {@link HtmlUnitBrowserCompatCookieSpec})
 *
 * @author Ronald Brill
 */
public class HtmlUnitBrowserCompatCookieHeaderValueFormatter extends BasicHeaderValueFormatter {

    /**
     * Single instance as in BasicHeaderValueFormatter.
     */
    public static final HtmlUnitBrowserCompatCookieHeaderValueFormatter
                            INSTANCE = new HtmlUnitBrowserCompatCookieHeaderValueFormatter();

    /**
     * {@inheritDoc}
     * Overwritten to disable automatic addition of quotes.
     */
    @Override
    protected boolean isSeparator(final char ch) {
        return false;
    }

    /**
     * Looks like browsers are not doing any escaping.
     * {@inheritDoc}
     */
    @Override
    protected boolean isUnsafe(final char ch) {
        return false;
    }
}
