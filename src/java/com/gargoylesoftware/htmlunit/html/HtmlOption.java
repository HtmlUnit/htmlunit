/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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

import java.util.Map;

/**
 *  Wrapper for the html element "option"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 */
public class HtmlOption extends ClickableElement {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "option";

    private final boolean initialSelectedState_;

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlOption( final HtmlPage page, final Map attributes ) {
        super(page, attributes);
        initialSelectedState_ = isAttributeDefined("selected");
    }

    /**
     * @return the HTML tag name
     */
    public String getTagName() {
        return TAG_NAME;
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
     */
    public void setSelected( final boolean selected ) {
        getEnclosingSelectOrDie().setSelectedAttribute(this, selected);
    }


    private HtmlSelect getEnclosingSelectOrDie() {
        DomNode parent = getParentNode();
        while( parent != null ) {
            if( parent instanceof HtmlSelect ) {
                return (HtmlSelect)parent;
            }
            parent = parent.getParentNode();
        }
        throw new IllegalStateException("Can't find enclosing select element");
    }


    /**
     * Reset the option to its original selected state.
     */
    public void reset() {
        if( initialSelectedState_ ) {
            setAttributeValue("selected", "selected");
        }
        else {
            removeAttribute("selected");
        }
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
     *
     * @return The value of the attribute "value"
     * or an empty string if that attribute isn't defined.
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
     * Return the value of this option as it will be submitted
     *
     * @return The value of the control
     */    
    public String getValue() {
        if (isAttributeDefined("value")){
            return getValueAttribute();
        } 
        else {
            return asText();
        }
    }
}
