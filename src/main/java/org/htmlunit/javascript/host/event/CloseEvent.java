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
 * JavaScript host object for {@code CloseEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CloseEvent">MDN Documentation</a>
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
        super();
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
            code_ = JavaScriptEngine.toInt32(details.get("code"));
            wasClean_ = JavaScriptEngine.toBoolean(details.get("wasClean"));

            final Object reason = details.get("reason");
            if (!isNullMissingOrUndefined(reason)) {
                reason_ = JavaScriptEngine.toString(reason);
            }
        }
    }

    /**
     * Returns the close code.
     *
     * @return the close code
     */
    @JsxGetter
    public int getCode() {
        return code_;
    }

    /**
     * Sets the close code.
     *
     * @param code the close code
     */
    public void setCode(final int code) {
        code_ = code;
    }

    /**
     * Returns the reason the connection was closed.
     *
     * @return the reason
     */
    @JsxGetter
    public String getReason() {
        return reason_;
    }

    /**
     * Sets the reason the connection was closed.
     *
     * @param reason the reason
     */
    public void setReason(final String reason) {
        reason_ = reason;
    }

    /**
     * Returns whether the connection was closed cleanly.
     *
     * @return {@code true} if the connection was closed cleanly
     */
    @JsxGetter
    public boolean isWasClean() {
        return wasClean_;
    }

    /**
     * Sets whether the connection was closed cleanly.
     *
     * @param wasClean {@code true} if the connection was closed cleanly
     */
    public void setWasClean(final boolean wasClean) {
        wasClean_ = wasClean;
    }
}
