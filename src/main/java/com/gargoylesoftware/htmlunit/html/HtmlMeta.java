/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.HtmlUnitBrowserCompatCookieSpec;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Wrapper for the HTML element "meta".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlMeta extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "meta";

    /**
     * Creates an instance of HtmlMeta
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlMeta(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        if ("set-cookie".equalsIgnoreCase(getHttpEquivAttribute())) {
            performSetCookie();
        }
    }

    /**
     * Handles the cookies specified in meta tags,
     * like <tt>&lt;meta http-equiv='set-cookie' content='webm=none; path=/;'&gt;</tt>.
     */
    protected void performSetCookie() {
        final String content = getContentAttribute();
        final CharArrayBuffer buffer = new CharArrayBuffer(content.length() + 12);
        buffer.append("Set-Cookie: ");
        buffer.append(content);

        final SgmlPage page = getPage();
        final WebClient client = page.getWebClient();
        final BrowserVersion browserVersion = client.getBrowserVersion();
        final URL url = page.getUrl();
        final CookieManager cookieManager = client.getCookieManager();
        final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);

        try {
            final List<org.apache.http.cookie.Cookie> cookies =
                    cookieSpec.parse(new BufferedHeader(buffer), cookieManager.buildCookieOrigin(url));

            for (org.apache.http.cookie.Cookie cookie : cookies) {
                final Cookie htmlUnitCookie = new Cookie((ClientCookie) cookie);
                cookieManager.addCookie(htmlUnitCookie);
            }
        }
        catch (final MalformedCookieException e) {
            notifyIncorrectness("set-cookie http-equiv meta tag: invalid cookie '"
                    + content + "'; reason: '" + e.getMessage() + "'.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayBeDisplayed() {
        return false;
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
