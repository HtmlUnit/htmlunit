/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INTL_V8_BREAK_ITERATOR;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.RecursiveFunctionObject;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for Intl.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class Intl extends SimpleScriptable {

    /**
     * Define needed properties.
     * @param browserVersion the browser version
     */
    public void defineProperties(final BrowserVersion browserVersion) {
        setClassName("Object");
        define(Collator.class);
        define(DateTimeFormat.class);
        define(NumberFormat.class);
        if (browserVersion.hasFeature(JS_INTL_V8_BREAK_ITERATOR)) {
            define(V8BreakIterator.class);
        }
    }

    private void define(final Class<? extends SimpleScriptable> c) {
        try {
            final FunctionObject functionObject =
                    new RecursiveFunctionObject(c.getSimpleName(), c.getConstructor(), this);
            final SimpleScriptable scriptable = c.newInstance();
            if (c == V8BreakIterator.class) {
                scriptable.setClassName("v8BreakIterator");
            }
            functionObject.addAsConstructor(this, scriptable);
        }
        catch (final Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }
}
