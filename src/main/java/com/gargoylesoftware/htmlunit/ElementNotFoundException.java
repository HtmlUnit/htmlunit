/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

/**
 * An exception that is thrown when a specified XML element cannot be found in the DOM model.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ElementNotFoundException extends RuntimeException {

    private final String elementName_;
    private final String attributeName_;
    private final String attributeValue_;

    /**
     * Creates an instance from the variables that were used to search for the XML element.
     *
     * @param elementName the name of the element
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     */
    public ElementNotFoundException(
            final String elementName, final String attributeName, final String attributeValue) {
        super("elementName=[" + elementName
                 + "] attributeName=[" + attributeName
                 + "] attributeValue=[" + attributeValue + "]");

        elementName_ = elementName;
        attributeName_ = attributeName;
        attributeValue_ = attributeValue;
    }

    /**
     * Returns the name of the element.
     *
     * @return the name of the element
     */
    public String getElementName() {
        return elementName_;
    }

    /**
     * Returns the name of the attribute.
     *
     * @return the name of the attribute
     */
    public String getAttributeName() {
        return attributeName_;
    }

    /**
     * Returns the value of the attribute.
     *
     * @return the value of the attribute
     */
    public String getAttributeValue() {
        return attributeValue_;
    }
}

