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

import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_ADD;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_BACK_SPACE;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_DECIMAL;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_DELETE;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_DIVIDE;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_DOWN;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_END;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_EQUALS;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_HOME;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_LEFT;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_MULTIPLY;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_NUMPAD0;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_NUMPAD9;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_RIGHT;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_SEMICOLON;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_SEPARATOR;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_SPACE;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_SUBTRACT;
import static org.htmlunit.javascript.host.event.KeyboardEvent.DOM_VK_UP;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.htmlunit.ClipboardHandler;
import org.htmlunit.html.impl.SelectionDelegate;

/**
 * The processor for {@link HtmlElement#doType(char, boolean)}
 * and {@link HtmlElement#doType(int, boolean)}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
class DoTypeProcessor implements Serializable {

    private static final Map<Integer, Character> SPECIAL_KEYS_MAP_ = new HashMap<>();

    /**
     * Either {@link HtmlElement} or {@link DomText}.
     */
    private final DomNode domNode_;

    static {
        SPECIAL_KEYS_MAP_.put(DOM_VK_ADD, '+');
        SPECIAL_KEYS_MAP_.put(DOM_VK_DECIMAL, '.');
        SPECIAL_KEYS_MAP_.put(DOM_VK_DIVIDE, '/');
        SPECIAL_KEYS_MAP_.put(DOM_VK_EQUALS, '=');
        SPECIAL_KEYS_MAP_.put(DOM_VK_MULTIPLY, '*');
        SPECIAL_KEYS_MAP_.put(DOM_VK_SEMICOLON, ';');
        SPECIAL_KEYS_MAP_.put(DOM_VK_SEPARATOR, ',');
        SPECIAL_KEYS_MAP_.put(DOM_VK_SPACE, ' ');
        SPECIAL_KEYS_MAP_.put(DOM_VK_SUBTRACT, '-');

        for (int i = DOM_VK_NUMPAD0; i <= DOM_VK_NUMPAD9; i++) {
            SPECIAL_KEYS_MAP_.put(i, (char) ('0' + i - DOM_VK_NUMPAD0));
        }
    }

    DoTypeProcessor(final DomNode domNode) {
        domNode_ = domNode;
    }

    void doType(final String currentValue, final SelectionDelegate selectionDelegate,
            final char c, final HtmlElement element, final boolean lastType) {

        int selectionStart = selectionDelegate.getSelectionStart();
        selectionStart = Math.max(0, Math.min(selectionStart, currentValue.length()));

        int selectionEnd = selectionDelegate.getSelectionEnd();
        selectionEnd = Math.max(selectionStart, Math.min(selectionEnd, currentValue.length()));

        final StringBuilder newValue = new StringBuilder(currentValue);
        if (c == '\b') {
            if (selectionStart > 0) {
                newValue.deleteCharAt(selectionStart - 1);
                selectionStart--;
                selectionEnd--;
            }
        }
        else if (acceptChar(c)) {
            final boolean ctrlKey = element.isCtrlPressed();
            if (ctrlKey && (c == 'C' || c == 'c')) {
                final ClipboardHandler clipboardHandler = element.getPage().getWebClient().getClipboardHandler();
                if (clipboardHandler != null) {
                    final String content = newValue.substring(selectionStart, selectionEnd);
                    clipboardHandler.setClipboardContent(content);
                }
            }
            else if (ctrlKey && (c == 'V' || c == 'v')) {
                final ClipboardHandler clipboardHandler = element.getPage().getWebClient().getClipboardHandler();
                if (clipboardHandler != null) {
                    final String content = clipboardHandler.getClipboardContent();
                    add(newValue, content, selectionStart, selectionEnd);
                    selectionStart += content.length();
                    selectionEnd = selectionStart;
                }
            }
            else if (ctrlKey && (c == 'X' || c == 'x')) {
                final ClipboardHandler clipboardHandler = element.getPage().getWebClient().getClipboardHandler();
                if (clipboardHandler != null) {
                    final String content = newValue.substring(selectionStart, selectionEnd);
                    clipboardHandler.setClipboardContent(content);
                    newValue.delete(selectionStart, selectionEnd);
                    selectionEnd = selectionStart;
                }
            }
            else if (ctrlKey && (c == 'A' || c == 'a')) {
                selectionStart = 0;
                selectionEnd = newValue.length();
            }
            else {
                add(newValue, c, selectionStart, selectionEnd);
                selectionStart++;
                selectionEnd = selectionStart;
            }
        }

        typeDone(newValue.toString(), lastType);

        selectionDelegate.setSelectionStart(selectionStart);
        selectionDelegate.setSelectionEnd(selectionEnd);
    }

    private static void add(final StringBuilder newValue, final char c, final int selectionStart,
            final int selectionEnd) {
        if (selectionStart == newValue.length()) {
            newValue.append(c);
        }
        else {
            newValue.replace(selectionStart, selectionEnd, Character.toString(c));
        }
    }

    private static void add(final StringBuilder newValue, final String string, final int selectionStart,
            final int selectionEnd) {
        if (selectionStart == newValue.length()) {
            newValue.append(string);
        }
        else {
            newValue.replace(selectionStart, selectionEnd, string);
        }
    }

    private void typeDone(final String newValue, final boolean notifyAttributeChangeListeners) {
        if (domNode_ instanceof DomText text) {
            text.setData(newValue);
        }
        else {
            ((HtmlElement) domNode_).typeDone(newValue, notifyAttributeChangeListeners);
        }
    }

    private boolean acceptChar(final char ch) {
        if (domNode_ instanceof DomText text) {
            return text.acceptChar(ch);
        }
        return ((HtmlElement) domNode_).acceptChar(ch);
    }

    void doType(final String currentValue, final SelectionDelegate selectionDelegate,
            final int keyCode, final HtmlElement element, final boolean lastType) {

        int selectionStart = selectionDelegate.getSelectionStart();
        selectionStart = Math.max(0, Math.min(selectionStart, currentValue.length()));

        int selectionEnd = selectionDelegate.getSelectionEnd();
        selectionEnd = Math.max(selectionStart, Math.min(selectionEnd, currentValue.length()));

        final Character ch = SPECIAL_KEYS_MAP_.get(keyCode);
        if (ch != null) {
            doType(currentValue, selectionDelegate, ch, element, lastType);
            return;
        }

        final StringBuilder newValue = new StringBuilder(currentValue);
        switch (keyCode) {
            case DOM_VK_BACK_SPACE:
                if (selectionEnd != selectionStart) {
                    newValue.delete(selectionStart, selectionEnd);
                    selectionEnd = selectionStart;
                    break;
                }

                if (selectionStart > 0) {
                    if (element.isCtrlPressed()) {
                        int targetStart = selectionStart;
                        while (targetStart > 0 && Character.isWhitespace(newValue.charAt(targetStart - 1))) {
                            targetStart--;
                        }
                        while (targetStart > 0 && !Character.isWhitespace(newValue.charAt(targetStart - 1))) {
                            targetStart--;
                        }
                        newValue.delete(targetStart, selectionStart);
                        selectionStart = targetStart;
                    }
                    else {
                        newValue.deleteCharAt(selectionStart - 1);
                        selectionStart--;
                    }
                }
                break;

            case DOM_VK_LEFT:
                if (element.isCtrlPressed()) {
                    int targetPos = selectionStart;
                    // 1. Skip whitespace to the left of caret
                    while (targetPos > 0 && Character.isWhitespace(newValue.charAt(targetPos - 1))) {
                        targetPos--;
                    }
                    // 2. Skip word characters to the left
                    while (targetPos > 0 && !Character.isWhitespace(newValue.charAt(targetPos - 1))) {
                        targetPos--;
                    }
                    selectionStart = targetPos;
                }
                else if (element.isShiftPressed()) {
                    if (selectionStart > 0) {
                        selectionStart--;
                    }
                }
                else if (selectionStart > 0) {
                    selectionStart--;
                }
                break;

            case DOM_VK_RIGHT:
                if (element.isCtrlPressed()) {
                    int targetPos = selectionStart;
                    // 1. Skip word characters to the right
                    while (targetPos < newValue.length() && !Character.isWhitespace(newValue.charAt(targetPos))) {
                        targetPos++;
                    }
                    // 2. Skip whitespace to the right
                    while (targetPos < newValue.length() && Character.isWhitespace(newValue.charAt(targetPos))) {
                        targetPos++;
                    }
                    if (element.isShiftPressed()) {
                        selectionEnd = targetPos;
                    }
                    else {
                        selectionStart = targetPos;
                    }
                }
                else if (element.isShiftPressed()) {
                    if (selectionEnd < newValue.length()) {
                        selectionEnd++;
                    }
                }
                else if (selectionStart < newValue.length()) {
                    selectionStart++;
                }
                break;

            case DOM_VK_UP:
                final int lastLfUp = currentValue.lastIndexOf('\n', selectionStart - 1);
                final int lastCrUp = currentValue.lastIndexOf('\r', selectionStart - 1);
                final int lastBreakUp = Math.max(lastLfUp, lastCrUp);

                if (lastBreakUp == -1) {
                    selectionStart = 0;
                }
                else {
                    final int currentLineStart = lastBreakUp + 1;
                    final int column = selectionStart - currentLineStart;

                    final int prevLineEnd = (lastBreakUp > 0 && newValue.charAt(lastBreakUp) == '\n'
                            && newValue.charAt(lastBreakUp - 1) == '\r') ? lastBreakUp - 1 : lastBreakUp;

                    final int prevLf = currentValue.lastIndexOf('\n', prevLineEnd - 1);
                    final int prevCr = currentValue.lastIndexOf('\r', prevLineEnd - 1);
                    final int prevBreak = Math.max(prevLf, prevCr);

                    final int prevLineStart = (prevBreak == -1) ? 0 : prevBreak + 1;
                    final int prevLineLength = prevLineEnd - prevLineStart;

                    selectionStart = prevLineStart + Math.min(column, prevLineLength);
                }
                break;

            case DOM_VK_DOWN:
                final int lastLfDown = currentValue.lastIndexOf('\n', selectionStart - 1);
                final int lastCrDown = currentValue.lastIndexOf('\r', selectionStart - 1);
                final int lastBreakDown = Math.max(lastLfDown, lastCrDown);
                final int currentLineStartDown = (lastBreakDown == -1) ? 0 : lastBreakDown + 1;
                final int columnDown = selectionStart - currentLineStartDown;

                final int nextLfDown = currentValue.indexOf('\n', selectionStart);
                final int nextCrDown = currentValue.indexOf('\r', selectionStart);
                final int nextBreakDown;
                if (nextLfDown == -1) {
                    nextBreakDown = nextCrDown;
                }
                else if (nextCrDown == -1) {
                    nextBreakDown = nextLfDown;
                }
                else {
                    nextBreakDown = Math.min(nextLfDown, nextCrDown);
                }

                final int targetDown;
                if (nextBreakDown == -1) {
                    targetDown = newValue.length();
                }
                else {
                    int nextLineStart = nextBreakDown + 1;
                    if (newValue.charAt(nextBreakDown) == '\r' && nextLineStart < newValue.length()
                            && newValue.charAt(nextLineStart) == '\n') {
                        nextLineStart++;
                    }

                    final int followingLf = currentValue.indexOf('\n', nextLineStart);
                    final int followingCr = currentValue.indexOf('\r', nextLineStart);
                    final int followingBreak;
                    if (followingLf == -1) {
                        followingBreak = followingCr;
                    }
                    else if (followingCr == -1) {
                        followingBreak = followingLf;
                    }
                    else {
                        followingBreak = Math.min(followingLf, followingCr);
                    }

                    final int nextLineEnd = (followingBreak == -1) ? newValue.length() : followingBreak;
                    final int nextLineLength = nextLineEnd - nextLineStart;

                    targetDown = nextLineStart + Math.min(columnDown, nextLineLength);
                }

                if (element.isShiftPressed()) {
                    selectionEnd = targetDown;
                }
                else {
                    selectionStart = targetDown;
                }
                break;

            case DOM_VK_HOME:
                if (element.isCtrlPressed()) {
                    selectionStart = 0;
                    break;
                }

                final int lastLf = currentValue.lastIndexOf('\n', selectionStart - 1);
                final int lastCr = currentValue.lastIndexOf('\r', selectionStart - 1);
                // In a \r\n sequence, \n comes last, so Math.pax places cursor left before \n
                final int lastBreak = Math.max(lastLf, lastCr);
                // right after \n
                selectionStart = (lastBreak == -1) ? 0 : lastBreak + 1;
                break;

            case DOM_VK_END:
                int targetEnd;
                if (element.isCtrlPressed()) {
                    targetEnd = newValue.length();
                }
                else {
                    final int nextLf = currentValue.indexOf('\n', selectionStart);
                    final int nextCr = currentValue.indexOf('\r', selectionStart);
                    if (nextLf == -1) {
                        targetEnd = nextCr;
                    }
                    else if (nextCr == -1) {
                        targetEnd = nextLf;
                    }
                    else {
                        targetEnd = Math.min(nextLf, nextCr);
                    }
                    if (targetEnd == -1) {
                        targetEnd = newValue.length();
                    }
                }

                if (element.isShiftPressed()) {
                    selectionEnd = targetEnd;
                }
                else {
                    selectionStart = targetEnd;
                }
                break;

            case DOM_VK_DELETE:
                if (selectionEnd != selectionStart) {
                    newValue.delete(selectionStart, selectionEnd);
                    selectionEnd = selectionStart;
                    break;
                }

                if (selectionStart < newValue.length()) {
                    if (element.isCtrlPressed()) {
                        int delTargetEnd = selectionStart;
                        // 1. Skip initial whitespace at caret (if caret is on a space)
                        while (delTargetEnd < newValue.length()
                                && Character.isWhitespace(newValue.charAt(delTargetEnd))) {
                            delTargetEnd++;
                        }
                        // 2. Skip word characters
                        while (delTargetEnd < newValue.length()
                                && !Character.isWhitespace(newValue.charAt(delTargetEnd))) {
                            delTargetEnd++;
                        }
                        // 3. Skip trailing whitespace after the word
                        while (delTargetEnd < newValue.length()
                                && Character.isWhitespace(newValue.charAt(delTargetEnd))) {
                            delTargetEnd++;
                        }
                        newValue.delete(selectionStart, delTargetEnd);
                    }
                    else {
                        newValue.deleteCharAt(selectionStart);
                    }
                }
                break;

            default:
                return;
        }

        if (!element.isShiftPressed()) {
            selectionEnd = selectionStart;
        }

        typeDone(newValue.toString(), lastType);

        selectionDelegate.setSelectionStart(selectionStart);
        selectionDelegate.setSelectionEnd(selectionEnd);
    }
}
