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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_POP_STATE_EVENT_CLONE_STATE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_POP_STATE_EVENT_TYPE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

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
        setEventType("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !Context.getUndefinedValue().equals(details)) {
            state_ = details.get("state");
        }
    }

    /**
     * Creates a new event instance.
     *
     * @param scriptable the SimpleScriptable that triggered the event
     * @param type the event type
     * @param state the state object
     */
    public PopStateEvent(final SimpleScriptable scriptable, final String type, final Object state) {
        super(scriptable, type);
        if (state instanceof NativeObject && getBrowserVersion().hasFeature(JS_POP_STATE_EVENT_CLONE_STATE)) {
            final NativeObject old = (NativeObject) state;
            final NativeObject newState = new NativeObject();
            for (final Object o : ScriptableObject.getPropertyIds(old)) {
                final String property = Context.toString(o);
                newState.defineProperty(property, ScriptableObject.getProperty(old, property), ScriptableObject.EMPTY);
            }
            state_ = newState;
        }
        else {
            state_ = state;
        }
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
    public void eventCreated() {
        super.eventCreated();
        if (getBrowserVersion().hasFeature(JS_POP_STATE_EVENT_TYPE)) {
            setType(Event.TYPE_POPSTATE);
            setCancelable(true);
        }
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
