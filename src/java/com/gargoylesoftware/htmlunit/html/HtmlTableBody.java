/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;

/**
 * Wrapper for the html element "tbody".
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlTableBody extends TableRowGroup {

    /**
     * Create an instance of HtmlTableBody
     *
     * @param page The HtmlPage that contains this element.
     * @param xmlElement The actual html element that we are wrapping.
     */
    HtmlTableBody( final HtmlPage page, final Element xmlElement ) {
        super(page, xmlElement);
    }
}
