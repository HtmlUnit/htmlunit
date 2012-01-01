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
package com.gargoylesoftware.htmlunit;

/**
 * Thrown if an object could not be instantiated for some reason.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
public class ObjectInstantiationException extends RuntimeException {
    /**
     * Creates a new instance.
     * @param message a message explaining the failure
     * @param cause the exception that was thrown
     */
    public ObjectInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns the exception that had been thrown during instantiation of the object.
     * @return the cause exception
     * @deprecated as of 2.9, please use {@link #getCause()} instead
     */
    @Deprecated
    public Throwable getCauseException() {
        return getCause();
    }
}
