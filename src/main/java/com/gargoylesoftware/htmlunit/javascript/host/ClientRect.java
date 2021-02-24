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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * Specifies a rectangle that contains a line of text in either an element or a TextRange object.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535906.aspx">MSDN Documentation</a>
 */
@JsxClass(IE)
@JsxClass(className = "DOMRect", value = {CHROME, EDGE, FF, FF78})
public class ClientRect extends SimpleScriptable {

    private int bottom_;
    private int left_;
    private int right_;
    private int top_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public ClientRect() {
    }

    /**
     * Creates an instance, with the given coordinates.
     *
     * @param bottom the bottom coordinate of the rectangle surrounding the object content
     * @param left the left coordinate of the rectangle surrounding the object content
     * @param right the right coordinate of the rectangle surrounding the object content
     * @param top the top coordinate of the rectangle surrounding the object content
     */
    public ClientRect(final int bottom, final int left, final int right, final int top) {
        bottom_ = bottom;
        left_ = left;
        right_ = right;
        top_ = top;
    }

    /**
     * Sets the bottom coordinate of the rectangle surrounding the object content.
     * @param bottom the bottom coordinate of the rectangle surrounding the object content
     */
    @JsxSetter
    public void setBottom(final int bottom) {
        bottom_ = bottom;
    }

    /**
     * Returns the bottom coordinate of the rectangle surrounding the object content.
     * @return the bottom coordinate of the rectangle surrounding the object content
     */
    @JsxGetter
    public int getBottom() {
        return bottom_;
    }

    /**
     * Sets the left coordinate of the rectangle surrounding the object content.
     * @param left the left coordinate of the rectangle surrounding the object content
     */
    @JsxSetter
    public void setLeft(final int left) {
        left_ = left;
    }

    /**
     * Returns the left coordinate of the rectangle surrounding the object content.
     * @return the left coordinate of the rectangle surrounding the object content
     */
    @JsxGetter
    public int getLeft() {
        return left_;
    }

    /**
     * Sets the right coordinate of the rectangle surrounding the object content.
     * @param right the right coordinate of the rectangle surrounding the object content
     */
    @JsxSetter
    public void setRight(final int right) {
        right_ = right;
    }

    /**
     * Returns the right coordinate of the rectangle surrounding the object content.
     * @return the right coordinate of the rectangle surrounding the object content
     */
    @JsxGetter
    public int getRight() {
        return right_;
    }

    /**
     * Sets the top coordinate of the rectangle surrounding the object content.
     * @param top the top coordinate of the rectangle surrounding the object content
     */
    @JsxSetter
    public void setTop(final int top) {
        top_ = top;
    }

    /**
     * Returns the top coordinate of the rectangle surrounding the object content.
     * @return the top coordinate of the rectangle surrounding the object content
     */
    @JsxGetter
    public int getTop() {
        return top_;
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @JsxGetter
    public int getWidth() {
        return getRight() - getLeft();
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @JsxGetter
    public int getHeight() {
        return getBottom() - getTop();
    }
}
