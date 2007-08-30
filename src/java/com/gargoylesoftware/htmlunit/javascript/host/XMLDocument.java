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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for XMLDocument.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XMLDocument extends Document {

    private boolean async_ = true;
    
    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public XMLDocument() {
    }

    /**
     * Sets the <tt>async</tt> attribute.
     * @param async Whether or not to send the request to the server asynchronously.
     */
    public void jsxSet_async(final boolean async) {
        this.async_ = async;
    }

    /**
     * Returns Whether or not to send the request to the server asynchronously.
     * @return the <tt>async</tt> attribute.
     */
    public boolean jsxGet_async() {
        return async_;
    }
    

    /**
     * Loads an XML document from the specified location.
     *
     * @param xmlSrouce A string containing a URL that specifies the location of the XML file.
     * @return true if the load succeeded; false if the load failed.
     */
    public boolean jsxFunction_load(final String xmlSrouce) {
        if (async_) {
            getLog().debug("XMLDocument.load(): 'async' is true, currently treated as false.");
        }
        try {
            final WebRequestSettings settings = new WebRequestSettings(new URL(xmlSrouce));
            final WebResponse webResponse = getWindow().getWebWindow().getWebClient().loadWebResponse(settings);
            final XmlPage page = new XmlPage(webResponse, null);
            setDomNode(page);
            return true;
        }
        catch (final IOException e) {
            getLog().debug("Error parsing XML from '" + xmlSrouce + "'", e);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected Object getWithPreemption(final String name) {
        return NOT_FOUND;
    }

    /**
     * {@inheritDoc}
     */
    public SimpleScriptable makeScriptableFor(final DomNode domNode) {
        final XMLElement element = new XMLElement();
        element.setPrototype(getPrototype(element.getClass()));
        element.setParentScope(getParentScope());
        element.setDomNode(domNode);
        return element;
    }
}
