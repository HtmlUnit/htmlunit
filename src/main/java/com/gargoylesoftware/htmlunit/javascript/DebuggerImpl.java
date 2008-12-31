/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;

/**
 * <p>
 * HtmlUnit's implementation of the {@link Debugger} interface, which registers
 * {@link DebugFrameImpl} instances with Rhino for each new execution frame created. See <a
 * href="http://www.mozilla.org/rhino/rhino15R4-debugger.html">the Rhino documentation</a> or <a
 * href="http://lxr.mozilla.org/mozilla/source/js/rhino/src/org/mozilla/javascript/debug/Debugger.java">the
 * interface source code</a> for more information on the {@link Debugger} interface and its uses.
 * </p>
 *
 * <p>
 * Please note that this class is intended mainly to aid in the debugging and development of
 * HtmlUnit itself, rather than the debugging and development of web applications.
 * </p>
 *
 * <p>
 * In order to enable the debugging output, call
 * {@link HtmlUnitContextFactory#setDebugger(Debugger)}, passing in an instance of this class,
 * and make sure your loggers are configured to output <tt>TRACE</tt> level log messages.
 * </p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @see DebugFrameImpl
 * @see HtmlUnitContextFactory#setDebugger(Debugger)
 */
public class DebuggerImpl implements Debugger {

    /**
     * {@inheritDoc}
     */
    public DebugFrame getFrame(final Context cx, final DebuggableScript functionOrScript) {
        return new DebugFrameImpl(functionOrScript);
    }

    /**
     * {@inheritDoc}
     */
    public void handleCompilationDone(final Context cx, final DebuggableScript functionOrScript, final String source) {
        // Ignore: we don't care.
    }

}
