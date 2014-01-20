/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMDocumentType.<br>
 * Contains information associated with the document type declaration.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762752.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(domClass = DomDocumentType.class, browsers = @WebBrowser(IE))
public class XMLDOMDocumentType extends XMLDOMNode {

    private XMLDOMNamedNodeMap attributes_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMDocumentType() {
    }

    /**
     * Returns the list of attributes for this element.
     * @return the list of attributes for this element
     */
    @Override
    public Object getAttributes() {
        if (attributes_ == null) {
            attributes_ = new XMLDOMNamedNodeMap(getDomNodeOrDie());
        }
        return attributes_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseName() {
        return getName();
    }

    /**
     * Returns a list of the entities declared in the DOCTYPE declaration.
     * @return entities
     */
    @JsxGetter
    public Object getEntities() {
        final DomDocumentType domDocumentType = getDomNodeOrDie();
        final NamedNodeMap entities = domDocumentType.getEntities();
        if (null != entities) {
            return entities;
        }

        return "";
    }

    /**
     * Returns the name of the document type.
     * @return the name
     */
    @JsxGetter
    public String getName() {
        final DomDocumentType domDocumentType = getDomNodeOrDie();
        return domDocumentType.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return getName();
    }

    /**
     * Attempting to set the value of document types generates an error.
     * @param newValue the new value to set
     */
    @Override
    public void setNodeValue(final String newValue) {
        if (newValue == null || "null".equals(newValue)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        throw Context.reportRuntimeError("This operation cannot be performed with a node of type DTD.");
    }

    /**
     * Returns a list of the XMLDOMNotation objects present in the document type declaration.
     * @return notations
     */
    @JsxGetter
    public Object getNotations() {
        final DomDocumentType domDocumentType = getDomNodeOrDie();
        final NamedNodeMap notations = domDocumentType.getNotations();
        if (null != notations) {
            return notations;
        }

        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParentNode() {
        final DomDocumentType domDocumentType = getDomNodeOrDie();
        return domDocumentType.getPage().getScriptObject();
    }

    /**
     * Returns an empty string (""). Document types do not have associated text.
     * @return an empty string
     */
    @Override
    public Object getText() {
        return "";
    }

    /**
     * Attempting to set the text of document fragments generates an error.
     * @param value the new value for the contents of this node
     */
    @Override
    public void setText(final Object value) {
        if (value == null || "null".equals(value)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        throw Context.reportRuntimeError("This operation cannot be performed with a node of type DTD.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getXml() {
        return "<!DOCTYPE " + getName() + " [  ]>";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomDocumentType getDomNodeOrDie() {
        return (DomDocumentType) super.getDomNodeOrDie();
    }
}
