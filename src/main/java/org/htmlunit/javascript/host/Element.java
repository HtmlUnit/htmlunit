/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import static org.htmlunit.BrowserVersionFeatures.EVENT_SCROLL_UIEVENT;
import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;
import org.htmlunit.SgmlPage;
import org.htmlunit.corejs.javascript.BaseFunction;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.TopLevel;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.corejs.javascript.WithScope;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.css.ElementCssStyleDeclaration;
import org.htmlunit.cssparser.parser.CSSException;
import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomCDataSection;
import org.htmlunit.html.DomCharacterData;
import org.htmlunit.html.DomComment;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlElement.DisplayStyle;
import org.htmlunit.html.HtmlTemplate;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.css.CSSStyleDeclaration;
import org.htmlunit.javascript.host.dom.Attr;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.dom.DOMTokenList;
import org.htmlunit.javascript.host.dom.Node;
import org.htmlunit.javascript.host.dom.NodeList;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventHandler;
import org.htmlunit.javascript.host.event.UIEvent;
import org.htmlunit.javascript.host.html.HTMLCollection;
import org.htmlunit.javascript.host.html.HTMLElement;
import org.htmlunit.javascript.host.html.HTMLElement.ProxyDomNode;
import org.htmlunit.javascript.host.html.HTMLScriptElement;
import org.htmlunit.javascript.host.html.HTMLStyleElement;
import org.htmlunit.javascript.host.html.HTMLTemplateElement;
import org.htmlunit.util.StringUtils;
import org.xml.sax.SAXException;

/**
 * JavaScript host object for {@code Element}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element">MDN Documentation</a>
 */
@JsxClass(domClass = DomElement.class)
public class Element extends Node {

    static final String POSITION_BEFORE_BEGIN = "beforebegin";
    static final String POSITION_AFTER_BEGIN = "afterbegin";
    static final String POSITION_BEFORE_END = "beforeend";
    static final String POSITION_AFTER_END = "afterend";

    private static final Pattern CLASS_NAMES_SPLIT_PATTERN = Pattern.compile("\\s");
    private static final Pattern PRINT_NODE_PATTERN = Pattern.compile(" {2}");
    private static final Pattern PRINT_NODE_QUOTE_PATTERN = Pattern.compile("\"");

    private NamedNodeMap attributes_;
    private Map<String, HTMLCollection> elementsByTagName_; // for performance and for equality (==)
    private int scrollLeft_;
    private int scrollTop_;
    private CSSStyleDeclaration style_;

    /**
     * Creates an instance of this object.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     *
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        final Window window = getWindow();
        setParentScope(new WithScope(getTopLevelScope(getParentScope()), window.getDocument()));
        // CSSStyleDeclaration uses the parent scope
        style_ = new CSSStyleDeclaration(this, new ElementCssStyleDeclaration(getDomNodeOrDie()));

        // Convert JavaScript snippets defined in the attribute map to executable event handlers.
        // Should be called only on construction.
        final DomElement htmlElt = (DomElement) domNode;
        for (final DomAttr attr : htmlElt.getAttributesMap().values()) {
            final String eventName = StringUtils.toRootLowerCase(attr.getName());
            if (eventName.startsWith("on")) {
                createEventHandler(eventName.substring(2), attr.getValue());
            }
        }
    }

    /**
     * Creates the event handler function from the attribute value.
     *
     * @param eventName the event name (e.g. {@code onclick})
     * @param attrValue the attribute value
     */
    protected void createEventHandler(final String eventName, final String attrValue) {
        final DomElement htmlElt = getDomNodeOrDie();

        // TODO: check that it is an "allowed" event for the browser, and take care to the case
        final BaseFunction eventHandler = new EventHandler(htmlElt, eventName, attrValue);
        eventHandler.setPrototype(ScriptableObject.getClassPrototype(getParentScope(), "Function"));

        setEventHandler(eventName, eventHandler);
    }

    /**
     * Returns the tag name of this element.
     *
     * @return the tag name
     */
    @JsxGetter
    public String getTagName() {
        return getNodeName();
    }

    /**
     * Returns the attributes of this XML element.
     *
     * @return the attributes of this XML element
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/attributes">MDN Documentation</a>
     */
    @Override
    @JsxGetter
    public NamedNodeMap getAttributes() {
        if (attributes_ == null) {
            attributes_ = createAttributesObject();
        }
        return attributes_;
    }

    /**
     * Creates the JS object for the {@code attributes} property. This object will be cached.
     *
     * @return the JS object
     */
    protected NamedNodeMap createAttributesObject() {
        return new NamedNodeMap(getDomNodeOrDie());
    }

    /**
     * Returns the value of the specified attribute, or {@code null} if the attribute is not defined.
     *
     * @param attributeName the name of the attribute to retrieve
     * @return the value of the specified attribute, or {@code null} if not defined
     */
    @JsxFunction
    public String getAttribute(final String attributeName) {
        String value = getDomNodeOrDie().getAttribute(attributeName);

        if (ATTRIBUTE_NOT_DEFINED == value) {
            value = null;
        }

        return value;
    }

    /**
     * Sets the specified attribute to the given value.
     *
     * @param name the name of the attribute to set
     * @param value the value to set the attribute to
     */
    @JsxFunction
    public void setAttribute(final String name, final String value) {
        getDomNodeOrDie().setAttribute(name, value);
    }

    /**
     * Returns all descendant elements with the specified tag name.
     *
     * @param tagName the tag name to search for
     * @return all descendant elements with the specified tag name
     */
    @JsxFunction
    public HTMLCollection getElementsByTagName(final String tagName) {
        if (elementsByTagName_ == null) {
            elementsByTagName_ = new HashMap<>();
        }

        final String searchTagName;
        final boolean caseSensitive;
        final DomNode dom = getDomNodeOrNull();
        if (dom == null) {
            searchTagName = StringUtils.toRootLowerCase(tagName);
            caseSensitive = false;
        }
        else {
            final SgmlPage page = dom.getPage();
            if (page != null && page.hasCaseSensitiveTagNames()) {
                searchTagName = tagName;
                caseSensitive = true;
            }
            else {
                searchTagName = StringUtils.toRootLowerCase(tagName);
                caseSensitive = false;
            }
        }

        HTMLCollection collection = elementsByTagName_.get(searchTagName);
        if (collection != null) {
            return collection;
        }

        final DomNode node = getDomNodeOrDie();
        collection = new HTMLCollection(node, false);
        if (StringUtils.equalsChar('*', tagName)) {
            collection.setIsMatchingPredicate((Predicate<DomNode> & Serializable) nodeToMatch -> true);
        }
        else {
            collection.setIsMatchingPredicate(
                    (Predicate<DomNode> & Serializable) nodeToMatch -> {
                        if (caseSensitive) {
                            return searchTagName.equals(nodeToMatch.getNodeName());
                        }
                        return searchTagName.equalsIgnoreCase(nodeToMatch.getNodeName());
                    });
        }

        elementsByTagName_.put(tagName, collection);

        return collection;
    }

    /**
     * Retrieves an attribute node by name.
     *
     * @param name the name of the attribute to retrieve
     * @return the {@link Attr} node with the specified name, or {@code null} if there is no such attribute
     */
    @JsxFunction
    public HtmlUnitScriptable getAttributeNode(final String name) {
        final Map<String, DomAttr> attributes = getDomNodeOrDie().getAttributesMap();
        for (final DomAttr attr : attributes.values()) {
            if (attr.getName().equals(name)) {
                return attr.getScriptableObject();
            }
        }
        return null;
    }

    /**
     * Returns a live {@link HTMLCollection} of elements with the given tag name belonging to the given namespace.
     *
     * @param namespaceURI the namespace URI of elements to look for
     * @param localName the local name of elements to look for, or {@code "*"} to match all elements
     * @return a live {@link HTMLCollection} of found elements in document order
     */
    @JsxFunction
    public HTMLCollection getElementsByTagNameNS(final Object namespaceURI, final String localName) {
        final HTMLCollection elements = new HTMLCollection(getDomNodeOrDie(), false);
        elements.setIsMatchingPredicate(
                (Predicate<DomNode> & Serializable)
                node -> ("*".equals(namespaceURI) || Objects.equals(namespaceURI, node.getNamespaceURI()))
                                && ("*".equals(localName) || Objects.equals(localName, node.getLocalName())));
        return elements;
    }

    /**
     * Returns {@code true} when an attribute with the given name is specified on this element or has a default value.
     *
     * @param name the name of the attribute to look for
     * @return {@code true} if the attribute exists or has a default value
     */
    @JsxFunction
    public boolean hasAttribute(final String name) {
        return getDomNodeOrDie().hasAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction
    public boolean hasAttributes() {
        return super.hasAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getDomNodeOrDie() {
        return (DomElement) super.getDomNodeOrDie();
    }

    /**
     * Removes the attribute with the specified name.
     *
     * @param name the name of the attribute to remove
     */
    @JsxFunction
    public void removeAttribute(final String name) {
        getDomNodeOrDie().removeAttribute(name);
    }

    /**
     * Returns the bounding rectangle of this element relative to the viewport.
     *
     * @return a {@link DOMRect} object describing the element's size and position
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/getBoundingClientRect">MDN Documentation</a>
     */
    @JsxFunction
    public DOMRect getBoundingClientRect() {
        final DOMRect textRectangle = new DOMRect(1, 1, 0, 0);
        textRectangle.setParentScope(getTopLevelScope(getParentScope()));
        textRectangle.setPrototype(getPrototype(textRectangle.getClass()));
        return textRectangle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public int getChildElementCount() {
        return getDomNodeOrDie().getChildElementCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Element getFirstElementChild() {
        return super.getFirstElementChild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Element getLastElementChild() {
        return super.getLastElementChild();
    }

    /**
     * Returns the next sibling that is an element.
     *
     * @return the next element sibling, or {@code null} if none
     */
    @JsxGetter
    public Element getNextElementSibling() {
        final DomElement child = getDomNodeOrDie().getNextElementSibling();
        if (child != null) {
            return child.getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the previous sibling that is an element.
     *
     * @return the previous element sibling, or {@code null} if none
     */
    @JsxGetter
    public Element getPreviousElementSibling() {
        final DomElement child = getDomNodeOrDie().getPreviousElementSibling();
        if (child != null) {
            return child.getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the first ancestor that is an {@link Element}. Skips non-{@link Element} nodes.
     *
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
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public HTMLCollection getChildren() {
        return super.getChildren();
    }

    /**
     * Returns the token list of the {@code class} attribute.
     *
     * @return the token list of the {@code class} attribute
     */
    @JsxGetter
    public DOMTokenList getClassList() {
        return new DOMTokenList(this, "class");
    }

    /**
     * Returns the value of the specified attribute in the given namespace,
     * or {@code null} if the attribute is not defined.
     *
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to retrieve
     * @return the attribute value, or {@code null} if not found
     */
    @JsxFunction
    public String getAttributeNS(final String namespaceURI, final String localName) {
        final String value = getDomNodeOrDie().getAttributeNS(namespaceURI, localName);
        if (ATTRIBUTE_NOT_DEFINED == value) {
            return null;
        }
        return value;
    }

    /**
     * Returns {@code true} if the element has an attribute with the given namespace URI and local name.
     *
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return {@code true} if the attribute exists
     */
    @JsxFunction
    public boolean hasAttributeNS(final String namespaceURI, final String localName) {
        return getDomNodeOrDie().hasAttributeNS(namespaceURI, localName);
    }

    /**
     * Sets the attribute with the given namespace URI and qualified name to the given value.
     *
     * @param namespaceURI the namespace URI
     * @param qualifiedName the qualified name of the attribute
     * @param value the new attribute value
     */
    @JsxFunction
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value) {
        getDomNodeOrDie().setAttributeNS(namespaceURI, qualifiedName, value);
    }

    /**
     * Removes the attribute with the given namespace URI and local name.
     *
     * @param namespaceURI the namespace URI of the attribute to remove
     * @param localName the local name of the attribute to remove
     */
    @JsxFunction
    public void removeAttributeNS(final String namespaceURI, final String localName) {
        getDomNodeOrDie().removeAttributeNS(namespaceURI, localName);
    }

    /**
     * Sets the attribute node for the specified attribute, replacing the existing node if present.
     *
     * @param newAtt the attribute node to set
     * @return the replaced attribute node, if any
     */
    @JsxFunction
    public Attr setAttributeNode(final Attr newAtt) {
        final String name = newAtt.getName();

        final NamedNodeMap nodes = getAttributes();
        final Attr replacedAtt = (Attr) nodes.getNamedItemWithoutSytheticClassAttr(name);
        if (replacedAtt != null) {
            replacedAtt.detachFromParent();
        }

        final DomAttr newDomAttr = newAtt.getDomNodeOrDie();
        getDomNodeOrDie().setAttributeNode(newDomAttr);
        return replacedAtt;
    }

    /**
     * Returns a static {@link NodeList} of all descendant elements matching the given CSS selector(s).
     *
     * @param selectors the CSS selector(s)
     * @return the static node list of matching elements
     */
    @JsxFunction
    public NodeList querySelectorAll(final String selectors) {
        try {
            return NodeList.staticNodeList(getParentScope(), getDomNodeOrDie().querySelectorAll(selectors));
        }
        catch (final CSSException e) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "An invalid or illegal selector was specified (selector: '"
                            + selectors + "' error: " + e.getMessage() + ").",
                    DOMException.SYNTAX_ERR);
        }
    }

    /**
     * Returns the first descendant element that matches the specified CSS selector,
     * or {@code null} if no matches are found.
     *
     * @param selectors the CSS selector(s)
     * @return the first matching element, or {@code null}
     */
    @JsxFunction
    public Node querySelector(final String selectors) {
        try {
            final DomNode node = getDomNodeOrDie().querySelector(selectors);
            if (node != null) {
                return node.getScriptableObject();
            }
            return null;
        }
        catch (final CSSException e) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "An invalid or illegal selector was specified (selector: '"
                            + selectors + "' error: " + e.getMessage() + ").",
                    DOMException.SYNTAX_ERR);
        }
    }

    /**
     * Returns the value of the {@code class} attribute.
     *
     * @return the class name
     */
    @JsxGetter(propertyName = "className")
    public String getClassName_js() {
        return getDomNodeOrDie().getAttributeDirect("class");
    }

    /**
     * Sets the {@code class} attribute for this element.
     *
     * @param className the new class name
     */
    @JsxSetter(propertyName = "className")
    public void setClassName_js(final String className) {
        getDomNodeOrDie().setAttribute("class", className);
    }

    /**
     * Returns the {@code clientHeight} property.
     *
     * @return the {@code clientHeight} property
     */
    @JsxGetter
    public int getClientHeight() {
        final ComputedCssStyleDeclaration style = getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
        return style.getCalculatedHeight(false, true);
    }

    /**
     * Returns the {@code clientWidth} property.
     *
     * @return the {@code clientWidth} property
     */
    @JsxGetter
    public int getClientWidth() {
        final ComputedCssStyleDeclaration style = getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
        return style.getCalculatedWidth(false, true);
    }

    /**
     * Returns the {@code clientLeft} property.
     *
     * @return the {@code clientLeft} property
     */
    @JsxGetter
    public int getClientLeft() {
        final ComputedCssStyleDeclaration style = getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
        return style.getBorderLeftValue();
    }

    /**
     * Returns the {@code clientTop} property.
     *
     * @return the {@code clientTop} property
     */
    @JsxGetter
    public int getClientTop() {
        final ComputedCssStyleDeclaration style = getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
        return style.getBorderTopValue();
    }

    /**
     * Returns the attribute node with the given namespace URI and local name.
     *
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to retrieve
     * @return the specified attribute node, or {@code null} if not found
     */
    @JsxFunction
    public HtmlUnitScriptable getAttributeNodeNS(final String namespaceURI, final String localName) {
        return getDomNodeOrDie().getAttributeNodeNS(namespaceURI, localName).getScriptableObject();
    }

    /**
     * Returns all descendant elements that have the specified class name(s).
     *
     * @param className the class name(s) to search for (space-separated)
     * @return all descendant elements with the specified class name(s)
     */
    @JsxFunction
    public HTMLCollection getElementsByClassName(final String className) {
        final DomElement elt = getDomNodeOrDie();
        final String[] classNames = CLASS_NAMES_SPLIT_PATTERN.split(className, 0);

        final HTMLCollection elements = new HTMLCollection(elt, true);

        elements.setIsMatchingPredicate(
                (Predicate<DomNode> & Serializable)
                node -> {
                    if (!(node instanceof HtmlElement)) {
                        return false;
                    }
                    String classAttribute = ((HtmlElement) node).getAttributeDirect("class");
                    if (ATTRIBUTE_NOT_DEFINED == classAttribute) {
                        return false; // probably better performance as most elements won't have a class attribute
                    }

                    classAttribute = " " + classAttribute + " ";
                    for (final String aClassName : classNames) {
                        if (!classAttribute.contains(" " + aClassName + " ")) {
                            return false;
                        }
                    }
                    return true;
                });

        return elements;
    }

    /**
     * Returns a collection of {@link DOMRect} objects that describe the layout of the element's
     * content on screen. Each rectangle represents one line of the element's content.
     *
     * @return a {@link DOMRectList} describing the element's line boxes
     */
    @JsxFunction
    public DOMRectList getClientRects() {
        final TopLevel topScope = getTopLevelScope(getParentScope());
        final DOMRectList rectList = new DOMRectList();
        rectList.setParentScope(topScope);
        rectList.setPrototype(getPrototype(rectList.getClass()));

        if (!isDisplayNone() && getDomNodeOrDie().isAttachedToPage()) {
            final DOMRect rect = new DOMRect(0, 0, 1, 1);
            rect.setParentScope(topScope);
            rect.setPrototype(getPrototype(rect.getClass()));
            rectList.add(rect);
        }

        return rectList;
    }

    /**
     * Returns the attribute names of this element as an array of strings.
     * Returns an empty array if the element has no attributes.
     *
     * @return the attribute names as an array
     */
    @JsxFunction
    public Scriptable getAttributeNames() {
        final org.w3c.dom.NamedNodeMap attributes = getDomNodeOrDie().getAttributes();

        if (attributes.getLength() == 0) {
            return JavaScriptEngine.newArray(getParentScope(), 0);
        }

        final ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            res.add(attributes.item(i).getNodeName());
        }

        return JavaScriptEngine.newArray(getParentScope(), res.toArray());
    }

    /**
     * Returns whether the {@code display} style of this element or any ancestor is {@code none}.
     *
     * @return {@code true} if the element or any ancestor has {@code display: none}
     */
    protected final boolean isDisplayNone() {
        Element element = this;
        while (element != null) {
            final CSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
            final String display = style.getDisplay();
            if (DisplayStyle.NONE.value().equals(display)) {
                return true;
            }
            element = element.getParentElement();
        }
        return false;
    }

    /**
     * Inserts the given element at the specified position relative to this element.
     *
     * @param where specifies where to insert the element; one of {@code beforebegin},
     *        {@code afterbegin}, {@code beforeend}, or {@code afterend} (case-insensitive)
     * @param insertedElement the element to insert
     * @return the inserted element
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/insertAdjacentElement">MDN Documentation</a>
     */
    @JsxFunction
    public Node insertAdjacentElement(final String where, final Object insertedElement) {
        if (insertedElement instanceof Node insertedElementNode) {
            final DomNode childNode = insertedElementNode.getDomNodeOrDie();
            final Object[] values = getInsertAdjacentLocation(where);
            final DomNode node = (DomNode) values[0];
            final boolean append = ((Boolean) values[1]).booleanValue();

            if (append) {
                node.appendChild(childNode);
            }
            else {
                node.insertBefore(childNode);
            }
            return insertedElementNode;
        }
        throw JavaScriptEngine.reportRuntimeError("Passed object is not an element: " + insertedElement);
    }

    /**
     * Inserts the given text at the specified position relative to this element.
     *
     * @param where specifies where to insert the text; one of {@code beforebegin},
     *        {@code afterbegin}, {@code beforeend}, or {@code afterend} (case-insensitive)
     * @param text the text to insert
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/insertAdjacentText">MDN Documentation</a>
     */
    @JsxFunction
    public void insertAdjacentText(final String where, final String text) {
        final Object[] values = getInsertAdjacentLocation(where);
        final DomNode node = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        final DomText domText = new DomText(node.getPage(), text);
        // add the new nodes
        if (append) {
            node.appendChild(domText);
        }
        else {
            node.insertBefore(domText);
        }
    }

    /**
     * Returns the target node and insertion mode for the given adjacent position string.
     * Used by {@link #insertAdjacentHTML(String, String)},
     * {@link #insertAdjacentElement(String, Object)}, and
     * {@link #insertAdjacentText(String, String)}.
     *
     * @param where specifies where to insert; one of {@code beforebegin},
     *        {@code afterbegin}, {@code beforeend}, or {@code afterend} (case-insensitive)
     * @return an array of [{@link DomNode} parentNode, {@link Boolean} append]
     */
    private Object[] getInsertAdjacentLocation(final String where) {
        final DomNode currentNode = getDomNodeOrDie();
        final DomNode node;
        final boolean append;

        // compute the where and how the new nodes should be added
        if (POSITION_AFTER_BEGIN.equalsIgnoreCase(where)) {
            if (currentNode.getFirstChild() == null) {
                // new nodes should append to the children of current node
                node = currentNode;
                append = true;
            }
            else {
                // new nodes should be inserted before first child
                node = currentNode.getFirstChild();
                append = false;
            }
        }
        else if (POSITION_BEFORE_BEGIN.equalsIgnoreCase(where)) {
            // new nodes should be inserted before current node
            node = currentNode;
            append = false;
        }
        else if (POSITION_BEFORE_END.equalsIgnoreCase(where)) {
            // new nodes should append to the children of current node
            node = currentNode;
            append = true;
        }
        else if (POSITION_AFTER_END.equalsIgnoreCase(where)) {
            if (currentNode.getNextSibling() == null) {
                // new nodes should append to the children of parent node
                node = currentNode.getParentNode();
                append = true;
            }
            else {
                // new nodes should be inserted before current node's next sibling
                node = currentNode.getNextSibling();
                append = false;
            }
        }
        else {
            throw JavaScriptEngine.reportRuntimeError("Illegal position value: \"" + where + "\"");
        }

        if (append) {
            return new Object[] {node, Boolean.TRUE};
        }
        return new Object[] {node, Boolean.FALSE};
    }

    /**
     * Parses the given text as HTML or XML and inserts the resulting nodes at the specified position.
     *
     * @param position specifies where to insert the nodes; one of {@code beforebegin},
     *        {@code afterbegin}, {@code beforeend}, or {@code afterend} (case-insensitive)
     * @param text the HTML or XML text to parse and insert
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/insertAdjacentHTML">MDN Documentation</a>
     */
    @JsxFunction
    public void insertAdjacentHTML(final String position, final String text) {
        final Object[] values = getInsertAdjacentLocation(position);
        final DomNode domNode = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        // add the new nodes
        final DomNode proxyDomNode = new ProxyDomNode(domNode.getPage(), domNode, append);
        parseHtmlSnippet(proxyDomNode, text);
    }

    /**
     * Moves a given node inside this element as a direct child, before a given reference node.
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     */
    @JsxFunction({CHROME, EDGE, FF})
    public static void moveBefore(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        Node.moveBefore(context, scope, thisObj, args, function);
    }

    /**
     * Parses the specified HTML source code and appends the resulting content at the specified target location.
     *
     * @param target the node indicating the position at which the parsed content should be placed
     * @param source the HTML code to parse
     */
    private static void parseHtmlSnippet(final DomNode target, final String source) {
        try {
            target.parseHtmlSnippet(source);
        }
        catch (final IOException | SAXException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
            throw JavaScriptEngine.reportRuntimeError("Unexpected exception occurred while parsing HTML snippet: "
                    + e.getMessage());
        }
    }

    /**
     * Returns the contents of this node as HTML, ignoring shadow DOM parameters as shadow DOM is not supported.
     *
     * @return the contents of this node as HTML
     */
    @JsxFunction
    public String getHTML() {
        // ignore the params because we have no shadow dom support so far
        return getInnerHTML();
    }

    /**
     * Returns the {@code innerHTML} of this element.
     *
     * @return the contents of this node as HTML
     */
    @JsxGetter
    public String getInnerHTML() {
        try {
            DomNode domNode = getDomNodeOrDie();
            if (this instanceof HTMLTemplateElement) {
                domNode = ((HtmlTemplate) getDomNodeOrDie()).getContent();
            }
            return getInnerHTML(domNode);
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.typeError(e.getMessage());
        }
    }

    /**
     * Replaces all child elements of this element with the supplied HTML value.
     *
     * @param value the new HTML content for this element
     */
    @JsxSetter
    public void setInnerHTML(final Object value) {
        final DomElement domNode;
        try {
            domNode = getDomNodeOrDie();
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.typeError(e.getMessage());
        }

        String html = null;
        if (value != null) {
            html = JavaScriptEngine.toString(value);
            if (StringUtils.isEmptyString(html)) {
                html = null;
            }
        }

        try {
            domNode.setInnerHtml(html);
        }
        catch (final IOException | SAXException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
            throw JavaScriptEngine.reportRuntimeError("Unexpected exception occurred while parsing HTML snippet: "
                    + e.getMessage());
        }
    }

    /**
     * Helper for {@code getInnerHTML}, reusable by {@code HTMLTemplateElement}.
     *
     * @param domNode the node to serialize
     * @return the contents of this node as HTML
     */
    protected String getInnerHTML(final DomNode domNode) {
        final StringBuilder buf = new StringBuilder();

        final String tagName = getTagName();
        boolean isPlain = "SCRIPT".equals(tagName);

        isPlain = isPlain || "STYLE".equals(tagName);

        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, domNode, !isPlain);
        return buf.toString();
    }

    /**
     * Returns the {@code outerHTML} of this element, including the element's own tags.
     *
     * @return the contents of this node as HTML, including the opening and closing tags
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/outerHTML">MDN Documentation</a>
     */
    @JsxGetter
    public String getOuterHTML() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printNode(buf, getDomNodeOrDie(), true);
        return buf.toString();
    }

    /**
     * Replaces this element (including all child elements) with the supplied HTML value.
     *
     * @param value the new HTML to replace this element
     */
    @JsxSetter
    public void setOuterHTML(final Object value) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode parent = domNode.getParentNode();
        if (null == parent) {
            return;
        }

        if (value == null) {
            domNode.remove();
            return;
        }

        final String valueStr = JavaScriptEngine.toString(value);
        if (valueStr.isEmpty()) {
            domNode.remove();
            return;
        }

        final DomNode nextSibling = domNode.getNextSibling();
        domNode.remove();

        final DomNode target;
        final boolean append;
        if (nextSibling != null) {
            target = nextSibling;
            append = false;
        }
        else {
            target = parent;
            append = true;
        }

        final DomNode proxyDomNode = new ProxyDomNode(target.getPage(), target, append);
        parseHtmlSnippet(proxyDomNode, valueStr);
    }

    /**
     * Serializes the children of the given node to the provided builder.
     *
     * @param builder the builder to write to
     * @param node the node whose children are to be serialized
     * @param html whether to use HTML serialization
     */
    protected final void printChildren(final StringBuilder builder, final DomNode node, final boolean html) {
        if (node instanceof HtmlTemplate template) {

            for (final DomNode child : template.getContent().getChildren()) {
                printNode(builder, child, html);
            }
            return;
        }

        for (final DomNode child : node.getChildren()) {
            printNode(builder, child, html);
        }
    }

    protected void printNode(final StringBuilder builder, final DomNode node, final boolean html) {
        if (node instanceof DomComment) {
            if (html) {
                // Remove whitespace sequences.
                final String s = PRINT_NODE_PATTERN.matcher(node.getNodeValue()).replaceAll(" ");
                builder.append("<!--").append(s).append("-->");
            }
        }
        else if (node instanceof DomCDataSection) {
            builder.append("<![CDATA[").append(node.getNodeValue()).append("]]>");
        }
        else if (node instanceof DomCharacterData) {
            // Remove whitespace sequences, possibly escape XML characters.
            String s = node.getNodeValue();
            if (html) {
                s = StringUtils.escapeXmlChars(s);
            }
            builder.append(s);
        }
        else if (html) {
            final DomElement element = (DomElement) node;
            final Element scriptObject = node.getScriptableObject();
            final String tag = element.getTagName();

            Element htmlElement = null;
            if (scriptObject instanceof HTMLElement) {
                htmlElement = scriptObject;
            }
            builder.append('<').append(tag);
            for (final DomAttr attr : element.getAttributesMap().values()) {
                if (!attr.getSpecified()) {
                    continue;
                }

                final String name = attr.getName();
                final String value = PRINT_NODE_QUOTE_PATTERN.matcher(attr.getValue()).replaceAll("&quot;");
                builder.append(' ').append(name).append("=\"").append(value).append('\"');
            }
            builder.append('>');
            // Add the children.
            final boolean isHtml = !(scriptObject instanceof HTMLScriptElement)
                    && !(scriptObject instanceof HTMLStyleElement);
            printChildren(builder, node, isHtml);
            if (null == htmlElement || !htmlElement.isEndTagForbidden()) {
                builder.append("</").append(tag).append('>');
            }
        }
        else {
            if (node instanceof HtmlElement element) {
                if (StringUtils.equalsChar('p', element.getTagName())) {
                    int i = builder.length() - 1;
                    while (i >= 0 && Character.isWhitespace(builder.charAt(i))) {
                        i--;
                    }
                    builder.setLength(i + 1);
                    builder.append('\n');
                }
                if (!"script".equals(element.getTagName())) {
                    printChildren(builder, node, html);
                }
            }
        }
    }

    /**
     * Returns whether the end tag is forbidden for this element.
     *
     * @return whether the end tag is forbidden
     * @see <a href="http://www.w3.org/TR/html4/index/elements.html">HTML 4 specs</a>
     */
    protected boolean isEndTagForbidden() {
        return false;
    }

    /**
     * Returns the element ID.
     *
     * @return the ID of this element
     */
    @JsxGetter
    public String getId() {
        return getDomNodeOrDie().getId();
    }

    /**
     * Sets the ID of this element.
     *
     * @param newId the new ID value for this element
     */
    @JsxSetter
    public void setId(final String newId) {
        getDomNodeOrDie().setId(newId);
    }

    /**
     * Removes the specified attribute node from this element.
     *
     * @param attribute the attribute node to remove
     */
    @JsxFunction
    public void removeAttributeNode(final Attr attribute) {
        final String name = attribute.getName();
        final String namespaceUri = attribute.getNamespaceURI();
        removeAttributeNS(namespaceUri, name);
    }

    /**
     * Returns the {@code scrollTop} value for this element.
     *
     * @return the {@code scrollTop} value
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/scrollTop">MDN Documentation</a>
     */
    @JsxGetter
    public int getScrollTop() {
        // It's easier to perform these checks and adjustments in the getter, rather than in the setter,
        // because modifying the CSS style of the element is supposed to affect the attribute value.
        if (scrollTop_ < 0) {
            scrollTop_ = 0;
        }
        else if (scrollTop_ > 0) {
            final ComputedCssStyleDeclaration style =
                    getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
            if (!style.isScrollable(false)) {
                scrollTop_ = 0;
            }
        }
        return scrollTop_;
    }

    /**
     * Sets the {@code scrollTop} value for this element.
     *
     * @param scroll the new {@code scrollTop} value
     */
    @JsxSetter
    public void setScrollTop(final int scroll) {
        scrollTop_ = scroll;
    }

    /**
     * Returns the {@code scrollLeft} value for this element.
     *
     * @return the {@code scrollLeft} value
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/scrollLeft">MDN Documentation</a>
     */
    @JsxGetter
    public int getScrollLeft() {
        // It's easier to perform these checks and adjustments in the getter, rather than in the setter,
        // because modifying the CSS style of the element is supposed to affect the attribute value.
        if (scrollLeft_ < 0) {
            scrollLeft_ = 0;
        }
        else if (scrollLeft_ > 0) {
            final ComputedCssStyleDeclaration style =
                    getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
            if (!style.isScrollable(true)) {
                scrollLeft_ = 0;
            }
        }
        return scrollLeft_;
    }

    /**
     * Sets the {@code scrollLeft} value for this element.
     *
     * @param scroll the new {@code scrollLeft} value
     */
    @JsxSetter
    public void setScrollLeft(final int scroll) {
        scrollLeft_ = scroll;
    }

    /**
     * Returns the {@code scrollHeight} for this element.
     * Currently returns the same value as {@link #getClientHeight()}.
     *
     * @return the scroll height
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/scrollHeight">MDN Documentation</a>
     */
    @JsxGetter
    public int getScrollHeight() {
        return getClientHeight();
    }

    /**
     * Returns the {@code scrollWidth} for this element.
     * Currently returns the same value as {@link #getClientWidth()}.
     *
     * @return the scroll width
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/scrollWidth">MDN Documentation</a>
     */
    @JsxGetter
    public int getScrollWidth() {
        return getClientWidth();
    }

    /**
     * Returns the style object for this element.
     *
     * @return the style object for this element
     */
    protected CSSStyleDeclaration getStyle() {
        return style_;
    }

    /**
     * Sets the CSS text for this element's inline style.
     *
     * @param style the new CSS style text
     */
    protected void setStyle(final String style) {
        getStyle().setCssText(style);
    }

    /**
     * Scrolls to a particular set of coordinates inside this element.
     *
     * @param x the horizontal pixel value to scroll to
     * @param y the vertical pixel value to scroll to
     */
    @JsxFunction
    public void scroll(final Scriptable x, final Scriptable y) {
        scrollTo(x, y);
    }

    /**
     * Scrolls the element by the given amount.
     *
     * @param x the horizontal pixel value to scroll by
     * @param y the vertical pixel value to scroll by
     */
    @JsxFunction
    public void scrollBy(final Scriptable x, final Scriptable y) {
        int xOff = 0;
        int yOff = 0;
        if (y != null) {
            xOff = JavaScriptEngine.toInt32(x);
            yOff = JavaScriptEngine.toInt32(y);
        }
        else {
            if (!(x instanceof NativeObject)) {
                throw JavaScriptEngine.typeError("eee");
            }
            if (x.has("left", x)) {
                xOff = JavaScriptEngine.toInt32(x.get("left", x));
            }
            if (x.has("top", x)) {
                yOff = JavaScriptEngine.toInt32(x.get("top", x));
            }
        }

        setScrollLeft(getScrollLeft() + xOff);
        setScrollTop(getScrollTop() + yOff);

        fireScrollEvent(this);
    }

    private void fireScrollEvent(final Node node) {
        final Event event;
        if (getBrowserVersion().hasFeature(EVENT_SCROLL_UIEVENT)) {
            event = new UIEvent(node, Event.TYPE_SCROLL);
        }
        else {
            event = new Event(node, Event.TYPE_SCROLL);
            event.setCancelable(false);
        }
        event.setBubbles(false);
        node.fireEvent(event);
    }

    private void fireScrollEvent(final Window window) {
        final Event event;
        if (getBrowserVersion().hasFeature(EVENT_SCROLL_UIEVENT)) {
            event = new UIEvent(window.getDocument(), Event.TYPE_SCROLL);
        }
        else {
            event = new Event(window.getDocument(), Event.TYPE_SCROLL);
            event.setCancelable(false);
        }
        window.fireEvent(event);
    }

    /**
     * Scrolls to a particular set of coordinates inside this element.
     *
     * @param x the horizontal pixel value to scroll to
     * @param y the vertical pixel value to scroll to
     */
    @JsxFunction
    public void scrollTo(final Scriptable x, final Scriptable y) {
        int xOff;
        int yOff;
        if (y != null) {
            xOff = JavaScriptEngine.toInt32(x);
            yOff = JavaScriptEngine.toInt32(y);
        }
        else {
            if (!(x instanceof NativeObject)) {
                throw JavaScriptEngine.typeError("eee");
            }

            xOff = getScrollLeft();
            yOff = getScrollTop();
            if (x.has("left", x)) {
                xOff = JavaScriptEngine.toInt32(x.get("left", x));
            }
            if (x.has("top", x)) {
                yOff = JavaScriptEngine.toInt32(x.get("top", x));
            }
        }

        setScrollLeft(xOff);
        setScrollTop(yOff);

        fireScrollEvent(this);
    }

    /**
     * Scrolls the element into the visible area of the browser window.
     * This implementation triggers the scroll event but does not actually scroll
     * (headless environment).
     */
    @JsxFunction
    public void scrollIntoView() {
        // do nothing at the moment, only trigger the scroll event

        // we do not really handle scrollable elements (we are headless)
        // we trigger the event for the whole parent tree (to inform all)
        Node parent = getParent();
        while (parent != null) {
            if (parent instanceof HTMLElement) {
                fireScrollEvent(parent);
            }

            parent = parent.getParent();
        }
        fireScrollEvent(getWindow());
    }

    /**
     * Scrolls the element into the visible area if needed.
     * This is a no-op implementation.
     */
    @JsxFunction({CHROME, EDGE})
    public void scrollIntoViewIfNeeded() {
        /* do nothing at the moment */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public String getPrefix() {
        return super.getPrefix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public String getLocalName() {
        return super.getLocalName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public String getNamespaceURI() {
        return super.getNamespaceURI();
    }

    /**
     * Returns the {@code onbeforecopy} event handler for this element.
     *
     * @return the {@code onbeforecopy} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforecopy() {
        return getEventHandler(Event.TYPE_BEFORECOPY);
    }

    /**
     * Sets the {@code onbeforecopy} event handler for this element.
     *
     * @param onbeforecopy the {@code onbeforecopy} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforecopy(final Object onbeforecopy) {
        setEventHandler(Event.TYPE_BEFORECOPY, onbeforecopy);
    }

    /**
     * Returns the {@code onbeforecut} event handler for this element.
     *
     * @return the {@code onbeforecut} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforecut() {
        return getEventHandler(Event.TYPE_BEFORECUT);
    }

    /**
     * Sets the {@code onbeforecut} event handler for this element.
     *
     * @param onbeforecut the {@code onbeforecut} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforecut(final Object onbeforecut) {
        setEventHandler(Event.TYPE_BEFORECUT, onbeforecut);
    }

    /**
     * Returns the {@code onbeforepaste} event handler for this element.
     *
     * @return the {@code onbeforepaste} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforepaste() {
        return getEventHandler(Event.TYPE_BEFOREPASTE);
    }

    /**
     * Sets the {@code onbeforepaste} event handler for this element.
     *
     * @param onbeforepaste the {@code onbeforepaste} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforepaste(final Object onbeforepaste) {
        setEventHandler(Event.TYPE_BEFOREPASTE, onbeforepaste);
    }

    /**
     * Returns the {@code onsearch} event handler for this element.
     *
     * @return the {@code onsearch} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnsearch() {
        return getEventHandler(Event.TYPE_SEARCH);
    }

    /**
     * Sets the {@code onsearch} event handler for this element.
     *
     * @param onsearch the {@code onsearch} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnsearch(final Object onsearch) {
        setEventHandler(Event.TYPE_SEARCH, onsearch);
    }

    /**
     * Returns the {@code onwebkitfullscreenchange} event handler for this element.
     *
     * @return the {@code onwebkitfullscreenchange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitfullscreenchange() {
        return getEventHandler(Event.TYPE_WEBKITFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onwebkitfullscreenchange} event handler for this element.
     *
     * @param onwebkitfullscreenchange the {@code onwebkitfullscreenchange} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkitfullscreenchange(final Object onwebkitfullscreenchange) {
        setEventHandler(Event.TYPE_WEBKITFULLSCREENCHANGE, onwebkitfullscreenchange);
    }

    /**
     * Returns the {@code onwebkitfullscreenerror} event handler for this element.
     *
     * @return the {@code onwebkitfullscreenerror} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitfullscreenerror() {
        return getEventHandler(Event.TYPE_WEBKITFULLSCREENERROR);
    }

    /**
     * Sets the {@code onwebkitfullscreenerror} event handler for this element.
     *
     * @param onwebkitfullscreenerror the {@code onwebkitfullscreenerror} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkitfullscreenerror(final Object onwebkitfullscreenerror) {
        setEventHandler(Event.TYPE_WEBKITFULLSCREENERROR, onwebkitfullscreenerror);
    }

    /**
     * Returns the {@code onwheel} event handler for this element.
     *
     * @return the {@code onwheel} event handler for this element
     */
    public Function getOnwheel() {
        return getEventHandler(Event.TYPE_WHEEL);
    }

    /**
     * Sets the {@code onwheel} event handler for this element.
     *
     * @param onwheel the {@code onwheel} event handler for this element
     */
    public void setOnwheel(final Object onwheel) {
        setEventHandler(Event.TYPE_WHEEL, onwheel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction
    public void remove() {
        super.remove();
    }

    /**
     * Sets mouse capture to the object that belongs to the current document.
     * This is a mock implementation.
     *
     * @param retargetToElement if {@code true}, all events are targeted directly to this element;
     *        if {@code false}, events can also fire at descendants of this element
     */
    @JsxFunction({FF, FF_ESR})
    public void setCapture(final boolean retargetToElement) {
        // empty
    }

    /**
     * Releases mouse capture from the object in the current document.
     * This is a mock implementation.
     */
    @JsxFunction({FF, FF_ESR})
    public void releaseCapture() {
        // nothing to do
    }

    /**
     * Inserts a set of {@link Node} or {@code DOMString} objects in the children list of this node's parent,
     * just before this node.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void before(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        Node.before(context, thisObj, args, function);
    }

    /**
     * Inserts a set of {@link Node} or {@code DOMString} objects in the children list of this node's parent,
     * just after this node.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void after(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        Node.after(context, thisObj, args, function);
    }

    /**
     * Replaces this node with a set of {@link Node} or {@code DOMString} objects.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void replaceWith(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        Node.replaceWith(context, thisObj, args, function);
    }

    /**
     * Returns {@code true} if the element would be selected by the specified CSS selector string.
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return {@code true} if the element matches the selector
     */
    @JsxFunction
    public static boolean matches(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof Element)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        final String selectorString = (String) args[0];
        try {
            final DomNode domNode = ((Element) thisObj).getDomNodeOrNull();
            return domNode != null && ((DomElement) domNode).matches(selectorString);
        }
        catch (final CSSException e) {
            throw JavaScriptEngine.asJavaScriptException(
                    (HtmlUnitScriptable) getTopLevelScope(scope).getGlobalThis(),
                    "An invalid or illegal selector was specified (selector: '"
                            + selectorString + "' error: " + e.getMessage() + ").",
                    DOMException.SYNTAX_ERR);
        }
    }

    /**
     * Returns {@code true} if the element would be selected by the specified CSS selector string.
     * Firefox-specific alias for {@link #matches}.
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return {@code true} if the element matches the selector
     */
    @JsxFunction({FF, FF_ESR})
    public static boolean mozMatchesSelector(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        return matches(context, scope, thisObj, args, function);
    }

    /**
     * Returns {@code true} if the element would be selected by the specified CSS selector string.
     * WebKit-specific alias for {@link #matches}.
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return {@code true} if the element matches the selector
     */
    @JsxFunction
    public static boolean webkitMatchesSelector(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        return matches(context, scope, thisObj, args, function);
    }

    /**
     * Traverses this element and its ancestors until it finds a node that matches the specified CSS selector.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return the closest matching ancestor element, or {@code null} if none found
     */
    @JsxFunction
    public static Element closest(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof Element)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        final String selectorString = (String) args[0];
        try {
            final DomNode domNode = ((Element) thisObj).getDomNodeOrNull();
            if (domNode == null) {
                return null;
            }
            final DomElement elem = domNode.closest(selectorString);
            if (elem == null) {
                return null;
            }
            return elem.getScriptableObject();
        }
        catch (final CSSException e) {
            throw JavaScriptEngine.syntaxError(
                    "An invalid or illegal selector was specified (selector: '"
                    + selectorString + "' error: " + e.getMessage() + ").");
        }
    }

    /**
     * Toggles a Boolean attribute on this element. If {@code force} is {@code true}, adds
     * the attribute. If {@code force} is {@code false}, removes the attribute.
     * If {@code force} is not specified, the attribute is toggled.
     *
     * @param name the name of the attribute to toggle; automatically converted to lower-case for HTML elements
     * @param force if {@code true}, adds the attribute; if {@code false}, removes it
     * @return {@code true} if the attribute is present after the call, {@code false} otherwise
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/toggleAttribute">MDN Documentation</a>
     */
    @JsxFunction
    public boolean toggleAttribute(final String name, final Object force) {
        if (JavaScriptEngine.isUndefined(force)) {
            if (hasAttribute(name)) {
                removeAttribute(name);
                return false;
            }
            setAttribute(name, "");
            return true;
        }
        if (JavaScriptEngine.toBoolean(force)) {
            setAttribute(name, "");
            return true;
        }
        removeAttribute(name);
        return false;
    }

    /**
     * Inserts a set of {@link Node} objects or string objects after the last child of this element.
     * String objects are inserted as equivalent {@code Text} nodes.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void append(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof Element)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        Node.append(context, thisObj, args, function);
    }

    /**
     * Inserts a set of {@link Node} objects or string objects before the first child of this element.
     * String objects are inserted as equivalent {@code Text} nodes.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void prepend(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof Element)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        Node.prepend(context, thisObj, args, function);
    }

    /**
     * Replaces the existing children of this element with a specified new set of children.
     * These can be string or {@link Node} objects.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void replaceChildren(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof Element)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        Node.replaceChildren(context, thisObj, args, function);
    }
}
