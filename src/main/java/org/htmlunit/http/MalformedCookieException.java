/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.http;

/**
 * Exception thrown when a cookie string cannot be parsed or does not conform to the cookie specification.
 * <p>
 * This exception is thrown in the following scenarios:
 * <ul>
 *   <li>The cookie string is null</li>
 *   <li>The cookie name is empty or invalid</li>
 *   <li>The domain attribute is malformed or invalid</li>
 *   <li>The expires attribute cannot be parsed as a valid date</li>
 *   <li>The max-age attribute is not a valid integer</li>
 *   <li>Any other violation of the HTTP cookie specification (RFC 2109, RFC 2965, Netscape spec)</li>
 * </ul>
 *
 * @author Ronald Brill
 */
public class MalformedCookieException extends Exception {

    /**
     * Constructs a new MalformedCookieException with the specified detail message.
     *
     * @param message the detail message explaining why the cookie is malformed
     */
    public MalformedCookieException(final String message) {
        super(message);
    }

    /**
     * Constructs a new MalformedCookieException with the specified detail message and cause.
     *
     * @param message the detail message explaining why the cookie is malformed
     * @param cause the underlying cause of this exception (e.g., a parsing exception)
     */
    public MalformedCookieException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
