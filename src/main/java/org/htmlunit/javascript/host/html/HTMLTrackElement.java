/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import static org.htmlunit.BrowserVersionFeatures.HTMLTRACK_END_TAG_FORBIDDEN;

import org.htmlunit.html.HtmlTrack;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;

/**
 * The JavaScript object {@code HTMLTrackElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlTrack.class)
public class HTMLTrackElement extends HTMLElement {

    /** Constant. */
    @JsxConstant
    public static final int NONE = 0;

    /** Constant. */
    @JsxConstant
    public static final int LOADING = 1;

    /** Constant. */
    @JsxConstant
    public static final int LOADED = 2;

    /** Constant. */
    @JsxConstant
    public static final int ERROR = 3;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        if (getBrowserVersion().hasFeature(HTMLTRACK_END_TAG_FORBIDDEN)) {
            return true;
        }
        return super.isEndTagForbidden();
    }
}
