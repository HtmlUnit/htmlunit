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

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * A javascript object for a Location
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author Marc Guillemot
 */
public class SimpleScriptable extends ScriptableObject {
    private static final long serialVersionUID = 3120000176890886780L;
    
    private static final Map PROPERTY_MAPS = Collections.synchronizedMap( new HashMap(89) );
    private static Map HtmlJavaScriptMap_ = null;

    private JavaScriptEngine.PageInfo pageInfo_;
    private DomNode domNode_;

    private class PropertyInfo {
        private Method getter_;
        private Method setter_;
        private FunctionObject function_;
        /** @return The getter method */
        public Method getGetter() { return getter_; }
        /** @return The setter method */
        public Method getSetter() { return setter_; }
        /** @return The function object */
        public FunctionObject getFunction() { return function_; }
        /** @param getter The new getter method */
        public void setGetter( final Method getter ) { getter_ = getter; }
        /** @param setter The new setter method */
        public void setSetter( final Method setter ) { setter_ = setter; }
        /** @param function The new function object */
        public void setFunction( final FunctionObject function ) { function_ = function; }
    }

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
        if( HtmlJavaScriptMap_ != null ) {
            return HtmlJavaScriptMap_;
        }

        final String[][] mapping = {
            {"DomText", "TextImpl"},
            {"HtmlAnchor", "Anchor"},
            {"HtmlArea", "FocusableHostElement"},
            {"HtmlButton", "Button"},
            {"HtmlInput", "Input"},
            {"HtmlCheckBoxInput", "Input"},
            {"HtmlFileInput", "Input"},
            {"HtmlForm", "Form"},
            {"HtmlHiddenInput", "Input"},
            {"HtmlImage", "Image"},
            {"HtmlFrame", "Frame"},
            {"HtmlInlineFrame", "Frame"},
            {"HtmlLabel", "FocusableHostElement"},
            {"HtmlOption", "Option"},
            {"HtmlPasswordInput", "Input"},
            {"HtmlRadioButtonInput", "Input"},
            {"HtmlResetInput", "Input"},
            {"HtmlSelect", "Select"},
            {"HtmlSubmitInput", "Input"},
            {"HtmlTable", "Table"},
            {"HtmlTextInput", "Input"},
            {"HtmlTextArea", "Textarea"},
            {"HtmlElement", "HTMLElement"},
        };

        final Map map = new HashMap();
        for( int i=0; i<mapping.length; i++ ) {
            final String htmlClassName = mapping[i][0];
            final String javaScriptClassName = mapping[i][1];

            try {
                final Class htmlClass = Class.forName("com.gargoylesoftware.htmlunit.html."+htmlClassName);
                Class.forName("com.gargoylesoftware.htmlunit.javascript.host."+javaScriptClassName);
                map.put( htmlClass, javaScriptClassName );
            }
            catch( final ClassNotFoundException e ) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }

        HtmlJavaScriptMap_ = Collections.unmodifiableMap(map);
        return map;
    }


    private Map getPropertyMap() {
        synchronized( PROPERTY_MAPS ) {
            final Class clazz = getClass();
            final String className = clazz.getName();
            Map localPropertyMap = (Map)PROPERTY_MAPS.get(className);
            if( localPropertyMap == null && domNode_ != null ) {
                try {
                    localPropertyMap = createLocalPropertyMap();
                }
                catch( final Exception e ) {
                    throw new ScriptException(e);
                }
                PROPERTY_MAPS.put( className, localPropertyMap );
            }
            return localPropertyMap;
        }
    }


    private JavaScriptConfiguration getJavaScriptConfiguration() {
        final BrowserVersion browserVersion
            = getDomNodeOrDie().getPage().getWebClient().getBrowserVersion();
        return JavaScriptConfiguration.getInstance(browserVersion);
    }


    private Map createLocalPropertyMap() throws Exception {
        final Class clazz = getClass();
        final JavaScriptConfiguration configuration = getJavaScriptConfiguration();

        final Map localPropertyMap = new HashMap(89);

        final Method[] methods = getClass().getMethods();
        for( int i=0; i<methods.length; i++ ) {
            final String methodName = methods[i].getName();
            if( methodName.startsWith("jsGet_")  && methods[i].getParameterTypes().length == 0 ) {
                final String propertyName = methodName.substring(6);
                final int state = configuration.getReadablePropertyNameState(clazz, propertyName);
                if( state == JavaScriptConfiguration.ENABLED ) {
                    getPropertyInfo(localPropertyMap, propertyName).setGetter( methods[i] );
                }
                else if( state == JavaScriptConfiguration.NOT_FOUND ) {
                    throw new IllegalStateException("Getter for ["
                            + propertyName + "] not found in configuration");
                }
            }
            else if( methodName.startsWith("jsSet_")
                && methods[i].getParameterTypes().length == 1 ) {

                final String propertyName = methodName.substring(6);
                final int state = configuration.getWritablePropertyNameState(clazz, propertyName);
                if( state == JavaScriptConfiguration.ENABLED ) {
                    getPropertyInfo(localPropertyMap, propertyName).setSetter( methods[i] );
                }
                else if( state == JavaScriptConfiguration.NOT_FOUND ) {
                    throw new IllegalStateException("Setter for [" 
                            + propertyName + "] not found in configuration");
                }
            }
            else if( methodName.startsWith("jsFunction_") ) {
                final String functionName = methodName.substring("jsFunction_".length());
                final int state = configuration.getFunctionNameState(clazz, functionName);

                if( state == JavaScriptConfiguration.ENABLED ) {
                    final FunctionObject functionObject
                        = new FunctionObject(functionName, methods[i], this);
                    getPropertyInfo(localPropertyMap, functionName).setFunction( functionObject );
                }
                else if( state == JavaScriptConfiguration.NOT_FOUND ) {
                    throw new IllegalStateException("Function ["
                            + functionName + "] not found in configuration");
                }
            }
        }
        return localPropertyMap;
    }


    private PropertyInfo getPropertyInfo( final Map localPropertyMap, final String name ) {
        PropertyInfo info = (PropertyInfo)localPropertyMap.get(name);
        if( info == null ) {
            info = new PropertyInfo();
            localPropertyMap.put(name, info);
        }
        return info;
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
     * Set the page info.  This contains information specific to the rhino engine.
     * @param pageInfo The new pageInfo.
     */
    public final void setPageInfo( final JavaScriptEngine.PageInfo pageInfo ) {
        Assert.notNull("pageInfo", pageInfo);
        pageInfo_ = pageInfo;
    }


    /**
     * Return the javascript engine that is executing this code.
     * @return The engine
     */
    public JavaScriptEngine getJavaScriptEngine() {
        return getPageInfo().getJavaScriptEngine();
    }


    /**
     * Create a new javascript object
     * @param className The class name of the new object
     * @return The new object
     */
    public SimpleScriptable makeJavaScriptObject( final String className ) {
        final JavaScriptEngine.PageInfo pageInfo = getPageInfo();

        final SimpleScriptable newObject;
        try {
            newObject = (SimpleScriptable)pageInfo.getContext().newObject(
                pageInfo.getScope(), className, new Object[0]);
            initJavaScriptObject( newObject );
            return newObject;
        }
        catch( final Exception e ) {
            throw new ScriptException(e);
        }
    }


    /**
     * Initialize a new javascript object
     * @param newObject The JavaScript object to initialize.
     */
    protected void initJavaScriptObject( final SimpleScriptable newObject ) {
        final JavaScriptEngine.PageInfo pageInfo = getPageInfo();

        newObject.setPageInfo( pageInfo );
    }


    /**
     * Return the DOM node that corresponds to this javascript object or throw
     * an exception if one cannot be found.
     * @return The DOM node
     * @exception IllegalStateException If the DOM node could not be found.
     */
     public final DomNode getDomNodeOrDie() throws IllegalStateException {
         if( domNode_ == null ) {
             throw new IllegalStateException(
                "DomNode has not been set for this SimpleScriptable: "+getClass().getName());
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
      * Return the specified property or NOT_FOUND if it could not be found.
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

         final PropertyInfo info = (PropertyInfo)getPropertyMap().get(name);
         final int propertyNameState = configuration.getReadablePropertyNameState(clazz, name);
         final int functionNameState = configuration.getFunctionNameState(clazz, name);
         if( propertyNameState == JavaScriptConfiguration.ENABLED
                 && functionNameState == JavaScriptConfiguration.ENABLED ) {
             throw new IllegalStateException("Name is both a property and a function: name=["
                +name+"] class=["+clazz.getName()+"]");
         }

         final Object result;
         if( propertyNameState == JavaScriptConfiguration.ENABLED ) {
             if( info == null || info.getGetter() == null ) {
                 getLog().debug("Getter not implemented for property ["+name+"]");
                 result = NOT_FOUND;
             }
             else {
                 try {
                     result = info.getGetter().invoke( this, new Object[0] );
                 }
                 catch( final Exception e ) {
                     throw new ScriptException(e);
                 }
             }
         }
         else if( functionNameState == JavaScriptConfiguration.ENABLED ) {
             if( info == null || info.getFunction() == null ) {
                 getLog().debug("Function not implemented ["+name+"]");
                 result = NOT_FOUND;
             }
             else {
                 result = info.getFunction();
             }
         }
         else {
             result = super.get(name, start);
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
         final int propertyNameState = configuration.getWritablePropertyNameState(getClass(), name);

         if (propertyNameState == JavaScriptConfiguration.DISABLED) {
             throw Context.reportRuntimeError("Property \"" + name + "\" is not writable for " + start + ". "
                     + "Cant set it to: " + newValue);
         }
         else if( propertyNameState == JavaScriptConfiguration.ENABLED ) {
             final PropertyInfo info = (PropertyInfo) simpleScriptable.getPropertyMap().get(name);
             if( info == null || info.getSetter() == null ) {
                 throw Context.reportRuntimeError("Setter configured but not implemented for property \"" 
                         + name + "\" for " + start + ". "
                        + "Cant set it to: " + newValue);
             }
             else {
                 final Class parameterClass = info.getSetter().getParameterTypes()[0];
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
                     info.getSetter().invoke(
                             simpleScriptable.findMatchingScriptable(start, info.getSetter()), 
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
         else {
             getLog().debug("No configured setter \"" + name + "\" found for " 
                     + start + ". Setting it as pure javascript property.");
             super.put(name, start, newValue);
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


     private JavaScriptEngine.PageInfo getPageInfo() {
         if( pageInfo_ == null ) {
             throw new IllegalStateException("pageInfo_ has not been initialized!");
         }
         return pageInfo_;
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

        final String javaScriptClassName;
        javaScriptClassName = (String)getHtmlJavaScriptMapping().get(domNode.getClass());
        if( javaScriptClassName == null ) {
            // We don't have a specific subclass for this element so create something generic.
            final SimpleScriptable scriptable = makeJavaScriptObject("HTMLElement");
            scriptable.setDomNode(domNode);
            //getLog().info("No javascript class found for element <"+domNode.getTagName()+">.  Using HTMLElement");
            return scriptable;
        }
        else {
            final SimpleScriptable scriptable = makeJavaScriptObject(javaScriptClassName);
            scriptable.setDomNode(domNode);
            return scriptable;
        }
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
        final Object object = getObjectArg(index, args, defaultValue);
        if( object == null ) {
            return null;
        }
        else {
            return object.toString();
        }
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
        final Boolean defaultBoolean;
        if( defaultValue ) {
            defaultBoolean = Boolean.TRUE;
        }
        else {
            defaultBoolean = Boolean.FALSE;
        }
        return ((Boolean)getObjectArg(index, args, defaultBoolean)).booleanValue();
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
        final Object result = getObjectArg(index, args, new Integer(defaultValue));
        if( result instanceof String ) {
            return Integer.parseInt( (String)result );
        }
        else if( result instanceof Integer ) {
            return ((Integer)result).intValue();
        }
        else if( result instanceof Number ) {
            return ((Number)result).intValue();
        }
        else {
            throw new IllegalStateException("Unexpected type for result: "+result.getClass().getName());
        }
    }


    /**
     * Return the javascript default value of this object.  This is the javascript equivilent
     * of a toString() in java.
     *
     * @param hint A hint as to the format of the default value.  Ignored in this case.
     * @return The default value.
     */
    public Object getDefaultValue( final Class hint ) {
        return toString();
    }
}

