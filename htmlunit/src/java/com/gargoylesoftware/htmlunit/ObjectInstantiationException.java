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

import java.io.PrintWriter;
import java.io.PrintStream;

/**
 * Thrown if an object could not be instantiated for some reason.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ObjectInstantiationException extends RuntimeException {
    private static final long serialVersionUID = 8831953284047722098L;
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
