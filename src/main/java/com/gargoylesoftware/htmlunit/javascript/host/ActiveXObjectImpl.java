/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * An implementation of native ActiveX components using <a href="http://jacob-project.wiki.sourceforge.net/">Jacob</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class ActiveXObjectImpl extends SimpleScriptable {

    private static final long serialVersionUID = 6954481782205807262L;

    private final Class< ? > activeXComponentClass_ = Class.forName("com.jacob.activeX.ActiveXComponent");
    private final Object activXComponent_;
    private final Method getMethod_;
    private final Method callMethod_;

    /**
     * Constructs a new instance.
     *
     * @param activeXName ActiveX object name
     * @throws Exception if failed to initiate Jacob
     */
    @SuppressWarnings("unchecked")
    public ActiveXObjectImpl(final String activeXName) throws Exception {
        getMethod_ = activeXComponentClass_.getMethod("getProperty", String.class);
        activXComponent_ = activeXComponentClass_.getConstructor(String.class).newInstance(activeXName);
        final Class dispatchClass = Class.forName("com.jacob.com.Dispatch");
        callMethod_ = dispatchClass.getMethod("callN", dispatchClass, String.class, Object[].class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        try {
            return getMethod_.invoke(activXComponent_, name);
        }
        catch (final Exception e) {
            return new Function() {
                public Object call(final Context arg0, final Scriptable arg1, final Scriptable arg2,
                    final Object[] arg3) {
                    try {
                        return callMethod_.invoke(null, activXComponent_, name, arg3);
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
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        try {
            final Method setMethod = activeXComponentClass_.getMethod("setProperty", String.class, value.getClass());
            setMethod.invoke(activXComponent_, name, value);
        }
        catch (final Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }
}
