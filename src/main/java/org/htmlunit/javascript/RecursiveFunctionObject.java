/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript;

import java.lang.reflect.Member;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import org.htmlunit.javascript.configuration.ClassConfiguration;
import org.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;

/**
 * A FunctionObject that returns IDs of this object and all its parent classes.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class RecursiveFunctionObject extends FunctionObject {

    private final BrowserVersion browserVersion_;

    /**
     * The constructor.
     * @param name the name of the function
     * @param methodOrConstructor a {@link Member} that defines the object
     * @param scope the enclosing scope of function
     * @param browserVersion the browserVersion
     */
    public RecursiveFunctionObject(final String name, final Member methodOrConstructor,
            final Scriptable scope, final BrowserVersion browserVersion) {
        super(name, methodOrConstructor, scope);
        browserVersion_ = browserVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        if (super.has(name, start)) {
            return true;
        }
        for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass();
                c != null; c = c.getSuperclass()) {
            final Object scripatble = getParentScope().get(c.getSimpleName(), this);
            if (scripatble instanceof Scriptable && ((Scriptable) scripatble).has(name, start)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getIds() {
        final Set<Object> objects = new LinkedHashSet<>();
        for (final Object obj : super.getIds()) {
            objects.add(obj);
        }

        for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass();
                c != null; c = c.getSuperclass()) {
            final Object scripatble = getParentScope().get(c.getSimpleName(), this);
            if (scripatble instanceof Scriptable) {
                for (final Object obj : ((Scriptable) scripatble).getIds()) {
                    objects.add(obj);
                }
            }
        }
        return objects.toArray(new Object[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        Object value = super.get(name, start);
        if (value != NOT_FOUND) {
            return value;
        }

        Class<?> klass = getPrototypeProperty().getClass();

        while (value == NOT_FOUND && HtmlUnitScriptable.class.isAssignableFrom(klass)) {
            final ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(
                    klass.asSubclass(HtmlUnitScriptable.class), browserVersion_);
            if (config != null) {
                final List<ConstantInfo> constants = config.getConstants();
                if (constants != null) {
                    for (final ConstantInfo constantInfo : constants) {
                        if (constantInfo.getName().equals(name)) {
                            value = ScriptableObject.getProperty((Scriptable) getPrototypeProperty(), name);
                            break;
                        }
                    }
                }
            }
            klass = klass.getSuperclass();
        }
        return value;
    }

    /**
     * Make this public.
     */
    @Override
    public Scriptable getClassPrototype() {
        return super.getClassPrototype();
    }
}
