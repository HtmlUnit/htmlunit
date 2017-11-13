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

    private HttpHeader() {
    }
}
