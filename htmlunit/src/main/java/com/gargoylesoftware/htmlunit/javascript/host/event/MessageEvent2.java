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

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

public class MessageEvent2 extends Event2 {

    private Object data_;
    private String origin_;
    private String lastEventId_;
    private Window2 source_;
    private Object ports_;

    public static MessageEvent2 constructor(final boolean newObj, final Object self) {
        final MessageEvent2 host = new MessageEvent2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
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
    @Function({@WebBrowser(CHROME), @WebBrowser(IE), @WebBrowser(value = FF, minVersion = 45)})
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

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("MessageEvent", 
                    staticHandle("constructor", MessageEvent2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends PrototypeObject {

        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "MessageEvent";
        }
    }
}
