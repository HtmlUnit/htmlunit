/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 *  An abstract page that represents some content returned from a server
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface Page {
    /**
     *  Return the web response that was originally used to create this page.
     *
     * @return  The web response
     */
    WebResponse getWebResponse();


    /**
     * Return the window that this page is sitting inside.
     *
     * @return The enclosing frame or null if this page isn't inside a frame.
     */
    WebWindow getEnclosingWindow();
}

