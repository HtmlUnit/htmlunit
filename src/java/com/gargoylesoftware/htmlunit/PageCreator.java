/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.io.IOException;

/**
 * Something that knows how to create a page object.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface PageCreator {
    /**
     * Create a Page object for the specified web response.
     *
     * @param webClient The web client that loaded the page
     * @param webResponse The response from the server
     * @param webWindow The window that this page will be loaded into.
     * @exception IOException If an io problem occurs
     * @return The new page.
     */
    Page createPage(
        WebClient webClient, WebResponse webResponse, WebWindow webWindow )
        throws IOException ;
}

