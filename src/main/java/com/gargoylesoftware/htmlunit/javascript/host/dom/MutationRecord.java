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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for {@code MutationRecord}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class MutationRecord extends SimpleScriptable {

    private String type_;
    private ScriptableObject target_;
    private String oldValue_;
    private String attributeName_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public MutationRecord() {
    }

    /**
     * Sets the {@code type} property.
     * @param type the {@code type} property
     */
    void setType(final String type) {
        type_ = type;
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return type_;
    }

    /**
     * Sets the {@code target} property.
     * @param target the {@code target} property
     */
    void setTarget(final ScriptableObject target) {
        target_ = target;
    }

    /**
     * Returns the {@code target} property.
     * @return the {@code target} property
     */
    @JsxGetter
    public ScriptableObject getTarget() {
        return target_;
    }

    /**
     * Sets the {@code oldValue} property.
     * @param oldValue the {@code oldValue} property
     */
    void setOldValue(final String oldValue) {
        oldValue_ = oldValue;
    }

    /**
     * Returns the {@code oldValue} property.
     * @return the {@code oldValue} property
     */
    @JsxGetter
    public String getOldValue() {
        return oldValue_;
    }

    /**
     * Sets the {@code attributeName} property.
     * @param attributeName the {@code attributeName} property
     */
    void setAttributeName(final String attributeName) {
        attributeName_ = attributeName;
    }

    /**
     * Returns the {@code attributeName} property.
     * @return the {@code attributeName} property
     */
    @JsxGetter
    public String getAttributeName() {
        return attributeName_;
    }

}
