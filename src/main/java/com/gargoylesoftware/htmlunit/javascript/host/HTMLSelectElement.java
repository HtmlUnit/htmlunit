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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

/**
 * The JavaScript object for {@link HtmlSelect}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLSelectElement extends FormField {

    private static final long serialVersionUID = 4332789476842114628L;
    private HTMLOptionsCollection optionsArray_;

    /**
     * Create an instance.
     */
    public HTMLSelectElement() {
    }

    /**
     * JavaScript constructor.  This must be declared in every JavaScript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Initialize the object.
     *
     */
    public void initialize() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        htmlSelect.setScriptObject(this);
        if (optionsArray_ == null) {
            optionsArray_ = new HTMLOptionsCollection(this);
            optionsArray_.initialize(htmlSelect);
        }
    }

    /**
     * Remove option at the specified index
     * @param index The index of the item to remove
     */
    public void jsxFunction_remove(final int index) {
        put(index, null, null);
    }

    /**
     * Add a new item to the list (optionally) before the specified item
     * @param newOptionObject The DomNode to insert
     * @param arg2 for Firefox: the DomNode to insert the previous element before (null if at end),
     * for Internet Explorer: the index where the element should be placed (optional)
     */
    public void jsxFunction_add(final HTMLOptionElement newOptionObject, final Object arg2) {
        if (getBrowserVersion().isIE()) {
            add_IE(newOptionObject, arg2);
        }
        else {
            add(newOptionObject, arg2);
        }
    }
    
    /**
     * Add a new item to the list (optionally) at the specified index in IE way
     * @param newOptionObject The DomNode to insert
     * @param index (optional) the index where the node should be inserted
     */
    protected void add_IE(final HTMLOptionElement newOptionObject, final Object index) {
        final HtmlSelect select = getHtmlSelect();
        final HtmlOption beforeOption;
        if (Context.getUndefinedValue().equals(index)) {
            beforeOption = null;
        }
        else {
            final int intIndex = ((Integer) Context.jsToJava(index, Integer.class)).intValue();
            if (intIndex >= select.getOptionSize()) {
                beforeOption = null;
            }
            else {
                beforeOption = select.getOption(intIndex);
            }
        }
        
        addBefore(newOptionObject, beforeOption);
    }

    /**
     * Add a new item to the list (optionally) before the specified item in Mozilla way
     * @param newOptionObject The DomNode to insert
     * @param beforeOptionObject The DomNode to insert the previous element before (null if at end)
     */
    protected void add(final HTMLOptionElement newOptionObject, final Object beforeOptionObject) {
        final HtmlOption beforeOption;
        if (beforeOptionObject == null) {
            beforeOption = null;
        }
        else if (Context.getUndefinedValue().equals(beforeOptionObject)) {
            throw Context.reportRuntimeError("Not enough arguments [SelectElement.add]");
        }
        else {
            beforeOption = (HtmlOption) ((HTMLOptionElement) beforeOptionObject).getDomNodeOrDie();
        }
        addBefore(newOptionObject, beforeOption);
    }
    
    /**
     * Adds the option (and create the associated dom node if needed) before the specified one
     * or at the end if the specified one in null
     * @param newOptionObject the new option to add
     * @param beforeOption the option that should be after the option to add
     */
    protected void addBefore(final HTMLOptionElement newOptionObject, final HtmlOption beforeOption) {
        final HtmlSelect select = getHtmlSelect();

        HtmlOption htmlOption = (HtmlOption) newOptionObject.getHtmlElementOrNull();
        if (htmlOption == null) {
            htmlOption = (HtmlOption) HTMLParser.getFactory(HtmlOption.TAG_NAME).createElement(
                    select.getPage(), HtmlOption.TAG_NAME, null);
        }

        if (beforeOption == null) {
            select.appendDomChild(htmlOption);
        }
        else {
            beforeOption.insertBefore(htmlOption);
        }
    }

    /**
     * Return the type of this input.
     * @return The type
     */
    @Override
    public String jsxGet_type() {
        final String type;
        if (getHtmlSelect().isMultipleSelectEnabled()) {
            type = "select-multiple";
        }
        else {
            type = "select-one";
        }
        return type;
    }

    /**
     * Return the value of the "options" property
     * @return The options property
     */
    public HTMLOptionsCollection jsxGet_options() {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_;
    }

    /**
     * Return the value of the "selectedIndex" property
     * @return The selectedIndex property
     */
    public int jsxGet_selectedIndex() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        final List<HtmlOption> selectedOptions = htmlSelect.getSelectedOptions();
        if (selectedOptions.isEmpty()) {
            return -1;
        }
        else {
            final List<HtmlOption> allOptions = htmlSelect.getOptions();
            return allOptions.indexOf(selectedOptions.get(0));
        }
    }

    /**
     * Set the value of the "selectedIndex" property
     * @param index The new value
     */
    public void jsxSet_selectedIndex(final int index) {
        final HtmlSelect htmlSelect = getHtmlSelect();

        for (final HtmlOption itemToUnSelect : htmlSelect.getSelectedOptions()) {
            htmlSelect.setSelectedAttribute(itemToUnSelect, false);
        }
        if (index < 0) {
            return;
        }

        final List<HtmlOption> allOptions = htmlSelect.getOptions();

        if (index < allOptions.size()) {
            final HtmlOption itemToSelect = allOptions.get(index);
            htmlSelect.setSelectedAttribute(itemToSelect, true);
        }
    }

    /**
     * Return the actual value of the selected Option
     * @return The value
     */
    @Override
    public String jsxGet_value() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        final List<HtmlOption> selectedOptions = htmlSelect.getSelectedOptions();
        if (selectedOptions.isEmpty()) {
            return "";
        }
        else {
            return selectedOptions.get(0).getValueAttribute();
        }
    }

    /**
     * Return the value of the "length" property
     * @return The length property
     */
    public int jsxGet_length() {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_.jsxGet_length();
    }

    /**
     * Remove options by reducing the "length" property
     * @param newLength The new length property value
     */
    public void jsxSet_length(final int newLength) {
        if (optionsArray_ == null) {
            initialize();
        }
        optionsArray_.jsxSet_length(newLength);
    }

    /**
     * Return the specified indexed property
     * @param index The index of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_.get(index, start);
    }

    /**
     * Set the index property
     * @param index The index
     * @param start The scriptable object that was originally invoked for this property
     * @param newValue The new value
     */
    @Override
    public void put(final int index, final Scriptable start, final Object newValue) {
        if (optionsArray_ == null) {
            initialize();
        }
        optionsArray_.put(index, start, newValue);
    }

    /**
     * Return the HTML select object.
     * @return The HTML select object.
     */
    private HtmlSelect getHtmlSelect() {
        return (HtmlSelect) getHtmlElementOrDie();
    }

    /**
     * Selects the option with the specified value
     * @param newValue The value of the option to select
     */
    @Override
    public void jsxSet_value(final String newValue) {
        getHtmlSelect().setSelectedAttribute(newValue, true);
    }

    /**
     * Returns the <tt>size</tt> attribute.
     * @return the <tt>size</tt> attribute.
     */
    public int jsxGet_size() {
        int size = 0;
        final String sizeAttribute = getHtmlElementOrDie().getAttributeValue("size");
        if (sizeAttribute != HtmlSelect.ATTRIBUTE_NOT_DEFINED && sizeAttribute != HtmlSelect.ATTRIBUTE_VALUE_EMPTY) {
            try {
                size = Integer.parseInt(sizeAttribute);
            }
            catch (final Exception e) {
                //silently ignore
            }
        }
        return size;
    }
    
    /**
     * Sets the <tt>size</tt> attribute.
     * @param size The <tt>size</tt> attribute.
     */
    public void jsxSet_size(final String size) {
        getHtmlElementOrDie().setAttributeValue("size", size);
    }
}
