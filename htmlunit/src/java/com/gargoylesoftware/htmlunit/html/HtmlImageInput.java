/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;
import com.gargoylesoftware.htmlunit.Page;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlImageInput extends HtmlInput {

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlImageInput( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     * Simulate clicking this input in some way other than with a pointing device.
     * No x,y coordinates will be sent to the server.
     *
     * @return The page that is loaded after the click has taken place.
     */
    public Page click() {
        notImplemented();
        return null;
    }


    /**
     * Simulate clicking this input with a pointing device.  The x and y coordinates
     * of the pointing device will be sent to the server.
     *
     * @param x The x coordinate of the pointing device at the time of clicking
     * @param y The y coordinate of the pointing device at the time of clicking
     * @return The page that is loaded after the click has taken place.
     */
    public Page click( final int x, final int y ) {
        notImplemented();
        return null;
    }
}

