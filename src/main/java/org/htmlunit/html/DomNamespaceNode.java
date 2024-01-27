/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.html;

import org.htmlunit.SgmlPage;
import org.htmlunit.WebAssert;
import org.htmlunit.html.xpath.XPathHelper;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.util.StringUtils;

/**
 * Intermediate base class for DOM Nodes that have namespaces. That includes HtmlElement and HtmlAttr.
 *
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public abstract class DomNamespaceNode extends DomNode {

    private String namespaceURI_;
    private String qualifiedName_;
    private final String localName_;
    private final String localNameLC_;
    private String prefix_;

    /**
     * Creates an instance of a DOM node that can have a namespace.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     */
    protected DomNamespaceNode(final String namespaceURI, final String qualifiedName, final SgmlPage page) {
        super(page);
        WebAssert.notNull("qualifiedName", qualifiedName);
        qualifiedName_ = qualifiedName;

        if (qualifiedName.indexOf(':') == -1) {
            namespaceURI_ = namespaceURI;
            localName_ = qualifiedName_;
            prefix_ = null;
        }
        else {
            namespaceURI_ = namespaceURI;
            final int colonPosition = qualifiedName_.indexOf(':');
            localName_ = qualifiedName_.substring(colonPosition + 1);
            prefix_ = qualifiedName_.substring(0, colonPosition);
        }

        localNameLC_ = StringUtils.toRootLowerCase(localName_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI() {
        if (getPage().isHtmlPage() && !(getPage() instanceof XHtmlPage)
            && Html.XHTML_NAMESPACE.equals(namespaceURI_)
            && XPathHelper.isProcessingXPath()) {
            // for Xalan processing we have to strip the 'default' XHTML namespace for HTML pages to be able to find
            // the elements by XPath without needing to add the namespace to it
            return null;
        }
        return namespaceURI_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        final boolean caseSensitive = getPage().hasCaseSensitiveTagNames();
        if (!caseSensitive && XPathHelper.isProcessingXPath()) { // and this method was called from Xalan
            return localNameLC_;
        }
        return localName_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * @return the element name as lowercase
     */
    public String getLowercaseName() {
        return localNameLC_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return prefix_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix(final String prefix) {
        prefix_ = prefix;
        if (prefix_ != null && localName_ != null) {
            qualifiedName_ = prefix_ + ":" + localName_;
        }
    }

    /**
     * Returns this node's qualified name.
     * @return this node's qualified name
     */
    public String getQualifiedName() {
        return qualifiedName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processImportNode(final Document doc) {
        super.processImportNode(doc);

        // if we are importing from a namespace-aware source
        // we have to drop the XHtmlNamespace because we did this already
        // for the HTML document itself
        final SgmlPage page = (SgmlPage) doc.getDomNodeOrDie();
        if (page.isHtmlPage() && !(page instanceof XHtmlPage) && Html.XHTML_NAMESPACE.equals(namespaceURI_)) {
            namespaceURI_ = null;
        }
    }
}
