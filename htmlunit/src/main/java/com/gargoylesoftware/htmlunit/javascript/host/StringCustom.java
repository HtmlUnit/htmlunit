/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Contains some missing features of Rhino NativeString.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class StringCustom {

    private StringCustom() { }

    /**
     * Determines whether one string may be found within another string,
     * returning true or false as appropriate.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return true or false
     */
    public static boolean contains(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        if (args.length < 1) {
            return false;
        }
        final String string = Context.toString(thisObj);
        final String search = Context.toString(args[0]);

        if (args.length < 2) {
            return string.contains(search);
        }

        final int start = (int) Math.max(0, Context.toNumber(args[1]));
        return string.indexOf(search, start) > -1;
    }
}
