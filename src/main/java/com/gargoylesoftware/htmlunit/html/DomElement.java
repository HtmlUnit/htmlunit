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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCLICK_USES_POINTEREVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_AREA_WITHOUT_HREF_FOCUSABLE;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.SelectorList;
import com.gargoylesoftware.css.parser.selector.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.css.StyleElement;
import com.gargoylesoftware.htmlunit.javascript.AbstractJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 * @author Ronald Brill
 * @author Frank Danek
 */
public class DomElement extends DomNamespaceNode implements Element {

    private static final Log LOG = LogFactory.getLog(DomElement.class);

    /** src. */
    public static final String SRC_ATTRIBUTE = "src";

    /** Constant meaning that the specified attribute was not defined. */
    public static final String ATTRIBUTE_NOT_DEFINED = new String("");

    /** Constant meaning that the specified attribute was found but its value was empty. */
    public static final String ATTRIBUTE_VALUE_EMPTY = new String();

    /** The map holding the attributes, keyed by name. */
    private NamedAttrNodeMapImpl attributes_ = new NamedAttrNodeMapImpl(this, isAttributeCaseSensitive());

    /** The map holding the namespaces, keyed by URI. */
    private final Map<String, String> namespaces_ = new HashMap<>();

    /** Cache for the styles. */
    private String styleString_;
    private Map<String, StyleElement> styleMap_;

    /**
     * Whether the Mouse is currently over this element or not.
     */
    private boolean mouseOver_;

    /**
     * Creates an instance of a DOM element that can have a namespace.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes a map ready initialized with the attributes for this element, or
     * {@code null}. The map will be stored as is, not copied.
     */
    public DomElement(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page);
        if (attributes != null && !attributes.isEmpty()) {
            attributes_ = new NamedAttrNodeMapImpl(this, isAttributeCaseSensitive(), attributes);
            for (final DomAttr entry : attributes_.values()) {
                entry.setParentNode(this);
                final String attrNamespaceURI = entry.getNamespaceURI();
                final String prefix = entry.getPrefix();
                if (attrNamespaceURI != null && prefix != null) {
                    namespaces_.put(attrNamespaceURI, prefix);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return getQualifiedName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final short getNodeType() {
        return ELEMENT_NODE;
    }

    /**
     * Returns the tag name of this element.
     * @return the tag name of this element
     */
    @Override
    public final String getTagName() {
        return getNodeName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean hasAttributes() {
        return !attributes_.isEmpty();
    }

    /**
     * Returns whether the attribute specified by name has a value.
     *
     * @param attributeName the name of the attribute
     * @return true if an attribute with the given name is specified on this element or has a
     * default value, false otherwise.
     */
    @Override
    public boolean hasAttribute(final String attributeName) {
        return attributes_.containsKey(attributeName);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Replaces the value of the named style attribute. If there is no style attribute with the
     * specified name, a new one is added. If the specified value is an empty (or all whitespace)
     * string, this method actually removes the named style attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     * @param value the attribute value
     * @param priority the new priority of the property; <code>"important"</code>or the empty string if none.
     */
    public void replaceStyleAttribute(final String name, final String value, final String priority) {
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            removeStyleAttribute(name);
            return;
        }

        final Map<String, StyleElement> styleMap = getStyleMap();
        final StyleElement old = styleMap.get(name);
        final StyleElement element;
        if (old == null) {
            element = new StyleElement(name, value, priority, SelectorSpecificity.FROM_STYLE_ATTRIBUTE);
        }
        else {
            element = new StyleElement(name, value, priority,
                    SelectorSpecificity.FROM_STYLE_ATTRIBUTE, old.getIndex());
        }
        styleMap.put(name, element);
        writeStyleToElement(styleMap);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Removes the specified style attribute, returning the value of the removed attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     * @return the removed value
     */
    public String removeStyleAttribute(final String name) {
        final Map<String, StyleElement> styleMap = getStyleMap();
        final StyleElement value = styleMap.get(name);
        if (value == null) {
            return "";
        }
        styleMap.remove(name);
        writeStyleToElement(styleMap);
        return value.getValue();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Determines the StyleElement for the given name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    public StyleElement getStyleElement(final String name) {
        final Map<String, StyleElement> map = getStyleMap();
        if (map != null) {
            return map.get(name);
        }
        return null;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Determines the StyleElement for the given name.
     * This ignores the case of the name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    public StyleElement getStyleElementCaseInSensitive(final String name) {
        final Map<String, StyleElement> map = getStyleMap();
        for (final Map.Entry<String, StyleElement> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns a sorted map containing style elements, keyed on style element name. We use a
     * {@link LinkedHashMap} map so that results are deterministic and are thus testable.
     *
     * @return a sorted map containing style elements, keyed on style element name
     */
    public Map<String, StyleElement> getStyleMap() {
        final String styleAttribute = getAttributeDirect("style");
        if (styleString_ == styleAttribute) {
            return styleMap_;
        }

        final Map<String, StyleElement> styleMap = new LinkedHashMap<>();
        if (DomElement.ATTRIBUTE_NOT_DEFINED == styleAttribute || DomElement.ATTRIBUTE_VALUE_EMPTY == styleAttribute) {
            styleMap_ = styleMap;
            styleString_ = styleAttribute;
            return styleMap_;
        }

        // TODO this should be done by using cssparser also
        for (final String token : org.apache.commons.lang3.StringUtils.split(styleAttribute, ';')) {
            final int index = token.indexOf(':');
            if (index != -1) {
                final String key = token.substring(0, index).trim().toLowerCase(Locale.ROOT);
                String value = token.substring(index + 1).trim();
                String priority = "";
                if (org.apache.commons.lang3.StringUtils.endsWithIgnoreCase(value, "!important")) {
                    priority = StyleElement.PRIORITY_IMPORTANT;
                    value = value.substring(0, value.length() - 10);
                    value = value.trim();
                }
                final StyleElement element = new StyleElement(key, value, priority,
                                                    SelectorSpecificity.FROM_STYLE_ATTRIBUTE);
                styleMap.put(key, element);
            }
        }

        styleMap_ = styleMap;
        styleString_ = styleAttribute;
        return styleMap_;
    }

    /**
     * Prints the content between "&lt;" and "&gt;" (or "/&gt;") in the output of the tag name
     * and its attributes in XML format.
     * @param printWriter the writer to print in
     */
    protected void printOpeningTagContentAsXml(final PrintWriter printWriter) {
        printWriter.print(getTagName());
        for (final Map.Entry<String, DomAttr> entry : attributes_.entrySet()) {
            printWriter.print(" ");
            printWriter.print(entry.getKey());
            printWriter.print("=\"");
            printWriter.print(StringUtils.escapeXmlAttributeValue(entry.getValue().getNodeValue()));
            printWriter.print("\"");
        }
    }

    /**
     * Recursively write the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        final boolean hasChildren = getFirstChild() != null;
        printWriter.print(indent + "<");
        printOpeningTagContentAsXml(printWriter);

        if (hasChildren || isEmptyXmlTagExpanded()) {
            printWriter.print(">\r\n");
            printChildrenAsXml(indent, printWriter);
            printWriter.print(indent);
            printWriter.print("</");
            printWriter.print(getTagName());
            printWriter.print(">\r\n");
        }
        else {
            printWriter.print("/>\r\n");
        }
    }

    /**
     * Indicates if a node without children should be written in expanded form as XML
     * (i.e. with closing tag rather than with "/&gt;")
     * @return {@code false} by default
     */
    protected boolean isEmptyXmlTagExpanded() {
        return false;
    }

    /**
     * Returns the qualified name (prefix:local) for the specified namespace and local name,
     * or {@code null} if the specified namespace URI does not exist.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param localName the name within the namespace
     * @return the qualified name for the specified namespace and local name
     */
    String getQualifiedName(final String namespaceURI, final String localName) {
        final String qualifiedName;
        if (namespaceURI == null) {
            qualifiedName = localName;
        }
        else {
            final String prefix = namespaces_.get(namespaceURI);
            if (prefix == null) {
                qualifiedName = null;
            }
            else {
                qualifiedName = prefix + ':' + localName;
            }
        }
        return qualifiedName;
    }

    /**
     * Returns the value of the attribute specified by name or an empty string. If the
     * result is an empty string then it will be either {@link #ATTRIBUTE_NOT_DEFINED}
     * if the attribute wasn't specified or {@link #ATTRIBUTE_VALUE_EMPTY} if the
     * attribute was specified but it was empty.
     *
     * @param attributeName the name of the attribute
     * @return the value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED} or {@link #ATTRIBUTE_VALUE_EMPTY}
     */
    @Override
    public String getAttribute(final String attributeName) {
        final DomAttr attr = attributes_.get(attributeName);
        if (attr != null) {
            return attr.getNodeValue();
        }
        return ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param attributeName the name of the attribute
     * @return the value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED} or {@link #ATTRIBUTE_VALUE_EMPTY}
     */
    public String getAttributeDirect(final String attributeName) {
        final DomAttr attr = attributes_.getDirect(attributeName);
        if (attr != null) {
            return attr.getNodeValue();
        }
        return ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * Removes an attribute specified by name from this element.
     * @param attributeName the attribute attributeName
     */
    @Override
    public void removeAttribute(final String attributeName) {
        attributes_.remove(attributeName);
    }

    /**
     * Removes an attribute specified by namespace and local name from this element.
     * @param namespaceURI the URI that identifies an XML namespace
     * @param localName the name within the namespace
     */
    @Override
    public final void removeAttributeNS(final String namespaceURI, final String localName) {
        final String qualifiedName = getQualifiedName(namespaceURI, localName);
        if (qualifiedName != null) {
            removeAttribute(qualifiedName);
        }
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public final Attr removeAttributeNode(final Attr attribute) {
        throw new UnsupportedOperationException("DomElement.removeAttributeNode is not yet implemented.");
    }

    /**
     * Returns whether the attribute specified by namespace and local name has a value.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param localName the name within the namespace
     * @return true if an attribute with the given name is specified on this element or has a
     * default value, false otherwise.
     */
    @Override
    public final boolean hasAttributeNS(final String namespaceURI, final String localName) {
        final String qualifiedName = getQualifiedName(namespaceURI, localName);
        if (qualifiedName != null) {
            return attributes_.get(qualifiedName) != null;
        }
        return false;
    }

    /**
     * Returns the map holding the attributes, keyed by name.
     * @return the attributes map
     */
    public final Map<String, DomAttr> getAttributesMap() {
        return attributes_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamedNodeMap getAttributes() {
        return attributes_;
    }

    /**
     * Sets the value of the attribute specified by name.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     */
    @Override
    public void setAttribute(final String attributeName, final String attributeValue) {
        setAttributeNS(null, attributeName, attributeValue);
    }

    /**
     * Sets the value of the attribute specified by namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name (prefix:local) of the attribute
     * @param attributeValue the value of the attribute
     */
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        setAttributeNS(namespaceURI, qualifiedName, attributeValue, true, true);
    }

    /**
     * Sets the value of the attribute specified by namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name (prefix:local) of the attribute
     * @param attributeValue the value of the attribute
     * @param notifyAttributeChangeListeners to notify the associated {@link HtmlAttributeChangeListener}s
     * @param notifyMutationObservers to notify {@code MutationObserver}s or not
     */
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName,
            final String attributeValue, final boolean notifyAttributeChangeListeners,
            final boolean notifyMutationObservers) {
        final String value = attributeValue;
        final DomAttr newAttr = new DomAttr(getPage(), namespaceURI, qualifiedName, value, true);
        newAttr.setParentNode(this);
        attributes_.put(qualifiedName, newAttr);

        if (namespaceURI != null) {
            namespaces_.put(namespaceURI, newAttr.getPrefix());
        }
    }

    /**
     * Indicates if the attribute names are case sensitive.
     * @return {@code true}
     */
    protected boolean isAttributeCaseSensitive() {
        return true;
    }

    /**
     * Returns the value of the attribute specified by namespace and local name or an empty
     * string. If the result is an empty string then it will be either {@link #ATTRIBUTE_NOT_DEFINED}
     * if the attribute wasn't specified or {@link #ATTRIBUTE_VALUE_EMPTY} if the
     * attribute was specified but it was empty.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param localName the name within the namespace
     * @return the value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED} or {@link #ATTRIBUTE_VALUE_EMPTY}
     */
    @Override
    public final String getAttributeNS(final String namespaceURI, final String localName) {
        final String qualifiedName = getQualifiedName(namespaceURI, localName);
        if (qualifiedName != null) {
            return getAttribute(qualifiedName);
        }
        return ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr getAttributeNode(final String name) {
        return attributes_.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr getAttributeNodeNS(final String namespaceURI, final String localName) {
        final String qualifiedName = getQualifiedName(namespaceURI, localName);
        if (qualifiedName != null) {
            return attributes_.get(qualifiedName);
        }
        return null;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param styleMap the styles
     */
    public void writeStyleToElement(final Map<String, StyleElement> styleMap) {
        final StringBuilder builder = new StringBuilder();
        final SortedSet<StyleElement> sortedValues = new TreeSet<>(styleMap.values());
        for (final StyleElement e : sortedValues) {
            if (builder.length() != 0) {
                builder.append(' ');
            }
            builder.append(e.getName());
            builder.append(": ");
            builder.append(e.getValue());

            final String prio = e.getPriority();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(prio)) {
                builder.append(" !");
                builder.append(prio);
            }
            builder.append(';');
        }
        final String value = builder.toString();
        setAttribute("style", value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNodeList<HtmlElement> getElementsByTagName(final String tagName) {
        return getElementsByTagNameImpl(tagName);
    }

    /**
     * This should be {@link #getElementsByTagName(String)}, but is separate because of the type erasure in Java.
     * @param tagName The name of the tag to match on
     * @return A list of matching elements.
     */
    <E extends HtmlElement> DomNodeList<E> getElementsByTagNameImpl(final String tagName) {
        return new AbstractDomNodeList<E>(this) {
            @Override
            @SuppressWarnings("unchecked")
            protected List<E> provideElements() {
                final List<E> res = new LinkedList<>();
                for (final HtmlElement elem : getDomNode().getHtmlElementDescendants()) {
                    if (elem.getLocalName().equalsIgnoreCase(tagName)) {
                        res.add((E) elem);
                    }
                }
                return res;
            }
        };
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public DomNodeList<HtmlElement> getElementsByTagNameNS(final String namespace, final String localName) {
        throw new UnsupportedOperationException("DomElement.getElementsByTagNameNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("DomElement.getSchemaTypeInfo is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setIdAttribute(final String name, final boolean isId) {
        throw new UnsupportedOperationException("DomElement.setIdAttribute is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setIdAttributeNS(final String namespaceURI, final String localName, final boolean isId) {
        throw new UnsupportedOperationException("DomElement.setIdAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attr setAttributeNode(final Attr attribute) {
        attributes_.setNamedItem(attribute);
        return null;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Attr setAttributeNodeNS(final Attr attribute) {
        throw new UnsupportedOperationException("DomElement.setAttributeNodeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public final void setIdAttributeNode(final Attr idAttr, final boolean isId) {
        throw new UnsupportedOperationException("DomElement.setIdAttributeNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final DomElement clone = (DomElement) super.cloneNode(deep);
        clone.attributes_ = new NamedAttrNodeMapImpl(clone, isAttributeCaseSensitive());
        clone.attributes_.putAll(attributes_);
        return clone;
    }

    /**
     * @return the identifier of this element
     */
    public final String getId() {
        return getAttributeDirect("id");
    }

    /**
     * Sets the identifier this element.
     *
     * @param newId the new identifier of this element
     */
    public final void setId(final String newId) {
        setAttribute("id", newId);
    }

    /**
     * Returns the first child element node of this element. null if this element has no child elements.
     * @return the first child element node of this element. null if this element has no child elements
     */
    public DomElement getFirstElementChild() {
        final Iterator<DomElement> i = getChildElements().iterator();
        if (i.hasNext()) {
            return i.next();
        }
        return null;
    }

    /**
     * Returns the last child element node of this element. null if this element has no child elements.
     * @return the last child element node of this element. null if this element has no child elements
     */
    public DomElement getLastElementChild() {
        DomElement lastChild = null;
        final Iterator<DomElement> i = getChildElements().iterator();
        while (i.hasNext()) {
            lastChild = i.next();
        }
        return lastChild;
    }

    /**
     * Returns the current number of element nodes that are children of this element.
     * @return the current number of element nodes that are children of this element.
     */
    public int getChildElementCount() {
        int counter = 0;
        for (final Iterator<DomElement> i = getChildElements().iterator(); i.hasNext(); i.next()) {
            counter++;
        }
        return counter;
    }

    /**
     * @return an Iterable over the DomElement children of this object, i.e. excluding the non-element nodes
     */
    public final Iterable<DomElement> getChildElements() {
        return new ChildElementsIterable(this);
    }

    /**
     * An Iterable over the DomElement children.
     */
    private static class ChildElementsIterable implements Iterable<DomElement> {
        private final Iterator<DomElement> iterator_;

        /** Constructor.
         * @param domNode the parent
         */
        protected ChildElementsIterable(final DomNode domNode) {
            iterator_ = new ChildElementsIterator(domNode);
        }

        @Override
        public Iterator<DomElement> iterator() {
            return iterator_;
        }
    }

    /**
     * An iterator over the DomElement children.
     */
    protected static class ChildElementsIterator implements Iterator<DomElement> {

        private DomElement nextElement_;

        /** Constructor.
         * @param domNode the parent
         */
        protected ChildElementsIterator(final DomNode domNode) {
            final DomNode child = domNode.getFirstChild();
            if (child != null) {
                if (child instanceof DomElement) {
                    nextElement_ = (DomElement) child;
                }
                else {
                    setNextElement(child);
                }
            }
        }

        /** @return is there a next one ? */
        @Override
        public boolean hasNext() {
            return nextElement_ != null;
        }

        /** @return the next one */
        @Override
        public DomElement next() {
            if (nextElement_ != null) {
                final DomElement result = nextElement_;
                setNextElement(nextElement_);
                return result;
            }
            throw new NoSuchElementException();
        }

        /** Removes the current one. */
        @Override
        public void remove() {
            if (nextElement_ == null) {
                throw new IllegalStateException();
            }
            final DomNode sibling = nextElement_.getPreviousSibling();
            if (sibling != null) {
                sibling.remove();
            }
        }

        private void setNextElement(final DomNode node) {
            DomNode next = node.getNextSibling();
            while (next != null && !(next instanceof DomElement)) {
                next = next.getNextSibling();
            }
            nextElement_ = (DomElement) next;
        }
    }

    /**
     * Returns a string representation of this element.
     * @return a string representation of this element
     */
    @Override
    public String toString() {
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);

        printWriter.print(getClass().getSimpleName());
        printWriter.print("[<");
        printOpeningTagContentAsXml(printWriter);
        printWriter.print(">]");
        printWriter.flush();
        return writer.toString();
    }

    /**
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.<br>
     * This only clicks the element if it is visible and enabled (isDisplayed() &amp; !isDisabled()).
     * In case the element is not visible and/or disabled, only a log output is generated.
     * <br>
     * If you circumvent the visible/disabled check use click(shiftKey, ctrlKey, altKey, true, true, false)
     *
     * @param <P> the page type
     * @return the page contained in the current window as returned by {@link WebClient#getCurrentWindow()}
     * @exception IOException if an IO error occurs
     */
    public <P extends Page> P click() throws IOException {
        return click(false, false, false);
    }

    /**
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.<br>
     * This only clicks the element if it is visible and enabled (isDisplayed() &amp; !isDisabled()).
     * In case the element is not visible and/or disabled, only a log output is generated.
     * <br>
     * If you circumvent the visible/disabled check use click(shiftKey, ctrlKey, altKey, true, true, false)
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the click
     * @param ctrlKey {@code true} if CTRL is pressed during the click
     * @param altKey {@code true} if ALT is pressed during the click
     * @param <P> the page type
     * @return the page contained in the current window as returned by {@link WebClient#getCurrentWindow()}
     * @exception IOException if an IO error occurs
     */
    public <P extends Page> P click(final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {

        return click(shiftKey, ctrlKey, altKey, true);
    }

    /**
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.<br>
     * This only clicks the element if it is visible and enabled (isDisplayed() &amp; !isDisabled()).
     * In case the element is not visible and/or disabled, only a log output is generated.
     * <br>
     * If you circumvent the visible/disabled check use click(shiftKey, ctrlKey, altKey, true, true, false)
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the click
     * @param ctrlKey {@code true} if CTRL is pressed during the click
     * @param altKey {@code true} if ALT is pressed during the click
     * @param triggerMouseEvents if true trigger the mouse events also
     * @param <P> the page type
     * @return the page contained in the current window as returned by {@link WebClient#getCurrentWindow()}
     * @exception IOException if an IO error occurs
     */
    public <P extends Page> P click(final boolean shiftKey, final boolean ctrlKey, final boolean altKey,
            final boolean triggerMouseEvents) throws IOException {
        return click(shiftKey, ctrlKey, altKey, triggerMouseEvents, false, false);
    }

    /**
     * @return true if this is an {@link DisabledElement} and disabled
     */
    protected boolean isDisabledElementAndDisabled() {
        return this instanceof DisabledElement && ((DisabledElement) this).isDisabled();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the click
     * @param ctrlKey {@code true} if CTRL is pressed during the click
     * @param altKey {@code true} if ALT is pressed during the click
     * @param triggerMouseEvents if true trigger the mouse events also
     * @param ignoreVisibility whether to ignore visibility or not
     * @param disableProcessLabelAfterBubbling ignore label processing
     * @param <P> the page type
     * @return the page contained in the current window as returned by {@link WebClient#getCurrentWindow()}
     * @exception IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final boolean shiftKey, final boolean ctrlKey, final boolean altKey,
            final boolean triggerMouseEvents, final boolean ignoreVisibility,
            final boolean disableProcessLabelAfterBubbling) throws IOException {

        // make enclosing window the current one
        final SgmlPage page = getPage();
        page.getWebClient().setCurrentWindow(page.getEnclosingWindow());

        if (!ignoreVisibility) {
            if (!(page instanceof HtmlPage)) {
                return (P) page;
            }

            if (!isDisplayed()) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Calling click() ignored because the target element '" + toString()
                                    + "' is not displayed.");
                }
                return (P) page;
            }

            if (isDisabledElementAndDisabled()) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Calling click() ignored because the target element '" + toString() + "' is disabled.");
                }
                return (P) page;
            }
        }

        synchronized (page) {
            if (triggerMouseEvents) {
                mouseDown(shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_LEFT);
            }

            // give focus to current element (if possible) or only remove it from previous one
            DomElement elementToFocus = null;
            if (this instanceof SubmittableElement
                || this instanceof HtmlAnchor
                    && ((HtmlAnchor) this).getHrefAttribute() != DomElement.ATTRIBUTE_NOT_DEFINED
                || this instanceof HtmlArea
                    && (((HtmlArea) this).getHrefAttribute() != DomElement.ATTRIBUTE_NOT_DEFINED
                        || getPage().getWebClient().getBrowserVersion().hasFeature(JS_AREA_WITHOUT_HREF_FOCUSABLE))
                || this instanceof HtmlElement && ((HtmlElement) this).getTabIndex() != null) {
                elementToFocus = this;
            }
            else if (this instanceof HtmlOption) {
                elementToFocus = ((HtmlOption) this).getEnclosingSelect();
            }

            if (elementToFocus == null) {
                ((HtmlPage) page).setFocusedElement(null);
            }
            else {
                elementToFocus.focus();
            }

            if (triggerMouseEvents) {
                mouseUp(shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_LEFT);
            }

            MouseEvent event = null;
            if (page.getWebClient().isJavaScriptEnabled()) {
                if (page.getWebClient().getBrowserVersion().hasFeature(EVENT_ONCLICK_USES_POINTEREVENT)) {
                    event = new PointerEvent(getEventTargetElement(), MouseEvent.TYPE_CLICK, shiftKey,
                            ctrlKey, altKey, MouseEvent.BUTTON_LEFT);
                }
                else {
                    event = new MouseEvent(getEventTargetElement(), MouseEvent.TYPE_CLICK, shiftKey,
                            ctrlKey, altKey, MouseEvent.BUTTON_LEFT);
                }

                if (disableProcessLabelAfterBubbling) {
                    event.disableProcessLabelAfterBubbling();
                }
            }
            return click(event, shiftKey, ctrlKey, altKey, ignoreVisibility);
        }
    }

    /**
     * Returns the event target element. This could be overridden by subclasses to have other targets.
     * The default implementation returns 'this'.
     * @return the event target element.
     */
    protected DomNode getEventTargetElement() {
        return this;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.
     *
     * @param event the click event used
     * @param shiftKey {@code true} if SHIFT is pressed during the click
     * @param ctrlKey {@code true} if CTRL is pressed during the click
     * @param altKey {@code true} if ALT is pressed during the click
     * @param ignoreVisibility whether to ignore visibility or not
     * @param <P> the page type
     * @return the page contained in the current window as returned by {@link WebClient#getCurrentWindow()}
     * @exception IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final Event event,
                        final boolean shiftKey, final boolean ctrlKey, final boolean altKey,
                        final boolean ignoreVisibility) throws IOException {
        final SgmlPage page = getPage();

        if ((!ignoreVisibility && !isDisplayed()) || isDisabledElementAndDisabled()) {
            return (P) page;
        }

        if (!page.getWebClient().isJavaScriptEnabled()) {
            doClickStateUpdate(shiftKey, ctrlKey);
            page.getWebClient().loadDownloadedResponses();
            return (P) getPage().getWebClient().getCurrentWindow().getEnclosedPage();
        }

        // may be different from page when working with "orphaned pages"
        // (ex: clicking a link in a page that is not active anymore)
        final Page contentPage = page.getEnclosingWindow().getEnclosedPage();

        boolean stateUpdated = false;
        boolean changed = false;
        if (isStateUpdateFirst()) {
            changed = doClickStateUpdate(shiftKey, ctrlKey);
            stateUpdated = true;
        }

        final AbstractJavaScriptEngine<?> jsEngine = page.getWebClient().getJavaScriptEngine();
        jsEngine.holdPosponedActions();
        try {
            final ScriptResult scriptResult = doClickFireClickEvent(event);
            final boolean eventIsAborted = event.isAborted(scriptResult);

            final boolean pageAlreadyChanged = contentPage != page.getEnclosingWindow().getEnclosedPage();
            if (!pageAlreadyChanged && !stateUpdated && !eventIsAborted) {
                changed = doClickStateUpdate(shiftKey, ctrlKey);
            }
        }
        finally {
            jsEngine.processPostponedActions();
        }

        if (changed) {
            doClickFireChangeEvent();
        }

        return (P) getPage().getWebClient().getCurrentWindow().getEnclosedPage();
    }

    /**
     * This method implements the control state update part of the click action.
     *
     * <p>The default implementation only calls doClickStateUpdate on parent's DomElement (if any).
     * Subclasses requiring different behavior (like {@link HtmlSubmitInput}) will override this method.</p>
     * @param shiftKey {@code true} if SHIFT is pressed
     * @param ctrlKey {@code true} if CTRL is pressed
     *
     * @return true if doClickFireEvent method has to be called later on (to signal,
     * that the value was changed)
     * @throws IOException if an IO error occurs
     */
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        if (propagateClickStateUpdateToParent()) {
            // needed for instance to perform link doClickAction when a nested element is clicked
            // it should probably be changed to do this at the event level but currently
            // this wouldn't work with JS disabled as events are propagated in the host object tree.
            final DomNode parent = getParentNode();
            if (parent instanceof DomElement) {
                return ((DomElement) parent).doClickStateUpdate(false, false);
            }
        }

        return false;
    }

    /**
     * @see #doClickStateUpdate(boolean, boolean)
     * Usually the click is propagated to the parent. Overwrite if you
     * like to disable this.
     *
     * @return true or false
     */
    protected boolean propagateClickStateUpdateToParent() {
        return true;
    }

    /**
     * This method implements the control onchange handler call during the click action.
     */
    protected void doClickFireChangeEvent() {
        // nothing to do, in the default case
    }

    /**
     * This method implements the control onclick handler call during the click action.
     * @param event the click event used
     * @return the script result
     */
    protected ScriptResult doClickFireClickEvent(final Event event) {
        return fireEvent(event);
    }

    /**
     * Simulates double-clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc. Note also that {@link #click()} is automatically called first.
     *
     * @param <P> the page type
     * @return the page that occupies this element's window after the element has been double-clicked
     * @exception IOException if an IO error occurs
     */
    public <P extends Page> P dblClick() throws IOException {
        return dblClick(false, false, false);
    }

    /**
     * Simulates double-clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc. Note also that {@link #click(boolean, boolean, boolean)} is automatically
     * called first.
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the double-click
     * @param ctrlKey {@code true} if CTRL is pressed during the double-click
     * @param altKey {@code true} if ALT is pressed during the double-click
     * @param <P> the page type
     * @return the page that occupies this element's window after the element has been double-clicked
     * @exception IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P dblClick(final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if (isDisabledElementAndDisabled()) {
            return (P) getPage();
        }

        // call click event first
        P clickPage = click(shiftKey, ctrlKey, altKey);
        if (clickPage != getPage()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("dblClick() is ignored, as click() loaded a different page.");
            }
            return clickPage;
        }

        // call click event a second time
        clickPage = click(shiftKey, ctrlKey, altKey);
        if (clickPage != getPage()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("dblClick() is ignored, as click() loaded a different page.");
            }
            return clickPage;
        }

        final Event event;
        if (getPage().getWebClient().getBrowserVersion().hasFeature(EVENT_ONCLICK_USES_POINTEREVENT)) {
            event = new PointerEvent(this, MouseEvent.TYPE_DBL_CLICK, shiftKey, ctrlKey, altKey,
                    MouseEvent.BUTTON_LEFT);
        }
        else {
            event = new MouseEvent(this, MouseEvent.TYPE_DBL_CLICK, shiftKey, ctrlKey, altKey,
                    MouseEvent.BUTTON_LEFT);
        }
        final ScriptResult scriptResult = fireEvent(event);
        if (scriptResult == null) {
            return clickPage;
        }
        return (P) getPage().getWebClient().getCurrentWindow().getEnclosedPage();
    }

    /**
     * Simulates moving the mouse over this element, returning the page which this element's window contains
     * after the mouse move. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @return the page which this element's window contains after the mouse move
     */
    public Page mouseOver() {
        return mouseOver(false, false, false, MouseEvent.BUTTON_LEFT);
    }

    /**
     * Simulates moving the mouse over this element, returning the page which this element's window contains
     * after the mouse move. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the mouse move
     * @param ctrlKey {@code true} if CTRL is pressed during the mouse move
     * @param altKey {@code true} if ALT is pressed during the mouse move
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     * @return the page which this element's window contains after the mouse move
     */
    public Page mouseOver(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_OVER, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulates moving the mouse over this element, returning the page which this element's window contains
     * after the mouse move. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @return the page which this element's window contains after the mouse move
     */
    public Page mouseMove() {
        return mouseMove(false, false, false, MouseEvent.BUTTON_LEFT);
    }

    /**
     * Simulates moving the mouse over this element, returning the page which this element's window contains
     * after the mouse move. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the mouse move
     * @param ctrlKey {@code true} if CTRL is pressed during the mouse move
     * @param altKey {@code true} if ALT is pressed during the mouse move
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     * @return the page which this element's window contains after the mouse move
     */
    public Page mouseMove(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_MOVE, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulates moving the mouse out of this element, returning the page which this element's window contains
     * after the mouse move. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @return the page which this element's window contains after the mouse move
     */
    public Page mouseOut() {
        return mouseOut(false, false, false, MouseEvent.BUTTON_LEFT);
    }

    /**
     * Simulates moving the mouse out of this element, returning the page which this element's window contains
     * after the mouse move. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the mouse move
     * @param ctrlKey {@code true} if CTRL is pressed during the mouse move
     * @param altKey {@code true} if ALT is pressed during the mouse move
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     * @return the page which this element's window contains after the mouse move
     */
    public Page mouseOut(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_OUT, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulates clicking the mouse on this element, returning the page which this element's window contains
     * after the mouse click. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @return the page which this element's window contains after the mouse click
     */
    public Page mouseDown() {
        return mouseDown(false, false, false, MouseEvent.BUTTON_LEFT);
    }

    /**
     * Simulates clicking the mouse on this element, returning the page which this element's window contains
     * after the mouse click. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the mouse click
     * @param ctrlKey {@code true} if CTRL is pressed during the mouse click
     * @param altKey {@code true} if ALT is pressed during the mouse click
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     * @return the page which this element's window contains after the mouse click
     */
    public Page mouseDown(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_DOWN, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulates releasing the mouse click on this element, returning the page which this element's window contains
     * after the mouse click release. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @return the page which this element's window contains after the mouse click release
     */
    public Page mouseUp() {
        return mouseUp(false, false, false, MouseEvent.BUTTON_LEFT);
    }

    /**
     * Simulates releasing the mouse click on this element, returning the page which this element's window contains
     * after the mouse click release. The returned page may or may not be the same as the original page, depending
     * on JavaScript event handlers, etc.
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the mouse click release
     * @param ctrlKey {@code true} if CTRL is pressed during the mouse click release
     * @param altKey {@code true} if ALT is pressed during the mouse click release
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     * @return the page which this element's window contains after the mouse click release
     */
    public Page mouseUp(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_UP, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulates right clicking the mouse on this element, returning the page which this element's window
     * contains after the mouse click. The returned page may or may not be the same as the original page,
     * depending on JavaScript event handlers, etc.
     *
     * @return the page which this element's window contains after the mouse click
     */
    public Page rightClick() {
        return rightClick(false, false, false);
    }

    /**
     * Simulates right clicking the mouse on this element, returning the page which this element's window
     * contains after the mouse click. The returned page may or may not be the same as the original page,
     * depending on JavaScript event handlers, etc.
     *
     * @param shiftKey {@code true} if SHIFT is pressed during the mouse click
     * @param ctrlKey {@code true} if CTRL is pressed during the mouse click
     * @param altKey {@code true} if ALT is pressed during the mouse click
     * @return the page which this element's window contains after the mouse click
     */
    public Page rightClick(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        final Page mouseDownPage = mouseDown(shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_RIGHT);
        if (mouseDownPage != getPage()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("rightClick() is incomplete, as mouseDown() loaded a different page.");
            }
            return mouseDownPage;
        }
        final Page mouseUpPage = mouseUp(shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_RIGHT);
        if (mouseUpPage != getPage()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("rightClick() is incomplete, as mouseUp() loaded a different page.");
            }
            return mouseUpPage;
        }
        return doMouseEvent(MouseEvent.TYPE_CONTEXT_MENU, shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_RIGHT);
    }

    /**
     * Simulates the specified mouse event, returning the page which this element's window contains after the event.
     * The returned page may or may not be the same as the original page, depending on JavaScript event handlers, etc.
     *
     * @param eventType the mouse event type to simulate
     * @param shiftKey {@code true} if SHIFT is pressed during the mouse event
     * @param ctrlKey {@code true} if CTRL is pressed during the mouse event
     * @param altKey {@code true} if ALT is pressed during the mouse event
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     * @return the page which this element's window contains after the event
     */
    private Page doMouseEvent(final String eventType, final boolean shiftKey, final boolean ctrlKey,
        final boolean altKey, final int button) {
        final SgmlPage page = getPage();
        if (!page.getWebClient().isJavaScriptEnabled()) {
            return page;
        }

        final ScriptResult scriptResult;
        final Event event;
        if (MouseEvent.TYPE_CONTEXT_MENU.equals(eventType)
                && getPage().getWebClient().getBrowserVersion().hasFeature(EVENT_ONCLICK_USES_POINTEREVENT)) {
            event = new PointerEvent(this, eventType, shiftKey, ctrlKey, altKey, button);
        }
        else {
            event = new MouseEvent(this, eventType, shiftKey, ctrlKey, altKey, button);
        }
        scriptResult = fireEvent(event);

        final Page currentPage;
        if (scriptResult == null) {
            currentPage = page;
        }
        else {
            currentPage = page.getWebClient().getCurrentWindow().getEnclosedPage();
        }

        final boolean mouseOver = !MouseEvent.TYPE_MOUSE_OUT.equals(eventType);
        if (mouseOver_ != mouseOver) {
            mouseOver_ = mouseOver;

            final SimpleScriptable scriptable = getScriptableObject();
            scriptable.getWindow().clearComputedStyles();
        }

        return currentPage;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Shortcut for {@link #fireEvent(Event)}.
     * @param eventType the event type (like "load", "click")
     * @return the execution result, or {@code null} if nothing is executed
     */
    public ScriptResult fireEvent(final String eventType) {
        if (getPage().getWebClient().isJavaScriptEnabled()) {
            return fireEvent(new Event(this, eventType));
        }
        return null;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Fires the event on the element. Nothing is done if JavaScript is disabled.
     * @param event the event to fire
     * @return the execution result, or {@code null} if nothing is executed
     */
    public ScriptResult fireEvent(final Event event) {
        final WebClient client = getPage().getWebClient();
        if (!client.isJavaScriptEnabled()) {
            return null;
        }

        if (!handles(event)) {
            return null;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Firing " + event);
        }

        final EventTarget jsElt = getScriptableObject();
        final HtmlUnitContextFactory cf = ((JavaScriptEngine) client.getJavaScriptEngine()).getContextFactory();
        final ScriptResult result = cf.callSecured(cx -> jsElt.fireEvent(event), getHtmlPageOrNull());
        if (event.isAborted(result)) {
            preventDefault();
        }
        return result;
    }

    /**
     * This method is called if the current fired event is canceled by <tt>preventDefault()</tt> in FireFox,
     * or by returning {@code false} in Internet Explorer.
     *
     * The default implementation does nothing.
     */
    protected void preventDefault() {
        // Empty by default; override as needed.
    }

    /**
     * Sets the focus on this element.
     */
    public void focus() {
        if (!(this instanceof SubmittableElement
            || this instanceof HtmlAnchor && ((HtmlAnchor) this).getHrefAttribute() != DomElement.ATTRIBUTE_NOT_DEFINED
            || this instanceof HtmlArea
                && (((HtmlArea) this).getHrefAttribute() != DomElement.ATTRIBUTE_NOT_DEFINED
                    || getPage().getWebClient().getBrowserVersion().hasFeature(JS_AREA_WITHOUT_HREF_FOCUSABLE))
            || this instanceof HtmlElement && ((HtmlElement) this).getTabIndex() != null)) {
            return;
        }

        if (!isDisplayed() || isDisabledElementAndDisabled()) {
            return;
        }

        final HtmlPage page = (HtmlPage) getPage();
        page.setFocusedElement(this);
    }

    /**
     * Removes focus from this element.
     */
    public void blur() {
        final HtmlPage page = (HtmlPage) getPage();
        if (page.getFocusedElement() != this) {
            return;
        }

        page.setFocusedElement(null);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Gets notified that it has lost the focus.
     */
    public void removeFocus() {
        // nothing
    }

    /**
     * Returns {@code true} if state updates should be done before onclick event handling. This method
     * returns {@code false} by default, and is expected to be overridden to return {@code true} by
     * derived classes like {@link HtmlCheckBoxInput}.
     * @return {@code true} if state updates should be done before onclick event handling
     */
    protected boolean isStateUpdateFirst() {
        return false;
    }

    /**
     * Returns whether the Mouse is currently over this element or not.
     * @return whether the Mouse is currently over this element or not
     */
    public boolean isMouseOver() {
        if (mouseOver_) {
            return true;
        }
        for (final DomElement child : getChildElements()) {
            if (child.isMouseOver()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the element would be selected by the specified selector string; otherwise, returns false.
     * @param selectorString the selector to test
     * @return true if the element would be selected by the specified selector string; otherwise, returns false.
     */
    public boolean matches(final String selectorString) {
        try {
            final BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();
            final SelectorList selectorList = getSelectorList(selectorString, browserVersion);

            if (selectorList != null) {
                for (final Selector selector : selectorList) {
                    if (CSSStyleSheet.selects(browserVersion, selector, this, null, true)) {
                        return true;
                    }
                }
            }
            return false;
        }
        catch (final IOException e) {
            throw new CSSException("Error parsing CSS selectors from '" + selectorString + "': " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String value) {
        // Default behavior is to do nothing, overridden in some subclasses
    }
}

/**
 * The {@link NamedNodeMap} to store the node attributes.
 */
class NamedAttrNodeMapImpl implements Map<String, DomAttr>, NamedNodeMap, Serializable {
    protected static final NamedAttrNodeMapImpl EMPTY_MAP = new NamedAttrNodeMapImpl();
    private static final DomAttr[] EMPTY_ARRAY = new DomAttr[0];

    private final Map<String, DomAttr> map_ = new LinkedHashMap<>();
    private boolean dirty_ = false;
    private DomAttr[] attrPositions_ = EMPTY_ARRAY;
    private final DomElement domNode_;
    private final boolean caseSensitive_;

    private NamedAttrNodeMapImpl() {
        super();
        domNode_ = null;
        caseSensitive_ = true;
    }

    NamedAttrNodeMapImpl(final DomElement domNode, final boolean caseSensitive) {
        super();
        if (domNode == null) {
            throw new IllegalArgumentException("Provided domNode can't be null.");
        }
        domNode_ = domNode;
        caseSensitive_ = caseSensitive;
    }

    NamedAttrNodeMapImpl(final DomElement domNode, final boolean caseSensitive,
            final Map<String, DomAttr> attributes) {
        this(domNode, caseSensitive);
        putAll(attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr getNamedItem(final String name) {
        return get(name);
    }

    private String fixName(final String name) {
        if (caseSensitive_) {
            return name;
        }
        return name.toLowerCase(Locale.ROOT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNamedItemNS(final String namespaceURI, final String localName) {
        if (domNode_ == null) {
            return null;
        }
        return get(domNode_.getQualifiedName(namespaceURI, fixName(localName)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node item(final int index) {
        if (index < 0 || index >= map_.size()) {
            return null;
        }
        if (dirty_) {
            attrPositions_ = map_.values().toArray(attrPositions_);
            dirty_ = false;
        }
        return attrPositions_[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node removeNamedItem(final String name) throws DOMException {
        return remove(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node removeNamedItemNS(final String namespaceURI, final String localName) {
        if (domNode_ == null) {
            return null;
        }
        return remove(domNode_.getQualifiedName(namespaceURI, fixName(localName)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr setNamedItem(final Node node) {
        return put(node.getLocalName(), (DomAttr) node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node setNamedItemNS(final Node node) throws DOMException {
        return put(node.getNodeName(), (DomAttr) node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr put(final String key, final DomAttr value) {
        final String name = fixName(key);
        dirty_ = true;
        return map_.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr remove(final Object key) {
        if (key instanceof String) {
            final String name = fixName((String) key);
            dirty_ = true;
            return map_.remove(name);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        dirty_ = true;
        map_.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(final Map<? extends String, ? extends DomAttr> t) {
        // add one after the other to save the positions
        for (final Map.Entry<? extends String, ? extends DomAttr> entry : t.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(final Object key) {
        if (key instanceof String) {
            final String name = fixName((String) key);
            return map_.containsKey(name);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr get(final Object key) {
        if (key instanceof String) {
            final String name = fixName((String) key);
            return map_.get(name);
        }
        return null;
    }

    /**
     * Fast access.
     * @param key the key
     */
    protected DomAttr getDirect(final String key) {
        return map_.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(final Object value) {
        return map_.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Map.Entry<String, DomAttr>> entrySet() {
        return map_.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return map_.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> keySet() {
        return map_.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return map_.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DomAttr> values() {
        return map_.values();
    }
}
