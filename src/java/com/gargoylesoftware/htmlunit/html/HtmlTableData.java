/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "td"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlTableData extends HtmlTableCell {
    /**
     *  Create an instance
     *
     * @param  page The page that this element is contained within
     * @param  element The xml element that represents this html element
     * @param  row The row within the table that this cell is located at
     * @param  column The column within the table that this cell is located at
     */
    HtmlTableData( final HtmlPage page, final Element element, final int row, final int column ) {
        super( page, element, row, column );
    }
}

