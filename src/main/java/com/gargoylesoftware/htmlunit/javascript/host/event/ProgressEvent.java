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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for {@code ProgressEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class ProgressEvent extends Event {

    private boolean lengthComputable_;
    private long loaded_;
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
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !Context.getUndefinedValue().equals(details)) {
            final Object lengthComputable = details.get("lengthComputable");
            if (lengthComputable instanceof Boolean) {
                lengthComputable_ = (Boolean) lengthComputable;
            }
            else {
                lengthComputable_ = Boolean.parseBoolean(lengthComputable.toString());
            }

            final Object loaded = details.get("loaded");
            if (loaded instanceof Long) {
                loaded_ = (Long) loaded;
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
     * @param scriptable the SimpleScriptable that triggered the event
     * @param type the event type
     */
    public ProgressEvent(final SimpleScriptable scriptable, final String type) {
        super(scriptable, type);
    }

    /**
     * Returns the lengthComputable property from the event.
     * @return the lengthComputable property from the event.
     */
    @JsxGetter
    public boolean getLengthComputable() {
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
    public long getLoaded() {
        return loaded_;
    }

    /**
     * Sets the loaded information for this event.
     *
     * @param loaded the loaded information for this event
     */
    public void setLoaded(final long loaded) {
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
