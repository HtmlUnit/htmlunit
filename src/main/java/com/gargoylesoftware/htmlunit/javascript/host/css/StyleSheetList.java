/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.css.CssStyleSheet;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;

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

    private List<CssStyleSheet> styleSheets;

    private static final class ChangeListener
        implements DomChangeListener, HtmlAttributeChangeListener {

        private Runnable clearCacheFct;

        private ChangeListener(Runnable clearCacheFct) {
            this.clearCacheFct = clearCacheFct;
        }

        @Override
        public void attributeAdded(HtmlAttributeChangeEvent event) {
            if (shouldClearCache(event)) {
                clearCacheFct.run();
            }
        }

        @Override
        public void attributeRemoved(HtmlAttributeChangeEvent event) {
            if (shouldClearCache(event)) {
                clearCacheFct.run();
            }
        }

        @Override
        public void attributeReplaced(HtmlAttributeChangeEvent event) {
            if (shouldClearCache(event)) {
                clearCacheFct.run();
            }
        }

        protected boolean shouldClearCache(final HtmlAttributeChangeEvent event) {
            final HtmlElement node = event.getHtmlElement();
            if (node instanceof HtmlLink && "rel".equalsIgnoreCase(event.getName())) {
                return true;
            }
            return false;
        }

        @Override
        public void nodeAdded(DomChangeEvent event) {
            clearCacheFct.run();
        }

        @Override
        public void nodeDeleted(DomChangeEvent event) {
            clearCacheFct.run();
        }
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public StyleSheetList() {
    }

    /**
     * Creates a new style sheet list owned by the specified document.
     * @param document the owning document
     */
    public StyleSheetList(final Document document) {
        setParentScope(document);
        setPrototype(getPrototype(getClass()));

        SgmlPage page = document.getPage();
        final WebClient webClient = page.getWebClient();

        if (webClient.getOptions().isCssEnabled()) {
            styleSheets = page.getStyleSheets();
            ChangeListener changeListener = new ChangeListener(() -> {
                page.resetCachedStyleSheets();
                styleSheets = page.getStyleSheets();
            });

            page.addDomChangeListener(changeListener);
            if(page instanceof HtmlPage){
                ((HtmlPage) page).addHtmlAttributeChangeListener(changeListener);
            }
        } else {
            styleSheets = Collections.emptyList();
        }
    }

    /**
     * Returns the list's length.
     * @return the list's length
     */
    @JsxGetter
    public int getLength() {
        return styleSheets.size();
    }

    /**
     * Returns the style sheet at the specified index.
     * @param index the index of the style sheet to return
     * @return the style sheet at the specified index
     */
    @JsxFunction
    public Object item(final int index) {
        if (styleSheets == null || index < 0 || index >= styleSheets.size()) {
            return Undefined.instance;
        }
        CssStyleSheet cssStyleSheet = styleSheets.get(index);
        return new CSSStyleSheet(cssStyleSheet);
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
