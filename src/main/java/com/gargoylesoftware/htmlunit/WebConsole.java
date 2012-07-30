/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class can be used to print messages to the logger. The first parameter
 * can be a message-object containing format specifiers such as ("%o", "%s",
 * "%d", "%i", "%f"). The logging methods are null-safe, so if the number of
 * format specifiers and the numbers of parameters don't match, no exception is thrown.
 *
 * The default logger uses Apache Commons Logging.
 *
 * @version $Revision$
 * @author Andrea Martino
 */
public class WebConsole {

    /**
     * A simple logging interface abstracting logging APIs.
     */
    public interface Logger {

        /**
         * Logs a message with trace log level.
         *
         * @param message log this message
         */
        void trace(final Object message);

        /**
         * Logs a message with debug log level.
         *
         * @param message log this message
         */
        void debug(final Object message);

        /**
         * Logs a message with info log level.
         *
         * @param message log this message
         */
        void info(final Object message);

        /**
         * Logs a message with warn log level.
         *
         * @param message log this message
         */
        void warn(final Object message);

        /**
         * Logs a message with error log level.
         *
         * @param message log this message
         */
        void error(final Object message);
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
        String paramterAsString(Object o);

        /**
         * Function that is used to print an object as integer using
         * format specifiers.
         * @param o object to be printed using integer format specifiers
         * @return a string representation of the passed object
         */
        String paramterAsInteger(Object o);

        /**
         * Function that is used to print an object as float using
         * format specifiers.
         * @param o object to be printed using float format specifiers
         * @return a string representation of the passed object
         */
        String paramterAsFloat(Object o);
    }

    private Formatter formatter_  = new DefaultFormatter();
    private Logger logger_ = new DefaultLogger();

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
     * Prints the passed objects using logger trace level.
     * @param args the logging parameters
     */
    public void trace(final Object... args) {
        logger_.trace(process(args));
    }

    /**
     * Prints the passed objects using logger debug level.
     * @param args the logging parameters
     */
    public void debug(final Object... args) {
        logger_.debug(process(args));
    }

    /**
     * Prints the passed objects using logger info level.
     * @param args the logging parameters
     */
    public void info(final Object... args) {
        logger_.info(process(args));
    }

    /**
     * Prints the passed objects using logger warn level.
     * @param args the logging parameters
     */
    public void warn(final Object... args) {
        logger_.warn(process(args));
    }

    /**
     * Prints the passed objects using logger error level.
     * @param args the logging parameters
     */
    public void error(final Object... args) {
        logger_.error(process(args));
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

        final StringBuffer sb = new StringBuffer();
        final LinkedList<Object> args = new LinkedList<Object>(Arrays.asList(objs));

        final Formatter formatter = getFormatter();

        if (args.size() > 1 && args.get(0) instanceof String) {
            // Format specification regular expression.
            final Pattern p = Pattern.compile("%(\\d*).?(\\d*)[a-zA-Z]");
            final Matcher m = p.matcher((String) args.remove(0));

            while (m.find()) {
                final String group = m.group();
                final char ch = group.charAt(group.length() - 1);
                String replacement = null;
                switch (ch) {
                    case 'o':
                    case 's':
                        replacement = formatter.paramterAsString(pop(args));
                        break;
                    case 'd':
                    case 'i':
                        replacement = formatter.paramterAsInteger(pop(args));
                        break;
                    case 'f':
                        replacement =  formatter.paramterAsFloat(pop(args));
                        break;
                    default:
                        replacement = group;
                        break;
                }
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
        }

        for (final Object o : args) {
            if (sb.length() > 0) {
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
    private static class DefaultFormatter implements Formatter {

        public String printObject(final Object o) {
            return paramterAsString(o);
        }

        @Override
        public String paramterAsString(final Object o) {
            if (o != null) {
                return o.toString();
            }
            return "null";
        }

        @Override
        public String paramterAsInteger(final Object o) {
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
        public String paramterAsFloat(final Object o) {
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
    private static class DefaultLogger implements Logger {
        /** Logging support. */
        private static final Log LOG = LogFactory.getLog(WebConsole.class);

        @Override
        public void trace(final Object message) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(message);
            }
        }

        @Override
        public void debug(final Object message) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(message);
            }
        }

        @Override
        public void info(final Object message) {
            if (LOG.isInfoEnabled()) {
                LOG.info(message);
            }
        }

        @Override
        public void warn(final Object message) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(message);
            }
        }

        @Override
        public void error(final Object message) {
            if (LOG.isErrorEnabled()) {
                LOG.error(message);
            }
        }
    }
}
