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
package org.htmlunit.javascript.host.security;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript host object for {@code Credential}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Credential">MDN Documentation</a>
 */
@JsxClass
public class Credential extends HtmlUnitScriptable {

    private String id_;
    private String type_;

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Sets the identifier and the type of this credential.
     * @param id the identifier
     * @param type the type (e.g. {@code public-key})
     */
    protected void setIdAndType(final String id, final String type) {
        id_ = id;
        type_ = type;
    }

    /**
     * Returns the identifier of this credential.
     * @return the identifier
     */
    @JsxGetter
    public String getId() {
        return id_;
    }

    /**
     * Returns the type of this credential.
     * @return the type
     */
    @JsxGetter
    public String getType() {
        return type_;
    }
}
