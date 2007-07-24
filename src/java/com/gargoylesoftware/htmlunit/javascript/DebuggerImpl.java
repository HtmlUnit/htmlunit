/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 * {@link HtmlUnitContextFactory#setDebuggerEnabled(boolean)}, and make sure you loggers are
 * configured to output <tt>TRACE</tt> level log messages.
 * </p>
 *
 * @version $Revision: $
 * @author Daniel Gredler
 * @see DebugFrameImpl
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
