/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
 * A javascript object for a Screen. Combines properties from both Mozilla's DOM
 * and IE's DOM.
 * 
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_screen.asp">
 * MSDN documentation</a>
 * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref.html">Mozilla documentation</a>
 */
public class Screen extends SimpleScriptable {

    private static final long serialVersionUID = 7775024295042666245L;

    private int left_;
    private int top_;
    private int width_;
    private int height_;
    private int colorDepth_;
    private int bufferDepth_;
    private int dpi_;
    private boolean fontSmoothingEnabled_;
    private int updateInterval_;

    /**
     * Create an instance. Javascript objects must have a default constructor.
     */
    public Screen() {
        left_ = 0;
        top_ = 0;
        width_ = 800;
        height_ = 600;
        colorDepth_ = 24;
        bufferDepth_ = 24;
        dpi_ = 96;
        fontSmoothingEnabled_ = true;
        updateInterval_ = 0;
    }

    /**
     * Return the <tt>availHeight</tt> property.
     * @return the <tt>availHeight</tt> property
     */
    public int jsGet_availHeight() {
        return height_;
    }

    /**
     * Return the <tt>availLeft</tt> property.
     * @return the <tt>availLeft</tt> property
     */
    public int jsGet_availLeft() {
        return left_;
    }

    /**
     * Return the <tt>availTop</tt> property.
     * @return the <tt>availTop</tt> property
     */
    public int jsGet_availTop() {
        return top_;
    }

    /**
     * Return the <tt>availWidth</tt> property.
     * @return the <tt>availWidth</tt> property
     */
    public int jsGet_availWidth() {
        return width_;
    }

    /**
     * Return the <tt>bufferDepth</tt> property.
     * @return the <tt>bufferDepth</tt> property
     */
    public int jsGet_bufferDepth() {
        return bufferDepth_;
    }

    /**
     * Sets the <tt>bufferDepth</tt> property.
     * @param bufferDepth the <tt>bufferDepth</tt> property
     */
    public void jsSet_bufferDepth(final int bufferDepth) {
        bufferDepth_ = bufferDepth;
    }

    /**
     * Return the <tt>colorDepth</tt> property.
     * @return the <tt>colorDepth</tt> property
     */
    public int jsGet_colorDepth() {
        return colorDepth_;
    }

    /**
     * Return the <tt>deviceXDPI</tt> property.
     * @return the <tt>deviceXDPI</tt> property
     */
    public int jsGet_deviceXDPI() {
        return dpi_;
    }

    /**
     * Return the <tt>deviceYDPI</tt> property.
     * @return the <tt>deviceYDPI</tt> property
     */
    public int jsGet_deviceYDPI() {
        return dpi_;
    }

    /**
     * Return the <tt>fontSmoothingEnabled</tt> property.
     * @return the <tt>fontSmoothingEnabled</tt> property
     */
    public boolean jsGet_fontSmoothingEnabled() {
        return fontSmoothingEnabled_;
    }

    /**
     * Return the <tt>height</tt> property.
     * @return the <tt>height</tt> property
     */
    public int jsGet_height() {
        return height_;
    }

    /**
     * Return the <tt>left</tt> property.
     * @return the <tt>left</tt> property
     */
    public int jsGet_left() {
        return left_;
    }

    /**
     * Sets the <tt>left</tt> property.
     * @param left the <tt>left</tt> property
     */
    public void jsSet_left(final int left) {
        left_ = left;
    }

    /**
     * Return the <tt>logicalXDPI</tt> property.
     * @return the <tt>logicalXDPI</tt> property
     */
    public int jsGet_logicalXDPI() {
        return dpi_;
    }

    /**
     * Return the <tt>logicalYDPI</tt> property.
     * @return the <tt>logicalYDPI</tt> property
     */
    public int jsGet_logicalYDPI() {
        return dpi_;
    }

    /**
     * Return the <tt>pixelDepth</tt> property.
     * @return the <tt>pixelDepth</tt> property
     */
    public int jsGet_pixelDepth() {
        return colorDepth_;
    }

    /**
     * Return the <tt>top</tt> property.
     * @return the <tt>top</tt> property
     */
    public int jsGet_top() {
        return top_;
    }

    /**
     * Sets the <tt>top</tt> property.
     * @param top the <tt>top</tt> property
     */
    public void jsSet_top(final int top) {
        top_ = top;
    }

    /**
     * Return the <tt>updateInterval</tt> property.
     * @return the <tt>updateInterval</tt> property
     */
    public int jsGet_updateInterval() {
        return updateInterval_;
    }

    /**
     * Sets the <tt>updateInterval</tt> property.
     * @param updateInterval the <tt>updateInterval</tt> property
     */
    public void jsSet_updateInterval(final int updateInterval) {
        updateInterval_ = updateInterval;
    }

    /**
     * Return the <tt>width</tt> property.
     * @return the <tt>width</tt> property
     */
    public int jsGet_width() {
        return width_;
    }
}
