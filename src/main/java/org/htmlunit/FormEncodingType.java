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

import java.io.Serializable;

import org.htmlunit.util.MimeType;

/**
 * A collection of constants that represent the various ways a form can be encoded when submitted.
 *
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class FormEncodingType implements Serializable {

    /** URL-encoded form encoding. */
    public static final FormEncodingType URL_ENCODED = new FormEncodingType("application/x-www-form-urlencoded");

    /** Multipart form encoding (used to be a constant in HttpClient, but it was deprecated with no alternative). */
    public static final FormEncodingType MULTIPART = new FormEncodingType("multipart/form-data");

    /** text/plain. */
    public static final FormEncodingType TEXT_PLAIN = new FormEncodingType(MimeType.TEXT_PLAIN);

    private final String name_;

    private FormEncodingType(final String name) {
        name_ = name;
    }

    /**
     * Returns the name of this encoding type.
     *
     * @return the name of this encoding type
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the constant that matches the specified name.
     *
     * @param name the name to search by
     * @return the constant corresponding to the specified name, {@link #URL_ENCODED} if none match.
     */
    public static FormEncodingType getInstance(final String name) {
        if (MULTIPART.getName().equalsIgnoreCase(name)) {
            return MULTIPART;
        }

        if (TEXT_PLAIN.getName().equalsIgnoreCase(name)) {
            return TEXT_PLAIN;
        }

        return URL_ENCODED;
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "EncodingType[name=" + getName() + "]";
    }
}
