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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import org.xml.sax.SAXException;

/**
 * The default implementation of PageCreator.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 */
public class DefaultPageCreator implements PageCreator {

    /**
     * Create an instance.
     */
    public DefaultPageCreator() {
    }


    /**
     * Create a Page object for the specified web response.
     *
     * @param webClient The web client that loaded the page
     * @param webResponse The response from the server
     * @param webWindow The window that this page will be loaded into.
     * @exception IOException If an io problem occurs
     * @return The new page object
     */
    public Page createPage(
            final WebClient webClient,
            final WebResponse webResponse,
            final WebWindow webWindow )
        throws
            IOException {

        final Page newPage;
        final String contentType = webResponse.getContentType();
        if( contentType.equals( "text/html" ) || contentType.equals( "text/xhtml" ) ) {
            try {
                HTMLParser parser = new HTMLParser();
                newPage = parser.parse(webClient, webResponse, webWindow);
            }
            catch (SAXException e) {
                throw new ObjectInstantiationException("Unable to parse html input", e);
            }
        }
        //else if( contentType.equals("application/xhtml+xml") ) {
        // Create validated xhtml document
        //}
        else if( contentType.equals("text/javascript") || contentType.equals("application/x-javascript") ) {
            newPage = new JavaScriptPage( webResponse, webWindow );
            webWindow.setEnclosedPage(newPage);
        }
        else if( contentType.startsWith( "text/" ) ) {
            newPage = new TextPage( webResponse, webWindow );
            webWindow.setEnclosedPage(newPage);
        }
        else {
            newPage = new UnexpectedPage( webResponse, webWindow );
            webWindow.setEnclosedPage(newPage);
        }
        return newPage;
    }
}

