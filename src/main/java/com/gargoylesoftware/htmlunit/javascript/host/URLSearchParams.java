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

import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code URLSearchParams}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Ween Jiann
 */
@JsxClass({CHROME, FF})
public class URLSearchParams extends SimpleScriptable {

    private final LinkedHashSet<Entry<String, String>> params_ = new LinkedHashSet<>();

    /**
     * Constructs a new instance.
     */
    public URLSearchParams() {
    }

    /**
     * Constructs a new instance.
     * @param params the params string
     */
    @JsxConstructor
    public URLSearchParams(final Object params) {
        // TODO: Pass in a sequence
        // new URLSearchParams([["foo", 1],["bar", 2]]);

        // TODO: Pass in a record
        // new URLSearchParams({"foo" : 1 , "bar" : 2});

        if (Undefined.instance == params) {
            return;
        }

        splitQuery(Context.toString(params));
    }

    private void splitQuery(String params) {
        params = StringUtils.stripStart(params, "?");
        if (StringUtils.isEmpty(params)) {
            return;
        }

        // TODO: encoding
        final String[] parts = StringUtils.split(params, '&');
        for (int i = 0; i < parts.length; i++) {
            params_.add(splitQueryParameter(parts[i]));
        }
    }

    private Entry<String, String> splitQueryParameter(final String singleParam) {
        final int idx = singleParam.indexOf('=');
        if (idx > 0) {
            final String key = singleParam.substring(0, idx);
            String value = null;
            if (idx < singleParam.length()) {
                value = singleParam.substring(idx + 1);
            }
            return new AbstractMap.SimpleEntry<>(key, value);
        }
        final String key = singleParam;
        final String value = null;
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        final StringBuilder paramStr = new StringBuilder();
        String delim = "";
        for (Entry<String, String> param : params_) {
            paramStr.append(delim);
            delim = "&";
            paramStr.append(param.getKey());
            paramStr.append("=");
            // TODO: need to encode value
            paramStr.append(param.getValue());
        }
        return paramStr.toString();
    }
}
