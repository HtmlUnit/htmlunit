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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeConsole;
import net.sourceforge.htmlunit.corejs.javascript.NativeConsole.ConsolePrinter;
import net.sourceforge.htmlunit.corejs.javascript.NativeConsole.Level;
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
 * @author Ronald Brill
 */
public class WebConsole implements ConsolePrinter, Serializable {

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

    @Override
    public void print(final Context cx, final Scriptable scope, final Level level,
            final Object[] args, final ScriptStackElement[] stack) {

        switch (level) {
            case TRACE:
                if (logger_.isInfoEnabled()) {
                    String msg = NativeConsole.format(cx, scope, args);
                    if (stack != null) {
                        final StringBuilder scriptStack = new StringBuilder();
                        scriptStack.append(msg);

                        for (final ScriptStackElement scriptStackElement : stack) {
                            if (scriptStack.length() > 0) {
                                scriptStack.append('\n');
                            }
                            scriptStack.append(scriptStackElement);
                        }

                        msg = scriptStack.toString();
                    }
                    logger_.info(msg);
                }
                break;
            case DEBUG:
                if (logger_.isDebugEnabled()) {
                    logger_.debug(NativeConsole.format(cx, scope, args));
                }
                break;
            case INFO:
                if (logger_.isInfoEnabled()) {
                    logger_.info(NativeConsole.format(cx, scope, args));
                }
                break;
            case WARN:
                if (logger_.isWarnEnabled()) {
                    logger_.warn(NativeConsole.format(cx, scope, args));
                }
                break;
            case ERROR:
                if (logger_.isErrorEnabled()) {
                    logger_.error(NativeConsole.format(cx, scope, args));
                }
                break;

            default:
                break;
        }
    }

    /**
     * This class is the default logger used by WebConsole.
     */
    private static class DefaultLogger implements Logger, Serializable {

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
}
