/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The default implementation of PageCreator.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
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
            newPage = new HtmlPage( webClient, webResponse.getUrl(), webResponse, webWindow );
        }
        else if( contentType.equals("text/javascript") || contentType.equals("application/x-javascript") ) {
            newPage = new JavaScriptPage( webResponse, webWindow );
        }
        else if( contentType.startsWith( "text/" ) ) {
            newPage = new TextPage( webResponse, webWindow );
        }
        else {
            newPage = new UnexpectedPage( webResponse, webWindow );
        }
        return newPage;
    }
}

