/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;

/**
 * Jelly tag "assertTitle"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class AssertTitleTag extends HtmlUnitTagSupport {
    private String expectedText_;

    /**
     * Process the tag.
     * @param xmlOutput The xml output
     * @throws JellyTagException If a problem occurs
     */
    public void doTag(XMLOutput xmlOutput) throws JellyTagException {
        invokeBody(xmlOutput);
        if( expectedText_ == null ) {
            throw new JellyTagException("text is a mandatory attribute");
        }

        final String actualText = getHtmlPage().getTitleText();
        if( actualText.equals(expectedText_) == false ) {
            throw new JellyTagException("Expected text ["+expectedText_+"] but got ["+actualText+"] instead");
        }
    }


    /**
     * Callback from Jelly to set the value of the text attribute.
     * @param text The new value.
     */
    public void setText( final String text ) {
        expectedText_ = text;;
    }
}

