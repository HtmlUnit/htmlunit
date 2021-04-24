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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextSelectionDelegate;

/**
 * Wrapper for the HTML element "input".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 */
public class HtmlPasswordInput extends HtmlInput implements SelectableTextInput, LabelableElement {

    private SelectableTextSelectionDelegate selectionDelegate_ = new SelectableTextSelectionDelegate(this);
    private DoTypeProcessor doTypeProcessor_ = new DoTypeProcessor(this);

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlPasswordInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isSubmittableByEnter() {
        return true;
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
    public String getText() {
        return getValueAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(final String text) {
        setValueAttribute(text);
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
    protected void doType(final char c, final boolean lastType) {
        doTypeProcessor_.doType(getValueAttribute(), selectionDelegate_, c, this, lastType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final int keyCode, final boolean lastType) {
        doTypeProcessor_.doType(getValueAttribute(), selectionDelegate_, keyCode, this, lastType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void typeDone(final String newValue, final boolean notifyAttributeChangeListeners) {
        if (newValue.length() <= getMaxLength()) {
            setAttributeNS(null, "value", newValue, notifyAttributeChangeListeners, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);
        if ("value".equals(qualifiedName)) {
            final SgmlPage page = getPage();
            if (page != null && page.isHtmlPage()) {
                int pos = 0;
                if (!hasFeature(JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START)) {
                    pos = attributeValue.length();
                }
                setSelectionStart(pos);
                setSelectionEnd(pos);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        final boolean modifyValue = getValueAttribute().equals(getDefaultValue());
        setDefaultValue(defaultValue, modifyValue);
    }

    /**
     * {@inheritDoc}
     * @see HtmlInput#reset()
     */
    @Override
    public void reset() {
        super.reset();
        setSelectionEnd(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final HtmlPasswordInput newnode = (HtmlPasswordInput) super.cloneNode(deep);
        newnode.selectionDelegate_ = new SelectableTextSelectionDelegate(newnode);
        newnode.doTypeProcessor_ = new DoTypeProcessor(newnode);

        return newnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isPatternSupported() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isMinMaxLengthSupported() {
        return true;
    }
}
