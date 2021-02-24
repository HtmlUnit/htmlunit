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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_INPUT_CTOR_INPUTTYPE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code InputEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF78})
public class InputEvent extends UIEvent {

    private String data_;
    private String inputType_;
    private boolean isComposing_;

    /**
     * Default constructor.
     */
    public InputEvent() {
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

        if (details != null && !Undefined.isUndefined(details)) {
            if (getBrowserVersion().hasFeature(JS_EVENT_INPUT_CTOR_INPUTTYPE)) {
                final Object inputType = details.get("inputType", details);
                if (!isMissingOrUndefined(inputType)) {
                    inputType_ = ScriptRuntime.toString(inputType);
                }
            }

            final Object dataObj = details.get("data", details);
            if (!isMissingOrUndefined(dataObj)) {
                data_ = ScriptRuntime.toString(dataObj);
            }

            final Object isComposing = details.get("isComposing", details);
            if (!isMissingOrUndefined(isComposing)) {
                setIsComposing(ScriptRuntime.toBoolean(isComposing));
            }
        }
    }

    /**
     * Returns whether or not the event is fired after the compositionstart and before the compositionend events.
     * @return whether or not the event is fired while composing
     */
    @JsxGetter
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
