/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_DISTINGUISH_PRINTABLE_KEY;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * JavaScript object representing a Keyboard Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3c.org/TR/DOM-Level-3-Events/#Events-KeyboardEvents-Interfaces">
 * DOM Level 3 Events</a>.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass
public class KeyboardEvent extends UIEvent {

    /** Constant for {@code DOM_KEY_LOCATION_STANDARD}. */
    @JsxConstant
    public static final int DOM_KEY_LOCATION_STANDARD = 0;

    /** Constant for {@code DOM_KEY_LOCATION_LEFT}. */
    @JsxConstant
    public static final int DOM_KEY_LOCATION_LEFT = 1;

    /** Constant for {@code DOM_KEY_LOCATION_RIGHT}. */
    @JsxConstant
    public static final int DOM_KEY_LOCATION_RIGHT = 2;

    /** Constant for {@code DOM_KEY_LOCATION_NUMPAD}. */
    @JsxConstant
    public static final int DOM_KEY_LOCATION_NUMPAD = 3;

    /** Constant for {@code DOM_KEY_LOCATION_MOBILE}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int DOM_KEY_LOCATION_MOBILE = 4;

    /** Constant for {@code DOM_KEY_LOCATION_JOYSTICK}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int DOM_KEY_LOCATION_JOYSTICK = 5;

    /** Constant for DOM_VK_CANCEL. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CANCEL = 3;

    /** Constant for DOM_VK_HELP. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_HELP = 6;

    /** Constant for DOM_VK_TAB. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_TAB = 9;

    /** Constant for DOM_VK_CLEAR. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CLEAR = 12;

    /** Constant for DOM_VK_RETURN. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_RETURN = 13;

    /** Constant for DOM_VK_SHIFT. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SHIFT = 16;

    /** Constant for DOM_VK_CONTROL. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CONTROL = 17;

    /** Constant for DOM_VK_ALT. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_ALT = 18;

    /** Constant for DOM_VK_PAUSE. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PAUSE = 19;

    /** Constant for DOM_VK_CAPS_LOCK. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CAPS_LOCK = 20;

    /** Constant for {@code DOM_VK_HANGUL}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_HANGUL = 21;

    /** Constant for {@code DOM_VK_KANA}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_KANA = 21;

    /** Constant for {@code DOM_VK_EISU}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_EISU = 22;

    /** Constant for {@code DOM_VK_FINAL}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_FINAL = 24;

    /** Constant for {@code DOM_VK_JUNJA}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_JUNJA = 23;

    /** Constant for {@code DOM_VK_HANJA}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_HANJA = 25;

    /** Constant for {@code DOM_VK_KANJI}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_KANJI = 25;

    /** Constant for DOM_VK_ESCAPE. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_ESCAPE = 27;

    /** Constant for {@code DOM_VK_CONVERT}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CONVERT = 28;

    /** Constant for {@code DOM_VK_NONCONVERT}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NONCONVERT = 29;

    /** Constant for {@code DOM_VK_ACCEPT}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_ACCEPT = 30;

    /** Constant for {@code DOM_VK_MODECHANGE}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_MODECHANGE = 31;

    /** Constant for DOM_VK_SPACE. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SPACE = 32;

    /** Constant for DOM_VK_PAGE_UP. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PAGE_UP = 33;

    /** Constant for DOM_VK_PAGE_DOWN. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PAGE_DOWN = 34;

    /** Constant for DOM_VK_END. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_END = 35;

    /** Constant for DOM_VK_HOME. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_HOME = 36;

    /** Constant for DOM_VK_LEFT. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_LEFT = 37;

    /** Constant for DOM_VK_UP. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_UP = 38;

    /** Constant for DOM_VK_RIGHT. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_RIGHT = 39;

    /** Constant for {@code DOM_VK_SELECT}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SELECT = 41;

    /** Constant for DOM_VK_DOWN. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_DOWN = 40;

    /** Constant for {@code DOM_VK_PRINT}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PRINT = 42;

    /** Constant for {@code DOM_VK_EXECUTE}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_EXECUTE = 43;

    /** Constant for DOM_VK_PRINTSCREEN. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PRINTSCREEN = 44;

    /** Constant for DOM_VK_INSERT. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_INSERT = 45;

    /** Constant for DOM_VK_DELETE. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_DELETE = 46;

    /** Constant for DOM_VK_0. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_0 = 48;

    /** Constant for DOM_VK_1. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_1 = 49;

    /** Constant for DOM_VK_2. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_2 = 50;

    /** Constant for DOM_VK_3. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_3 = 51;

    /** Constant for DOM_VK_4. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_4 = 52;

    /** Constant for DOM_VK_5. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_5 = 53;

    /** Constant for DOM_VK_6. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_6 = 54;

    /** Constant for DOM_VK_7. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_7 = 55;

    /** Constant for DOM_VK_8. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_8 = 56;

    /** Constant for DOM_VK_9. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_9 = 57;

    /** Constant for {@code DOM_VK_COLON}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_COLON = 58;

    /** Constant for DOM_VK_SEMICOLON. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SEMICOLON = 59;

    /** Constant for {@code DOM_VK_LESS_THAN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_LESS_THAN = 60;

    /** Constant for {@code DOM_VK_EQUALS}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_EQUALS = 61;

    /** Constant for {@code DOM_VK_GREATER_THAN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_GREATER_THAN = 62;

    /** Constant for {@code DOM_VK_QUESTION_MARK}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_QUESTION_MARK = 63;

    /** Constant for {@code DOM_VK_AT}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_AT = 64;

    /** Constant for DOM_VK_A. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_A = 65;

    /** Constant for DOM_VK_B. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_B = 66;

    /** Constant for DOM_VK_C. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_C = 67;

    /** Constant for DOM_VK_D. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_D = 68;

    /** Constant for DOM_VK_E. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_E = 69;

    /** Constant for DOM_VK_F. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F = 70;

    /** Constant for DOM_VK_G. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_G = 71;

    /** Constant for DOM_VK_H. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_H = 72;

    /** Constant for DOM_VK_I. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_I = 73;

    /** Constant for DOM_VK_J. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_J = 74;

    /** Constant for DOM_VK_K. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_K = 75;

    /** Constant for DOM_VK_L. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_L = 76;

    /** Constant for DOM_VK_M. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_M = 77;

    /** Constant for DOM_VK_N. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_N = 78;

    /** Constant for DOM_VK_O. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_O = 79;

    /** Constant for DOM_VK_BACK_SPACE. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_BACK_SPACE = 8;

    /** Constant for DOM_VK_P. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_P = 80;

    /** Constant for DOM_VK_Q. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_Q = 81;

    /** Constant for DOM_VK_R. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_R = 82;

    /** Constant for DOM_VK_S. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_S = 83;

    /** Constant for DOM_VK_T. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_T = 84;

    /** Constant for DOM_VK_U. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_U = 85;

    /** Constant for DOM_VK_V. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_V = 86;

    /** Constant for DOM_VK_W. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_W = 87;

    /** Constant for DOM_VK_X. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_X = 88;

    /** Constant for DOM_VK_Y. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_Y = 89;

    /** Constant for DOM_VK_Z. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_Z = 90;

    /** Constant for {@code DOM_VK_WIN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN = 91;

    /** Constant for DOM_VK_CONTEXT_MENU. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CONTEXT_MENU = 93;

    /** Constant for {@code DOM_VK_SLEEP}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SLEEP = 95;

    /** Constant for DOM_VK_NUMPAD0. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD0 = 96;

    /** Constant for DOM_VK_NUMPAD1. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD1 = 97;

    /** Constant for DOM_VK_NUMPAD2. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD2 = 98;

    /** Constant for DOM_VK_NUMPAD3. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD3 = 99;

    /** Constant for DOM_VK_NUMPAD4. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD4 = 100;

    /** Constant for DOM_VK_NUMPAD5. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD5 = 101;

    /** Constant for DOM_VK_NUMPAD6. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD6 = 102;

    /** Constant for DOM_VK_NUMPAD7. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD7 = 103;

    /** Constant for DOM_VK_NUMPAD8. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD8 = 104;

    /** Constant for DOM_VK_NUMPAD9. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUMPAD9 = 105;

    /** Constant for DOM_VK_MULTIPLY. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_MULTIPLY = 106;

    /** Constant for DOM_VK_ADD. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_ADD = 107;

    /** Constant for DOM_VK_SEPARATOR. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SEPARATOR = 108;

    /** Constant for DOM_VK_SUBTRACT. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SUBTRACT = 109;

    /** Constant for DOM_VK_DECIMAL. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_DECIMAL = 110;

    /** Constant for DOM_VK_DIVIDE. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_DIVIDE = 111;

    /** Constant for DOM_VK_F1. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F1 = 112;

    /** Constant for DOM_VK_F2. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F2 = 113;

    /** Constant for DOM_VK_F3. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F3 = 114;

    /** Constant for DOM_VK_F4. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F4 = 115;

    /** Constant for DOM_VK_F5. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F5 = 116;

    /** Constant for DOM_VK_F6. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F6 = 117;

    /** Constant for DOM_VK_F7. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F7 = 118;

    /** Constant for DOM_VK_F8. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F8 = 119;

    /** Constant for DOM_VK_F9. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F9 = 120;

    /** Constant for DOM_VK_F10. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F10 = 121;

    /** Constant for DOM_VK_F11. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F11 = 122;

    /** Constant for DOM_VK_F12. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F12 = 123;

    /** Constant for DOM_VK_F13. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F13 = 124;

    /** Constant for DOM_VK_F14. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F14 = 125;

    /** Constant for DOM_VK_F15. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F15 = 126;

    /** Constant for DOM_VK_F16. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F16 = 127;

    /** Constant for DOM_VK_F17. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F17 = 128;

    /** Constant for DOM_VK_F18. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F18 = 129;

    /** Constant for DOM_VK_F19. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F19 = 130;

    /** Constant for DOM_VK_F20. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F20 = 131;

    /** Constant for DOM_VK_F21. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F21 = 132;

    /** Constant for DOM_VK_F22. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F22 = 133;

    /** Constant for DOM_VK_F23. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F23 = 134;

    /** Constant for DOM_VK_F24. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_F24 = 135;

    /** Constant for DOM_VK_NUM_LOCK. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_NUM_LOCK = 144;

    /** Constant for DOM_VK_SCROLL_LOCK. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SCROLL_LOCK = 145;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_JISHO}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_FJ_JISHO = 146;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_MASSHOU}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_FJ_MASSHOU = 147;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_TOUROKU}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_FJ_TOUROKU = 148;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_LOYA}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_FJ_LOYA = 149;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_ROYA}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_FJ_ROYA = 150;

    /** Constant for {@code DOM_VK_CIRCUMFLEX}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CIRCUMFLEX = 160;

    /** Constant for {@code DOM_VK_EXCLAMATION}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_EXCLAMATION = 161;

    /** Constant for {@code DOM_VK_DOUBLE_QUOTE}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_DOUBLE_QUOTE = 162;

    /** Constant for {@code DOM_VK_HASH}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_HASH = 163;

    /** Constant for {@code DOM_VK_DOLLAR}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_DOLLAR = 164;

    /** Constant for {@code DOM_VK_PERCENT}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PERCENT = 165;

    /** Constant for {@code DOM_VK_AMPERSAND}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_AMPERSAND = 166;

    /** Constant for {@code DOM_VK_UNDERSCORE}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_UNDERSCORE = 167;

    /** Constant for {@code DOM_VK_OPEN_PAREN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_OPEN_PAREN = 168;

    /** Constant for {@code DOM_VK_CLOSE_PAREN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CLOSE_PAREN = 169;

    /** Constant for {@code DOM_VK_ASTERISK}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_ASTERISK = 170;

    /** Constant for {@code DOM_VK_PLUS}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PLUS = 171;

    /** Constant for {@code DOM_VK_PIPE}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PIPE = 172;

    /** Constant for {@code DOM_VK_HYPHEN_MINUS}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_HYPHEN_MINUS = 173;

    /** Constant for {@code DOM_VK_OPEN_CURLY_BRACKET}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_OPEN_CURLY_BRACKET = 174;

    /** Constant for {@code DOM_VK_CLOSE_CURLY_BRACKET}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CLOSE_CURLY_BRACKET = 175;

    /** Constant for {@code DOM_VK_TILDE}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_TILDE = 176;

    /** Constant for {@code DOM_VK_VOLUME_MUTE}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_VOLUME_MUTE = 181;

    /** Constant for {@code DOM_VK_VOLUME_DOWN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_VOLUME_DOWN = 182;

    /** Constant for {@code DOM_VK_VOLUME_UP}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_VOLUME_UP = 183;

    /** Constant for DOM_VK_COMMA. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_COMMA = 188;

    /** Constant for DOM_VK_PERIOD. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PERIOD = 190;

    /** Constant for DOM_VK_SLASH. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_SLASH = 191;

    /** Constant for DOM_VK_BACK_QUOTE. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_BACK_QUOTE = 192;

    /** Constant for DOM_VK_OPEN_BRACKET. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_OPEN_BRACKET = 219;

    /** Constant for DOM_VK_BACK_SLASH. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_BACK_SLASH = 220;

    /** Constant for DOM_VK_CLOSE_BRACKET. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CLOSE_BRACKET = 221;

    /** Constant for DOM_VK_QUOTE. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_QUOTE = 222;

    /** Constant for DOM_VK_META. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_META = 224;

    /** Constant for {@code DOM_VK_ALTGR}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_ALTGR = 225;

    /** Constant for {@code DOM_VK_WIN_ICO_HELP}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_ICO_HELP = 227;

    /** Constant for {@code DOM_VK_WIN_ICO_00}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_ICO_00 = 228;

    /** Constant for {@code DOM_VK_WIN_ICO_CLEAR}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_ICO_CLEAR = 230;

    /** Constant for {@code DOM_VK_WIN_OEM_RESET}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_RESET = 233;

    /** Constant for {@code DOM_VK_WIN_OEM_JUMP}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_JUMP = 234;

    /** Constant for {@code DOM_VK_WIN_OEM_PA1}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_PA1 = 235;

    /** Constant for {@code DOM_VK_WIN_OEM_PA2}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_PA2 = 236;

    /** Constant for {@code DOM_VK_WIN_OEM_PA3}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_PA3 = 237;

    /** Constant for {@code DOM_VK_WIN_OEM_WSCTRL}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_WSCTRL = 238;

    /** Constant for {@code DOM_VK_WIN_OEM_CUSEL}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_CUSEL = 239;

    /** Constant for {@code DOM_VK_WIN_OEM_ATTN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_ATTN = 240;

    /** Constant for {@code DOM_VK_WIN_OEM_FINISH}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_FINISH = 241;

    /** Constant for {@code DOM_VK_WIN_OEM_COPY}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_COPY = 242;

    /** Constant for {@code DOM_VK_WIN_OEM_AUTO}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_AUTO = 243;

    /** Constant for {@code DOM_VK_WIN_OEM_ENLW}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_ENLW = 244;

    /** Constant for {@code DOM_VK_WIN_OEM_BACKTAB}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_BACKTAB = 245;

    /** Constant for {@code DOM_VK_ATTN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_ATTN = 246;

    /** Constant for {@code DOM_VK_CRSEL}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_CRSEL = 247;

    /** Constant for {@code DOM_VK_EXSEL}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_EXSEL = 248;

    /** Constant for {@code DOM_VK_EREOF}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_EREOF = 249;

    /** Constant for {@code DOM_VK_PLAY}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PLAY = 250;

    /** Constant for {@code DOM_VK_ZOOM}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_ZOOM = 251;

    /** Constant for {@code DOM_VK_PA1}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_PA1 = 253;

    /** Constant for {@code DOM_VK_WIN_OEM_CLEAR}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DOM_VK_WIN_OEM_CLEAR = 254;

    /**
     * For {@link #KEYDOWN} and {@link #KEYUP}, this map stores {@link #setKeyCode(Object)} associated with
     * the character (if they are not the same).
     * You can verify this <a href="http://www.asquare.net/javascript/tests/KeyCode.html">here</a>
     */
    private static final Map<Character, Integer> keyCodeMap = new HashMap<>();
    static {
        keyCodeMap.put('`', DOM_VK_BACK_QUOTE);
        keyCodeMap.put('~', DOM_VK_BACK_QUOTE);
        keyCodeMap.put('!', DOM_VK_1);
        keyCodeMap.put('@', DOM_VK_2);
        keyCodeMap.put('#', DOM_VK_3);
        keyCodeMap.put('$', DOM_VK_4);
        keyCodeMap.put('%', DOM_VK_5);
        keyCodeMap.put('^', DOM_VK_6);
        keyCodeMap.put('&', DOM_VK_7);
        keyCodeMap.put('*', DOM_VK_8);
        keyCodeMap.put('(', DOM_VK_9);
        keyCodeMap.put(')', DOM_VK_0);
        //Chrome/IE11 189
        keyCodeMap.put('-', DOM_VK_HYPHEN_MINUS);
        keyCodeMap.put('_', DOM_VK_HYPHEN_MINUS);
        //Chrome/IE11 187
        keyCodeMap.put('+', DOM_VK_EQUALS);
        keyCodeMap.put('[', DOM_VK_OPEN_BRACKET);
        keyCodeMap.put('{', DOM_VK_OPEN_BRACKET);
        keyCodeMap.put(']', DOM_VK_CLOSE_BRACKET);
        keyCodeMap.put('}', DOM_VK_CLOSE_BRACKET);
        //Chrome/IE11 186
        keyCodeMap.put(':', DOM_VK_SEMICOLON);
        keyCodeMap.put('\'', DOM_VK_QUOTE);
        keyCodeMap.put('"', DOM_VK_QUOTE);
        keyCodeMap.put(',', DOM_VK_COMMA);
        keyCodeMap.put('<', DOM_VK_COMMA);
        keyCodeMap.put('.', DOM_VK_PERIOD);
        keyCodeMap.put('>', DOM_VK_PERIOD);
        keyCodeMap.put('/', DOM_VK_SLASH);
        keyCodeMap.put('?', DOM_VK_SLASH);
        keyCodeMap.put('\\', DOM_VK_BACK_SLASH);
        keyCodeMap.put('|', DOM_VK_BACK_SLASH);
    }

    /**
     * Returns whether the specified character can be written only when {@code SHIFT} key is pressed.
     * @param ch the character
     * @param shiftKey is shift key pressed
     * @return whether the specified character can be written only when {@code SHIFT} key is pressed
     */
    public static boolean isShiftNeeded(final char ch, final boolean shiftKey) {
        return "~!@#$%^&*()_+{}:\"<>?|".indexOf(ch) != -1
                || (!shiftKey && ch >= 'A' && ch <= 'Z');
    }

    private int charCode_;
    private int which_;

    /**
     * Creates a new keyboard event instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public KeyboardEvent() {
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
    public KeyboardEvent(final DomNode domNode, final String type, final char character,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        super(domNode, type);

        setShiftKey(shiftKey);
        setCtrlKey(ctrlKey);
        setAltKey(altKey);

        if ('\n' == character) {
            setKeyCode(DOM_VK_RETURN);
            if (!getBrowserVersion().hasFeature(JS_EVENT_DISTINGUISH_PRINTABLE_KEY)) {
                charCode_ = DOM_VK_RETURN;
            }
            which_ = DOM_VK_RETURN;
            return;
        }

        int keyCode = 0;
        if (!getType().equals(Event.TYPE_KEY_PRESS)) {
            keyCode = Integer.valueOf(charToKeyCode(character));
        }
        else {
            if (getBrowserVersion().hasFeature(JS_EVENT_DISTINGUISH_PRINTABLE_KEY)) {
                if (character < 32 || character > 126) {
                    keyCode = Integer.valueOf(charToKeyCode(character));
                }
            }
            else {
                keyCode = Integer.valueOf(character);
            }
        }
        setKeyCode(keyCode);
        if (getType().equals(Event.TYPE_KEY_PRESS)) {
            if ((character >= 32 && character <= 126)
                    || !getBrowserVersion().hasFeature(JS_EVENT_DISTINGUISH_PRINTABLE_KEY)) {
                charCode_ = character;
            }
        }
        which_ = charCode_ != 0 ? Integer.valueOf(charCode_) : keyCode;
    }

    /**
     * Creates a new keyboard event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param keyCode the key code associated with the event
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     */
    public KeyboardEvent(final DomNode domNode, final String type, final int keyCode,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        super(domNode, type);

        if (isAmbiguousKeyCode(keyCode)) {
            throw new IllegalArgumentException("Please use the 'char' constructor instead of int");
        }
        setKeyCode(keyCode);
        if (getType().equals(Event.TYPE_KEY_PRESS)) {
            which_ = 0;
        }
        else {
            which_ = keyCode;
        }
        setShiftKey(shiftKey);
        setCtrlKey(ctrlKey);
        setAltKey(altKey);
    }

    /** We can not accept DOM_VK_A, because is it 'A' or 'a', so the character constructor should be used. */
    private static boolean isAmbiguousKeyCode(final int keyCode) {
        return (keyCode >= DOM_VK_0 && keyCode <= DOM_VK_9) || (keyCode >= DOM_VK_A && keyCode <= DOM_VK_Z);
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
    @JsxFunction(@WebBrowser(FF))
    public void initKeyEvent(
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

        initUIEvent(type, bubbles, cancelable, view, 0);
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
    @JsxGetter
    public int getCharCode() {
        return charCode_;
    }

    /**
     * Returns the numeric keyCode of the key pressed, or the charCode for an alphanumeric key pressed.
     * @return the numeric keyCode of the key pressed, or the charCode for an alphanumeric key pressed
     */
    @JsxGetter
    public Object getWhich() {
        return which_;
    }

    /**
     * Converts a Java character to a keyCode.
     * @see <a href="http://www.w3.org/TR/DOM-Level-3-Events/#keyset-keyidentifiers">DOM 3 Events</a>
     * @param c the character
     * @return the corresponding keycode
     */
    private static int charToKeyCode(final char c) {
        if (c >= 'a' && c <= 'z') {
            return 'A' + c - 'a';
        }

        final Integer i = keyCodeMap.get(c);
        if (i != null) {
            return i;
        }
        return c;
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public Object getKeyCode() {
        return super.getKeyCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public boolean getShiftKey() {
        return super.getShiftKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public boolean getCtrlKey() {
        return super.getCtrlKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public boolean getAltKey() {
        return super.getAltKey();
    }
}
