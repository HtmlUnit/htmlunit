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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;

/**
 * JavaScript object representing a {@code PointerEvent}.
 * @see <a href="http://www.w3.org/TR/pointerevents/">W3C Spec</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ie/hh772103.aspx">MSDN</a>
 *
 * @author Frank Danek
 * @author Ahmed Ashour
 */
public class PointerEvent2 extends MouseEvent2 {

    private int pointerId_;
    private int width_;
    private int height_;
    private double pressure_;
    private int tiltX_;
    private int tiltY_;
    private String pointerType_ = "";
    private boolean isPrimary_;

    /**
     * Creates a new event instance.
     */
    public PointerEvent2() {
    }

    /**
     * Creates a new event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link #BUTTON_LEFT}, {@link #BUTTON_MIDDLE} or {@link #BUTTON_RIGHT}
     */
    public PointerEvent2(final DomNode domNode, final String type, final boolean shiftKey,
            final boolean ctrlKey, final boolean altKey, final int button) {
        super(domNode, type, shiftKey, ctrlKey, altKey, button);
        setDetail(0);

        pointerId_ = 1;
        width_ = 1;
        height_ = 1;
        pointerType_ = "mouse";
        isPrimary_ = true;
    }

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static PointerEvent2 constructor(final boolean newObj, final Object self) {
        final PointerEvent2 host = new PointerEvent2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(PointerEvent2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Object constructor. */
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("PointerEvent");
        }
    }

}
