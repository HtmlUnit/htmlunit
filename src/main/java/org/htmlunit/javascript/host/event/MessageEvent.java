/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.http.HttpHeader;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.WindowProxy;

/**
 * A JavaScript object for {@code MessageEvent}.
 * <p>
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/comms.html#messageevent">
 * Event definitions</a>.
 *
 * @see <a href="https://developer.mozilla.org/en/WebSockets/WebSockets_reference/MessageEvent">
 *          Mozilla documentation</a>
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class MessageEvent extends Event {

    private Object data_;
    private String origin_;
    private String lastEventId_;
    private Window source_;
    private Object ports_;

    /**
     * Default constructor used to build the prototype.
     */
    public MessageEvent() {
        setType(TYPE_MESSAGE);
        origin_ = "";
        lastEventId_ = "";
        data_ = JavaScriptEngine.Undefined;
    }

    /**
     * Constructs a Message Event with the provided data.
     * @param data the data
     */
    public MessageEvent(final Object data) {
        this();
        data_ = data;
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        data_ = null;

        String origin = "";
        String lastEventId = "";
        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            data_ = details.get("data");

            final String detailOrigin = (String) details.get(HttpHeader.ORIGIN_LC);
            if (detailOrigin != null) {
                origin = detailOrigin;
            }

            final Object detailLastEventId = details.get("lastEventId");
            if (detailLastEventId != null) {
                lastEventId = JavaScriptEngine.toString(detailLastEventId);
            }

            source_ = null;
            final Object detailSource = details.get("source");
            if (detailSource instanceof Window) {
                source_ = (Window) detailSource;
            }
            else if (detailSource instanceof WindowProxy) {
                source_ = ((WindowProxy) detailSource).getDelegee();
            }
            ports_ = details.get("ports");
        }
        origin_ = origin;
        lastEventId_ = lastEventId;
    }

    /**
     * Initializes an event object.
     * @param type the event type
     * @param canBubble can the event bubble
     * @param cancelable can the event be canceled
     * @param data the message
     * @param origin the scheme, hostname and port of the document that caused the event
     * @param lastEventId the identifier of the last event
     * @param source the window object that contains the document that caused the event
     * @param ports the message ports
     */
    @JsxFunction
    public void initMessageEvent(
            final String type,
            final boolean canBubble,
            final boolean cancelable,
            final Object data,
            final String origin,
            final String lastEventId,
            final Window source,
            final Object ports) {
        initEvent(type, canBubble, cancelable);
        data_ = data;
        origin_ = origin;
        lastEventId_ = lastEventId;
        source_ = source;

        if (JavaScriptEngine.isUndefined(ports)
            || ports instanceof NativeArray
            || (ports instanceof Scriptable && ScriptableObject.hasProperty((Scriptable) ports, "length"))) {
            ports_ = ports;
        }
        else {
            throw JavaScriptEngine.typeError(
                    "Argument 8 of MessageEvent.initMessageEvent can't be converted to a sequence.");
        }
    }

    /**
     * Retrieves the data contained.
     * @return the data contained
     */
    @JsxGetter
    public Object getData() {
        return data_;
    }

    /**
     * Gets the URI of the document of origin.
     * @return the origin
     */
    @JsxGetter
    public String getOrigin() {
        return origin_;
    }

    /**
     * Sets the URI of the document of origin.
     * @param origin the origin
     */
    public void setOrigin(final String origin) {
        origin_ = origin;
    }

    /**
     * Retrieves the identifier of the last event.
     * @return the identified of the last event
     */
    @JsxGetter
    public String getLastEventId() {
        return lastEventId_;
    }

    /**
     * Retrieves the data contained.
     * @return the data contained
     */
    @JsxGetter
    public Window getSource() {
        return source_;
    }

    /**
     * Returns the {@code ports} property.
     * @return the {@code ports} property
     */
    @JsxGetter
    public Object getPorts() {
        return ports_;
    }
}
