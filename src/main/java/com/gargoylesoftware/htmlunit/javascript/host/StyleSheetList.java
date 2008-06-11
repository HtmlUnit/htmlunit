/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.StringReader;
import java.util.WeakHashMap;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.w3c.css.sac.InputSource;

import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * <p>An ordered list of stylesheets, accessible via <tt>document.styleSheets</tt>, as specified by the
 * <a href="http://www.w3.org/TR/DOM-Level-2-Style/stylesheets.html#StyleSheets-StyleSheetList">DOM
 * Level 2 Style spec</a> and the <a href="http://developer.mozilla.org/en/docs/DOM:document.styleSheets">Gecko
 * DOM Guide</a>.</p>
 *
 * <p>If CSS is disabled via {@link com.gargoylesoftware.htmlunit.WebClient#setCssEnabled(boolean)}, instances
 * of this class will always be empty. This allows us to check for CSS enablement/disablement in a single
 * location, without having to sprinkle checks throughout the code.</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class StyleSheetList extends SimpleScriptable {

    private static final long serialVersionUID = -8607630805490604483L;

    /**
     * We back the stylesheet list with an {@link HTMLCollection} of styles/links because this list
     * must be "live".
     */
    private HTMLCollection nodes_;

    /**
     * Cache the stylesheets parsed for each style/link node, but don't keep the garbage collector
     * from releasing the associated nodes if they are removed from the document.
     */
    private WeakHashMap<DomNode, Stylesheet> sheets_ = new WeakHashMap<DomNode, Stylesheet>();

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

        nodes_ = new HTMLCollection(document);

        final boolean cssEnabled = getWindow().getWebWindow().getWebClient().isCssEnabled();
        if (cssEnabled) {
            nodes_.init(document.getHtmlPage(), ".//style | .//link[lower-case(@rel)='stylesheet']");
        }
    }

    /**
     * Returns the list's length.
     *
     * @return the list's length
     */
    public int jsxGet_length() {
        return nodes_.jsxGet_length();
    }

    /**
     * Returns the style sheet at the specified index.
     *
     * @param index the index of the style sheet to return
     * @return the style sheet at the specified index
     */
    public Stylesheet jsxFunction_item(final int index) {
        final HTMLElement element = (HTMLElement) nodes_.jsxFunction_item(new Integer(index));
        final DomNode node = element.getDomNodeOrDie();
        Stylesheet sheet = sheets_.get(node);
        if (sheet == null) {
            if (node instanceof HtmlStyle) {
                final HtmlStyle style = (HtmlStyle) node;
                String styleText = "";
                if (style.getFirstChild() != null) {
                    styleText = style.getFirstChild().asText();
                }
                sheet = new Stylesheet(element, new InputSource(new StringReader(styleText)));
            }
            else {
                final HtmlLink link = (HtmlLink) node;
                try {
                    final HtmlPage htmlPage = link.getPage();
                    final WebRequestSettings webRequestSettings =
                        new WebRequestSettings(htmlPage.getFullyQualifiedUrl(link.getHrefAttribute()));
                    final String content =
                        htmlPage.getWebClient().loadWebResponse(webRequestSettings).getContentAsString();
                    final InputSource source = new InputSource(new StringReader(content));
                    sheet = new Stylesheet(element, source);
                }
                catch (final Exception e) {
                    throw Context.reportRuntimeError("Exception: " + e);
                }
            }
            sheets_.put(node, sheet);
        }
        return sheet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (this == start) {
            return jsxFunction_item(index);
        }
        return super.get(index, start);
    }
}
