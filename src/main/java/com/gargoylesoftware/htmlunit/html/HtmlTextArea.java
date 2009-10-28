/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.PrintWriter;
import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectionDelegate;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "textarea".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:BarnabyCourt@users.sourceforge.net">Barnaby Court</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 */
public class HtmlTextArea extends HtmlElement implements DisabledElement, SubmittableElement, SelectableTextInput {

    private static final long serialVersionUID = 4572856255042499634L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "textarea";

    private String defaultValue_;
    private String valueAtFocus_;

    private final SelectionDelegate selectionDelegate_ = new SelectionDelegate(this);

    private final DoTypeProcessor doTypeProcessor_ = new DoTypeProcessor() {
        private static final long serialVersionUID = 2906652041039202266L;
        @Override
        void typeDone(final String newValue, final int newCursorPosition) {
            setTextInternal(newValue);
            setSelectionStart(newCursorPosition);
            setSelectionEnd(newCursorPosition);
        }
        @Override
        protected boolean acceptChar(final char c) {
            return super.acceptChar(c) || c == '\n' || c == '\r';
        }
    };

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlTextArea(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
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
     * Returns the value that would be displayed in the text area.
     *
     * @return the text
     */
    public final String getText() {
        return readValue();
    }

    private String readValue() {
        final StringBuilder buffer = new StringBuilder();
        for (final DomNode node : getChildren()) {
            if (node instanceof DomText) {
                buffer.append(((DomText) node).getData());
            }
        }
        // if content starts with new line, it is ignored (=> for the parser?)
        if (buffer.length() > 0 && buffer.charAt(0) == '\n') {
            buffer.deleteCharAt(0);
        }
        return buffer.toString();
    }

    /**
     * Sets the new value of this text area.
     *
     * Note that this acts like 'pasting' the text, but to simulate characters entry
     * you should use {@link #type(String)}.
     *
     * @param newValue the new value
     */
    public final void setText(final String newValue) {
        initDefaultValue();
        setTextInternal(newValue);

        HtmlInput.executeOnChangeHandlerIfAppropriate(this);
    }

    private void setTextInternal(final String newValue) {
        initDefaultValue();
        final DomText child = (DomText) getFirstChild();
        if (child == null) {
            final DomText newChild = new DomText(getPage(), newValue);
            appendChild(newChild);
        }
        else {
            child.setData(newValue);
        }

        setSelectionStart(newValue.length());
        setSelectionEnd(newValue.length());
    }

    /**
     * {@inheritDoc}
     */
    public NameValuePair[] getSubmitKeyValuePairs() {
        String text = getText();
        text = text.replace("\r\n", "\n").replace("\n", "\r\n");

        return new NameValuePair[]{new NameValuePair(getNameAttribute(), text)};
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    public void reset() {
        initDefaultValue();
        setText(defaultValue_);
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultValue(String)
     */
    public void setDefaultValue(String defaultValue) {
        initDefaultValue();
        if (defaultValue == null) {
            defaultValue = "";
        }

        // for FF, if value is still default value, change value too
        if (getPage().getWebClient().getBrowserVersion().isFirefox() && getText().equals(getDefaultValue())) {
            setTextInternal(defaultValue);
        }
        defaultValue_ = defaultValue;
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#getDefaultValue()
     */
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
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc} This implementation returns <tt>false</tt>; only checkboxes and
     * radio buttons really care what the default checked value is.
     * @see SubmittableElement#isDefaultChecked()
     * @see HtmlRadioButtonInput#isDefaultChecked()
     * @see HtmlCheckBoxInput#isDefaultChecked()
     */
    public boolean isDefaultChecked() {
        return false;
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute "rows". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rows" or an empty string if that attribute isn't defined
     */
    public final String getRowsAttribute() {
        return getAttribute("rows");
    }

    /**
     * Returns the value of the attribute "cols". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "cols" or an empty string if that attribute isn't defined
     */
    public final String getColumnsAttribute() {
        return getAttribute("cols");
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isDisabled() {
        return hasAttribute("disabled");
    }

    /**
     * {@inheritDoc}
     */
    public final String getDisabledAttribute() {
        return getAttribute("disabled");
    }

    /**
     * Returns the value of the attribute "readonly". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "readonly" or an empty string if that attribute isn't defined
     */
    public final String getReadOnlyAttribute() {
        return getAttribute("readonly");
    }

    /**
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex" or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttribute("tabindex");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey" or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttribute("accesskey");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus" or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttribute("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur" or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttribute("onblur");
    }

    /**
     * Returns the value of the attribute "onselect". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onselect" or an empty string if that attribute isn't defined
     */
    public final String getOnSelectAttribute() {
        return getAttribute("onselect");
    }

    /**
     * Returns the value of the attribute "onchange". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onchange" or an empty string if that attribute isn't defined
     */
    public final String getOnChangeAttribute() {
        return getAttribute("onchange");
    }

    /**
     * {@inheritDoc}
     */
    public void select() {
        selectionDelegate_.select();
    }

    /**
     * {@inheritDoc}
     */
    public String getSelectedText() {
        return selectionDelegate_.getSelectedText();
    }

    /**
     * {@inheritDoc}
     */
    public int getSelectionStart() {
        return selectionDelegate_.getSelectionStart();
    }

    /**
     * {@inheritDoc}
     */
    public void setSelectionStart(final int selectionStart) {
        selectionDelegate_.setSelectionStart(selectionStart);
    }

    /**
     * {@inheritDoc}
     */
    public int getSelectionEnd() {
        return selectionDelegate_.getSelectionEnd();
    }

    /**
     * {@inheritDoc}
     */
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
        printWriter.print(getText());
        printWriter.print(indent + "</textarea>");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        doTypeProcessor_.doType(getText(), getSelectionStart(), getSelectionEnd(),
            c, shiftKey, ctrlKey, altKey);
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
    void removeFocus() {
        super.removeFocus();
        if (!valueAtFocus_.equals(getText())) {
            HtmlInput.executeOnChangeHandlerIfAppropriate(this);
        }
        valueAtFocus_ = null;
    }

    /**
     * Sets the "readOnly" attribute.
     *
     * @param isReadOnly <tt>true</tt> if this element is read only
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
     * Returns <tt>true</tt> if this element is read only.
     * @return <tt>true</tt> if this element is read only
     */
    public boolean isReadOnly() {
        return hasAttribute("readOnly");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new HtmlTextArea(getNamespaceURI(), getQualifiedName(), getPage(), getAttributesMap());
    }
}
