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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
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
    private CSSStyleDeclaration style_;

    /**
     * Applies the specified XPath expression to this node's context and returns the generated list of matching nodes.
     * @param expression a string specifying an XPath expression
     * @return list of the found elements
     */
    public HTMLCollection jsxFunction_selectNodes(final String expression) {
        final DomElement domNode = getDomNodeOrDie();
        final boolean attributeChangeSensitive = expression.contains("@");
        final String description = "Element.selectNodes('" + expression + "')";
        final HTMLCollection collection = new HTMLCollection(domNode, attributeChangeSensitive, description) {
            protected List<Object> computeElements() {
                final List<Object> list = new ArrayList<Object>(domNode.getByXPath(expression));
                return list;
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
    public Object jsxFunction_getElementsByTagName(final String tagName) {
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
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
        }
        else {
            collection = new HTMLCollection(node, false, description) {
                protected boolean isMatching(final DomNode node) {
                    return tagNameLC.equalsIgnoreCase(node.getLocalName());
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
        final String description = "Element.getElementsByTagNameNS('" + namespaceURI + "', '" + localName + "')";
        final DomElement domNode = getDomNodeOrDie();

        final String prefix;
        if (namespaceURI != null && !"*".equals("*")) {
            prefix = XmlUtil.lookupPrefix(domNode, Context.toString(namespaceURI));
        }
        else {
            prefix = null;
        }

        final HTMLCollection collection = new HTMLCollection(domNode, false, description) {
            @Override
            protected boolean isMatching(final DomNode node) {
                if (!localName.equals(node.getLocalName())) {
                    return false;
                }
                if (prefix == null) {
                    return true;
                }
                return true;
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

    /**
     * Retrieves an object that specifies the bounds of a collection of TextRectangle objects.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536433.aspx">MSDN doc</a>
     * @return an object that specifies the bounds of a collection of TextRectangle objects
     */
    public ClientRect jsxFunction_getBoundingClientRect() {
        int left = getPosX();
        int top = getPosY();

        // account for any scrolled ancestors
        Object parentNode = jsxGet_offsetParent();
        while (parentNode != null
                && (parentNode instanceof HTMLElement)
                && !(parentNode instanceof HTMLBodyElement)) {
            final HTMLElement elem = (HTMLElement) parentNode;
            left -= elem.jsxGet_scrollLeft();
            top -= elem.jsxGet_scrollTop();

            parentNode = elem.jsxGet_parentNode();
        }

        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_BOUNDING_CLIENT_RECT_OFFSET_TWO)) {
            left += 2;
            top += 2;
        }

        final ClientRect textRectangle = new ClientRect(0, left, 0, top);
        textRectangle.setParentScope(getWindow());
        textRectangle.setPrototype(getPrototype(textRectangle.getClass()));
        return textRectangle;
    }

    /**
     * Returns this element's X position.
     * @return this element's X position
     */
    public int getPosX() {
        int cumulativeOffset = 0;
        Element element = this;
        while (element != null) {
            cumulativeOffset += element.jsxGet_offsetLeft();
            if (element != this) {
                cumulativeOffset += element.jsxGet_currentStyle().getBorderLeft();
            }
            element = element.getOffsetParent();
        }
        return cumulativeOffset;
    }

    /**
     * Returns this element's Y position.
     * @return this element's Y position
     */
    public int getPosY() {
        int cumulativeOffset = 0;
        Element element = this;
        while (element != null) {
            cumulativeOffset += element.jsxGet_offsetTop();
            if (element != this) {
                cumulativeOffset += element.jsxGet_currentStyle().getBorderTop();
            }
            element = element.getOffsetParent();
        }
        return cumulativeOffset;
    }

    /**
     * Gets the offset parent or <code>null</code> if this is not an {@link HTMLElement}.
     * @return the offset parent or <code>null</code>
     */
    private HTMLElement getOffsetParent() {
        final Object offsetParent = jsxGet_offsetParent();
        if (offsetParent instanceof HTMLElement) {
            return (HTMLElement) offsetParent;
        }
        return null;
    }

    /**
     * Returns "clientLeft" attribute.
     * @return the "clientLeft" attribute
     */
    public int jsxGet_clientLeft() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CLIENT_LEFT_TOP_ZERO)) {
            return 0;
        }
        return jsxGet_currentStyle().getBorderLeft();
    }

    /**
     * Returns "clientTop" attribute.
     * @return the "clientTop" attribute
     */
    public int jsxGet_clientTop() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CLIENT_LEFT_TOP_ZERO)) {
            return 0;
        }
        return jsxGet_currentStyle().getBorderTop();
    }

    /**
     * Returns this element's <tt>offsetTop</tt>, which is the calculated top position of this
     * element relative to the <tt>offsetParent</tt>.
     *
     * @return this element's <tt>offsetTop</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534303.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    public int jsxGet_offsetTop() {
        if (this instanceof HTMLBodyElement) {
            return 0;
        }

        int top = 0;
        final HTMLElement offsetParent = getOffsetParent();

        // Add the offset for this node.
        DomNode node = getDomNodeOrDie();
        HTMLElement element = (HTMLElement) node.getScriptObject();
        top += element.jsxGet_currentStyle().getTop(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = element.jsxGet_currentStyle().getPositionWithInheritance();
        if ("absolute".equals(position)) {
            return top;
        }

        // Add the offset for the ancestor nodes.
        node = node.getParentNode();
        while (node != null && node.getScriptObject() != offsetParent) {
            if (node.getScriptObject() instanceof HTMLElement) {
                element = (HTMLElement) node.getScriptObject();
                top += element.jsxGet_currentStyle().getTop(false, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            final HTMLElement thiz = (HTMLElement) getDomNodeOrDie().getScriptObject();
            final boolean thisElementHasTopMargin = (thiz.jsxGet_currentStyle().getMarginTop() != 0);
            if (!thisElementHasTopMargin) {
                top += offsetParent.jsxGet_currentStyle().getMarginTop();
            }
            top += offsetParent.jsxGet_currentStyle().getPaddingTop();
        }

        return top;
    }

    /**
     * Returns this element's <tt>offsetParent</tt>. The <tt>offsetLeft</tt> and
     * <tt>offsetTop</tt> attributes are relative to the <tt>offsetParent</tt>.
     *
     * @return this element's <tt>offsetParent</tt>. This may be <code>undefined</code> when this node is
     * not attached or <code>null</code> for <code>body</code>.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534302.aspx">MSDN Documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_el_ref20.html">Gecko DOM Reference</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://www.w3.org/TR/REC-CSS2/box.html">Box Model</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    public Object jsxGet_offsetParent() {
        DomNode currentElement = getDomNodeOrDie();

        if (currentElement.getParentNode() == null) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OFFSET_PARENT_THROWS_NOT_ATTACHED)) {
                throw Context.reportRuntimeError("Unspecified error");
            }
            return null;
        }

        Object offsetParent = null;
        final HTMLElement htmlElement = (HTMLElement) currentElement.getScriptObject();
        final ComputedCSSStyleDeclaration style = htmlElement.jsxGet_currentStyle();
        final String position = style.getPositionWithInheritance();
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_72);
        final boolean staticPos = "static".equals(position);
        final boolean fixedPos = "fixed".equals(position);
        final boolean useTables = ((ie && (staticPos || fixedPos)) || (!ie && staticPos));

        while (currentElement != null) {

            final DomNode parentNode = currentElement.getParentNode();
            if (parentNode instanceof HtmlBody
                || (useTables && parentNode instanceof HtmlTableDataCell)
                || (useTables && parentNode instanceof HtmlTable)) {
                offsetParent = parentNode.getScriptObject();
                break;
            }

            if (parentNode != null && parentNode.getScriptObject() instanceof HTMLElement) {
                final HTMLElement parentElement = (HTMLElement) parentNode.getScriptObject();
                final ComputedCSSStyleDeclaration parentStyle = parentElement.jsxGet_currentStyle();
                final String parentPosition = parentStyle.getPositionWithInheritance();
                final boolean parentIsStatic = "static".equals(parentPosition);
                final boolean parentIsFixed = "fixed".equals(parentPosition);
                if ((ie && !parentIsStatic && !parentIsFixed) || (!ie && !parentIsStatic)) {
                    offsetParent = parentNode.getScriptObject();
                    break;
                }
            }

            currentElement = currentElement.getParentNode();
        }

        return offsetParent;
    }

    /**
     * Returns this element's <tt>offsetLeft</tt>, which is the calculated left position of this
     * element relative to the <tt>offsetParent</tt>.
     *
     * @return this element's <tt>offsetLeft</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534200.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    public int jsxGet_offsetLeft() {
        if (this instanceof HTMLBodyElement) {
            return 0;
        }

        int left = 0;
        final HTMLElement offsetParent = getOffsetParent();

        // Add the offset for this node.
        DomNode node = getDomNodeOrDie();
        HTMLElement element = (HTMLElement) node.getScriptObject();
        left += element.jsxGet_currentStyle().getLeft(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = element.jsxGet_currentStyle().getPositionWithInheritance();
        if ("absolute".equals(position)) {
            return left;
        }

        // Add the offset for the ancestor nodes.
        node = node.getParentNode();
        while (node != null && node.getScriptObject() != offsetParent) {
            if (node.getScriptObject() instanceof HTMLElement) {
                element = (HTMLElement) node.getScriptObject();
                left += element.jsxGet_currentStyle().getLeft(true, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            left += offsetParent.jsxGet_currentStyle().getMarginLeft();
            left += offsetParent.jsxGet_currentStyle().getPaddingLeft();
        }

        return left;
    }

    /**
     * Returns the current (calculated) style object for this element.
     * @return the current (calculated) style object for this element
     */
    public ComputedCSSStyleDeclaration jsxGet_currentStyle() {
        return getWindow().jsxFunction_getComputedStyle(this, null);
    }

    /**
     * Returns the style object for this element.
     * @return the style object for this element
     */
    public CSSStyleDeclaration jsxGet_style() {
        return style_;
    }

    /**
     * Returns the runtime style object for this element.
     * @return the runtime style object for this element
     */
    public CSSStyleDeclaration jsxGet_runtimeStyle() {
        return style_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);
        style_ = new CSSStyleDeclaration(this);
    }

    /**
     * Gets the first ancestor instance of {@link Element}. It is mostly identical
     * to {@link #getParent()} except that it skips non {@link Element} nodes.
     * @return the parent element
     * @see #getParent()
     */
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
}
