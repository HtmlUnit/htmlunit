/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import java.io.Serializable;

import org.apache.commons.httpclient.methods.PostMethod;

/**
 * A collection of constants that represent the various ways a form can be encoded when submitted.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public final class FormEncodingType implements Serializable {

    private static final long serialVersionUID = -7341913381207910442L;

    /** URL-encoded form encoding. */
    public static final FormEncodingType URL_ENCODED = new FormEncodingType(PostMethod.FORM_URL_ENCODED_CONTENT_TYPE);

    /** Multipart form encoding (used to be a constant in HttpClient but it was deprecated with no alternative). */
    public static final FormEncodingType MULTIPART = new FormEncodingType("multipart/form-data");

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
     * @return the constant that matches the specified name
     */
    public static FormEncodingType getInstance(final String name) {
        final String lowerCaseName = name.toLowerCase();

        for (final FormEncodingType type : new FormEncodingType[] {URL_ENCODED, MULTIPART}) {
            if (type.getName().equals(lowerCaseName)) {
                return type;
            }
        }

        // Special case: empty string defaults to URL encoded
        if (name.equals("")) {
            return URL_ENCODED;
        }

        throw new IllegalArgumentException("No encoding type found for [" + name + "]");
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
