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
import java.util.Iterator;

import com.gargoylesoftware.htmlunit.Page;

/**
 * Wrapper for the html element "label".
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 */
public class HtmlLabel extends FocusableElement {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "label";

    /**
     * Create an instance of HtmlLabel
     *
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     */
    public HtmlLabel( final HtmlPage page, final Map attributes ) {
        super( page, attributes );
    }

    /**
     * @return the HTML tag name
     */
    public String getTagName() {
        return TAG_NAME;
    }

    /**
     * Return the value of the attribute "for".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "for"
     * or an empty string if that attribute isn't defined.
     */
    public final String getForAttribute() {
        return getAttributeValue("for");
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
     * Remove focus from this element.
     */
    public void blur() {
        final FocusableElement element = getReferencedElement();
        if (element != null) {
            element.blur();
        }
    }


    /**
     * Set the focus to this element.
     */
    public void focus() {
        final FocusableElement element = getReferencedElement();
        if (element != null) {
            element.focus();
        }
    }


    private FocusableElement getReferencedElement() {
        final String elementId = getAttributeValue("for");
        if (elementId.length() != 0) {
            final HtmlElement element = getHtmlElementById(elementId);
            if (element != null && element instanceof HtmlInput) {
                return (FocusableElement) element;
            }
        }
        else {
            final Iterator childIterator = getChildIterator();
            while (childIterator.hasNext()) {
                final DomNode element = (DomNode) childIterator.next();
                if (element instanceof HtmlInput) {
                    return (FocusableElement) element;
                }
            }
        }
        return null;
    }
    
    /**
     * Clicks the label and propagates to the referenced element.
     * {@inheritDoc}
     */
    public Page click() throws IOException {
        // first the click on the label
        final Page page = super.click();
        
        // not sure which page we should return
        final Page response;

        // then the click on the referenced element
        final FocusableElement element = getReferencedElement();
        if (element != null) {
            response = element.click();
        }
        else {
            response = page;
        }

        return response;
    }
}
