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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * A JavaScript object for {@link HtmlInlineFrame}.
 *
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@ScriptClass
public class HTMLIFrameElement2 extends HTMLElement2 {

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static HTMLIFrameElement2 constructor(final boolean newObj, final Object self) {
        final HTMLIFrameElement2 host = new HTMLIFrameElement2();
        ScriptUtils.initialize(host);
        host.setProto(((Global) self).getPrototype(host.getClass()));
        return host;
    }

    /**
     * Returns the window the frame contains, if any.
     * @return the window
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref5.html">Gecko DOM Reference</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533692.aspx">MSDN documentation</a>
     */
    @Getter
    public Global getContentWindow() {
        return getFrame().getEnclosedWindow().getScriptableObject();
    }

    private BaseFrameElement getFrame() {
        return (BaseFrameElement) getDomNodeOrDie();
    }

    /**
     * Returns the value of the name attribute.
     * @return the value of this attribute
     */
    @Getter
    public String getName() {
        return getFrame().getNameAttribute();
    }

    /**
     * Sets the value of the name attribute.
     * @param name the new value
     */
    @Setter
    public void setName(final String name) {
        getFrame().setNameAttribute(name);
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLIFrameElement2.class,
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
            super("HTMLIFrameElement",
                    staticHandle("constructor", HTMLIFrameElement2.class, boolean.class, Object.class),
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
            return "HTMLIFrameElement";
        }
    }
}
