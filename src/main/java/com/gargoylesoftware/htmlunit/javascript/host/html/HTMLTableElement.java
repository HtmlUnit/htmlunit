/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_READONLY_FOR_TABLE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object {@code HTMLTableElement}.
 *
 * @author David D. Kilzer
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlTable.class)
public class HTMLTableElement extends RowContainer {

    private HTMLCollection tBodies_; // has to be a member to have equality (==) working

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLTableElement() {
    }

    /**
     * Returns the table's caption element, or {@code null} if none exists. If more than one
     * caption is declared in the table, this method returns the first one.
     * @return the table's caption element
     */
    @JsxGetter
    public Object getCaption() {
        final List<HtmlElement> captions = getDomNodeOrDie().getElementsByTagName("caption");
        if (captions.isEmpty()) {
            return null;
        }
        return getScriptableFor(captions.get(0));
    }

    /**
     * Sets the caption.
     * @param o the caption
     */
    @JsxSetter
    public void setCaption(final Object o) {
        if (!(o instanceof HTMLTableCaptionElement)) {
            throw Context.reportRuntimeError("Not a caption");
        }

        // remove old caption (if any)
        deleteCaption();

        final HTMLTableCaptionElement caption = (HTMLTableCaptionElement) o;
        getDomNodeOrDie().appendChild(caption.getDomNodeOrDie());
    }

    /**
     * Returns the table's tfoot element, or {@code null} if none exists. If more than one
     * tfoot is declared in the table, this method returns the first one.
     * @return the table's tfoot element
     */
    @JsxGetter
    public Object getTFoot() {
        final List<HtmlElement> tfoots = getDomNodeOrDie().getElementsByTagName("tfoot");
        if (tfoots.isEmpty()) {
            return null;
        }
        return getScriptableFor(tfoots.get(0));
    }

    /**
     * Sets the tFoot.
     * @param o the tFoot
     */
    @JsxSetter
    public void setTFoot(final Object o) {
        if (!(o instanceof HTMLTableSectionElement
            && "TFOOT".equals(((HTMLTableSectionElement) o).getTagName()))) {
            throw Context.reportRuntimeError("Not a tFoot");
        }

        // remove old caption (if any)
        deleteTFoot();

        final HTMLTableSectionElement tfoot = (HTMLTableSectionElement) o;
        getDomNodeOrDie().appendChild(tfoot.getDomNodeOrDie());
    }

    /**
     * Returns the table's thead element, or {@code null} if none exists. If more than one
     * thead is declared in the table, this method returns the first one.
     * @return the table's thead element
     */
    @JsxGetter
    public Object getTHead() {
        final List<HtmlElement> theads = getDomNodeOrDie().getElementsByTagName("thead");
        if (theads.isEmpty()) {
            return null;
        }
        return getScriptableFor(theads.get(0));
    }

    /**
     * Sets the {@code tHead}.
     * @param o the {@code tHead}
     */
    @JsxSetter
    public void setTHead(final Object o) {
        if (!(o instanceof HTMLTableSectionElement
            && "THEAD".equals(((HTMLTableSectionElement) o).getTagName()))) {
            throw Context.reportRuntimeError("Not a tHead");
        }

        // remove old caption (if any)
        deleteTHead();

        final HTMLTableSectionElement thead = (HTMLTableSectionElement) o;
        getDomNodeOrDie().appendChild(thead.getDomNodeOrDie());
    }

    /**
     * Returns the tbody's in the table.
     * @return the tbody's in the table
     */
    @JsxGetter
    public Object getTBodies() {
        if (tBodies_ == null) {
            final HtmlTable table = (HtmlTable) getDomNodeOrDie();
            tBodies_ = new HTMLCollection(table, false) {
                @Override
                protected List<Object> computeElements() {
                    return new ArrayList<Object>(table.getBodies());
                }
            };
        }
        return tBodies_;
    }

    /**
     * If this table does not have a caption, this method creates an empty table caption,
     * adds it to the table and then returns it. If one or more captions already exist,
     * this method returns the first existing caption.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536381.aspx">MSDN Documentation</a>
     * @return a newly added caption if no caption exists, or the first existing caption
     */
    @JsxFunction
    public Object createCaption() {
        return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("caption"));
    }

    /**
     * If this table does not have a tfoot element, this method creates an empty tfoot
     * element, adds it to the table and then returns it. If this table already has a
     * tfoot element, this method returns the existing tfoot element.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536402.aspx">MSDN Documentation</a>
     * @return a newly added caption if no caption exists, or the first existing caption
     */
    @JsxFunction
    public Object createTFoot() {
        return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("tfoot"));
    }

    /**
     * If this table does not have a thead element, this method creates an empty
     * thead element, adds it to the table and then returns it. If this table
     * already has a thead element, this method returns the existing thead element.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536403.aspx">MSDN Documentation</a>
     * @return a newly added caption if no caption exists, or the first existing caption
     */
    @JsxFunction
    public Object createTHead() {
        return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("thead"));
    }

    /**
     * Deletes this table's caption. If the table has multiple captions, this method
     * deletes only the first caption. If this table does not have any captions, this
     * method does nothing.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536405.aspx">MSDN Documentation</a>
     */
    @JsxFunction
    public void deleteCaption() {
        getDomNodeOrDie().removeChild("caption", 0);
    }

    /**
     * Deletes this table's tfoot element. If the table has multiple tfoot elements, this
     * method deletes only the first tfoot element. If this table does not have any tfoot
     * elements, this method does nothing.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536409.aspx">MSDN Documentation</a>
     */
    @JsxFunction
    public void deleteTFoot() {
        getDomNodeOrDie().removeChild("tfoot", 0);
    }

    /**
     * Deletes this table's thead element. If the table has multiple thead elements, this
     * method deletes only the first thead element. If this table does not have any thead
     * elements, this method does nothing.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536410.aspx">MSDN Documentation</a>
     */
    @JsxFunction
    public void deleteTHead() {
        getDomNodeOrDie().removeChild("thead", 0);
    }

    /**
     * Indicates if the row belongs to this container.
     * @param row the row to test
     * @return {@code true} if it belongs to this container
     */
    @Override
    protected boolean isContainedRow(final HtmlTableRow row) {
        final DomNode parent = row.getParentNode(); // the tbody, thead or tfoo
        return (parent != null) && parent.getParentNode() == getDomNodeOrDie();
    }

    /**
     * Handle special case where table is empty.
     * {@inheritDoc}
     */
    @Override
    public Object insertRow(final int index) {
        // check if a tbody should be created
        final List<?> rowContainers =
            getDomNodeOrDie().getByXPath("//tbody | //thead | //tfoot");
        if (rowContainers.isEmpty() || index == 0) {
            final HtmlElement tBody = getDomNodeOrDie().appendChildIfNoneExists("tbody");
            return ((RowContainer) getScriptableFor(tBody)).insertRow(0);
        }
        return super.insertRow(index);
    }

    /**
     * Returns the {@code width} attribute.
     * @return the {@code width} attribute
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        return getDomNodeOrDie().getAttribute("width");
    }

    /**
     * Sets the {@code width} attribute.
     * @param width the {@code width} attribute
     */
    @JsxSetter
    public void setWidth(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the {@code cellSpacing} attribute.
     * @return the {@code cellSpacing} attribute
     */
    @JsxGetter
    public String getCellSpacing() {
        return getDomNodeOrDie().getAttribute("cellspacing");
    }

    /**
     * Sets the {@code cellSpacing} attribute.
     * @param cellSpacing the {@code cellSpacing} attribute
     */
    @JsxSetter
    public void setCellSpacing(final String cellSpacing) {
        getDomNodeOrDie().setAttribute("cellspacing", cellSpacing);
    }

    /**
     * Returns the {@code cellPadding} attribute.
     * @return the {@code cellPadding} attribute
     */
    @JsxGetter
    public String getCellPadding() {
        return getDomNodeOrDie().getAttribute("cellpadding");
    }

    /**
     * Sets the {@code cellPadding} attribute.
     * @param cellPadding the {@code cellPadding} attribute
     */
    @JsxSetter
    public void setCellPadding(final String cellPadding) {
        getDomNodeOrDie().setAttribute("cellpadding", cellPadding);
    }

    /**
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the {@code border} attribute.
     * @param border the {@code border} attribute
     */
    @JsxSetter
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the {@code bgColor} attribute.
     * @return the value of the {@code bgColor} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getBgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the {@code bgColor} attribute.
     * @param bgColor the value of the {@code bgColor} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setBgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Gets the {@code borderColor} attribute.
     * @return the attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBorderColor() {
        return getDomNodeOrDie().getAttribute("borderColor");
    }

    /**
     * Sets the {@code borderColor} attribute.
     * @param borderColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBorderColor(final String borderColor) {
        setColorAttribute("borderColor", borderColor);
    }

    /**
     * Gets the {@code borderColor} attribute.
     * @return the attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBorderColorDark() {
        return getDomNodeOrDie().getAttribute("borderColorDark");
    }

    /**
     * Sets the {@code borderColor} attribute.
     * @param borderColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBorderColorDark(final String borderColor) {
        setColorAttribute("borderColorDark", borderColor);
    }

    /**
     * Gets the {@code borderColor} attribute.
     * @return the attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBorderColorLight() {
        return getDomNodeOrDie().getAttribute("borderColorLight");
    }

    /**
     * Sets the {@code borderColor} attribute.
     * @param borderColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBorderColorLight(final String borderColor) {
        setColorAttribute("borderColorLight", borderColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInnerText(final Object value) {
        if (getBrowserVersion().hasFeature(JS_INNER_TEXT_READONLY_FOR_TABLE)) {
            throw Context.reportRuntimeError("innerText is read-only for tag 'table'");
        }
        super.setInnerText(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInnerText() {
        return getDomNodeOrDie().asText();
    }
}

