/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript;

import java.util.List;

import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * An array returned by frames property of Window
 *
 * @author <a href="mailto:chen_jun@users.sourceforge.net>Chen Jun</a>
 * @version $Revision$
 */
public class WindowFramesArray extends SimpleScriptable {
    private static final long serialVersionUID = 3108681888070448618L;
	private HtmlPage htmlPage_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public WindowFramesArray() {
    }

    /**
     * <p>Return the object at the specified index.</p>
     *
     * <p>TODO: This implementation is particularly inefficient but without a way
     * to detect if an element has been inserted or removed, it isn't safe to
     * cache the array/<p>
     *
     * @param index The index
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get(final int index, final Scriptable start) {
        final HtmlPage htmlPage = ((WindowFramesArray) start).htmlPage_;
        if (htmlPage == null) {
            return super.get(index, start);
        }
        final List frames = htmlPage.getFrames();
        if( frames == null ) {
            return NOT_FOUND;
        }
        final int frameCount = frames.size();
        if (index < 0 || index >= frameCount) {
            return NOT_FOUND;
        }
        return ((WebWindow) frames.get(index)).getScriptObject();
    }

    /**
     * Return the frame at the specified name or NOT_FOUND.
     *
     * @param name The name.
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get(final String name, final Scriptable start) {
        final HtmlPage htmlPage = ((WindowFramesArray) start).htmlPage_;
        if (htmlPage == null) {
            return super.get(name, start);
        }
        final List frames = htmlPage.getFrames();
        if (frames == null) {
            return NOT_FOUND;
        }
        final int frameCount = frames.size();

        for (int i = 0; i < frameCount; i++) {
            if (((WebWindow) frames.get(i)).getName().equals(name)) {
                return ((WebWindow) frames.get(i)).getScriptObject();
            }
        }
        return NOT_FOUND;
    }

    /**
     * Initialize this object
     * @param page The HtmlPage that this object will retrive elements from.
     */
    public void initialize(final HtmlPage page) {
        Assert.notNull("page", page);
        htmlPage_ = page;
    }

    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }

    /**
     * <p>Return the number of elements in this array</p>
     *
     * @return The number of elements in the array
     */
    public int jsGet_length() {
        Assert.notNull("htmlpage", htmlPage_);
        return htmlPage_.getFrames().size();
    }
}
