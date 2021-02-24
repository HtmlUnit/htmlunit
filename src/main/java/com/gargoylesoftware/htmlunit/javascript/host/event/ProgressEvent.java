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

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code ProgressEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 */
@JsxClass
public class ProgressEvent extends Event {

    private boolean lengthComputable_;
    private Object loaded_ = Long.valueOf(0L);
    private long total_;

    /**
     * Default constructor.
     */
    public ProgressEvent() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !Undefined.isUndefined(details)) {
            final Object lengthComputable = details.get("lengthComputable");
            if (lengthComputable instanceof Boolean) {
                lengthComputable_ = (Boolean) lengthComputable;
            }
            else {
                lengthComputable_ = Boolean.parseBoolean(lengthComputable.toString());
            }

            final Object loaded = details.get("loaded");
            if (loaded instanceof Long) {
                loaded_ = loaded;
            }
            else if (loaded instanceof Double) {
                loaded_ = ((Double) loaded).longValue();
            }
            else {
                try {
                    loaded_ = Long.parseLong(loaded.toString());
                }
                catch (final NumberFormatException e) {
                    // ignore
                }
            }

            final Object total = details.get("total");
            if (total instanceof Long) {
                total_ = (Long) total;
            }
            else if (total instanceof Double) {
                total_ = ((Double) total).longValue();
            }
            else {
                try {
                    total_ = Long.parseLong(details.get("total").toString());
                }
                catch (final NumberFormatException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Creates a new event instance.
     * @param target the event target
     * @param type the event type
     */
    public ProgressEvent(final EventTarget target, final String type) {
        super(target, type);
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
    public Object getLoaded() {
        return loaded_;
    }

    /**
     * Sets the loaded information for this event.
     *
     * @param loaded the loaded information for this event
     */
    public void setLoaded(final Object loaded) {
        loaded_ = loaded;
    }

    /**
     * Returns the total property from the event.
     * @return the total property from the event.
     */
    @JsxGetter
    public long getTotal() {
        return total_;
    }

    /**
     * Sets the total information for this event.
     *
     * @param total the total information for this event
     */
    public void setTotal(final long total) {
        total_ = total;
    }
}
