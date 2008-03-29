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
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.CharacterData;

import com.gargoylesoftware.htmlunit.Page;

/**
 * Wrapper for the DOM node CharacterData.
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public abstract class DomCharacterData extends DomNode implements CharacterData {

    /** The data string. */
    private String data_;

    /**
     * Creates an instance of DomCharacterData.
     *
     * @param page the Page that contains this element
     * @param data the data string wrapped by this node
     */
    public DomCharacterData(final Page page, final String data) {
        super(page);
        data_ = data;
    }

    /**
     * Gets the data character string for this character data node.
     * @return the data character string
     */
    public String getData() {
        return data_;
    }

    /**
     * Sets the data character string for this character data node.
     * @param data the new data character string
     */
    public void setData(final String data) {
        data_ = data;
    }

    /**
     * Sets the data character string to the new string.
     * @param newValue the new string of data
     */
    @Override
    public void setNodeValue(final String newValue) {
        data_ = newValue;
    }

    /**
     * Returns the number of characters in the character data.
     * @return the number of characters
     */
    public int getLength() {
        return data_.length();
    }

    /**
     * Appends a string to character data.
     * @param newData the string to be appended to the character data
     */
    public void appendData(final String newData) {
        data_ += newData;
    }

    /**
     * Deletes characters from character data.
     * @param offset the position of the first character to be deleted
     * @param count the number of characters to be deleted
     */
    public void deleteData(final int offset, final int count) {
        if (offset < 0 || count < 0) {
            throw new IllegalArgumentException("offset: " + offset + " count: " + count);
        }

        final int tailLength = Math.max(data_.length() - count - offset, 0);
        if (tailLength > 0) {
            data_ = data_.substring(0, offset) + data_.substring(offset + count, offset + count + tailLength);
        }
        else {
            data_ = "";
        }
    }

    /**
     * Inserts a string into character data.
     * @param offset The position within the first character at which the string is to be inserted
     * @param arg the string to insert
     */
    public void insertData(final int offset, final String arg) {
        data_ = new StringBuilder(data_).insert(offset, arg).toString();
    }

    /**
     * Replaces characters of character data with a string.
     * @param offset the position within the first character at which the string is to be replaced
     * @param count the number of characters to be replaced
     * @param arg the string that replaces the count characters beginning at the character at offset
     */
    public void replaceData(final int offset, final int count, final String arg) {
        deleteData(offset, count);
        insertData(offset, arg);
    }

    /**
     * Extracts a substring from character data.
     * @param offset the position of the first character to be extracted
     * @param count the number of characters to be extracted
     * @return a string that consists of the count characters of the character data starting
     *         from the character at position offset
     */
    public String substringData(final int offset, final int count) {
        final int length = data_.length();
        if (count < 0 || offset < 0 || offset > length - 1) {
            throw new IllegalArgumentException("offset: " + offset + " count: " + count);
        }

        final int tailIndex = Math.min(offset + count, length);
        return data_.substring(offset, tailIndex);
    }

    /**
     * {@inheritDoc}
     * @return the string data held by this node
     */
    @Override
    public String getNodeValue() {
        return data_;
    }
}
