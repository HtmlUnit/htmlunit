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
public class HtmlBody extends ClickableElement {

    /** Serial version UID. */
    private static final long serialVersionUID = -4133102076637734903L;

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
        getScriptObject();
    }

    /**
     * Returns the value of the attribute "onload". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onload" or an empty string if that attribute isn't defined
     */
    public final String getOnLoadAttribute() {
        return getAttributeValue("onload");
    }

    /**
     * Returns the value of the attribute "onunload". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onunload" or an empty string if that attribute isn't defined
     */
    public final String getOnUnloadAttribute() {
        return getAttributeValue("onunload");
    }

    /**
     * Returns the value of the attribute "background". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "background" or an empty string if that attribute isn't defined
     */
    public final String getBackgroundAttribute() {
        return getAttributeValue("background");
    }

    /**
     * Returns the value of the attribute "bgcolor". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "bgcolor" or an empty string if that attribute isn't defined
     */
    public final String getBgcolorAttribute() {
        return getAttributeValue("bgcolor");
    }

    /**
     * Returns the value of the attribute "text". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "text" or an empty string if that attribute isn't defined
     */
    public final String getTextAttribute() {
        return getAttributeValue("text");
    }

    /**
     * Returns the value of the attribute "link". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "link" or an empty string if that attribute isn't defined
     */
    public final String getLinkAttribute() {
        return getAttributeValue("link");
    }

    /**
     * Returns the value of the attribute "vlink". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "vlink" or an empty string if that attribute isn't defined
     */
    public final String getVlinkAttribute() {
        return getAttributeValue("vlink");
    }

    /**
     * Returns the value of the attribute "alink". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "alink" or an empty string if that attribute isn't defined
     */
    public final String getAlinkAttribute() {
        return getAttributeValue("alink");
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
