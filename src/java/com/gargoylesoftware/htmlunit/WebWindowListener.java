/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 * A listener for WebWindowEvent's
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface WebWindowListener {
    /**
     * A web window has been opened
     *
     * @param event The event
     */
    void webWindowOpened( final WebWindowEvent event );

    /**
     * The contents of a web window has been changed
     *
     * @param event The event
     */
    void webWindowContentChanged( final WebWindowEvent event );
}

