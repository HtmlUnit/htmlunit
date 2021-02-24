/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TABLE_VALIGN_SUPPORTS_IE_VALUES;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

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

    private static final List<String> VALID_RULES_ = Arrays.asList("none", "groups", "rows", "cols");

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
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
        final HtmlTable table = (HtmlTable) getDomNodeOrDie();
        return new HTMLCollection(table, false) {
            @Override
            protected List<DomNode> computeElements() {
                return new ArrayList<>(table.getBodies());
            }
        };
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
     * If this table does not have a tbody element, this method creates an empty tbody
     * element, adds it to the table and then returns it. If this table already has a
     * tbody element, this method returns the existing tbody element.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536402.aspx">MSDN Documentation</a>
     * @return a newly added caption if no caption exists, or the first existing caption
     */
    @JsxFunction
    public Object createTBody() {
        return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("tbody"));
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
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        return getDomNodeOrDie().getAttributeDirect("width");
    }

    /**
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    @JsxSetter
    public void setWidth(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the {@code cellSpacing} property.
     * @return the {@code cellSpacing} property
     */
    @JsxGetter
    public String getCellSpacing() {
        return getDomNodeOrDie().getAttributeDirect("cellspacing");
    }

    /**
     * Sets the {@code cellSpacing} property.
     * @param cellSpacing the {@code cellSpacing} property
     */
    @JsxSetter
    public void setCellSpacing(final String cellSpacing) {
        getDomNodeOrDie().setAttribute("cellspacing", cellSpacing);
    }

    /**
     * Returns the {@code cellPadding} property.
     * @return the {@code cellPadding} property
     */
    @JsxGetter
    public String getCellPadding() {
        return getDomNodeOrDie().getAttributeDirect("cellpadding");
    }

    /**
     * Sets the {@code cellPadding} property.
     * @param cellPadding the {@code cellPadding} property
     */
    @JsxSetter
    public void setCellPadding(final String cellPadding) {
        getDomNodeOrDie().setAttribute("cellpadding", cellPadding);
    }

    /**
     * Gets the {@code border} property.
     * @return the {@code border} property
     */
    @JsxGetter
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttributeDirect("border");
        return border;
    }

    /**
     * Sets the {@code border} property.
     * @param border the {@code border} property
     */
    @JsxSetter
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the {@code bgColor} property.
     * @return the value of the {@code bgColor} property
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getBgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the {@code bgColor} property.
     * @param bgColor the value of the {@code bgColor} property
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setBgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Gets the {@code borderColor} property.
     * @return the property
     */
    @JsxGetter(IE)
    public String getBorderColor() {
        return getDomNodeOrDie().getAttribute("borderColor");
    }

    /**
     * Sets the {@code borderColor} property.
     * @param borderColor the new property
     */
    @JsxSetter(IE)
    public void setBorderColor(final String borderColor) {
        setColorAttribute("borderColor", borderColor);
    }

    /**
     * Gets the {@code borderColor} property.
     * @return the property
     */
    @JsxGetter(IE)
    public String getBorderColorDark() {
        return getDomNodeOrDie().getAttribute("borderColorDark");
    }

    /**
     * Sets the {@code borderColor} property.
     * @param borderColor the new property
     */
    @JsxSetter(IE)
    public void setBorderColorDark(final String borderColor) {
        setColorAttribute("borderColorDark", borderColor);
    }

    /**
     * Gets the {@code borderColor} property.
     * @return the property
     */
    @JsxGetter(IE)
    public String getBorderColorLight() {
        return getDomNodeOrDie().getAttribute("borderColorLight");
    }

    /**
     * Sets the {@code borderColor} property.
     * @param borderColor the new property
     */
    @JsxSetter(IE)
    public void setBorderColorLight(final String borderColor) {
        setColorAttribute("borderColorLight", borderColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInnerText() {
        return getDomNodeOrDie().asText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object appendChild(final Object childObject) {
        final Object appendedChild = super.appendChild(childObject);
        getWindow().clearComputedStyles(this);
        return appendedChild;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object removeChild(final Object childObject) {
        final Object removedChild = super.removeChild(childObject);
        getWindow().clearComputedStyles(this);
        return removedChild;
    }

    /**
     * Gets the {@code summary} property.
     * @return the property
     */
    @JsxGetter
    public String getSummary() {
        return getDomNodeOrDie().getAttributeDirect("summary");
    }

    /**
     * Sets the {@code summary} property.
     * @param summary the new property
     */
    @JsxSetter
    public void setSummary(final String summary) {
        setAttribute("summary", summary);
    }

    /**
     * Gets the {@code rules} property.
     * @return the property
     */
    @JsxGetter
    public String getRules() {
        String rules = getDomNodeOrDie().getAttributeDirect("rules");
        if (getBrowserVersion().hasFeature(JS_TABLE_VALIGN_SUPPORTS_IE_VALUES)
                && !VALID_RULES_.contains(rules)) {
            rules = "";
        }
        return rules;
    }

    /**
     * Sets the {@code rules} property.
     * @param rules the new property
     */
    @JsxSetter
    public void setRules(String rules) {
        if (getBrowserVersion().hasFeature(JS_TABLE_VALIGN_SUPPORTS_IE_VALUES)) {
            rules = rules.toLowerCase(Locale.ROOT);
            if (!rules.isEmpty() && !VALID_RULES_.contains(rules)) {
                throw Context.throwAsScriptRuntimeEx(new Exception("Invalid argument"));
            }
        }
        setAttribute("rules", rules);
    }

}
