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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
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
        final Map map = JavaScriptEngine.getWebClientForCurrentThread().getActiveXObjectMap();
        if ( map == null ) {
             throw Context.reportRuntimeError( "ActiveXObject Error: the map is null." );
        }
        final Object mapValue = map.get(args[0]);
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

    /**
     * Get the name of the host object class.
     * @return the javascript class name
     */
    public String getClassName() {
        return "ActiveXObject";
    }
}
