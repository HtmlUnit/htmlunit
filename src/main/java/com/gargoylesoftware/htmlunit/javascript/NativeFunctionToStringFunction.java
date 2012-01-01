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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Replacement (in fact a wrapper) for Rhino's native toString function on Function prototype
 * allowing to produce the desired formatting.
 * @author Marc Guillemot
 * @version $Revision$
 */
class NativeFunctionToStringFunction extends FunctionWrapper {
    private final String separator_;

    NativeFunctionToStringFunction(final Function wrapped, final String separator) {
        super(wrapped);
        separator_ = separator;
    }

    /**
     * {@inheritDoc}
     */
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        final String s = (String) super.call(cx, scope, thisObj, args);

        if (thisObj instanceof IdFunctionObject && s.contains("() { [native code for ")) {
            final String functionName = ((IdFunctionObject) thisObj).getFunctionName();
            return separator_ + "function " + functionName + "() {\n    [native code]\n}" + separator_;
        }
        return s.trim();
    }

    /**
     * Install the wrapper in place of the native toString function on Function's prototype.
     * @param window the scope
     * @param browserVersion the simulated browser
     */
    static void installFix(final Scriptable window, final BrowserVersion browserVersion) {
        final ScriptableObject fnPrototype = (ScriptableObject) ScriptableObject.getClassPrototype(window, "Function");
        final Function originalToString = (Function) ScriptableObject.getProperty(fnPrototype, "toString");
        final String separator;
        if (browserVersion.hasFeature(BrowserVersionFeatures.JS_NATIVE_FUNCTION_TOSTRING_NEW_LINE)) {
            separator = "\n";
        }
        else {
            separator = "";
        }
        final Function newToString = new NativeFunctionToStringFunction(originalToString, separator);
        ScriptableObject.putProperty(fnPrototype, "toString", newToString);
    }
}
