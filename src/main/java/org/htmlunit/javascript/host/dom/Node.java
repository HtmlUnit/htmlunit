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
package org.htmlunit.javascript.host.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.htmlunit.SgmlPage;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.DomDocumentFragment;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlInlineFrame;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.javascript.host.NamedNodeMap;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.html.HTMLCollection;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.javascript.host.html.HTMLHtmlElement;

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

    /**
     * @see org.w3c.dom.Node#ELEMENT_NODE
     */
    @JsxConstant
    public static final int ELEMENT_NODE = org.w3c.dom.Node.ELEMENT_NODE;

    /**
     * @see org.w3c.dom.Node#ATTRIBUTE_NODE
     */
    @JsxConstant
    public static final int ATTRIBUTE_NODE = org.w3c.dom.Node.ATTRIBUTE_NODE;

    /**
     * @see org.w3c.dom.Node#TEXT_NODE
     */
    @JsxConstant
    public static final int TEXT_NODE = org.w3c.dom.Node.TEXT_NODE;

    /**
     * @see org.w3c.dom.Node#CDATA_SECTION_NODE
     */
    @JsxConstant
    public static final int CDATA_SECTION_NODE = org.w3c.dom.Node.CDATA_SECTION_NODE;

    /**
     * @see org.w3c.dom.Node#ENTITY_REFERENCE_NODE
     */
    @JsxConstant
    public static final int ENTITY_REFERENCE_NODE = org.w3c.dom.Node.ENTITY_REFERENCE_NODE;

    /**
     * @see org.w3c.dom.Node#ENTITY_NODE
     */
    @JsxConstant
    public static final int ENTITY_NODE = org.w3c.dom.Node.ENTITY_NODE;

    /**
     * @see org.w3c.dom.Node#PROCESSING_INSTRUCTION_NODE
     */
    @JsxConstant
    public static final int PROCESSING_INSTRUCTION_NODE = org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE;

    /**
     * @see org.w3c.dom.Node#COMMENT_NODE
     */
    @JsxConstant
    public static final int COMMENT_NODE = org.w3c.dom.Node.COMMENT_NODE;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_NODE
     */
    @JsxConstant
    public static final int DOCUMENT_NODE = org.w3c.dom.Node.DOCUMENT_NODE;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_TYPE_NODE
     */
    @JsxConstant
    public static final int DOCUMENT_TYPE_NODE = org.w3c.dom.Node.DOCUMENT_TYPE_NODE;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_FRAGMENT_NODE
     */
    @JsxConstant
    public static final int DOCUMENT_FRAGMENT_NODE = org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE;

    /**
     * @see org.w3c.dom.Node#NOTATION_NODE
     */
    @JsxConstant
    public static final int NOTATION_NODE = org.w3c.dom.Node.NOTATION_NODE;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_POSITION_DISCONNECTED
     */
    @JsxConstant
    public static final int DOCUMENT_POSITION_DISCONNECTED = org.w3c.dom.Node.DOCUMENT_POSITION_DISCONNECTED;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_POSITION_PRECEDING
     */
    @JsxConstant
    public static final int DOCUMENT_POSITION_PRECEDING = org.w3c.dom.Node.DOCUMENT_POSITION_PRECEDING;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_POSITION_FOLLOWING
     */
    @JsxConstant
    public static final int DOCUMENT_POSITION_FOLLOWING = org.w3c.dom.Node.DOCUMENT_POSITION_FOLLOWING;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_POSITION_CONTAINS
     */
    @JsxConstant
    public static final int DOCUMENT_POSITION_CONTAINS = org.w3c.dom.Node.DOCUMENT_POSITION_CONTAINS;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_POSITION_CONTAINED_BY
     */
    @JsxConstant
    public static final int DOCUMENT_POSITION_CONTAINED_BY = org.w3c.dom.Node.DOCUMENT_POSITION_CONTAINED_BY;

    /**
     * @see org.w3c.dom.Node#DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
     */
    @JsxConstant
    public static final int DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
        = org.w3c.dom.Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;

    /** "Live" child nodes collection; has to be a member to have equality (==) working. */
    private NodeList childNodes_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Gets the JavaScript property {@code nodeType} for the current node.
     * @return the node type
     */
    @JsxGetter
    public int getNodeType() {
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
    public Node appendChild(final Object childObject) {
        if (childObject instanceof Node) {
            final Node childNode = (Node) childObject;

            // is the node allowed here?
            if (!isNodeInsertable(childNode)) {
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "Node cannot be inserted at the specified point in the hierarchy",
                        DOMException.HIERARCHY_REQUEST_ERR);
            }

            // Get XML node for the DOM node passed in
            final DomNode childDomNode = childNode.getDomNodeOrDie();

            // Get the parent XML node that the child should be added to.
            final DomNode parentNode = getDomNodeOrDie();

            // Append the child to the parent node
            try {
                parentNode.appendChild(childDomNode);
            }
            catch (final org.w3c.dom.DOMException e) {
                throw JavaScriptEngine.asJavaScriptException(getWindow(), e.getMessage(), e.code);
            }

            initInlineFrameIfNeeded(childDomNode);
            for (final HtmlElement htmlElement : childDomNode.getHtmlElementDescendants()) {
                initInlineFrameIfNeeded(htmlElement);
            }
            return childNode;
        }
        return null;
    }

    /**
     * If we have added a new iframe that
     * had no source attribute, we have to take care the
     * 'onload' handler is triggered.
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
     * Add a DOM node as a child to this node before the referenced node.
     * If the referenced node is null, append to the end.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the newly added child node
     */
    @JsxFunction
    public static Node insertBefore(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        return ((Node) thisObj).insertBeforeImpl(args);
    }

    /**
     * Add a DOM node as a child to this node before the referenced node.
     * If the referenced node is null, append to the end.
     * @param args the arguments
     * @return the newly added child node
     */
    protected Node insertBeforeImpl(final Object[] args) {
        if (args.length < 1) {
            throw JavaScriptEngine.typeError(
                    "Failed to execute 'insertBefore' on 'Node': 2 arguments required, but only 0 present.");
        }

        final Object newChildObject = args[0];
        final Object refChildObject;
        if (args.length > 1) {
            refChildObject = args[1];
        }
        else {
            refChildObject = JavaScriptEngine.UNDEFINED;
        }

        if (newChildObject instanceof Node) {
            final Node newChild = (Node) newChildObject;

            // is the node allowed here?
            if (!isNodeInsertable(newChild)) {
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "Node cannot be inserted at the specified point in the hierarchy",
                        DOMException.HIERARCHY_REQUEST_ERR);
            }

            final DomNode newChildNode = newChild.getDomNodeOrDie();
            if (newChildNode instanceof DomDocumentFragment) {
                final DomDocumentFragment fragment = (DomDocumentFragment) newChildNode;
                for (final DomNode child : fragment.getChildren()) {
                    if (!isNodeInsertable(child.getScriptableObject())) {
                        throw JavaScriptEngine.asJavaScriptException(
                                getWindow(),
                                "Node cannot be inserted at the specified point in the hierarchy",
                                DOMException.HIERARCHY_REQUEST_ERR);
                    }
                }
            }

            // extract refChild
            final DomNode refChildNode;
            if (JavaScriptEngine.isUndefined(refChildObject)) {
                if (args.length == 2) {
                    refChildNode = null;
                }
                else {
                    throw JavaScriptEngine.typeError(
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
                throw JavaScriptEngine.asJavaScriptException(getWindow(), e.getMessage(), DOMException.NOT_FOUND_ERR);
            }
            return newChild;
        }
        return null;
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
    public Node removeChild(final Object childObject) {
        if (!(childObject instanceof Node)) {
            return null;
        }

        // Get XML node for the DOM node passed in
        final Node childObjectNode = (Node) childObject;
        final DomNode childDomNode = childObjectNode.getDomNodeOrDie();

        if (!getDomNodeOrDie().isAncestorOf(childDomNode)) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "Failed to execute 'removeChild' on '"
                            + this + "': The node to be removed is not a child of this node.",
                    DOMException.NOT_FOUND_ERR);
        }
        // Remove the child from the parent node
        childDomNode.remove();
        return childObjectNode;
    }

    /**
     * Replaces a child DOM node with another DOM node.
     * @param newChildObject the node to add as a child of this node
     * @param oldChildObject the node to remove as a child of this node
     * @return the removed child node
     */
    @JsxFunction
    public Node replaceChild(final Object newChildObject, final Object oldChildObject) {
        if (newChildObject instanceof DocumentFragment) {
            final DocumentFragment fragment = (DocumentFragment) newChildObject;
            Node firstNode = null;

            final Node oldChildNode = (Node) oldChildObject;
            final Node refChildObject = oldChildNode.getNextSibling();
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

            return oldChildNode;
        }

        if (newChildObject instanceof Node && oldChildObject instanceof Node) {
            final Node newChild = (Node) newChildObject;

            // is the node allowed here?
            if (!isNodeInsertable(newChild)) {
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "Node cannot be inserted at the specified point in the hierarchy",
                        DOMException.HIERARCHY_REQUEST_ERR);
            }

            // Get XML nodes for the DOM nodes passed in
            final DomNode newChildDomNode = newChild.getDomNodeOrDie();
            final Node oldChildNode = (Node) oldChildObject;
            final DomNode oldChildDomNode = oldChildNode.getDomNodeOrDie();

            // Replace the old child with the new child.
            oldChildDomNode.replace(newChildDomNode);

            return oldChildNode;
        }

        return null;
    }

    /**
     * Clones this node.
     * @param deep if {@code true}, recursively clones all descendants
     * @return the newly cloned node
     */
    @JsxFunction
    public Node cloneNode(final boolean deep) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode clonedNode = domNode.cloneNode(deep);

        return getJavaScriptNode(clonedNode);
    }

    /**
     * Check if 2 nodes are equals.
     * For detail specifications
     * @see <a href="https://dom.spec.whatwg.org/#concept-node-equals">concept-node-equals</a>
     * @param other the node to compare with
     * @return true or false
     */
    @JsxFunction
    public boolean isEqualNode(final Node other) {
        if (isSameNode(other)) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!getClassName().equals(other.getClassName())) {
            return false;
        }

        if (this instanceof DocumentType) {
            final DocumentType docType = (DocumentType) this;
            final DocumentType otherDocType = (DocumentType) other;
            if (!Objects.equals(docType.getName(), otherDocType.getName())
                    || !Objects.equals(docType.getPublicId(), otherDocType.getPublicId())
                    || !Objects.equals(docType.getSystemId(), otherDocType.getSystemId())) {
                return false;
            }

        }
        else if (this instanceof Element) {
            final Element element = (Element) this;
            final Element otherElement = (Element) other;
            if (!Objects.equals(element.getNodeName(), otherElement.getNodeName())
                    || !Objects.equals(element.getPrefix(), otherElement.getPrefix())
                    || !Objects.equals(element.getLocalName(), otherElement.getLocalName())) {
                return false;
            }

            final NamedNodeMap attributesMap = element.getAttributes();
            final NamedNodeMap otherAttributesMap = otherElement.getAttributes();
            if (attributesMap != null || otherAttributesMap != null) {
                if (attributesMap == null || otherAttributesMap == null) {
                    return false;
                }

                final int length = attributesMap.getLength();
                if (length != otherAttributesMap.getLength()) {
                    return false;
                }

                final Map<String, Attr> name2Attributes = new HashMap<>();
                for (int i = 0; i < length; i++) {
                    final Attr attribute = (Attr) attributesMap.item(i);
                    name2Attributes.put(attribute.getName(), attribute);
                }

                for (int i = 0; i < length; i++) {
                    final Attr otherAttribute = (Attr) otherAttributesMap.item(i);
                    final Attr attribute = name2Attributes.get(otherAttribute.getName());
                    if (attribute == null) {
                        return false;
                    }
                    if (!attribute.isEqualNode(otherAttribute)) {
                        return false;
                    }
                }
            }

        }
        else if (this instanceof Attr) {
            final Attr attr = (Attr) this;
            final Attr otherAttr = (Attr) other;
            if (!Objects.equals(attr.getName(), otherAttr.getName())
                    || !Objects.equals(attr.getLocalName(), otherAttr.getLocalName())
                    || !Objects.equals(attr.getValue(), otherAttr.getValue())) {
                return false;
            }

        }
        else if (this instanceof ProcessingInstruction) {
            final ProcessingInstruction instruction = (ProcessingInstruction) this;
            final ProcessingInstruction otherInstruction = (ProcessingInstruction) other;
            if (!Objects.equals(instruction.getTarget(), otherInstruction.getTarget())
                    || !Objects.equals(instruction.getData(), otherInstruction.getData())) {
                return false;
            }

        }
        else if (this instanceof Text || this instanceof Comment) {
            final CharacterData data = (CharacterData) this;
            final CharacterData otherData = (CharacterData) other;
            if (!Objects.equals(data.getData(), otherData.getData())) {
                return false;
            }
        }

        final NodeList childNodes = getChildNodes();
        final NodeList otherChildNodes = other.getChildNodes();
        if (childNodes != null || otherChildNodes != null) {
            if (childNodes == null || otherChildNodes == null) {
                return false;
            }

            final int length = childNodes.getLength();
            final int otherLength = otherChildNodes.getLength();
            if (length != otherLength) {
                return false;
            }

            for (int i = 0; i < length; i++) {
                final Node childNode = (Node) childNodes.item(i);
                final Node otherChildNode = (Node) otherChildNodes.item(i);
                if (!childNode.isEqualNode(otherChildNode)) {
                    return false;
                }
            }
        }

        return true;
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
        return this == other;
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
     * @param namespace string containing the namespace to look the prefix up
     * @return a string containing the prefix for a given namespace URI,
     *         if present, and null if not. When multiple prefixes are possible,
     *         the first prefix is returned.
     */
    @JsxFunction
    public String lookupPrefix(final String namespace) {
        return null;
    }

    /**
     * Returns the child nodes of the current element.
     * @return the child nodes of the current element
     */
    @JsxGetter
    public NodeList getChildNodes() {
        if (childNodes_ == null) {
            final DomNode node = getDomNodeOrDie();
            childNodes_ = new NodeList(node, false);
            childNodes_.setElementsSupplier(
                    (Supplier<List<DomNode>> & Serializable)
                    () -> {
                        final List<DomNode> response = new ArrayList<>();
                        for (final DomNode child : node.getChildren()) {
                            response.add(child);
                        }

                        return response;
                    });
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
     *         no next sibling.
     */
    @JsxGetter
    public Node getNextSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getNextSibling());
    }

    /**
     * Gets the JavaScript property {@code previousSibling} for the node that
     * contains the current node.
     * @return the previous sibling node or null if the current node has
     *         no previous sibling.
     */
    @JsxGetter
    public Node getPreviousSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getPreviousSibling());
    }

    /**
     * Gets the JavaScript property {@code firstChild} for the node that
     * contains the current node.
     * @return the first child node or null if the current node has
     *         no children.
     */
    @JsxGetter
    public Node getFirstChild() {
        return getJavaScriptNode(getDomNodeOrDie().getFirstChild());
    }

    /**
     * Gets the JavaScript property {@code lastChild} for the node that
     * contains the current node.
     * @return the last child node or null if the current node has
     *         no children.
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
    public HtmlUnitScriptable getOwnerDocument() {
        final Object document = getDomNodeOrDie().getOwnerDocument();
        if (document != null) {
            return ((SgmlPage) document).getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the owner document.
     * @return the document
     */
    @JsxFunction
    public Node getRootNode() {
        Node parent = this;
        while (parent != null) {
            if (parent instanceof Document || parent instanceof DocumentFragment) {
                return parent;
            }
            parent = parent.getParent();
        }
        return this;
    }

    /**
     * Compares the positions of this node and the provided node within the document.
     * @param node node object that specifies the node to check
     * @return how the node is positioned relatively to the reference node.
     * @see <a href="http://www.w3.org/TR/DOM-Level-3-Core/core.html#Node3-compareDocumentPosition">DOM level 3</a>
     * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
     */
    @JsxFunction
    public int compareDocumentPosition(final Object node) {
        if (!(node instanceof Node)) {
            throw JavaScriptEngine.typeError("Could not convert JavaScript argument arg 0");
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
        getDomNodeOrDie().setTextContent(value == null ? null : JavaScriptEngine.toString(value));
    }

    /**
     * Gets the JavaScript property {@code parentElement}.
     * @return the parent element
     * @see #getParentNode()
     */
    @JsxGetter
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
    public NamedNodeMap getAttributes() {
        return null;
    }

    /**
     * Checks whether the given element is contained within this object.
     * @param element element object that specifies the element to check
     * @return true if the element is contained within this object
     */
    @JsxFunction
    public boolean contains(final Object element) {
        if (element == null || JavaScriptEngine.isUndefined(element)) {
            return false;
        }

        if (!(element instanceof Node)) {
            throw JavaScriptEngine.reportRuntimeError("Could not convert JavaScript argument arg 0");
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
    @JsxGetter
    public String getBaseURI() {
        return getDomNodeOrDie().getBaseURI();
    }

    /**
     * Returns true when the current element has any attributes or not.
     * @return true if an attribute is specified on this element
     */
    public boolean hasAttributes() {
        return getDomNodeOrDie().hasAttributes();
    }

    /**
     * Returns the namespace prefix.
     * @return the namespace prefix
     */
    public String getPrefix() {
        return getDomNodeOrDie().getPrefix();
    }

    /**
     * Returns the local name of this attribute.
     * @return the local name of this attribute
     */
    public String getLocalName() {
        return getDomNodeOrDie().getLocalName();
    }

    /**
     * Returns the URI that identifies an XML namespace.
     * @return the URI that identifies an XML namespace
     */
    public String getNamespaceURI() {
        return getDomNodeOrDie().getNamespaceURI();
    }

    /**
     * Returns the current number of child elements.
     * @return the child element count
     */
    protected int getChildElementCount() {
        final DomNode domNode = getDomNodeOrDie();
        if (domNode instanceof DomElement) {
            return ((DomElement) domNode).getChildElementCount();
        }

        int counter = 0;
        for (final DomNode child : getDomNodeOrDie().getChildren()) {
            if (child != null) {
                final HtmlUnitScriptable scriptable = child.getScriptableObject();
                if (scriptable instanceof Element) {
                    counter++;
                }
            }
        }
        return counter;
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
                return child.getScriptableObject();
            }
            return null;
        }

        for (final DomNode child : domNode.getChildren()) {
            if (child != null) {
                final HtmlUnitScriptable scriptable = child.getScriptableObject();
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
                return child.getScriptableObject();
            }
            return null;
        }

        Element result = null;
        for (final DomNode child : domNode.getChildren()) {
            final HtmlUnitScriptable scriptable = child.getScriptableObject();
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
        final HTMLCollection childrenColl = new HTMLCollection(node, false);
        childrenColl.setElementsSupplier(
                (Supplier<List<DomNode>> & Serializable)
                () -> {
                    final List<DomNode> children = new ArrayList<>();
                    for (final DomNode domNode : node.getChildNodes()) {
                        if (domNode instanceof DomElement) {
                            children.add(domNode);
                        }
                    }
                    return children;
                });
        return childrenColl;
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

    /**
     * Inserts a set of Node objects or string objects after the last child of the Element.
     * String objects are inserted as equivalent Text nodes.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    protected static void append(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        if (!(thisObj instanceof Node)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        final Node thisNode = (Node) thisObj;
        final DomNode thisDomNode = thisNode.getDomNodeOrDie();

        for (final Object arg : args) {
            final Node node = toNodeOrTextNode(thisNode, arg);
            thisDomNode.appendChild(node.getDomNodeOrDie());
        }
    }

    /**
     * Inserts a set of Node objects or string objects before the first child of the Element.
     * String objects are inserted as equivalent Text nodes.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    protected static void prepend(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        if (!(thisObj instanceof Node)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        final Node thisNode = (Node) thisObj;
        final DomNode thisDomNode = thisNode.getDomNodeOrDie();
        final DomNode firstChild = thisDomNode.getFirstChild();

        for (final Object arg : args) {
            final Node node = toNodeOrTextNode(thisNode, arg);
            final DomNode newNode = node.getDomNodeOrDie();
            if (firstChild == null) {
                thisDomNode.appendChild(newNode);
            }
            else {
                firstChild.insertBefore(newNode);
            }
        }
    }

    /**
     * Replaces the existing children of a Node with a specified new set of children.
     * These can be string or Node objects.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    protected static void replaceChildren(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        if (!(thisObj instanceof Node)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        final Node thisNode = (Node) thisObj;
        final DomNode thisDomNode = thisNode.getDomNodeOrDie();
        thisDomNode.removeAllChildren();

        for (final Object arg : args) {
            final Node node = toNodeOrTextNode(thisNode, arg);
            thisDomNode.appendChild(node.getDomNodeOrDie());
        }
    }

    private static Node toNodeOrTextNode(final Node thisObj, final Object obj) {
        if (obj instanceof Node) {
            return (Node) obj;
        }
        return (Node)
                ((HTMLDocument) thisObj.getOwnerDocument()).createTextNode(JavaScriptEngine.toString(obj));
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

        if (args.length == 0) {
            parentNode.removeChild(thisDomNode);
            return;
        }

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
