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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code NodeFilter}.
 *
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">
 * DOM-Level-2-Traversal-Range</a>
 *
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class NodeFilter extends SimpleScriptable {

    /**
     * Accept the node.
     */
    @JsxConstant
    public static final short FILTER_ACCEPT = org.w3c.dom.traversal.NodeFilter.FILTER_ACCEPT;

    /**
     * Reject the node.
     */
    @JsxConstant
    public static final short FILTER_REJECT = org.w3c.dom.traversal.NodeFilter.FILTER_REJECT;

    /**
     * Skip the node.
     */
    @JsxConstant
    public static final short FILTER_SKIP = org.w3c.dom.traversal.NodeFilter.FILTER_SKIP;

    /** Show all nodes. */
    @JsxConstant
    public static final long SHOW_ALL = 0xFFFFFFFFL;

    /** Show Element nodes. */
    @JsxConstant
    public static final int SHOW_ELEMENT = org.w3c.dom.traversal.NodeFilter.SHOW_ELEMENT;

    /** Show Attr nodes. Only useful when creating a TreeWalker with an
     * attribute node as its root. */
    @JsxConstant
    public static final int SHOW_ATTRIBUTE = org.w3c.dom.traversal.NodeFilter.SHOW_ATTRIBUTE;

    /** Show Text nodes. */
    @JsxConstant
    public static final int SHOW_TEXT = org.w3c.dom.traversal.NodeFilter.SHOW_TEXT;

    /** Show CDATASection nodes. */
    @JsxConstant
    public static final int SHOW_CDATA_SECTION = org.w3c.dom.traversal.NodeFilter.SHOW_CDATA_SECTION;

    /** Show EntityReference nodes. */
    @JsxConstant
    public static final int SHOW_ENTITY_REFERENCE = org.w3c.dom.traversal.NodeFilter.SHOW_ENTITY_REFERENCE;

    /** Show Entity nodes. */
    @JsxConstant
    public static final int SHOW_ENTITY = org.w3c.dom.traversal.NodeFilter.SHOW_ENTITY;

    /** Show ProcessingInstruction nodes. */
    @JsxConstant
    public static final int SHOW_PROCESSING_INSTRUCTION = org.w3c.dom.traversal.NodeFilter.SHOW_PROCESSING_INSTRUCTION;

    /** Show Comment nodes. */
    @JsxConstant
    public static final int SHOW_COMMENT = org.w3c.dom.traversal.NodeFilter.SHOW_COMMENT;

    /** Show Document nodes. */
    @JsxConstant
    public static final int SHOW_DOCUMENT = org.w3c.dom.traversal.NodeFilter.SHOW_DOCUMENT;

    /** Show DocumentType nodes. */
    @JsxConstant
    public static final int SHOW_DOCUMENT_TYPE = org.w3c.dom.traversal.NodeFilter.SHOW_DOCUMENT_TYPE;

    /** Show DocumentFragment nodes. */
    @JsxConstant
    public static final int SHOW_DOCUMENT_FRAGMENT = org.w3c.dom.traversal.NodeFilter.SHOW_DOCUMENT_FRAGMENT;

    /**
     * Show Notation nodes. Only useful when creating a TreeWalker with a
     * Notation node as its root.
     */
    @JsxConstant
    public static final int SHOW_NOTATION = org.w3c.dom.traversal.NodeFilter.SHOW_NOTATION;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public NodeFilter() {
    }
}
