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
public class HtmlTextInput extends HtmlInput {
    private final String initialValue_;

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlTextInput( final HtmlPage page, final Element element ) {
        super( page, element );
        initialValue_ = getValueAttribute();
    }


    /**
     * Return the value of this element to what it was at the time the page was loaded.
     */
    public void reset() {
        setValueAttribute(initialValue_);
    }
}

