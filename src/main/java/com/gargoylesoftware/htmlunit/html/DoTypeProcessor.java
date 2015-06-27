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
import java.util.HashMap;
import java.util.Map;

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
class DoTypeProcessor implements Serializable {

    private static Map<Integer, Character> SPECIAL_KEYS_MAP_ = new HashMap<>();

    private HtmlElement htmlElement_;

    static {
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_ADD, '+');
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_DECIMAL, '.');
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_DIVIDE, '/');
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_EQUALS, '=');
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_MULTIPLY, '*');
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_SEMICOLON, ';');
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_SEPARATOR, ',');
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_SPACE, ' ');
        SPECIAL_KEYS_MAP_.put(KeyboardEvent.DOM_VK_SUBTRACT, '-');

        for (int i = KeyboardEvent.DOM_VK_NUMPAD0; i <= KeyboardEvent.DOM_VK_NUMPAD9; i++) {
            SPECIAL_KEYS_MAP_.put(i, (char) ('0' + (i - KeyboardEvent.DOM_VK_NUMPAD0)));
        }
    }

    DoTypeProcessor(final HtmlElement htmlElement) {
        htmlElement_ = htmlElement;
    }

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
        else if (htmlElement_.acceptChar(c)) {
            if (selectionStart != currentValue.length()) {
                newValue.replace(selectionStart, selectionEnd, Character.toString(c));
            }
            else {
                newValue.append(c);
            }
            selectionStart++;
        }

        selectionEnd = selectionStart;

        htmlElement_.typeDone(newValue.toString());

        selectionDelegate.setSelectionStart(selectionStart);
        selectionDelegate.setSelectionEnd(selectionEnd);
    }

    void doType(final String currentValue, final SelectionDelegate selectionDelegate,
            final int keyCode, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {

        final StringBuilder newValue = new StringBuilder(currentValue);
        int selectionStart = selectionDelegate.getSelectionStart();
        int selectionEnd = selectionDelegate.getSelectionEnd();

        final Character ch = SPECIAL_KEYS_MAP_.get(keyCode);
        if (ch != null) {
            doType(currentValue, selectionDelegate, ch, shiftKey, ctrlKey, altKey);
            return;
        }
        switch (keyCode) {
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
                if (shiftKey) {
                    selectionEnd++;
                }
                else if (selectionStart > 0) {
                    selectionStart++;
                }
                break;

            case KeyboardEvent.DOM_VK_HOME:
                selectionStart = 0;
                break;

            case KeyboardEvent.DOM_VK_END:
                if (shiftKey) {
                    selectionEnd = newValue.length();
                }
                else {
                    selectionStart = newValue.length();
                }
                break;

            case KeyboardEvent.DOM_VK_DELETE:
                if (selectionEnd == selectionStart) {
                    selectionEnd++;
                }
                newValue.delete(selectionStart, selectionEnd);
                selectionEnd = selectionStart;
                break;

            default:
                break;
        }

        if (!shiftKey) {
            selectionEnd = selectionStart;
        }

        htmlElement_.typeDone(newValue.toString());

        selectionDelegate.setSelectionStart(selectionStart);
        selectionDelegate.setSelectionEnd(selectionEnd);
    }

}
