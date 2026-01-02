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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.html.impl.SimpleRange;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * The JavaScript object that represents a AbstractRange.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/AbstractRange">AbstractRange</a>
 * @author Ronald Brill
 */
@JsxClass
public class AbstractRange extends HtmlUnitScriptable {

    private Node startContainer_;
    private Node endContainer_;
    private int startOffset_;
    private int endOffset_;

    /**
     * Creates an instance.
     */
    public AbstractRange() {
        super();
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Creates a new instance.
     *
     * @param startContainer the start node
     * @param endContainer the end node
     * @param startOffset the start offset
     * @param endOffset the end offset
     */
    protected AbstractRange(final Node startContainer, final Node endContainer,
            final int startOffset, final int endOffset) {
        super();
        startContainer_ = startContainer;
        endContainer_ = endContainer;

        startOffset_ = startOffset;
        endOffset_ = endOffset;
    }

    /**
     * @return the start container
     */
    protected Node internGetStartContainer() {
        return startContainer_;
    }

    /**
     * Sets the start container.
     *
     * @param startContainer the new start container
     */
    protected void internSetStartContainer(final Node startContainer) {
        startContainer_ = startContainer;
    }

    /**
     * @return the end container
     */
    protected Node internGetEndContainer() {
        return endContainer_;
    }

    /**
     * Sets the end container.
     *
     * @param endContainer the new end container
     */
    protected void internSetEndContainer(final Node endContainer) {
        endContainer_ = endContainer;
    }

    /**
     * @return the start offset
     */
    protected int internGetStartOffset() {
        return startOffset_;
    }

    /**
     * Sets the start offset.
     *
     * @param startOffset the new start offset
     */
    protected void internSetStartOffset(final int startOffset) {
        startOffset_ = startOffset;
    }

    /**
     * @return the end offset
     */
    protected int internGetEndOffset() {
        return endOffset_;
    }

    /**
     * Sets the end offset.
     *
     * @param endOffset the new end offset
     */
    protected void internSetEndOffset(final int endOffset) {
        endOffset_ = endOffset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (getPrototype() == null
                || startContainer_ == null
                || endContainer_ == null) {
            return super.getDefaultValue(hint);
        }
        return getSimpleRange().toString();
    }

    /**
     * Gets the node within which the Range begins.
     * @return <code>undefined</code> if not initialized
     */
    @JsxGetter
    public Object getStartContainer() {
        if (startContainer_ == null) {
            return JavaScriptEngine.UNDEFINED;
        }
        return startContainer_;
    }

    /**
     * Gets the node within which the Range ends.
     * @return <code>undefined</code> if not initialized
     */
    @JsxGetter
    public Object getEndContainer() {
        if (endContainer_ == null) {
            return JavaScriptEngine.UNDEFINED;
        }
        return endContainer_;
    }

    /**
     * Gets the offset within the starting node of the Range.
     * @return <code>0</code> if not initialized
     */
    @JsxGetter
    public int getStartOffset() {
        return startOffset_;
    }

    /**
     * Gets the offset within the end node of the Range.
     * @return <code>0</code> if not initialized
     */
    @JsxGetter
    public int getEndOffset() {
        return endOffset_;
    }

    /**
     * Indicates if the range is collapsed.
     * @return {@code true} if the range is collapsed
     */
    @JsxGetter
    public boolean isCollapsed() {
        return startContainer_ == endContainer_ && startOffset_ == endOffset_;
    }

    /**
     * @return a {@link SimpleRange} version of this object
     */
    public SimpleRange getSimpleRange() {
        return new SimpleRange(startContainer_.getDomNodeOrNull(), startOffset_,
            endContainer_.getDomNodeOrDie(), endOffset_);
    }

    @Override
    protected Object equivalentValues(final Object value) {
        if (!(value instanceof AbstractRange other)) {
            return false;
        }
        return startContainer_ == other.startContainer_
                && endContainer_ == other.endContainer_
                && startOffset_ == other.startOffset_
                && endOffset_ == other.endOffset_;
    }
}
