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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * A JavaScript object for XMLSerializer.
 *
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 * @author Ronald Brill
 * @author Frank Danek
 */
public class XMLSerializer {

    private final boolean preserveWhiteSpace_;

    /**
     * @param preserveWhiteSpace whether to preserve whitespaces or not
     */
    public XMLSerializer(final boolean preserveWhiteSpace) {
        preserveWhiteSpace_ = preserveWhiteSpace;
    }

    /**
     * The subtree rooted by the specified element is serialized to a string.
     * @param root the root of the subtree to be serialized (this may be any node, even a document)
     * @return the serialized string
     */
    public String serializeToString(XMLDOMNode root) {
        if (root == null) {
            return "";
        }
        if (root instanceof XMLDOMDocument) {
            root = ((XMLDOMDocument) root).getDocumentElement();
        }
        else if (root instanceof XMLDOMDocumentFragment) {
            root = root.getFirstChild();
        }
        if (root instanceof XMLDOMElement) {
            final StringBuilder builder = new StringBuilder();
            final DomNode node = root.getDomNodeOrDie();

            toXml(1, node, builder);

            builder.append("\r\n");
            return builder.toString();
        }
        return root.getDomNodeOrDie().asXml();
    }

    private void toXml(final int indent,
            final DomNode node, final StringBuilder builder) {
        final String nodeName = node.getNodeName();
        builder.append('<').append(nodeName);

        final String optionalPrefix = "";
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
            builder.append(' ').append(attrib.getQualifiedName()).append('=')
                .append('"').append(attrib.getValue()).append('"');
        }
        boolean startTagClosed = false;
        for (final DomNode child : node.getChildren()) {
            if (!startTagClosed) {
                builder.append(optionalPrefix).append('>');
                startTagClosed = true;
            }
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    toXml(indent + 1, child, builder);
                    break;

                case Node.TEXT_NODE:
                    String value = child.getNodeValue();
                    value = StringUtils.escapeXmlChars(value);
                    if (preserveWhiteSpace_) {
                        builder.append(value.replace("\n", "\r\n"));
                    }
                    else if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
                        builder.append("\r\n");
                        final DomNode sibling = child.getNextSibling();
                        if (sibling != null && sibling.getNodeType() == Node.ELEMENT_NODE) {
                            for (int i = 0; i < indent; i++) {
                                builder.append('\t');
                            }
                        }
                    }
                    else {
                        builder.append(value.replace("\n", "\r\n"));
                    }
                    break;

                case Node.CDATA_SECTION_NODE:
                case Node.COMMENT_NODE:
                    if (!preserveWhiteSpace_ && builder.charAt(builder.length() - 1) == '\n') {
                        for (int i = 0; i < indent; i++) {
                            builder.append('\t');
                        }
                    }
                    builder.append(child.asXml());
                    break;

                default:

            }
        }
        if (startTagClosed) {
            if (!preserveWhiteSpace_ && builder.charAt(builder.length() - 1) == '\n') {
                for (int i = 0; i < indent - 1; i++) {
                    builder.append('\t');
                }
            }
            builder.append('<').append('/').append(nodeName).append('>');
        }
        else {
            builder.append(optionalPrefix).append("/>");
        }
    }

}
