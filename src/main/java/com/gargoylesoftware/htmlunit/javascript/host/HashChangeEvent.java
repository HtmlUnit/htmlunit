/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * JavaScript object representing the HashChangeEvent.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3c.org/TR/DOM-Level-3-Events/#Events-KeyboardEvents-Interfaces">
 * DOM Level 3 Events</a>.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@JsxClass(browsers = { @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF) })
public class HashChangeEvent extends UIEvent {

    private Object oldURL_;
    private Object newURL_;

    /**
     * Creates a new UI event instance.
     */
    public HashChangeEvent() {
        // Empty.
    }

    /**
     * Creates a new event instance.
     *
     * @param scriptable the SimpleScriptable that triggered the event
     * @param type the event type
     * @param oldUrl the old url
     * @param newUrl the new url
     */
    public HashChangeEvent(final SimpleScriptable scriptable, final String type,
            final Object oldUrl, final Object newUrl) {
        super(scriptable, type);
        oldURL_ = oldUrl;
        newURL_ = newUrl;
    }

    /**
     * Returns the old URL.
     * @return the old URL
     */
    @JsxGetter
    public Object getOldURL() {
        return oldURL_;
    }

    /**
     * Returns the new URL.
     * @return the new URL
     */
    @JsxGetter
    public Object getNewURL() {
        return newURL_;
    }
}
