/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

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
     * @see org.mozilla.javascript.ScriptableObject#getClassName()
     * @return a name based on the method name
     */
    @Override
    public String getClassName() {
        return "function " + method_.getName();
    }

    /**
     * @see org.mozilla.javascript.Function#call(Context, Scriptable, Scriptable, Object[])
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
     * @see org.mozilla.javascript.Function#construct(Context, Scriptable, Object[])
     * {@inheritDoc}
     */
    public Scriptable construct(final Context context, final Scriptable scope, final Object[] args) {
        throw Context.reportRuntimeError("Function " + method_.getName() + " can't be used as a constructor");
    }

}
