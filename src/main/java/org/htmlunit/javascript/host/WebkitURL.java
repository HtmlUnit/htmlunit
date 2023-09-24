/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;

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
    @JsxConstructor
    public WebkitURL(final String url, final Object base) {
        super(url, base);
    }
}
