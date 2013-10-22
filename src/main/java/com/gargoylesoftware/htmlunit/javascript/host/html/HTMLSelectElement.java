/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DISPLAY_DEFAULT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_ADD_SECOND_PARAM_IS_INDEX;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_ADD_SECOND_PARAM_IS_REQUIRED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_ITEM_THROWS_IF_NEGATIVE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_SELECTED_INDEX_THROWS_IF_BAD;

import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.FormField;

/**
 * The JavaScript object for {@link HtmlSelect}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlSelect.class)
public class HTMLSelectElement extends FormField {

    private HTMLOptionsCollection optionsArray_;

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
     * Removes option at the specified index.
     * @param index the index of the item to remove
     */
    @JsxFunction
    public void remove(final int index) {
        put(index, null, null);
    }

    /**
     * Adds a new item to the list (optionally) before the specified item.
     * @param newOptionObject the DomNode to insert
     * @param arg2 for Firefox: the DomNode to insert the previous element before (null if at end),
     * for Internet Explorer: the index where the element should be placed (optional).
     */
    @JsxFunction
    public void add(final HTMLOptionElement newOptionObject, final Object arg2) {
        if (getBrowserVersion().hasFeature(JS_SELECT_ADD_SECOND_PARAM_IS_INDEX)) {
            add_IE(newOptionObject, arg2);
        }
        else {
            add_FF(newOptionObject, arg2);
        }
        ensureSelectedIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object appendChild(final Object childObject) {
        final Object object = super.appendChild(childObject);
        ensureSelectedIndex();
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object insertBeforeImpl(final Object[] args) {
        final Object object = super.insertBeforeImpl(args);
        ensureSelectedIndex();
        return object;
    }

    /**
     * Gets the item at the specified index.
     * @param index the position of the option to retrieve
     * @return the option
     */
    @JsxFunction
    public HTMLOptionElement item(final int index) {
        if (index < 0) {
            if (getBrowserVersion().hasFeature(JS_SELECT_ITEM_THROWS_IF_NEGATIVE)) {
                throw Context.reportRuntimeError("Invalid index for select node: " + index);
            }
            return null;
        }

        final int length = getLength();
        if (index > length) {
            return null;
        }

        return (HTMLOptionElement) getHtmlSelect().getOption(index).getScriptObject();
    }

    /**
     * Adds a new item to the list (optionally) at the specified index in IE way.
     * @param newOptionObject the DomNode to insert
     * @param index (optional) the index where the node should be inserted
     */
    protected void add_IE(final HTMLOptionElement newOptionObject, final Object index) {
        final HtmlOption beforeOption;
        if (index == null) {
            throw new EvaluatorException("Null not supported as index.");
        }

        if (Context.getUndefinedValue().equals(index)) {
            beforeOption = null;
        }
        else {
            final HtmlSelect select = getHtmlSelect();
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
     * Adds a new item to the list (optionally) before the specified item in Firefox way.
     * @param newOptionObject the DomNode to insert
     * @param beforeOptionObject the DomNode to insert the previous element before (null if at end)
     */
    protected void add_FF(final HTMLOptionElement newOptionObject, final Object beforeOptionObject) {
        final HtmlOption beforeOption;
        if (beforeOptionObject == null) {
            beforeOption = null;
        }
        else if (Context.getUndefinedValue().equals(beforeOptionObject)) {
            if (getBrowserVersion().hasFeature(JS_SELECT_ADD_SECOND_PARAM_IS_REQUIRED)) {
                throw Context.reportRuntimeError("Not enough arguments [SelectElement.add]");
            }
            beforeOption = null;
        }
        else if (beforeOptionObject instanceof Number) {
            final HtmlSelect select = getHtmlSelect();
            final int intIndex = ((Integer) Context.jsToJava(beforeOptionObject, Integer.class)).intValue();
            if (intIndex >= select.getOptionSize()) {
                beforeOption = null;
            }
            else {
                beforeOption = select.getOption(intIndex);
            }
        }
        else {
            beforeOption = (HtmlOption) ((HTMLOptionElement) beforeOptionObject).getDomNodeOrDie();
        }
        addBefore(newOptionObject, beforeOption);
    }

    /**
     * Adds the option (and create the associated DOM node if needed) before the specified one
     * or at the end if the specified one in null.
     * @param newOptionObject the new option to add
     * @param beforeOption the option that should be after the option to add
     */
    protected void addBefore(final HTMLOptionElement newOptionObject, final HtmlOption beforeOption) {
        final HtmlSelect select = getHtmlSelect();

        HtmlOption htmlOption = newOptionObject.getDomNodeOrNull();
        if (htmlOption == null) {
            htmlOption = (HtmlOption) HTMLParser.getFactory(HtmlOption.TAG_NAME).createElement(
                    select.getPage(), HtmlOption.TAG_NAME, null);
        }

        if (beforeOption == null) {
            select.appendChild(htmlOption);
        }
        else {
            beforeOption.insertBefore(htmlOption);
        }
    }

    /**
     * Returns the type of this input.
     * @return the type
     */
    @Override
    public String getType() {
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
     * Returns the value of the "options" property.
     * @return the options property
     */
    @JsxGetter
    public HTMLOptionsCollection getOptions() {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_;
    }

    /**
     * Returns the value of the "selectedIndex" property.
     * @return the selectedIndex property
     */
    @JsxGetter
    public int getSelectedIndex() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        final List<HtmlOption> selectedOptions = htmlSelect.getSelectedOptions();
        if (selectedOptions.isEmpty()) {
            return -1;
        }
        final List<HtmlOption> allOptions = htmlSelect.getOptions();
        return allOptions.indexOf(selectedOptions.get(0));
    }

    /**
     * Sets the value of the "selectedIndex" property.
     * @param index the new value
     */
    @JsxSetter
    public void setSelectedIndex(final int index) {
        final HtmlSelect htmlSelect = getHtmlSelect();

        if (index != 0 && getBrowserVersion().hasFeature(JS_SELECT_SELECTED_INDEX_THROWS_IF_BAD)
                && (index < -1 || index >= htmlSelect.getOptionSize())) {
            throw Context.reportRuntimeError("Invalid index for select node: " + index);
        }

        for (final HtmlOption itemToUnSelect : htmlSelect.getSelectedOptions()) {
            htmlSelect.setSelectedAttribute(itemToUnSelect, false);
        }
        if (index < 0) {
            return;
        }

        final List<HtmlOption> allOptions = htmlSelect.getOptions();

        if (index < allOptions.size()) {
            final HtmlOption itemToSelect = allOptions.get(index);
            htmlSelect.setSelectedAttribute(itemToSelect, true, false);
        }
    }

    /**
     * Returns the actual value of the selected Option.
     * @return the value
     */
    @Override
    public String getValue() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        final List<HtmlOption> selectedOptions = htmlSelect.getSelectedOptions();
        if (selectedOptions.isEmpty()) {
            return "";
        }
        return ((HTMLOptionElement) selectedOptions.get(0).getScriptObject()).getValue();
    }

    /**
     * Returns the value of the "length" property.
     * @return the length property
     */
    @JsxGetter
    public int getLength() {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_.getLength();
    }

    /**
     * Removes options by reducing the "length" property.
     * @param newLength the new length property value
     */
    @JsxSetter
    public void setLength(final int newLength) {
        if (optionsArray_ == null) {
            initialize();
        }
        optionsArray_.setLength(newLength);
    }

    /**
     * Returns the specified indexed property.
     * @param index the index of the property
     * @param start the scriptable object that was originally queried for this property
     * @return the property
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_.get(index, start);
    }

    /**
     * Sets the index property.
     * @param index the index
     * @param start the scriptable object that was originally invoked for this property
     * @param newValue the new value
     */
    @Override
    public void put(final int index, final Scriptable start, final Object newValue) {
        if (optionsArray_ == null) {
            initialize();
        }
        optionsArray_.put(index, start, newValue);
    }

    /**
     * Returns the HTML select object.
     * @return the HTML select object
     */
    private HtmlSelect getHtmlSelect() {
        return (HtmlSelect) getDomNodeOrDie();
    }

    /**
     * Selects the option with the specified value.
     * @param newValue the value of the option to select
     */
    @Override
    public void setValue(final String newValue) {
        getHtmlSelect().setSelectedAttribute(newValue, true);
    }

    /**
     * Returns the <tt>size</tt> attribute.
     * @return the <tt>size</tt> attribute
     */
    @JsxGetter
    public int getSize() {
        int size = 0;
        final String sizeAttribute = getDomNodeOrDie().getAttribute("size");
        if (sizeAttribute != DomElement.ATTRIBUTE_NOT_DEFINED && sizeAttribute != DomElement.ATTRIBUTE_VALUE_EMPTY) {
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
     * @param size the <tt>size</tt> attribute
     */
    @JsxSetter
    public void setSize(final String size) {
        getDomNodeOrDie().setAttribute("size", size);
    }

    /**
     * Returns <tt>true</tt> if the <tt>multiple</tt> attribute is set.
     * @return <tt>true</tt> if the <tt>multiple</tt> attribute is set
     */
    @JsxGetter
    public boolean getMultiple() {
        return getDomNodeOrDie().hasAttribute("multiple");
    }

    /**
     * Sets or clears the <tt>multiple</tt> attribute.
     * @param multiple <tt>true</tt> to set the <tt>multiple</tt> attribute, <tt>false</tt> to clear it
     */
    @JsxSetter
    public void setMultiple(final boolean multiple) {
        if (multiple) {
            getDomNodeOrDie().setAttribute("multiple", "multiple");
        }
        else {
            getDomNodeOrDie().removeAttribute("multiple");
        }
    }

    private void ensureSelectedIndex() {
        final HtmlSelect select = getHtmlSelect();
        if (select.getOptionSize() == 0) {
            setSelectedIndex(-1);
        }
        else if (getSelectedIndex() == -1) {
            setSelectedIndex(0);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * {@inheritDoc}
    */
    @Override
    public String getDefaultStyleDisplay() {
        if (getBrowserVersion().hasFeature(CSS_DISPLAY_DEFAULT)) {
            return "inline";
        }
        return "inline-block";
    }
}
