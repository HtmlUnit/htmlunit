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

import java.lang.reflect.Field;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Implements a workaround for Rhino bug https://bugzilla.mozilla.org/show_bug.cgi?id=374918.
 * Once this bug is solved, this class can be safely removed. Unit tests will ensure that this works
 * correctly.<br/>
 * Bug Description:
 * string primitive prototype is badly resolved when many top scopes are involved.<br/>
 * Workaround:
 * a custom prototype is placed on the String object. It wraps the original one and fix bad scope resolution
 * when needed. For this purpose it needs to change the visibility of some Rhino private members to
 * access them and is likely to get broken on Rhino version updates.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class StringPrimitivePrototypeBugFixer implements Scriptable {
    private static final Field FieldPrototypeProperty_;
    private static final Field FieldContextLastInterpreterFrame_;
    private static Field FieldInterpreterCallFrameScope_;

    static {
        try {
            // BaseFunction's prototypeProperty
            FieldPrototypeProperty_ = BaseFunction.class.getDeclaredField("prototypeProperty");
            FieldPrototypeProperty_.setAccessible(true);
            // Context's lastInterpreterFrame
            FieldContextLastInterpreterFrame_ = Context.class.getDeclaredField("lastInterpreterFrame");
            FieldContextLastInterpreterFrame_.setAccessible(true);
        }
        catch (final Exception e) {
            throw new Error("Bad Rhino version: can't install custom String primitive prototype fix");
        }
    }

    /**
     * Install the workaround for the Rhino bug
     * @param topScope the top scope (which contains the standard objects)
     * @throws Exception if initialization fails
     */
    static void installWorkaround(final Scriptable topScope) throws Exception {
        final BaseFunction stringObj = (BaseFunction) topScope.get("String", topScope);
        final Scriptable stringObjPrototype = (Scriptable) FieldPrototypeProperty_.get(stringObj);
        final StringPrimitivePrototypeBugFixer prototypeWrapper =
            new StringPrimitivePrototypeBugFixer(stringObjPrototype);
        FieldPrototypeProperty_.set(stringObj, prototypeWrapper);
    }

    private final Scriptable wrapped_;

    StringPrimitivePrototypeBugFixer(final Scriptable wrapped) {
        wrapped_ = wrapped;
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final int index) {
        wrapped_.delete(index);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final String name) {
        wrapped_.delete(name);
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final int index, Scriptable start) {
        if (start == this) {
            start = wrapped_;
        }
        return wrapped_.get(index, start);
    }
    
    private Scriptable getRealScope() throws Exception {
        final Object o = FieldContextLastInterpreterFrame_.get(Context.getCurrentContext());
        if (FieldInterpreterCallFrameScope_ == null) {
            FieldInterpreterCallFrameScope_ = o.getClass().getDeclaredField("scope");
            FieldInterpreterCallFrameScope_.setAccessible(true);
        }
        return (Scriptable) FieldInterpreterCallFrameScope_.get(o);
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final String name, Scriptable start) {
        try {
            final Scriptable originalScope = getRealScope();
            final Scriptable originalTopScope = ScriptableObject.getTopLevelScope(originalScope);
            final Scriptable currentTopScope = ScriptableObject.getTopLevelScope(this);
            if (originalTopScope != currentTopScope) {
                final Scriptable s = (Scriptable) originalTopScope.get("String", originalTopScope);
                final Scriptable p = (Scriptable) s.get("prototype", s);
                return p.get(name, p);
            }
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }

        if (start == this) {
            start = wrapped_;
        }
        return wrapped_.get(name, start);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getClassName() {
        return wrapped_.getClassName();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object getDefaultValue(final Class hint) {
        return wrapped_.getDefaultValue(hint);
    }

    /**
     * {@inheritDoc}
     */
    public Object[] getIds() {
        return wrapped_.getIds();
    }

    /**
     * {@inheritDoc}
     */
    public Scriptable getParentScope() {
        return wrapped_.getParentScope();
    }

    /**
     * {@inheritDoc}
     */
    public Scriptable getPrototype() {
        return wrapped_.getPrototype();
    }

    /**
     * {@inheritDoc}
     */
    public boolean has(final int index, Scriptable start) {
        if (start == this) {
            start = wrapped_;
        }
        return wrapped_.has(index, start);
    }

    /**
     * {@inheritDoc}
     */
    public boolean has(final String name, Scriptable start) {
        if (start == this) {
            start = wrapped_;
        }
        return wrapped_.has(name, start);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasInstance(final Scriptable instance) {
        return wrapped_.hasInstance(instance);
    }

    /**
     * {@inheritDoc}
     */
    public void put(final int index, Scriptable start, final Object value) {
        if (start == this) {
            start = wrapped_;
        }
        wrapped_.put(index, start, value);
    }

    /**
     * {@inheritDoc}
     */
    public void put(final String name, Scriptable start, final Object value) {
        if (start == this) {
            start = wrapped_;
        }
        wrapped_.put(name, start, value);
    }

    /**
     * {@inheritDoc}
     */
    public void setParentScope(final Scriptable parent) {
        wrapped_.setParentScope(parent);
    }

    /**
     * {@inheritDoc}
     */
    public void setPrototype(final Scriptable prototype) {
        wrapped_.setPrototype(prototype);
    }
}
