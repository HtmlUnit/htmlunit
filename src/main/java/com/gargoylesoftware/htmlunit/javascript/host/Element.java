/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ELEMENT_BASE_URL_NULL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.w3c.css.sac.CSSException;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.EventNode;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@link DomElement}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = DomElement.class)
public class Element extends EventNode {

    private NamedNodeMap attributes_;
    private Map<String, HTMLCollection> elementsByTagName_; // for performance and for equality (==)
    private CSSStyleDeclaration style_;

    /**
     * Default constructor.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
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
            final String eventName = attr.getName();
            if (eventName.length() > 2 // the has to be onX at least
                    && Character.toLowerCase(eventName.charAt(0)) == 'o'
                    && Character.toLowerCase(eventName.charAt(1)) == 'n') {
                createEventHandler(eventName, attr.getValue());
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
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public String getBaseURI() {
        if ("Element".equals(getClass().getSimpleName()) && getBrowserVersion().hasFeature(JS_ELEMENT_BASE_URL_NULL)) {
            return null;
        }
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
     * @return the value of the specified attribute, {@code null} if the attribute is not defined
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536429.aspx">MSDN Documentation</a>
     * @see <a href="http://reference.sitepoint.com/javascript/Element/getAttribute">IE Bug Documentation</a>
     */
    @JsxFunction
    public Object getAttribute(final String attributeName, final Integer flags) {
        Object value = getDomNodeOrDie().getAttribute(attributeName);

        if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
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
        final String tagNameLC = tagName.toLowerCase(Locale.ROOT);

        if (elementsByTagName_ == null) {
            elementsByTagName_ = new HashMap<>();
        }

        HTMLCollection collection = elementsByTagName_.get(tagNameLC);
        if (collection != null) {
            return collection;
        }

        final DomNode node = getDomNodeOrDie();
        if ("*".equals(tagName)) {
            collection = new HTMLCollection(node, false) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
        }
        else {
            collection = new HTMLCollection(node, false) {
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
    @JsxFunction
    public boolean hasAttribute(final String name) {
        return getDomNodeOrDie().hasAttribute(name);
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
     * Returns the current number of child elements.
     * @return the child element count
     */
    @JsxGetter
    public int getChildElementCount() {
        return getDomNodeOrDie().getChildElementCount();
    }

    /**
     * Returns the first element child.
     * @return the first element child
     */
    @JsxGetter
    public Element getFirstElementChild() {
        final DomElement child = getDomNodeOrDie().getFirstElementChild();
        if (child != null) {
            return (Element) child.getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the last element child.
     * @return the last element child
     */
    @JsxGetter
    public Element getLastElementChild() {
        final DomElement child = getDomNodeOrDie().getLastElementChild();
        if (child != null) {
            return (Element) child.getScriptableObject();
        }
        return null;
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
     * Gets the children of the current node.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537446.aspx">MSDN documentation</a>
     * @return the child at the given position
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public HTMLCollection getChildren() {
        final DomElement node = getDomNodeOrDie();
        final HTMLCollection collection = new HTMLCollection(node, false) {
            @Override
            protected List<Object> computeElements() {
                final List<Object> children = new LinkedList<>();
                for (DomNode domNode : node.getChildNodes()) {
                    if (domNode instanceof DomElement) {
                        children.add(domNode);
                    }
                }
                return children;
            }
        };
        return collection;
    }

    /**
     * Gets the token list of class attribute.
     * @return the token list of class attribute
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF) })
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
     * Returns the style object for this element.
     * @return the style object for this element
     */
    @JsxGetter
    public CSSStyleDeclaration getStyle() {
        return style_;
    }

    /**
     * Returns the runtime style object for this element.
     * @return the runtime style object for this element
     */
    @JsxGetter(@WebBrowser(IE))
    public CSSStyleDeclaration getRuntimeStyle() {
        return style_;
    }

    /**
     * Returns the current (calculated) style object for this element.
     * @return the current (calculated) style object for this element
     */
    @JsxGetter(@WebBrowser(IE))
    public ComputedCSSStyleDeclaration getCurrentStyle() {
        if (!getDomNodeOrDie().isAttachedToPage()) {
            return null;
        }
        return getWindow().getComputedStyle(this, null);
    }

    /**
     * Sets the attribute node for the specified attribute.
     * @param newAtt the attribute to set
     * @return the replaced attribute node, if any
     */
    @JsxFunction
    public Attr setAttributeNode(final Attr newAtt) {
        final String name = newAtt.getName();

        final NamedNodeMap nodes = (NamedNodeMap) getAttributes();
        final Attr replacedAtt = (Attr) nodes.getNamedItemWithoutSytheticClassAttr(name);
        if (replacedAtt != null) {
            replacedAtt.detachFromParent();
        }

        final DomAttr newDomAttr = newAtt.getDomNodeOrDie();
        getDomNodeOrDie().setAttributeNode(newDomAttr);
        return replacedAtt;
    }

    /**
     * Remove focus from this element.
     */
    @JsxFunction(@WebBrowser(CHROME))
    public void blur() {
        final DomNode domNode = getDomNodeOrDie();
        if (domNode instanceof HtmlElement) {
            ((HtmlElement) domNode).blur();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        final Object response = super.get(name, start);

        // IE8 support .querySelector(All) but not in quirks mode
        // => TODO: find a better way to handle this!
        if (response instanceof FunctionObject
                && ("querySelectorAll".equals(name) || "querySelector".equals(name))
                && getBrowserVersion().hasFeature(QUERYSELECTORALL_NOT_IN_QUIRKS)) {
            final Document doc = getWindow().getDocument();
            if ((doc instanceof HTMLDocument) && ((HTMLDocument) doc).getDocumentMode() < 8) {
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
            final List<Object> nodes = new ArrayList<>();
            for (final DomNode domNode : getDomNodeOrDie().querySelectorAll(selectors)) {
                nodes.add(domNode.getScriptableObject());
            }
            return NodeList.staticNodeList(this, nodes);
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
                return (Node) node.getScriptableObject();
            }
            return null;
        }
        catch (final CSSException e) {
            throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
                    + selectors + "' error: " + e.getMessage() + ").");
        }
    }
}
