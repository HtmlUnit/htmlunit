/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FUNCTION_TOSTRING_ENUMERATED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IMAGE_HTML_IMAGE_ELEMENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IMAGE_OBJECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XMLHTTPREQUEST_OBJECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XSLTPROCESSOR_OBJECT;

import java.lang.reflect.Member;
import java.util.LinkedHashSet;
import java.util.Set;

import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A FunctionObject that returns IDs of this object and all its parent classes.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class RecursiveFunctionObject extends FunctionObject {

    /**
     * The constructor.
     * @param name the name of the function
     * @param methodOrConstructor a {@link Member} that defines the object
     * @param scope the enclosing scope of function
     */
    public RecursiveFunctionObject(final String name, final Member methodOrConstructor,
            final Scriptable scope) {
        super(name, methodOrConstructor, scope);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        if (super.has(name, start)) {
            return true;
        }
        if ("toString".equals(name) && getBrowserVersion().hasFeature(JS_FUNCTION_TOSTRING_ENUMERATED)) {
            return true;
        }
        for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass();
                c != null; c = c.getSuperclass()) {
            final Object scripatble = getParentScope().get(c.getSimpleName(), this);
            if (scripatble instanceof Scriptable) {
                if (((Scriptable) scripatble).has(name, start)) {
                    return true;
                }
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
        if (getBrowserVersion().hasFeature(JS_FUNCTION_TOSTRING_ENUMERATED)) {
            objects.add("toString");
        }
        for (final Object o : super.getIds()) {
            objects.add(o);
        }
        for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass();
                c != null; c = c.getSuperclass()) {
            final Object scripatble = getParentScope().get(c.getSimpleName(), this);
            if (scripatble instanceof Scriptable) {
                for (Object id : ((Scriptable) scripatble).getIds()) {
                    objects.add(id);
                }
            }
        }
        return objects.toArray(new Object[objects.size()]);
    }

    /**
     * Gets the browser version currently used.
     * @return the browser version
     */
    public BrowserVersion getBrowserVersion() {
        return ((Window) getParentScope()).getBrowserVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> typeHint) {
        if ("HTMLImageElement".equals(getFunctionName()) && getBrowserVersion().hasFeature(JS_IMAGE_OBJECT)) {
            return "[object " + getFunctionName() + ']';
        }
        if ("XSLTProcessor".equals(getFunctionName()) && getBrowserVersion().hasFeature(JS_XSLTPROCESSOR_OBJECT)) {
            return "[object " + getFunctionName() + ']';
        }
        if ("XMLHttpRequest".equals(getFunctionName()) && getBrowserVersion().hasFeature(JS_XMLHTTPREQUEST_OBJECT)) {
            return "[object " + getFunctionName() + ']';
        }
        return super.getDefaultValue(typeHint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeOf() {
        if ("XMLHttpRequest".equals(getFunctionName()) && getBrowserVersion().hasFeature(JS_XMLHTTPREQUEST_OBJECT)) {
            return "object";
        }
        return super.getTypeOf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFunctionName() {
        final String functionName = super.getFunctionName();
        if ("Image".equals(functionName) && getBrowserVersion().hasFeature(JS_IMAGE_HTML_IMAGE_ELEMENT)) {
            return "HTMLImageElement";
        }
        return functionName;
    }
}
