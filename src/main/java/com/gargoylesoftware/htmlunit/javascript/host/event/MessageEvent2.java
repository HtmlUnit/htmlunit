/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * A JavaScript object for {@code MessageEvent}.
 *
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/comms.html#messageevent">
 * Event definitions</a>.
 *
 * @see <a href="https://developer.mozilla.org/en/WebSockets/WebSockets_reference/MessageEvent">
 *          Mozilla documentation</a>
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@ScriptClass
public class MessageEvent2 extends Event2 {

    private Object data_;
    private String origin_;
    private String lastEventId_;
    private Window2 source_;
    private Object ports_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static MessageEvent2 constructor(final boolean newObj, final Object self) {
        final MessageEvent2 host = new MessageEvent2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
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
    @Function
    public void initMessageEvent(
            final String type,
            final boolean canBubble,
            final boolean cancelable,
            final Object data,
            final String origin,
            final String lastEventId,
            final Window2 source,
            final Object ports) {
        initEvent(type, canBubble, cancelable);
        data_ = data;
        origin_ = origin;
        lastEventId_ = lastEventId;
        source_ = source;
        ports_ = ports;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(MessageEvent2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Retrieves the data contained.
     * @return the data contained
     */
    @Getter
    public Object getData() {
        return data_;
    }

    /**
     * Gets the URI of the document of origin.
     * @return the origin
     */
    @Getter
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
    @Getter({CHROME, FF})
    public String getLastEventId() {
        return lastEventId_;
    }

    /**
     * Retrieves the data contained.
     * @return the data contained
     */
    @Getter
    public Window2 getSource() {
        return source_;
    }

    /**
     * Returns the {@code ports} property.
     * @return the {@code ports} property
     */
    @Getter
    public Object getPorts() {
        return ports_;
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("MessageEvent",
                    staticHandle("constructor", MessageEvent2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("MessageEvent");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("MessageEvent");
        }
    }

}
