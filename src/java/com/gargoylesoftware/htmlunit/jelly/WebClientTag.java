/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.jelly;

import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;

/**
 * Jelly tag "webClient".
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WebClientTag extends HtmlUnitTagSupport {
    private WebClient webClient_;

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
        webClient_ = new WebClient();
        invokeBody(xmlOutput);
    }


    /**
     * Callback from Jelly to set the value of the browserVersion attribute.
     * @param browserVersion The new value.
     */
    public void setBrowserVersion( final String browserVersion ) {
        System.out.println("** BrowserVersion="+browserVersion);
    }


    /**
     * Return the WebClient created by this tag.
     * @return The web client
     */
    public WebClient getWebClient() {
        return webClient_;
    }
}
