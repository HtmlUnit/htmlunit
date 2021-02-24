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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine.KEY_STARTING_SCOPE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.BasicJavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.IteratorLikeIterable;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.TopLevel;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Promise}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Rural Hunter
 */
@JsxClass({CHROME, EDGE, FF, FF78})
public class Promise extends SimpleScriptable {

    private enum PromiseState { PENDING, FULFILLED, REJECTED }
    private PromiseState state_ = PromiseState.PENDING;
    private Object value_;

    private boolean race_;
    private Promise[] all_;

    private List<BasicJavaScriptJob> settledJobs_;
    private List<Promise> dependentPromises_;

    /**
     * Default constructor.
     */
    public Promise() {
    }

    /**
     * Facility constructor.
     * @param window the owning window
     */
    public Promise(final Window window) {
        setParentScope(window);
        setPrototype(window.getPrototype(Promise.class));
    }

    /**
     * Constructor new promise with the given {@code object}.
     *
     * @param object the object
     */
    @JsxConstructor
    public Promise(final Object object) {
        if (!(object instanceof Function)) {
            throw ScriptRuntime.typeError("Promise resolver '"
                        + ScriptRuntime.toString(object) + "' is not a function");
        }

        final Function fun = (Function) object;
        final Window window = getWindow(fun);
        this.setParentScope(window);
        this.setPrototype(window.getPrototype(this.getClass()));
        final Promise thisPromise = this;

        callThenableFunction(fun, window, thisPromise, window);
    }

    private static void callThenableFunction(final Function fun, final Window window,
                            final Promise promise, final Scriptable thisObj) {
        final Function resolve = new BaseFunction(window, ScriptableObject.getFunctionPrototype(window)) {
            @Override
            public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj,
                                        final Object[] args) {
                promise.settle(true, args.length == 0 ? Undefined.instance : args[0], window);
                return promise;
            }
        };

        final Function reject = new BaseFunction(window, ScriptableObject.getFunctionPrototype(window)) {
            @Override
            public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj,
                                        final Object[] args) {
                promise.settle(false, args.length == 0 ? Undefined.instance : args[0], window);
                return promise;
            }
        };

        final Context cx = Context.getCurrentContext();
        try {
            // KEY_STARTING_SCOPE maintains a stack of scopes
            @SuppressWarnings("unchecked")
            Deque<Scriptable> stack = (Deque<Scriptable>) cx.getThreadLocal(KEY_STARTING_SCOPE);
            if (null == stack) {
                stack = new ArrayDeque<>();
                cx.putThreadLocal(KEY_STARTING_SCOPE, stack);
            }
            stack.push(window);
            try {
                fun.call(cx, window, thisObj, new Object[] {resolve, reject});
            }
            finally {
                stack.pop();
            }

            window.getWebWindow().getWebClient().getJavaScriptEngine().processPostponedActions();
        }
        catch (final JavaScriptException e) {
            promise.settle(false, e.getValue(), window);
        }
    }

    /**
     * Returns a {@link Promise} object that is resolved with the given value.
     *
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return a {@link Promise}
     */
    @JsxStaticFunction
    public static Promise resolve(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        return create(thisObj, args, PromiseState.FULFILLED);
    }

    /**
     * Returns a {@link Promise} object that is rejected with the given value.
     *
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return a {@link Promise}
     */
    @JsxStaticFunction
    public static Promise reject(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        return create(thisObj, args, PromiseState.REJECTED);
    }

    private static Promise create(final Scriptable thisObj, final Object[] args, final PromiseState state) {
        // fulfilled promises are returned
        if (args.length != 0 && args[0] instanceof Promise && state == PromiseState.FULFILLED) {
            return (Promise) args[0];
        }

        final Promise promise;
        if (args.length > 0) {
            final Object arg = args[0];
            if (arg instanceof NativeObject) {
                final NativeObject nativeObject = (NativeObject) arg;
                final Object thenFunction = nativeObject.get("then", nativeObject);
                if (thenFunction == NOT_FOUND) {
                    promise = new Promise();
                    promise.value_ = arg;
                    promise.state_ = state;
                }
                else {
                    promise = new Promise(thenFunction);
                }
            }
            else {
                promise = new Promise();
                promise.value_ = arg;
                promise.state_ = state;
            }
        }
        else {
            promise = new Promise();
            promise.value_ = Undefined.instance;
            promise.state_ = state;
        }

        promise.setParentScope(thisObj.getParentScope());
        promise.setPrototype(getWindow(thisObj).getPrototype(promise.getClass()));
        return promise;
    }

    void settle(final boolean fulfilled, final Object newValue, final Window window) {
        if (state_ != PromiseState.PENDING) {
            return;
        }

        if (all_ != null) {
            settleAll(window);
            return;
        }
        settleThis(fulfilled, newValue, window);
    }

    private void settleThis(final boolean fulfilled, final Object newValue, final Window window) {
        value_ = newValue;

        if (fulfilled) {
            state_ = PromiseState.FULFILLED;
        }
        else {
            state_ = PromiseState.REJECTED;
        }

        if (settledJobs_ != null) {
            for (final BasicJavaScriptJob job : settledJobs_) {
                window.getWebWindow().getJobManager().addJob(job, window.getDocument().getPage());
            }
            settledJobs_ = null;
        }

        if (dependentPromises_ != null) {
            for (final Promise promise : dependentPromises_) {
                promise.settle(fulfilled, newValue, window);
            }
            dependentPromises_ = null;
        }
    }

    private void settleAll(final Window window) {
        if (race_) {
            for (final Promise promise : all_) {
                if (promise.state_ == PromiseState.REJECTED) {
                    settleThis(false, promise.value_, window);
                    return;
                }
                else if (promise.state_ == PromiseState.FULFILLED) {
                    settleThis(true, promise.value_, window);
                    return;
                }
            }
            return;
        }

        final ArrayList<Object> values = new ArrayList<>(all_.length);
        for (final Promise promise : all_) {
            if (promise.state_ == PromiseState.REJECTED) {
                settleThis(false, promise.value_, window);
                return;
            }
            else if (promise.state_ == PromiseState.PENDING) {
                return;
            }
            values.add(promise.value_);
        }

        final NativeArray jsValues = new NativeArray(values.toArray());
        ScriptRuntime.setBuiltinProtoAndParent(jsValues, window, TopLevel.Builtins.Array);
        settleThis(true, jsValues, window);
    }

    /**
     * Returns a {@link Promise} that resolves when all of the promises in the iterable argument have resolved,
     * or rejects with the reason of the first passed promise that rejects.
     *
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return a {@link Promise}
     */
    @JsxStaticFunction
    public static Promise all(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        return all(false, context, thisObj, args);
    }

    /**
     * Returns a {@link Promise} that that resolves or rejects as soon as one of the promises
     * in the iterable resolves or rejects, with the value or reason from that promise.
     *
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return a {@link Promise}
     */
    @JsxStaticFunction
    public static Promise race(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        return all(true, context, thisObj, args);
    }

    private static Promise all(final boolean race,
            final Context context, final Scriptable thisObj, final Object[] args) {
        final Window window = getWindow(thisObj);
        final Promise returnPromise = new Promise(window);

        if (args.length == 0) {
            returnPromise.all_ = new Promise[0];
        }
        else {
            // Call the "[Symbol.iterator]" property as a function.
            final Object ito = ScriptRuntime.callIterator(args[0], context, window);
            final ArrayList<Promise> promises = new ArrayList<>();
            if (!Undefined.isUndefined(ito)) {
                // run through all the iterated values and add them!
                try (IteratorLikeIterable it = new IteratorLikeIterable(context, window, ito)) {
                    for (final Object val : it) {
                        if (val instanceof Promise) {
                            final Promise promis = (Promise) val;
                            promises.add(promis);
                            if (promis.dependentPromises_ == null) {
                                promis.dependentPromises_ = new ArrayList<Promise>(2);
                            }
                            promis.dependentPromises_.add(returnPromise);
                        }
                        else {
                            promises.add(create(thisObj, new Object[] {val}, PromiseState.FULFILLED));
                        }
                    }
                }

                returnPromise.all_ = promises.toArray(new Promise[promises.size()]);
            }
        }

        returnPromise.race_ = race;

        returnPromise.settleAll(window);
        return returnPromise;
    }

    /**
     * It takes two arguments, both are callback functions for the success and failure cases of the Promise.
     *
     * @param onFulfilled success function
     * @param onRejected failure function
     * @return {@link Promise}
     */
    @JsxFunction
    public Promise then(final Object onFulfilled, final Object onRejected) {
        final Window window = getWindow();
        final Promise returnPromise = new Promise(window);

        final Promise thisPromise = this;

        final BasicJavaScriptJob job = new BasicJavaScriptJob() {

            @Override
            public void run() {
                final WebClient client = window.getWebWindow().getWebClient();
                final HtmlUnitContextFactory cf = ((JavaScriptEngine) client
                        .getJavaScriptEngine()).getContextFactory();

                final ContextAction<Object> contextAction = new ContextAction<Object>() {
                    @Override
                    public Object run(final Context cx) {
                        Function toExecute = null;
                        if (thisPromise.state_ == PromiseState.FULFILLED && onFulfilled instanceof Function) {
                            toExecute = (Function) onFulfilled;
                        }
                        else if (thisPromise.state_ == PromiseState.REJECTED && onRejected instanceof Function) {
                            toExecute = (Function) onRejected;
                        }

                        try {
                            final Object callbackResult;
                            if (toExecute == null) {
                                final Promise dummy = new Promise();
                                dummy.state_ = thisPromise.state_;
                                dummy.value_ = thisPromise.value_;
                                callbackResult = dummy;
                            }
                            else {
                                // KEY_STARTING_SCOPE maintains a stack of scopes
                                @SuppressWarnings("unchecked")
                                Deque<Scriptable> stack = (Deque<Scriptable>) cx.getThreadLocal(KEY_STARTING_SCOPE);
                                if (null == stack) {
                                    stack = new ArrayDeque<>();
                                    cx.putThreadLocal(KEY_STARTING_SCOPE, stack);
                                }
                                stack.push(window);
                                try {
                                    if (ScriptRuntime.hasTopCall(cx)) {
                                        callbackResult = toExecute.call(cx, window, thisPromise, new Object[] {value_});
                                    }
                                    else {
                                        callbackResult = ScriptRuntime.doTopCall(toExecute, cx, window, thisPromise,
                                                                            new Object[] {value_}, cx.isStrictMode());
                                    }
                                }
                                finally {
                                    stack.pop();
                                }

                                window.getWebWindow().getWebClient().getJavaScriptEngine().processPostponedActions();
                            }
                            if (callbackResult instanceof Promise) {
                                final Promise resultPromise = (Promise) callbackResult;
                                if (resultPromise.state_ == PromiseState.FULFILLED) {
                                    returnPromise.settle(true, resultPromise.value_, window);
                                }
                                else if (resultPromise.state_ == PromiseState.REJECTED) {
                                    returnPromise.settle(false, resultPromise.value_, window);
                                }
                                else {
                                    if (resultPromise.dependentPromises_ == null) {
                                        resultPromise.dependentPromises_ = new ArrayList<Promise>(2);
                                    }
                                    resultPromise.dependentPromises_.add(returnPromise);
                                }
                            }
                            else if (callbackResult instanceof NativeObject) {
                                final NativeObject nativeObject = (NativeObject) callbackResult;
                                final Object thenFunction = ScriptableObject.getProperty(nativeObject, "then");
                                if (thenFunction instanceof Function) {
                                    toExecute = (Function) thenFunction;

                                    callThenableFunction(toExecute, window, returnPromise, nativeObject);
                                }
                                else {
                                    returnPromise.settle(true, callbackResult, window);
                                }
                            }
                            else {
                                returnPromise.settle(true, callbackResult, window);
                            }
                        }
                        catch (final JavaScriptException e) {
                            returnPromise.settle(false, e.getValue(), window);
                        }
                        return null;
                    }
                };
                cf.call(contextAction);
            }

            /** {@inheritDoc} */
            @Override
            public String toString() {
                return super.toString() + " Promise.then";
            }
        };

        if (state_ == PromiseState.FULFILLED || state_ == PromiseState.REJECTED) {
            window.getWebWindow().getJobManager().addJob(job, window.getDocument().getPage());
        }
        else {
            if (settledJobs_ == null) {
                settledJobs_ = new ArrayList<BasicJavaScriptJob>(2);
            }
            settledJobs_.add(job);
        }

        return returnPromise;
    }

    /**
     * Returns a Promise and deals with rejected cases only.
     *
     * @param onRejected failure function
     * @return {@link Promise}
     */
    @JsxFunction(functionName = "catch")
    public Promise catch_js(final Object onRejected) {
        return then(null, onRejected);
    }

    /**
     * @return state and value details as string
     */
    public String getLogDetails() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{ <state>: ")
            .append(state_.toString().toLowerCase())
            .append(", <value>: ")
            .append(value_)
            .append(" }");
        return sb.toString();
    }
}
