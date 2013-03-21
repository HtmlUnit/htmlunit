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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.DISPLAYED_COLLAPSE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.DOM_NORMALIZE_REMOVE_CHILDREN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.NODE_APPEND_CHILD_SELF_IGNORE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

/**
 * Base class for nodes in the HTML DOM tree. This class is modeled after the
 * W3C DOM specification, but does not implement it.
 *
 * @version $Revision$
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
 */
public abstract class DomNode implements Cloneable, Serializable, Node {

    /** Indicates a block. Will be rendered as line separator (multiple block marks are ignored) */
    protected static final String AS_TEXT_BLOCK_SEPARATOR = "§bs§";
    /** Indicates a new line. Will be rendered as line separator. */
    protected static final String AS_TEXT_NEW_LINE = "§nl§";
    /** Indicates a non blank that can't be trimmed or reduced. */
    protected static final String AS_TEXT_BLANK = "§blank§";
    /** Indicates a tab. */
    protected static final String AS_TEXT_TAB = "§tab§";

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
     * The next sibling. The last child's <code>nextSibling</code> is <code>null</code>
     */
    private DomNode nextSibling_;

    /** Start of the child list. */
    private DomNode firstChild_;

    /**
     * This is the JavaScript object corresponding to this DOM node. It may
     * be null if there isn't a corresponding JavaScript object.
     */
    private ScriptableObject scriptObject_;

    /** The ready state is is an IE-only value that is available to a large number of elements. */
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

    private boolean directlyAttachedToPage_;

    private Collection<DomChangeListener> domListeners_;
    private final Object domListeners_lock_ = new Serializable() { };

    /**
     * Never call this, used for Serialization.
     */
    @Deprecated
    protected DomNode() {
        this(null);
    }

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
    void setStartLocation(final int startLineNumber, final int startColumnNumber) {
        startLineNumber_ = startLineNumber;
        startColumnNumber_ = startColumnNumber;
    }

    /**
     * Sets the line and column numbers in the source page where the DOM node ends.
     *
     * @param endLineNumber the line number where the DOM node ends
     * @param endColumnNumber the column number where the DOM node ends
     */
    void setEndLocation(final int endLineNumber, final int endColumnNumber) {
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
    public SgmlPage getPage() {
        return page_;
    }

    /**
     * {@inheritDoc}
     */
    public Document getOwnerDocument() {
        return getPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Sets the JavaScript object that corresponds to this node. This is not guaranteed to be set even if
     * there is a JavaScript object for this DOM node.
     *
     * @param scriptObject the JavaScript object
     */
    public void setScriptObject(final ScriptableObject scriptObject) {
        scriptObject_ = scriptObject;
    }

    /**
     * {@inheritDoc}
     */
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
    public DomNode getNextSibling() {
        return nextSibling_;
    }

    /**
     * {@inheritDoc}
     */
    public DomNode getFirstChild() {
        return firstChild_;
    }

    /**
     * Returns <tt>true</tt> if this node is an ancestor of the specified node.
     *
     * @param node the node to check
     * @return <tt>true</tt> if this node is an ancestor of the specified node
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
     * Returns <tt>true</tt> if this node is an ancestor of any of the specified nodes.
     *
     * @param nodes the nodes to check
     * @return <tt>true</tt> if this node is an ancestor of any of the specified nodes
     */
    public boolean isAncestorOfAny(final DomNode... nodes) {
        for (final DomNode node : nodes) {
            if (isAncestorOf(node)) {
                return true;
            }
        }
        return false;
    }

    /** @param previous set the previousSibling field value */
    protected void setPreviousSibling(final DomNode previous) {
        previousSibling_ = previous;
    }

    /** @param next set the nextSibling field value */
    protected void setNextSibling(final DomNode next) {
        nextSibling_ = next;
    }

    /**
     * Returns this node's node type.
     * @return this node's node type
     */
    public abstract short getNodeType();

    /**
     * Returns this node's node name.
     * @return this node's node name
     */
    public abstract String getNodeName();

    /**
     * {@inheritDoc}
     */
    public String getNamespaceURI() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getLocalName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getPrefix() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setPrefix(final String prefix) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasChildNodes() {
        return firstChild_ != null;
    }

    /**
     * {@inheritDoc}
     */
    public DomNodeList<DomNode> getChildNodes() {
        return new SiblingDomNodeList(this);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean isSupported(final String namespace, final String featureName) {
        throw new UnsupportedOperationException("DomNode.isSupported is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
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
     * Not yet implemented.
     */
    public String getBaseURI() {
        throw new UnsupportedOperationException("DomNode.getBaseURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public short compareDocumentPosition(final Node other) {
        if (other == this) {
            return 0; // strange, no constant available?
        }

        // get ancestors of both
        final List<Node> myAncestors = getAncestors(true);
        final List<Node> otherAncestors = ((DomNode) other).getAncestors(true);

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
     * Gets the ancestors of the node.
     * @param includeSelf should this node be returned too
     * @return a list of the ancestors with the root at the first position
     */
    protected List<Node> getAncestors(final boolean includeSelf) {
        final List<Node> list = new ArrayList<Node>();
        if (includeSelf) {
            list.add(this);
        }
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
    public void setTextContent(final String textContent) {
        removeAllChildren();
        if (textContent != null) {
            appendChild(new DomText(getPage(), textContent));
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSameNode(final Node other) {
        return other == this;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String lookupPrefix(final String namespaceURI) {
        throw new UnsupportedOperationException("DomNode.lookupPrefix is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean isDefaultNamespace(final String namespaceURI) {
        throw new UnsupportedOperationException("DomNode.isDefaultNamespace is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String lookupNamespaceURI(final String prefix) {
        throw new UnsupportedOperationException("DomNode.lookupNamespaceURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean isEqualNode(final Node arg) {
        throw new UnsupportedOperationException("DomNode.isEqualNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Object getFeature(final String feature, final String version) {
        throw new UnsupportedOperationException("DomNode.getFeature is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Object getUserData(final String key) {
        throw new UnsupportedOperationException("DomNode.getUserData is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Object setUserData(final String key, final Object data, final UserDataHandler handler) {
        throw new UnsupportedOperationException("DomNode.setUserData is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasAttributes() {
        return false;
    }

    /**
     * Returns a flag indicating whether or not this node should have any leading and trailing
     * whitespace removed when {@link #asText()} is called. This method should usually return
     * <tt>true</tt>, but must return <tt>false</tt> for such things as text formatting tags.
     *
     * @return a flag indicating whether or not this node should have any leading and trailing
     *         whitespace removed when {@link #asText()} is called
     */
    protected boolean isTrimmedText() {
        return true;
    }

    /**
     * <p>Returns <tt>true</tt> if this node is displayed and can be visible to the user
     * (ignoring screen size, scrolling limitations, color, font-size, or overlapping nodes).</p>
     *
     * <p><b>NOTE:</b> If CSS is
     * {@link com.gargoylesoftware.htmlunit.WebClientOptions#setCssEnabled(boolean) disabled}, this method
     * does <b>not</b> take this element's style into consideration!</p>
     *
     * @see <a href="http://www.w3.org/TR/CSS2/visufx.html#visibility">CSS2 Visibility</a>
     * @see <a href="http://www.w3.org/TR/CSS2/visuren.html#propdef-display">CSS2 Display</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms531180.aspx">MSDN Documentation</a>
     * @return <tt>true</tt> if the node is visible to the user, <tt>false</tt> otherwise
     * @see #mayBeDisplayed()
     */
    public boolean isDisplayed() {
        if (!mayBeDisplayed()) {
            return false;
        }
        final Page page = getPage();
        if (page instanceof HtmlPage && page.getEnclosingWindow().getWebClient().getOptions().isCssEnabled()) {
            // display: iterate top to bottom, because if a parent is display:none,
            // there's nothing that a child can do to override it
            for (final Node node : getAncestors(true)) {
                final ScriptableObject scriptableObject = ((DomNode) node).getScriptObject();
                if (scriptableObject instanceof HTMLElement) {
                    final CSSStyleDeclaration style = ((HTMLElement) scriptableObject).getCurrentStyle();
                    final String display = style.getDisplay();
                    if ("none".equals(display)) {
                        return false;
                    }
                }
            }
            // visibility: iterate bottom to top, because children can override
            // the visibility used by parent nodes
            final boolean collapseInvisible = hasFeature(DISPLAYED_COLLAPSE);
            DomNode node = this;
            do {
                final ScriptableObject scriptableObject = node.getScriptObject();
                if (scriptableObject instanceof HTMLElement) {
                    final CSSStyleDeclaration style = ((HTMLElement) scriptableObject).getCurrentStyle();
                    final String visibility = style.getVisibility();
                    if (visibility.length() > 0) {
                        if ("visible".equals(visibility)) {
                            return true;
                        }
                        else if ("hidden".equals(visibility) || (collapseInvisible && "collapse".equals(visibility))) {
                            return false;
                        }
                    }
                }
                node = node.getParentNode();
            } while (node != null);
        }
        return true;
    }

    /**
     * Returns <tt>true</tt> if nodes of this type can ever be displayed, <tt>false</tt> otherwise. Examples of nodes
     * that can never be displayed are <tt>&lt;head&gt;</tt>, <tt>&lt;meta&gt;</tt>, <tt>&lt;script&gt;</tt>, etc.
     * @return <tt>true</tt> if nodes of this type can ever be displayed, <tt>false</tt> otherwise
     * @see #isDisplayed()
     */
    public boolean mayBeDisplayed() {
        return true;
    }

    /**
     * Returns a textual representation of this element that represents what would
     * be visible to the user if this page was shown in a web browser. For example,
     * a single-selection select element would return the currently selected value
     * as text.
     *
     * @return a textual representation of this element that represents what would
     *         be visible to the user if this page was shown in a web browser
     */
    public String asText() {
        final HtmlSerializer ser = new HtmlSerializer();
        return ser.asText(this);
    }

    /**
     * Indicates if the text representation of this element is made as a block, ie if new lines need
     * to be inserted before and after it.
     * @return <code>true</code> if this element represents a block
     */
    protected boolean isBlock() {
        return false;
    }

    /**
     * Returns a string representation of the XML document from this element and all it's children (recursively).
     * The charset used is the current page encoding.
     *
     * @return the XML string
     */
    public String asXml() {
        String charsetName = null;
        if (getPage() instanceof HtmlPage) {
            charsetName = ((HtmlPage) getPage()).getPageEncoding();
        }
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        if (charsetName != null && this instanceof HtmlHtml) {
            printWriter.println("<?xml version=\"1.0\" encoding=\"" + charsetName + "\"?>");
        }
        printXml("", printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * Recursively writes the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    protected void printXml(final String indent, final PrintWriter printWriter) {
        printWriter.println(indent + this);
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
    public String getNodeValue() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setNodeValue(final String value) {
        // Default behavior is to do nothing, overridden in some subclasses
    }

    /**
     * {@inheritDoc}
     */
    public DomNode cloneNode(final boolean deep) {
        final DomNode newnode;
        try {
            newnode = (DomNode) clone();
        }
        catch (final CloneNotSupportedException e) {
            throw new IllegalStateException("Clone not supported for node [" + this + "]");
        }

        newnode.parent_ = null;
        newnode.nextSibling_ = null;
        newnode.previousSibling_ = null;
        newnode.firstChild_ = null;
        newnode.scriptObject_ = null;

        // if deep, clone the kids too.
        if (deep) {
            for (DomNode child = firstChild_; child != null; child = child.nextSibling_) {
                newnode.appendChild(child.cloneNode(true));
            }
        }
        return newnode;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the JavaScript object that corresponds to this node, lazily initializing a new one if necessary.
     *
     * The logic of when and where the JavaScript object is created needs a clean up: functions using
     * a DOM node's JavaScript object should not have to check if they should create it first.
     *
     * @return the JavaScript object that corresponds to this node
     */
    public ScriptableObject getScriptObject() {
        if (scriptObject_ == null) {
            final SgmlPage page = getPage();
            if (this == getPage()) {
                final StringBuilder msg = new StringBuilder("No script object associated with the Page.");
                // because this is a strange case we like to provide as many info as possible
                msg.append(" class: '");
                msg.append(page.getClass().getName());
                msg.append("'");
                try {
                    msg.append(" url: '" + page.getUrl() + "'");
                    msg.append(" content: ");
                    msg.append(page.getWebResponse().getContentAsString());
                }
                catch (final Exception e) {
                    // ok bad luck with detail
                    msg.append(" no details: '" + e.toString() + "'");
                }
                throw new IllegalStateException(msg.toString());
            }
            scriptObject_ = ((SimpleScriptable) ((DomNode) page_).getScriptObject()).makeScriptableFor(this);
        }
        return scriptObject_;
    }

    /**
     * {@inheritDoc}
     */
    public DomNode appendChild(final Node node) {
        if (node == this) {
            if (!hasFeature(NODE_APPEND_CHILD_SELF_IGNORE)) {
                Context.throwAsScriptRuntimeEx(new Exception("Can not add not to itself " + this));
            }
            return this;
        }
        final DomNode domNode = (DomNode) node;
        if (domNode.isDescendant(this)) {
            Context.throwAsScriptRuntimeEx(new Exception("Can not add (grand)parent to itself " + this));
        }

        if (domNode instanceof DomDocumentFragment) {
            final DomDocumentFragment fragment = (DomDocumentFragment) domNode;
            for (final DomNode child : fragment.getChildren()) {
                appendChild(child);
            }
        }
        else {
            // clean up the new node, in case it is being moved
            if (domNode != this && domNode.getParentNode() != null) {
                domNode.remove();
            }
            // move the node
            basicAppend(domNode);

            fireAddition(domNode);
        }

        return domNode;
    }

    private void fireAddition(final DomNode domNode) {
        final boolean wasAlreadyAttached = domNode.isDirectlyAttachedToPage();
        domNode.directlyAttachedToPage_ = isDirectlyAttachedToPage();

        // trigger events
        if (!(this instanceof DomDocumentFragment) && (getPage() instanceof HtmlPage)) {
            ((HtmlPage) getPage()).notifyNodeAdded(domNode);
        }

        // a node that is already "complete" (ie not being parsed) and not yet attached
        if (!domNode.isBodyParsed() && isDirectlyAttachedToPage() && !wasAlreadyAttached) {
            domNode.onAddedToPage();
            for (final DomNode child : domNode.getDescendants()) {
                child.directlyAttachedToPage_ = true;
                child.onAllChildrenAddedToPage(true);
            }
            domNode.onAllChildrenAddedToPage(true);
        }

        fireNodeAdded(this, domNode);
    }

    /**
     * Indicates if the current node is being parsed. This means that the opening tag has already been
     * parsed but not the body and end tag.
     */
    private boolean isBodyParsed() {
        return getStartLineNumber() != -1 && getEndLineNumber() == -1;
    }

    /**
     * Quietly removes this node and moves its children to the specified destination. "Quietly" means
     * that no node events are fired. This method is not appropriate for most use cases. It should
     * only be used in specific cases for HTML parsing hackery.
     *
     * @param destination the node to which this node's children should be moved before this node is removed
     */
    void quietlyRemoveAndMoveChildrenTo(final DomNode destination) {
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
     * Appends the specified node to the end of this node's children, assuming the specified
     * node is clean (doesn't have preexisting relationships to other nodes.
     *
     * @param node the node to append to this node's children
     */
    private void basicAppend(final DomNode node) {
        node.setPage(getPage());
        if (firstChild_ == null) {
            firstChild_ = node;
            firstChild_.previousSibling_ = node;
        }
        else {
            final DomNode last = getLastChild();
            last.nextSibling_ = node;
            node.previousSibling_ = last;
            node.nextSibling_ = null; // safety first
            firstChild_.previousSibling_ = node; // new last node
        }
        node.parent_ = this;
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
     * {@inheritDoc}
     */
    public Node insertBefore(final Node newChild, final Node refChild) {
        if (refChild == null) {
            appendChild(newChild);
        }
        else {
            if (refChild.getParentNode() != this) {
                throw new DOMException(DOMException.NOT_FOUND_ERR, "Reference node is not a child of this node.");
            }
            ((DomNode) refChild).insertBefore((DomNode) newChild);
        }
        return null;
    }

    /**
     * Inserts a new child node before this node into the child relationship this node is a
     * part of. If the specified node is this node, this method is a no-op.
     *
     * @param newNode the new node to insert
     * @throws IllegalStateException if this node is not a child of any other node
     */
    public void insertBefore(final DomNode newNode) throws IllegalStateException {
        if (previousSibling_ == null) {
            throw new IllegalStateException("Previous sibling for " + this + " is null.");
        }

        if (newNode == this) {
            return;
        }

        //clean up the new node, in case it is being moved
        final DomNode exParent = newNode.getParentNode();
        newNode.basicRemove();

        if (parent_.firstChild_ == this) {
            parent_.firstChild_ = newNode;
        }
        else {
            previousSibling_.nextSibling_ = newNode;
        }
        newNode.previousSibling_ = previousSibling_;
        newNode.nextSibling_ = this;
        previousSibling_ = newNode;
        newNode.parent_ = parent_;
        newNode.setPage(page_);

        fireAddition(newNode);

        if (exParent != null) {
            fireNodeDeleted(exParent, newNode);
            exParent.fireNodeDeleted(exParent, this);
        }
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
    public NamedNodeMap getAttributes() {
        return NamedAttrNodeMapImpl.EMPTY_MAP;
    }

    /**
     * {@inheritDoc}
     */
    public Node removeChild(final Node child) {
        if (child.getParentNode() != this) {
            throw new DOMException(DOMException.NOT_FOUND_ERR, "Node is not a child of this node.");
        }
        ((DomNode) child).remove();
        return child;
    }

    /**
     * Removes this node from all relationships with other nodes.
     */
    public void remove() {
        final DomNode exParent = parent_;
        basicRemove();

        if (getPage() instanceof HtmlPage) {
            ((HtmlPage) getPage()).notifyNodeRemoved(this);
        }

        if (exParent != null) {
            fireNodeDeleted(exParent, this);
            //ask ex-parent to fire event (because we don't have parent now)
            exParent.fireNodeDeleted(exParent, this);
        }
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
    }

    /**
     * {@inheritDoc}
     */
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
     * @throws IllegalStateException if this node is not a child of any other node
     */
    public void replace(final DomNode newNode) throws IllegalStateException {
        if (newNode != this) {
            newNode.remove();
            insertBefore(newNode);
            remove();
        }
    }

    /**
     * Lifecycle method invoked whenever a node is added to a page. Intended to
     * be overridden by nodes which need to perform custom logic when they are
     * added to a page. This method is recursive, so if you override it, please
     * be sure to call <tt>super.onAddedToPage()</tt>.
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
     * <tt>super.onAllChildrenAddedToPage()</tt> if you implement this method.
     * @param postponed whether to use {@link com.gargoylesoftware.htmlunit.javascript.PostponedAction} or no
     */
    protected void onAllChildrenAddedToPage(final boolean postponed) {
        // Empty by default.
    }

    /**
     * @return an Iterable over the children of this node
     */
    public final Iterable<DomNode> getChildren() {
        return new Iterable<DomNode>() {
            public Iterator<DomNode> iterator() {
                return new ChildIterator();
            }
        };
    }

    /**
     * An iterator over all children of this node.
     */
    protected class ChildIterator implements Iterator<DomNode> {

        private DomNode nextNode_ = firstChild_;
        private DomNode currentNode_ = null;

        /** {@inheritDoc} */
        public boolean hasNext() {
            return nextNode_ != null;
        }

        /** {@inheritDoc} */
        public DomNode next() {
            if (nextNode_ != null) {
                currentNode_ = nextNode_;
                nextNode_ = nextNode_.nextSibling_;
                return currentNode_;
            }
            throw new NoSuchElementException();
        }

        /** {@inheritDoc} */
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
        return new Iterable<DomNode>() {
            public Iterator<DomNode> iterator() {
                return new DescendantElementsIterator<DomNode>(DomNode.class);
            }
        };
    }

    /**
     * Returns an {@link Iterable} that will recursively iterate over all of this node's {@link HtmlElement}
     * descendants. If you want to iterate over all descendants (including {@link DomText} elements,
     * {@link DomComment} elements, etc.), please use {@link #getDescendants()}.
     * @return an {@link Iterable} that will recursively iterate over all of this node's {@link HtmlElement}
     *         descendants
     */
    public final Iterable<HtmlElement> getHtmlElementDescendants() {
        return new Iterable<HtmlElement>() {
            public Iterator<HtmlElement> iterator() {
                return new DescendantElementsIterator<HtmlElement>(HtmlElement.class);
            }
        };
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
        public boolean hasNext() {
            return nextNode_ != null;
        }

        /** {@inheritDoc} */
        public T next() {
            return nextNode();
        }

        /** {@inheritDoc} */
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
            final DomNode parent = startingNode.getParentNode();
            if (parent == null || parent == DomNode.this) {
                return null;
            }
            DomNode next = parent.getNextSibling();
            while (next != null && !isAccepted(next)) {
                next = next.getNextSibling();
            }
            if (next == null) {
                return getNextElementUpwards(parent);
            }
            return next;
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
         * @return <code>true</code> if accepted
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
     * Removes all of this node's children.
     */
    public void removeAllChildren() {
        while (getFirstChild() != null) {
            getFirstChild().remove();
        }
    }

    /**
     * Evaluates the specified XPath expression from this node, returning the matching elements.
     *
     * @param xpathExpr the XPath expression to evaluate
     * @return the elements which match the specified XPath expression
     * @see #getFirstByXPath(String)
     * @see #getCanonicalXPath()
     */
    public List<?> getByXPath(final String xpathExpr) {
        return XPathUtils.getByXPath(this, xpathExpr);
    }

    /**
     * Evaluates the specified XPath expression from this node, returning the first matching element,
     * or <tt>null</tt> if no node matches the specified XPath expression.
     *
     * @param xpathExpr the XPath expression
     * @param <X> the expression type
     * @return the first element matching the specified XPath expression
     * @see #getByXPath(String)
     * @see #getCanonicalXPath()
     */
    @SuppressWarnings("unchecked")
    public <X> X getFirstByXPath(final String xpathExpr) {
        final List<?> results = getByXPath(xpathExpr);
        if (results.isEmpty()) {
            return null;
        }
        return (X) results.get(0);
    }

    /**
     * <p>Returns the canonical XPath expression which identifies this node, for instance
     * <tt>"/html/body/table[3]/tbody/tr[5]/td[2]/span/a[3]"</tt>.</p>
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
        synchronized (domListeners_lock_) {
            if (domListeners_ == null) {
                domListeners_ = new LinkedHashSet<DomChangeListener>();
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
        synchronized (domListeners_lock_) {
            if (domListeners_ != null) {
                domListeners_.remove(listener);
            }
        }
    }

    /**
     * Support for reporting DOM changes. This method can be called when a node has been added and it
     * will send the appropriate {@link DomChangeEvent} to any registered {@link DomChangeListener}s.
     *
     * Note that this method recursively calls this node's parent's {@link #fireNodeAdded(DomNode, DomNode)}.
     *
     * @param parentNode the parent of the node that was added
     * @param addedNode the node that was added
     */
    protected void fireNodeAdded(final DomNode parentNode, final DomNode addedNode) {
        final List<DomChangeListener> listeners = safeGetDomListeners();
        if (listeners != null) {
            final DomChangeEvent event = new DomChangeEvent(parentNode, addedNode);
            for (final DomChangeListener listener : listeners) {
                listener.nodeAdded(event);
            }
        }
        if (parent_ != null) {
            parent_.fireNodeAdded(parentNode, addedNode);
        }
    }

    /**
     * Support for reporting DOM changes. This method can be called when a node has been deleted and it
     * will send the appropriate {@link DomChangeEvent} to any registered {@link DomChangeListener}s.
     *
     * Note that this method recursively calls this node's parent's {@link #fireNodeDeleted(DomNode, DomNode)}.
     *
     * @param parentNode the parent of the node that was deleted
     * @param deletedNode the node that was deleted
     */
    protected void fireNodeDeleted(final DomNode parentNode, final DomNode deletedNode) {
        final List<DomChangeListener> listeners = safeGetDomListeners();
        if (listeners != null) {
            final DomChangeEvent event = new DomChangeEvent(parentNode, deletedNode);
            for (final DomChangeListener listener : listeners) {
                listener.nodeDeleted(event);
            }
        }
        if (parent_ != null) {
            parent_.fireNodeDeleted(parentNode, deletedNode);
        }
    }

    private List<DomChangeListener> safeGetDomListeners() {
        synchronized (domListeners_lock_) {
            if (domListeners_ != null) {
                return new ArrayList<DomChangeListener>(domListeners_);
            }
            return null;
        }
    }

    /**
     * Retrieves all element nodes from descendants of the starting element node that match any selector
     * within the supplied selector strings.
     * @param selectors one or more CSS selectors separated by commas
     * @return list of all found nodes
     */
    public DomNodeList<DomNode> querySelectorAll(final String selectors) {
        final List<DomNode> elements = new ArrayList<DomNode>();
        try {
            final WebClient webClient = getPage().getWebClient();
            final AtomicBoolean errorOccured = new AtomicBoolean(false);
            final ErrorHandler errorHandler = new ErrorHandler() {
                public void warning(final CSSParseException exception) throws CSSException {
                    // ignore
                }

                public void fatalError(final CSSParseException exception) throws CSSException {
                    errorOccured.set(true);
                }

                public void error(final CSSParseException exception) throws CSSException {
                    errorOccured.set(true);
                }
            };
            final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
            parser.setErrorHandler(errorHandler);
            final SelectorList selectorList = parser.parseSelectors(new InputSource(new StringReader(selectors)));
            // in case of error parseSelectors returns null
            if (errorOccured.get()) {
                throw new CSSException("Invalid selectors: " + selectors);
            }
            if (null != selectorList) {
                final BrowserVersion browserVersion = webClient.getBrowserVersion();
                int documentMode = 9;
                if (browserVersion.hasFeature(QUERYSELECTORALL_NOT_IN_QUIRKS)) {
                    final ScriptableObject sobj = getPage().getScriptObject();
                    if (sobj instanceof HTMLDocument) {
                        documentMode = ((HTMLDocument) sobj).getDocumentMode();
                    }
                }
                CSSStyleSheet.validateSelectors(selectorList, documentMode);

                for (final HtmlElement child : getHtmlElementDescendants()) {
                    for (int i = 0; i < selectorList.getLength(); i++) {
                        final Selector selector = selectorList.item(i);
                        if (CSSStyleSheet.selects(browserVersion, selector, child)) {
                            elements.add(child);
                            break;
                        }
                    }
                }
            }
        }
        catch (final IOException e) {
            throw new CSSException("Error parsing CSS selectors from '" + selectors + "': " + e.getMessage());
        }
        return new StaticDomNodeList(elements);
    }

    /**
     * Returns the first element within the document that matches the specified group of selectors.
     * @param selectors one or more CSS selectors separated by commas
     * @return null if no matches are found; otherwise, it returns the first matching element
     */
    public DomNode querySelector(final String selectors) {
        final DomNodeList<DomNode> list = querySelectorAll(selectors);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Indicates if this node is currently directly attached to the page.
     * @return <code>true</code> if the page is one ancestor of the node.
     */
    protected boolean isDirectlyAttachedToPage() {
        return directlyAttachedToPage_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Lifecycle method to support special processing for js method importNode.
     * @param doc the import target document
     * @see com.gargoylesoftware.htmlunit.javascript.host.Document#importNode(
     * com.gargoylesoftware.htmlunit.javascript.host.Node, boolean)
     * @see HtmlScript#processImportNode(Document)
     */
    public void processImportNode(final com.gargoylesoftware.htmlunit.javascript.host.Document doc) {
        // empty default impl
    }

    /**
     * Helper for a common call sequence.
     * @param feature the feature to check
     * @return <code>true</code> if the currently emulated browser has this feature.
     */
    protected boolean hasFeature(final BrowserVersionFeatures feature) {
        return getPage().getWebClient().getBrowserVersion().hasFeature(feature);
    }

    /**
     * Checks whether the specified node is descendant of this node or not.
     * @param node the node to check if it is descendant or not
     * @return whether the specified node is descendant of this node or not
     */
    protected boolean isDescendant(final DomNode node) {
        for (DomNode parent = node; parent != null; parent = parent.getParentNode()) {
            if (parent == this) {
                return true;
            }
        }
        return false;
    }

}
