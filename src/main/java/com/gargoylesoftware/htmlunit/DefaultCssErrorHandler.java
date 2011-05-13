/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

/**
 * HtmlUnit's default implementation of {@link ErrorHandler}, which logs all CSS problems.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @see SilentCssErrorHandler
 */
public class DefaultCssErrorHandler implements ErrorHandler, Serializable {
    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(DefaultCssErrorHandler.class);

    /**
     * {@inheritDoc}
     */
    public void error(final CSSParseException exception) {
        LOG.warn("CSS error: " + buildMessage(exception));
    }

    /**
     * {@inheritDoc}
     */
    public void fatalError(final CSSParseException exception) {
        LOG.warn("CSS fatal error: " + buildMessage(exception));
    }

    /**
     * {@inheritDoc}
     */
    public void warning(final CSSParseException exception) {
        LOG.warn("CSS warning: " + buildMessage(exception));
    }

    /**
     * Builds a message for the specified CSS parsing exception.
     * @param exception the CSS parsing exception to build a message for
     * @return a message for the specified CSS parsing exception
     */
    private String buildMessage(final CSSParseException exception) {
        final String uri = exception.getURI();
        final int line = exception.getLineNumber();
        final int col = exception.getColumnNumber();

        if (null == uri) {
            return "[" + line + ":" + col + "] " + exception.getMessage();
        }
        return "'" + uri + "' [" + line + ":" + col + "] " + exception.getMessage();

    }

}
