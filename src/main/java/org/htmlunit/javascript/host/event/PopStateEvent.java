/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code PopStateEvent}.
 *
 * @author Ahmed Ashour
 * @author Adam Afeltowicz
 * @author Ronald Brill
 */
@JsxClass
public class PopStateEvent extends Event {

    private Object state_;

    /**
     * Default constructor.
     */
    public PopStateEvent() {
        super();
        setEventType("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            state_ = details.get("state");
        }
    }

    /**
     * Creates a new event instance.
     *
     * @param target the event target
     * @param type the event type
     * @param state the state object
     */
    public PopStateEvent(final EventTarget target, final String type, final Object state) {
        super(target, type);
        state_ = state;
    }

    /**
     * Return the state object.
     * @return the state object
     */
    @JsxGetter
    public Object getState() {
        return state_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (!"state".equals(name)) {
            super.put(name, start, value);
        }
    }
}
