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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

/**
 * The JavaScript object that represents an "Option", this is used to construct {@literal HTMLOptionElement}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class Option extends HTMLOptionElement {

    /**
     * JavaScript constructor.
     * <p>
     * {@inheritDoc}
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final Object newText, final String newValue,
            final boolean defaultSelected, final boolean selected) {
        super.jsConstructor(newText, newValue, defaultSelected, selected);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (String.class.equals(hint) || hint == null) {
            return "[object " + getClassName() + "]";
        }
        return super.getDefaultValue(hint);
    }
}
