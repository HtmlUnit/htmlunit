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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_LF;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_NULL_AS_STRING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_REMOVES_CHILDREN_FOR_DETACHED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_THROWS_FOR_DETACHED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement.DisplayStyle;
import com.gargoylesoftware.htmlunit.html.HtmlTemplate;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement.ProxyDomNode;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTemplateElement;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code Element}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = DomElement.class)
public class Element extends Node {

    static final String POSITION_BEFORE_BEGIN = "beforebegin";
    static final String POSITION_AFTER_BEGIN = "afterbegin";
    static final String POSITION_BEFORE_END = "beforeend";
    static final String POSITION_AFTER_END = "afterend";

    private static final Pattern CLASS_NAMES_SPLIT_PATTERN = Pattern.compile("\\s");
    private static final Pattern PRINT_NODE_PATTERN = Pattern.compile("  ");
    private static final Pattern PRINT_NODE_QUOTE_PATTERN = Pattern.compile("\"");

    private NamedNodeMap attributes_;
    private Map<String, HTMLCollection> elementsByTagName_; // for performance and for equality (==)
    private int scrollLeft_;
    private int scrollTop_;
    private CSSStyleDeclaration style_;

    /**
     * Default constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public Element() {
        // Empty.
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        style_ = new CSSStyleDeclaration(this);

        setParentScope(getWindow().getDocument());

        /**
         * Convert JavaScript snippets defined in the attribute map to executable event handlers.
         * Should be called only on construction.
         */
        final DomElement htmlElt = (DomElement) domNode;
        for (final DomAttr attr : htmlElt.getAttributesMap().values()) {
            final String eventName = attr.getName().toLowerCase(Locale.ROOT);
            if (eventName.startsWith("on")) {
                createEventHandler(eventName.substring(2), attr.getValue());
            }
        }
    }

    /**
     * Create the event handler function from the attribute value.
     * @param eventName the event name (ex: "onclick")
     * @param attrValue the attribute value
     */
    protected void createEventHandler(final String eventName, final String attrValue) {
        final DomElement htmlElt = getDomNodeOrDie();
        // TODO: check that it is an "allowed" event for the browser, and take care to the case
        final BaseFunction eventHandler = new EventHandler(htmlElt, eventName, attrValue);
        setEventHandler(eventName, eventHandler);
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
    public NamedNodeMap getAttributes() {
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
     * @return the value of the specified attribute, {@code null} if the attribute is not defined
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536429.aspx">MSDN Documentation</a>
     * @see <a href="http://reference.sitepoint.com/javascript/Element/getAttribute">IE Bug Documentation</a>
     */
    @JsxFunction
    public String getAttribute(final String attributeName, final Integer flags) {
        String value = getDomNodeOrDie().getAttribute(attributeName);

        if (value == ATTRIBUTE_NOT_DEFINED) {
            value = null;
        }

        return value;
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
    public HTMLCollection getElementsByTagName(final String tagName) {
        if (elementsByTagName_ == null) {
            elementsByTagName_ = new HashMap<>();
        }

        final String searchTagName;
        final boolean caseSensitive;
        final DomNode dom = getDomNodeOrNull();
        if (dom == null) {
            searchTagName = tagName.toLowerCase(Locale.ROOT);
            caseSensitive = false;
        }
        else {
            final SgmlPage page = dom.getPage();
            if (page != null && page.hasCaseSensitiveTagNames()) {
                searchTagName = tagName;
                caseSensitive = true;
            }
            else {
                searchTagName = tagName.toLowerCase(Locale.ROOT);
                caseSensitive = false;
            }
        }

        HTMLCollection collection = elementsByTagName_.get(searchTagName);
        if (collection != null) {
            return collection;
        }

        final DomNode node = getDomNodeOrDie();
        if ("*".equals(tagName)) {
            collection = new HTMLCollection(node, false) {
                @Override
                protected boolean isMatching(final DomNode nodeToMatch) {
                    return true;
                }
            };
        }
        else {
            collection = new HTMLCollection(node, false) {
                @Override
                protected boolean isMatching(final DomNode nodeToMatch) {
                    if (caseSensitive) {
                        return searchTagName.equals(nodeToMatch.getNodeName());
                    }
                    return searchTagName.equalsIgnoreCase(nodeToMatch.getNodeName());
                }
            };
        }

        elementsByTagName_.put(tagName, collection);

        return collection;
    }

    /**
     * Retrieves an attribute node by name.
     * @param name the name of the attribute to retrieve
     * @return the XMLAttr node with the specified name or {@code null} if there is no such attribute
     */
    @JsxFunction
    public Object getAttributeNode(final String name) {
        final Map<String, DomAttr> attributes = getDomNodeOrDie().getAttributesMap();
        for (final DomAttr attr : attributes.values()) {
            if (attr.getName().equals(name)) {
                return attr.getScriptableObject();
            }
        }
        return null;
    }

    /**
     * Returns a list of elements with the given tag name belonging to the given namespace.
     * @param namespaceURI the namespace URI of elements to look for
     * @param localName is either the local name of elements to look for or the special value "*",
     *                  which matches all elements.
     * @return a live NodeList of found elements in the order they appear in the tree
     */
    @JsxFunction
    public Object getElementsByTagNameNS(final Object namespaceURI, final String localName) {
        final HTMLCollection collection = new HTMLCollection(getDomNodeOrDie(), false) {
            @Override
            protected boolean isMatching(final DomNode node) {
                return ("*".equals(namespaceURI) || Objects.equals(namespaceURI, node.getNamespaceURI()))
                        && ("*".equals(localName) || Objects.equals(localName, node.getLocalName()));
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
    @JsxFunction
    public boolean hasAttribute(final String name) {
        return getDomNodeOrDie().hasAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction({CHROME, EDGE, FF, FF78})
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
     * Removes the specified attribute.
     * @param name the name of the attribute to remove
     */
    @JsxFunction
    public void removeAttribute(final String name) {
        getDomNodeOrDie().removeAttribute(name);
    }

    /**
     * Retrieves an object that specifies the bounds of a collection of TextRectangle objects.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536433.aspx">MSDN doc</a>
     * @return an object that specifies the bounds of a collection of TextRectangle objects
     */
    @JsxFunction
    public ClientRect getBoundingClientRect() {
        if (!getDomNodeOrDie().isAttachedToPage()
                && getBrowserVersion().hasFeature(JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED)) {
            throw Context.reportRuntimeError("Element is not attache to a page");
        }

        final ClientRect textRectangle = new ClientRect(1, 1, 1, 1);
        textRectangle.setParentScope(getWindow());
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
     * Returns the next element sibling.
     * @return the next element sibling
     */
    @JsxGetter
    public Element getNextElementSibling() {
        final DomElement child = getDomNodeOrDie().getNextElementSibling();
        if (child != null) {
            return (Element) child.getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the previous element sibling.
     * @return the previous element sibling
     */
    @JsxGetter
    public Element getPreviousElementSibling() {
        final DomElement child = getDomNodeOrDie().getPreviousElementSibling();
        if (child != null) {
            return (Element) child.getScriptableObject();
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
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public HTMLCollection getChildren() {
        return super.getChildren();
    }

    /**
     * Gets the token list of class attribute.
     * @return the token list of class attribute
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public DOMTokenList getClassList() {
        return new DOMTokenList(this, "class");
    }

    /**
     * Gets the specified attribute.
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return the value of the specified attribute, {@code null} if the attribute is not defined
     */
    @JsxFunction
    public String getAttributeNS(final String namespaceURI, final String localName) {
        final String value = getDomNodeOrDie().getAttributeNS(namespaceURI, localName);
        if (ATTRIBUTE_NOT_DEFINED == value
                && !getBrowserVersion().hasFeature(JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY)) {
            return null;
        }
        return value;
    }

    /**
     * Test for attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-ElHasAttrNS">
     * the DOM reference</a>
     *
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return {@code true} if the node has this attribute
     */
    @JsxFunction
    public boolean hasAttributeNS(final String namespaceURI, final String localName) {
        return getDomNodeOrDie().hasAttributeNS(namespaceURI, localName);
    }

    /**
     * Sets the specified attribute.
     * @param namespaceURI the namespace URI
     * @param qualifiedName the qualified name of the attribute to look for
     * @param value the new attribute value
     */
    @JsxFunction
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value) {
        getDomNodeOrDie().setAttributeNS(namespaceURI, qualifiedName, value);
    }

    /**
     * Removes the specified attribute.
     * @param namespaceURI the namespace URI of the attribute to remove
     * @param localName the local name of the attribute to remove
     */
    @JsxFunction
    public void removeAttributeNS(final String namespaceURI, final String localName) {
        getDomNodeOrDie().removeAttributeNS(namespaceURI, localName);
    }

    /**
     * Sets the attribute node for the specified attribute.
     * @param newAtt the attribute to set
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
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        final Object response = super.get(name, start);

        // IE support .querySelector(All) but not in quirks mode
        // => TODO: find a better way to handle this!
        if (response instanceof FunctionObject
                && ("querySelectorAll".equals(name) || "querySelector".equals(name))
                && getBrowserVersion().hasFeature(QUERYSELECTORALL_NOT_IN_QUIRKS)) {
            final Document doc = getWindow().getDocument();
            if (doc instanceof HTMLDocument && ((HTMLDocument) doc).getDocumentMode() < 8) {
                return NOT_FOUND;
            }
        }

        return response;
    }

    /**
     * Retrieves all element nodes from descendants of the starting element node that match any selector
     * within the supplied selector strings.
     * The NodeList object returned by the querySelectorAll() method must be static, not live.
     * @param selectors the selectors
     * @return the static node list
     */
    @JsxFunction
    public NodeList querySelectorAll(final String selectors) {
        try {
            return NodeList.staticNodeList(this, getDomNodeOrDie().querySelectorAll(selectors));
        }
        catch (final CSSException e) {
            throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
                    + selectors + "' error: " + e.getMessage() + ").");
        }
    }

    /**
     * Returns the first element within the document that matches the specified group of selectors.
     * @param selectors the selectors
     * @return null if no matches are found; otherwise, it returns the first matching element
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
            throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
                    + selectors + "' error: " + e.getMessage() + ").");
        }
    }

    /**
     * Returns the class defined for this element.
     * @return the class name
     */
    @JsxGetter(propertyName = "className", value = {CHROME, EDGE, FF, FF78})
    public Object getClassName_js() {
        return getDomNodeOrDie().getAttributeDirect("class");
    }

    /**
     * Sets the class attribute for this element.
     * @param className the new class name
     */
    @JsxSetter(propertyName = "className", value = {CHROME, EDGE, FF, FF78})
    public void setClassName_js(final String className) {
        getDomNodeOrDie().setAttribute("class", className);
    }

    /**
     * Returns the {@code clientHeight} attribute.
     * @return the {@code clientHeight} attribute
     */
    @JsxGetter
    public int getClientHeight() {
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getCalculatedHeight(false, true);
    }

    /**
     * Returns the {@code clientWidth} attribute.
     * @return the {@code clientWidth} attribute
     */
    @JsxGetter
    public int getClientWidth() {
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getCalculatedWidth(false, true);
    }

    /**
     * Returns the {@code clientLeft} attribute.
     * @return the {@code clientLeft} attribute
     */
    @JsxGetter
    public int getClientLeft() {
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getBorderLeftValue();
    }

    /**
     * Returns {@code clientTop} attribute.
     * @return the {@code clientTop} attribute
     */
    @JsxGetter
    public int getClientTop() {
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getBorderTopValue();
    }

    /**
     * Returns the specified attribute.
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return the specified attribute, {@code null} if the attribute is not defined
     */
    @JsxFunction
    public Object getAttributeNodeNS(final String namespaceURI, final String localName) {
        return getDomNodeOrDie().getAttributeNodeNS(namespaceURI, localName).getScriptableObject();
    }

    /**
     * Returns all the descendant elements with the specified class.
     * @param className the name to search for
     * @return all the descendant elements with the specified class name
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public HTMLCollection getElementsByClassName(final String className) {
        final DomElement elt = getDomNodeOrDie();
        final String[] classNames = CLASS_NAMES_SPLIT_PATTERN.split(className, 0);

        final HTMLCollection collection = new HTMLCollection(elt, true) {
            @Override
            protected boolean isMatching(final DomNode node) {
                if (!(node instanceof HtmlElement)) {
                    return false;
                }
                String classAttribute = ((HtmlElement) node).getAttributeDirect("class");
                if (classAttribute == ATTRIBUTE_NOT_DEFINED) {
                    return false; // probably better performance as most of elements won't have a class attribute
                }

                classAttribute = " " + classAttribute + " ";
                for (final String aClassName : classNames) {
                    if (!classAttribute.contains(" " + aClassName + " ")) {
                        return false;
                    }
                }
                return true;
            }
        };

        return collection;
    }

    /**
     * Retrieves a collection of rectangles that describes the layout of the contents of an object
     * or range within the client. Each rectangle describes a single line.
     * @return a collection of rectangles that describes the layout of the contents
     */
    @JsxFunction
    public ClientRectList getClientRects() {
        final Window w = getWindow();
        final ClientRectList rectList = new ClientRectList();
        rectList.setParentScope(w);
        rectList.setPrototype(getPrototype(rectList.getClass()));

        if (!isDisplayNone() && getDomNodeOrDie().isAttachedToPage()) {
            final ClientRect rect = new ClientRect(0, 0, 1, 1);
            rect.setParentScope(w);
            rect.setPrototype(getPrototype(rect.getClass()));
            rectList.add(rect);
        }

        return rectList;
    }

    /**
     * Returns whether the {@code display} is {@code none} or not.
     * @return whether the {@code display} is {@code none} or not
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
     * Creates a new TextRange object for this element.
     * @return a new TextRange object for this element
     */
    protected TextRange createTextRange() {
        final TextRange range = new TextRange(this);
        range.setParentScope(getParentScope());
        range.setPrototype(getPrototype(range.getClass()));
        return range;
    }

    /**
     * Inserts the given element into the element at the location.
     * @param where specifies where to insert the element, using one of the following values (case-insensitive):
     *        beforebegin, afterbegin, beforeend, afterend
     * @param insertedElement the element to be inserted
     * @return an element object
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536451.aspx">MSDN</a>
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public Object insertAdjacentElement(final String where, final Object insertedElement) {
        if (insertedElement instanceof Node) {
            final DomNode childNode = ((Node) insertedElement).getDomNodeOrDie();
            final Object[] values = getInsertAdjacentLocation(where);
            final DomNode node = (DomNode) values[0];
            final boolean append = ((Boolean) values[1]).booleanValue();

            if (append) {
                node.appendChild(childNode);
            }
            else {
                node.insertBefore(childNode);
            }
            return insertedElement;
        }
        throw Context.reportRuntimeError("Passed object is not an element: " + insertedElement);
    }

    /**
     * Inserts the given text into the element at the specified location.
     * @param where specifies where to insert the text, using one of the following values (case-insensitive):
     *      beforebegin, afterbegin, beforeend, afterend
     * @param text the text to insert
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536453.aspx">MSDN</a>
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
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
     * Returns where and how to add the new node.
     * Used by {@link #insertAdjacentHTML(String, String)},
     * {@link #insertAdjacentElement(String, Object)} and
     * {@link #insertAdjacentText(String, String)}.
     * @param where specifies where to insert the element, using one of the following values (case-insensitive):
     *        beforebegin, afterbegin, beforeend, afterend
     * @return an array of 1-DomNode:parentNode and 2-Boolean:append
     */
    private Object[] getInsertAdjacentLocation(final String where) {
        final DomNode currentNode = getDomNodeOrDie();
        final DomNode node;
        final boolean append;

        // compute the where and how the new nodes should be added
        if (POSITION_AFTER_BEGIN.equalsIgnoreCase(where)) {
            if (currentNode.getFirstChild() == null) {
                // new nodes should appended to the children of current node
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
            // new nodes should appended to the children of current node
            node = currentNode;
            append = true;
        }
        else if (POSITION_AFTER_END.equalsIgnoreCase(where)) {
            if (currentNode.getNextSibling() == null) {
                // new nodes should appended to the children of parent node
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
            throw Context.reportRuntimeError("Illegal position value: \"" + where + "\"");
        }

        if (append) {
            return new Object[] {node, Boolean.TRUE};
        }
        return new Object[] {node, Boolean.FALSE};
    }

    /**
     * Parses the given text as HTML or XML and inserts the resulting nodes into the tree in the position given by the
     * position argument.
     * @param position specifies where to insert the nodes, using one of the following values (case-insensitive):
     *        <code>beforebegin</code>, <code>afterbegin</code>, <code>beforeend</code>, <code>afterend</code>
     * @param text the text to parse
     *
     * @see <a href="http://www.w3.org/TR/DOM-Parsing/#methods-2">W3C Spec</a>
     * @see <a href="http://domparsing.spec.whatwg.org/#dom-element-insertadjacenthtml">WhatWG Spec</a>
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element.insertAdjacentHTML"
     *      >Mozilla Developer Network</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536452.aspx">MSDN</a>
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public void insertAdjacentHTML(final String position, final String text) {
        final Object[] values = getInsertAdjacentLocation(position);
        final DomNode domNode = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        // add the new nodes
        final DomNode proxyDomNode = new ProxyDomNode(domNode.getPage(), domNode, append);
        parseHtmlSnippet(proxyDomNode, text);
    }

    /**
     * Parses the specified HTML source code, appending the resulting content at the specified target location.
     * @param target the node indicating the position at which the parsed content should be placed
     * @param source the HTML code extract to parse
     */
    private static void parseHtmlSnippet(final DomNode target, final String source) {
        try {
            target.getPage().getWebClient().getPageCreator().getHtmlParser().parseFragment(target, source);
        }
        catch (final IOException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
            throw Context.reportRuntimeError("Unexpected exception occurred while parsing HTML snippet: "
                    + e.getMessage());
        }
        catch (final SAXException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
            throw Context.reportRuntimeError("Unexpected exception occurred while parsing HTML snippet: "
                    + e.getMessage());
        }
    }

    /**
     * Gets the {@code innerHTML} attribute.
     * @return the contents of this node as HTML
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public String getInnerHTML() {
        try {
            DomNode domNode = getDomNodeOrDie();
            if (this instanceof HTMLTemplateElement) {
                domNode = ((HtmlTemplate) getDomNodeOrDie()).getContent();
            }
            return getInnerHTML(domNode);
        }
        catch (final IllegalStateException e) {
            Context.throwAsScriptRuntimeEx(e);
            return "";
        }
    }

    /**
     * Replaces all child elements of this element with the supplied value.
     * @param value the new value for the contents of this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setInnerHTML(final Object value) {
        final DomNode domNode;
        try {
            domNode = getDomNodeOrDie();
        }
        catch (final IllegalStateException e) {
            Context.throwAsScriptRuntimeEx(e);
            return;
        }

        domNode.removeAllChildren();
        getWindow().clearComputedStylesUpToRoot(this);

        final boolean addChildForNull = getBrowserVersion().hasFeature(JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE);
        if ((value == null && addChildForNull) || (value != null && !"".equals(value))) {
            final String valueAsString = Context.toString(value);
            parseHtmlSnippet(domNode, valueAsString);
        }
    }

    /**
     * Helper for getInnerHtml (to be reuses bei HTMLTemplate.
     * @param domNode the node
     * @return the contents of this node as HTML
     */
    protected String getInnerHTML(final DomNode domNode) {
        final StringBuilder buf = new StringBuilder();

        final String tagName = getTagName();
        boolean isPlain = "SCRIPT".equals(tagName);

        isPlain = isPlain || "STYLE".equals(tagName);

        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, domNode, false, !isPlain);
        return buf.toString();
    }

    /**
     * Gets the outerHTML of the node.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534310.aspx">MSDN documentation</a>
     * @return the contents of this node as HTML
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public String getOuterHTML() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printNode(buf, getDomNodeOrDie(), true, true);
        return buf.toString();
    }

    /**
     * Replaces this element (including all child elements) with the supplied value.
     * @param value the new value for replacing this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOuterHTML(final Object value) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode parent = domNode.getParentNode();
        if (null == parent) {
            if (getBrowserVersion().hasFeature(JS_OUTER_HTML_REMOVES_CHILDREN_FOR_DETACHED)) {
                domNode.removeAllChildren();
            }
            if (getBrowserVersion().hasFeature(JS_OUTER_HTML_THROWS_FOR_DETACHED)) {
                throw Context.reportRuntimeError("outerHTML is readonly for detached nodes");
            }
            return;
        }

        if (value == null && !getBrowserVersion().hasFeature(JS_OUTER_HTML_NULL_AS_STRING)) {
            domNode.remove();
            return;
        }
        final String valueStr = Context.toString(value);
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
     * Helper for getting code back from nodes.
     * @param builder the builder to write to
     * @param node the node to be serialized
     * @param forOuter true if this is called during processing of a outerHtml call
     * @param html flag
     */
    protected final void printChildren(final StringBuilder builder, final DomNode node,
                final boolean forOuter, final boolean html) {
        if (forOuter && (node instanceof HtmlTemplate)) {
            final HtmlTemplate template = (HtmlTemplate) node;

            for (final DomNode child : template.getContent().getChildren()) {
                printNode(builder, child, forOuter, html);
            }
            return;
        }

        for (final DomNode child : node.getChildren()) {
            printNode(builder, child, forOuter, html);
        }
    }

    protected void printNode(final StringBuilder builder, final DomNode node,
                    final boolean forOuter, final boolean html) {
        if (node instanceof DomComment) {
            if (html) {
                // Remove whitespace sequences.
                final String s = PRINT_NODE_PATTERN.matcher(node.getNodeValue()).replaceAll(" ");
                builder.append("<!--").append(s).append("-->");
            }
        }
        else if (node instanceof DomCharacterData) {
            // Remove whitespace sequences, possibly escape XML characters.
            String s = node.getNodeValue();
            if (html) {
                s = com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(s);
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
            // Add the attributes. IE does not use quotes, FF does.
            for (final DomAttr attr : element.getAttributesMap().values()) {
                if (!attr.getSpecified()) {
                    continue;
                }

                final String name = attr.getName();
                final String value = PRINT_NODE_QUOTE_PATTERN.matcher(attr.getValue()).replaceAll("&quot;");
                builder.append(' ').append(name).append('=');
                builder.append('\"');
                builder.append(value);
                builder.append('\"');
            }
            builder.append('>');
            // Add the children.
            final boolean isHtml = html
                    && !(scriptObject instanceof HTMLScriptElement)
                    && !(scriptObject instanceof HTMLStyleElement);
            printChildren(builder, node, forOuter, isHtml);
            if (null == htmlElement || !htmlElement.isEndTagForbidden()) {
                builder.append("</").append(tag).append('>');
            }
        }
        else {
            if (node instanceof HtmlElement) {
                final HtmlElement element = (HtmlElement) node;
                if ("p".equals(element.getTagName())) {
                    if (getBrowserVersion().hasFeature(JS_INNER_TEXT_LF)) {
                        builder.append('\n'); // \r\n because it's to implement something IE specific
                    }
                    else {
                        int i = builder.length() - 1;
                        while (i >= 0 && Character.isWhitespace(builder.charAt(i))) {
                            i--;
                        }
                        builder.setLength(i + 1);
                        builder.append('\n');
                    }
                }
                if (!"script".equals(element.getTagName())) {
                    printChildren(builder, node, forOuter, html);
                }
            }
        }
    }

    /**
     * Returns whether the end tag is forbidden or not.
     * @see <a href="http://www.w3.org/TR/html4/index/elements.html">HTML 4 specs</a>
     * @return whether the end tag is forbidden or not
     */
    protected boolean isEndTagForbidden() {
        return false;
    }

    /**
     * Returns the element ID.
     * @return the ID of this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public String getId() {
        return getDomNodeOrDie().getId();
    }

    /**
     * Sets the id value for this element.
     * @param newId the newId value for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setId(final String newId) {
        getDomNodeOrDie().setId(newId);
    }

    /**
     * Removes the specified attribute.
     * @param attribute the attribute to remove
     */
    @JsxFunction
    public void removeAttributeNode(final Attr attribute) {
        final String name = attribute.getName();
        final Object namespaceUri = attribute.getNamespaceURI();
        if (namespaceUri instanceof String) {
            removeAttributeNS((String) namespaceUri, name);
            return;
        }
        removeAttributeNS(null, name);
    }

    /**
     * Gets the scrollTop value for this element.
     * @return the scrollTop value for this element
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534618.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public int getScrollTop() {
        // It's easier to perform these checks and adjustments in the getter, rather than in the setter,
        // because modifying the CSS style of the element is supposed to affect the attribute value.
        if (scrollTop_ < 0) {
            scrollTop_ = 0;
        }
        else if (scrollTop_ > 0) {
            final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
            if (!style.isScrollable(false)) {
                scrollTop_ = 0;
            }
        }
        return scrollTop_;
    }

    /**
     * Sets the scrollTop value for this element.
     * @param scroll the scrollTop value for this element
     */
    @JsxSetter
    public void setScrollTop(final int scroll) {
        scrollTop_ = scroll;
    }

    /**
     * Gets the scrollLeft value for this element.
     * @return the scrollLeft value for this element
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534617.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public int getScrollLeft() {
        // It's easier to perform these checks and adjustments in the getter, rather than in the setter,
        // because modifying the CSS style of the element is supposed to affect the attribute value.
        if (scrollLeft_ < 0) {
            scrollLeft_ = 0;
        }
        else if (scrollLeft_ > 0) {
            final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
            if (!style.isScrollable(true)) {
                scrollLeft_ = 0;
            }
        }
        return scrollLeft_;
    }

    /**
     * Sets the scrollLeft value for this element.
     * @param scroll the scrollLeft value for this element
     */
    @JsxSetter
    public void setScrollLeft(final int scroll) {
        scrollLeft_ = scroll;
    }

    /**
     * Gets the scrollHeight for this element.
     * @return at the moment the same as client height
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534615.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public int getScrollHeight() {
        return getClientHeight();
    }

    /**
     * Gets the scrollWidth for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534619.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public int getScrollWidth() {
        return getClientWidth();
    }

    /**
     * Returns the style object for this element.
     * @return the style object for this element
     */
    protected CSSStyleDeclaration getStyle() {
        return style_;
    }

    /**
     * Sets the styles for this element.
     * @param style the style of the element
     */
    protected void setStyle(final String style) {
        if (!getBrowserVersion().hasFeature(JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY)) {
            getStyle().setCssText(style);
        }
    }

    /**
     * Implement the {@code scrollIntoView()} JavaScript function but don't actually do
     * anything. The requirement
     * is just to prevent scripts that call that method from failing
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public void scrollIntoView() {
        /* do nothing at the moment */
    }

    /**
     * Implement the {@code scrollIntoViewIfNeeded()} JavaScript function but don't actually do
     * anything.
     */
    @JsxFunction({CHROME, EDGE})
    public void scrollIntoViewIfNeeded() {
        /* do nothing at the moment */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Object getPrefix() {
        return super.getPrefix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Object getLocalName() {
        return super.getLocalName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Object getNamespaceURI() {
        return super.getNamespaceURI();
    }

    /**
     * Returns the {@code onbeforecopy} event handler for this element.
     * @return the {@code onbeforecopy} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforecopy() {
        return getEventHandler(Event.TYPE_BEFORECOPY);
    }

    /**
     * Sets the {@code onbeforecopy} event handler for this element.
     * @param onbeforecopy the {@code onbeforecopy} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforecopy(final Object onbeforecopy) {
        setEventHandler(Event.TYPE_BEFORECOPY, onbeforecopy);
    }

    /**
     * Returns the {@code onbeforecut} event handler for this element.
     * @return the {@code onbeforecut} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforecut() {
        return getEventHandler(Event.TYPE_BEFORECUT);
    }

    /**
     * Sets the {@code onbeforecut} event handler for this element.
     * @param onbeforecut the {@code onbeforecut} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforecut(final Object onbeforecut) {
        setEventHandler(Event.TYPE_BEFORECUT, onbeforecut);
    }

    /**
     * Returns the {@code onbeforepaste} event handler for this element.
     * @return the {@code onbeforepaste} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforepaste() {
        return getEventHandler(Event.TYPE_BEFOREPASTE);
    }

    /**
     * Sets the {@code onbeforepaste} event handler for this element.
     * @param onbeforepaste the {@code onbeforepaste} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforepaste(final Object onbeforepaste) {
        setEventHandler(Event.TYPE_BEFOREPASTE, onbeforepaste);
    }

    /**
     * Returns the {@code onsearch} event handler for this element.
     * @return the {@code onsearch} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnsearch() {
        return getEventHandler(Event.TYPE_SEARCH);
    }

    /**
     * Sets the {@code onsearch} event handler for this element.
     * @param onsearch the {@code onsearch} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnsearch(final Object onsearch) {
        setEventHandler(Event.TYPE_SEARCH, onsearch);
    }

    /**
     * Returns the {@code onwebkitfullscreenchange} event handler for this element.
     * @return the {@code onwebkitfullscreenchange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitfullscreenchange() {
        return getEventHandler(Event.TYPE_WEBKITFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onwebkitfullscreenchange} event handler for this element.
     * @param onwebkitfullscreenchange the {@code onwebkitfullscreenchange} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkitfullscreenchange(final Object onwebkitfullscreenchange) {
        setEventHandler(Event.TYPE_WEBKITFULLSCREENCHANGE, onwebkitfullscreenchange);
    }

    /**
     * Returns the {@code onwebkitfullscreenerror} event handler for this element.
     * @return the {@code onwebkitfullscreenerror} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitfullscreenerror() {
        return getEventHandler(Event.TYPE_WEBKITFULLSCREENERROR);
    }

    /**
     * Sets the {@code onwebkitfullscreenerror} event handler for this element.
     * @param onwebkitfullscreenerror the {@code onwebkitfullscreenerror} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkitfullscreenerror(final Object onwebkitfullscreenerror) {
        setEventHandler(Event.TYPE_WEBKITFULLSCREENERROR, onwebkitfullscreenerror);
    }

    /**
     * Returns the {@code onwheel} event handler for this element.
     * @return the {@code onwheel} event handler for this element
     */
    public Function getOnwheel() {
        return getEventHandler(Event.TYPE_WHEEL);
    }

    /**
     * Sets the {@code onwheel} event handler for this element.
     * @param onwheel the {@code onwheel} event handler for this element
     */
    public void setOnwheel(final Object onwheel) {
        setEventHandler(Event.TYPE_WHEEL, onwheel);
    }

    /**
     * Returns the {@code ongotpointercapture} event handler for this element.
     * @return the {@code ongotpointercapture} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOngotpointercapture() {
        return getEventHandler(Event.TYPE_GOTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code ongotpointercapture} event handler for this element.
     * @param ongotpointercapture the {@code ongotpointercapture} event handler for this element
     */
    @JsxSetter(IE)
    public void setOngotpointercapture(final Object ongotpointercapture) {
        setEventHandler(Event.TYPE_GOTPOINTERCAPTURE, ongotpointercapture);
    }

    /**
     * Returns the {@code onlostpointercapture} event handler for this element.
     * @return the {@code onlostpointercapture} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnlostpointercapture() {
        return getEventHandler(Event.TYPE_LOSTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code onlostpointercapture} event handler for this element.
     * @param onlostpointercapture the {@code onlostpointercapture} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnlostpointercapture(final Object onlostpointercapture) {
        setEventHandler(Event.TYPE_LOSTPOINTERCAPTURE, onlostpointercapture);
    }

    /**
     * Returns the {@code onmsgesturechange} event handler for this element.
     * @return the {@code onmsgesturechange} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsgesturechange() {
        return getEventHandler(Event.TYPE_MSGESTURECHANGE);
    }

    /**
     * Sets the {@code onmsgesturechange} event handler for this element.
     * @param onmsgesturechange the {@code onmsgesturechange} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsgesturechange(final Object onmsgesturechange) {
        setEventHandler(Event.TYPE_MSGESTURECHANGE, onmsgesturechange);
    }

    /**
     * Returns the {@code onmsgesturedoubletap} event handler for this element.
     * @return the {@code onmsgesturedoubletap} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsgesturedoubletap() {
        return getEventHandler(Event.TYPE_MSGESTUREDOUBLETAP);
    }

    /**
     * Sets the {@code onmsgesturedoubletap} event handler for this element.
     * @param onmsgesturedoubletap the {@code onmsgesturedoubletap} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsgesturedoubletap(final Object onmsgesturedoubletap) {
        setEventHandler(Event.TYPE_MSGESTUREDOUBLETAP, onmsgesturedoubletap);
    }

    /**
     * Returns the {@code onmsgestureend} event handler for this element.
     * @return the {@code onmsgestureend} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsgestureend() {
        return getEventHandler(Event.TYPE_MSGESTUREEND);
    }

    /**
     * Sets the {@code onmsgestureend} event handler for this element.
     * @param onmsgestureend the {@code onmsgestureend} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsgestureend(final Object onmsgestureend) {
        setEventHandler(Event.TYPE_MSGESTUREEND, onmsgestureend);
    }

    /**
     * Returns the {@code onmsgesturehold} event handler for this element.
     * @return the {@code onmsgesturehold} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsgesturehold() {
        return getEventHandler(Event.TYPE_MSGESTUREHOLD);
    }

    /**
     * Sets the {@code onmsgesturehold} event handler for this element.
     * @param onmsgesturehold the {@code onmsgesturehold} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsgesturehold(final Object onmsgesturehold) {
        setEventHandler(Event.TYPE_MSGESTUREHOLD, onmsgesturehold);
    }

    /**
     * Returns the {@code onmsgesturestart} event handler for this element.
     * @return the {@code onmsgesturestart} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsgesturestart() {
        return getEventHandler(Event.TYPE_MSGESTURESTART);
    }

    /**
     * Sets the {@code onmsgesturestart} event handler for this element.
     * @param onmsgesturestart the {@code onmsgesturestart} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsgesturestart(final Object onmsgesturestart) {
        setEventHandler(Event.TYPE_MSGESTURESTART, onmsgesturestart);
    }

    /**
     * Returns the {@code onmsgesturetap} event handler for this element.
     * @return the {@code onmsgesturetap} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsgesturetap() {
        return getEventHandler(Event.TYPE_MSGESTURETAP);
    }

    /**
     * Sets the {@code onmsgesturetap} event handler for this element.
     * @param onmsgesturetap the {@code onmsgesturetap} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsgesturetap(final Object onmsgesturetap) {
        setEventHandler(Event.TYPE_MSGESTURETAP, onmsgesturetap);
    }

    /**
     * Returns the {@code onmsgotpointercapture} event handler for this element.
     * @return the {@code onmsgotpointercapture} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsgotpointercapture() {
        return getEventHandler(Event.TYPE_MSGOTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code onmsgotpointercapture} event handler for this element.
     * @param onmsgotpointercapture the {@code onmsgotpointercapture} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsgotpointercapture(final Object onmsgotpointercapture) {
        setEventHandler(Event.TYPE_MSGOTPOINTERCAPTURE, onmsgotpointercapture);
    }

    /**
     * Returns the {@code onmsinertiastart} event handler for this element.
     * @return the {@code onmsinertiastart} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsinertiastart() {
        return getEventHandler(Event.TYPE_MSINERTIASTART);
    }

    /**
     * Sets the {@code onmsinertiastart} event handler for this element.
     * @param onmsinertiastart the {@code onmsinertiastart} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsinertiastart(final Object onmsinertiastart) {
        setEventHandler(Event.TYPE_MSINERTIASTART, onmsinertiastart);
    }

    /**
     * Returns the {@code onmslostpointercapture} event handler for this element.
     * @return the {@code onmslostpointercapture} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmslostpointercapture() {
        return getEventHandler(Event.TYPE_MSLOSTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code onmslostpointercapture} event handler for this element.
     * @param onmslostpointercapture the {@code onmslostpointercapture} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmslostpointercapture(final Object onmslostpointercapture) {
        setEventHandler(Event.TYPE_MSLOSTPOINTERCAPTURE, onmslostpointercapture);
    }

    /**
     * Returns the {@code onmspointercancel} event handler for this element.
     * @return the {@code onmspointercancel} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmspointercancel() {
        return getEventHandler(Event.TYPE_MSPOINTERCANCEL);
    }

    /**
     * Sets the {@code onmspointercancel} event handler for this element.
     * @param onmspointercancel the {@code onmspointercancel} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmspointercancel(final Object onmspointercancel) {
        setEventHandler(Event.TYPE_MSPOINTERCANCEL, onmspointercancel);
    }

    /**
     * Returns the {@code onmspointerdown} event handler for this element.
     * @return the {@code onmspointerdown} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmspointerdown() {
        return getEventHandler(Event.TYPE_MSPOINTERDOWN);
    }

    /**
     * Sets the {@code onmspointerdown} event handler for this element.
     * @param onmspointerdown the {@code onmspointerdown} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmspointerdown(final Object onmspointerdown) {
        setEventHandler(Event.TYPE_MSPOINTERDOWN, onmspointerdown);
    }

    /**
     * Returns the {@code onmspointerenter} event handler for this element.
     * @return the {@code onmspointerenter} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmspointerenter() {
        return getEventHandler(Event.TYPE_MSPOINTENTER);
    }

    /**
     * Sets the {@code onmspointerenter} event handler for this element.
     * @param onmspointerenter the {@code onmspointerenter} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmspointerenter(final Object onmspointerenter) {
        setEventHandler(Event.TYPE_MSPOINTENTER, onmspointerenter);
    }

    /**
     * Returns the {@code onmspointerleave} event handler for this element.
     * @return the {@code onmspointerleave} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmspointerleave() {
        return getEventHandler(Event.TYPE_MSPOINTERLEAVE);
    }

    /**
     * Sets the {@code onmspointerleave} event handler for this element.
     * @param onmspointerleave the {@code onmspointerleave} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmspointerleave(final Object onmspointerleave) {
        setEventHandler(Event.TYPE_MSPOINTERLEAVE, onmspointerleave);
    }

    /**
     * Returns the {@code onmspointermove} event handler for this element.
     * @return the {@code onmspointermove} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmspointermove() {
        return getEventHandler(Event.TYPE_MSPOINTERMOVE);
    }

    /**
     * Sets the {@code onmspointermove} event handler for this element.
     * @param onmspointermove the {@code onmspointermove} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmspointermove(final Object onmspointermove) {
        setEventHandler(Event.TYPE_MSPOINTERMOVE, onmspointermove);
    }

    /**
     * Returns the {@code onmspointerout} event handler for this element.
     * @return the {@code onmspointerout} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmspointerout() {
        return getEventHandler(Event.TYPE_MSPOINTEROUT);
    }

    /**
     * Sets the {@code onmspointerout} event handler for this element.
     * @param onmspointerout the {@code onmspointerout} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmspointerout(final Object onmspointerout) {
        setEventHandler(Event.TYPE_MSPOINTEROUT, onmspointerout);
    }

    /**
     * Returns the {@code onmspointerover} event handler for this element.
     * @return the {@code onmspointerover} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmspointerover() {
        return getEventHandler(Event.TYPE_MSPOINTEROVER);
    }

    /**
     * Sets the {@code onmspointerover} event handler for this element.
     * @param onmspointerover the {@code onmspointerover} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmspointerover(final Object onmspointerover) {
        setEventHandler(Event.TYPE_MSPOINTEROVER, onmspointerover);
    }

    /**
     * Returns the {@code onmspointerup} event handler for this element.
     * @return the {@code onmspointerup} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmspointerup() {
        return getEventHandler(Event.TYPE_MSPOINTERUP);
    }

    /**
     * Sets the {@code onmspointerup} event handler for this element.
     * @param onmspointerup the {@code onmspointerup} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmspointerup(final Object onmspointerup) {
        setEventHandler(Event.TYPE_MSPOINTERUP, onmspointerup);
    }

    /**
     * Returns the {@code onpointercancel} event handler for this element.
     * @return the {@code onpointercancel} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnpointercancel() {
        return getEventHandler(Event.TYPE_POINTERCANCEL);
    }

    /**
     * Sets the {@code onpointercancel} event handler for this element.
     * @param onpointercancel the {@code onpointercancel} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnpointercancel(final Object onpointercancel) {
        setEventHandler(Event.TYPE_POINTERCANCEL, onpointercancel);
    }

    /**
     * Returns the {@code onpointerdown} event handler for this element.
     * @return the {@code onpointerdown} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnpointerdown() {
        return getEventHandler(Event.TYPE_POINTERDOWN);
    }

    /**
     * Sets the {@code onpointerdown} event handler for this element.
     * @param onpointerdown the {@code onpointerdown} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnpointerdown(final Object onpointerdown) {
        setEventHandler(Event.TYPE_POINTERDOWN, onpointerdown);
    }

    /**
     * Returns the {@code onpointerenter} event handler for this element.
     * @return the {@code onpointerenter} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnpointerenter() {
        return getEventHandler(Event.TYPE_POINTERENTER);
    }

    /**
     * Sets the {@code onpointerenter} event handler for this element.
     * @param onpointerenter the {@code onpointerenter} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnpointerenter(final Object onpointerenter) {
        setEventHandler(Event.TYPE_POINTERENTER, onpointerenter);
    }

    /**
     * Returns the {@code onpointerleave} event handler for this element.
     * @return the {@code onpointerleave} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnpointerleave() {
        return getEventHandler(Event.TYPE_POINTERLEAVE);
    }

    /**
     * Sets the {@code onpointerleave} event handler for this element.
     * @param onpointerleave the {@code onpointerleave} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnpointerleave(final Object onpointerleave) {
        setEventHandler(Event.TYPE_POINTERLEAVE, onpointerleave);
    }

    /**
     * Returns the {@code onpointermove} event handler for this element.
     * @return the {@code onpointermove} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnpointermove() {
        return getEventHandler(Event.TYPE_POINTERMOVE);
    }

    /**
     * Sets the {@code onpointermove} event handler for this element.
     * @param onpointermove the {@code onpointermove} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnpointermove(final Object onpointermove) {
        setEventHandler(Event.TYPE_POINTERMOVE, onpointermove);
    }

    /**
     * Returns the {@code onpointerout} event handler for this element.
     * @return the {@code onpointerout} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnpointerout() {
        return getEventHandler(Event.TYPE_POINTEROUT);
    }

    /**
     * Sets the {@code onpointerout} event handler for this element.
     * @param onpointerout the {@code onpointerout} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnpointerout(final Object onpointerout) {
        setEventHandler(Event.TYPE_POINTEROUT, onpointerout);
    }

    /**
     * Returns the {@code onpointerover} event handler for this element.
     * @return the {@code onpointerover} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnpointerover() {
        return getEventHandler(Event.TYPE_POINTEROVER);
    }

    /**
     * Sets the {@code onpointerover} event handler for this element.
     * @param onpointerover the {@code onpointerover} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnpointerover(final Object onpointerover) {
        setEventHandler(Event.TYPE_POINTEROVER, onpointerover);
    }

    /**
     * Returns the {@code onpointerup} event handler for this element.
     * @return the {@code onpointerup} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnpointerup() {
        return getEventHandler(Event.TYPE_POINTERUP);
    }

    /**
     * Sets the {@code onpointerup} event handler for this element.
     * @param onpointerup the {@code onpointerup} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnpointerup(final Object onpointerup) {
        setEventHandler(Event.TYPE_POINTERUP, onpointerup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public void remove() {
        super.remove();
    }

    /**
     * Mock for the moment.
     * @param retargetToElement if true, all events are targeted directly to this element;
     * if false, events can also fire at descendants of this element
     */
    @JsxFunction({FF, FF78})
    public void setCapture(final boolean retargetToElement) {
        // empty
    }

    /**
     * Mock for the moment.
     * @return true for success
     */
    @JsxFunction({FF, FF78})
    public boolean releaseCapture() {
        return true;
    }

    /**
     * Inserts a set of Node or DOMString objects in the children list of this ChildNode's parent,
     * just before this ChildNode.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public static void before(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        Node.before(context, thisObj, args, function);
    }

    /**
     * Inserts a set of Node or DOMString objects in the children list of this ChildNode's parent,
     * just after this ChildNode.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public static void after(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        Node.after(context, thisObj, args, function);
    }

    /**
     * Replaces the node wit a set of Node or DOMString objects.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public static void replaceWith(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        Node.replaceWith(context, thisObj, args, function);
    }

    /**
     * Returns true if the element would be selected by the specified selector string; otherwise, returns false.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the value
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public static boolean matches(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final String selectorString = (String) args[0];
        if (!(thisObj instanceof Element)) {
            throw ScriptRuntime.typeError("Illegal invocation");
        }
        try {
            final DomNode domNode = ((Element) thisObj).getDomNodeOrNull();
            return domNode != null && ((DomElement) domNode).matches(selectorString);
        }
        catch (final CSSException e) {
            throw ScriptRuntime.constructError("SyntaxError",
                    "An invalid or illegal selector was specified (selector: '"
                    + selectorString + "' error: " + e.getMessage() + ").");
        }
    }

    /**
     * Returns true if the element would be selected by the specified selector string; otherwise, returns false.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the value
     */
    @JsxFunction({FF, FF78})
    public static boolean mozMatchesSelector(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        return matches(context, thisObj, args, function);
    }

    /**
     * Returns true if the element would be selected by the specified selector string; otherwise, returns false.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the value
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public static boolean webkitMatchesSelector(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        return matches(context, thisObj, args, function);
    }

    /**
     * Returns true if the element would be selected by the specified selector string; otherwise, returns false.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the value
     */
    @JsxFunction(IE)
    public static boolean msMatchesSelector(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        return matches(context, thisObj, args, function);
    }
}
