/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package netscape.javascript;

/**
 * Stub for the JSException. This is part of the Applet
 * LiveConnect simulation.
 *
 * TODO: we have to evaluate if it is possible to use plugin.jar from jdk
 *
 * @version $Revision$
 * @author Ronald Brill
 */
public class JSException extends RuntimeException {
    /** EXCEPTION_TYPE_EMPTY = -1. */
    public static final int EXCEPTION_TYPE_EMPTY = -1;

    /** EXCEPTION_TYPE_VOID = 0. */
    public static final int EXCEPTION_TYPE_VOID = 0;

    /** EXCEPTION_TYPE_OBJECT = 1. */
    public static final int EXCEPTION_TYPE_OBJECT = 1;

    /** EXCEPTION_TYPE_FUNCTION = 2. */
    public static final int EXCEPTION_TYPE_FUNCTION = 2;

    /** EXCEPTION_TYPE_STRING = 3. */
    public static final int EXCEPTION_TYPE_STRING = 3;

    /** EXCEPTION_TYPE_NUMBER = 4. */
    public static final int EXCEPTION_TYPE_NUMBER = 4;

    /** EXCEPTION_TYPE_BOOLEAN = 5. */
    public static final int EXCEPTION_TYPE_BOOLEAN = 5;

    /** EXCEPTION_TYPE_ERROR = 6. */
    public static final int EXCEPTION_TYPE_ERROR = 6;

    /** wrappedExceptionType. */
    private int wrappedExceptionType_ = EXCEPTION_TYPE_EMPTY;

    /** wrappedException. */
    private Object wrappedException_;

    /**
     * Constructor.
     */
    public JSException() {
        this(null);
    }

    /**
     * Constructor.
     * @param message the message
     */
    public JSException(final String message) {
        this(message, null, -1, null, -1);
    }

    /**
     * Constructor.
     * @param message the message
     * @param filename the filename
     * @param lineno the lineno
     * @param source the source
     * @param tokenIndex the tokenIndex
     */
    public JSException(final String message,
            final String filename, final int lineno,
            final String source, final int tokenIndex) {
        super(message);
    }

    /**
     * Constructor.
     * @param exceptionType the exception type
     * @param exception the exception
     */
    public JSException(final int exceptionType, final Object exception) {
        this();
        wrappedExceptionType_ = exceptionType;
        wrappedException_ = exception;
    }

    /**
     * Returns the wrapped exception type.
     * @return wrapped exception type
     */
    public int getWrappedExceptionType() {
        return wrappedExceptionType_;
    }

    /**
     * Returns the wrapped exception.
     * @return wrapped exception
     */
    public Object getWrappedException() {
        return wrappedException_;
    }
}
