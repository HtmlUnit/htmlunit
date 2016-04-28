/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Wrapper for the HTML element "object".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlObject extends HtmlElement {

    private static final Log LOG = LogFactory.getLog(HtmlObject.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "object";

    /**
     * Creates an instance of HtmlObject
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlObject(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute {@code declare}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code declare}
     * or an empty string if that attribute isn't defined.
     */
    public final String getDeclareAttribute() {
        return getAttribute("declare");
    }

    /**
     * Returns the value of the attribute {@code classid}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code classid}
     * or an empty string if that attribute isn't defined.
     */
    public final String getClassIdAttribute() {
        return getAttribute("classid");
    }

    /**
     * Returns the value of the attribute "codebase". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "codebase"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCodebaseAttribute() {
        return getAttribute("codebase");
    }

    /**
     * Returns the value of the attribute {@code data}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code data}
     * or an empty string if that attribute isn't defined.
     */
    public final String getDataAttribute() {
        return getAttribute("data");
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type}
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttribute("type");
    }

    /**
     * Returns the value of the attribute "codetype". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "codetype"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCodeTypeAttribute() {
        return getAttribute("codetype");
    }

    /**
     * Returns the value of the attribute {@code archive}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code archive}
     * or an empty string if that attribute isn't defined.
     */
    public final String getArchiveAttribute() {
        return getAttribute("archive");
    }

    /**
     * Returns the value of the attribute {@code standby}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code standby}
     * or an empty string if that attribute isn't defined.
     */
    public final String getStandbyAttribute() {
        return getAttribute("standby");
    }

    /**
     * Returns the value of the attribute {@code height}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code height}
     * or an empty string if that attribute isn't defined.
     */
    public final String getHeightAttribute() {
        return getAttribute("height");
    }

    /**
     * Returns the value of the attribute {@code width}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code width}
     * or an empty string if that attribute isn't defined.
     */
    public final String getWidthAttribute() {
        return getAttribute("width");
    }

    /**
     * Returns the value of the attribute {@code usemap}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code usemap}
     * or an empty string if that attribute isn't defined.
     */
    public final String getUseMapAttribute() {
        return getAttribute("usemap");
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name}
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex}
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttribute("tabindex");
    }

    /**
     * Returns the value of the attribute {@code align}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code align}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttribute("align");
    }

    /**
     * Returns the value of the attribute {@code border}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code border}
     * or an empty string if that attribute isn't defined.
     */
    public final String getBorderAttribute() {
        return getAttribute("border");
    }

    /**
     * Returns the value of the attribute {@code hspace}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code hspace}
     * or an empty string if that attribute isn't defined.
     */
    public final String getHspaceAttribute() {
        return getAttribute("hspace");
    }

    /**
     * Returns the value of the attribute {@code vspace}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code vspace}
     * or an empty string if that attribute isn't defined.
     */
    public final String getVspaceAttribute() {
        return getAttribute("vspace");
    }

    /**
     * Initialize the ActiveX(Mock).
     * {@inheritDoc}
     */
    @Override
    protected void onAllChildrenAddedToPage(final boolean postponed) {
        if (getOwnerDocument() instanceof XmlPage) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Object node added: " + asXml());
        }

        final String clsId = getClassIdAttribute();
        if (ATTRIBUTE_NOT_DEFINED != clsId) {
            ((HTMLObjectElement) getScriptableObject()).setClassid(clsId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE;
    }
}
