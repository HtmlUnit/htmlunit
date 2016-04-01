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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCLOSE_DEFAULT_TYPE_EMPTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCLOSE_INIT_CLOSE_EVENT_THROWS;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

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
        if (getBrowserVersion().hasFeature(EVENT_ONCLOSE_DEFAULT_TYPE_EMPTY)) {
            setType("");
        }
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        int code = 0;
        String reason = "";
        boolean wasClean = false;

        if (details != null && !Context.getUndefinedValue().equals(details)) {
            final Double detailCode = (Double) details.get("code");
            if (detailCode != null) {
                code = detailCode.intValue();
            }

            final String detailReason = (String) details.get("reason");
            if (detailReason != null) {
                reason = detailReason;
            }

            final Boolean detailWasClean = (Boolean) details.get("wasClean");
            if (detailWasClean != null) {
                wasClean = detailWasClean.booleanValue();
            }
        }
        code_ = code;
        reason_ = reason;
        wasClean_ = wasClean;
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
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(IE) })
    public void initCloseEvent(final String type, final boolean bubbles, final boolean cancelable,
            final boolean wasClean, final int reasonCode, final String reason) {
        if (getBrowserVersion().hasFeature(EVENT_ONCLOSE_INIT_CLOSE_EVENT_THROWS)) {
            Context.throwAsScriptRuntimeEx(new IllegalArgumentException("Illegal call to initCloseEvent()"));
        }
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
    public boolean getWasClean() {
        return wasClean_;
    }

    /**
     * @param wasClean the wasClean
     */
    public void setWasClean(final boolean wasClean) {
        wasClean_ = wasClean;
    }
}
