/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * Specifies a rectangle that contains a line of text in either an element or a TextRange object.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535906.aspx">MSDN Documentation</a>
 */
public class TextRectangle extends SimpleScriptable {

    private static final long serialVersionUID = 8045028432279353283L;

    private int bottom_;
    private int left_;
    private int right_;
    private int top_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public TextRectangle() {
        // Empty.
    }

    /**
     * Creates an instance, with the given coordinates.
     *
     * @param bottom the bottom coordinate of the rectangle surrounding the object content
     * @param left the left coordinate of the rectangle surrounding the object content
     * @param right the right coordinate of the rectangle surrounding the object content
     * @param top the top coordinate of the rectangle surrounding the object content
     */
    public TextRectangle(final int bottom, final int left, final int right, final int top) {
        this.bottom_ = bottom;
        this.left_ = left;
        this.right_ = right;
        this.top_ = top;
    }

    /**
     * Sets the bottom coordinate of the rectangle surrounding the object content.
     * @param bottom the bottom coordinate of the rectangle surrounding the object content
     */
    public void jsxSet_bottom(final int bottom) {
        this.bottom_ = bottom;
    }

    /**
     * Returns the bottom coordinate of the rectangle surrounding the object content.
     * @return the bottom coordinate of the rectangle surrounding the object content
     */
    public int jsxGet_bottom() {
        return bottom_;
    }

    /**
     * Sets the left coordinate of the rectangle surrounding the object content.
     * @param left the left coordinate of the rectangle surrounding the object content
     */
    public void jsxSet_left(final int left) {
        this.left_ = left;
    }

    /**
     * Returns the left coordinate of the rectangle surrounding the object content.
     * @return the left coordinate of the rectangle surrounding the object content
     */
    public int jsxGet_left() {
        return left_;
    }

    /**
     * Sets the right coordinate of the rectangle surrounding the object content.
     * @param right the right coordinate of the rectangle surrounding the object content
     */
    public void jsxSet_right(final int right) {
        this.right_ = right;
    }

    /**
     * Returns the right coordinate of the rectangle surrounding the object content.
     * @return the right coordinate of the rectangle surrounding the object content
     */
    public int jsxGet_right() {
        return right_;
    }

    /**
     * Sets the top coordinate of the rectangle surrounding the object content.
     * @param top the top coordinate of the rectangle surrounding the object content
     */
    public void jsxSet_top(final int top) {
        this.top_ = top;
    }

    /**
     * Returns the top coordinate of the rectangle surrounding the object content.
     * @return the top coordinate of the rectangle surrounding the object content
     */
    public int jsxGet_top() {
        return top_;
    }
}
