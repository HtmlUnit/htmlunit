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
package org.htmlunit.javascript.host.event;

import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript host object for {@code CustomEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CustomEvent">MDN Documentation</a>
 */
@JsxClass
public class CustomEvent extends Event {

    /** The data passed when initializing the event. */
    private Object detail_;

    /**
     * Creates an instance of this event.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(JavaScriptEngine.toString(type), details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            final Object detail = details.get("detail", details);
            if (NOT_FOUND != detail) {
                detail_ = detail;
            }
        }
    }

    /**
     * Initializes the custom event.
     *
     * @param type the event type
     * @param bubbles whether the event bubbles
     * @param cancelable whether the event can be canceled
     * @param detail the custom detail data for the event
     */
    @JsxFunction
    public void initCustomEvent(
            final String type,
            final boolean bubbles,
            final boolean cancelable,
            final Object detail) {
        initEvent(type, bubbles, cancelable);
        detail_ = detail;
    }

    /**
     * Returns any data passed when the event was initialized.
     *
     * @return any data passed when the event was initialized
     */
    @JsxGetter
    public Object getDetail() {
        return detail_;
    }
}
