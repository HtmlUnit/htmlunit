/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A javascript object for a Location
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Daniel Gredler
 */
public class SimpleScriptable extends ScriptableObject {
    private static final long serialVersionUID = 3120000176890886780L;

    private DomNode domNode_;


    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public SimpleScriptable() {
    }


    /**
     * Return an immutable map containing the html to javascript mappings.  Keys are
     * java classes for the various html classes (ie HtmlInput.class) and the values
     * are the javascript class names (ie "Anchor").
     * @return the mappings
     */
    public static synchronized Map getHtmlJavaScriptMapping() {
        return JavaScriptConfiguration.getHtmlJavaScriptMapping();
    }



    private JavaScriptConfiguration getJavaScriptConfiguration() {
        final BrowserVersion browserVersion
            = getWindow().getWebWindow().getWebClient().getBrowserVersion();
        return JavaScriptConfiguration.getInstance(browserVersion);
    }


    /**
     * Return the javascript class name
     * @return The javascript class name
     */
    public String getClassName() {
        final String javaClassName = getClass().getName();
        final int index = javaClassName.lastIndexOf(".");
        if( index == -1 ) {
            throw new IllegalStateException("No dot in classname: "+javaClassName);
        }

        return javaClassName.substring(index+1);
    }


    /**
     * Create a new javascript object
     * @param className The class name of the new object
     * @return The new object
     */
    public SimpleScriptable makeJavaScriptObject( final String className ) {
        final Context c = JavaScriptEngine.enterContext();
        try {
            final SimpleScriptable scriptable = (SimpleScriptable) c.newObject(this, className);
            return scriptable;
        }
        catch( final Exception e ) {
            throw new ScriptException(e);
        }
        finally {
            Context.exit();
        }
    }

    /**
     * Return the DOM node that corresponds to this javascript object or throw
     * an exception if one cannot be found.
     * @return The DOM node
     * @exception IllegalStateException If the DOM node could not be found.
     */
    public final DomNode getDomNodeOrDie() throws IllegalStateException {
        if( domNode_ == null ) {
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
    public void setDomNode( final DomNode domNode ) {
        setDomNode( domNode, true );
    }

    /**
     * Set the DOM node that corresponds to this javascript object
     * @param domNode The DOM node
     * @param assignScriptObject If true, call <code>setScriptObject</code> on domNode
     */
    protected void setDomNode( final DomNode domNode, final boolean assignScriptObject ) {
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
    public void setHtmlElement( final HtmlElement htmlElement ) {
        setDomNode(htmlElement);
    }


    /**
     * Return the specified property or NOT_FOUND if it could not be found.  This could also be 
     * a call to a function so check it out as a function also.
     * @param name The name of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    public Object get( final String name, final Scriptable start ) {
        // Some calls to get will happen during the initialization of the superclass.
        // At this point, we don't have enough information to do our own initialization
        // so we have to just pass this call through to the superclass.
        if( domNode_ == null ) {
            final Object result = super.get(name, start);
            // this may help to find which properties htmlunit should impement
            if (result == NOT_FOUND) {
                getLog().debug("Property \"" + name + "\" of " + start + " not defined as pure js property");
            }
            return result;
        }

        final JavaScriptConfiguration configuration = getJavaScriptConfiguration();
        final Class clazz = getClass();
        final Object result;

        final Method propertyMethod = configuration.getPropertyReadMethod(clazz, name);
        final Method functionMethod = configuration.getFunctionMethod(clazz, name);
        if (propertyMethod != null && functionMethod != null) {
            throw new IllegalStateException("Name is both a property and a function: name=["
                + name + "] class=[" + clazz.getName() + "]");
        }
        if (propertyMethod == null) {
            if (functionMethod == null) {
                result = super.get(name, start);
            }
            else {
                result = new FunctionObject(name, functionMethod, this);
            }
        }
        else {
            try {
                result = propertyMethod.invoke(this, new Object[0]);
            }
            catch (final Exception e) {
                throw new ScriptException(e);
            }
        }

        // this may help to find which properties htmlunit should impement
        if (result == NOT_FOUND) {
            getLog().debug("Property \"" + name + "\" of " + start + " not defined as fixed property");
        }

        return result;
    }


    /**
     * Set the specified property
     * @param name The name of the property
     * @param start The scriptable object that was originally invoked for this property
     * @param newValue The new value
     */
    public void put( final String name, final Scriptable start, Object newValue ) {
        // Some calls to put will happen during the initialization of the superclass.
        // At this point, we don't have enough information to do our own initialization
        // so we have to just pass this call through to the superclass.
        final SimpleScriptable simpleScriptable = (SimpleScriptable) start;
        if (simpleScriptable.domNode_ == null ) {
            super.put(name, start, newValue);
            return;
        }

        final JavaScriptConfiguration configuration = simpleScriptable.getJavaScriptConfiguration();
        final Method setterMethod = configuration.getPropertyWriteMethod(getClass(), name);

        if (setterMethod == null) {
            if (configuration.propertyExists(getClass(), name)) {
                throw Context.reportRuntimeError("Property \"" + name + "\" is not writable for " + start + ". "
                    + "Cant set it to: " + newValue);
            }
            else {
                getLog().debug("No configured setter \"" + name + "\" found for "
                    + start + ". Setting it as pure javascript property.");

                super.put(name, start, newValue);
            }
        }
        else {
            final Class parameterClass = setterMethod.getParameterTypes()[0];
            if( parameterClass == String.class) {
                newValue = Context.toString(newValue);
            }
            else if (Integer.TYPE.equals(parameterClass)) {
                newValue = new Integer((new Double(Context.toNumber(newValue))).intValue());
            }
            else if (Boolean.TYPE.equals(parameterClass)) {
                newValue = Boolean.valueOf(Context.toBoolean(newValue));
            }
            try {
                setterMethod.invoke(
                    simpleScriptable.findMatchingScriptable(start, setterMethod),
                    new Object[]{ newValue } );
            }
            catch( final InvocationTargetException e ) {
                throw new ScriptException(e.getTargetException());
            }
            catch( final Exception e ) {
                throw new ScriptException(e);
            }

        }
    }


    /**
     * Walk up the prototype chain and return the first scriptable that this method can
     * be invoked on.
     * @param start The object on which we are starting the search
     * @param method The method
     * @return The first scriptable
     * @throws IllegalStateException If a matching scriptable could not be found.
     */
    private Scriptable findMatchingScriptable( final Scriptable start, final Method method ) {
        final Class declaringClass = method.getDeclaringClass();
        Scriptable scriptable = start;
        while( declaringClass.isInstance(start) == false ) {
            scriptable = scriptable.getPrototype();
            if( scriptable == null ) {
                throw new IllegalStateException("Couldn't find a matching scriptable");
            }
        }

        return scriptable;
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
        if( scriptObject != null ) {
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

        final String javaScriptClassName = (String)getHtmlJavaScriptMapping().get(domNode.getClass());
        final SimpleScriptable scriptable;
        if (javaScriptClassName == null) {
            // We don't have a specific subclass for this element so create something generic.
            scriptable = makeJavaScriptObject("HTMLElement");
            getLog().debug("No javascript class found for element <"+domNode.getNodeName()+">. Using HTMLElement");
        }
        else {
            scriptable = makeJavaScriptObject(javaScriptClassName);
        }
        scriptable.setDomNode(domNode);
        // parent scope needs to be set to "window" (no simple unit test found to illustrate the necessity)
        // if navigation has continued, the window may contain an other page as the on to which the current node
        // belongs to
        // see test JavaScriptEngineTest#testScopeInInactivePage
        scriptable.setParentScope(ScriptableObject.getTopLevelScope((Scriptable) domNode.getPage().getScriptObject()));
        return scriptable;
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
    public static Object getObjectArg( final int index, final Object[] args, final Object defaultValue ) {
        if( index >= args.length ) {
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
    public static String getStringArg( final int index, final Object[] args, final String defaultValue ) {
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
    public static boolean getBooleanArg( final int index, final Object[] args, final boolean defaultValue ) {
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
    public static int getIntArg( final int index, final Object[] args, final int defaultValue ) {
        return (int) Context.toNumber(getObjectArg(index, args, new Integer(defaultValue)));
    }


    /**
     * Return the javascript default value of this object.  This is the javascript equivilent
     * of a toString() in java.
     *
     * @param hint A hint as to the format of the default value.  Ignored in this case.
     * @return The default value.
     */
    public Object getDefaultValue( final Class hint ) {
        if (String.class.equals(hint)) {
            return "[object " + getClassName() + "]";
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
        return getWindow( this );
    }

    /**
     * Gets the window that is the top scope for the specified object.
     * @param s The JavaScript object whose associated window is to be returned.
     * @return The window associated with the specified JavaScript object.
     * @throws RuntimeException If the window cannot be found, which should never occur.
     */
    protected static Window getWindow( final Scriptable s ) throws RuntimeException {
        final Scriptable top = ScriptableObject.getTopLevelScope( s );
        if( top instanceof Window ) {
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

    /**
     * {@inheritDoc}
     */
    public boolean has(final String name, final Scriptable start) {
        final SimpleScriptable simpleScriptable = (SimpleScriptable) start;

        final JavaScriptConfiguration configuration = simpleScriptable.getJavaScriptConfiguration();
        // getters and setters are defined on prototypes
        if ((start == start.getPrototype() || simpleScriptable.getDomNodeOrNull() != null)
                && configuration.getPropertyReadMethod(getClass(), name) != null) {
            return true;
        }
        else {
            return super.has(name, start);
        }
    }
}
