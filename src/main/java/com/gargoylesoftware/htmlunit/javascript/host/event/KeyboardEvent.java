/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_DISTINGUISH_PRINTABLE_KEY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_KEYBOARD_CTOR_WHICH;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * JavaScript object representing a Keyboard Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3c.org/TR/DOM-Level-3-Events/#Events-KeyboardEvents-Interfaces">
 * DOM Level 3 Events</a>.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author Joerg Werner
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
    @JsxConstant(IE)
    public static final int DOM_KEY_LOCATION_MOBILE = 4;

    /** Constant for {@code DOM_KEY_LOCATION_JOYSTICK}. */
    @JsxConstant(IE)
    public static final int DOM_KEY_LOCATION_JOYSTICK = 5;

    /** Constant for {@code DOM_VK_CANCEL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CANCEL = 3;

    /** Constant for {@code DOM_VK_HELP}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_HELP = 6;

    /** Constant for {@code DOM_VK_TAB}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_TAB = 9;

    /** Constant for {@code DOM_VK_CLEAR}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CLEAR = 12;

    /** Constant for {@code DOM_VK_RETURN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_RETURN = 13;

    /** Constant for {@code DOM_VK_SHIFT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SHIFT = 16;

    /** Constant for {@code DOM_VK_CONTROL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CONTROL = 17;

    /** Constant for {@code DOM_VK_ALT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_ALT = 18;

    /** Constant for {@code DOM_VK_PAUSE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PAUSE = 19;

    /** Constant for {@code DOM_VK_CAPS_LOCK}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CAPS_LOCK = 20;

    /** Constant for {@code DOM_VK_HANGUL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_HANGUL = 21;

    /** Constant for {@code DOM_VK_KANA}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_KANA = 21;

    /** Constant for {@code DOM_VK_EISU}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_EISU = 22;

    /** Constant for {@code DOM_VK_FINAL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_FINAL = 24;

    /** Constant for {@code DOM_VK_JUNJA}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_JUNJA = 23;

    /** Constant for {@code DOM_VK_HANJA}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_HANJA = 25;

    /** Constant for {@code DOM_VK_KANJI}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_KANJI = 25;

    /** Constant for {@code DOM_VK_ESCAPE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_ESCAPE = 27;

    /** Constant for {@code DOM_VK_CONVERT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CONVERT = 28;

    /** Constant for {@code DOM_VK_NONCONVERT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NONCONVERT = 29;

    /** Constant for {@code DOM_VK_ACCEPT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_ACCEPT = 30;

    /** Constant for {@code DOM_VK_MODECHANGE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_MODECHANGE = 31;

    /** Constant for {@code DOM_VK_SPACE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SPACE = 32;

    /** Constant for {@code DOM_VK_PAGE_UP}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PAGE_UP = 33;

    /** Constant for {@code DOM_VK_PAGE_DOWN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PAGE_DOWN = 34;

    /** Constant for {@code DOM_VK_END}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_END = 35;

    /** Constant for {@code DOM_VK_HOME}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_HOME = 36;

    /** Constant for {@code DOM_VK_LEFT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_LEFT = 37;

    /** Constant for {@code DOM_VK_UP}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_UP = 38;

    /** Constant for {@code DOM_VK_RIGHT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_RIGHT = 39;

    /** Constant for {@code DOM_VK_SELECT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SELECT = 41;

    /** Constant for {@code DOM_VK_DOWN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_DOWN = 40;

    /** Constant for {@code DOM_VK_PRINT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PRINT = 42;

    /** Constant for {@code DOM_VK_EXECUTE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_EXECUTE = 43;

    /** Constant for {@code DOM_VK_PRINTSCREEN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PRINTSCREEN = 44;

    /** Constant for {@code DOM_VK_INSERT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_INSERT = 45;

    /** Constant for {@code DOM_VK_DELETE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_DELETE = 46;

    /** Constant for {@code DOM_VK_0}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_0 = 48;

    /** Constant for {@code DOM_VK_1}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_1 = 49;

    /** Constant for {@code DOM_VK_2}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_2 = 50;

    /** Constant for {@code DOM_VK_3}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_3 = 51;

    /** Constant for {@code DOM_VK_4}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_4 = 52;

    /** Constant for {@code DOM_VK_5}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_5 = 53;

    /** Constant for {@code DOM_VK_6}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_6 = 54;

    /** Constant for {@code DOM_VK_7}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_7 = 55;

    /** Constant for {@code DOM_VK_8}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_8 = 56;

    /** Constant for {@code DOM_VK_9}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_9 = 57;

    /** Constant for {@code DOM_VK_COLON}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_COLON = 58;

    /** Constant for {@code DOM_VK_SEMICOLON}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SEMICOLON = 59;

    /** Constant for {@code DOM_VK_LESS_THAN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_LESS_THAN = 60;

    /** Constant for {@code DOM_VK_EQUALS}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_EQUALS = 61;

    /** Constant for {@code DOM_VK_GREATER_THAN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_GREATER_THAN = 62;

    /** Constant for {@code DOM_VK_QUESTION_MARK}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_QUESTION_MARK = 63;

    /** Constant for {@code DOM_VK_AT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_AT = 64;

    /** Constant for {@code DOM_VK_A}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_A = 65;

    /** Constant for {@code DOM_VK_B}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_B = 66;

    /** Constant for {@code DOM_VK_C}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_C = 67;

    /** Constant for {@code DOM_VK_D}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_D = 68;

    /** Constant for {@code DOM_VK_E}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_E = 69;

    /** Constant for {@code DOM_VK_F}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F = 70;

    /** Constant for {@code DOM_VK_G}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_G = 71;

    /** Constant for {@code DOM_VK_H}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_H = 72;

    /** Constant for {@code DOM_VK_I}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_I = 73;

    /** Constant for {@code DOM_VK_J}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_J = 74;

    /** Constant for {@code DOM_VK_K}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_K = 75;

    /** Constant for {@code DOM_VK_L}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_L = 76;

    /** Constant for {@code DOM_VK_M}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_M = 77;

    /** Constant for {@code DOM_VK_N}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_N = 78;

    /** Constant for {@code DOM_VK_O}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_O = 79;

    /** Constant for {@code DOM_VK_BACK_SPACE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_BACK_SPACE = 8;

    /** Constant for {@code DOM_VK_P}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_P = 80;

    /** Constant for {@code DOM_VK_Q}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_Q = 81;

    /** Constant for {@code DOM_VK_R}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_R = 82;

    /** Constant for {@code DOM_VK_S}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_S = 83;

    /** Constant for {@code DOM_VK_T}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_T = 84;

    /** Constant for {@code DOM_VK_U}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_U = 85;

    /** Constant for {@code DOM_VK_V}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_V = 86;

    /** Constant for {@code DOM_VK_W}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_W = 87;

    /** Constant for {@code DOM_VK_X}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_X = 88;

    /** Constant for {@code DOM_VK_Y}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_Y = 89;

    /** Constant for {@code DOM_VK_Z}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_Z = 90;

    /** Constant for {@code DOM_VK_WIN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN = 91;

    /** Constant for {@code DOM_VK_CONTEXT_MENU}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CONTEXT_MENU = 93;

    /** Constant for {@code DOM_VK_SLEEP}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SLEEP = 95;

    /** Constant for {@code DOM_VK_NUMPAD0}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD0 = 96;

    /** Constant for {@code DOM_VK_NUMPAD1}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD1 = 97;

    /** Constant for {@code DOM_VK_NUMPAD2}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD2 = 98;

    /** Constant for {@code DOM_VK_NUMPAD3}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD3 = 99;

    /** Constant for {@code DOM_VK_NUMPAD4}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD4 = 100;

    /** Constant for {@code DOM_VK_NUMPAD5}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD5 = 101;

    /** Constant for {@code DOM_VK_NUMPAD6}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD6 = 102;

    /** Constant for {@code DOM_VK_NUMPAD7}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD7 = 103;

    /** Constant for {@code DOM_VK_NUMPAD8}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD8 = 104;

    /** Constant for {@code DOM_VK_NUMPAD9}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUMPAD9 = 105;

    /** Constant for {@code DOM_VK_MULTIPLY}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_MULTIPLY = 106;

    /** Constant for {@code DOM_VK_ADD}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_ADD = 107;

    /** Constant for {@code DOM_VK_SEPARATOR}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SEPARATOR = 108;

    /** Constant for {@code DOM_VK_SUBTRACT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SUBTRACT = 109;

    /** Constant for {@code DOM_VK_DECIMAL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_DECIMAL = 110;

    /** Constant for {@code DOM_VK_DIVIDE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_DIVIDE = 111;

    /** Constant for {@code DOM_VK_F1}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F1 = 112;

    /** Constant for {@code DOM_VK_F2}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F2 = 113;

    /** Constant for {@code DOM_VK_F3}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F3 = 114;

    /** Constant for {@code DOM_VK_F4}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F4 = 115;

    /** Constant for {@code DOM_VK_F5}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F5 = 116;

    /** Constant for {@code DOM_VK_F6}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F6 = 117;

    /** Constant for {@code DOM_VK_F7}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F7 = 118;

    /** Constant for {@code DOM_VK_F8}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F8 = 119;

    /** Constant for {@code DOM_VK_F9}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F9 = 120;

    /** Constant for {@code DOM_VK_F10}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F10 = 121;

    /** Constant for {@code DOM_VK_F11}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F11 = 122;

    /** Constant for {@code DOM_VK_F12}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F12 = 123;

    /** Constant for {@code DOM_VK_F13}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F13 = 124;

    /** Constant for {@code DOM_VK_F14}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F14 = 125;

    /** Constant for {@code DOM_VK_F15}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F15 = 126;

    /** Constant for {@code DOM_VK_F16}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F16 = 127;

    /** Constant for {@code DOM_VK_F17}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F17 = 128;

    /** Constant for {@code DOM_VK_F18}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F18 = 129;

    /** Constant for {@code DOM_VK_F19}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F19 = 130;

    /** Constant for {@code DOM_VK_F20}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F20 = 131;

    /** Constant for {@code DOM_VK_F21}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F21 = 132;

    /** Constant for {@code DOM_VK_F22}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F22 = 133;

    /** Constant for {@code DOM_VK_F23}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F23 = 134;

    /** Constant for {@code DOM_VK_F24}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_F24 = 135;

    /** Constant for {@code DOM_VK_NUM_LOCK}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_NUM_LOCK = 144;

    /** Constant for {@code DOM_VK_SCROLL_LOCK}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SCROLL_LOCK = 145;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_JISHO}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_FJ_JISHO = 146;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_MASSHOU}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_FJ_MASSHOU = 147;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_TOUROKU}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_FJ_TOUROKU = 148;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_LOYA}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_FJ_LOYA = 149;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_ROYA}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_FJ_ROYA = 150;

    /** Constant for {@code DOM_VK_CIRCUMFLEX}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CIRCUMFLEX = 160;

    /** Constant for {@code DOM_VK_EXCLAMATION}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_EXCLAMATION = 161;

    /** Constant for {@code DOM_VK_DOUBLE_QUOTE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_DOUBLE_QUOTE = 162;

    /** Constant for {@code DOM_VK_HASH}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_HASH = 163;

    /** Constant for {@code DOM_VK_DOLLAR}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_DOLLAR = 164;

    /** Constant for {@code DOM_VK_PERCENT}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PERCENT = 165;

    /** Constant for {@code DOM_VK_AMPERSAND}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_AMPERSAND = 166;

    /** Constant for {@code DOM_VK_UNDERSCORE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_UNDERSCORE = 167;

    /** Constant for {@code DOM_VK_OPEN_PAREN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_OPEN_PAREN = 168;

    /** Constant for {@code DOM_VK_CLOSE_PAREN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CLOSE_PAREN = 169;

    /** Constant for {@code DOM_VK_ASTERISK}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_ASTERISK = 170;

    /** Constant for {@code DOM_VK_PLUS}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PLUS = 171;

    /** Constant for {@code DOM_VK_PIPE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PIPE = 172;

    /** Constant for {@code DOM_VK_HYPHEN_MINUS}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_HYPHEN_MINUS = 173;

    /** Constant for {@code DOM_VK_OPEN_CURLY_BRACKET}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_OPEN_CURLY_BRACKET = 174;

    /** Constant for {@code DOM_VK_CLOSE_CURLY_BRACKET}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CLOSE_CURLY_BRACKET = 175;

    /** Constant for {@code DOM_VK_TILDE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_TILDE = 176;

    /** Constant for {@code DOM_VK_VOLUME_MUTE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_VOLUME_MUTE = 181;

    /** Constant for {@code DOM_VK_VOLUME_DOWN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_VOLUME_DOWN = 182;

    /** Constant for {@code DOM_VK_VOLUME_UP}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_VOLUME_UP = 183;

    /** Constant for {@code DOM_VK_COMMA}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_COMMA = 188;

    /** Constant for {@code DOM_VK_PERIOD}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PERIOD = 190;

    /** Constant for {@code DOM_VK_SLASH}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_SLASH = 191;

    /** Constant for {@code DOM_VK_BACK_QUOTE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_BACK_QUOTE = 192;

    /** Constant for {@code DOM_VK_OPEN_BRACKET}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_OPEN_BRACKET = 219;

    /** Constant for {@code DOM_VK_BACK_SLASH}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_BACK_SLASH = 220;

    /** Constant for {@code DOM_VK_CLOSE_BRACKET}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CLOSE_BRACKET = 221;

    /** Constant for {@code DOM_VK_QUOTE}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_QUOTE = 222;

    /** Constant for {@code DOM_VK_META}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_META = 224;

    /** Constant for {@code DOM_VK_ALTGR}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_ALTGR = 225;

    /** Constant for {@code DOM_VK_WIN_ICO_HELP}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_ICO_HELP = 227;

    /** Constant for {@code DOM_VK_WIN_ICO_00}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_ICO_00 = 228;

    /** Constant for {@code DOM_VK_PROCESSKEY}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PROCESSKEY = 229;

    /** Constant for {@code DOM_VK_WIN_ICO_CLEAR}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_ICO_CLEAR = 230;

    /** Constant for {@code DOM_VK_WIN_OEM_RESET}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_RESET = 233;

    /** Constant for {@code DOM_VK_WIN_OEM_JUMP}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_JUMP = 234;

    /** Constant for {@code DOM_VK_WIN_OEM_PA1}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_PA1 = 235;

    /** Constant for {@code DOM_VK_WIN_OEM_PA2}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_PA2 = 236;

    /** Constant for {@code DOM_VK_WIN_OEM_PA3}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_PA3 = 237;

    /** Constant for {@code DOM_VK_WIN_OEM_WSCTRL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_WSCTRL = 238;

    /** Constant for {@code DOM_VK_WIN_OEM_CUSEL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_CUSEL = 239;

    /** Constant for {@code DOM_VK_WIN_OEM_ATTN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_ATTN = 240;

    /** Constant for {@code DOM_VK_WIN_OEM_FINISH}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_FINISH = 241;

    /** Constant for {@code DOM_VK_WIN_OEM_COPY}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_COPY = 242;

    /** Constant for {@code DOM_VK_WIN_OEM_AUTO}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_AUTO = 243;

    /** Constant for {@code DOM_VK_WIN_OEM_ENLW}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_ENLW = 244;

    /** Constant for {@code DOM_VK_WIN_OEM_BACKTAB}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_BACKTAB = 245;

    /** Constant for {@code DOM_VK_ATTN}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_ATTN = 246;

    /** Constant for {@code DOM_VK_CRSEL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_CRSEL = 247;

    /** Constant for {@code DOM_VK_EXSEL}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_EXSEL = 248;

    /** Constant for {@code DOM_VK_EREOF}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_EREOF = 249;

    /** Constant for {@code DOM_VK_PLAY}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PLAY = 250;

    /** Constant for {@code DOM_VK_ZOOM}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_ZOOM = 251;

    /** Constant for {@code DOM_VK_PA1}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_PA1 = 253;

    /** Constant for {@code DOM_VK_WIN_OEM_CLEAR}. */
    @JsxConstant({FF, FF78})
    public static final int DOM_VK_WIN_OEM_CLEAR = 254;

    /**
     * For {@link #KEYDOWN} and {@link #KEYUP}, this map stores {@link #setKeyCode(int)} associated with
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
        //Chrome/IE 189
        keyCodeMap.put('-', DOM_VK_HYPHEN_MINUS);
        keyCodeMap.put('_', DOM_VK_HYPHEN_MINUS);
        //Chrome/IE 187
        keyCodeMap.put('+', DOM_VK_EQUALS);
        keyCodeMap.put('[', DOM_VK_OPEN_BRACKET);
        keyCodeMap.put('{', DOM_VK_OPEN_BRACKET);
        keyCodeMap.put(']', DOM_VK_CLOSE_BRACKET);
        keyCodeMap.put('}', DOM_VK_CLOSE_BRACKET);
        //Chrome/IE 186
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

    /*
     * Standard properties
     */

    /** The key value of the key represented by the event. */
    private String key_ = "";

    /** The code value of the physical key represented by the event. */
    private String code_ = "";

    /** The location of the key on the keyboard or other input device. See DOM_KEY_LOCATION_* constants. */
    private int location_;

    /** Whether or not the "meta" key was pressed during the firing of the event. */
    private boolean metaKey_;

    /** Whether the key is being held down such that it is automatically repeating. */
    private boolean repeat_;

    /** Whether the event is fired after the compositionstart and before the compositionend events. */
    private boolean isComposing_;

    /*
     * Deprecated properties
     */

    /** The Unicode reference number of the key. */
    private int charCode_;

    /** The unmodified value of the pressed key. This is usually the same as keyCode. */
    private int which_;

    /**
     * Creates a new keyboard event instance.
     */
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
        if (getType().equals(Event.TYPE_KEY_PRESS)) {
            if (getBrowserVersion().hasFeature(JS_EVENT_DISTINGUISH_PRINTABLE_KEY)) {
                if (character < 32 || character > 126) {
                    keyCode = Integer.valueOf(charToKeyCode(character));
                }
            }
            else {
                keyCode = Integer.valueOf(character);
            }
        }
        else {
            keyCode = Integer.valueOf(charToKeyCode(character));
        }
        setKeyCode(keyCode);
        if (getType().equals(Event.TYPE_KEY_PRESS) && (character >= 32 && character <= 126
                    || !getBrowserVersion().hasFeature(JS_EVENT_DISTINGUISH_PRINTABLE_KEY))) {
            charCode_ = character;
        }
        which_ = charCode_ == 0 ? keyCode : Integer.valueOf(charCode_);

        key_ = determineKey();
        code_ = determineCode();
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

        key_ = determineKey();
        code_ = determineCode();
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

    /** We can not accept DOM_VK_A, because is it 'A' or 'a', so the character constructor should be used. */
    private static boolean isAmbiguousKeyCode(final int keyCode) {
        return (keyCode >= DOM_VK_0 && keyCode <= DOM_VK_9) || (keyCode >= DOM_VK_A && keyCode <= DOM_VK_Z);
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
     * Determines the value of the 'key' property from the current value of 'keyCode', 'charCode', or 'which'.
     * @return the key value
     */
    private String determineKey() {
        int code = getKeyCode();
        if (code == 0) {
            code = getCharCode();
        }
        switch (code) {
            case DOM_VK_SHIFT:
                return "Shift";
            case DOM_VK_PERIOD:
                return ".";
            case DOM_VK_RETURN:
                return "Enter";

            default:
                return String.valueOf(isShiftKey() ? (char) which_ : Character.toLowerCase((char) which_));
        }
    }

    /**
     * Determines the value of the 'code' property from the current value of 'keyCode', 'charCode', or 'which'.
     * @return the code value
     */
    private String determineCode() {
        int code = getKeyCode();
        if (code == 0) {
            code = getCharCode();
        }
        switch (code) {
            case DOM_VK_SHIFT:
                return "ShiftLeft";
            case DOM_VK_PERIOD:
            case '.':
                return "Period";
            case DOM_VK_RETURN:
                return "Enter";

            default:
                return "Key" + Character.toUpperCase((char) which_);
        }
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    @Override
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !Undefined.isUndefined(details)) {

            final Object key = details.get("key", details);
            if (!isMissingOrUndefined(key)) {
                setKey(ScriptRuntime.toString(key));
            }

            final Object code = details.get("code", details);
            if (!isMissingOrUndefined(code)) {
                setCode(ScriptRuntime.toString(code));
            }

            final Object location = details.get("location", details);
            if (!isMissingOrUndefined(location)) {
                setLocation(ScriptRuntime.toInt32(location));
            }

            final Object ctrlKey = details.get("ctrlKey", details);
            if (!isMissingOrUndefined(ctrlKey)) {
                setCtrlKey(ScriptRuntime.toBoolean(ctrlKey));
            }

            final Object shiftKey = details.get("shiftKey", details);
            if (!isMissingOrUndefined(shiftKey)) {
                setShiftKey(ScriptRuntime.toBoolean(shiftKey));
            }

            final Object altKey = details.get("altKey", details);
            if (!isMissingOrUndefined(altKey)) {
                setAltKey(ScriptRuntime.toBoolean(altKey));
            }

            final Object metaKey = details.get("metaKey", details);
            if (!isMissingOrUndefined(metaKey)) {
                setMetaKey(ScriptRuntime.toBoolean(metaKey));
            }

            final Object repeat = details.get("repeat", details);
            if (!isMissingOrUndefined(repeat)) {
                setRepeat(ScriptRuntime.toBoolean(repeat));
            }

            final Object isComposing = details.get("isComposing", details);
            if (!isMissingOrUndefined(isComposing)) {
                setIsComposing(ScriptRuntime.toBoolean(isComposing));
            }

            final Object charCode = details.get("charCode", details);
            if (!isMissingOrUndefined(charCode)) {
                setCharCode(ScriptRuntime.toInt32(charCode));
            }

            final Object keyCode = details.get("keyCode", details);
            if (!isMissingOrUndefined(keyCode)) {
                setKeyCode(ScriptRuntime.toInt32(keyCode));
            }

            if (getBrowserVersion().hasFeature(JS_EVENT_KEYBOARD_CTOR_WHICH)) {
                final Object which = details.get("which", details);
                if (!isMissingOrUndefined(which)) {
                    setWhich(ScriptRuntime.toInt32(which));
                }
            }
        }
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
    @JsxFunction({FF, FF78})
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
        setKeyCode(keyCode);
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
     * Sets the char code associated with the event.
     * @param charCode the char code associated with the event
     */
    protected void setCharCode(final int charCode) {
        charCode_ = charCode;
    }

    /**
     * Returns the numeric keyCode of the key pressed, or the charCode for an alphanumeric key pressed.
     * @return the numeric keyCode of the key pressed, or the charCode for an alphanumeric key pressed
     */
    @JsxGetter
    public int getWhich() {
        return which_;
    }

    /**
     * Sets the numeric keyCode of the key pressed, or the charCode for an alphanumeric key pressed.
     * @param which the numeric keyCode of the key pressed, or the charCode for an alphanumeric key pressed
     */
    protected void setWhich(final int which) {
        which_ = which;
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public int getKeyCode() {
        return super.getKeyCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public boolean isShiftKey() {
        return super.isShiftKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public boolean isCtrlKey() {
        return super.isCtrlKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public boolean isAltKey() {
        return super.isAltKey();
    }

    /**
     * Returns the value of a key or keys pressed by the user.
     * @return the value of a key or keys pressed by the user
     */
    @JsxGetter
    public String getKey() {
        return key_;
    }

    /**
     * Sets the value of a key or keys pressed by the user.
     * @param key the value of a key or keys pressed by the user
     */
    protected void setKey(final String key) {
        key_ = key;
    }

    /**
     * Returns the value of a key or keys pressed by the user.
     * @return the value of a key or keys pressed by the user
     */
    @JsxGetter(IE)
    public String getChar() {
        int code = getKeyCode();
        if (code == 0) {
            code = getCharCode();
        }
        switch (code) {
            case DOM_VK_SHIFT:
                return "";
            case DOM_VK_RETURN:
                return "\n";
            case DOM_VK_PERIOD:
                return ".";

            default:
                return String.valueOf(isShiftKey() ? (char) which_ : Character.toLowerCase((char) which_));
        }
    }

    /**
     * Returns a physical key on the keyboard.
     * @return a physical key on the keyboard
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public String getCode() {
        return code_;
    }

    /**
     * Sets a physical key on the keyboard.
     * @param code a physical key on the keyboard
     */
    protected void setCode(final String code) {
        code_ = code;
    }

    /**
     * Returns whether or not the "meta" key was pressed during the event firing.
     * @return whether or not the "meta" key was pressed during the event firing
     */
    @JsxGetter
    public boolean getMetaKey() {
        return metaKey_;
    }

    /**
     * Sets whether or not the "meta" key was pressed during the event firing.
     * @param metaKey whether or not the "meta" was pressed during the event firing
     */
    protected void setMetaKey(final boolean metaKey) {
        metaKey_ = metaKey;
    }

    /**
     * Returns the location of the key on the keyboard.
     * @return the location of the key on the keyboard
     */
    @JsxGetter
    public int getLocation() {
        return location_;
    }

    /**
     * Sets the location of the key on the keyboard.
     * @param location the location of the key on the keyboard
     */
    protected void setLocation(final int location) {
        location_ = location;
    }

    /**
     * Returns whether or not the key is being held down such that it is automatically repeating.
     * @return whether or not the key is being held down
     */
    @JsxGetter
    public boolean isRepeat() {
        return repeat_;
    }

    /**
     * Sets whether or not the key is being held down such that it is automatically repeating.
     * @param repeat whether or not the key is being held down
     */
    protected void setRepeat(final boolean repeat) {
        repeat_ = repeat;
    }

    /**
     * Returns whether or not the event is fired after the compositionstart and before the compositionend events.
     * @return whether or not the event is fired while composing
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public boolean getIsComposing() {
        return isComposing_;
    }

    /**
     * Sets whether or not this event is fired after the compositionstart and before the compositionend events.
     * @param isComposing whether or not this event is fired while composing
     */
    protected void setIsComposing(final boolean isComposing) {
        isComposing_ = isComposing;
    }
}
