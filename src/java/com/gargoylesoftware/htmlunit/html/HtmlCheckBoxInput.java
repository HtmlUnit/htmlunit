/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlCheckBoxInput extends HtmlInput {

    private final boolean initialCheckedState_;

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlCheckBoxInput( final HtmlPage page, final Element element ) {
        super( page, element );
        initialCheckedState_ = isAttributeDefined("checked");
    }


    /**
     *  Set the "checked" attribute
     *
     * @param  isChecked true if this element is to be selected
     */
    public void setChecked( final boolean isChecked ) {
        if( isChecked ) {
            getElement().setAttribute( "checked", "checked" );
        }
        else {
            getElement().removeAttribute( "checked" );
        }
    }


    /**
     *  Return true if this element is currently selected
     *
     * @return  See above
     */
    public boolean isChecked() {
        return getAttributeValue("checked").length() != 0;
    }


    /**
     * Return the value of this element to what it was at the time the page was loaded.
     */
    public void reset() {
        setChecked(initialCheckedState_);
    }
}

