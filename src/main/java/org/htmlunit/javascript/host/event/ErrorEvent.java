/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.event;

import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxAnyReturnType;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript host object for {@code ErrorEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/ErrorEvent">MDN Documentation</a>
 */
@JsxClass
public class ErrorEvent extends Event {

    private String message_ = "";
    private String filename_ = "";
    private int lineno_;
    private int colno_;
    private Object error_ = JavaScriptEngine.UNDEFINED;

    /**
     * Creates an instance of this event.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            final Object message = details.get("message");
            if (message != null && !JavaScriptEngine.isUndefined(message)) {
                message_ = JavaScriptEngine.toString(message);
            }
            final Object filename = details.get("filename");
            if (filename != null && !JavaScriptEngine.isUndefined(filename)) {
                filename_ = JavaScriptEngine.toString(filename);
            }
            final Object lineno = details.get("lineno");
            if (lineno != null && !JavaScriptEngine.isUndefined(lineno)) {
                lineno_ = JavaScriptEngine.toInt32(lineno);
            }
            final Object colno = details.get("colno");
            if (colno != null && !JavaScriptEngine.isUndefined(colno)) {
                colno_ = JavaScriptEngine.toInt32(colno);
            }
            final Object error = details.get("error");
            if (error != null) {
                error_ = error;
            }
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Initializes the error details (used for the error event of a script error).
     *
     * @param message the message
     * @param filename the url of the script
     * @param lineno the line number
     * @param colno the column number
     * @param error the error object
     */
    public void initErrorEvent(final String message, final String filename, final int lineno, final int colno,
            final Object error) {
        message_ = message;
        filename_ = filename;
        lineno_ = lineno;
        colno_ = colno;
        error_ = error;
    }

    /**
     * Returns the arguments for the {@code onerror} handler of a window (OnErrorEventHandler):
     * message, source, lineno, colno and error.
     *
     * @return the arguments
     */
    Object[] getOnErrorHandlerArgs() {
        return new Object[] {message_, filename_, Integer.valueOf(lineno_), Integer.valueOf(colno_), error_};
    }

    /**
     * Returns the error message.
     *
     * @return the error message
     */
    @JsxGetter
    public String getMessage() {
        return message_;
    }

    /**
     * Returns the url of the script the error occurred in.
     *
     * @return the url of the script the error occurred in
     */
    @JsxGetter
    public String getFilename() {
        return filename_;
    }

    /**
     * Returns the line number of the error.
     *
     * @return the line number of the error
     */
    @JsxGetter
    public int getLineno() {
        return lineno_;
    }

    /**
     * Returns the column number of the error.
     *
     * @return the column number of the error
     */
    @JsxGetter
    public int getColno() {
        return colno_;
    }

    /**
     * Returns the error object.
     *
     * @return the error object
     */
    @JsxGetter
    @JsxAnyReturnType
    public Object getError() {
        return error_;
    }
}
