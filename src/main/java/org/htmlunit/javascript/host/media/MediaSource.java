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
package org.htmlunit.javascript.host.media;

import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.javascript.host.event.EventTarget;

/**
 * A JavaScript object for {@code MediaSource}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/MediaSource">MDN Documentation</a>
 */
@JsxClass
public class MediaSource extends EventTarget {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns whether the specified MIME type is supported.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/MediaSource/isTypeSupported">
     *     MDN Documentation</a>
     *
     * @param mimeType the MIME type to check
     * @return {@code true} if the current user agent supports creating
     *         {@code SourceBuffer} objects for the specified MIME type
     */
    @JsxStaticFunction
    public static boolean isTypeSupported(final String mimeType) {
        return false;
    }
}
