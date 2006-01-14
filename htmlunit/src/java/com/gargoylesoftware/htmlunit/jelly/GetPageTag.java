/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.jelly;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;

/**
 * Jelly tag to load a page from a server.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.com">Christian Sell</a>
 */
public class GetPageTag extends HtmlUnitTagSupport {
    private String url_ = null;
    private List parameters_ = null;
    private String method_ = "get";
    private String webClientName_;

    /**
     * Create an instance
     */
    public GetPageTag() {
    }


    /**
     * Process the tag
     * @param xmlOutput The xml output
     * @throws JellyTagException If a problem occurs
     */
    public void doTag(final XMLOutput xmlOutput) throws JellyTagException {
        invokeBody(xmlOutput);

        final JellyContext jellyContext = getContext();

        final WebClient webClient;
        if( webClientName_ != null ) {
            webClient = (WebClient)jellyContext.getVariable(webClientName_);
            if( webClient == null ) {
                throw new JellyTagException("No webclient found for name ["+webClientName_+"]");
            }
        }
        else {
            webClient = getWebClient();
        }

        final Page page;
        try {
            final WebRequestSettings settings = new WebRequestSettings(getUrl(), getSubmitMethod());
            settings.setRequestParameters(getParameters());
            page = webClient.getPage(settings);
            jellyContext.setVariable( getVarValueOrDie(), page );
        }
        catch( final IOException e ) {
            throw new JellyTagException(e);
        }
    }


    /**
     * Callback from Jelly to set the value of the url attribute.
     * @param url The new value.
     */
    public void setUri( final String url ) {
        url_ = url;
    }


    /**
     * Return the value of the url attribute or throw an exception if it hasn't been set.
     * @return The URL
     * @throws JellyTagException If the attribute hasn't been set or the url is malformed.
     */
    public URL getUrl() throws JellyTagException {
        if( url_ == null ) {
            throw new JellyTagException("url attribute is mandatory");
        }

        try {
            return new URL(url_);
        }
        catch( final MalformedURLException e ) {
            throw new JellyTagException("url attribute is malformed: "+url_);
        }
    }


    /**
     * Add a parameter to the request.
     * @param parameter the new parameter
     */
    public synchronized void addParameter( final String parameter ) {
        if( parameters_ == null ) {
            parameters_ = new ArrayList();
        }
        parameters_.add(parameter);
    }


    /**
     * Return the list of parameters.
     * @return The list of parameters
     */
    public synchronized List getParameters() {
        if( parameters_ == null ) {
            return Collections.EMPTY_LIST;
        }
        else {
            return parameters_;
        }
    }


    /**
     * Callback from Jelly to set the value of the method attribute.
     * @param method The new value.
     */
    public void setMethod( final String method ) {
        method_ = method;
    }


    /**
     * Callback from Jelly to set the value of the method attribute.
     * @param webClientName The new value.
     */
    public void setWebclient( final String webClientName ) {
        webClientName_ = webClientName;
    }


    /**
     * Return the submit method to be used when retrieving the page.
     * @return The submit method
     * @throws JellyTagException If the submit method could not be determined.
     */
    public SubmitMethod getSubmitMethod() throws JellyTagException {
        try {
            return SubmitMethod.getInstance(method_);
        }
        catch( final IllegalArgumentException e ) {
            // Provide a nicer error message
            throw new JellyTagException("Value of method attribute is not a valid submit method: "+method_);
        }
    }
}
