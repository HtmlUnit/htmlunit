/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.KeyValuePair;

import java.util.Map;

/**
 *  Wrapper for the html element "textarea"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:BarnabyCourt@users.sourceforge.net">Barnaby Court</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HtmlTextArea extends FocusableElement implements DisabledElement, SubmittableElement {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "textarea";

    private String defaultValue_;

    private int selectionStart_;
    
    private int selectionEnd_;
    
    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlTextArea.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlTextArea(final HtmlPage page, final Map attributes) {
        this(null, TAG_NAME, page, attributes);
    }

    /**
     *  Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     */
    HtmlTextArea(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Initializes the default value if necessary. We cannot do it in the constructor
     * because the child node variable will not have been initialized yet. Must be called
     * from all methods that use the default value.
     */
    private void initDefaultValue() {
        if( defaultValue_ == null ) {
            final DomText child = (DomText) getFirstChild();
            if( child != null ) {
                defaultValue_ = child.getData();
                if( defaultValue_ == null ) {
                    defaultValue_ = "";
                }
            }
            else {
                defaultValue_ = "";
            }
        }
    }

    /**
     * Return the value that would be displayed in the text area
     *
     * @return The text
     */
    public final String getText() {
        return getChildrenAsText();
    }

    /**
     *  Set the new value of this text area.
     *
     * @param  newValue The new value.
     */
    public final void setText( final String newValue ) {
        initDefaultValue();
        final DomText child = (DomText) getFirstChild();
        if (child == null) {
            final DomText newChild = new DomText(getPage(), newValue); 
            appendChild( newChild );
        }
        else {
            child.setData( newValue );
        }

        getPage().executeOnChangeHandlerIfAppropriate(this);
        setSelectionStart( newValue.length() );
        setSelectionEnd( newValue.length() );
    }

    /**
     *  Return an array of KeyValuePairs that are the values that will be sent
     *  back to the server whenever the current form is submitted.<p>
     *
     *  THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     *  BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.
     *
     * @return  See above
     */
    public KeyValuePair[] getSubmitKeyValuePairs() {
        return new KeyValuePair[]{new KeyValuePair( getNameAttribute(), getText() )};
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    public void reset() {
        initDefaultValue();
        setText( defaultValue_ );
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultValue(String)
     */
    public void setDefaultValue( final String defaultValue ) {
        initDefaultValue();
        if( defaultValue == null ) {
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
    public void setDefaultChecked( final boolean defaultChecked ) {
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
     * Return the value of the attribute "name".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "name"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }

    /**
     * Return the value of the attribute "rows".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "rows"
     * or an empty string if that attribute isn't defined.
     */
    public final String getRowsAttribute() {
        return getAttributeValue("rows");
    }

    /**
     * Return the value of the attribute "cols".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "cols"
     * or an empty string if that attribute isn't defined.
     */
    public final String getColumnsAttribute() {
        return getAttributeValue("cols");
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
     * Return the value of the attribute "readonly".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "readonly"
     * or an empty string if that attribute isn't defined.
     */
    public final String getReadOnlyAttribute() {
        return getAttributeValue("readonly");
    }

    /**
     * Return the value of the attribute "tabindex".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "tabindex"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }

    /**
     * Return the value of the attribute "accesskey".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "accesskey"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAccessKeyAttribute() {
        return getAttributeValue("accesskey");
    }

    /**
     * Return the value of the attribute "onfocus".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onfocus"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue("onfocus");
    }

    /**
     * Return the value of the attribute "onblur".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onblur"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue("onblur");
    }

    /**
     * Return the value of the attribute "onselect".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onselect"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnSelectAttribute() {
        return getAttributeValue("onselect");
    }

    /**
     * Return the value of the attribute "onchange".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onchange"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnChangeAttribute() {
        return getAttributeValue("onchange");
    }

    /**
     * Returns the selected text contained in this HtmlTextArea, <code>null</code> if no selection (Firefox only).
     * @return the text
     */
    public String getSelectedText() {
        String text = null;
        if( selectionStart_ != selectionEnd_ ) {
            text = getText().substring( selectionStart_, selectionEnd_ );
        }
        return text;
    }
    
    /**
     * Returns the selected text's start position (Firefox only).
     * @return the start position >= 0
     */
    public int getSelectionStart() {
        return selectionStart_;
    }

    /**
     * Sets the selection start to the specified position (Firefox only).
     * @param selectionStart the start position of the text >= 0
     */
    public void setSelectionStart(int selectionStart) {
        if( selectionStart < 0 ) {
            selectionStart = 0;
        }
        final int length = getText().length();
        if( selectionStart > length ) {
            selectionStart = length;
        }
        if( selectionEnd_ < selectionStart ) {
            selectionEnd_ = selectionStart;
        }
        this.selectionStart_ = selectionStart; 
    }
    
    /**
     * Returns the selected text's end position (Firefox only).
     * @return the end position >= 0
     */
    public int getSelectionEnd() {
        return selectionEnd_;
    }
 
    /**
     * Sets the selection end to the specified position (Firefox only). 
     * @param selectionEnd the end position of the text >= 0

     */
    public void setSelectionEnd(int selectionEnd) {
        if( selectionEnd < 0 ) {
            selectionEnd = 0;
        }
        final int length = getText().length();
        if( selectionEnd > length ) {
            selectionEnd = length;
        }
        if( selectionEnd < selectionStart_ ) {
            selectionStart_ = selectionEnd;
        }
        this.selectionEnd_ = selectionEnd;
    }
}
