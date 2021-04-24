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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DISPLAY_BLOCK;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_MOUSE_ON_DISABLED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLTEXTAREA_SET_DEFAULT_VALUE_UPDATES_VALUE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLTEXTAREA_USE_ALL_TEXT_CHILDREN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextSelectionDelegate;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "textarea".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:BarnabyCourt@users.sourceforge.net">Barnaby Court</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Amit Khanna
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlTextArea extends HtmlElement implements DisabledElement, SubmittableElement,
                LabelableElement, SelectableTextInput,
    FormFieldWithNameHistory {
    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "textarea";

    private String defaultValue_;
    private String valueAtFocus_;
    private final String originalName_;
    private Collection<String> newNames_ = Collections.emptySet();

    private SelectableTextSelectionDelegate selectionDelegate_ = new SelectableTextSelectionDelegate(this);
    private DoTypeProcessor doTypeProcessor_ = new DoTypeProcessor(this);

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlTextArea(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
        originalName_ = getNameAttribute();
    }

    /**
     * Initializes the default value if necessary. We cannot do it in the constructor
     * because the child node variable will not have been initialized yet. Must be called
     * from all methods that use the default value.
     */
    private void initDefaultValue() {
        if (defaultValue_ == null) {
            defaultValue_ = readValue();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(final Event event) {
        if (event instanceof MouseEvent && hasFeature(EVENT_MOUSE_ON_DISABLED)) {
            return true;
        }

        return super.handles(event);
    }

    /**
     * Returns the value that would be displayed in the text area.
     *
     * @return the text
     */
    @Override
    public final String getText() {
        if (hasFeature(HTMLTEXTAREA_USE_ALL_TEXT_CHILDREN)) {
            return readValueIE();
        }
        return readValue();
    }

    private String readValue() {
        final StringBuilder builder = new StringBuilder();
        for (final DomNode node : getChildren()) {
            if (node instanceof DomText) {
                builder.append(((DomText) node).getData());
            }
        }
        // if content starts with new line, it is ignored (=> for the parser?)
        if (builder.length() != 0 && builder.charAt(0) == '\n') {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    private String readValueIE() {
        final StringBuilder builder = new StringBuilder();
        for (final DomNode node : getDescendants()) {
            if (node instanceof DomText) {
                builder.append(((DomText) node).getData());
            }
        }
        // if content starts with new line, it is ignored (=> for the parser?)
        if (builder.length() != 0 && builder.charAt(0) == '\n') {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * Sets the new value of this text area.
     *
     * Note that this acts like 'pasting' the text, but to simulate characters entry
     * you should use {@link #type(String)}.
     *
     * @param newValue the new value
     */
    @Override
    public final void setText(final String newValue) {
        setTextInternal(newValue);

        HtmlInput.executeOnChangeHandlerIfAppropriate(this);
    }

    private void setTextInternal(final String newValue) {
        initDefaultValue();
        DomNode child = getFirstChild();
        if (child == null) {
            final DomText newChild = new DomText(getPage(), newValue);
            appendChild(newChild);
        }
        else {
            if (hasFeature(HTMLTEXTAREA_USE_ALL_TEXT_CHILDREN)) {
                removeAllChildren();
                final DomText newChild = new DomText(getPage(), newValue);
                appendChild(newChild);
            }
            else {
                DomNode next = child.getNextSibling();
                while (next != null && !(next instanceof DomText)) {
                    child = next;
                    next = child.getNextSibling();
                }

                if (next == null) {
                    removeChild(child);
                    final DomText newChild = new DomText(getPage(), newValue);
                    appendChild(newChild);
                }
                else {
                    ((DomText) next).setData(newValue);
                }
            }
        }

        int pos = 0;
        if (!hasFeature(JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START)) {
            pos = newValue.length();
        }
        setSelectionStart(pos);
        setSelectionEnd(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitNameValuePairs() {
        String text = getText();
        text = text.replace("\r\n", "\n").replace("\n", "\r\n");

        return new NameValuePair[]{new NameValuePair(getNameAttribute(), text)};
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        initDefaultValue();
        setText(defaultValue_);
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(String defaultValue) {
        initDefaultValue();
        if (defaultValue == null) {
            defaultValue = "";
        }

        // for FF, if value is still default value, change value too
        if (hasFeature(HTMLTEXTAREA_SET_DEFAULT_VALUE_UPDATES_VALUE)
                && getText().equals(getDefaultValue())) {
            setTextInternal(defaultValue);
        }
        defaultValue_ = defaultValue;
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#getDefaultValue()
     */
    @Override
    public String getDefaultValue() {
        initDefaultValue();
        return defaultValue_;
    }

    /**
     * {@inheritDoc} This implementation is empty; only checkboxes and radio buttons
     * really care what the default checked value is.
     * @see SubmittableElement#setDefaultChecked(boolean)
     * @see HtmlRadioButtonInput#setDefaultChecked(boolean)
     * @see HtmlCheckBoxInput#setDefaultChecked(boolean)
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc} This implementation returns {@code false}; only checkboxes and
     * radio buttons really care what the default checked value is.
     * @see SubmittableElement#isDefaultChecked()
     * @see HtmlRadioButtonInput#isDefaultChecked()
     * @see HtmlCheckBoxInput#isDefaultChecked()
     */
    @Override
    public boolean isDefaultChecked() {
        return false;
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeDirect("name");
    }

    /**
     * Returns the value of the attribute {@code rows}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rows} or an empty string if that attribute isn't defined
     */
    public final String getRowsAttribute() {
        return getAttributeDirect("rows");
    }

    /**
     * Returns the value of the attribute {@code cols}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code cols} or an empty string if that attribute isn't defined
     */
    public final String getColumnsAttribute() {
        return getAttributeDirect("cols");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isDisabled() {
        return hasAttribute("disabled");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttributeDirect("disabled");
    }

    /**
     * Returns the value of the attribute {@code readonly}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code readonly} or an empty string if that attribute isn't defined
     */
    public final String getReadOnlyAttribute() {
        return getAttributeDirect("readonly");
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex} or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeDirect("tabindex");
    }

    /**
     * Returns the value of the attribute {@code accesskey}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accesskey} or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeDirect("accesskey");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus} or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeDirect("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur} or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeDirect("onblur");
    }

    /**
     * Returns the value of the attribute {@code onselect}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onselect} or an empty string if that attribute isn't defined
     */
    public final String getOnSelectAttribute() {
        return getAttributeDirect("onselect");
    }

    /**
     * Returns the value of the attribute {@code onchange}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onchange} or an empty string if that attribute isn't defined
     */
    public final String getOnChangeAttribute() {
        return getAttributeDirect("onchange");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void select() {
        selectionDelegate_.select();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelectedText() {
        return selectionDelegate_.getSelectedText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSelectionStart() {
        return selectionDelegate_.getSelectionStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectionStart(final int selectionStart) {
        selectionDelegate_.setSelectionStart(selectionStart);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSelectionEnd() {
        return selectionDelegate_.getSelectionEnd();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectionEnd(final int selectionEnd) {
        selectionDelegate_.setSelectionEnd(selectionEnd);
    }

    /**
     * Recursively write the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        printWriter.print(indent + "<");
        printOpeningTagContentAsXml(printWriter);

        printWriter.print(">");
        printWriter.print(StringEscapeUtils.escapeXml10(getText()));
        printWriter.print("</textarea>");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final char c, final boolean lastType) {
        doTypeProcessor_.doType(getText(), selectionDelegate_, c, this, lastType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final int keyCode, final boolean lastType) {
        doTypeProcessor_.doType(getText(), selectionDelegate_, keyCode, this, lastType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void typeDone(final String newValue, final boolean notifyAttributeChangeListeners) {
        setTextInternal(newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean acceptChar(final char c) {
        return super.acceptChar(c) || c == '\n' || c == '\r';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focus() {
        super.focus();
        valueAtFocus_ = getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFocus() {
        super.removeFocus();
        if (valueAtFocus_ != null && !valueAtFocus_.equals(getText())) {
            HtmlInput.executeOnChangeHandlerIfAppropriate(this);
        }
        valueAtFocus_ = null;
    }

    /**
     * Sets the {@code readOnly} attribute.
     *
     * @param isReadOnly {@code true} if this element is read only
     */
    public void setReadOnly(final boolean isReadOnly) {
        if (isReadOnly) {
            setAttribute("readOnly", "readOnly");
        }
        else {
            removeAttribute("readOnly");
        }
    }

    /**
     * Returns {@code true} if this element is read only.
     * @return {@code true} if this element is read only
     */
    public boolean isReadOnly() {
        return hasAttribute("readOnly");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        if ("name".equals(qualifiedName)) {
            if (newNames_.isEmpty()) {
                newNames_ = new HashSet<>();
            }
            newNames_.add(attributeValue);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOriginalName() {
        return originalName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getNewNames() {
        return newNames_;
    }

    /**
     * {@inheritDoc}
     * @return {@code true} to make generated XML readable as HTML
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        if (hasFeature(CSS_DISPLAY_BLOCK)) {
            return DisplayStyle.INLINE;
        }
        return DisplayStyle.INLINE_BLOCK;
    }

    /**
     * Returns the value of the {@code placeholder} attribute.
     *
     * @return the value of the {@code placeholder} attribute
     */
    public String getPlaceholder() {
        return getAttributeDirect("placeholder");
    }

    /**
     * Sets the {@code placeholder} attribute.
     *
     * @param placeholder the {@code placeholder} attribute
     */
    public void setPlaceholder(final String placeholder) {
        setAttribute("placeholder", placeholder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRequiredSupported() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final HtmlTextArea newnode = (HtmlTextArea) super.cloneNode(deep);
        newnode.selectionDelegate_ = new SelectableTextSelectionDelegate(newnode);
        newnode.doTypeProcessor_ = new DoTypeProcessor(newnode);
        newnode.newNames_ = new HashSet<>(newNames_);

        return newnode;
    }
}
