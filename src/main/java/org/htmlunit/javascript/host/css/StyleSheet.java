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
package org.htmlunit.javascript.host.css;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlLink;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlStyle;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for {@code StyleSheet}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class StyleSheet extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(StyleSheet.class);

    /** The HTML element which owns this stylesheet. */
    private final HTMLElement ownerNode_;

    /**
     * Default constructor.
     */
    public StyleSheet() {
        ownerNode_ = null;
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    public StyleSheet(final HTMLElement ownerNode) {
        ownerNode_ = ownerNode;
    }

    /**
     * Returns the owner node.
     * @return the owner node
     */
    @JsxGetter
    public HTMLElement getOwnerNode() {
        return ownerNode_;
    }

    /**
     * Returns the URL of the stylesheet.
     * @return the URL of the stylesheet
     */
    @JsxGetter
    public String getHref() {
        if (ownerNode_ != null) {
            final DomNode node = ownerNode_.getDomNodeOrDie();
            if (node instanceof HtmlStyle) {
                return null;
            }
            if (node instanceof HtmlLink) {
                // <link rel="stylesheet" type="text/css" href="..." />
                final HtmlLink link = (HtmlLink) node;
                final String href = link.getHrefAttribute();
                // Expand relative URLs.
                try {
                    final HtmlPage page = (HtmlPage) link.getPage();
                    final URL url = page.getFullyQualifiedUrl(href);
                    return url.toExternalForm();
                }
                catch (final MalformedURLException e) {
                    // Log the error and fall through to the return values below.
                    LOG.warn(e.getMessage(), e);
                }
            }
        }

        return getUri();
    }

    /**
     * Returns this stylesheet's URI (used to resolved contained @import rules).
     * For inline styles this is the page uri.
     * @return this stylesheet's URI (used to resolved contained @import rules)
     */
    public String getUri() {
        return null;
    }
}
