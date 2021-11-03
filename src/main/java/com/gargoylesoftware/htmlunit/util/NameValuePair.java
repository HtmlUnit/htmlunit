/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.LangUtils;

/**
 * A name/value pair.
 *
 * @author Daniel Gredler
 * @author Nicolas Belisle
 * @author Ronald Brill
 */
public class NameValuePair implements Serializable {

    /** The name. */
    private final String name_;

    /** The value. */
    private final String value_;

    /**
     * Creates a new instance.
     * @param name the name
     * @param value the value
     */
    public NameValuePair(final String name, final String value) {
        name_ = name;
        value_ = value;
    }

    /**
     * Returns the name.
     * @return the name
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the value.
     * @return the value
     */
    public String getValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof NameValuePair)) {
            return false;
        }
        final NameValuePair other = (NameValuePair) object;
        return LangUtils.equals(name_, other.name_) && LangUtils.equals(value_, other.value_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, name_);
        hash = LangUtils.hashCode(hash, value_);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name_ + "=" + value_;
    }

    /**
     * Converts the specified name/value pairs into HttpClient name/value pairs.
     * @param pairs the name/value pairs to convert
     * @return the converted name/value pairs
     */
    public static List<org.apache.http.NameValuePair> toHttpClient(final List<NameValuePair> pairs) {
        final List<org.apache.http.NameValuePair> resultingPairs = new ArrayList<>(pairs.size());
        for (int i = 0; i < pairs.size(); i++) {
            final NameValuePair pair = pairs.get(i);
            resultingPairs.add(new BasicNameValuePair(pair.getName(), pair.getValue()));
        }
        return resultingPairs;
    }

}
