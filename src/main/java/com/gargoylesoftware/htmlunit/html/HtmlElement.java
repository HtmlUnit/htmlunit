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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;

/**
 * An abstract wrapper for HTML elements.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Mike Gallaher
 * @author Denis N. Antonioli
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Dmitri Zoubkov
 */
public abstract class HtmlElement extends DomElement implements Element {

    /** Constant meaning that the specified attribute was not defined. */
    public static final String ATTRIBUTE_NOT_DEFINED = new String("");

    /** Constant meaning that the specified attribute was found but its value was empty. */
    public static final String ATTRIBUTE_VALUE_EMPTY = new String("");

    /**
     * Constant indicating that a tab index value is out of bounds (less than <tt>0</tt> or greater
     * than <tt>32767</tt>).
     *
     * @see #getTabIndex()
     */
    public static final Short TAB_INDEX_OUT_OF_BOUNDS = new Short(Short.MIN_VALUE);
    
    private final transient Log mainLog_ = LogFactory.getLog(getClass());

    /** The map holding the attributes, keyed by name. */
    private Map<String, DomAttr> attributes_;

    /** The map holding the namespaces, keyed by URI. */
    private Map<String, String> namespaces_ = new HashMap<String, String>();

    /** The listeners which are to be notified of attribute changes. */
    private List<HtmlAttributeChangeListener> attributeListeners_;

    private HtmlForm owningForm_; // the owning form for lost form children

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes a map ready initialized with the attributes for this element, or
     * <code>null</code>. The map will be stored as is, not copied.
     */
    protected HtmlElement(final String namespaceURI, final String qualifiedName, final Page page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page);
        if (attributes != null) {
            attributes_ = attributes;
            // The HtmlAttr objects are created before the HtmlElement, so we need to go set the
            // parent HtmlElement, now. Also index the namespaces while we are at it.
            for (final DomAttr entry : attributes_.values()) {
                entry.setParentNode(this);
                final String attrNamespaceURI = entry.getNamespaceURI();
                if (attrNamespaceURI != null) {
                    namespaces_.put(attrNamespaceURI, entry.getPrefix());
                }
            }
        }
        else {
            attributes_ = Collections.emptyMap();
        }
    }

    /**
     * Overrides {@link DomNode#cloneNode(boolean)} so clone gets its own Map of attributes.
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final HtmlElement newNode = (HtmlElement) super.cloneNode(deep);
        newNode.attributes_ = createAttributeMap(attributes_.size());
        for (final DomAttr attr : attributes_.values()) {
            newNode.setAttributeValue(attr.getNamespaceURI(), attr.getQualifiedName(), attr.getNodeValue(), true);
        }
        return newNode;
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
    public final String getAttribute(final String attributeName) {
        return getAttributeValue(attributeName);
    }

    /**
     * Returns the qualified name (prefix:local) for the namespace and local name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param localName the name within the namespace
     * @return the qualified name or just local name if the namespace is not fully defined
     */
    private String getQualifiedName(final String namespaceURI, final String localName) {
        final String qualifiedName;
        if (namespaceURI != null) {
            final String prefix = namespaces_.get(namespaceURI);
            if (prefix != null) {
                qualifiedName = prefix + ':' + localName;
            }
            else {
                qualifiedName = localName;
            }
        }
        else {
            qualifiedName = localName;
        }
        return qualifiedName;
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
        return getAttributeValue(getQualifiedName(namespaceURI, localName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAttributes() {
        return attributes_.size() > 0;
    }

    /**
     * Returns whether the attribute specified by name has a value.
     *
     * @param attributeName the name of the attribute
     * @return true if an attribute with the given name is specified on this element or has a
     * default value, false otherwise.
     */
    public final boolean hasAttribute(final String attributeName) {
        return attributes_.get(attributeName) != null;
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
        return attributes_.get(getQualifiedName(namespaceURI, localName)) != null;
    }

    /**
     * Returns the value of the specified attribute or an empty string. If the
     * result is an empty string then it will be either {@link #ATTRIBUTE_NOT_DEFINED}
     * if the attribute wasn't specified or {@link #ATTRIBUTE_VALUE_EMPTY} if the
     * attribute was specified but it was empty.
     *
     * @param attributeName the name of the attribute
     * @return the value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED} or {@link #ATTRIBUTE_VALUE_EMPTY}
     */
    public final String getAttributeValue(final String attributeName) {
        final DomAttr attr = attributes_.get(attributeName.toLowerCase());
        if (attr != null) {
            return attr.getNodeValue();
        }
        return ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * Sets the value of the attribute specified by name.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     */
    public final void setAttribute(final String attributeName, final String attributeValue) {
        setAttributeValue(null, attributeName, attributeValue);
    }

    /**
     * Sets the value of the attribute specified by namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name (prefix:local) of the attribute
     * @param attributeValue the value of the attribute
     */
    public final void setAttributeNS(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        setAttributeValue(namespaceURI, qualifiedName, attributeValue);
    }

    /**
     * Sets the value of the specified attribute.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     */
    public final void setAttributeValue(final String attributeName, final String attributeValue) {
        setAttributeValue(null, attributeName, attributeValue);
    }

    /**
     * Sets the value of the specified attribute.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the attribute
     * @param attributeValue the value of the attribute
     */
    public final void setAttributeValue(final String namespaceURI, final String qualifiedName,
        final String attributeValue) {
        setAttributeValue(namespaceURI, qualifiedName, attributeValue, false);
    }

    /**
     * Sets the value of the specified attribute. This method may be overridden by subclasses
     * which are interested in specific attribute value changes, but such methods <b>must</b>
     * invoke <tt>super.setAttributeValue()</tt>, and <b>should</b> consider the value of the
     * <tt>cloning</tt> parameter when deciding whether or not to execute custom logic.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the attribute
     * @param attributeValue the value of the attribute
     * @param cloning whether or not this attribute value change is the result of a node clone operation
     */
    protected void setAttributeValue(final String namespaceURI, final String qualifiedName,
        final String attributeValue, final boolean cloning) {

        final String oldAttributeValue = getAttributeValue(qualifiedName);
        String value = attributeValue;

        if (attributes_ == Collections.EMPTY_MAP) {
            attributes_ = createAttributeMap(1);
        }
        if (value.length() == 0) {
            value = ATTRIBUTE_VALUE_EMPTY;
        }

        getPage().removeMappedElement(this);
        final DomAttr newAttr = addAttributeToMap(getPage(), attributes_, namespaceURI,
            qualifiedName.toLowerCase(), value);
        if (namespaceURI != null) {
            namespaces_.put(namespaceURI, newAttr.getPrefix());
        }
        getPage().addMappedElement(this);

        final HtmlAttributeChangeEvent htmlEvent;
        if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
            htmlEvent = new HtmlAttributeChangeEvent(this, qualifiedName, attributeValue);
        }
        else {
            htmlEvent = new HtmlAttributeChangeEvent(this, qualifiedName, oldAttributeValue);
        }
        
        if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
            fireHtmlAttributeAdded(htmlEvent);
            getPage().fireHtmlAttributeAdded(htmlEvent);
        }
        else {
            fireHtmlAttributeReplaced(htmlEvent);
            getPage().fireHtmlAttributeReplaced(htmlEvent);
        }
        if (getPage().getWebClient().getBrowserVersion().isIE()) {
            fireEvent(Event.createPropertyChangeEvent(this, qualifiedName));
        }
    }

    /**
     * {@inheritDoc}
     */
    public NodeList getElementsByTagName(final String tagName) {
        return new DomNodeList(this, "//" + tagName);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public NodeList getElementsByTagNameNS(final String namespace, final String name) {
        throw new UnsupportedOperationException("HtmlElement.getElementsByTagNameNS is not yet implemented.");
    }

    /**
     * Returns the HTML elements that are descendants of this element and that have one of the specified tag names.
     * @param tagNames the tag names to match (case-insensitive)
     * @return the HTML elements that are descendants of this element and that have one of the specified tag name
     */
    public final List< ? extends HtmlElement> getHtmlElementsByTagNames(final List<String> tagNames) {
        final List<HtmlElement> list = new ArrayList<HtmlElement>();
        for (final String tagName : tagNames) {
            list.addAll(getHtmlElementsByTagName(tagName));
        }
        return list;
    }

    /**
     * Returns the HTML elements that are descendants of this element and that have the specified tag name.
     * @param tagName the tag name to match (case-insensitive)
     * @return the HTML elements that are descendants of this element and that have the specified tag name
     */
    public final List< ? extends HtmlElement> getHtmlElementsByTagName(final String tagName) {
        final List<HtmlElement> list = new ArrayList<HtmlElement>();
        final String lowerCaseTagName = tagName.toLowerCase();
        for (final HtmlElement element : getAllHtmlChildElements()) {
            if (lowerCaseTagName.equals(element.getTagName())) {
                list.add(element);
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr setAttributeNodeNS(final Attr attribute) {
        throw new UnsupportedOperationException("HtmlElement.setAttributeNodeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr setAttributeNode(final Attr attribute) {
        throw new UnsupportedOperationException("HtmlElement.setAttributeNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr removeAttributeNode(final Attr attribute) {
        throw new UnsupportedOperationException("HtmlElement.removeAttributeNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr getAttributeNodeNS(final String namespaceURI, final String localName) {
        throw new UnsupportedOperationException("HtmlElement.getAttributeNodeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr getAttributeNode(final String name) {
        throw new UnsupportedOperationException("HtmlElement.getAttributeNode is not yet implemented.");
    }

    /**
     * Removes an attribute specified by name from this element.
     * @param attributeName the attribute attributeName
     */
    public final void removeAttribute(final String attributeName) {
        final String value = getAttributeValue(attributeName);

        getPage().removeMappedElement(this);
        attributes_.remove(attributeName.toLowerCase());
        getPage().addMappedElement(this);

        final HtmlAttributeChangeEvent event = new HtmlAttributeChangeEvent(this, attributeName, value);
        fireHtmlAttributeRemoved(event);
        getPage().fireHtmlAttributeRemoved(event);
    }

    /**
     * Removes an attribute specified by namespace and local name from this element.
     * @param namespaceURI the URI that identifies an XML namespace
     * @param localName the name within the namespace
     */
    public final void removeAttributeNS(final String namespaceURI, final String localName) {
        removeAttribute(getQualifiedName(namespaceURI, localName));
    }

    /**
     * Support for reporting HTML attribute changes.
     * This method can be called when an attribute has been added and it will send the
     * appropriate HtmlAttributeChangeEvent to any registered HtmlAttributeChangeListener.
     *
     * Note that this methods recursively calls this parent fireHtmlAttributeAdded.
     *
     * @param event the event
     */
    protected void fireHtmlAttributeAdded(final HtmlAttributeChangeEvent event) {
        if (attributeListeners_ != null) {
            synchronized (this) {
                for (final HtmlAttributeChangeListener listener : attributeListeners_) {
                    listener.attributeAdded(event);
                }
            }
        }
        final DomNode parentNode = getParentNode();
        if (parentNode instanceof HtmlElement) {
            ((HtmlElement) parentNode).fireHtmlAttributeAdded(event);
        }
    }

    /**
     * Support for reporting HTML attribute changes.
     * This method can be called when an attribute has been replaced and it will send the
     * appropriate HtmlAttributeChangeEvent to any registered HtmlAttributeChangeListener.
     *
     * Note that this methods recursively calls this parent fireHtmlAttributeReplaced.
     *
     * @param event the event
     */
    protected void fireHtmlAttributeReplaced(final HtmlAttributeChangeEvent event) {
        if (attributeListeners_ != null) {
            synchronized (this) {
                for (final HtmlAttributeChangeListener listner : attributeListeners_) {
                    listner.attributeReplaced(event);
                }
            }
        }
        final DomNode parentNode = getParentNode();
        if (parentNode instanceof HtmlElement) {
            ((HtmlElement) parentNode).fireHtmlAttributeReplaced(event);
        }
    }

    /**
     * Support for reporting HTML attribute changes.
     * This method can be called when an attribute has been removed and it will send the
     * appropriate HtmlAttributeChangeEvent to any registered HtmlAttributeChangeListener.
     *
     * Note that this methods recursively calls this parent fireHtmlAttributeRemoved.
     *
     * @param event the event
     */
    protected void fireHtmlAttributeRemoved(final HtmlAttributeChangeEvent event) {
        if (attributeListeners_ != null) {
            synchronized (this) {
                for (final HtmlAttributeChangeListener listener : attributeListeners_) {
                    listener.attributeRemoved(event);
                }
            }
        }
        final DomNode parentNode = getParentNode();
        if (parentNode instanceof HtmlElement) {
            ((HtmlElement) parentNode).fireHtmlAttributeRemoved(event);
        }
    }

    /**
     * Returns true if the specified attribute has been defined. This is necessary
     * in order to distinguish between an attribute that is set to an empty string
     * and one that was not defined at all.
     *
     * @param attributeName the attribute to check
     * @return true if the attribute is defined
     */
    public boolean isAttributeDefined(final String attributeName) {
        return attributes_.get(attributeName.toLowerCase()) != null;
    }

    /**
     * @return an iterator over the {@link DomAttr} objects representing the
     * attributes of this element.
     * The elements are ordered as found in the HTML source code.
     * @deprecated As of 2.0
     */
    @Deprecated
    public Iterator<DomAttr> getAttributeEntriesIterator() {
        return getAttributesCollection().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public org.w3c.dom.NamedNodeMap getAttributes() {
        return new NamedNodeMap(this);
    }

    /**
     * @return a collection of {@link DomAttr} objects representing the
     * attributes of this element. The elements are ordered as found in the HTML source code.
     */
    public Collection<DomAttr> getAttributesCollection() {
        return attributes_.values();
    }

    /**
     * Returns the tag name of this element. The tag name is the actual HTML name. For example
     * the tag name for HtmlAnchor is "a" and the tag name for HtmlTable is "table".
     * This tag name will always be in lowercase, no matter what case was used in the original
     * document, when no namespace is defined.
     *
     * @return the tag name of this element
     */
    public String getTagName() {
        if (getNamespaceURI() == null) {
            return getLocalName().toLowerCase();
        }
        return getQualifiedName();
    }

    /** @return the node type */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.ELEMENT_NODE;
    }

    /**
     * @return the same value as returned by {@link #getTagName()}
     */
    @Override
    public String getNodeName() {
        return getTagName();
    }

    /**
     * @return the identifier of this element
     */
    public final String getId() {
        return getAttributeValue("id");
    }

    /**
     * Sets the identifier this element.
     *
     * @param newId the new identifier of this element
     */
    public final void setId(final String newId) {
        setAttributeValue("id", newId);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("HtmlElement.getSchemaTypeInfo is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public final void setIdAttribute(final String name, final boolean isId) throws DOMException {
        throw new UnsupportedOperationException("HtmlElement.setIdAttribute is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public final void setIdAttributeNS(final String namespaceURI, final String localName, final boolean isId)
        throws DOMException {
        throw new UnsupportedOperationException("HtmlElement.setIdAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public final void setIdAttributeNode(final Attr idAttr, final boolean isId) throws DOMException {
        throw new UnsupportedOperationException("HtmlElement.setIdAttributeNode is not yet implemented.");
    }

    /**
     * Returns this element's tab index, if it has one. If the tab index is outside of the
     * valid range (less than <tt>0</tt> or greater than <tt>32767</tt>), this method
     * returns {@link #TAB_INDEX_OUT_OF_BOUNDS}. If this element does not have
     * a tab index, or its tab index is otherwise invalid, this method returns <tt>null</tt>.
     *
     * @return this element's tab index
     */
    public Short getTabIndex() {
        final String index = getAttributeValue("tabindex");
        if (index == null || index.length() == 0) {
            return null;
        }
        try {
            final long l = Long.parseLong(index);
            if (l >= 0 && l <= Short.MAX_VALUE) {
                return new Short(new Long(l).shortValue());
            }
            return TAB_INDEX_OUT_OF_BOUNDS;
        }
        catch (final NumberFormatException e) {
            return null;
        }
    }

    /**
     * Returns the first element with the specified tag name that is an ancestor to this element, or
     * <tt>null</tt> if no such element is found.
     * @param tagName the name of the tag searched (case insensitive)
     * @return the first element with the specified tag name that is an ancestor to this element
     */
    public HtmlElement getEnclosingElement(final String tagName) {
        final String tagNameLC = tagName.toLowerCase();

        DomNode currentNode = getParentNode();
        while (currentNode != null) {
            if (currentNode instanceof HtmlElement
                    && currentNode.getNodeName().equals(tagNameLC)) {

                return (HtmlElement) currentNode;
            }
            currentNode = currentNode.getParentNode();
        }
        return null;
    }

    /**
     * Returns the form which contains this element, or <tt>null</tt> if this element is not inside
     * of a form.
     * @return the form which contains this element
     */
    public HtmlForm getEnclosingForm() {
        if (owningForm_ != null) {
            return owningForm_;
        }
        else {
            return (HtmlForm) getEnclosingElement("form");
        }
    }

    /**
     * Returns the form which contains this element. If this element is not inside a form, this method
     * throws an {@link IllegalStateException}.
     * @return the form which contains this element
     * @throws IllegalStateException if the element is not inside a form
     */
    public HtmlForm getEnclosingFormOrDie() throws IllegalStateException {
        final HtmlForm form = getEnclosingForm();
        if (form == null) {
            throw new IllegalStateException("Element is not contained within a form: " + this);
        }
        return form;
    }

    /**
     * Simulates typing the specified text while this element has focus.
     * Note that for some elements, typing '\n' submits the enclosed form.
     * @param text the text you with to simulate typing
     * @exception IOException If an IO error occurs
     */
    public void type(final String text) throws IOException {
        for (final char ch : text.toCharArray()) {
            type(ch);
        }
    }

    /**
     * Simulates typing the specified text while this element has focus.
     * Note that for some elements, typing '\n' submits the enclosed form.
     * @param text the text you with to simulate typing
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @exception IOException If an IO error occurs
     */
    public void type(final String text, final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        for (final char ch : text.toCharArray()) {
            type(ch, shiftKey, ctrlKey, altKey);
        }
    }

    /**
     * Simulates typing the specified character while this element has focus, returning the page contained
     * by this element's window after typing. Note that it may or may not be the same as the original page,
     * depending on the JavaScript event handlers, etc. Note also that for some elements, typing <tt>'\n'</tt>
     * submits the enclosed form.
     *
     * @param c the character you with to simulate typing
     * @return the page that occupies this window after typing
     * @exception IOException if an IO error occurs
     */
    public Page type(final char c) throws IOException {
        return type(c, false, false, false);
    }

    /**
     * Simulates typing the specified character while this element has focus, returning the page contained
     * by this element's window after typing. Note that it may or may not be the same as the original page,
     * depending on the JavaScript event handlers, etc. Note also that for some elements, typing <tt>'\n'</tt>
     * submits the enclosed form.
     *
     * @param c the character you with to simulate typing
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the typing
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the typing
     * @param altKey <tt>true</tt> if ALT is pressed during the typing
     * @return the page that occupies this window after typing
     * @exception IOException if an IO error occurs
     */
    public Page type(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return getPage();
        }

        fireEvent(new Event(this, Event.TYPE_KEY_DOWN, c, shiftKey, ctrlKey, altKey));
        fireEvent(new Event(this, Event.TYPE_KEY_PRESS, c, shiftKey, ctrlKey, altKey));
        doType(c, shiftKey, ctrlKey, altKey);
        fireEvent(new Event(this, Event.TYPE_KEY_UP, c, shiftKey, ctrlKey, altKey));

        final HtmlForm form = getEnclosingForm();
        if (form != null && c == '\n' && isSubmittableByEnter()) {
            return form.submit((SubmittableElement) this);
        }
        return getPage();
    }

    /**
     * Performs the effective type action, called after the keyPress event and before the keyUp event.
     * @param c the character you with to simulate typing
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the typing
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the typing
     * @param altKey <tt>true</tt> if ALT is pressed during the typing
     * @exception IOException if an IO error occurs
     */
    protected void doType(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        // nothing
    }

    /**
     * Returns <tt>true</tt> if clicking Enter (ASCII 10, or '\n') should submit the enclosed form (if any).
     * The default implementation returns <tt>false</tt>.
     * @return <tt>true</tt> if clicking Enter should submit the enclosed form (if any)
     */
    protected boolean isSubmittableByEnter() {
        return false;
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
     * Prints the content between "&lt;" and "&gt;" (or "/&gt;") in the output of the tag name
     * and its attributes in XML format.
     * @param printWriter the writer to print in
     */
    protected void printOpeningTagContentAsXml(final PrintWriter printWriter) {
        printWriter.print(getTagName());

        for (final String name : attributes_.keySet()) {
            printWriter.print(" ");
            printWriter.print(name);
            printWriter.print("=\"");
            printWriter.print(StringEscapeUtils.escapeXml(attributes_.get(name).getNodeValue()));
            printWriter.print("\"");
        }
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();

        buffer.append(ClassUtils.getShortClassName(getClass()));
        buffer.append("[<");

        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        printOpeningTagContentAsXml(printWriter);
        buffer.append(writer.toString());

        buffer.append(">]");

        return buffer.toString();
    }

    /**
     * Searches for an element based on the specified criteria, returning the first element which matches
     * said criteria. Only elements which are descendants of this element are included in the search.
     *
     * @param elementName the name of the element to search for
     * @param attributeName the name of the attribute to search for
     * @param attributeValue the value of the attribute to search for
     * @return the first element which matches the specified search criteria
     * @exception ElementNotFoundException if no element matches the specified search criteria
     */
    public final HtmlElement getOneHtmlElementByAttribute(final String elementName, final String attributeName,
        final String attributeValue) throws ElementNotFoundException {

        WebAssert.notNull("elementName", elementName);
        WebAssert.notNull("attributeName", attributeName);
        WebAssert.notNull("attributeValue", attributeValue);

        final List< ? extends HtmlElement> list =
            getHtmlElementsByAttribute(elementName, attributeName, attributeValue);

        final int listSize = list.size();
        if (listSize == 0) {
            throw new ElementNotFoundException(elementName, attributeName, attributeValue);
        }

        return list.get(0);
    }

    /**
     * Returns the element in this element's page with the specified ID. If more than one element
     * has the specified ID (not allowed by the HTML spec), this method returns the first one.
     *
     * @param id the ID value to search for
     * @return the element in this element's page with the specified ID
     * @exception ElementNotFoundException if no element has the specified ID
     */
    public HtmlElement getHtmlElementById(final String id) throws ElementNotFoundException {
        return getPage().getHtmlElementById(id);
    }

    /**
     * <p>Returns <tt>true</tt> if there is an element in this element's page with the specified ID.
     * This method is intended for situations where it is enough to know whether a specific
     * element is present in the document.</p>
     *
     * <p>Implementation Note: This method calls {@link #getHtmlElementById(String)} internally,
     * so writing code such as the following would be extremely inefficient:</p>
     *
     * <pre>
     * if (hasHtmlElementWithId(id)) {
     *     HtmlElement element = getHtmlElementWithId(id)
     *     ...
     * }
     * </pre>
     *
     * @param id the id to search for
     * @return <tt>true</tt> if there is an element in this element's page with the specified ID
     */
    public boolean hasHtmlElementWithId(final String id) {
        try {
            getHtmlElementById(id);
            return true;
        }
        catch (final ElementNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns all elements which are descendants of this element and match the specified search criteria.
     *
     * @param elementName the name of the element to search for
     * @param attributeName the name of the attribute to search for
     * @param attributeValue the value of the attribute to search for
     * @return all elements which are descendants of this element and match the specified search criteria
     */
    public final List< ? extends HtmlElement> getHtmlElementsByAttribute(
            final String elementName,
            final String attributeName,
            final String attributeValue) {

        final List<HtmlElement> list = new ArrayList<HtmlElement>();
        final String lowerCaseTagName = elementName.toLowerCase();

        for (final HtmlElement next : getAllHtmlChildElements()) {
            if (next.getTagName().equals(lowerCaseTagName)) {
                final String attValue = next.getAttributeValue(attributeName);
                if (attValue != null && attValue.equals(attributeValue)) {
                    list.add(next);
                }
            }
        }
        return list;
    }

    /**
     * Appends a child element to this HTML element with the specified tag name
     * if this HTML element does not already have a child with that tag name.
     * Returns the appended child element, or the first existent child element
     * with the specified tag name if none was appended.
     * @param tagName the tag name of the child to append
     * @return the added child, or the first existing child if none was added
     */
    public final HtmlElement appendChildIfNoneExists(final String tagName) {
        final HtmlElement child;
        final List< ? extends HtmlElement> children = getHtmlElementsByTagName(tagName);
        if (children.isEmpty()) {
            // Add a new child and return it.
            child = getPage().createHtmlElement(tagName);
            appendChild(child);
        }
        else {
            // Return the first existing child.
            child = children.get(0);
        }
        return child;
    }

    /**
     * Removes the <tt>i</tt>th child element with the specified tag name
     * from all relationships, if possible.
     * @param tagName the tag name of the child to remove
     * @param i the index of the child to remove
     */
    public final void removeChild(final String tagName, final int i) {
        final List< ? extends HtmlElement> children = getHtmlElementsByTagName(tagName);
        if (i >= 0 && i < children.size()) {
            final HtmlElement child = children.get(i);
            child.remove();
        }
    }

    /**
     * @return an Iterable over the HtmlElement children of this object, i.e. excluding the non-element nodes
     */
    public final Iterable<HtmlElement> getChildElements() {
        return new Iterable<HtmlElement>() {
            public Iterator<HtmlElement> iterator() {
                return new ChildElementsIterator();
            }
        };
    }

    /**
     * @return an iterator over the HtmlElement children of this object, i.e. excluding the non-element nodes
     * @deprecated As of 2.0, use {@link #getChildElements()}.
     */
    @Deprecated
    public final Iterator<HtmlElement> getChildElementsIterator() {
        return new ChildElementsIterator();
    }

    /**
     * An iterator over the HtmlElement children.
     */
    protected class ChildElementsIterator implements Iterator<HtmlElement> {

        private HtmlElement nextElement_;

        /** Constructor. */
        public ChildElementsIterator() {
            if (getFirstChild() != null) {
                if (getFirstChild() instanceof HtmlElement) {
                    nextElement_ = (HtmlElement) getFirstChild();
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
        public HtmlElement next() {
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
        public HtmlElement nextElement() {
            if (nextElement_ != null) {
                final HtmlElement result = nextElement_;
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
            nextElement_ = (HtmlElement) next;
        }
    }

    /**
     * Creates an attribute map as needed by HtmlElement. This is just used by the element factories.
     * @param attributeCount the initial number of attributes to be added to the map
     * @return the attribute map
     */
    @SuppressWarnings("unchecked")
    static Map<String, DomAttr> createAttributeMap(final int attributeCount) {
        return ListOrderedMap.decorate(new HashMap<String, DomAttr>(attributeCount)); // preserve insertion order
    }

    /**
      * Add an attribute to the attribute map. This is just used by the element factories.
     * @param attributeMap the attribute map where the attribute will be added
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the attribute
     * @param value the value of the attribute
     */
    static DomAttr addAttributeToMap(final Page page, final Map<String, DomAttr> attributeMap,
            final String namespaceURI, final String qualifiedName, final String value) {
        final DomAttr newAttr = new DomAttr(page, namespaceURI, qualifiedName, value);
        attributeMap.put(qualifiedName, newAttr);
        return newAttr;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Return a Function to be executed when a given event occurs.
     * @param eventName Name of event such as "onclick" or "onblur", etc
     * @return a Rhino JavaScript executable Function, or <tt>null</tt> if no event handler has been defined
     */
    public final Function getEventHandler(final String eventName) {
        final HTMLElement jsObj = (HTMLElement) getScriptObject();
        return jsObj.getEventHandler(eventName);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Register a Function as an event handler.
     * @param eventName Name of event such as "onclick" or "onblur", etc
     * @param eventHandler a Rhino JavaScript executable Function
     */
    public final void setEventHandler(final String eventName, final Function eventHandler) {
        final HTMLElement jsObj = (HTMLElement) getScriptObject();
        jsObj.setEventHandler(eventName, eventHandler);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Register a snippet of JavaScript code as an event handler. The JavaScript code will
     * be wrapped inside a unique function declaration which provides one argument named
     * "event"
     * @param eventName Name of event such as "onclick" or "onblur", etc
     * @param jsSnippet executable JavaScript code
     */
    public final void setEventHandler(final String eventName, final String jsSnippet) {
        final BaseFunction function = new EventHandler(this, eventName, jsSnippet);
        setEventHandler(eventName, function);
        if (mainLog_.isDebugEnabled()) {
            mainLog_.debug("Created event handler " + function.getFunctionName()
                    + " for " + eventName + " on " + this);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Removes the specified event handler.
     * @param eventName Name of the event such as "onclick" or "onblur", etc
     */
    public final void removeEventHandler(final String eventName) {
        setEventHandler(eventName, (Function) null);
    }

    /**
     * Adds an HtmlAttributeChangeListener to the listener list.
     * The listener is registered for all attributes of this HtmlElement,
     * as well as descendant elements.
     *
     * @param listener the attribute change listener to be added
     * @see #removeHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void addHtmlAttributeChangeListener(final HtmlAttributeChangeListener listener) {
        WebAssert.notNull("listener", listener);
        synchronized (this) {
            if (attributeListeners_ == null) {
                attributeListeners_ = new ArrayList<HtmlAttributeChangeListener>();
            }
            attributeListeners_.add(listener);
        }
    }

    /**
     * Removes an HtmlAttributeChangeListener from the listener list.
     * This method should be used to remove HtmlAttributeChangeListener that were registered
     * for all attributes of this HtmlElement, as well as descendant elements.
     *
     * @param listener the attribute change listener to be removed
     * @see #addHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void removeHtmlAttributeChangeListener(final HtmlAttributeChangeListener listener) {
        WebAssert.notNull("listener", listener);
        synchronized (this) {
            if (attributeListeners_ != null) {
                attributeListeners_.remove(listener);
            }
        }
    }

    /**
     * Shortcut for {@link #fireEvent(Event)}.
     * @param eventType the event type (like "load", "click")
     * @return the execution result, or <code>null</code> if nothing is executed
     */
    public ScriptResult fireEvent(final String eventType) {
        return fireEvent(new Event(this, eventType));
    }

    /**
     * Fires the event on the element. Nothing is done if JavaScript is disabled.
     * @param event the event to fire
     * @return the execution result, or <tt>null</tt> if nothing is executed
     */
    public ScriptResult fireEvent(final Event event) {
        if (!getPage().getWebClient().isJavaScriptEnabled()) {
            return null;
        }

        if (mainLog_.isDebugEnabled()) {
            mainLog_.debug("Firing " + event);
        }
        final HTMLElement jsElt = (HTMLElement) getScriptObject();
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                return jsElt.fireEvent(event);
            }
        };

        final ScriptResult result = (ScriptResult) ContextFactory.getGlobal().call(action);
        final boolean isIE = getPage().getWebClient().getBrowserVersion().isIE();
        if ((!isIE && event.isPreventDefault()) || (isIE && ScriptResult.isFalse(result))) {
            preventDefault();
        }
        return result;
    }

    /**
     * This method is called if the current fired event is canceled by <tt>preventDefault()</tt> in FireFox,
     * or by returning <tt>false</tt> in Internet Explorer.
     *
     * The default implementation does nothing.
     */
    protected void preventDefault() {
        // Empty by default; override as needed.
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
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the mouse move
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the mouse move
     * @param altKey <tt>true</tt> if ALT is pressed during the mouse move
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
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the mouse move
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the mouse move
     * @param altKey <tt>true</tt> if ALT is pressed during the mouse move
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
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the mouse move
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the mouse move
     * @param altKey <tt>true</tt> if ALT is pressed during the mouse move
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
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the mouse click
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the mouse click
     * @param altKey <tt>true</tt> if ALT is pressed during the mouse click
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
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the mouse click release
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the mouse click release
     * @param altKey <tt>true</tt> if ALT is pressed during the mouse click release
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
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the mouse click
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the mouse click
     * @param altKey <tt>true</tt> if ALT is pressed during the mouse click
     * @return the page which this element's window contains after the mouse click
     */
    public Page rightClick(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        final Page mouseDownPage = mouseDown(shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_RIGHT);
        if (mouseDownPage != getPage()) {
            if (mainLog_.isDebugEnabled()) {
                mainLog_.debug("rightClick() is incomplete, as mouseDown() loaded a different page.");
            }
            return mouseDownPage;
        }
        final Page mouseUpPage = mouseUp(shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_RIGHT);
        if (mouseUpPage != getPage()) {
            if (mainLog_.isDebugEnabled()) {
                mainLog_.debug("rightClick() is incomplete, as mouseUp() loaded a different page.");
            }
            return mouseUpPage;
        }
        return doMouseEvent(MouseEvent.TYPE_CONTEXT_MENU, shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_RIGHT);
    }

    /**
     * Simulates the specified mouse event, returning the page which this element's window contains after the event.
     * The returned page may or may not be the same as the original page, depending on JavaScript event handlers, etc.
     *
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the mouse event
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the mouse event
     * @param altKey <tt>true</tt> if ALT is pressed during the mouse event
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     * @return the page which this element's window contains after the event
     */
    private Page doMouseEvent(final String eventType, final boolean shiftKey, final boolean ctrlKey,
        final boolean altKey, final int button) {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return getPage();
        }
        final HtmlPage page = getPage();
        final Event event = new MouseEvent(this, eventType, shiftKey, ctrlKey, altKey, button);
        final ScriptResult scriptResult = fireEvent(event);
        final Page currentPage;
        if (scriptResult == null) {
            currentPage = page;
        }
        else {
            currentPage = scriptResult.getNewPage();
        }
        return currentPage;
    }

    /**
     * Removes focus from this element.
     */
    public void blur() {
        getPage().setFocusedElement(null);
    }

    /**
     * Sets the focus on this element.
     */
    public void focus() {
        getPage().setFocusedElement(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPage getPage() {
        return (HtmlPage) super.getPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkChildHierarchy(final Node childNode) throws DOMException {
        if (!((childNode instanceof Element) || (childNode instanceof Text)
            || (childNode instanceof Comment) || (childNode instanceof ProcessingInstruction)
            || (childNode instanceof CDATASection) || (childNode instanceof EntityReference))) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "The Element may not have a child of this type: " + childNode.getNodeType());
        }
        super.checkChildHierarchy(childNode);
    }

    /**
     * Custom serialization logic which ensures that {@link NonSerializable} {@link HtmlAttributeChangeListener}s
     * are not serialized.
     * @param stream the stream to write the object to
     * @throws IOException if an error occurs during writing
     */
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        synchronized (this) {
            // Store the original list of listeners in a temporary variable, and then
            // modify the listener list by removing any NonSerializable listeners.
            final List<HtmlAttributeChangeListener> temp;
            if (attributeListeners_ != null) {
                temp = new ArrayList<HtmlAttributeChangeListener>(attributeListeners_);
                for (final Iterator<HtmlAttributeChangeListener> i = attributeListeners_.iterator(); i.hasNext();) {
                    final HtmlAttributeChangeListener listener = i.next();
                    if (listener instanceof NonSerializable) {
                        i.remove();
                    }
                }
            }
            else {
                temp = null;
            }
            // Perform object serialization, now that NonSerializable listeners have been removed.
            stream.defaultWriteObject();
            // Restore the old listeners, now that serialization has been performed.
            attributeListeners_ = temp;
        }
    }

    void setOwningForm(final HtmlForm form) {
        owningForm_ = form;
    }

}
