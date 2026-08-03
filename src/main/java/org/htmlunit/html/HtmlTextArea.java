/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.io.PrintWriter;
import java.util.Map;

import org.htmlunit.SgmlPage;
import org.htmlunit.html.impl.SelectableTextInput;
import org.htmlunit.html.impl.SelectableTextSelectionDelegate;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.MouseEvent;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.StringUtils;

/**
 * Wrapper for the HTML element "textarea".
 *
 * @author Mike Bowler
 * @author Barnaby Court
 * @author David K. Taylor
 * @author Christian Sell
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Amit Khanna
 * @author Ronald Brill
 * @author Frank Danek
 * @author Lai Quang Duong
 */
public class HtmlTextArea extends HtmlElement implements DisabledElement, SubmittableElement,
                LabelableElement, SelectableTextInput, ValidatableHtmlElement {
    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "textarea";

    private String defaultValue_;

    /**
     * The element's raw value (spec term), decoupled from the DOM child nodes
     * once {@link #isValueDirty_} is {@code true}. Mirrors {@code HtmlInput}'s
     * dirty-value-flag model rather than reading/writing child text nodes directly.
     */
    private String rawValue_;

    /**
     * The dirty value flag (spec term). While {@code false}, the raw value tracks
     * the element's child text content automatically. Once {@code true} (set by
     * the {@code value} setter, or by a user edit/type), child mutations no
     * longer affect the raw value until {@link #reset()} clears the flag again.
     */
    private boolean isValueDirty_;

    private String valueAtFocus_;
    private String customValidity_;

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
    }

    /**
     * Initializes the default value if necessary. We cannot do it in the constructor
     * because the child node variable will not have been initialized yet. Must be called
     * from all methods that use the default value.
     */
    private void initDefaultValue() {
        if (defaultValue_ == null) {
            defaultValue_ = computeValueFromChildText();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(final Event event) {
        if (event instanceof MouseEvent) {
            return true;
        }

        return super.handles(event);
    }

    /**
     * Returns the value that would be displayed in the text area.
     * This is the element's "raw value" (spec term). While the dirty value
     * flag is {@code false}, this is always computed fresh from the current
     * child text content -- deliberately NOT cached and re-synced via
     * mutation hooks, since that approach cannot reliably catch every way the
     * children can change (e.g. a child {@code DomText}'s {@code data} being
     * reassigned directly bypasses any hook on this element). Once the dirty
     * flag becomes {@code true} (via {@link #setText(String)} or typing), the
     * value is held in {@link #rawValue_} and is fully decoupled from the
     * children until {@link #reset()} clears the flag again.
     *
     * @return the text
     */
    @Override
    public final String getText() {
        if (isValueDirty_) {
            return rawValue_;
        }
        return computeValueFromChildText();
    }

    /**
     * Computes what the raw value would be purely from the current child text
     * content -- i.e. the spec's "child text content" used both for the initial/
     * reset raw value and for {@code defaultValue}. Renamed from the old
     * {@code readValue()}.
     *
     * @return the concatenated child text content, with a single leading newline
     *     stripped per the HTML parsing algorithm
     */
    private String computeValueFromChildText() {
        final StringBuilder builder = new StringBuilder();
        for (final DomNode node : getChildren()) {
            if (node instanceof DomText text) {
                builder.append(text.getData());
            }
        }
        // if content starts with new line, it is ignored (=> for the parser?)
        if (builder.length() != 0 && builder.charAt(0) == '\n') {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * Sets the new value of this text area. Per spec, this sets the raw value
     * directly and marks the dirty flag -- it must NOT touch the DOM child
     * nodes at all, and no subsequent child mutation may affect this value
     * again until {@link #reset()}.
     * <p>
     * Note that this acts like 'pasting' the text, but to simulate characters entry
     * you should use {@link #type(String)}.
     * </p>
     *
     * @param newValue the new value
     */
    @Override
    public final void setText(final String newValue) {
        setTextInternal(newValue);

        HtmlInput.executeOnChangeHandlerIfAppropriate(this);
    }

    private void setTextInternal(final String newValue) {
        final String oldValue = getText();

        rawValue_ = newValue;
        isValueDirty_ = true;

        if (!newValue.equals(oldValue)) {
            final int pos = newValue.length();
            setSelectionStart(pos);
            setSelectionEnd(pos);
        }
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
     * Per the spec's reset algorithm for textarea elements: clears the dirty
     * value flag. Once clear, {@link #getText()} automatically recomputes from
     * the CURRENT child text content (not the page's originally-parsed text,
     * and not {@link #defaultValue_} -- those can differ from the live
     * children if the children were mutated while the dirty flag was
     * {@code true}). Deliberately does not call {@link #setText(String)} /
     * fire the onchange handler: a form reset fires a {@code reset} event,
     * not a {@code change} event.
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        final String oldValue = getText();

        isValueDirty_ = false;

        final String newValue = computeValueFromChildText();
        if (!newValue.equals(oldValue)) {
            final int pos = newValue.length();
            setSelectionStart(pos);
            setSelectionEnd(pos);
        }
    }

    /**
     * {@inheritDoc}
     * Per spec, {@code defaultValue} is specified in terms of the element's
     * child text content -- setting it mutates the children (here, via the
     * same node-replacement helper the old {@code setTextInternal()} used).
     * Since {@link #getText()} recomputes directly from the current children
     * whenever the dirty flag is {@code false}, {@code value} is automatically
     * updated as a side effect ONLY while still clean -- exactly matching
     * observed real-browser behavior (replacing the old, less precise "if
     * value still equals old default value" equality check).
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(String defaultValue) {
        initDefaultValue();
        if (defaultValue == null) {
            defaultValue = "";
        }

        replaceChildTextContent(defaultValue);
        defaultValue_ = defaultValue;
    }

    /**
     * Replaces this element's child text content with {@code newText}, reusing
     * an existing text-node child in place where possible rather than always
     * removing and recreating one (avoids unnecessary DOM node identity churn).
     * Used only for mutating the DOM representation (e.g. from
     * {@link #setDefaultValue(String)}) -- NOT for the {@code value}
     * setter, which must not touch the children at all.
     *
     * @param newText the new child text content
     */
    private void replaceChildTextContent(final String newText) {
        DomNode child = getFirstChild();
        if (child == null) {
            appendChild(new DomText(getPage(), newText));
        }
        else if (child instanceof DomText) {
            ((DomText) child).setData(newText);
        }
        else {
            DomNode next = child.getNextSibling();
            while (next != null && !(next instanceof DomText)) {
                child = next;
                next = child.getNextSibling();
            }

            if (next == null) {
                removeChild(child);
                appendChild(new DomText(getPage(), newText));
            }
            else {
                ((DomText) next).setData(newText);
            }
        }
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
     * {@inheritDoc}
     * This implementation is empty; only check boxes and radio buttons
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeDirect(DomElement.NAME_ATTRIBUTE);
    }

    /**
     * Returns the value of the attribute {@code rows}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rows} or an empty string if that attribute isn't defined
     */
    public final String getRowsAttribute() {
        return getAttributeDirect("rows");
    }

    /**
     * Returns the value of the attribute {@code cols}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
    public final String getDisabledAttribute() {
        return getAttributeDirect(ATTRIBUTE_DISABLED);
    }

    /**
     * Returns the value of the attribute {@code readonly}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code readonly} or an empty string if that attribute isn't defined
     */
    public final String getReadOnlyAttribute() {
        return getAttributeDirect(ATTRIBUTE_READONLY);
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex} or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeDirect("tabindex");
    }

    /**
     * Returns the value of the attribute {@code accesskey}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accesskey} or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeDirect("accesskey");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus} or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeDirect("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur} or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeDirect("onblur");
    }

    /**
     * Returns the value of the attribute {@code onselect}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onselect} or an empty string if that attribute isn't defined
     */
    public final String getOnSelectAttribute() {
        return getAttributeDirect("onselect");
    }

    /**
     * Returns the value of the attribute {@code onchange}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * {@inheritDoc}
     */
    @Override
    protected boolean printXml(final String indent, final boolean indentBefore, final PrintWriter printWriter) {
        printWriter.print(indent + "<");
        printOpeningTagContentAsXml(printWriter);

        printWriter.print(">");
        printWriter.print(StringUtils.escapeXml(getText()));
        printWriter.print("</textarea>");
        return true;
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
            setAttribute(ATTRIBUTE_READONLY, "");
        }
        else {
            removeAttribute(ATTRIBUTE_READONLY);
        }
    }

    /**
     * Returns {@code true} if this element is read only.
     * @return {@code true} if this element is read only
     */
    public boolean isReadOnly() {
        return hasAttribute(ATTRIBUTE_READONLY);
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

        return newnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willValidate() {
        return !isDisabled() && !isReadOnly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCustomValidity() {
        return customValidity_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCustomValidity(final String message) {
        customValidity_ = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return isValidValidityState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCustomErrorValidityState() {
        return !StringUtils.isEmptyOrNull(customValidity_);
    }

    @Override
    public boolean isValidValidityState() {
        return !isCustomErrorValidityState()
                && !isValueMissingValidityState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValueMissingValidityState() {
        return ATTRIBUTE_NOT_DEFINED != getAttributeDirect(ATTRIBUTE_REQUIRED)
                && getText().isEmpty();
    }
}
