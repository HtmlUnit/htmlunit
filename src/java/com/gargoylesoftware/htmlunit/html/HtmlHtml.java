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

import java.util.Map;

/**
 *  A representation of an html element "html".
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public final class HtmlHtml extends HtmlElement {

    private static final long serialVersionUID = 8928934628059990747L;

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "html";

    /**
     * Create an instance of HtmlHtml
     *
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlHtml.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlHtml(final HtmlPage page, final Map attributes) {
        this(null, TAG_NAME, page, attributes);
    }

    /**
     * Create an instance of HtmlHtml
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     */
    HtmlHtml(final String namespaceURI, final String qualifiedName, final HtmlPage page, final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     *  Return the value of the attribute "lang". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "lang" or an empty string if that
     *      attribute isn't defined.
     */
    public String getLangAttribute() {
        return getAttributeValue( "lang" );
    }

    /**
     *  Return the value of the attribute "xml:lang". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "xml:lang" or an empty string if that
     *      attribute isn't defined.
     */
    public String getXmlLangAttribute() {
        return getAttributeValue( "xml:lang" );
    }

    /**
     *  Return the value of the attribute "dir". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "dir" or an empty string if that
     *      attribute isn't defined.
     */
    public String getTextDirectionAttribute() {
        return getAttributeValue( "dir" );
    }
}

