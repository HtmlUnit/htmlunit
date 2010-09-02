/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A NodeFilter.
 *
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">
 * DOM-Level-2-Traversal-Range</a>
 * @version $Revision$
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 */
public class NodeFilter extends SimpleScriptable {
    // Constants returned by acceptNode
    /**
     * Accept the node. TreeWalker navigation will return this node.
     */
    public static final short FILTER_ACCEPT = 1;
    /**
     * Reject the node. TreeWalker navigation will not return this node or
     * any of it's children.
     */
    public static final short FILTER_REJECT = 2;
    /**
     * Skip the node. TreeWalker navigation will not return this node, but WILL
     * still consider the children of this node.
     */
    public static final short FILTER_SKIP = 3;

    // Constants for whatToShow
    /** Show all nodes. */
    public static final int SHOW_ALL = 0xFFFFFFFF;
    /** Show Element nodes. */
    public static final int SHOW_ELEMENT = 0x00000001;
    /** Show Attr nodes. Only useful when creating a TreeWalker with an
     * attribute node as its root. */
    public static final int SHOW_ATTRIBUTE = 0x00000002;
    /** Show Text nodes. */
    public static final int SHOW_TEXT = 0x00000004;
    /** Show CDATASection nodes. */
    public static final int SHOW_CDATA_SECTION = 0x00000008;
    /** Show EntityReference nodes. */
    public static final int SHOW_ENTITY_REFERENCE = 0x00000010;
    /** Show Entity nodes. */
    public static final int SHOW_ENTITY = 0x00000020;
    /** Show ProcessingInstruction nodes. */
    public static final int SHOW_PROCESSING_INSTRUCTION = 0x00000040;
    /** Show Comment nodes. */
    public static final int SHOW_COMMENT = 0x00000080;
    /** Show Document nodes. */
    public static final int SHOW_DOCUMENT = 0x00000100;
    /** Show DocumentType nodes. */
    public static final int SHOW_DOCUMENT_TYPE = 0x00000200;
    /** Show DocumentFragment nodes. */
    public static final int SHOW_DOCUMENT_FRAGMENT = 0x00000400;
    /**
     * Show Notation nodes. Only useful when creating a TreeWalker with a
     * Notation node as its root.
     */
    public static final int SHOW_NOTATION = 0x00000800;

    /**
     * Test whether a specified node is visible in the logical view of a
     * TreeWalker. This is not normally called directly from user code.
     *
     * @param n The node to check to see if it passes the filter or not.
     * @return a constant to determine whether the node is accepted, rejected,
     *          or skipped.
     */
    public short acceptNode(final Node n) {
        return FILTER_ACCEPT;
    }
}
