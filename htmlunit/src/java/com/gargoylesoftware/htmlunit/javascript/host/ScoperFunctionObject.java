/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * Object f in "f = new Function("...");" has not the current execution scope as its
 * parent scope and can't find objects in this scope when it is runned.
 * This class wrapps the native object for "Function" and fixes the problem by
 * seting the parent scope of the object created in {@link #construct(Context, Scriptable, Object[])}.
 * <p>
 * This class should disappear when the bug is fixed by Rhino: 
 * <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=277462">bug 277462</a>
 * </p>
 * <p>
 * The problem is tested by 
 * {@link com.gargoylesoftware.htmlunit.javascript.JavaScriptEngineTest#testScopeOfNestedNewFunction()}
 * </p>
 * @version $Revision$
 * @author Marc Guillemot
 * @author Mike Bowler
 */
class ScoperFunctionObject implements Function {
    private final Function wrapped_;
    private final Scriptable scope_;

    /**
     * Create an instance.
     * @param wrapped The wrapped function.
     * @param scope The scope.
     */
    public ScoperFunctionObject(Function wrapped, final Scriptable scope) {
        wrapped_ = wrapped;
        scope_ = scope;
    }
    
    /** @see Function#call(Context,Scriptable,Scriptable,Object[]) */
    public Object call(
            final Context cx, final Scriptable scope, final Scriptable thisObj,
            final Object[] args) {
        return wrapped_.call(cx, scope, thisObj, args);
    }

    /** @see Function#construct(Context,Scriptable,Object[]) */
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        final Scriptable o = wrapped_.construct(cx, scope, args);
        o.setParentScope(scope_);

        return o;
    }

    /** @see Function#delete(int) */
    public void delete(final int index) {
        wrapped_.delete(index);
    }
    
    /** @see Function#delete(String) */
    public void delete(final String name) {
        wrapped_.delete(name);
    }
    
    /** @see Object#equals(Object) */
    public boolean equals(final Object obj) {
        return wrapped_.equals(obj);
    }
    
    /** @see Function#get(int,Scriptable) */
    public Object get(final int index, final Scriptable start) {
        return wrapped_.get(index, start);
    }
    
    /** @see Function#get(String,Scriptable) */
    public Object get(final String name, final Scriptable start) {
        return wrapped_.get(name, start);
    }
    
    /** @see Function#getClassName() */
    public String getClassName() {
        return wrapped_.getClassName();
    }
    
    /** @see Function#getDefaultValue(Class) */
    public Object getDefaultValue(final Class hint) {
        return wrapped_.getDefaultValue(hint);
    }

    /** @see Function#getIds() */
    public Object[] getIds() {
        return wrapped_.getIds();
    }

    /** @see Function#getParentScope() */
    public Scriptable getParentScope() {
        return wrapped_.getParentScope();
    }

    /** @see Function#getPrototype() */
    public Scriptable getPrototype() {
        return wrapped_.getPrototype();
    }

    /** @see Function#has(int,Scriptable) */
    public boolean has(final int index, final Scriptable start) {
        return wrapped_.has(index, start);
    }

    /** @see Function#has(String,Scriptable) */
    public boolean has(final String name, final Scriptable start) {
        return wrapped_.has(name, start);
    }

    /** @see Object#hashCode() */
    public int hashCode() {
        return wrapped_.hashCode();
    }

    /** @see Function#hasInstance(Scriptable) */
    public boolean hasInstance(final Scriptable instance) {
        return wrapped_.hasInstance(instance);
    }

    /** @see Function#put(int,Scriptable,Object) */
    public void put(final int index, final Scriptable start, final Object value) {
        wrapped_.put(index, start, value);
    }

    /** @see Function#put(String,Scriptable,Object) */
    public void put(final String name, final Scriptable start, final Object value) {
        wrapped_.put(name, start, value);
    }

    /** @see Function#setParentScope(Scriptable) */
    public void setParentScope(final Scriptable parent) {
        wrapped_.setParentScope(parent);
    }

    /** @see Function#setPrototype(Scriptable) */
    public void setPrototype(final Scriptable prototype) {
        wrapped_.setPrototype(prototype);
    }

    /** @see Object#toString() */
    public String toString() {
        return wrapped_.toString();
    }
}
