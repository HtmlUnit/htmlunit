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

import java.net.MalformedURLException;

import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The javascript object that represents an "Image"
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 */
public class HTMLImageElement extends HTMLElement {

    private static final long serialVersionUID = 5630843390548382869L;
    private String src_;

    /**
     * Create an instance.
     */
    public HTMLImageElement() {
    }

    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }
    
    /**
     * Set the src property, either on the DOM node which corresponds to this
     * JavaScript object, or if none exists (as when using JavaScript to preload
     * images), on the JavaScript object itself.
     * @param src the src attribute value
     */
    public void jsxSet_src(final String src) {
        src_ = src;
        final HtmlImage htmlImageElement = (HtmlImage) getHtmlElementOrNull();
        if (htmlImageElement != null) {
            htmlImageElement.setAttributeValue("src", src);
        }
    }
    
    /**
     * Return the value of the src property, either from the DOM node which
     * corresponds to this JavaScript object, or if that doesn't exist (as
     * when using JavaScript to preload images), from the JavaScript object
     * itself.
     * @return the src attribute
     */
    public String jsxGet_src() {
        final HtmlImage htmlImageElement = (HtmlImage) getHtmlElementOrNull();
        if (htmlImageElement != null) {
            final String srcValue = htmlImageElement.getSrcAttribute();
            try {
                return htmlImageElement.getPage().getFullyQualifiedUrl(srcValue).toExternalForm();
            }
            catch (final MalformedURLException e) {
                throw Context.reportRuntimeError("Unable to create fully qualified URL for src attribute of image: "
                                                  + e.getMessage());
            }
        }
        else {
            // this is an image instantiated in js with "new Image()" and not yet added to the DOM tree.
            final WebClient webClient = getWindow().getWebWindow().getWebClient();
            final HtmlPage currentPage = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
            try {
                return currentPage.getFullyQualifiedUrl(src_).toExternalForm();
            }
            catch (final MalformedURLException e) {
                throw Context.reportRuntimeError("Unable to create fully qualified URL for src attribute of image: "
                                                 + e.getMessage());
            }
        }
    }
}
