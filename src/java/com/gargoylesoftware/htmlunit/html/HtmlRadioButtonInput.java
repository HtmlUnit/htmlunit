/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
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
public class HtmlRadioButtonInput extends HtmlInput {

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlRadioButtonInput( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Set the "checked" attribute
     *
     * @param  isChecked true if this element is to be selected
     */
    public final void setChecked( final boolean isChecked ) {
        final String type = getTypeAttribute();

        final HtmlForm form = getEnclosingForm();

        if( isChecked ) {
            form.setCheckedRadioButton( getNameAttribute(), getValueAttribute() );
        }
        else {
            form.setCheckedAttribute( getElement(), false );
        }
    }


    /**
     *  Return true if this element is currently selected
     *
     * @return  See above
     */
    public final boolean isChecked() {
        return isAttributeDefined("checked");
    }
}

