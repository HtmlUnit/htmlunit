/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.JaxenException;
import org.jaxen.Navigator;
import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

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
 */
public abstract class DomNode implements Cloneable, Serializable {

    /**
     * Node type constant for the <code>Document</code> node.
     * @deprecated use {@link org.w3c.dom.Node#DOCUMENT_NODE} instead.
     */
    public static final short DOCUMENT_NODE = 9;

    /**
     * Node type constant for <code>Element</code> nodes.
     * @deprecated use {@link org.w3c.dom.Node#ELEMENT_NODE} instead.
     */
    public static final short ELEMENT_NODE = 1;

    /**
     * Node type constant for <code>Text</code> nodes.
     * @deprecated use {@link org.w3c.dom.Node#TEXT_NODE} instead.
     */
    public static final short TEXT_NODE = 3;

    /**
     * Node type constant for <code>Attribute</code> nodes.
     * @deprecated use {@link org.w3c.dom.Node#ATTRIBUTE_NODE} instead.
     */
    public static final short ATTRIBUTE_NODE = 2;

    /**
     * Node type constant for <code>Comment</code> nodes.
     * @deprecated use {@link org.w3c.dom.Node#COMMENT_NODE} instead.
     */
    public static final short COMMENT_NODE = 8;

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

    /** the owning page of this node */
    private final HtmlPage htmlPage_;

    /** the parent node */
    private DomNode parent_;

    /**
     * the previous sibling. The first child's <code>previousSibling</code> points
     * to the end of the list
     */
    private DomNode previousSibling_;

    /**
     * The next sibling. The last child's <code>nextSibling</code> is <code>null</code>
     */
    private DomNode nextSibling_;

    /** Start of the child list */
    private DomNode firstChild_;

    /**
     * This is the javascript object corresponding to this DOM node.  It is
     * declared as Object so that we don't have a dependency on the rhino jar
     * file.<p>
     *
     * It may be null if there isn't a corresponding javascript object.
     */
    private transient Object scriptObject_;

    /** The ready state is is an IE-only value that is available to a large number of elements. */
    private String readyState_;

    /** We do lazy initialization on this since the vast majority of HtmlElement instances won't need it. */
    private PropertyChangeSupport propertyChangeSupport_;

    /** The name of the "element" property.  Used when watching property change events. */
    public static final String PROPERTY_ELEMENT = "element";

    /**
     * The line number in the source page where the DOM node starts.
     */
    private int startLineNumber_;

    /**
     * The column number in the source page where the DOM node starts.
     */
    private int startColumnNumber_;

    /**
     * The line number in the source page where the DOM node ends.
     */
    private int endLineNumber_;

    /**
     * The column number in the source page where the DOM node ends.
     */
    private int endColumnNumber_;

    private List/* DomChangeListener */ domListeners_;

    /**
     * Never call this, used for Serialization.
     * @deprecated
     */
    protected DomNode() {
        this(null);
    }

    /**
     * Creates an instance.
     * @param htmlPage The page which contains this node.
     */
    protected DomNode(final HtmlPage htmlPage) {
        readyState_ = READY_STATE_LOADING;
        htmlPage_ = htmlPage;
        startLineNumber_ = 0;
        startColumnNumber_ = 0;
        endLineNumber_ = 0;
        endColumnNumber_ = 0;
    }

    /**
     * Set the line and column numbers in the source page where the
     * DOM node starts.
     *
     * @param startLineNumber The line number where the DOM node starts.
     * @param startColumnNumber The column number where the DOM node starts.
     */
    void setStartLocation(final int startLineNumber, final int startColumnNumber) {
        startLineNumber_ = startLineNumber;
        startColumnNumber_ = startColumnNumber;
    }
    
    /**
     * Set the line and column numbers in the source page where the
     * DOM node ends.
     *
     * @param endLineNumber The line number where the DOM node ends.
     * @param endColumnNumber The column number where the DOM node ends.
     */
    void setEndLocation(final int endLineNumber, final int endColumnNumber) {
        endLineNumber_ = endLineNumber;
        endColumnNumber_ = endColumnNumber;
    }

    /**
     * Get the line number in the source page where the DOM node starts.
     *
     * @return See above.
     */
    public int getStartLineNumber() {
        return startLineNumber_;
    }

    /**
     * Get the column number in the source page where the DOM node starts.
     *
     * @return See above.
     */
    public int getStartColumnNumber() {
        return startColumnNumber_;
    }

    /**
     * Get the line number in the source page where the DOM node ends.
     *
     * @return See above.
     */
    public int getEndLineNumber() {
        return endLineNumber_;
    }

    /**
     * Get the column number in the source page where the DOM node ends.
     *
     * @return See above.
     */
    public int getEndColumnNumber() {
        return endColumnNumber_;
    }

    /**
     *  Return the HtmlPage that contains this node
     *
     * @return See above
     */
    public HtmlPage getPage() {
        return htmlPage_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Set the javascript object that corresponds to this node.  This is not
     * guaranteed to be set even if there is a javascript object for this
     * DOM node.
     * @param scriptObject The javascript object.
     */
    public void setScriptObject(final Object scriptObject) {
        scriptObject_ = scriptObject;
    }

    /**
     * Get the last child node.
     * @return The last child node or null if the current node has
     * no children.
     * @deprecated This method conflicts with the W3C DOM API since the return values are
     * different.  Use getLastDomChild instead.
     */
    public DomNode getLastChild() {
        return getLastDomChild();
    }

    /**
     * Get the last child DomNode.
     * @return The last child node or null if the current node has
     * no children.
     */
    public DomNode getLastDomChild() {
        if (firstChild_ != null) {
            // last child is stored as the previous sibling of first child
            return firstChild_.previousSibling_;
        }
        else {
            return null;
        }
    }

    /**
     * @return the parent of this node, which may be <code>null</code> if this
     * is the root node
     * @deprecated This method conflicts with the W3C DOM API since the return values are
     * different.  Use getParentDomNode instead.
     */
    public DomNode getParentNode() {
        return getParentDomNode();
    }

    /**
     * @return the parent DomNode of this node, which may be <code>null</code> if this
     * is the root node
     */
    public DomNode getParentDomNode() {
        return parent_;
    }

    /**
     * Set the parent node
     * @param parent the parent node
     */
    protected void setParentNode(final DomNode parent) {
        parent_ = parent;
    }

    /**
     * @return the previous sibling of this node, or <code>null</code> if this is
     * the first node
     * @deprecated This method conflicts with the W3C DOM API since the return values are
     * different.  Use getPreviousDomSibling instead.
     */
    public DomNode getPreviousSibling() {
        return getPreviousDomSibling();
    }

    /**
     * @return the previous sibling of this node, or <code>null</code> if this is
     * the first node
     */
    public DomNode getPreviousDomSibling() {
        if (parent_ == null || this == parent_.firstChild_) {
            // previous sibling of first child points to last child
            return null;
        }
        else {
            return previousSibling_;
        }
    }

    /**
     * @return the next sibling
     * @deprecated This method conflicts with the W3C DOM API since the return values are
     * different.  Use getNextDomSibling instead.
     */
    public DomNode getNextSibling() {
        return getNextDomSibling();
    }

    /**
     * @return the next sibling
     */
    public DomNode getNextDomSibling() {
        return nextSibling_;
    }

    /**
     * @return the previous sibling
     * @deprecated This method conflicts with the W3C DOM API since the return values are
     * different.  Use getFirstDomChild instead.
     */
    public DomNode getFirstChild() {
        return getFirstDomChild();
    }

    /**
     * @return the previous sibling
     */
    public DomNode getFirstDomChild() {
        return firstChild_;
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
     * Get the type of the current node.
     * @return The node type
     */
    public abstract short getNodeType();

    /**
     * Get the name for the current node.
     * @return The node name
     */
    public abstract String getNodeName();

    /**
     *  Returns a flag indicating whether or not this node itself results
     *   in any space taken up in the browser windows; for instance, "<b>"
     *   affects the specified text, but does not use up any space itself
     *
     * @return The flag
     */
    protected boolean isRenderedVisible() {
        return false;
    }

    /**
     *  Returns a flag indicating whether or not this node should
     *   have any leading and trailing whitespace removed when asText()
     *   is called; mostly this should be true, but must be false for
     *   such things as text formatting tags
     *
     * @return The flag
     */
    protected boolean isTrimmedText() {
        return true;
    }
    
    /**
     *  Returns a text representation of this element that represents what would
     *  be visible to the user if this page was shown in a web browser. For
     *  example, a single-selection select element would return the currently selected
     *  value as text.
     *
     * @return The element as text.
     */
    public String asText() {
        String text = getChildrenAsText();
        text = reduceWhitespace(text);
        
        if (isTrimmedText()) {
            text = text.trim();
        }
        
        return text;
    }

    /**
     *  Return a text string that represents all the child elements as they
     *  would be visible in a web browser
     *
     * @return See above
     * @see #asText()
     */
    protected final String getChildrenAsText() {
        final StringBuffer buffer = new StringBuffer();
        final Iterator childIterator = getChildIterator();

        if (!childIterator.hasNext()) {
            return "";
        }
        boolean previousNodeWasText = false;
        final StringBuffer textBuffer = new StringBuffer();
        while (childIterator.hasNext()) {
            final DomNode node = (DomNode) childIterator.next();
            if (node instanceof DomText) {
                textBuffer.append(((DomText) node).getData());
                previousNodeWasText = true;
            }
            else {
                if (previousNodeWasText) {
                    // Whitespace between adjacent text nodes should reamin as a single
                    // space.  So, append raw adjacent text and reduce it as a whole.
                    buffer.append(reduceWhitespace(textBuffer.toString()));
                    textBuffer.setLength(0);
                    previousNodeWasText = false;
                }
                
                if (node.isRenderedVisible()) {
                    buffer.append(" ");
                    buffer.append(node.asText());
                    buffer.append(" ");
                }
                else if (node.getNodeName().equals("p")) {
                    // this is a bit kludgey, but we can't add the space
                    //  inside the node's asText(), since it doesn't belong
                    //  with the contents of the 'p' tag
                    buffer.append(" ");
                    buffer.append(node.asText());
                }
                else {
                    buffer.append(node.asText());
                }
            }
        }
        if (previousNodeWasText) {
            // we ended with text
            buffer.append(textBuffer.toString());
        }

        return buffer.toString();
    }

    /**
     * Removes extra whitespace from a string similar to what a browser does
     * when it displays text.
     * @param text The text to clean up.
     * @return The cleaned up text.
     */
    protected static String reduceWhitespace(final String text) {
        final StringBuffer buffer = new StringBuffer(text.length());
        final int length = text.length();
        boolean whitespace = false;
        for (int i = 0; i < length; i++) {
            final char ch = text.charAt(i);

            // Translate non-breaking space to regular space.
            if (ch == (char) 160) {
                buffer.append(' ');
                whitespace = false;
            }
            else {
                if (whitespace) {
                    if (!Character.isWhitespace(ch)) {
                        buffer.append(ch);
                        whitespace = false;
                    }
                }
                else {
                    if (Character.isWhitespace(ch)) {
                        whitespace = true;
                        buffer.append(' ');
                    }
                    else {
                        buffer.append(ch);
                    }
                }
            }
        }
        return buffer.toString();
    }

    /**
     * Return the log object for this element.
     * @return The log object for this element.
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Return a string representation of the xml document from this element and all
     * it's children (recursively).
     *
     * @return The xml string.
     */
    public String asXml() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        printXml("", printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * recursively write the XML data for the node tree starting at <code>node</code>
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    protected void printXml(final String indent, final PrintWriter printWriter) {
        printWriter.println(indent + this);
        printChildrenAsXml(indent, printWriter);
    }

    /**
     * recursively write the XML data for the node tree starting at <code>node</code>
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    protected void printChildrenAsXml(final String indent, final PrintWriter printWriter) {
        DomNode child = getFirstChild();
        while (child != null) {
            child.printXml(indent + "  ", printWriter);
            child = child.getNextDomSibling();
        }
    }

    /**
     * Get the value for the current node.
     * @return The node value
     */
    public String getNodeValue() {
        return null;
    }

    /**
     * @param x The new value
     */
    public void setNodeValue(final String x) {
        // Default behavior is to do nothing, overridden in some subclasses
    }

    /**
     * Make a clone of this node
     *
     * @param deep if <code>true</code>, the clone will be propagated to the whole subtree
     * below this one. Otherwise, the new node will not have any children. The page reference
     * will always be the same as this node's.
     * @return a new node
     * @deprecated This method conflicts with the W3C DOM API since the return values are
     * different.  Use cloneDomNode instead.
     */
    public DomNode cloneNode(final boolean deep) {
        return cloneDomNode(deep);
    }

    /**
     * Make a clone of this node
     *
     * @param deep if <code>true</code>, the clone will be propagated to the whole subtree
     * below this one. Otherwise, the new node will not have any children. The page reference
     * will always be the same as this node's.
     * @return a new node
     */
    public DomNode cloneDomNode(final boolean deep) {
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
                newnode.appendDomChild(child.cloneDomNode(true));
            }
        }
        return newnode;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * The logic of when and where the js object is created needs a clean up: functions using
     * a js object of a dom node should not have to look if they should create it first
     * Return the javascript object that corresponds to this node.
     * @return The javascript object that corresponds to this node building it if necessary.
     */
    public Object getScriptObject() {
        if (scriptObject_ == null) {
            if (this == getPage()) {
                throw new IllegalStateException("No script object associated with the HtmlPage");
            }
            scriptObject_ = ((SimpleScriptable) getPage().getScriptObject()).makeScriptableFor(this);
        }
        return scriptObject_;
    }

    /**
     * append a child node to the end of the current list
     * @param node the node to append
     * @return the node added
     * @deprecated This method conflicts with the W3C DOM API since the return values are
     * different.  Use appendDomChild instead.
     */
    public DomNode appendChild(final DomNode node) {
        return appendDomChild(node);
    }

    /**
     * append a child node to the end of the current list
     * @param node the node to append
     * @return the node added
     */
    public DomNode appendDomChild(final DomNode node) {

        //clean up the new node, in case it is being moved
        if (node != this) {
            node.basicRemove();
        }
        if (firstChild_ == null) {
            firstChild_ = node;
            firstChild_.previousSibling_ = node;
        }
        else {
            final DomNode last = getLastDomChild();

            last.nextSibling_ = node;
            node.previousSibling_ = last;
            node.nextSibling_ = null; //safety first
            firstChild_.previousSibling_ = node; //new last node
        }
        node.parent_ = this;

        getPage().notifyNodeAdded(node);
        
        fireNodeAddded(this, node);

        return node;
    }

    /**
     * Inserts a new child node before this node into the child relationship this node is a
     * part of.
     *
     * @param newNode the new node to insert
     * @throws IllegalStateException if this node is not a child of any other node
     */
    public void insertBefore(final DomNode newNode) throws IllegalStateException {

        if (previousSibling_ == null) {
            throw new IllegalStateException();
        }

        //clean up the new node, in case it is being moved
        if (newNode != this) {
            newNode.basicRemove();
        }

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

        getPage().notifyNodeAdded(newNode);
        fireNodeAddded(this, newNode);
    }

    /**
     * remove this node from all relationships this node has with siblings an parents
     * @throws IllegalStateException if this node is not a child of any other node
     */
    public void remove() throws IllegalStateException {
        if (previousSibling_ == null) {
            throw new IllegalStateException();
        }
        final DomNode exParent = parent_;
        basicRemove();
        getPage().notifyNodeRemoved(this);
        
        fireNodeDeleted(exParent, this);
        //ask ex-parent to fire event (because we don't have parent now)
        exParent.fireNodeDeleted(exParent, this);
    }

    /**
     * cut off all relationships this node has with siblings an parents
     */
    private void basicRemove() {
        if (parent_ != null && parent_.firstChild_ == this) {
            parent_.firstChild_ = nextSibling_;
        }
        else if (previousSibling_ != null && previousSibling_.nextSibling_ == this) {
            previousSibling_.nextSibling_ = nextSibling_;
        }
        if (nextSibling_ != null && nextSibling_.previousSibling_ == this) {
            nextSibling_.previousSibling_ = previousSibling_;
        }
        if (parent_ != null && this == parent_.getLastDomChild()) {
            parent_.firstChild_.previousSibling_ = previousSibling_;
        }

        nextSibling_ = null;
        previousSibling_ = null;
        parent_ = null;
    }

    /**
     * Replaces this node with another node.
     *
     * @param newNode the node to replace this one
     * @throws IllegalStateException if this node is not a child of any other node
     */
    public void replace(final DomNode newNode) throws IllegalStateException {
        insertBefore(newNode);
        remove();
    }

    /**
     * Lifecycle method invoked whenever a node is added to a page. Intended to
     * be overridden by nodes which need to perform custom logic when they are
     * added to a page. This method is recursive, so if you override it, please
     * be sure to call <tt>super.onAddedToPage()</tt>.
     */
    protected void onAddedToPage() {
        if (firstChild_ != null) {
            for (final Iterator i = getChildIterator(); i.hasNext();) {
                final DomNode child = (DomNode) i.next();
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
     */
    protected void onAllChildrenAddedToPage() {
        // Empty by default.
    }

    /**
     * @return an iterator over the children of this node
     */
    public Iterator getChildIterator() {
        return new ChildIterator();
    }
    
    // TODO: remove event handlers methods! Nothing to do in DomNode!
    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Return a Function to be executed when a given event occurs.
     * @param eventName Name of event such as "onclick" or "onblur", etc.
     * @return A rhino javascript executable Function, or null if no event
     * handler has been defined
     */
    public Function getEventHandler(final String eventName) {
        return null;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Register a Function as an event handler.
     * @param eventName Name of event such as "onclick" or "onblur", etc.
     * @param eventHandler A rhino javascript executable Function
     */
    public void setEventHandler(final String eventName, final Function eventHandler) {
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Register a snippet of javascript code as an event handler.  The javascript code will
     * be wrapped inside a unique function declaration which provides one argument named
     * "event"
     * @param eventName Name of event such as "onclick" or "onblur", etc.
     * @param jsSnippet executable javascript code
     */
    public void setEventHandler(final String eventName, final String jsSnippet) {
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Removes the specified event handler.
     * @param eventName Name of the event such as "onclick" or "onblur", etc.
     */
    public void removeEventHandler(final String eventName) {
    }

    /**
     * Add a property change listener to this node.
     * @param listener The new listener.
     * @deprecated Not used
     */
    //deprecated after 1.11
    public final synchronized void addPropertyChangeListener(
        final PropertyChangeListener listener) {

        Assert.notNull("listener", listener);
        if (propertyChangeSupport_ == null) {
            propertyChangeSupport_ = new PropertyChangeSupport(this);
        }
        propertyChangeSupport_.addPropertyChangeListener(listener);
    }

    /**
     * Remove a property change listener from this node.
     * @param listener The listener.
     * @deprecated Not used
     */
    //deprecated after 1.11
    public final synchronized void removePropertyChangeListener(
        final PropertyChangeListener listener) {

        Assert.notNull("listener", listener);
        if (propertyChangeSupport_ != null) {
            propertyChangeSupport_.removePropertyChangeListener(listener);
        }
    }

    /**
     * Fire a property change event
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     * @deprecated Not used
     */
    //deprecated after 1.11
    protected final synchronized void firePropertyChange(
        final String propertyName, final Object oldValue, final Object newValue) {

        if (propertyChangeSupport_ != null) {
            propertyChangeSupport_.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * an iterator over all children of this node
     */
    protected class ChildIterator implements Iterator {

        private DomNode nextNode_ = firstChild_;
        private DomNode currentNode_ = null;

        /** @return whether there is a next object */
        public boolean hasNext() {
            return nextNode_ != null;
        }

        /** @return the next object */
        public Object next() {
            if (nextNode_ != null) {
                currentNode_ = nextNode_;
                nextNode_ = nextNode_.nextSibling_;
                return currentNode_;
            }
            else {
                throw new NoSuchElementException();
            }
        }

        /** remove the current object */
        public void remove() {
            if (currentNode_ == null) {
                throw new IllegalStateException();
            }
            currentNode_.remove();
        }
    }

    /**
     * Return an iterator that will recursively iterate over every child element
     * below this one.
     * @return The iterator.
     */
    public Iterator getAllHtmlChildElements() {
        return new DescendantElementsIterator();
    }

    /**
     * An iterator over all HtmlElement descendants in document order.
     */
    protected class DescendantElementsIterator implements Iterator {

        private HtmlElement nextElement_ = getFirstChildElement(DomNode.this);

        /** @return is there a next one? */
        public boolean hasNext() {
            return nextElement_ != null;
        }

        /** @return the next one */
        public Object next() {
            return nextElement();
        }

        /** @throws UnsupportedOperationException always */
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        /** @return is there a next one? */
        public HtmlElement nextElement() {
            final HtmlElement result = nextElement_;
            setNextElement();
            return result;
        }

        private void setNextElement() {
            HtmlElement next = getFirstChildElement(nextElement_);
            if (next == null) {
                next = getNextDomSibling(nextElement_);
            }
            if (next == null) {
                next = getNextElementUpwards(nextElement_);
            }
            nextElement_ = next;
        }

        private HtmlElement getNextElementUpwards(final DomNode startingNode) {
            if (startingNode == DomNode.this) {
                return null;
            }

            final DomNode parent = startingNode.getParentDomNode();
            if (parent == DomNode.this) {
                return null;
            }

            DomNode next = parent.getNextDomSibling();
            while (next != null && next instanceof HtmlElement == false) {
                next = next.getNextDomSibling();
            }

            if (next == null) {
                return getNextElementUpwards(parent);
            }
            else {
                return (HtmlElement) next;
            }
        }

        private HtmlElement getFirstChildElement(final DomNode parent) {
            if (parent instanceof HtmlNoScript && getPage().getWebClient().isJavaScriptEnabled()) {
                return null;
            }
            DomNode node = parent.getFirstDomChild();
            while (node != null && node instanceof HtmlElement == false) {
                node = node.getNextDomSibling();
            }
            return (HtmlElement) node;
        }

        private HtmlElement getNextDomSibling(final HtmlElement element) {
            DomNode node = element.getNextDomSibling();
            while (node != null && node instanceof HtmlElement == false) {
                node = node.getNextDomSibling();
            }
            return (HtmlElement) node;
        }
    }

    /**
     * Return this node's ready state (IE only).
     * @return This node's ready state.
     */
    public String getReadyState() {
        return readyState_;
    }

    /**
     * Sets this node's ready state (IE only).
     * @param state This node's ready state.
     */
    public void setReadyState(final String state) {
        readyState_ = state;
    }

    /**
     * Remove all the children of this node.
     *
     */
    public void removeAllChildren() {
        if (getFirstChild() == null) {
            return;
        }
        final Iterator it = getChildIterator();
        DomNode child;
        while (it.hasNext()) {
            child = (DomNode) it.next();
            child.removeAllChildren();
            it.remove();
        }
    }

    /**
     * Facility to evaluate an xpath from the current node. The current node is considered as the
     * document root for the evaluation therefore parent nodes can't be reached.
     * @param xpathExpr the xpath expression
     * @return See {@link XPath#selectNodes(Object)}
     * @throws JaxenException if the xpath expression can't be parsed/evaluated
     */
    public List getByXPath(final String xpathExpr) throws JaxenException {
        if (xpathExpr == null) {
            throw new NullPointerException("Null is not a valid xpath expression");
        }

        final Navigator navigator = HtmlUnitXPath.buildSubtreeNavigator(this);
        final HtmlUnitXPath xpath = new HtmlUnitXPath(xpathExpr, navigator);
        return xpath.selectNodes(this);
    }
    
    /**
     * Facility to notify the registered {@link IncorrectnessListener} of something that is not fully correct.
     * @param message the notification
     */
    protected void notifyIncorrectness(final String message) {
        final IncorrectnessListener incorrectnessListener = getPage().getWebClient().getIncorrectnessListener();
        incorrectnessListener.notify(message, this);
    }

    /**
     * Adds a DomChangeListener to the listener list.
     * The listener is registered for all children nodes of this DomNode,
     * as well as the descendant nodes.
     *
     * @param listener the dom structure change listener to be added.
     * @see #removeDomChangeListener(DomChangeListener)
     */
    public void addDomChangeListener(final DomChangeListener listener) {
        Assert.notNull("listener", listener);
        synchronized (this) {
            if (domListeners_ == null) {
                domListeners_ = new ArrayList();
            }
            domListeners_.add(listener);
        }
    }

    /**
     * Removes an DomChangeListener from the listener list.
     * This method should be used to remove DomChangeListener that were registered
     * for all children nodes and descendant nodes of this DomNode.
     *
     * @param listener the dom structure change listener to be removed.
     * @see #addDomChangeListener(DomChangeListener)
     */
    public void removeDomChangeListener(final DomChangeListener listener) {
        Assert.notNull("listener", listener);
        synchronized (this) {
            if (domListeners_ != null) {
                domListeners_.remove(listener);
            }
        }
    }

    /**
     * Support for reporting dom changes.
     * This method can be called when a node has been added and it will send the
     * appropriate DomChangeEvent to any registered DomChangeListeners.
     *
     * Note that this methods recursively calls this parent fireNoddeAdded.
     *
     * @param parentNode the parent of the node that was added.
     * @param addedNode the node that was added.
     */
    protected void fireNodeAddded(final DomNode parentNode, final DomNode addedNode) {
        if (domListeners_ != null) {
            final DomChangeEvent event = new DomChangeEvent(parentNode, addedNode);
            synchronized (this) {
                for (final Iterator iterator = domListeners_.iterator(); iterator.hasNext();) {
                    final DomChangeListener listener = (DomChangeListener) iterator.next();
                    listener.nodeAdded(event);
                }
            }
        }
        if (parent_ != null) {
            parent_.fireNodeAddded(parentNode, addedNode);
        }
    }

    /**
     * Support for reporting dom changes.
     * This method can be called when a node has been deleted and it will send the
     * appropriate DomChangeEvent to any registered DomChangeListeners.
     *
     * Note that this methods recursively calls this parent fireNoddeDeleted.
     *
     * @param parentNode the parent of the node that was deleted.
     * @param deletedNode the node that was deleted.
     */
    protected void fireNodeDeleted(final DomNode parentNode, final DomNode deletedNode) {
        if (domListeners_ != null) {
            final DomChangeEvent event = new DomChangeEvent(parentNode, deletedNode);
            synchronized (this) {
                for (final Iterator iterator = domListeners_.iterator(); iterator.hasNext();) {
                    final DomChangeListener listener = (DomChangeListener) iterator.next();
                    listener.nodeDeleted(event);
                }
            }
        }
        if (parent_ != null) {
            parent_.fireNodeDeleted(parentNode, deletedNode);
        }
    }
}
