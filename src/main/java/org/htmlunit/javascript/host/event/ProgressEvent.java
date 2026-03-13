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
 * A JavaScript object for {@code ProgressEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 * @author Lai Quang Duong
 */
@JsxClass
public class ProgressEvent extends Event {

    private boolean lengthComputable_;
    private double loaded_;
    private double total_;

    /**
     * Default constructor.
     */
    public ProgressEvent() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            final Object lengthComputable = details.get("lengthComputable");
            if (lengthComputable instanceof Boolean boolean1) {
                lengthComputable_ = boolean1;
            }
            else {
                lengthComputable_ = Boolean.parseBoolean(lengthComputable.toString());
            }

            final Object loaded = details.get("loaded");
            loaded_ = JavaScriptEngine.toNumber(loaded);

            final Object total = details.get("total");
            total_ = JavaScriptEngine.toNumber(total);
        }
    }

    /**
     * Creates a new event instance.
     * @param target the event target
     * @param type the event type
     */
    public ProgressEvent(final EventTarget target, final String type) {
        this(target, type, false, 0d, 0d);
    }

    /**
     * Creates a new event instance.
     * @param target the event target
     * @param type the event type
     * @param lengthComputable whether the total size is known
     * @param loaded the number of bytes loaded
     * @param total the total number of bytes
     */
    public ProgressEvent(final EventTarget target, final String type,
            final boolean lengthComputable, final double loaded, final double total) {
        super(target, type);
        lengthComputable_ = lengthComputable;
        loaded_ = loaded;
        total_ = total;
    }

    /**
     * Returns the lengthComputable property from the event.
     * @return the lengthComputable property from the event.
     */
    @JsxGetter
    public boolean isLengthComputable() {
        return lengthComputable_;
    }

    /**
     * Sets the lengthComputable information for this event.
     *
     * @param lengthComputable the lengthComputable information for this event
     */
    public void setLengthComputable(final boolean lengthComputable) {
        lengthComputable_ = lengthComputable;
    }

    /**
     * Returns the loaded property from the event.
     * @return the loaded property from the event.
     */
    @JsxGetter
    public double getLoaded() {
        return loaded_;
    }

    /**
     * Sets the loaded information for this event.
     *
     * @param loaded the loaded information for this event
     */
    public void setLoaded(final double loaded) {
        loaded_ = loaded;
    }

    /**
     * Returns the total property from the event.
     * @return the total property from the event.
     */
    @JsxGetter
    public double getTotal() {
        return total_;
    }

    /**
     * Sets the total information for this event.
     *
     * @param total the total information for this event
     */
    public void setTotal(final double total) {
        total_ = total;
    }
}
