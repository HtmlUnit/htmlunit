/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLESHEETLIST_ACTIVE_ONLY;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * <p>An ordered list of stylesheets, accessible via <tt>document.styleSheets</tt>, as specified by the
 * <a href="http://www.w3.org/TR/DOM-Level-2-Style/stylesheets.html#StyleSheets-StyleSheetList">DOM
 * Level 2 Style spec</a> and the <a href="https://developer.mozilla.org/en-US/docs/DOM/document.styleSheets">Gecko
 * DOM Guide</a>.</p>
 *
 * <p>If CSS is disabled via {@link com.gargoylesoftware.htmlunit.WebClientOptions#setCssEnabled(boolean)}, instances
 * of this class will always be empty. This allows us to check for CSS enablement/disablement in a single
 * location, without having to sprinkle checks throughout the code.</p>
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
@JsxClass
public class StyleSheetList extends SimpleScriptable {

    /**
     * We back the stylesheet list with an {@link HTMLCollection} of styles/links because this list must be "live".
     */
    private HTMLCollection nodes_;

    /**
     * Verifies if the provided node is a link node pointing to a stylesheet.
     *
     * @param domNode the mode to check
     * @return true if the provided node is a stylesheet link
     */
    public static boolean isStyleSheetLink(final DomNode domNode) {
        if (domNode instanceof HtmlLink) {
            final HtmlLink link = (HtmlLink) domNode;
            String rel = link.getRelAttribute();
            if (rel != null) {
                rel = rel.trim();
            }
            return "stylesheet".equalsIgnoreCase(rel);
        }
        return false;
    }

    /**
     * Verifies if the provided node is a link node pointing to an active stylesheet.
     *
     * @param domNode the mode to check
     * @return true if the provided node is a stylesheet link
     */
    public boolean isActiveStyleSheetLink(final DomNode domNode) {
        if (domNode instanceof HtmlLink) {
            final HtmlLink link = (HtmlLink) domNode;
            String rel = link.getRelAttribute();
            if (rel != null) {
                rel = rel.trim();
            }
            if ("stylesheet".equalsIgnoreCase(rel)) {
                final String media = link.getMediaAttribute();
                if (StringUtils.isBlank(media)) {
                    return true;
                }
                final WebClient webClient = getWindow().getWebWindow().getWebClient();
                final MediaListImpl mediaList = CSSStyleSheet.parseMedia(webClient.getCssErrorHandler(), media);
                return CSSStyleSheet.isActive(this, mediaList);
            }
        }
        return false;
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public StyleSheetList() {
    }

    /**
     * Creates a new style sheet list owned by the specified document.
     *
     * @param document the owning document
     */
    public StyleSheetList(final Document document) {
        setParentScope(document);
        setPrototype(getPrototype(getClass()));

        final WebClient webClient = getWindow().getWebWindow().getWebClient();

        if (webClient.getOptions().isCssEnabled()) {
            final boolean onlyActive = webClient.getBrowserVersion().hasFeature(JS_STYLESHEETLIST_ACTIVE_ONLY);
            nodes_ = new HTMLCollection(document.getDomNodeOrDie(), true) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    if (node instanceof HtmlStyle) {
                        return true;
                    }
                    if (onlyActive) {
                        return isActiveStyleSheetLink(node);
                    }
                    return isStyleSheetLink(node);
                }

                @Override
                protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                    final HtmlElement node = event.getHtmlElement();
                    if (node instanceof HtmlLink && "rel".equalsIgnoreCase(event.getName())) {
                        return EffectOnCache.RESET;
                    }
                    return EffectOnCache.NONE;
                }
            };
        }
        else {
            nodes_ = HTMLCollection.emptyCollection(getWindow().getDomNodeOrDie());
        }
    }

    /**
     * Returns the list's length.
     *
     * @return the list's length
     */
    @JsxGetter
    public int getLength() {
        return nodes_.getLength();
    }

    /**
     * Returns the style sheet at the specified index.
     *
     * @param index the index of the style sheet to return
     * @return the style sheet at the specified index
     */
    @JsxFunction
    public Object item(final int index) {
        if (nodes_ == null || index < 0 || index >= nodes_.getLength()) {
            return Undefined.instance;
        }

        final HTMLElement element = (HTMLElement) nodes_.item(Integer.valueOf(index));

        // <style type="text/css"> ... </style>
        if (element instanceof HTMLStyleElement) {
            return ((HTMLStyleElement) element).getSheet();
        }
        // <link rel="stylesheet" type="text/css" href="..." />
        return ((HTMLLinkElement) element).getSheet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (this == start) {
            return item(index);
        }
        return super.get(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object equivalentValues(final Object value) {
        return value != null
                && getClass() == value.getClass()
                && getDomNodeOrNull() == ((StyleSheetList) value).getDomNodeOrNull();
    }
}
