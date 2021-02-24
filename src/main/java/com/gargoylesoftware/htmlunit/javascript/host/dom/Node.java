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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_NODE_CONTAINS_RETURNS_FALSE_FOR_INVALID_ARG;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_NODE_INSERT_BEFORE_REF_OPTIONAL;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Interpreter;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.RhinoException;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * The JavaScript object {@code Node} which is the base class for all DOM
 * objects. This will typically wrap an instance of {@link DomNode}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Chris Erskine
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass
public class Node extends EventTarget {

    /** @see org.w3c.dom.Node#ELEMENT_NODE */
    @JsxConstant
    public static final short ELEMENT_NODE = org.w3c.dom.Node.ELEMENT_NODE;

    /** @see org.w3c.dom.Node#ATTRIBUTE_NODE */
    @JsxConstant
    public static final short ATTRIBUTE_NODE = org.w3c.dom.Node.ATTRIBUTE_NODE;

    /** @see org.w3c.dom.Node#TEXT_NODE */
    @JsxConstant
    public static final short TEXT_NODE = org.w3c.dom.Node.TEXT_NODE;

    /** @see org.w3c.dom.Node#CDATA_SECTION_NODE */
    @JsxConstant
    public static final short CDATA_SECTION_NODE = org.w3c.dom.Node.CDATA_SECTION_NODE;

    /** @see org.w3c.dom.Node#ENTITY_REFERENCE_NODE */
    @JsxConstant
    public static final short ENTITY_REFERENCE_NODE = org.w3c.dom.Node.ENTITY_REFERENCE_NODE;

    /** @see org.w3c.dom.Node#ENTITY_NODE */
    @JsxConstant
    public static final short ENTITY_NODE = org.w3c.dom.Node.ENTITY_NODE;

    /** @see org.w3c.dom.Node#PROCESSING_INSTRUCTION_NODE */
    @JsxConstant
    public static final short PROCESSING_INSTRUCTION_NODE = org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE;

    /** @see org.w3c.dom.Node#COMMENT_NODE */
    @JsxConstant
    public static final short COMMENT_NODE = org.w3c.dom.Node.COMMENT_NODE;

    /** @see org.w3c.dom.Node#DOCUMENT_NODE */
    @JsxConstant
    public static final short DOCUMENT_NODE = org.w3c.dom.Node.DOCUMENT_NODE;

    /** @see org.w3c.dom.Node#DOCUMENT_TYPE_NODE */
    @JsxConstant
    public static final short DOCUMENT_TYPE_NODE = org.w3c.dom.Node.DOCUMENT_TYPE_NODE;

    /** @see org.w3c.dom.Node#DOCUMENT_FRAGMENT_NODE */
    @JsxConstant
    public static final short DOCUMENT_FRAGMENT_NODE = org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE;

    /** @see org.w3c.dom.Node#NOTATION_NODE */
    @JsxConstant
    public static final short NOTATION_NODE = org.w3c.dom.Node.NOTATION_NODE;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_DISCONNECTED */
    @JsxConstant
    public static final short DOCUMENT_POSITION_DISCONNECTED = org.w3c.dom.Node.DOCUMENT_POSITION_DISCONNECTED;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_PRECEDING */
    @JsxConstant
    public static final short DOCUMENT_POSITION_PRECEDING = org.w3c.dom.Node.DOCUMENT_POSITION_PRECEDING;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_FOLLOWING */
    @JsxConstant
    public static final short DOCUMENT_POSITION_FOLLOWING = org.w3c.dom.Node.DOCUMENT_POSITION_FOLLOWING;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_CONTAINS */
    @JsxConstant
    public static final short DOCUMENT_POSITION_CONTAINS = org.w3c.dom.Node.DOCUMENT_POSITION_CONTAINS;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_CONTAINED_BY */
    @JsxConstant
    public static final short DOCUMENT_POSITION_CONTAINED_BY = org.w3c.dom.Node.DOCUMENT_POSITION_CONTAINED_BY;

    /** @see org.w3c.dom.Node#DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC */
    @JsxConstant
    public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
        = org.w3c.dom.Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;

    /** "Live" child nodes collection; has to be a member to have equality (==) working. */
    private NodeList childNodes_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public Node() {
        // Empty.
    }

    /**
     * Gets the JavaScript property {@code nodeType} for the current node.
     * @return the node type
     */
    @JsxGetter
    public short getNodeType() {
        return getDomNodeOrDie().getNodeType();
    }

    /**
     * Gets the JavaScript property {@code nodeName} for the current node.
     * @return the node name
     */
    @JsxGetter
    public String getNodeName() {
        return getDomNodeOrDie().getNodeName();
    }

    /**
     * Gets the JavaScript property {@code nodeValue} for the current node.
     * @return the node value
     */
    @JsxGetter
    public String getNodeValue() {
        return getDomNodeOrDie().getNodeValue();
    }

    /**
     * Sets the JavaScript property {@code nodeValue} for the current node.
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

            initInlineFrameIfNeeded(childDomNode);
            for (final DomNode domNode : childDomNode.getDescendants()) {
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
    private static void initInlineFrameIfNeeded(final DomNode childDomNode) {
        if (childDomNode instanceof HtmlInlineFrame) {
            final HtmlInlineFrame frame = (HtmlInlineFrame) childDomNode;
            if (DomElement.ATTRIBUTE_NOT_DEFINED == frame.getSrcAttribute()) {
                frame.loadInnerPage();
            }
        }
    }

    /**
     * Encapsulates the given {@link DOMException} into a Rhino-compatible exception.
     *
     * @param exception the exception to encapsulate
     * @return the created exception
     */
    protected RhinoException asJavaScriptException(final DOMException exception) {
        final Window w = getWindow();
        exception.setPrototype(w.getPrototype(exception.getClass()));
        exception.setParentScope(w);

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
        if (args.length < 1) {
            throw ScriptRuntime.typeError(
                    "Failed to execute 'insertBefore' on 'Node': 2 arguments required, but only 0 present.");
        }

        final Object newChildObject = args[0];
        final Object refChildObject;
        if (args.length > 1) {
            refChildObject = args[1];
        }
        else {
            refChildObject = Undefined.instance;
        }
        Object insertedChild = null;

        if (newChildObject instanceof Node) {
            final Node newChild = (Node) newChildObject;
            final DomNode newChildNode = newChild.getDomNodeOrDie();

            // is the node allowed here?
            if (!isNodeInsertable(newChild)) {
                throw ScriptRuntime.constructError("ReferenceError",
                        "Node cannot be inserted at the specified point in the hierarchy");
            }
            if (newChildNode instanceof DomDocumentFragment) {
                final DomDocumentFragment fragment = (DomDocumentFragment) newChildNode;
                for (final DomNode child : fragment.getChildren()) {
                    if (!isNodeInsertable((Node) child.getScriptableObject())) {
                        throw ScriptRuntime.constructError("ReferenceError",
                                "Node cannot be inserted at the specified point in the hierarchy");
                    }
                }
            }

            // extract refChild
            final DomNode refChildNode;
            if (Undefined.isUndefined(refChildObject)) {
                if (args.length == 2 || getBrowserVersion().hasFeature(JS_NODE_INSERT_BEFORE_REF_OPTIONAL)) {
                    refChildNode = null;
                }
                else {
                    throw ScriptRuntime.typeError(
                            "Failed to execute 'insertBefore' on 'Node': 2 arguments required, but only 1 present.");
                }
            }
            else if (refChildObject == null) {
                refChildNode = null;
            }
            else {
                refChildNode = ((Node) refChildObject).getDomNodeOrDie();
            }

            final DomNode domNode = getDomNodeOrDie();

            try {
                domNode.insertBefore(newChildNode, refChildNode);
            }
            catch (final org.w3c.dom.DOMException e) {
                throw ScriptRuntime.constructError("ReferenceError", e.getMessage());
            }
            insertedChild = newChild;
        }
        return insertedChild;
    }

    /**
     * Indicates if the node can be inserted.
     * @param childObject the node
     * @return {@code false} if it is not allowed here
     */
    private static boolean isNodeInsertable(final Node childObject) {
        if (childObject instanceof HTMLHtmlElement) {
            final DomNode domNode = childObject.getDomNodeOrDie();
            return domNode.getPage().getDocumentElement() != domNode;
        }
        return true;
    }

    /**
     * Removes the DOM node from its parent.
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/ChildNode/remove">MDN documentation</a>
     */
    protected void remove() {
        getDomNodeOrDie().remove();
    }

    /**
     * Removes a DOM node from this node.
     * @param childObject the node to remove from this node
     * @return the removed child node
     */
    @JsxFunction
    public Object removeChild(final Object childObject) {
        if (!(childObject instanceof Node)) {
            return null;
        }

        // Get XML node for the DOM node passed in
        final DomNode childNode = ((Node) childObject).getDomNodeOrDie();

        if (!getDomNodeOrDie().isAncestorOf(childNode)) {
            Context.throwAsScriptRuntimeEx(new Exception("NotFoundError: Failed to execute 'removeChild' on '"
                        + this + "': The node to be removed is not a child of this node."));
        }
        // Remove the child from the parent node
        childNode.remove();
        return childObject;
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
                    replaceChild(node.getScriptableObject(), oldChildObject);
                    firstNode = node.getScriptableObject();
                }
                else {
                    insertBeforeImpl(new Object[] {node.getScriptableObject(), refChildObject});
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
     * Clones this node.
     * @param deep if {@code true}, recursively clones all descendants
     * @return the newly cloned node
     */
    @JsxFunction
    public Object cloneNode(final boolean deep) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode clonedNode = domNode.cloneNode(deep);

        final Node jsClonedNode = getJavaScriptNode(clonedNode);
        return jsClonedNode;
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
    @JsxFunction
    public boolean isSameNode(final Object other) {
        return other == this;
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
            childNodes_ = new NodeList(node, false) {
                @Override
                protected List<DomNode> computeElements() {
                    final List<DomNode> response = new ArrayList<>();
                    for (final DomNode child : node.getChildren()) {
                        response.add(child);
                    }

                    return response;
                }
            };
        }
        return childNodes_;
    }

    /**
     * Returns this node's parent node.
     * @return this node's parent node
     */
    public final Node getParent() {
        return getJavaScriptNode(getDomNodeOrDie().getParentNode());
    }

    /**
     * Gets the JavaScript property {@code parentNode} for the node that
     * contains the current node.
     * @return the parent node
     */
    @JsxGetter
    public Object getParentNode() {
        return getJavaScriptNode(getDomNodeOrDie().getParentNode());
    }

    /**
     * Gets the JavaScript property {@code nextSibling} for the node that
     * contains the current node.
     * @return the next sibling node or null if the current node has
     * no next sibling.
     */
    @JsxGetter
    public Node getNextSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getNextSibling());
    }

    /**
     * Gets the JavaScript property {@code previousSibling} for the node that
     * contains the current node.
     * @return the previous sibling node or null if the current node has
     * no previous sibling.
     */
    @JsxGetter
    public Node getPreviousSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getPreviousSibling());
    }

    /**
     * Gets the JavaScript property {@code firstChild} for the node that
     * contains the current node.
     * @return the first child node or null if the current node has
     * no children.
     */
    @JsxGetter
    public Node getFirstChild() {
        return getJavaScriptNode(getDomNodeOrDie().getFirstChild());
    }

    /**
     * Gets the JavaScript property {@code lastChild} for the node that
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
     * Returns the owner document.
     * @return the document
     */
    @JsxGetter
    public Object getOwnerDocument() {
        final Object document = getDomNodeOrDie().getOwnerDocument();
        if (document != null) {
            return ((SgmlPage) document).getScriptableObject();
        }
        return null;
    }

    /**
     * Compares the positions of this node and the provided node within the document.
     * @param node node object that specifies the node to check
     * @return how the node is positioned relatively to the reference node.
     * @see <a href="http://www.w3.org/TR/DOM-Level-3-Core/core.html#Node3-compareDocumentPosition">DOM level 3</a>
     * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
     */
    @JsxFunction
    public short compareDocumentPosition(final Object node) {
        if (!(node instanceof Node)) {
            throw Context.reportRuntimeError("Could not convert JavaScript argument arg 0");
        }
        return getDomNodeOrDie().compareDocumentPosition(((Node) node).getDomNodeOrDie());
    }

    /**
     * Merges adjacent TextNode objects to produce a normalized document object model.
     */
    @JsxFunction
    public void normalize() {
        getDomNodeOrDie().normalize();
    }

    /**
     * Gets the textContent attribute.
     * @return the contents of this node as text
     */
    @JsxGetter
    public String getTextContent() {
        return getDomNodeOrDie().getTextContent();
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value - the new value for the contents of this node
     */
    @JsxSetter
    public void setTextContent(final Object value) {
        getDomNodeOrDie().setTextContent(value == null ? null : Context.toString(value));
    }

    /**
     * Gets the JavaScript property {@code parentElement}.
     * @return the parent element
     * @see #getParentNode()
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Element getParentElement() {
        final Node parent = getParent();
        if (!(parent instanceof Element)) {
            return null;
        }
        return (Element) parent;
    }

    /**
     * Returns the attributes of this XML element.
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/Node.attributes">Gecko DOM Reference</a>
     * @return the attributes of this XML element
     */
    @JsxGetter(IE)
    public Object getAttributes() {
        return null;
    }

    /**
     * Checks whether the given element is contained within this object.
     * @param element element object that specifies the element to check
     * @return true if the element is contained within this object
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public boolean contains(final Object element) {
        if (!(element instanceof Node)) {
            if (getBrowserVersion().hasFeature(JS_NODE_CONTAINS_RETURNS_FALSE_FOR_INVALID_ARG)) {
                return false;
            }
            throw Context.reportRuntimeError("Could not convert JavaScript argument arg 0");
        }

        if (getBrowserVersion().hasFeature(JS_NODE_CONTAINS_RETURNS_FALSE_FOR_INVALID_ARG)) {
            if (element instanceof CharacterData) {
                return false;
            }
            if (this instanceof CharacterData) {
                throw Context.reportRuntimeError("Function 'contains' not available for text nodes.");
            }
        }

        for (Node parent = (Node) element; parent != null; parent = parent.getParentElement()) {
            if (this == parent) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the Base URI as a string.
     * @return the Base URI as a string
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public String getBaseURI() {
        return getDomNodeOrDie().getBaseURI();
    }

    /**
     * Returns true when the current element has any attributes or not.
     * @return true if an attribute is specified on this element
     */
    @JsxFunction(IE)
    public boolean hasAttributes() {
        return getDomNodeOrDie().hasAttributes();
    }

    /**
     * Returns the namespace prefix.
     * @return the namespace prefix
     */
    @JsxGetter(IE)
    public Object getPrefix() {
        return getDomNodeOrDie().getPrefix();
    }

    /**
     * Returns the local name of this attribute.
     * @return the local name of this attribute
     */
    @JsxGetter(IE)
    public Object getLocalName() {
        return getDomNodeOrDie().getLocalName();
    }

    /**
     * Returns the URI that identifies an XML namespace.
     * @return the URI that identifies an XML namespace
     */
    @JsxGetter(IE)
    public Object getNamespaceURI() {
        return getDomNodeOrDie().getNamespaceURI();
    }

    /**
     * Returns the current number of child elements.
     * @return the child element count
     */
    protected int getChildElementCount() {
        return ((DomElement) getDomNodeOrDie()).getChildElementCount();
    }

    /**
     * Returns the first element child.
     * @return the first element child
     */
    protected Element getFirstElementChild() {
        final DomNode domNode = getDomNodeOrDie();
        if (domNode instanceof DomElement) {
            final DomElement child = ((DomElement) domNode).getFirstElementChild();
            if (child != null) {
                return (Element) child.getScriptableObject();
            }
            return null;
        }

        for (final DomNode child : domNode.getChildren()) {
            if (child != null) {
                final Scriptable scriptable = child.getScriptableObject();
                if (scriptable instanceof Element) {
                    return (Element) scriptable;
                }
            }
        }
        return null;
    }

    /**
     * Returns the last element child.
     * @return the last element child
     */
    protected Element getLastElementChild() {
        final DomNode domNode = getDomNodeOrDie();
        if (domNode instanceof DomElement) {
            final DomElement child = ((DomElement) getDomNodeOrDie()).getLastElementChild();
            if (child != null) {
                return (Element) child.getScriptableObject();
            }
            return null;
        }

        Element result = null;
        for (final DomNode child : domNode.getChildren()) {
            final Scriptable scriptable = child.getScriptableObject();
            if (scriptable instanceof Element) {
                result = (Element) scriptable;
            }
        }
        return result;
    }

    /**
     * Gets the children of the current node.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537446.aspx">MSDN documentation</a>
     * @return the child at the given position
     */
    protected HTMLCollection getChildren() {
        final DomNode node = getDomNodeOrDie();
        final HTMLCollection collection = new HTMLCollection(node, false) {
            @Override
            protected List<DomNode> computeElements() {
                final List<DomNode> children = new LinkedList<>();
                for (final DomNode domNode : node.getChildNodes()) {
                    if (domNode instanceof DomElement) {
                        children.add(domNode);
                    }
                }
                return children;
            }
        };
        return collection;
    }

    /**
     * Inserts a set of Node or DOMString objects in the children list of this ChildNode's parent,
     * just after this ChildNode.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    protected static void after(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        final DomNode thisDomNode = ((Node) thisObj).getDomNodeOrDie();
        final DomNode parentNode = thisDomNode.getParentNode();
        final DomNode nextSibling = thisDomNode.getNextSibling();
        for (final Object arg : args) {
            final Node node = toNodeOrTextNode((Node) thisObj, arg);
            final DomNode newNode = node.getDomNodeOrDie();
            if (nextSibling == null) {
                parentNode.appendChild(newNode);
            }
            else {
                nextSibling.insertBefore(newNode);
            }
        }
    }

    private static Node toNodeOrTextNode(final Node thisObj, final Object obj) {
        if (obj instanceof Node) {
            return (Node) obj;
        }
        return (Node)
                ((HTMLDocument) thisObj.getOwnerDocument()).createTextNode(Context.toString(obj));
    }

    /**
     * Inserts a set of Node or DOMString objects in the children list of this ChildNode's parent,
     * just before this ChildNode.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    protected static void before(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        for (final Object arg : args) {
            final Node node = toNodeOrTextNode((Node) thisObj, arg);
            ((Node) thisObj).getDomNodeOrDie().insertBefore(node.getDomNodeOrDie());
        }
    }

    /**
     * Replaces this ChildNode in the children list of its parent with a set of Node or DOMString objects.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    protected static void replaceWith(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        final DomNode thisDomNode = ((Node) thisObj).getDomNodeOrDie();
        final DomNode parentNode = thisDomNode.getParentNode();
        final DomNode nextSibling = thisDomNode.getNextSibling();
        boolean isFirst = true;
        for (final Object arg : args) {
            final DomNode newNode = toNodeOrTextNode((Node) thisObj, arg).getDomNodeOrDie();
            if (isFirst) {
                isFirst = false;
                thisDomNode.replace(newNode);
            }
            else {
                if (nextSibling == null) {
                    parentNode.appendChild(newNode);
                }
                else {
                    nextSibling.insertBefore(newNode);
                }
            }
        }
    }
}
