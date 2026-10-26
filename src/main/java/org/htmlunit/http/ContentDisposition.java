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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The result of parsing a {@code Content-Disposition} header (RFC 6266, RFC 2183).
 * <p>
 * Parameter names are case-insensitive (stored lower case); if a parameter is
 * defined more than once, the first one wins. The values are not sanitized.
 * </p>
 *
 * @see ContentDispositionParser
 *
 * @author Ronald Brill
 */
public final class ContentDisposition {

    private final String type_;
    private final Map<String, String> parameters_;
    private final Map<String, String> extendedParameters_;

    ContentDisposition(final String type, final Map<String, String> parameters,
            final Map<String, String> extendedParameters) {
        type_ = type;
        parameters_ = Collections.unmodifiableMap(new LinkedHashMap<>(parameters));
        extendedParameters_ = Collections.unmodifiableMap(new LinkedHashMap<>(extendedParameters));
    }

    /**
     * Returns the disposition type.
     *
     * @return the disposition type in lower case (e.g. {@code attachment} or {@code inline});
     *         an empty string if the header has none
     */
    public String getType() {
        return type_;
    }

    /**
     * Checks the disposition type.
     *
     * @return {@code true} if the type is {@code attachment}
     */
    public boolean isAttachment() {
        return "attachment".equals(type_);
    }

    /**
     * Checks the disposition type.
     *
     * @return {@code true} if the type is {@code inline}
     */
    public boolean isInline() {
        return "inline".equals(type_);
    }

    /**
     * Returns the value of a plain parameter.
     *
     * @param name the name of the parameter (case-insensitive), without a trailing {@code *}
     * @return the value of the plain parameter (quotes and escapes resolved) or {@code null};
     *         an empty string for a parameter without a value or with an empty value
     */
    public String getParameter(final String name) {
        return parameters_.get(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns the decoded value of an extended parameter (RFC 8187).
     *
     * @param name the name of the parameter (case-insensitive), without the trailing {@code *}
     * @return the decoded value of the extended parameter ({@code name*}, RFC 8187) or {@code null}
     *         if there is no such parameter or the value can not be decoded
     */
    public String getExtendedParameter(final String name) {
        return extendedParameters_.get(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns all plain parameters.
     *
     * @return the plain parameters (lower case names, in header order); not modifiable
     */
    public Map<String, String> getParameters() {
        return parameters_;
    }

    /**
     * Returns the file name suggested by the server.
     *
     * @return the file name; the extended parameter {@code filename*} wins over {@code filename};
     *         {@code null} if there is none. The name is returned as sent by the server, so it is not
     *         safe to use it as file name without checking.
     */
    public String getFilename() {
        final String extended = getExtendedParameter("filename");
        return extended != null ? extended : getParameter("filename");
    }
}
