/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import java.io.IOException;
import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlRadioButtonInput extends HtmlInput {
    private final boolean initialCheckedState_;

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlRadioButtonInput( final HtmlPage page, final Element element ) {
        super( page, element );
        initialCheckedState_ = isAttributeDefined("checked");
    }


    /**
     *  Set the "checked" attribute
     *
     * @param  isChecked true if this element is to be selected
     */
    public final void setChecked( final boolean isChecked ) {
        final HtmlForm form = getEnclosingForm();

        if( isChecked ) {
            try {
                form.setCheckedRadioButton( getNameAttribute(), getValueAttribute() );
            }
            catch( final ElementNotFoundException e ) {
                // Shouldn't be possible
                throw new IllegalStateException("Can't find this element when going up to the form and back down.");
            }
        }
        else {
            getElement().removeAttribute( "checked" );
        }
    }


    /**
     *  Return true if this element is currently selected
     *
     * @return  See above
     */
    public final boolean isChecked() {
        return isAttributeDefined("checked");
    }


    /**
     * Return the value of this element to what it was at the time the page was loaded.
     */
    public void reset() {
        if( initialCheckedState_ ) {
            getElement().setAttribute("checked", "checked");
        }
        else {
            getElement().removeAttribute("checked");
        }
    }


    /**
     *  Submit the form that contains this input
     *
     * @return  The Page that is the result of submitting this page to the
     *      server
     * @exception  IOException If an io error occurs
     */
    public Page click() throws IOException {
        return super.click();
    }
}

