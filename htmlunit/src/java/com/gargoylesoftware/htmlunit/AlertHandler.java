/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;


/**
 * A handler for javascript alerts.  Alerts are triggered when the javascript method Window.alert()
 * is called.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface AlertHandler {
    /**
     * Handle an alert for the given page.
     * @param page The page on which the alert occurred.
     * @param message The message in the alert.
     */
    void handleAlert( final Page page, final String message );
}
