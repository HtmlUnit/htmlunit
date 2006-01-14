/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.lang.reflect.Method;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * This is the host object that allows javascript to instantiate java objects via the ActiveXObject
 * constructor. This host object enables a person to emulate ActiveXObjects in javascript with java
 * objects. See the <code>WebClient</code> class to see how ActiveXObject string parameter specifies
 * which java class is instantiated.
 * @see com.gargoylesoftware.htmlunit.WebClient
 * @version $Revision$
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 */
public class ActiveXObject extends SimpleScriptable {
    private static final long serialVersionUID = 7327032075131452226L;

    /**
     * The default constructor.
     */
    public ActiveXObject() {
    }

    /**
     * This method searches the map specified in the <code>WebClient</code> class for the java
     * object to instantiate based on the ActiveXObject constructor String.
     * @param cx The current context
     * @param args The arguments to the ActiveXObject constructor
     * @param ctorObj The function object
     * @param inNewExpr Is new or not
     * @return the java object to allow javascript to access
     */
    public static Scriptable jsConstructor(
            final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) {
        if( args.length < 1 || args.length > 2 ) {
            throw Context.reportRuntimeError(
                    "ActiveXObject Error: constructor must have one or two String parameters." );
        }
        if( args[0] == Context.getUndefinedValue() ) {
            throw Context.reportRuntimeError( "ActiveXObject Error: constructor parameter is undefined." );
        }
        if( !(args[0] instanceof String) ) {
            throw Context.reportRuntimeError( "ActiveXObject Error: constructor parameter must be a String." );
        }
        final Map map = getWindow( ctorObj ).getWebWindow().getWebClient().getActiveXObjectMap();
        if (map == null) {
            throw Context.reportRuntimeError("ActiveXObject Error: the map is null.");
        }
        final Object mapValue = map.get(args[0]);

        // quick and dirty hack
        // the js configuration should probably be extended to allow to specify something like
        // <class name="XMLHttpRequest"
        //   classname="com.gargoylesoftware.htmlunit.javascript.host.XMLHttpRequest"
        //   activeX="Microsoft.XMLHTTP">
        // and to build the object from the config
        if ("Microsoft.XMLHTTP".equals(args[0])) {
            return buildXMLHTTPActiveX();
        }
        if( mapValue == null ) {
            throw Context.reportRuntimeError( "ActiveXObject Error: no map for " + args[0] + "." );
        }
        if( !(mapValue instanceof String) ) {
            throw Context.reportRuntimeError( "ActiveXObject Error: value for " + args[0] + " is not a String." );
        }

        final String xClassString = (String)mapValue;
        Object object = null;
        try {
            final Class xClass = Class.forName(xClassString);
            object = xClass.newInstance();
        }
        catch( final Exception e ) {
            throw Context.reportRuntimeError( "ActiveXObject Error: failed instantiating class " + xClassString +
                    " because " + e.getMessage() + "." );
        }
        return Context.toObject( object, ctorObj );
    }

    private static Scriptable buildXMLHTTPActiveX() {
        final SimpleScriptable resp = new XMLHttpRequest();

        // the properties
        addProperty(resp, "onreadystatechange", "jsxGet_onreadystatechange", "jsxSet_onreadystatechange");
        addProperty(resp, "readyState", "jsxGet_readyState", null);
        addProperty(resp, "responseText", "jsxGet_responseText", null);
        addProperty(resp, "responseXML", "jsxGet_responseXML", null);
        addProperty(resp, "status", "jsxGet_status", null);
        addProperty(resp, "statusText", "jsxGet_statusText", null);

        // the functions
        addFunction(resp, "abort", "jsxFunction_abort");
        addFunction(resp, "abort", "jsxFunction_abort");
        addFunction(resp, "getAllResponseHeaders", "jsxFunction_getAllResponseHeaders");
        addFunction(resp, "getResponseHeader", "jsxFunction_getResponseHeader");
        addFunction(resp, "open", "jsxFunction_open");
        addFunction(resp, "send", "jsxFunction_send");
        addFunction(resp, "setRequestHeader", "jsxFunction_setRequestHeader");

        return resp;
    }

    private static void addFunction(final SimpleScriptable scriptable,
            final String jsMethodName, final String javaMethodName) {
        final Method javaFunction = getMethod(scriptable.getClass(), javaMethodName);
        final FunctionObject fo = new FunctionObject(null, javaFunction, scriptable);
        scriptable.defineProperty(jsMethodName, fo, READONLY);
    }

    static void addProperty(final SimpleScriptable scriptable, final String propertyName,
            final String getterName, final String setterName) {
        scriptable.defineProperty(propertyName, null,
                getMethod(scriptable.getClass(), getterName),
                getMethod(scriptable.getClass(), setterName), PERMANENT);
    }

    /**
     * Gets the first method found of the class with the given name 
     * @param clazz the class to search on
     * @param name the name of the searched method
     * @return <code>null</code> if not found
     */
    static Method getMethod(final Class clazz, final String name) {
        if (name == null) {
            return null;
        }
        final Method[] methods = clazz.getMethods();
        for (int i=0; i<methods.length; ++i) {
            if (methods[i].getName().equals(name)) {
                return methods[i];
            }
        }
        return null;
    }

    /**
     * Get the name of the host object class.
     * @return the javascript class name
     */
    public String getClassName() {
        return "ActiveXObject";
    }
}
