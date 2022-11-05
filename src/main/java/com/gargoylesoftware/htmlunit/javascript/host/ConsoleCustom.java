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
package com.gargoylesoftware.htmlunit.javascript.host;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeConsole;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Contains some missing features of Rhino {@link NativeConsole}.
 *
 * @author Ronald Brill
 */
public final class ConsoleCustom {

    private ConsoleCustom() {
    }

    /**
     * Adds a single marker to the browser's Performance or Waterfall tool.
     * This lets you correlate a point in your code with the other events
     * recorded in the timeline, such as layout and paint events.
     * <p>Currently a noop because timeline is not supported so far</p>
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     */
    public static void timeStamp(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        // noop
    }
}
