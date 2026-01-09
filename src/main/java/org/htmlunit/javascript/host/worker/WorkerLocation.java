/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.worker;

import java.net.URL;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.util.UrlUtils;

/**
 * A JavaScript object for WorkerLocation.
 *
 * @author Ronald Brill
 */
@JsxClass
public class WorkerLocation extends HtmlUnitScriptable {

    private final URL url_;
    private final String origin_;

    /**
     * Default constructor.
     */
    public WorkerLocation() {
        // prototype constructor
        super();
        url_ = null;
        origin_ = null;
    }

    WorkerLocation(final URL url, final String origin) {
        url_ = url;
        origin_ = origin;
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (url_ != null && (hint == null || String.class.equals(hint))) {
            return getHref();
        }
        return super.getDefaultValue(hint);
    }

    /**
     * @return a string containing the serialized URL for the worker's location.
     */
    @JsxGetter
    public String getHref() {
        String s = url_.toExternalForm();
        if (s.startsWith("file:/") && !s.startsWith("file:///")) {
            // Java (sometimes?) returns file URLs with a single slash; however, browsers return
            // three slashes. See http://www.cyanwerks.com/file-url-formats.html for more info.
            s = "file:///" + s.substring("file:/".length());
        }
        return s;
    }

    /**
     * @return the protocol portion of the location URL, including the trailing ':'
     */
    @JsxGetter
    public String getProtocol() {
        return url_.getProtocol() + ":";
    }

    /**
     * @return the host portion of the location URL
     */
    @JsxGetter
    public String getHost() {
        final int port = url_.getPort();
        final String host = url_.getHost();

        if (port == -1) {
            return host;
        }
        return host + ":" + port;
    }

    /**
     * @return the hostname portion of the location URL
     */
    @JsxGetter
    public String getHostname() {
        return url_.getHost();
    }

    /**
     * @return the {@code origin} property
     */
    @JsxGetter
    public String getOrigin() {
        return origin_;
    }

    /**
     * @return the port portion of the location URL
     */
    @JsxGetter
    public String getPort() {
        final int port = url_.getPort();
        if (port == -1) {
            return "";
        }
        return Integer.toString(port);
    }

    /**
     * @return the pathname portion of the location URL
     */
    @JsxGetter
    public String getPathname() {
        if (UrlUtils.URL_ABOUT_BLANK == url_) {
            return "blank";
        }
        return url_.getPath();
    }

    /**
     * @return the search portion of the location URL
     */
    @JsxGetter
    public String getSearch() {
        final String search = url_.getQuery();
        if (search == null) {
            return "";
        }
        return "?" + search;
    }

    /**
     * @return the hash portion of the location URL
     */
    @JsxGetter
    public String getHash() {
        final String ref = url_.getRef();
        return ref == null ? "" : "#" + ref;
    }
}
