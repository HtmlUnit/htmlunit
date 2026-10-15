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
package org.htmlunit.html;

import java.io.PrintWriter;

import org.htmlunit.SgmlPage;
import org.w3c.dom.DOMException;
import org.w3c.dom.ProcessingInstruction;

/**
 * Wrapper for the DOM node ProcessingInstruction.
 * <p>
 * <em>Note:</em> There is an architectural difference between the Java DOM layer and the JavaScript host layer:
 * This Java class extends {@link DomNode} directly rather than a character data class, whereas the corresponding
 * JavaScript host class inherits from {@link org.htmlunit.javascript.host.dom.CharacterData} to match browser behavior.
 * </p>
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class DomProcessingInstruction extends DomNode implements ProcessingInstruction {

    private final String target_;
    private String data_;

    /**
     * Creates a new instance.
     *
     * @param page the Page that contains this element
     * @param target the target
     * @param data the data
     */
    public DomProcessingInstruction(final SgmlPage page, final String target, final String data) {
        super(page);
        target_ = target;
        setData(data);
    }

    /**
     * {@inheritDoc}
     * @return the node type constant, in this case {@link org.w3c.dom.Node#PROCESSING_INSTRUCTION_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return target_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTarget() {
        return getNodeName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getData() {
        return getNodeValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(final String data) throws DOMException {
        setNodeValue(data);
    }

    /**
     * Returns the number of characters in the character data.
     * @return the number of characters
     */
    public int getLength() {
        return data_ == null ? 0 : data_.length();
    }

    /**
     * Appends a string to character data.
     * @param newData the string to be appended to the character data
     */
    public void appendData(final String newData) {
        if (newData != null) {
            setData(data_ == null ? newData : data_ + newData);
        }
    }

    /**
     * Deletes characters from character data.
     * @param offset the position of the first character to be deleted (can't be
     *        less than zero)
     * @param count the number of characters to be deleted, if less than zero
     *        leaves the first offset chars
     */
    public void deleteData(final int offset, final int count) {
        if (offset < 0) {
            throw new IllegalArgumentException("Provided offset: " + offset + " is less than zero.");
        }
        if (data_ == null) {
            return;
        }

        final String data = data_.substring(0, Math.min(offset, data_.length()));
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
    public void insertData(final int offset, final String arg) {
        if (data_ == null) {
            setData(arg);
            return;
        }

        setData(new StringBuilder(data_).insert(offset, arg).toString());
    }

    /**
     * Replaces characters of character data with a string.
     * @param offset the position within the first character at which the string is to be replaced
     * @param count the number of characters to be replaced
     * @param arg the string that replaces the count characters beginning at the character at offset
     */
    public void replaceData(final int offset, final int count, final String arg) {
        if (offset < 0 || count < 0) {
            throw new IllegalArgumentException("offset: " + offset + " count: " + count);
        }
        if (data_ == null) {
            setData(arg);
            return;
        }

        final int end = Math.min(offset + count, data_.length());
        final StringBuilder sb = new StringBuilder(data_);
        sb.replace(offset, end, arg != null ? arg : "");
        setData(sb.toString());
    }

    /**
     * Extracts a substring from character data.
     * @param offset the position of the first character to be extracted
     * @param count the number of characters to be extracted
     * @return a string that consists of the count characters of the character data starting
     *         from the character at position offset
     */
    public String substringData(final int offset, final int count) {
        if (data_ == null) {
            throw new IllegalArgumentException("offset: " + offset + " count: " + count);
        }
        final int length = data_.length();
        if (count < 0 || offset < 0 || offset > length - 1) {
            throw new IllegalArgumentException("offset: " + offset + " count: " + count);
        }

        final int tailIndex = Math.min(offset + count, length);
        return data_.substring(offset, tailIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String value) {
        data_ = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeValue() {
        return data_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextContent(final String textContent) {
        setNodeValue(textContent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean printXml(final String indent, final boolean indentBefore, final PrintWriter printWriter) {
        printWriter.print("<?");
        printWriter.print(getTarget());
        printWriter.print(" ");
        printWriter.print(getData());
        printWriter.print("?>");
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix(final String prefix) {
        // Empty.
    }
}
