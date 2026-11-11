/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.htmlunit.BrowserVersionFeatures.CSP_OBJECT_BLOCKED_FIRES_ERROR;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.html.HTMLObjectElement;
import org.htmlunit.util.StringUtils;
import org.htmlunit.xml.XmlPage;

/**
 * Wrapper for the HTML element "object".
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Christian Sell
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlObject extends HtmlElement implements ValidatableHtmlElement {

    private static final Log LOG = LogFactory.getLog(HtmlObject.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "object";

    private String customValidity_;

    /**
     * Creates an instance of HtmlObject.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
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
     *         or an empty string if that attribute isn't defined.
     */
    public final String getVspaceAttribute() {
        return getAttributeDirect("vspace");
    }

    /**
     * {@inheritDoc}
     * Checks the data url against the Content-Security-Policy (object-src).
     */
    @Override
    protected void onAddedToPage() {
        super.onAddedToPage();
        checkContentSecurityPolicy(getAttributeDirect("data"));
    }

    /**
     * {@inheritDoc}
     * Checks the new data url against the Content-Security-Policy (object-src).
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        final String qualifiedNameLC = StringUtils.toRootLowerCase(qualifiedName);
        final boolean changed = "data".equals(qualifiedNameLC)
                && !getAttributeNS(namespaceURI, qualifiedNameLC).equals(value);

        super.setAttributeNS(namespaceURI, qualifiedName, value,
                notifyAttributeChangeListeners, notifyMutationObservers);

        if (changed && isAttachedToPage()) {
            checkContentSecurityPolicy(value);
        }
    }

    /**
     * Checks the url against the Content-Security-Policy (object-src); if the url is blocked,
     * Firefox fires the error event (async).
     */
    private void checkContentSecurityPolicy(final String data) {
        if (ContentSecurityPolicy.allowsObject(this, data)) {
            return;
        }

        final HtmlPage htmlPage = getHtmlPageOrNull();
        if (htmlPage == null || !hasFeature(CSP_OBJECT_BLOCKED_FIRES_ERROR)) {
            return;
        }

        final WebClient client = htmlPage.getWebClient();
        if (!client.isJavaScriptEngineEnabled()) {
            return;
        }

        final PostponedAction action = new PostponedAction(htmlPage, "HtmlObject.blocked") {
            @Override
            public void execute() {
                HtmlObject.this.fireEvent(Event.TYPE_ERROR);
            }
        };
        if (READY_STATE_LOADING.equals(htmlPage.getReadyState())) {
            htmlPage.addAfterLoadAction(action);
        }
        else {
            client.getJavaScriptEngine().addPostponedAction(action);
        }
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
    public String getCustomValidity() {
        return customValidity_;
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
        return !StringUtils.isEmptyOrNull(customValidity_);
    }

    @Override
    public boolean isValidValidityState() {
        return !isCustomErrorValidityState();
    }
}
