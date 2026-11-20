/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.abort;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.JavaScriptException;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.SymbolKey;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import org.htmlunit.javascript.background.JavaScriptJob;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.WindowOrWorkerGlobalScope;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;

/**
 * The JavaScript object {@code AbortSignal}.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/AbortSignal">MDN Documentation</a>
 * @see <a href="https://dom.spec.whatwg.org/#interface-AbortSignal">DOM Standard - AbortSignal</a>
 * @author Ronald Brill
 */
@JsxClass
public class AbortSignal extends EventTarget {

    private boolean aborted_;
    private Object reason_ = JavaScriptEngine.UNDEFINED;

    // the steps to run when this signal is aborted (e.g. abort a fetch);
    // not serialized (the algorithms are lambdas), created on demand
    private transient List<Runnable> abortAlgorithms_;
    // the signals created by any() that depend on this signal
    private final List<AbortSignal> dependentSignals_ = new ArrayList<>();
    // true for signals created by any(); their sources are never dependent signals
    private boolean dependent_;
    private final List<AbortSignal> sourceSignals_ = new ArrayList<>();

    /**
     * JavaScript constructor; like the browsers, an {@code AbortSignal} can't be created by script.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeError("Failed to construct 'AbortSignal': Illegal constructor");
    }

    /**
     * Returns whether the signal is aborted.
     *
     * @return whether the signal is aborted
     */
    @JsxGetter
    public boolean isAborted() {
        return aborted_;
    }

    /**
     * Returns the abort reason ({@code undefined} if not aborted).
     *
     * @return the abort reason
     */
    @JsxGetter
    public Object getReason() {
        return reason_;
    }

    /**
     * Throws the abort reason if the signal is aborted.
     */
    @JsxFunction
    public void throwIfAborted() {
        if (aborted_) {
            throw new JavaScriptException(reason_, "", 0);
        }
    }

    /**
     * Returns the {@code onabort} event handler.
     *
     * @return the handler
     */
    @JsxGetter
    public Function getOnabort() {
        return getEventHandler(Event.TYPE_ABORT);
    }

    /**
     * Sets the {@code onabort} event handler.
     *
     * @param handler the handler
     */
    @JsxSetter
    public void setOnabort(final Object handler) {
        setEventHandler(Event.TYPE_ABORT, handler);
    }

    /**
     * Returns a signal that is already aborted.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (the optional reason)
     * @param funObj the function
     * @return the aborted signal
     */
    @JsxStaticFunction(functionName = "abort")
    public static AbortSignal jsAbort(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        final AbortSignal signal = newSignal(funObj);
        signal.signalAbort(args.length > 0 ? args[0] : JavaScriptEngine.UNDEFINED);
        return signal;
    }

    /**
     * Returns a signal that is aborted with a {@code TimeoutError} after the given time.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (the time in milliseconds)
     * @param funObj the function
     * @return the signal
     */
    @JsxStaticFunction
    public static AbortSignal timeout(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        if (args.length < 1) {
            throw JavaScriptEngine.typeError("Failed to execute 'timeout' on 'AbortSignal': "
                    + "1 argument required, but only 0 present.");
        }
        final double ms = JavaScriptEngine.toNumber(args[0]);
        if (Double.isNaN(ms) || Double.isInfinite(ms) || ms < 0 || ms > 9007199254740991d) {
            throw JavaScriptEngine.typeError("Failed to execute 'timeout' on 'AbortSignal': "
                    + "Value is outside the 'unsigned long long' value range.");
        }

        final AbortSignal signal = newSignal(funObj);
        final WindowOrWorkerGlobalScope global = getWindowOrWorkerGlobalScope(signal);
        if (global instanceof Window window) {
            final WebWindow webWindow = window.getWebWindow();
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().createJavaScriptJob((int) ms, null,
                () -> {
                    final AbstractJavaScriptEngine<?> engine = webWindow.getWebClient().getJavaScriptEngine();
                    if (engine != null) {
                        engine.getContextFactory().call(c -> {
                            signal.signalAbort(
                                    signal.createDOMException("signal timed out", DOMException.TIMEOUT_ERR));
                            return null;
                        });
                    }
                });
            webWindow.getJobManager().addJob(job, webWindow.getEnclosedPage());
        }
        return signal;
    }

    /**
     * Returns a signal that is aborted when any of the given signals is aborted.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (the signals)
     * @param funObj the function
     * @return the signal
     */
    @JsxStaticFunction
    public static AbortSignal any(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        if (args.length < 1) {
            throw JavaScriptEngine.typeError("Failed to execute 'any' on 'AbortSignal': "
                    + "1 argument required, but only 0 present.");
        }
        if (!(args[0] instanceof Scriptable iterable)
                || !ScriptableObject.hasProperty(iterable, SymbolKey.ITERATOR)) {
            throw JavaScriptEngine.typeError("Failed to execute 'any' on 'AbortSignal': "
                    + "The provided value cannot be converted to a sequence.");
        }

        final List<AbortSignal> signals = new ArrayList<>();
        JavaScriptEngine.iterate(cx, scope, iterable,
                item -> {
                    if (!(item instanceof AbortSignal s)) {
                        throw JavaScriptEngine.typeError("Failed to execute 'any' on 'AbortSignal': "
                                + "Failed to convert value to 'AbortSignal'.");
                    }
                    signals.add(s);
                });

        return createDependentSignal(newSignal(funObj), signals);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Creates a signal that is aborted when any of the given signals is aborted (like {@code AbortSignal.any()});
     * used e.g. for the signal of a {@code Request}.
     *
     * @param owner the object used for the scope and the prototype of the new signal
     * @param signals the signals
     * @return the new signal
     * @see <a href="https://dom.spec.whatwg.org/#create-a-dependent-abort-signal">DOM Standard -
     *     create a dependent abort signal</a>
     */
    public static AbortSignal createDependentSignal(final HtmlUnitScriptable owner, final List<AbortSignal> signals) {
        return createDependentSignal(newSignal(owner), signals);
    }

    private static AbortSignal createDependentSignal(final AbortSignal result, final List<AbortSignal> signals) {
        for (final AbortSignal s : signals) {
            if (s.aborted_) {
                result.aborted_ = true;
                result.reason_ = s.reason_;
                return result;
            }
        }
        result.dependent_ = true;
        for (final AbortSignal s : signals) {
            if (s.dependent_) {
                // link to the sources of a dependent signal (a dependent signal is never aborted by itself)
                for (final AbortSignal source : s.sourceSignals_) {
                    result.addSource(source);
                }
            }
            else {
                result.addSource(s);
            }
        }
        return result;
    }

    private void addSource(final AbortSignal source) {
        if (!sourceSignals_.contains(source)) {
            sourceSignals_.add(source);
            source.dependentSignals_.add(this);
        }
    }

    /**
     * Adds an algorithm that is run when this signal is aborted.
     *
     * @param algorithm the algorithm
     */
    public void addAbortAlgorithm(final Runnable algorithm) {
        if (abortAlgorithms_ == null) {
            abortAlgorithms_ = new ArrayList<>();
        }
        abortAlgorithms_.add(algorithm);
    }

    /**
     * Removes an algorithm added by {@link #addAbortAlgorithm(Runnable)}.
     *
     * @param algorithm the algorithm
     */
    public void removeAbortAlgorithm(final Runnable algorithm) {
        if (abortAlgorithms_ != null) {
            abortAlgorithms_.remove(algorithm);
        }
    }

    /**
     * Aborts this signal: sets the reason (an {@code AbortError} if {@code undefined}), runs
     * the abort algorithms, fires the abort event and aborts the dependent signals.
     *
     * @param reason the reason
     * @see <a href="https://dom.spec.whatwg.org/#abortsignal-signal-abort">DOM Standard - signal abort</a>
     */
    public void signalAbort(final Object reason) {
        if (aborted_) {
            return;
        }

        aborted_ = true;
        reason_ = JavaScriptEngine.isUndefined(reason)
                ? createDOMException("signal is aborted without reason", DOMException.ABORT_ERR)
                : reason;

        final List<AbortSignal> dependents = new ArrayList<>();
        for (final AbortSignal dependent : dependentSignals_) {
            if (!dependent.aborted_) {
                dependent.aborted_ = true;
                dependent.reason_ = reason_;
                dependents.add(dependent);
            }
        }
        dependentSignals_.clear();

        runAbortSteps();
        for (final AbortSignal dependent : dependents) {
            dependent.runAbortSteps();
        }
    }

    private void runAbortSteps() {
        if (abortAlgorithms_ != null) {
            final List<Runnable> algorithms = abortAlgorithms_;
            abortAlgorithms_ = null;
            for (final Runnable algorithm : algorithms) {
                algorithm.run();
            }
        }

        final Event event = new Event(this, Event.TYPE_ABORT);
        event.setBubbles(false);
        event.setCancelable(false);
        fireEvent(event);
    }

    /**
     * Creates a new signal for the global scope of the given function.
     *
     * @param funObj the function
     * @return the signal
     */
    static AbortSignal newSignal(final Scriptable funObj) {
        final HtmlUnitScriptable global = (HtmlUnitScriptable) getWindowOrWorkerGlobalScope(funObj);
        return newSignal(global);
    }

    /**
     * Creates a new signal with the scope and the prototype of the given object.
     *
     * @param scriptable the object
     * @return the signal
     */
    static AbortSignal newSignal(final HtmlUnitScriptable scriptable) {
        final AbortSignal signal = new AbortSignal();
        signal.setParentScope(scriptable.getParentScope());
        signal.setPrototype(scriptable.getPrototype(AbortSignal.class));
        return signal;
    }
}
