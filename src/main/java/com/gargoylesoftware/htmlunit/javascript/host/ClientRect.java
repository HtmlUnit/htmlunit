/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * Specifies a rectangle that contains a line of text in either an element or a TextRange object.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535906.aspx">MSDN Documentation</a>
 */
@JsxClass
public class ClientRect extends SimpleScriptable {

    private int bottom_;
    private int left_;
    private int right_;
    private int top_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public ClientRect() {
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
     * Returns the "width" property.
     * @return the "width" property
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getWidth() {
        return getRight() - getLeft();
    }

    /**
     * Returns the "height" property.
     * @return the "height" property
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getHeight() {
        return getBottom() - getTop();
    }
}
