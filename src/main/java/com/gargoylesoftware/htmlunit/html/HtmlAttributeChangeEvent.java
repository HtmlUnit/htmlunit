/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.EventObject;

/**
 * This is the event class for notifications about changes to the attributes of the
 * HtmlElement.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @see HtmlAttributeChangeListener
 */
public class HtmlAttributeChangeEvent extends EventObject {

    private final String name_;
    private final String value_;

    /**
     * Constructs a new AttributeEvent from the given element, for the given attribute name and attribute value.
     *
     * @param element the element that is sending the event
     * @param name the name of the attribute that changed on the element
     * @param value the value of the attribute that has been added, removed, or replaced
     */
    public HtmlAttributeChangeEvent(final HtmlElement element, final String name, final String value) {
        super(element);
        name_ = name;
        value_ = value;
    }

    /**
     * Returns the HtmlElement that changed.
     * @return the HtmlElement that sent the event
     */
    public HtmlElement getHtmlElement() {
        return (HtmlElement) getSource();
    }

    /**
     * Returns the name of the attribute that changed on the element.
     * @return the name of the attribute that changed on the element
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the value of the attribute that has been added, removed, or replaced.
     * If the attribute was added, this is the value of the attribute.
     * If the attribute was removed, this is the value of the removed attribute.
     * If the attribute was replaced, this is the old value of the attribute.
     *
     * @return the value of the attribute that has been added, removed, or replaced
     */
    public String getValue() {
        return value_;
    }
}
