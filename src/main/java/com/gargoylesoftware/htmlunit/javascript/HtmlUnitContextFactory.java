/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ARGUMENTS_READ_ONLY_ACCESSED_FROM_FUNCTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ERROR_STACK;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IGNORES_LAST_LINE_CONTAINING_UNCOMMENTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_PROPERTY_DESCRIPTOR_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_PROPERTY_DESCRIPTOR_NEW_LINE;

import java.io.Serializable;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.regexp.HtmlUnitRegExpProxy;

import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.ClassShutter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.ErrorReporter;
import net.sourceforge.htmlunit.corejs.javascript.Evaluator;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.WrapFactory;
import net.sourceforge.htmlunit.corejs.javascript.debug.Debugger;

/**
 * ContextFactory that supports termination of scripts if they exceed a timeout. Based on example from
 * <a href="http://www.mozilla.org/rhino/apidocs/org/mozilla/javascript/ContextFactory.html">ContextFactory</a>.
 *
 * @author Andre Soereng
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class HtmlUnitContextFactory extends ContextFactory {

    private static final int INSTRUCTION_COUNT_THRESHOLD = 10_000;

    private final WebClient webClient_;
    private final BrowserVersion browserVersion_;
    private long timeout_;
    private Debugger debugger_;
    private final WrapFactory wrapFactory_ = new HtmlUnitWrapFactory();
    private boolean deminifyFunctionCode_;

    /**
     * Creates a new instance of HtmlUnitContextFactory.
     *
     * @param webClient the web client using this factory
     */
    public HtmlUnitContextFactory(final WebClient webClient) {
        webClient_ = webClient;
        browserVersion_ = webClient.getBrowserVersion();
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
     * @param debugger the JavaScript debugger to use (may be {@code null})
     */
    public void setDebugger(final Debugger debugger) {
        debugger_ = debugger;
    }

    /**
     * Returns the JavaScript debugger to use to receive JavaScript execution debugging information.
     * By default, no debugger is used, and this method returns {@code null}.
     *
     * @return the JavaScript debugger to use to receive JavaScript execution debugging information
     */
    public Debugger getDebugger() {
        return debugger_;
    }

    /**
     * Configures if the code of <code>new Function("...some code...")</code> should be deminified to be more readable
     * when using the debugger. This is a small performance cost.
     * @param deminify the new value
     */
    public void setDeminifyFunctionCode(final boolean deminify) {
        deminifyFunctionCode_ = deminify;
    }

    /**
     * Indicates code of calls like <code>new Function("...some code...")</code> should be deminified to be more
     * readable when using the debugger.
     * @return the de-minify status
     */
    public boolean isDeminifyFunctionCode() {
        return deminifyFunctionCode_;
    }

    /**
     * Custom context to store execution time and handle timeouts.
     */
    private class TimeoutContext extends Context {
        private long startTime_;

        protected TimeoutContext(final ContextFactory factory) {
            super(factory);
        }

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

        @Override
        protected Script compileString(String source, final Evaluator compiler,
                final ErrorReporter compilationErrorReporter, final String sourceName,
                final int lineno, final Object securityDomain) {

            // this method gets called by Context.compileString and by ScriptRuntime.evalSpecial
            // which is used for window.eval. We have to take care in which case we are.
            final boolean isWindowEval = compiler != null;

            // Remove HTML comments around the source if needed
            if (!isWindowEval) {

                // **** Memory Optimization ****
                // final String sourceCodeTrimmed = source.trim();
                // if (sourceCodeTrimmed.startsWith("<!--")) {
                // **** Memory Optimization ****
                // do not trim because this will create a copy of the
                // whole string (usually large for libs like jQuery
                // if there is whitespace to trim (e.g. cr at end)
                final int length = source.length();
                int start = 0;
                while ((start < length) && (source.charAt(start) <= ' ')) {
                    start++;
                }
                if (start + 3 < length
                        && source.charAt(start++) == '<'
                        && source.charAt(start++) == '!'
                        && source.charAt(start++) == '-'
                        && source.charAt(start++) == '-') {
                    source = source.replaceFirst("<!--", "// <!--");
                }

                // IE ignores the last line containing uncommented -->
                // if (browserVersion_.hasFeature(JS_IGNORES_LAST_LINE_CONTAINING_UNCOMMENTED)
                //         && sourceCodeTrimmed.endsWith("-->")) {
                // **** Memory Optimization ****
                // see above
                if (browserVersion_.hasFeature(JS_IGNORES_LAST_LINE_CONTAINING_UNCOMMENTED)) {
                    int end = source.length() - 1;
                    while ((end > -1) && (source.charAt(end) <= ' ')) {
                        end--;
                    }
                    if (1 < end
                            && source.charAt(end--) == '>'
                            && source.charAt(end--) == '-'
                            && source.charAt(end--) == '-') {
                        final int lastDoubleSlash = source.lastIndexOf("//");
                        final int lastNewLine = Math.max(source.lastIndexOf('\n'), source.lastIndexOf('\r'));
                        if (lastNewLine > lastDoubleSlash) {
                            source = source.substring(0, lastNewLine);
                        }
                    }
                }
            }

            // Pre process the source code
            final HtmlPage page = (HtmlPage) Context.getCurrentContext()
                .getThreadLocal(JavaScriptEngine.KEY_STARTING_PAGE);
            source = preProcess(page, source, sourceName, lineno, null);

            return super.compileString(source, compiler, compilationErrorReporter,
                    sourceName, lineno, securityDomain);
        }

        @Override
        protected Function compileFunction(final Scriptable scope, String source,
                final Evaluator compiler, final ErrorReporter compilationErrorReporter,
                final String sourceName, final int lineno, final Object securityDomain) {

            if (deminifyFunctionCode_) {
                final Function f = super.compileFunction(scope, source, compiler,
                        compilationErrorReporter, sourceName, lineno, securityDomain);
                source = decompileFunction(f, 4).trim().replace("\n    ", "\n");
            }
            return super.compileFunction(scope, source, compiler,
                    compilationErrorReporter, sourceName, lineno, securityDomain);
        }
    }

    /**
     * Pre process the specified source code in the context of the given page using the processor specified
     * in the webclient. This method delegates to the pre processor handler specified in the
     * <code>WebClient</code>. If no pre processor handler is defined, the original source code is returned
     * unchanged.
     * @param htmlPage the page
     * @param sourceCode the code to process
     * @param sourceName a name for the chunk of code (used in error messages)
     * @param lineNumber the line number of the source code
     * @param htmlElement the HTML element that will act as the context
     * @return the source code after being pre processed
     * @see com.gargoylesoftware.htmlunit.ScriptPreProcessor
     */
    protected String preProcess(
        final HtmlPage htmlPage, final String sourceCode, final String sourceName, final int lineNumber,
        final HtmlElement htmlElement) {

        String newSourceCode = sourceCode;
        final ScriptPreProcessor preProcessor = webClient_.getScriptPreProcessor();
        if (preProcessor != null) {
            newSourceCode = preProcessor.preProcess(htmlPage, sourceCode, sourceName, lineNumber, htmlElement);
            if (newSourceCode == null) {
                newSourceCode = "";
            }
        }
        return newSourceCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Context makeContext() {
        final TimeoutContext cx = new TimeoutContext(this);
        cx.setLanguageVersion(Context.VERSION_ES6);

        // make sure no java classes are usable from js
        cx.setClassShutter(new ClassShutter() {
            @Override
            public boolean visibleToScripts(final String fullClassName) {
                final  Map<String, String> activeXObjectMap = webClient_.getActiveXObjectMap();
                if (activeXObjectMap != null) {
                    for (final String mappedClass : activeXObjectMap.values()) {
                        if (fullClassName.equals(mappedClass)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        // Use pure interpreter mode to get observeInstructionCount() callbacks.
        cx.setOptimizationLevel(-1);

        // Set threshold on how often we want to receive the callbacks
        cx.setInstructionObserverThreshold(INSTRUCTION_COUNT_THRESHOLD);

        cx.setErrorReporter(new HtmlUnitErrorReporter(webClient_.getJavaScriptErrorListener()));
        cx.setWrapFactory(wrapFactory_);

        if (debugger_ != null) {
            cx.setDebugger(debugger_, null);
        }

        // register custom RegExp processing
        ScriptRuntime.setRegExpProxy(cx, new HtmlUnitRegExpProxy(ScriptRuntime.getRegExpProxy(cx), browserVersion_));

        cx.setMaximumInterpreterStackDepth(10_000);

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
     * Same as {@link ContextFactory}{@link #call(ContextAction)} but with handling
     * of some exceptions.
     *
     * @param <T> return type of the action
     * @param action the contextAction
     * @param page the page
     * @return the result of the call
     */
    public final <T> T callSecured(final ContextAction<T> action, final HtmlPage page) {
        try {
            return call(action);
        }
        catch (final StackOverflowError e) {
            webClient_.getJavaScriptErrorListener().scriptException(page, new ScriptException(page, e));
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasFeature(final Context cx, final int featureIndex) {
        switch (featureIndex) {
            case Context.FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER:
                return true;
            case Context.FEATURE_E4X:
                return false;
            case Context.FEATURE_OLD_UNDEF_NULL_THIS:
                return true;
            case Context.FEATURE_NON_ECMA_GET_YEAR:
                return false;
            case Context.FEATURE_LITTLE_ENDIAN:
                return true;
            case Context.FEATURE_LOCATION_INFORMATION_IN_ERROR:
                return true;
            case Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW:
                return browserVersion_.hasFeature(JS_ARGUMENTS_READ_ONLY_ACCESSED_FROM_FUNCTION);
            case Context.FEATURE_HTMLUNIT_ERROR_STACK:
                return browserVersion_.hasFeature(JS_ERROR_STACK);
            case Context.FEATURE_HTMLUNIT_FUNCTION_DECLARED_FORWARD_IN_BLOCK:
                return true;
            case Context.FEATURE_HTMLUNIT_ENUM_NUMBERS_FIRST:
                return true;
            case Context.FEATURE_HTMLUNIT_MEMBERBOX_NAME:
                return browserVersion_.hasFeature(JS_PROPERTY_DESCRIPTOR_NAME);
            case Context.FEATURE_HTMLUNIT_MEMBERBOX_NEWLINE:
                return browserVersion_.hasFeature(JS_PROPERTY_DESCRIPTOR_NEW_LINE);
            case Context.FEATURE_HTMLUNIT_ARRAY_PROPERTIES:
                return false;
            default:
                return super.hasFeature(cx, featureIndex);
        }
    }

    private static final class HtmlUnitErrorReporter implements ErrorReporter, Serializable {

        private final JavaScriptErrorListener javaScriptErrorListener_;

        /**
         * Ctor.
         *
         * @param javaScriptErrorListener the listener to be used
         */
        HtmlUnitErrorReporter(final JavaScriptErrorListener javaScriptErrorListener) {
            javaScriptErrorListener_ = javaScriptErrorListener;
        }

        /**
         * Logs a warning.
         *
         * @param message the message to be displayed
         * @param sourceName the name of the source file
         * @param line the line number
         * @param lineSource the source code that failed
         * @param lineOffset the line offset
         */
        @Override
        public void warning(
                final String message, final String sourceName, final int line,
                final String lineSource, final int lineOffset) {
            javaScriptErrorListener_.warn(message, sourceName, line, lineSource, lineOffset);
        }

        /**
         * Logs an error.
         *
         * @param message the message to be displayed
         * @param sourceName the name of the source file
         * @param line the line number
         * @param lineSource the source code that failed
         * @param lineOffset the line offset
         */
        @Override
        public void error(final String message, final String sourceName, final int line,
                final String lineSource, final int lineOffset) {
            // no need to log here, this is only used to create the exception
            // the exception gets logged if not catched later on
            throw new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
        }

        /**
         * Logs a runtime error.
         *
         * @param message the message to be displayed
         * @param sourceName the name of the source file
         * @param line the line number
         * @param lineSource the source code that failed
         * @param lineOffset the line offset
         * @return an evaluator exception
         */
        @Override
        public EvaluatorException runtimeError(
                final String message, final String sourceName, final int line,
                final String lineSource, final int lineOffset) {
            // no need to log here, this is only used to create the exception
            // the exception gets logged if not catched later on
            return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
        }
    }
}
