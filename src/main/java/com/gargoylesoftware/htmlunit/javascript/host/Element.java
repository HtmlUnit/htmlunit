/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_37;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_GET_ATTRIBUTE_SUPPORTS_FLAGS;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;

/**
 * A JavaScript object for {@link DomElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 */
@JsxClass(domClasses = DomElement.class)
public class Element extends EventNode {

    private NamedNodeMap attributes_;
    private Map<String, HTMLCollection> elementsByTagName_; // for performance and for equality (==)

    /**
     * Applies the specified XPath expression to this node's context and returns the generated list of matching nodes.
     * @param expression a string specifying an XPath expression
     * @return list of the found elements
     */
    @JsxFunction(@WebBrowser(IE))
    public HTMLCollection selectNodes(final String expression) {
        final DomElement domNode = getDomNodeOrDie();
        final boolean attributeChangeSensitive = expression.contains("@");
        final String description = "Element.selectNodes('" + expression + "')";
        final HTMLCollection collection = new HTMLCollection(domNode, attributeChangeSensitive, description) {
            @Override
            protected List<Object> computeElements() {
                return new ArrayList<Object>(domNode.getByXPath(expression));
            }
        };
        return collection;
    }

    /**
     * Applies the specified pattern-matching operation to this node's context and returns the first matching node.
     * @param expression a string specifying an XPath expression
     * @return the first node that matches the given pattern-matching operation
     *         If no nodes match the expression, returns a null value.
     */
    @JsxFunction(@WebBrowser(IE))
    public Object selectSingleNode(final String expression) {
        final HTMLCollection collection = selectNodes(expression);
        if (collection.getLength() > 0) {
            return collection.get(0, collection);
        }
        return null;
    }

    /**
     * Returns the tag name of this element.
     * @return the tag name
     */
    @JsxGetter
    public String getTagName() {
        return getNodeName();
    }

    /**
     * Returns the attributes of this XML element.
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/Node.attributes">Gecko DOM Reference</a>
     * @return the attributes of this XML element
     */
    @Override
    @JsxGetter
    public Object getAttributes() {
        if (attributes_ == null) {
            attributes_ = createAttributesObject();
        }
        return attributes_;
    }

    /**
     * Returns the Base URI as a string.
     * @return the Base URI as a string
     */
    @JsxGetter
    public String getBaseURI() {
        return getDomNodeOrDie().getPage().getUrl().toExternalForm();
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
    @JsxFunction
    public Object getAttribute(String attributeName, final Integer flags) {
        attributeName = fixAttributeName(attributeName);
        final boolean supportsFlags = getBrowserVersion().hasFeature(JS_GET_ATTRIBUTE_SUPPORTS_FLAGS);

        Object value;
        if (supportsFlags && flags != null && flags == 2 && "style".equalsIgnoreCase(attributeName)) {
            value = "";
        }
        else {
            value = getDomNodeOrDie().getAttribute(attributeName);
        }

        if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
            value = null;
            if (supportsFlags) {
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
    @JsxFunction
    public void setAttribute(final String name, final String value) {
        getDomNodeOrDie().setAttribute(name, value);
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    @JsxFunction
    public Object getElementsByTagName(final String tagName) {
        final String tagNameLC = tagName.toLowerCase();

        if (elementsByTagName_ == null) {
            elementsByTagName_ = new HashMap<String, HTMLCollection>();
        }

        HTMLCollection collection = elementsByTagName_.get(tagNameLC);
        if (collection != null) {
            return collection;
        }

        final DomNode node = getDomNodeOrDie();
        final String description = "Element.getElementsByTagName('" + tagNameLC + "')";
        if ("*".equals(tagName)) {
            collection = new HTMLCollection(node, false, description) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
        }
        else {
            collection = new HTMLCollection(node, false, description) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return tagNameLC.equalsIgnoreCase(node.getNodeName());
                }
            };
        }

        elementsByTagName_.put(tagName, collection);

        return collection;
    }

    /**
     * Retrieves an attribute node by name.
     * @param name the name of the attribute to retrieve
     * @return the XMLAttr node with the specified name or <code>null</code> if there is no such attribute
     */
    @JsxFunction
    public Object getAttributeNode(final String name) {
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
    @JsxGetter(@WebBrowser(IE))
    public String getText() {
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
    @JsxFunction(@WebBrowser(FF))
    public Object getElementsByTagNameNS(final Object namespaceURI, final String localName) {
        final String description = "Element.getElementsByTagNameNS('" + namespaceURI + "', '" + localName + "')";

        final HTMLCollection collection = new HTMLCollection(getDomNodeOrDie(), false, description) {
            @Override
            protected boolean isMatching(final DomNode node) {
                return localName.equals(node.getLocalName());
            }
        };

        return collection;
    }

    /**
     * Returns true when an attribute with a given name is specified on this element or has a default value.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-ElHasAttr">
     * the DOM reference</a>
     * @param name the name of the attribute to look for
     * @return true if an attribute with the given name is specified on this element or has a default value
     */
    @JsxFunction(@WebBrowser(FF))
    public boolean hasAttribute(final String name) {
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
    @JsxFunction
    public void removeAttribute(final String name) {
        getDomNodeOrDie().removeAttribute(name);
        if (getBrowserVersion().hasFeature(GENERATED_37)) {
            delete(name);
        }
    }

    /**
     * Retrieves an object that specifies the bounds of a collection of TextRectangle objects.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536433.aspx">MSDN doc</a>
     * @return an object that specifies the bounds of a collection of TextRectangle objects
     */
    @JsxFunction
    public ClientRect getBoundingClientRect() {
        return null;
    }

    /**
     * Returns the current number of child elements.
     * @return the child element count
     */
    @JsxGetter(@WebBrowser(FF))
    public int getChildElementCount() {
        return getDomNodeOrDie().getChildElementCount();
    }

    /**
     * Returns the first element child.
     * @return the first element child
     */
    @JsxGetter(@WebBrowser(FF))
    public Element getFirstElementChild() {
        final DomElement child = getDomNodeOrDie().getFirstElementChild();
        if (child != null) {
            return (Element) child.getScriptObject();
        }
        return null;
    }

    /**
     * Returns the last element child.
     * @return the last element child
     */
    @JsxGetter(@WebBrowser(FF))
    public Element getLastElementChild() {
        final DomElement child = getDomNodeOrDie().getLastElementChild();
        if (child != null) {
            return (Element) child.getScriptObject();
        }
        return null;
    }

    /**
     * Returns the next element sibling.
     * @return the next element sibling
     */
    @JsxGetter(@WebBrowser(FF))
    public Element getNextElementSibling() {
        final DomElement child = getDomNodeOrDie().getNextElementSibling();
        if (child != null) {
            return (Element) child.getScriptObject();
        }
        return null;
    }

    /**
     * Returns the previous element sibling.
     * @return the previous element sibling
     */
    @JsxGetter(@WebBrowser(FF))
    public Element getPreviousElementSibling() {
        final DomElement child = getDomNodeOrDie().getPreviousElementSibling();
        if (child != null) {
            return (Element) child.getScriptObject();
        }
        return null;
    }

    /**
     * Gets the first ancestor instance of {@link Element}. It is mostly identical
     * to {@link #getParent()} except that it skips non {@link Element} nodes.
     * @return the parent element
     * @see #getParent()
     */
    @Override
    public Element getParentElement() {
        Node parent = getParent();
        while (parent != null && !(parent instanceof Element)) {
            parent = parent.getParent();
        }
        return (Element) parent;
    }

    /**
     * Callback method which allows different HTML element types to perform custom
     * initialization of computed styles. For example, body elements in most browsers
     * have default values for their margins.
     *
     * @param style the style to initialize
     */
    public void setDefaults(final ComputedCSSStyleDeclaration style) {
        // Empty by default; override as necessary.
    }

    /**
     * Gets the children of the current node.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537446.aspx">MSDN documentation</a>
     * @return the child at the given position
     */
    @JsxGetter(@WebBrowser(FF))
    public HTMLCollection getChildren() {
        final DomElement node = getDomNodeOrDie();
        final HTMLCollection collection = new HTMLCollection(node, false, "Element.children") {
            @Override
            protected List<Object> computeElements() {
                return new ArrayList<Object>(node.getChildNodes());
            }
        };
        return collection;
    }

    /**
     * Gets the token list of class attribute.
     * @return the token list of class attribute
     */
    @JsxGetter(@WebBrowser(FF))
    public DOMTokenList getClassList() {
        return null;
    }

    /**
     * Gets the specified attribute.
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return the value of the specified attribute, <code>null</code> if the attribute is not defined
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public String getAttributeNS(final String namespaceURI, final String localName) {
        return getDomNodeOrDie().getAttributeNS(namespaceURI, localName);
    }

    /**
     * Test for attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-ElHasAttrNS">
     * the DOM reference</a>
     *
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return <code>true</code> if the node has this attribute
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public boolean hasAttributeNS(final String namespaceURI, final String localName) {
        return getDomNodeOrDie().hasAttributeNS(namespaceURI, localName);
    }

    /**
     * Sets the specified attribute.
     * @param namespaceURI the namespace URI
     * @param qualifiedName the qualified name of the attribute to look for
     * @param value the new attribute value
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value) {
        getDomNodeOrDie().setAttributeNS(namespaceURI, qualifiedName, value);
    }

    /**
     * Removes the specified attribute.
     * @param namespaceURI the namespace URI of the attribute to remove
     * @param localName the local name of the attribute to remove
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public void removeAttributeNS(final String namespaceURI, final String localName) {
        getDomNodeOrDie().removeAttributeNS(namespaceURI, localName);
    }
}
