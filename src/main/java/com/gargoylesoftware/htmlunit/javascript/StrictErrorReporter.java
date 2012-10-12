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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;

import net.sourceforge.htmlunit.corejs.javascript.ErrorReporter;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A JavaScript error reporter that will log all warnings and errors, no matter how trivial.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class StrictErrorReporter implements ErrorReporter, Serializable {

    private static final Log LOG = LogFactory.getLog(StrictErrorReporter.class);

    /**
     * Logs a warning.
     *
     * @param message the message to be displayed
     * @param sourceName the name of the source file
     * @param line the line number
     * @param lineSource the source code that failed
     * @param lineOffset the line offset
     */
    public void warning(
            final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {
        LOG.warn(format("warning", message, sourceName, line, lineSource, lineOffset));
    }

    /**
     * Logs an error.
     *
     * @param message the message to be displayed
     * @param sourceName the name of the source file
     * @param line the line number
     * @param lineSource the source code that failed
     * @param lineOffset the line offset
     */
    public void error(final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {
        LOG.error(format("error", message, sourceName, line, lineSource, lineOffset));
        throw new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    /**
     * Logs a runtime error.
     *
     * @param message the message to be displayed
     * @param sourceName the name of the source file
     * @param line the line number
     * @param lineSource the source code that failed
     * @param lineOffset the line offset
     * @return an evaluator exception
     */
    public EvaluatorException runtimeError(
            final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {
        LOG.error(format("runtimeError", message, sourceName, line, lineSource, lineOffset));
        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    private String format(
            final String prefix, final String message, final String sourceName,
            final int line, final String lineSource, final int lineOffset) {
        final StringBuilder result = new StringBuilder();
        result.append(prefix);
        result.append(": message=[").append(message);
        result.append("] sourceName=[").append(sourceName);
        result.append("] line=[").append(line);
        result.append("] lineSource=[").append(lineSource);
        result.append("] lineOffset=[").append(lineOffset);
        result.append("]");

        return result.toString();
    }
}
