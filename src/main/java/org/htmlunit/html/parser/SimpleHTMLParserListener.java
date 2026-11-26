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
package org.htmlunit.html.parser;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple implementation of {@link HTMLParserListener} logging the received warnings
 * and errors in the "org.htmlunit.html.HTMLParserListener" log.<br/>
 * Errors are logged at the error level and warnings at the warning level.
 *
 * @author Daniel Gredler
 * @author Ronald Brill
 */
class SimpleHTMLParserListener implements HTMLParserListener {

    private static final Log LOG = LogFactory.getLog(SimpleHTMLParserListener.class);

    @Override
    public void error(final String message, final URL url, final String html,
            final int line, final int column, final String key) {
        LOG.error(format(message, url, html, line, column));
    }

    @Override
    public void warning(final String message, final URL url, final String html,
            final int line, final int column, final String key) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(format(message, url, html, line, column));
        }
    }

    private static String format(final String message, final URL url, final String html,
            final int line, final int column) {
        final StringBuilder builder = new StringBuilder(message);
        builder.append(" (")
                .append(url.toExternalForm())
                .append(' ')
                .append(line)
                .append(':')
                .append(column);
        if (null != html) {
            builder.append(" htmlSnippet: '")
                    .append(html)
                    .append('\'');
        }
        builder.append(')');
        return builder.toString();
    }
}
