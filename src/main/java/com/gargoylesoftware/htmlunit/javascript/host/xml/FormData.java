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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_DATA_CONTENT_TYPE_PLAIN_IF_FILE_TYPE_UNKNOWN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_DATA_ITERATOR_SIMPLE_NAME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ES6Iterator;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for {@code FormData}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 */
@JsxClass
public class FormData extends SimpleScriptable {

    /** Constant used to register the prototype in the context. */
    public static final String FORM_DATA_TAG = "FormData";

    private final List<NameValuePair> requestParameters_ = new ArrayList<>();

    public static final class FormDataIterator extends ES6Iterator {

        private String className_;
        private List<NameValuePair> nameValuePairList_;
        private int index_;

        public static void init(final ScriptableObject scope, final String className) {
            ES6Iterator.init(scope, false, new FormDataIterator(className), FORM_DATA_TAG);
        }

        public FormDataIterator(final String className) {
            index_ = 0;
            nameValuePairList_ = Collections.emptyList();
            className_ = className;
        }

        public FormDataIterator(final Scriptable scope, final String className,
                final List<NameValuePair> nameValuePairList) {
            super(scope, FORM_DATA_TAG);
            index_ = 0;
            nameValuePairList_ = nameValuePairList;
            className_ = className;
        }

        @Override
        public String getClassName() {
            return className_;
        }

        @Override
        protected boolean isDone(final Context cx, final Scriptable scope) {
            return index_ >= nameValuePairList_.size();
        }

        @Override
        protected Object nextValue(final Context cx, final Scriptable scope) {
            if (isDone(cx, scope)) {
                return Context.getUndefinedValue();
            }

            final NameValuePair nextNameValuePair = nameValuePairList_.get(index_++);
            return cx.newArray(scope, new Object[] {nextNameValuePair.getName(), nextNameValuePair.getValue()});
        }
    }

    /**
     * Default constructor.
     */
    public FormData() {
    }

    /**
     * Constructor.
     * @param formObj a form
     */
    @JsxConstructor
    public FormData(final Object formObj) {
        if (formObj instanceof HTMLFormElement) {
            final HTMLFormElement form = (HTMLFormElement) formObj;
            requestParameters_.addAll(form.getHtmlForm().getParameterListForSubmit(null));
        }
    }

    /**
     * Appends a new value onto an existing key inside a {@code FormData} object,
     * or adds the key if it does not already exist.
     * @param name the name of the field whose data is contained in {@code value}
     * @param value the field's value
     * @param filename the filename reported to the server (optional)
     */
    @JsxFunction
    public void append(final String name, final Object value, final Object filename) {
        if (value instanceof File) {
            final File file = (File) value;
            String fileName = null;
            String contentType = null;
            if (filename instanceof String) {
                fileName = (String) filename;
            }
            contentType = file.getType();
            if (StringUtils.isEmpty(contentType)) {
                if (getBrowserVersion().hasFeature(JS_FORM_DATA_CONTENT_TYPE_PLAIN_IF_FILE_TYPE_UNKNOWN)) {
                    contentType = MimeType.TEXT_PLAIN;
                }
                else {
                    contentType = MimeType.APPLICATION_OCTET_STREAM;
                }
            }
            requestParameters_.add(new KeyDataPair(name, file.getFile(), fileName, contentType, (Charset) null));
        }
        else {
            requestParameters_.add(new NameValuePair(name, Context.toString(value)));
        }
    }

    /**
     * Removes the entry (if exists).
     * @param name the name of the field to remove
     */
    @JsxFunction(functionName = "delete", value = {CHROME, EDGE, FF, FF78})
    public void delete_js(final String name) {
        if (StringUtils.isEmpty(name)) {
            return;
        }

        final Iterator<NameValuePair> iter = requestParameters_.iterator();
        while (iter.hasNext()) {
            final NameValuePair pair = iter.next();
            if (name.equals(pair.getName())) {
                iter.remove();
            }
        }
    }

    /**
     * @param name the name of the field to check
     * @return the first value found for the give name
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public String get(final String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        final Iterator<NameValuePair> iter = requestParameters_.iterator();
        while (iter.hasNext()) {
            final NameValuePair pair = iter.next();
            if (name.equals(pair.getName())) {
                return pair.getValue();
            }
        }
        return null;
    }

    /**
     * @param name the name of the field to check
     * @return the values found for the give name
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public Scriptable getAll(final String name) {
        if (StringUtils.isEmpty(name)) {
            return Context.getCurrentContext().newArray(this, 0);
        }

        final List<Object> values = new ArrayList<>();
        final Iterator<NameValuePair> iter = requestParameters_.iterator();
        while (iter.hasNext()) {
            final NameValuePair pair = iter.next();
            if (name.equals(pair.getName())) {
                values.add(pair.getValue());
            }
        }

        final Object[] stringValues = values.toArray(new Object[values.size()]);
        return Context.getCurrentContext().newArray(this, stringValues);
    }

    /**
     * @param name the name of the field to check
     * @return true if the name exists
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public boolean has(final String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }

        final Iterator<NameValuePair> iter = requestParameters_.iterator();
        while (iter.hasNext()) {
            final NameValuePair pair = iter.next();
            if (name.equals(pair.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets a new value for an existing key inside a {@code FormData} object,
     * or adds the key if it does not already exist.
     * @param name the name of the field whose data is contained in {@code value}
     * @param value the field's value
     * @param filename the filename reported to the server (optional)
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public void set(final String name, final Object value, final Object filename) {
        if (StringUtils.isEmpty(name)) {
            return;
        }

        int pos = -1;

        final Iterator<NameValuePair> iter = requestParameters_.iterator();
        int idx = 0;
        while (iter.hasNext()) {
            final NameValuePair pair = iter.next();
            if (name.equals(pair.getName())) {
                iter.remove();
                if (pos < 0) {
                    pos = idx;
                }
            }
            idx++;
        }

        if (pos < 0) {
            pos = requestParameters_.size();
        }

        if (value instanceof File) {
            final File file = (File) value;
            String fileName = null;
            if (filename instanceof String) {
                fileName = (String) filename;
            }
            requestParameters_.add(pos,
                    new KeyDataPair(name, file.getFile(), fileName, file.getType(), (Charset) null));
        }
        else {
            requestParameters_.add(pos, new NameValuePair(name, Context.toString(value)));
        }
    }

    /**
     * @return An Iterator that contains all the requestParameters name[0] and value[1]
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public Scriptable entries() {
        if (getBrowserVersion().hasFeature(JS_FORM_DATA_ITERATOR_SIMPLE_NAME)) {
            return new FormDataIterator(this, "Iterator", requestParameters_);
        }

        return new FormDataIterator(this, "FormData Iterator", requestParameters_);
    }

    /**
     * Sets the specified request with the parameters in this {@code FormData}.
     * @param webRequest the web request to fill
     */
    public void fillRequest(final WebRequest webRequest) {
        webRequest.setEncodingType(FormEncodingType.MULTIPART);
        webRequest.setRequestParameters(requestParameters_);
    }
}
