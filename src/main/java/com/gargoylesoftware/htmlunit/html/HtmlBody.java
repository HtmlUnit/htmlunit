/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "body".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlBody extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "body";

    /** Whether or not this body is temporary (created because the <tt>body</tt> tag has not yet been parsed). */
    private final boolean temporary_;

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     * @param temporary whether or not this body is temporary (created because the <tt>body</tt>
     *        tag does not exist or has not yet been parsed)
     */
    public HtmlBody(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes, final boolean temporary) {

        super(namespaceURI, qualifiedName, page, attributes);

        temporary_ = temporary;

        // Force script object creation now to forward onXXX handlers to window.
        if (getOwnerDocument() instanceof HtmlPage) {
            getScriptObject();
        }
    }

    /**
     * Returns the value of the attribute "onload". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onload" or an empty string if that attribute isn't defined
     */
    public final String getOnLoadAttribute() {
        return getAttribute("onload");
    }

    /**
     * Returns the value of the attribute "onunload". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onunload" or an empty string if that attribute isn't defined
     */
    public final String getOnUnloadAttribute() {
        return getAttribute("onunload");
    }

    /**
     * Returns the value of the attribute "background". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "background" or an empty string if that attribute isn't defined
     */
    public final String getBackgroundAttribute() {
        return getAttribute("background");
    }

    /**
     * Returns the value of the attribute "bgcolor". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "bgcolor" or an empty string if that attribute isn't defined
     */
    public final String getBgcolorAttribute() {
        return getAttribute("bgcolor");
    }

    /**
     * Returns the value of the attribute "text". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "text" or an empty string if that attribute isn't defined
     */
    public final String getTextAttribute() {
        return getAttribute("text");
    }

    /**
     * Returns the value of the attribute "link". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "link" or an empty string if that attribute isn't defined
     */
    public final String getLinkAttribute() {
        return getAttribute("link");
    }

    /**
     * Returns the value of the attribute "vlink". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "vlink" or an empty string if that attribute isn't defined
     */
    public final String getVlinkAttribute() {
        return getAttribute("vlink");
    }

    /**
     * Returns the value of the attribute "alink". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "alink" or an empty string if that attribute isn't defined
     */
    public final String getAlinkAttribute() {
        return getAttribute("alink");
    }

    /**
     * Returns <tt>true</tt> if this body is temporary (created because the <tt>body</tt> tag
     * has not yet been parsed).
     *
     * @return <tt>true</tt> if this body is temporary (created because the <tt>body</tt> tag
     *         has not yet been parsed)
     */
    public final boolean isTemporary() {
        return temporary_;
    }

}
