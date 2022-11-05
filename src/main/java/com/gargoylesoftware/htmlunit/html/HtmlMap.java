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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "map".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlMap extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "map";

    /**
     * Creates an instance of HtmlMap
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlMap(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name}
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeDirect("name");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE;
    }

    @Override
    public boolean isDisplayed() {
        final HtmlImage image = findReferencingImage();
        if (null != image) {
            return image.isDisplayed();
        }
        return false;
    }

    private HtmlImage findReferencingImage() {
        final HtmlPage page = getHtmlPageOrNull();
        String name = getNameAttribute();
        if (null != page && StringUtils.isNotBlank(name)) {
            name = "#" + name.trim();
            for (final HtmlElement elem : page.getDocumentElement().getElementsByTagName("img")) {
                final HtmlImage image = (HtmlImage) elem;
                if (name.equals(image.getUseMapAttribute())) {
                    return image;
                }
            }
        }
        return null;
    }
}
