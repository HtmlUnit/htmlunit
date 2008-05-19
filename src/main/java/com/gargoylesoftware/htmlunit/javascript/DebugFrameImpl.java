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
package com.gargoylesoftware.htmlunit.javascript;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;

/**
 * <p>
 * HtmlUnit's implementation of the {@link DebugFrame} interface, which logs stack entries as well
 * as exceptions. All logging is done at the <tt>TRACE</tt> level. This class does a fairly good
 * job of guessing names for anonymous functions when they are referenced by name from an existing
 * object. See <a href="http://www.mozilla.org/rhino/rhino15R4-debugger.html">the Rhino
 * documentation</a> or <a
 * href="http://lxr.mozilla.org/mozilla/source/js/rhino/src/org/mozilla/javascript/debug/DebugFrame.java">the
 * interface source code</a> for more information on the {@link DebugFrame} interface and its uses.
 * </p>
 *
 * <p>
 * Please note that this class is intended mainly to aid in the debugging and development of
 * HtmlUnit itself, rather than the debugging and development of web applications.
 * </p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @see DebuggerImpl
 */
public class DebugFrameImpl implements DebugFrame {

    private static final Log LOG = LogFactory.getLog(DebugFrameImpl.class);

    private final DebuggableScript functionOrScript_;

    /**
     * Creates a new debug frame.
     *
     * @param functionOrScript the function or script to which this frame corresponds
     */
    public DebugFrameImpl(final DebuggableScript functionOrScript) {
        this.functionOrScript_ = functionOrScript;
    }

    /**
     * {@inheritDoc}
     */
    public void onEnter(final Context cx, final Scriptable activation, final Scriptable thisObj, final Object[] args) {
        if (LOG.isTraceEnabled()) {
            final StringBuilder sb = new StringBuilder();
            Scriptable parent = activation.getParentScope();
            while (parent != null) {
                sb.append("   ");
                parent = parent.getParentScope();
            }
            sb.append(this.getFunctionName(thisObj)).append("(");
            for (int i = 0; i < args.length; i++) {
                sb.append(this.getParamName(i)).append(" : ").append(args[i]);
                if (i < args.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(") @ line ").append(this.getFirstLine());
            sb.append(" of ").append(this.getSourceName());
            LOG.trace(sb);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onExceptionThrown(final Context cx, final Throwable t) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Throwing exception: " + t.getMessage(), t);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onExit(final Context cx, final boolean byThrow, final Object resultOrException) {
        // Ignore.
    }

    /**
     * {@inheritDoc}
     */
    public void onLineChange(final Context cx, final int lineNumber) {
        // Ignore.
    }

    /**
     * {@inheritDoc}
     */
    public void onDebuggerStatement(final Context cx) {
        // Ignore.
    }

    /**
     * Returns the name of the function corresponding to this frame, if it is a function and it has
     * a name. If the function does not have a name, this method will try to return the name under
     * which it was referenced. See <a
     * href="http://www.digital-web.com/articles/scope_in_javascript/">this page</a> for a good
     * explanation of how the <tt>thisObj</tt> plays into this guess.
     *
     * @param thisObj the object via which the function was referenced, used to try to guess a
     *        function name if the function is anonymous
     * @return the name of the function corresponding to this frame
     */
    private String getFunctionName(final Scriptable thisObj) {
        if (this.functionOrScript_.isFunction()) {
            final String name = this.functionOrScript_.getFunctionName();
            if (name != null && name.length() > 0) {
                // A named function -- we can just return the name.
                return name;
            }
            // An anonymous function -- try to figure out how it was referenced.
            // For example, someone may have set foo.prototype.bar = function() { ... };
            // And then called fooInstance.bar() -- in which case it's "named" bar.
            for (final Object id : thisObj.getIds()) {
                if (id instanceof String) {
                    final String s = (String) id;
                    if (thisObj instanceof ScriptableObject) {
                        Object o = ((ScriptableObject) thisObj).getGetterOrSetter(s, 0, false);
                        if (o == null) {
                            o = ((ScriptableObject) thisObj).getGetterOrSetter(s, 0, true);
                            if (o != null && o instanceof Callable) {
                                return "__defineSetter__ " + s;
                            }
                        }
                        else if (o instanceof Callable) {
                            return "__defineGetter__ " + s;
                        }
                    }
                    final Object o = thisObj.get(s, thisObj);
                    if (o instanceof NativeFunction) {
                        final NativeFunction f = (NativeFunction) o;
                        if (f.getDebuggableView() == this.functionOrScript_) {
                            return s;
                        }
                    }
                }
            }
            // Unable to intuit a name -- doh!
            return "[anonymous]";
        }
        // Just a script -- no function name.
        return "[script]";
    }

    /**
     * Returns the name of the parameter at the specified index, or <tt>???</tt> if there is no
     * corresponding name.
     *
     * @param index the index of the parameter whose name is to be returned
     * @return the name of the parameter at the specified index, or <tt>???</tt> if there is no corresponding name
     */
    private String getParamName(final int index) {
        if (index >= 0 && this.functionOrScript_.getParamCount() > index) {
            return this.functionOrScript_.getParamOrVarName(index);
        }
        return "???";
    }

    /**
     * Returns the name of this frame's source.
     *
     * @return the name of this frame's source
     */
    private String getSourceName() {
        return this.functionOrScript_.getSourceName().trim();
    }

    /**
     * Returns the line number of the first line in this frame's function or script, or <tt>???</tt>
     * if it cannot be determined. This is necessary because the line numbers provided by Rhino are unordered.
     *
     * @return the line number of the first line in this frame's function or script, or <tt>???</tt>
     *         if it cannot be determined
     */
    private String getFirstLine() {
        int first = Integer.MAX_VALUE;
        for (final int current : functionOrScript_.getLineNumbers()) {
            if (current < first) {
                first = current;
            }
        }
        if (first != Integer.MAX_VALUE) {
            return String.valueOf(first);
        }
        return "???";
    }

}
