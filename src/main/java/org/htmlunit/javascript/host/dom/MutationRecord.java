/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code MutationRecord}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class MutationRecord extends HtmlUnitScriptable {

    private String type_;
    private ScriptableObject target_;
    private String oldValue_;
    private String attributeName_;

    private NodeList addedNodes_;
    private NodeList removedNodes_;
    private Node previousSibling_;
    private Node nextSibling_;
    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
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

    /**
     * Sets the {@code addedNodes} property.
     * @param addedNodes the {@code addedNodes} property
     */
    void setAddedNodes(final NodeList addedNodes) {
        addedNodes_ = addedNodes;
    }

    /**
     * @return the {@code addedNodes} property
     */
    @JsxGetter
    public NodeList getAddedNodes() {
        return addedNodes_;
    }

    /**
     * Sets the {@code removedNodes} property.
     * @param removedNodes the {@code removedNodes} property
     */
    void setRemovedNodes(final NodeList removedNodes) {
        removedNodes_ = removedNodes;
    }

    /**
     * @return the {@code removedNodes} property
     */
    @JsxGetter
    public NodeList getRemovedNodes() {
        return removedNodes_;
    }

    /**
     * Sets the {@code previousSibling} property.
     * @param previousSibling the {@code previousSibling} property
     */
    void setPreviousSibling(final Node previousSibling) {
        previousSibling_ = previousSibling;
    }

    /**
     * @return the {@code previousSibling} property
     */
    @JsxGetter
    public Node getPreviousSibling() {
        return previousSibling_;
    }

    /**
     * Sets the {@code nextSibling} property.
     * @param nextSibling the {@code nextSibling} property
     */
    void setNextSibling(final Node nextSibling) {
        nextSibling_ = nextSibling;
    }

    /**
     * @return the {@code nextSibling} property
     */
    @JsxGetter
    public Node getNextSibling() {
        return nextSibling_;
    }
}
