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
package com.gargoylesoftware.htmlunit.html;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.ElementTraversal;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 */
public class DomElement extends DomNamespaceNode implements Element, ElementTraversal {

    /** Constant meaning that the specified attribute was not defined. */
    public static final String ATTRIBUTE_NOT_DEFINED = new String("");

    /** Constant meaning that the specified attribute was found but its value was empty. */
    public static final String ATTRIBUTE_VALUE_EMPTY = new String();

    /** The map holding the attributes, keyed by name. */
    private NamedAttrNodeMapImpl attributes_ = new NamedAttrNodeMapImpl(this, isAttributeCaseSensitive());

    /** The map holding the namespaces, keyed by URI. */
    private Map<String, String> namespaces_ = new HashMap<String, String>();

    /**
     * Creates an instance of a DOM element that can have a namespace.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes a map ready initialized with the attributes for this element, or
     * <code>null</code>. The map will be stored as is, not copied.
     */
    public DomElement(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page);
        if (attributes != null && !attributes.isEmpty()) {
            attributes_ = new NamedAttrNodeMapImpl(this, isAttributeCaseSensitive(), attributes);
            for (final DomAttr entry : attributes_.values()) {
                entry.setParentNode(this);
                final String attrNamespaceURI = entry.getNamespaceURI();
                if (attrNamespaceURI != null) {
                    namespaces_.put(attrNamespaceURI, entry.getPrefix());
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
     * Returns namespaces.
     * @return namespaces
     */
    //TODO: must be removed.
    protected Map<String, String> namespaces() {
        return namespaces_;
    }

    /**
     * Returns the tag name of this element.
     * @return the tag name of this element
     */
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
    public boolean hasAttribute(final String attributeName) {
        return attributes_.containsKey(attributeName);
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
        final boolean hasChildren = (getFirstChild() != null);
        printWriter.print(indent + "<");
        printOpeningTagContentAsXml(printWriter);

        if (!hasChildren && !isEmptyXmlTagExpanded()) {
            printWriter.println("/>");
        }
        else {
            printWriter.println(">");
            printChildrenAsXml(indent, printWriter);
            printWriter.println(indent + "</" + getTagName() + ">");
        }
    }

    /**
     * Indicates if a node without children should be written in expanded form as XML
     * (i.e. with closing tag rather than with "/&gt;")
     * @return <code>false</code> by default
     */
    protected boolean isEmptyXmlTagExpanded() {
        return false;
    }

    /**
     * Returns the qualified name (prefix:local) for the specified namespace and local name,
     * or <tt>null</tt> if the specified namespace URI does not exist.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param localName the name within the namespace
     * @return the qualified name for the specified namespace and local name
     */
    String getQualifiedName(final String namespaceURI, final String localName) {
        final String qualifiedName;
        if (namespaceURI != null) {
            final String prefix = namespaces().get(namespaceURI);
            if (prefix != null) {
                qualifiedName = prefix + ':' + localName;
            }
            else {
                qualifiedName = null;
            }
        }
        else {
            qualifiedName = localName;
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
    public String getAttribute(final String attributeName) {
        final DomAttr attr = attributes_.get(attributeName);
        if (attr != null) {
            return attr.getNodeValue();
        }
        return ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * Removes an attribute specified by name from this element.
     * @param attributeName the attribute attributeName
     */
    public void removeAttribute(String attributeName) {
        if (this instanceof HtmlElement) {
            attributeName = attributeName.toLowerCase();
        }
        attributes_.remove(attributeName);
    }

    /**
     * Removes an attribute specified by namespace and local name from this element.
     * @param namespaceURI the URI that identifies an XML namespace
     * @param localName the name within the namespace
     */
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
    public final void setAttribute(final String attributeName, final String attributeValue) {
        setAttributeNS(null, attributeName, attributeValue);
    }

    /**
     * Sets the value of the attribute specified by namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name (prefix:local) of the attribute
     * @param attributeValue the value of the attribute
     */
    public void setAttributeNS(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        final String value = attributeValue;
        final DomAttr newAttr = new DomAttr(getPage(), namespaceURI, qualifiedName, value, true);
        newAttr.setParentNode(this);
        attributes_.put(qualifiedName, newAttr);

        if (namespaceURI != null) {
            namespaces().put(namespaceURI, newAttr.getPrefix());
        }
    }

    /**
     * Indicates if the attribute names are case sensitive.
     * @return <code>true</code>
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
    public DomAttr getAttributeNode(final String name) {
        return attributes_.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public DomAttr getAttributeNodeNS(final String namespaceURI, final String localName) {
        final String qualifiedName = getQualifiedName(namespaceURI, localName);
        if (qualifiedName != null) {
            return attributes_.get(qualifiedName);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public DomNodeList<HtmlElement> getElementsByTagName(final String tagName) {
        return new XPathDomNodeList<HtmlElement>(this, ".//*[local-name()='" + tagName + "']");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public DomNodeList<HtmlElement> getElementsByTagNameNS(final String namespace, final String localName) {
        throw new UnsupportedOperationException("DomElement.getElementsByTagNameNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("DomElement.getSchemaTypeInfo is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setIdAttribute(final String name, final boolean isId) {
        throw new UnsupportedOperationException("DomElement.setIdAttribute is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setIdAttributeNS(final String namespaceURI, final String localName, final boolean isId) {
        throw new UnsupportedOperationException("DomElement.setIdAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public Attr setAttributeNode(final Attr attribute) {
        attributes_.setNamedItem(attribute);
        return null;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr setAttributeNodeNS(final Attr attribute) {
        throw new UnsupportedOperationException("DomElement.setAttributeNodeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
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
        return getAttribute("id");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getFirstElementChild() {
        final Iterator<DomElement> i = getChildElements().iterator();
        if (i.hasNext()) {
            return i.next();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getLastElementChild() {
        DomElement lastChild = null;
        final Iterator<DomElement> i = getChildElements().iterator();
        while (i.hasNext()) {
            lastChild = i.next();
        }
        return lastChild;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getPreviousElementSibling() {
        DomNode node = getPreviousSibling();
        while (node != null && !(node instanceof DomElement)) {
            node = node.getPreviousSibling();
        }
        return (DomElement) node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getNextElementSibling() {
        DomNode node = getNextSibling();
        while (node != null && !(node instanceof DomElement)) {
            node = node.getNextSibling();
        }
        return (DomElement) node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChildElementCount() {
        int counter = 0;
        final Iterator<DomElement> i = getChildElements().iterator();
        while (i.hasNext()) {
            i.next();
            counter++;
        }
        return counter;
    }

    /**
     * @return an Iterable over the DomElement children of this object, i.e. excluding the non-element nodes
     */
    public final Iterable<DomElement> getChildElements() {
        return new Iterable<DomElement>() {
            public Iterator<DomElement> iterator() {
                return new ChildElementsIterator();
            }
        };
    }

    /**
     * An iterator over the DomElement children.
     */
    protected class ChildElementsIterator implements Iterator<DomElement> {

        private DomElement nextElement_;

        /** Constructor. */
        protected ChildElementsIterator() {
            if (getFirstChild() != null) {
                if (getFirstChild() instanceof DomElement) {
                    nextElement_ = (DomElement) getFirstChild();
                }
                else {
                    setNextElement(getFirstChild());
                }
            }
        }

        /** @return is there a next one ? */
        public boolean hasNext() {
            return nextElement_ != null;
        }

        /** @return the next one */
        public DomElement next() {
            return nextElement();
        }

        /** Removes the current one. */
        public void remove() {
            if (nextElement_ == null) {
                throw new IllegalStateException();
            }
            final DomNode sibling = nextElement_.getPreviousSibling();
            if (sibling != null) {
                sibling.remove();
            }
        }

        /** @return the next element */
        public DomElement nextElement() {
            if (nextElement_ != null) {
                final DomElement result = nextElement_;
                setNextElement(nextElement_);
                return result;
            }
            throw new NoSuchElementException();
        }

        private void setNextElement(final DomNode node) {
            DomNode next = node.getNextSibling();
            while (next != null && !(next instanceof HtmlElement)) {
                next = next.getNextSibling();
            }
            nextElement_ = (DomElement) next;
        }
    }
}

/**
 * The {@link NamedNodeMap} to store the node attributes.
 */
class NamedAttrNodeMapImpl implements Map<String, DomAttr>, NamedNodeMap, Serializable {
    public static final NamedAttrNodeMapImpl EMPTY_MAP = new NamedAttrNodeMapImpl();

    private final Map<String, DomAttr> map_;
    private final List<String> attrPositions_ = new ArrayList<String>();
    private final DomElement domNode_;
    private final boolean caseSensitive_;

    private NamedAttrNodeMapImpl() {
        super();
        map_ = new LinkedHashMap<String, DomAttr>();
        domNode_ = null;
        caseSensitive_ = true;
    }

    NamedAttrNodeMapImpl(final DomElement domNode, final boolean caseSensitive) {
        super();
        map_ = new LinkedHashMap<String, DomAttr>();
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
    public int getLength() {
        return size();
    }

    /**
     * {@inheritDoc}
     */
    public DomAttr getNamedItem(final String name) {
        return get(name);
    }

    private String fixName(final String name) {
        if (caseSensitive_) {
            return name;
        }
        return name.toLowerCase();
    }

    /**
     * {@inheritDoc}
     */
    public Node getNamedItemNS(final String namespaceURI, final String localName) {
        if (domNode_ == null) {
            return null;
        }
        return get(domNode_.getQualifiedName(namespaceURI, fixName(localName)));
    }

    /**
     * {@inheritDoc}
     */
    public Node item(final int index) {
        if (index < 0 || index >= attrPositions_.size()) {
            return null;
        }
        return map_.get(attrPositions_.get(index));
    }

    /**
     * {@inheritDoc}
     */
    public Node removeNamedItem(final String name) throws DOMException {
        return remove(name);
    }

    /**
     * {@inheritDoc}
     */
    public Node removeNamedItemNS(final String namespaceURI, final String localName) {
        if (domNode_ == null) {
            return null;
        }
        return remove(domNode_.getQualifiedName(namespaceURI, fixName(localName)));
    }

    /**
     * {@inheritDoc}
     */
    public DomAttr setNamedItem(final Node node) {
        return put(node.getLocalName(), (DomAttr) node);
    }

    /**
     * {@inheritDoc}
     */
    public Node setNamedItemNS(final Node node) throws DOMException {
        return put(node.getNodeName(), (DomAttr) node);
    }

    /**
     * {@inheritDoc}
     */
    public DomAttr put(final String key, final DomAttr value) {
        final String name = fixName(key);
        if (!map_.containsKey(name)) {
            attrPositions_.add(name);
        }
        return map_.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    public DomAttr remove(final Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        final String name = fixName((String) key);
        attrPositions_.remove(name);
        return map_.remove(name);
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        attrPositions_.clear();
        map_.clear();
    }

    /**
     * {@inheritDoc}
     */
    public void putAll(final Map<? extends String, ? extends DomAttr> t) {
        // add one after the other to save the positions
        for (final Map.Entry<? extends String, ? extends DomAttr> entry : t.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) {
        if (!(key instanceof String)) {
            return false;
        }
        final String name = fixName((String) key);
        return map_.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    public DomAttr get(final Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        final String name = fixName((String) key);
        return map_.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) {
        return map_.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    public Set<java.util.Map.Entry<String, DomAttr>> entrySet() {
        return map_.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return map_.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> keySet() {
        return map_.keySet();
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return map_.size();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<DomAttr> values() {
        return map_.values();
    }
}
