/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
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

