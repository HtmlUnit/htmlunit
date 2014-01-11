/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_BEFOREUNLOAD_AUTO_TYPE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * JavaScript object representing the BeforeUnloadEvent.
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window.onbeforeunload">Mozilla Developer Network</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ff974336.aspx">MSDN</a>
 *
 * @version $Revision$
 * @author Frank Danek
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
public class BeforeUnloadEvent extends Event {

    /**
     * Creates a new event instance.
     */
    public BeforeUnloadEvent() {
        setEventType("");
        setReturnValue("");
    }

    /**
     * Creates a new event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     */
    public BeforeUnloadEvent(final DomNode domNode, final String type) {
        super(domNode, type);

        setBubbles(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eventCreated() {
        super.eventCreated();

        if (getBrowserVersion().hasFeature(EVENT_BEFOREUNLOAD_AUTO_TYPE)) {
            setEventType(TYPE_BEFORE_UNLOAD);
            setCancelable(true);
        }
    }

    /**
     * Returns the return value associated with the event.
     * @return the return value associated with the event
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public Object getReturnValue() {
        return super.getReturnValue();
    }

    /**
     * Sets the return value associated with the event.
     * @param returnValue the return value associated with the event
     */
    @JsxSetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void setReturnValue(final Object returnValue) {
        super.setReturnValue(returnValue);
    }
}
