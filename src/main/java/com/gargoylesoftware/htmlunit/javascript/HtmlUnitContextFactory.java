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
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.javascript.regexp.HtmlUnitRegExpProxy;

/**
 * ContextFactory that supports termination of scripts if they exceed a timeout. Based on example from
 * <a href="http://www.mozilla.org/rhino/apidocs/org/mozilla/javascript/ContextFactory.html">ContextFactory</a>.
 *
 * @version $Revision$
 * @author Andre Soereng
 * @author Ahmed Ashour
 */
public class HtmlUnitContextFactory extends ContextFactory {

    private final Log log_;
    private static final int INSTRUCTION_COUNT_THRESHOLD = 10000;
    private static long Timeout_;
    private static boolean DebuggerEnabled_;

    private static final ThreadLocal<BrowserVersion> browserVersion_ = new ThreadLocal<BrowserVersion>();

    /**
     * Creates a new instance of HtmlUnitContextFactory.
     *
     * @param log the log that the error reporter should use
     */
    public HtmlUnitContextFactory(final Log log) {
        WebAssert.notNull("log", log);
        log_ = log;
    }

    /**
     * Sets the number of milliseconds a script is allowed to execute before
     * being terminated. A value of 0 or less means no timeout.
     *
     * @param timeout the timeout value
     */
    public static void setTimeout(final long timeout) {
        Timeout_ = timeout;
    }

    /**
     * Returns the number of milliseconds a script is allowed to execute before
     * being terminated. A value of 0 or less means no timeout.
     *
     * @return the timeout value (default value is <tt>0</tt>)
     */
    public static long getTimeout() {
        return Timeout_;
    }

    /**
     * Enables or disables the debugger, which logs stack entries and exceptions. Enabling the
     * debugger may be useful if HtmlUnit is having trouble with JavaScript, especially if you are
     * using some of the more advanced libraries like Dojo, Prototype or jQuery.
     *
     * @param enabled whether or not the debugger should be enabled
     * @see DebuggerImpl
     * @see DebugFrameImpl
     */
    public static void setDebuggerEnabled(final boolean enabled) {
        DebuggerEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if the debugger is enabled, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if the debugger is enabled, <tt>false</tt> otherwise
     * @see DebuggerImpl
     * @see DebugFrameImpl
     */
    public static boolean getDebuggerEnabled() {
        return DebuggerEnabled_;
    }

    // Custom Context to store execution time.
    @SuppressWarnings("deprecation")
    private static class TimeoutContext extends Context {
        private long startTime_;

        public void startClock() {
            startTime_ = System.currentTimeMillis();
        }

        public void terminateScriptIfNecessary() {
            if (Timeout_ > 0) {
                final long currentTime = System.currentTimeMillis();
                if (currentTime - startTime_ > Timeout_) {
                    // Terminate script by throwing an Error instance to ensure that the
                    // script will never get control back through catch or finally.
                    throw new TimeoutError(Timeout_, currentTime - startTime_);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Context makeContext() {
        final TimeoutContext cx = new TimeoutContext();

        // Use pure interpreter mode to get observeInstructionCount() callbacks.
        cx.setOptimizationLevel(-1);

        // Set threshold on how often we want to receive the callbacks
        cx.setInstructionObserverThreshold(INSTRUCTION_COUNT_THRESHOLD);

        cx.setErrorReporter(new StrictErrorReporter(log_));
        cx.setWrapFactory(new HtmlUnitWrapFactory());

        if (DebuggerEnabled_) {
            cx.setDebugger(new DebuggerImpl(), null);
        }

        // register custom RegExp processing
        ScriptRuntime.setRegExpProxy(cx, new HtmlUnitRegExpProxy(ScriptRuntime.getRegExpProxy(cx)));

        return cx;
    }

    /**
     * Run-time calls this when instruction counting is enabled and the counter
     * reaches limit set by setInstructionObserverThreshold(). A script can be
     * terminated by throwing an Error instance here.
     *
     * @param cx the context calling us
     * @param instructionCount amount of script instruction executed since last call to observeInstructionCount
     */
    @Override
    protected void observeInstructionCount(final Context cx, final int instructionCount) {
        final TimeoutContext tcx = (TimeoutContext) cx;
        tcx.terminateScriptIfNecessary();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object doTopCall(final Callable callable,
            final Context cx, final Scriptable scope,
            final Scriptable thisObj, final Object[] args) {

        final TimeoutContext tcx = (TimeoutContext) cx;
        tcx.startClock();
        return super.doTopCall(callable, cx, scope, thisObj, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasFeature(final Context cx, final int featureIndex) {
        if (Context.FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER == featureIndex) {
            return true;
        }
        else if (Context.FEATURE_PARENT_PROTO_PROPERTIES == featureIndex) {
            return !browserVersion_.get().isIE();
        }
        else {
            return super.hasFeature(cx, featureIndex);
        }
    }
    /**
     * Puts the specified {@link BrowserVersion} as a thread local variable.
     * @param browserVersion the BrowserVersion that is currently used
     */
    public static void putThreadLocal(final BrowserVersion browserVersion) {
        browserVersion_.set(browserVersion);
    }
}
