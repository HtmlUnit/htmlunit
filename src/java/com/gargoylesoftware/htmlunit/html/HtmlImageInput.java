/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import java.io.IOException;
import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlImageInput extends HtmlInput {
    private boolean wasPositionSpecified_ = false;
    private int xPosition_;
    private int yPosition_;


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
     * @exception  IOException If an io error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page click()
        throws
            IOException,
            ElementNotFoundException {

        wasPositionSpecified_ = false;
        return super.click();
    }


    /**
     * Simulate clicking this input with a pointing device.  The x and y coordinates
     * of the pointing device will be sent to the server.
     *
     * @param x The x coordinate of the pointing device at the time of clicking
     * @param y The y coordinate of the pointing device at the time of clicking
     * @return The page that is loaded after the click has taken place.
     * @exception  IOException If an io error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page click( final int x, final int y )
        throws
            IOException,
            ElementNotFoundException {

        wasPositionSpecified_ = true;
        xPosition_ = x;
        yPosition_ = y;
        return super.click();
    }


    /**
     *  Return an array of KeyValuePairs that are the values that will be sent
     *  back to the server whenever the current form is submitted.<p>
     *
     *  THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     *  BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.
     *
     * @return  See above
     */
    public KeyValuePair[] getSubmitKeyValuePairs() {
        final String name = getNameAttribute();
        if( wasPositionSpecified_ == true ) {
            return new KeyValuePair[]{
                new KeyValuePair( name, getValueAttribute() ),
                new KeyValuePair( name+".x", String.valueOf(xPosition_) ),
                new KeyValuePair( name+".y", String.valueOf(yPosition_) )
            };
        }
        else {
            return new KeyValuePair[]{new KeyValuePair( name, getValueAttribute() )};
        }
    }


    /**
     * Reset the value of this element to its initial state.  This is a no-op for
     * this component.
     */
    public void reset() {
    }
}

