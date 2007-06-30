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

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Jun Chen</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Mike Bresnahan
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HtmlCheckBoxInput extends HtmlInput {

    private boolean defaultCheckedState_;

    /**
     * Create an instance.
     * If no value is specified, it is set to "on" as browsers do (e.g. IE6 and Mozilla 1.7) 
     * even if spec says that it is not allowed 
     * (<a href="http://www.w3.org/TR/REC-html40/interact/forms.html#adef-value-INPUT">W3C</a>).
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlCheckBoxInput.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlCheckBoxInput(final HtmlPage page, final Map attributes) {
        this(null, TAG_NAME, page, attributes);
    }
    
    /**
     * Create an instance.
     * If no value is specified, it is set to "on" as browsers do (e.g. IE6 and Mozilla 1.7) 
     * even if spec says that it is not allowed 
     * (<a href="http://www.w3.org/TR/REC-html40/interact/forms.html#adef-value-INPUT">W3C</a>).
     * 
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     */
    HtmlCheckBoxInput(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);

        //From the checkbox creator
        defaultCheckedState_ = isAttributeDefined("checked");

        // default value for both IE6 and Mozilla 1.7 even if spec says it is unspecified
        if (getAttributeValue("value") == ATTRIBUTE_NOT_DEFINED) {
            setAttributeValue("value", "on");
        }
    }


    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    public void reset() {
        setChecked( defaultCheckedState_ );
    }

    /**
     *  Set the "checked" attribute
     *
     * @param  isChecked true if this element is to be selected
     * @return The page that occupies this window after setting checked status.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page setChecked( final boolean isChecked ) {
        if( isChecked ) {
            setAttributeValue( "checked", "checked" );
        }
        else {
            removeAttribute( "checked" );
        }
        return getPage().executeOnChangeHandlerIfAppropriate(this);
    }

    /**
     * A checkbox does not have a textual representation,
     * but we invent one for it because it is useful for testing.
     * @return "checked" or "unchecked" according to the radio state
     */
    public String asText() {
        if (isChecked()) {
            return "checked";
        }
        else {
            return "unchecked";
        }
    }

    /**
     * Override so that checkbox can change its state correctly when its
     * click() method is called.
     *
     * {@inheritDoc}
     */
    protected Page doClickAction(final Page defaultPage) throws IOException {
        if (isChecked()) {
            setChecked(false);
        }
        else {
            setChecked(true);
        }

        return super.doClickAction(defaultPage);
    }

    /**
     * Both IE and Mozilla will first update the internal state of checkbox
     * and then handle "onclick" event.
     * {@inheritDoc}
     */
    protected boolean isStateUpdateFirst() {
        return true;
    }

    /**
     * {@inheritDoc} Also sets the value to the new default value.
     * @see SubmittableElement#setDefaultValue(String)
     */
    public void setDefaultValue( final String defaultValue ) {
        super.setDefaultValue( defaultValue );
        setValueAttribute( defaultValue );
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultChecked(boolean)
     */
    public void setDefaultChecked( final boolean defaultChecked ) {
        defaultCheckedState_ = defaultChecked;
        if( getPage().getWebClient().getBrowserVersion().isNetscape() ) {
            setChecked( defaultChecked );
        }
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#isDefaultChecked()
     */
    public boolean isDefaultChecked() {
        return defaultCheckedState_;
    }

}

