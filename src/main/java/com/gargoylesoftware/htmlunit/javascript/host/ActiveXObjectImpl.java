/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import java.lang.reflect.Method;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * An implementation of native ActiveX components using <a href="http://jacob-project.wiki.sourceforge.net/">Jacob</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class ActiveXObjectImpl extends SimpleScriptable {

    private static final Class< ? > activeXComponentClass_;

    /** ActiveXComponent.getProperty(String) */
    private static final Method METHOD_getProperty_;
    private final Object object_;

    /** Dispatch.callN(Dispatch, String, Object[]) */
    private static final Method METHOD_callN_;

    /** Variant.getvt() */
    private static final Method METHOD_getvt_;

    /** Variant.getDispatch() */
    private static final Method METHOD_getDispatch_;

    static {
        try {
            activeXComponentClass_ = Class.forName("com.jacob.activeX.ActiveXComponent");
            METHOD_getProperty_ = activeXComponentClass_.getMethod("getProperty", String.class);
            final Class< ? > dispatchClass = Class.forName("com.jacob.com.Dispatch");
            METHOD_callN_ = dispatchClass.getMethod("callN", dispatchClass, String.class, Object[].class);
            final Class< ? > variantClass = Class.forName("com.jacob.com.Variant");
            METHOD_getvt_ = variantClass.getMethod("getvt");
            METHOD_getDispatch_ = variantClass.getMethod("getDispatch");
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a new instance.
     *
     * @param activeXName ActiveX object name
     * @throws Exception if failed to initiate Jacob
     */
    public ActiveXObjectImpl(final String activeXName) throws Exception {
        this(activeXComponentClass_.getConstructor(String.class).newInstance(activeXName));
    }

    private ActiveXObjectImpl(final Object object) {
        object_ = object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        try {
            final Object variant = METHOD_getProperty_.invoke(object_, name);
            return wrapIfNecessary(variant);
        }
        catch (final Exception e) {
            return new Function() {
                public Object call(final Context arg0, final Scriptable arg1, final Scriptable arg2,
                    final Object[] arg3) {
                    try {
                        final Object rv = METHOD_callN_.invoke(null, object_, name, arg3);
                        return wrapIfNecessary(rv);
                    }
                    catch (final Exception e) {
                        throw Context.throwAsScriptRuntimeEx(e);
                    }
                }

                public Scriptable construct(final Context arg0, final Scriptable arg1, final Object[] arg2) {
                    throw new UnsupportedOperationException();
                }

                public void delete(final String arg0) {
                    throw new UnsupportedOperationException();
                }

                public void delete(final int arg0) {
                    throw new UnsupportedOperationException();
                }

                public Object get(final String arg0, final Scriptable arg1) {
                    throw new UnsupportedOperationException();
                }

                public Object get(final int arg0, final Scriptable arg1) {
                    throw new UnsupportedOperationException();
                }

                public String getClassName() {
                    throw new UnsupportedOperationException();
                }

                public Object getDefaultValue(final Class< ? > arg0) {
                    throw new UnsupportedOperationException();
                }

                public Object[] getIds() {
                    throw new UnsupportedOperationException();
                }

                public Scriptable getParentScope() {
                    throw new UnsupportedOperationException();
                }

                public Scriptable getPrototype() {
                    throw new UnsupportedOperationException();
                }

                public boolean has(final String arg0, final Scriptable arg1) {
                    throw new UnsupportedOperationException();
                }

                public boolean has(final int arg0, final Scriptable arg1) {
                    throw new UnsupportedOperationException();
                }

                public boolean hasInstance(final Scriptable arg0) {
                    throw new UnsupportedOperationException();
                }

                public void put(final String arg0, final Scriptable arg1, final Object arg2) {
                    throw new UnsupportedOperationException();
                }

                public void put(final int arg0, final Scriptable arg1, final Object arg2) {
                    throw new UnsupportedOperationException();
                }

                public void setParentScope(final Scriptable arg0) {
                    throw new UnsupportedOperationException();
                }

                public void setPrototype(final Scriptable arg0) {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    /**
     * Wrap the specified variable into {@link ActiveXObjectImpl} if its type is Variant.VariantDispatch.
     * @param variant the variant to potentially wrap
     * @return either the variant if it is basic type or wrapped {@link ActiveXObjectImpl}
     */
    private Object wrapIfNecessary(final Object variant) throws Exception {
        if (((Short) METHOD_getvt_.invoke(variant)) == 9) { //Variant.VariantDispatch
            return new ActiveXObjectImpl(METHOD_getDispatch_.invoke(variant));
        }
        return variant;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        try {
            final Method setMethod = activeXComponentClass_.getMethod("setProperty", String.class, value.getClass());
            setMethod.invoke(object_, name, Context.toString(value));
        }
        catch (final Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }
}
