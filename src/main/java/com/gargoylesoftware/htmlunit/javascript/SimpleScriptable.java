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
package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Method;

import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for a Location.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class SimpleScriptable extends ScriptableObject {
    private static final long serialVersionUID = 3120000176890886780L;

    private DomNode domNode_;

    /**
     * Gets a named property from the object.
     * Normally HtmlUnit objects don't need to overwrite this method as properties are defined
     * on the prototypes from the XML configuration. In some cases where "content" of object
     * has priority compared to the properties consider using utility {@link #getWithPreemption(String)}.
     * For fallback case just implement {@link ScriptableWithFallbackGetter}.
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        // try to get property configured on object itself
        final Object response = super.get(name, start);
        if (response != NOT_FOUND) {
            return response;
        }

        if (this == start) {
            return getWithPreemption(name);
        }
        return NOT_FOUND;
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

    /**
     * Returns the JavaScript class name.
     * @return the JavaScript class name
     */
    @Override
    public String getClassName() {
        final String javaClassName = getClass().getName();
        final int index = javaClassName.lastIndexOf(".");
        if (index == -1) {
            throw new IllegalStateException("No dot in classname: " + javaClassName);
        }

        return javaClassName.substring(index + 1);
    }

    /**
     * Returns the DOM node that corresponds to this JavaScript object or throw
     * an exception if one cannot be found.
     * @return the DOM node
     * @exception IllegalStateException If the DOM node could not be found.
     */
    public final DomNode getDomNodeOrDie() throws IllegalStateException {
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
    public final DomNode getDomNodeOrNull() {
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
            domNode_.setScriptObject(this);
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
     * Returns the log that is being used for all scripting objects.
     * @return the log
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
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
            return (SimpleScriptable) ((WebWindow) object).getScriptObject();
        }

        final DomNode domNode = (DomNode) object;

        final Object scriptObject = domNode.getScriptObject();
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
    public SimpleScriptable makeScriptableFor(final DomNode domNode) {
        // Get the JS class name for the specified DOM node.
        // Walk up the inheritance chain if necessary.
        Class< ? extends SimpleScriptable> javaScriptClass = null;
        for (Class< ? > c = domNode.getClass(); javaScriptClass == null && c != null; c = c.getSuperclass()) {
            javaScriptClass = JavaScriptConfiguration.getHtmlJavaScriptMapping().get(c);
        }

        final SimpleScriptable scriptable;
        if (javaScriptClass == null) {
            // We don't have a specific subclass for this element so create something generic.
            scriptable = new HTMLElement();
            getLog().debug("No JavaScript class found for element <" + domNode.getNodeName() + ">. Using HTMLElement");
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
        // parent scope needs to be set to the enclosing "window" (no simple unit test found to illustrate the
        // necessity) if navigation has continued, the window may contain an other page as the one to which
        // the current node belongs to.
        // See test JavaScriptEngineTest#testScopeInInactivePage
        if (domNode.getPage().getEnclosingWindow().getEnclosedPage() == domNode.getPage()) {
            scriptable.setParentScope(getWindow());
        }
        else {
            scriptable.setParentScope(ScriptableObject.getTopLevelScope(
                    ((SgmlPage) domNode.getPage()).getScriptObject()));
        }
    }

    /**
     * Gets the prototype object for the given host class.
     * @param javaScriptClass the host class
     * @return the prototype
     */
    @SuppressWarnings("unchecked")
    protected Scriptable getPrototype(final Class< ? extends SimpleScriptable> javaScriptClass) {
        final Scriptable prototype = getWindow().getPrototype(javaScriptClass);
        if (prototype == null && javaScriptClass != SimpleScriptable.class) {
            return getPrototype((Class< ? extends SimpleScriptable>) javaScriptClass.getSuperclass());
        }
        return prototype;
    }

    /**
     * Gets a transformer getting the scriptable element for an {@link HtmlElement}.
     * @return the transformer
     */
    protected Transformer getTransformerScriptableFor() {
        return new Transformer() {
            public Object transform(final Object obj) {
                return getScriptableFor(obj);
            }
        };
    }

    /**
     * Returns the value at the specified location in the argument list. If the index is larger
     * than the argument array then return the default value.
     *
     * @param index the index into the argument list
     * @param args the argument list
     * @param defaultValue the default value to return if the arg wasn't specified
     * @return the specified object or null
     */
    public static Object getObjectArg(final int index, final Object[] args, final Object defaultValue) {
        if (index >= args.length) {
            return defaultValue;
        }
        return args[index];
    }

    /**
     * Returns the string value at the specified location in the argument list. If the index is larger
     * than the argument array then return the default value.
     *
     * @param index the index into the argument list
     * @param args the argument list
     * @param defaultValue the default value to return if the arg wasn't specified
     * @return the specified string or null
     */
    public static String getStringArg(final int index, final Object[] args, final String defaultValue) {
        return Context.toString(getObjectArg(index, args, defaultValue));
    }

    /**
     * Returns the boolean value at the specified location in the argument list. If the index is larger
     * than the argument array then return the default value.
     *
     * @param index the index into the argument list
     * @param args the argument list
     * @param defaultValue the default value to be used
     * @return the specified boolean or the default value
     */
    public static boolean getBooleanArg(final int index, final Object[] args, final boolean defaultValue) {
        final Boolean defaultBoolean = Boolean.valueOf(defaultValue);

        return Context.toBoolean(getObjectArg(index, args, defaultBoolean));
    }

    /**
     * Returns the int value at the specified location in the argument list. If the index is larger
     * than the argument array then return the default value.
     *
     * @param index the index into the argument list
     * @param args the argument list
     * @param defaultValue the default value to be used
     * @return the specified int or the default value
     */
    public static int getIntArg(final int index, final Object[] args, final int defaultValue) {
        return (int) Context.toNumber(getObjectArg(index, args, new Integer(defaultValue)));
    }

    /**
     * Returns the JavaScript default value of this object. This is the JavaScript equivalent
     * of a toString() in Java.
     *
     * @param hint a hint as to the format of the default value (ignored in this case)
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class< ? > hint) {
        if (String.class.equals(hint) || hint == null) {
            if (getBrowserVersion().isIE()) {
                return "[object]"; // the super helpful IE solution
            }
            final Window window = (Window) getTopLevelScope(this);
            if (ScriptableObject.getProperty(window, getClassName()) == this) {
                return "[" + getClassName() + "]";
            }
            return "[object " + getClassName() + "]"; // not fully correct as htmlunit names are not FF ones
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
        return (Scriptable) Context.getCurrentContext().getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
    }

    /**
     * {@inheritDoc}
     * Same as base implementation, but includes all methods inherited from super classes as well.
     */
    @Override
    public void defineProperty(final String propertyName, final Class< ? > clazz, int attributes) {
        final int length = propertyName.length();
        if (length == 0) {
            throw new IllegalArgumentException();
        }
        final char[] buf = new char[3 + length];
        propertyName.getChars(0, length, buf, 3);
        buf[3] = Character.toUpperCase(buf[3]);
        buf[0] = 'g';
        buf[1] = 'e';
        buf[2] = 't';
        final String getterName = new String(buf);
        buf[0] = 's';
        final String setterName = new String(buf);

        final Method[] methods = clazz.getMethods();
        final Method getter = findMethod(methods, getterName);
        final Method setter = findMethod(methods, setterName);
        if (setter == null) {
            attributes |= ScriptableObject.READONLY;
        }
        defineProperty(propertyName, null, getter, setter, attributes);
    }

    /**
     * {@inheritDoc}
     * Same as base implementation, but includes all methods inherited from super classes as well.
     */
    @Override
    public void defineFunctionProperties(final String[] names, final Class< ? > clazz, final int attributes) {
        final Method[] methods = clazz.getMethods();
        for (final String name : names) {
            final Method method = findMethod(methods, name);
            if (method == null) {
                throw Context.reportRuntimeError("Method \"" + name + "\" not found in \"" + clazz.getName() + '"');
            }
            final FunctionObject f = new FunctionObject(name, method, this);
            defineProperty(name, f, attributes);
        }
    }

    /**
     * Returns the method with the specified name.
     */
    private static Method findMethod(final Method[] methods, final String name) {
        for (Method m : methods) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Gets the browser version currently used.
     * @return the browser version
     */
    protected BrowserVersion getBrowserVersion() {
        return getWindow().getWebWindow().getWebClient().getBrowserVersion();
    }
}
