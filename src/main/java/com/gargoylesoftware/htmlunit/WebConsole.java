/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeConsole.ConsolePrinter;
import net.sourceforge.htmlunit.corejs.javascript.NativeConsole.Level;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptStackElement;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * This class can be used to print messages to the logger. The first parameter
 * can be a message-object containing format specifiers such as ("%o", "%s",
 * "%d", "%i", "%f"). The logging methods are null-safe, so if the number of
 * format specifiers and the numbers of parameters don't match, no exception is thrown.
 *
 * The default logger uses Apache Commons Logging.
 *
 * @author Andrea Martino
 */
public class WebConsole implements ConsolePrinter, Serializable {

    private static final Formatter CONSOLE_FORMATTER_ = new ConsoleFormatter();

    private Formatter formatter_ = new DefaultFormatter();
    private Logger logger_ = new DefaultLogger();

    /**
     * A simple logging interface abstracting logging APIs.
     */
    public interface Logger {

        /**
         * Is trace logging currently enabled?
         * <p>
         * Call this method to prevent having to perform expensive operations
         * (for example, <code>String</code> concatenation)
         * when the log level is more than trace.
         *
         * @return true if trace is enabled in the underlying logger.
         */
        boolean isTraceEnabled();

        /**
         * Logs a message with trace log level.
         *
         * @param message log this message
         */
        void trace(Object message);

        /**
         * Is debug logging currently enabled?
         * <p>
         * Call this method to prevent having to perform expensive operations
         * (for example, <code>String</code> concatenation)
         * when the log level is more than debug.
         *
         * @return true if debug is enabled in the underlying logger.
         */
        boolean isDebugEnabled();

        /**
         * Logs a message with debug log level.
         *
         * @param message log this message
         */
        void debug(Object message);

        /**
         * Is info logging currently enabled?
         * <p>
         * Call this method to prevent having to perform expensive operations
         * (for example, <code>String</code> concatenation)
         * when the log level is more than info.
         *
         * @return true if info is enabled in the underlying logger.
         */
        boolean isInfoEnabled();

        /**
         * Logs a message with info log level.
         *
         * @param message log this message
         */
        void info(Object message);

        /**
         * Is warn logging currently enabled?
         * <p>
         * Call this method to prevent having to perform expensive operations
         * (for example, <code>String</code> concatenation)
         * when the log level is more than warn.
         *
         * @return true if warn is enabled in the underlying logger.
         */
        boolean isWarnEnabled();

        /**
         * Logs a message with warn log level.
         *
         * @param message log this message
         */
        void warn(Object message);

        /**
         * Is error logging currently enabled?
         * <p>
         * Call this method to prevent having to perform expensive operations
         * (for example, <code>String</code> concatenation)
         * when the log level is more than error.
         *
         * @return true if error is enabled in the underlying logger.
         */
        boolean isErrorEnabled();

        /**
         * Logs a message with error log level.
         *
         * @param message log this message
         */
        void error(Object message);
    }

    /**
     * This interface can be implemented by clients that want to customize
     * the way parameters and objects are logged.
     */
    public interface Formatter {
        /**
         * Function that is used to print an object to the console.
         * @param o object to be printed
         * @return a string representation of the passed object
         */
        String printObject(Object o);

        /**
         * Function that is used to print an object as string using
         * format specifiers.
         * @param o object to be printed using string format specifiers
         * @return a string representation of the passed object
         */
        String parameterAsString(Object o);

        /**
         * Function that is used to print an object as integer using
         * format specifiers.
         * @param o object to be printed using integer format specifiers
         * @return a string representation of the passed object
         */
        String parameterAsInteger(Object o);

        /**
         * Function that is used to print an object as float using
         * format specifiers.
         * @param o object to be printed using float format specifiers
         * @return a string representation of the passed object
         */
        String parameterAsFloat(Object o);
    }

    /**
     * Sets the Formatter.
     * @param formatter the formatter
     */
    public void setFormatter(final Formatter formatter) {
        formatter_ = formatter;
    }

    /**
     * Returns the current Formatter.
     * @return the Formatter
     */
    public Formatter getFormatter() {
        return formatter_;
    }

    /**
     * Sets the Logger_.
     * @param logger the logger
     */
    public void setLogger(final Logger logger) {
        logger_ = logger;
    }

    /**
     * Returns the current Logger.
     * @return the logger
     */
    public Logger getLogger() {
        return logger_;
    }

    /**
     * Prints the passed objects using logger debug level.
     * @param args the logging parameters
     */
    public void debug(final Object... args) {
        if (logger_.isDebugEnabled()) {
            logger_.debug(process(args));
        }
    }

    /**
     * Prints the passed objects using logger info level.
     * @param args the logging parameters
     */
    public void info(final Object... args) {
        if (logger_.isInfoEnabled()) {
            logger_.info(process(args));
        }
    }

    /**
     * Prints the passed objects using logger warn level.
     * @param args the logging parameters
     */
    public void warn(final Object... args) {
        if (logger_.isWarnEnabled()) {
            logger_.warn(process(args));
        }
    }

    /**
     * Prints the passed objects using logger error level.
     * @param args the logging parameters
     */
    public void error(final Object... args) {
        if (logger_.isErrorEnabled()) {
            logger_.error(process(args));
        }
    }

    @Override
    public void print(final Context cx, final Scriptable scope, final Level level,
            final Object[] args, final ScriptStackElement[] stack) {

        final Formatter oldFormatter = getFormatter();
        setFormatter(CONSOLE_FORMATTER_);

        switch (level) {
            case TRACE:
                if (logger_.isTraceEnabled()) {
                    logger_.trace(process(args));
                }
                break;
            case DEBUG:
                debug(args);
                break;
            case INFO:
                info(args);
                break;
            case WARN:
                warn(args);
                break;
            case ERROR:
                error(args);
                break;

            default:
                break;
        }

        setFormatter(oldFormatter);
    }

    /**
     * This method is used by all the public method to process the passed
     * parameters.
     *
     * If the last parameter implements the Formatter interface, it will be
     * used to format the parameters and print the object.
     * @param objs the logging parameters
     * @return a String to be printed using the logger
     */
    private String process(final Object[] objs) {
        if (objs == null) {
            return "null";
        }

        final StringBuilder sb = new StringBuilder();
        final List<Object> args = new ArrayList<>(Arrays.asList(objs));

        final Formatter formatter = getFormatter();

        if (args.size() > 1 && args.get(0) instanceof String) {
            final StringBuilder msg = new StringBuilder((String) args.remove(0));
            int startPos = msg.indexOf("%");

            while (startPos > -1 && startPos < msg.length() - 1 && args.size() > 0) {
                if (startPos != 0 && msg.charAt(startPos - 1) == '%') {
                    // double %
                    msg.replace(startPos, startPos + 1, "");
                }
                else {
                    final char type = msg.charAt(startPos + 1);
                    String replacement = null;
                    switch (type) {
                        case 'o':
                        case 's':
                            replacement = formatter.parameterAsString(pop(args));
                            break;
                        case 'd':
                        case 'i':
                            replacement = formatter.parameterAsInteger(pop(args));
                            break;
                        case 'f':
                            replacement = formatter.parameterAsFloat(pop(args));
                            break;
                        default:
                            break;
                    }
                    if (replacement == null) {
                        startPos++;
                    }
                    else {
                        msg.replace(startPos, startPos + 2, replacement);
                        startPos = startPos + replacement.length();
                    }
                }
                startPos = msg.indexOf("%", startPos);
            }
            sb.append(msg);
        }

        for (final Object o : args) {
            if (sb.length() != 0) {
                sb.append(' ');
            }
            sb.append(formatter.printObject(o));
        }
        return sb.toString();
    }

    /**
     * This method removes and returns the first (i.e. at index 0) element in
     * the passed list if it exists. Otherwise, null is returned.
     * @param list the
     * @return the first object in the list or null
     */
    private static Object pop(final List<Object> list) {
        return list.isEmpty() ? null : list.remove(0);
    }

    /**
     * This class is the default formatter used by WebConsole.
     */
    private static class DefaultFormatter implements Formatter, Serializable {

        DefaultFormatter() {
        }

        @Override
        public String printObject(final Object o) {
            return parameterAsString(o);
        }

        @Override
        public String parameterAsString(final Object o) {
            if (o != null) {
                return o.toString();
            }
            return "null";
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

    /**
     * This class is the default logger used by WebConsole.
     */
    private static class DefaultLogger implements Logger, Serializable {
        /** Logging support. */
        private static final Log LOG = LogFactory.getLog(WebConsole.class);

        DefaultLogger() {
        }

        @Override
        public boolean isTraceEnabled() {
            return LOG.isTraceEnabled();
        }

        @Override
        public void trace(final Object message) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(message);
            }
        }

        @Override
        public boolean isDebugEnabled() {
            return LOG.isDebugEnabled();
        }

        @Override
        public void debug(final Object message) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(message);
            }
        }

        @Override
        public boolean isInfoEnabled() {
            return LOG.isInfoEnabled();
        }

        @Override
        public void info(final Object message) {
            LOG.info(message);
        }

        @Override
        public boolean isWarnEnabled() {
            return LOG.isWarnEnabled();
        }

        @Override
        public void warn(final Object message) {
            LOG.warn(message);
        }

        @Override
        public boolean isErrorEnabled() {
            return LOG.isErrorEnabled();
        }

        @Override
        public void error(final Object message) {
            LOG.error(message);
        }
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
                final Matcher m = p.matcher(val.toString());
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
            else if (val instanceof HtmlUnitScriptable) {
                if (level == 0) {
                    sb.append("[object ");
                    sb.append(((HtmlUnitScriptable) val).getClassName());
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
                sb.append(val.toString());
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
                            sb.append("\\u").append(Integer.toHexString(ch).toUpperCase(Locale.ROOT));
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
                return o.toString();
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
            else if (o instanceof HtmlUnitScriptable) {
                return "[object " + ((HtmlUnitScriptable) o).getClassName() + "]";
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
