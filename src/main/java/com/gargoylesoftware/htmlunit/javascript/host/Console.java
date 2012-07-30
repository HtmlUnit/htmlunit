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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.WebConsole;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a console. This implementation simulates Firefox's console, which utilizes FireBug API.
 *
 * @version $Revision$
 * @author Andrea Martino
 */
public class Console extends SimpleScriptable {

    private static final Map<String, Long> TIMERS = new HashMap<String, Long>();

    private WebWindow webWindow_;

    /**
     * Sets the Window JavaScript object this console belongs to.
     * @param webWindow the Window JavaScript object this console belongs to
     */
    public void setWebWindow(final WebWindow webWindow) {
        webWindow_ = webWindow;
    }

    /**
     * This method performs logging to the console at "log" level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     * @throws IOException if an IO error occurs
     */
    public static void jsxFunction_log(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) throws IOException {
        final Console console = (Console) thisObj;
        console.getWebConsole().info(args);
    }

    /**
     * This method performs logging to the console at "info" level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     * @throws IOException if an IO error occurs
     */
    public static void jsxFunction_info(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) throws IOException {
        final Console console = (Console) thisObj;
        console.getWebConsole().info(args);
    }

    /**
     * This method performs logging to the console at "warn" level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     * @throws IOException if an IO error occurs
     */
    public static void jsxFunction_warn(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) throws IOException {
        final Console console = (Console) thisObj;
        console.getWebConsole().warn(args);
    }

    /**
     * This method performs logging to the console at "error" level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     * @throws IOException if an IO error occurs
     */
    public static void jsxFunction_error(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) throws IOException {
        final Console console = (Console) thisObj;
        console.getWebConsole().error(args);
    }

    /**
     * This method performs logging to the console at "debug" level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     * @throws IOException if an IO error occurs
     */
    public static void jsxFunction_debug(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) throws IOException {
        final Console console = (Console) thisObj;
        console.getWebConsole().debug(args);
    }

    /**
     * This method performs logging to the console at "trace" level.
     * @param cx the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param funObj the function
     * @throws IOException if an IO error occurs
     */
    public static void jsxFunction_trace(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) throws IOException {
        final Console console = (Console) thisObj;
        console.getWebConsole().trace(args);
    }

    private WebConsole getWebConsole() {
        return webWindow_.getWebClient().getWebConsole();
    }

    /**
     * Implementation of console dir function. This method does not enter recursively
     * in the passed object, nor prints the details of objects or functions.
     * @param o the object to be printed
     * @throws IOException if an IO error occurs
     */
    public void jsxFunction_dir(final Object o) throws IOException {
        if (o instanceof ScriptableObject) {
            final ScriptableObject obj = (ScriptableObject) o;
            final Object[] ids = obj.getIds();
            if (ids != null && ids.length > 0) {
                final StringBuffer sb = new StringBuffer();
                for (Object id : ids) {
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
     * @throws IOException if an IO error occurs
     */
    public void jsxFunction_group() throws IOException {
        // TODO not implemented
    }

    /**
     * Implementation of endGroup. Currently missing.
     * @throws IOException if an IO error occurs
     */
    public void jsxFunction_groupEnd() throws IOException {
        // TODO not implemented
    }

    /**
     * Implementation of groupCollapsed. Currently missing.
     * @throws IOException if an IO error occurs
     */
    public void jsxFunction_groupCollapsed() throws IOException {
         // TODO not implemented
    }

    /**
     * This method replicates Firefox's behavior: if the timer already exists,
     * the start time is not overwritten. In both cases, the line is printed on the
     * console.
     * @param timerName the name of the timer
     * @throws IOException if an IO error occurs
     */
    public void jsxFunction_time(final String timerName) throws IOException {
        if (!TIMERS.containsKey(timerName)) {
            TIMERS.put(timerName, Long.valueOf(System.currentTimeMillis()));
        }
        getWebConsole().info(timerName + ": timer started");
    }

    /**
     * This method replicates Firefox's behavior: if no timer is found, nothing is
     * logged to the console.
     * @param timerName the name of the timer
     * @throws IOException if an IO error occurs
     */
    public void jsxFunction_timeEnd(final String timerName) throws IOException {
        final Long startTime = TIMERS.remove(timerName);
        if (startTime != null) {
            getWebConsole().info(timerName + ": " + (System.currentTimeMillis() - startTime.longValue()) + "ms");
        }
    }
}
