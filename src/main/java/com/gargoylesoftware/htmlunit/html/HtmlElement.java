/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
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
    private Map<String, HtmlAttr> attributes_;

    /** The map holding the namespaces, keyed by URI. */
    private Map<String, String> namespaces_ = new HashMap<String, String>();

    private List<HtmlAttributeChangeListener> attributeListeners_;

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param htmlPage The page that contains this element
     * @param attributes a map ready initialized with the attributes for this element, or
     * <code>null</code>. The map will be stored as is, not copied.
     */
    protected HtmlElement(final String namespaceURI, final String qualifiedName, final HtmlPage htmlPage,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, htmlPage);
        if (attributes != null) {
            attributes_ = attributes;
            // The HtmlAttr objects are created before the HtmlElement, so we need to go set the
            // parent HtmlElement, now.  Also index the namespaces while we are at it.
            for (final HtmlAttr entry : attributes_.values()) {
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
    public Node cloneNode(final boolean deep) {
        return cloneDomNode(deep);
    }

    /**
     * Overrides {@link DomNode#cloneDomNode(boolean)} so clone gets its own Map of attributes.
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneDomNode(final boolean deep) {
        final HtmlElement newNode = (HtmlElement) super.cloneDomNode(deep);
        newNode.attributes_ = createAttributeMap(attributes_.size());
        for (final HtmlAttr attr : attributes_.values()) {
            newNode.setAttributeValue(attr.getNamespaceURI(), attr.getQualifiedName(), attr.getNodeValue());
        }
        return newNode;
    }

    /**
     * Returns the value of the attribute specified by name or an empty string.  If the
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
            final String prefix = (String) namespaces_.get(namespaceURI);
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
     * string.  If the result is an empty string then it will be either {@link #ATTRIBUTE_NOT_DEFINED}
     * if the attribute wasn't specified or {@link #ATTRIBUTE_VALUE_EMPTY} if the
     * attribute was specified but it was empty.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param localName The name within the namespace.
     * @return The value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED}
     * or {@link #ATTRIBUTE_VALUE_EMPTY}
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
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param localName The name within the namespace.
     * @return true if an attribute with the given name is specified on this element or has a
     * default value, false otherwise.
     */
    public final boolean hasAttributeNS(final String namespaceURI, final String localName) {
        return attributes_.get(getQualifiedName(namespaceURI, localName)) != null;
    }

    /**
     * Returns the value of the specified attribute or an empty string.  If the
     * result is an empty string then it will be either {@link #ATTRIBUTE_NOT_DEFINED}
     * if the attribute wasn't specified or {@link #ATTRIBUTE_VALUE_EMPTY} if the
     * attribute was specified but it was empty.
     *
     * @param attributeName the name of the attribute
     * @return The value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED}
     * or {@link #ATTRIBUTE_VALUE_EMPTY}
     */
    public final String getAttributeValue(final String attributeName) {
        final HtmlAttr attr = (HtmlAttr) attributes_.get(attributeName.toLowerCase());

        if (attr != null) {
            return attr.getNodeValue();
        }
        else {
            return ATTRIBUTE_NOT_DEFINED;
        }
    }

    /**
     * Sets the value of the attribute specified by name.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue The value of the attribute
     */
    public final void setAttribute(final String attributeName, final String attributeValue) {
        setAttributeValue(null, attributeName, attributeValue);
    }

    /**
     * Sets the value of the attribute specified by namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name (prefix:local) of the attribute.
     * @param attributeValue The value of the attribute
     */
    public final void setAttributeNS(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        setAttributeValue(namespaceURI, qualifiedName, attributeValue);
    }

    /**
     * Sets the value of the specified attribute.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue The value of the attribute
     */
    public final void setAttributeValue(final String attributeName, final String attributeValue) {
        setAttributeValue(null, attributeName, attributeValue);
    }

    /**
     * Sets the value of the specified attribute.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the attribute
     * @param attributeValue The value of the attribute
     */
    public void setAttributeValue(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        final String oldAttributeValue = getAttributeValue(qualifiedName);
        String value = attributeValue;

        if (attributes_ == Collections.EMPTY_MAP) {
            attributes_ = createAttributeMap(1);
        }
        if (value.length() == 0) {
            value = ATTRIBUTE_VALUE_EMPTY;
        }

        getPage().removeMappedElement(this);
        final HtmlAttr newAttr = addAttributeToMap(getPage(), attributes_, namespaceURI,
            qualifiedName.toLowerCase(), value);
        if (namespaceURI != null) {
            namespaces_.put(namespaceURI, newAttr.getPrefix());
        }
        getPage().addMappedElement(this);

        final HtmlAttributeChangeEvent event;
        if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
            event = new HtmlAttributeChangeEvent(this, qualifiedName, attributeValue);
        }
        else {
            event = new HtmlAttributeChangeEvent(this, qualifiedName, oldAttributeValue);
        }
        
        if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
            fireHtmlAttributeAdded(event);
            getPage().fireHtmlAttributeAdded(event);
        }
        else {
            fireHtmlAttributeReplaced(event);
            getPage().fireHtmlAttributeReplaced(event);
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
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param localName The name within the namespace.
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
     * @param event The event.
     */
    protected void fireHtmlAttributeAdded(final HtmlAttributeChangeEvent event) {
        if (attributeListeners_ != null) {
            synchronized (this) {
                for (final HtmlAttributeChangeListener listener : attributeListeners_) {
                    listener.attributeAdded(event);
                }
            }
        }
        final DomNode parentNode = getParentDomNode();
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
     * @param event The event.
     */
    protected void fireHtmlAttributeReplaced(final HtmlAttributeChangeEvent event) {
        if (attributeListeners_ != null) {
            synchronized (this) {
                for (final HtmlAttributeChangeListener listner : attributeListeners_) {
                    listner.attributeReplaced(event);
                }
            }
        }
        final DomNode parentNode = getParentDomNode();
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
     * @param event The event.
     */
    protected void fireHtmlAttributeRemoved(final HtmlAttributeChangeEvent event) {
        if (attributeListeners_ != null) {
            synchronized (this) {
                for (final HtmlAttributeChangeListener listener : attributeListeners_) {
                    listener.attributeRemoved(event);
                }
            }
        }
        final DomNode parentNode = getParentDomNode();
        if (parentNode instanceof HtmlElement) {
            ((HtmlElement) parentNode).fireHtmlAttributeRemoved(event);
        }
    }

    /**
     * Returns true if the specified attribute has been defined.  This is necessary
     * in order to distinguish between an attribute that is set to an empty string
     * and one that was not defined at all.
     *
     * @param attributeName The attribute to check
     * @return true if the attribute is defined
     */
    public boolean isAttributeDefined(final String attributeName) {
        return attributes_.get(attributeName.toLowerCase()) != null;
    }

    /**
     * @return an iterator over the {@link HtmlAttr} objects representing the
     * attributes of this element.
     * The elements are ordered as found in the HTML source code.
     * @deprecated As of version 2.0
     */
    public Iterator<HtmlAttr> getAttributeEntriesIterator() {
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
     * @return a collection of {@link HtmlAttr} objects representing the
     * attributes of this element. The elements are ordered as found in the HTML source code.
     */
    public Collection<HtmlAttr> getAttributesCollection() {
        return attributes_.values();
    }

    /**
     * Returns the tag name of this element.  The tag name is the actual HTML name.  For example
     * the tag name for HtmlAnchor is "a" and the tag name for HtmlTable is "table".
     * This tag name will always be in lowercase, no matter what case was used in the original
     * document, when no namespace is defined.
     *
     * @return the tag name of this element.
     */
    public String getTagName() {
        if (getNamespaceURI() == null) {
            return getLocalName().toLowerCase();
        }
        else {
            return getQualifiedName();
        }
    }

    /** @return the node type */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.ELEMENT_NODE;
    }

    /**
     * @return The same value as returned by {@link #getTagName()},
     */
    @Override
    public String getNodeName() {
        return getTagName();
    }

    /**
     * @return the identifier of this element.
     */
    public final String getId() {
        return getAttributeValue("id");
    }

    /**
     * Sets the identifier this element.
     *
     * @param newId The new identifier of this element.
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
            else {
                return TAB_INDEX_OUT_OF_BOUNDS;
            }
        }
        catch (final NumberFormatException e) {
            return null;
        }
    }

    /**
     * Returns the element with the given name that enclosed this element or null if this element is
     * no such element is found.
     * @param tagName the name of the tag searched (case insensitive)
     * @return See above
     */
    public HtmlElement getEnclosingElement(final String tagName) {
        final String tagNameLC = tagName.toLowerCase();

        DomNode currentNode = getParentDomNode();
        while (currentNode != null) {
            if (currentNode instanceof HtmlElement
                    && currentNode.getNodeName().equals(tagNameLC)) {

                return (HtmlElement) currentNode;
            }
            currentNode = currentNode.getParentDomNode();
        }
        return null;
    }

    /**
     * Returns the form that enclosed this element or null if this element is not within a form.
     *
     * @return See above
     */
    public HtmlForm getEnclosingForm() {
        return (HtmlForm) getEnclosingElement("form");
    }

    /**
     * Returns the form that enclosed this element or throw an exception if this element is not within a form.
     *
     * @return See above
     * @throws IllegalStateException If the element is not within a form.
     */
    public HtmlForm getEnclosingFormOrDie() throws IllegalStateException {
        final HtmlForm form = getEnclosingForm();
        if (form == null) {
            throw new IllegalStateException("Element is not contained within a form: " + this);
        }

        return form;
    }

    /**
     * Simulates pressing a key on this element.
     *
     * @param keyCode the key you wish to press
     * @deprecated use {@link #type(char)} instead
     */
    public void keyDown(final int keyCode) {
        keyDown(keyCode, false, false, false);
    }

    /**
     * Simulates pressing a key on this element.
     *
     * @param keyCode the key you wish to press
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @deprecated use {@link #type(char, boolean, boolean, boolean)} instead
     */
    public void keyDown(final int keyCode, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return;
        }
        fireEvent(new Event(this, Event.TYPE_KEY_DOWN, keyCode, shiftKey, ctrlKey, altKey));
    }

    /**
     * Simulates typing the specified text while this element has focus.
     * Note that for some elements, typing '\n' submits the enclosed form.
     * @param text the text you with to simulate typing
     * @exception IOException If an IO error occurs
     */
    public void type(final String text) throws IOException {
        for (int i = 0; i < text.length(); i++) {
            type(text.charAt(i));
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
        for (int i = 0; i < text.length(); i++) {
            type(text.charAt(i), shiftKey, ctrlKey, altKey);
        }
    }

    /**
     * Simulates typing the specified character while this element has focus.
     * Note that for some elements, typing '\n' submits the enclosed form.
     * @param c the character you with to simulate typing
     * @return The page that occupies this window after typing.
     * It may be the same window or it may be a freshly loaded one.
     * @exception IOException If an IO error occurs
     */
    public Page type(final char c) throws IOException {
        return type(c, false, false, false);
    }

    /**
     * Simulates typing the specified character while this element has focus.
     * Note that for some elements, typing '\n' submits the enclosed form.
     * @param c the character you with to simulate typing
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @return The page that occupies this window after typing.
     * It may be the same window or it may be a freshly loaded one.
     * @exception IOException If an IO error occurs
     */
    public Page type(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return getPage();
        }
        fireEvent(new Event(this, Event.TYPE_KEY_DOWN, c, shiftKey, ctrlKey, altKey));
        fireEvent(new Event(this, Event.TYPE_KEY_PRESS, c, shiftKey, ctrlKey, altKey));
        fireEvent(new Event(this, Event.TYPE_KEY_UP, c, shiftKey, ctrlKey, altKey));
        
        final HtmlForm form = getEnclosingForm();
        if (form != null && c == '\n' && isSubmittableByEnter()) {
            return form.submit((SubmittableElement) this);
        }
        else {
            return getPage();
        }
    }

    /**
     * Returns true if clicking Enter (ASCII 10, or '\n') should submit the enclosed form (if any).
     * Default implementation is false.
     * @return true if clicking Enter should submit the enclosed form (if any).
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
        final boolean hasChildren = (getFirstDomChild() != null);
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
            printWriter.print(StringEscapeUtils.escapeXml(((HtmlAttr) attributes_.get(name)).getNodeValue()));
            printWriter.print("\"");
        }
    }

    /**
     * Returns a string representation of this object.
     *
     * @return See above
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
     * Searches by the specified criteria and return the first HtmlElement that is found.
     *
     * @param elementName The name of the element.
     * @param attributeName The name of the attribute.
     * @param attributeValue The value of the attribute.
     * @return The HtmlElement.
     * @exception ElementNotFoundException If a particular XML element could not be found in the DOM model.
     */
    public final HtmlElement getOneHtmlElementByAttribute(
            final String elementName,
            final String attributeName,
            final String attributeValue)
        throws
            ElementNotFoundException {

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
     * Returns the HTML element with the specified id. If more than one element
     * has this id (not allowed by the HTML spec) then return the first one.
     *
     * @param id The id value to search by
     * @return The HTML element found
     * @exception ElementNotFoundException If no element was found that matches the id
     */
    public HtmlElement getHtmlElementById(final String id)
        throws ElementNotFoundException {

        return getPage().getHtmlElementById(id);
    }

    /**
     * Returns true if there is a element with the specified id. This method
     * is intended for situations where it is enough to know whether a specific
     * element is present in the document.<p>
     *
     * Implementation note: This method calls getHtmlElementById() internally
     * so writing code like the following would be extremely inefficient.
     * <pre>
     * if (hasHtmlElementWithId(id)) {
     *     HtmlElement element = getHtmlElementWithId(id)
     *     ...
     * }
     * </pre>
     *
     * @param id The id to search by
     * @return true if an element was found with the specified id.
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
     * Searches by the specified criteria and return all the HtmlElement that are found.
     *
     * @param elementName The name of the element
     * @param attributeName The name of the attribute
     * @param attributeValue The value of the attribute
     * @return A list of HtmlElements
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
            appendDomChild(child);
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
     * @return an Iterable over the HtmlElement children of this object, i.e. excluding the non-element nodes.
     */
    public final Iterable<HtmlElement> getChildElements() {
        return new Iterable<HtmlElement>() {
            public Iterator<HtmlElement> iterator() {
                return new ChildElementsIterator();
            }
        };
    }

    /**
     * @return an iterator over the HtmlElement children of this object, i.e. excluding the
     * non-element nodes
     * @deprecated After 1.14, use {@link #getChildElements()}.
     */
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
            if (getFirstDomChild() != null) {
                if (getFirstDomChild() instanceof HtmlElement) {
                    nextElement_ = (HtmlElement) getFirstDomChild();
                }
                else {
                    setNextElement(getFirstDomChild());
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
            final DomNode sibling = nextElement_.getPreviousDomSibling();
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
            else {
                throw new NoSuchElementException();
            }
        }

        private void setNextElement(final DomNode node) {
            DomNode next = node.getNextDomSibling();
            while (next != null && !(next instanceof HtmlElement)) {
                next = next.getNextDomSibling();
            }
            nextElement_ = (HtmlElement) next;
        }
    }

    /**
     * Creates an attribute map as needed by HtmlElement.  This is just used by the element factories.
     * @param attributeCount the initial number of attributes to be added to the map.
     * @return the attribute map.
     */
    @SuppressWarnings("unchecked")
    static Map<String, HtmlAttr> createAttributeMap(final int attributeCount) {
        return ListOrderedMap.decorate(new HashMap<String, HtmlAttr>(attributeCount)); // preserve insertion order
    }

    /**
      * Add an attribute to the attribute map.  This is just used by the element factories.
     * @param attributeMap the attribute map where the attribute will be added.
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the attribute
     * @param value The value of the attribute
     */
    static HtmlAttr addAttributeToMap(final HtmlPage page, final Map<String, HtmlAttr> attributeMap,
            final String namespaceURI, final String qualifiedName, final String value) {
        final HtmlAttr newAttr = new HtmlAttr(page, namespaceURI, qualifiedName, value);
        attributeMap.put(qualifiedName, newAttr);
        return newAttr;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Return a Function to be executed when a given event occurs.
     * @param eventName Name of event such as "onclick" or "onblur", etc.
     * @return A rhino JavaScript executable Function, or null if no event
     * handler has been defined
     */
    public final Function getEventHandler(final String eventName) {
        final HTMLElement jsObj = (HTMLElement) getScriptObject();
        return jsObj.getEventHandler(eventName);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Register a Function as an event handler.
     * @param eventName Name of event such as "onclick" or "onblur", etc.
     * @param eventHandler A rhino JavaScript executable Function
     */
    public final void setEventHandler(final String eventName, final Function eventHandler) {
        final HTMLElement jsObj = (HTMLElement) getScriptObject();
        jsObj.setEventHandler(eventName, eventHandler);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Register a snippet of JavaScript code as an event handler.  The JavaScript code will
     * be wrapped inside a unique function declaration which provides one argument named
     * "event"
     * @param eventName Name of event such as "onclick" or "onblur", etc.
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
     * @param eventName Name of the event such as "onclick" or "onblur", etc.
     */
    public final void removeEventHandler(final String eventName) {
        setEventHandler(eventName, (Function) null);
    }

    /**
     * Adds an HtmlAttributeChangeListener to the listener list.
     * The listener is registered for all attributes of this HtmlElement,
     * as well as descendant elements.
     *
     * @param listener the attribute change listener to be added.
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
     * @param listener the attribute change listener to be removed.
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
     * @return the execution result. <code>null</code> if nothing is executed.
     */
    public ScriptResult fireEvent(final String eventType) {
        return fireEvent(new Event(this, eventType));
    }

    /**
     * Fire the event on the element. Nothing is done if JavaScript is disabled
     * @param event the event to fire.
     * @return the execution result. <code>null</code> if nothing is executed.
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
    }

    /**
     * Simulate moving the mouse over this element.
     *
     * @return The page that occupies this window after the mouse moves over this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseOver() {
        return mouseOver(false, false, false, MouseEvent.BUTTON_LEFT);
    }
    
    /**
     * Simulate moving the mouse over this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     *
     * @return The page that occupies this window after the mouse moves over this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseOver(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_OVER, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulate moving the mouse inside this element.
     *
     * @return The page that occupies this window after the mouse moves inside this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseMove() {
        return mouseMove(false, false, false, MouseEvent.BUTTON_LEFT);
    }
    
    /**
     * Simulate moving the mouse inside this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     *
     * @return The page that occupies this window after the mouse moves inside this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseMove(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_MOVE, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulate moving the mouse out of this element.
     *
     * @return The page that occupies this window after the mouse moves out of this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseOut() {
        return mouseOut(false, false, false, MouseEvent.BUTTON_LEFT);
    }
    
    /**
     * Simulate moving the mouse out of this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     *
     * @return The page that occupies this window after the mouse moves out of this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseOut(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_OUT, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulate clicking the mouse in this element.
     *
     * @return The page that occupies this window after the mouse is clicked in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseDown() {
        return mouseDown(false, false, false, MouseEvent.BUTTON_LEFT);
    }
    
    /**
     * Simulate clicking the mouse in this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     *
     * @return The page that occupies this window after the mouse is clicked in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseDown(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_DOWN, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulate releasing the mouse click in this element.
     *
     * @return The page that occupies this window after the mouse click is released in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseUp() {
        return mouseUp(false, false, false, MouseEvent.BUTTON_LEFT);
    }
    
    /**
     * Simulate releasing the mouse click in this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     *
     * @return The page that occupies this window after the mouse click is released in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseUp(final boolean shiftKey, final boolean ctrlKey, final boolean altKey, final int button) {
        return doMouseEvent(MouseEvent.TYPE_MOUSE_UP, shiftKey, ctrlKey, altKey, button);
    }

    /**
     * Simulate the given mouse event.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link MouseEvent#BUTTON_LEFT}, {@link MouseEvent#BUTTON_MIDDLE}
     *        or {@link MouseEvent#BUTTON_RIGHT}
     *
     * @return The page that occupies this window after the mouse event occur on this element.
     * It may be the same window or it may be a freshly loaded one.
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
     * Simulate right clicking the mouse in this element.
     *
     * @return The page that occupies this window after the mouse is right clicked in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page rightClick() {
        return rightClick(false, false, false);
    }

    /**
     * Simulate right clicking the mouse in this element.
     *
     * <p>This is equivalent to calling {@link #mouseDown(boolean, boolean, boolean, int)},
     * then {@link #mouseUp(boolean, boolean, boolean, int)}
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     *
     * @return The page that occupies this window after the mouse is right clicked in this element.
     * It may be the same window or it may be a freshly loaded one.
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
     * Remove focus from this element.
     */
    public void blur() {
        getPage().setFocusedElement(null);
    }

    /**
     * Sets the focus to this element.
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
}
