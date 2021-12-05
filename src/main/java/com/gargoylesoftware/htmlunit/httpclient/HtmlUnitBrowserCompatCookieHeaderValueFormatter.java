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

import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicHeaderValueFormatter;
import org.apache.hc.core5.http.message.HeaderValueFormatter;
import org.apache.hc.core5.util.Args;
import org.apache.hc.core5.util.CharArrayBuffer;

/**
 * Customized BasicHeaderValueFormatter for HtmlUnit.
 *
 * We use our own class because browsers do not automatically quote version1 cookies
 * if the value contains special chars.
 * I guess this is something special for HttpClient because HttpClient also removes
 * the quotes from cookies (@see {@link HtmlUnitBrowserCompatCookieSpec})
 * <p>
 * The code is basically copied from {@link BasicHeaderValueFormatter} which does no
 * longer allow overwriting the relevant methods.
 *
 * @author Ronald Brill
 * @author Joerg Werner
 */
public class HtmlUnitBrowserCompatCookieHeaderValueFormatter implements HeaderValueFormatter {

    /**
     * Single instance as in BasicHeaderValueFormatter.
     */
    public static final HtmlUnitBrowserCompatCookieHeaderValueFormatter
                            INSTANCE = new HtmlUnitBrowserCompatCookieHeaderValueFormatter();

    @Override
    public void formatElements(
            final CharArrayBuffer buffer, final HeaderElement[] elems, final boolean quote) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(elems, "Header element array");

        for (int i = 0; i < elems.length; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            formatHeaderElement(buffer, elems[i], quote);
        }
    }

    @Override
    public void formatHeaderElement(
            final CharArrayBuffer buffer, final HeaderElement elem, final boolean quote) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(elem, "Header element");

        buffer.append(elem.getName());
        final String value = elem.getValue();
        if (value != null) {
            buffer.append('=');
            formatValue(buffer, value, quote);
        }

        final int c = elem.getParameterCount();
        if (c > 0) {
            for (int i = 0; i < c; i++) {
                buffer.append("; ");
                formatNameValuePair(buffer, elem.getParameter(i), quote);
            }
        }
    }

    @Override
    public void formatParameters(
            final CharArrayBuffer buffer, final NameValuePair[] nvps, final boolean quote) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(nvps, "Header parameter array");

        for (int i = 0; i < nvps.length; i++) {
            if (i > 0) {
                buffer.append("; ");
            }
            formatNameValuePair(buffer, nvps[i], quote);
        }
    }

    @Override
    public void formatNameValuePair(
            final CharArrayBuffer buffer, final NameValuePair nvp, final boolean quote) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(nvp, "Name / value pair");

        buffer.append(nvp.getName());
        final String value = nvp.getValue();
        if (value != null) {
            buffer.append('=');
            formatValue(buffer, value, quote);
        }
    }

    void formatValue(final CharArrayBuffer buffer, final String value, final boolean quote) {
        if (quote) {
            buffer.append('"');
        }
        for (int i = 0; i < value.length(); i++) {
            final char ch = value.charAt(i);
            buffer.append(ch);
        }
        if (quote) {
            buffer.append('"');
        }
    }
}
