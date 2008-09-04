/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;

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
public class HtmlTextArea extends ClickableElement implements DisabledElement, SubmittableElement {

    private static final long serialVersionUID = 4572856255042499634L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "textarea";

    private String defaultValue_;

    private boolean preventDefault_;

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
            final DomText child = (DomText) getFirstChild();
            if (child != null) {
                defaultValue_ = child.getData();
                if (defaultValue_ == null) {
                    defaultValue_ = "";
                }
            }
            else {
                defaultValue_ = "";
            }
        }
    }

    /**
     * Returns the value that would be displayed in the text area.
     *
     * @return the text
     */
    public final String getText() {
        final StringBuilder buffer = new StringBuilder();
        for (final DomNode node : getChildren()) {
            if (node instanceof DomText) {
                buffer.append(((DomText) node).getData());
            }
        }
        return buffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asText() {
        if (isStyleVisible()) {
            return getText();
        }
        return "";
    }

    /**
     * Sets the new value of this text area.
     *
     * @param newValue the new value
     */
    public final void setText(final String newValue) {
        initDefaultValue();
        final DomText child = (DomText) getFirstChild();
        if (child == null) {
            final DomText newChild = new DomText(getPage(), newValue);
            appendChild(newChild);
        }
        else {
            child.setData(newValue);
        }

        HtmlInput.executeOnChangeHandlerIfAppropriate(this);

        setSelectionStart(newValue.length());
        setSelectionEnd(newValue.length());
    }

    /**
     * {@inheritDoc}
     */
    public NameValuePair[] getSubmitKeyValuePairs() {
        return new NameValuePair[]{new NameValuePair(getNameAttribute(), getText())};
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
    public void setDefaultValue(final String defaultValue) {
        initDefaultValue();
        if (defaultValue == null) {
            defaultValue_ = "";
        }
        else {
            defaultValue_ = defaultValue;
        }
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
        return getAttributeValue("name");
    }

    /**
     * Returns the value of the attribute "rows". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rows" or an empty string if that attribute isn't defined
     */
    public final String getRowsAttribute() {
        return getAttributeValue("rows");
    }

    /**
     * Returns the value of the attribute "cols". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "cols" or an empty string if that attribute isn't defined
     */
    public final String getColumnsAttribute() {
        return getAttributeValue("cols");
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isDisabled() {
        return isAttributeDefined("disabled");
    }

    /**
     * {@inheritDoc}
     */
    public final String getDisabledAttribute() {
        return getAttributeValue("disabled");
    }

    /**
     * Returns the value of the attribute "readonly". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "readonly" or an empty string if that attribute isn't defined
     */
    public final String getReadOnlyAttribute() {
        return getAttributeValue("readonly");
    }

    /**
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex" or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey" or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeValue("accesskey");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus" or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur" or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue("onblur");
    }

    /**
     * Returns the value of the attribute "onselect". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onselect" or an empty string if that attribute isn't defined
     */
    public final String getOnSelectAttribute() {
        return getAttributeValue("onselect");
    }

    /**
     * Returns the value of the attribute "onchange". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onchange" or an empty string if that attribute isn't defined
     */
    public final String getOnChangeAttribute() {
        return getAttributeValue("onchange");
    }

    /**
     * Returns the selected text contained in this text area, or <tt>null</tt> if no selection (Firefox only).
     * @return the selected text contained in this text area
     */
    public String getSelectedText() {
        final Range selection = getThisSelection();
        if (selection != null) {
            return getText().substring(selection.getStartOffset(), selection.getEndOffset());
        }
        else {
            return null;
        }
    }

    private Range getThisSelection() {
        if (getPage() instanceof HtmlPage) {
            final Range selection = ((HtmlPage) getPage()).getSelection();
            if (selection.getStartContainer() == this && selection.getEndContainer() == this) {
                return selection;
            }
        }

        return null;
    }

    /**
     * Returns the selected text's start position (Firefox only).
     * @return the start position >= 0
     */
    public int getSelectionStart() {
        final Range selection = getThisSelection();
        if (selection != null) {
            return selection.getStartOffset();
        }
        else {
            return 0;
        }
    }

    /**
     * Sets the selection start to the specified position (Firefox only).
     * @param selectionStart the start position of the text >= 0
     */
    public void setSelectionStart(int selectionStart) {
        if (getPage() instanceof HtmlPage) {
            final HtmlPage page = (HtmlPage) getPage();
            final int length = getText().length();
            selectionStart = Math.max(0, Math.min(selectionStart, length));
            page.getSelection().setStart(this, selectionStart);
            if (page.getSelection().getEndContainer() != this) {
                page.getSelection().setEnd(this, length);
            }
            else if (page.getSelection().getEndOffset() < selectionStart) {
                page.getSelection().setEnd(this, selectionStart);
            }
        }
    }

    /**
     * Returns the selected text's end position (Firefox only).
     * @return the end position >= 0
     */
    public int getSelectionEnd() {
        final Range selection = getThisSelection();
        if (selection != null) {
            return selection.getEndOffset();
        }
        else {
            return 0;
        }
    }

    /**
     * Sets the selection end to the specified position (Firefox only).
     * @param selectionEnd the end position of the text >= 0

     */
    public void setSelectionEnd(int selectionEnd) {
        if (getPage() instanceof HtmlPage) {
            final HtmlPage page = (HtmlPage) getPage();
            final int length = getText().length();
            selectionEnd = Math.min(length, Math.max(selectionEnd, 0));
            page.getSelection().setEnd(this, selectionEnd);
            if (page.getSelection().getStartContainer() != this) {
                page.getSelection().setStart(this, 0);
            }
            else if (page.getSelection().getStartOffset() > selectionEnd) {
                page.getSelection().setStart(this, selectionEnd);
            }
        }
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
    public Page type(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if (isDisabled()) {
            return getPage();
        }
        preventDefault_ = false;
        return super.type(c, shiftKey, ctrlKey, altKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        if (preventDefault_) {
            return;
        }
        final String text = getText();
        if (c == '\b') {
            if (text.length() > 0) {
                setText(text.substring(0, text.length() - 1));
            }
        }
        else if ((c == ' ' || c == '\n' || c == '\r' || !Character.isWhitespace(c))) {
            setText(text + c);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preventDefault() {
        preventDefault_ = true;
    }

    /**
     * Select all the text in this input.
     */
    public void select() {
        focus();
        setSelectionStart(0);
        setSelectionEnd(getText().length());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focus() {
        super.focus();
        if (getPage() instanceof HtmlPage) {
            final Range selection = ((HtmlPage) getPage()).getSelection();
            selection.setStart(this, 0);
            selection.setEnd(this, getText().length());
        }
    }
}
