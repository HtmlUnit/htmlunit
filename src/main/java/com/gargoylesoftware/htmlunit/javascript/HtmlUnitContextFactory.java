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
import org.mozilla.javascript.debug.Debugger;

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

    private static final int INSTRUCTION_COUNT_THRESHOLD = 10000;

    private final Log log_;
    private final ThreadLocal<BrowserVersion> browserVersion_;
    private long timeout_;
    private Debugger debugger_;

    /**
     * Creates a new instance of HtmlUnitContextFactory.
     *
     * @param log the log that the error reporter should use
     */
    public HtmlUnitContextFactory(final Log log) {
        WebAssert.notNull("log", log);
        log_ = log;
        browserVersion_ = new ThreadLocal<BrowserVersion>();
    }

    /**
     * Sets the number of milliseconds a script is allowed to execute before
     * being terminated. A value of 0 or less means no timeout.
     *
     * @param timeout the timeout value
     */
    public void setTimeout(final long timeout) {
        timeout_ = timeout;
    }

    /**
     * Returns the number of milliseconds a script is allowed to execute before
     * being terminated. A value of 0 or less means no timeout.
     *
     * @return the timeout value (default value is <tt>0</tt>)
     */
    public long getTimeout() {
        return timeout_;
    }

    /**
     * Sets the JavaScript debugger to use to receive JavaScript execution debugging information.
     * The HtmlUnit default implementation ({@link DebuggerImpl}, {@link DebugFrameImpl}) may be
     * used, or a custom debugger may be used instead. By default, no debugger is used.
     *
     * @param debugger the JavaScript debugger to use (may be <tt>null</tt>)
     */
    public void setDebugger(final Debugger debugger) {
        debugger_ = debugger;
    }

    /**
     * Returns the JavaScript debugger to use to receive JavaScript execution debugging information.
     * By default, no debugger is used, and this method returns <tt>null</tt>.
     *
     * @return the JavaScript debugger to use to receive JavaScript execution debugging information
     */
    public Debugger getDebugger() {
        return debugger_;
    }

    /**
     * Puts the specified {@link BrowserVersion} as a thread local variable.
     * @param browserVersion the BrowserVersion that is currently used
     */
    public void putThreadLocal(final BrowserVersion browserVersion) {
        browserVersion_.set(browserVersion);
    }

    /**
     * Custom context to store execution time and handle timeouts.
     */
    @SuppressWarnings("deprecation")
    private class TimeoutContext extends Context {
        private long startTime_;
        public void startClock() {
            startTime_ = System.currentTimeMillis();
        }
        public void terminateScriptIfNecessary() {
            if (timeout_ > 0) {
                final long currentTime = System.currentTimeMillis();
                if (currentTime - startTime_ > timeout_) {
                    // Terminate script by throwing an Error instance to ensure that the
                    // script will never get control back through catch or finally.
                    throw new TimeoutError(timeout_, currentTime - startTime_);
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

        if (debugger_ != null) {
            cx.setDebugger(debugger_, null);
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
     * <p>Returns the global {@link ContextFactory}, assuming that it's an {@link HtmlUnitContextFactory}.</p>
     *
     * <p>This method, {@link #getGlobal()}, and all uses of both methods may disappear if we decide to move
     * to a per-{@link com.gargoylesoftware.htmlunit.WebClient} or per-{@link JavaScriptEngine}
     * <tt>ContextFactory</tt> model, which would make it easier to use multiple independent
     * <tt>WebClient</tt> instances within a single JVM.</p>
     *
     * @return the global {@link ContextFactory}, assuming that it's an {@link HtmlUnitContextFactory}
     */
    public static HtmlUnitContextFactory getGlobal2() {
        return (HtmlUnitContextFactory) ContextFactory.getGlobal();
    }

}
