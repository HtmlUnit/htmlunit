/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple alert handler that keeps track of alerts in a list.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class CollectingAlertHandler implements AlertHandler {
    private final List collectedAlerts_;

    /**
     * Create an instance with an ArrayList
     */
    public CollectingAlertHandler() {
        this( new ArrayList() );
    }


    /**
     * Create an instance with the specified list.
     *
     * @param list The list to store alerts in.
     */
    public CollectingAlertHandler( final List list ) {
        Assert.notNull("list",list);
        collectedAlerts_ = list;
    }


    /**
     * Handle the alert.  This implementation will store the message in a list
     * for retrieval later.
     *
     * @param page The page that triggered the alert
     * @param message The message in the alert.
     */
    public void handleAlert( final Page page, final String message ) {
        collectedAlerts_.add(message);
    }


    /**
     * Return a list containing the message portion of any collected alerts.
     * @return a list of alert messages
     */
    public List getCollectedAlerts() {
        return collectedAlerts_;
    }
}
