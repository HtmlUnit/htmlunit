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
package com.gargoylesoftware.htmlunit;

/**
 * Various constants.
 *
 * @author Ronald Brill
 * @author Anton Demydenko
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

    /** Last-Modified. */
    public static final String LAST_MODIFIED = "Last-Modified";

    /** Expires. */
    public static final String EXPIRES = "Expires";

    /** Accept. */
    public static final String ACCEPT = "Accept";
    /** Accept-LC. */
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

    /** Connection. */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    /** DNT. */
    public static final String DNT = "DNT";

    /** Upgrade-Insecure-Requests. */
    public static final String UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";

    /** Sec-Fetch-Dest. */
    public static final String SEC_FETCH_DEST = "Sec-Fetch-Dest";

    /** Sec-Fetch-Mode. */
    public static final String SEC_FETCH_MODE = "Sec-Fetch-Mode";

    /** Sec-Fetch-Site. */
    public static final String SEC_FETCH_SITE = "Sec-Fetch-Site";

    /** Sec-Fetch-User. */
    public static final String SEC_FETCH_USER = "Sec-Fetch-User";

    /** Access-Control-Request-Method. */
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    /** Access-Control-Request-Headers. */
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    /** Access-Control-Allow-Origin. */
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    /** Access-Control-Allow-Credentials. */
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    /** Access-Control-Allow-Headers. */
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    /** Ping-From. */
    public static final String PING_FROM = "Ping-From";

    /** Ping-From. */
    public static final String PING_TO = "Ping-To";

    /** X-Frame-Options. */
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";

    /** Content-Security-Policy. */
    public static final String CONTENT_SECURIRY_POLICY = "Content-Security-Policy";

    private HttpHeader() {
    }
}
