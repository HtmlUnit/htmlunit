/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * An array of elements. Used for the element arrays returned by <tt>document.all</tt>,
 * <tt>document.all.tags('x')</tt>, <tt>document.forms</tt>, <tt>window.frames</tt>, etc.
 * Note that this class must not be used for collections that can be modified, for example
 * <tt>map.areas</tt> and <tt>select.options</tt>.
 * <br>
 * This class (like all classes in this package) is specific for the JavaScript engine.
 * Users of HtmlUnit shouldn't use it directly.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLCollection extends SimpleScriptable implements Function, NodeList {
    /**
     * Cache effect of some changes.
     */
    protected enum EffectOnCache {
        /** No effect, cache is still valid. */
        NONE,
        /** Cache is not valid anymore and should be reset. */
        RESET
    }

    private DomNode node_;
    private boolean avoidObjectDetection_ = false;
    private String description_;
    private boolean attributeChangeSensitive_ = true;

    /**
     * Cache collection elements when possible, so as to avoid expensive XPath expression evaluations.
     */
    private List<Object> cachedElements_;

    /**
     * IE provides a way of enumerating through some element collections; this counter supports that functionality.
     */
    private int currentIndex_ = 0;

    private boolean listenerRegistered_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     * Don't call.
     */
    @Deprecated
    public HTMLCollection() {
        // Empty.
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     */
    private HTMLCollection(final ScriptableObject parentScope) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     * @param description a text useful for debugging
     */
    public HTMLCollection(final DomNode parentScope, final boolean attributeChangeSensitive, final String description) {
        node_ = parentScope;
        description_ = description;
        attributeChangeSensitive_ = attributeChangeSensitive;
        setParentScope(parentScope.getScriptObject());
        setPrototype(getPrototype(getClass()));
        setDomNode(node_, false);
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param parentScope the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    HTMLCollection(final DomNode parentScope, final List<?> initialElements) {
        this(parentScope.getScriptObject());
        cachedElements_ = new ArrayList<Object>(initialElements);
    }

    /**
     * Gets an empty collection.
     * @param window the current scope
     * @return an empty collection
     */
    public static HTMLCollection emptyCollection(final Window window) {
        final List<Object> list = Collections.emptyList();
        return new HTMLCollection(window) {
            @Override
            protected List<Object> getElements() {
                return list;
            }
        };
    }

    /**
     * Only needed to make collections like <tt>document.all</tt> available but "invisible" when simulating Firefox.
     * {@inheritDoc}
     */
    @Override
    public boolean avoidObjectDetection() {
        return avoidObjectDetection_;
    }

    /**
     * @param newValue the new value
     */
    public void setAvoidObjectDetection(final boolean newValue) {
        avoidObjectDetection_ = newValue;
    }

    /**
     * {@inheritDoc}
     */
    public final Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (args.length == 0) {
            throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
        }
        return nullIfNotFound(getIt(args[0]));
    }

    /**
     * {@inheritDoc}
     */
    public final Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        return null;
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
     * Returns the element at the specified index, or <tt>NOT_FOUND</tt> if the index is invalid.
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final HTMLCollection array = (HTMLCollection) start;
        final List<Object> elements = array.getElements();
        if (index >= 0 && index < elements.size()) {
            return getScriptableForElement(elements.get(index));
        }
        return NOT_FOUND;
    }

    /**
     * Gets the HTML elements from cache or retrieve them at first call.
     * @return the list of {@link HtmlElement} contained in this collection
     */
    protected List<Object> getElements() {
        if (cachedElements_ == null) {
            cachedElements_ = computeElements();
            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            if (!listenerRegistered_) {
                node_.addDomChangeListener(listener);
                if (attributeChangeSensitive_ && node_ instanceof HtmlElement) {
                    ((HtmlElement) node_).addHtmlAttributeChangeListener(listener);
                }
                listenerRegistered_ = true;
            }
        }
        return cachedElements_;
    }

    /**
     * Returns the elements whose associated host objects are available through this collection.
     * @return the elements whose associated host objects are available through this collection
     */
    protected List<Object> computeElements() {
        final List<Object> response;
        if (node_ != null) {
            response = new ArrayList<Object>();
            for (final DomNode node : getCandidates()) {
                if (node instanceof DomElement && isMatching(node)) {
                    response.add(node);
                }
            }
        }
        else {
            response = new ArrayList<Object>();
        }

        return response;
    }

    /**
     * Gets the DOM node that have to be examined to see if they are matching.
     * Default implementation looks at all descendants of reference node.
     * @return the nodes
     */
    protected Iterable<DomNode> getCandidates() {
        return node_.getDescendants();
    }

    /**
     * Indicates if the node should belong to the collection.
     * Belongs to the refactoring effort to improve HTMLCollection's performance.
     * @param node the node to test. Will be a child node of the reference node.
     * @return <code>false</code> here as subclasses for concrete collections should decide it.
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

        final List<Object> elements = getElements();

        // See if there is an element in the element array with the specified id.
        final List<Object> matchingElements = new ArrayList<Object>();

        for (final Object next : elements) {
            if (next instanceof DomElement) {
                final String id = ((DomElement) next).getAttribute("id");
                if (name.equals(id)) {
                    if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_IDENTICAL_IDS)) {
                        return getScriptableForElement(next);
                    }
                    matchingElements.add(next);
                }
            }
        }

        if (matchingElements.size() == 1) {
            return getScriptableForElement(matchingElements.get(0));
        }
        else if (!matchingElements.isEmpty()) {
            final HTMLCollection collection = new HTMLCollection(getDomNodeOrDie(), matchingElements);
            collection.setAvoidObjectDetection(!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_46));
            return collection;
        }

        // no element found by id, let's search by name
        for (final Object next : elements) {
            if (next instanceof DomElement) {
                final String nodeName = ((DomElement) next).getAttribute("name");
                if (name.equals(nodeName)) {
                    if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_IDENTICAL_IDS)) {
                        return getScriptableForElement(next);
                    }
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
        final HTMLCollection collection = new HTMLCollection(node_, matchingElements);
        collection.setAvoidObjectDetection(!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_46));
        return collection;
    }

    /**
     * Returns the length of this element array.
     * @return the length of this element array
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534101.aspx">MSDN doc</a>
     */
    public final int jsxGet_length() {
        return getElements().size();
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536460.aspx">MSDN doc</a>
     */
    public final Object jsxFunction_item(final Object index) {
        return nullIfNotFound(getIt(index));
    }

    /**
     * Returns the specified object, unless it is the <tt>NOT_FOUND</tt> constant, in which case <tt>null</tt>
     * is returned for IE.
     * @param object the object to return
     * @return the specified object, unless it is the <tt>NOT_FOUND</tt> constant, in which case <tt>null</tt>
     *         is returned for IE.
     */
    private Object nullIfNotFound(final Object object) {
        if (object == NOT_FOUND) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_48)) {
                return null;
            }
            return Context.getUndefinedValue();
        }
        return object;
    }

    /**
     * Retrieves the item or items corresponding to the specified name (checks ids, and if
     * that does not work, then names).
     * @param name the name or id the element or elements to return
     * @return the element or elements corresponding to the specified name or id
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536634.aspx">MSDN doc</a>
     */
    public final Object jsxFunction_namedItem(final String name) {
        return nullIfNotFound(getIt(name));
    }

    /**
     * Returns the next node in the collection (supporting iteration in IE only).
     * @return the next node in the collection
     */
    public Object jsxFunction_nextNode() {
        Object nextNode;
        final List<Object> elements = getElements();
        if (currentIndex_ >= 0 && currentIndex_ < elements.size()) {
            nextNode = elements.get(currentIndex_);
        }
        else {
            nextNode = null;
        }
        currentIndex_++;
        return nextNode;
    }

    /**
     * Resets the node iterator accessed via {@link #jsxFunction_nextNode()}.
     */
    public void jsxFunction_reset() {
        currentIndex_ = 0;
    }

    /**
     * Returns all the elements in this element array that have the specified tag name.
     * This method returns an empty element array if there are no elements with the
     * specified tag name.
     * @param tagName the name of the tag of the elements to return
     * @return all the elements in this element array that have the specified tag name
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536776.aspx">MSDN doc</a>
     */
    public Object jsxFunction_tags(final String tagName) {
        final String tagNameLC = tagName.toLowerCase();
        final HTMLCollection collection = new HTMLSubCollection(this, ".tags('" + tagName + "')") {
            @Override
            protected boolean isMatching(final DomNode node) {
                return tagNameLC.equalsIgnoreCase(node.getLocalName());
            }
        };
        return collection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return description_;
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
        else if (other instanceof HTMLCollection) {
            final HTMLCollection otherArray = (HTMLCollection) other;
            if (getClass() == other.getClass()
                    && node_ == otherArray.node_
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
    public boolean has(final String name, final Scriptable start) {
        // let's Rhino work normally if current instance is the prototype
        if (isPrototype()) {
            return super.has(name, start);
        }

        try {
            final int index = Integer.parseInt(name);
            final List<Object> elements = getElements();
            if (index >= 0 && index < elements.size()) {
                return true;
            }
        }
        catch (final NumberFormatException e) {
            // Ignore.
        }

        if ("length".equals(name)) {
            return true;
        }
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_49)) {
            final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(BrowserVersion.FIREFOX_3_6);
            for (final String functionName : jsConfig.getClassConfiguration(getClassName()).functionKeys()) {
                if (name.equals(functionName)) {
                    return true;
                }
            }
            return false;
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

        final List<String> idList = new ArrayList<String>();

        final List<Object> elements = getElements();

        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_50)) {
            final int length = elements.size();
            for (int i = 0; i < length; i++) {
                idList.add(Integer.toString(i));
            }

            idList.add("length");
            final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(BrowserVersion.FIREFOX_3_6);
            for (final String name : jsConfig.getClassConfiguration(getClassName()).functionKeys()) {
                idList.add(name);
            }
        }
        else {
            idList.add("length");

            addElementIds(idList, elements);
        }
        return idList.toArray();
    }

    private boolean isPrototype() {
        return !(getPrototype() instanceof HTMLCollection);
    }

    /**
     * Adds the ids of the collection's elements to the idList.
     * @param idList the list to add the ids to
     * @param elements the collection's elements
     */
    protected void addElementIds(final List<String> idList, final List<Object> elements) {
        int index = 0;
        for (final Object next : elements) {
            final HtmlElement element = (HtmlElement) next;
            final String name = element.getAttribute("name");
            if (name != HtmlElement.ATTRIBUTE_NOT_DEFINED) {
                idList.add(name);
            }
            else {
                final String id = element.getId();
                if (id != HtmlElement.ATTRIBUTE_NOT_DEFINED) {
                    idList.add(id);
                }
                else {
                    idList.add(Integer.toString(index));
                }
            }
            index++;
        }
    }

    private class DomHtmlAttributeChangeListenerImpl implements DomChangeListener, HtmlAttributeChangeListener {

        /**
         * {@inheritDoc}
         */
        public void nodeAdded(final DomChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void nodeDeleted(final DomChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            handleChangeOnCache(getEffectOnCache(event), event.getHtmlElement());
        }

        /**
         * {@inheritDoc}
         */
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            handleChangeOnCache(getEffectOnCache(event), event.getHtmlElement());
        }

        /**
         * {@inheritDoc}
         */
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            if (attributeChangeSensitive_) {
                handleChangeOnCache(getEffectOnCache(event), event.getHtmlElement());
            }
        }

        private void handleChangeOnCache(final EffectOnCache effectOnCache, final HtmlElement htmlElement) {
            if (EffectOnCache.NONE == effectOnCache) {
                return;
            }
            else if (EffectOnCache.RESET == effectOnCache) {
                cachedElements_ = null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getLength() {
        return jsxGet_length();
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
    public Node item(final int index) {
        return (DomNode) getElements().get(index);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return "HTMLCollection";
    }
}

class HTMLSubCollection extends HTMLCollection {
    private final HTMLCollection mainCollection_;

    public HTMLSubCollection(final HTMLCollection mainCollection, final String subDescription) {
        super(mainCollection.getDomNodeOrDie(), false, mainCollection.toString() + subDescription);
        mainCollection_ = mainCollection;
    }

    @Override
    protected List<Object> computeElements() {
        final List<Object> list = new ArrayList<Object>();
        for (final Object o : mainCollection_.getElements()) {
            if (isMatching((DomNode) o)) {
                list.add(o);
            }
        }
        return list;
    }
}
