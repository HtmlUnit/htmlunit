/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.net.MalformedURLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * HTML Media element, e.g. {@link HtmlAudio} or {@link HtmlVideo}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlMedia extends HtmlElement {

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlMedia(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Determines whether the specified media type can be played back.
     * @param type the type
     * @return "probably", "maybe", or "". The current implementation returns ""
     */
    public String canPlayType(final String type) {
        if (StringUtils.isAllBlank(type)) {
            return "";
        }

        final int semPos = type.indexOf(';');
        final int codecPos = type.indexOf("codec");

        if (semPos > 0 && codecPos > semPos) {
            return "probably";
        }

        return "maybe";
    }

    /**
     * Returns the value of the attribute {@code src}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src} or an empty string if that attribute isn't defined
     */
    public final String getSrcAttribute() {
        return getSrcAttributeNormalized();
    }

    /**
     * @return the value of the {@code src} value
     */
    public String getSrc() {
        final String src = getSrcAttribute();
        if ("".equals(src)) {
            return src;
        }
        try {
            final HtmlPage page = (HtmlPage) getPage();
            return page.getFullyQualifiedUrl(src).toExternalForm();
        }
        catch (final MalformedURLException e) {
            final String msg = "Unable to create fully qualified URL for src attribute of media " + e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Sets the value of the {@code src} attribute.
     * @param src the value of the {@code src} attribute
     */
    public void setSrc(final String src) {
        setAttribute(SRC_ATTRIBUTE, src);
    }

    /**
     * Returns the absolute URL of the chosen media resource.
     * This could happen, for example, if the web server selects a
     * media file based on the resolution of the user's display.
     * The value is an empty string if the networkState property is EMPTY.
     * @return the absolute URL of the chosen media resource
     */
    public String getCurrentSrc() {
        return "";
    }
}
