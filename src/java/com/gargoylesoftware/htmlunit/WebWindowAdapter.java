/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 * An adapter for the WebWindowListener interface.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WebWindowAdapter implements WebWindowListener {

    /**
     * Create an instance
     */
    public WebWindowAdapter() {
    }


    /**
     * A web window has been opened
     *
     * @param event The event
     */
    public void webWindowOpened( final WebWindowEvent event ) {
    }


    /**
     * The contents of a web window has been changed
     *
     * @param event The event
     */
    public void webWindowContentChanged( final WebWindowEvent event ) {
    }
}

