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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * A JavaScript object for {@code Attr}.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-63764602">W3C DOM Level 2</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535187.aspx">MSDN documentation</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@ScriptClass
public class Attr2 extends Node2 {

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static Attr2 constructor(final boolean newObj, final Object self) {
        final Attr2 host = new Attr2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Returns the name of the attribute.
     * @return the name of the attribute
     */
    @Getter
    public String getName() {
        return getDomNodeOrDie().getName();
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute
     */
    @Override
    public String getNodeValue() {
        return getValue();
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute
     */
    @Getter
    public String getValue() {
        return getDomNodeOrDie().getValue();
    }

    /**
     * Sets the value of this attribute.
     * @param value the new value of this attribute
     */
    @Setter
    public void setValue(final String value) {
        getDomNodeOrDie().setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr getDomNodeOrDie() throws IllegalStateException {
        return (DomAttr) super.getDomNodeOrDie();
    }

    /**
     * Detaches this attribute from the parent HTML element after caching the attribute value.
     */
    public void detachFromParent() {
        final DomAttr domNode = getDomNodeOrDie();
        final DomElement parent = (DomElement) domNode.getParentNode();
        if (parent != null) {
            domNode.setValue(parent.getAttribute(getName()));
        }
        domNode.remove();
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Attr2.class,
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
            super("Attr",
                    staticHandle("constructor", Attr2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("Attr");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("Attr");
        }
    }
}
