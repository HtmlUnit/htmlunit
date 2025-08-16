/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit;

/**
 * Represents the various ways a page can be submitted.
 *
 * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
 * @see <a href="http://tools.ietf.org/html/rfc5789">RFC5789</a>
 *
 * @author Mike Bowler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public enum HttpMethod {
    /** OPTIONS. */
    OPTIONS,
    /** GET. */
    GET,
    /** HEAD. */
    HEAD,
    /** POST. */
    POST,
    /** PUT. */
    PUT,
    /** DELETE. */
    DELETE,
    /** TRACE. */
    TRACE,
    /** PATCH. */
    PATCH
}
