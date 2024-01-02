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

import static org.htmlunit.BrowserVersionFeatures.JS_POP_STATE_EVENT_CLONE_STATE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static org.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.htmlunit.WebClient;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
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
        setEventType("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
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
        if (state instanceof NativeObject && getBrowserVersion().hasFeature(JS_POP_STATE_EVENT_CLONE_STATE)) {
            final NativeObject old = (NativeObject) state;
            final NativeObject newState = new NativeObject();

            final WebClient client = getWindow().getWebWindow().getWebClient();
            final HtmlUnitContextFactory cf = ((JavaScriptEngine) client.getJavaScriptEngine()).getContextFactory();

            final ContextAction<Object> contextAction = cx -> {
                for (final Object o : ScriptableObject.getPropertyIds(old)) {
                    final String property = JavaScriptEngine.toString(o);
                    newState.defineProperty(property, ScriptableObject.getProperty(old, property),
                            ScriptableObject.EMPTY);
                }
                return null;
            };
            cf.call(contextAction);
            state_ = newState;
        }
        else {
            state_ = state;
        }
    }

    /**
     * Initializes this event.
     * @param type the event type
     * @param bubbles whether or not the event should bubble
     * @param cancelable whether or not the event the event should be cancelable
     * @param state the state
     */
    @JsxFunction(IE)
    public void initPopStateEvent(final String type, final boolean bubbles,
            final boolean cancelable, final Object state) {
        initEvent(type, bubbles, cancelable);
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
