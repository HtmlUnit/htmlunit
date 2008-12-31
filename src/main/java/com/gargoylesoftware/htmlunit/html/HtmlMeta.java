/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;

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

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "meta";

    /**
     * Create an instance of HtmlMeta
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlMeta(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);

        if ("set-cookie".equalsIgnoreCase(getHttpEquivAttribute())) {
            performSetCookie();
        }
    }

    /**
     * Handles the cookies specified in meta tags,
     * like <tt>&lt;meta http-equiv='set-cookie' content='webm=none; path=/;'&gt;</tt>.
     */
    protected void performSetCookie() {
        final String[] parts = getContentAttribute().split("\\s*;\\s*");
        final String name = StringUtils.substringBefore(parts[0], "=");
        final String value = StringUtils.substringAfter(parts[0], "=");
        final Cookie cookie = new Cookie(getPage().getWebResponse().getRequestUrl().getHost(), name, value);
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
            getPage().getWebClient().getCookieManager().addCookie(cookie);
        }
    }

    /**
     * Returns the value of the attribute "lang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLangAttribute() {
        return getAttribute("lang");
    }

    /**
     * Returns the value of the attribute "xml:lang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "xml:lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getXmlLangAttribute() {
        return getAttribute("xml:lang");
    }

    /**
     * Returns the value of the attribute "dir". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "dir"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTextDirectionAttribute() {
        return getAttribute("dir");
    }

    /**
     * Returns the value of the attribute "http-equiv". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "http-equiv"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHttpEquivAttribute() {
        return getAttribute("http-equiv");
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute "content". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "content"
     * or an empty string if that attribute isn't defined.
     */
    public final String getContentAttribute() {
        return getAttribute("content");
    }

    /**
     * Returns the value of the attribute "scheme". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "scheme"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSchemeAttribute() {
        return getAttribute("scheme");
    }
}
