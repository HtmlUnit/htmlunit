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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;

/**
 * Wrapper for the HTML element "option".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class HtmlOption extends HtmlElement implements DisabledElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "option";

    private final boolean initialSelectedState_;

    private boolean selected_;

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlOption(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
        initialSelectedState_ = hasAttribute("selected");
    }

    /**
     * Returns <tt>true</tt> if this option is currently selected.
     * @return <tt>true</tt> if this option is currently selected
     */
    public boolean isSelected() {
        return hasAttribute("selected") || selected_;
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
        setSelected(selected, true);
        return getPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Sets the selected state of this option. This will possibly also change the
     * selected properties of sibling option elements.
     *
     * @param selected true if this option should be selected
     * @param invokeOnFocus whether to set focus or not.
     */
    public void setSelected(boolean selected, final boolean invokeOnFocus) {
        if (selected == isSelected()) {
            return;
        }
        final HtmlSelect select = getEnclosingSelect();
        if (select != null) {
            if (!select.isMultipleSelectEnabled() && select.getOptionSize() == 1) {
                selected = true;
            }
            select.setSelectedAttribute(this, selected, invokeOnFocus);
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
    public void insertBefore(final DomNode newNode) throws IllegalStateException {
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
     * @return <code>null</code> if no select is found (for instance malformed html)
     */
    public HtmlSelect getEnclosingSelect() {
        return (HtmlSelect) getEnclosingElement("select");
    }

    /**
     * Resets the option to its original selected state.
     */
    public void reset() {
        setSelectedInternal(initialSelectedState_);
    }

    /**
     * Returns the value of the attribute "selected". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "selected"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSelectedAttribute() {
        return getAttribute("selected");
    }

    /**
     * Returns whether this Option is selected by default.
     * That is whether the "selected"
     * attribute exists when the Option is constructed. This also determines
     * the value of getSelectedAttribute() after a reset() on the form.
     * @return whether the option is selected by default
     */
    public final boolean isDefaultSelected() {
        return initialSelectedState_;
    }

    /**
     * Returns <tt>true</tt> if the disabled attribute is set for this element. Note that this
     * method always returns <tt>false</tt> when emulating IE, because IE does not allow individual
     * options to be disabled.
     *
     * @return <tt>true</tt> if the disabled attribute is set for this element (always <tt>false</tt>
     *         when emulating IE)
     */
    public final boolean isDisabled() {
        if (getPage().getWebClient().getBrowserVersion().hasFeature(
                BrowserVersionFeatures.HTMLOPTION_PREVENT_DISABLED)) {
            return false;
        }
        return hasAttribute("disabled");
    }

    /**
     * {@inheritDoc}
     */
    public final String getDisabledAttribute() {
        return getAttribute("disabled");
    }

    /**
     * Returns the value of the attribute "label". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "label" or an empty string if that attribute isn't defined
     */
    public final String getLabelAttribute() {
        return getAttribute("label");
    }

    /**
     * Sets the value of the attribute "label". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @param newLabel the value of the attribute "label"
     */
    public final void setLabelAttribute(final String newLabel) {
        setAttribute("label", newLabel);
    }

    /**
     * Returns the value of the attribute "value". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-value-OPTION">
     * initial value if value attribute is not set</a>
     * @return the value of the attribute "value"
     */
    public final String getValueAttribute() {
        String value = getAttribute("value");
        if (value == ATTRIBUTE_NOT_DEFINED) {
            value = getText();
        }
        return value;
    }

    /**
     * Sets the value of the attribute "value". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @param newValue the value of the attribute "value"
     */
    public final void setValueAttribute(final String newValue) {
        setAttribute("value", newValue);
    }

    /**
     * Selects the option if it's not already selected.
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate() throws IOException {
        boolean changed = false;
        if (!isSelected()) {
            setSelected(true);
            changed = true;
        }
        else if (getEnclosingSelect().isMultipleSelectEnabled()) {
            setSelected(false);
            changed = true;
        }
        super.doClickStateUpdate();
        return changed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScriptResult doClickFireClickEvent(final Event event) throws IOException {
        final ScriptResult scriptResult = super.doClickFireClickEvent(event);
        final boolean triggerClickOption = getPage().getWebClient().getBrowserVersion()
                .hasFeature(BrowserVersionFeatures.EVENT_ONCLICK_FOR_SELECT_OPTION_ALSO);
        if (!triggerClickOption) {
            return scriptResult;
        }

        if (event.isAborted(scriptResult)) {
            return scriptResult;
        }

        final Event optionClickEvent = new MouseEvent(this, MouseEvent.TYPE_CLICK,
                event.jsxGet_shiftKey(), event.jsxGet_ctrlKey(), event.jsxGet_altKey(),
            MouseEvent.BUTTON_LEFT);
        return super.doClickFireClickEvent(optionClickEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void printOpeningTagContentAsXml(final PrintWriter printWriter) {
        super.printOpeningTagContentAsXml(printWriter);
        if (selected_ && getAttribute("selected") == ATTRIBUTE_NOT_DEFINED) {
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
        if (!selected) {
            removeAttribute("selected");
        }
    }

    /**
     * {@inheritDoc}
     * This implementation will show the label attribute before the
     * content of the tag if the attribute exists.
     */
    // we need to preserve this method as it is there since many versions with the above documentation.
    @Override
    public String asText() {
        return super.asText();
    }

    /**
     * Sets the text for this HtmlOption.
     * @param text the text
     */
    public void setText(final String text) {
        if ((text == null || text.length() == 0)
                && getPage().getWebClient().getBrowserVersion()
                .hasFeature(BrowserVersionFeatures.HTMLOPTION_EMPTY_TEXT_IS_NO_CHILDREN)) {
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
        final HtmlSerializer ser = new HtmlSerializer();
        ser.setIgnoreMaskedElements(false);
        return ser.asText(this);
    }

    /**
     * {@inheritDoc}
     */
    protected DomNode getEventTargetElement() {
        final HtmlSelect select = getEnclosingSelect();
        if (select != null) {
            return select;
        }
        return super.getEventTargetElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isStateUpdateFirst() {
        return getPage().getWebClient().getBrowserVersion()
                .hasFeature(BrowserVersionFeatures.GENERATED_103);
    }
}
