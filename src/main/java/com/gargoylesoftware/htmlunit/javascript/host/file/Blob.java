/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF68;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Blob}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class Blob extends SimpleScriptable {

    private static final String OPTIONS_TYPE_NAME = "type";
    //default according to https://developer.mozilla.org/en-US/docs/Web/API/Blob/Blob
    private static final String OPTIONS_TYPE_DEFAULT = "";

    private String mimeType;
    
    private NativeArray nativeArray;

    /**
     * Creates an instance.
     */
    public Blob() {
    }

    @JsxConstructor({CHROME, FF, FF68, IE})
    public Blob(final NativeArray array, final ScriptableObject properties) {
        this.nativeArray = (array == null ? new NativeArray(0) : array);
        this.mimeType = extractMimeTypeOrDefault(properties);
    }
    
    public NativeArray getNativeArray() {
        return nativeArray;
    }

    public String getMimeType() {
        return mimeType;
    }

    private String extractMimeTypeOrDefault(final ScriptableObject properties) {
        if (properties == null || Undefined.isUndefined(properties)) {
            return OPTIONS_TYPE_DEFAULT;
        }

        final Object optionsType = properties.get(OPTIONS_TYPE_NAME, properties);
        if (optionsType != null && properties != Scriptable.NOT_FOUND && !Undefined.isUndefined(optionsType)) {
            return Context.toString(optionsType);
        }

        return OPTIONS_TYPE_DEFAULT;
    }
    
}
