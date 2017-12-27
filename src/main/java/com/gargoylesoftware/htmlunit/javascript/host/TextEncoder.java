/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;

import java.nio.charset.StandardCharsets;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8Array;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code TextEncoder}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass({CHROME, FF})
public class TextEncoder extends SimpleScriptable {

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public TextEncoder() {
    }

    /**
     * @return always "utf-8"
     */
    @JsxGetter
    public String getEncoding() {
        return "utf-8";
    }

    /**
     * @param toEncode the string to encode
     * @return returns a Uint8Array containing the text given encoded .
     */
    @JsxFunction
    public Uint8Array encode(final Object toEncode) {
        if (Undefined.instance == toEncode) {
            return new Uint8Array(new byte[0], getWindow());
        }

        final String txt;
        if (toEncode == null) {
            txt = "null";
        }
        else {
            txt = Context.toString(toEncode);
        }

        final byte[] bytes = txt.getBytes(StandardCharsets.UTF_8);
        return new Uint8Array(bytes, getWindow());
    }
}
