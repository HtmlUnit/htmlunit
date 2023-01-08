/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.platform.dom.traversal;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * An implementation of {@link TreeWalker} backed by {@link com.gargoylesoftware.htmlunit.html.HtmlDomTreeWalker}.
 *
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">
 * DOM-Level-2-Traversal-Range</a>
 * @author Ronald Brill
 */
public class DomTreeWalker implements TreeWalker {

    private final com.gargoylesoftware.htmlunit.html.HtmlDomTreeWalker domTreeWalker_;

    /**
     * Creates an instance.
     *
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

    public DomTreeWalker(final DomNode root, final int whatToShow, final NodeFilter filter,
            final boolean expandEntityReferences) throws DOMException {
        domTreeWalker_ = new com.gargoylesoftware.htmlunit.html.HtmlDomTreeWalker(
                                        root, whatToShow, filter, expandEntityReferences);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode getRoot() {
        return domTreeWalker_.getRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWhatToShow() {
        return domTreeWalker_.getWhatToShow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeFilter getFilter() {
        return domTreeWalker_.getFilter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getExpandEntityReferences() {
        return domTreeWalker_.getExpandEntityReferences();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode getCurrentNode() {
        return domTreeWalker_.getCurrentNode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentNode(final Node currentNode) throws DOMException {
        domTreeWalker_.setCurrentNode(currentNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode nextNode() {
        return domTreeWalker_.nextNode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode nextSibling() {
        return domTreeWalker_.nextSibling();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode parentNode() {
        return domTreeWalker_.parentNode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode previousSibling() {
        return domTreeWalker_.previousSibling();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode lastChild() {
        return domTreeWalker_.lastChild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode previousNode() {
        return domTreeWalker_.previousNode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode firstChild() {
        return domTreeWalker_.firstChild();
    }
}
