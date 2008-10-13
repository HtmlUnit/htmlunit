/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;

/**
 * A JavaScript object for {@link DomElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 */
public class Element extends EventNode {

    private static final long serialVersionUID = 5616690634173934926L;

    /**
     * Applies the specified XPath expression to this node's context and returns the generated list of matching nodes.
     * @param expression a string specifying an XPath expression
     * @return list of the found elements
     */
    public HTMLCollection jsxFunction_selectNodes(final String expression) {
        final HTMLCollection collection = new HTMLCollection(this);
        collection.init(getDomNodeOrDie(), expression);
        return collection;
    }

    /**
     * Applies the specified pattern-matching operation to this node's context and returns the first matching node.
     * @param expression a string specifying an XPath expression
     * @return the first node that matches the given pattern-matching operation
     *         If no nodes match the expression, returns a null value.
     */
    public Object jsxFunction_selectSingleNode(final String expression) {
        final HTMLCollection collection = jsxFunction_selectNodes(expression);
        if (collection.jsxGet_length() > 0) {
            return collection.get(0, collection);
        }
        return null;
    }

    /**
     * Returns the tag name of this element.
     * @return the tag name
     */
    public final String jsxGet_tagName() {
        return jsxGet_nodeName();
    }

    /**
     * Returns the attributes of this XML element.
     * @return the attributes of this XML element
     */
    public Object jsxGet_attributes() {
        final Map<String, DomAttr> attributes = ((DomElement) getDomNodeOrDie()).getAttributesMap();
        final List<ScriptableObject> list = new ArrayList<ScriptableObject>();
        for (final DomAttr attr : attributes.values()) {
            list.add(attr.getScriptObject());
        }
        return new NamedNodeMap((DomElement) getDomNodeOrDie(), false);
    }

    /**
     * Gets the specified attribute.
     * @param attributeName attribute name
     * @return the value of the specified attribute, <code>null</code> if the attribute is not defined
     */
    public String jsxFunction_getAttribute(final String attributeName) {
        final String value = ((DomElement) getDomNodeOrDie()).getAttributeValue(attributeName);
        if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
            return null;
        }
        return value;
    }

    /**
     * Sets an attribute.
     *
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    public void jsxFunction_setAttribute(final String name, final String value) {
        ((DomElement) getDomNodeOrDie()).setAttributeValue(name, value);
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    public Object jsxFunction_getElementsByTagName(final String tagName) {
        final DomNode domNode = getDomNodeOrDie();
        final HTMLCollection collection = new HTMLCollection(this);
        final String xpath = ".//" + tagName;
        collection.init(domNode, xpath);
        return collection;
    }

    /**
     * Retrieves an attribute node by name.
     * @param name the name of the attribute to retrieve
     * @return the XMLAttr node with the specified name or <code>null</code> if there is no such attribute
     */
    public Object jsxFunction_getAttributeNode(final String name) {
        final Map<String, DomAttr> attributes = ((DomElement) getDomNodeOrDie()).getAttributesMap();
        for (final DomAttr attr : attributes.values()) {
            if (attr.getName().equals(name)) {
                return attr.getScriptObject();
            }
        }
        return null;
    }

    /**
     * Represents the text content of the node or the concatenated text representing the node and its descendants.
     * @return the text content of the node or the concatenated text representing the node and its descendants
     */
    public String jsxGet_text() {
        final StringBuilder buffer = new StringBuilder();
        toText(getDomNodeOrDie(), buffer);
        return buffer.toString();
    }

    private void toText(final DomNode node, final StringBuilder buffer) {
        switch (node.getNodeType()) {
            case org.w3c.dom.Node.DOCUMENT_TYPE_NODE:
            case org.w3c.dom.Node.NOTATION_NODE:
                return;
            case org.w3c.dom.Node.TEXT_NODE:
            case org.w3c.dom.Node.CDATA_SECTION_NODE:
            case org.w3c.dom.Node.COMMENT_NODE:
            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                buffer.append(node.getNodeValue());
                break;
            default:
        }
        for (final DomNode child : node.getChildren()) {
            switch (child.getNodeType()) {
                case org.w3c.dom.Node.ELEMENT_NODE:
                    toText(child, buffer);
                    break;

                case org.w3c.dom.Node.TEXT_NODE:
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    buffer.append(child.getNodeValue());
                    break;
                default:
            }
        }
    }

    /**
     * Returns a list of elements with the given tag name belonging to the given namespace.
     * @param namespaceURI the namespace URI of elements to look for
     * @param localName is either the local name of elements to look for or the special value "*",
     *                  which matches all elements.
     * @return a live NodeList of found elements in the order they appear in the tree
     */
    public Object jsxFunction_getElementsByTagNameNS(final String namespaceURI, final String localName) {
        final DomNode domNode = getDomNodeOrDie();
        final HTMLCollection collection = new HTMLCollection(this);
        final String xpath;
        if (namespaceURI.equals("*")) {
            xpath = ".//" + localName;
        }
        else {
            final String prefix = XmlUtil.lookupPrefix((DomElement) domNode, namespaceURI);
            xpath = ".//" + prefix + ':' + localName;
        }
        collection.init(domNode, xpath);
        return collection;
    }

    /**
     * Returns true when an attribute with a given name is specified on this element or has a default value.
     * @param name the name of the attribute to look for
     * @return true if an attribute with the given name is specified on this element or has a default value
     */
    public boolean jsxFunction_hasAttribute(final String name) {
        return ((DomElement) getDomNodeOrDie()).hasAttribute(name);
    }

    /**
     * Represents the xml content of the node and its descendants.
     * @return the xml content of the node and its descendants
     */
    public String jsxGet_xml() {
        final OutputFormat format = new OutputFormat();
        format.setOmitXMLDeclaration(true);
        final StringWriter writer = new StringWriter();
        final XMLSerializer serializer = new XMLSerializer(format);
        serializer.setOutputCharStream(writer);
        try {
            serializer.serialize(((DomElement) getDomNodeOrDie()));
        }
        catch (final Exception e) {
            throw new RuntimeException("Internal error: failed to serialize", e);
        }

        return writer.toString();
    }

    /**
     * Returns The Namespace prefix.
     * @return the Namespace prefix
     */
    public Object jsxGet_prefix() {
        if (getBrowserVersion().isIE()) {
            if (getDomNodeOrDie().getPage() instanceof HtmlPage) {
                return NOT_FOUND;
            }
            return "";
        }
        return getDomNodeOrDie().getPrefix();
    }

    /**
     * Returns the local name of this element.
     * @return the local name of this element
     */
    public Object jsxGet_localName() {
        if (getBrowserVersion().isIE()) {
            return NOT_FOUND;
        }
        return getDomNodeOrDie().getLocalName();
    }

    /**
     * Returns The URI that identifies an XML namespace.
     * @return the URI that identifies an XML namespace
     */
    public Object jsxGet_namespaceURI() {
        if (getBrowserVersion().isIE()) {
            if (getDomNodeOrDie().getPage() instanceof HtmlPage) {
                return NOT_FOUND;
            }
            return "";
        }
        return getDomNodeOrDie().getNamespaceURI();
    }
}
