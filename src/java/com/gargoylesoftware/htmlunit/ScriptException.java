/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.WrappedException;

/**
 * An exception that will be thrown if an error occurs during the processing of
 * a script.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ScriptException extends RuntimeException {
    private final Throwable throwable_;
    private final String scriptSourceCode_;


    /**
     * Create an instance
     *
     * @param throwable The exception that was thrown from the script engine.
     * @param scriptSourceCode The code that was being executed when this exception
     * was thrown.  This may be null if the exception was not caused by execution
     * of javascript.
     */
    public ScriptException( final Throwable throwable, final String scriptSourceCode ) {
        super(getMessageFrom(throwable));
        throwable_ = throwable;
        scriptSourceCode_ = scriptSourceCode;
    }

    private static String getMessageFrom( final Throwable throwable ) {
        if( throwable == null ) {
            return "null";
        }
        else {
            return throwable.getMessage();
        }
    }

    /**
     * Create an instance
     *
     * @param throwable The exception that was thrown from the script engine.
     */
    public ScriptException( final Throwable throwable ) {
        this( throwable, null );
    }


    /**
     * Return the enclosed exception.
     * @return The enclosed exception
     */
    public Throwable getEnclosedException() {
        return throwable_;
    }

    /**
     *  Print the stack trace. If this exception contains another exception then
     *  the stack traces for both will be printed.
     *
     * @param  writer  Where the stack trace will be written
     */
    public void printStackTrace( final PrintWriter writer ) {
        writer.write( createPrintableStackTrace() );
    }


    /**
     *  Print the stack trace. If this exception contains another exception then
     *  the stack traces for both will be printed.
     *
     * @param  stream  Where the stack trace will be written
     */
    public void printStackTrace( final PrintStream stream ) {
        stream.print(createPrintableStackTrace());
    }


    private String createPrintableStackTrace() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        printWriter.println("======= EXCEPTION START ========");

        if( throwable_ != null ) {
            if( throwable_ instanceof EcmaError ) {
                final EcmaError ecmaError = (EcmaError)throwable_;
                printWriter.print("EcmaError: ");
                printWriter.print("lineNumber=[");
                printWriter.print(ecmaError.getLineNumber());
                printWriter.print("] column=[");
                printWriter.print(ecmaError.getColumnNumber());
                printWriter.print("] lineSource=[");
                printWriter.print(getFailingLine());
                printWriter.print("] name=[");
                printWriter.print(ecmaError.getName());
                printWriter.print("] sourceName=[");
                printWriter.print(ecmaError.getSourceName());
                printWriter.print("] message=[");
                printWriter.print(ecmaError.getMessage());
                printWriter.print("] errorObject=[");
                printWriter.print(ecmaError.getErrorObject());
                printWriter.print("]");
                printWriter.println();
            }
            else {
                printWriter.println("Exception class=["+throwable_.getClass().getName()+"]");
            }
        }

        super.printStackTrace(printWriter);
        if( throwable_ != null && throwable_ instanceof JavaScriptException ) {
            final Object value = ((JavaScriptException)throwable_).getValue();

            printWriter.print("JavaScriptException value = ");
            if( value instanceof Throwable ) {
                ((Throwable)value).printStackTrace(printWriter);
            }
            else {
               printWriter.println(value);
            }
        }
        else if( throwable_ != null && throwable_ instanceof WrappedException ) {
            final WrappedException wrappedException = (WrappedException)throwable_;
            printWriter.print("WrappedException: ");
            wrappedException.printStackTrace(printWriter);

            final Throwable innerException = wrappedException.getWrappedException();
            if( innerException == null ) {
                printWriter.println("Inside wrapped exception: null");
            }
            else {
                printWriter.println("Inside wrapped exception:");
                innerException.printStackTrace(printWriter);
            }
        }
        else if( throwable_ != null ) {
            printWriter.println( "Enclosed exception: " );
            throwable_.printStackTrace( printWriter );
        }

        if( scriptSourceCode_ != null && scriptSourceCode_.length() > 0 ) {
            printWriter.println("== CALLING JAVASCRIPT ==");
            printWriter.println(scriptSourceCode_);
        }
        printWriter.println("======= EXCEPTION END ========");

        return stringWriter.toString();
    }


    /**
     * Return the source code line that failed
     * @return the source code line that failed
     */
    public String getScriptSourceCode() {
        return scriptSourceCode_;
    }


    /**
     * Return the line of source that was being executed when this exception was
     * thrown.
     *
     * @return The line of source or an empty string if the exception was not thrown
     * due to the execution of a script.
     */
    public String getFailingLine() {
        final int lineNumber = getFailingLineNumber();
        if( lineNumber == -1 ) {
            return "<no source>";
        }

        try {
            final BufferedReader reader = new BufferedReader( new StringReader(scriptSourceCode_) );
            for( int i=0; i<lineNumber-1; i++ ) {
                reader.readLine();
            }
            final String result = reader.readLine();
            reader.close();
            return result;
        }
        catch( final IOException e ) {
            // Theoretically impossible
            e.printStackTrace();
        }
        return "";
    }


    /**
     * Return the line number of the source that was executing at the time of the
     * exception.
     *
     * @return The line number or -1 if the exception was not thrown due to the
     * execution of a script.
     */
    public int getFailingLineNumber() {
        if( scriptSourceCode_ == null ) {
            return -1;
        }

        if( throwable_ instanceof EcmaError ) {
            final EcmaError ecmaError = (EcmaError)throwable_;
            return ecmaError.getLineNumber();
        }

        return -1;
    }
}
