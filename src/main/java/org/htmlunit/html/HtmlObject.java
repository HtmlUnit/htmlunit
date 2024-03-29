/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.html;

import static org.htmlunit.BrowserVersionFeatures.JS_HTML_OBJECT_VALIDITYSTATE_ISVALID_IGNORES_CUSTOM_ERROR;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.SgmlPage;
import org.htmlunit.javascript.host.html.HTMLObjectElement;
import org.htmlunit.xml.XmlPage;

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
public class HtmlObject extends HtmlElement implements ValidatableElement {

    private static final Log LOG = LogFactory.getLog(HtmlObject.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "object";

    private String customValidity_;

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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code declare}
     * or an empty string if that attribute isn't defined.
     */
    public final String getDeclareAttribute() {
        return getAttributeDirect("declare");
    }

    /**
     * Returns the value of the attribute {@code classid}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code classid}
     * or an empty string if that attribute isn't defined.
     */
    public final String getClassIdAttribute() {
        return getAttributeDirect("classid");
    }

    /**
     * Returns the value of the attribute "codebase". Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "codebase"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCodebaseAttribute() {
        return getAttributeDirect("codebase");
    }

    /**
     * Returns the value of the attribute {@code data}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code data}
     * or an empty string if that attribute isn't defined.
     */
    public final String getDataAttribute() {
        return getAttributeDirect("data");
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type}
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttributeDirect(TYPE_ATTRIBUTE);
    }

    /**
     * Returns the value of the attribute "codetype". Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "codetype"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCodeTypeAttribute() {
        return getAttributeDirect("codetype");
    }

    /**
     * Returns the value of the attribute {@code archive}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code archive}
     * or an empty string if that attribute isn't defined.
     */
    public final String getArchiveAttribute() {
        return getAttributeDirect("archive");
    }

    /**
     * Returns the value of the attribute {@code standby}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code standby}
     * or an empty string if that attribute isn't defined.
     */
    public final String getStandbyAttribute() {
        return getAttributeDirect("standby");
    }

    /**
     * Returns the value of the attribute {@code height}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code height}
     * or an empty string if that attribute isn't defined.
     */
    public final String getHeightAttribute() {
        return getAttributeDirect("height");
    }

    /**
     * Returns the value of the attribute {@code width}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code width}
     * or an empty string if that attribute isn't defined.
     */
    public final String getWidthAttribute() {
        return getAttributeDirect("width");
    }

    /**
     * Returns the value of the attribute {@code usemap}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code usemap}
     * or an empty string if that attribute isn't defined.
     */
    public final String getUseMapAttribute() {
        return getAttributeDirect("usemap");
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
        return getAttributeDirect(NAME_ATTRIBUTE);
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex}
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeDirect("tabindex");
    }

    /**
     * Returns the value of the attribute {@code align}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code align}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttributeDirect("align");
    }

    /**
     * Returns the value of the attribute {@code border}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code border}
     * or an empty string if that attribute isn't defined.
     */
    public final String getBorderAttribute() {
        return getAttributeDirect("border");
    }

    /**
     * Returns the value of the attribute {@code hspace}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code hspace}
     * or an empty string if that attribute isn't defined.
     */
    public final String getHspaceAttribute() {
        return getAttributeDirect("hspace");
    }

    /**
     * Returns the value of the attribute {@code vspace}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code vspace}
     * or an empty string if that attribute isn't defined.
     */
    public final String getVspaceAttribute() {
        return getAttributeDirect("vspace");
    }

    /**
     * Initialize the clsid.
     * {@inheritDoc}
     */
    @Override
    public void onAllChildrenAddedToPage(final boolean postponed) {
        if (getOwnerDocument() instanceof XmlPage) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Object node added: " + asXml());
        }

        final String clsId = getClassIdAttribute();
        if (ATTRIBUTE_NOT_DEFINED != clsId && getPage().getWebClient().isJavaScriptEngineEnabled()) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willValidate() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCustomValidity(final String message) {
        customValidity_ = message;
    }

    @Override
    public boolean isCustomErrorValidityState() {
        if (hasFeature(JS_HTML_OBJECT_VALIDITYSTATE_ISVALID_IGNORES_CUSTOM_ERROR)) {
            return false;
        }

        return !StringUtils.isEmpty(customValidity_);
    }

    @Override
    public boolean isValidValidityState() {
        return !isCustomErrorValidityState();
    }
}
