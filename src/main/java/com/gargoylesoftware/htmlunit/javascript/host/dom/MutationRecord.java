/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for {@code MutationRecord}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
public class MutationRecord extends SimpleScriptable {

    private String type_;
    private ScriptableObject target_;
    private NodeList addedNodes_;
    private NodeList removedNodes_;
    private Node previousSibling_;
    private Node nextSibling_;
    private String attributeName_;
    private String attributeNamespace_;
    private String oldValue_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public MutationRecord() {
    }

    /**
     * Sets the {@code type} property.
     * @param type the {@code type} property
     */
    void setType(final String type) {
        this.type_ = type;
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
        this.target_ = target;
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
        this.oldValue_ = oldValue;
    }

    /**
     * Returns the {@code oldValue} property.
     * @return the {@code oldValue} property
     */
    @JsxGetter
    public String getOldValue() {
        return oldValue_;
    }

}
