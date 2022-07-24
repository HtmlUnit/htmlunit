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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;

/**
 * A JavaScript object for {@code webkitURL}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(value = {CHROME, EDGE, FF, FF_ESR}, className = "webkitURL")
public class WebkitURL extends URL {

    /**
     * Creates an instance.
     */
    public WebkitURL() {
    }

    /**
     * Creates an instance.
     * @param url a string representing an absolute or relative URL.
     * If url is a relative URL, base is required, and will be used
     * as the base URL. If url is an absolute URL, a given base will be ignored.
     * @param base a string representing the base URL to use in case url
     * is a relative URL. If not specified, it defaults to ''.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public WebkitURL(final String url, final Object base) {
        super(url, base);
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
        return URL.createObjectURL(fileOrBlob);
    }

    /**
     * @param objectURL String representing the object URL that was
     *          created by calling URL.createObjectURL().
     */
    @JsxStaticFunction
    public static void revokeObjectURL(final Object objectURL) {
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getHash() {
        return super.getHash();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setHash(final String fragment) throws MalformedURLException {
        super.setHash(fragment);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getHost() {
        return super.getHost();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setHost(final String host) throws MalformedURLException {
        super.setHost(host);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getHostname() {
        return super.getHostname();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setHostname(final String hostname) throws MalformedURLException {
        super.setHostname(hostname);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getHref() {
        return super.getHref();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setHref(final String href) throws MalformedURLException {
        super.setHostname(href);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public Object getOrigin() {
        return super.getOrigin();
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public URLSearchParams getSearchParams() {
        return super.getSearchParams();
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setPassword(final String password) throws MalformedURLException {
        super.setPassword(password);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getPathname() {
        return super.getPathname();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setPathname(final String path) throws MalformedURLException {
        super.setPathname(path);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getPort() {
        return super.getPort();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setPort(final String port) throws MalformedURLException {
        super.setPort(port);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getProtocol() {
        return super.getProtocol();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setProtocol(final String protocol) throws MalformedURLException {
        super.setProtocol(protocol);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getSearch() {
        return super.getSearch();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setSearch(final String search) throws MalformedURLException {
        super.setSearch(search);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getUsername() {
        return super.getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setUsername(final String username) throws MalformedURLException {
        super.setUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @JsxFunction
    @Override
    public String toJSON() {
        return super.toJSON();
    }

    /**
     * {@inheritDoc}
     */
    @JsxFunction(functionName = "toString")
    @Override
    public String jsToString() {
        return super.jsToString();
    }
}
