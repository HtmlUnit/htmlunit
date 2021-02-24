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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CONSOLE_HANDLE_WINDOW;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebConsole;
import com.gargoylesoftware.htmlunit.WebConsole.Formatter;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.ConsString;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for {@code Console}.
 *
 * @author Andrea Martino
 * @author Ronald Brill
 */
@JsxClass(isJSObject = false, value = {CHROME, EDGE, FF, FF78})
@JsxClass(IE)
public class Console extends SimpleScriptable {

    private static final Map<String, Long> TIMERS = new HashMap<>();
    private static Formatter FORMATTER_ = new ConsoleFormatter();

    private WebWindow webWindow_;

    /**
     * Default constructor.
     */
    public Console() {
    }

    /**
     * Sets the Window JavaScript object this console belongs to.
     * @param webWindow the Window JavaScript object this console belongs to
     */
    public void setWebWindow(final WebWindow webWindow) {
        webWindow_ = webWindow;
    }

    /**
     * This method writes an error message to the console if the
     * assertion is false. If the assertion is true, nothing happens.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     */
    @JsxFunction(functionName = "assert")
    public static void assertMethod(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {
        if (args.length < 1 || ScriptRuntime.toBoolean(args[0])) {
            return;
        }

        if (args.length == 1) {
            log(cx, thisObj, new String[] {"Assertion failed"}, null);
            return;
        }

        final Object[] data;
        final Object first = args[1];
        if (first instanceof String
                || first instanceof ConsString
                || first instanceof ScriptableObject && ("String".equals(((Scriptable) first).getClassName()))) {
            data = new Object[args.length - 1];
            data[0] = "Assertion failed: " + first.toString();
            System.arraycopy(args, 2, data, 1, data.length - 1);
        }
        else {
            data = new Object[args.length];
            data[0] = "Assertion failed:";
            System.arraycopy(args, 1, data, 1, data.length - 1);
        }

        log(cx, thisObj, data, null);
    }

    /**
     * This method performs logging to the console at {@code log} level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     */
    @JsxFunction
    public static void log(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {
        final WebConsole webConsole = toWebConsole(thisObj);
        final Formatter oldFormatter = webConsole.getFormatter();
        webConsole.setFormatter(FORMATTER_);
        webConsole.info(args);
        webConsole.setFormatter(oldFormatter);
    }

    private static WebConsole toWebConsole(Scriptable thisObj) {
        if (thisObj instanceof Window
                && ((SimpleScriptable) thisObj).getDomNodeOrDie().hasFeature(JS_CONSOLE_HANDLE_WINDOW)) {
            thisObj = ((Window) thisObj).getConsole();
        }
        if (thisObj instanceof Console) {
            return ((Console) thisObj).getWebConsole();
        }
        throw ScriptRuntime.typeError("object does not implemennt interface Console");
    }

    /**
     * This method performs logging to the console at {@code info} level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     */
    @JsxFunction
    public static void info(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {
        final WebConsole webConsole = toWebConsole(thisObj);
        final Formatter oldFormatter = webConsole.getFormatter();
        webConsole.setFormatter(FORMATTER_);
        webConsole.info(args);
        webConsole.setFormatter(oldFormatter);
    }

    /**
     * This method performs logging to the console at {@code warn} level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     */
    @JsxFunction
    public static void warn(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {
        final WebConsole webConsole = toWebConsole(thisObj);
        final Formatter oldFormatter = webConsole.getFormatter();
        webConsole.setFormatter(FORMATTER_);
        webConsole.warn(args);
        webConsole.setFormatter(oldFormatter);
    }

    /**
     * This method performs logging to the console at {@code error} level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     */
    @JsxFunction
    public static void error(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {
        final WebConsole webConsole = toWebConsole(thisObj);
        final Formatter oldFormatter = webConsole.getFormatter();
        webConsole.setFormatter(FORMATTER_);
        webConsole.error(args);
        webConsole.setFormatter(oldFormatter);
    }

    /**
     * This method performs logging to the console at {@code debug} level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     */
    @JsxFunction
    public static void debug(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {
        final WebConsole webConsole = toWebConsole(thisObj);
        final Formatter oldFormatter = webConsole.getFormatter();
        webConsole.setFormatter(FORMATTER_);
        webConsole.debug(args);
        webConsole.setFormatter(oldFormatter);
    }

    /**
     * This method performs logging to the console at {@code trace} level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     */
    @JsxFunction
    public static void trace(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {
        final WebConsole webConsole = toWebConsole(thisObj);
        final Formatter oldFormatter = webConsole.getFormatter();
        webConsole.setFormatter(FORMATTER_);
        webConsole.trace(args);
        webConsole.setFormatter(oldFormatter);
    }

    private WebConsole getWebConsole() {
        return webWindow_.getWebClient().getWebConsole();
    }

    /**
     * Implementation of console {@code dir} function. This method does not enter recursively
     * in the passed object, nor prints the details of objects or functions.
     * @param o the object to be printed
     */
    @JsxFunction
    public void dir(final Object o) {
        if (o instanceof ScriptableObject) {
            final ScriptableObject obj = (ScriptableObject) o;
            final Object[] ids = obj.getIds();
            if (ids != null && ids.length > 0) {
                final StringBuilder sb = new StringBuilder();
                for (final Object id : ids) {
                    final Object value = obj.get(id);
                    if (value instanceof Delegator) {
                        sb.append(id + ": " + ((Delegator) value).getClassName() + "\n");
                    }
                    else if (value instanceof SimpleScriptable) {
                        sb.append(id + ": " + ((SimpleScriptable) value).getClassName() + "\n");
                    }
                    else if (value instanceof BaseFunction) {
                        sb.append(id + ": function " + ((BaseFunction) value).getFunctionName() + "()\n");
                    }
                    else {
                        sb.append(id + ": " + value  + "\n");
                    }
                }
                getWebConsole().info(sb.toString());
            }
        }
    }

    /**
     * Implementation of group. Currently missing.
     */
    @JsxFunction
    public void group() {
        // TODO not implemented
    }

    /**
     * Implementation of endGroup. Currently missing.
     */
    @JsxFunction
    public void groupEnd() {
        // TODO not implemented
    }

    /**
     * Implementation of groupCollapsed. Currently missing.
     */
    @JsxFunction
    public void groupCollapsed() {
         // TODO not implemented
    }

    /**
     * This method replicates Firefox's behavior: if the timer already exists,
     * the start time is not overwritten. In both cases, the line is printed on the
     * console.
     * @param timerName the name of the timer
     */
    @JsxFunction
    public void time(final String timerName) {
        if (!TIMERS.containsKey(timerName)) {
            TIMERS.put(timerName, Long.valueOf(System.currentTimeMillis()));
        }
        getWebConsole().info(timerName + ": timer started");
    }

    /**
     * This method replicates Firefox's behavior: if no timer is found, nothing is
     * logged to the console.
     * @param timerName the name of the timer
     */
    @JsxFunction
    public void timeEnd(final String timerName) {
        final Long startTime = TIMERS.remove(timerName);
        if (startTime != null) {
            getWebConsole().info(timerName + ": " + (System.currentTimeMillis() - startTime.longValue()) + "ms");
        }
    }

    /**
     * Because there is no timeline in HtmlUnit this does nothing.
     * @param label the label
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public void timeStamp(final String label) {
    }

    /**
     * This class is the default formatter used by Console.
     */
    private static class ConsoleFormatter implements Formatter {

        private static void appendNativeArray(final NativeArray a, final StringBuilder sb, final int level) {
            sb.append('[');
            if (level < 3) {
                for (int i = 0; i < a.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    final Object val = a.get(i);
                    if (val != null) {
                        appendValue(val, sb, level + 1);
                    }
                }
            }
            sb.append(']');
        }

        private static void appendNativeObject(final NativeObject obj, final StringBuilder sb, final int level) {
            if (level == 0) {
                // For whatever reason, when a native object is printed at the
                // root level Firefox puts brackets outside it. This is not the
                // case when a native object is printed as part of an array or
                // inside another object.
                sb.append('(');
            }
            sb.append('{');
            if (level < 3) {
                final Object[] ids = obj.getIds();
                if (ids != null && ids.length > 0) {
                    boolean needsSeparator = false;
                    for (final Object key : ids) {
                        if (needsSeparator) {
                            sb.append(", ");
                        }
                        sb.append(key);
                        sb.append(": ");
                        appendValue(obj.get(key), sb, level + 1);
                        needsSeparator = true;
                    }
                }
            }
            sb.append('}');
            if (level == 0) {
                sb.append(')');
            }
        }

        /**
         * This methods appends the val parameter to the passed StringBuffer.
         * FireBug's console prints some object differently if printed at the
         * root level or as part of an array or native object. This method tries
         * to simulate Firebus's behavior.
         *
         * @param val
         *            the object to be printed
         * @param sb
         *            the StringBuilder used as destination
         * @param level
         *            the recursion level. If zero, it mean the object is
         *            printed at the console root level. Otherwise, the object
         *            is printed as part of an array or a native object.
         */
        private static void appendValue(final Object val, final StringBuilder sb, final int level) {
            if (val instanceof NativeFunction) {
                sb.append('(');
                // Remove unnecessary new lines and spaces from the function
                final Pattern p = Pattern.compile("[ \\t]*\\r?\\n[ \\t]*",
                        Pattern.MULTILINE);
                final Matcher m = p.matcher(((NativeFunction) val).toString());
                sb.append(m.replaceAll(" ").trim());
                sb.append(')');
            }
            else if (val instanceof BaseFunction) {
                sb.append("function ");
                sb.append(((BaseFunction) val).getFunctionName());
                sb.append("() {[native code]}");
            }
            else if (val instanceof NativeObject) {
                appendNativeObject((NativeObject) val, sb, level);
            }
            else if (val instanceof NativeArray) {
                appendNativeArray((NativeArray) val, sb, level);
            }
            else if (val instanceof Delegator) {
                if (level == 0) {
                    sb.append("[object ");
                    sb.append(((Delegator) val).getDelegee().getClassName());
                    sb.append(']');
                }
                else {
                    sb.append("({})");
                }
            }
            else if (val instanceof Promise) {
                sb.append("Promise");
                if (level == 0) {
                    final Promise p = (Promise) val;
                    sb.append(' ').append(p.getLogDetails());
                }
            }
            else if (val instanceof SimpleScriptable) {
                if (level == 0) {
                    sb.append("[object ");
                    sb.append(((SimpleScriptable) val).getClassName());
                    sb.append(']');
                }
                else {
                    sb.append("({})");
                }
            }
            else if (val instanceof String) {
                if (level == 0) {
                    sb.append((String) val);
                }
                else {
                    // When printing a string as part of an array or native
                    // object,
                    // enclose it in double quotes and escape its content.
                    sb.append(quote((String) val));
                }
            }
            else if (val instanceof Number) {
                sb.append(((Number) val).toString());
            }
            else {
                // ?!?
                sb.append(val);
            }
        }

        /**
         * Even if similar, this is not JSON encoding. This replicates the way
         * Firefox console prints strings when logging.
         * @param s the string to be quoted
         */
        private static String quote(final CharSequence s) {
            final StringBuilder sb = new StringBuilder();
            sb.append('\"');
            for (int i = 0; i < s.length(); i++) {
                final char ch = s.charAt(i);
                switch (ch) {
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '\"':
                        sb.append("\\\"");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    default:
                        if (ch < ' ' || ch > '~') {
                            sb.append("\\u" + Integer.toHexString(ch).toUpperCase(Locale.ROOT));
                        }
                        else {
                            sb.append(ch);
                        }
                }
            }
            sb.append('\"');
            return sb.toString();
        }

        private static String formatToString(final Object o) {
            if (o == null) {
                return "null";
            }
            else if (o instanceof NativeFunction) {
                return ((NativeFunction) o).toString();
            }
            else if (o instanceof BaseFunction) {
                return "function " + ((BaseFunction) o).getFunctionName()
                        + "\n" + "    [native code]\n" + "}";
            }
            else if (o instanceof NativeArray) {
                // If an array is embedded inside another array, just return
                // "[object Object]"
                return "[object Object]";
            }
            else if (o instanceof Delegator) {
                return "[object " + ((Delegator) o).getDelegee().getClassName()
                        + "]";
            }
            else if (o instanceof NativeObject) {
                return "[object " + ((NativeObject) o).getClassName() + "]";
            }
            else if (o instanceof SimpleScriptable) {
                return "[object " + ((SimpleScriptable) o).getClassName() + "]";
            }
            else {
                return o.toString();
            }
        }

        ConsoleFormatter() {
        }

        @Override
        public String printObject(final Object o) {
            final StringBuilder sb = new StringBuilder();
            appendValue(o, sb, 0);
            return sb.toString();
        }

        @Override
        public String parameterAsString(final Object o) {
            if (o instanceof NativeArray) {
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < ((NativeArray) o).size(); i++) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append(formatToString(((NativeArray) o).get(i)));
                }
                return sb.toString();
            }
            return formatToString(o);
        }

        @Override
        public String parameterAsInteger(final Object o) {
            if (o instanceof Number) {
                return Integer.toString(((Number) o).intValue());
            }
            else if (o instanceof String) {
                try {
                    return Integer.toString(Integer.parseInt((String) o));
                }
                catch (final NumberFormatException e) {
                    // Swallow the exception and return below
                }
            }
            return "NaN";
        }

        @Override
        public String parameterAsFloat(final Object o) {
            if (o instanceof Number) {
                return Float.toString(((Number) o).floatValue());
            }
            else if (o instanceof String) {
                try {
                    return Float.toString(Float.parseFloat((String) o));
                }
                catch (final NumberFormatException e) {
                    // Swallow the exception and return below
                }
            }
            return "NaN";
        }
    }
}
