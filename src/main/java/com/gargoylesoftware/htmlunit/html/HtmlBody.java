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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BODY_MARGINS_8;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.css.ComputedCssStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

/**
 * Wrapper for the HTML element "body".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class HtmlBody extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "body";

    /** Whether or not this body is temporary (created because the <code>body</code> tag has not yet been parsed). */
    private final boolean temporary_;

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     * @param temporary whether or not this body is temporary (created because the <code>body</code>
     *        tag does not exist or has not yet been parsed)
     */
    public HtmlBody(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes, final boolean temporary) {

        super(qualifiedName, page, attributes);

        temporary_ = temporary;
    }

    /**
     * Returns the value of the attribute {@code onload}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onload} or an empty string if that attribute isn't defined
     */
    public final String getOnLoadAttribute() {
        return getAttributeDirect("onload");
    }

    /**
     * Returns the value of the attribute {@code onunload}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onunload} or an empty string if that attribute isn't defined
     */
    public final String getOnUnloadAttribute() {
        return getAttributeDirect("onunload");
    }

    /**
     * Returns the value of the attribute {@code background}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code background} or an empty string if that attribute isn't defined
     */
    public final String getBackgroundAttribute() {
        return getAttributeDirect("background");
    }

    /**
     * Returns the value of the attribute {@code bgcolor}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code bgcolor} or an empty string if that attribute isn't defined
     */
    public final String getBgcolorAttribute() {
        return getAttributeDirect("bgcolor");
    }

    /**
     * Returns the value of the attribute {@code text}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code text} or an empty string if that attribute isn't defined
     */
    public final String getTextAttribute() {
        return getAttributeDirect("text");
    }

    /**
     * Returns the value of the attribute {@code link}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code link} or an empty string if that attribute isn't defined
     */
    public final String getLinkAttribute() {
        return getAttributeDirect("link");
    }

    /**
     * Returns the value of the attribute {@code vlink}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code vlink} or an empty string if that attribute isn't defined
     */
    public final String getVlinkAttribute() {
        return getAttributeDirect("vlink");
    }

    /**
     * Returns the value of the attribute {@code alink}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code alink} or an empty string if that attribute isn't defined
     */
    public final String getAlinkAttribute() {
        return getAttributeDirect("alink");
    }

    /**
     * Returns {@code true} if this body is temporary (created because the <code>body</code> tag
     * has not yet been parsed).
     *
     * @return {@code true} if this body is temporary (created because the <code>body</code> tag
     *         has not yet been parsed)
     */
    public final boolean isTemporary() {
        return temporary_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(final Event event) {
        if (Event.TYPE_BLUR.equals(event.getType()) || Event.TYPE_FOCUS.equals(event.getType())) {
            return true;
        }
        return super.handles(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(final ComputedCssStyleDeclaration style) {
        if (getPage().getWebClient().getBrowserVersion().hasFeature(JS_BODY_MARGINS_8)) {
            style.setDefaultLocalStyleAttribute("margin", "8px");
            style.setDefaultLocalStyleAttribute("padding", "0px");
        }
        else {
            style.setDefaultLocalStyleAttribute("margin-left", "8px");
            style.setDefaultLocalStyleAttribute("margin-right", "8px");
            style.setDefaultLocalStyleAttribute("margin-top", "8px");
            style.setDefaultLocalStyleAttribute("margin-bottom", "8px");
        }
    }
}
