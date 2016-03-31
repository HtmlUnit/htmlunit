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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * The JavaScript object for {@link HtmlSelect}.
 *
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

    /** "Live" labels collection; has to be a member to have equality (==) working. */
    private AbstractList labels_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLSelectElement() {
    }

    /**
     * Initialize the object.
     *
     */
    public void initialize() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        htmlSelect.setScriptableObject(this);
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
        if (index < 0 && getBrowserVersion().hasFeature(JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE)) {
            return;
        }
        final HTMLOptionsCollection options = getOptions();
        if (index >= options.getLength() && getBrowserVersion().hasFeature(JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE)) {
            return;
        }

        getOptions().remove(index);
    }

    /**
     * Adds a new item to the list (optionally) before the specified item.
     * @param newOptionObject the DomNode to insert
     * @param beforeOptionObject for Firefox: the DomNode to insert the previous element before (null if at end),
     * for Internet Explorer: the index where the element should be placed (optional).
     */
    @JsxFunction
    public void add(final HTMLOptionElement newOptionObject, final Object beforeOptionObject) {
        getOptions().add(newOptionObject, beforeOptionObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object appendChild(final Object childObject) {
        final Object object = super.appendChild(childObject);
        getHtmlSelect().ensureSelectedIndex();
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object insertBeforeImpl(final Object[] args) {
        final Object object = super.insertBeforeImpl(args);
        getHtmlSelect().ensureSelectedIndex();
        return object;
    }

    /**
     * Gets the item at the specified index.
     * @param index the position of the option to retrieve
     * @return the option
     */
    @JsxFunction
    public Object item(final int index) {
        final Object option = getOptions().item(index);
        if (option == Context.getUndefinedValue()) {
            return null;
        }
        return option;
    }

    /**
     * Returns the type of this input.
     * @return the type
     */
    @JsxGetter
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
     * Returns the value of the {@code options} property.
     * @return the {@code options} property
     */
    @JsxGetter
    public HTMLOptionsCollection getOptions() {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_;
    }

    /**
     * Returns the value of the {@code selectedIndex} property.
     * @return the {@code selectedIndex} property
     */
    @JsxGetter
    public int getSelectedIndex() {
        return getHtmlSelect().getSelectedIndex();
    }

    /**
     * Sets the value of the {@code selectedIndex} property.
     * @param index the new value
     */
    @JsxSetter
    public void setSelectedIndex(final int index) {
        getHtmlSelect().setSelectedIndex(index);
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
        return ((HTMLOptionElement) selectedOptions.get(0).getScriptableObject()).getValue();
    }

    /**
     * Returns the value of the {@code length} property.
     * @return the {@code length} property
     */
    @JsxGetter
    public int getLength() {
        return getOptions().getLength();
    }

    /**
     * Removes options by reducing the {@code length} property.
     * @param newLength the new {@code length} property value
     */
    @JsxSetter
    public void setLength(final int newLength) {
        getOptions().setLength(newLength);
    }

    /**
     * Returns the specified indexed property.
     * @param index the index of the property
     * @param start the scriptable object that was originally queried for this property
     * @return the property
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        return getOptions().get(index, start);
    }

    /**
     * Sets the index property.
     * @param index the index
     * @param start the scriptable object that was originally invoked for this property
     * @param newValue the new value
     */
    @Override
    public void put(final int index, final Scriptable start, final Object newValue) {
        getOptions().put(index, start, newValue);
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
    public void setValue(final Object newValue) {
        final String val = Context.toString(newValue);
        getHtmlSelect().setSelectedAttribute(val, true, false);
    }

    /**
     * Returns the {@code size} attribute.
     * @return the {@code size} attribute
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
     * Sets the {@code size} attribute.
     * @param size the {@code size} attribute
     */
    @JsxSetter
    public void setSize(final String size) {
        getDomNodeOrDie().setAttribute("size", size);
    }

    /**
     * Returns {@code true} if the {@code multiple} attribute is set.
     * @return {@code true} if the {@code multiple} attribute is set
     */
    @JsxGetter
    public boolean getMultiple() {
        return getDomNodeOrDie().hasAttribute("multiple");
    }

    /**
     * Sets or clears the {@code multiple} attribute.
     * @param multiple {@code true} to set the {@code multiple} attribute, {@code false} to clear it
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

    /**
     * Returns the labels associated with the element.
     * @return the labels associated with the element
     */
    @JsxGetter(@WebBrowser(CHROME))
    public AbstractList getLabels() {
        if (labels_ == null) {
            labels_ = new LabelsHelper(getDomNodeOrDie());
        }
        return labels_;
    }

}
