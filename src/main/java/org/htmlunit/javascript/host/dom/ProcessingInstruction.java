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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.html.DomProcessingInstruction;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for {@code ProcessingInstruction}.
 * <p>
 * <em>Note:</em> There is an architectural difference between the Java DOM layer and the JavaScript host layer:
 * {@link org.htmlunit.html.DomProcessingInstruction} extends {@link org.htmlunit.html.DomNode} directly
 * rather than a Java-equivalent character data class, whereas this JavaScript class inherits from {@link CharacterData}
 * to correctly mirror the standard browser prototype chain.
 * </p>
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/ProcessingInstruction">MDN Documentation</a>
 */
@JsxClass(domClass = DomProcessingInstruction.class)
public class ProcessingInstruction extends CharacterData {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the {@code target} attribute.
     * @return the target
     */
    @JsxGetter
    public String getTarget() {
        return getDomProcessingInstruction().getTarget();
    }

    /**
     * Returns the {@code data} attribute.
     * @return the data
     */
    @Override
    @JsxGetter
    public String getData() {
        return getDomProcessingInstruction().getData();
    }

    /**
     * Sets the {@code data} attribute.
     * @param data the data
     */
    @Override
    @JsxSetter
    public void setData(final String data) {
        getDomProcessingInstruction().setData(data);
    }

    /**
     * Gets the number of character in the character data.
     * @return the number of characters
     */
    @Override
    @JsxGetter
    public int getLength() {
        return getDomProcessingInstruction().getLength();
    }

    /**
     * Appends a string to character data.
     * @param arg the string to be appended to the character data
     */
    @Override
    @JsxFunction
    public void appendData(final String arg) {
        getDomProcessingInstruction().appendData(arg);
    }

    /**
     * Deletes characters from character data.
     * @param offset the position of the first character to be deleted
     * @param count the number of characters to be deleted
     */
    @Override
    @JsxFunction
    public void deleteData(final int offset, final int count) {
        if (offset < 0) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "Provided offset: " + offset + " is less than zero.",
                    DOMException.INDEX_SIZE_ERR);
        }

        final DomProcessingInstruction domProcessingInstruction = getDomProcessingInstruction();
        if (offset > domProcessingInstruction.getLength()) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "Provided offset: " + offset + " is greater than length.",
                    DOMException.INDEX_SIZE_ERR);
        }

        domProcessingInstruction.deleteData(offset, count);
    }

    /**
     * Inserts a string into character data.
     * @param offset the position within the first character at which
     *        the string is to be inserted.
     * @param arg the string to insert
     */
    @Override
    @JsxFunction
    public void insertData(final int offset, final String arg) {
        getDomProcessingInstruction().insertData(offset, arg);
    }

    /**
     * Replaces characters of character data with a string.
     * @param offset the position within the first character at which
     *        the string is to be replaced.
     * @param count the number of characters to be replaced
     * @param arg the string that replaces the count characters beginning at
     *        the character at offset.
     */
    @Override
    @JsxFunction
    public void replaceData(final int offset, final int count, final String arg) {
        getDomProcessingInstruction().replaceData(offset, count, arg);
    }

    /**
     * Extracts a substring from character data.
     * @param offset the position of the first character to be extracted
     * @param count the number of characters to be extracted
     * @return a string that consists of the count characters of the character
     *         data starting from the character at position offset
     */
    @Override
    @JsxFunction
    public String substringData(final int offset, final int count) {
        return getDomProcessingInstruction().substringData(offset, count);
    }

    private DomProcessingInstruction getDomProcessingInstruction() {
        return (DomProcessingInstruction) getDomNodeOrDie();
    }

}
