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

import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Wrapper for the HTML element "meta".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlMeta extends HtmlElement {

    private static final long serialVersionUID = 7408601325303605790L;

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "meta";

    /**
     * Create an instance of HtmlMeta
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     */
    HtmlMeta(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
        
        if ("set-cookie".equalsIgnoreCase(getHttpEquivAttribute())) {
            performSetCookie();
        }
    }

    /**
     * Handles the cookies specified in meta tags
     * like <meta http-equiv='set-cookie' content='webm=none; path=/;'>
     */
    protected void performSetCookie() {
        final String[] parts = getContentAttribute().split("\\s*;\\s*");
        final String name = StringUtils.substringBefore(parts[0], "=");
        final String value = StringUtils.substringAfter(parts[0], "=");
        final Cookie cookie = new Cookie(getPage().getWebResponse().getUrl().getHost(), name, value);
        for (int i = 1; i < parts.length; i++) {
            final String partName = StringUtils.substringBefore(parts[i], "=").trim().toLowerCase();
            final String partValue = StringUtils.substringAfter(parts[i], "=").trim();
            if ("path".equals(partName)) {
                cookie.setPath(partValue);
            }
            else if ("expires".equals(partName)) {
                try {
                    cookie.setExpiryDate(DateUtil.parseDate(partValue));
                }
                catch (final DateParseException e) {
                    notifyIncorrectness("set-cookie http-equiv meta tag: can't parse expiration date >"
                            + partValue + "<.");
                }
            }
            else {
                notifyIncorrectness("set-cookie http-equiv meta tag: unknown attribute >" + partName + "<");
            }
            getPage().getWebClient().getWebConnection().getState().addCookie(cookie);
        }
    }

    /**
     * Return the value of the attribute "lang".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLangAttribute() {
        return getAttributeValue("lang");
    }

    /**
     * Return the value of the attribute "xml:lang".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "xml:lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getXmlLangAttribute() {
        return getAttributeValue("xml:lang");
    }

    /**
     * Return the value of the attribute "dir".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "dir"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTextDirectionAttribute() {
        return getAttributeValue("dir");
    }

    /**
     * Return the value of the attribute "http-equiv".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "http-equiv"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHttpEquivAttribute() {
        return getAttributeValue("http-equiv");
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
     * Return the value of the attribute "content".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "content"
     * or an empty string if that attribute isn't defined.
     */
    public final String getContentAttribute() {
        return getAttributeValue("content");
    }

    /**
     * Return the value of the attribute "scheme".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "scheme"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSchemeAttribute() {
        return getAttributeValue("scheme");
    }
}
