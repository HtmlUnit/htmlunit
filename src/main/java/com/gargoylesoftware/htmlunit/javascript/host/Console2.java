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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebConsole;
import com.gargoylesoftware.htmlunit.WebConsole.Formatter;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * A JavaScript object for {@code Console}.
 *
 * @author Andrea Martino
 */
@ScriptClass
public class Console2 extends SimpleScriptObject {

    private static final Map<String, Long> TIMERS = new HashMap<>();
    private static Formatter FORMATTER_ = new ConsoleFormatter();

    private WebWindow webWindow_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static Console2 constructor(final boolean newObj, final Object self) {
        final Console2 host = new Console2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    private WebConsole getWebConsole() {
        return webWindow_.getWebClient().getWebConsole();
    }

    /**
     * This method performs logging to the console at {@code log} level.
     * @param args the arguments passed into the method
     */
    @Function
    public void log(final Object... args) {
        final WebConsole webConsole = getWebConsole();
        final Formatter oldFormatter = webConsole.getFormatter();
        webConsole.setFormatter(FORMATTER_);
        webConsole.info(args);
        webConsole.setFormatter(oldFormatter);
    }

    /**
     * Sets the Window JavaScript object this console belongs to.
     * @param webWindow the Window JavaScript object this console belongs to
     */
    public void setWebWindow(final WebWindow webWindow) {
        webWindow_ = webWindow;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Console2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("Console",
                    staticHandle("constructor", Console2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends PrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        @Override
        public String getClassName() {
            return "Console";
        }
    }

    /**
     * This class is the default formatter used by Console.
     */
    private static class ConsoleFormatter implements Formatter {

//        private static void appendNativeArray(final NativeArray a, final StringBuilder sb, final int level) {
//            sb.append("[");
//            if (level < 3) {
//                for (int i = 0; i < a.size(); i++) {
//                    if (i > 0) {
//                        sb.append(", ");
//                    }
//                    final Object val = a.get(i);
//                    if (val != null) {
//                        appendValue(val, sb, level + 1);
//                    }
//                }
//            }
//            sb.append("]");
//        }
//
//        private static void appendNativeObject(final NativeObject obj, final StringBuilder sb, final int level) {
//            if (level == 0) {
//                // For whatever reason, when a native object is printed at the
//                // root level Firefox puts brackets outside it. This is not the
//                // case when a native object is printed as part of an array or
//                // inside another object.
//                sb.append("(");
//            }
//            sb.append("{");
//            if (level < 3) {
//                final Object[] ids = obj.getIds();
//                if (ids != null && ids.length > 0) {
//                    boolean needsSeparator = false;
//                    for (Object key : ids) {
//                        if (needsSeparator) {
//                            sb.append(", ");
//                        }
//                        sb.append(key);
//                        sb.append(":");
//                        appendValue(obj.get(key), sb, level + 1);
//                        needsSeparator = true;
//                    }
//                }
//            }
//            sb.append("}");
//            if (level == 0) {
//                sb.append(")");
//            }
//        }
//
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
            /*if (val instanceof NativeFunction) {
                sb.append("(");
                // Remove unnecessary new lines and spaces from the function
                final Pattern p = Pattern.compile("[ \\t]*\\r?\\n[ \\t]*",
                        Pattern.MULTILINE);
                final Matcher m = p.matcher(((NativeFunction) val).toString());
                sb.append(m.replaceAll(" ").trim());
                sb.append(")");
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
                    sb.append("]");
                }
                else {
                    sb.append("({})");
                }
            }
            else*/
            if (val instanceof SimpleScriptObject) {
                if (level == 0) {
                    sb.append("[object ");
                    sb.append(((SimpleScriptObject) val).getClassName());
                    sb.append("]");
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
            final StringBuffer sb = new StringBuffer();
            sb.append("\"");
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
            sb.append("\"");
            return sb.toString();
        }

        private static String formatToString(final Object o) {
            if (o == null) {
                return "null";
            }
//            else if (o instanceof NativeFunction) {
//                return ((NativeFunction) o).toString();
//            }
//            else if (o instanceof BaseFunction) {
//                return "function " + ((BaseFunction) o).getFunctionName()
//                        + "\n" + "    [native code]\n" + "}";
//            }
//            else if (o instanceof NativeArray) {
//                // If an array is embedded inside another array, just return
//                // "[object Object]"
//                return "[object Object]";
//            }
//            else if (o instanceof Delegator) {
//                return "[object " + ((Delegator) o).getDelegee().getClassName()
//                        + "]";
//            }
//            else if (o instanceof NativeObject) {
//                return "[object " + ((NativeObject) o).getClassName() + "]";
//            }
            else if (o instanceof SimpleScriptObject) {
                return "[object " + ((SimpleScriptObject) o).getClassName() + "]";
            }
            else {
                return o.toString();
            }
        }

        @Override
        public String printObject(final Object o) {
            final StringBuilder sb = new StringBuilder();
            appendValue(o, sb, 0);
            return sb.toString();
        }

        @Override
        public String parameterAsString(final Object o) {
//            if (o instanceof NativeArray) {
//                final StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < ((NativeArray) o).size(); i++) {
//                    if (i > 0) {
//                        sb.append(",");
//                    }
//                    sb.append(formatToString(((NativeArray) o).get(i)));
//                }
//                return sb.toString();
//            }
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
