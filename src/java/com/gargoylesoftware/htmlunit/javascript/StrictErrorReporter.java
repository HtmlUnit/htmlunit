/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
import org.apache.commons.logging.Log;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 * A javascript error reporter that will log all warnings and errors, no matter how trivial.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class StrictErrorReporter implements ErrorReporter {
    private final Log log_;

    /**
     * Create an instance.
     *
     * @param log The log to use when reporting errors
     */
    public StrictErrorReporter( final Log log ) {
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
        print( "warning", message, sourceName, line, lineSource, lineOffset );
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

        print( "error", message, sourceName, line, lineSource, lineOffset );
        throw new EvaluatorException(message);
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

        print( "runtimeError", message, sourceName, line, lineSource, lineOffset );
        return new EvaluatorException(message);
    }


    private void print(
            final String prefix, final String message, final String sourceName,
            final int line, final String lineSource, final int lineOffset) {

        log_.debug(prefix+": message=["+message+"] sourceName=["+sourceName+"] line=["+line
            +"] lineSource=["+lineSource+"] lineOffset=["+lineOffset+"]");
    }
}
