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

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
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

    private NamedNodeMap attributes_;
    private Map<String, HTMLCollection> elementsByTagName_; // for performance and for equality (==)

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
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.attributes">Gecko DOM Reference</a>
     * @return the attributes of this XML element
     */
    public Object jsxGet_attributes() {
        if (attributes_ == null) {
            attributes_ = createAttributesObject();
        }
        return attributes_;
    }

    /**
     * Creates the JS object for the property attributes. This object will the be cached.
     * @return the JS object
     */
    protected NamedNodeMap createAttributesObject() {
        return new NamedNodeMap(getDomNodeOrDie());
    }

    /**
     * Returns the value of the specified attribute.
     * @param attributeName attribute name
     * @param flags IE-specific flags (see the MSDN documentation for more info)
     * @return the value of the specified attribute, <code>null</code> if the attribute is not defined
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536429.aspx">MSDN Documentation</a>
     * @see <a href="http://reference.sitepoint.com/javascript/Element/getAttribute">IE Bug Documentation</a>
     */
    public Object jsxFunction_getAttribute(String attributeName, final Integer flags) {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_36);
        attributeName = fixAttributeName(attributeName);

        Object value;
        if (ie && flags != null && flags == 2 && "style".equalsIgnoreCase(attributeName)) {
            value = "";
        }
        else {
            value = getDomNodeOrDie().getAttribute(attributeName);
        }

        if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
            value = null;
            if (ie) {
                for (Scriptable object = this; object != null; object = object.getPrototype()) {
                    final Object property = object.get(attributeName, this);
                    if (property != NOT_FOUND) {
                        value = property;
                        break;
                    }
                }
            }
        }

        return value;
    }

    /**
     * Allows subclasses to transform the attribute name before it gets used.
     * @param attributeName the original attribute
     * @return this implementation returns the original value
     */
    protected String fixAttributeName(final String attributeName) {
        return attributeName;
    }

    /**
     * Sets an attribute.
     *
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    public void jsxFunction_setAttribute(final String name, final String value) {
        getDomNodeOrDie().setAttribute(name, value);
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    public Object jsxFunction_getElementsByTagName(String tagName) {
        tagName = tagName.toLowerCase();

        if (elementsByTagName_ == null) {
            elementsByTagName_ = new HashMap<String, HTMLCollection>();
        }

        HTMLCollection collection = elementsByTagName_.get(tagName);
        if (collection != null) {
            return collection;
        }

        final DomNode node = getDomNodeOrDie();
        collection = new HTMLCollection(this);
        final String xpath;
        if ("*".equals(tagName)) {
            xpath = ".//*";
        }
        else {
            xpath = ".//*[local-name() = '" + tagName + "']";
        }
        collection.init(node, xpath);

        elementsByTagName_.put(tagName, collection);

        return collection;
    }

    /**
     * Retrieves an attribute node by name.
     * @param name the name of the attribute to retrieve
     * @return the XMLAttr node with the specified name or <code>null</code> if there is no such attribute
     */
    public Object jsxFunction_getAttributeNode(final String name) {
        final Map<String, DomAttr> attributes = getDomNodeOrDie().getAttributesMap();
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
            case Node.DOCUMENT_TYPE_NODE:
            case Node.NOTATION_NODE:
                return;
            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
                buffer.append(node.getNodeValue());
                break;
            default:
        }
        for (final DomNode child : node.getChildren()) {
            switch (child.getNodeType()) {
                case ELEMENT_NODE:
                    toText(child, buffer);
                    break;

                case TEXT_NODE:
                case CDATA_SECTION_NODE:
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
    public Object jsxFunction_getElementsByTagNameNS(final Object namespaceURI, final String localName) {
        final DomNode domNode = getDomNodeOrDie();
        final HTMLCollection collection = new HTMLCollection(this);
        final String xpath;
        if (namespaceURI == null || namespaceURI.equals("*")) {
            xpath = ".//" + localName;
        }
        else {
            final String prefix = XmlUtil.lookupPrefix((DomElement) domNode, Context.toString(namespaceURI));
            xpath = ".//" + prefix + ':' + localName;
        }
        collection.init(domNode, xpath);
        return collection;
    }

    /**
     * Returns true when an attribute with a given name is specified on this element or has a default value.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-ElHasAttr">
     * the DOM reference</a>
     * @param name the name of the attribute to look for
     * @return true if an attribute with the given name is specified on this element or has a default value
     */
    public boolean jsxFunction_hasAttribute(final String name) {
        return getDomNodeOrDie().hasAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomElement getDomNodeOrDie() {
        return (DomElement) super.getDomNodeOrDie();
    }

    /**
     * Removes the specified attribute.
     * @param name the name of the attribute to remove
     */
    public void jsxFunction_removeAttribute(final String name) {
        getDomNodeOrDie().removeAttribute(name);
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_37)) {
            delete(name);
        }
    }

}
