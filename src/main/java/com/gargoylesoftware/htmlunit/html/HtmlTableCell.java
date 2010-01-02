/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

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
public abstract class HtmlTableCell extends HtmlElement {

    private static final long serialVersionUID = -6362606593038086865L;

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that this element is contained within
     * @param attributes the initial attributes
     */
    protected HtmlTableCell(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the colspan attribute, or <tt>1</tt> if the attribute wasn't specified.
     * @return the value of the colspan attribute, or <tt>1</tt> if the attribute wasn't specified
     */
    public int getColumnSpan() {
        final String spanString = getAttribute("colspan");
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
        final String spanString = getAttribute("rowspan");
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
