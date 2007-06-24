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
 * Wrapper for the html element "object".
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlObject extends ClickableElement {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "object";

    /**
     * Create an instance of HtmlObject
     *
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlObject.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlObject(final HtmlPage page, final Map attributes) {
        this(null, TAG_NAME, page, attributes);
    }

    /**
     * Create an instance of HtmlObject
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     */
    HtmlObject(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Return the value of the attribute "declare".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "declare"
     * or an empty string if that attribute isn't defined.
     */
    public final String getDeclareAttribute() {
        return getAttributeValue("declare");
    }


    /**
     * Return the value of the attribute "classid".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "classid"
     * or an empty string if that attribute isn't defined.
     */
    public final String getClassIdAttribute() {
        return getAttributeValue("classid");
    }


    /**
     * Return the value of the attribute "codebase".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "codebase"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCodebaseAttribute() {
        return getAttributeValue("codebase");
    }


    /**
     * Return the value of the attribute "data".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "data"
     * or an empty string if that attribute isn't defined.
     */
    public final String getDataAttribute() {
        return getAttributeValue("data");
    }


    /**
     * Return the value of the attribute "type".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "type"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttributeValue("type");
    }


    /**
     * Return the value of the attribute "codetype".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "codetype"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCodeTypeAttribute() {
        return getAttributeValue("codetype");
    }


    /**
     * Return the value of the attribute "archive".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "archive"
     * or an empty string if that attribute isn't defined.
     */
    public final String getArchiveAttribute() {
        return getAttributeValue("archive");
    }


    /**
     * Return the value of the attribute "standby".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "standby"
     * or an empty string if that attribute isn't defined.
     */
    public final String getStandbyAttribute() {
        return getAttributeValue("standby");
    }


    /**
     * Return the value of the attribute "height".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "height"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHeightAttribute() {
        return getAttributeValue("height");
    }


    /**
     * Return the value of the attribute "width".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "width"
     * or an empty string if that attribute isn't defined.
     */
    public final String getWidthAttribute() {
        return getAttributeValue("width");
    }


    /**
     * Return the value of the attribute "usemap".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "usemap"
     * or an empty string if that attribute isn't defined.
     */
    public final String getUseMapAttribute() {
        return getAttributeValue("usemap");
    }


    /**
     * Return the value of the attribute "name".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "name"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }


    /**
     * Return the value of the attribute "tabindex".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "tabindex"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }


    /**
     * Return the value of the attribute "align".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "align"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttributeValue("align");
    }


    /**
     * Return the value of the attribute "border".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "border"
     * or an empty string if that attribute isn't defined.
     */
    public final String getBorderAttribute() {
        return getAttributeValue("border");
    }


    /**
     * Return the value of the attribute "hspace".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "hspace"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHspaceAttribute() {
        return getAttributeValue("hspace");
    }


    /**
     * Return the value of the attribute "vspace".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "vspace"
     * or an empty string if that attribute isn't defined.
     */
    public final String getVspaceAttribute() {
        return getAttributeValue("vspace");
    }
}
