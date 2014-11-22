/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_NATIVE_FUNCTION_TOSTRING_COMPACT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_NATIVE_FUNCTION_TOSTRING_NEW_LINE;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Replacement (in fact a wrapper) for Rhino's native toString function on Function prototype
 * allowing to produce the desired formatting.
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ronald Brill
 */
class NativeFunctionToStringFunction extends FunctionWrapper {

    /**
     * Install the wrapper in place of the native toString function on Function's prototype.
     * @param window the scope
     * @param browserVersion the simulated browser
     */
    static void installFix(final Scriptable window, final BrowserVersion browserVersion) {
        if (browserVersion.hasFeature(JS_NATIVE_FUNCTION_TOSTRING_NEW_LINE)) {
            final ScriptableObject fnPrototype =
                    (ScriptableObject) ScriptableObject.getClassPrototype(window, "Function");
            final Function originalToString = (Function) ScriptableObject.getProperty(fnPrototype, "toString");
            final Function newToString = new NativeFunctionToStringFunction(originalToString);
            ScriptableObject.putProperty(fnPrototype, "toString", newToString);
        }
        else if (browserVersion.hasFeature(JS_NATIVE_FUNCTION_TOSTRING_COMPACT)) {
            final ScriptableObject fnPrototype =
                    (ScriptableObject) ScriptableObject.getClassPrototype(window, "Function");
            final Function originalToString = (Function) ScriptableObject.getProperty(fnPrototype, "toString");
            final Function newToString = new NativeFunctionToStringFunctionChrome(originalToString);
            ScriptableObject.putProperty(fnPrototype, "toString", newToString);
        }
    }

    NativeFunctionToStringFunction(final Function wrapped) {
        super(wrapped);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        final String s = (String) super.call(cx, scope, thisObj, args);

        if (thisObj instanceof BaseFunction && s.indexOf("[native code]") > -1) {
            final String functionName = ((BaseFunction) thisObj).getFunctionName();
            return "\nfunction " + functionName + "() {\n    [native code]\n}\n";
        }
        return s;
    }

    static class NativeFunctionToStringFunctionChrome extends FunctionWrapper {

        NativeFunctionToStringFunctionChrome(final Function wrapped) {
            super(wrapped);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
            final String s = (String) super.call(cx, scope, thisObj, args);

            if (thisObj instanceof BaseFunction && s.indexOf("[native code]") > -1) {
                final String functionName = ((BaseFunction) thisObj).getFunctionName();
                return "function " + functionName + "() { [native code] }";
            }
            return s;
        }
    }
}
