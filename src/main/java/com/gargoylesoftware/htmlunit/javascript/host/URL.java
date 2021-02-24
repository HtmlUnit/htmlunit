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
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.net.MalformedURLException;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;
import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code URL}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class URL extends SimpleScriptable {

    private java.net.URL url_;

    /**
     * Creates an instance.
     */
    public URL() {
    }

    /**
     * Creates an instance.
     * @param url a string representing an absolute or relative URL.
     * If url is a relative URL, base is required, and will be used
     * as the base URL. If url is an absolute URL, a given base will be ignored.
     * @param base a string representing the base URL to use in case url
     * is a relative URL. If not specified, it defaults to ''.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public URL(final String url, final Object base) {
        String baseStr = null;
        if (!Undefined.isUndefined(base)) {
            baseStr = Context.toString(base);
        }

        try {
            if (StringUtils.isBlank(baseStr)) {
                url_ = UrlUtils.toUrlUnsafe(url);
            }
            else {
                final java.net.URL baseUrl = UrlUtils.toUrlUnsafe(baseStr);
                url_ = new java.net.URL(baseUrl, url);
            }
        }
        catch (final MalformedURLException e) {
            throw ScriptRuntime.typeError(e.toString());
        }
    }

    /**
     * The URL.createObjectURL() static method creates a DOMString containing a URL
     * representing the object given in parameter.
     * The URL lifetime is tied to the document in the window on which it was created.
     * The new object URL represents the specified File object or Blob object.
     *
     * @param fileOrBlob Is a File object or a Blob object to create a object URL for.
     * @return the url
     */
    @JsxStaticFunction
    public static String createObjectURL(final Object fileOrBlob) {
        if (fileOrBlob instanceof File) {
            final File file = (File) fileOrBlob;
            return file.getFile().toURI().normalize().toString();
        }

        return null;
    }

    /**
     * @param objectURL String representing the object URL that was
     *          created by calling URL.createObjectURL().
     */
    @JsxStaticFunction
    public static void revokeObjectURL(final Object objectURL) {
    }

    /**
     * @return the origin
     */
    @JsxGetter
    public Object getOrigin() {
        if (url_ == null) {
            return null;
        }

        return url_.getProtocol() + "://" + url_.getHost();
    }

    /**
     * @return the origin
     */
    @JsxGetter
    public URLSearchParams getSearchParams() {
        if (url_ == null) {
            return null;
        }

        return new URLSearchParams(url_.getQuery());
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (url_ == null) {
            return super.getDefaultValue(hint);
        }

        if (StringUtils.isEmpty(url_.getPath())) {
            return url_.toExternalForm() + "/";
        }
        return url_.toExternalForm();
    }
}
