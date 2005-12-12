/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
import java.io.StringWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 *  Base class for nodes in the Html DOM tree. This class is modelled after the
 * W3c DOM specification, but does not implement it.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Mike Williams
 * @author Marc Guillemot
 * @author Denis N. Antonioli
 */
public abstract class DomNode implements Cloneable {
    /**
     * node type constant for the <code>Document</code> node.
     */
    public static final short DOCUMENT_NODE             = 0;
    /**
     * node type constant for <code>Element</code> nodes.
     */
    public static final short ELEMENT_NODE              = 1;
    /**
     * node type constant for <code>Text</code> nodes.
     */
    public static final short TEXT_NODE                 = 3;
    /**
     * node type constant for <code>Attribute</code> nodes.
     */
    public static final short ATTRIBUTE_NODE             = 4;

    /**
     * readyState constant for IE.
     */
    public static final String READY_STATE_UNINITIALIZED = "uninitialized";

    /**
     * readyState constant for IE.
     */
    public static final String READY_STATE_LOADING = "loading";

    /**
     * readyState constant for IE.
     */
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
     * declared as Object so that we don't have a dependancy on the rhino jar
     * file.<p>
     *
     * It may be null if there isn't a corresponding javascript object.
     */
    private Object scriptObject_;

    /**
     * Ready state is is an IE only value that is available to a large number of elements.
     *
     */
    // TODO - Add processing for the other elements - Currently only body.
    private String readyState_ = READY_STATE_UNINITIALIZED;

    // We do lazy initialization on this since the vast majority of
    // HtmlElement instances won't need it.
    private PropertyChangeSupport propertyChangeSupport_ = null;

    /** The name of the "element" property.  Used when watching property change events. */
    public static final String PROPERTY_ELEMENT = "element";

    /**
     *  Create an instance
     *
     * @param htmlPage The page that contains this node
     */
    protected DomNode(final HtmlPage htmlPage) {
        readyState_ = READY_STATE_LOADING;      // At this time, I should be loading the object.
        htmlPage_ = htmlPage;
    }


    /**
     *  Return the HtmlPage that contains this node
     *
     * @return  See above
     */
    public HtmlPage getPage() {
        return htmlPage_;
    }

    /**
     * Internal use only - subject to change without notice.<p>
     * Set the javascript object that corresponds to this node.  This is not
     * guarenteed to be set even if there is a javascript object for this
     * DOM node.
     * @param scriptObject The javascript object.
     */
    public void setScriptObject( final Object scriptObject ) {
        scriptObject_ = scriptObject;
    }

    /**
     * Get the last child node.
     * @return The last child node or null if the current node has
     * no children.
     */
    public DomNode getLastChild() {
        if(firstChild_ != null) {
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
     */
    public DomNode getParentNode() {
        return parent_;
    }

    /**
     * set the aprent node
     * @param parent the parent node
     */
    protected void setParentNode(final DomNode parent) {
        parent_ = parent;
    }


    /**
     * @return the previous sibling of this node, or <code>null</code> if this is
     * the first node
     */
    public DomNode getPreviousSibling() {
        if(parent_ == null || this == parent_.firstChild_) {
            // previous sibling of first child points to last child
            return null;
        }
        else {
            return previousSibling_;
        }
    }

    /**
     * @return the next sibling
     */
    public DomNode getNextSibling() {
        return nextSibling_;
    }

    /**
     * @return the previous sibling
     */
    public DomNode getFirstChild() {
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
     *  Return a text representation of this element that represents what would
     *  be visible to the user if this page was shown in a web browser. For
     *  example, a select element would return the currently selected value as
     *  text
     *
     * @return  The element as text
     */
    public String asText() {
        String text = getChildrenAsText();

        // Translate non-breaking spaces to regular spaces.
        text = text.replace((char)160,' ');

        // Remove extra whitespace
        text = reduceWhitespace(text);
        return text;
    }


    /**
     *  Return a text string that represents all the child elements as they
     *  would be visible in a web browser
     *
     * @return  See above
     * @see  #asText()
     */
    protected final String getChildrenAsText() {
        final StringBuffer buffer = new StringBuffer();
        final Iterator childIterator = getChildIterator();

        if(!childIterator.hasNext()) {
            return "";
        }
        while(childIterator.hasNext()) {
            final DomNode node = (DomNode)childIterator.next();
            buffer.append(node.asText());
        }

        return buffer.toString();
    }


    /**
     * Removes extra whitespace from a string similar to what a browser does
     * when it displays text.
     * @param text The text to clean up.
     * @return The cleaned up text.
     */
    private static String reduceWhitespace( final String text ) {
        final StringBuffer buffer = new StringBuffer( text.length() );
        final int length = text.length();
        boolean whitespace = false;
        for( int i = 0; i < length; ++i) {
            final char ch = text.charAt(i);
            if( whitespace ) {
                if( Character.isWhitespace(ch) == false ) {
                    buffer.append(ch);
                    whitespace = false;
                }
            }
            else {
                if( Character.isWhitespace(ch) ) {
                    whitespace = true;
                    buffer.append(' ');
                }
                else {
                    buffer.append(ch);
                }
            }
        }
        return buffer.toString().trim();
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
    protected void printXml( final String indent, final PrintWriter printWriter ) {

        printWriter.println(indent+this);
        printChildrenAsXml( indent, printWriter );
    }

    /**
     * recursively write the XML data for the node tree starting at <code>node</code>
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    protected void printChildrenAsXml( final String indent, final PrintWriter printWriter ) {
        DomNode child = getFirstChild();
        while (child != null) {
            child.printXml(indent+"  ", printWriter);
            child = child.getNextSibling();
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
     * make a clone of this node
     *
     * @param deep if <code>true</code>, the clone will be propagated to the whole subtree
     * below this one. Otherwise, the new node will not have any children. The page reference
     * will always be the same as this node's.
     * @return a new node
     */
    public DomNode cloneNode(final boolean deep) {

        final DomNode newnode;
        try {
            newnode = (DomNode) clone();
        }
        catch( final CloneNotSupportedException e ) {
            throw new IllegalStateException("Clone not supported for node ["+this+"]");
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
     * Internal use only - subject to change without notice.<p>
     * The logic of when and where the js object is created needs a clean up: functions using
     * a js object of a dom node should not have to look if they should create it first 
     * Return the javascript object that corresponds to this node.
     * @return The javascript object that corresponds to this node building it if necessary.
     */
    public Object getScriptObject() {
        if (scriptObject_ == null) {
            scriptObject_ = ((SimpleScriptable) getPage().getScriptObject()).makeScriptableFor(this);
        }
        return scriptObject_;
    }

    /**
     * append a child node to the end of the current list
     * @param node the node to append
     * @return the node added
     */
    public DomNode appendChild(final DomNode node) {

        //clean up the new node, in case it is being moved
        if(node != this) {
            node.basicRemove();
        }
        if(firstChild_ == null) {
            firstChild_ = node;
            firstChild_.previousSibling_ = node;
        }
        else {
            final DomNode last = getLastChild();

            last.nextSibling_ = node;
            node.previousSibling_ = last;
            node.nextSibling_ = null; //safety first
            firstChild_.previousSibling_ = node; //new last node
        }
        node.parent_ = this;

        getHtmlPage().notifyNodeAdded(node);

        return node;
    }

    /**
     * Gets the html page to which this objects belongs
     * @return the html page
     */
    private HtmlPage getHtmlPage() {
        if (this instanceof HtmlPage) {
            return (HtmlPage) this;
        }
        else {
            return htmlPage_;
        }
    }

    /**
     * insert a new child node before this node into the child relationship this node is a
     * part of.
     *
     * @param newNode the new node to insert
     * @throws IllegalStateException if this node is not a child of any other node
     */
    public void insertBefore(final DomNode newNode) throws IllegalStateException {

        if(previousSibling_ == null) {
            throw new IllegalStateException();
        }

        //clean up the new node, in case it is being moved
        if(newNode != this) {
            newNode.basicRemove();
        }

        if(parent_.firstChild_ == this) {
            parent_.firstChild_ = newNode;
        }
        else {
            previousSibling_.nextSibling_ = newNode;
        }
        newNode.previousSibling_ = previousSibling_;
        newNode.nextSibling_ = this;
        previousSibling_ = newNode;
        newNode.parent_ = parent_;

        getHtmlPage().notifyNodeAdded(newNode);
    }

    /**
     * remove this node from all relationships this node has with siblings an parents
     * @throws IllegalStateException if this node is not a child of any other node
     */
    public void remove() throws IllegalStateException {
        if(previousSibling_ == null) {
            throw new IllegalStateException();
        }
        basicRemove();

        getHtmlPage().notifyNodeRemoved(this);
    }

    /**
     * cut off all relationships this node has with siblings an parents
     */
    private void basicRemove() {
        if(parent_ != null && parent_.firstChild_ == this) {
            parent_.firstChild_ = nextSibling_;
        }
        else if(previousSibling_ != null && previousSibling_.nextSibling_ == this) {
            previousSibling_.nextSibling_ = nextSibling_;
        }
        if(nextSibling_ != null && nextSibling_.previousSibling_ == this) {
            nextSibling_.previousSibling_ = previousSibling_;
        }
        if(parent_ != null && this == parent_.getLastChild()) {
            parent_.firstChild_.previousSibling_ = previousSibling_;
        }

        nextSibling_ = null;
        previousSibling_ = null;
        parent_ = null;
    }

    /**
     * replace this node with another node in the child relationship is part of
     *
     * @param newNode the node to replace this one
     * @throws IllegalStateException if this node is not a child of any other node
     */
    public void replace(final DomNode newNode) throws IllegalStateException {
        insertBefore(newNode);
        remove();
    }

    /**
     * @return an iterator over the children of this node
     */
    public Iterator getChildIterator() {
        return new ChildIterator();
    }

    /**
     * Add a property change listener to this node.
     * @param listener The new listener.
     */
    public final synchronized void addPropertyChangeListener(
        final PropertyChangeListener listener ) {

        Assert.notNull("listener", listener);
        if( propertyChangeSupport_ == null ) {
            propertyChangeSupport_ = new PropertyChangeSupport(this);
        }
        propertyChangeSupport_.addPropertyChangeListener(listener);
    }

    /**
     * Remove a property change listener from this node.
     * @param listener The istener.
     */
    public final synchronized void removePropertyChangeListener(
        final PropertyChangeListener listener ) {

        Assert.notNull("listener", listener);
        if( propertyChangeSupport_ != null ) {
            propertyChangeSupport_.removePropertyChangeListener(listener);
        }
    }

    /**
     * Fire a property change event
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected final synchronized void firePropertyChange(
        final String propertyName, final Object oldValue, final Object newValue ) {

        if( propertyChangeSupport_ != null ) {
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
            if(nextNode_ != null) {
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
            if(currentNode_ == null) {
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
    public DescendantElementsIterator getAllHtmlChildElements() {
        return new DescendantElementsIterator();
    }

    /**
     * an iterator over all HtmlElement descendants in document order
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

        /** remove the current object */
        public void remove() {
            if(nextElement_ == null) {
                throw new IllegalStateException();
            }
            if(nextElement_.getPreviousSibling() != null) {
                nextElement_.getPreviousSibling().remove();
            }
        }

        /** @return is there a next one? */
        public HtmlElement nextElement() {
            final HtmlElement result = nextElement_;
            setNextElement();
            return result;
        }

        private void setNextElement() {
            HtmlElement next = getFirstChildElement(nextElement_);
            if( next == null ) {
                next = getNextSibling(nextElement_);
            }
            if( next == null ) {
                next = getNextElementUpwards(nextElement_);
            }
            nextElement_ = next;
        }

        private HtmlElement getNextElementUpwards( final DomNode startingNode ) {
            if( startingNode == DomNode.this) {
                return null;
            }

            final DomNode parent = startingNode.getParentNode();
            if( parent == DomNode.this ) {
                return null;
            }

            DomNode next = parent.getNextSibling();
            while( next != null && next instanceof HtmlElement == false ) {
                next = next.getNextSibling();
            }

            if( next == null ) {
                return getNextElementUpwards(parent);
            }
            else {
                return (HtmlElement)next;
            }
        }

        private HtmlElement getFirstChildElement(final DomNode parent) {
            DomNode node = parent.getFirstChild();
            while( node != null && node instanceof HtmlElement == false ) {
                node = node.getNextSibling();
            }
            return (HtmlElement)node;
        }

        private HtmlElement getNextSibling( final HtmlElement element) {
            DomNode node = element.getNextSibling();
            while( node != null && node instanceof HtmlElement == false ) {
                node = node.getNextSibling();
            }
            return (HtmlElement)node;
        }
    }

    /**
     * Return the readyState of this element.  IE only.
     * @return String - The current readyState for this element.
     */
    public String getReadyState() {
        return readyState_;
    }

    /**
     * Set the readyState for this element.  IE only.
     * @param state - The value to set the current state to.
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
}
