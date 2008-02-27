/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * The javascript object "Node" which is the base class for all DOM
 * objects.  This will typically wrap an instance of {@link DomNode}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Chris Erskine
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 */
public class Node extends SimpleScriptable {

    private HTMLCollection childNodes_; //has to be a member to have equality (==) working
    private static final long serialVersionUID = -5695262053081637445L;
    private EventListenersContainer eventListenersContainer_;

    /**
     * @see {@link org.w3c.dom.Node#ELEMENT_NODE}.
     */
    public static final short ELEMENT_NODE = org.w3c.dom.Node.ELEMENT_NODE;
    /**
     * @see {@link org.w3c.dom.Node#ATTRIBUTE_NODE}.
     */
    public static final short ATTRIBUTE_NODE = org.w3c.dom.Node.ATTRIBUTE_NODE;
    /**
     * @see {@link org.w3c.dom.Node#TEXT_NODE}.
     */
    public static final short TEXT_NODE = org.w3c.dom.Node.TEXT_NODE;
    /**
     * @see {@link org.w3c.dom.Node#CDATA_SECTION_NODE}.
     */
    public static final short CDATA_SECTION_NODE = org.w3c.dom.Node.CDATA_SECTION_NODE;
    /**
     * @see {@link org.w3c.dom.Node#ENTITY_REFERENCE_NODE}.
     */
    public static final short ENTITY_REFERENCE_NODE = org.w3c.dom.Node.ENTITY_REFERENCE_NODE;
    /**
     * @see {@link org.w3c.dom.Node#ENTITY_NODE}.
     */
    public static final short ENTITY_NODE = org.w3c.dom.Node.ENTITY_NODE;
    /**
     * @see {@link org.w3c.dom.Node#PROCESSING_INSTRUCTION_NODE}.
     */
    public static final short PROCESSING_INSTRUCTION_NODE = org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE;
    /**
     * @see {@link org.w3c.dom.Node#COMMENT_NODE}.
     */
    public static final short COMMENT_NODE = org.w3c.dom.Node.COMMENT_NODE;
    /**
     * @see {@link org.w3c.dom.Node#DOCUMENT_NODE}.
     */
    public static final short DOCUMENT_NODE = org.w3c.dom.Node.DOCUMENT_NODE;
    /**
     * @see {@link org.w3c.dom.Node#DOCUMENT_TYPE_NODE}.
     */
    public static final short DOCUMENT_TYPE_NODE = org.w3c.dom.Node.DOCUMENT_TYPE_NODE;
    /**
     * @see {@link org.w3c.dom.Node#DOCUMENT_FRAGMENT_NODE}.
     */
    public static final short DOCUMENT_FRAGMENT_NODE = org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE;
    /**
     * @see {@link org.w3c.dom.Node#NOTATION_NODE}.
     */
    public static final short NOTATION_NODE = org.w3c.dom.Node.NOTATION_NODE;
    
    /**
     * Create an instance.
     */
    public Node() {
    }

    /**
     * Get the JavaScript property "nodeType" for the current node.
     * @return The node type
     */
    public short jsxGet_nodeType() {
        return getDomNodeOrDie().getNodeType();
    }

    /**
     * Get the JavaScript property "nodeName" for the current node.
     * @return The node name
     */
    public String jsxGet_nodeName() {
        final DomNode domNode = getDomNodeOrDie();
        String nodeName = domNode.getNodeName();

        // If this is an HtmlElement then flip the result to uppercase.  This should really be
        // changed in HtmlElement itself but that would break backwards compatibility fairly
        // significantly as that one is documented as always returning a lowercase value.
        if (domNode instanceof HtmlElement && ((HtmlElement) domNode).getNamespaceURI() == null) {
            nodeName = nodeName.toUpperCase();
        }
        return nodeName;
    }

    /**
     * Get the JavaScript property "nodeValue" for the current node.
     * @return The node value
     */
    public String jsxGet_nodeValue() {
        return getDomNodeOrDie().getNodeValue();
    }

    /**
     * Set the JavaScript property "nodeValue" for the current node.
     * @param newValue The new node value
     */
    public void jsxSet_nodeValue(final String newValue) {
        getDomNodeOrDie().setNodeValue(newValue);
    }

    /**
     * Add a DOM node to the node
     * @param childObject The node to add to this node
     * @return The newly added child node.
     */
    public Object jsxFunction_appendChild(final Object childObject) {
        Object appendedChild = null;
        if (childObject instanceof Node) {
            // Get XML node for the DOM node passed in
            final DomNode childDomNode = ((Node) childObject).getDomNodeOrDie();

            // Get the parent XML node that the child should be added to.
            final DomNode parentNode = getDomNodeOrDie();

            // Append the child to the parent node
            parentNode.appendDomChild(childDomNode);
            appendedChild = childObject;
            
            //if the parentNode has null parentNode in IE,
            //create a DocumentFragment to be the parentNode's parentNode.
            if (!(this instanceof DocumentFragment) && parentNode.getParentDomNode() == null 
                    && getBrowserVersion().isIE()) {
                final DomDocumentFragment fragment =
                    ((SgmlPage) parentNode.getPage()).createDomDocumentFragment();
                fragment.appendDomChild(parentNode);
            }
        }
        return appendedChild;
    }

    /**
     * Duplicate an XML node
     * @param deep If true, recursively clone all descendants.  Otherwise,
     * just clone this node.
     * @return The newly cloned node.
     */
    public Object jsxFunction_cloneNode(final boolean deep) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode clonedNode = domNode.cloneDomNode(deep);
        return getJavaScriptNode(clonedNode);
    }

    /**
     * Add a DOM node as a child to this node before the referenced
     * node.  If the referenced node is null, append to the end.
     * @param newChildObject The node to add to this node
     * @param refChildObject The node before which to add the new child
     * @return The newly added child node.
     */
    public Object jsxFunction_insertBefore(
            final Object newChildObject, final Object refChildObject) {
        Object appendedChild = null;

        if (newChildObject instanceof Node) {

            final DomNode newChildNode = ((Node) newChildObject).getDomNodeOrDie();
            if (newChildNode instanceof DomDocumentFragment) {
                final DomDocumentFragment fragment = (DomDocumentFragment) newChildNode;
                for (final DomNode child : fragment.getChildren()) {
                    jsxFunction_insertBefore(child.getScriptObject(), refChildObject);
                }
                return newChildObject;
            }
            else {
                final DomNode refChildNode;
                // IE accepts non standard calls with only one arg
                if (Context.getUndefinedValue().equals(refChildObject)) {
                    if (getBrowserVersion().isIE()) {
                        refChildNode = null;
                    }
                    else {
                        throw Context.reportRuntimeError("insertBefore: not enough arguments");
                    }
                }
                else if (refChildObject != null) {
                    refChildNode = ((Node) refChildObject).getDomNodeOrDie();
                }
                else {
                    refChildNode = null;
                }

                // Append the child to the parent node
                if (refChildNode != null) {
                    refChildNode.insertBefore(newChildNode);
                    appendedChild = newChildObject;
                }
                else {
                    getDomNodeOrDie().appendDomChild(newChildNode);
                }

                //if parentNode is null in IE, create a DocumentFragment to be the parentNode
                if (getDomNodeOrDie().getParentDomNode() == null
                        && getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE()) {
                    final DomDocumentFragment fragment =
                        ((HtmlPage) getDomNodeOrDie().getPage()).createDomDocumentFragment();
                    fragment.appendDomChild(getDomNodeOrDie());
                }
            }
        }
        return appendedChild;
    }

    /**
     * This method provides a way to determine whether two Node references returned by
     * the implementation reference the same object.
     * When two Node references are references to the same object, even if through a proxy,
     * the references may be used completely interchangeably, such that all attributes
     * have the same values and calling the same DOM method on either reference always has exactly the same effect.
     *
     * @param other The node to test against.
     *
     * @return whether this node is the same node as the given one.
     */
    public boolean jsxFunction_isSameNode(final Object other) {
        return other == this;
    }

    /**
     * Remove a DOM node from this node
     * @param childObject The node to remove from this node
     * @return The removed child node.
     */
    public Object jsxFunction_removeChild(final Object childObject) {
        Object removedChild = null;

        if (childObject instanceof Node) {
            // Get XML node for the DOM node passed in
            final DomNode childNode = ((Node) childObject).getDomNodeOrDie();

            // Remove the child from the parent node
            childNode.remove();
            removedChild = childObject;
        }
        return removedChild;
    }

    /**
     * Returns whether this node has any children.
     * @return boolean true if this node has any children, false otherwise.
     */
    public boolean jsxFunction_hasChildNodes() {
        return getDomNodeOrDie().getChildren().iterator().hasNext();
    }

    /**
     * Returns the child nodes of the current element.
     * @return The child nodes of the current element.
     */
    public Object jsxGet_childNodes() {
        if (childNodes_ == null) {
            childNodes_ = new HTMLCollection(this);
            try {
                childNodes_.init(getDomNodeOrDie(), new HtmlUnitXPath("(./* | text() | comment())"));
            }
            catch (final JaxenException je) {
                throw Context.reportRuntimeError("Failed to initialize collection element.childNodes: "
                        + je.getMessage());
            }
        }
        return childNodes_;
    }

    /**
     * Replaces a child DOM node with another DOM node.
     * @param newChildObject the node to add as a child of this node
     * @param oldChildObject the node to remove as a child of this node
     * @return the removed child node
     */
    public Object jsxFunction_replaceChild(final Object newChildObject, final Object oldChildObject) {
        Object removedChild = null;

        if (newChildObject instanceof DocumentFragment) {
            final DocumentFragment fragment = (DocumentFragment) newChildObject;
            Node firstNode = null;
            final Node refChildObject = (Node) ((Node) oldChildObject).jsxGet_nextSibling();
            for (final DomNode node : fragment.getDomNodeOrDie().getChildren()) {
                if (firstNode == null) {
                    jsxFunction_replaceChild(node.getScriptObject(), oldChildObject);
                    firstNode = (Node) node.getScriptObject();
                }
                else {
                    jsxFunction_insertBefore(node.getScriptObject(), refChildObject);
                }
            }
            if (firstNode == null) {
                jsxFunction_removeChild(oldChildObject);
            }
            removedChild = oldChildObject;
        }
        else if (newChildObject instanceof Node && oldChildObject instanceof Node) {
            // Get XML nodes for the DOM nodes passed in
            final DomNode newChildNode = ((Node) newChildObject).getDomNodeOrDie();
            final DomNode oldChildNode;
            if (oldChildObject != null) {
                // Replace the old child with the new child.
                oldChildNode = ((Node) oldChildObject).getDomNodeOrDie();
                oldChildNode.replace(newChildNode);
                removedChild = oldChildObject;
            }
        }

        return removedChild;
    }

    /**
     * Get the JavaScript property "parentNode" for the node that
     * contains the current node.
     * @return The parent node
     */
    public Object jsxGet_parentNode() {
        return getJavaScriptNode(getDomNodeOrDie().getParentDomNode());
    }

    /**
     * Get the JavaScript property "nextSibling" for the node that
     * contains the current node.
     * @return The next sibling node or null if the current node has
     * no next sibling.
     */
    public Object jsxGet_nextSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getNextDomSibling());
    }

    /**
     * Get the JavaScript property "previousSibling" for the node that
     * contains the current node.
     * @return The previous sibling node or null if the current node has
     * no previous sibling.
     */
    public Object jsxGet_previousSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getPreviousDomSibling());
    }

    /**
     * Get the JavaScript property "firstChild" for the node that
     * contains the current node.
     * @return The first child node or null if the current node has
     * no children.
     */
    public Object jsxGet_firstChild() {
        return getJavaScriptNode(getDomNodeOrDie().getFirstDomChild());
    }

    /**
     * Get the JavaScript property "lastChild" for the node that
     * contains the current node.
     * @return The last child node or null if the current node has
     * no children.
     */
    public Object jsxGet_lastChild() {
        return getJavaScriptNode(getDomNodeOrDie().getLastDomChild());
    }

    /**
     * Get the JavaScript node for a given DomNode
     * @param domNode The DomNode
     * @return The JavaScript node or null if the DomNode was null.
     */
    protected Object getJavaScriptNode(final DomNode domNode) {
        if (domNode == null) {
            return null;
        }
        return getScriptableFor(domNode);
    }

    /**
     * Allows the registration of event listeners on the event target.
     * @param type the event type to listen for (like "onclick")
     * @param listener the event listener
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/attachevent.asp">
     * MSDN documentation</a>
     * @return <code>true</code> if the listener has been added
     */
    public boolean jsxFunction_attachEvent(final String type, final Function listener) {
        return getEventListenersContainer().addEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the registration of event listeners on the event target.
     * @param type the event type to listen for (like "click")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.addEventListener">Mozilla documentation</a>
     */
    public void jsxFunction_addEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().addEventListener(type, listener, useCapture);
    }

    /**
     * Gets the container for event listeners
     * @return the container (newly created if needed)
     */
    private EventListenersContainer getEventListenersContainer() {
        if (eventListenersContainer_ == null) {
            eventListenersContainer_ = new EventListenersContainer(this);
        }
        return eventListenersContainer_;
    }

    /**
     * Allows the removal of event listeners on the event target.
     * @param type the event type to listen for (like "onclick")
     * @param listener the event listener
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/detachevent.asp">
     * MSDN documentation</a>
     */
    public void jsxFunction_detachEvent(final String type, final Function listener) {
        jsxFunction_removeEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the removal of event listeners on the event target.
     * @param type the event type to listen for (like "click")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.removeEventListener">Mozilla documentation</a>
     */
    public void jsxFunction_removeEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().removeEventListener(type, listener, useCapture);
    }

    /**
     * Execute the event on this object only (needed for instance for onload on (i)frame tags)
     * @param event the event
     * @return the result
     */
    public ScriptResult executeEvent(final Event event) {
        if (eventListenersContainer_ != null) {
            final HtmlPage page = (HtmlPage) getDomNodeOrDie().getPage();
            final boolean isIE = getBrowserVersion().isIE();
            final Window window = (Window) page.getEnclosingWindow().getScriptObject();
            final Object[] args = new Object[] {event};
            if (isIE) {
                window.setEvent(event);
            }
            
            // handlers declared as property on a node don't receive the event as argument for IE
            final Object[] propHandlerArgs;
            if (isIE) {
                propHandlerArgs = ArrayUtils.EMPTY_OBJECT_ARRAY;
            }
            else {
                propHandlerArgs = args;
            }
            
            try {
                return eventListenersContainer_.executeListeners(event, args, propHandlerArgs);
            }
            finally {
                window.setEvent(null); // reset event
            }
        }
        
        return null;
    }
    
    /**
     * Fires the event on the node with capturing and bubbling phase
     * @param event the event
     * @return the result
     */
    public ScriptResult fireEvent(final Event event) {
        final HtmlPage page = (HtmlPage) getDomNodeOrDie().getPage();
        final boolean isIE = getBrowserVersion().isIE();
        final Window window = (Window) page.getEnclosingWindow().getScriptObject();
        final Object[] args = new Object[] {event};

        event.startFire();
        ScriptResult result = null;
        if (isIE) {
            window.setEvent(event);
        }
        try {
            // window's listeners
            final EventListenersContainer windowsListeners = getWindow().getEventListenersContainer();
    
            // capturing phase
            event.setEventPhase(Event.CAPTURING_PHASE);
            result = windowsListeners.executeCapturingListeners(event, args);
            if (event.isPropagationStopped()) {
                return result;
            }
            final List<DomNode> parents = new ArrayList<DomNode>();
            DomNode node = getDomNodeOrDie();
            while (node != null) {
                parents.add(node);
                node = node.getParentDomNode();
            }
            for (int i = parents.size() - 1; i >= 0; i--) {
                final DomNode curNode = parents.get(i);
                final Node jsNode = (Node) curNode.getScriptObject();
                if (jsNode.eventListenersContainer_ != null) {
                    result = defaultResult(jsNode.eventListenersContainer_.executeCapturingListeners(event, args),
                            result);
                    if (event.isPropagationStopped()) {
                        return result;
                    }
                }
            }
    
            // handlers declared as property on a node don't receive the event as argument for IE
            final Object[] propHandlerArgs;
            if (isIE) {
                propHandlerArgs = ArrayUtils.EMPTY_OBJECT_ARRAY;
            }
            else {
                propHandlerArgs = args;
            }
    
            // bubbling phase
            event.setEventPhase(Event.AT_TARGET);
            node = getDomNodeOrDie();
            while (node != null) {
                final Node jsNode = (Node) node.getScriptObject();
                
                if (jsNode.eventListenersContainer_ != null) {
                    result = defaultResult(
                            jsNode.eventListenersContainer_.executeBubblingListeners(event, args, propHandlerArgs),
                            result);
                    if (event.isPropagationStopped()) {
                        return result;
                    }
                }

                node = node.getParentDomNode();
                event.setEventPhase(Event.BUBBLING_PHASE);
            }

            result = defaultResult(windowsListeners.executeBubblingListeners(event, args, propHandlerArgs), result);
        }
        finally {
            event.endFire();
            window.setEvent(null); // reset event
        }

        return result;
    }

    private ScriptResult defaultResult(final ScriptResult newResult, final ScriptResult defaultResult) {
        if (newResult != null) {
            return newResult;
        }
        return defaultResult;
    }

    /**
     * Gets an event handler
     * @param eventName the event name (ex: "onclick")
     * @return the handler function, <code>null</code> if the property is null or not a function
     */
    public Function getEventHandler(final String eventName) {
        if (eventListenersContainer_ == null) {
            return null;
        }
        return eventListenersContainer_.getEventHandler(StringUtils.substring(eventName, 2));
    }

    /**
     * Defines an event handler
     * @param eventName the event name (like "onclick")
     * @param eventHandler the handler (<code>null</code> to reset it)
     */
    public void setEventHandler(final String eventName, final Function eventHandler) {
        setEventHandlerProp(eventName, eventHandler);
    }

    /**
     * Defines an event handler (or maybe any other object)
     * @param eventName the event name (like "onclick")
     * @param value the property (<code>null</code> to reset it)
     */
    protected void setEventHandlerProp(final String eventName, final Object value) {
        getEventListenersContainer().setEventHandlerProp(StringUtils.substring(eventName.toLowerCase(), 2), value);
    }

    /**
     * Gets the property defined as event handler (not necessary a Function if something else has been set)
     * @param eventName the event name (like "onclick")
     * @return the property
     */
    protected Object getEventHandlerProp(final String eventName) {
        if (eventListenersContainer_ == null) {
            return null;
        }
        else {
            return eventListenersContainer_.getEventHandlerProp(StringUtils.substring(eventName.toLowerCase(), 2));
        }
    }
}
