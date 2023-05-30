/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;

/**
 * A JavaScript object for {@code Reflect}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
@JsxClass({CHROME, EDGE, FF, FF_ESR})
public class Reflect extends HtmlUnitScriptable {

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setParentScope(final Scriptable scope) {
        super.setParentScope(scope);

        try {
            final FunctionObject functionObject = new FunctionObject("has",
                    getClass().getDeclaredMethod("has", Scriptable.class, String.class), this);
            defineProperty("has", functionObject, ScriptableObject.DONTENUM);
        }
        catch (final Exception e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * The static Reflect.has() method works like the in operator as a function.
     *
     * @param target The target object in which to look for the property.
     * @param propertyKey The name of the property to check.
     * @return true or false
     */
    public boolean has(final Scriptable target, final String propertyKey) {
        return ScriptableObject.hasProperty(target, propertyKey);
    }
}
