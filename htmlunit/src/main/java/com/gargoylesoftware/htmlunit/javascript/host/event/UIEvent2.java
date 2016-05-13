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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

public class UIEvent2 extends Event2 {

    /** Specifies some detail information about the event. */
    private long detail_;

    /** Whether or not the "meta" key was pressed during the firing of the event. */
    private boolean metaKey_;

    /**
     * Creates a new UI event instance.
     */
    public UIEvent2() {
    }

    /**
     * Creates a new UI event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     */
    public UIEvent2(final DomNode domNode, final String type) {
        super(domNode, type);
    }

    public static UIEvent2 constructor(final boolean newObj, final Object self) {
        final UIEvent2 host = new UIEvent2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Returns whether or not the "meta" key was pressed during the event firing.
     * @return whether or not the "meta" key was pressed during the event firing
     */
    @Getter
    public boolean getMetaKey() {
        return metaKey_;
    }

    /**
     * @param metaKey whether Meta has been pressed during this event or not
     */
    protected void setMetaKey(final boolean metaKey) {
        metaKey_ = metaKey;
    }

    /**
     * Returns some detail information about the event, depending on the event type. For mouse events,
     * the detail property indicates how many times the mouse has been clicked in the same location for
     * this event.
     *
     * @return some detail information about the event, depending on the event type
     */
    @Getter
    public long getDetail() {
        return detail_;
    }

    /**
     * Sets the detail information for this event.
     *
     * @param detail the detail information for this event
     */
    protected void setDetail(final long detail) {
        detail_ = detail;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(UIEvent2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("UIEvent", 
                    staticHandle("constructor", UIEvent2.class, boolean.class, Object.class),
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
            return "UIEvent";
        }
    }
}
