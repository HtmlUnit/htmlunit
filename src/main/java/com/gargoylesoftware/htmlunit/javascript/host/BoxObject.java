/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
    public HTMLElement jsxGet_element() {
        return element_;
    }

    /**
     * Returns this box object's element's first child.
     * @return this box object's element's first child
     */
    @JsxGetter
    public Object jsxGet_firstChild() {
        return element_.jsxGet_firstChild();
    }

    /**
     * Returns this box object's element's last child.
     * @return this box object's element's last child
     */
    @JsxGetter
    public Object jsxGet_lastChild() {
        return element_.jsxGet_lastChild();
    }

    /**
     * Returns this box object's element's next sibling.
     * @return this box object's element's next sibling
     */
    @JsxGetter
    public Object jsxGet_nextSibling() {
        return element_.jsxGet_nextSibling();
    }

    /**
     * Returns this box object's element's previous sibling.
     * @return this box object's element's previous sibling
     */
    @JsxGetter
    public Object jsxGet_previousSibling() {
        return element_.jsxGet_previousSibling();
    }

    /**
     * Returns the X position of this box object's element.
     * @return the X position of this box object's element
     */
    @JsxGetter
    public int jsxGet_x() {
        return element_.getPosX();
    }

    /**
     * Returns the Y position of this box object's element.
     * @return the Y position of this box object's element
     */
    @JsxGetter
    public int jsxGet_y() {
        return element_.getPosY();
    }

    /**
     * Returns the <tt>screenX</tt> property. Testing in FF2 suggests that this value is always
     * the same as the value returned by the <tt>x</tt> property.
     *
     * @return the <tt>screenX</tt> property
     */
    @JsxGetter
    public int jsxGet_screenX() {
        return jsxGet_x();
    }

    /**
     * Returns the <tt>screenY</tt> property. Testing in FF2 suggests that this value is always
     * equal to the value returned by the <tt>y</tt> property plus <tt>121</tt> (probably for the
     * title bar, menu bar and toolbar).
     *
     * @return the <tt>screenY</tt> property
     */
    @JsxGetter
    public int jsxGet_screenY() {
        return jsxGet_y() + 121;
    }

    /**
     * Returns the width of this box object's element, including padding, excluding margin and border.
     * @return the width of this box object's element, including padding, excluding margin and border
     */
    @JsxGetter
    public int jsxGet_width() {
        return element_.jsxGet_clientWidth();
    }

    /**
     * Returns the height of this box object's element, including padding, excluding margin and border.
     * @return the height of this box object's element, including padding, excluding margin and border
     */
    @JsxGetter
    public int jsxGet_height() {
        return element_.jsxGet_clientHeight();
    }

}
