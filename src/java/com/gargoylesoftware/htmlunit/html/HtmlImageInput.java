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

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HtmlImageInput extends HtmlInput {

    // For click with x, y position.
    private boolean wasPositionSpecified_ = false;
    private int xPosition_;
    private int yPosition_;

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlImageInput.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlImageInput(final HtmlPage page, final Map attributes) {
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
    HtmlImageInput(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
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
        final String name = getNameAttribute();
        final String prefix;
        // a clicked image without name sends parameter x and y
        if (StringUtils.isEmpty(name)) {
            prefix = "";
        }
        else {
            prefix = name + ".";
        }
            
        if( wasPositionSpecified_ ) {
            return new KeyValuePair[]{
                new KeyValuePair(prefix + "x", String.valueOf(xPosition_)),
                new KeyValuePair(prefix + "y", String.valueOf(yPosition_))
            };
        }
        return new KeyValuePair[]{new KeyValuePair( getNameAttribute(), getValueAttribute() )};
    }


    /**
     * Submit the form that contains this input.  Only a couple of the inputs
     * support this method so it is made protected here.  Those subclasses
     * that wish to expose it will override and make it public.
     *
     * @return  The Page that is the result of submitting this page to the
     *      server
     * @exception  IOException If an io error occurs
     */
    public Page click() throws IOException {
        return click(0,0);
    }

    /**
     * This method will be called if there either wasn't an onclick handler or there was
     * but the result of that handler was true.  This is the default behaviour of clicking
     * the element.  The default implementation returns the current page - subclasses
     * requiring different behaviour (like {@link HtmlSubmitInput}) will override this
     * method.
     *
     * @param defaultPage The default page to return if the action does not
     * load a new page.
     * @return The page that is currently loaded after execution of this method
     * @throws IOException If an IO error occured
     */
    protected Page doClickAction(final Page defaultPage) throws IOException {
        return getEnclosingFormOrDie().submit(this);
    }


    /**
     * Simulate clicking this input with a pointing device.  The x and y coordinates
     * of the pointing device will be sent to the server.
     *
     * @param x The x coordinate of the pointing device at the time of clicking
     * @param y The y coordinate of the pointing device at the time of clicking
     * @return The page that is loaded after the click has taken place.
     * @exception  IOException If an io error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page click( final int x, final int y )
        throws
            IOException,
            ElementNotFoundException {

        wasPositionSpecified_ = true;
        xPosition_ = x;
        yPosition_ = y;
        return super.click();
    }

    /**
     * {@inheritDoc} Also sets the value to the new default value, just like IE.
     * @see SubmittableElement#setDefaultValue(String)
     */
    public void setDefaultValue( final String defaultValue ) {
        super.setDefaultValue( defaultValue );
        setValueAttribute( defaultValue );
    }

}
