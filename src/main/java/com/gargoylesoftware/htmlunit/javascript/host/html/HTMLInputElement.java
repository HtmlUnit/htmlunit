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
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALIGN_FOR_INPUT_IGNORES_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLICK_CHECKBOX_TRIGGERS_NO_CHANGE_EVENT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.lang3.math.NumberUtils;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.InputElementFactory;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.FormField;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;

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
 * @author Ronald Brill
 */
@JsxClass(domClasses = HtmlInput.class)
public class HTMLInputElement extends FormField {

    /**
     * Sets the value of the attribute "type".
     * Note: this replace the DOM node with a new one.
     * @param newType the new type to set
     */
    @JsxSetter
    public void setType(final String newType) {
        HtmlInput input = getHtmlInputOrDie();

        if (!input.getTypeAttribute().equalsIgnoreCase(newType)) {
            final AttributesImpl attributes = readAttributes(input);
            final int index = attributes.getIndex("type");
            attributes.setValue(index, newType);

            final HtmlInput newInput = (HtmlInput) InputElementFactory.instance
                .createElement(input.getPage(), "input", attributes);

            if (input.wasCreatedByJavascript()) {
                newInput.markAsCreatedByJavascript();
            }

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
    @JsxSetter
    public void setChecked(final boolean checked) {
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
    @JsxGetter
    public boolean getChecked() {
        return ((HtmlInput) getDomNodeOrDie()).isChecked();
    }

    /**
     * Select this element.
     */
    @JsxFunction
    public void select() {
        final HtmlInput input = getHtmlInputOrDie();
        if (input instanceof HtmlTextInput) {
            ((HtmlTextInput) getDomNodeOrDie()).select();
        }
        // currently nothing for other input types
    }

    /**
     * Uses {@link #setType(String)} if attribute's name is type to
     * replace DOM node as well as long as we have subclasses of {@link HtmlInput}.
     * {@inheritDoc}
     */
    @Override
    public void setAttribute(final String name, final String value) {
        if ("type".equals(name)) {
            setType(value);
        }
        else {
            super.setAttribute(name, value);
        }
    }

    /**
     * Returns the input's default value, used if the containing form gets reset.
     * @return the input's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getDefaultValue() {
        return ((HtmlInput) getDomNodeOrDie()).getDefaultValue();
    }

    /**
     * Sets the input's default value, used if the containing form gets reset.
     * @param defaultValue the input's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setDefaultValue(final String defaultValue) {
        ((HtmlInput) getDomNodeOrDie()).setDefaultValue(defaultValue);
    }

    /**
     * Returns the input's default checked value, used if the containing form gets reset.
     * @return the input's default checked value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533715.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public boolean getDefaultChecked() {
        return ((HtmlInput) getDomNodeOrDie()).isDefaultChecked();
    }

    /**
     * Sets the input's default checked value, used if the containing form gets reset.
     * @param defaultChecked the input's default checked value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533715.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setDefaultChecked(final boolean defaultChecked) {
        ((HtmlInput) getDomNodeOrDie()).setDefaultChecked(defaultChecked);
    }

    /**
     * Gets the value of "textLength" attribute.
     * @return the text length
     */
    @JsxGetter(@WebBrowser(FF))
    public int getTextLength() {
        return getValue().length();
    }

    /**
     * Gets the value of "selectionStart" attribute.
     * @return the selection start
     */
    @JsxGetter(@WebBrowser(FF))
    public int getSelectionStart() {
        return ((SelectableTextInput) getDomNodeOrDie()).getSelectionStart();
    }

    /**
     * Sets the value of "selectionStart" attribute.
     * @param start selection start
     */
    @JsxSetter(@WebBrowser(FF))
    public void setSelectionStart(final int start) {
        ((SelectableTextInput) getDomNodeOrDie()).setSelectionStart(start);
    }

    /**
     * Gets the value of "selectionEnd" attribute.
     * @return the selection end
     */
    @JsxGetter(@WebBrowser(FF))
    public int getSelectionEnd() {
        return ((SelectableTextInput) getDomNodeOrDie()).getSelectionEnd();
    }

    /**
     * Sets the value of "selectionEnd" attribute.
     * @param end selection end
     */
    @JsxSetter(@WebBrowser(FF))
    public void setSelectionEnd(final int end) {
        ((SelectableTextInput) getDomNodeOrDie()).setSelectionEnd(end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAttributeName(final String name) {
        final String nameLC = name.toLowerCase(Locale.ENGLISH);
        if ("maxlength".equals(nameLC)) {
            return "maxLength".equals(name);
        }

        if ("readOnly".equals(nameLC)) {
            return "readOnly".equals(name);
        }
        return super.isAttributeName(name);
    }

    /**
     * Gets the max length.
     * @return the max length
     */
    @JsxGetter
    public int getMaxLength() {
        final String attrValue = getDomNodeOrDie().getAttribute("maxLength");
        return NumberUtils.toInt(attrValue, -1);
    }

    /**
     * Sets the value of "maxLength" attribute.
     * @param length the new value
     */
    @JsxSetter
    public void setMaxLength(final int length) {
        getDomNodeOrDie().setAttribute("maxLength", Integer.toString(length));
    }

    /**
     * Gets the value of "readOnly" attribute.
     * @return the readOnly attribute
     */
    @JsxGetter
    public boolean getReadOnly() {
        return ((HtmlInput) getDomNodeOrDie()).isReadOnly();
    }

    /**
     * Sets the value of "readOnly" attribute.
     * @param readOnly the new value
     */
    @JsxSetter
    public void setReadOnly(final boolean readOnly) {
        ((HtmlInput) getDomNodeOrDie()).setReadOnly(readOnly);
    }

    /**
     * Sets the selected portion of this input element.
     * @param start the index of the first character to select
     * @param end the index of the character after the selection
     */
    @JsxFunction(@WebBrowser(FF))
    public void setSelectionRange(final int start, final int end) {
        setSelectionStart(start);
        setSelectionEnd(end);
    }

    /**
     * Returns the value of the "alt" property.
     * @return the value of the "alt" property
     */
    @JsxGetter
    public String getAlt() {
        final String alt = getDomNodeOrDie().getAttribute("alt");
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    @JsxSetter
    public void setAlt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    @JsxGetter
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    @JsxSetter
    public void setAlign(final String align) {
        final boolean ignoreIfNoError = getBrowserVersion().hasFeature(JS_ALIGN_FOR_INPUT_IGNORES_VALUES);
        setAlign(align, ignoreIfNoError);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter(@WebBrowser(FF))
    public String getAccessKey() {
        return super.getAccessKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter(@WebBrowser(FF))
    public void setAccessKey(final String accessKey) {
        super.setAccessKey(accessKey);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxFunction(@WebBrowser(FF))
    public void click() throws IOException {
        final HtmlInput domNode = (HtmlInput) getDomNodeOrDie();
        final boolean originalState = domNode.isChecked();
        final Event event = new MouseEvent(domNode, MouseEvent.TYPE_CLICK, false,
                false, false, MouseEvent.BUTTON_LEFT);
        domNode.click(event);

        final boolean newState = domNode.isChecked();

        if (originalState != newState
            && (domNode instanceof HtmlRadioButtonInput
                    || (domNode instanceof HtmlCheckBoxInput
                            && !getBrowserVersion().hasFeature(JS_CLICK_CHECKBOX_TRIGGERS_NO_CHANGE_EVENT)))) {
            domNode.fireEvent(Event.TYPE_CHANGE);
        }
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public String getType() {
        return super.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
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
