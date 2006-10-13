/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Simple wrapper to make "normal" object scriptable according to specific configuration
 * and allowing use of index properties. 
 * TODO: Configuration of the
 * properties and functions should occur from the xml configuration according to
 * the browser to simulate.
 * 
 * @author Marc Guillemot
 * @version $Revision: 1158 $
 */
public class ScriptableWrapper extends ScriptableObject {
    private static final long serialVersionUID = 1736378450382368760L;

    private final Map properties_ = new HashMap();

    private Method getByIndexMethod_;
    private final Object javaObject_;
    private final String jsClassName_;
    private Method getByNameFallback_;

    /**
     * Constructs a wrapper for the java object. 
     * @param scope the scope of the executing script
     * @param javaObject the javaObject to wrap
     * @param staticType the static type of the object
     */
    public ScriptableWrapper(final Scriptable scope, final Object javaObject,
            final Class staticType) {
        javaObject_ = javaObject;
        setParentScope(scope);

        // all these information should come from the xml js configuration file
        // just for a first time...
        if (NodeList.class.equals(staticType)
                || NamedNodeMap.class.equals(staticType)) {
            try {
                jsClassName_ = ClassUtils.getShortClassName(staticType);

                // is there a better way that would avoid to keep local
                // information?
                // it seems that defineProperty with only accepts delegate if
                // its method takes a ScriptableObject
                // as parameter.
                final Method length = javaObject.getClass().getMethod(
                        "getLength", ArrayUtils.EMPTY_CLASS_ARRAY);
                properties_.put("length", length);

                final Method item = javaObject.getClass().getMethod("item",
                        new Class[] { Integer.TYPE });
                defineProperty("item", new MethodWrapper("item", staticType, new Class[] { Integer.TYPE }),
                        0);

                final Method toString = getClass().getMethod("jsToString",
                        ArrayUtils.EMPTY_CLASS_ARRAY);
                defineProperty("toString", new FunctionObject("toString",
                        toString, this), 0);

                getByIndexMethod_ = item;

                if (NamedNodeMap.class.equals(staticType)) {
                    final Method getNamedItem = javaObject.getClass()
                            .getMethod("getNamedItem",
                                    new Class[] { String.class });
                    defineProperty("getNamedItem", 
                            new MethodWrapper("getNamedItem", staticType, new Class[] { String.class }), 0);

                    getByNameFallback_ = getNamedItem;
                }
            }
            catch (final Exception e) {
                throw new RuntimeException("Method not found", e);
            }
        }
        else {
            throw new RuntimeException("Unknown type: " + staticType.getName());
        }
    }

    /**
     * {@inheritDoc}
     * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
     */
    public Object get(final String name, final Scriptable start) {
        final Method propertyGetter = (Method) properties_.get(name);
        final Object response;
        if (propertyGetter != null) {
            response = invoke(propertyGetter);
        }
        else {
            final Object fromSuper = super.get(name, start);
            if (fromSuper != Scriptable.NOT_FOUND) {
                response = fromSuper;
            }
            else {
                final Object byName = invoke(getByNameFallback_,
                        new Object[] { name });
                if (byName != null) {
                    response = byName;
                }
                else {
                    response = Scriptable.NOT_FOUND;
                }
            }
        }

        return Context.javaToJS(response, ScriptableObject
                .getTopLevelScope(start));
    }

    /**
     * {@inheritDoc}
     * @see org.mozilla.javascript.ScriptableObject#has(java.lang.String,
     *      org.mozilla.javascript.Scriptable)
     */
    public boolean has(final String name, final Scriptable start) {
        return properties_.containsKey(name) || super.has(name, start);
    }

    /**
     * Invokes the method on the wrapped object
     * @param method the method to invoke
     * @return the invocation result
     */
    protected Object invoke(final Method method) {
        return invoke(method, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Invokes the method on the wrapped object
     * @param method the method to invoke
     * @param args the argument to pass to the method
     * @return the invocation result
     */
    protected Object invoke(final Method method, final Object[] args) {
        try {
            return method.invoke(javaObject_, args);
        }
        catch (final Exception e) {
            throw new RuntimeException(
                    "Invocation of method on java object failed", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.mozilla.javascript.ScriptableObject#get(int, org.mozilla.javascript.Scriptable)
     */
    public Object get(final int index, final Scriptable start) {
        if (getByIndexMethod_ != null) {
            final Object byIndex = invoke(getByIndexMethod_,
                    new Object[] { new Integer(index) });
            return Context.javaToJS(byIndex, ScriptableObject
                    .getTopLevelScope(start));
        }
        else {
            return super.get(index, start);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.mozilla.javascript.ScriptableObject#getDefaultValue(java.lang.Class)
     */
    public Object getDefaultValue(final Class hint) {
        if (hint == null || String.class == null) {
            return jsToString();
        }
        else {
            return super.getDefaultValue(hint);
        }
    }

    /**
     * To use as "toString" function in javascript
     * @return the string representation
     */
    public String jsToString() {
        return "[object " + getClassName() + "]";
    }

    /**
     * {@inheritDoc}
     * @see org.mozilla.javascript.ScriptableObject#getClassName()
     */
    public String getClassName() {
        return jsClassName_;
    }
    
    /**
     * Gets the java object made availabe to javascript through this wrapper
     * @return the wrapped object
     */
    public Object getWrappedObject() {
        return javaObject_;
    }
}
