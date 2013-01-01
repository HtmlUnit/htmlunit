/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;
import net.sourceforge.htmlunit.corejs.javascript.debug.Debugger;

/**
 * An adapter class for debugger implementations. The methods in this class are empty. This class
 * exists as a convenience for creating debugger objects.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class DebuggerAdapter implements Debugger {

    /** {@inheritDoc} */
    public void handleCompilationDone(final Context cx, final DebuggableScript functionOrScript, final String source) {
        // Empty.
    }

    /** {@inheritDoc} */
    public DebugFrame getFrame(final Context cx, final DebuggableScript fnOrScript) {
        return new DebugFrameAdapter();
    }

}
