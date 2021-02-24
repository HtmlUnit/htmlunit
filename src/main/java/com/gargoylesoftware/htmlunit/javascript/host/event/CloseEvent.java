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
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code CloseEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class CloseEvent extends Event {

    private String reason_;
    private int code_;
    private boolean wasClean_;

    /**
     * Creates a new event instance.
     */
    public CloseEvent() {
        setType(TYPE_CLOSE);
        reason_ = "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eventCreated() {
        super.eventCreated();
        setType("");
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !Undefined.isUndefined(details)) {
            code_ = ScriptRuntime.toInt32(details.get("code"));
            reason_ = ScriptRuntime.toString(details.get("reason"));
            wasClean_ = ScriptRuntime.toBoolean(details.get("wasClean"));
        }
    }

    /**
     * Initializes this close event.
     * @param type the event type
     * @param bubbles whether or not the event should bubble
     * @param cancelable whether or not the event the event should be cancelable
     * @param wasClean the wasClean flag
     * @param reasonCode the reason code
     * @param reason the reason
     */
    @JsxFunction(IE)
    public void initCloseEvent(final String type, final boolean bubbles, final boolean cancelable,
            final boolean wasClean, final int reasonCode, final String reason) {
        super.initEvent(type, bubbles, cancelable);
        wasClean_ = wasClean;
        code_ = reasonCode;
        reason_ = reason;
    }

    /**
     * @return the code
     */
    @JsxGetter
    public int getCode() {
        return code_;
    }

    /**
     * @param code the code
     */
    public void setCode(final int code) {
        code_ = code;
    }

    /**
     * @return the reason
     */
    @JsxGetter
    public String getReason() {
        return reason_;
    }

    /**
     * @param reason the reason
     */
    public void setReason(final String reason) {
        reason_ = reason;
    }

    /**
     * @return the wasClean
     */
    @JsxGetter
    public boolean isWasClean() {
        return wasClean_;
    }

    /**
     * @param wasClean the wasClean
     */
    public void setWasClean(final boolean wasClean) {
        wasClean_ = wasClean;
    }
}
