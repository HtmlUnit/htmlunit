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
package com.gargoylesoftware.htmlunit.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Abstract superclass for all the HtmlUnit jelly tags.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public abstract class HtmlUnitTagSupport extends TagSupport {
    /** The var attribute which is the name of the variable to put the result into */
    private String var_;
    private String page_;


    /**
     * Create an instance
     */
    public HtmlUnitTagSupport() {
    }


    /**
     * Callback from Jelly to set the value of the page attribute.
     * @param page The new value.
     */
    public void setPage( final String page ) {
        page_ = page;
    }


    /**
     * Return the page specified in the script
     * @return The page.
     * @throws JellyTagException If a problem occurs.
     */
    protected final Page getPage() throws JellyTagException {
        if( page_ == null ) {
            throw new JellyTagException("page is a mandatory attribute");
        }

        final Object object = getContext().getVariable(page_);
        try {
            return (Page)object;
        }
        catch( final ClassCastException e ) {
            throw new JellyTagException("Expected page object in variable ["+page_+"] but found ["+object+"]");
        }
    }


    /**
     * Return the HtmlPage specified in the script.  If the page is not an instance of HtmlPage then
     * throw a JellyTagException
     * @return the HtmlPage
     * @throws JellyTagException If a problem occurs.
     */
    protected final HtmlPage getHtmlPage() throws JellyTagException {
        final Page page = getPage();
        if( page instanceof HtmlPage ) {
            return (HtmlPage)page;
        }
        else {
            throw new JellyTagException("Page isn't an instance of HtmlPage: "+page.getClass().getName());
        }
    }


    /**
     * Callback from Jelly to set the value of the var attribute.
     * @param var The new value.
     */
    public void setVar( final String var ) {
        var_ = var;
    }


    /**
     * Return the value of the "var" attribute or throw an exception if it hasn't been set.
     * @return The value of var
     * @throws MissingAttributeException If var hasn't been set.
     */
    public String getVarValueOrDie() throws MissingAttributeException {
        if( var_ == null ) {
            throw new MissingAttributeException("var");
        }
        return var_;
    }


    /**
     * Return the value of the "var" attribute or throw an exception if it hasn't been set.
     * @return The value of var
     * @throws MissingAttributeException if missing
     */
    public String getVarValueOrNull() throws MissingAttributeException {
        return var_;
    }


    /**
     * Return the web client that is currently in use.
     * @return the web client
     * @throws JellyTagException If a web client cannot be found.
     */
    public WebClient getWebClient() throws JellyTagException {
        final WebClientTag webClientTag = (WebClientTag) findAncestorWithClass(WebClientTag.class);
        if( webClientTag == null ) {
            throw new JellyTagException("Unable to determine webClient");
        }
        else {
            return webClientTag.getWebClient();
        }
    }
}
