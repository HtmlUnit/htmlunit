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

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlCheckBoxInput( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Set the "checked" attribute
     *
     * @param  isChecked true if this element is to be selected
     */
    public void setChecked( final boolean isChecked ) {
        getEnclosingForm().setCheckedAttribute(getElement(), isChecked);
    }


    /**
     *  Return true if this element is currently selected
     *
     * @return  See above
     */
    public boolean isChecked() {
        return getAttributeValue("checked").length() != 0;
    }
}

