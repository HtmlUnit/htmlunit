/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.javascript.host.Event;

/**
 * <p>
 * HtmlUnit's implementation of the {@link net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame} interface,
 * which logs stack entries as well as exceptions. All logging is done at the <tt>TRACE</tt> level. This class does
 * a fairly good job of guessing names for anonymous functions when they are referenced by name from an existing
 * object. See <a href="http://www.mozilla.org/rhino/rhino15R4-debugger.html">the Rhino documentation</a> or
 * <a href="http://lxr.mozilla.org/mozilla/source/js/rhino/src/org/mozilla/javascript/debug/DebugFrame.java">the
 * interface source code</a> for more information on the
 * {@link net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame} interface and its uses.
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
public class DebugFrameImpl extends DebugFrameAdapter {

    private static final Log LOG = LogFactory.getLog(DebugFrameImpl.class);

    private static final String KEY_LAST_LINE = "DebugFrameImpl#line";
    private static final String KEY_LAST_SOURCE = "DebugFrameImpl#source";

    private final DebuggableScript functionOrScript_;

    /**
     * Creates a new debug frame.
     *
     * @param functionOrScript the function or script to which this frame corresponds
     */
    public DebugFrameImpl(final DebuggableScript functionOrScript) {
        functionOrScript_ = functionOrScript;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnter(final Context cx, final Scriptable activation, final Scriptable thisObj, final Object[] args) {
        if (LOG.isTraceEnabled()) {
            final StringBuilder sb = new StringBuilder();

            final String line = getFirstLine(cx);
            final String source = getSourceName(cx);
            sb.append(source).append(":").append(line).append(" ");

            Scriptable parent = activation.getParentScope();
            while (parent != null) {
                sb.append("   ");
                parent = parent.getParentScope();
            }
            final String functionName = getFunctionName(thisObj);
            sb.append(functionName).append("(");
            final int nbParams = functionOrScript_.getParamCount();
            for (int i = 0; i < nbParams; i++) {
                final String argAsString;
                if (i < args.length) {
                    argAsString = stringValue(args[i]);
                }
                else {
                    argAsString = "undefined";
                }
                sb.append(getParamName(i)).append(": ").append(argAsString);
                if (i < nbParams - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");

            LOG.trace(sb);
        }
    }

    private String stringValue(final Object arg) {
        if (arg instanceof NativeFunction) {
            // Don't return the string value of the function, because it's usually
            // multiple lines of content and messes up the log.
            final String name = StringUtils.defaultIfEmpty(((NativeFunction) arg).getFunctionName(), "anonymous");
            return "[function " + name + "]";
        }
        else if (arg instanceof IdFunctionObject) {
            return "[function " + ((IdFunctionObject) arg).getFunctionName() + "]";
        }
        else if (arg instanceof Function) {
            return "[function anonymous]";
        }
        String asString = null;
        try {
            // try to get the js representation
            asString = Context.toString(arg);
            if (arg instanceof Event) {
                asString += "<" + ((Event) arg).jsxGet_type() + ">";
            }
        }
        catch (final Throwable e) {
            // seems to be a bug (many bugs) in rhino (TODO: investigate it)
            asString = String.valueOf(arg);
        }
        return asString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExceptionThrown(final Context cx, final Throwable t) {
        if (LOG.isTraceEnabled()) {
            if (t instanceof JavaScriptException) {
                final JavaScriptException e = (JavaScriptException) t;
                LOG.trace(getSourceName(cx) + ":" + getFirstLine(cx)
                    + " Exception thrown: " + Context.toString(e.details()));
            }
            else if (t instanceof EcmaError) {
                final EcmaError e = (EcmaError) t;
                LOG.trace(getSourceName(cx) + ":" + getFirstLine(cx)
                    + " Exception thrown: " + Context.toString(e.details()));
            }
            else {
                LOG.trace(getSourceName(cx) + ":" + getFirstLine(cx) + " Exception thrown: " + t.getCause());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLineChange(final Context cx, final int lineNumber) {
        cx.putThreadLocal(KEY_LAST_LINE, lineNumber);
        cx.putThreadLocal(KEY_LAST_SOURCE, functionOrScript_.getSourceName());
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
        if (functionOrScript_.isFunction()) {
            final String name = functionOrScript_.getFunctionName();
            if (name != null && name.length() > 0) {
                // A named function -- we can just return the name.
                return name;
            }
            // An anonymous function -- try to figure out how it was referenced.
            // For example, someone may have set foo.prototype.bar = function() { ... };
            // And then called fooInstance.bar() -- in which case it's "named" bar.

            // on our SimpleScriptable we need to avoid looking at the properties we have defined => TODO: improve it
            if (thisObj instanceof SimpleScriptable) {
                return "[anonymous]";
            }

            Scriptable obj = thisObj;
            while (obj != null) {
                for (final Object id : obj.getIds()) {
                    if (id instanceof String) {
                        final String s = (String) id;
                        if (obj instanceof ScriptableObject) {
                            Object o = ((ScriptableObject) obj).getGetterOrSetter(s, 0, false);
                            if (o == null) {
                                o = ((ScriptableObject) obj).getGetterOrSetter(s, 0, true);
                                if (o != null && o instanceof Callable) {
                                    return "__defineSetter__ " + s;
                                }
                            }
                            else if (o instanceof Callable) {
                                return "__defineGetter__ " + s;
                            }
                        }
                        final Object o;
                        try {
                            // within a try block as this sometimes throws (not sure why)
                            o = obj.get(s, obj);
                        }
                        catch (final Exception e) {
                            return "[anonymous]";
                        }
                        if (o instanceof NativeFunction) {
                            final NativeFunction f = (NativeFunction) o;
                            if (f.getDebuggableView() == functionOrScript_) {
                                return s;
                            }
                        }
                    }
                }
                obj = obj.getPrototype();
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
        if (index >= 0 && functionOrScript_.getParamCount() > index) {
            return functionOrScript_.getParamOrVarName(index);
        }
        return "???";
    }

    /**
     * Returns the name of this frame's source.
     *
     * @return the name of this frame's source
     */
    private String getSourceName(final Context cx) {
        String source = (String) cx.getThreadLocal(KEY_LAST_SOURCE);
        if (source == null) {
            return "unknown";
        }
        // only the file name is interesting the rest of the url is mostly noise
        source = StringUtils.substringAfterLast(source, "/");
        // embedded scripts have something like "foo.html from (3, 10) to (10, 13)"
        source = StringUtils.substringBefore(source, " ");
        return source;
    }

    /**
     * Returns the line number of the first line in this frame's function or script, or <tt>???</tt>
     * if it cannot be determined. This is necessary because the line numbers provided by Rhino are unordered.
     *
     * @return the line number of the first line in this frame's function or script, or <tt>???</tt>
     *         if it cannot be determined
     */
    private String getFirstLine(final Context cx) {
        final Object line = cx.getThreadLocal(KEY_LAST_LINE);
        String result;
        if (line == null) {
            result = "??";
        }
        else {
            result = String.valueOf(line);
        }
        return StringUtils.leftPad(result, 5);
    }
}
