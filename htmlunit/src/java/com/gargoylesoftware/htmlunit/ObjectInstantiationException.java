/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.io.PrintWriter;
import java.io.PrintStream;

/**
 * Thrown if an object could not be instantiated for some reason.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ObjectInstantiationException extends RuntimeException {
    private final Throwable causeException_;


    /**
     * Create an instance.
     * @param message A message explaining the failure
     * @param cause The exception that was thrown
     */
    public ObjectInstantiationException( final String message, final Throwable cause ) {
        super(message);
        causeException_ = cause;
    }


    /**
     * Return the exception that had been thrown during instantiation of the object.
     * @return The cause exception
     */
    public Throwable getCauseException() {
        return causeException_;
    }


    /**
     *  Print the stack trace. If this exception contains another exception then
     *  the stack traces for both will be printed.
     *
     * @param  writer  Where the stack trace will be written
     */
    public void printStackTrace( final PrintWriter writer ) {
        super.printStackTrace( writer );
        if( causeException_ != null ) {
            writer.write( "Enclosed exception: " );
            causeException_.printStackTrace( writer );
        }
    }


    /**
     *  Print the stack trace. If this exception contains another exception then
     *  the stack traces for both will be printed.
     *
     * @param  stream  Where the stack trace will be written
     */
    public void printStackTrace( final PrintStream stream ) {
        super.printStackTrace( stream );
        if( causeException_ != null ) {
            stream.print( "Enclosed exception: " );
            causeException_.printStackTrace( stream );
        }
    }
}
