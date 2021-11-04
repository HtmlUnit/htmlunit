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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLESHEETLIST_ACTIVE_ONLY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_COMPUTED_STYLE_PSEUDO_ACCEPT_WITHOUT_COLON;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;

import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.htmlunit.css.ComputedCssStyleDeclaration;
import com.gargoylesoftware.htmlunit.css.CssStyleDeclaration;
import com.gargoylesoftware.htmlunit.css.CssStyleDeclarationBuilder;
import com.gargoylesoftware.htmlunit.css.CssStyleSheet;
import com.gargoylesoftware.htmlunit.html.AbstractDomNodeList;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeIterator;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.DomTreeWalker;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;

/**
 * A basic class of Standard Generalized Markup Language (SGML), e.g. HTML and XML.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public abstract class SgmlPage extends DomNode implements Page, Document, DocumentTraversal {

    private DocumentType documentType_;
    private final WebResponse webResponse_;
    private WebWindow enclosingWindow_;
    private final WebClient webClient_;
    private transient List<CssStyleSheet> styleSheetList_;
    private final CSSPropertiesCache cssPropertiesCache_;

    /**
     * Cache computed styles when possible, because their calculation is very expensive.
     * We use a weak hash map because we don't want this cache to be the only reason
     * nodes are kept around in the JVM, if all other references to them are gone.
     */
    private static final class CSSPropertiesCache implements Serializable {
        private transient WeakHashMap<DomElement, Map<String, ComputedCssStyleDeclaration>> computedStyles_ = new WeakHashMap<>();

        CSSPropertiesCache() {
        }

        public synchronized ComputedCssStyleDeclaration get(final DomElement element, final String normalizedPseudo) {
            final Map<String, ComputedCssStyleDeclaration> elementMap = computedStyles_.get(element);
            if (elementMap != null) {
                return elementMap.get(normalizedPseudo);
            }
            return null;
        }

        public synchronized void put(final DomElement element, final String normalizedPseudo, final ComputedCssStyleDeclaration style) {
            Map<String, ComputedCssStyleDeclaration> elementMap = computedStyles_.get(element);
            if (elementMap == null) {
                elementMap = new WeakHashMap<>();
                computedStyles_.put(element, elementMap);
            }
            elementMap.put(normalizedPseudo, style);
        }

        public synchronized void nodeChanged(final DomNode changed, final boolean clearParents) {
            final Iterator<Entry<DomElement, Map<String, ComputedCssStyleDeclaration>>> i = computedStyles_.entrySet().iterator();
            while (i.hasNext()) {
                final Map.Entry<DomElement, Map<String, ComputedCssStyleDeclaration>> entry = i.next();
                final DomNode node = entry.getKey();
                if (changed == node
                    || changed.getParentNode() == node.getParentNode()
                    || changed.isAncestorOf(node)
                    || clearParents && node.isAncestorOf(changed)) {
                    i.remove();
                }
            }

            // maybe this is a better solution but i have to think a bit more about this
            //
            //            if (computedStyles_.isEmpty()) {
            //                return;
            //            }
            //
            //            // remove all siblings
            //            DomNode parent = changed.getParentNode();
            //            if (parent != null) {
            //                for (DomNode sibling : parent.getChildNodes()) {
            //                    computedStyles_.remove(sibling.getScriptableObject());
            //                }
            //
            //                if (clearParents) {
            //                    // remove all parents
            //                    while (parent != null) {
            //                        computedStyles_.remove(parent.getScriptableObject());
            //                        parent = parent.getParentNode();
            //                    }
            //                }
            //            }
            //
            //            // remove changed itself and all descendants
            //            computedStyles_.remove(changed.getScriptableObject());
            //            for (DomNode descendant : changed.getDescendants()) {
            //                computedStyles_.remove(descendant.getScriptableObject());
            //            }
        }

        public synchronized void clear() {
            computedStyles_.clear();
        }

        public synchronized Map<String, ComputedCssStyleDeclaration> remove(final com.gargoylesoftware.htmlunit.javascript.host.Element element) {
            return computedStyles_.remove(element.getDomNodeOrNull());
        }

        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            computedStyles_ = new WeakHashMap<>();
        }
    }

    /**
     * Creates an instance of SgmlPage.
     *
     * @param webResponse the web response that was used to create this page
     * @param webWindow the window that this page is being loaded into
     */
    public SgmlPage(final WebResponse webResponse, final WebWindow webWindow) {
        super(null);
        webResponse_ = webResponse;
        enclosingWindow_ = webWindow;
        webClient_ = webWindow.getWebClient();
        cssPropertiesCache_ = new CSSPropertiesCache();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanUp() {
        if (getWebClient().getCache().getCachedResponse(webResponse_.getWebRequest()) == null) {
            webResponse_.cleanUp();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * Gets the name for the current node.
     * @return the node name
     */
    @Override
    public String getNodeName() {
        return "#document";
    }

    /**
     * Gets the type of the current node.
     * @return the node type
     */
    @Override
    public short getNodeType() {
        return DOCUMENT_NODE;
    }

    /**
     * Returns the window that this page is sitting inside.
     *
     * @return the enclosing frame or null if this page isn't inside a frame
     */
    @Override
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }

    /**
     * Sets the window that contains this page.
     *
     * @param window the new frame or null if this page is being removed from a frame
     */
    public void setEnclosingWindow(final WebWindow window) {
        enclosingWindow_ = window;
    }

    /**
     * Returns the WebClient that originally loaded this page.
     *
     * @return the WebClient that originally loaded this page
     */
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     * Creates an empty {@link DomDocumentFragment} object.
     * @return a newly created {@link DomDocumentFragment}
     */
    @Override
    public DomDocumentFragment createDocumentFragment() {
        return new DomDocumentFragment(this);
    }

    /**
     * Returns the document type.
     * @return the document type
     */
    @Override
    public final DocumentType getDoctype() {
        return documentType_;
    }

    /**
     * Sets the document type.
     * @param type the document type
     */
    protected void setDocumentType(final DocumentType type) {
        documentType_ = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SgmlPage getPage() {
        return this;
    }

    /**
     * Creates an element, the type of which depends on the specified tag name.
     * @param tagName the tag name which determines the type of element to be created
     * @return an element, the type of which depends on the specified tag name
     */
    @Override
    public abstract Element createElement(String tagName);

    /**
     * Create a new Element with the given namespace and qualified name.
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @return the new element
     */
    @Override
    public abstract Element createElementNS(String namespaceURI, String qualifiedName);

    /**
     * Returns the encoding.
     * @return the encoding
     */
    public abstract Charset getCharset();

    /**
     * Returns the document element.
     * @return the document element
     */
    @Override
    public DomElement getDocumentElement() {
        DomNode childNode = getFirstChild();
        while (childNode != null && !(childNode instanceof DomElement)) {
            childNode = childNode.getNextSibling();
        }
        return (DomElement) childNode;
    }

    /**
     * Creates a clone of this instance.
     * @return a clone of this instance
     */
    @Override
    protected SgmlPage clone() {
        try {
            final SgmlPage result = (SgmlPage) super.clone();
            return result;
        }
        catch (final CloneNotSupportedException e) {
            throw new IllegalStateException("Clone not supported");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asXml() {
        final DomElement documentElement = getDocumentElement();
        if (documentElement == null) {
            return "";
        }
        return documentElement.asXml();
    }

    /**
     * Returns {@code true} if this page has case-sensitive tag names, {@code false} otherwise. In general,
     * XML has case-sensitive tag names, and HTML doesn't. This is especially important during XPath matching.
     * @return {@code true} if this page has case-sensitive tag names, {@code false} otherwise
     */
    public abstract boolean hasCaseSensitiveTagNames();

    /**
     * {@inheritDoc}
     * The current implementation just {@link DomNode#normalize()}s the document element.
     */
    @Override
    public void normalizeDocument() {
        getDocumentElement().normalize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCanonicalXPath() {
        return "/";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr createAttribute(final String name) {
        return new DomAttr(getPage(), null, name, "", false);
    }

    /**
     * Returns the URL of this page.
     * @return the URL of this page
     */
    @Override
    public URL getUrl() {
        final WebResponse wr = getWebResponse();
        if (null == wr) {
            return UrlUtils.URL_ABOUT_BLANK;
        }
        return getWebResponse().getWebRequest().getUrl();
    }

    @Override
    public boolean isHtmlPage() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNodeList<DomElement> getElementsByTagName(final String tagName) {
        return new AbstractDomNodeList<DomElement>(this) {
            @Override
            protected List<DomElement> provideElements() {
                final List<DomElement> res = new ArrayList<>();
                final boolean caseSensitive = hasCaseSensitiveTagNames();
                for (final DomElement elem : getDomElementDescendants()) {
                    final String localName = elem.getLocalName();
                    if ("*".equals(tagName) || localName.equals(tagName)
                            || (!caseSensitive && localName.equalsIgnoreCase(tagName))) {
                        res.add(elem);
                    }
                }
                return res;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNodeList<DomElement> getElementsByTagNameNS(final String namespaceURI, final String localName) {
        return new AbstractDomNodeList<DomElement>(this) {
            @Override
            protected List<DomElement> provideElements() {
                final List<DomElement> res = new ArrayList<>();
                final Comparator<String> comparator;

                if (hasCaseSensitiveTagNames()) {
                    comparator = Comparator.nullsFirst(String::compareTo);
                }
                else {
                    comparator = Comparator.nullsFirst(String::compareToIgnoreCase);
                }

                for (final DomElement elem : getDomElementDescendants()) {
                    final String locName = elem.getLocalName();

                    if (("*".equals(namespaceURI) || comparator.compare(namespaceURI, elem.getNamespaceURI()) == 0)
                            && ("*".equals(locName) || comparator.compare(locName, elem.getLocalName()) == 0)) {
                        res.add(elem);
                    }
                }
                return res;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CDATASection createCDATASection(final String data) {
        return new DomCDataSection(this, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Text createTextNode(final String data) {
        return new DomText(this, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comment createComment(final String data) {
        return new DomComment(this, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomTreeWalker createTreeWalker(final Node root, final int whatToShow, final NodeFilter filter,
            final boolean entityReferenceExpansion) throws DOMException {
        return new DomTreeWalker((DomNode) root, whatToShow, filter, entityReferenceExpansion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNodeIterator createNodeIterator(final Node root, final int whatToShow, final NodeFilter filter,
            final boolean entityReferenceExpansion) throws DOMException {
        return new DomNodeIterator((DomNode) root, whatToShow, filter, entityReferenceExpansion);
    }

    /**
     * Returns the content type of this page.
     * @return the content type of this page
     */
    public abstract String getContentType();

    /**
     * Verifies if the provided node is a link node pointing to an active stylesheet.
     * @param link the html link
     * @return true if the provided node is a stylesheet link
     */
    private boolean isActiveStyleSheetLink(final HtmlLink link) {
        String rel = link.getRelAttribute();
        if (rel != null) {
            rel = rel.trim();
        }
        if ("stylesheet".equalsIgnoreCase(rel)) {
            final String media = link.getMediaAttribute();
            if (StringUtils.isBlank(media)) {
                return true;
            }

            final MediaListImpl mediaList = CssStyleSheet.parseMedia(getWebClient().getCssErrorHandler(), media);
            return CssStyleSheet.isActive(getEnclosingWindow(), mediaList);
        }
        return false;
    }

    /**
     * Retrieves a collection of stylesheet objects representing the style sheets that correspond
     * to each instance of a Link or
     * {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration} object in the document.
     *
     * @return styleSheet collection
     */
    public List<CssStyleSheet> getStyleSheets() {
        if (styleSheetList_ == null) {
            final boolean onlyActive = getWebClient().getBrowserVersion().hasFeature(JS_STYLESHEETLIST_ACTIVE_ONLY);
            styleSheetList_ = StreamSupport.stream(getDescendants().spliterator(), false)
                .map(node -> {
                    // <style type="text/css"> ... </style>
                    if (node instanceof HtmlStyle) {
                        return ((HtmlStyle) node).getSheet();
                    }
                    // <link rel="stylesheet" type="text/css" href="..." />
                    if (node instanceof HtmlLink) {
                        boolean valid;
                        HtmlLink link = (HtmlLink) node;
                        if (onlyActive) {
                            valid = isActiveStyleSheetLink(link);
                        } else {
                            valid = link.isStyleSheetLink();
                        }
                        if (valid)
                            return link.getSheet();
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
        return styleSheetList_;
    }

    public void resetCachedStyleSheets() {
        styleSheetList_ = null;
    }

    public ComputedCssStyleDeclaration getComputedStyle(final DomElement domNode, final String pseudoElement) {
        String normalizedPseudo = pseudoElement;
        if (normalizedPseudo != null) {
            if (normalizedPseudo.startsWith("::")) {
                normalizedPseudo = normalizedPseudo.substring(1);
            }
            else if (getWebClient().getBrowserVersion().hasFeature(JS_WINDOW_COMPUTED_STYLE_PSEUDO_ACCEPT_WITHOUT_COLON)
                && normalizedPseudo.length() > 0 && normalizedPseudo.charAt(0) != ':') {
                normalizedPseudo = ":" + normalizedPseudo;
            }
        }

        final ComputedCssStyleDeclaration styleFromCache = cssPropertiesCache_.get(domNode, normalizedPseudo);
        if (styleFromCache != null) {
            return styleFromCache;
        }

        final Collection<CssStyleSheet> sheets =  domNode.getPage().getStyleSheets();
        final ComputedCssStyleDeclaration style = CssStyleDeclarationBuilder.build(sheets, domNode, normalizedPseudo);

        cssPropertiesCache_.put(domNode, normalizedPseudo, style);
        return style;
    }

    /**
     * Clears the computed styles.
     */
    public void clearComputedStyles() {
        cssPropertiesCache_.clear();
    }

    /**
     * Clears the computed styles for a specific {@link com.gargoylesoftware.htmlunit.javascript.host.Element}.
     * @param element the element to clear its cache
     */
    public void clearComputedStyles(final com.gargoylesoftware.htmlunit.javascript.host.Element element) {
        cssPropertiesCache_.remove(element);
    }

    /**
     * Clears the computed styles for a specific {@link com.gargoylesoftware.htmlunit.javascript.host.Element}
     * and all parent elements.
     * @param element the element to clear its cache
     */
    public void clearComputedStylesUpToRoot(final com.gargoylesoftware.htmlunit.javascript.host.Element element) {
        cssPropertiesCache_.remove(element);

        com.gargoylesoftware.htmlunit.javascript.host.Element parent = element.getParentElement();
        while (parent != null) {
            cssPropertiesCache_.remove(parent);
            parent = parent.getParentElement();
        }
    }

    public void nodeChanged(DomNode changed, boolean clearParents) {
        cssPropertiesCache_.nodeChanged(changed, clearParents);
    }
}
