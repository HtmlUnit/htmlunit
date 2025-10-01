/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import org.htmlunit.Page;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import org.htmlunit.javascript.background.JavaScriptJob;
import org.htmlunit.util.StringUtils;

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
     * <a href="http://jsninja.com/Timers#Minimum_Timer_Delay_and_Reliability">
     * http://jsninja.com/Timers#Minimum_Timer_Delay_and_Reliability</a>
     */
    private static final int MIN_TIMER_DELAY = 1;

    private WindowOrWorkerGlobalScopeMixin() {
        super();
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding.
     * @param encodedData the encoded string
     * @param scriptable the HtmlUnitScriptable scope
     * @return the decoded value
     */
    public static String atob(final String encodedData, final HtmlUnitScriptable scriptable) {
        final String withoutWhitespace = StringUtils.replaceChars(encodedData, " \t\r\n\u000c", "");
        final byte[] bytes = withoutWhitespace.getBytes(StandardCharsets.ISO_8859_1);
        try {
            return new String(Base64.getDecoder().decode(bytes), StandardCharsets.ISO_8859_1);
        }
        catch (final IllegalArgumentException e) {
            throw JavaScriptEngine.asJavaScriptException(
                    scriptable,
                    "Failed to execute atob(): " + e.getMessage(),
                    org.htmlunit.javascript.host.dom.DOMException.INVALID_CHARACTER_ERR);
        }
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @param scriptable the HtmlUnitScriptable scope
     * @return the encoded string
     */
    public static String btoa(final String stringToEncode, final HtmlUnitScriptable scriptable) {
        final int l = stringToEncode.length();
        for (int i = 0; i < l; i++) {
            if (stringToEncode.charAt(i) > 255) {
                throw JavaScriptEngine.asJavaScriptException(
                        scriptable,
                        "Function btoa supports only latin1 characters",
                        org.htmlunit.javascript.host.dom.DOMException.INVALID_CHARACTER_ERR);
            }
        }

        final byte[] bytes = stringToEncode.getBytes(StandardCharsets.ISO_8859_1);
        try {
            return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
        }
        catch (final IllegalArgumentException e) {
            throw JavaScriptEngine.asJavaScriptException(
                    scriptable,
                    "Failed to execute btoa(): " + e.getMessage(),
                    org.htmlunit.javascript.host.dom.DOMException.INVALID_CHARACTER_ERR);
        }
    }

    /**
     * Sets a chunk of JavaScript to be invoked at some specified time later.
     * The invocation occurs only if the window is opened after the delay
     * and does not contain an other page than the one that originated the setTimeout.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setTimeout">
     *     MDN web docs</a>
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
            throw JavaScriptEngine.typeError("Function not provided");
        }

        final int timeout = JavaScriptEngine.toInt32((args.length > 1) ? args[1] : JavaScriptEngine.UNDEFINED);
        final Object[] params = (args.length > 2)
                ? Arrays.copyOfRange(args, 2, args.length)
                : JavaScriptEngine.EMPTY_ARGS;
        return setTimeoutIntervalImpl((Window) thisObj, args[0], timeout, true, params);
    }

    /**
     * Sets a chunk of JavaScript to be invoked each time a specified number of milliseconds has elapsed.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setInterval">
     *     MDN web docs</a>
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created interval
     */
    public static Object setInterval(final Context context, final Scriptable thisObj,
            final Object[] args, final Function function) {
        if (args.length < 1) {
            throw JavaScriptEngine.typeError("Function not provided");
        }

        final int timeout = JavaScriptEngine.toInt32((args.length > 1) ? args[1] : JavaScriptEngine.UNDEFINED);
        final Object[] params = (args.length > 2)
                ? Arrays.copyOfRange(args, 2, args.length)
                : JavaScriptEngine.EMPTY_ARGS;
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

        throw JavaScriptEngine.reportRuntimeError("Unknown type for function.");
    }
}
