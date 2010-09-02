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
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.DocumentFragment;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * A DOM object for DocumentFragment.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class DomDocumentFragment extends DomNode implements DocumentFragment {

    /** The symbolic node name. */
    public static final String NODE_NAME = "#document-fragment";

    /**
     * Creates a new instance.
     * @param page the page which contains this node
     */
    public DomDocumentFragment(final SgmlPage page) {
        super(page);
    }

    /**
     * {@inheritDoc}
     * @return the node name, in this case {@link #NODE_NAME}
     */
    @Override
    public String getNodeName() {
        return NODE_NAME;
    }

    /**
     * {@inheritDoc}
     * @return the node type constant, in this case {@link org.w3c.dom.Node#DOCUMENT_FRAGMENT_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asXml() {
        return getFirstChild().asXml();
    }
}
