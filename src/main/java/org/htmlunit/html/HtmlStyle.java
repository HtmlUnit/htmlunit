/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.Cache;
import org.htmlunit.SgmlPage;
import org.htmlunit.css.CssStyleSheet;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.parser.InputSource;

/**
 * Wrapper for the HTML element "style".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class HtmlStyle extends HtmlElement {

    private static final Log LOG = LogFactory.getLog(HtmlStyle.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "style";

    private CssStyleSheet sheet_;

    /**
     * Creates an instance of HtmlStyle
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlStyle(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type} or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttributeDirect(TYPE_ATTRIBUTE);
    }

    /**
     * Sets the value of the attribute {@code type}.
     *
     * @param type the new type
     */
    public final void setTypeAttribute(final String type) {
        setAttribute(TYPE_ATTRIBUTE, type);
    }

    /**
     * Returns the value of the attribute {@code media}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code media} or an empty string if that attribute isn't defined
     */
    public final String getMediaAttribute() {
        return getAttributeDirect("media");
    }

    /**
     * Returns the value of the attribute {@code title}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code title} or an empty string if that attribute isn't defined
     */
    public final String getTitleAttribute() {
        return getAttributeDirect("title");
    }

    /**
     * {@inheritDoc}
     * @return {@code true} to make generated XML readable as HTML on Firefox 3 for instance.
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.NONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayBeDisplayed() {
        return false;
    }

    /**
     * @return the referenced style sheet
     */
    public CssStyleSheet getSheet() {
        if (sheet_ != null) {
            return sheet_;
        }

        final Cache cache = getPage().getWebClient().getCache();
        final CSSStyleSheetImpl cached = cache.getCachedStyleSheet(getTextContent());
        final String uri = getPage().getWebResponse().getWebRequest().getUrl().toExternalForm();

        if (cached != null) {
            sheet_ = new CssStyleSheet(this, cached, uri);
        }
        else {
            final String css = getTextContent();
            try (InputSource source = new InputSource(new StringReader(css))) {
                sheet_ = new CssStyleSheet(this, source, uri);
                cache.cache(css, sheet_.getWrappedSheet());
            }
            catch (final IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return sheet_;
    }
}
