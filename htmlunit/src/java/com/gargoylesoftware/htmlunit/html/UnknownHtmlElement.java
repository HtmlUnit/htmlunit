/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;

/**
 *  An element that is returned for an html tag that is not supported by this
 *  framework.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class UnknownHtmlElement extends HtmlElement {
    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element The xml element that represents this html element
     */
    UnknownHtmlElement( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Return the tag name of the unknown element.
     *
     * @return  The tag name
     */
    public String getName() {
        return getElement().getTagName().toLowerCase();
    }
}

