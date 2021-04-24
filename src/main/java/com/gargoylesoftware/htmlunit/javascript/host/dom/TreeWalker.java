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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.w3c.dom.DOMException;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomTreeWalker;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object that represents a {@code TreeWalker}.
 *
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">
 * DOM-Level-2-Traversal-Range</a>
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 * @author Frank Danek
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class TreeWalker extends SimpleScriptable {

    private DomTreeWalker walker_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public TreeWalker() {
    }

    /**
     * Creates an instance.
     *
     * @param page the page
     * @param root The root node of the TreeWalker. Must not be
     *          {@code null}.
     * @param whatToShow Flag specifying which types of nodes appear in the
     *          logical view of the TreeWalker. See {@link NodeFilter} for the
     *          set of possible Show_ values.
     * @param filter The {@link NodeFilter} to be used with this TreeWalker,
     *          or {@code null} to indicate no filter.
     * @param expandEntityReferences If false, the contents of
     *          EntityReference nodes are not present in the logical view.
     * @throws DOMException on attempt to create a TreeWalker with a root that
     *          is {@code null}.
     */
    public TreeWalker(final SgmlPage page, final Node root,
                      final int whatToShow,
                      final org.w3c.dom.traversal.NodeFilter filter,
                      final boolean expandEntityReferences) throws DOMException {
        if (root == null) {
            Context.throwAsScriptRuntimeEx(new DOMException(DOMException.NOT_SUPPORTED_ERR,
                                   "root must not be null"));
        }
        walker_ = page.createTreeWalker(root.getDomNodeOrDie(), whatToShow, filter, expandEntityReferences);
    }

    /**
     * Gets the root node of the TreeWalker, as specified when it was created.
     *
     * @return the root node of the TreeWalker
     */
    @JsxGetter
    public Node getRoot() {
        return getNodeOrNull(walker_.getRoot());
    }

    /**
     * Gets the whatToShow attribute of the TreeWalker. This attribute
     * determines which node types are presented via the TreeWalker. The set
     * of available constants is defined in {@link NodeFilter}.
     *
     * @return the value of the whatToShow attribute of the TreeWalker
     */
    @JsxGetter
    public long getWhatToShow() {
        long whatToShow = walker_.getWhatToShow();
        if (whatToShow == org.w3c.dom.traversal.NodeFilter.SHOW_ALL) {
            whatToShow = 0xFFFFFFFFL;
        }
        return whatToShow;
    }

    /**
     * Gets the filter used to screen nodes.
     *
     * @return the filter used to screen nodes
     */
    @JsxGetter
    public Object getFilter() {
        //TODO: we should return the original filter
        return walker_.getFilter();
    }

    /**
     * Gets the flag specifying whether or not to reject EntityReference nodes.
     *
     * @return the value of the expandEntityReferences flag
     */
    @JsxGetter(IE)
    public boolean isExpandEntityReferences() {
        return walker_.getExpandEntityReferences();
    }

    /**
     * Gets the node at which the TreeWalker is currently positioned.
     *
     * @return the currentNode
     */
    @JsxGetter
    public Node getCurrentNode() {
        return getNodeOrNull(walker_.getCurrentNode());
    }

    /**
     * Sets the node at which the TreeWalker is currently positioned.
     *
     * @param currentNode The node to be used as the current position of the
     *          TreeWalker.
     * @throws DOMException on attempt to set currentNode to
     *          {@code null}.
     */
    @JsxSetter
    public void setCurrentNode(final Node currentNode) throws DOMException {
        if (currentNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
                                   "currentNode cannot be set to null");
        }
        walker_.setCurrentNode(currentNode.getDomNodeOrDie());
    }

    /**
     * Given a {@link Node}, return the appropriate constant for whatToShow.
     *
     * @param node the node
     * @return the whatToShow constant for the type of specified node
     */
    static int getFlagForNode(final Node node) {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                return NodeFilter.SHOW_ELEMENT;
            case Node.ATTRIBUTE_NODE:
                return NodeFilter.SHOW_ATTRIBUTE;
            case Node.TEXT_NODE:
                return NodeFilter.SHOW_TEXT;
            case Node.CDATA_SECTION_NODE:
                return NodeFilter.SHOW_CDATA_SECTION;
            case Node.ENTITY_REFERENCE_NODE:
                return NodeFilter.SHOW_ENTITY_REFERENCE;
            case Node.ENTITY_NODE:
                return NodeFilter.SHOW_ENTITY;
            case Node.PROCESSING_INSTRUCTION_NODE:
                return NodeFilter.SHOW_PROCESSING_INSTRUCTION;
            case Node.COMMENT_NODE:
                return NodeFilter.SHOW_COMMENT;
            case Node.DOCUMENT_NODE:
                return NodeFilter.SHOW_DOCUMENT;
            case Node.DOCUMENT_TYPE_NODE:
                return NodeFilter.SHOW_DOCUMENT_TYPE;
            case Node.DOCUMENT_FRAGMENT_NODE:
                return NodeFilter.SHOW_DOCUMENT_FRAGMENT;
            case Node.NOTATION_NODE:
                return NodeFilter.SHOW_NOTATION;
            default:
                return 0;
        }
    }

    /**
     * Moves to and returns the closest visible ancestor node of the current
     * node. If the search for parentNode attempts to step upward from the
     * TreeWalker's root node, or if it fails to find a visible ancestor node,
     * this method retains the current position and returns null.
     *
     * @return The new parent node, or {@code null} if the current node
     *          has no parent in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node parentNode() {
        return getNodeOrNull(walker_.parentNode());
    }

    private static Node getNodeOrNull(final DomNode domNode) {
        if (domNode == null) {
            return null;
        }
        return (Node) domNode.getScriptableObject();
    }

    /**
     * Moves the TreeWalker to the first visible child of the current node,
     * and returns the new node. If the current node has no visible children,
     * returns {@code null}, and retains the current node.
     *
     * @return The new node, or {@code null} if the current node has no
     *          visible children in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node firstChild() {
        return getNodeOrNull(walker_.firstChild());
    }

    /**
     * Moves the TreeWalker to the last visible child of the current node,
     * and returns the new node. If the current node has no visible children,
     * returns {@code null}, and retains the current node.
     *
     * @return The new node, or {@code null} if the current node has no
     *          visible children in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node lastChild() {
        return getNodeOrNull(walker_.lastChild());
    }

    /**
      * Moves the TreeWalker to the previous sibling of the current node, and
      * returns the new node. If the current node has no visible previous
      * sibling, returns {@code null}, and retains the current node.
      *
      * @return The new node, or {@code null} if the current node has no
      *          previous sibling in the TreeWalker's logical view.
      */
    @JsxFunction
    public Node previousSibling() {
        return getNodeOrNull(walker_.previousSibling());
    }

     /**
      * Moves the TreeWalker to the next sibling of the current node, and
      * returns the new node. If the current node has no visible next sibling,
      * returns {@code null}, and retains the current node.
      *
      * @return The new node, or {@code null} if the current node has no
      *          next sibling in the TreeWalker's logical view.
      */
    @JsxFunction
    public Node nextSibling() {
        return getNodeOrNull(walker_.nextSibling());
    }

    /**
     * Moves the TreeWalker to the previous visible node in document order
     * relative to the current node, and returns the new node. If the current
     * node has no previous node, or if the search for previousNode attempts
     * to step upward from the TreeWalker's root node, returns
     * {@code null}, and retains the current node.
     *
     * @return The new node, or {@code null} if the current node has no
     *          previous node in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node previousNode() {
        return getNodeOrNull(walker_.previousNode());
    }

    /**
     * Moves the TreeWalker to the next visible node in document order
     * relative to the current node, and returns the new node. If the current
     * node has no next node, or if the search for nextNode attempts to step
     * upward from the TreeWalker's root node, returns {@code null}, and
     * retains the current node.
     *
     * @return The new node, or {@code null} if the current node has no
     *          next node in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node nextNode() {
        return getNodeOrNull(walker_.nextNode());
    }

}
