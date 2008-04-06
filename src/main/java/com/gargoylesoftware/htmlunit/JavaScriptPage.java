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
package com.gargoylesoftware.htmlunit;

/**
 * A generic page that will be returned for JavaScript content.
 * Specifically any content types of "text/javascript"
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 */
public class JavaScriptPage implements Page {
    private final WebResponse webResponse_;
    private final String content_;

    private WebWindow enclosingWindow_;

    /**
     * Creates an instance.
     *
     * @param webResponse the response from the server
     * @param enclosingWindow the window that holds the page
     */
    public JavaScriptPage(final WebResponse webResponse, final WebWindow enclosingWindow) {
        webResponse_ = webResponse;
        content_ = webResponse.getContentAsString();
        enclosingWindow_ = enclosingWindow;
    }

    /**
     * Initializes this page.
     */
    public void initialize() {
    }

    /**
     * Cleans up this page.
     */
    public void cleanUp() {
    }

    /**
     * Returns the content of the page.
     *
     * @return the content of the page
     */
    public String getContent() {
        return content_;
    }

    /**
     * Returns the web response that was originally used to create this page.
     *
     * @return the web response that was originally used to create this page
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * Returns the window that this page is sitting inside.
     *
     * @return the enclosing frame or null if this page isn't inside a frame
     */
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }
}

