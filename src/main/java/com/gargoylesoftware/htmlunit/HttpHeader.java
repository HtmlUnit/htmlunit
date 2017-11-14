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
package com.gargoylesoftware.htmlunit;

/**
 * Various constants.
 *
 * @author Ronald Brill
 */
public final class HttpHeader {

    /** Referer. */
    public static final String REFERER = "Referer";
    /** referer. */
    public static final String REFERER_LC = "referer";

    /** Origin. */
    public static final String ORIGIN = "Origin";
    /** origin. */
    public static final String ORIGIN_LC = "origin";

    /** Cache-Control. */
    public static final String CACHE_CONTROL = "Cache-Control";

    /** Accept. */
    public static final String ACCEPT = "Accept";
    /** accept. */
    public static final String ACCEPT_LC = "accept";

    /** Accept-Language. */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    /** accept-language. */
    public static final String ACCEPT_LANGUAGE_LC = "accept-language";

    /** Accept-Encoding. */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    /** accept-encoding. */
    public static final String ACCEPT_ENCODING_LC = "accept-encoding";

    /** User-Agent. */
    public static final String USER_AGENT = "User-Agent";
    /** user-agent. */
    public static final String USER_AGENT_LC = "user-agent";

    /** Host. */
    public static final String HOST = "Host";
    /** host. */
    public static final String HOST_LC = "host";

    /** Content-Length. */
    public static final String CONTENT_LENGTH = "Content-Length";
    /** content-length. */
    public static final String CONTENT_LENGTH_LC = "content-length";

    /** Content-Type. */
    public static final String CONTENT_TYPE = "Content-Type";
    /** content-type. */
    public static final String CONTENT_TYPE_LC = "content-type";

    /** content-language. */
    public static final String CONTENT_LANGUAGE_LC = "content-language";

    /** Cookie. */
    public static final String COOKIE = "Cookie";
    /** cookie. */
    public static final String COOKIE_LC = "cookie";

    /** Connection. */
    public static final String CONNECTION = "Connection";
    /** connection. */
    public static final String CONNECTION_LC = "connection";

    /** DNT. */
    public static final String DNT = "DNT";

    private HttpHeader() {
    }
}
