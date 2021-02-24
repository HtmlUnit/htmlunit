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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * JavaScript object representing a Mutation Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-MutationEvent">
 * DOM Level 2 Events</a>.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class MutationEvent extends Event {

    /** Modification. */
    @JsxConstant
    public static final int MODIFICATION = 1;

    /** Addition. */
    @JsxConstant
    public static final int ADDITION = 2;

    /** Removal. */
    @JsxConstant
    public static final int REMOVAL = 3;

    /**
     * Creates an instance.
     */
    public MutationEvent() {
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
        throw ScriptRuntime.typeError("Illegal constructor call for MutationEvent");
    }
}
