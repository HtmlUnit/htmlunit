/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * A JavaScript object for XMLSerializer.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 */
public class XMLSerializer extends SimpleScriptable {

    /**
     * JavaScript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * The subtree rooted by the specified element is serialized to a string.
     * @param root the root of the subtree to be serialized (this may be any node, even a document)
     * @return the serialized string
     */
    public String jsxFunction_serializeToString(Node root) {
        if (root instanceof Document) {
            root = ((Document) root).jsxGet_documentElement();
        }
        else if (root instanceof DocumentFragment) {
            root = root.jsxGet_firstChild();
        }
        if (root instanceof Element) {
            final StringBuilder buffer = new StringBuilder();
            final DomNode node = root.getDomNodeOrDie();
            final boolean nodeNameAsUpperCase =
                getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SERIALIZER_NODE_AS_UPPERCASE)
                && (node.getPage() instanceof HtmlPage);
            final boolean appendCrlf =
                getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SERIALIZER_APPENDS_CRLF);

            toXml(1, node, buffer, nodeNameAsUpperCase, appendCrlf);

            if (appendCrlf) {
                buffer.append("\r\n");
            }
            return buffer.toString();
        }
        if (root == null) {
            return "";
        }
        return root.<DomNode>getDomNodeOrDie().asXml();
    }

    private void toXml(final int indent,
            final DomNode node, final StringBuilder buffer,
            final boolean nodeNameAsUpperCase, final boolean appendCrLf) {
        String nodeName = node.getNodeName();
        if (nodeNameAsUpperCase) {
            nodeName = nodeName.toUpperCase();
        }
        buffer.append('<').append(nodeName);

        final String namespaceURI = node.getNamespaceURI();
        final String prefix = node.getPrefix();
        if (namespaceURI != null && prefix != null) {
            boolean sameNamespace = false;
            for (DomNode parentNode = node.getParentNode(); parentNode instanceof DomElement;
                    parentNode = parentNode.getParentNode()) {
                if (namespaceURI.equals(parentNode.getNamespaceURI())) {
                    sameNamespace = true;
                }
            }
            if (node.getParentNode() == null || !sameNamespace) {
                ((DomElement) node).setAttribute("xmlns:" + prefix, namespaceURI);
            }
        }
        final NamedNodeMap attributesMap = node.getAttributes();
        for (int i = 0; i < attributesMap.getLength(); i++) {
            final DomAttr attrib = (DomAttr) attributesMap.item(i);
            buffer.append(' ').append(attrib.getQualifiedName()).append('=')
                .append('"').append(attrib.getValue()).append('"');
        }
        boolean startTagClosed = false;
        for (final DomNode child : node.getChildren()) {
            if (!startTagClosed) {
                buffer.append('>');
                startTagClosed = true;
            }
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    toXml(indent + 1, child, buffer, nodeNameAsUpperCase, appendCrLf);
                    break;

                case Node.TEXT_NODE:
                    String value = child.getNodeValue();
                    value = StringUtils.escapeXmlChars(value);
                    if (appendCrLf && org.apache.commons.lang.StringUtils.isBlank(value)) {
                        buffer.append("\r\n");
                        final DomNode sibling = child.getNextSibling();
                        if (sibling != null && sibling.getNodeType() == Node.ELEMENT_NODE) {
                            for (int i = 0; i < indent; i++) {
                                buffer.append('\t');
                            }
                        }
                    }
                    else {
                        buffer.append(value);
                    }
                    break;

                case Node.CDATA_SECTION_NODE:
                case Node.COMMENT_NODE:
                    buffer.append(child.asXml());
                    break;

                default:

            }
        }
        if (!startTagClosed) {
            buffer.append('/').append('>');
        }
        else {
            buffer.append('<').append('/').append(node.getNodeName()).append('>');
        }
    }

}
