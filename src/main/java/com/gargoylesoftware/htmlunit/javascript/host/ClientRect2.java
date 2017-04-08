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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * Specifies a rectangle that contains a line of text in either an element or a TextRange object.
 *
 * @author Ahmed Ashour
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535906.aspx">MSDN Documentation</a>
 */
public class ClientRect2 extends SimpleScriptObject {

    private int bottom_;
    private int left_;
    private int right_;
    private int top_;

    /**
     * Creates an instance, with the given coordinates.
     *
     * @param bottom the bottom coordinate of the rectangle surrounding the object content
     * @param left the left coordinate of the rectangle surrounding the object content
     * @param right the right coordinate of the rectangle surrounding the object content
     * @param top the top coordinate of the rectangle surrounding the object content
     */
    public ClientRect2(final int bottom, final int left, final int right, final int top) {
        bottom_ = bottom;
        left_ = left;
        right_ = right;
        top_ = top;
    }

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static ClientRect2 constructor(final boolean newObj, final Object self) {
        final ClientRect2 host = new ClientRect2(0, 0, 0, 0);
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Sets the bottom coordinate of the rectangle surrounding the object content.
     * @param bottom the bottom coordinate of the rectangle surrounding the object content
     */
    @Setter
    public void setBottom(final int bottom) {
        bottom_ = bottom;
    }

    /**
     * Returns the bottom coordinate of the rectangle surrounding the object content.
     * @return the bottom coordinate of the rectangle surrounding the object content
     */
    @Getter
    public int getBottom() {
        return bottom_;
    }

    /**
     * Sets the left coordinate of the rectangle surrounding the object content.
     * @param left the left coordinate of the rectangle surrounding the object content
     */
    @Setter
    public void setLeft(final int left) {
        left_ = left;
    }

    /**
     * Returns the left coordinate of the rectangle surrounding the object content.
     * @return the left coordinate of the rectangle surrounding the object content
     */
    @Getter
    public int getLeft() {
        return left_;
    }

    /**
     * Sets the right coordinate of the rectangle surrounding the object content.
     * @param right the right coordinate of the rectangle surrounding the object content
     */
    @Setter
    public void setRight(final int right) {
        right_ = right;
    }

    /**
     * Returns the right coordinate of the rectangle surrounding the object content.
     * @return the right coordinate of the rectangle surrounding the object content
     */
    @Getter
    public int getRight() {
        return right_;
    }

    /**
     * Sets the top coordinate of the rectangle surrounding the object content.
     * @param top the top coordinate of the rectangle surrounding the object content
     */
    @Setter
    public void setTop(final int top) {
        top_ = top;
    }

    /**
     * Returns the top coordinate of the rectangle surrounding the object content.
     * @return the top coordinate of the rectangle surrounding the object content
     */
    @Getter
    public int getTop() {
        return top_;
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @Getter
    public int getWidth() {
        return getRight() - getLeft();
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @Getter
    public int getHeight() {
        return getBottom() - getTop();
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(ClientRect2.class,
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
            super("ClientRect",
                    staticHandle("constructor", ClientRect2.class, boolean.class, Object.class),
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
            return "ClientRect";
        }
    }
}
