/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;

/**
 *  Wrapper for the html element "option"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlOption extends ClickableElement implements DisabledElement {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "option";

    private final boolean initialSelectedState_;

    /**
     *  Create an instance
     *
     * @param page The page that contains this element
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlOption.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlOption(final HtmlPage page, final Map attributes) {
        this(null, TAG_NAME, page, attributes);
    }

    /**
     *  Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param page The page that contains this element
     * @param attributes the initial attributes
     */
    HtmlOption(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
        initialSelectedState_ = isAttributeDefined("selected");
    }

    /**
     *  Return true if this option is currently selected
     *
     * @return  See above
     */
    public boolean isSelected() {
        return isAttributeDefined("selected");
    }

    /**
     * Set the selected state of this option. This will possibly also change the
     * selected properties of sibling option elements
     *
     * @param selected true if this option should be selected.
     * @return  The page that occupies this window after this change is made.  It
     * may be the same window or it may be a freshly loaded one.
     */
    public Page setSelected( final boolean selected ) {
        if (selected == isSelected()) {
            return getPage();
        }
        else {
            final HtmlSelect select = getEnclosingSelect();
            if (select != null) {
                return select.setSelectedAttribute(this, selected);
            }
            else {
                // for instance from JS for an option created by document.createElement('option')
                // and not yet added to a select
                setSelectedInternal(selected);
                return getPage();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     * @see DomNode#insertBefore(DomNode)
     */
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
     * Gets the enclosing select of this option
     * @return <code>null</code> if no select is found (for instance malformed html)
     */
    public HtmlSelect getEnclosingSelect() {
        return (HtmlSelect) getEnclosingElement("select");
    }


    /**
     * Reset the option to its original selected state.
     */
    public void reset() {
        setSelectedInternal(initialSelectedState_);
    }


    /**
     * Return the value of the attribute "selected".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "selected"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSelectedAttribute() {
        return getAttributeValue("selected");
    }
    
    /**
     * Return whether this Option is selected by default. 
     * That is whether the "selected"
     * attribute exists when the Option is constructed. This also determines
     * the value of getSelectedAttribute() after a reset() on the form.
     * @return whether the option is selected by default.
     */
    public final boolean isDefaultSelected() {
        return initialSelectedState_;
    }


    /**
     * Return true if the disabled attribute is set for this element.
     *
     * @return Return true if this element is disabled.
     */
    public final boolean isDisabled() {
        return isAttributeDefined("disabled");
    }


    /**
     * Return the value of the attribute "disabled".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "disabled"
     * or an empty string if that attribute isn't defined.
     */
    public final String getDisabledAttribute() {
        return getAttributeValue("disabled");
    }


    /**
     * Return the value of the attribute "label".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "label"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLabelAttribute() {
        return getAttributeValue("label");
    }


    /**
     * Set the value of the attribute "label".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @param newLabel The value of the attribute "label".
     */
    public final void setLabelAttribute( final String newLabel ) {
        setAttributeValue("label", newLabel);
    }


    /**
     * Return the value of the attribute "value".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-value-OPTION">
     * initial value if value attribute is not set</a>
     * @return The value of the attribute "value"
     */
    public final String getValueAttribute() {
        return getAttributeValue("value");
    }


    /**
     * Set the value of the attribute "value".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @param newValue The value of the attribute "value".
     */
    public final void setValueAttribute( final String newValue ) {
        setAttributeValue("value", newValue);
    }

    /**
     * Selects the option if it's not already selected.
     * {@inheritDoc}
     */
    protected Page doClickAction(final Page defaultPage) throws IOException {
        if (!isSelected()) {
            return setSelected(true);
        }
        return defaultPage;
    }

    /**
     * {@inheritDoc}
     */
    public DomNode appendChild(final DomNode node) {
        final DomNode addedNode = super.appendChild(node);

        // default value is the text of the option
        // see http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-value-OPTION
        if (getAttributeValue("value") == ATTRIBUTE_NOT_DEFINED) {
            setAttributeValue("value", asText());
        }

        return addedNode;
    }

    /**
     * For internal use only.
     * Sets/remove the selected attribute to reflect the select state
     * @param selected the selected status
     */
    void setSelectedInternal(final boolean selected) {
        if (selected) {
            setAttributeValue("selected", "selected");
        }
        else {
            removeAttribute("selected");
        }
    }

    /**
     * {@inheritDoc}
     * This implementation will show the label attribute before the 
     * content of the tag if the attribute exists.
     */
    public String asText() {
        if (getLabelAttribute() != ATTRIBUTE_NOT_DEFINED){
            return getLabelAttribute();
        }
        return super.asText();
    }
}
