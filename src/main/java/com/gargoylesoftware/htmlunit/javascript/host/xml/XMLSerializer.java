/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.Node;

/**
 * A JavaScript object for XMLSerializer.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 */
public class XMLSerializer extends SimpleScriptable {

    private static final long serialVersionUID = -934136191731299896L;

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
        final StringBuilder buffer = new StringBuilder();
        final boolean isIE = getBrowserVersion().isIE();
        toXml(1, root.getDomNodeOrDie(), buffer, isIE);
        if (isIE) {
            buffer.append('\r').append('\n');
        }
        return buffer.toString();
    }

    private void toXml(final int indent, final DomNode node, final StringBuilder buffer, final boolean isIE) {
        buffer.append('<').append(node.getNodeName());
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
                case org.w3c.dom.Node.ELEMENT_NODE:
                    toXml(indent + 1, child, buffer, isIE);
                    break;

                case org.w3c.dom.Node.TEXT_NODE:
                    final String value = child.getNodeValue();
                    if (isIE && value.trim().length() == 0) {
                        buffer.append('\r').append('\n');
                        final DomNode sibling = child.getNextSibling();
                        if (sibling != null && sibling.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            for (int i = 0; i < indent; i++) {
                                buffer.append('\t');
                            }
                        }
                    }
                    else {
                        buffer.append(child.getNodeValue());
                    }
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
