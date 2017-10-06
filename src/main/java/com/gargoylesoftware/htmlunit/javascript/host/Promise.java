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

import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
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

    private PostponedAction thenAction_;
    private Promise linkedPromise_;

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
                thisPromise.update(PromiseState.FULFILLED, args.length != 0 ? args[0] : Undefined.instance);
                return thisPromise;
            }
        };

        final Function reject = new BaseFunction(window, ScriptableObject.getFunctionPrototype(window)) {
            @Override
            public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj,
                                        final Object[] args) {
                thisPromise.update(PromiseState.REJECTED, args.length != 0 ? args[0] : Undefined.instance);
                return thisPromise;
            }
        };

        try {
            fun.call(Context.getCurrentContext(), window, window, new Object[] {resolve, reject});
        }
        catch (final JavaScriptException e) {
            thisPromise.update(PromiseState.REJECTED, e.getValue());
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

    private void update(final PromiseState newState, final Object newValue) {
        if (state_ == newState || state_ != PromiseState.PENDING) {
            return;
        }

        value_ = newValue;
        state_ = newState;

        if (thenAction_ != null) {
            try {
                thenAction_.execute();
            }
            catch (final Exception e) {
                // ignore for now
            }
            thenAction_ = null;
        }

        if (linkedPromise_ != null) {
            linkedPromise_.update(newState, newValue);
            linkedPromise_ = null;
        }
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
        final Promise promise = new Promise();
        promise.state_ = PromiseState.FULFILLED;
        if (args.length == 0) {
            promise.all_ = new Promise[0];
        }
        else {
            final NativeArray array = (NativeArray) args[0];
            final int length = (int) array.getLength();
            promise.all_ = new Promise[length];
            for (int i = 0; i < length; i++) {
                final Object o = array.get(i);
                if (o instanceof Promise) {
                    promise.all_[i] = (Promise) o;
                }
                else {
                    promise.all_[i] = resolve(null, thisObj, new Object[] {o}, null);
                }
            }
        }
        promise.setParentScope(thisObj.getParentScope());
        promise.setPrototype(getWindow(thisObj).getPrototype(promise.getClass()));
        return promise;
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
        final Promise promise = new Promise(window);
        final Promise thisPromise = this;

        thenAction_ = new PostponedAction(window.getDocument().getPage(), "Promise.then") {

            @Override
            public void execute() throws Exception {
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
                            final Object newValue = toExecute.call(Context.getCurrentContext(),
                                    window, thisPromise, new Object[] {value_});
                            if (newValue instanceof Promise) {
                                final Promise callPromise = (Promise) newValue;
                                if (callPromise.state_ == PromiseState.FULFILLED) {
                                    promise.update(PromiseState.FULFILLED, callPromise.value_);
                                }
                                else if (callPromise.state_ == PromiseState.REJECTED) {
                                    promise.update(PromiseState.REJECTED, callPromise.value_);
                                }
                                else {
                                    callPromise.linkedPromise_ = promise;
                                }
                            }
                            else {
                                promise.update(PromiseState.FULFILLED, newValue);
                            }
                        }
                        catch (final JavaScriptException e) {
                            promise.update(PromiseState.REJECTED, e.getValue());
                        }
                    }
                }
                finally {
                    Context.exit();
                }
            }
        };

        if (state_ != PromiseState.PENDING) {
            final JavaScriptEngine jsEngine
                = (JavaScriptEngine) getWindow(this).getWebWindow().getWebClient().getJavaScriptEngine();
            jsEngine.addPostponedAction(thenAction_);
            thenAction_ = null;
        }
        else {
            thisPromise.linkedPromise_ = promise;
        }

        return promise;
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
