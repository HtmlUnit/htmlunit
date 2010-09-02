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

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * A DOM object for DocumentType.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class DomDocumentType extends DomNode implements DocumentType {

    private final String name_;
    private final String publicId_;
    private final String systemId_;

    /**
     * Creates a new instance.
     * @param page the page which contains this node
     * @param name the name
     * @param publicId the public ID
     * @param systemId the system ID
     */
    public DomDocumentType(final SgmlPage page, final String name, final String publicId, final String systemId) {
        super(page);
        name_ = name;
        publicId_ = publicId;
        systemId_ = systemId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return name_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getNodeType() {
        return DOCUMENT_TYPE_NODE;
    }

    /**
     * {@inheritDoc}
     */
    public NamedNodeMap getEntities() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getInternalSubset() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name_;
    }

    /**
     * {@inheritDoc}
     */
    public NamedNodeMap getNotations() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getPublicId() {
        return publicId_;
    }

    /**
     * {@inheritDoc}
     */
    public String getSystemId() {
        return systemId_;
    }
}
