/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a BoxObject.
 *
 * @version $Revision$
 * @author <a href="mailto:sam@redspr.com">Sam Hough</a>
 */
public class BoxObject extends SimpleScriptable {

    private static final long serialVersionUID = -6650009325965623469L;

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
    public HTMLElement jsxGet_element() {
        return element_;
    }

    /**
     * Returns this box object's element's first child.
     * @return this box object's element's first child
     */
    public Object jsxGet_firstChild() {
        return element_.jsxGet_firstChild();
    }

    /**
     * Returns this box object's element's last child.
     * @return this box object's element's last child
     */
    public Object jsxGet_lastChild() {
        return element_.jsxGet_lastChild();
    }

    /**
     * Returns this box object's element's next sibling.
     * @return this box object's element's next sibling
     */
    public Object jsxGet_nextSibling() {
        return element_.jsxGet_nextSibling();
    }

    /**
     * Returns this box object's element's previous sibling.
     * @return this box object's element's previous sibling
     */
    public Object jsxGet_previousSibling() {
        return element_.jsxGet_previousSibling();
    }

    /**
     * Returns the X position of this box object's element.
     * @return the X position of this box object's element
     */
    public int jsxGet_x() {
        return element_.getPosX();
    }

    /**
     * Returns the Y position of this box object's element.
     * @return the Y position of this box object's element
     */
    public int jsxGet_y() {
        return element_.getPosY();
    }

    /**
     * Returns the <tt>screenX</tt> property. Testing in FF2 suggests that this value is always
     * the same as the value returned by the <tt>x</tt> property.
     *
     * @return the <tt>screenX</tt> property
     */
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
    public int jsxGet_screenY() {
        return jsxGet_y() + 121;
    }

    /**
     * Returns the width of this box object's element, including padding, excluding margin and border.
     * @return the width of this box object's element, including padding, excluding margin and border
     */
    public int jsxGet_width() {
        return element_.jsxGet_clientWidth();
    }

    /**
     * Returns the height of this box object's element, including padding, excluding margin and border.
     * @return the height of this box object's element, including padding, excluding margin and border
     */
    public int jsxGet_height() {
        return element_.jsxGet_clientHeight();
    }

}
