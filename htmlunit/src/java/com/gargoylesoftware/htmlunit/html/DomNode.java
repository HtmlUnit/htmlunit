/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.Assert;

/**
 *  An abstract wrapper for DOM nodes
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 */
public abstract class DomNode {
    private Node node_;
    private final HtmlPage htmlPage_;

    /**
     * This is the javascript object corresponding to this DOM node.  It is
     * declared as Object so that we don't have a dependancy on the rhino jar
     * file.<p>
     *
     * It may be null if there isn't a corresponding javascript object.
     */
    private Object scriptObject_;

    // We do lazy initialization on this since the vast majority of
    // HtmlElement instances won't need it.
    private PropertyChangeSupport propertyChangeSupport_ = null;

    /** The name of the "element" property.  Used when watching property change events. */
    public static final String PROPERTY_ELEMENT = "element";

    /**
     *  Create an instance
     *
     * @param htmlPage The page that contains this node
     * @param node The XML node that represents this DomNode
     */
    protected DomNode( final HtmlPage htmlPage, final Node node ) {
        if( this instanceof HtmlPage == false ) {
            Assert.notNull("node", node);
        }
        node_ = node;

        if( htmlPage == null && this instanceof HtmlPage ) {
            htmlPage_ = ( HtmlPage )this;
        }
        else {
            Assert.notNull( "htmlPage", htmlPage );
            htmlPage_ = htmlPage;
        }
    }


    /**
     * Set the DOM node that maps to this DomNode.
     * @param node The DOM node.
     */
    protected final void setNode( final Node node ) {
        Assert.notNull("node", node);

        if( node_ == null ) {
            final Object oldValue = node_;
            node_ = node;
            firePropertyChange(PROPERTY_ELEMENT, oldValue, node);
        }
        else {
            throw new IllegalStateException("node_ has already been set");
        }
    }


    /**
     *  Return the XML node that corresponds to this component.
     *
     * @return  The node
     */
    public Node getNode() {
        return node_;
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
     * Get the parent node of this node
     * @return the parent node of this node
     */
    public DomNode getParentNode() {
        return getPage().getDomNode(getNode().getParentNode());
    }


    /**
     * Return the next sibling node in the document
     * @return The next sibling node in the document.
     */
    public DomNode getNextSibling() {
        return getPage().getDomNode(getNode().getNextSibling());
    }


    /**
     * Return the previous sibling node in the document
     * @return The previous sibling node in the document.
     */
    public DomNode getPreviousSibling() {
        return getPage().getDomNode(getNode().getPreviousSibling());
    }


    /**
     * Get the first child node.
     * @return The first child node or null if the current node has
     * no children.
     */
    public DomNode getFirstChild() {
        return getPage().getDomNode(getNode().getFirstChild());
    }


    /**
     * Get the last child node.
     * @return The last child node or null if the current node has
     * no children.
     */
    public DomNode getLastChild() {
        return getPage().getDomNode(getNode().getLastChild());
    }


    /**
     * Get the type of the current node.
     * @return The node type
     */
    public short getNodeType() {
        return getNode().getNodeType();
    }


    /**
     * Get the name for the current node.
     * @return The node name
     */
    public String getNodeName() {
        return getNode().getNodeName();
    }


    /**
     * Get the value for the current node.
     * @return The node value
     */
    public String getNodeValue() {
        return getNode().getNodeValue();
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
     * Internal use only - subject to change without notice.<p>
     * Return the javascript object that corresponds to this node.
     * @return The javascript object that corresponds to this node.
     */
    public Object getScriptObject() {
        return scriptObject_;
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
}

