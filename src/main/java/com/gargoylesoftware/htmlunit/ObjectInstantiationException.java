/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Thrown if an object could not be instantiated for some reason.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ObjectInstantiationException extends RuntimeException {
    private static final long serialVersionUID = 8831953284047722098L;
    private final Throwable causeException_;

    /**
     * Creates a new instance.
     * @param message a message explaining the failure
     * @param cause the exception that was thrown
     */
    public ObjectInstantiationException(final String message, final Throwable cause) {
        super(message);
        causeException_ = cause;
    }

    /**
     * Returns the exception that had been thrown during instantiation of the object.
     * @return the cause exception
     */
    public Throwable getCauseException() {
        return causeException_;
    }

    /**
     * Print the stack trace. If this exception contains another exception then
     * the stack traces for both will be printed.
     *
     * @param writer  Where the stack trace will be written
     */
    @Override
    public void printStackTrace(final PrintWriter writer) {
        super.printStackTrace(writer);
        if (causeException_ != null) {
            writer.write("Enclosed exception: ");
            causeException_.printStackTrace(writer);
        }
    }

    /**
     * Print the stack trace. If this exception contains another exception then
     * the stack traces for both will be printed.
     *
     * @param stream Where the stack trace will be written
     */
    @Override
    public void printStackTrace(final PrintStream stream) {
        super.printStackTrace(stream);
        if (causeException_ != null) {
            stream.print("Enclosed exception: ");
            causeException_.printStackTrace(stream);
        }
    }
}
