/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Attribute;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * JavaScript object representing a Keyboard Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3c.org/TR/DOM-Level-3-Events/#Events-KeyboardEvents-Interfaces">
 * DOM Level 3 Events</a>.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@ScriptClass
public class KeyboardEvent2 extends Event2 {

    /** Constant for {@code DOM_KEY_LOCATION_STANDARD}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int DOM_KEY_LOCATION_STANDARD = 0;

    /** Constant for {@code DOM_KEY_LOCATION_LEFT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int DOM_KEY_LOCATION_LEFT = 1;

    /** Constant for {@code DOM_KEY_LOCATION_RIGHT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int DOM_KEY_LOCATION_RIGHT = 2;

    /** Constant for {@code DOM_KEY_LOCATION_NUMPAD}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int DOM_KEY_LOCATION_NUMPAD = 3;

    /** Constant for {@code DOM_KEY_LOCATION_MOBILE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = IE)
    public static final int DOM_KEY_LOCATION_MOBILE = 4;

    /** Constant for {@code DOM_KEY_LOCATION_JOYSTICK}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = IE)
    public static final int DOM_KEY_LOCATION_JOYSTICK = 5;

    /** Constant for {@code DOM_VK_CANCEL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CANCEL = 3;

    /** Constant for {@code DOM_VK_HELP}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_HELP = 6;

    /** Constant for {@code DOM_VK_TAB}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_TAB = 9;

    /** Constant for {@code DOM_VK_CLEAR}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CLEAR = 12;

    /** Constant for {@code DOM_VK_RETURN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_RETURN = 13;

    /** Constant for {@code DOM_VK_SHIFT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SHIFT = 16;

    /** Constant for {@code DOM_VK_CONTROL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CONTROL = 17;

    /** Constant for {@code DOM_VK_ALT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_ALT = 18;

    /** Constant for {@code DOM_VK_PAUSE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PAUSE = 19;

    /** Constant for {@code DOM_VK_CAPS_LOCK}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CAPS_LOCK = 20;

    /** Constant for {@code DOM_VK_HANGUL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_HANGUL = 21;

    /** Constant for {@code DOM_VK_KANA}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_KANA = 21;

    /** Constant for {@code DOM_VK_EISU}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_EISU = 22;

    /** Constant for {@code DOM_VK_FINAL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_FINAL = 24;

    /** Constant for {@code DOM_VK_JUNJA}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_JUNJA = 23;

    /** Constant for {@code DOM_VK_HANJA}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_HANJA = 25;

    /** Constant for {@code DOM_VK_KANJI}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_KANJI = 25;

    /** Constant for {@code DOM_VK_ESCAPE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_ESCAPE = 27;

    /** Constant for {@code DOM_VK_CONVERT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CONVERT = 28;

    /** Constant for {@code DOM_VK_NONCONVERT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NONCONVERT = 29;

    /** Constant for {@code DOM_VK_ACCEPT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_ACCEPT = 30;

    /** Constant for {@code DOM_VK_MODECHANGE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_MODECHANGE = 31;

    /** Constant for {@code DOM_VK_SPACE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SPACE = 32;

    /** Constant for {@code DOM_VK_PAGE_UP}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PAGE_UP = 33;

    /** Constant for {@code DOM_VK_PAGE_DOWN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PAGE_DOWN = 34;

    /** Constant for {@code DOM_VK_END}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_END = 35;

    /** Constant for {@code DOM_VK_HOME}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_HOME = 36;

    /** Constant for {@code DOM_VK_LEFT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_LEFT = 37;

    /** Constant for {@code DOM_VK_UP}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_UP = 38;

    /** Constant for {@code DOM_VK_RIGHT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_RIGHT = 39;

    /** Constant for {@code DOM_VK_SELECT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SELECT = 41;

    /** Constant for {@code DOM_VK_DOWN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_DOWN = 40;

    /** Constant for {@code DOM_VK_PRINT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PRINT = 42;

    /** Constant for {@code DOM_VK_EXECUTE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_EXECUTE = 43;

    /** Constant for {@code DOM_VK_PRINTSCREEN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PRINTSCREEN = 44;

    /** Constant for {@code DOM_VK_INSERT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_INSERT = 45;

    /** Constant for {@code DOM_VK_DELETE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_DELETE = 46;

    /** Constant for {@code DOM_VK_0}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_0 = 48;

    /** Constant for {@code DOM_VK_1}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_1 = 49;

    /** Constant for {@code DOM_VK_2}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_2 = 50;

    /** Constant for {@code DOM_VK_3}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_3 = 51;

    /** Constant for {@code DOM_VK_4}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_4 = 52;

    /** Constant for {@code DOM_VK_5}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_5 = 53;

    /** Constant for {@code DOM_VK_6}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_6 = 54;

    /** Constant for {@code DOM_VK_7}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_7 = 55;

    /** Constant for {@code DOM_VK_8}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_8 = 56;

    /** Constant for {@code DOM_VK_9}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_9 = 57;

    /** Constant for {@code DOM_VK_COLON}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_COLON = 58;

    /** Constant for {@code DOM_VK_SEMICOLON}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SEMICOLON = 59;

    /** Constant for {@code DOM_VK_LESS_THAN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_LESS_THAN = 60;

    /** Constant for {@code DOM_VK_EQUALS}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_EQUALS = 61;

    /** Constant for {@code DOM_VK_GREATER_THAN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_GREATER_THAN = 62;

    /** Constant for {@code DOM_VK_QUESTION_MARK}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_QUESTION_MARK = 63;

    /** Constant for {@code DOM_VK_AT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_AT = 64;

    /** Constant for {@code DOM_VK_A}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_A = 65;

    /** Constant for {@code DOM_VK_B}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_B = 66;

    /** Constant for {@code DOM_VK_C}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_C = 67;

    /** Constant for {@code DOM_VK_D}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_D = 68;

    /** Constant for {@code DOM_VK_E}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_E = 69;

    /** Constant for {@code DOM_VK_F}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F = 70;

    /** Constant for {@code DOM_VK_G}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_G = 71;

    /** Constant for {@code DOM_VK_H}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_H = 72;

    /** Constant for {@code DOM_VK_I}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_I = 73;

    /** Constant for {@code DOM_VK_J}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_J = 74;

    /** Constant for {@code DOM_VK_K}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_K = 75;

    /** Constant for {@code DOM_VK_L}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_L = 76;

    /** Constant for {@code DOM_VK_M}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_M = 77;

    /** Constant for {@code DOM_VK_N}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_N = 78;

    /** Constant for {@code DOM_VK_O}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_O = 79;

    /** Constant for {@code DOM_VK_BACK_SPACE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_BACK_SPACE = 8;

    /** Constant for {@code DOM_VK_P}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_P = 80;

    /** Constant for {@code DOM_VK_Q}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_Q = 81;

    /** Constant for {@code DOM_VK_R}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_R = 82;

    /** Constant for {@code DOM_VK_S}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_S = 83;

    /** Constant for {@code DOM_VK_T}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_T = 84;

    /** Constant for {@code DOM_VK_U}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_U = 85;

    /** Constant for {@code DOM_VK_V}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_V = 86;

    /** Constant for {@code DOM_VK_W}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_W = 87;

    /** Constant for {@code DOM_VK_X}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_X = 88;

    /** Constant for {@code DOM_VK_Y}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_Y = 89;

    /** Constant for {@code DOM_VK_Z}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_Z = 90;

    /** Constant for {@code DOM_VK_WIN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN = 91;

    /** Constant for {@code DOM_VK_CONTEXT_MENU}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CONTEXT_MENU = 93;

    /** Constant for {@code DOM_VK_SLEEP}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SLEEP = 95;

    /** Constant for {@code DOM_VK_NUMPAD0}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD0 = 96;

    /** Constant for {@code DOM_VK_NUMPAD1}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD1 = 97;

    /** Constant for {@code DOM_VK_NUMPAD2}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD2 = 98;

    /** Constant for {@code DOM_VK_NUMPAD3}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD3 = 99;

    /** Constant for {@code DOM_VK_NUMPAD4}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD4 = 100;

    /** Constant for {@code DOM_VK_NUMPAD5}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD5 = 101;

    /** Constant for {@code DOM_VK_NUMPAD6}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD6 = 102;

    /** Constant for {@code DOM_VK_NUMPAD7}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD7 = 103;

    /** Constant for {@code DOM_VK_NUMPAD8}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD8 = 104;

    /** Constant for {@code DOM_VK_NUMPAD9}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUMPAD9 = 105;

    /** Constant for {@code DOM_VK_MULTIPLY}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_MULTIPLY = 106;

    /** Constant for {@code DOM_VK_ADD}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_ADD = 107;

    /** Constant for {@code DOM_VK_SEPARATOR}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SEPARATOR = 108;

    /** Constant for {@code DOM_VK_SUBTRACT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SUBTRACT = 109;

    /** Constant for {@code DOM_VK_DECIMAL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_DECIMAL = 110;

    /** Constant for {@code DOM_VK_DIVIDE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_DIVIDE = 111;

    /** Constant for {@code DOM_VK_F1}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F1 = 112;

    /** Constant for {@code DOM_VK_F2}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F2 = 113;

    /** Constant for {@code DOM_VK_F3}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F3 = 114;

    /** Constant for {@code DOM_VK_F4}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F4 = 115;

    /** Constant for {@code DOM_VK_F5}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F5 = 116;

    /** Constant for {@code DOM_VK_F6}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F6 = 117;

    /** Constant for {@code DOM_VK_F7}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F7 = 118;

    /** Constant for {@code DOM_VK_F8}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F8 = 119;

    /** Constant for {@code DOM_VK_F9}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F9 = 120;

    /** Constant for {@code DOM_VK_F10}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F10 = 121;

    /** Constant for {@code DOM_VK_F11}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F11 = 122;

    /** Constant for {@code DOM_VK_F12}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F12 = 123;

    /** Constant for {@code DOM_VK_F13}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F13 = 124;

    /** Constant for {@code DOM_VK_F14}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F14 = 125;

    /** Constant for {@code DOM_VK_F15}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F15 = 126;

    /** Constant for {@code DOM_VK_F16}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F16 = 127;

    /** Constant for {@code DOM_VK_F17}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F17 = 128;

    /** Constant for {@code DOM_VK_F18}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F18 = 129;

    /** Constant for {@code DOM_VK_F19}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F19 = 130;

    /** Constant for {@code DOM_VK_F20}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F20 = 131;

    /** Constant for {@code DOM_VK_F21}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F21 = 132;

    /** Constant for {@code DOM_VK_F22}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F22 = 133;

    /** Constant for {@code DOM_VK_F23}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F23 = 134;

    /** Constant for {@code DOM_VK_F24}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_F24 = 135;

    /** Constant for {@code DOM_VK_NUM_LOCK}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_NUM_LOCK = 144;

    /** Constant for {@code DOM_VK_SCROLL_LOCK}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SCROLL_LOCK = 145;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_JISHO}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_FJ_JISHO = 146;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_MASSHOU}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_FJ_MASSHOU = 147;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_TOUROKU}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_FJ_TOUROKU = 148;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_LOYA}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_FJ_LOYA = 149;

    /** Constant for {@code DOM_VK_WIN_OEM_FJ_ROYA}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_FJ_ROYA = 150;

    /** Constant for {@code DOM_VK_CIRCUMFLEX}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CIRCUMFLEX = 160;

    /** Constant for {@code DOM_VK_EXCLAMATION}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_EXCLAMATION = 161;

    /** Constant for {@code DOM_VK_DOUBLE_QUOTE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_DOUBLE_QUOTE = 162;

    /** Constant for {@code DOM_VK_HASH}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_HASH = 163;

    /** Constant for {@code DOM_VK_DOLLAR}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_DOLLAR = 164;

    /** Constant for {@code DOM_VK_PERCENT}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PERCENT = 165;

    /** Constant for {@code DOM_VK_AMPERSAND}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_AMPERSAND = 166;

    /** Constant for {@code DOM_VK_UNDERSCORE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_UNDERSCORE = 167;

    /** Constant for {@code DOM_VK_OPEN_PAREN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_OPEN_PAREN = 168;

    /** Constant for {@code DOM_VK_CLOSE_PAREN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CLOSE_PAREN = 169;

    /** Constant for {@code DOM_VK_ASTERISK}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_ASTERISK = 170;

    /** Constant for {@code DOM_VK_PLUS}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PLUS = 171;

    /** Constant for {@code DOM_VK_PIPE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PIPE = 172;

    /** Constant for {@code DOM_VK_HYPHEN_MINUS}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_HYPHEN_MINUS = 173;

    /** Constant for {@code DOM_VK_OPEN_CURLY_BRACKET}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_OPEN_CURLY_BRACKET = 174;

    /** Constant for {@code DOM_VK_CLOSE_CURLY_BRACKET}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CLOSE_CURLY_BRACKET = 175;

    /** Constant for {@code DOM_VK_TILDE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_TILDE = 176;

    /** Constant for {@code DOM_VK_VOLUME_MUTE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_VOLUME_MUTE = 181;

    /** Constant for {@code DOM_VK_VOLUME_DOWN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_VOLUME_DOWN = 182;

    /** Constant for {@code DOM_VK_VOLUME_UP}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_VOLUME_UP = 183;

    /** Constant for {@code DOM_VK_COMMA}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_COMMA = 188;

    /** Constant for {@code DOM_VK_PERIOD}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PERIOD = 190;

    /** Constant for {@code DOM_VK_SLASH}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_SLASH = 191;

    /** Constant for {@code DOM_VK_BACK_QUOTE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_BACK_QUOTE = 192;

    /** Constant for {@code DOM_VK_OPEN_BRACKET}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_OPEN_BRACKET = 219;

    /** Constant for {@code DOM_VK_BACK_SLASH}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_BACK_SLASH = 220;

    /** Constant for {@code DOM_VK_CLOSE_BRACKET}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CLOSE_BRACKET = 221;

    /** Constant for {@code DOM_VK_QUOTE}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_QUOTE = 222;

    /** Constant for {@code DOM_VK_META}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_META = 224;

    /** Constant for {@code DOM_VK_ALTGR}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_ALTGR = 225;

    /** Constant for {@code DOM_VK_WIN_ICO_HELP}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_ICO_HELP = 227;

    /** Constant for {@code DOM_VK_WIN_ICO_00}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_ICO_00 = 228;

    /** Constant for {@code DOM_VK_WIN_ICO_CLEAR}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_ICO_CLEAR = 230;

    /** Constant for {@code DOM_VK_WIN_OEM_RESET}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_RESET = 233;

    /** Constant for {@code DOM_VK_WIN_OEM_JUMP}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_JUMP = 234;

    /** Constant for {@code DOM_VK_WIN_OEM_PA1}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_PA1 = 235;

    /** Constant for {@code DOM_VK_WIN_OEM_PA2}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_PA2 = 236;

    /** Constant for {@code DOM_VK_WIN_OEM_PA3}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_PA3 = 237;

    /** Constant for {@code DOM_VK_WIN_OEM_WSCTRL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_WSCTRL = 238;

    /** Constant for {@code DOM_VK_WIN_OEM_CUSEL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_CUSEL = 239;

    /** Constant for {@code DOM_VK_WIN_OEM_ATTN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_ATTN = 240;

    /** Constant for {@code DOM_VK_WIN_OEM_FINISH}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_FINISH = 241;

    /** Constant for {@code DOM_VK_WIN_OEM_COPY}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_COPY = 242;

    /** Constant for {@code DOM_VK_WIN_OEM_AUTO}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_AUTO = 243;

    /** Constant for {@code DOM_VK_WIN_OEM_ENLW}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_ENLW = 244;

    /** Constant for {@code DOM_VK_WIN_OEM_BACKTAB}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_WIN_OEM_BACKTAB = 245;

    /** Constant for {@code DOM_VK_ATTN}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_ATTN = 246;

    /** Constant for {@code DOM_VK_CRSEL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_CRSEL = 247;

    /** Constant for {@code DOM_VK_EXSEL}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_EXSEL = 248;

    /** Constant for {@code DOM_VK_EREOF}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_EREOF = 249;

    /** Constant for {@code DOM_VK_PLAY}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PLAY = 250;

    /** Constant for {@code DOM_VK_ZOOM}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_ZOOM = 251;

    /** Constant for {@code DOM_VK_PA1}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
    public static final int DOM_VK_PA1 = 253;

    /** Constant for {@code DOM_VK_WIN_OEM_CLEAR}. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
            where = Where.CONSTRUCTOR, value = FF)
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

    private int charCode_;
    private int which_;

    /**
     * Creates a new keyboard event instance.
     */
    public KeyboardEvent2() {
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
    public KeyboardEvent2(final DomNode domNode, final String type, final char character,
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
        if (!getType().equals(Event2.TYPE_KEY_PRESS)) {
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
    public KeyboardEvent2(final DomNode domNode, final String type, final int keyCode,
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
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static KeyboardEvent2 constructor(final boolean newObj, final Object self) {
        final KeyboardEvent2 host = new KeyboardEvent2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
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

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(KeyboardEvent2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("KeyboardEvent",
                    staticHandle("constructor", KeyboardEvent2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends PrototypeObject {

        Prototype() {
            ScriptUtils.initialize(this);
        }

        @Override
        public String getClassName() {
            return "KeyboardEvent";
        }
    }
}
