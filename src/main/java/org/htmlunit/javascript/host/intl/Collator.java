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
package org.htmlunit.javascript.host.intl;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxSymbolConstant;

/**
 * A JavaScript object for Intl.Collator.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/Collator">MDN Documentation</a>
 */
@JsxClass
public class Collator extends HtmlUnitScriptable {

    /** Symbol.toStringTag support. */
    @JsxSymbolConstant
    public static final String TO_STRING_TAG = "Intl.Collator";

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the Collator constructor
     * @param ctorObj the function object
     * @param inNewExpr {@code true} if invoked with the {@code new} operator
     * @return the Java object that JavaScript can access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        // the locales are validated but not used so far
        Intl.canonicalizeLocaleList(cx, Intl.firstArgument(args));

        final Collator format = new Collator();
        format.setParentScope(scope);
        format.setPrototype(((FunctionObject) ctorObj).getClassPrototype());
        return format;
    }

    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (String.class.equals(hint) || hint == null) {
            return "[object Intl.Collator]";
        }
        return super.getDefaultValue(hint);
    }
}
