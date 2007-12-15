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

import java.io.IOException;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * A basic class to be implemented by {@link HtmlPage} and {@link XmlPage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public abstract class SgmlPage extends DomNode implements Page {

    private final WebResponse webResponse_;
    private WebWindow enclosingWindow_;
    private final WebClient webClient_;


    /**
     * Create an instance of SgmlPage
     *
     * @param webResponse The web response that was used to create this page
     * @param webWindow The window that this page is being loaded into.
     */
    public SgmlPage(final WebResponse webResponse, final WebWindow webWindow) {
        super(null);
        webResponse_ = webResponse;
        enclosingWindow_ = webWindow;
        webClient_ = webWindow.getWebClient();
    }
    
    /**
     * {@inheritDoc}
     */
    public void cleanUp() throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() throws IOException {
    }

    /**
     * Get the name for the current node.
     * @return The node name
     */
    public String getNodeName() {
        return "#document";
    }

    /**
     * Get the type of the current node.
     * @return The node type
     */
    public short getNodeType() {
        return org.w3c.dom.Node.DOCUMENT_NODE;
    }

    /**
     * Return the window that this page is sitting inside.
     *
     * @return The enclosing frame or null if this page isn't inside a frame.
     */
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }

    /**
     * Set the window that contains this page.
     *
     * @param window The new frame or null if this page is being removed from a frame.
     */
    public void setEnclosingWindow(final WebWindow window) {
        enclosingWindow_ = window;
    }

    /**
     * Return the WebClient that originally loaded this page
     *
     * @return See above
     */
    public WebClient getWebClient() {
        return webClient_;
    }

}
