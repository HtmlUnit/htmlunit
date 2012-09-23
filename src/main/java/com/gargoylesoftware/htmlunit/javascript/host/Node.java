/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Interpreter;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.RhinoException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * The JavaScript object "Node" which is the base class for all DOM
 * objects. This will typically wrap an instance of {@link DomNode}.
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
 * @author Ronald Brill
 */
@JsxClass
public class Node extends SimpleScriptable {

    /** "Live" child nodes collection; has to be a member to have equality (==) working. */
    private NodeList childNodes_;

    private EventListenersContainer eventListenersContainer_;

    /** @see org.w3c.dom.Node#ELEMENT_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short ELEMENT_NODE = org.w3c.dom.Node.ELEMENT_NODE;

    /** @see org.w3c.dom.Node#ATTRIBUTE_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short ATTRIBUTE_NODE = org.w3c.dom.Node.ATTRIBUTE_NODE;

    /** @see org.w3c.dom.Node#TEXT_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short TEXT_NODE = org.w3c.dom.Node.TEXT_NODE;

    /** @see org.w3c.dom.Node#CDATA_SECTION_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short CDATA_SECTION_NODE = org.w3c.dom.Node.CDATA_SECTION_NODE;

    /** @see org.w3c.dom.Node#ENTITY_REFERENCE_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short ENTITY_REFERENCE_NODE = org.w3c.dom.Node.ENTITY_REFERENCE_NODE;

    /** @see org.w3c.dom.Node#ENTITY_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short ENTITY_NODE = org.w3c.dom.Node.ENTITY_NODE;

    /** @see org.w3c.dom.Node#PROCESSING_INSTRUCTION_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short PROCESSING_INSTRUCTION_NODE = org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE;

    /** @see org.w3c.dom.Node#COMMENT_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short COMMENT_NODE = org.w3c.dom.Node.COMMENT_NODE;

    /** @see org.w3c.dom.Node#DOCUMENT_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_NODE = org.w3c.dom.Node.DOCUMENT_NODE;

    /** @see org.w3c.dom.Node#DOCUMENT_TYPE_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_TYPE_NODE = org.w3c.dom.Node.DOCUMENT_TYPE_NODE;

    /** @see org.w3c.dom.Node#DOCUMENT_FRAGMENT_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_FRAGMENT_NODE = org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE;

    /** @see org.w3c.dom.Node#NOTATION_NODE */
    @JsxConstant(@WebBrowser(FF))
    public static final short NOTATION_NODE = org.w3c.dom.Node.NOTATION_NODE;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_DISCONNECTED */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_POSITION_DISCONNECTED = org.w3c.dom.Node.DOCUMENT_POSITION_DISCONNECTED;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_PRECEDING */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_POSITION_PRECEDING = org.w3c.dom.Node.DOCUMENT_POSITION_PRECEDING;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_FOLLOWING */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_POSITION_FOLLOWING = org.w3c.dom.Node.DOCUMENT_POSITION_FOLLOWING;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_CONTAINS */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_POSITION_CONTAINS = org.w3c.dom.Node.DOCUMENT_POSITION_CONTAINS;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_CONTAINED_BY */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_POSITION_CONTAINED_BY = org.w3c.dom.Node.DOCUMENT_POSITION_CONTAINED_BY;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC */
    @JsxConstant(@WebBrowser(FF))
    public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
        = org.w3c.dom.Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;

    /**
     * Creates an instance.
     */
    public Node() {
        // Empty.
    }

    /**
     * Gets the JavaScript property "nodeType" for the current node.
     * @return the node type
     */
    @JsxGetter
    public short getNodeType() {
        return getDomNodeOrDie().getNodeType();
    }

    /**
     * Gets the JavaScript property "nodeName" for the current node.
     * @return the node name
     */
    @JsxGetter
    public String getNodeName() {
        return getDomNodeOrDie().getNodeName();
    }

    /**
     * Gets the JavaScript property "nodeValue" for the current node.
     * @return the node value
     */
    @JsxGetter
    public String getNodeValue() {
        return getDomNodeOrDie().getNodeValue();
    }

    /**
     * Sets the JavaScript property "nodeValue" for the current node.
     * @param newValue the new node value
     */
    @JsxSetter
    public void setNodeValue(final String newValue) {
        getDomNodeOrDie().setNodeValue(newValue);
    }

    /**
     * Adds a DOM node to the node.
     * @param childObject the node to add to this node
     * @return the newly added child node
     */
    @JsxFunction
    public Object appendChild(final Object childObject) {
        Object appendedChild = null;
        if (childObject instanceof Node) {
            final Node childNode = (Node) childObject;

            // is the node allowed here?
            if (!isNodeInsertable(childNode)) {
                // IE silently ignores it
                if (getBrowserVersion().hasFeature(
                        BrowserVersionFeatures.JS_APPEND_CHILD_THROWS_NO_EXCEPTION_FOR_WRONG_NOTE)) {
                    return childObject;
                }

                throw asJavaScriptException(
                    new DOMException("Node cannot be inserted at the specified point in the hierarchy",
                        DOMException.HIERARCHY_REQUEST_ERR));
            }

            // Get XML node for the DOM node passed in
            final DomNode childDomNode = childNode.getDomNodeOrDie();

            // Get the parent XML node that the child should be added to.
            final DomNode parentNode = getDomNodeOrDie();

            // Append the child to the parent node
            parentNode.appendChild(childDomNode);
            appendedChild = childObject;

            // if the parentNode has null parentNode in IE,
            // create a DocumentFragment to be the parentNode's parentNode.
            if (!(parentNode instanceof SgmlPage)
                    && !(this instanceof DocumentFragment) && parentNode.getParentNode() == null
                    && getBrowserVersion().hasFeature(
                            BrowserVersionFeatures.JS_APPEND_CHILD_CREATE_DOCUMENT_FRAGMENT_PARENT_IF_PARENT_IS_NULL)) {
                final DomDocumentFragment fragment = parentNode.getPage().createDomDocumentFragment();
                fragment.appendChild(parentNode);
            }

            initInlineFrameIfNeeded(childDomNode);
            for (final DomNode domNode : childDomNode.getChildren()) {
                initInlineFrameIfNeeded(domNode);
            }
        }
        return appendedChild;
    }

    /**
     * If we have added a new iframe that
     * had no source attribute, we have to take care the
     * 'onload' handler is triggered.
     *
     * @param childDomNode
     */
    private void initInlineFrameIfNeeded(final DomNode childDomNode) {
        if (childDomNode instanceof HtmlInlineFrame) {
            final HtmlInlineFrame frame = (HtmlInlineFrame) childDomNode;
            if (DomElement.ATTRIBUTE_NOT_DEFINED == frame.getSrcAttribute()) {
                frame.loadInnerPage();
            }
        }
    }

    private RhinoException asJavaScriptException(final DOMException exception) {
        exception.setPrototype(getWindow().getPrototype(exception.getClass()));
        exception.setParentScope(getWindow());

        // get current line and file name
        // this method can only be used in interpreted mode. If one day we choose to use compiled mode,
        // then we'll have to find an other way here.
        final String fileName;
        final int lineNumber;
        if (Context.getCurrentContext().getOptimizationLevel() == -1) {
            final int[] linep = new int[1];
            final String sourceName = new Interpreter().getSourcePositionFromStack(Context.getCurrentContext(), linep);
            fileName = sourceName.replaceFirst("script in (.*) from .*", "$1");
            lineNumber = linep[0];
        }
        else {
            throw new Error("HtmlUnit not ready to run in compiled mode");
        }

        exception.setLocation(fileName, lineNumber);

        return new JavaScriptException(exception, fileName, lineNumber);
    }

    /**
     * Indicates if the node can be inserted.
     * @param childObject the node
     * @return <code>false</code> if it is not allowed here
     */
    private boolean isNodeInsertable(final Node childObject) {
        return !(childObject instanceof HTMLHtmlElement);
    }

    /**
     * Clones this node.
     * @param deep if <tt>true</tt>, recursively clones all descendants
     * @return the newly cloned node
     */
    @JsxFunction
    public Object cloneNode(final boolean deep) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode clonedNode = domNode.cloneNode(deep);

        final Node jsClonedNode = getJavaScriptNode(clonedNode);
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CLONE_NODE_COPIES_EVENT_LISTENERS)) {
            // need to copy the event listener when they exist
            copyEventListenersWhenNeeded(domNode, clonedNode);
        }
        return jsClonedNode;
    }

    private void copyEventListenersWhenNeeded(final DomNode domNode, final DomNode clonedNode) {
        final Node jsNode = (Node) domNode.getScriptObject();
        if (jsNode != null) {
            final Node jsClonedNode = getJavaScriptNode(clonedNode);
            jsClonedNode.getEventListenersContainer().copyFrom(jsNode.getEventListenersContainer());
        }

        // look through the children
        DomNode child = domNode.getFirstChild();
        DomNode clonedChild = clonedNode.getFirstChild();
        while (child != null && clonedChild != null) {
            copyEventListenersWhenNeeded(child, clonedChild);
            child = child.getNextSibling();
            clonedChild = clonedChild.getNextSibling();
        }
    }

    /**
     * Add a DOM node as a child to this node before the referenced node.
     * If the referenced node is null, append to the end.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the newly added child node
     */
    @JsxFunction
    public static Object insertBefore(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        return ((Node) thisObj).insertBeforeImpl(args);
    }

    /**
     * Add a DOM node as a child to this node before the referenced node.
     * If the referenced node is null, append to the end.
     * @param args the arguments
     * @return the newly added child node
     */
    protected Object insertBeforeImpl(final Object[] args) {
        final Object newChildObject = args[0];
        final Object refChildObject;
        if (args.length > 1) {
            refChildObject = args[1];
        }
        else {
            refChildObject = Undefined.instance;
        }
        Object appendedChild = null;

        if (newChildObject instanceof Node) {
            final Node newChild = (Node) newChildObject;
            final DomNode newChildNode = newChild.getDomNodeOrDie();

            // is the node allowed here?
            if (!isNodeInsertable(newChild)) {
                // IE silently ignores it
                if (getBrowserVersion().hasFeature(
                        BrowserVersionFeatures.JS_APPEND_CHILD_THROWS_NO_EXCEPTION_FOR_WRONG_NOTE)) {
                    return newChildNode;
                }
                throw Context.reportRuntimeError("Node cannot be inserted at the specified point in the hierarchy");
            }

            if (newChildNode instanceof DomDocumentFragment) {
                final DomDocumentFragment fragment = (DomDocumentFragment) newChildNode;
                for (final DomNode child : fragment.getChildren()) {
                    insertBeforeImpl(new Object[] {child.getScriptObject(), refChildObject});
                }
                return newChildObject;
            }
            final DomNode refChildNode;
            // IE accepts non standard calls with only one arg
            if (refChildObject == Undefined.instance) {
                if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_121)) {
                    if (args.length > 1) {
                        throw Context.reportRuntimeError("Invalid argument.");
                    }
                    refChildNode = null;
                }
                else {
                    if (args.length == 2) {
                        refChildNode = null;
                    }
                    else {
                        throw Context.reportRuntimeError("insertBefore: not enough arguments");
                    }
                }
            }
            else if (refChildObject != null) {
                refChildNode = ((Node) refChildObject).getDomNodeOrDie();
            }
            else {
                refChildNode = null;
            }

            final DomNode domNode = getDomNodeOrDie();
            // Append the child to the parent node
            if (refChildNode != null) {
                refChildNode.insertBefore(newChildNode);
                appendedChild = newChildObject;
            }
            else {
                domNode.appendChild(newChildNode);
                appendedChild = newChildObject;
            }

            // if parentNode is null in IE, create a DocumentFragment to be the parentNode
            if (domNode.getParentNode() == null
                    && getWindow().getWebWindow().getWebClient().getBrowserVersion()
                    .hasFeature(BrowserVersionFeatures.
                            JS_APPEND_CHILD_CREATE_DOCUMENT_FRAGMENT_PARENT_IF_PARENT_IS_NULL)) {
                final DomDocumentFragment fragment = domNode.getPage().createDomDocumentFragment();
                fragment.appendChild(domNode);
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
     * @param other the node to test against
     *
     * @return whether this node is the same node as the given one
     */
    @JsxFunction(@WebBrowser(value = FF, maxVersion = 3.6f))
    public boolean isSameNode(final Object other) {
        return other == this;
    }

    /**
     * Removes a DOM node from this node.
     * @param childObject the node to remove from this node
     * @return the removed child node
     */
    @JsxFunction
    public Object removeChild(final Object childObject) {
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
     * @return boolean true if this node has any children, false otherwise
     */
    @JsxFunction
    public boolean hasChildNodes() {
        return getDomNodeOrDie().getChildren().iterator().hasNext();
    }

    /**
     * Returns the child nodes of the current element.
     * @return the child nodes of the current element
     */
    @JsxGetter
    public NodeList getChildNodes() {
        if (childNodes_ == null) {
            final DomNode node = getDomNodeOrDie();
            final boolean isXmlPage = node.getOwnerDocument() instanceof XmlPage;
            final boolean isIE = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_45);
            final Boolean xmlSpaceDefault = isXMLSpaceDefault(node);
            final boolean skipEmptyTextNode = isIE && isXmlPage && !Boolean.FALSE.equals(xmlSpaceDefault);

            childNodes_ = new NodeList(node, false, "Node.childNodes") {
                @Override
                protected List<Object> computeElements() {
                    final List<Object> response = new ArrayList<Object>();
                    for (final DomNode child : node.getChildren()) {
                        //IE: XmlPage ignores all empty text nodes
                        if (skipEmptyTextNode && child instanceof DomText
                            && StringUtils.isBlank(((DomText) child).getNodeValue())) { //and 'xml:space' is 'default'
                            continue;
                        }
                        response.add(child);
                    }

                    return response;
                }
            };
        }
        return childNodes_;
    }

    /**
     * Recursively checks whether "xml:space" attribute is set to "default".
     * @param node node to start checking from
     * @return {@link Boolean#TRUE} if "default" is set, {@link Boolean#FALSE} for other value,
     *         or null if nothing is set.
     */
    private static Boolean isXMLSpaceDefault(DomNode node) {
        for ( ; node instanceof DomElement; node = node.getParentNode()) {
            final String value = ((DomElement) node).getAttribute("xml:space");
            if (!value.isEmpty()) {
                if ("default".equals(value)) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        }
        return null;
    }

    /**
     * Replaces a child DOM node with another DOM node.
     * @param newChildObject the node to add as a child of this node
     * @param oldChildObject the node to remove as a child of this node
     * @return the removed child node
     */
    @JsxFunction
    public Object replaceChild(final Object newChildObject, final Object oldChildObject) {
        Object removedChild = null;

        if (newChildObject instanceof DocumentFragment) {
            final DocumentFragment fragment = (DocumentFragment) newChildObject;
            Node firstNode = null;
            final Node refChildObject = ((Node) oldChildObject).getNextSibling();
            for (final DomNode node : fragment.getDomNodeOrDie().getChildren()) {
                if (firstNode == null) {
                    replaceChild(node.getScriptObject(), oldChildObject);
                    firstNode = (Node) node.getScriptObject();
                }
                else {
                    insertBeforeImpl(new Object[] {node.getScriptObject(), refChildObject});
                }
            }
            if (firstNode == null) {
                removeChild(oldChildObject);
            }
            removedChild = oldChildObject;
        }
        else if (newChildObject instanceof Node && oldChildObject instanceof Node) {
            final Node newChild = (Node) newChildObject;

            // is the node allowed here?
            if (!isNodeInsertable(newChild)) {
                throw Context.reportRuntimeError("Node cannot be inserted at the specified point in the hierarchy");
            }

            // Get XML nodes for the DOM nodes passed in
            final DomNode newChildNode = newChild.getDomNodeOrDie();
            final DomNode oldChildNode = ((Node) oldChildObject).getDomNodeOrDie();

            // Replace the old child with the new child.
            oldChildNode.replace(newChildNode);
            removedChild = oldChildObject;
        }

        return removedChild;
    }

    /**
     * Returns this node's parent node.
     * @return this node's parent node
     */
    public Node getParent() {
        return getJavaScriptNode(getDomNodeOrDie().getParentNode());
    }

    /**
     * Gets the JavaScript property "parentNode" for the node that
     * contains the current node.
     * @return the parent node
     */
    @JsxGetter
    public Object getParentNode() {
        return getJavaScriptNode(getDomNodeOrDie().getParentNode());
    }

    /**
     * Gets the JavaScript property "nextSibling" for the node that
     * contains the current node.
     * @return the next sibling node or null if the current node has
     * no next sibling.
     */
    @JsxGetter
    public Node getNextSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getNextSibling());
    }

    /**
     * Gets the JavaScript property "previousSibling" for the node that
     * contains the current node.
     * @return the previous sibling node or null if the current node has
     * no previous sibling.
     */
    @JsxGetter
    public Node getPreviousSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getPreviousSibling());
    }

    /**
     * Gets the JavaScript property "firstChild" for the node that
     * contains the current node.
     * @return the first child node or null if the current node has
     * no children.
     */
    @JsxGetter
    public Node getFirstChild() {
        return getJavaScriptNode(getDomNodeOrDie().getFirstChild());
    }

    /**
     * Gets the JavaScript property "lastChild" for the node that
     * contains the current node.
     * @return the last child node or null if the current node has
     * no children.
     */
    @JsxGetter
    public Node getLastChild() {
        return getJavaScriptNode(getDomNodeOrDie().getLastChild());
    }

    /**
     * Gets the JavaScript node for a given DomNode.
     * @param domNode the DomNode
     * @return the JavaScript node or null if the DomNode was null
     */
    protected Node getJavaScriptNode(final DomNode domNode) {
        if (domNode == null) {
            return null;
        }
        return (Node) getScriptableFor(domNode);
    }

    /**
     * Allows the registration of event listeners on the event target.
     * @param type the event type to listen for (like "onclick")
     * @param listener the event listener
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536343.aspx">MSDN documentation</a>
     * @return <code>true</code> if the listener has been added
     */
    @JsxFunction(@WebBrowser(IE))
    public boolean attachEvent(final String type, final Function listener) {
        return getEventListenersContainer().addEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the registration of event listeners on the event target.
     * @param type the event type to listen for (like "click")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.addEventListener">Mozilla documentation</a>
     */
    @JsxFunction(@WebBrowser(FF))
    public void addEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().addEventListener(type, listener, useCapture);
    }

    /**
     * Gets the container for event listeners.
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
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536411.aspx">MSDN documentation</a>
     */
    @JsxFunction(@WebBrowser(IE))
    public void detachEvent(final String type, final Function listener) {
        removeEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the removal of event listeners on the event target.
     * @param type the event type to listen for (like "click")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.removeEventListener">Mozilla documentation</a>
     */
    @JsxFunction(@WebBrowser(FF))
    public void removeEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().removeEventListener(type, listener, useCapture);
    }

    /**
     * Executes the event on this object only (needed for instance for onload on (i)frame tags).
     * @param event the event
     * @return the result
     */
    public ScriptResult executeEvent(final Event event) {
        if (eventListenersContainer_ != null) {
            final HtmlPage page = (HtmlPage) getDomNodeOrDie().getPage();
            final Window window = (Window) page.getEnclosingWindow().getScriptObject();
            return window.executeEvent(event, eventListenersContainer_);
        }

        return null;
    }

    /**
     * Fires the event on the node with capturing and bubbling phase.
     * @param event the event
     * @return the result
     */
    public ScriptResult fireEvent(final Event event) {
        final HtmlPage page = (HtmlPage) getDomNodeOrDie().getPage();
        final Window window = (Window) page.getEnclosingWindow().getScriptObject();
        final Object[] args = new Object[] {event};

        event.startFire();
        ScriptResult result = null;
        final Event previousEvent = window.getCurrentEvent();
        window.setCurrentEvent(event);

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
                node = node.getParentNode();
            }

            final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_124);
            for (int i = parents.size() - 1; i >= 0; i--) {
                final DomNode curNode = parents.get(i);
                final Node jsNode = (Node) curNode.getScriptObject();
                final EventListenersContainer elc = jsNode.eventListenersContainer_;
                if (elc != null) {
                    final ScriptResult r = elc.executeCapturingListeners(event, args);
                    result = ScriptResult.combine(r, result, ie);
                    if (event.isPropagationStopped()) {
                        return result;
                    }
                }
            }

            // handlers declared as property on a node don't receive the event as argument for IE
            final Object[] propHandlerArgs;
            if (ie) {
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
                final EventListenersContainer elc = jsNode.eventListenersContainer_;
                if (elc != null) {
                    final ScriptResult r = elc.executeBubblingListeners(event, args, propHandlerArgs);
                    result = ScriptResult.combine(r, result, ie);
                    if (event.isPropagationStopped()) {
                        return result;
                    }
                }
                node = node.getParentNode();
                event.setEventPhase(Event.BUBBLING_PHASE);
            }

            final ScriptResult r = windowsListeners.executeBubblingListeners(event, args, propHandlerArgs);
            result = ScriptResult.combine(r, result, ie);
        }
        finally {
            event.endFire();
            window.setCurrentEvent(previousEvent); // reset event
        }

        return result;
    }

    /**
     * Returns the specified event handler.
     * @param eventName the event name (e.g. "onclick")
     * @return the handler function, or <tt>null</tt> if the property is null or not a function
     */
    public Function getEventHandler(final String eventName) {
        if (eventListenersContainer_ == null) {
            return null;
        }
        return eventListenersContainer_.getEventHandler(StringUtils.substring(eventName, 2));
    }

    /**
     * Returns <tt>true</tt> if there are any event handlers for the specified event.
     * @param eventName the event name (e.g. "onclick")
     * @return <tt>true</tt> if there are any event handlers for the specified event, <tt>false</tt> otherwise
     */
    public boolean hasEventHandlers(final String eventName) {
        if (eventListenersContainer_ == null) {
            return false;
        }
        return eventListenersContainer_.hasEventHandlers(StringUtils.substring(eventName, 2));
    }

    /**
     * Defines an event handler.
     * @param eventName the event name (e.g. "onclick")
     * @param eventHandler the handler (<code>null</code> to reset it)
     */
    public void setEventHandler(final String eventName, final Function eventHandler) {
        setEventHandlerProp(eventName, eventHandler);
    }

    /**
     * Defines an event handler (or maybe any other object).
     * @param eventName the event name (e.g. "onclick")
     * @param value the property (<code>null</code> to reset it)
     */
    protected void setEventHandlerProp(final String eventName, final Object value) {
        getEventListenersContainer().setEventHandlerProp(StringUtils.substring(eventName.toLowerCase(), 2), value);
    }

    /**
     * Gets the property defined as event handler (not necessary a Function if something else has been set).
     * @param eventName the event name (e.g. "onclick")
     * @return the property
     */
    protected Object getEventHandlerProp(final String eventName) {
        if (eventListenersContainer_ == null) {
            return null;
        }
        return eventListenersContainer_.getEventHandlerProp(StringUtils.substring(eventName.toLowerCase(), 2));
    }

    /**
     * Returns the owner document.
     * @return the document
     */
    @JsxGetter
    public Object getOwnerDocument() {
        final Object document = getDomNodeOrDie().getOwnerDocument();
        if (document == null) {
            return null;
        }
        return ((SgmlPage) document).getScriptObject();
    }

    /**
     * Returns the namespace prefix.
     * @return the namespace prefix
     */
    @JsxGetter(@WebBrowser(FF))
    public String getPrefix() {
        final DomNode domNode = getDomNodeOrDie();
        final String prefix = domNode.getPrefix();
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_125)
                && (prefix == null || domNode.getPage() instanceof HtmlPage)) {
            return "";
        }
        return prefix;
    }

    /**
     * Returns the local name of this element.
     * @return the local name of this element
     */
    @JsxGetter(@WebBrowser(FF))
    public String getLocalName() {
        return getDomNodeOrDie().getLocalName();
    }

    /**
     * Returns The URI that identifies an XML namespace.
     * @return the URI that identifies an XML namespace
     */
    @JsxGetter(@WebBrowser(FF))
    public String getNamespaceURI() {
        final String namespaceURI = getDomNodeOrDie().getNamespaceURI();
        if (namespaceURI == null && getBrowserVersion()
                .hasFeature(BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
            return "";
        }
        return namespaceURI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)
                && !(getDomNodeOrDie().getPage() instanceof HtmlPage)) {
            ActiveXObject.addProperty(this, "namespaceURI", true, false);
            ActiveXObject.addProperty(this, "prefix", true, false);
        }
    }

    /**
     * Compares the positions of this node and the provided node within the document.
     * @param node node object that specifies the node to check
     * @return how the node is positioned relatively to the reference node.
     * @see <a href="http://www.w3.org/TR/DOM-Level-3-Core/core.html#Node3-compareDocumentPosition">DOM level 3</a>
     * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
     */
    @JsxFunction(@WebBrowser(FF))
    public short compareDocumentPosition(final Node node) {
        return getDomNodeOrDie().compareDocumentPosition(node.getDomNodeOrDie());
    }

    /**
     * Merges adjacent TextNode objects to produce a normalized document object model.
     */
    @JsxFunction
    public void normalize() {
        getDomNodeOrDie().normalize();
    }

    /**
     * Represents the xml content of the node and its descendants.
     * @return the xml content of the node and its descendants
     */
    @JsxGetter(@WebBrowser(IE))
    public Object getXml() {
        final DomNode node = getDomNodeOrDie();
        if (node.getPage() instanceof XmlPage) {
            if (this instanceof Element) {
                final XMLSerializer serializer = new XMLSerializer();
                serializer.setParentScope(getParentScope());
                String xml = serializer.serializeToString(this);
                if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SERIALIZER_APPENDS_CRLF)
                        && xml.endsWith("\r\n")) {
                    xml = xml.substring(0, xml.length() - 2);
                }
                return xml;
            }
            return node.asXml();
        }
        return Undefined.instance;
    }

    /**
     * Gets the textContent attribute.
     * @return the contents of this node as text
     */
    @JsxGetter(@WebBrowser(FF))
    public String getTextContent() {
        return getDomNodeOrDie().getTextContent();
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value - the new value for the contents of this node
     */
    @JsxSetter(@WebBrowser(FF))
    public void setTextContent(final Object value) {
        getDomNodeOrDie().setTextContent(value == null ? null : Context.toString(value));
    }

    /**
     * Gets the JavaScript property "parentElement".
     * @return the parent element
     * @see #getParentNode()
     */
    @JsxGetter({ @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME) })
    public Element getParentElement() {
        final Node parent = getParent();
        if (!(parent instanceof Element)) {
            return null;
        }
        return (Element) parent;
    }

    /**
     * Returns the attributes of this XML element.
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.attributes">Gecko DOM Reference</a>
     * @return the attributes of this XML element
     */
    @JsxGetter
    public Object getAttributes() {
        return null;
    }

}
