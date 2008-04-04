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

import org.apache.commons.lang.math.NumberUtils;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.InputElementFactory;

/**
 * The JavaScript object for form input elements (html tag &lt;input ...&gt;).
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLInputElement extends FormField {

    private static final long serialVersionUID = 3712016051364495710L;

    /**
     * Create an instance.
     */
    public HTMLInputElement() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file
     * because the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
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

            // Added check to make sure there is a previous sibling before trying to replace
            // newly created input variable which has yet to be inserted into DOM tree
            if (input.getPreviousSibling() != null) {
                // if the input has a previous sibling, then it was already in the
                // DOM tree and can be replaced
                input.replace(newInput);
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
     * Commodity for <code>(HtmlInput) getHtmlElementOrDie()</code>.
     * @return the bound HTML input.
     */
    protected HtmlInput getHtmlInputOrDie() {
        return (HtmlInput) getHtmlElementOrDie();
    }

    /**
     * Returns the value of the checked property. Although this property is
     * defined in Input it doesn't make any sense for input's other than
     * checkbox and radio. This implementation does nothing. The
     * implementations in Checkbox and Radio actually do the work.
     *
     *@return The checked property.
     */
    public boolean jsxGet_checked() {
        return ((HtmlInput) getDomNodeOrDie()).isChecked();
    }

    /**
     * Select this element.
     */
    public void jsxFunction_select() {
        ((HtmlTextInput) getDomNodeOrDie()).select();
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
     * @return The input's default value, used if the containing form gets reset.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/defaultvalue.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_defaultValue() {
        return ((HtmlInput) getDomNodeOrDie()).getDefaultValue();
    }

    /**
     * Sets the input's default value, used if the containing form gets reset.
     * @param defaultValue The input's default value, used if the containing form gets reset.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/defaultvalue.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_defaultValue(final String defaultValue) {
        ((HtmlInput) getDomNodeOrDie()).setDefaultValue(defaultValue);
    }

    /**
     * Returns the input's default checked value, used if the containing form gets reset.
     * @return The input's default checked value, used if the containing form gets reset.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/defaultchecked.asp">
     * MSDN Documentation</a>
     */
    public boolean jsxGet_defaultChecked() {
        return ((HtmlInput) getDomNodeOrDie()).isDefaultChecked();
    }

    /**
     * Sets the input's default checked value, used if the containing form gets reset.
     * @param defaultChecked The input's default checked value, used if the containing form gets reset.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/defaultchecked.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_defaultChecked(final boolean defaultChecked) {
        ((HtmlInput) getDomNodeOrDie()).setDefaultChecked(defaultChecked);
    }

    /**
     * Gets the value of "textLength" attribute.
     * @return the text length.
     */
    public int jsxGet_textLength() {
        return jsxGet_value().length();
    }

    /**
     * Gets the value of "selectionStart" attribute.
     * @return the selection start.
     */
    public int jsxGet_selectionStart() {
        return ((HtmlTextInput) getHtmlElementOrDie()).getSelectionStart();
    }

    /**
     * Sets the value of "selectionStart" attribute.
     * @param start selection start.
     */
    public void jsxSet_selectionStart(final int start) {
        ((HtmlTextInput) getHtmlElementOrDie()).setSelectionStart(start);
    }

    /**
     * Gets the value of "selectionEnd" attribute.
     * @return the selection end.
     */
    public int jsxGet_selectionEnd() {
        return ((HtmlTextInput) getHtmlElementOrDie()).getSelectionEnd();
    }

    /**
     * Sets the value of "selectionEnd" attribute.
     * @param end selection end.
     */
    public void jsxSet_selectionEnd(final int end) {
        ((HtmlTextInput) getHtmlElementOrDie()).setSelectionEnd(end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAttributeName(final String name) {
        if ("maxlength".equals(name.toLowerCase())) {
            return "maxLength".equals(name);
        }

        return super.isAttributeName(name);
    }

    /**
     * Gets the max length.
     * @return the max length.
     */
    public int jsxGet_maxLength() {
        final String attrValue = getHtmlElementOrDie().getAttribute("maxLength");
        return NumberUtils.toInt(attrValue, -1);
    }

    /**
     * Sets the value of "maxLength" attribute.
     * @param length the new value.
     */
    public void jsxSet_maxLength(final int length) {
        getHtmlElementOrDie().setAttribute("maxLength", String.valueOf(length));
    }
    
    /**
     * Sets the selected portion of this input element.
     * @param start the index of the first character to select.
     * @param end the index of the character after the selection.
     */
    public void jsxFunction_setSelectionRange(final int start, final int end) {
        jsxSet_selectionStart(start);
        jsxSet_selectionEnd(end);
    }
}
