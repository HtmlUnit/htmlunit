/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    HTMLParserListener LOG_REPORTER = new HTMLParserListener() {
        protected final Log getLog() {
            return LogFactory.getLog(HTMLParserListener.class);
        }
        public void error(final String message, final URL url, final int line, final int column, final String key) {
            getLog().error(format(message, url, line, column, key));
        }
        public void warning(final String message, final URL url, final int line, final int column, final String key) {
            getLog().warn(format(message, url, line, column, key));
        }
        private String format(final String message, final URL url, final int line, final int column, final String key) {
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
    };

    /**
     * Called when the HTML parser reports an error.
     * @param message the description of the problem
     * @param url the URL of the document in which the problem occurs
     * @param line the line of the problem
     * @param column the column of the problem
     * @param key the key identifying the "type" of problem. May be interesting to filter messages.
     */
    void error(final String message, final URL url, final int line, final int column, final String key);
    
    /**
     * Called when the HTML parser reports a warning.
     * @param message the description of the problem
     * @param url the URL of the document in which the problem occurs
     * @param line the line of the problem
     * @param column the column of the problem
     * @param key the key identifying the "type" of problem. May be interesting to filter messages.
     */
    void warning(final String message, final URL url, final int line, final int column, final String key);
}
