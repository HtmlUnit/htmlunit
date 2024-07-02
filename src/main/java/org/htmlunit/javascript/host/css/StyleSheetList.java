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

import java.io.Serializable;
import java.util.function.Predicate;

import org.htmlunit.WebClient;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlAttributeChangeEvent;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlLink;
import org.htmlunit.html.HtmlStyle;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.javascript.host.html.HTMLCollection;
import org.htmlunit.javascript.host.html.HTMLElement;
import org.htmlunit.javascript.host.html.HTMLLinkElement;
import org.htmlunit.javascript.host.html.HTMLStyleElement;

/**
 * <p>An ordered list of stylesheets, accessible via <code>document.styleSheets</code>, as specified by the
 * <a href="http://www.w3.org/TR/DOM-Level-2-Style/stylesheets.html#StyleSheets-StyleSheetList">DOM
 * Level 2 Style spec</a> and the <a href="https://developer.mozilla.org/en-US/docs/DOM/document.styleSheets">Gecko
 * DOM Guide</a>.</p>
 *
 * <p>If CSS is disabled via {@link org.htmlunit.WebClientOptions#setCssEnabled(boolean)}, instances
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
public class StyleSheetList extends HtmlUnitScriptable {

    /**
     * We back the stylesheet list with an {@link HTMLCollection} of styles/links because this list must be "live".
     */
    private HTMLCollection nodes_;

    /**
     * Creates an instance.
     */
    public StyleSheetList() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Creates a new style sheet list owned by the specified document.
     *
     * @param document the owning document
     */
    public StyleSheetList(final Document document) {
        super();
        setParentScope(document);
        setPrototype(getPrototype(getClass()));

        final WebClient webClient = getWindow().getWebWindow().getWebClient();

        if (webClient.getOptions().isCssEnabled()) {
            nodes_ = new HTMLCollection(document.getDomNodeOrDie(), true);

            nodes_.setEffectOnCacheFunction(
                    (java.util.function.Function<HtmlAttributeChangeEvent, EffectOnCache> & Serializable)
                    event -> {
                        final HtmlElement node = event.getHtmlElement();
                        if (node instanceof HtmlLink && "rel".equalsIgnoreCase(event.getName())) {
                            return EffectOnCache.RESET;
                        }
                        return EffectOnCache.NONE;
                    });

            nodes_.setIsMatchingPredicate(
                    (Predicate<DomNode> & Serializable)
                    node -> {
                        if (node instanceof HtmlStyle) {
                            return true;
                        }
                        if (node instanceof HtmlLink) {
                            return ((HtmlLink) node).isActiveStyleSheetLink();
                        }
                        return false;
                    });
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
            return JavaScriptEngine.Undefined;
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
