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
package org.htmlunit.html;

import static org.htmlunit.BrowserVersionFeatures.DOM_NORMALIZE_REMOVE_CHILDREN;
import static org.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS;
import static org.htmlunit.BrowserVersionFeatures.XPATH_SELECTION_NAMESPACES;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.htmlunit.BrowserVersionFeatures;
import org.htmlunit.IncorrectnessListener;
import org.htmlunit.Page;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebAssert;
import org.htmlunit.WebClient;
import org.htmlunit.WebClient.PooledCSS3Parser;
import org.htmlunit.WebWindow;
import org.htmlunit.activex.javascript.msxml.XMLDOMDocument;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.css.CssStyleSheet;
import org.htmlunit.css.StyleAttributes;
import org.htmlunit.cssparser.parser.CSSErrorHandler;
import org.htmlunit.cssparser.parser.CSSException;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.CSSParseException;
import org.htmlunit.cssparser.parser.selector.Selector;
import org.htmlunit.cssparser.parser.selector.SelectorList;
import org.htmlunit.html.HtmlElement.DisplayStyle;
import org.htmlunit.html.serializer.HtmlSerializerNormalizedText;
import org.htmlunit.html.serializer.HtmlSerializerVisibleText;
import org.htmlunit.html.xpath.XPathHelper;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.util.StringUtils;
import org.htmlunit.xml.XmlPage;
import org.htmlunit.xpath.xml.utils.PrefixResolver;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Base class for nodes in the HTML DOM tree. This class is modeled after the
 * W3C DOM specification, but does not implement it.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Mike Williams
 * @author Marc Guillemot
 * @author Denis N. Antonioli
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Sudhan Moghe
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 * @author Ronald Brill
 * @author Chuck Dumont
 * @author Frank Danek
 */
public abstract class DomNode implements Cloneable, Serializable, Node {

    /** A ready state constant for IE (state 1). */
    public static final String READY_STATE_UNINITIALIZED = "uninitialized";

    /** A ready state constant for IE (state 2). */
    public static final String READY_STATE_LOADING = "loading";

    /** A ready state constant for IE (state 3). */
    public static final String READY_STATE_LOADED = "loaded";

    /** A ready state constant for IE (state 4). */
    public static final String READY_STATE_INTERACTIVE = "interactive";

    /** A ready state constant for IE (state 5). */
    public static final String READY_STATE_COMPLETE = "complete";

    /** The name of the "element" property. Used when watching property change events. */
    public static final String PROPERTY_ELEMENT = "element";

    /** The owning page of this node. */
    private SgmlPage page_;

    /** The parent node. */
    private DomNode parent_;

    /**
     * The previous sibling. The first child's <code>previousSibling</code> points
     * to the end of the list
     */
    private DomNode previousSibling_;

    /**
     * The next sibling. The last child's <code>nextSibling</code> is {@code null}
     */
    private DomNode nextSibling_;

    /** Start of the child list. */
    private DomNode firstChild_;

    /**
     * This is the JavaScript object corresponding to this DOM node. It may
     * be null if there isn't a corresponding JavaScript object.
     */
    private HtmlUnitScriptable scriptObject_;

    /** The ready state is an IE-only value that is available to a large number of elements. */
    private String readyState_;

    /**
     * The line number in the source page where the DOM node starts.
     */
    private int startLineNumber_ = -1;

    /**
     * The column number in the source page where the DOM node starts.
     */
    private int startColumnNumber_ = -1;

    /**
     * The line number in the source page where the DOM node ends.
     */
    private int endLineNumber_ = -1;

    /**
     * The column number in the source page where the DOM node ends.
     */
    private int endColumnNumber_ = -1;

    private boolean attachedToPage_;

    /** The listeners which are to be notified of characterData change. */
    private List<CharacterDataChangeListener> characterDataListeners_;
    private List<DomChangeListener> domListeners_;

    private Map<String, Object> userData_;

    /**
     * Creates a new instance.
     * @param page the page which contains this node
     */
    protected DomNode(final SgmlPage page) {
        readyState_ = READY_STATE_LOADING;
        page_ = page;
    }

    /**
     * Sets the line and column numbers in the source page where the DOM node starts.
     *
     * @param startLineNumber the line number where the DOM node starts
     * @param startColumnNumber the column number where the DOM node starts
     */
    public void setStartLocation(final int startLineNumber, final int startColumnNumber) {
        startLineNumber_ = startLineNumber;
        startColumnNumber_ = startColumnNumber;
    }

    /**
     * Sets the line and column numbers in the source page where the DOM node ends.
     *
     * @param endLineNumber the line number where the DOM node ends
     * @param endColumnNumber the column number where the DOM node ends
     */
    public void setEndLocation(final int endLineNumber, final int endColumnNumber) {
        endLineNumber_ = endLineNumber;
        endColumnNumber_ = endColumnNumber;
    }

    /**
     * Returns the line number in the source page where the DOM node starts.
     * @return the line number in the source page where the DOM node starts
     */
    public int getStartLineNumber() {
        return startLineNumber_;
    }

    /**
     * Returns the column number in the source page where the DOM node starts.
     * @return the column number in the source page where the DOM node starts
     */
    public int getStartColumnNumber() {
        return startColumnNumber_;
    }

    /**
     * Returns the line number in the source page where the DOM node ends.
     * @return 0 if no information on the line number is available (for instance for nodes dynamically added),
     * -1 if the end tag has not yet been parsed (during page loading)
     */
    public int getEndLineNumber() {
        return endLineNumber_;
    }

    /**
     * Returns the column number in the source page where the DOM node ends.
     * @return 0 if no information on the line number is available (for instance for nodes dynamically added),
     * -1 if the end tag has not yet been parsed (during page loading)
     */
    public int getEndColumnNumber() {
        return endColumnNumber_;
    }

    /**
     * Returns the page that contains this node.
     * @return the page that contains this node
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public SgmlPage getPage() {
        return page_;
    }

    /**
     * Returns the page that contains this node.
     * @return the page that contains this node
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public HtmlPage getHtmlPageOrNull() {
        if (page_ == null || !page_.isHtmlPage()) {
            return null;
        }
        return (HtmlPage) page_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document getOwnerDocument() {
        return getPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the JavaScript object that corresponds to this node. This is not guaranteed to be set even if
     * there is a JavaScript object for this DOM node.
     *
     * @param scriptObject the JavaScript object
     */
    public void setScriptableObject(final HtmlUnitScriptable scriptObject) {
        scriptObject_ = scriptObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public DomNode getLastChild() {
        if (firstChild_ != null) {
            // last child is stored as the previous sibling of first child
            return firstChild_.previousSibling_;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public DomNode getParentNode() {
        return parent_;
    }

    /**
     * Sets the parent node.
     * @param parent the parent node
     */
    protected void setParentNode(final DomNode parent) {
        parent_ = parent;
    }

    /**
     * Returns this node's index within its parent's child nodes (zero-based).
     * @return this node's index within its parent's child nodes (zero-based)
     */
    public int getIndex() {
        int index = 0;
        for (DomNode n = previousSibling_; n != null && n.nextSibling_ != null; n = n.previousSibling_) {
            index++;
        }
        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public DomNode getPreviousSibling() {
        if (parent_ == null || this == parent_.firstChild_) {
            // previous sibling of first child points to last child
            return null;
        }
        return previousSibling_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public DomNode getNextSibling() {
        return nextSibling_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public DomNode getFirstChild() {
        return firstChild_;
    }

    /**
     * Returns {@code true} if this node is an ancestor of the specified node.
     *
     * @param node the node to check
     * @return {@code true} if this node is an ancestor of the specified node
     */
    public boolean isAncestorOf(DomNode node) {
        while (node != null) {
            if (node == this) {
                return true;
            }
            node = node.getParentNode();
        }
        return false;
    }

    /**
     * Returns {@code true} if this node is an ancestor of the specified nodes.
     *
     * @param nodes the nodes to check
     * @return {@code true} if this node is an ancestor of the specified nodes
     */
    public boolean isAncestorOfAny(final DomNode... nodes) {
        for (final DomNode node : nodes) {
            if (isAncestorOf(node)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildNodes() {
        return firstChild_ != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNodeList<DomNode> getChildNodes() {
        return new SiblingDomNodeList(this);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public boolean isSupported(final String namespace, final String featureName) {
        throw new UnsupportedOperationException("DomNode.isSupported is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void normalize() {
        for (DomNode child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof DomText) {
                final boolean removeChildTextNodes = hasFeature(DOM_NORMALIZE_REMOVE_CHILDREN);
                final StringBuilder dataBuilder = new StringBuilder();
                DomNode toRemove = child;
                DomText firstText = null;
                //IE removes all child text nodes, but FF preserves the first
                while (toRemove instanceof DomText && !(toRemove instanceof DomCDataSection)) {
                    final DomNode nextChild = toRemove.getNextSibling();
                    dataBuilder.append(toRemove.getTextContent());
                    if (removeChildTextNodes || firstText != null) {
                        toRemove.remove();
                    }
                    if (firstText == null) {
                        firstText = (DomText) toRemove;
                    }
                    toRemove = nextChild;
                }
                if (firstText != null) {
                    if (removeChildTextNodes) {
                        final DomText newText = new DomText(getPage(), dataBuilder.toString());
                        insertBefore(newText, toRemove);
                    }
                    else {
                        firstText.setData(dataBuilder.toString());
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseURI() {
        return getPage().getUrl().toExternalForm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short compareDocumentPosition(final Node other) {
        if (other == this) {
            return 0; // strange, no constant available?
        }

        // get ancestors of both
        final List<Node> myAncestors = getAncestors();
        final List<Node> otherAncestors = ((DomNode) other).getAncestors();

        final int max = Math.min(myAncestors.size(), otherAncestors.size());

        int i = 1;
        while (i < max && myAncestors.get(i) == otherAncestors.get(i)) {
            i++;
        }

        if (i != 1 && i == max) {
            if (myAncestors.size() == max) {
                return DOCUMENT_POSITION_CONTAINED_BY | DOCUMENT_POSITION_FOLLOWING;
            }
            return DOCUMENT_POSITION_CONTAINS | DOCUMENT_POSITION_PRECEDING;
        }

        if (max == 1) {
            if (myAncestors.contains(other)) {
                return DOCUMENT_POSITION_CONTAINS;
            }
            if (otherAncestors.contains(this)) {
                return DOCUMENT_POSITION_CONTAINED_BY | DOCUMENT_POSITION_FOLLOWING;
            }
            return DOCUMENT_POSITION_DISCONNECTED | DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;
        }

        // neither contains nor contained by
        final Node myAncestor = myAncestors.get(i);
        final Node otherAncestor = otherAncestors.get(i);
        Node node = myAncestor;
        while (node != otherAncestor && node != null) {
            node = node.getPreviousSibling();
        }
        if (node == null) {
            return DOCUMENT_POSITION_FOLLOWING;
        }
        return DOCUMENT_POSITION_PRECEDING;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Gets the ancestors of the node.
     * @return a list of the ancestors with the root at the first position
     */
    public List<Node> getAncestors() {
        final List<Node> list = new ArrayList<>();
        list.add(this);

        Node node = getParentNode();
        while (node != null) {
            list.add(0, node);
            node = node.getParentNode();
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextContent() {
        switch (getNodeType()) {
            case ELEMENT_NODE:
            case ATTRIBUTE_NODE:
            case ENTITY_NODE:
            case ENTITY_REFERENCE_NODE:
            case DOCUMENT_FRAGMENT_NODE:
                final StringBuilder builder = new StringBuilder();
                for (final DomNode child : getChildren()) {
                    final short childType = child.getNodeType();
                    if (childType != COMMENT_NODE && childType != PROCESSING_INSTRUCTION_NODE) {
                        builder.append(child.getTextContent());
                    }
                }
                return builder.toString();

            case TEXT_NODE:
            case CDATA_SECTION_NODE:
            case COMMENT_NODE:
            case PROCESSING_INSTRUCTION_NODE:
                return getNodeValue();

            default:
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextContent(final String textContent) {
        removeAllChildren();
        if (textContent != null && !textContent.isEmpty()) {
            appendChild(new DomText(getPage(), textContent));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameNode(final Node other) {
        return other == this;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String lookupPrefix(final String namespaceURI) {
        throw new UnsupportedOperationException("DomNode.lookupPrefix is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public boolean isDefaultNamespace(final String namespaceURI) {
        throw new UnsupportedOperationException("DomNode.isDefaultNamespace is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String lookupNamespaceURI(final String prefix) {
        throw new UnsupportedOperationException("DomNode.lookupNamespaceURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public boolean isEqualNode(final Node arg) {
        throw new UnsupportedOperationException("DomNode.isEqualNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Object getFeature(final String feature, final String version) {
        throw new UnsupportedOperationException("DomNode.getFeature is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getUserData(final String key) {
        Object value = null;
        if (userData_ != null) {
            value = userData_.get(key);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object setUserData(final String key, final Object data, final UserDataHandler handler) {
        if (userData_ == null) {
            userData_ = new HashMap<>();
        }
        return userData_.put(key, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAttributes() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamedNodeMap getAttributes() {
        return NamedAttrNodeMapImpl.EMPTY_MAP;
    }

    /**
     * <p>Returns {@code true} if this node is displayed and can be visible to the user
     * (ignoring screen size, scrolling limitations, color, font-size, or overlapping nodes).</p>
     *
     * <p><b>NOTE:</b> If CSS is
     * {@link org.htmlunit.WebClientOptions#setCssEnabled(boolean) disabled}, this method
     * does <b>not</b> take this element's style into consideration!</p>
     *
     * @see <a href="http://www.w3.org/TR/CSS2/visufx.html#visibility">CSS2 Visibility</a>
     * @see <a href="http://www.w3.org/TR/CSS2/visuren.html#propdef-display">CSS2 Display</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms531180.aspx">MSDN Documentation</a>
     * @return {@code true} if the node is visible to the user, {@code false} otherwise
     * @see #mayBeDisplayed()
     */
    public boolean isDisplayed() {
        if (!mayBeDisplayed()) {
            return false;
        }

        final Page page = getPage();
        final WebWindow window = page.getEnclosingWindow();
        final WebClient webClient = window.getWebClient();
        if (webClient.getOptions().isCssEnabled()) {
            // display: iterate top to bottom, because if a parent is display:none,
            // there's nothing that a child can do to override it
            final List<Node> ancestors = getAncestors();
            final ArrayList<ComputedCssStyleDeclaration> styles = new ArrayList<>(ancestors.size());

            for (final Node node : ancestors) {
                if (node instanceof HtmlElement) {
                    final HtmlElement elem = (HtmlElement) node;
                    if (elem.isHidden()) {
                        return false;
                    }

                    if (elem instanceof HtmlDialog) {
                        if (!((HtmlDialog) elem).isOpen()) {
                            return false;
                        }
                    }
                    else {
                        final ComputedCssStyleDeclaration style = window.getComputedStyle(elem, null);
                        if (DisplayStyle.NONE.value().equals(style.getDisplay())) {
                            return false;
                        }
                        styles.add(style);
                    }
                }
            }

            // visibility: iterate bottom to top, because children can override
            // the visibility used by parent nodes
            for (int i = styles.size() - 1; i >= 0; i--) {
                final ComputedCssStyleDeclaration style = styles.get(i);
                final String visibility = style.getStyleAttribute(StyleAttributes.Definition.VISIBILITY, true);
                if (visibility.length() > 5) {
                    if ("visible".equals(visibility)) {
                        return true;
                    }
                    if ("hidden".equals(visibility) || "collapse".equals(visibility)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if nodes of this type can ever be displayed, {@code false} otherwise. Examples of nodes
     * that can never be displayed are <code>&lt;head&gt;</code>,
     * <code>&lt;meta&gt;</code>, <code>&lt;script&gt;</code>, etc.
     * @return {@code true} if nodes of this type can ever be displayed, {@code false} otherwise
     * @see #isDisplayed()
     */
    public boolean mayBeDisplayed() {
        return true;
    }

    /**
     * Returns a normalized textual representation of this element that represents
     * what would be visible to the user if this page was shown in a web browser.
     * Whitespace is normalized like in the browser and block tags are separated by '\n'.
     *
     * @return a normalized textual representation of this element
     */
    public String asNormalizedText() {
        final HtmlSerializerNormalizedText ser = new HtmlSerializerNormalizedText();
        return ser.asText(this);
    }

    /**
     * Returns a textual representation of this element in the same way as
     * the selenium/WebDriver WebElement#getText() property does.<br>
     * see <a href="https://w3c.github.io/webdriver/#get-element-text">get-element-text</a> and
     * <a href="https://w3c.github.io/webdriver/#dfn-bot-dom-getvisibletext">dfn-bot-dom-getvisibletext</a>
     * Note: this is different from {@link #asNormalizedText()}
     *
     * @return a textual representation of this element that represents what would
     *         be visible to the user if this page was shown in a web browser
     */
    public String getVisibleText() {
        final HtmlSerializerVisibleText ser = new HtmlSerializerVisibleText();
        return ser.asText(this);
    }

    /**
     * Returns a string representation of the XML document from this element and all it's children (recursively).
     * The charset used is the current page encoding.
     * @return the XML string
     */
    public String asXml() {
        Charset charsetName = null;
        final HtmlPage htmlPage = getHtmlPageOrNull();
        if (htmlPage != null) {
            charsetName = htmlPage.getCharset();
        }

        final StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            if (charsetName != null && this instanceof HtmlHtml) {
                printWriter.print("<?xml version=\"1.0\" encoding=\"");
                printWriter.print(charsetName);
                printWriter.print("\"?>\r\n");
            }
            printXml("", printWriter);
            return stringWriter.toString();
        }
    }

    /**
     * Recursively writes the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    protected void printXml(final String indent, final PrintWriter printWriter) {
        printWriter.print(indent);
        printWriter.print(this);
        printWriter.print("\r\n");
        printChildrenAsXml(indent, printWriter);
    }

    /**
     * Recursively writes the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    protected void printChildrenAsXml(final String indent, final PrintWriter printWriter) {
        DomNode child = getFirstChild();
        while (child != null) {
            child.printXml(indent + "  ", printWriter);
            child = child.getNextSibling();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeValue() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final DomNode newnode;
        try {
            newnode = (DomNode) clone();
        }
        catch (final CloneNotSupportedException e) {
            throw new IllegalStateException("Clone not supported for node [" + this + "]", e);
        }

        newnode.parent_ = null;
        newnode.nextSibling_ = null;
        newnode.previousSibling_ = null;
        newnode.scriptObject_ = null;
        newnode.firstChild_ = null;
        newnode.attachedToPage_ = false;

        // if deep, clone the children too.
        if (deep) {
            for (DomNode child = firstChild_; child != null; child = child.nextSibling_) {
                newnode.appendChild(child.cloneNode(true));
            }
        }

        return newnode;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * <p>Returns the JavaScript object that corresponds to this node, lazily initializing a new one if necessary.</p>
     *
     * <p>The logic of when and where the JavaScript object is created needs a clean up: functions using
     * a DOM node's JavaScript object should not have to check if they should create it first.</p>
     *
     * @param <T> the object type
     * @return the JavaScript object that corresponds to this node
     */
    @SuppressWarnings("unchecked")
    public <T extends HtmlUnitScriptable> T getScriptableObject() {
        if (scriptObject_ == null) {
            final SgmlPage page = getPage();
            if (this == page) {
                final StringBuilder msg = new StringBuilder("No script object associated with the Page.");
                // because this is a strange case we like to provide as much info as possible
                msg.append(" class: '")
                    .append(page.getClass().getName())
                    .append('\'');
                try {
                    msg.append(" url: '")
                        .append(page.getUrl()).append("' content: ")
                        .append(page.getWebResponse().getContentAsString());
                }
                catch (final Exception e) {
                    // ok bad luck with detail
                    msg.append(" no details: '").append(e).append('\'');
                }
                throw new IllegalStateException(msg.toString());
            }
            scriptObject_ = page.getScriptableObject().makeScriptableFor(this);
        }
        return (T) scriptObject_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode appendChild(final Node node) {
        if (node == this) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(new Exception("Can not add not to itself " + this));
        }
        final DomNode domNode = (DomNode) node;
        if (domNode.isAncestorOf(this)) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(new Exception("Can not add (grand)parent to itself " + this));
        }

        if (domNode instanceof DomDocumentFragment) {
            final DomDocumentFragment fragment = (DomDocumentFragment) domNode;
            for (final DomNode child : fragment.getChildren()) {
                appendChild(child);
            }
        }
        else {
            // clean up the new node, in case it is being moved
            if (domNode.getParentNode() != null) {
                domNode.detach();
            }

            basicAppend(domNode);

            fireAddition(domNode);
        }

        return domNode;
    }

    /**
     * Appends the specified node to the end of this node's children, assuming the specified
     * node is clean (doesn't have preexisting relationships to other nodes).
     *
     * @param node the node to append to this node's children
     */
    private void basicAppend(final DomNode node) {
        // try to make the node setup as complete as possible
        // before the node is reachable
        node.setPage(getPage());
        node.parent_ = this;

        if (firstChild_ == null) {
            firstChild_ = node;
        }
        else {
            final DomNode last = getLastChild();
            node.previousSibling_ = last;
            node.nextSibling_ = null; // safety first

            last.nextSibling_ = node;
        }
        firstChild_.previousSibling_ = node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node insertBefore(final Node newChild, final Node refChild) {
        if (newChild instanceof DomDocumentFragment) {
            final DomDocumentFragment fragment = (DomDocumentFragment) newChild;
            for (final DomNode child : fragment.getChildren()) {
                insertBefore(child, refChild);
            }
            return newChild;
        }

        if (refChild == null) {
            appendChild(newChild);
            return newChild;
        }

        if (refChild.getParentNode() != this) {
            throw new DOMException(DOMException.NOT_FOUND_ERR, "Reference node is not a child of this node.");
        }

        ((DomNode) refChild).insertBefore((DomNode) newChild);
        return newChild;
    }

    /**
     * Inserts the specified node as a new child node before this node into the child relationship this node is a
     * part of. If the specified node is this node, this method is a no-op.
     *
     * @param newNode the new node to insert
     */
    public void insertBefore(final DomNode newNode) {
        if (previousSibling_ == null) {
            throw new IllegalStateException("Previous sibling for " + this + " is null.");
        }

        if (newNode == this) {
            return;
        }

        // clean up the new node, in case it is being moved
        if (newNode.getParentNode() != null) {
            newNode.detach();
        }

        basicInsertBefore(newNode);

        fireAddition(newNode);
    }

    /**
     * Inserts the specified node into this node's parent's children right before this node, assuming the specified
     * node is clean (doesn't have preexisting relationships to other nodes).
     *
     * @param node the node to insert before this node
     */
    private void basicInsertBefore(final DomNode node) {
        // try to make the node setup as complete as possible
        // before the node is reachable
        node.setPage(page_);
        node.parent_ = parent_;
        node.previousSibling_ = previousSibling_;
        node.nextSibling_ = this;

        if (parent_.firstChild_ == this) {
            parent_.firstChild_ = node;
        }
        else {
            previousSibling_.nextSibling_ = node;
        }
        previousSibling_ = node;
    }

    private void fireAddition(final DomNode domNode) {
        final boolean wasAlreadyAttached = domNode.isAttachedToPage();
        domNode.attachedToPage_ = isAttachedToPage();

        if (domNode.attachedToPage_) {
            // trigger events
            final Page page = getPage();
            if (null != page && page.isHtmlPage()) {
                ((HtmlPage) page).notifyNodeAdded(domNode);
            }

            // a node that is already "complete" (ie not being parsed) and not yet attached
            if (!domNode.isBodyParsed() && !wasAlreadyAttached) {
                for (final DomNode child : domNode.getDescendants()) {
                    child.attachedToPage_ = true;
                    child.onAllChildrenAddedToPage(true);
                }
                domNode.onAllChildrenAddedToPage(true);
            }
        }

        if (this instanceof DomDocumentFragment) {
            onAddedToDocumentFragment();
        }

        fireNodeAdded(new DomChangeEvent(this, domNode));
    }

    /**
     * Indicates if the current node is being parsed. This means that the opening tag has already been
     * parsed but not the body and end tag.
     */
    private boolean isBodyParsed() {
        return getStartLineNumber() != -1 && getEndLineNumber() == -1;
    }

    /**
     * Recursively sets the new page on the node and its children
     * @param newPage the new owning page
     */
    private void setPage(final SgmlPage newPage) {
        if (page_ == newPage) {
            return; // nothing to do
        }

        page_ = newPage;
        for (final DomNode node : getChildren()) {
            node.setPage(newPage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node removeChild(final Node child) {
        if (child.getParentNode() != this) {
            throw new DOMException(DOMException.NOT_FOUND_ERR, "Node is not a child of this node.");
        }
        ((DomNode) child).remove();
        return child;
    }

    /**
     * Removes all of this node's children.
     */
    public void removeAllChildren() {
        while (getFirstChild() != null) {
            getFirstChild().remove();
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Parses the specified HTML source code, appending the resulting content at the specified target location.
     * @param source the HTML code extract to parse
     * @throws IOException in case of error
     * @throws SAXException in case of error
     */
    public void parseHtmlSnippet(final String source) throws SAXException, IOException {
        getPage().getWebClient().getPageCreator().getHtmlParser().parseFragment(this, source);
    }

    /**
     * Removes this node from all relationships with other nodes.
     */
    public void remove() {
        // same as detach for the moment
        detach();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Detach this node from all relationships with other nodes.
     * This is the first step of a move.
     */
    protected void detach() {
        final DomNode exParent = parent_;

        basicRemove();

        fireRemoval(exParent);
    }

    /**
     * Cuts off all relationships this node has with siblings and parents.
     */
    protected void basicRemove() {
        if (parent_ != null && parent_.firstChild_ == this) {
            parent_.firstChild_ = nextSibling_;
        }
        else if (previousSibling_ != null && previousSibling_.nextSibling_ == this) {
            previousSibling_.nextSibling_ = nextSibling_;
        }
        if (nextSibling_ != null && nextSibling_.previousSibling_ == this) {
            nextSibling_.previousSibling_ = previousSibling_;
        }
        if (parent_ != null && this == parent_.getLastChild()) {
            parent_.firstChild_.previousSibling_ = previousSibling_;
        }

        nextSibling_ = null;
        previousSibling_ = null;
        parent_ = null;
        attachedToPage_ = false;
        for (final DomNode descendant : getDescendants()) {
            descendant.attachedToPage_ = false;
        }
    }

    private void fireRemoval(final DomNode exParent) {
        final HtmlPage htmlPage = getHtmlPageOrNull();
        if (htmlPage != null) {
            // some actions executed on removal need an intact parent relationship (e.g. for the
            // DocumentPositionComparator) so we have to restore it temporarily
            parent_ = exParent;
            htmlPage.notifyNodeRemoved(this);
            parent_ = null;
        }

        if (exParent != null) {
            final DomChangeEvent event = new DomChangeEvent(exParent, this);
            fireNodeDeleted(event);
            // ask ex-parent to fire event (because we don't have parent now)
            exParent.fireNodeDeleted(event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node replaceChild(final Node newChild, final Node oldChild) {
        if (oldChild.getParentNode() != this) {
            throw new DOMException(DOMException.NOT_FOUND_ERR, "Node is not a child of this node.");
        }
        ((DomNode) oldChild).replace((DomNode) newChild);
        return oldChild;
    }

    /**
     * Replaces this node with another node. If the specified node is this node, this
     * method is a no-op.
     * @param newNode the node to replace this one
     */
    public void replace(final DomNode newNode) {
        if (newNode != this) {
            final DomNode exParent = parent_;
            final DomNode exNextSibling = nextSibling_;

            remove();

            exParent.insertBefore(newNode, exNextSibling);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Quietly removes this node and moves its children to the specified destination. "Quietly" means
     * that no node events are fired. This method is not appropriate for most use cases. It should
     * only be used in specific cases for HTML parsing hackery.
     *
     * @param destination the node to which this node's children should be moved before this node is removed
     */
    public void quietlyRemoveAndMoveChildrenTo(final DomNode destination) {
        if (destination.getPage() != getPage()) {
            throw new RuntimeException("Cannot perform quiet move on nodes from different pages.");
        }
        for (final DomNode child : getChildren()) {
            child.basicRemove();
            destination.basicAppend(child);
        }
        basicRemove();
    }

    /**
     * Check for insertion errors for a new child node. This is overridden by derived
     * classes to enforce which types of children are allowed.
     *
     * @param newChild the new child node that is being inserted below this node
     * @throws DOMException HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does
     * not allow children of the type of the newChild node, or if the node to insert is one of
     * this node's ancestors or this node itself, or if this node is of type Document and the
     * DOM application attempts to insert a second DocumentType or Element node.
     * WRONG_DOCUMENT_ERR: Raised if newChild was created from a different document than the
     * one that created this node.
     */
    protected void checkChildHierarchy(final Node newChild) throws DOMException {
        Node parentNode = this;
        while (parentNode != null) {
            if (parentNode == newChild) {
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "Child node is already a parent.");
            }
            parentNode = parentNode.getParentNode();
        }
        final Document thisDocument = getOwnerDocument();
        final Document childDocument = newChild.getOwnerDocument();
        if (childDocument != thisDocument && childDocument != null) {
            throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, "Child node " + newChild.getNodeName()
                + " is not in the same Document as this " + getNodeName() + ".");
        }
    }

    /**
     * Lifecycle method invoked whenever a node is added to a page. Intended to
     * be overridden by nodes which need to perform custom logic when they are
     * added to a page. This method is recursive, so if you override it, please
     * be sure to call <code>super.onAddedToPage()</code>.
     */
    protected void onAddedToPage() {
        if (firstChild_ != null) {
            for (final DomNode child : getChildren()) {
                child.onAddedToPage();
            }
        }
    }

    /**
     * Lifecycle method invoked after a node and all its children have been added to a page, during
     * parsing of the HTML. Intended to be overridden by nodes which need to perform custom logic
     * after they and all their child nodes have been processed by the HTML parser. This method is
     * not recursive, and the default implementation is empty, so there is no need to call
     * <code>super.onAllChildrenAddedToPage()</code> if you implement this method.
     * @param postponed whether to use {@link org.htmlunit.javascript.PostponedAction} or no
     */
    public void onAllChildrenAddedToPage(final boolean postponed) {
        // Empty by default.
    }

    /**
     * Lifecycle method invoked whenever a node is added to a document fragment. Intended to
     * be overridden by nodes which need to perform custom logic when they are
     * added to a fragment. This method is recursive, so if you override it, please
     * be sure to call <code>super.onAddedToDocumentFragment()</code>.
     */
    protected void onAddedToDocumentFragment() {
        if (firstChild_ != null) {
            for (final DomNode child : getChildren()) {
                child.onAddedToDocumentFragment();
            }
        }
    }

    /**
     * @return an {@link Iterable} over the children of this node
     */
    public final Iterable<DomNode> getChildren() {
        return () -> new ChildIterator(firstChild_);
    }

    /**
     * An iterator over all children of this node.
     */
    protected static class ChildIterator implements Iterator<DomNode> {

        private DomNode nextNode_;
        private DomNode currentNode_;

        public ChildIterator(final DomNode nextNode) {
            nextNode_ = nextNode;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            return nextNode_ != null;
        }

        /** {@inheritDoc} */
        @Override
        public DomNode next() {
            if (nextNode_ != null) {
                currentNode_ = nextNode_;
                nextNode_ = nextNode_.nextSibling_;
                return currentNode_;
            }
            throw new NoSuchElementException();
        }

        /** {@inheritDoc} */
        @Override
        public void remove() {
            if (currentNode_ == null) {
                throw new IllegalStateException();
            }
            currentNode_.remove();
        }
    }

    /**
     * Returns an {@link Iterable} that will recursively iterate over all of this node's descendants,
     * including {@link DomText} elements, {@link DomComment} elements, etc. If you want to iterate
     * only over {@link HtmlElement} descendants, please use {@link #getHtmlElementDescendants()}.
     * @return an {@link Iterable} that will recursively iterate over all of this node's descendants
     */
    public final Iterable<DomNode> getDescendants() {
        return () -> new DescendantElementsIterator<>(DomNode.class);
    }

    /**
     * Returns an {@link Iterable} that will recursively iterate over all of this node's {@link HtmlElement}
     * descendants. If you want to iterate over all descendants (including {@link DomText} elements,
     * {@link DomComment} elements, etc.), please use {@link #getDescendants()}.
     * @return an {@link Iterable} that will recursively iterate over all of this node's {@link HtmlElement}
     *         descendants
     * @see #getDomElementDescendants()
     */
    public final Iterable<HtmlElement> getHtmlElementDescendants() {
        return () -> new DescendantElementsIterator<>(HtmlElement.class);
    }

    /**
     * Returns an {@link Iterable} that will recursively iterate over all of this node's {@link DomElement}
     * descendants. If you want to iterate over all descendants (including {@link DomText} elements,
     * {@link DomComment} elements, etc.), please use {@link #getDescendants()}.
     * @return an {@link Iterable} that will recursively iterate over all of this node's {@link DomElement}
     *         descendants
     * @see #getHtmlElementDescendants()
     */
    public final Iterable<DomElement> getDomElementDescendants() {
        return () -> new DescendantElementsIterator<>(DomElement.class);
    }

    /**
     * Iterates over all descendants of a specific type, in document order.
     * @param <T> the type of nodes over which to iterate
     */
    protected class DescendantElementsIterator<T extends DomNode> implements Iterator<T> {

        private DomNode currentNode_;
        private DomNode nextNode_;
        private final Class<T> type_;

        /**
         * Creates a new instance which iterates over the specified node type.
         * @param type the type of nodes over which to iterate
         */
        public DescendantElementsIterator(final Class<T> type) {
            type_ = type;
            nextNode_ = getFirstChildElement(DomNode.this);
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            return nextNode_ != null;
        }

        /** {@inheritDoc} */
        @Override
        public T next() {
            return nextNode();
        }

        /** {@inheritDoc} */
        @Override
        public void remove() {
            if (currentNode_ == null) {
                throw new IllegalStateException("Unable to remove current node, because there is no current node.");
            }
            final DomNode current = currentNode_;
            while (nextNode_ != null && current.isAncestorOf(nextNode_)) {
                next();
            }
            current.remove();
        }

        /** @return the next node, if there is one */
        @SuppressWarnings("unchecked")
        public T nextNode() {
            currentNode_ = nextNode_;
            setNextElement();
            return (T) currentNode_;
        }

        private void setNextElement() {
            DomNode next = getFirstChildElement(nextNode_);
            if (next == null) {
                next = getNextDomSibling(nextNode_);
            }
            if (next == null) {
                next = getNextElementUpwards(nextNode_);
            }
            nextNode_ = next;
        }

        private DomNode getNextElementUpwards(final DomNode startingNode) {
            if (startingNode == DomNode.this) {
                return null;
            }

            DomNode parent = startingNode.getParentNode();
            while (parent != null && parent != DomNode.this) {
                DomNode next = parent.getNextSibling();
                while (next != null && !isAccepted(next)) {
                    next = next.getNextSibling();
                }
                if (next != null) {
                    return next;
                }
                parent = parent.getParentNode();
            }
            return null;
        }

        private DomNode getFirstChildElement(final DomNode parent) {
            DomNode node = parent.getFirstChild();
            while (node != null && !isAccepted(node)) {
                node = node.getNextSibling();
            }
            return node;
        }

        /**
         * Indicates if the node is accepted. If not it won't be explored at all.
         * @param node the node to test
         * @return {@code true} if accepted
         */
        protected boolean isAccepted(final DomNode node) {
            return type_.isAssignableFrom(node.getClass());
        }

        private DomNode getNextDomSibling(final DomNode element) {
            DomNode node = element.getNextSibling();
            while (node != null && !isAccepted(node)) {
                node = node.getNextSibling();
            }
            return node;
        }
    }

    /**
     * Returns this node's ready state (IE only).
     * @return this node's ready state
     */
    public String getReadyState() {
        return readyState_;
    }

    /**
     * Sets this node's ready state (IE only).
     * @param state this node's ready state
     */
    public void setReadyState(final String state) {
        readyState_ = state;
    }

    /**
     * Parses the SelectionNamespaces property into a map of prefix/namespace pairs.
     * The default namespace (specified by xmlns=) is placed in the map using the
     * empty string ("") key.
     *
     * @param selectionNS the value of the SelectionNamespaces property
     * @return map of prefix/namespace value pairs
     */
    private static Map<String, String> parseSelectionNamespaces(final String selectionNS) {
        final Map<String, String> result = new HashMap<>();
        final String[] toks = StringUtils.splitAtJavaWhitespace(selectionNS);
        for (final String tok : toks) {
            if (tok.startsWith("xmlns=")) {
                result.put("", tok.substring(7, tok.length() - 7));
            }
            else if (tok.startsWith("xmlns:")) {
                final String[] prefix = tok.substring(6).split("=");
                result.put(prefix[0], prefix[1].substring(1, prefix[1].length() - 1));
            }
        }
        return result.isEmpty() ? null : result;
    }

    /**
     * Evaluates the specified XPath expression from this node, returning the matching elements.
     * <br>
     * Note: This implies that the ',' point to this node but the general axis like '//' are still
     * looking at the whole document. E.g. if you like to get all child h1 nodes from the current one
     * you have to use './/h1' instead of '//h1' because the latter matches all h1 nodes of the#
     * whole document.
     *
     * @param <T> the expected type
     * @param xpathExpr the XPath expression to evaluate
     * @return the elements which match the specified XPath expression
     * @see #getFirstByXPath(String)
     * @see #getCanonicalXPath()
     */
    public <T> List<T> getByXPath(final String xpathExpr) {
        // strange feature of the old IE XML support
        if (!hasFeature(XPATH_SELECTION_NAMESPACES)) {
            return XPathHelper.getByXPath(this, xpathExpr, null);
        }

        // See if the document has the SelectionNamespaces property defined. If so,
        // create a PrefixResolver that resolves the defined namespaces.
        PrefixResolver prefixResolver = null;
        final Document doc = getOwnerDocument();
        if (doc instanceof XmlPage) {
            final HtmlUnitScriptable scriptable = ((XmlPage) doc).getScriptableObject();
            if (scriptable instanceof XMLDOMDocument) {
                final String selectionNS = ((XMLDOMDocument) scriptable).getProperty("SelectionNamespaces");
                if (selectionNS != null && !selectionNS.isEmpty()) {
                    final Map<String, String> namespaces = parseSelectionNamespaces(selectionNS.toString());
                    if (namespaces != null) {
                        prefixResolver = new PrefixResolver() {
                            @Override
                            public String getNamespaceForPrefix(final String prefix) {
                                return namespaces.get(prefix);
                            }

                            @Override
                            public String getNamespaceForPrefix(final String prefix, final Node node) {
                                throw new UnsupportedOperationException();
                            }

                            @Override
                            public boolean handlesNullPrefixes() {
                                return false;
                            }
                        };
                    }
                }
            }
        }
        return XPathHelper.getByXPath(this, xpathExpr, prefixResolver);
    }

    /**
     * Evaluates the specified XPath expression from this node, returning the matching elements.
     *
     * @param xpathExpr the XPath expression to evaluate
     * @param resolver the prefix resolver to use for resolving namespace prefixes, or null
     * @return the elements which match the specified XPath expression
     * @see #getFirstByXPath(String)
     * @see #getCanonicalXPath()
     */
    public List<?> getByXPath(final String xpathExpr, final PrefixResolver resolver) {
        return XPathHelper.getByXPath(this, xpathExpr, resolver);
    }

    /**
     * Evaluates the specified XPath expression from this node, returning the first matching element,
     * or {@code null} if no node matches the specified XPath expression.
     *
     * @param xpathExpr the XPath expression
     * @param <X> the expression type
     * @return the first element matching the specified XPath expression
     * @see #getByXPath(String)
     * @see #getCanonicalXPath()
     */
    public <X> X getFirstByXPath(final String xpathExpr) {
        return getFirstByXPath(xpathExpr, null);
    }

    /**
     * Evaluates the specified XPath expression from this node, returning the first matching element,
     * or {@code null} if no node matches the specified XPath expression.
     *
     * @param xpathExpr the XPath expression
     * @param <X> the expression type
     * @param resolver the prefix resolver to use for resolving namespace prefixes, or null
     * @return the first element matching the specified XPath expression
     * @see #getByXPath(String)
     * @see #getCanonicalXPath()
     */
    @SuppressWarnings("unchecked")
    public <X> X getFirstByXPath(final String xpathExpr, final PrefixResolver resolver) {
        final List<?> results = getByXPath(xpathExpr, resolver);
        if (results.isEmpty()) {
            return null;
        }
        return (X) results.get(0);
    }

    /**
     * <p>Returns the canonical XPath expression which identifies this node, for instance
     * <code>"/html/body/table[3]/tbody/tr[5]/td[2]/span/a[3]"</code>.</p>
     *
     * <p><span style="color:red">WARNING:</span> This sort of automated XPath expression
     * is often quite bad at identifying a node, as it is highly sensitive to changes in
     * the DOM tree.</p>
     *
     * @return the canonical XPath expression which identifies this node
     * @see #getByXPath(String)
     */
    public String getCanonicalXPath() {
        throw new RuntimeException("Method getCanonicalXPath() not implemented for nodes of type " + getNodeType());
    }

    /**
     * Notifies the registered {@link IncorrectnessListener} of something that is not fully correct.
     * @param message the notification to send to the registered {@link IncorrectnessListener}
     */
    protected void notifyIncorrectness(final String message) {
        final WebClient client = getPage().getEnclosingWindow().getWebClient();
        final IncorrectnessListener incorrectnessListener = client.getIncorrectnessListener();
        incorrectnessListener.notify(message, this);
    }

    /**
     * Adds a {@link DomChangeListener} to the listener list. The listener is registered for
     * all descendants of this node.
     *
     * @param listener the DOM structure change listener to be added
     * @see #removeDomChangeListener(DomChangeListener)
     */
    public void addDomChangeListener(final DomChangeListener listener) {
        WebAssert.notNull("listener", listener);

        synchronized (this) {
            if (domListeners_ == null) {
                domListeners_ = new ArrayList<>();
            }
            domListeners_.add(listener);
        }
    }

    /**
     * Removes a {@link DomChangeListener} from the listener list. The listener is deregistered for
     * all descendants of this node.
     *
     * @param listener the DOM structure change listener to be removed
     * @see #addDomChangeListener(DomChangeListener)
     */
    public void removeDomChangeListener(final DomChangeListener listener) {
        WebAssert.notNull("listener", listener);

        synchronized (this) {
            if (domListeners_ != null) {
                domListeners_.remove(listener);
            }
        }
    }

    /**
     * Support for reporting DOM changes. This method can be called when a node has been added, and it
     * will send the appropriate {@link DomChangeEvent} to any registered {@link DomChangeListener}s.
     *
     * <p>Note that this method recursively calls this node's parent's {@link #fireNodeAdded(DomChangeEvent)}.</p>
     *
     * @param event the DomChangeEvent to be propagated
     */
    protected void fireNodeAdded(final DomChangeEvent event) {
        DomNode toInform = this;
        while (toInform != null) {
            final List<DomChangeListener> listeners = toInform.safeGetDomListeners();
            if (listeners != null) {
                // iterate by index and safe on an iterator copy
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).nodeAdded(event);
                }
            }
            toInform = toInform.getParentNode();
        }
    }

    /**
     * Adds a {@link CharacterDataChangeListener} to the listener list. The listener is registered for
     * all descendants of this node.
     *
     * @param listener the character data change listener to be added
     * @see #removeCharacterDataChangeListener(CharacterDataChangeListener)
     */
    public void addCharacterDataChangeListener(final CharacterDataChangeListener listener) {
        WebAssert.notNull("listener", listener);

        synchronized (this) {
            if (characterDataListeners_ == null) {
                characterDataListeners_ = new ArrayList<>();
            }
            characterDataListeners_.add(listener);
        }
    }

    /**
     * Removes a {@link CharacterDataChangeListener} from the listener list. The listener is deregistered for
     * all descendants of this node.
     *
     * @param listener the Character Data change listener to be removed
     * @see #addCharacterDataChangeListener(CharacterDataChangeListener)
     */
    public void removeCharacterDataChangeListener(final CharacterDataChangeListener listener) {
        WebAssert.notNull("listener", listener);

        synchronized (this) {
            if (characterDataListeners_ != null) {
                characterDataListeners_.remove(listener);
            }
        }
    }

    /**
     * Support for reporting Character Data changes.
     *
     * <p>Note that this method recursively calls this node's parent's {@link #fireCharacterDataChanged}.</p>
     *
     * @param event the CharacterDataChangeEvent to be propagated
     */
    protected void fireCharacterDataChanged(final CharacterDataChangeEvent event) {
        DomNode toInform = this;
        while (toInform != null) {

            final List<CharacterDataChangeListener> listeners = toInform.safeGetCharacterDataListeners();
            if (listeners != null) {
                // iterate by index and safe on an iterator copy
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).characterDataChanged(event);
                }
            }
            toInform = toInform.getParentNode();
        }
    }

    /**
     * Support for reporting DOM changes. This method can be called when a node has been deleted, and it
     * will send the appropriate {@link DomChangeEvent} to any registered {@link DomChangeListener}s.
     *
     * <p>Note that this method recursively calls this node's parent's {@link #fireNodeDeleted(DomChangeEvent)}.</p>
     *
     * @param event the DomChangeEvent to be propagated
     */
    protected void fireNodeDeleted(final DomChangeEvent event) {
        DomNode toInform = this;
        while (toInform != null) {
            final List<DomChangeListener> listeners = toInform.safeGetDomListeners();
            if (listeners != null) {
                // iterate by index and safe on an iterator copy
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).nodeDeleted(event);
                }
            }
            toInform = toInform.getParentNode();
        }
    }

    private List<DomChangeListener> safeGetDomListeners() {
        synchronized (this) {
            return domListeners_ == null ? null : new ArrayList<>(domListeners_);
        }
    }

    private List<CharacterDataChangeListener> safeGetCharacterDataListeners() {
        synchronized (this) {
            return characterDataListeners_ == null ? null : new ArrayList<>(characterDataListeners_);
        }
    }

    /**
     * Retrieves all element nodes from descendants of the starting element node that match any selector
     * within the supplied selector strings.
     * @param selectors one or more CSS selectors separated by commas
     * @return list of all found nodes
     */
    public DomNodeList<DomNode> querySelectorAll(final String selectors) {
        try {
            final WebClient webClient = getPage().getWebClient();
            final SelectorList selectorList = getSelectorList(selectors, webClient);

            final List<DomNode> elements = new ArrayList<>();
            if (selectorList != null) {
                for (final DomElement child : getDomElementDescendants()) {
                    for (final Selector selector : selectorList) {
                        if (CssStyleSheet.selects(webClient.getBrowserVersion(), selector, child, null, true, true)) {
                            elements.add(child);
                            break;
                        }
                    }
                }
            }
            return new StaticDomNodeList(elements);
        }
        catch (final IOException e) {
            throw new CSSException("Error parsing CSS selectors from '" + selectors + "': " + e.getMessage());
        }
    }

    /**
     * Returns the {@link SelectorList}.
     * @param selectors the selectors
     * @param webClient the {@link WebClient}
     * @return the {@link SelectorList}
     * @throws IOException if an error occurs
     */
    protected SelectorList getSelectorList(final String selectors, final WebClient webClient)
            throws IOException {

        // get us a CSS3Parser from the pool so the chance of reusing it are high
        try (PooledCSS3Parser pooledParser = webClient.getCSS3Parser()) {
            final CSSOMParser parser = new CSSOMParser(pooledParser);
            final CheckErrorHandler errorHandler = new CheckErrorHandler();
            parser.setErrorHandler(errorHandler);

            final SelectorList selectorList = parser.parseSelectors(selectors);
            // in case of error parseSelectors returns null
            if (errorHandler.errorDetected()) {
                throw new CSSException("Invalid selectors: '" + selectors + "'");
            }

            if (selectorList != null) {
                int documentMode = 9;
                if (webClient.getBrowserVersion().hasFeature(QUERYSELECTORALL_NOT_IN_QUIRKS)) {
                    final HtmlUnitScriptable sobj = getPage().getScriptableObject();
                    if (sobj instanceof HTMLDocument) {
                        documentMode = ((HTMLDocument) sobj).getDocumentMode();
                    }
                }
                CssStyleSheet.validateSelectors(selectorList, documentMode, this);

            }
            return selectorList;
        }
    }

    /**
     * Returns the first element within the document that matches the specified group of selectors.
     * @param selectors one or more CSS selectors separated by commas
     * @param <N> the node type
     * @return null if no matches are found; otherwise, it returns the first matching element
     */
    @SuppressWarnings("unchecked")
    public <N extends DomNode> N querySelector(final String selectors) {
        final DomNodeList<DomNode> list = querySelectorAll(selectors);
        if (!list.isEmpty()) {
            return (N) list.get(0);
        }
        return null;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Indicates if this node is currently attached to the page.
     * @return {@code true} if the page is one ancestor of the node.
     */
    public boolean isAttachedToPage() {
        return attachedToPage_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Lifecycle method to support special processing for js method importNode.
     * @param doc the import target document
     * @see org.htmlunit.javascript.host.dom.Document#importNode(
     * org.htmlunit.javascript.host.dom.Node, boolean)
     * @see HtmlScript#processImportNode(org.htmlunit.javascript.host.dom.Document)
     */
    public void processImportNode(final org.htmlunit.javascript.host.dom.Document doc) {
        page_ = (SgmlPage) doc.getDomNodeOrDie();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Helper for a common call sequence.
     * @param feature the feature to check
     * @return {@code true} if the currently emulated browser has this feature.
     */
    public boolean hasFeature(final BrowserVersionFeatures feature) {
        return getPage().getWebClient().getBrowserVersion().hasFeature(feature);
    }

    private static final class CheckErrorHandler implements CSSErrorHandler {
        private boolean errorDetected_;

        private CheckErrorHandler() {
            errorDetected_ = false;
        }

        boolean errorDetected() {
            return errorDetected_;
        }

        @Override
        public void warning(final CSSParseException exception) throws CSSException {
            // ignore
        }

        @Override
        public void fatalError(final CSSParseException exception) throws CSSException {
            errorDetected_ = true;
        }

        @Override
        public void error(final CSSParseException exception) throws CSSException {
            errorDetected_ = true;
        }
    }

    /**
     * Indicates if the provided event can be applied to this node.
     * Overwrite this.
     * @param event the event
     * @return {@code false} if the event can't be applied
     */
    public boolean handles(final Event event) {
        return true;
    }

    /**
     * Returns the previous sibling element node of this element.
     * null if this element has no element sibling nodes that come before this one in the document tree.
     * @return the previous sibling element node of this element.
     * null if this element has no element sibling nodes that come before this one in the document tree
     */
    public DomElement getPreviousElementSibling() {
        DomNode node = getPreviousSibling();
        while (node != null && !(node instanceof DomElement)) {
            node = node.getPreviousSibling();
        }
        return (DomElement) node;
    }

    /**
     * Returns the next sibling element node of this element.
     * null if this element has no element sibling nodes that come after this one in the document tree.
     * @return the next sibling element node of this element.
     * null if this element has no element sibling nodes that come after this one in the document tree
     */
    public DomElement getNextElementSibling() {
        DomNode node = getNextSibling();
        while (node != null && !(node instanceof DomElement)) {
            node = node.getNextSibling();
        }
        return (DomElement) node;
    }

    /**
     * @param selectorString the selector to test
     * @return the selected {@link DomElement} or null.
     */
    public DomElement closest(final String selectorString) {
        try {
            final WebClient webClient = getPage().getWebClient();
            final SelectorList selectorList = getSelectorList(selectorString, webClient);

            DomNode current = this;
            if (selectorList != null) {
                do {
                    for (final Selector selector : selectorList) {
                        final DomElement elem = (DomElement) current;
                        if (CssStyleSheet.selects(webClient.getBrowserVersion(), selector, elem, null, true, true)) {
                            return elem;
                        }
                    }

                    do {
                        current = current.getParentNode();
                    }
                    while (current != null && !(current instanceof DomElement));
                }
                while (current != null);
            }
            return null;
        }
        catch (final IOException e) {
            throw new CSSException("Error parsing CSS selectors from '" + selectorString + "': " + e.getMessage());
        }
    }
}
