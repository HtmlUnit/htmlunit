/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.Iterator;

/**
 * An object that knows how to create Table elements
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 */
class TableElementCreator extends HtmlElementCreator {

    /**
     * Create an HtmlElement for the specified xmlElement, contained in the specified page.
     *
     * @param page The page that this element will belong to.
     * @param xmlNode The xml element that this HtmlElement corresponds to.
     * @return The new HtmlElement.
     */
    DomNode create( final HtmlPage page, final Node xmlNode ) {
        final Element xmlElement = (Element) xmlNode;
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

