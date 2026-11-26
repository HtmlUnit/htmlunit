/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html.parser.neko;

import java.net.URL;

import org.htmlunit.WebAssert;
import org.htmlunit.cyberneko.xerces.util.DefaultErrorHandler;
import org.htmlunit.cyberneko.xerces.xni.XNIException;
import org.htmlunit.cyberneko.xerces.xni.parser.XMLErrorHandler;
import org.htmlunit.cyberneko.xerces.xni.parser.XMLParseException;
import org.htmlunit.html.parser.HTMLParserListener;

/**
 * Utility to transmit parsing errors to a {@link HTMLParserListener}.
 *
 * @author Ronald Brill
 */
class HtmlUnitNekoHTMLErrorHandler implements XMLErrorHandler {
    private final HTMLParserListener listener_;
    private final URL url_;
    private final String html_;

    HtmlUnitNekoHTMLErrorHandler(final HTMLParserListener listener, final URL url, final String htmlContent) {
        WebAssert.notNull("listener", listener);
        WebAssert.notNull("url", url);
        listener_ = listener;
        url_ = url;
        html_ = htmlContent;
    }

    /**
     * {@inheritDoc}
     * @see DefaultErrorHandler#error(String,String,XMLParseException)
     */
    @Override
    public void error(final String domain, final String key,
            final XMLParseException exception) throws XNIException {
        listener_.error(exception.getMessage(),
                url_,
                html_,
                exception.getLineNumber(),
                exception.getColumnNumber(),
                key);
    }

    /**
     * {@inheritDoc}
     * @see DefaultErrorHandler#warning(String,String,XMLParseException)
     */
    @Override
    @SuppressWarnings("PMD.GuardLogStatement") // listener_ is not a logger
    public void warning(final String domain, final String key,
            final XMLParseException exception) throws XNIException {
        listener_.warning(exception.getMessage(),
                url_,
                html_,
                exception.getLineNumber(),
                exception.getColumnNumber(),
                key);
    }

    @Override
    public void fatalError(final String domain, final String key,
            final XMLParseException exception) throws XNIException {
        listener_.error(exception.getMessage(),
                url_,
                html_,
                exception.getLineNumber(),
                exception.getColumnNumber(),
                key);
    }
}
