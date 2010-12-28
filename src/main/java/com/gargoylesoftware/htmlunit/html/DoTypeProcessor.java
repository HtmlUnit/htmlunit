/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.Serializable;

abstract class DoTypeProcessor implements Serializable {

    void doType(final String currentValue, final int selectionStart, final int selectionEnd,
            final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {

        final StringBuilder newValue = new StringBuilder(currentValue);
        int cursorPosition = selectionStart;
        if (c == '\b') {
            if (selectionStart > 0) {
                newValue.deleteCharAt(selectionStart - 1);
                cursorPosition = selectionStart - 1;
            }
        }
        else if (c >= '\uE000' && c <= '\uF8FF') {
            // nothing, this is private use area
            // see http://www.unicode.org/charts/PDF/UE000.pdf
        }
        else if (acceptChar(c)) {
            if (selectionStart != currentValue.length()) {
                newValue.replace(selectionStart, selectionEnd, Character.toString(c));
            }
            else {
                newValue.append(c);
            }
            cursorPosition++;
        }

        typeDone(newValue.toString(), cursorPosition);
    }

    /**
     * Indicates if the provided character can by "typed" in the text.
     * @param c the character
     * @return <code>true</code> if it is accepted
     */
    protected boolean acceptChar(final char c) {
        return (c == ' ' || !Character.isWhitespace(c));
    }

    abstract void typeDone(final String newValue, final int newCursorPosition);

}
