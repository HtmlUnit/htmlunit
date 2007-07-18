/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

/**
 * A window representing a top level browser window.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author David D. Kilzer
 * @author Ahmed Ashour
 */
public class TopLevelWindow extends WebWindowImpl implements Serializable  {

    private static final long serialVersionUID = 2448888802967514906L;

    private WebWindow opener_;

    /**
     * Create an instance.
     * @param name The name of the new window
     * @param webClient The web client that "owns" this window.
     */
    public TopLevelWindow(final String name, final WebClient webClient) {
        super(webClient);
        Assert.notNull("name", name);
        setName(name);
    }

    /**
     * {@inheritDoc}
     * Since this is a top level window, return this window.
     */
    public WebWindow getParentWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     * Since this is a top level window, return this window.
     */
    public WebWindow getTopWindow() {
        return this;
    }

    /**
     * Return a string representation of this object
     * @return A string representation of this object
     */
    public String toString() {
        return "TopLevelWindow[name=\"" + getName() + "\"]";
    }

    /**
     * Set the opener property.  This is the WebWindow that caused this new window to be opened.
     * @param opener The new opener
     */
    public void setOpener(final WebWindow opener) {
        opener_ = opener;
    }

    /**
     * Return the opener property.  This is the WebWindow that caused this new window to be opened.
     * @return The opener
     */
    public WebWindow getOpener() {
        return opener_;
    }

    /**
     * Close this window.
     */
    public void close() {
        destroyChildren();
        getWebClient().deregisterWebWindow(this);
    }
}
