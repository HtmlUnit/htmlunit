/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;
import java.util.Iterator;

/**
 * An object that knows how to create Table elements
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
class TableElementCreator extends HtmlElementCreator {

    /**
     * Create an HtmlElement for the specified xmlElement, contained in the specified page.
     *
     * @param page The page that this element will belong to.
     * @param xmlElement The xml element that this HtmlElement corresponds to.
     * @return The new HtmlElement.
     */
    HtmlElement create( final HtmlPage page, final Element xmlElement ) {
        final String tagName = page.getTagName(xmlElement);

        if( tagName.equals("tr") ) {
            return createTableRow( page, xmlElement );
        }
        else if( tagName.equals("td") || tagName.equals("th") ) {
            return createTableCell( page, xmlElement );
        }
        else {
            throw new IllegalStateException("Unexpected element "+xmlElement);
        }
    }


    private HtmlElement createTableRow( final HtmlPage page, final Element xmlElement ) {
        final Iterator iterator = getTable(page, xmlElement).getRows().iterator();
        while( iterator.hasNext() ) {
            final HtmlTableRow row = (HtmlTableRow)iterator.next();
            if( row.getElement() == xmlElement ) {
                return row;
            }
        }

        throw new IllegalStateException("specified row could not be found in table");
    }


    private HtmlElement createTableCell( final HtmlPage page, final Element xmlElement ) {
        final Iterator rowIterator = getTable(page, xmlElement).getRows().iterator();
        while( rowIterator.hasNext() ) {
            final HtmlTableRow row = (HtmlTableRow)rowIterator.next();
            final Iterator cellIterator = row.getCells().iterator();
            while( cellIterator.hasNext() ) {
                final HtmlTableCell cell = (HtmlTableCell)cellIterator.next();
                if( cell.getElement() == xmlElement ) {
                    return cell;
                }
            }
        }

        throw new IllegalStateException("specified cell could not be found in table");
    }


    private HtmlTable getTable( final HtmlPage page, final Element startingElement ) {
        Element element = startingElement;
        while( page.getTagName(element).equals("table") == false ) {
            element = (Element)element.getParentNode();
        }

        return (HtmlTable)page.getHtmlElement(element);
    }
}

