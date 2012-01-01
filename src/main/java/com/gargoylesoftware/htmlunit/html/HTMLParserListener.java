/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Listener for messages from the HTML parser. <br/>
 * The classification of problems as warnings or errors is the one of the HTML parser
 * used by HtmlUnit. The line and column may indicates the position of the problem detected
 * by the parser. This is only an indication and in some cases the position where
 * the problem has to be solved is located lines before.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public interface HTMLParserListener {

    /**
     * Simple implementation of {@link HTMLParserListener} logging the received warnings
     * and errors in the "com.gargoylesoftware.htmlunit.html.HTMLParserListener" log.<br/>
     * Errors are logged at the error level and warnings at the warning level.
     */
    HTMLParserListener LOG_REPORTER = new SimpleHTMLParserListener();

    /**
     * Called when the HTML parser reports an error.
     * @param message the description of the problem
     * @param url the URL of the document in which the problem occurs
     * @param line the line of the problem
     * @param column the column of the problem
     * @param key the key identifying the "type" of problem
     */
    void error(final String message, final URL url, final int line, final int column, final String key);

    /**
     * Called when the HTML parser reports a warning.
     * @param message the description of the problem
     * @param url the URL of the document in which the problem occurs
     * @param line the line of the problem
     * @param column the column of the problem
     * @param key the key identifying the "type" of problem
     */
    void warning(final String message, final URL url, final int line, final int column, final String key);
}

/**
 * Simple implementation of {@link HTMLParserListener} logging the received warnings
 * and errors in the "com.gargoylesoftware.htmlunit.html.HTMLParserListener" log.<br/>
 * Errors are logged at the error level and warnings at the warning level.
 */
class SimpleHTMLParserListener implements HTMLParserListener {

    private static final Log LOG = LogFactory.getLog(HTMLParserListener.class);

    public void error(final String message, final URL url, final int line, final int column, final String key) {
        LOG.error(format(message, url, line, column));
    }

    public void warning(final String message, final URL url, final int line, final int column, final String key) {
        LOG.warn(format(message, url, line, column));
    }

    private String format(final String message, final URL url, final int line, final int column) {
        final StringBuilder buffer = new StringBuilder(message);
        buffer.append(" (");
        buffer.append(url.toExternalForm());
        buffer.append(" ");
        buffer.append(line);
        buffer.append(":");
        buffer.append(column);
        buffer.append(")");
        return buffer.toString();
    }

}
