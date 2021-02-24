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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLIMAGE_HTMLELEMENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLIMAGE_HTMLUNKNOWNELEMENT;

import java.util.Deque;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * Base class for Rhino host objects in HtmlUnit.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class SimpleScriptable extends HtmlUnitScriptable implements Cloneable {

    private static final Log LOG = LogFactory.getLog(SimpleScriptable.class);

    private DomNode domNode_;
    private boolean caseSensitive_ = true;

    /**
     * Gets a named property from the object.
     * Normally HtmlUnit objects don't need to overwrite this method as properties are defined
     * on the prototypes from the XML configuration. In some cases where "content" of object
     * has priority compared to the properties consider using utility {@link #getWithPreemption(String)}.
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, final Scriptable start) {
        // If this object is not case-sensitive about property names, transform the property name accordingly.
        if (!caseSensitive_) {
            for (final Object o : getAllIds()) {
                final String objectName = Context.toString(o);
                if (name.equalsIgnoreCase(objectName)) {
                    name = objectName;
                    break;
                }
            }
        }
        // Try to get property configured on object itself.
        Object response = super.get(name, start);
        if (response != NOT_FOUND) {
            return response;
        }
        if (this == start) {
            response = getWithPreemption(name);
        }
        if (response == NOT_FOUND && start instanceof Window) {
            response = ((Window) start).getWithFallback(name);
        }
        return response;
    }

    /**
     * <p>Called by {@link #get(String, Scriptable)} to allow retrieval of the property before the prototype
     * chain is searched.</p>
     *
     * <p>IMPORTANT: This method is invoked *very* often by Rhino. If you override this method, the implementation
     * needs to be as fast as possible!</p>
     *
     * @param name the property name
     * @return {@link Scriptable#NOT_FOUND} if not found
     */
    protected Object getWithPreemption(final String name) {
        return NOT_FOUND;
    }

    @Override
    public boolean has(final int index, final Scriptable start) {
        final Object found = get(index, start);
        if (!Undefined.isUndefined(found) && Scriptable.NOT_FOUND != found) {
            return true;
        }
        return super.has(index, start);
    }

    /**
     * Returns the DOM node that corresponds to this JavaScript object or throw
     * an exception if one cannot be found.
     * @return the DOM node
     */
    public DomNode getDomNodeOrDie() {
        if (domNode_ == null) {
            final String clazz = getClass().getName();
            throw new IllegalStateException("DomNode has not been set for this SimpleScriptable: " + clazz);
        }
        return domNode_;
    }

    /**
     * Returns the DOM node that corresponds to this JavaScript object
     * or null if a node hasn't been set.
     * @return the DOM node or null
     */
    public DomNode getDomNodeOrNull() {
        return domNode_;
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    public void setDomNode(final DomNode domNode) {
        setDomNode(domNode, true);
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     * @param assignScriptObject If true, call <code>setScriptObject</code> on domNode
     */
    protected void setDomNode(final DomNode domNode, final boolean assignScriptObject) {
        WebAssert.notNull("domNode", domNode);
        domNode_ = domNode;
        if (assignScriptObject) {
            domNode_.setScriptableObject(this);
        }
    }

    /**
     * Sets the HTML element that corresponds to this JavaScript object.
     * @param htmlElement the HTML element
     */
    public void setHtmlElement(final HtmlElement htmlElement) {
        setDomNode(htmlElement);
    }

    /**
     * Returns the JavaScript object that corresponds to the specified object.
     * New JavaScript objects will be created as needed. If a JavaScript object
     * cannot be created for a domNode then NOT_FOUND will be returned.
     *
     * @param object a {@link DomNode} or a {@link WebWindow}
     * @return the JavaScript object or NOT_FOUND
     */
    protected SimpleScriptable getScriptableFor(final Object object) {
        if (object instanceof WebWindow) {
            return (SimpleScriptable) ((WebWindow) object).getScriptableObject();
        }

        final DomNode domNode = (DomNode) object;

        final Object scriptObject = domNode.getScriptableObject();
        if (scriptObject != null) {
            return (SimpleScriptable) scriptObject;
        }
        return makeScriptableFor(domNode);
    }

    /**
     * Builds a new the JavaScript object that corresponds to the specified object.
     * @param domNode the DOM node for which a JS object should be created
     * @return the JavaScript object
     */
    @SuppressWarnings("unchecked")
    public SimpleScriptable makeScriptableFor(final DomNode domNode) {
        // Get the JS class name for the specified DOM node.
        // Walk up the inheritance chain if necessary.
        Class<? extends SimpleScriptable> javaScriptClass = null;
        if (domNode instanceof HtmlImage && "image".equals(((HtmlImage) domNode).getOriginalQualifiedName())
                && ((HtmlImage) domNode).wasCreatedByJavascript()) {
            if (domNode.hasFeature(HTMLIMAGE_HTMLELEMENT)) {
                javaScriptClass = HTMLElement.class;
            }
            else if (domNode.hasFeature(HTMLIMAGE_HTMLUNKNOWNELEMENT)) {
                javaScriptClass = HTMLUnknownElement.class;
            }
        }
        if (javaScriptClass == null) {
            final JavaScriptEngine javaScriptEngine =
                    (JavaScriptEngine) getWindow().getWebWindow().getWebClient().getJavaScriptEngine();
            for (Class<?> c = domNode.getClass(); javaScriptClass == null && c != null; c = c.getSuperclass()) {
                javaScriptClass = (Class<? extends SimpleScriptable>) javaScriptEngine.getJavaScriptClass(c);
            }
        }

        final SimpleScriptable scriptable;
        if (javaScriptClass == null) {
            // We don't have a specific subclass for this element so create something generic.
            scriptable = new HTMLElement();
            if (LOG.isDebugEnabled()) {
                LOG.debug("No JavaScript class found for element <" + domNode.getNodeName() + ">. Using HTMLElement");
            }
        }
        else {
            try {
                scriptable = javaScriptClass.newInstance();
            }
            catch (final Exception e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }
        initParentScope(domNode, scriptable);

        scriptable.setPrototype(getPrototype(javaScriptClass));
        scriptable.setDomNode(domNode);

        return scriptable;
    }

    /**
     * Initialize the parent scope of a newly created scriptable.
     * @param domNode the DOM node for the script object
     * @param scriptable the script object to initialize
     */
    protected void initParentScope(final DomNode domNode, final SimpleScriptable scriptable) {
        final SgmlPage page = domNode.getPage();
        final WebWindow enclosingWindow = page.getEnclosingWindow();
        if (enclosingWindow != null && enclosingWindow.getEnclosedPage() == page) {
            scriptable.setParentScope(enclosingWindow.getScriptableObject());
        }
        else {
            scriptable.setParentScope(ScriptableObject.getTopLevelScope(page.getScriptableObject()));
        }
    }

    /**
     * Gets the prototype object for the given host class.
     * @param javaScriptClass the host class
     * @return the prototype
     */
    @SuppressWarnings("unchecked")
    public Scriptable getPrototype(final Class<? extends SimpleScriptable> javaScriptClass) {
        final Scriptable prototype = getWindow().getPrototype(javaScriptClass);
        if (prototype == null && javaScriptClass != SimpleScriptable.class) {
            return getPrototype((Class<? extends SimpleScriptable>) javaScriptClass.getSuperclass());
        }
        return prototype;
    }

    /**
     * Returns the JavaScript default value of this object. This is the JavaScript equivalent of a toString() in Java.
     *
     * @param hint a hint as to the format of the default value (ignored in this case)
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (String.class.equals(hint) || hint == null) {
            return "[object " + getClassName() + "]";
        }
        return super.getDefaultValue(hint);
    }

    /**
     * Gets the window that is the top scope for this object.
     * @return the window associated with this object
     * @throws RuntimeException if the window cannot be found, which should never occur
     */
    public Window getWindow() throws RuntimeException {
        return getWindow(this);
    }

    /**
     * Gets the window that is the top scope for the specified object.
     * @param s the JavaScript object whose associated window is to be returned
     * @return the window associated with the specified JavaScript object
     * @throws RuntimeException if the window cannot be found, which should never occur
     */
    protected static Window getWindow(final Scriptable s) throws RuntimeException {
        final Scriptable top = ScriptableObject.getTopLevelScope(s);
        if (top instanceof Window) {
            return (Window) top;
        }
        throw new RuntimeException("Unable to find window associated with " + s);
    }

    /**
     * Gets the scriptable used at starting scope for the execution of current script.
     * @return the scope as defined in {@link JavaScriptEngine#callFunction}
     * or {@link JavaScriptEngine#execute}.
     */
    protected Scriptable getStartingScope() {
        @SuppressWarnings("unchecked")
        final Deque<Scriptable> stack =
                (Deque<Scriptable>) Context.getCurrentContext().getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
        if (null == stack) {
            return null;
        }
        return stack.peek();
    }

    /**
     * Gets the browser version currently used.
     * @return the browser version
     */
    public BrowserVersion getBrowserVersion() {
        final DomNode node = getDomNodeOrNull();
        if (node != null) {
            return node.getPage().getWebClient().getBrowserVersion();
        }
        return getWindow().getWebWindow().getWebClient().getBrowserVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasInstance(final Scriptable instance) {
        if (getPrototype() == null) {
            // to handle cases like "x instanceof HTMLElement",
            // but HTMLElement is not in the prototype chain of any element
            final Object prototype = get("prototype", this);
            if (!(prototype instanceof ScriptableObject)) {
                Context.throwAsScriptRuntimeEx(new Exception("Null prototype"));
            }
            return ((ScriptableObject) prototype).hasInstance(instance);
        }

        return super.hasInstance(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object equivalentValues(Object value) {
        if (value instanceof SimpleScriptableProxy<?>) {
            value = ((SimpleScriptableProxy<?>) value).getDelegee();
        }
        return super.equivalentValues(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleScriptable clone() {
        try {
            return (SimpleScriptable) super.clone();
        }
        catch (final Exception e) {
            throw new IllegalStateException("Clone not supported");
        }
    }

    /**
     * Sets case sensitivity of all properties of this scriptable.
     * @param caseSensitive case sensitive or no
     */
    public void setCaseSensitive(final boolean caseSensitive) {
        caseSensitive_ = caseSensitive;
        final Scriptable prototype = getPrototype();
        if (prototype instanceof SimpleScriptable) {
            ((SimpleScriptable) prototype).setCaseSensitive(caseSensitive);
        }
    }
}
