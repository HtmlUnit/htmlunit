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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_NODE_LIST_ENUMERATE_CHILDREN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_NODE_LIST_ENUMERATE_FUNCTIONS;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * The parent class of {@link NodeList} and {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(isJSObject = false, isDefinedInStandardsMode = false)
public class AbstractList extends SimpleScriptable implements Function {

    /**
     * Cache effect of some changes.
     */
    protected enum EffectOnCache {
        /** No effect, cache is still valid. */
        NONE,
        /** Cache is not valid anymore and should be reset. */
        RESET
    }

    private boolean avoidObjectDetection_;

    private boolean attributeChangeSensitive_;

    /**
     * Cache collection elements when possible, so as to avoid expensive XPath expression evaluations.
     */
    private List<Object> cachedElements_;

    private boolean listenerRegistered_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public AbstractList() {
    }

    /**
     * Creates an instance.
     *
     * @param domeNode the {@link DomNode}
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     */
    public AbstractList(final DomNode domeNode, final boolean attributeChangeSensitive) {
        this(domeNode, attributeChangeSensitive, null);
    }

    /**
     * Creates an instance with an initial cache value.
     *
     * @param domNode the {@link DomNode}
     * @param initialElements the initial content for the cache
     */
    protected AbstractList(final DomNode domNode, final List<?> initialElements) {
        this(domNode, true, new ArrayList<>(initialElements));
    }

    /**
     * Creates an instance.
     *
     * @param domeNode the {@link DomNode}
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     * @param initialElements the initial content for the cache
     */
    private AbstractList(final DomNode domeNode, final boolean attributeChangeSensitive,
            final List<Object> initialElements) {
        if (domeNode != null) {
            setDomNode(domeNode, false);
            final ScriptableObject parentScope = domeNode.getScriptableObject();
            if (parentScope != null) {
                setParentScope(parentScope);
                setPrototype(getPrototype(getClass()));
            }
        }
        attributeChangeSensitive_ = attributeChangeSensitive;
        cachedElements_ = initialElements;
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
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (args.length == 0) {
            throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
        }
        final Object object = getIt(args[0]);
        if (object == NOT_FOUND) {
            if (getBrowserVersion().hasFeature(HTMLCOLLECTION_NULL_IF_NOT_FOUND)) {
                return null;
            }
            return Context.getUndefinedValue();
        }
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * Returns the element at the specified index, or {@link #NOT_FOUND} if the index is invalid.
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final AbstractList array = (AbstractList) start;
        final List<Object> elements = array.getElements();
        if (index >= 0 && index < elements.size()) {
            return getScriptableForElement(elements.get(index));
        }
        return NOT_FOUND;
    }

    @Override
    protected void setDomNode(final DomNode domNode, final boolean assignScriptObject) {
        final DomNode oldDomNode = getDomNodeOrNull();

        super.setDomNode(domNode, assignScriptObject);

        if (oldDomNode != domNode) {
            listenerRegistered_ = false;
        }
    }

    /**
     * Gets the HTML elements from cache or retrieve them at first call.
     * @return the list of {@link HtmlElement} contained in this collection
     */
    public List<Object> getElements() {
        // a bit strange but we like to avoid sync
        List<Object> cachedElements = cachedElements_;

        if (cachedElements == null) {
            if (getParentScope() == null) {
                cachedElements = new ArrayList<>();
            }
            else {
                cachedElements = computeElements();
            }
            cachedElements_ = cachedElements;
            if (!listenerRegistered_) {
                final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl(this);
                final DomNode domNode = getDomNodeOrNull();
                if (domNode != null) {
                    domNode.addDomChangeListener(listener);
                    if (attributeChangeSensitive_) {
                        if (domNode instanceof HtmlElement) {
                            ((HtmlElement) domNode).addHtmlAttributeChangeListener(listener);
                        }
                        else if (domNode instanceof HtmlPage) {
                            ((HtmlPage) domNode).addHtmlAttributeChangeListener(listener);
                        }
                    }
                    listenerRegistered_ = true;
                }
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
    protected List<Object> computeElements() {
        final List<Object> response = new ArrayList<>();
        final DomNode domNode = getDomNodeOrNull();
        if (domNode == null) {
            return response;
        }
        for (final DomNode node : getCandidates()) {
            if ((node instanceof DomElement) && isMatching(node)) {
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

        final List<Object> elements = getElements();

        // See if there is an element in the element array with the specified id.
        final List<Object> matchingElements = new ArrayList<>();

        for (final Object next : elements) {
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
            final AbstractList collection = create(getDomNodeOrDie(), matchingElements);
            collection.setAvoidObjectDetection(true);
            return collection;
        }

        // no element found by id, let's search by name
        return getWithPreemptionByName(name, elements);
    }

    /**
     * Constructs a new instance with an initial cache value.
     * @param parentScope the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     * @return the newly created instance
     */
    protected AbstractList create(final DomNode parentScope, final List<?> initialElements) {
        return new AbstractList(parentScope, initialElements);
    }

    /**
     * Helper for {@link #getWithPreemption(String)} when finding by id doesn't get results.
     * @param name the property name
     * @param elements the children elements.
     * @return {@link Scriptable#NOT_FOUND} if not found
     */
    protected Object getWithPreemptionByName(final String name, final List<Object> elements) {
        final List<Object> matchingElements = new ArrayList<>();
        for (final Object next : elements) {
            if (next instanceof DomElement) {
                final String nodeName = ((DomElement) next).getAttribute("name");
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
        final AbstractList collection = create(domNode, matchingElements);
        collection.setAvoidObjectDetection(true);
        return collection;
    }

    /**
     * Returns the length.
     * @return the length
     */
    @JsxGetter
    public final int getLength() {
        return getElements().size();
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536460.aspx">MSDN doc</a>
     */
    @JsxFunction
    public Object item(final Object index) {
        final Object object = getIt(index);
        if (object == NOT_FOUND) {
            if (getBrowserVersion().hasFeature(HTMLCOLLECTION_NULL_IF_ITEM_NOT_FOUND)) {
                return null;
            }
            return Context.getUndefinedValue();
        }
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " for " + getDomNodeOrNull();
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
        else if (other instanceof AbstractList) {
            final AbstractList otherArray = (AbstractList) other;
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
        return index >= 0 && index < getElements().size();
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
        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(JS_NODE_LIST_ENUMERATE_FUNCTIONS)) {
            final JavaScriptConfiguration jsConfig = getWindow().getWebWindow().getWebClient()
                    .getJavaScriptEngine().getJavaScriptConfiguration();
            if (jsConfig.getClassConfiguration(getClassName()).getFunctionKeys().contains(name)) {
                return true;
            }
        }

        if (browserVersion.hasFeature(JS_NODE_LIST_ENUMERATE_CHILDREN)) {
            for (final Object next : getElements()) {
                if (next instanceof DomElement) {
                    final DomElement element = (DomElement) next;
                    if (name.equals(element.getAttribute("name"))) {
                        return true;
                    }

                    final String id = element.getId();
                    if (name.equals(id)) {
                        return true;
                    }
                }
            }
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
        final List<Object> elements = getElements();

        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(JS_NODE_LIST_ENUMERATE_FUNCTIONS)) {
            final int length = elements.size();
            for (int i = 0; i < length; i++) {
                idList.add(Integer.toString(i));
            }

            idList.add("length");
            final JavaScriptConfiguration jsConfig = getWindow().getWebWindow().getWebClient()
                .getJavaScriptEngine().getJavaScriptConfiguration();
            for (final String name : jsConfig.getClassConfiguration(getClassName()).getFunctionKeys()) {
                idList.add(name);
            }
        }
        else {
            idList.add("length");
        }
        if (browserVersion.hasFeature(JS_NODE_LIST_ENUMERATE_CHILDREN)) {
            addElementIds(idList, elements);
        }

        return idList.toArray();
    }

    private boolean isPrototype() {
        return !(getPrototype() instanceof AbstractList);
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
            if (name != DomElement.ATTRIBUTE_NOT_DEFINED) {
                idList.add(name);
            }
            final String id = element.getId();
            if (id != DomElement.ATTRIBUTE_NOT_DEFINED) {
                idList.add(id);
            }
            if (!getBrowserVersion().hasFeature(JS_NODE_LIST_ENUMERATE_FUNCTIONS)) {
                idList.add(Integer.toString(index));
            }
            index++;
        }
    }

    private static final class DomHtmlAttributeChangeListenerImpl
                                    implements DomChangeListener, HtmlAttributeChangeListener {

        private transient WeakReference<AbstractList> nodeList_;

        private DomHtmlAttributeChangeListenerImpl(final AbstractList nodeList) {
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
            final AbstractList nodes = nodeList_.get();
            if (null == nodes) {
                return;
            }
            if (nodes.attributeChangeSensitive_) {
                handleChangeOnCache(event);
            }
        }

        private void handleChangeOnCache(final HtmlAttributeChangeEvent event) {
            final AbstractList nodes = nodeList_.get();
            if (null == nodes) {
                return;
            }

            final EffectOnCache effectOnCache = nodes.getEffectOnCache(event);
            if (EffectOnCache.NONE == effectOnCache) {
                return;
            }
            if (EffectOnCache.RESET == effectOnCache) {
                clearCache();
            }
        }

        private void clearCache() {
            final AbstractList nodes = nodeList_.get();
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
