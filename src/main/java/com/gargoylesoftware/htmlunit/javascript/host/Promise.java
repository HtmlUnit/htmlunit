/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.BasicJavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
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
 */
@JsxClass({CHROME, FF, EDGE})
public class Promise extends SimpleScriptable {

    private enum PromiseState { PENDING, FULFILLED, REJECTED }
    private PromiseState state_ = PromiseState.PENDING;
    private Object value_;
    /** To be set only by {@link #all(Context, Scriptable, Object[], Function)}. */
    private Promise[] all_;

    private List<BasicJavaScriptJob> settledJobs_;
    private Promise dependentPromise_;

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
            throw ScriptRuntime.typeError("Promise resolver is not a function");
        }

        final Function fun = (Function) object;
        final Window window = getWindow(fun);
        this.setParentScope(window);
        this.setPrototype(window.getPrototype(this.getClass()));
        final Promise thisPromise = this;

        final Function resolve = new BaseFunction(window, ScriptableObject.getFunctionPrototype(window)) {
            @Override
            public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj,
                                        final Object[] args) {
                thisPromise.settle(true, args.length != 0 ? args[0] : Undefined.instance, window);
                return thisPromise;
            }
        };

        final Function reject = new BaseFunction(window, ScriptableObject.getFunctionPrototype(window)) {
            @Override
            public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj,
                                        final Object[] args) {
                thisPromise.settle(false, args.length != 0 ? args[0] : Undefined.instance, window);
                return thisPromise;
            }
        };

        try {
            fun.call(Context.getCurrentContext(), window, window, new Object[] {resolve, reject});
        }
        catch (final JavaScriptException e) {
            thisPromise.settle(false, e.getValue(), window);
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
        // fulfilled promises are returend
        if (args.length != 0 && args[0] instanceof Promise && state == PromiseState.FULFILLED) {
            return (Promise) args[0];
        }

        final Promise promise;
        if (args.length > 0) {
            final Object arg = args[0];
            if (arg instanceof NativeObject) {
                final NativeObject nativeObject = (NativeObject) arg;
                promise = new Promise(nativeObject.get("then", nativeObject));
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

    private void settle(final boolean fulfilled, final Object newValue, final Window window) {
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
            for (BasicJavaScriptJob job : settledJobs_) {
                window.getWebWindow().getJobManager().addJob(job, window.getDocument().getPage());
            }
            settledJobs_ = null;
        }

        if (dependentPromise_ != null) {
            dependentPromise_.settle(fulfilled, newValue, window);
            dependentPromise_ = null;
        }
    }

    private void settleAll(final Window window) {
        final ArrayList<Object> values = new ArrayList<>(all_.length);
        for (Promise promise : all_) {
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
        final Window window = getWindow(thisObj);
        final Promise returnPromise = new Promise(window);

        if (args.length == 0) {
            returnPromise.all_ = new Promise[0];
        }
        else if (args[0] instanceof NativeArray) {
            final NativeArray array = (NativeArray) args[0];
            final int length = (int) array.getLength();
            returnPromise.all_ = new Promise[length];
            for (int i = 0; i < length; i++) {
                final Object o = array.get(i);
                if (o instanceof Promise) {
                    returnPromise.all_[i] = (Promise) o;
                    returnPromise.all_[i].dependentPromise_ = returnPromise;
                }
                else {
                    returnPromise.all_[i] = create(thisObj, new Object[] {o}, PromiseState.FULFILLED);
                }
            }
        }
        else {
            // TODO
        }

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
    public Promise then(final Function onFulfilled, final Function onRejected) {
        final Window window = getWindow();
        final Promise returnPromise = new Promise(window);

        final Promise thisPromise = this;

        final BasicJavaScriptJob job = new BasicJavaScriptJob() {

            @Override
            public void run() {
                Context.enter();
                try {
                    Function toExecute = null;
                    if (thisPromise.state_ == PromiseState.FULFILLED) {
                        toExecute = onFulfilled;
                    }
                    else if (thisPromise.state_ == PromiseState.REJECTED) {
                        toExecute = onRejected;
                    }

                    if (toExecute != null) {
                        try {
                            final Object callbackResult = toExecute.call(Context.getCurrentContext(),
                                    window, thisPromise, new Object[] {value_});
                            if (callbackResult instanceof Promise) {
                                final Promise resultPromise = (Promise) callbackResult;
                                if (resultPromise.state_ == PromiseState.FULFILLED) {
                                    returnPromise.settle(true, resultPromise.value_, window);
                                }
                                else if (resultPromise.state_ == PromiseState.REJECTED) {
                                    returnPromise.settle(false, resultPromise.value_, window);
                                }
                                else {
                                    resultPromise.dependentPromise_ = returnPromise;
                                }
                            }
                            else {
                                returnPromise.settle(true, callbackResult, window);
                            }
                        }
                        catch (final JavaScriptException e) {
                            returnPromise.settle(false, e.getValue(), window);
                        }
                    }
                }
                finally {
                    Context.exit();
                }
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
    public Promise catch_js(final Function onRejected) {
        return then(null, onRejected);
    }
}
