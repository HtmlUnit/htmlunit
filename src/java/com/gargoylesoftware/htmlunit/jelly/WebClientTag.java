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

import java.lang.reflect.Field;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SimpleCredentialProvider;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Jelly tag "webClient".
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WebClientTag extends HtmlUnitTagSupport {
    private WebClient webClient_;
    private String userId_;
    private String password_;
    private String browserVersionName_;
    
    /**
     * Create an instance
     */
    public WebClientTag() {
    }


    /**
     * Process the tag
     *
     * @param xmlOutput to write output
     * @throws JellyTagException when any error occurs
     */
    public void doTag(XMLOutput xmlOutput) throws JellyTagException {
        final BrowserVersion browserVersion = getBrowserVersion();
        if( browserVersion == null ) {
            webClient_ = new WebClient();
        }
        else {
            webClient_ = new WebClient(browserVersion);
        }
        if( userId_ != null || password_ != null ) {
            if( userId_ == null || password_ == null ) {
                throw new JellyTagException("userid and password must either both be set or neither set");
            }
            webClient_.setCredentialProvider( new SimpleCredentialProvider(userId_, password_) );
        }
        
        final String varName = getVarValueOrNull();
        if( varName != null ) {
            getContext().setVariable(varName, webClient_);
        }
        invokeBody(xmlOutput);
    }


    /**
     * Callback from Jelly to set the value of the browserVersion attribute.
     * @param browserVersion The new value.
     */
    public void setBrowserVersion( final String browserVersion ) {
        browserVersionName_ = browserVersion;
    }


    private BrowserVersion getBrowserVersion() {
        if( browserVersionName_ == null ) {
            return null;
        }
        
        try {
            final Field field = BrowserVersion.class.getDeclaredField(browserVersionName_);
            return (BrowserVersion)field.get(null);
        }
        catch( final NoSuchFieldException e ) {
            return null;
        }
        catch( final IllegalAccessException e ) {
            // All the legitimate constants are accessible so this exception 
            // would mean that something else has been specified.
            return null;
        }
    }
    
    /**
     * Return the WebClient created by this tag.
     * @return The web client
     */
    public WebClient getWebClient() {
        return webClient_;
    }


    /**
     * Set the userid attribute
     * @param userid the new value
     */
    public void setUserid( final String userid ) {
        userId_ = userid;
    }


    /**
     * Set the password attribute
     * @param password the new value
     */
    public void setPassword( final String password ) {
        password_ = password;
    }
}
