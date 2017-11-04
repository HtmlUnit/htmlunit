/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_NODE_INSERT_BEFORE_REF_OPTIONAL;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;

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
@ScriptClass
public class Node2 extends EventTarget2 {

    /** "Live" child nodes collection; has to be a member to have equality (==) working. */
    private NodeList2 childNodes_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static Node2 constructor(final boolean newObj, final Object self) {
        final Node2 host = new Node2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Returns the owner document.
     * @return the document
     */
    @Getter
    public Object getOwnerDocument() {
        final Object document = getDomNodeOrDie().getOwnerDocument();
        if (document != null) {
            return ((SgmlPage) document).getScriptObject2();
        }
        return null;
    }

    /**
     * Gets the JavaScript property {@code parentElement}.
     * @return the parent element
     * @see #getParentNode()
     */
    @Getter({CHROME, FF})
    public Element2 getParentElement() {
        final Node2 parent = getParent();
        if (!(parent instanceof Element2)) {
            return null;
        }
        return (Element2) parent;
    }

    /**
     * Returns this node's parent node.
     * @return this node's parent node
     */
    public final Node2 getParent() {
        return getJavaScriptNode(getDomNodeOrDie().getParentNode());
    }

    /**
     * Gets the JavaScript node for a given DomNode.
     * @param domNode the DomNode
     * @return the JavaScript node or null if the DomNode was null
     */
    protected Node2 getJavaScriptNode(final DomNode domNode) {
        if (domNode == null) {
            return null;
        }
        return (Node2) getScriptableFor(domNode);
    }

    /**
     * Returns the JavaScript object that corresponds to the specified object.
     * New JavaScript objects will be created as needed. If a JavaScript object
     * cannot be created for a domNode then NOT_FOUND will be returned.
     *
     * @param object a {@link DomNode} or a {@link WebWindow}
     * @return the JavaScript object or NOT_FOUND
     */
    @Override
    protected SimpleScriptObject getScriptableFor(final Object object) {
        final DomNode domNode = (DomNode) object;

        final Object scriptObject = domNode.getScriptObject2();
        if (scriptObject != null) {
            return (SimpleScriptObject) scriptObject;
        }
        return makeScriptableFor(domNode);
    }

    /**
     * Adds a DOM node to the node.
     * @param childObject the node to add to this node
     * @return the newly added child node
     */
    @Function
    public Object appendChild(final Object childObject) {
        Object appendedChild = null;
        if (childObject instanceof Node2) {
            final Node2 childNode = (Node2) childObject;

            // is the node allowed here?
            if (!isNodeInsertable(childNode)) {
                throw new RuntimeException("Node cannot be inserted at the specified point in the hierarchy");
//                throw new RuntimeException(
//                    new DOMException("Node cannot be inserted at the specified point in the hierarchy",
//                        DOMException.HIERARCHY_REQUEST_ERR));
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
     * Indicates if the node can be inserted.
     * @param childObject the node
     * @return {@code false} if it is not allowed here
     */
    private static boolean isNodeInsertable(final Node2 childObject) {
        if (childObject instanceof HTMLHtmlElement2) {
            final DomNode domNode = childObject.getDomNodeOrDie();
            return !(domNode.getPage().getDocumentElement() == domNode);
        }
        return true;
    }

    /**
     * Gets the JavaScript property {@code firstChild} for the node that
     * contains the current node.
     * @return the first child node or null if the current node has
     * no children.
     */
    @Getter
    public Node2 getFirstChild() {
        return getJavaScriptNode(getDomNodeOrDie().getFirstChild());
    }

    /**
     * Gets the JavaScript property {@code lastChild} for the node that
     * contains the current node.
     * @return the last child node or null if the current node has
     * no children.
     */
    @Getter
    public Node2 getLastChild() {
        return getJavaScriptNode(getDomNodeOrDie().getLastChild());
    }

    /**
     * Gets the JavaScript property {@code nodeType} for the current node.
     * @return the node type
     */
    @Getter
    public Short getNodeType() {
        return getDomNodeOrDie().getNodeType();
    }

    /**
     * Gets the JavaScript property {@code nodeName} for the current node.
     * @return the node name
     */
    @Getter
    public String getNodeName() {
        return getDomNodeOrDie().getNodeName();
    }

    /**
     * Gets the JavaScript property {@code nodeValue} for the current node.
     * @return the node value
     */
    @Getter
    public String getNodeValue() {
        return getDomNodeOrDie().getNodeValue();
    }

    /**
     * Sets the JavaScript property {@code nodeValue} for the current node.
     * @param newValue the new node value
     */
    @Setter
    public void setNodeValue(final String newValue) {
        getDomNodeOrDie().setNodeValue(newValue);
    }

    /**
     * Returns the namespace prefix.
     * @return the namespace prefix
     */
    @Getter
    public Object getPrefix() {
        final DomNode domNode = getDomNodeOrDie();
        return domNode.getPrefix();
    }

    /**
     * Add a DOM node as a child to this node before the referenced node.
     * If the referenced node is null, append to the end.
     * @param newChild the node to be inserted
     * @param refChildObject the node before which {@code newChild} is inserted
     * @return the newly added child node
     */
    @Function
    public Object insertBefore(final Node2 newChild, final Object refChildObject) {
        Object insertedChild = null;

        final DomNode newChildNode = newChild.getDomNodeOrDie();

        // is the node allowed here?
        if (!isNodeInsertable(newChild)) {
            /*throw new DOMException2("Node cannot be inserted at the specified point in the hierarchy",
                        DOMException.HIERARCHY_REQUEST_ERR);*/
        }
        if (newChildNode instanceof DomDocumentFragment) {
            final DomDocumentFragment fragment = (DomDocumentFragment) newChildNode;
            for (final DomNode child : fragment.getChildren()) {
                if (!isNodeInsertable((Node2) child.getScriptObject2())) {
//                    throw asJavaScriptException(
//                        new DOMException("Node cannot be inserted at the specified point in the hierarchy",
//                            DOMException.HIERARCHY_REQUEST_ERR));
                }
            }
        }

        // extract refChild
        final DomNode refChildNode;
        if (refChildObject == ScriptRuntime.UNDEFINED) {
            if (/*args.length == 2 || */getBrowserVersion().hasFeature(JS_NODE_INSERT_BEFORE_REF_OPTIONAL)) {
                refChildNode = null;
            }
            else {
                throw new RuntimeException("insertBefore: not enough arguments");
                //throw ECMAErrors.error(new RuntimeExc"insertBefore: not enough arguments");
            }
        }
        else if (refChildObject != null) {
            refChildNode = ((Node2) refChildObject).getDomNodeOrDie();
        }
        else {
            refChildNode = null;
        }

        final DomNode domNode = getDomNodeOrDie();

        try {
            domNode.insertBefore(newChildNode, refChildNode);
        }
        catch (final org.w3c.dom.DOMException e) {
//            throw asJavaScriptException(
//                    new DOMException(e.getMessage(),
//                        DOMException.HIERARCHY_REQUEST_ERR));
        }
        insertedChild = newChild;
        return insertedChild;
    }

    /**
     * Replaces a child DOM node with another DOM node.
     * @param newChildObject the node to add as a child of this node
     * @param oldChildObject the node to remove as a child of this node
     * @return the removed child node
     */
    @Function
    public Object replaceChild(final Object newChildObject, final Object oldChildObject) {
        Object removedChild = null;

        if (newChildObject instanceof DocumentFragment) {
            final DocumentFragment fragment = (DocumentFragment) newChildObject;
            Node2 firstNode = null;
            final Node2 refChildObject = ((Node2) oldChildObject).getNextSibling();
            for (final DomNode node : fragment.getDomNodeOrDie().getChildren()) {
                if (firstNode == null) {
                    replaceChild(node.getScriptObject2(), oldChildObject);
                    firstNode = (Node2) node.getScriptObject2();
                }
                else {
                    insertBefore((Node2) node.getScriptObject2(), refChildObject);
                }
            }
            if (firstNode == null) {
                removeChild(oldChildObject);
            }
            removedChild = oldChildObject;
        }
        else if (newChildObject instanceof Node2 && oldChildObject instanceof Node2) {
            final Node2 newChild = (Node2) newChildObject;

            // is the node allowed here?
            if (!isNodeInsertable(newChild)) {
                throw new RuntimeException("Node cannot be inserted at the specified point in the hierarchy");
            }

            // Get XML nodes for the DOM nodes passed in
            final DomNode newChildNode = newChild.getDomNodeOrDie();
            final DomNode oldChildNode = ((Node2) oldChildObject).getDomNodeOrDie();

            // Replace the old child with the new child.
            oldChildNode.replace(newChildNode);
            removedChild = oldChildObject;
        }

        return removedChild;
    }

    /**
     * Gets the JavaScript property {@code nextSibling} for the node that
     * contains the current node.
     * @return the next sibling node or null if the current node has
     * no next sibling.
     */
    @Getter
    public Node2 getNextSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getNextSibling());
    }

    /**
     * Gets the JavaScript property {@code previousSibling} for the node that
     * contains the current node.
     * @return the previous sibling node or null if the current node has
     * no previous sibling.
     */
    @Getter
    public Node2 getPreviousSibling() {
        return getJavaScriptNode(getDomNodeOrDie().getPreviousSibling());
    }

    /**
     * Removes a DOM node from this node.
     * @param childObject the node to remove from this node
     * @return the removed child node
     */
    @Function
    public Object removeChild(final Object childObject) {
        if (!(childObject instanceof Node)) {
            return null;
        }

        // Get XML node for the DOM node passed in
        final DomNode childNode = ((Node) childObject).getDomNodeOrDie();

        if (!getDomNodeOrDie().isAncestorOf(childNode)) {
            throw new RuntimeException("NotFoundError: Failed to execute 'removeChild' on '"
                        + this + "': The node to be removed is not a child of this node.");
        }
        // Remove the child from the parent node
        childNode.remove();
        return childObject;
    }

    /**
     * Returns the local name of this element.
     * @return the local name of this element
     */
    @Getter
    public Object getLocalName() {
        return getDomNodeOrDie().getLocalName();
    }

    /**
     * Returns The URI that identifies an XML namespace.
     * @return the URI that identifies an XML namespace
     */
    @Getter
    public Object getNamespaceURI() {
        return getDomNodeOrDie().getNamespaceURI();
    }

    /**
     * Compares the positions of this node and the provided node within the document.
     * @param node node object that specifies the node to check
     * @return how the node is positioned relatively to the reference node.
     * @see <a href="http://www.w3.org/TR/DOM-Level-3-Core/core.html#Node3-compareDocumentPosition">DOM level 3</a>
     * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
     */
    @Function
    public short compareDocumentPosition(final Object node) {
        if (!(node instanceof Node2)) {
            throw new RuntimeException("Could not convert JavaScript argument arg 0");
        }
        return getDomNodeOrDie().compareDocumentPosition(((Node2) node).getDomNodeOrDie());
    }

    /**
     * Merges adjacent TextNode objects to produce a normalized document object model.
     */
    @Function
    public void normalize() {
        getDomNodeOrDie().normalize();
    }

    /**
     * Gets the textContent attribute.
     * @return the contents of this node as text
     */
    @Getter
    public String getTextContent() {
        return getDomNodeOrDie().getTextContent();
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value - the new value for the contents of this node
     */
    @Setter
    public void setTextContent(final Object value) {
        getDomNodeOrDie().setTextContent(value == null ? null : /*Context.toString*/value.toString());
    }

    /**
     * Returns the attributes of this XML element.
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/Node.attributes">Gecko DOM Reference</a>
     * @return the attributes of this XML element
     */
    @Getter(IE)
    public Object getAttributes() {
        return null;
    }

    /**
     * Returns the child nodes of the current element.
     * @return the child nodes of the current element
     */
    @Getter
    public NodeList2 getChildNodes() {
        if (childNodes_ == null) {
            final DomNode node = getDomNodeOrDie();
            childNodes_ = new NodeList2(node, false) {
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
     * Clones this node.
     *
     * @param deep if {@code true}, recursively clones all descendants
     * @return the newly cloned node
     */
    @Function
    public Object cloneNode(final boolean deep) {
        final DomNode clonedNode = getDomNodeOrDie().cloneNode(deep);
        return getJavaScriptNode(clonedNode);
    }

    /**
     * Gets the JavaScript property {@code parentNode} for the node that
     * contains the current node.
     * @return the parent node
     */
    @Getter
    public Object getParentNode() {
        return getJavaScriptNode(getDomNodeOrDie().getParentNode());
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Node2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("Node",
                    staticHandle("constructor", Node2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("Node");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("Node");
        }
    }
}
