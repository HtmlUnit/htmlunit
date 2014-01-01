/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMCharacterData.<br>
 * Provides text manipulation methods that are used by several objects.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms767515.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Frank Danek
 */
@JsxClass(domClass = DomCharacterData.class, browsers = @WebBrowser(IE))
public class XMLDOMCharacterData extends XMLDOMNode {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMCharacterData() {
    }

    /**
     * Returns the node data depending on the node type.
     * @return the node data depending on the node type
     */
    @JsxGetter
    public Object getData() {
        final DomCharacterData domCharacterData = getDomNodeOrDie();
        return domCharacterData.getData();
    }

    /**
     * Sets the node data depending on the node type.
     * @param newData the node data depending on the node type
     */
    @JsxSetter
    public void setData(final String newData) {
        if (newData == null || "null".equals(newData)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final DomCharacterData domCharacterData = getDomNodeOrDie();
        domCharacterData.setData(newData);
    }

    /**
     * Returns the length, in characters, of the data.
     * @return the length of the data
     */
    @JsxGetter
    public int getLength() {
        final DomCharacterData domCharacterData = getDomNodeOrDie();
        return domCharacterData.getLength();
    }

    /**
     * Sets the text contained in the node.
     * @param newText the text contained in the node
     */
    @Override
    public void setText(final Object newText) {
        setData(newText == null ? null : Context.toString(newText));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getXml() {
        Object xml = super.getXml();
        if (xml instanceof String) {
            final String xmlString = (String) xml;
            if (xmlString.indexOf('\n') >= 0) {
                xml = xmlString.replaceAll("([^\r])\n", "$1\r\n");
            }
        }
        return xml;
    }

    /**
     * Appends the supplied string to the existing string data.
     * @param data the data that is to be appended to the existing string
     */
    @JsxFunction
    public void appendData(final String data) {
        if (data == null || "null".equals(data)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final DomCharacterData domCharacterData = getDomNodeOrDie();
        domCharacterData.appendData(data);
    }

    /**
     * Deletes specified data.
     * @param offset the offset, in characters, at which to start deleting string data
     * @param count the number of characters to delete
     */
    @JsxFunction
    public void deleteData(final int offset, final int count) {
        if (offset < 0) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }
        if (count < 0) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }
        if (count == 0) {
            return;
        }

        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        if (offset > domCharacterData.getLength()) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }

        domCharacterData.deleteData(offset, count);
    }

    /**
     * Inserts a string at the specified offset.
     * @param offset the offset, in characters, at which to insert the supplied string data
     * @param data the data that is to be inserted into the existing string
     */
    @JsxFunction
    public void insertData(final int offset, final String data) {
        if (data.isEmpty()) {
            return;
        }
        if (offset < 0) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }
        if (data == null || "null".equals(data)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final DomCharacterData domCharacterData = getDomNodeOrDie();
        if (offset > domCharacterData.getLength()) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }

        domCharacterData.insertData(offset, data);
    }

    /**
     * Replaces the specified number of characters with the supplied string.
     * @param offset the offset, in characters, at which to start replacing string data
     * @param count the number of characters to replace
     * @param data the new data that replaces the old string data
     */
    @JsxFunction
    public void replaceData(final int offset, final int count, final String data) {
        if (offset < 0) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }
        if (count < 0) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }
        if (data == null || "null".equals(data)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final DomCharacterData domCharacterData = getDomNodeOrDie();
        if (offset > domCharacterData.getLength()) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }

        domCharacterData.replaceData(offset, count, data);
    }

    /**
     * Retrieves a substring of the full string from the specified range.
     * @param offset the offset, in characters, from the beginning of the string. An offset of zero indicates
     *     copying from the start of the data
     * @param count the number of characters to retrieve from the specified offset
     * @return the substring
     */
    @JsxFunction
    public String substringData(final int offset, final int count) {
        if (offset < 0) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }
        if (count < 0) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }

        final DomCharacterData domCharacterData = getDomNodeOrDie();
        if (offset > domCharacterData.getLength()) {
            throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the "
                    + "number of characters in the data.");
        }

        return domCharacterData.substringData(offset, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomCharacterData getDomNodeOrDie() {
        return (DomCharacterData) super.getDomNodeOrDie();
    }
}
