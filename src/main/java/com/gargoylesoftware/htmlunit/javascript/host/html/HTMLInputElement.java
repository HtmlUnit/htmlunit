/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import org.apache.commons.lang3.math.NumberUtils;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.InputElementFactory;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.FormField;

/**
 * The JavaScript object for form input elements (html tag &lt;input ...&gt;).
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class HTMLInputElement extends FormField {

    /**
     * Creates an instance.
     */
    public HTMLInputElement() {
        // Empty.
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file
     * because the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * Sets the value of the attribute "type".
     * Note: this replace the DOM node with a new one.
     * @param newType the new type to set
     */
    public void jsxSet_type(final String newType) {
        HtmlInput input = getHtmlInputOrDie();

        if (!input.getTypeAttribute().equalsIgnoreCase(newType)) {
            final AttributesImpl attributes = readAttributes(input);
            final int index = attributes.getIndex("type");
            attributes.setValue(index, newType);

            final HtmlInput newInput = (HtmlInput) InputElementFactory.instance
                .createElement(input.getPage(), "input", attributes);

            if (input.getParentNode() != null) {
                input.getParentNode().replaceChild(newInput, input);
            }
            else {
                // the input hasn't yet been inserted into the DOM tree (likely has been
                // created via document.createElement()), so simply replace it with the
                // new Input instance created in the code above
                input = newInput;
            }

            input.setScriptObject(null);
            setDomNode(newInput, true);
        }
    }

    /**
     * Sets the checked property. Although this property is defined in Input it
     * doesn't make any sense for input's other than checkbox and radio. This
     * implementation does nothing. The implementations in Checkbox and Radio
     * actually do the work.
     *
     * @param checked  True if this input should have the "checked" attribute set
     */
    public void jsxSet_checked(final boolean checked) {
        ((HtmlInput) getDomNodeOrDie()).setChecked(checked);
    }

    /**
     * Commodity for <code>(HtmlInput) getDomNodeOrDie()</code>.
     * @return the bound HTML input
     */
    protected HtmlInput getHtmlInputOrDie() {
        return (HtmlInput) getDomNodeOrDie();
    }

    /**
     * Returns the value of the checked property. Although this property is
     * defined in Input it doesn't make any sense for input's other than
     * checkbox and radio. This implementation does nothing. The
     * implementations in Checkbox and Radio actually do the work.
     *
     *@return the checked property
     */
    public boolean jsxGet_checked() {
        return ((HtmlInput) getDomNodeOrDie()).isChecked();
    }

    /**
     * Select this element.
     */
    public void jsxFunction_select() {
        final HtmlInput input = getHtmlInputOrDie();
        if (input instanceof HtmlTextInput) {
            ((HtmlTextInput) getDomNodeOrDie()).select();
        }
        // currently nothing for other input types
    }

    /**
     * Uses {@link #jsxSet_type(String)} if attribute's name is type to
     * replace DOM node as well as long as we have subclasses of {@link HtmlInput}.
     * {@inheritDoc}
     */
    @Override
    public void jsxFunction_setAttribute(final String name, final String value) {
        if ("type".equals(name)) {
            jsxSet_type(value);
        }
        else {
            super.jsxFunction_setAttribute(name, value);
        }
    }

    /**
     * Returns the input's default value, used if the containing form gets reset.
     * @return the input's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    public String jsxGet_defaultValue() {
        return ((HtmlInput) getDomNodeOrDie()).getDefaultValue();
    }

    /**
     * Sets the input's default value, used if the containing form gets reset.
     * @param defaultValue the input's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    public void jsxSet_defaultValue(final String defaultValue) {
        ((HtmlInput) getDomNodeOrDie()).setDefaultValue(defaultValue);
    }

    /**
     * Returns the input's default checked value, used if the containing form gets reset.
     * @return the input's default checked value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533715.aspx">MSDN Documentation</a>
     */
    public boolean jsxGet_defaultChecked() {
        return ((HtmlInput) getDomNodeOrDie()).isDefaultChecked();
    }

    /**
     * Sets the input's default checked value, used if the containing form gets reset.
     * @param defaultChecked the input's default checked value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533715.aspx">MSDN Documentation</a>
     */
    public void jsxSet_defaultChecked(final boolean defaultChecked) {
        ((HtmlInput) getDomNodeOrDie()).setDefaultChecked(defaultChecked);
    }

    /**
     * Gets the value of "textLength" attribute.
     * @return the text length
     */
    public int jsxGet_textLength() {
        return jsxGet_value().length();
    }

    /**
     * Gets the value of "selectionStart" attribute.
     * @return the selection start
     */
    public int jsxGet_selectionStart() {
        return ((SelectableTextInput) getDomNodeOrDie()).getSelectionStart();
    }

    /**
     * Sets the value of "selectionStart" attribute.
     * @param start selection start
     */
    public void jsxSet_selectionStart(final int start) {
        ((SelectableTextInput) getDomNodeOrDie()).setSelectionStart(start);
    }

    /**
     * Gets the value of "selectionEnd" attribute.
     * @return the selection end
     */
    public int jsxGet_selectionEnd() {
        return ((SelectableTextInput) getDomNodeOrDie()).getSelectionEnd();
    }

    /**
     * Sets the value of "selectionEnd" attribute.
     * @param end selection end
     */
    public void jsxSet_selectionEnd(final int end) {
        ((SelectableTextInput) getDomNodeOrDie()).setSelectionEnd(end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAttributeName(final String name) {
        if ("maxlength".equals(name.toLowerCase())) {
            return "maxLength".equals(name);
        }

        if ("readOnly".equals(name.toLowerCase())) {
            return "readOnly".equals(name);
        }
        return super.isAttributeName(name);
    }

    /**
     * Gets the max length.
     * @return the max length
     */
    public int jsxGet_maxLength() {
        final String attrValue = getDomNodeOrDie().getAttribute("maxLength");
        return NumberUtils.toInt(attrValue, -1);
    }

    /**
     * Sets the value of "maxLength" attribute.
     * @param length the new value
     */
    public void jsxSet_maxLength(final int length) {
        getDomNodeOrDie().setAttribute("maxLength", Integer.toString(length));
    }

    /**
     * Gets the value of "readOnly" attribute.
     * @return the readOnly attribute
     */
    public boolean jsxGet_readOnly() {
        return ((HtmlInput) getDomNodeOrDie()).isReadOnly();
    }

    /**
     * Sets the value of "readOnly" attribute.
     * @param readOnly the new value
     */
    public void jsxSet_readOnly(final boolean readOnly) {
        ((HtmlInput) getDomNodeOrDie()).setReadOnly(readOnly);
    }

    /**
     * Sets the selected portion of this input element.
     * @param start the index of the first character to select
     * @param end the index of the character after the selection
     */
    public void jsxFunction_setSelectionRange(final int start, final int end) {
        jsxSet_selectionStart(start);
        jsxSet_selectionEnd(end);
    }

    /**
     * Returns the value of the "alt" property.
     * @return the value of the "alt" property
     */
    public String jsxGet_alt() {
        final String alt = getDomNodeOrDie().getAttribute("alt");
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    public void jsxSet_alt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    public String jsxGet_border() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    public String jsxGet_align() {
        return getAlign(true);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    public void jsxSet_align(final String align) {
        final boolean ignoreIfNoError = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_84);
        setAlign(align, ignoreIfNoError);
    }

}
