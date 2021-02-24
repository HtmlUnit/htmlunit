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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * The implementation of {@code WindowOrWorkerGlobalScope}
 * to be used by the implementers of the mixin.
 *
 * @author Ronald Brill
 * @author Rural Hunter
 */
public final class WindowOrWorkerGlobalScopeMixin {

    /**
     * The minimum delay that can be used with setInterval() or setTimeout(). Browser minimums are
     * usually in the 10ms to 15ms range, but there's really no reason for us to waste that much time.
     * http://jsninja.com/Timers#Minimum_Timer_Delay_and_Reliability
     */
    private static final int MIN_TIMER_DELAY = 1;

    private WindowOrWorkerGlobalScopeMixin() {
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding.
     * @param encodedData the encoded string
     * @return the decoded value
     */
    public static String atob(final String encodedData) {
        final int l = encodedData.length();
        for (int i = 0; i < l; i++) {
            if (encodedData.charAt(i) > 255) {
                throw new EvaluatorException("Function atob supports only latin1 characters");
            }
        }
        final byte[] bytes = encodedData.getBytes(StandardCharsets.ISO_8859_1);
        return new String(Base64.decodeBase64(bytes), StandardCharsets.ISO_8859_1);
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    public static String btoa(final String stringToEncode) {
        final int l = stringToEncode.length();
        for (int i = 0; i < l; i++) {
            if (stringToEncode.charAt(i) > 255) {
                throw new EvaluatorException("Function btoa supports only latin1 characters");
            }
        }
        final byte[] bytes = stringToEncode.getBytes(StandardCharsets.ISO_8859_1);
        return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
    }

    /**
     * Sets a chunk of JavaScript to be invoked at some specified time later.
     * The invocation occurs only if the window is opened after the delay
     * and does not contain an other page than the one that originated the setTimeout.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setTimeout">
     * MDN web docs</a>
     *
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created timer
     */
    public static Object setTimeout(final Context context, final Scriptable thisObj,
            final Object[] args, final Function function) {
        if (args.length < 1) {
            throw ScriptRuntime.typeError("Function not provided");
        }

        final int timeout = ScriptRuntime.toInt32((args.length > 1) ? args[1] : Undefined.instance);
        final Object[] params = (args.length > 2)
                ? Arrays.copyOfRange(args, 2, args.length)
                : ScriptRuntime.emptyArgs;
        return setTimeoutIntervalImpl((Window) thisObj, args[0], timeout, true, params);
    }

    /**
     * Sets a chunk of JavaScript to be invoked each time a specified number of milliseconds has elapsed.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setInterval">
     * MDN web docs</a>
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created interval
     */
    public static Object setInterval(final Context context, final Scriptable thisObj,
            final Object[] args, final Function function) {
        if (args.length < 1) {
            throw ScriptRuntime.typeError("Function not provided");
        }

        final int timeout = ScriptRuntime.toInt32((args.length > 1) ? args[1] : Undefined.instance);
        final Object[] params = (args.length > 2)
                ? Arrays.copyOfRange(args, 2, args.length)
                : ScriptRuntime.emptyArgs;
        return setTimeoutIntervalImpl((Window) thisObj, args[0], timeout, false, params);
    }

    private static int setTimeoutIntervalImpl(final Window window, final Object code,
            int timeout, final boolean isTimeout, final Object[] params) {
        if (timeout < MIN_TIMER_DELAY) {
            timeout = MIN_TIMER_DELAY;
        }

        final WebWindow webWindow = window.getWebWindow();
        final Page page = (Page) window.getDomNodeOrNull();
        Integer period = null;
        if (!isTimeout) {
            period = timeout;
        }

        if (code instanceof String) {
            final String s = (String) code;
            final String description = "window.set"
                                        + (isTimeout ? "Timeout" : "Interval")
                                        + "(" + s + ", " + timeout + ")";
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavaScriptJob(timeout, period, description, webWindow, s);
            return webWindow.getJobManager().addJob(job, page);
        }

        if (code instanceof Function) {
            final Function f = (Function) code;
            final String functionName;
            if (f instanceof FunctionObject) {
                functionName = ((FunctionObject) f).getFunctionName();
            }
            else {
                functionName = String.valueOf(f); // can this happen?
            }

            final String description = "window.set"
                                        + (isTimeout ? "Timeout" : "Interval")
                                        + "(" + functionName + ", " + timeout + ")";
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavaScriptJob(timeout, period, description, webWindow, f, params);
            return webWindow.getJobManager().addJob(job, page);
        }

        throw Context.reportRuntimeError("Unknown type for function.");
    }
}
