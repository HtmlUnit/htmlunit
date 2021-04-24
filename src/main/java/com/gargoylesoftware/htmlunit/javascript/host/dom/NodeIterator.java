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

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeIterator;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code NodeIterator}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class NodeIterator extends SimpleScriptable {

    private DomNodeIterator iterator_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public NodeIterator() {
    }

    /**
     * Creates a new instance.
     *
     * @param page the page
     * @param root The root node at which to begin the {@link NodeIterator}'s traversal
     * @param whatToShow an optional long representing a bitmask created by combining
     * the constant properties of {@link NodeFilter}
     * @param filter an object implementing the {@link NodeFilter} interface
     */
    public NodeIterator(final SgmlPage page, final Node root, final int whatToShow,
            final org.w3c.dom.traversal.NodeFilter filter) {
        iterator_ = page.createNodeIterator(root.getDomNodeOrDie(), whatToShow, filter, true);
    }

    /**
     * Returns the root node.
     * @return the root node
     */
    @JsxGetter
    public Node getRoot() {
        return getNodeOrNull(iterator_.getRoot());
    }

    private static Node getNodeOrNull(final DomNode domNode) {
        if (domNode == null) {
            return null;
        }
        return (Node) domNode.getScriptableObject();
    }

    /**
     * Returns the types of nodes being presented.
     * @return combined bitmask of {@link NodeFilter}
     */
    public long getWhatToShow() {
        if (iterator_.getWhatToShow() == NodeFilter.SHOW_ALL) {
            return 0xFFFFFFFFL;
        }
        return iterator_.getWhatToShow();
    }

    /**
     * Returns the filter.
     * @return the filter
     */
    @JsxGetter
    public Object getFilter() {
        //TODO: we should return the original filter
        return iterator_.getFilter();
    }

    /**
     * This operation is a no-op.
     */
    @JsxFunction
    public void detach() {
        iterator_.detach();
    }

    /**
     * Returns the next Node in the document, or null if there are none.
     * @return the next node
     */
    @JsxFunction
    public Node nextNode() {
        return getNodeOrNull(iterator_.nextNode());
    }

    /**
     * Returns the previous Node in the document, or null if there are none.
     * @return the previous node
     */
    @JsxFunction
    public Node previousNode() {
        return getNodeOrNull(iterator_.previousNode());
    }
}
