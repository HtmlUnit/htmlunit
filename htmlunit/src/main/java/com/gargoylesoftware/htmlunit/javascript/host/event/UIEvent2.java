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

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * JavaScript object representing a UI event. For general information on which properties and functions should be
 * supported, see <a href="http://www.w3.org/TR/DOM-Level-3-Events/events.html#Events-UIEvent">DOM Level 3 Events</a>.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@ScriptClass
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

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
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
    public Boolean getMetaKey() {
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

    /**
     * Implementation of the DOM Level 3 Event method for initializing the UI event.
     *
     * @param type the event type
     * @param bubbles can the event bubble
     * @param cancelable can the event be canceled
     * @param view the view to use for this event
     * @param detail the detail to set for the event
     */
    @Function({CHROME,  FF, IE})
    public void initUIEvent(
            final String type,
            final boolean bubbles,
            final boolean cancelable,
            final Object view,
            final int detail) {
        initEvent(type, bubbles, cancelable);
        // Ignore the view parameter; we always use the window.
        setDetail(detail);
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

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("UIEvent",
                    staticHandle("constructor", UIEvent2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("UIEvent");
        }
    }
}
