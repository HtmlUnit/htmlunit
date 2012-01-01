/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * JavaScript object representing a Keyboard Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3c.org/TR/DOM-Level-3-Events/#Events-KeyboardEvents-Interfaces">
 * DOM Level 3 Events</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class KeyboardEvent extends UIEvent {

    /** Constant for DOM_VK_MULTIPLY. */
    public static final int DOM_VK_MULTIPLY = 106;

    /** Constant for DOM_VK_ADD. */
    public static final int DOM_VK_ADD = 107;

    /** Constant for DOM_VK_SEPARATOR. */
    public static final int DOM_VK_SEPARATOR = 108;

    /** Constant for DOM_VK_SUBTRACT. */
    public static final int DOM_VK_SUBTRACT = 109;

    /** Constant for DOM_VK_DECIMAL. */
    public static final int DOM_VK_DECIMAL = 110;

    /** Constant for DOM_VK_DIVIDE. */
    public static final int DOM_VK_DIVIDE = 111;

    /** Constant for DOM_VK_F1. */
    public static final int DOM_VK_F1 = 112;

    /** Constant for DOM_VK_F2. */
    public static final int DOM_VK_F2 = 113;

    /** Constant for DOM_VK_F3. */
    public static final int DOM_VK_F3 = 114;

    /** Constant for DOM_VK_F4. */
    public static final int DOM_VK_F4 = 115;

    /** Constant for DOM_VK_F5. */
    public static final int DOM_VK_F5 = 116;

    /** Constant for DOM_VK_F6. */
    public static final int DOM_VK_F6 = 117;

    /** Constant for DOM_VK_F7. */
    public static final int DOM_VK_F7 = 118;

    /** Constant for DOM_VK_F8. */
    public static final int DOM_VK_F8 = 119;

    /** Constant for DOM_VK_CLEAR. */
    public static final int DOM_VK_CLEAR = 12;

    /** Constant for DOM_VK_F9. */
    public static final int DOM_VK_F9 = 120;

    /** Constant for DOM_VK_F10. */
    public static final int DOM_VK_F10 = 121;

    /** Constant for DOM_VK_F11. */
    public static final int DOM_VK_F11 = 122;

    /** Constant for DOM_VK_F12. */
    public static final int DOM_VK_F12 = 123;

    /** Constant for DOM_VK_F13. */
    public static final int DOM_VK_F13 = 124;

    /** Constant for DOM_VK_F14. */
    public static final int DOM_VK_F14 = 125;

    /** Constant for DOM_VK_F15. */
    public static final int DOM_VK_F15 = 126;

    /** Constant for DOM_VK_F16. */
    public static final int DOM_VK_F16 = 127;

    /** Constant for DOM_VK_F17. */
    public static final int DOM_VK_F17 = 128;

    /** Constant for DOM_VK_F18. */
    public static final int DOM_VK_F18 = 129;

    /** Constant for DOM_VK_RETURN. */
    public static final int DOM_VK_RETURN = 13;

    /** Constant for DOM_VK_F19. */
    public static final int DOM_VK_F19 = 130;

    /** Constant for DOM_VK_F20. */
    public static final int DOM_VK_F20 = 131;

    /** Constant for DOM_VK_F21. */
    public static final int DOM_VK_F21 = 132;

    /** Constant for DOM_VK_F22. */
    public static final int DOM_VK_F22 = 133;

    /** Constant for DOM_VK_F23. */
    public static final int DOM_VK_F23 = 134;

    /** Constant for DOM_VK_F24. */
    public static final int DOM_VK_F24 = 135;

    /** Constant for DOM_VK_ENTER. */
    public static final int DOM_VK_ENTER = 14;

    /** Constant for DOM_VK_NUM_LOCK. */
    public static final int DOM_VK_NUM_LOCK = 144;

    /** Constant for DOM_VK_SCROLL_LOCK. */
    public static final int DOM_VK_SCROLL_LOCK = 145;

    /** Constant for DOM_VK_SHIFT. */
    public static final int DOM_VK_SHIFT = 16;

    /** Constant for DOM_VK_CONTROL. */
    public static final int DOM_VK_CONTROL = 17;

    /** Constant for DOM_VK_ALT. */
    public static final int DOM_VK_ALT = 18;

    /** Constant for DOM_VK_COMMA. */
    public static final int DOM_VK_COMMA = 188;

    /** Constant for DOM_VK_PAUSE. */
    public static final int DOM_VK_PAUSE = 19;

    /** Constant for DOM_VK_PERIOD. */
    public static final int DOM_VK_PERIOD = 190;

    /** Constant for DOM_VK_SLASH. */
    public static final int DOM_VK_SLASH = 191;

    /** Constant for DOM_VK_BACK_QUOTE. */
    public static final int DOM_VK_BACK_QUOTE = 192;

    /** Constant for DOM_VK_CAPS_LOCK. */
    public static final int DOM_VK_CAPS_LOCK = 20;

    /** Constant for DOM_VK_OPEN_BRACKET. */
    public static final int DOM_VK_OPEN_BRACKET = 219;

    /** Constant for DOM_VK_BACK_SLASH. */
    public static final int DOM_VK_BACK_SLASH = 220;

    /** Constant for DOM_VK_CLOSE_BRACKET. */
    public static final int DOM_VK_CLOSE_BRACKET = 221;

    /** Constant for DOM_VK_QUOTE. */
    public static final int DOM_VK_QUOTE = 222;

    /** Constant for DOM_VK_META. */
    public static final int DOM_VK_META = 224;

    /** Constant for DOM_VK_ESCAPE. */
    public static final int DOM_VK_ESCAPE = 27;

    /** Constant for DOM_VK_CANCEL. */
    public static final int DOM_VK_CANCEL = 3;

    /** Constant for DOM_VK_SPACE. */
    public static final int DOM_VK_SPACE = 32;

    /** Constant for DOM_VK_PAGE_UP. */
    public static final int DOM_VK_PAGE_UP = 33;

    /** Constant for DOM_VK_PAGE_DOWN. */
    public static final int DOM_VK_PAGE_DOWN = 34;

    /** Constant for DOM_VK_END. */
    public static final int DOM_VK_END = 35;

    /** Constant for DOM_VK_HOME. */
    public static final int DOM_VK_HOME = 36;

    /** Constant for DOM_VK_LEFT. */
    public static final int DOM_VK_LEFT = 37;

    /** Constant for DOM_VK_UP. */
    public static final int DOM_VK_UP = 38;

    /** Constant for DOM_VK_RIGHT. */
    public static final int DOM_VK_RIGHT = 39;

    /** Constant for DOM_VK_DOWN. */
    public static final int DOM_VK_DOWN = 40;

    /** Constant for DOM_VK_PRINTSCREEN. */
    public static final int DOM_VK_PRINTSCREEN = 44;

    /** Constant for DOM_VK_INSERT. */
    public static final int DOM_VK_INSERT = 45;

    /** Constant for DOM_VK_DELETE. */
    public static final int DOM_VK_DELETE = 46;

    /** Constant for DOM_VK_0. */
    public static final int DOM_VK_0 = 48;

    /** Constant for DOM_VK_1. */
    public static final int DOM_VK_1 = 49;

    /** Constant for DOM_VK_2. */
    public static final int DOM_VK_2 = 50;

    /** Constant for DOM_VK_3. */
    public static final int DOM_VK_3 = 51;

    /** Constant for DOM_VK_4. */
    public static final int DOM_VK_4 = 52;

    /** Constant for DOM_VK_5. */
    public static final int DOM_VK_5 = 53;

    /** Constant for DOM_VK_6. */
    public static final int DOM_VK_6 = 54;

    /** Constant for DOM_VK_7. */
    public static final int DOM_VK_7 = 55;

    /** Constant for DOM_VK_8. */
    public static final int DOM_VK_8 = 56;

    /** Constant for DOM_VK_9. */
    public static final int DOM_VK_9 = 57;

    /** Constant for DOM_VK_SEMICOLON. */
    public static final int DOM_VK_SEMICOLON = 59;

    /** Constant for DOM_VK_HELP. */
    public static final int DOM_VK_HELP = 6;

    /** Constant for DOM_VK_EQUALS. */
    public static final int DOM_VK_EQUALS = 61;

    /** Constant for DOM_VK_A. */
    public static final int DOM_VK_A = 65;

    /** Constant for DOM_VK_B. */
    public static final int DOM_VK_B = 66;

    /** Constant for DOM_VK_C. */
    public static final int DOM_VK_C = 67;

    /** Constant for DOM_VK_D. */
    public static final int DOM_VK_D = 68;

    /** Constant for DOM_VK_E. */
    public static final int DOM_VK_E = 69;

    /** Constant for DOM_VK_F. */
    public static final int DOM_VK_F = 70;

    /** Constant for DOM_VK_G. */
    public static final int DOM_VK_G = 71;

    /** Constant for DOM_VK_H. */
    public static final int DOM_VK_H = 72;

    /** Constant for DOM_VK_I. */
    public static final int DOM_VK_I = 73;

    /** Constant for DOM_VK_J. */
    public static final int DOM_VK_J = 74;

    /** Constant for DOM_VK_K. */
    public static final int DOM_VK_K = 75;

    /** Constant for DOM_VK_L. */
    public static final int DOM_VK_L = 76;

    /** Constant for DOM_VK_M. */
    public static final int DOM_VK_M = 77;

    /** Constant for DOM_VK_N. */
    public static final int DOM_VK_N = 78;

    /** Constant for DOM_VK_O. */
    public static final int DOM_VK_O = 79;

    /** Constant for DOM_VK_BACK_SPACE. */
    public static final int DOM_VK_BACK_SPACE = 8;

    /** Constant for DOM_VK_P. */
    public static final int DOM_VK_P = 80;

    /** Constant for DOM_VK_Q. */
    public static final int DOM_VK_Q = 81;

    /** Constant for DOM_VK_R. */
    public static final int DOM_VK_R = 82;

    /** Constant for DOM_VK_S. */
    public static final int DOM_VK_S = 83;

    /** Constant for DOM_VK_T. */
    public static final int DOM_VK_T = 84;

    /** Constant for DOM_VK_U. */
    public static final int DOM_VK_U = 85;

    /** Constant for DOM_VK_V. */
    public static final int DOM_VK_V = 86;

    /** Constant for DOM_VK_W. */
    public static final int DOM_VK_W = 87;

    /** Constant for DOM_VK_X. */
    public static final int DOM_VK_X = 88;

    /** Constant for DOM_VK_Y. */
    public static final int DOM_VK_Y = 89;

    /** Constant for DOM_VK_TAB. */
    public static final int DOM_VK_TAB = 9;

    /** Constant for DOM_VK_Z. */
    public static final int DOM_VK_Z = 90;

    /** Constant for DOM_VK_CONTEXT_MENU. */
    public static final int DOM_VK_CONTEXT_MENU = 93;

    /** Constant for DOM_VK_NUMPAD0. */
    public static final int DOM_VK_NUMPAD0 = 96;

    /** Constant for DOM_VK_NUMPAD1. */
    public static final int DOM_VK_NUMPAD1 = 97;

    /** Constant for DOM_VK_NUMPAD2. */
    public static final int DOM_VK_NUMPAD2 = 98;

    /** Constant for DOM_VK_NUMPAD3. */
    public static final int DOM_VK_NUMPAD3 = 99;

    /** Constant for DOM_VK_NUMPAD4. */
    public static final int DOM_VK_NUMPAD4 = 100;

    /** Constant for DOM_VK_NUMPAD5. */
    public static final int DOM_VK_NUMPAD5 = 101;

    /** Constant for DOM_VK_NUMPAD6. */
    public static final int DOM_VK_NUMPAD6 = 102;

    /** Constant for DOM_VK_NUMPAD7. */
    public static final int DOM_VK_NUMPAD7 = 103;

    /** Constant for DOM_VK_NUMPAD8. */
    public static final int DOM_VK_NUMPAD8 = 104;

    /** Constant for DOM_VK_NUMPAD9. */
    public static final int DOM_VK_NUMPAD9 = 105;

    private int charCode_;

    /**
     * Creates a new keyboard event instance.
     */
    public KeyboardEvent() {
        // Empty.
    }

    /**
     * Creates a new keyboard event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param character the character associated with the event
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     */
    public KeyboardEvent(final DomNode domNode, final String type, final int character,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        super(domNode, type);
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_113)) {
            if (jsxGet_type().equals(Event.TYPE_KEY_PRESS)) {
                setKeyCode(Integer.valueOf(character));
            }
            else {
                setKeyCode(Integer.valueOf(charToKeyCode(character)));
            }
        }
        else {
            if (jsxGet_type().equals(Event.TYPE_KEY_PRESS) && character >= 33 && character <= 126) {
                charCode_ = character;
            }
            else {
                setKeyCode(Integer.valueOf(charToKeyCode(character)));
            }
        }
        setShiftKey(shiftKey);
        setCtrlKey(ctrlKey);
        setAltKey(altKey);
    }

    /**
     * Implementation of the DOM Level 3 Event method for initializing the key event.
     *
     * @param type the event type
     * @param bubbles can the event bubble
     * @param cancelable can the event be canceled
     * @param view the view to use for this event
     * @param ctrlKey is the control key pressed
     * @param altKey is the alt key pressed
     * @param shiftKey is the shift key pressed
     * @param metaKey is the meta key pressed
     * @param keyCode the virtual key code value of the key which was depressed, otherwise zero
     * @param charCode the Unicode character associated with the depressed key otherwise zero
     */
    public void jsxFunction_initKeyEvent(
            final String type,
            final boolean bubbles,
            final boolean cancelable,
            final Object view,
            final boolean ctrlKey,
            final boolean altKey,
            final boolean shiftKey,
            final boolean metaKey,
            final int keyCode,
            final int charCode) {

        jsxFunction_initUIEvent(type, bubbles, cancelable, view, 0);
        setCtrlKey(ctrlKey);
        setAltKey(altKey);
        setShiftKey(shiftKey);
        setKeyCode(Integer.valueOf(keyCode));
        setMetaKey(metaKey);
        charCode_ = 0;
    }

    /**
     * Returns the char code associated with the event.
     * @return the char code associated with the event
     */
    public int jsxGet_charCode() {
        return charCode_;
    }

    /**
     * Returns the numeric keyCode of the key pressed, or the charCode for an alphanumeric key pressed.
     * @return the numeric keyCode of the key pressed, or the charCode for an alphanumeric key pressed
     */
    public Object jsxGet_which() {
        return charCode_ != 0 ? Integer.valueOf(charCode_) : jsxGet_keyCode();
    }

    /**
     * Converts a Java character to a keyCode.
     * @see <a href="http://www.w3.org/TR/DOM-Level-3-Events/#keyset-keyidentifiers">DOM 3 Events</a>
     * @param c the character
     * @return the corresponding keycode
     */
    private static int charToKeyCode(final int c) {
        if (c >= 'a' && c <= 'z') {
            return 'A' + c - 'a';
        }

        switch (c) {
            case '.':
                return DOM_VK_PERIOD;

            case ',':
                return DOM_VK_COMMA;

            case '/':
                return DOM_VK_SLASH;

            default:
                return c;
        }
    }
}
