/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.NOPTransformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

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

    private static final long serialVersionUID = 4049916048017011764L;
    private static final Log LOG = LogFactory.getLog(HTMLCollection.class);

    private String xpath_;
    private DomNode node_;
    private boolean avoidObjectDetection_ = false;

    /**
     * The transformer used to get the element to return from the HTML element.
     * It returns the HTML element itself except for frames where it returns the nested window.
     */
    private Transformer transformer_;

    /**
     * Cache collection elements when possible, so as to avoid expensive XPath expression evaluations.
     */
    private List<Object> cachedElements_;

    /**
     * IE provides a way of enumerating through some element collections; this counter supports that functionality.
     */
    private int currentIndex_ = 0;

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
    public HTMLCollection(final DomNode parentScope) {
        this(parentScope.getScriptObject());
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     */
    public HTMLCollection(final ScriptableObject parentScope) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param parentScope the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    HTMLCollection(final DomNode parentScope, final List<?> initialElements) {
        this(parentScope);
        init(parentScope, null);
        cachedElements_ = new ArrayList<Object>(initialElements);
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
     * Initializes the content of this collection. The elements will be "calculated" at each
     * access using the specified XPath expression, applied to the specified node.
     * @param node the node to serve as root for the XPath expression
     * @param xpath the XPath expression which determines the elements of the collection
     */
    public void init(final DomNode node, final String xpath) {
        init(node, xpath, NOPTransformer.INSTANCE);
    }

    /**
     * Initializes the content of this collection. The elements will be "calculated" at each
     * access using the specified XPath expression, applied to the specified node, and
     * transformed using the specified transformer.
     * @param node the node to serve as root for the XPath expression
     * @param xpath the XPath expression which determines the elements of the collection
     * @param transformer the transformer enabling the retrieval of the expected objects from
     *        the results of the XPath evaluation
     */
    public void init(final DomNode node, final String xpath, final Transformer transformer) {
        if (node != null) {
            node_ = node;
            xpath_ = xpath;
            transformer_ = transformer;
            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            node_.addDomChangeListener(listener);
            if (node_ instanceof HtmlElement) {
                ((HtmlElement) node_).addHtmlAttributeChangeListener(listener);
                cachedElements_ = null;
            }
        }
    }

    /**
     * Initializes the collection. The elements will be "calculated" as the children of the node.
     * @param node the node to grab children from
     */
    public void initFromChildren(final DomNode node) {
        if (node != null) {
            node_ = node;
            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            node_.addDomChangeListener(listener);
            if (node_ instanceof HtmlElement) {
                ((HtmlElement) node_).addHtmlAttributeChangeListener(listener);
                cachedElements_ = null;
            }
        }
        transformer_ = NOPTransformer.INSTANCE;
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
            return getScriptableForElement(transformer_.transform(elements.get(index)));
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
            if (xpath_ != null) {
                response = XPathUtils.getByXPath(node_, xpath_);
            }
            else {
                response = new ArrayList<Object>();
                Node node = node_.getFirstChild();
                while (node != null) {
                    response.add(node);
                    node = node.getNextSibling();
                }
            }
        }
        else {
            response = new ArrayList<Object>();
        }

        final boolean isXmlPage = node_ != null && node_.getOwnerDocument() instanceof XmlPage;

        final boolean isIE = getBrowserVersion().isIE();

        for (int i = 0; i < response.size(); i++) {
            final DomNode element = (DomNode) response.get(i);

            //IE: XmlPage ignores all empty text nodes
            if (isIE && isXmlPage && element instanceof DomText
                && ((DomText) element).getNodeValue().trim().length() == 0) { //and 'xml:space' is 'default'
                final Boolean xmlSpaceDefault = isXMLSpaceDefault(element.getParentNode());
                if (xmlSpaceDefault != Boolean.FALSE) {
                    response.remove(i--);
                    continue;
                }
            }
            for (DomNode parent = element.getParentNode(); parent != null;
                parent = parent.getParentNode()) {
                if (parent instanceof HtmlNoScript) {
                    response.remove(i--);
                    break;
                }
            }
        }

        return response;
    }

    /**
     * Recursively checks whether "xml:space" attribute is set to "default".
     * @param node node to start checking from
     * @return {@link Boolean#TRUE} if "default" is set, {@link Boolean#FALSE} for other value,
     *         or null if nothing is set.
     */
    private static Boolean isXMLSpaceDefault(DomNode node) {
        for ( ; node instanceof DomElement; node = node.getParentNode()) {
            final String value = ((DomElement) node).getAttribute("xml:space");
            if (value.length() != 0) {
                if (value.equals("default")) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        }
        return null;
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
        if (xpath_ == null || "length".equals(name)) {
            return NOT_FOUND;
        }

        final List<Object> elements = getElements();
        CollectionUtils.transform(elements, transformer_);

        // See if there is an element in the element array with the specified id.
        for (final Object next : elements) {
            if (next instanceof DomElement) {
                final String id = ((DomElement) next).getAttribute("id");
                if (id != null && id.equals(name)) {
                    if (getBrowserVersion().isIE() || "FF3".equals(getBrowserVersion().getNickname())) {
                        int totalIDs = 0;
                        for (final Object o : elements) {
                            if (o instanceof DomElement && name.equals(((DomElement) o).getAttribute("id"))) {
                                totalIDs++;
                            }
                        }
                        if (totalIDs > 1) {
                            final HTMLCollectionTags collection =
                                new HTMLCollectionTags((SimpleScriptable) getParentScope());
                            collection.setAvoidObjectDetection(!getBrowserVersion().isIE());
                            collection.init(node_, ".//*[@id='" + id + "']");
                            return collection;
                        }
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Property \"" + name + "\" evaluated (by id) to " + next);
                    }
                    return getScriptableForElement(next);
                }
            }
            else if (next instanceof WebWindow) {
                final WebWindow window = (WebWindow) next;
                final String windowName = window.getName();
                if (windowName != null && windowName.equals(name)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Property \"" + name + "\" evaluated (by name) to " + window);
                    }
                    return getScriptableForElement(window);
                }
                if (getBrowserVersion().isIE() && window instanceof FrameWindow
                        && ((FrameWindow) window).getFrameElement().getAttribute("id").equals(name)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Property \"" + name + "\" evaluated (by id) to " + window);
                    }
                    return getScriptableForElement(window);
                }
            }
            else {
                LOG.warn("Unrecognized type in collection: " + next + " (" + next.getClass().getName() + ")");
            }
        }

        // See if there are any elements in the element array with the specified name.
        final HTMLCollection array = new HTMLCollection(this);
        final String newCondition = "@name = '" + name.replaceAll("\\$", "\\\\\\$") + "'";
        final String xpathExpr;
        if (xpath_.endsWith("]")) {
            xpathExpr = xpath_.replaceAll("\\[([^\\]]*)\\]$", "[($1) and " + newCondition + "]");
        }
        else {
            xpathExpr = xpath_ + "[" + newCondition + "]";
        }
        array.init(node_, xpathExpr);

        final List<Object> subElements = array.getElements();
        if (subElements.size() > 1) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Property \"" + name + "\" evaluated (by name) to " + array + " with "
                    + subElements.size() + " elements");
            }
            return array;
        }
        else if (subElements.size() == 1) {
            final Scriptable singleResult = getScriptableForElement(subElements.get(0));
            if (LOG.isDebugEnabled()) {
                LOG.debug("Property \"" + name + "\" evaluated (by name) to " + singleResult);
            }
            return singleResult;
        }

        // Nothing was found.
        return NOT_FOUND;
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
            if (getBrowserVersion().isIE()) {
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
        final HTMLCollection array = new HTMLCollection(this);
        array.init(node_, xpath_ + "[name() = '" + tagName.toLowerCase() + "']");
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (xpath_ != null) {
            return super.toString() + '<' + xpath_ + '>';
        }
        return super.toString();
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
            if (node_ == otherArray.node_
                    && xpath_.toString().equals(otherArray.xpath_.toString())
                    && transformer_.equals(otherArray.transformer_)) {
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
        try {
            final int index = Integer.parseInt(name);
            final List<Object> elements = getElements();
            CollectionUtils.transform(elements, transformer_);
            if (index >= 0 && index < elements.size()) {
                return true;
            }
        }
        catch (final NumberFormatException e) {
            // Ignore.
        }

        if (name.equals("length")) {
            return true;
        }
        if (!getBrowserVersion().isIE()) {
            final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(BrowserVersion.FIREFOX_3);
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
        final List<String> idList = new ArrayList<String>();

        final List<Object> elements = getElements();
        CollectionUtils.transform(elements, transformer_);

        if (!getBrowserVersion().isIE()) {
            final int length = getElements().size();
            for (int i = 0; i < length; i++) {
                idList.add(Integer.toString(i));
            }

            idList.add("length");
            final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(BrowserVersion.FIREFOX_3);
            for (final String name : jsConfig.getClassConfiguration(getClassName()).functionKeys()) {
                idList.add(name);
            }
        }
        else {
            idList.add("length");

            int index = 0;
            for (final Object next : elements) {
                if (next instanceof HtmlElement) {
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
                else if (next instanceof WebWindow) {
                    final WebWindow window = (WebWindow) next;
                    final String windowName = window.getName();
                    if (windowName != null) {
                        idList.add(windowName);
                    }
                }
                else if (LOG.isDebugEnabled()) {
                    LOG.debug("Unrecognized type in array: \"" + next.getClass().getName() + "\"");
                }
            }
        }
        return idList.toArray();
    }

    private class DomHtmlAttributeChangeListenerImpl implements DomChangeListener, HtmlAttributeChangeListener {

        private static final long serialVersionUID = -6690451155079053212L;

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
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getLength() {
        return jsxGet_length();
    }

    /**
     * {@inheritDoc}
     */
    public Node item(final int index) {
        return (DomNode) transformer_.transform(getElements().get(index));
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
        else if (object instanceof WebWindow) {
            return Window.getProxy((WebWindow) object);
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
