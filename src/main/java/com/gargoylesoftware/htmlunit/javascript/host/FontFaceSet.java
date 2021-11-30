/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.LambdaConstructor;
import net.sourceforge.htmlunit.corejs.javascript.LambdaFunction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for {@code FontFaceSet}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(isJSObject = false, value = {CHROME, EDGE})
@JsxClass({FF, FF_ESR})
public class FontFaceSet extends EventTarget {

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public FontFaceSet() {
    }

    /**
     * @param font a font specification using the CSS value syntax, e.g. "italic bold 16px Roboto"
     * @param text limit the font faces to those whose Unicode range contains at least one
     *          of the characters in text. This does not check for individual glyph coverage.
     * @return a Promise of an Array of FontFace loaded. The promise is fulfilled
     *          when all the fonts are loaded; it is rejected if one of the fonts failed to load.
     */
    @JsxFunction
    public Object load(final String font, final String text) {
        final Scriptable scope = ScriptableObject.getTopLevelScope(this);
        final LambdaConstructor ctor = (LambdaConstructor) getProperty(scope, "Promise");
        final LambdaFunction resolve = (LambdaFunction) getProperty(ctor, "resolve");
        return resolve.call(Context.getCurrentContext(), this, ctor, new Object[] {""});
    }
}
