/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

    private static final long serialVersionUID = -7432080435641028075L;

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
        this.name_ = name;
        this.value_ = value;
    }
    
    /**
     * Returns the HtmlElement that changed.
     * @return the HtmlElement that sent the event.
     */
    public HtmlElement getHtmlElement() {
        return (HtmlElement) getSource();
    }
    
    /**
     * Returns the name of the attribute that changed on the element.
     * @return the name of the attribute that changed on the element.
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
     * @return the value of the attribute that has been added, removed, or replaced.
     */
    public String getValue() {
        return value_;
    }
}
