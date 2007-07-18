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
package com.gargoylesoftware.htmlunit.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * A page that will be returned for response with content type "text/xml".
 * It doesn't implement itself {@link org.w3c.dom.Document} to allow to see the source of badly formed
 * xml responses.
 * @version $Revision$
 * @author Marc Guillemot
 * @author David K. Taylor
 */
public class XmlPage implements Page {
    private final String content_;
    private Document document_;

    private WebWindow enclosingWindow_;
    private final WebResponse webResponse_;

    /**
     * Create an instance. 
     * A warning is logged if an exception is thrown while parsing the xml content 
     * (for instance when the content is not a valid xml and can't be parsed).
     *
     * @param  webResponse The response from the server
     * @param  enclosingWindow The window that holds the page.
     * @throws IOException If the page could not be created
     */
    public XmlPage( final WebResponse webResponse, final WebWindow enclosingWindow ) throws IOException {
        webResponse_ = webResponse;
        content_ = webResponse.getContentAsString();
        enclosingWindow_ = enclosingWindow;

        try {
            document_ = XmlUtil.buildDocument(webResponse);
        }
        catch (final SAXException e) {
            getLog().warn("Failed parsing xml document " + webResponse.getUrl() + ": " + e.getMessage());
        }
        catch (final ParserConfigurationException e) {
            getLog().warn("Failed parsing xml document " + webResponse.getUrl() + ": " + e.getMessage());
        }
    }

    /**
     * Clean up this page.
     */
    public void cleanUp() {
    }

    /**
     *  Return the content of the page
     *
     * @return  See above
     */
    public String getContent() {
        return content_;
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
     * Return the log object for this web client
     * @return The log object
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     *  Return the web response that was originally used to create this page.
     *
     * @return  The web response
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * Gets the DOM representation of the xml content 
     * @return <code>null</code> if the content couldn't be parsed.
     */
    public Document getXmlDocument() {
        return document_;
    }

    /**
     * Initialize this page.
     */
    public void initialize() {
    }
}

