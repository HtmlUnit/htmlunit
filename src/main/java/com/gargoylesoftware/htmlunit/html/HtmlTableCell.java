/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import java.util.Map;

/**
 * An abstract cell that provides the implementation for HtmlTableDataCell and HtmlTableHeaderCell.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @see HtmlTableDataCell
 * @see HtmlTableHeaderCell
 */
public abstract class HtmlTableCell extends ClickableElement {

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that this element is contained within
     * @param attributes the initial attributes
     */
    protected HtmlTableCell(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the colspan attribute, or <tt>1</tt> if the attribute wasn't specified.
     * @return the value of the colspan attribute, or <tt>1</tt> if the attribute wasn't specified
     */
    public int getColumnSpan() {
        final String spanString = getAttributeValue("colspan");
        if (spanString == null || spanString.length() == 0) {
            return 1;
        }
        return Integer.parseInt(spanString);
    }

    /**
     * Returns the value of the rowspan attribute, or <tt>1</tt> if the attribute wasn't specified.
     * @return the value of the rowspan attribute, or <tt>1</tt> if the attribute wasn't specified
     */
    public int getRowSpan() {
        final String spanString = getAttributeValue("rowspan");
        if (spanString == null || spanString.length() == 0) {
            return 1;
        }
        return Integer.parseInt(spanString);
    }

    /**
     * Returns the table row containing this cell.
     * @return the table row containing this cell
     */
    public HtmlTableRow getEnclosingRow() {
        return (HtmlTableRow) getEnclosingElement("tr");
    }

}
