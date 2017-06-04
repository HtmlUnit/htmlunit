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

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

/**
 * A JavaScript object for {@code ProgressEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@ScriptClass
public class ProgressEvent2 extends Event2 {

    private boolean lengthComputable_;
    private long loaded_;
    private long total_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static ProgressEvent2 constructor(final boolean newObj, final Object self) {
        final ProgressEvent2 host = new ProgressEvent2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Default constructor.
     */
    public ProgressEvent2() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
//    @JsxConstructor({CHROME, FF, EDGE})
    public void jsConstructor(final String type, final ScriptObject details) {
        super.jsConstructor(type, details);

        if (details != null /*&& ScriptRuntime.UNDEFINED != details*/) {
            final Object lengthComputable = details.get("lengthComputable");
            if (lengthComputable instanceof Boolean) {
                lengthComputable_ = (Boolean) lengthComputable;
            }
            else {
                lengthComputable_ = Boolean.parseBoolean(lengthComputable.toString());
            }

            final Object loaded = details.get("loaded");
            if (loaded instanceof Long) {
                loaded_ = (Long) loaded;
            }
            else if (loaded instanceof Double) {
                loaded_ = ((Double) loaded).longValue();
            }
            else {
                try {
                    loaded_ = Long.parseLong(loaded.toString());
                }
                catch (final NumberFormatException e) {
                    // ignore
                }
            }

            final Object total = details.get("total");
            if (total instanceof Long) {
                total_ = (Long) total;
            }
            else if (total instanceof Double) {
                total_ = ((Double) total).longValue();
            }
            else {
                try {
                    total_ = Long.parseLong(details.get("total").toString());
                }
                catch (final NumberFormatException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Creates a new event instance.
     * @param target the event target
     * @param type the event type
     */
    public ProgressEvent2(final EventTarget2 target, final String type) {
        super(target, type);
    }

    /**
     * Returns the lengthComputable property from the event.
     * @return the lengthComputable property from the event.
     */
    @JsxGetter
    public boolean isLengthComputable() {
        return lengthComputable_;
    }

    /**
     * Sets the lengthComputable information for this event.
     *
     * @param lengthComputable the lengthComputable information for this event
     */
    public void setLengthComputable(final boolean lengthComputable) {
        lengthComputable_ = lengthComputable;
    }

    /**
     * Returns the loaded property from the event.
     * @return the loaded property from the event.
     */
    @JsxGetter
    public long getLoaded() {
        return loaded_;
    }

    /**
     * Sets the loaded information for this event.
     *
     * @param loaded the loaded information for this event
     */
    public void setLoaded(final long loaded) {
        loaded_ = loaded;
    }

    /**
     * Returns the total property from the event.
     * @return the total property from the event.
     */
    @JsxGetter
    public long getTotal() {
        return total_;
    }

    /**
     * Sets the total information for this event.
     *
     * @param total the total information for this event
     */
    public void setTotal(final long total) {
        total_ = total;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(ProgressEvent2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("ProgressEvent",
                    staticHandle("constructor", ProgressEvent2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends PrototypeObject {

        Prototype() {
            ScriptUtils.initialize(this);
        }

        @Override
        public String getClassName() {
            return "ProgressEvent";
        }
    }
}
