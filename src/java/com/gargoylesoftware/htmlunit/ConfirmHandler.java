/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;


/**
 * A handler for javascript window.confirm().  Confirms are triggered when the javascript
 * method Window.confirm() is called.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface ConfirmHandler {
    /**
     * Handle an alert for the given page.
     * @param page The page on which the alert occurred.
     * @param message The message in the alert.
     * @return true if we are simulating the ok button.  False for the cancel button.
     */
    boolean handleConfirm( final Page page, final String message );
}
