/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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

import org.xml.sax.Attributes;

import java.util.Map;
import java.util.HashMap;

/**
 * A specialized creator that knows how to create input objects
 *
 * @version  $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 */
final class InputElementFactory implements IElementFactory {

    public static final InputElementFactory instance = new InputElementFactory();

    /* private singleton constructor */
    private InputElementFactory() {
    }

    /**
     * Create an HtmlElement for the specified xmlElement, contained in the specified page.
     *
     * @param page The page that this element will belong to.
     * @param tagName the HTML tag name
     * @param attributes the SAX attributes
     *
     * @return a new HtmlInput element.
     */
    public HtmlElement createElement(HtmlPage page, String tagName, Attributes attributes) {

        String type = attributes.getValue("type");

        //type = type != null ? type.toLowerCase() : "";
        if(type != null) {
            type = type.toLowerCase();
        }
        else {
            type = "";
        }

        HtmlInput result;

        Map attributeMap = null;
        if(attributes != null) {
            attributeMap = new HashMap(attributes.getLength());
            for(int i=0; i < attributes.getLength(); i++) {
                attributeMap.put(attributes.getLocalName(i), attributes.getValue(i));
            }
        }
        if( type.length() == 0 ) {
            // This is really an illegal value but the common browsers seem to
            // treat it as a "text" input so we will as well.
            result = new HtmlTextInput(page, attributeMap);
        }
        else if( type.equals("submit") ) {
            result = new HtmlSubmitInput(page, attributeMap);
        }
        else if( type.equals("checkbox")) {
            result = new HtmlCheckBoxInput(page, attributeMap);
        }
        else if( type.equals("radio")) {
            result = new HtmlRadioButtonInput(page, attributeMap);
        }
        else if( type.equals("text")) {
            result = new HtmlTextInput(page, attributeMap);
        }
        else if( type.equals("hidden")) {
            result = new HtmlHiddenInput(page, attributeMap);
        }
        else if( type.equals("password")) {
            result = new HtmlPasswordInput(page, attributeMap);
        }
        else if( type.equals("image")) {
            result = new HtmlImageInput(page, attributeMap);
        }
        else if( type.equals("reset")) {
            result = new HtmlResetInput(page, attributeMap);
        }
        else if( type.equals("button")) {
            result = new HtmlButtonInput(page, attributeMap);
        }
        else if( type.equals("file")) {
            result = new HtmlFileInput(page, attributeMap);
        }
        else {
            throw new IllegalArgumentException("Unexpected input type ["+type+"]");
        }
        return result;
    }
}
