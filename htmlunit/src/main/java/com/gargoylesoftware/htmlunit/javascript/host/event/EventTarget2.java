/*
 * Copyright (c) 2015 Gargoyle Software Inc.
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

import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Browser;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

public class EventTarget2 extends ScriptObject {

    public static EventTarget2 constructor(final boolean newObj, final Object self) {
        final EventTarget2 host = new EventTarget2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
    }

    public static String addEventListener(final Object self) {
        return Browser.getCurrent().getFamily().name();
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(EventTarget2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("EventTarget", 
                    staticHandle("constructor", EventTarget2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends PrototypeObject {
        public ScriptFunction addEventListener;

        public ScriptFunction G$addEventListener() {
            return this.addEventListener;
        }

        public void S$addEventListener(final ScriptFunction function) {
            this.addEventListener = function;
        }

        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "EventTarget";
        }
    }

    public static final class ObjectConstructor extends ScriptObject {
        public ScriptFunction addEventListener;

        public ScriptFunction G$addEventListener() {
            return this.addEventListener;
        }

        public void S$addEventListener(final ScriptFunction function) {
            this.addEventListener = function;
        }

        public ObjectConstructor() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "EventTarget";
        }
    }

}
