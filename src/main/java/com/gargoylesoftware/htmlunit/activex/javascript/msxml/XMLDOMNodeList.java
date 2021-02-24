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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMNodeList.<br>
 * Supports iteration through the live collection, in addition to indexed access.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms757073.aspx">MSDN documentation</a>
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass(IE)
public class XMLDOMNodeList extends MSXMLScriptable implements Function, org.w3c.dom.NodeList {

    /**
     * Cache effect of some changes.
     */
    protected enum EffectOnCache {
        /** No effect, cache is still valid. */
        NONE,
        /** Cache is not valid anymore and should be reset. */
        RESET
    }

    private String description_;

    private final boolean attributeChangeSensitive_;

    /**
     * Cache collection elements when possible, so as to avoid expensive XPath expression evaluations.
     */
    private List<DomNode> cachedElements_;

    private boolean listenerRegistered_;

    /**
     * IE provides a way of enumerating through some element collections; this counter supports that functionality.
     */
    private int currentIndex_;

    /**
     * Creates an instance.
     */
    public XMLDOMNodeList() {
        attributeChangeSensitive_ = true;
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     * @param description a text useful for debugging
     */
    XMLDOMNodeList(final ScriptableObject parentScope, final boolean attributeChangeSensitive,
            final String description) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
        description_ = description;
        attributeChangeSensitive_ = attributeChangeSensitive;
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     * @param description a text useful for debugging
     */
    public XMLDOMNodeList(final DomNode parentScope, final boolean attributeChangeSensitive, final String description) {
        this((ScriptableObject) parentScope.getScriptableObject(), attributeChangeSensitive, description);
        setDomNode(parentScope, false);
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param parentScope the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    protected XMLDOMNodeList(final DomNode parentScope, final List<DomNode> initialElements) {
        this((ScriptableObject) parentScope.getScriptableObject(), true, null);
        cachedElements_ = new ArrayList<>(initialElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public final int getLength() {
        return getElements().size();
    }

    /**
     * Allows random access to individual nodes within the collection.
     * @param index the index of the item within the collection; the first item is zero
     * @return the element or elements corresponding to the specified index or key
     */
    @JsxFunction
    public final Object item(final Object index) {
        return nullIfNotFound(getIt(index));
    }

    /**
     * Returns the next node in the collection.
     * @return the next node in the collection
     */
    @JsxFunction
    public Object nextNode() {
        final Object nextNode;
        final List<DomNode> elements = getElements();
        if (currentIndex_ >= 0 && currentIndex_ < elements.size()) {
            nextNode = elements.get(currentIndex_).getScriptableObject();
        }
        else {
            nextNode = null;
        }
        currentIndex_++;
        return nextNode;
    }

    /**
     * Resets the iterator accessed via {@link #nextNode()}.
     */
    @JsxFunction
    public void reset() {
        currentIndex_ = 0;
    }

    /**
     * Gets an empty collection.
     * @param parentScope the current scope
     * @return an empty collection
     */
    public static XMLDOMNodeList emptyCollection(final MSXMLScriptable parentScope) {
        final List<DomNode> list = Collections.emptyList();
        return new XMLDOMNodeList(parentScope, true, null) {
            @Override
            protected List<DomNode> getElements() {
                return list;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (args.length == 0) {
            throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
        }
        return nullIfNotFound(getIt(args[0]));
    }

    /**
     * Private helper that retrieves the item or items corresponding to the specified
     * index or key.
     * @param o the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     */
    private Object getIt(final Object o) {
        if (o instanceof Number) {
            final Number n = (Number) o;
            final int i = n.intValue();
            return get(i, this);
        }
        final String key = String.valueOf(o);
        return get(key, this);
    }

    /**
     * Returns the element at the specified index, or {@link #NOT_FOUND} if the index is invalid.
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final XMLDOMNodeList array = (XMLDOMNodeList) start;
        final List<DomNode> elements = array.getElements();
        if (index >= 0 && index < elements.size()) {
            return getScriptableForElement(elements.get(index));
        }
        return NOT_FOUND;
    }

    /**
     * Gets the HTML elements from cache or retrieve them at first call.
     * @return the list of {@link HtmlElement} contained in this collection
     */
    protected List<DomNode> getElements() {
        // a bit strange but we like to avoid sync
        List<DomNode> cachedElements = cachedElements_;

        if (cachedElements == null) {
            cachedElements = computeElements();
            cachedElements_ = cachedElements;
            if (!listenerRegistered_) {
                final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl(this);
                final DomNode domNode = getDomNodeOrNull();
                if (domNode != null) {
                    domNode.addDomChangeListener(listener);
                    if (attributeChangeSensitive_ && domNode instanceof HtmlElement) {
                        ((HtmlElement) domNode).addHtmlAttributeChangeListener(listener);
                    }
                }
                listenerRegistered_ = true;
            }
        }

        // maybe the cache was cleared in between
        // then this returns the old state and never null
        return cachedElements;
    }

    /**
     * Returns the elements whose associated host objects are available through this collection.
     * @return the elements whose associated host objects are available through this collection
     */
    protected List<DomNode> computeElements() {
        final List<DomNode> response = new ArrayList<>();
        final DomNode domNode = getDomNodeOrNull();
        if (domNode == null) {
            return response;
        }
        for (final DomNode node : getCandidates()) {
            if (node instanceof DomElement && isMatching(node)) {
                response.add(node);
            }
        }
        return response;
    }

    /**
     * Gets the DOM node that have to be examined to see if they are matching.
     * Default implementation looks at all descendants of reference node.
     * @return the nodes
     */
    protected Iterable<DomNode> getCandidates() {
        final DomNode domNode = getDomNodeOrNull();
        return domNode.getDescendants();
    }

    /**
     * Indicates if the node should belong to the collection.
     * Belongs to the refactoring effort to improve HTMLCollection's performance.
     * @param node the node to test. Will be a child node of the reference node.
     * @return {@code false} here as subclasses for concrete collections should decide it.
     */
    protected boolean isMatching(final DomNode node) {
        return false;
    }

    /**
     * Returns the element or elements that match the specified key. If it is the name
     * of a property, the property value is returned. If it is the id of an element in
     * the array, that element is returned. Finally, if it is the name of an element or
     * elements in the array, then all those elements are returned. Otherwise,
     * {@link #NOT_FOUND} is returned.
     * {@inheritDoc}
     */
    @Override
    protected Object getWithPreemption(final String name) {
        // Test to see if we are trying to get the length of this collection?
        // If so return NOT_FOUND here to let the property be retrieved using the prototype
        if (/*xpath_ == null || */"length".equals(name)) {
            return NOT_FOUND;
        }

        final List<DomNode> elements = getElements();

        // See if there is an element in the element array with the specified id.
        final List<DomNode> matchingElements = new ArrayList<>();

        for (final DomNode next : elements) {
            if (next instanceof DomElement) {
                final String id = ((DomElement) next).getId();
                if (name.equals(id)) {
                    matchingElements.add(next);
                }
            }
        }

        if (matchingElements.size() == 1) {
            return getScriptableForElement(matchingElements.get(0));
        }
        else if (!matchingElements.isEmpty()) {
            final XMLDOMNodeList collection = new XMLDOMNodeList(getDomNodeOrDie(), matchingElements);
            return collection;
        }

        // no element found by id, let's search by name
        for (final DomNode next : elements) {
            if (next instanceof DomElement) {
                final String nodeName = ((DomElement) next).getAttributeDirect("name");
                if (name.equals(nodeName)) {
                    matchingElements.add(next);
                }
            }
        }

        if (matchingElements.isEmpty()) {
            return NOT_FOUND;
        }
        else if (matchingElements.size() == 1) {
            return getScriptableForElement(matchingElements.get(0));
        }

        // many elements => build a sub collection
        final DomNode domNode = getDomNodeOrNull();
        final XMLDOMNodeList collection = new XMLDOMNodeList(domNode, matchingElements);
        return collection;
    }

    /**
     * Returns the specified object, unless it is the {@link #NOT_FOUND} constant, in which case {@code null}
     * is returned for IE.
     * @param object the object to return
     * @return the specified object, unless it is the {@link #NOT_FOUND} constant, in which case {@code null}
     *         is returned for IE.
     */
    private static Object nullIfNotFound(final Object object) {
        if (object == NOT_FOUND) {
            return null;
        }
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return description_ == null ? super.toString() : description_;
    }

    /**
     * Called for the js "==".
     * {@inheritDoc}
     */
    @Override
    protected Object equivalentValues(final Object other) {
        if (other == this) {
            return Boolean.TRUE;
        }
        else if (other instanceof XMLDOMNodeList) {
            final XMLDOMNodeList otherArray = (XMLDOMNodeList) other;
            final DomNode domNode = getDomNodeOrNull();
            final DomNode domNodeOther = otherArray.getDomNodeOrNull();
            if (getClass() == other.getClass()
                    && domNode == domNodeOther
                    && getElements().equals(otherArray.getElements())) {
                return Boolean.TRUE;
            }
            return NOT_FOUND;
        }

        return super.equivalentValues(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final int index, final Scriptable start) {
        return index >= 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        // let's Rhino work normally if current instance is the prototype
        if (isPrototype()) {
            return super.has(name, start);
        }

        try {
            return has(Integer.parseInt(name), start);
        }
        catch (final NumberFormatException e) {
            // Ignore.
        }

        if ("length".equals(name)) {
            return true;
        }
        return getWithPreemption(name) != NOT_FOUND;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Object[] getIds() {
        // let's Rhino work normally if current instance is the prototype
        if (isPrototype()) {
            return super.getIds();
        }

        final List<String> idList = new ArrayList<>();

        final List<DomNode> elements = getElements();

        idList.add("length");

        addElementIds(idList, elements);
        return idList.toArray();
    }

    private boolean isPrototype() {
        return !(getPrototype() instanceof XMLDOMNodeList);
    }

    /**
     * Adds the ids of the collection's elements to the idList.
     * @param idList the list to add the ids to
     * @param elements the collection's elements
     */
    protected void addElementIds(final List<String> idList, final List<DomNode> elements) {
        int index = 0;
        for (final DomNode next : elements) {
            final HtmlElement element = (HtmlElement) next;
            final String name = element.getAttributeDirect("name");
            if (name == DomElement.ATTRIBUTE_NOT_DEFINED) {
                final String id = element.getId();
                if (id == DomElement.ATTRIBUTE_NOT_DEFINED) {
                    idList.add(Integer.toString(index));
                }
                else {
                    idList.add(id);
                }
            }
            else {
                idList.add(name);
            }
            index++;
        }
    }

    private static final class DomHtmlAttributeChangeListenerImpl
                                    implements DomChangeListener, HtmlAttributeChangeListener {

        private final transient WeakReference<XMLDOMNodeList> nodeList_;

        DomHtmlAttributeChangeListenerImpl(final XMLDOMNodeList nodeList) {
            super();

            nodeList_ = new WeakReference<>(nodeList);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void nodeAdded(final DomChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void nodeDeleted(final DomChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            handleChangeOnCache(event);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            handleChangeOnCache(event);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            final XMLDOMNodeList nodes = nodeList_.get();
            if (null == nodes) {
                return;
            }
            if (nodes.attributeChangeSensitive_) {
                handleChangeOnCache(nodes, event);
            }
        }

        private void handleChangeOnCache(final HtmlAttributeChangeEvent event) {
            final XMLDOMNodeList nodes = nodeList_.get();
            if (null == nodes) {
                return;
            }
            handleChangeOnCache(nodes, event);
        }

        private void handleChangeOnCache(final XMLDOMNodeList nodes, final HtmlAttributeChangeEvent event) {
            final EffectOnCache effectOnCache = nodes.getEffectOnCache(event);
            if (EffectOnCache.NONE == effectOnCache) {
                return;
            }
            if (EffectOnCache.RESET == effectOnCache) {
                clearCache();
            }
        }

        private void clearCache() {
            final XMLDOMNodeList nodes = nodeList_.get();
            if (null != nodes) {
                nodes.cachedElements_ = null;
            }
        }
    }

    /**
     * Gets the effect of the change on an attribute of the reference node
     * on this collection's cache.
     * @param event the change event
     * @return the effect on cache
     */
    protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
        return EffectOnCache.RESET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node item(final int index) {
        return getElements().get(index);
    }

    /**
     * Gets the scriptable for the provided element that may already be the right scriptable.
     * @param object the object for which to get the scriptable
     * @return the scriptable
     */
    protected Scriptable getScriptableForElement(final Object object) {
        if (object instanceof Scriptable) {
            return (Scriptable) object;
        }
        return getScriptableFor(object);
    }
}
