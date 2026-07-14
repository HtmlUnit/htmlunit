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
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript host object for {@code CompositionEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CompositionEvent">MDN Documentation</a>
 */
@JsxClass
public class CompositionEvent extends UIEvent {

    private String data_;

    /**
     * Creates a new event instance.
     */
    public CompositionEvent() {
        super();
        data_ = "";
    }

    /**
     * Creates an instance of this event.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            final Object dataObj = details.get("data", details);
            if (NOT_FOUND != dataObj) {
                data_ = JavaScriptEngine.toString(dataObj);
            }
        }
    }

    /**
     * Returns the composition data.
     *
     * @return the data
     */
    @JsxGetter
    public String getData() {
        return data_;
    }

    /**
     * Sets the composition data.
     *
     * @param data the data
     */
    public void setData(final String data) {
        data_ = data;
    }
}
