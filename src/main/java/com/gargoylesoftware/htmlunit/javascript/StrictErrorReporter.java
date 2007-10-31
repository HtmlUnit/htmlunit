/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
import org.apache.commons.logging.Log;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 * A javascript error reporter that will log all warnings and errors, no matter how trivial.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class StrictErrorReporter implements ErrorReporter {
    private final Log log_;

    /**
     * Create an instance.
     *
     * @param log The log to use when reporting errors
     */
    public StrictErrorReporter(final Log log) {
        Assert.notNull("log", log);
        log_ = log;
    }

    /**
     * Log a warning
     *
     * @param message The message to be displayed.
     * @param sourceName The name of the source file
     * @param line The line number
     * @param lineSource The source code that failed
     * @param lineOffset The line offset
     */
    public void warning(
            final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {
        log_.warn(format("warning", message, sourceName, line, lineSource, lineOffset));
    }

    /**
     * Log an error
     *
     * @param message The message to be displayed.
     * @param sourceName The name of the source file
     * @param line The line number
     * @param lineSource The source code that failed
     * @param lineOffset The line offset
     */
    public void error(
            final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {

        log_.error(format("error", message, sourceName, line, lineSource, lineOffset));
        throw new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    /**
     * Log a runtime error
     *
     * @param message The message to be displayed.
     * @param sourceName The name of the source file
     * @param line The line number
     * @param lineSource The source code that failed
     * @param lineOffset The line offset
     * @return An evaluator exception
     */
    public EvaluatorException runtimeError(
            final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {

        log_.error(format("runtimeError", message, sourceName, line, lineSource, lineOffset));
        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    private String format(
            final String prefix, final String message, final String sourceName,
            final int line, final String lineSource, final int lineOffset) {

        return prefix + ": message=[" + message + "] sourceName=[" + sourceName + "] line=[" + line
            + "] lineSource=[" + lineSource + "] lineOffset=[" + lineOffset + "]";
    }
}
