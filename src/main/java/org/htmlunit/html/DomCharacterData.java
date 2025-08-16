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
package org.htmlunit.html;

import org.htmlunit.SgmlPage;
import org.w3c.dom.CharacterData;

/**
 * Wrapper for the DOM node CharacterData.
 *
 * @author David K. Taylor
 * @author Christian Sell
 * @author Ahmed Ashour
 * @author Philip Graf
 * @author Ronald Brill
 * @author Frank Danek
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
    public DomCharacterData(final SgmlPage page, final String data) {
        super(page);
        setData(data);
    }

    /**
     * Gets the data character string for this character data node.
     * @return the data character string
     */
    @Override
    public String getData() {
        return data_;
    }

    /**
     * Sets the data character string for this character data node.
     * @param data the new data character string
     */
    @Override
    public void setData(final String data) {
        final String oldData = data_;
        data_ = data;

        final SgmlPage page = getPage();
        if (page == null || page.isCharacterDataChangeListenerInUse()) {
            fireCharacterDataChanged(this, oldData);
        }
    }

    /**
     * Sets the data character string to the new string.
     * @param newValue the new string of data
     */
    @Override
    public void setNodeValue(final String newValue) {
        setData(newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextContent(final String textContent) {
        setData(textContent);
    }

    /**
     * Returns the number of characters in the character data.
     * @return the number of characters
     */
    @Override
    public int getLength() {
        return data_.length();
    }

    /**
     * Appends a string to character data.
     * @param newData the string to be appended to the character data
     */
    @Override
    public void appendData(final String newData) {
        data_ += newData;
    }

    /**
     * Deletes characters from character data.
     * @param offset the position of the first character to be deleted (can't be
     *        less than zero)
     * @param count the number of characters to be deleted, if less than zero
     *        leaves the first offset chars
     */
    @Override
    public void deleteData(final int offset, final int count) {
        if (offset < 0) {
            throw new IllegalArgumentException("Provided offset: " + offset + " is less than zero.");
        }

        final String data = data_.substring(0, offset);
        if (count >= 0) {
            final int fromLeft = offset + count;
            if (fromLeft < data_.length()) {
                setData(data + data_.substring(fromLeft));
                return;
            }
        }
        setData(data);
    }

    /**
     * Inserts a string into character data.
     * @param offset the position within the first character at which the string is to be inserted
     * @param arg the string to insert
     */
    @Override
    public void insertData(final int offset, final String arg) {
        setData(new StringBuilder(data_).insert(offset, arg).toString());
    }

    /**
     * Replaces characters of character data with a string.
     * @param offset the position within the first character at which the string is to be replaced
     * @param count the number of characters to be replaced
     * @param arg the string that replaces the count characters beginning at the character at offset
     */
    @Override
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
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCanonicalXPath() {
        return getParentNode().getCanonicalXPath() + '/' + getXPathToken();
    }

    /**
     * Returns the XPath token for this node only.
     */
    private String getXPathToken() {
        final DomNode parent = getParentNode();

        // If there are other siblings of the same node type, we have to provide
        // the node's index.
        int siblingsOfSameType = 0;
        int nodeIndex = 0;
        for (final DomNode child : parent.getChildren()) {
            if (child == this) {
                nodeIndex = ++siblingsOfSameType;
                if (nodeIndex > 1) {
                    // Optimization: if the node index is greater than 1, there
                    // are at least two nodes of the same type.
                    break;
                }
            }
            else if (child.getNodeType() == getNodeType()) {
                siblingsOfSameType++;
                if (nodeIndex > 0) {
                    // Optimization: if the node index is greater than 0, there
                    // are at least two nodes of the same type.
                    break;
                }
            }
        }

        final String nodeName = getNodeName().substring(1) + "()";
        if (siblingsOfSameType == 1) {
            return nodeName;
        }
        return nodeName + '[' + nodeIndex + ']';
    }
}
