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
package com.gargoylesoftware.htmlunit;

/**
 *  An exception that is thrown when a specified xml element cannot be found in
 *  the dom model
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ElementNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 22133747152805455L;
    
    private final String elementName_;
    private final String attributeName_;
    private final String attributeValue_;


    /**
     *  Create an instance from the variables that were used to search for the
     *  xml element
     *
     * @param  elementName The name of the element
     * @param  attributeName The name of the attribute
     * @param  attributeValue The value of the attribute
     */
    public ElementNotFoundException(
            final String elementName, final String attributeName, final String attributeValue ) {
        super( "elementName=[" + elementName
                 + "] attributeName=[" + attributeName
                 + "] attributeValue=[" + attributeValue + "]" );

        elementName_ = elementName;
        attributeName_ = attributeName;
        attributeValue_ = attributeValue;
    }


    /**
     *  Return the name of the element
     *
     * @return  See above
     */
    public String getElementName() {
        return elementName_;
    }


    /**
     *  Return the name of the attribute
     *
     * @return  See above
     */
    public String getAttributeName() {
        return attributeName_;
    }


    /**
     *  Return the value of the attribute
     *
     * @return  See above
     */
    public String getAttributeValue() {
        return attributeValue_;
    }
}

