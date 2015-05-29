/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * JavaScript object representing a Keyboard Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3c.org/TR/DOM-Level-3-Events/#Events-KeyboardEvents-Interfaces">
 * DOM Level 3 Events</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 * @deprecated as of 2.17, please use {@link com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent}
 */
@Deprecated
@JsxClasses({
        @JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) }),
        @JsxClass(isJSObject = false, isDefinedInStandardsMode = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8))
    })
public class KeyboardEvent extends com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent {

    /**
     * Creates a new keyboard event instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public KeyboardEvent() {
    }

    /**
     * Creates a new keyboard event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param character the character associated with the event
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     */
    public KeyboardEvent(final DomNode domNode, final String type, final char character,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        super(domNode, type, character, shiftKey, ctrlKey, altKey);
    }

    /**
     * Creates a new keyboard event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param keyCode the key code associated with the event
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     */
    public KeyboardEvent(final DomNode domNode, final String type, final int keyCode,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        super(domNode, type, keyCode, shiftKey, ctrlKey, altKey);
    }

}
