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
package org.htmlunit;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.htmlunit.html.AbstractDomNodeList;
import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomCDataSection;
import org.htmlunit.html.DomComment;
import org.htmlunit.html.DomDocumentFragment;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeIterator;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.DomText;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeFilter;

/**
 * A basic class of Standard Generalized Markup Language (SGML), e.g. HTML and XML.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public abstract class SgmlPage extends DomNode implements Page, Document {

    private DocumentType documentType_;
    private final WebResponse webResponse_;
    private WebWindow enclosingWindow_;
    private final WebClient webClient_;
    private boolean printing_;
    private boolean domChangeListenerInUse_;
    private boolean characterDataChangeListenerInUse_;

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
            return (SgmlPage) super.clone();
        }
        catch (final CloneNotSupportedException e) {
            throw new IllegalStateException("Clone not supported", e);
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
                    if (StringUtils.equalsChar('*', tagName) || localName.equals(tagName)
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

                    if ((StringUtils.equalsChar('*', namespaceURI)
                                    || comparator.compare(namespaceURI, elem.getNamespaceURI()) == 0)
                            && (StringUtils.equalsChar('*', locName)
                                    || comparator.compare(locName, elem.getLocalName()) == 0)) {
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
     * Create a new <code>NodeIterator</code> over the subtree rooted at the
     * specified node.
     * @param root The node which will be iterated together with its
     *        children. The <code>NodeIterator</code> is initially positioned
     *        just before this node. The <code>whatToShow</code> flags and the
     *        filter, if any, are not considered when setting this position. The
     *        root must not be <code>null</code>.
     * @param whatToShow This flag specifies which node types may appear in
     *        the logical view of the tree presented by the
     *        <code>NodeIterator</code>. See the description of
     *        <code>NodeFilter</code> for the set of possible <code>SHOW_</code>
     *        values.These flags can be combined using <code>OR</code>.
     * @param filter The <code>NodeFilter</code> to be used with this
     *        <code>NodeIterator</code>, or <code>null</code> to indicate no
     *        filter.
     * @param entityReferenceExpansion The value of this flag determines
     *        whether entity reference nodes are expanded.
     * @return The newly created <code>NodeIterator</code>.
     * @exception DOMException
     *            NOT_SUPPORTED_ERR: Raised if the specified <code>root</code> is <code>null</code>.
     */
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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Clears the computed styles.
     */
    public void clearComputedStyles() {
        // nothing to do here, overwritten in HtmlPage
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Clears the computed styles for a specific {@link Element}.
     * @param element the element to clear its cache
     */
    public void clearComputedStyles(final DomElement element) {
        // nothing to do here, overwritten in HtmlPage
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Clears the computed styles for a specific {@link Element}
     * and all parent elements.
     * @param element the element to clear its cache
     */
    public void clearComputedStylesUpToRoot(final DomElement element) {
        // nothing to do here, overwritten in HtmlPage
    }

    /**
     * @return whether or not this is currently printing
     */
    public boolean isPrinting() {
        return printing_;
    }

    /**
     * @param printing the printing state to set
     */
    public void setPrinting(final boolean printing) {
        printing_ = printing;
        clearComputedStyles();
    }

    /**
     * Informs about the use of a domChangeListener.
     */
    public void domChangeListenerAdded() {
        domChangeListenerInUse_ = true;
    }

    /**
     * @return true if at least one domChangeListener was registered.
     */
    public boolean isDomChangeListenerInUse() {
        return domChangeListenerInUse_;
    }

    /**
     * Informs about the use of a characterDataChangeListener.
     */
    public void characterDataChangeListenerAdded() {
        characterDataChangeListenerInUse_ = true;
    }

    /**
     * @return true if at least one characterDataChangeListener was registered.
     */
    public boolean isCharacterDataChangeListenerInUse() {
        return characterDataChangeListenerInUse_;
    }
}
