/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_COMMENT_IS_ELEMENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_OBJECT_DETECTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_NODE_LIST_ENUMERATE_FUNCTIONS;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

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
 * @author Frank Danek
 */
@JsxClass
public class NodeList extends SimpleScriptable implements Function, org.w3c.dom.NodeList {
    /**
     * Cache effect of some changes.
     */
    protected enum EffectOnCache {
        /** No effect, cache is still valid. */
        NONE,
        /** Cache is not valid anymore and should be reset. */
        RESET
    }

    private boolean avoidObjectDetection_ = false;
    private String description_;

    private boolean attributeChangeSensitive_ = true;

    /**
     * Cache collection elements when possible, so as to avoid expensive XPath expression evaluations.
     */
    private List<Object> cachedElements_;

    private boolean listenerRegistered_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     * Don't call.
     */
    public NodeList() {
        // Empty.
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     */
    private NodeList(final ScriptableObject parentScope) {
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
    public NodeList(final DomNode parentScope, final boolean attributeChangeSensitive, final String description) {
        this(parentScope.getScriptObject());
        setDomNode(parentScope, false);
        description_ = description;
        attributeChangeSensitive_ = attributeChangeSensitive;
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param parentScope the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    protected NodeList(final DomNode parentScope, final List<?> initialElements) {
        this(parentScope.getScriptObject());
        cachedElements_ = new ArrayList<Object>(initialElements);
    }

    /**
     * Gets an empty collection.
     * @param window the current scope
     * @return an empty collection
     */
    public static NodeList emptyCollection(final Window window) {
        final List<Object> list = Collections.emptyList();
        return new NodeList(window) {
            @Override
            public List<Object> getElements() {
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
        final NodeList array = (NodeList) start;
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
            cachedElements = computeElements();
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
        final List<Object> response = new ArrayList<Object>();
        final DomNode domNode = getDomNodeOrNull();
        if (domNode == null) {
            return response;
        }
        for (final DomNode node : getCandidates()) {
            final boolean commentIncluded = getBrowserVersion().hasFeature(HTMLCOLLECTION_COMMENT_IS_ELEMENT)
                    && node instanceof DomComment;

            if ((node instanceof DomElement || commentIncluded) && isMatching(node)) {
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
                    matchingElements.add(next);
                }
            }
        }

        if (matchingElements.size() == 1) {
            return getScriptableForElement(matchingElements.get(0));
        }
        else if (!matchingElements.isEmpty()) {
            final NodeList collection = new NodeList(getDomNodeOrDie(), matchingElements);
            collection.setAvoidObjectDetection(!getBrowserVersion().hasFeature(HTMLCOLLECTION_OBJECT_DETECTION));
            return collection;
        }

        // no element found by id, let's search by name
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
        final NodeList collection = new NodeList(domNode, matchingElements);
        collection.setAvoidObjectDetection(
                !getBrowserVersion().hasFeature(HTMLCOLLECTION_OBJECT_DETECTION));
        return collection;
    }

    /**
     * {@inheritDoc}
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
        else if (other instanceof NodeList) {
            final NodeList otherArray = (NodeList) other;
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
        if (getBrowserVersion().hasFeature(JS_NODE_LIST_ENUMERATE_FUNCTIONS)) {
            final JavaScriptConfiguration jsConfig = getWindow().getWebWindow().getWebClient()
                    .getJavaScriptEngine().getJavaScriptConfiguration();
            return jsConfig.getClassConfiguration(getClassName()).functionKeys().contains(name);
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

        if (getBrowserVersion().hasFeature(JS_NODE_LIST_ENUMERATE_FUNCTIONS)) {
            final int length = elements.size();
            for (int i = 0; i < length; i++) {
                idList.add(Integer.toString(i));
            }

            idList.add("length");
            final JavaScriptConfiguration jsConfig = getWindow().getWebWindow().getWebClient()
                    .getJavaScriptEngine().getJavaScriptConfiguration();
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
        return !(getPrototype() instanceof NodeList);
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
            else {
                final String id = element.getId();
                if (id != DomElement.ATTRIBUTE_NOT_DEFINED) {
                    idList.add(id);
                }
                else {
                    idList.add(Integer.toString(index));
                }
            }
            index++;
        }
    }

    private static final class DomHtmlAttributeChangeListenerImpl
                                    implements DomChangeListener, HtmlAttributeChangeListener {

        private transient WeakReference<NodeList> nodeList_;

        private DomHtmlAttributeChangeListenerImpl(final NodeList nodeList) {
            super();

            nodeList_ = new WeakReference<NodeList>(nodeList);
        }

        /**
         * {@inheritDoc}
         */
        public void nodeAdded(final DomChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        public void nodeDeleted(final DomChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            handleChangeOnCache(event);
        }

        /**
         * {@inheritDoc}
         */
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            handleChangeOnCache(event);
        }

        /**
         * {@inheritDoc}
         */
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            final NodeList nodes = nodeList_.get();
            if (null == nodes) {
                return;
            }
            if (nodes.attributeChangeSensitive_) {
                handleChangeOnCache(event);
            }
        }

        private void handleChangeOnCache(final HtmlAttributeChangeEvent event) {
            final NodeList nodes = nodeList_.get();
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
            final NodeList nodes = nodeList_.get();
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
        return "NodeList";
    }
}
