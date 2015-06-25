/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.impl.SelectionDelegate;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;

/**
 * The process for {@link HtmlElement#doType(char, boolean, boolean, boolean)}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
abstract class DoTypeProcessor implements Serializable {

    void doType(final String currentValue, final SelectionDelegate selectionDelegate,
            final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {

        int selectionStart = selectionDelegate.getSelectionStart();
        int selectionEnd = selectionDelegate.getSelectionEnd();

        final StringBuilder newValue = new StringBuilder(currentValue);
        if (c == '\b') {
            if (selectionStart > 0) {
                newValue.deleteCharAt(selectionStart - 1);
                selectionStart--;
                selectionEnd--;
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
            selectionStart++;
        }

        if (!shiftKey) {
        	selectionEnd = selectionStart;
        }

        typeDone(newValue.toString());

        selectionDelegate.setSelectionStart(selectionStart);
        selectionDelegate.setSelectionEnd(selectionEnd);
    }

    void doType(final String currentValue, final SelectionDelegate selectionDelegate,
            final int keyCode, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {

        final StringBuilder newValue = new StringBuilder(currentValue);
        int selectionStart = selectionDelegate.getSelectionStart();
        int selectionEnd = selectionDelegate.getSelectionEnd();

        if (keyCode >= '\uE000' && keyCode <= '\uF8FF') {
            // nothing, this is private use area
            // see http://www.unicode.org/charts/PDF/UE000.pdf
        }
        else {
            switch (keyCode) {
                case KeyboardEvent.DOM_VK_SPACE:
                    doType(currentValue, selectionDelegate, ' ', shiftKey, ctrlKey, altKey);
                    return;

                case KeyboardEvent.DOM_VK_BACK_SPACE:
                    if (selectionStart > 0) {
                        newValue.deleteCharAt(selectionStart - 1);
                        selectionStart--;
                    }
                    break;

                case KeyboardEvent.DOM_VK_LEFT:
                    if (selectionStart > 0) {
                        selectionStart--;
                    }
                    break;

                case KeyboardEvent.DOM_VK_RIGHT:
                    if (selectionStart > 0) {
                        selectionStart++;
                    }
                    break;

                case KeyboardEvent.DOM_VK_HOME:
                    selectionStart = 0;
                    break;

                case KeyboardEvent.DOM_VK_END:
                	selectionStart = newValue.length();
                    break;

                case KeyboardEvent.DOM_VK_DELETE:
                	newValue.delete(selectionStart, selectionEnd);
                	selectionEnd = selectionStart;
                    break;

                default:
                    break;
            }
        }

        if (!shiftKey) {
        	selectionEnd = selectionStart;
        }

        typeDone(newValue.toString());

        selectionDelegate.setSelectionStart(selectionStart);
        selectionDelegate.setSelectionEnd(selectionEnd);
    }

    /**
     * Indicates if the provided character can by "typed" in the text.
     * @param c the character
     * @return <code>true</code> if it is accepted
     */
    protected boolean acceptChar(final char c) {
        return c == ' ' || !Character.isWhitespace(c);
    }

    abstract void typeDone(final String newValue);

}
