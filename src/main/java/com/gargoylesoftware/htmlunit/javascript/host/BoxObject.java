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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a BoxObject.
 *
 * @version $Revision$
 * @author <a href="mailto:sam@redspr.com">Sam Hough</a>
 */
@JsxClass
public class BoxObject extends SimpleScriptable {

    /** The element to which this box object corresponds. */
    private final HTMLElement element_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public BoxObject() {
        this(null);
    }

    /**
     * Creates a new instance.
     * @param element the element to which this box object corresponds
     */
    public BoxObject(final HTMLElement element) {
        element_ = element;
    }

    /**
     * Returns the element to which this box object corresponds.
     * @return the element to which this box object corresponds
     */
    @JsxGetter
    public HTMLElement getElement() {
        return element_;
    }

    /**
     * Returns this box object's element's first child.
     * @return this box object's element's first child
     */
    @JsxGetter
    public Object getFirstChild() {
        return element_.getFirstChild();
    }

    /**
     * Returns this box object's element's last child.
     * @return this box object's element's last child
     */
    @JsxGetter
    public Object getLastChild() {
        return element_.getLastChild();
    }

    /**
     * Returns this box object's element's next sibling.
     * @return this box object's element's next sibling
     */
    @JsxGetter
    public Object getNextSibling() {
        return element_.getNextSibling();
    }

    /**
     * Returns this box object's element's previous sibling.
     * @return this box object's element's previous sibling
     */
    @JsxGetter
    public Object getPreviousSibling() {
        return element_.getPreviousSibling();
    }

    /**
     * Returns the X position of this box object's element.
     * @return the X position of this box object's element
     */
    @JsxGetter
    public int getX() {
        return element_.getPosX();
    }

    /**
     * Returns the Y position of this box object's element.
     * @return the Y position of this box object's element
     */
    @JsxGetter
    public int getY() {
        return element_.getPosY();
    }

    /**
     * Returns the <tt>screenX</tt> property. Testing in FF2 suggests that this value is always
     * the same as the value returned by the <tt>x</tt> property.
     *
     * @return the <tt>screenX</tt> property
     */
    @JsxGetter
    public int getScreenX() {
        return getX();
    }

    /**
     * Returns the <tt>screenY</tt> property. Testing in FF2 suggests that this value is always
     * equal to the value returned by the <tt>y</tt> property plus <tt>121</tt> (probably for the
     * title bar, menu bar and toolbar).
     *
     * @return the <tt>screenY</tt> property
     */
    @JsxGetter
    public int getScreenY() {
        return getY() + 121;
    }

    /**
     * Returns the width of this box object's element, including padding, excluding margin and border.
     * @return the width of this box object's element, including padding, excluding margin and border
     */
    @JsxGetter
    public int getWidth() {
        return element_.getClientWidth();
    }

    /**
     * Returns the height of this box object's element, including padding, excluding margin and border.
     * @return the height of this box object's element, including padding, excluding margin and border
     */
    @JsxGetter
    public int getHeight() {
        return element_.getClientHeight();
    }

}
