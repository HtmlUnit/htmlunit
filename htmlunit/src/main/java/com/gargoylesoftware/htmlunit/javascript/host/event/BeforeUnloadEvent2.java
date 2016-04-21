/*
 * Copyright (c) 2016 Gargoyle Software Inc.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (http://www.gnu.org/licenses/).
 */
package com.gargoylesoftware.htmlunit.javascript.host.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

public class BeforeUnloadEvent2 extends Event2 {

    public static BeforeUnloadEvent2 constructor(final boolean newObj, final Object self) {
        final BeforeUnloadEvent2 host = new BeforeUnloadEvent2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
    }

    /**
     * Creates a new event instance.
     */
    public BeforeUnloadEvent2() {
//        setEventType("");
//        setReturnValue("");
    }

    /**
     * Creates a new event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     */
    public BeforeUnloadEvent2(final DomNode domNode, final String type) {
        super(domNode, type);

        //setBubbles(false);
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(BeforeUnloadEvent2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("BeforeUnloadEvent", 
                    staticHandle("constructor", BeforeUnloadEvent2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    static final class Prototype extends PrototypeObject {
        public String getClassName() {
            return "BeforeUnloadEvent";
        }
    }
}
