/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Method;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Base class for Rhino host objects in HtmlUnit (not bound to a DOM node).
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlUnitScriptable extends ScriptableObject {
    private String className_;

    /**
     * Returns the JavaScript class name.
     * @return the JavaScript class name
     */
    @Override
    public String getClassName() {
        if (className_ != null) {
            return className_;
        }
        if (getPrototype() != null) {
            return getPrototype().getClassName();
        }
        String className = getClass().getSimpleName();
        if (className.isEmpty()) {
            // for anonymous class
            className = getClass().getSuperclass().getSimpleName();
        }
        return className;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the class name.
     * @param className the class name.
     */
    public void setClassName(final String className) {
        className_ = className;
    }

    /**
     * {@inheritDoc}
     * Same as base implementation, but includes all methods inherited from super classes as well.
     */
    @Override
    public void defineProperty(final String propertyName, final Class<?> clazz, int attributes) {
        final int length = propertyName.length();
        if (length == 0) {
            throw new IllegalArgumentException();
        }
        final char[] buf = new char[3 + length];
        propertyName.getChars(0, length, buf, 3);
        buf[3] = Character.toUpperCase(buf[3]);
        buf[0] = 'g';
        buf[1] = 'e';
        buf[2] = 't';
        final String getterName = new String(buf);
        buf[0] = 's';
        final String setterName = new String(buf);

        final Method[] methods = clazz.getMethods();
        final Method getter = findMethod(methods, getterName);
        final Method setter = findMethod(methods, setterName);
        if (setter == null) {
            attributes |= ScriptableObject.READONLY;
        }
        defineProperty(propertyName, null, getter, setter, attributes);
    }

    /**
     * {@inheritDoc}
     * Same as base implementation, but includes all methods inherited from super classes as well.
     */
    @Override
    public void defineFunctionProperties(final String[] names, final Class<?> clazz, final int attributes) {
        final Method[] methods = clazz.getMethods();
        for (final String name : names) {
            final Method method = findMethod(methods, name);
            if (method == null) {
                throw Context.reportRuntimeError("Method \"" + name + "\" not found in \"" + clazz.getName() + '"');
            }
            final FunctionObject f = new FunctionObject(name, method, this);
            defineProperty(name, f, attributes);
        }
    }

    /**
     * Returns the method with the specified name.
     */
    private static Method findMethod(final Method[] methods, final String name) {
        for (final Method m : methods) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentScope(final Scriptable m) {
        if (m == this) {
            throw new IllegalArgumentException("Object can't be its own parentScope");
        }
        super.setParentScope(m);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> typeHint) {
        return "[object " + getClassName() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        try {
            super.put(name, start, value);
        }
        catch (final IllegalArgumentException e) {
            // is it the right place or should Rhino throw a RuntimeError instead of an IllegalArgumentException?
            throw Context.reportRuntimeError("'set "
                + name + "' called on an object that does not implement interface " + getClassName());
        }
    }
}
