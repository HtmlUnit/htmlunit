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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowImpl;

/**
 * The web window for a frame or iframe.
 * @version  $Revision$
 * @author Brad Clarke
 */
public class FrameWindow extends WebWindowImpl {
    private final BaseFrame frame_;
    /**
     * Create an instance for a given frame
     */
    FrameWindow(final BaseFrame frame) {
        super(frame.getPage().getWebClient());
        frame_ = frame;
    }

    /**
     * {@inheritDoc}
     * A FrameWindow shares it's name with it's containing frame.
     */
    public String getName() {
        return frame_.getNameAttribute();
    }

    /**
     * {@inheritDoc}
     * A FrameWindow shares it's name with it's containing frame.
     */
    public void setName(final String name) {
        frame_.setNameAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getParentWindow() {
        return frame_.getPage().getEnclosingWindow();
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getTopWindow() {
        return getParentWindow().getTopWindow();
    }

    /**
     * Return the html page in wich the &lt;frame&gt; or &lt;iframe&gt; tag is contained
     * for this frame window.
     * This is a facility method for <code>(HtmlPage) (getParentWindow().getEnclosedPage())</code>.
     * @return the page in the parent window.
     */
    public HtmlPage getEnclosingPage() {
        return frame_.getPage();
    }

    /**
     * Gives a basic representation for debugging purposes
     * @return a basic representation
     */
    public String toString() {
        return "FrameWindow[name=\"" + getName() + "\"]";
    }
}