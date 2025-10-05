/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import static org.htmlunit.BrowserVersionFeatures.JS_EVENT_INPUT_CTOR_INPUTTYPE;

import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code InputEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class InputEvent extends UIEvent {

    private String data_;
    private String inputType_;
    private boolean isComposing_;

    /**
     * Default constructor.
     */
    public InputEvent() {
        super();
        data_ = "";
        inputType_ = "";
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            if (getBrowserVersion().hasFeature(JS_EVENT_INPUT_CTOR_INPUTTYPE)) {
                final Object inputType = details.get("inputType", details);
                if (!isMissingOrUndefined(inputType)) {
                    inputType_ = JavaScriptEngine.toString(inputType);
                }
            }

            final Object dataObj = details.get("data", details);
            if (!isMissingOrUndefined(dataObj)) {
                data_ = JavaScriptEngine.toString(dataObj);
            }

            final Object isComposing = details.get("isComposing", details);
            if (!isMissingOrUndefined(isComposing)) {
                setIsComposing(JavaScriptEngine.toBoolean(isComposing));
            }
        }
    }

    /**
     * Returns whether or not the event is fired after the compositionstart and before the compositionend events.
     * @return whether or not the event is fired while composing
     */
    @JsxGetter
    public boolean isIsComposing() {
        return isComposing_;
    }

    /**
     * Sets whether or not this event is fired after the compositionstart and before the compositionend events.
     * @param isComposing whether or not this event is fired while composing
     */
    protected void setIsComposing(final boolean isComposing) {
        isComposing_ = isComposing;
    }

    /**
     * Retrieves the data contained.
     * @return the data contained
     */
    @JsxGetter
    public Object getData() {
        return data_;
    }

    /**
     * Retrieves the inputType.
     * @return the inputType
     */
    @JsxGetter
    public Object getInputType() {
        return inputType_;
    }
}
