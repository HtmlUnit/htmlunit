/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;

/**
 * Wrapper for the html element "base".
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlBase extends HtmlElement {

    /**
     * Create an instance of HtmlBase
     *
     * @param page The HtmlPage that contains this element.
     * @param xmlElement The actual html element that we are wrapping.
     */
    HtmlBase( final HtmlPage page, final Element xmlElement ) {
        super(page, xmlElement);
    }


    /**
     * Return the value of the attribute "href".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "href"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHrefAttribute() {
        return getAttributeValue("href");
    }


    /**
     * Return the value of the attribute "target".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "target"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTargetAttribute() {
        return getAttributeValue("target");
    }
}
