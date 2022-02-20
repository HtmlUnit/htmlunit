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
package com.gargoylesoftware.htmlunit.javascript.polyfill;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Support to use polyfills for js features not implemented so far
 * (idea from Colin Alworth).
 *
 * @author Ronald Brill
 */
public class Polyfill {

    private static final Map<String, Polyfill> CACHE = new HashMap<>();

    private String url_;
    private String source_;
    private Script script_;

    public static Polyfill getFetchPolyfill() throws IOException {
        return getPolyfill("fetch/fetch.umd.js");
    }

    private static Polyfill getPolyfill(final String resouceName) throws IOException {
        Polyfill poly = CACHE.get(resouceName);
        if (poly != null) {
            return poly;
        }

        poly = new Polyfill();
        poly.source_ = IOUtils.toString(poly.getClass().getResourceAsStream(resouceName), StandardCharsets.UTF_8);
        poly.url_ = poly.getClass().getResource(resouceName).toExternalForm();

        return poly;
    }

    /**
     * Compile the script if needed and exec to setup the context.
     *
     * @param context the context
     * @param scriptable the scriptable
     */
    public void apply(final Context context, final Scriptable scriptable) {
        if (script_ == null) {
            script_ = context.compileString(source_, url_, 0, null);
        }

        if (script_ != null) {
            script_.exec(context, scriptable);
        }
    }
}
