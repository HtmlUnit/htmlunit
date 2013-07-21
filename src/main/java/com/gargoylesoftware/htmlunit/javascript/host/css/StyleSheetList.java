/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;

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
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class StyleSheetList extends SimpleScriptable {

    /**
     * We back the stylesheet list with an {@link HTMLCollection} of styles/links because this list
     * must be "live".
     */
    private HTMLCollection nodes_;

    /**
     * Rhino requires default constructors.
     */
    public StyleSheetList() {
        // Empty.
    }

    /**
     * Creates a new style sheet list owned by the specified document.
     *
     * @param document the owning document
     */
    public StyleSheetList(final HTMLDocument document) {
        setParentScope(document);
        setPrototype(getPrototype(getClass()));

        final boolean cssEnabled = getWindow().getWebWindow().getWebClient().getOptions().isCssEnabled();
        if (cssEnabled) {
            nodes_ = new HTMLCollection(document.getDomNodeOrDie(), true, "stylesheets") {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return node instanceof HtmlStyle
                        || (node instanceof HtmlLink
                            && "stylesheet".equalsIgnoreCase(((HtmlLink) node).getRelAttribute()));
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
            nodes_ = HTMLCollection.emptyCollection(getWindow());
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
        if (index < 0) {
            throw Context.reportRuntimeError("Invalid negative index: " + index);
        }
        else if (index >= nodes_.getLength()) {
            if (getWindow().getWebWindow().getWebClient().getBrowserVersion().hasFeature(
                    BrowserVersionFeatures.JS_STYLESHEET_LIST_EXEPTION_FOR_ALL_INVALID_INDEXES)) {
                throw Context.reportRuntimeError("Invalid index: " + index);
            }
            return Context.getUndefinedValue();
        }

        final HTMLElement element = (HTMLElement) nodes_.item(Integer.valueOf(index));

        final CSSStyleSheet sheet;
        // <style type="text/css"> ... </style>
        if (element instanceof HTMLStyleElement) {
            sheet = ((HTMLStyleElement) element).getSheet();
        }
        else {
            // <link rel="stylesheet" type="text/css" href="..." />
            sheet = ((HTMLLinkElement) element).getSheet();
        }

        return sheet;
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
}
