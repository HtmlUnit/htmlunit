/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DISPLAY_BLOCK2;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCLICK_FOR_SELECT_ONLY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONMOUSEDOWN_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_DOWN_FOR_SELECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONMOUSEDOWN_NOT_FOR_SELECT_OPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONMOUSEOVER_FOR_DISABLED_OPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONMOUSEOVER_NEVER_FOR_SELECT_OPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONMOUSEUP_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_UP_FOR_SELECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONMOUSEUP_NOT_FOR_SELECT_OPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLOPTION_PREVENT_DISABLED;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.serializer.HtmlSerializerNormalizedText;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;

/**
 * Wrapper for the HTML element "option".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlOption extends HtmlElement implements DisabledElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "option";

    private boolean selected_;

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlOption(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
        reset();
    }

    /**
     * Returns {@code true} if this option is currently selected.
     * @return {@code true} if this option is currently selected
     */
    public boolean isSelected() {
        return selected_;
    }

    /**
     * Sets the selected state of this option. This will possibly also change the
     * selected properties of sibling option elements.
     *
     * @param selected true if this option should be selected
     * @return the page that occupies this window after this change is made (may or
     *         may not be the same as the original page)
     */
    public Page setSelected(final boolean selected) {
        setSelected(selected, true, false, false, false);
        return getPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the selected state of this option. This will possibly also change the
     * selected properties of sibling option elements.
     *
     * @param selected true if this option should be selected
     */
    public void setSelectedFromJavaScript(final boolean selected) {
        setSelected(selected, false, false, true, false);
    }

    /**
     * Sets the selected state of this option. This will possibly also change the
     * selected properties of sibling option elements.
     *
     * @param selected true if this option should be selected
     * @param invokeOnFocus whether to set focus or not.
     * @param isClick is mouse clicked
     * @param shiftKey {@code true} if SHIFT is pressed
     * @param ctrlKey {@code true} if CTRL is pressed
     */
    private void setSelected(final boolean selected, final boolean invokeOnFocus, final boolean isClick,
            final boolean shiftKey, final boolean ctrlKey) {
        if (selected == isSelected()) {
            return;
        }
        final HtmlSelect select = getEnclosingSelect();
        if (select != null) {
            select.setSelectedAttribute(this, selected, invokeOnFocus, shiftKey, ctrlKey, isClick);
            return;
        }
        // for instance from JS for an option created by document.createElement('option')
        // and not yet added to a select
        setSelectedInternal(selected);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertBefore(final DomNode newNode) {
        super.insertBefore(newNode);
        if (newNode instanceof HtmlOption) {
            final HtmlOption option = (HtmlOption) newNode;
            if (option.isSelected()) {
                getEnclosingSelect().setSelectedAttribute(option, true);
            }
        }
    }

    /**
     * Gets the enclosing select of this option.
     * @return {@code null} if no select is found (for instance malformed HTML)
     */
    public HtmlSelect getEnclosingSelect() {
        return (HtmlSelect) getEnclosingElement(HtmlSelect.TAG_NAME);
    }

    /**
     * Resets the option to its original selected state.
     */
    public void reset() {
        setSelectedInternal(hasAttribute("selected"));
    }

    /**
     * Returns the value of the attribute {@code selected}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code selected}
     * or an empty string if that attribute isn't defined.
     */
    public final String getSelectedAttribute() {
        return getAttributeDirect("selected");
    }

    /**
     * Returns whether this Option is selected by default.
     * That is whether the "selected"
     * attribute exists when the Option is constructed. This also determines
     * the value of getSelectedAttribute() after a reset() on the form.
     * @return whether the option is selected by default
     */
    public final boolean isDefaultSelected() {
        return hasAttribute("selected");
    }

    /**
     * Returns {@code true} if the disabled attribute is set for this element. Note that this
     * method always returns {@code false} when emulating IE, because IE does not allow individual
     * options to be disabled.
     *
     * @return {@code true} if the disabled attribute is set for this element (always {@code false}
     *         when emulating IE)
     */
    @Override
    public final boolean isDisabled() {
        if (hasFeature(HTMLOPTION_PREVENT_DISABLED)) {
            return false;
        }
        return hasAttribute(ATTRIBUTE_DISABLED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttributeDirect(ATTRIBUTE_DISABLED);
    }

    /**
     * Returns the value of the attribute {@code label}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code label} or an empty string if that attribute isn't defined
     */
    public final String getLabelAttribute() {
        return getAttributeDirect("label");
    }

    /**
     * Sets the value of the attribute {@code label}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @param newLabel the value of the attribute {@code label}
     */
    public final void setLabelAttribute(final String newLabel) {
        setAttribute("label", newLabel);
    }

    /**
     * Returns the value of the attribute {@code value}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-value-OPTION">
     * initial value if value attribute is not set</a>
     * @return the value of the attribute {@code value}
     */
    public final String getValueAttribute() {
        String value = getAttributeDirect("value");
        if (ATTRIBUTE_NOT_DEFINED == value) {
            value = getText();
        }
        return value;
    }

    /**
     * Sets the value of the attribute {@code value}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @param newValue the value of the attribute {@code value}
     */
    public final void setValueAttribute(final String newValue) {
        setAttribute("value", newValue);
    }

    /**
     * Selects the option if it's not already selected.
     * {@inheritDoc}
     */
    @Override
    public Page mouseDown(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        Page page = null;
        if (hasFeature(EVENT_ONMOUSEDOWN_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_DOWN_FOR_SELECT)) {
            page = getEnclosingSelect().mouseDown(shiftKey, ctrlKey, altKey, button);
        }
        if (hasFeature(EVENT_ONMOUSEDOWN_NOT_FOR_SELECT_OPTION)) {
            return page;
        }
        return super.mouseDown(shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Selects the option if it's not already selected.
     * {@inheritDoc}
     */
    @Override
    public Page mouseUp(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        Page page = null;
        if (hasFeature(EVENT_ONMOUSEUP_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_UP_FOR_SELECT)) {
            page = getEnclosingSelect().mouseUp(shiftKey, ctrlKey, altKey, button);
        }
        if (hasFeature(EVENT_ONMOUSEUP_NOT_FOR_SELECT_OPTION)) {
            return page;
        }
        return super.mouseUp(shiftKey, ctrlKey, altKey, button);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final Event event,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey,
            final boolean ignoreVisibility) throws IOException {
        if (hasFeature(EVENT_ONCLICK_FOR_SELECT_ONLY)) {
            final SgmlPage page = getPage();

            if (isDisabled()) {
                return (P) page;
            }

            if (isStateUpdateFirst()) {
                doClickStateUpdate(event.isShiftKey(), event.isCtrlKey());
            }

            return getEnclosingSelect().click(event, shiftKey, ctrlKey, altKey, ignoreVisibility);
        }
        return super.click(event, shiftKey, ctrlKey, altKey, ignoreVisibility);
    }

    /**
     * Selects the option if it's not already selected.
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        boolean changed = false;
        if (!isSelected()) {
            setSelected(true, true, true, shiftKey, ctrlKey);
            changed = true;
        }
        else if (getEnclosingSelect().isMultipleSelectEnabled()) {
            if (ctrlKey) {
                setSelected(false, true, true, shiftKey, ctrlKey);
                changed = true;
            }
            else {
                getEnclosingSelect().setOnlySelected(this, true);
            }
        }
        super.doClickStateUpdate(shiftKey, ctrlKey);
        return changed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DomNode getEventTargetElement() {
        if (hasFeature(EVENT_ONCLICK_FOR_SELECT_ONLY)) {
            final HtmlSelect select = getEnclosingSelect();
            if (select != null) {
                return select;
            }
        }
        return super.getEventTargetElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isStateUpdateFirst() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void printOpeningTagContentAsXml(final PrintWriter printWriter) {
        super.printOpeningTagContentAsXml(printWriter);
        if (selected_ && getAttributeDirect("selected") == ATTRIBUTE_NOT_DEFINED) {
            printWriter.print(" selected=\"selected\"");
        }
    }

    /**
     * For internal use only.
     * Sets/remove the selected attribute to reflect the select state
     * @param selected the selected status
     */
    void setSelectedInternal(final boolean selected) {
        selected_ = selected;
    }

    /**
     * Sets the text for this HtmlOption.
     * @param text the text
     */
    public void setText(final String text) {
        if (text == null || text.isEmpty()) {
            removeAllChildren();
        }
        else {
            final DomNode child = getFirstChild();
            if (child == null) {
                appendChild(new DomText(getPage(), text));
            }
            else {
                child.setNodeValue(text);
            }
        }
    }

    /**
     * Gets the text.
     * @return the text of this option.
     */
    public String getText() {
        final HtmlSerializerNormalizedText ser = new HtmlSerializerNormalizedText();
        ser.setIgnoreMaskedElements(false);
        return ser.asText(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page mouseOver(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        final SgmlPage page = getPage();
        if (page.getWebClient().getBrowserVersion().hasFeature(EVENT_ONMOUSEOVER_NEVER_FOR_SELECT_OPTION)) {
            return page;
        }

        if (hasFeature(EVENT_ONMOUSEOVER_FOR_DISABLED_OPTION) && isDisabled()) {
            getEnclosingSelect().mouseOver(shiftKey, ctrlKey, altKey, button);
        }
        return super.mouseOver(shiftKey, ctrlKey, altKey, button);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        if (hasFeature(CSS_DISPLAY_BLOCK2)) {
            return DisplayStyle.BLOCK;
        }
        return DisplayStyle.INLINE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(final Event event) {
        if (MouseEvent.TYPE_MOUSE_OVER.equals(event.getType())
                && getPage().getWebClient().getBrowserVersion().hasFeature(EVENT_ONMOUSEOVER_FOR_DISABLED_OPTION)) {
            return true;
        }
        return super.handles(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void basicRemove() {
        final DomNode parent = getParentNode();
        super.basicRemove();

        if (parent != null && isSelected()) {
            // update selection and size if needed
            parent.onAllChildrenAddedToPage(false);
        }
    }

}
