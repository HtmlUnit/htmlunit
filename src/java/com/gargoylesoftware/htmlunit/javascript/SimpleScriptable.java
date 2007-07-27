/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript;

import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A javascript object for a Location
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Daniel Gredler
 */
public class SimpleScriptable extends ScriptableObject {
    private static final long serialVersionUID = 3120000176890886780L;

    private DomNode domNode_;

    /**
     * Get a named property from the object.
     * Normally HtmlUnit objects don't need to overwrite this method as properties are defined
     * on the prototypes from the xml configuration. In some cases where "content" of object
     * has priority compared to the properties consider using utility {@link #getWithPreemption(String)}.
     * For fallback case just implement {@link ScriptableWithFallbackGetter}.
     * {@inheritDoc}
     */
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
     * Called by {@link #get(String, Scriptable)} to allow to retrieve property before the prototype
     * chain is searched.
     * @param name the property name
     * @return {@link Scriptable#NOT_FOUND} if not found
     */
    protected Object getWithPreemption(final String name) {
        return NOT_FOUND;
    }

    /**
     * Return the javascript class name
     * @return The javascript class name
     */
    public String getClassName() {
        final String javaClassName = getClass().getName();
        final int index = javaClassName.lastIndexOf(".");
        if (index == -1) {
            throw new IllegalStateException("No dot in classname: " + javaClassName);
        }

        return javaClassName.substring(index + 1);
    }

    /**
     * Return the DOM node that corresponds to this javascript object or throw
     * an exception if one cannot be found.
     * @return The DOM node
     * @exception IllegalStateException If the DOM node could not be found.
     */
    public final DomNode getDomNodeOrDie() throws IllegalStateException {
        if (domNode_ == null) {
            final String clazz = getClass().getName();
            throw new IllegalStateException("DomNode has not been set for this SimpleScriptable: " + clazz);
        }
        else {
            return domNode_;
        }
    }

    /**
     * Return the html element that corresponds to this javascript object or throw an exception
     * if one cannot be found.
     * @return The html element
     * @exception IllegalStateException If the html element could not be found.
     */
    public final HtmlElement getHtmlElementOrDie() throws IllegalStateException {
        return (HtmlElement) getDomNodeOrDie();
    }

    /**
     * Return the DOM node that corresponds to this javascript object
     * or null if a node hasn't been set.
     * @return The DOM node or null
     */
    public final DomNode getDomNodeOrNull() {
        return domNode_;
    }

    /**
     * Return the html element that corresponds to this javascript object
     * or null if an element hasn't been set.
     * @return The html element or null
     */
    public final HtmlElement getHtmlElementOrNull() {
        return (HtmlElement) getDomNodeOrNull();
    }

    /**
     * Set the DOM node that corresponds to this javascript object
     * @param domNode The DOM node
     */
    public void setDomNode(final DomNode domNode) {
        setDomNode(domNode, true);
    }

    /**
     * Set the DOM node that corresponds to this javascript object
     * @param domNode The DOM node
     * @param assignScriptObject If true, call <code>setScriptObject</code> on domNode
     */
    protected void setDomNode(final DomNode domNode, final boolean assignScriptObject) {
        Assert.notNull("domNode", domNode);
        domNode_ = domNode;
        if (assignScriptObject) {
            domNode_.setScriptObject(this);
        }
    }

    /**
     * Set the html element that corresponds to this javascript object
     * @param htmlElement The html element
     */
    public void setHtmlElement(final HtmlElement htmlElement) {
        setDomNode(htmlElement);
    }

    /**
     * Return the log that is being used for all scripting objects
     * @return The log.
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Return the javascript object that corresponds to the specified object.
     * New javascript objects will be created as needed.  If a javascript object
     * cannot be created for a domNode then NOT_FOUND will be returned.
     *
     * @param object a {@link DomNode} or a {@link WebWindow}
     * @return The javascript object or NOT_FOUND
     */
    protected SimpleScriptable getScriptableFor(final Object object) {
        if (object instanceof WebWindow) {
            return (SimpleScriptable) ((WebWindow) object).getScriptObject();
        }

        final DomNode domNode = (DomNode) object;

        final Object scriptObject = domNode.getScriptObject();
        if (scriptObject != null) {
            return (SimpleScriptable)scriptObject;
        }
        else {
            return makeScriptableFor(domNode);
        }
    }

    /**
     * Builds a new the javascript object that corresponds to the specified object.<br>
     * @param domNode the dom node for which a JS object should be created
     * @return The javascript object
     */
    public SimpleScriptable makeScriptableFor(final DomNode domNode) {

        // Get the JS class name for the specified DOM node.
        // Walk up the inheritance chain if necessary.
        Class javaScriptClass = null;
        for (Class c = domNode.getClass(); javaScriptClass == null && c != null; c = c.getSuperclass()) {
            javaScriptClass = (Class) JavaScriptConfiguration.getHtmlJavaScriptMapping().get(c);
        }

        final SimpleScriptable scriptable;
        if (javaScriptClass == null) {
            // We don't have a specific subclass for this element so create something generic.
            scriptable = new HTMLElement();
            getLog().debug("No javascript class found for element <" + domNode.getNodeName() + ">. Using HTMLElement");
        }
        else {
            try {
                scriptable = (SimpleScriptable) javaScriptClass.newInstance();
            }
            catch (final Exception e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }
        // parent scope needs to be set to the enclosing "window" (no simple unit test found to illustrate the
        // necessity) if navigation has continued, the window may contain an other page as the one to which
        // the current node belongs to.
        // See test JavaScriptEngineTest#testScopeInInactivePage
        if (domNode.getPage().getEnclosingWindow().getEnclosedPage() == domNode.getPage()) {
            scriptable.setParentScope(getWindow());
        }
        else {
            scriptable.setParentScope(ScriptableObject.getTopLevelScope(
                    (Scriptable) domNode.getPage().getScriptObject()));
        }
        
        scriptable.setPrototype(getPrototype(javaScriptClass));
        scriptable.setDomNode(domNode);

        return scriptable;
    }

    /**
     * Get the prototype object for the given host class
     * @param javaScriptClass the host class
     * @return the prototype
     */
    protected Scriptable getPrototype(final Class javaScriptClass) {
        return getWindow().getPrototype(javaScriptClass);
    }

    /**
     * Gets a transformer getting the scriptable element for an HtmlElement
     * @return the transformer.
     */
    protected Transformer getTransformerScriptableFor() {
        return new Transformer() {
            public Object transform(final Object obj) {
                return getScriptableFor(obj);
            }
        };
    }

    /**
     * Return the value at the specified location in the argument list.  If the index is larger
     * than the argument array then return the default value.
     *
     * @param index The index into the argument list.
     * @param args The argument list.
     * @param defaultValue The default value to return if the arg wasn't specified.
     * @return The specified object or null
     */
    public static Object getObjectArg(final int index, final Object[] args, final Object defaultValue) {
        if (index >= args.length) {
            return defaultValue;
        }
        else {
            return args[index];
        }
    }

    /**
     * Return the string value at the specified location in the argument list.  If the index is larger
     * than the argument array then return the default value.
     *
     * @param index The index into the argument list.
     * @param args The argument list.
     * @param defaultValue The default value to return if the arg wasn't specified.
     * @return The specified string or null
     */
    public static String getStringArg(final int index, final Object[] args, final String defaultValue) {
        return Context.toString(getObjectArg(index, args, defaultValue));
    }

    /**
     * Return the boolean value at the specified location in the argument list.  If the index is larger
     * than the argument array then return the default value.
     *
     * @param index The index into the argument list.
     * @param args The argument list.
     * @param defaultValue The default value to be used.
     * @return The specified boolean or the default value.
     */
    public static boolean getBooleanArg(final int index, final Object[] args, final boolean defaultValue) {
        final Boolean defaultBoolean = Boolean.valueOf(defaultValue);

        return Context.toBoolean(getObjectArg(index, args, defaultBoolean));
    }

    /**
     * Return the int value at the specified location in the argument list.  If the index is larger
     * than the argument array then return the default value.
     *
     * @param index The index into the argument list.
     * @param args The argument list.
     * @param defaultValue The default value to be used.
     * @return The specified int or the default value.
     */
    public static int getIntArg(final int index, final Object[] args, final int defaultValue) {
        return (int) Context.toNumber(getObjectArg(index, args, new Integer(defaultValue)));
    }

    /**
     * Return the javascript default value of this object.  This is the javascript equivalent
     * of a toString() in java.
     *
     * @param hint A hint as to the format of the default value.  Ignored in this case.
     * @return The default value.
     */
    public Object getDefaultValue(final Class hint) {
        if (String.class.equals(hint) || hint == null) {
            if (getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE()) {
                return "[object]"; // the super helpful IE solution
            }
            else {
                return "[object " + getClassName() + "]"; // not fully correct as htmlunit names are not FF ones
            }
        }
        else {
            return super.getDefaultValue(hint);
        }
    }

    /**
     * Gets the window that is the top scope for this object.
     * @return The window associated with this object.
     * @throws RuntimeException If the window cannot be found, which should never occur.
     */
    protected Window getWindow() throws RuntimeException {
        return getWindow(this);
    }

    /**
     * Gets the window that is the top scope for the specified object.
     * @param s The JavaScript object whose associated window is to be returned.
     * @return The window associated with the specified JavaScript object.
     * @throws RuntimeException If the window cannot be found, which should never occur.
     */
    protected static Window getWindow(final Scriptable s) throws RuntimeException {
        final Scriptable top = ScriptableObject.getTopLevelScope(s);
        if (top instanceof Window) {
            return (Window) top;
        }
        else {
            throw new RuntimeException("Unable to find window associated with " + s);
        }
    }

    /**
     * Gets the scriptable used at starting scope for the execution of current script.
     * @return the scope as defined in {@link JavaScriptEngine#callFunction}
     * or {@link JavaScriptEngine#execute}.
     */
    protected Scriptable getStartingScope() {
        return (Scriptable) Context.getCurrentContext().getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
    }
}
