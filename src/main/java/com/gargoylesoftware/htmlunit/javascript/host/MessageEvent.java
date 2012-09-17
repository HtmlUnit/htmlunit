/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for MessageEvent.
 *
 * @see <a href="https://developer.mozilla.org/en/WebSockets/WebSockets_reference/MessageEvent">
 * Mozilla documentation</a>
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class MessageEvent extends SimpleScriptable {

    private Object data_;

    /**
     * Default constructor used to build the prototype.
     */
    public MessageEvent() {
        // Empty.
    }

    /**
     * Constructs a Message Event with the provided data.
     * @param data the data
     */
    public MessageEvent(final Object data) {
        data_ = data;
    }

    /**
     * Retrieves the data contained.
     * @return the data contained
     */
    @JsxGetter
    public Object jsxGet_data() {
        return data_;
    }
}
