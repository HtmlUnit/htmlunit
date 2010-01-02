/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Wraps a java method to make it available as a JavaScript function
 * (more flexible than Rhino's {@link FunctionObject}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class MethodWrapper extends ScriptableObject implements Function {

    private static final long serialVersionUID = 6106771000496895783L;
    private final Class< ? > clazz_;
    private final Method method_;
    private final int[] jsTypeTags_;

    /**
     * Facility constructor to wrap a method without arguments
     * @param methodName the name of the method to wrap
     * @param clazz the class declaring the method
     * @throws NoSuchMethodException if the method is no found
     */
    MethodWrapper(final String methodName, final Class< ? > clazz) throws NoSuchMethodException {
        this(methodName, clazz, ArrayUtils.EMPTY_CLASS_ARRAY);
    }

    /**
     * Wraps a method as a JavaScript function.
     * @param methodName the name of the method to wrap
     * @param clazz the class declaring the method
     * @param parameterTypes the types of the method's parameter
     * @throws NoSuchMethodException if the method is no found
     */
    MethodWrapper(final String methodName, final Class< ? > clazz, final Class< ? >[] parameterTypes)
        throws NoSuchMethodException {

        clazz_ = clazz;
        method_ = clazz.getMethod(methodName, parameterTypes);
        jsTypeTags_ = new int[parameterTypes.length];
        int i = 0;
        for (final Class< ? > klass : parameterTypes) {
            jsTypeTags_[i++] = FunctionObject.getTypeTag(klass);
        }
    }

    /**
     * @see net.sourceforge.htmlunit.corejs.javascript.ScriptableObject#getClassName()
     * @return a name based on the method name
     */
    @Override
    public String getClassName() {
        return "function " + method_.getName();
    }

    /**
     * @see net.sourceforge.htmlunit.corejs.javascript.Function#call(Context, Scriptable, Scriptable, Object[])
     * {@inheritDoc}
     */
    public Object call(final Context context, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        final Object javaResp;
        if (thisObj instanceof ScriptableWrapper) {
            final ScriptableWrapper wrapper = (ScriptableWrapper) thisObj;
            final Object wrappedObject = wrapper.getWrappedObject();
            if (clazz_.isInstance(wrappedObject)) {
                // convert arguments
                final Object[] javaArgs = convertJSArgsToJavaArgs(context, scope, args);
                try {
                    javaResp = method_.invoke(wrappedObject, javaArgs);
                }
                catch (final Exception e) {
                    throw Context.reportRuntimeError("Exception calling wrapped function "
                            + method_.getName() + ": " + e.getMessage());
                }
            }
            else {
                throw buildInvalidCallException(thisObj);
            }

        }
        else {
            throw buildInvalidCallException(thisObj);
        }

        final Object jsResp = Context.javaToJS(javaResp, ScriptableObject.getTopLevelScope(scope));
        return jsResp;
    }

    private RuntimeException buildInvalidCallException(final Scriptable thisObj) {
        return Context.reportRuntimeError("Function " + method_.getName()
                + " called on incompatible object: " + thisObj);
    }

    /**
     * Converts js arguments to java arguments
     * @param context the current context
     * @param scope the current scope
     * @param jsArgs the JavaScript arguments
     * @return the java arguments
     */
    Object[] convertJSArgsToJavaArgs(final Context context, final Scriptable scope, final Object[] jsArgs) {
        if (jsArgs.length != jsTypeTags_.length) {
            throw Context.reportRuntimeError("Bad number of parameters for function " + method_.getName()
                    + ": expected " + jsTypeTags_.length + " got " + jsArgs.length);
        }
        final Object[] javaArgs = new Object[jsArgs.length];
        int i = 0;
        for (final Object object : jsArgs) {
            javaArgs[i] = FunctionObject.convertArg(context, scope, object, jsTypeTags_[i++]);
        }
        return javaArgs;
    }

    /**
     * @see net.sourceforge.htmlunit.corejs.javascript.Function#construct(Context, Scriptable, Object[])
     * {@inheritDoc}
     */
    public Scriptable construct(final Context context, final Scriptable scope, final Object[] args) {
        throw Context.reportRuntimeError("Function " + method_.getName() + " can't be used as a constructor");
    }

}
